package com.easypan.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import com.easypan.entity.enums.DateTimePatternEnum;
import com.easypan.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;


/**
 * 
 */
public class FileShare implements Serializable {


	/**
	 * share ID
	 */
	private String shareId;

	/**
	 * file ID
	 */
	private String fileId;

	/**
	 * User ID
	 */
	private String userId;

	/**
	 * valid_time_type: 0: 1 day 1: 7days 2: 30 days 3: forever
	 */
	private Integer validType;

	/**
	 * 
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date expireTime;

	/**
	 * 
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date shareTime;

	/**
	 * extract code
	 */
	private String code;

	/**
	 * view times
	 */
	private Integer showCount;

	private String fileName;

	private Integer folderType;

	private Integer fileCategory;

	public Integer getFolderType() {
		return folderType;
	}

	public void setFolderType(Integer folderType) {
		this.folderType = folderType;
	}

	public Integer getFileCategory() {
		return fileCategory;
	}

	public void setFileCategory(Integer fileCategory) {
		this.fileCategory = fileCategory;
	}

	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public String getFileCover() {
		return fileCover;
	}

	public void setFileCover(String fileCover) {
		this.fileCover = fileCover;
	}

	private Integer fileType;

	private String fileCover;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setShareId(String shareId){
		this.shareId = shareId;
	}

	public String getShareId(){
		return this.shareId;
	}

	public void setFileId(String fileId){
		this.fileId = fileId;
	}

	public String getFileId(){
		return this.fileId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setValidType(Integer validType){
		this.validType = validType;
	}

	public Integer getValidType(){
		return this.validType;
	}

	public void setExpireTime(Date expireTime){
		this.expireTime = expireTime;
	}

	public Date getExpireTime(){
		return this.expireTime;
	}

	public void setShareTime(Date shareTime){
		this.shareTime = shareTime;
	}

	public Date getShareTime(){
		return this.shareTime;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return this.code;
	}

	public void setShowCount(Integer showCount){
		this.showCount = showCount;
	}

	public Integer getShowCount(){
		return this.showCount;
	}

	@Override
	public String toString (){
		return "share ID:"+(shareId == null ? "空" : shareId)+"，file ID:"+(fileId == null ? "空" : fileId)+"，User ID:"+(userId == null ? "空" : userId)+"，valid_time_type: 0: 1 day 1: 7days 2: 30 days 3: forever:"+(validType == null ? "空" : validType)+"，expireTime:"+(expireTime == null ? "空" : DateUtil.format(expireTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，shareTime:"+(shareTime == null ? "空" : DateUtil.format(shareTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，extract code:"+(code == null ? "空" : code)+"，view times:"+(showCount == null ? "空" : showCount);
	}
}
