package com.my.bob.v1.redis;

import com.my.bob.core.external.redis.dto.RecentRecipeViewedEvent;
import com.my.bob.core.external.redis.service.RecentRecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecentRecipeEventListener {

    private final RecentRecipeService recentRecipeService;

    @Async
    @EventListener
    public void handleRecipeView(RecentRecipeViewedEvent event) {
        recentRecipeService.saveRecentRecipe(event.getEmail(), event.getRecipe());
    }
}
