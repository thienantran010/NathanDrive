package com.easypan.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SysSettingsDto implements Serializable {
    private String registerEmailTitle = "email auth code";
    private String registerEmailContent = "hello, your auth code is: %s, 15 mins valid";
    private Integer userInitUseSpace = 5;

    public String getRegisterEmailTitle() { return registerEmailTitle; }
    public void setRegisterEmailTitle(String registerEmailTitle) {this.registerEmailTitle = registerEmailTitle;}
    public String getRegisterEmailContent() { return registerEmailContent; }
    public void setRegisterEmailContent(String registerEmailContent) { this.registerEmailContent = registerEmailContent; }
    public Integer getUserInitUseSpace() {return userInitUseSpace; }
    public void setUserInitUseSpace(Integer userInitUseSpace) { this.userInitUseSpace = userInitUseSpace; }
}
