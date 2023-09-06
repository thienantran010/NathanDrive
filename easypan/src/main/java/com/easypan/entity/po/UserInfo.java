package com.easypan.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import com.easypan.entity.enums.DateTimePatternEnum;
import com.easypan.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;


/**
 * User Information
 */
public class UserInfo implements Serializable {


	/**
	 * user name
	 */
	private String userId;

	/**
	 * nickname
	 */
	private String nickName;

	/**
	 * user email
	 */
	private String email;

	/**
	 * qqOpenId
	 */
	private String qqOpenId;

	/**
	 * 
	 */
	private String qqAvatar;

	/**
	 * user password
	 */
	private String password;

	/**
	 * user register time
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date joinTime;

	/**
	 * last login date
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;

	/**
	 * user status
	 */
	private Integer status;

	/**
	 * already used storage space
	 */
	private Long useSpace;

	/**
	 * total storage space
	 */
	private Long totalSpace;


	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setNickName(String nickName){
		this.nickName = nickName;
	}

	public String getNickName(){
		return this.nickName;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return this.email;
	}

	public void setQqOpenId(String qqOpenId){
		this.qqOpenId = qqOpenId;
	}

	public String getQqOpenId(){
		return this.qqOpenId;
	}

	public void setQqAvatar(String qqAvatar){
		this.qqAvatar = qqAvatar;
	}

	public String getQqAvatar(){
		return this.qqAvatar;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return this.password;
	}

	public void setJoinTime(Date joinTime){
		this.joinTime = joinTime;
	}

	public Date getJoinTime(){
		return this.joinTime;
	}

	public void setLastLoginTime(Date lastLoginTime){
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLastLoginTime(){
		return this.lastLoginTime;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setUseSpace(Long useSpace){
		this.useSpace = useSpace;
	}

	public Long getUseSpace(){
		return this.useSpace;
	}

	public void setTotalSpace(Long totalSpace){
		this.totalSpace = totalSpace;
	}

	public Long getTotalSpace(){
		return this.totalSpace;
	}

	@Override
	public String toString (){
		return "user name:"+(userId == null ? "空" : userId)+"，nickname:"+(nickName == null ? "空" : nickName)+"，user email:"+(email == null ? "空" : email)+"，qqOpenId:"+(qqOpenId == null ? "空" : qqOpenId)+"，qqAvatar:"+(qqAvatar == null ? "空" : qqAvatar)+"，user password:"+(password == null ? "空" : password)+"，user register time:"+(joinTime == null ? "空" : DateUtil.format(joinTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，last login date:"+(lastLoginTime == null ? "空" : DateUtil.format(lastLoginTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，user status:"+(status == null ? "空" : status)+"，already used storage space:"+(useSpace == null ? "空" : useSpace)+"，total storage space:"+(totalSpace == null ? "空" : totalSpace);
	}

}
