package com.orderflow.orderflow_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recipe_supplies")
public class RecipeSupply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeSupplyId;

    @ManyToOne
    @JoinColumn(name = "supply_id")
    private Supply supply;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private Integer quantity;
    private String unit;
}
