package com.orderflow.orderflow_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    private String recipeName;

    private String preparationDescription;

    @ManyToOne
    public Item item;

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    public List<RecipeSupply> recipeSupplies = new ArrayList<>();

    public OffsetDateTime includedDate = OffsetDateTime.now();
    public OffsetDateTime updatedDate;

    public int timeMinutesToPrepare;

    public Recipe(String recipeName, String preparationDescription, OffsetDateTime includedDate, int timeMinutesToPrepare) {
        this.recipeName = recipeName;
        this.preparationDescription = preparationDescription;
        this.includedDate = includedDate;
        this.timeMinutesToPrepare = timeMinutesToPrepare;
    }
}
