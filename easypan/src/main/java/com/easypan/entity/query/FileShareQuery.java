package com.easypan.entity.query;

import java.util.Date;


/**
 * 参数
 */
public class FileShareQuery extends BaseParam {


	/**
	 * share ID
	 */
	private String shareId;

	private String shareIdFuzzy;

	/**
	 * file ID
	 */
	private String fileId;

	private String fileIdFuzzy;

	/**
	 * User ID
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * valid_time_type: 0: 1 day 1: 7days 2: 30 days 3: forever
	 */
	private Integer validType;

	/**
	 * 
	 */
	private String expireTime;

	private String expireTimeStart;

	private String expireTimeEnd;

	/**
	 * 
	 */
	private String shareTime;

	private String shareTimeStart;

	private String shareTimeEnd;

	/**
	 * extract code
	 */
	private String code;

	private String codeFuzzy;

	/**
	 * view times
	 */
	private Integer showCount;


	private Boolean queryFileName;

	public Boolean getQueryFileName() {
		return queryFileName;
	}

	public void setQueryFileName(Boolean queryFileName) {
		this.queryFileName = queryFileName;
	}

	public void setShareId(String shareId){
		this.shareId = shareId;
	}

	public String getShareId(){
		return this.shareId;
	}

	public void setShareIdFuzzy(String shareIdFuzzy){
		this.shareIdFuzzy = shareIdFuzzy;
	}

	public String getShareIdFuzzy(){
		return this.shareIdFuzzy;
	}

	public void setFileId(String fileId){
		this.fileId = fileId;
	}

	public String getFileId(){
		return this.fileId;
	}

	public void setFileIdFuzzy(String fileIdFuzzy){
		this.fileIdFuzzy = fileIdFuzzy;
	}

	public String getFileIdFuzzy(){
		return this.fileIdFuzzy;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setUserIdFuzzy(String userIdFuzzy){
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy(){
		return this.userIdFuzzy;
	}

	public void setValidType(Integer validType){
		this.validType = validType;
	}

	public Integer getValidType(){
		return this.validType;
	}

	public void setExpireTime(String expireTime){
		this.expireTime = expireTime;
	}

	public String getExpireTime(){
		return this.expireTime;
	}

	public void setExpireTimeStart(String expireTimeStart){
		this.expireTimeStart = expireTimeStart;
	}

	public String getExpireTimeStart(){
		return this.expireTimeStart;
	}
	public void setExpireTimeEnd(String expireTimeEnd){
		this.expireTimeEnd = expireTimeEnd;
	}

	public String getExpireTimeEnd(){
		return this.expireTimeEnd;
	}

	public void setShareTime(String shareTime){
		this.shareTime = shareTime;
	}

	public String getShareTime(){
		return this.shareTime;
	}

	public void setShareTimeStart(String shareTimeStart){
		this.shareTimeStart = shareTimeStart;
	}

	public String getShareTimeStart(){
		return this.shareTimeStart;
	}
	public void setShareTimeEnd(String shareTimeEnd){
		this.shareTimeEnd = shareTimeEnd;
	}

	public String getShareTimeEnd(){
		return this.shareTimeEnd;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return this.code;
	}

	public void setCodeFuzzy(String codeFuzzy){
		this.codeFuzzy = codeFuzzy;
	}

	public String getCodeFuzzy(){
		return this.codeFuzzy;
	}

	public void setShowCount(Integer showCount){
		this.showCount = showCount;
	}

	public Integer getShowCount(){
		return this.showCount;
	}

}
