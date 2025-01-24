package com.my.bob.core.domain.recipe.entity;

import com.my.bob.core.domain.base.entity.BaseEntity;
import com.my.bob.core.domain.file.entity.BobFile;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "bob_ingredients")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ingredient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "ingredient_name", nullable = false, length = 100)
    private String ingredientName;

    @Size(max = 1000)
    @Column(name = "ingredient_description", length = 1000)
    private String ingredientDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", insertable = false, updatable = false)
    private BobFile file;


    @Size(max = 100)
    @Column(name = "storage_method", length = 100)
    private String storageMethod;

    @Column(name = "storage_days")
    private Short storageDays;

    public Ingredient(String ingredientName) {
        this.ingredientName = ingredientName;
    }

}