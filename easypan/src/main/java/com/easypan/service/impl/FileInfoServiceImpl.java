package com.easypan.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.mail.Session;

import com.alibaba.fastjson.util.FieldInfo;
import com.easypan.component.RedisComponent;
import com.easypan.entity.config.AppConfig;
import com.easypan.entity.constants.Constants;
import com.easypan.entity.dto.SessionWebUserDto;
import com.easypan.entity.dto.UploadResultDto;
import com.easypan.entity.dto.UserSpaceDto;
import com.easypan.entity.enums.*;
import com.easypan.entity.po.UserInfo;
import com.easypan.entity.query.UserInfoQuery;
import com.easypan.exception.BusinessException;
import com.easypan.mappers.UserInfoMapper;
import com.easypan.utils.DateUtil;
import com.easypan.utils.ProcessUtils;
import com.easypan.utils.ScaleFilter;
import org.apache.commons.io.FileUtils;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.easypan.entity.query.FileInfoQuery;
import com.easypan.entity.po.FileInfo;
import com.easypan.entity.vo.PaginationResultVO;
import com.easypan.entity.query.SimplePage;
import com.easypan.mappers.FileInfoMapper;
import com.easypan.service.FileInfoService;
import com.easypan.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;


/**
 * file information 业务接口实现
 */
@Service("fileInfoService")
public class FileInfoServiceImpl implements FileInfoService {
	private static final Logger logger = LoggerFactory.getLogger(FileInfoServiceImpl.class);

	@Resource
	private FileInfoMapper<FileInfo, FileInfoQuery> fileInfoMapper;
	@Resource
	private RedisComponent redisComponent;
	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;
	@Resource
	private AppConfig appConfig;
	@Resource
	@Lazy
	private FileInfoServiceImpl fileInfoService;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<FileInfo> findListByParam(FileInfoQuery param) {
		return this.fileInfoMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(FileInfoQuery param) {
		return this.fileInfoMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<FileInfo> findListByPage(FileInfoQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<FileInfo> list = this.findListByParam(param);
		PaginationResultVO<FileInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(FileInfo bean) {
		return this.fileInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<FileInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.fileInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<FileInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.fileInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(FileInfo bean, FileInfoQuery param) {
		StringTools.checkParam(param);
		return this.fileInfoMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(FileInfoQuery param) {
		StringTools.checkParam(param);
		return this.fileInfoMapper.deleteByParam(param);
	}

	/**
	 * 根据FileIdAndUserId获取对象
	 */
	@Override
	public FileInfo getFileInfoByFileIdAndUserId(String fileId, String userId) {
		return this.fileInfoMapper.selectByFileIdAndUserId(fileId, userId);
	}

	/**
	 * 根据FileIdAndUserId修改
	 */
	@Override
	public Integer updateFileInfoByFileIdAndUserId(FileInfo bean, String fileId, String userId) {
		return this.fileInfoMapper.updateByFileIdAndUserId(bean, fileId, userId);
	}

	/**
	 * 根据FileIdAndUserId删除
	 */
	@Override
	public Integer deleteFileInfoByFileIdAndUserId(String fileId, String userId) {
		return this.fileInfoMapper.deleteByFileIdAndUserId(fileId, userId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public UploadResultDto uploadFile(SessionWebUserDto webUserDto, String fileId, MultipartFile file, String fileName, String filePid, String fileMd5, Integer chunkIndex, Integer chunks) {

		UploadResultDto resultDto = new UploadResultDto();
		Boolean uploadSuccess = true;
		File tempFileFolder = null;
		try{
			if(StringTools.isEmpty(fileId)){
				fileId = StringTools.getRandomString(Constants.LENGTH_10);
			}
			resultDto.setFileId(fileId);
			Date curDate = new Date();
			UserSpaceDto spaceDto = redisComponent.getUserSpaceUse(webUserDto.getUserId());

			if(chunkIndex == 0) {
				FileInfoQuery infoQuery = new FileInfoQuery();
				infoQuery.setFileMd5(fileMd5);
				infoQuery.setSimplePage(new SimplePage(0, 1));
				infoQuery.setStatus(FileStatusEnums.USING.getStatus());
				List<FileInfo> dbFileList = this.fileInfoMapper.selectList(infoQuery);
				// instantly transfer
				if (!dbFileList.isEmpty()) {
					FileInfo dbFile = dbFileList.get(0);
					// verify file size
					if (dbFile.getFileSize() + spaceDto.getUseSpace() > spaceDto.getTotalSpace()) {
						throw new BusinessException(ResponseCodeEnum.CODE_904);
					}

					dbFile.setFileId(fileId);
					dbFile.setFilePid(filePid);
					dbFile.setUserId(webUserDto.getUserId());
					dbFile.setCreateTime(curDate);
					dbFile.setLastUpdateTime(curDate);
					dbFile.setStatus(FileStatusEnums.USING.getStatus());
					dbFile.setDelFlag(FileDelFlagEnums.USING.getFlag());
					dbFile.setFileMd5(fileMd5);
					// file rename
					fileName = autoRename(filePid, webUserDto.getUserId(), fileName);
					dbFile.setFileMd5(fileMd5);
					this.fileInfoMapper.insert(dbFile);
					resultDto.setStatus(UploadStatusEnums.UPLOAD_SECONDS.getCode());
					// update user using space
					updateUserSpace(webUserDto, dbFile.getFileSize());
					return resultDto;
				}
			}
			// verify disk storage
			Long currentTempSize = redisComponent.getFileTempSize(webUserDto.getUserId(), fileId);
			if (file.getSize() + currentTempSize + spaceDto.getUseSpace() >spaceDto.getTotalSpace()) {
				throw new BusinessException(ResponseCodeEnum.CODE_904);
			}
			// temporarily store folder
			String tempFolderName = appConfig.getProjectFolder() + Constants.FILE_FOLDER_TEMP;
			String currentUserFolderName = webUserDto.getUserId() + fileId;
			tempFileFolder = new File(tempFolderName + currentUserFolderName);
			if (!tempFileFolder.exists()) {
				tempFileFolder.mkdirs();
			}
			File newFile = new File(tempFileFolder.getPath() + "/" + chunkIndex);
			file.transferTo(newFile);
			// save temporarily size
			redisComponent.saveFileTempSize(webUserDto.getUserId(), fileId, file.getSize());
			if (chunkIndex < chunks - 1) {
				resultDto.setStatus(UploadStatusEnums.UPLOADING.getCode());
				return resultDto;
			}
			redisComponent.saveFileTempSize(webUserDto.getUserId(), fileId, file.getSize());

			// last sharding uploaded, record database, asynchronous merge sharding
			String month = DateUtil.format(new Date(), DateTimePatternEnum.YYYYMM.getPattern());
			String fileSuffix = StringTools.getFileSuffix(fileName);
			// real file name
			String realFileName = currentUserFolderName + fileSuffix;
			FileTypeEnums fileTypeEnum = FileTypeEnums.getFileTypeBySuffix(fileSuffix);
			// auto rename
			fileName = autoRename(filePid, webUserDto.getUserId(), fileName);

			FileInfo fileInfo = new FileInfo();
			fileInfo.setFileId(fileId);
			fileInfo.setUserId(webUserDto.getUserId());
			fileInfo.setFileMd5(fileMd5);
			fileInfo.setFileName(fileName);
			fileInfo.setFilePath(month + "/" + realFileName);
			fileInfo.setFilePid(filePid);
			fileInfo.setCreateTime(curDate);
			fileInfo.setLastUpdateTime(curDate);
			fileInfo.setFileCategory(fileTypeEnum.getCategory().getCategory());
			fileInfo.setFileType(fileTypeEnum.getType());
			fileInfo.setStatus(FileStatusEnums.TRANSFER.getStatus());
			fileInfo.setFolderType(FileFolderTypeEnums.FILE.getType());
			fileInfo.setDelFlag(FileDelFlagEnums.USING.getFlag());
			this.fileInfoMapper.insert(fileInfo);
			Long totalSize = redisComponent.getFileTempSize(webUserDto.getUserId(), fileId);
			updateUserSpace(webUserDto, totalSize);
			resultDto.setStatus(UploadStatusEnums.UPLOAD_FINISH.getCode());

			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					fileInfoService.transferFile(fileInfo.getFileId(), webUserDto);
				}
			});
			return resultDto;

		} catch (BusinessException e) {
			logger.error("file uploading fail", e);
			uploadSuccess = false;
			throw e;
		}
		catch (Exception e) {
			logger.error("file uploading fail", e);
			uploadSuccess = false;
		} finally {
			if (!uploadSuccess && tempFileFolder != null) {
				try {
					FileUtils.deleteDirectory(tempFileFolder);
				} catch (IOException e) {
					logger.error("delete temporary folder failed", e);
				}
			}
		}
		return resultDto;
	}

	private String autoRename(String filePid, String userId, String fileName) {
		FileInfoQuery fileInfoQuery = new FileInfoQuery();
		fileInfoQuery.setUserId(userId);
		fileInfoQuery.setFilePid(filePid);
		fileInfoQuery.setDelFlag(FileDelFlagEnums.USING.getFlag());
		fileInfoQuery.setFileName(fileName);
		Integer count = this.fileInfoMapper.selectCount(fileInfoQuery);
		if (count > 0){
			fileName = StringTools.rename(fileName);
		}
		return fileName;
	}

	private void updateUserSpace(SessionWebUserDto webUserDto, Long useSpace) {
		Integer count = userInfoMapper.updateUserSpace(webUserDto.getUserId(), useSpace, null);
		if (count == 0) {
			throw new BusinessException(ResponseCodeEnum.CODE_904);
		}
		UserSpaceDto spaceDto = redisComponent.getUserSpaceUse(webUserDto.getUserId());
		spaceDto.setUseSpace(spaceDto.getUseSpace() + useSpace);
		redisComponent.saveUserSpaceUse(webUserDto.getUserId(), spaceDto);
	}

	@Async
	public void transferFile(String fileId, SessionWebUserDto webUserDto){
		Boolean transferSuccess = true;
		String targetFilePath = null;
		String cover = null;
		FileTypeEnums fileTypeEnum = null;
		FileInfo fileInfo = this.fileInfoMapper.selectByFileIdAndUserId(fileId, webUserDto.getUserId());
		try {
			if (fileInfo == null || !FileStatusEnums.TRANSFER.getStatus().equals(fileInfo.getStatus())) {
				return;
			}
			//temporary folder
			String tempFolderName = appConfig.getProjectFolder() + Constants.FILE_FOLDER_TEMP;
			String currentUserFolderName = webUserDto.getUserId() + fileId;
			File fileFolder = new File(tempFolderName + currentUserFolderName);

			String fileSuffix = StringTools.getFileSuffix(fileInfo.getFileName());
			String month = DateUtil.format(fileInfo.getCreateTime(), DateTimePatternEnum.YYYYMM.getPattern());
			// target folder
			String targetFolderName = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE;
			File targetFolder = new File(targetFolderName + "/" + month);
			if (!targetFolder.exists()){
				targetFolder.mkdirs();
			}
			// real file name
			String realFileName = currentUserFolderName + fileSuffix;
			targetFilePath = targetFolder.getPath() + "/" + realFileName;

			// merge file
			union(fileFolder.getPath(), targetFilePath, fileInfo.getFileName(), true);
			// video file sharing
			fileTypeEnum = FileTypeEnums.getFileTypeBySuffix(fileSuffix);
			if (FileTypeEnums.VIDEO == fileTypeEnum){
				cutFile4Video(fileId, targetFilePath);
				// video generate thumbnail
				cover = month + "/" + currentUserFolderName + Constants.IMAGE_PNG_SUFFIX;
				String coverPath = targetFolderName + "/" + cover;
				ScaleFilter.createCover4Video(new File(targetFilePath), Constants.LENGTH_150, new File(coverPath));
			}else if(FileTypeEnums.IMAGE == fileTypeEnum) {
				// generate thumbnail
				cover = month + "/" + realFileName.replace(".", "_.");
				String coverPath = targetFolderName + "/" + cover;
				Boolean created = ScaleFilter.createThumbnailWidthFFmpeg(new File(targetFilePath), Constants.LENGTH_150, new File(coverPath), false);
				if (!created){
					FileUtils.copyFile(new File(targetFilePath), new File(coverPath));
				}
			}

		} catch (Exception e) {
			logger.error("file transcoding fails, fileID:{}, userId:{}", fileId, webUserDto.getUserId(), e);
			transferSuccess = false;
		} finally {
			FileInfo updateInfo = new FileInfo();
			updateInfo.setFileSize(new File(targetFilePath).length());
			updateInfo.setFileCover(cover);
			updateInfo.setStatus(transferSuccess ? FileStatusEnums.USING.getStatus() : FileStatusEnums.TRANSFER_FAIL.getStatus());
			fileInfoMapper.updateFileStatusWithOldStatus(fileId, webUserDto.getUserId(), updateInfo, FileStatusEnums.TRANSFER.getStatus());
		}
	}

	private void union(String dirPath, String toFilePath, String fileName, Boolean delSource) {
		File dir = new File(dirPath);
		if (!dir.exists()){
			throw new BusinessException("folder doesn't exist");
		}

		File[] fileList = dir.listFiles();
		File targetFile = new File(toFilePath);
		RandomAccessFile writeFile = null;
		try{
			writeFile = new RandomAccessFile(targetFile, "rw");
			byte[] b = new byte[1024 * 10];
			for (int i = 0; i < fileList.length; i++) {
				int len = -1;
				File chunkFile = new File(dirPath + "/" + i);
				RandomAccessFile readFile = null;
				try {
					readFile = new RandomAccessFile(chunkFile, "r");
					while((len = readFile.read(b)) != -1) {
						writeFile.write(b, 0, len);
					}
				}catch (Exception e) {
					logger.error("merge sharding failed", e);
					throw new BusinessException("merge sharding failed");
				} finally {
					readFile.close();
				}
			}
		} catch (Exception e) {
			logger.error("merge file: {} fail",fileName, e);
			throw new BusinessException("merge file" + fileName + "error");
		} finally {
			if (null != writeFile) {
				try {
					writeFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (delSource && dir.exists()) {
				try {
					FileUtils.deleteDirectory(dir);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void cutFile4Video(String fileId, String videoFilePath) {
		// create sameName sharding folder
		File tsFolder = new File(videoFilePath.substring(0, videoFilePath.lastIndexOf(".")));
		if (!tsFolder.exists()){
			tsFolder.mkdirs();
		}

		final String CMD_TRANSFER_2TS = "ffmpeg -y -i %s  -vcodec copy -acodec copy -vbsf h264_mp4toannexb %s";
		final String CMD_CUT_TS = "ffmpeg -i %s -c copy -map 0 -f segment -segment_list %s -segment_time 30 %s/%s_%%4d.ts";
		String tsPath = tsFolder + "/" + Constants.TS_NAME;
		// generate ts
		String cmd = String.format(CMD_TRANSFER_2TS, videoFilePath, tsPath);
		ProcessUtils.executeCommand(cmd, false);
		// generate index file.m3u8 and sharding.ts
		cmd = String.format(CMD_CUT_TS, tsPath, tsFolder.getPath()+"/"+Constants.M3U8_NAME, tsFolder.getPath(), fileId);
		ProcessUtils.executeCommand(cmd, false);
		// delete index.tx
		new File(tsPath).delete();
	}

	@Override
	public FileInfo newFolder(String filePid, String userId, String folderName){
		checkFileName(filePid, userId, folderName, FileFolderTypeEnums.FOLDER.getType());
		Date curDate = new Date();
		FileInfo fileInfo = new FileInfo();
		fileInfo.setFileId(StringTools.getRandomString(Constants.LENGTH_10));
		fileInfo.setUserId(userId);
		fileInfo.setFilePid(filePid);
		fileInfo.setFileName(folderName);
		fileInfo.setFolderType(FileFolderTypeEnums.FOLDER.getType());
		fileInfo.setCreateTime(curDate);
		fileInfo.setLastUpdateTime(curDate);
		fileInfo.setStatus(FileStatusEnums.USING.getStatus());
		fileInfo.setDelFlag(FileDelFlagEnums.USING.getFlag());
		this.fileInfoMapper.insert(fileInfo);
		return fileInfo;
	}
	
	private void checkFileName(String filePid, String userId, String fileName, Integer folderType){
		FileInfoQuery fileInfoQuery = new FileInfoQuery();
		fileInfoQuery.setFolderType(folderType);
		fileInfoQuery.setFileName(fileName);
		fileInfoQuery.setFilePid(filePid);
		fileInfoQuery.setUserId(userId);
		fileInfoQuery.setDelFlag(FileDelFlagEnums.USING.getFlag());
		Integer count = this.fileInfoMapper.selectCount(fileInfoQuery);
		if(count > 0) {
			throw new BusinessException("under this folder, there is a file with same NAME, please change your file name");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public FileInfo rename(String fileId, String userId, String fileName) {
		FileInfo fileInfo = this.fileInfoMapper.selectByFileIdAndUserId(fileId, userId);

		if (fileInfo == null){
			throw new BusinessException("file doesn't exist");
		}

		String filePid = fileInfo.getFilePid();
		checkFileName(filePid, userId, fileName, fileInfo.getFolderType());
		// get file suffix
		if (FileFolderTypeEnums.FILE.getType().equals(fileInfo.getFolderType())) {
			fileName = fileName + StringTools.getFileSuffix(fileInfo.getFileName());
		}

		Date curDate = new Date();
		FileInfo dbInfo = new FileInfo();
		dbInfo.setFileName(fileName);
		dbInfo.setLastUpdateTime(curDate);
		this.fileInfoMapper.updateByFileIdAndUserId(dbInfo, fileId, userId);

		FileInfoQuery fileInfoQuery = new FileInfoQuery();
		fileInfoQuery.setFilePid(filePid);
		fileInfoQuery.setUserId(userId);
		fileInfoQuery.setFileName(fileName);
		fileInfoQuery.setDelFlag(FileDelFlagEnums.USING.getFlag());
		Integer count = this.fileInfoMapper.selectCount(fileInfoQuery);
		if (count > 1) {
			throw new BusinessException("File Name " + fileName + " already existed");
		}
		fileInfo.setFileName(fileName);
		fileInfo.setLastUpdateTime(curDate);
		return fileInfo;
	}

	@Override
	public void changeFileFolder(String fileIds, String filePid, String userId) {
		if (fileIds.equals(filePid)){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if (!Constants.ZERO_STR.equals(filePid)){
			FileInfo fileInfo = this.getFileInfoByFileIdAndUserId(filePid, userId);
			if (fileInfo == null || !FileDelFlagEnums.USING.getFlag().equals(fileInfo.getDelFlag())){
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
		}
		String[] fileIdArray = fileIds.split(",");
		FileInfoQuery query = new FileInfoQuery();
		query.setFilePid(filePid);
		query.setUserId(userId);
		List<FileInfo> dbFileList = this.findListByParam(query);

		Map<String, FileInfo> dbFileNameMap = dbFileList.stream().collect(Collectors.toMap(FileInfo::getFileName, Function.identity(), (data1, data2) -> data2));

		// search selected file
		query = new FileInfoQuery();
		query.setUserId(userId);
		query.setFileIdArray(fileIdArray);
		List<FileInfo> selectFileList = this.findListByParam(query);

		// rename selected file
		for (FileInfo item : selectFileList) {
			FileInfo rootFileInfo = dbFileNameMap.get(item.getFileName());
			// fileName already existed, rename fileName file the original name
			FileInfo updateInfo = new FileInfo();
			if (rootFileInfo != null){
				String fileName = StringTools.rename(item.getFileName());
				updateInfo.setFileName(fileName);
			}
			updateInfo.setFilePid(filePid);
			this.fileInfoMapper.updateByFileIdAndUserId(updateInfo, item.getFileId(), userId);
		}
	}

	@Override
	public void removeFile2RecycleBatch(String userId, String fileIds) {
		String[] fileIdArray = fileIds.split(",");
		FileInfoQuery query = new FileInfoQuery();
		query.setUserId(userId);
		query.setFileIdArray(fileIdArray);
		query.setDelFlag(FileDelFlagEnums.USING.getFlag());
		List<FileInfo> fileInfoList = this.fileInfoMapper.selectList(query);
		if (fileInfoList.isEmpty()){
			return;
		}
		List<String> delFilePidList = new ArrayList<>();
		for (FileInfo fileInfo : fileInfoList){
			findAllSubFolderFileList(delFilePidList, userId, fileInfo.getFileId(), FileDelFlagEnums.USING.getFlag());
		}
		if (!delFilePidList.isEmpty()) {
			FileInfo updateInfo = new FileInfo();
			updateInfo.setDelFlag(FileDelFlagEnums.DEL.getFlag());
			this.fileInfoMapper.updateFileDelFlagBatch(updateInfo, userId, delFilePidList, null, FileDelFlagEnums.USING.getFlag());
		}
		// update selected file to recycle status
		List<String> delFileIdList = Arrays.asList(fileIdArray);
		FileInfo fileInfo = new FileInfo();
		fileInfo.setRecoveryTime(new Date());
		fileInfo.setDelFlag(FileDelFlagEnums.RECYCLE.getFlag());
		this.fileInfoMapper.updateFileDelFlagBatch(fileInfo, userId, null, delFileIdList, FileDelFlagEnums.USING.getFlag());
	}

	private void findAllSubFolderFileList(List<String> fileIdList, String userId, String fileId, Integer delFlag){
		fileIdList.add(fileId);
		FileInfoQuery query = new FileInfoQuery();
		query.setUserId(userId);
		query.setFilePid(fileId);
		query.setDelFlag(delFlag);
		query.setFolderType(FileFolderTypeEnums.FOLDER.getType());
		List<FileInfo> fileInfoList = this.fileInfoMapper.selectList(query);
		for (FileInfo fileInfo : fileInfoList) {
			findAllSubFolderFileList(fileIdList, userId, fileInfo.getFileId(), delFlag);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void recoverFileBatch(String userId, String fileIds) {
		String[] fileIdArray = fileIds.split(",");
		FileInfoQuery query = new FileInfoQuery();
		query.setUserId(userId);
		query.setFileIdArray(fileIdArray);
		query.setDelFlag(FileDelFlagEnums.RECYCLE.getFlag());
		List<FileInfo> fileInfoList = this.fileInfoMapper.selectList(query);
		List<String> delFileSubFolderFileIdList = new ArrayList<>();
		for (FileInfo fileInfo : fileInfoList) {
			if (FileFolderTypeEnums.FOLDER.getType().equals(fileInfo.getFolderType())) {
				findAllSubFolderFileList(delFileSubFolderFileIdList, userId, fileInfo.getFileId(), FileDelFlagEnums.DEL.getFlag());
			}
		}
		// search all files under root path
		query = new FileInfoQuery();
		query.setUserId(userId);
		query.setDelFlag(FileDelFlagEnums.USING.getFlag());
		query.setFilePid(Constants.ZERO_STR);
		List<FileInfo> allRootFileList = this.findListByParam(query);
		Map<String, FileInfo> rootFileMap = allRootFileList.stream().collect(Collectors.toMap(FileInfo::getFileName, Function.identity(), (data1, data2) -> data2));

		// search all files, update all delete files under this path to USING
		if (!delFileSubFolderFileIdList.isEmpty()){
			FileInfo fileInfo = new FileInfo();
			fileInfo.setDelFlag(FileDelFlagEnums.USING.getFlag());
			this.fileInfoMapper.updateFileDelFlagBatch(fileInfo, userId, delFileSubFolderFileIdList, null, FileDelFlagEnums.DEL.getFlag());
		}

		// update selected files to normal status, set file parent path to root path
		List<String> delFileIdList = Arrays.asList(fileIdArray);
		FileInfo fileInfo = new FileInfo();
		fileInfo.setDelFlag(FileDelFlagEnums.USING.getFlag());
		fileInfo.setFilePid(Constants.ZERO_STR);
		fileInfo.setLastUpdateTime(new Date());
		this.fileInfoMapper.updateFileDelFlagBatch(fileInfo, userId, null, delFileIdList, FileDelFlagEnums.RECYCLE.getFlag());

		// rename selected file
		for (FileInfo item : fileInfoList) {
			FileInfo rootFileInfo = rootFileMap.get(item.getFileName());
			// filename already existed, rename file to its original name
			if (rootFileInfo != null){
				String fileName = StringTools.rename(item.getFileName());
				FileInfo updateInfo = new FileInfo();
				updateInfo.setFileName(fileName);
				this.fileInfoMapper.updateByFileIdAndUserId(updateInfo, item.getFileId(), userId);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delFileBatch(String userId, String fileIds, Boolean adminOp) {
		String[] fileIdArray = fileIds.split(",");
		FileInfoQuery query = new FileInfoQuery();
		query.setUserId(userId);
		query.setFileIdArray(fileIdArray);
		query.setDelFlag(FileDelFlagEnums.RECYCLE.getFlag());
		List<FileInfo> fileInfoList = this.fileInfoMapper.selectList(query);

		List<String> delFileSubFileFolderFileIdList = new ArrayList<>();
		//find fileID under the child path of selected file
		for (FileInfo fileInfo : fileInfoList) {
			if (FileFolderTypeEnums.FOLDER.getType().equals(fileInfo.getFolderType())){
				findAllSubFolderFileList(delFileSubFileFolderFileIdList, userId, fileInfo.getFileId(), FileDelFlagEnums.DEL.getFlag());
			}
		}

		// delete files in selected file or folder
		if (!delFileSubFileFolderFileIdList.isEmpty()){
			this.fileInfoMapper.delFileBatch(userId, delFileSubFileFolderFileIdList, null, adminOp ? null : FileDelFlagEnums.DEL.getFlag());
		}

		// delete selected file
		this.fileInfoMapper.delFileBatch(userId, null, Arrays.asList(fileIdArray), adminOp ? null : FileDelFlagEnums.RECYCLE.getFlag());

		Long useSpace = this.fileInfoMapper.selectUseSpace(userId);
		UserInfo userInfo = new UserInfo();
		userInfo.setUseSpace(useSpace);
		this.userInfoMapper.updateByUserId(userInfo, userId);

		// set cache
		UserSpaceDto userSpaceDto = redisComponent.getUserSpaceUse(userId);
		userSpaceDto.setUseSpace(useSpace);
		redisComponent.saveUserSpaceUse(userId, userSpaceDto);
	}

	@Override
	public void checkFootFilePid(String rootFilePid, String userId, String fileId) {
		if (StringTools.isEmpty(fileId)){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if (rootFilePid.equals(fileId)){
			return;
		}
		checkFilePid(rootFilePid, fileId, userId);
	}

	private void checkFilePid(String rootFilePid, String fileId, String userId){
		FileInfo fileInfo = this.fileInfoMapper.selectByFileIdAndUserId(fileId, userId);
		if (fileInfo==null){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if (Constants.ZERO_STR.equals(fileInfo.getFilePid())) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if (fileInfo.getFilePath().equals(rootFilePid)) {
			return;
		}
		checkFilePid(rootFilePid, fileInfo.getFilePid(), userId);
	}

	@Override
	public void saveShare(String shareRootFilePid, String shareFileIds, String myFolderId, String shareUserId, String currentUserId) {
		String[] shareFileIdArray = shareFileIds.split(",");
		// target file list
		FileInfoQuery fileInfoQuery = new FileInfoQuery();
		fileInfoQuery.setUserId(currentUserId);
		fileInfoQuery.setFilePid(myFolderId);
		List<FileInfo> currentFileList = this.fileInfoMapper.selectList(fileInfoQuery);
		Map<String, FileInfo> currentFileMap = currentFileList.stream().collect(Collectors.toMap(FileInfo::getFileName, Function.identity(), (data1, data2)->data2));

		// selected file
		fileInfoQuery = new FileInfoQuery();
		fileInfoQuery.setUserId(shareUserId);
		fileInfoQuery.setFileIdArray(shareFileIdArray);
		List<FileInfo> shareFileList = this.fileInfoMapper.selectList(fileInfoQuery);
		// rename selected file
		List<FileInfo> copyFileList = new ArrayList<>();
		Date curDate = new Date();
		for(FileInfo item : shareFileList) {
			FileInfo haveFile = currentFileMap.get(item.getFileName());
			if (haveFile != null) {
				item.setFileName(StringTools.rename(item.getFileName()));
			}
			findAllSubFile(copyFileList, item, shareUserId, currentUserId, curDate, myFolderId);
		}
		this.fileInfoMapper.insertBatch(copyFileList);
	}

	private void findAllSubFile(List<FileInfo> copyFileList, FileInfo fileInfo, String sourceUserId, String currentUserId, Date curDate, String newFilePid) {
		String sourceFileId = fileInfo.getFileId();
		fileInfo.setCreateTime(curDate);
		fileInfo.setLastUpdateTime(curDate);
		fileInfo.setFilePid(newFilePid);
		fileInfo.setUserId(currentUserId);
		String newFileId = StringTools.getRandomString(Constants.LENGTH_10);
		fileInfo.setFileId(newFileId);
		copyFileList.add(fileInfo);
		if (FileFolderTypeEnums.FOLDER.getType().equals(fileInfo.getFolderType())) {
			FileInfoQuery query = new FileInfoQuery();
			query.setFilePid(sourceFileId);
			query.setUserId(sourceUserId);
			List<FileInfo> sourceFileList = this.fileInfoMapper.selectList(query);
			for (FileInfo item : sourceFileList) {
				findAllSubFile(copyFileList, item, sourceUserId, currentUserId, curDate, newFilePid);
			}
		}
	}
}


















