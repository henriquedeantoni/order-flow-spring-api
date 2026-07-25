package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.InventorySupply;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class InventorySupplyTest {

    @Mock
    InventorySupply inventorySupply;

    @Mock
    InventorySupplyServiceImpl inventorySupplyService;

    private InventorySupply inventorySupplyOne;
    private InventorySupply inventorySupplyTwo;

    @BeforeEach
    public void setUp() {

        inventorySupplyOne = new InventorySupply("", "", "", );
    }
}
