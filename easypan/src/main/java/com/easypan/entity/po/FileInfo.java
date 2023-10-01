package com.easypan.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import com.easypan.entity.enums.DateTimePatternEnum;
import com.easypan.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;


/**
 * file information
 */
public class FileInfo implements Serializable {


	/**
	 * 
	 */
	private String fileId;

	/**
	 * 
	 */
	private String userId;

	/**
	 * 
	 */
	private String fileMd5;

	/**
	 * parentID
	 */
	private String filePid;

	/**
	 * 
	 */
	private Long fileSize;

	/**
	 * 
	 */
	private String fileName;

	/**
	 * 
	 */
	private String fileCover;

	/**
	 * 
	 */
	private String filePath;

	/**
	 * 
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastUpdateTime;

	/**
	 * 
	 */
	private Integer folderType;

	/**
	 * 
	 */
	private Integer fileCategory;

	/**
	 * 1:video 2:audio 3:picture 4:pdf 5:doc 6:excel 7:txt 8:code 9:zip 10:other
	 */
	private Integer fileType;

	/**
	 * 0: transcoding in progress 1: transcoding fail 2: transcoding success
	 */
	private Integer status;

	/**
	 * time that has been moved into recycle bin
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date recoveryTime;

	/**
	 * 0: delete 1: recycle bin 2: normal
	 */
	private Integer delFlag;

	private String nickName;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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

	public void setFileMd5(String fileMd5){
		this.fileMd5 = fileMd5;
	}

	public String getFileMd5(){
		return this.fileMd5;
	}

	public void setFilePid(String filePid){
		this.filePid = filePid;
	}

	public String getFilePid(){
		return this.filePid;
	}

	public void setFileSize(Long fileSize){
		this.fileSize = fileSize;
	}

	public Long getFileSize(){
		return this.fileSize;
	}

	public void setFileName(String fileName){
		this.fileName = fileName;
	}

	public String getFileName(){
		return this.fileName;
	}

	public void setFileCover(String fileCover){
		this.fileCover = fileCover;
	}

	public String getFileCover(){
		return this.fileCover;
	}

	public void setFilePath(String filePath){
		this.filePath = filePath;
	}

	public String getFilePath(){
		return this.filePath;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime){
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getLastUpdateTime(){
		return this.lastUpdateTime;
	}

	public void setFolderType(Integer folderType){
		this.folderType = folderType;
	}

	public Integer getFolderType(){
		return this.folderType;
	}

	public void setFileCategory(Integer fileCategory){
		this.fileCategory = fileCategory;
	}

	public Integer getFileCategory(){
		return this.fileCategory;
	}

	public void setFileType(Integer fileType){
		this.fileType = fileType;
	}

	public Integer getFileType(){
		return this.fileType;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setRecoveryTime(Date recoveryTime){
		this.recoveryTime = recoveryTime;
	}

	public Date getRecoveryTime(){
		return this.recoveryTime;
	}

	public void setDelFlag(Integer delFlag){
		this.delFlag = delFlag;
	}

	public Integer getDelFlag(){
		return this.delFlag;
	}

	@Override
	public String toString (){
		return "fileId:"+(fileId == null ? "空" : fileId)+"，userId:"+(userId == null ? "空" : userId)+"，fileMd5:"+(fileMd5 == null ? "空" : fileMd5)+"，parentID:"+(filePid == null ? "空" : filePid)+"，fileSize:"+(fileSize == null ? "空" : fileSize)+"，fileName:"+(fileName == null ? "空" : fileName)+"，fileCover:"+(fileCover == null ? "空" : fileCover)+"，filePath:"+(filePath == null ? "空" : filePath)+"，createTime:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，lastUpdateTime:"+(lastUpdateTime == null ? "空" : DateUtil.format(lastUpdateTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，folderType:"+(folderType == null ? "空" : folderType)+"，fileCategory:"+(fileCategory == null ? "空" : fileCategory)+"，1:video 2:audio 3:picture 4:pdf 5:doc 6:excel 7:txt 8:code 9:zip 10:other:"+(fileType == null ? "空" : fileType)+"，0: transcoding in progress 1: transcoding fail 2: transcoding success:"+(status == null ? "空" : status)+"，time that has been moved into recycle bin:"+(recoveryTime == null ? "空" : DateUtil.format(recoveryTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，0: delete 1: recycle bin 2: normal:"+(delFlag == null ? "空" : delFlag);
	}
}
