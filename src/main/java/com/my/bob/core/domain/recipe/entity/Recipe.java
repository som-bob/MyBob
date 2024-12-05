package com.my.bob.core.domain.recipe.entity;

import com.my.bob.core.domain.base.entity.BaseEntity;
import com.my.bob.core.domain.refrigerator.entity.RecipeIngredients;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "bob_recipe")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recipe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id", nullable = false)
    private Integer id;

    @Size(max = 200)
    @NotNull
    @Column(name = "recipe_name", nullable = false, length = 200)
    private String recipeName;

    @Size(max = 1000)
    @Column(name = "recipe_description", length = 1000)
    private String recipeDescription;

    @Size(max = 20)
    @Column(name = "difficulty", length = 20)
    private String difficulty;

    @Column(name = "cooking_time")
    private Short cookingTime;

    @Size(max = 255)
    @Column(name = "source")
    private String source;

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    @Size(max = 50)
    private String servings;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeDetail> bobRecipeDetails = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", orphanRemoval = true)
    private List<RecipeIngredients> recipeIngredients = new ArrayList<>();


}