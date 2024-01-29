package com.example.securitydemo.user.role;

/*
* User의 권한 정보를 저장해놓은 enum 클래스
* Authority 클래스가 따로 있기 때문에 사용하지 않음.
* TODO: 삭제 예정 */

public enum UserRoleEnum {

    USER(Authority.USER),
    ADMIN(Authority.ADMIN);

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority{
        public static final String USER = "ROLE_USER";

        public static final String ADMIN = "ROLE_ADMIN";
    }
}
