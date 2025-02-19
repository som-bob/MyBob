package com.my.bob.core.domain.recipe.entity;

import com.my.bob.core.domain.base.entity.BaseEntity;
import com.my.bob.core.domain.file.entity.BobFile;
import com.my.bob.core.domain.recipe.contants.Difficulty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", length = 20)
    private Difficulty difficulty;

    @Column(name = "cooking_time")
    private Short cookingTime;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Size(max = 255)
    @Column(name = "source")
    private String source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private BobFile file;

    @Size(max = 50)
    private String servings;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<RecipeDetail> recipeDetails = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<RecipeIngredients> recipeIngredients = new ArrayList<>();

    @Builder
    public Recipe(String recipeName, String recipeDescription,
                  Difficulty difficulty,
                  String servings, Short cookingTime) {
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.difficulty = difficulty;
        this.servings = servings;
        this.cookingTime = cookingTime;
    }

    public void setRecipeFile(BobFile file) {
        this.file = file;
    }

    protected void addIngredient(RecipeIngredients recipeIngredients) {
        this.recipeIngredients.add(recipeIngredients);
    }

    protected void addRecipeDetail(RecipeDetail recipeDetail) {
        this.recipeDetails.add(recipeDetail);
    }

    public void delete(){
        this.isDeleted = true;
    }

    public void cleanUpIngredients(){
        this.recipeIngredients.clear();
    }
}