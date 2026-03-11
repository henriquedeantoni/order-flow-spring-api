package com.orderflow.orderflow_api.GraphicEngine.datasets;

import com.orderflow.orderflow_api.exceptions.APIException;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DatasetFactory {


    public static <T, R> void processDTOsToCategoryDataset(List<T> list, Function<T, R> extractor) {

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
    }

    private Map<String, Integer> sortMap(Map<String, Integer> map) {
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
}
