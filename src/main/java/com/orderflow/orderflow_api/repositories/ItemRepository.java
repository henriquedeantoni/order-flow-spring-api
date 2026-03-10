package com.orderflow.orderflow_api.repositories;

import com.orderflow.orderflow_api.models.Category;
import com.orderflow.orderflow_api.models.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long>, JpaSpecificationExecutor<Item> {
    Page<Item> findByCategoryOrderByPriceAsc(Category categoryFromDb, Pageable pageDetails);

    Page<Item> findByItemNameLikeIgnoreCase(String s, Pageable pageDetails);

    Page<Item> findByIncludedDateGreaterThanEqualAndIncludedDateLessThanEqual(Instant firstDate, Instant lastDate, Pageable pageDetails);
}
