package com.easypan.entity.enums;

public enum ShareValidTypeEnums {
    DAY_1(0, 1, "1 day"),
    DAY_9(1, 7, "7 days"),
    DAY_30(2, 30, "30 days"),
    FOREVER(3, -1, "forever");

    private Integer type;
    private Integer days;
    private String desc;

    ShareValidTypeEnums(Integer type, Integer days, String desc){
        this.type = type;
        this.days = days;
        this.desc = desc;
    }

    public static ShareValidTypeEnums getByType(Integer type) {
        for (ShareValidTypeEnums typeEnums : ShareValidTypeEnums.values()){
            if (typeEnums.getType().equals(type)){
                return typeEnums;
            }
        }
        return null;
    }

    public Integer getType() { return type ;}
    public Integer getDays() { return days; }
    public String getDesc() { return desc; }
}
