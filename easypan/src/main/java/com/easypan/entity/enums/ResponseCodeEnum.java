package com.easypan.entity.enums;


public enum ResponseCodeEnum {
    CODE_200(200, "apply success"),
    CODE_404(404, "apply address doesn't exist"),
    CODE_600(600, "apply parameters incorrect"),
    CODE_601(601, "information has already existed"),
    CODE_500(500, "Server return error, please contact administrator"),
    CODE_901(901, "Login time out, please login again"),
    CODE_902(902, "share link doesn't exist, or is no longer valid"),
    CODE_903(903, "share verification failed, please verify again"),
    CODE_904(904, "storage full! please expand");

    private Integer code;

    private String msg;

    ResponseCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
