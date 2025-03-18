package com.my.bob.core.domain.recipe.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "RecentRecipe")  // ttl 설정 x
public class RecentRecipe {

    @Id
    private String id;

    private String email;       // 유저 이메일
    private String recipeName;  // 레시피 이름
    private String recipeJson;  // 레시피 상세 정보 (Json)
}
