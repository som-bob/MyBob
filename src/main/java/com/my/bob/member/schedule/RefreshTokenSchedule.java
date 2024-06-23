package com.my.bob.member.schedule;

import com.my.bob.member.service.BobUserRefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenSchedule {

    private final BobUserRefreshTokenService bobUserRefreshTokenService;

    // TODO 스케쥴러를 통한 만료 데이터 삭제
}
