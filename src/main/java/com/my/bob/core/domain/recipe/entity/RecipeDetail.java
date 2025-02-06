package com.my.bob.core.domain.recipe.entity;

import com.my.bob.core.domain.base.entity.BaseTimeEntity;
import com.my.bob.core.domain.file.entity.BobFile;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", insertable = false, updatable = false)
    private BobFile file;

    @NotNull
    @Column(name = "recipe_order", nullable = false)
    private Integer recipeOrder;

    @Size(max = 3000)
    @Column(name = "recipe_detail_text", length = 3000)
    private String recipeDetailText;

    public RecipeDetail(Recipe recipe, Integer recipeOrder, String recipeDetailText) {
        this.recipe = recipe;
        this.recipe.addRecipeDetail(this);
        this.recipeOrder = recipeOrder;
        this.recipeDetailText = recipeDetailText;
    }

    public void setRecipeDetailFile(BobFile file) {
        this.file = file;
    }

}