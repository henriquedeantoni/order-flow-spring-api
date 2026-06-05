package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Category;
import com.orderflow.orderflow_api.models.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private final Category firstCategory =  new Category();
    private final Category secondCategory =  new Category();
    private final Category thirdCategory =  new Category();

    private final Item itemOne = new Item();
    private final Item itemTwo = new Item();
    private final Item itemThree = new Item();
    private final Item itemFour = new Item();
    private final Item itemFive = new Item();

    private final OffsetDateTime specificDateTime = OffsetDateTime.of(
            2020, 1, 1, 12, 00, 0, 0, ZoneOffset.UTC
    );

    private final OffsetDateTime specificOtherDateTime = OffsetDateTime.of(
            2020, 1, 2, 12, 00, 0, 0, ZoneOffset.UTC
    );

    @BeforeEach
    public void setUp(){

        firstCategory.setCategoryName("First Category");
        firstCategory.setIncludedDate(specificDateTime);

        secondCategory.setCategoryName("Second Category");
        secondCategory.setIncludedDate(specificDateTime);

        thirdCategory.setCategoryName("Third Category");
        thirdCategory.setIncludedDate(specificDateTime);

        itemOne.setItemName("item one");
        itemOne.setDescription("item one Description");
        itemOne.setQuantity(10);
        itemOne.setPrice(25.00);
        itemOne.setIncludedDate(specificOtherDateTime);
        itemOne.setCategory(firstCategory);

        itemTwo.setItemName("item two");
        itemTwo.setDescription("item two Description");
        itemTwo.setQuantity(20);
        itemTwo.setPrice(30.00);
        itemTwo.setIncludedDate(specificOtherDateTime);
        itemTwo.setCategory(firstCategory);

        itemThree.setItemName("item three");
        itemThree.setDescription("item three Description");
        itemThree.setQuantity(10);
        itemThree.setPrice(30.00);
        itemThree.setIncludedDate(specificOtherDateTime);
        itemThree.setCategory(secondCategory);

        itemFour.setItemName("item four");
        itemFour.setDescription("item four Description");
        itemFour.setQuantity(20);
        itemFour.setPrice(30.00);
        itemFour.setIncludedDate(specificOtherDateTime);
        itemFour.setCategory(secondCategory);

        itemFour.setItemName("item five");
        itemFour.setQuantity(15);
        itemFour.setPrice(20.00);
        itemFour.setIncludedDate(specificOtherDateTime);
        itemFour.setCategory(thirdCategory);
    }

    @DisplayName("JUnit test for Given Item Object when Save then Return Item Object")
    @Test
    void testGivenItemObject_whenSave_thenReturnItemObject(){
        // Given/Arrange

        // When/Act
        Item savedItem = itemRepository.save(itemOne);

        // Then/Assert
        assertNotNull(savedItem);
        assertTrue(savedItem.getItemId()>0);
        assertEquals(itemOne.getItemName(), savedItem.getItemName());
        assertEquals(itemOne.getItemStatus(), savedItem.getItemStatus());
        assertEquals(itemOne.getDescription(), savedItem.getDescription());
        assertEquals(itemOne.getQuantity(), savedItem.getQuantity());
        assertEquals(itemOne.getPrice(), savedItem.getPrice());
        assertEquals(itemOne.getCategory(), savedItem.getCategory());
    }
}
