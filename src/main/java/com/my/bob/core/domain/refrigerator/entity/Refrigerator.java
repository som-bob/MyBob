package com.my.bob.core.domain.refrigerator.entity;

import com.my.bob.core.domain.base.entity.BaseTimeEntity;
import com.my.bob.core.domain.member.entity.BobUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
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

    @OneToMany(mappedBy = "refrigerator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RefrigeratorIngredient> bobRefrigeratorIngredients;

    public Refrigerator(String nickname, BobUser user) {
        this.nickname = nickname;
        this.user = user;
        this.bobRefrigeratorIngredients = new HashSet<>();
    }
}