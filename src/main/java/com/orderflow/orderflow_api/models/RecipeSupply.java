package com.orderflow.orderflow_api.models;

import jakarta.persistence.*;

public class RecipeSupply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @ManyToOne
    @JoinColumn(name = "supply_id")
    private Supply supply;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private Integer quantity;
    private String unit;
}
