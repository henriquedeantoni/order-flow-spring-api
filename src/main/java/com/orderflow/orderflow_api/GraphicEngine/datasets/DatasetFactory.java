package com.orderflow.orderflow_api.GraphicEngine.datasets;

import com.orderflow.orderflow_api.exceptions.APIException;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.springframework.stereotype.Component;

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

    public static <T, R> Map<String, Integer> processDTOsToMap(List<T> list, Function<T, R> extractor) {

        Boolean isStringCategory = true;

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
                    isStringCategory = false;
                }
            } catch (RuntimeException ex) {
                throw new APIException("Error on build dataset: " + ex.getMessage());
            }
        }

        return sortMap(mapCategoriesDataset);
    }

    public static <T, R> Map<String, Integer> processTimeDTOsToMap(List<T> list, Function<T, R> extractor, Integer qntyCategories, String chartName) {
        Map<String, Integer> mapCategoriesDataset = new HashMap<>();

        // TODO implement

        return null;
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
