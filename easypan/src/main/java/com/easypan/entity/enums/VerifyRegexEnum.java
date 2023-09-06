package com.easypan.entity.enums;

public enum VerifyRegexEnum {
    NO("", "No verify"),
    EMAIL("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$","email"),
    PASSWORD("^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$%^&*_]{8,}$", "only number, letter, special character 8 - 18digits");

    private String regex;
    private String desc;

    VerifyRegexEnum(String regex, String desc) {
        this.regex = regex;
        this.desc = desc;
    }

    public String getRegex() {return regex;}
    public String getDesc() {return desc;}
}
