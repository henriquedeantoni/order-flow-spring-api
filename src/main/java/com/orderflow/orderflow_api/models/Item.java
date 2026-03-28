package com.orderflow.orderflow_api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name="items")
public class Item {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="item_id")
    private Long itemId;

    @NotBlank
    @Size(max=100)
    @Column(name = "name")
    private String itemName;

    @NotBlank
    @Size(max=1024)
    @Column(name = "description")
    private String description;

    @NotNull
    private int quantity;

    @NotNull
    @Column(name = "price")
    private double price;

    @NotNull
    @Column(name = "discount")
    private double discount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String itemSize;

    private int timePrepareMinutes;

    private String itemStatus;

    @Column(name = "included_at")
    private OffsetDateTime includedDate = OffsetDateTime.now(ZoneOffset.UTC);

    @ToString.Exclude
    @OneToOne(mappedBy = "item", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private ItemImage itemImage;

    @ToString.Exclude
    @OneToOne(mappedBy = "item", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private AlbumImage albumImage;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "item", cascade = {CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    private List<CartItem> items = new ArrayList<>();

}
