package com.my.bob.recipe;

import com.my.bob.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

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

    @Size(max = 255)
    @Column(name = "source")
    private String source;

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "recipe")
    private Set<RecipeDetail> bobRecipeDetails = new LinkedHashSet<>();

    // TODO 재료 리스트를 연결해야함. 1:N:M 되어야할수도? (중간 테이블 두기)
}