package com.orderflow.orderflow_api.models;

import jakarta.persistence.*;

public class RecipeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private Integer quantity;
    private String unit;
}
