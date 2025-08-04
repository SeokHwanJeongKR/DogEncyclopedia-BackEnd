package com.est.mungpe.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessage {

    //admin
    public static final String NOT_ADMIN = "관리자가 아닙니다";

    //chat
    public static final String NO_SAME_SANDER_RECEIVER = "보내는이와 받는이가 같을 수 없습니다.";

    // login
    public static final String MEMBER_BLOCKED_ERROR = "차단된 계정입니다.";

    //member
    public static final String MEMBER_NOT_FOUND = "멤버를 조회 할 수 없습니다.";


    //token
    public static final String INVALID_REFRESH_TOKEN_PROVIDED = "토큰 형식이 올바르지 않습니다";
    public static final String TOKEN_IS_EMPTY = "토큰이 비여 있습니다.";

    //refreshToken
    public static final String REFRESH_TOKEN_NOT_FOUND = "리프레쉬 토큰을 조회 할 수 없습니다.";

    //Authentication
    public static final String EXISTING_AUTHENTICATION_IS_NULL = "인가정보가 존재하지 않습니다.";

    //image
    public static final String IMAGES_FILES_LIMIT_EXCEEDED_EXCEPTION = "파일이 5개 이상입니다";
    public static final String IMAGE_CONVERT_EXCEPTION = "convert 중 예외가 발생 하였습니다";
    public static final String IMAGE_FILES_EMPTY_EXCEPTION = "이미지 파일이 들어 있지 않습니다";
    public static final String IMAGE_FILE_TOO_LARGE_EXCEPTION = "파일의 크기가 허용치보다 큽니다.";
    public static final String IMAGE_DIMENSION_EXCEEDED_EXCEPTION = "파일의 폭 또는 높이가 초과되었습니다.";
    public static final String IMAGE_DIMENSION_TOO_SMALL_EXCEPTION = "파일의 폭 또는 높이가 너무 작습니다.";
    public static final String INVALID_IMAGE_FORMAT_EXCEPTION = "지원되지 않는 형식이거나 손상된 파일일 수 있습니다";

    //post
    public static final String POST_NOT_FOUND = "포스트를 조회 할 수 없습니다.";


    public static final String PEDIA_NOT_FOUND = "피디아를 조회 할 수 없습니다.";

    //event
    public static final String EVENT_NOT_FOUND = "이벤트를 조회 할 수 없습니다.";

    //editRequest
    public static final String EDIT_REQUEST_NOT_FOUND = "수정 요청을 조회 할 수 없습니다.";


    public static final String LOGO_NOT_FOUND = "로고를 찾을 수 없습니다.";

    //elastic Searching
    public static final String ELASTIC_NOT_FOUND = "포스트를 조회 할 수 없습니다.";

    //comment
    public static final String COMMENT_NOT_FOUND = "댓글을 찾을 수 없습니다";
}
