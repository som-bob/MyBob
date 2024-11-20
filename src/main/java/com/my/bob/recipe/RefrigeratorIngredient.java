package com.my.bob.recipe;

import com.my.bob.common.entity.BaseTimeEntity;
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
    @Column(name = "ingredient_id", nullable = false)
    private Integer ingredientId;

    @Column(name = "date_added")
    private LocalDate dateAdded;

}