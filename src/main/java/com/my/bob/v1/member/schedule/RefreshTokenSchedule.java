package com.my.bob.v1.member.schedule;

import com.my.bob.core.domain.member.entity.BobUserRefreshToken;
import com.my.bob.v1.member.service.BobUserRefreshTokenServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenSchedule {

    private final BobUserRefreshTokenServiceImpl bobUserRefreshTokenService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteTokensExpirationTimeHasExpired(){
        List<BobUserRefreshToken> allAlreadyExpired = bobUserRefreshTokenService.getAllAlreadyExpired();
        bobUserRefreshTokenService.deleteAll(allAlreadyExpired);
    }
}
