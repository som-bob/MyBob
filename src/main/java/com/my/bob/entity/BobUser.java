package com.my.bob.entity;

import com.my.bob.constants.Authority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "BOB_USER")
@NoArgsConstructor
@AllArgsConstructor
public class BobUser {
    // setter 사용하지 않는 방향으로 개발 진행 -> update 어떻게 대체할지 찾아볼 것

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에게 ID 생성을 맡김
    private long userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public BobUser(String email, String password) {
        this.email = email;
        this.password = password;
    }
}


