package com.easypan.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class FileInfoVO {

    // file id
    private String fileId;
    // parent id
    private String filePid;
    // file size
    private Long fileSize;
    // file name
    private String fileName;
    // cover
    private String fileCover;
    // last_update_time
    @JsonFormat(pattern = "yyyy_MM_dd HH:mm:ss", timezone = "GMT-4")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    // 0:file 1:category
    private Integer folderType;
    // 1:video 2:audio 3:image 4:doc 5:other
    private Integer fileCategory;
    // 1:video 2:audio 3:image 4:pdf 5:doc 6:excel 7:txt 8:code 9:zip 10:other
    private Integer fileType;
    //0: transcoding in progress 1: transcoding fail 2: transcoding success
    private Integer status;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFilePid() {
        return filePid;
    }

    public void setFilePid(String filePid) {
        this.filePid = filePid;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileCover() {
        return fileCover;
    }

    public void setFileCover(String fileCover) {
        this.fileCover = fileCover;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
