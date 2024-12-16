package com.my.bob.core.domain.refrigerator.entity;

import com.my.bob.core.domain.base.entity.BaseTimeEntity;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "bob_refrigerator_ingredient")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefrigeratorIngredient extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refrigerator_ingredient_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "refrigerator_id", nullable = false)
    private Refrigerator refrigerator;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    public RefrigeratorIngredient(Refrigerator refrigerator, Ingredient ingredient, LocalDate dateAdded) {
        this.refrigerator = refrigerator;
        this.ingredient = ingredient;
        this.dateAdded = dateAdded;

        this.refrigerator.addIngredient(this);
    }
}