package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.graphicEngine.charts.ChartEngine;
import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.mappers.ItemMapper;
import com.orderflow.orderflow_api.models.Category;
import com.orderflow.orderflow_api.models.Item;
import com.orderflow.orderflow_api.models.ItemImage;
import com.orderflow.orderflow_api.payload.ItemCategoryDTO;
import com.orderflow.orderflow_api.payload.ItemDTO;
import com.orderflow.orderflow_api.payload.ItemResponse;
import com.orderflow.orderflow_api.payload.ItemTimeDTO;
import com.orderflow.orderflow_api.repositories.CategoryRepository;
import com.orderflow.orderflow_api.repositories.ItemImageRepository;
import com.orderflow.orderflow_api.repositories.ItemRepository;
import com.orderflow.orderflow_api.security.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemImageRepository itemImageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;

    private final ItemMapper mapper;

    public ItemServiceImpl(ItemMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ItemResponse getAllItems(String keyword, String category, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder) {
        Sort sortByIdAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByIdAndOrder);

        Specification<Item> specification = Specification.allOf(List.of());

        if(keyword!=null && !keyword.isEmpty()){
            specification = specification.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.like(criteriaBuilder.lower(root.get("itemName")), "%" + keyword.toLowerCase() + "%"));
        }

        if(category!=null && !category.isEmpty()){
            specification = specification.and((root, query, criteriabuilder)
                    -> criteriabuilder.like(root.get("category").get("categoryName"), category));
        }

        Page<Item> itemsPage = itemRepository.findAll(specification, pageDetails);

        List<Item> items = itemsPage.getContent();

        List<ItemDTO> itemDTOS = items
                .stream()
                .map(item ->{
                    ItemDTO itemDTO = modelMapper.map(item, ItemDTO.class);
                    //ItemDTO.setImage();
                    return itemDTO;
                }).toList();

        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setContent(itemDTOS);
        itemResponse.setPageNumber(itemsPage.getNumber());
        itemResponse.setPageSize(itemsPage.getSize());
        itemResponse.setTotalPages(itemsPage.getTotalPages());
        itemResponse.setTotalElements(itemsPage.getTotalElements());
        itemResponse.setLastPage(itemsPage.isLast());
        itemResponse.setTimestamp(LocalDateTime.now(ZoneId.of("UTC")));
        return itemResponse;
    }

    @Override
    public ItemDTO addItem(Long categoryId, ItemDTO itemDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Item item = modelMapper.map(itemDTO, Item.class);
        item.setCategory(category);
        item.setUser(authUtil.userOnLoggedSession());

        Item savedItem = itemRepository.save(item);
        return modelMapper.map(savedItem, ItemDTO.class);
    }

    @Override
    public ItemDTO updateItem(ItemDTO itemDTO, Long itemId) {
        Item itemFromDb = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "itemId", itemId));

        itemFromDb.setItemSize(itemDTO.getItemSize());
        itemFromDb.setItemName(itemDTO.getItemName());
        itemFromDb.setItemStatus(itemDTO.getItemStatus());
        itemFromDb.setPrice(itemDTO.getPrice());
        itemFromDb.setQuantity(itemDTO.getQuantity());
        itemFromDb.setItemStatus(itemDTO.getItemStatus());
        itemFromDb.setDescription(itemDTO.getDescription());
        itemFromDb.setTimePrepareMinutes(itemDTO.getTimePrepareMinutes());
        itemFromDb.setDiscount(itemDTO.getDiscount());

        Item savedItem = itemRepository.save(itemFromDb);

        return modelMapper.map(savedItem, ItemDTO.class);
    }

    @Override
    public ItemDTO updateItemAndCategory(ItemDTO itemDTO, Long itemId, Long categoryId) {
        Item itemFromDb = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "itemId", itemId));

        Category categoryFromDb = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        itemFromDb.setItemSize(itemDTO.getItemSize());
        itemFromDb.setItemName(itemDTO.getItemName());
        itemFromDb.setItemStatus(itemDTO.getItemStatus());
        itemFromDb.setPrice(itemDTO.getPrice());
        itemFromDb.setQuantity(itemDTO.getQuantity());
        itemFromDb.setItemStatus(itemDTO.getItemStatus());
        itemFromDb.setDescription(itemDTO.getDescription());
        itemFromDb.setTimePrepareMinutes(itemDTO.getTimePrepareMinutes());
        itemFromDb.setDiscount(itemDTO.getDiscount());
        itemFromDb.setCategory(categoryFromDb);

        Item savedItem = itemRepository.save(itemFromDb);

        return modelMapper.map(savedItem, ItemDTO.class);
    }

    @Override
    public ItemDTO updateItemStatus(Long itemId, String status) {
        Item itemFromDb = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "itemId", itemId));

        itemFromDb.setItemStatus(status);
        Item savedItem = itemRepository.save(itemFromDb);

        return modelMapper.map(savedItem, ItemDTO.class);
    }

    @Override
    public ItemDTO updatedItemImage(Long itemId, Long imageId) {
        Item itemFromDb =  itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "itemId", itemId));

        ItemImage newItemImageFromDB = itemImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("ItemImage", "itemId", itemId));

        newItemImageFromDB.setItemImageId(imageId);
        itemFromDb.setItemImage(newItemImageFromDB);

        itemImageRepository.save(newItemImageFromDB);
        itemRepository.save(itemFromDb);
        return modelMapper.map(itemFromDb, ItemDTO.class);
    }

    @Override
    public ItemDTO findById(Long itemId) {
        Item itemFromDb = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "itemId", itemId));

        return modelMapper.map(itemFromDb, ItemDTO.class);
    }

    @Override
    public ItemResponse getAllItemsByCategoryId(Long categoryId, Integer pageSize, Integer pageNumber,  String sortBy, String sortOrder) {
        Category categoryFromDb = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Item> pageItems = itemRepository.findByCategoryOrderByPriceAsc(categoryFromDb, pageDetails);

        List<Item> items = pageItems.getContent();
        if(items.isEmpty()) {
            throw new APIException("Items not found with categoryId: "
                    + categoryId.toString() + " and categoryName: " + categoryFromDb.getCategoryName());
        }

        List<ItemDTO> itemDTOs = items
                .stream()
                .map(p -> modelMapper.map(p, ItemDTO.class))
                .toList();

        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setContent(itemDTOs);
        itemResponse.setTotalElements(pageItems.getTotalElements());
        itemResponse.setTotalPages(pageItems.getTotalPages());
        itemResponse.setPageNumber(pageDetails.getPageNumber());
        itemResponse.setPageSize(pageDetails.getPageSize());
        itemResponse.setLastPage(pageItems.isLast());

        return itemResponse;
    }

    @Override
    public ItemResponse getAllItemsByKeyword(String keyword, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Item> pageItems = itemRepository.findByItemNameLikeIgnoreCase('%' + keyword + '%' , pageDetails);

        List<Item> items = pageItems.getContent();
        if(items.isEmpty()) {
            throw new APIException("Items not found.");
        }

        List<ItemDTO> itemDTOs = items
                .stream()
                .map(p -> modelMapper.map(p, ItemDTO.class))
                .toList();

        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setContent(itemDTOs);
        itemResponse.setTotalElements(pageItems.getTotalElements());
        itemResponse.setTotalPages(pageItems.getTotalPages());
        itemResponse.setPageNumber(pageDetails.getPageNumber());
        itemResponse.setPageSize(pageDetails.getPageSize());
        itemResponse.setLastPage(pageItems.isLast());

        return itemResponse;
    }

    @Override
    public ItemResponse getItemsCreatedInInterval(Instant firstDate, Instant lastDate, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder) {

        if(!firstDate.isBefore(lastDate)) {
            throw  new APIException("First Date must be before Last Date");
        }
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Item> pageItems = itemRepository.findByIncludedDateGreaterThanEqualAndIncludedDateLessThanEqual(firstDate, lastDate, pageDetails);

        List<Item> itemsFromContent = pageItems.getContent();

        if(itemsFromContent.isEmpty()) {
            throw new APIException("Any Item found on interval criteria.");
        }

        List<ItemDTO> itemDTOS = itemsFromContent.stream()
                .map( item -> modelMapper.map(item, ItemDTO.class))
                .toList();

        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setContent(itemDTOS);
        itemResponse.setTotalElements(pageItems.getTotalElements());
        itemResponse.setTotalPages(pageItems.getTotalPages());
        itemResponse.setPageNumber(pageDetails.getPageNumber());
        itemResponse.setPageSize(pageDetails.getPageSize());
        itemResponse.setLastPage(pageItems.isLast());

        return itemResponse;
    }

    @Override
    public ItemDTO deleteItem(Long itemId) {
        Item itemFromDb = itemRepository.findById(itemId)
                .orElseThrow(() -> new APIException("Item not found with id: " + itemId));
        itemRepository.delete(itemFromDb);

        return modelMapper.map(itemFromDb, ItemDTO.class);
    }

    @Override
    public String createDashboardBarItemByCategories(Integer qtyLayers, String axisLabelName, String valuesLabelName, String chartTitleName) {
        List<Item> items = itemRepository.findAll();
        if(items.isEmpty()) {
            throw new APIException("Items not found.");
        }

        List<ItemCategoryDTO> itemDTOS = items.stream()
                .map(mapper::toItemCategoryDTO).toList();

        JFreeChart chart = ChartEngine.createBarChartSvg(
                itemDTOS,
                ItemCategoryDTO::getCategoryName,
                qtyLayers,
                axisLabelName,
                valuesLabelName,
                chartTitleName);

        CategoryPlot plot = chart.getCategoryPlot();

        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);

        int width = 800;
        int height = 600;

        SVGGraphics2D svg = new SVGGraphics2D(width, height);

        chart.draw(svg, new Rectangle2D.Double(0, 0, width, height));

        return svg.getSVGElement();
    }

    @Override
    public String createDashboardPieItemByCategories(Integer qtyLayers, String chartTitleName) {
        List<Item> items = itemRepository.findAll();
        if(items.isEmpty()) {
            throw new APIException("Items not found.");
        }

        List<ItemCategoryDTO> itemDTOS = items.stream()
                .map(mapper::toItemCategoryDTO).toList();

        JFreeChart chart = ChartEngine.createPieChartSvg(itemDTOS,ItemCategoryDTO::getCategoryName, qtyLayers, chartTitleName);

        PiePlot plot = (PiePlot) chart.getPlot();

        plot.setBackgroundPaint(Color.WHITE);
        plot.setCircular(true);
        plot.setOutlineVisible(false);
        plot.setIgnoreZeroValues(true);
        plot.setIgnoreNullValues(true);

        int width = 600;
        int height = 600;

        SVGGraphics2D svg = new SVGGraphics2D(width, height);

        chart.draw(svg, new Rectangle2D.Double(0, 0, width, height));

        return svg.getSVGElement();
    }

    @Override
    public String createDashboardTimeSeriesMonthlyItem(
            Instant firstDate,
            Instant lastDate,
            String chartTitleName,
            String axisTitleName,
            String valuesTitleName)
    {
        if(!firstDate.isBefore(lastDate)) {
            throw  new APIException("First Date must be before Last Date");
        }
        long months = ChronoUnit.MONTHS.between(firstDate, lastDate);
        if(months > 1) {
            throw  new APIException("The time duration must be less or equal 1 month");
        }

        List<Item> items = itemRepository.findByIncludedDateGreaterThanEqualAndIncludedDateLessThanEqual(firstDate, lastDate);

        List<ItemTimeDTO> itemTimeDTOS = items.stream()
                .map( item ->{
                    return modelMapper.map(item, ItemTimeDTO.class);
                }).toList();

        JFreeChart chart = ChartEngine.createTimeSeriesChartSvg(itemTimeDTOS, ItemTimeDTO::getIncludedDate, chartTitleName, axisTitleName, valuesTitleName, "month" );

        ChartPanel chartPanel = new ChartPanel(chart);

        SVGGraphics2D svg = new SVGGraphics2D(800, 600);
        chart.draw(svg, svg.getClipBounds());

        return svg.getSVGElement();
    }

    @Override
    public String createDashboardTimeSeriesYearItem(
            Instant firstDate,
            Instant lastDate,
            String chartTitleName,
            String axisTitleName,
            String valuesTitleName)
    {
        if(!firstDate.isBefore(lastDate)) {
            throw  new APIException("First Date must be before Last Date");
        }
        Duration duration = Duration.between(firstDate, lastDate);
        if(duration.toDays() > 365) {
            throw  new APIException("The time duration must be less or equal 1 year");
        }

        List<Item> items = itemRepository.findByIncludedDateGreaterThanEqualAndIncludedDateLessThanEqual(firstDate, lastDate);

        List<ItemTimeDTO> itemTimeDTOS = items.stream()
                .map( item ->{
                    return modelMapper.map(item, ItemTimeDTO.class);
                }).toList();

        JFreeChart chart = ChartEngine.createTimeSeriesChartSvg(itemTimeDTOS, ItemTimeDTO::getIncludedDate, chartTitleName, axisTitleName, valuesTitleName, "year" );

        ChartPanel chartPanel = new ChartPanel(chart);

        SVGGraphics2D svg = new SVGGraphics2D(800, 600);
        chart.draw(svg, svg.getClipBounds());

        return svg.getSVGElement();
    }

}
