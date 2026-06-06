package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.*;
import com.orderflow.orderflow_api.payload.CategoryDTO;
import com.orderflow.orderflow_api.services.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.Nested;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    private ModelMapper modelMapper = new ModelMapper();

    @Nested
    class simpleOperations {

        private Pageable pageable;

        private final CategoryDTO firstCategoryDTO =  new CategoryDTO();
        private final CategoryDTO secondCategoryDTO =  new CategoryDTO();

        private final Item itemOne = new Item();
        private final Item itemTwo = new Item();
        private final Item itemThree = new Item();
        private final Item itemFour = new Item();
        private final Item itemFive = new Item();

        private final User userOne = new User("UserOne", "userone@mail.com", "hashPassword1", "John", "Doe");
        private final ItemImage itemImageOne = new ItemImage("urlOne", "image one", "description image one");
        private final AlbumImage albumImageOne = new AlbumImage("Album Image Title", "Description Album Image");

        private final OffsetDateTime specificDateTime = OffsetDateTime.of(
                2020, 1, 1, 12, 00, 0, 0, ZoneOffset.UTC
        );

        private final OffsetDateTime specificOtherDateTime = OffsetDateTime.of(
                2020, 1, 2, 12, 00, 0, 0, ZoneOffset.UTC
        );

        @BeforeEach
        void setUp() {

            firstCategoryDTO.setCategoryName("First Category");

            itemOne.setItemName("item one");
            itemOne.setDescription("item one Description");
            itemOne.setQuantity(10);
            itemOne.setPrice(15.00);
            itemOne.setIncludedDate(specificOtherDateTime);
            itemOne.setUser(userOne);
            itemOne.setItemImage(itemImageOne);
            itemOne.setAlbumImage(albumImageOne);
        }


        @DisplayName("JUnit test for Given Item Object when Save then Return Item Object")
        @Test
        void testGivenItemObject_whenSave_thenReturnItemObject(){
            // Given/Arrange
            Category savedCategory = categoryRepository.save(modelMapper.map(firstCategoryDTO, Category.class));
            itemOne.setCategory(savedCategory);

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

    @Nested
    class elaborateOperations {

        private Pageable pageable;

        private final CategoryDTO firstCategoryDTO =  new CategoryDTO();
        private final CategoryDTO secondCategoryDTO =  new CategoryDTO();

        private Category firstCategory;
        private Category secondCategory;

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

            firstCategoryDTO.setCategoryName("First Category");
            secondCategoryDTO.setCategoryName("Second Category");

            firstCategory = modelMapper.map(firstCategoryDTO, Category.class);
            secondCategory = modelMapper.map(firstCategoryDTO, Category.class);

            entityManager.persist(firstCategory);

            itemOne.setItemName("item one");
            itemOne.setDescription("item one Description");
            itemOne.setQuantity(10);
            itemOne.setPrice(15.00);
            itemOne.setCategory(firstCategory);
            itemOne.setIncludedDate(specificOtherDateTime);

            itemTwo.setItemName("item two");
            itemTwo.setDescription("item two Description");
            itemTwo.setQuantity(20);
            itemTwo.setPrice(20.00);
            itemTwo.setCategory(firstCategory);
            itemTwo.setIncludedDate(specificOtherDateTime);

            itemThree.setItemName("item three");
            itemThree.setDescription("item three Description");
            itemThree.setQuantity(10);
            itemThree.setPrice(25.00);
            itemThree.setCategory(firstCategory);
            itemThree.setIncludedDate(specificOtherDateTime);

            itemFour.setItemName("item four");
            itemFour.setDescription("item four Description");
            itemFour.setQuantity(20);
            itemFour.setPrice(30.00);
            itemFour.setCategory(firstCategory);
            itemFour.setIncludedDate(specificOtherDateTime);

            itemFive.setItemName("item five");
            itemFive.setDescription("item five Description");
            itemFive.setQuantity(15);
            itemFive.setPrice(20.00);
            itemFive.setCategory(firstCategory);
            itemFive.setIncludedDate(specificOtherDateTime);

            entityManager.persist(itemOne);
            entityManager.persist(itemTwo);
            entityManager.persist(itemThree);
            entityManager.persist(itemFour);
            entityManager.persist(itemFive);

            pageable = PageRequest.of(0, 10);
        }

        @DisplayName("Junit test for")
        @Test
        void testGivenPageList_whenSave_thenReturnPageList(){
            // Given/Arange

            // When/Act
            Page<Item> itemPage = itemRepository.findByCategoryOrderByPriceAsc(firstCategory, pageable);

            // Then/Assert
            assertNotNull(itemPage);
            assertEquals(5, itemPage.getTotalElements());
        }
    }



}
