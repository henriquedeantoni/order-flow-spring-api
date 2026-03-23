package com.orderflow.orderflow_api.graphicEngine.datasets;

import com.orderflow.orderflow_api.exceptions.APIException;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DatasetFactory {

    public static <T, R> DefaultCategoryDataset createDefaultCategoryDatasetFromMap(List<T> list, Function<T, R> extractor, Integer qntyCategories, String chartName) {
        Map<String, Integer> map = processDTOsToMap(list, extractor);

        List<Map.Entry<String, Integer>> listEntry = map.entrySet().stream().limit(qntyCategories).collect(Collectors.toList());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Integer> entry : listEntry) {
            Integer value = entry.getValue();
            String rowKey = chartName;
            String columnKey = entry.getKey();
            dataset.addValue(value, rowKey, columnKey);
        }
        return dataset;
    }

    public static <T, R> DefaultPieDataset createDefaultPieDatasetFromMap(List<T> list, Function<T, R> extractor, Integer qntyCategories, String chartName) {
        Map<String, Integer> map = processDTOsToMap(list, extractor);

        List<Map.Entry<String, Integer>> listEntry = map.entrySet().stream().limit(qntyCategories).collect(Collectors.toList());

        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map.Entry<String, Integer> entry : listEntry) {
            Integer value = entry.getValue();
            String key = entry.getKey();
            dataset.setValue(key, value);
        }
        return dataset;
    }

    public static <T, R> TimeSeriesCollection createTimeSeriesCollection(List<T> list, Function<T, R> extractor, String chartName, String period) {
        Map<OffsetDateTime, Integer> map = processTimeDTOsToMap(list, extractor, period);

        Map<Day, Integer> mapDays = new HashMap<>();

        TimeSeries series = new TimeSeries(chartName);

        for (Map.Entry<OffsetDateTime, Integer> entry : map.entrySet()) {
            OffsetDateTime offsetDateTime = entry.getKey();
            Integer day = offsetDateTime.getDayOfMonth();
            Integer month = offsetDateTime.getMonthValue();
            Integer year = offsetDateTime.getYear();
            Day newDay = new Day(day, month, year);
            if(mapDays.containsKey(newDay)) {
                mapDays.put(newDay, mapDays.get(newDay) + 1);
            } else {
                mapDays.put(newDay, 1);
            }
        }

        for (Map.Entry<Day, Integer> entry : mapDays.entrySet()) {
            series.addOrUpdate(entry.getKey(), entry.getValue());
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);

        return dataset;
    }

    public static <T, R> Map<String, Integer> processDTOsToMap(List<T> list, Function<T, R> extractor) {

        Map<String, Integer> mapCategoriesDataset = new HashMap<>();

        for (T cat : list) {
            R value = extractor.apply(cat);
            //System.out.println(value);
            try{
                if(!mapCategoriesDataset.containsKey(cat.toString()) && value instanceof String) {
                    mapCategoriesDataset.put((String)value, 1);
                } else if(mapCategoriesDataset.containsKey(cat.toString()) && value instanceof String) {
                    Integer newValue = mapCategoriesDataset.get(value) + 1;
                    mapCategoriesDataset.put((String)value, newValue);
                } else {
                    throw new APIException("error on build dataset: " + value.toString());
                }
            } catch (RuntimeException ex) {
                throw new APIException("Error on build dataset: " + ex.getMessage());
            }
        }

        return sortMap(mapCategoriesDataset);
    }

    public static <T, R> Map<OffsetDateTime, Integer> processTimeDTOsToMap(
            List<T> list,
            Function<T, R> extractor,
            String period) {

        Map<OffsetDateTime, Integer> mapDateTimeDataset = new HashMap<>();

        for (T time : list) {
            R value = extractor.apply(time);
            try{
                if(!mapDateTimeDataset.containsKey(time) && value instanceof OffsetDateTime) {
                    mapDateTimeDataset.put((OffsetDateTime)value, 1);
                } else if(mapDateTimeDataset.containsKey(time) && value instanceof OffsetDateTime) {
                    Integer newValue = mapDateTimeDataset.get(value) + 1;
                    mapDateTimeDataset.put((OffsetDateTime)value, newValue);
                } else {
                    throw new APIException("error on build dataset: " + value.toString());
                }
            }  catch (RuntimeException ex) {
                throw new APIException("Error on build dataset: " + ex.getMessage());
            }
        }

        if (period.equals("month")) {
            return compressTimesSeriesMonthPeriod(mapDateTimeDataset);
        } else if (period.equals("year")) {
            return compressTimesSeriesYearPeriod(mapDateTimeDataset);
        } else {
            throw new APIException("error on build dataset: " + period);
        }
    }

    private static Map<OffsetDateTime, Integer> compressTimesSeriesMonthPeriod(Map<OffsetDateTime, Integer> mapDateTimeDataset)
    {
        // compress to days
        Map<OffsetDateTime, Integer> mapDates = new HashMap<>();
        for (Map.Entry<OffsetDateTime, Integer> entry : mapDateTimeDataset.entrySet()) {
            OffsetDateTime newDate = entry.getKey().truncatedTo(ChronoUnit.DAYS);

            if(mapDates.containsKey(newDate)) {
                mapDates.compute(newDate, (k,v)-> v+1);
            } else {
                mapDates.put(newDate, 1);
            }
        }

        return mapDates;
    }

    private static Map<OffsetDateTime, Integer> compressTimesSeriesYearPeriod(Map<OffsetDateTime, Integer> mapDateTimeDataset)
    {
        // compress to weeks
        Map<OffsetDateTime, Integer> mapDates = new HashMap<>();
        for (Map.Entry<OffsetDateTime, Integer> entry : mapDateTimeDataset.entrySet()) {
            OffsetDateTime newDate = entry.getKey().truncatedTo(ChronoUnit.WEEKS);

            if(mapDates.containsKey(newDate)) {
                mapDates.compute(newDate, (k,v)-> v+1);
            } else {
                mapDates.put(newDate, 1);
            }
        }

        return mapDates;
    }

    private static Map<String, Integer> sortMap(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
        ));
    }

    public DefaultCategoryDataset createDataset() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(10, "Sales", "Jan");
        dataset.addValue(20, "Sales", "Feb");
        dataset.addValue(15, "Sales", "Mar");

        return dataset;
    }

    public TimeSeries createTimeSeriesDataset(){

        TimeSeries timeSeries = new TimeSeries("Sales");
        timeSeries.add(new Day(1 , 1, 2022), 35);
        timeSeries.add(new Day(1 , 1, 2023), 42);
        timeSeries.add(new Day(1 , 1, 2024), 48);
        timeSeries.add(new Day(1 , 1, 2025), 56);

        return timeSeries;
    }

}
