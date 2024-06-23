package com.my.bob.member.schedule;

import com.my.bob.member.entity.BobUserRefreshToken;
import com.my.bob.member.service.BobUserRefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenSchedule {

    private final BobUserRefreshTokenService bobUserRefreshTokenService;

    @Scheduled(cron = "0 0 0 0 0")
    public void deleteTokensExpirationTimeHasExpired(){
        List<BobUserRefreshToken> allAlreadyExpired = bobUserRefreshTokenService.getAllAlreadyExpired();
        bobUserRefreshTokenService.deleteAll(allAlreadyExpired);
    }
}
