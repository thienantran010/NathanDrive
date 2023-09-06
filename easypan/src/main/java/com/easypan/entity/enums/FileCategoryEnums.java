package com.easypan.entity.enums;

public enum FileCategoryEnums {
    VIDEO(1, "video", "video"),
    MUSIC(2, "music", "music"),
    IMAGE(3, "image", "picture"),
    DOC(4, "doc", "doc"),
    OTHERS(5, "others", "others");

    private Integer category;
    private String code;
    private String desc;

    FileCategoryEnums(Integer category, String code, String desc) {
        this.category = category;
        this.code = code;
        this.desc = desc;
    }

    public static FileCategoryEnums getByCode(String code){
        for (FileCategoryEnums item : FileCategoryEnums.values()){
            if (item.getCode().equals(code)){
                return item;
            }
        }
        return null;
    }

    public Integer getCategory() { return category; }
    public String getCode() { return code; }
}



















