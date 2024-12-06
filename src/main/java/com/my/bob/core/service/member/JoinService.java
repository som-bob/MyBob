package com.my.bob.core.service.member;

import com.my.bob.core.domain.member.dto.JoinUserDto;
import com.my.bob.core.domain.member.exception.DuplicateUserException;

public interface JoinService {

    void joinMember(final JoinUserDto joinUserDto) throws DuplicateUserException;
}
