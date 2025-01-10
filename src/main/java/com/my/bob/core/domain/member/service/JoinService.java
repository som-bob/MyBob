package com.my.bob.core.domain.member.service;

import com.my.bob.core.domain.member.dto.request.JoinUserDto;
import com.my.bob.core.domain.member.exception.DuplicateUserException;

public interface JoinService {

    void joinMember(final JoinUserDto joinUserDto) throws DuplicateUserException;
}
