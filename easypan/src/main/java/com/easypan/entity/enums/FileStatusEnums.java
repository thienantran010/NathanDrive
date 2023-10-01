package com.easypan.entity.enums;

public enum FileStatusEnums {
    TRANSFER(0, "transcoding in progress"),
    TRANSFER_FAIL(1, "transcoding failed"),
    USING(2, "using");

    private Integer status;
    private String desc;

    FileStatusEnums(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() { return status; }
    public String getDesc() { return desc; }
}
