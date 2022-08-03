package com.team.unanimous.exceptionHandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400 Bad Request
    EMPTY_CONTENT(HttpStatus.BAD_REQUEST,"필수입력값이 없습니다."),
    EMPTY_PASSWORD(HttpStatus.BAD_REQUEST,"비밀번호를 입력해주세요."),

    EMPTY_NICKNAME(HttpStatus.BAD_REQUEST,"닉네임을 입력해주세요."),
    EMPTY_USERNAME(HttpStatus.BAD_REQUEST,"이메일을 입력해주세요."),
    EMPTY_ISSUE(HttpStatus.BAD_REQUEST,"안건을 입력해주세요."),
    EMPTY_CODE(HttpStatus.BAD_REQUEST,"인증코드를 입력해주세요."),
    CODE_WRONG(HttpStatus.BAD_REQUEST,"인증코드는 4자리 입니다."),
    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "만료되었거나 유효하지 않은 토큰입니다."),
    INVALID_LOGIN_ATTEMPT(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다(소셜로그인 에러)"),

    /* 403 FORBIDDEN : 권한이 없는 사용자 */
    INVALID_AUTHORITY(HttpStatus.FORBIDDEN,"권한이 없는 사용자 입니다"),
    INVALID_MEETING_ROOM(HttpStatus.FORBIDDEN,"회의에 참석할 수 없는 사용자 입니다"),
    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    ISSUE_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 안건을 찾을 수 없습니다"),
    MEETING_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 미팅을 찾을 수 없습니다"),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND,"팀을 찾을 수 없습니다"),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다"),
    USER_PASSWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "유저의 아이디,비밀번호를 다시 확인해주세요."),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND,"방이 존재하지 않습니다"),
    AUTH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다"),
    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    EXCESS_TEAM_NUMBER(HttpStatus.CONFLICT,"가입가능한 팀 갯수를 초과했습니다 (최대 5개)"),
    TEAM_MANAGER_CONFLICT(HttpStatus.CONFLICT,"팀장은 팀 탈퇴 및 자신을 강퇴시킬 수 없습니다"),
    MEETING_MEMBER_HAS_FULL(HttpStatus.CONFLICT,"회의에 참여할 수 있는 최대 인원을 초과했습니다 (최대 8명)"),
    DUPLICATE_TEAM_USER(HttpStatus.CONFLICT,"팀에 중복된 유저가 존재합니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "중복된 닉네임명이 존재합니다"),
    DUPLICATE_TEAM_NAME(HttpStatus.CONFLICT,"중복된 팀 이름이 존재합니다"),
    NICKNAME_LEGNTH(HttpStatus.CONFLICT, "닉네임은 최소 2자 이상 10자 이하여야 합니다"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일이 존재합니다"),
    USERNAME_WRONG(HttpStatus.CONFLICT, "아이디는 이메일 형식으로 입력해주세요"),
    PASSWORD_LEGNTH(HttpStatus.CONFLICT, "비밀번호는 6자 이상 12자 이하여야 합니다"),
    PASSWORD_WRONG(HttpStatus.CONFLICT, "비밀번호는 영문, 숫자, 특수문자를 포함해야합니다"),
    PASSWORD_CHECK(HttpStatus.CONFLICT, "비밀번호가 일치하지 않습니다."),
    TEAM_NAME_LENGTH(HttpStatus.CONFLICT,"팀명은 20자 이하여야 합니다"),
    MEETING_NAME_LENGTH(HttpStatus.CONFLICT,"회의 이름은 20자 이하여야 합니다"),
    ISSUE_LENGTH(HttpStatus.CONFLICT,"안건 내용은 최대 40자입니다"),
    MEETING_HAS_DONE(HttpStatus.CONFLICT,"미팅이 이미 진행중이거나 완료되었습니다"),

    // LOGIN
    INVALID_LENGTH_TOKEN(HttpStatus.BAD_REQUEST,"유효하지 않은 토큰입니다. 토큰의 길이를 확인해 주세요."),
    DECODING_FAILED_TOKEN(HttpStatus.BAD_REQUEST,"유효하지 않은 토큰입니다. 토큰 디코딩에 실패했습니다."),
    EXPIRATION_TOKEN(HttpStatus.UNAUTHORIZED,"Unauthorized"),
    INVAILD_CONTENTS_TOKEN(HttpStatus.BAD_REQUEST,"유효하지 않은 형식의 토큰입니다.");







    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}