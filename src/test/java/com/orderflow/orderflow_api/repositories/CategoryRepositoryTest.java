package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Category;
import com.orderflow.orderflow_api.models.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    private final Category firstCategory =  new Category();
    private final Category secondCategory =  new Category();
    private final Category thirdCategory =  new Category();

    private final Item itemOne = new Item();
    private final Item itemTwo = new Item();
    private final Item itemThree = new Item();
    private final Item itemFour = new Item();
    private final Item itemFive = new Item();

    @BeforeEach
    public void setUp(){
        firstCategory.setCategoryName("First Category");
        firstCategory.setIncludedDate(OffsetDateTime.now(ZoneOffset.UTC));

        secondCategory.setCategoryName("Second Category");
        secondCategory.setIncludedDate(OffsetDateTime.now(ZoneOffset.UTC));

        thirdCategory.setCategoryName("Third Category");
        thirdCategory.setIncludedDate(OffsetDateTime.now(ZoneOffset.UTC));

        itemOne.setItemName("item one");
        itemOne.setQuantity(10);
        itemOne.setPrice(25.00);
        itemOne.setCategory(firstCategory);

        itemTwo.setItemName("item two");
        itemTwo.setQuantity(20);
        itemTwo.setPrice(30.00);
        itemTwo.setCategory(firstCategory);

        itemThree.setItemName("item three");
        itemThree.setQuantity(10);
        itemThree.setPrice(30.00);
        itemThree.setCategory(secondCategory);

        itemFour.setItemName("item four");
        itemFour.setQuantity(20);
        itemFour.setPrice(30.00);
        itemFour.setCategory(secondCategory);

        itemFour.setItemName("item five");
        itemFour.setQuantity(15);
        itemFour.setPrice(20.00);
        itemFour.setCategory(thirdCategory);
    }

    @DisplayName("JUnit test Given Category Object when Save then Return Saved Category")
    @Test
    public void testGivenCategoryObject_whenSave_thenReturnSavedCategory(){
        // Given/Arrange

        // When/Act
        Category savedCategory = categoryRepository.save(firstCategory);

        // Then/Assert
        assertNotNull(savedCategory);
        assertTrue(savedCategory.getCategoryId()>0);
        assertEquals("First Category", savedCategory.getCategoryName());
    }
}
