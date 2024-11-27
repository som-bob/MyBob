package com.my.bob.v1.recipe.entity;

import com.my.bob.v1.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "bob_recipe_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_detail_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    @NotNull
    @Column(name = "recipe_order", nullable = false)
    private Integer recipeOrder;

    @Size(max = 3000)
    @Column(name = "recipe_detail_text", length = 3000)
    private String recipeDetailText;

    @OneToMany(mappedBy = "recipeDetail", orphanRemoval = true)
    private List<RecipeDetailIngredient> recipeDetailIngredients = new ArrayList<>();

}