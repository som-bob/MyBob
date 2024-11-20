package com.my.bob.recipe;

import com.my.bob.common.entity.BaseTimeEntity;
import com.my.bob.member.entity.BobUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "bob_refrigerator")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Refrigerator extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refrigerator_id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "nickname", length = 100)
    private String nickname;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private BobUser user;

    @OneToMany(mappedBy = "refrigerator")
    private Set<RefrigeratorIngredient> bobRefrigeratorIngredients = new LinkedHashSet<>();
}