package com.my.bob.member.entity;

import com.my.bob.common.entity.BaseTimeEntity;
import com.my.bob.constants.Authority;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.my.bob.util.RandomUtil.createRandomAlphabet;
import static com.my.bob.util.RandomUtil.createRandomNumeric;

@Entity
@Getter
@Table(name = "BOB_USER")
@NoArgsConstructor
public class BobUser extends BaseTimeEntity {
    // setter 사용하지 않는 방향으로 개발 진행 -> update 어떻게 대체할지 찾아볼 것

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


