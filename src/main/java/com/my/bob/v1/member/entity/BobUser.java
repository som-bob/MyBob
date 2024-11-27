package com.my.bob.v1.member.entity;

import com.my.bob.v1.common.entity.BaseTimeEntity;
import com.my.bob.v1.member.constants.Authority;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.my.bob.v1.util.RandomUtil.createRandomAlphabet;
import static com.my.bob.v1.util.RandomUtil.createRandomNumeric;

@Entity
@Getter
@Table(name = "BOB_USER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // protected 생성자 허용
public class BobUser extends BaseTimeEntity {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에게 ID 생성을 맡김
    private int userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String email;

    private String authority;

    private LocalDateTime lastLoginDate;

    @Builder
    public BobUser(String email, String password, String nickName) {
        // 일반 유저 회원가입
        this.email = email;
        this.password = password;
        if(StringUtils.isBlank(nickName)) {
            this.nickName = createRandomAlphabet(5) + createRandomNumeric(3) + createRandomAlphabet(2);
        } else {
            this.nickName = nickName;
        }
        this.authority = Authority.ROLE_USER.name();
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateLastLoginDate() {
        this.lastLoginDate = LocalDateTime.now();
    }
}


