package com.easypan.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.Session;

import com.easypan.entity.constants.Constants;
import com.easypan.entity.dto.SessionShareDto;
import com.easypan.entity.enums.ResponseCodeEnum;
import com.easypan.entity.enums.ShareValidTypeEnums;
import com.easypan.exception.BusinessException;
import com.easypan.utils.DateUtil;
import org.springframework.stereotype.Service;

import com.easypan.entity.enums.PageSize;
import com.easypan.entity.query.FileShareQuery;
import com.easypan.entity.po.FileShare;
import com.easypan.entity.vo.PaginationResultVO;
import com.easypan.entity.query.SimplePage;
import com.easypan.mappers.FileShareMapper;
import com.easypan.service.FileShareService;
import com.easypan.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;


/**
 *  业务接口实现
 */
@Service("fileShareService")
public class FileShareServiceImpl implements FileShareService {

	@Resource
	private FileShareMapper<FileShare, FileShareQuery> fileShareMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<FileShare> findListByParam(FileShareQuery param) {
		return this.fileShareMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(FileShareQuery param) {
		return this.fileShareMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<FileShare> findListByPage(FileShareQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<FileShare> list = this.findListByParam(param);
		PaginationResultVO<FileShare> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(FileShare bean) {
		return this.fileShareMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<FileShare> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.fileShareMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<FileShare> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.fileShareMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(FileShare bean, FileShareQuery param) {
		StringTools.checkParam(param);
		return this.fileShareMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(FileShareQuery param) {
		StringTools.checkParam(param);
		return this.fileShareMapper.deleteByParam(param);
	}

	/**
	 * 根据ShareId获取对象
	 */
	@Override
	public FileShare getFileShareByShareId(String shareId) {
		return this.fileShareMapper.selectByShareId(shareId);
	}

	/**
	 * 根据ShareId修改
	 */
	@Override
	public Integer updateFileShareByShareId(FileShare bean, String shareId) {
		return this.fileShareMapper.updateByShareId(bean, shareId);
	}

	/**
	 * 根据ShareId删除
	 */
	@Override
	public Integer deleteFileShareByShareId(String shareId) {
		return this.fileShareMapper.deleteByShareId(shareId);
	}

	@Override
	public void saveShare(FileShare share) {
		ShareValidTypeEnums typeEnums = ShareValidTypeEnums.getByType(share.getValidType());
		if (null == typeEnums){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if (ShareValidTypeEnums.FOREVER != typeEnums){
			share.setExpireTime(DateUtil.getAfterDate(typeEnums.getDays()));
		}
		Date curDate = new Date();
		share.setShareTime(curDate);
		if (StringTools.isEmpty(share.getCode())){
			share.setCode(StringTools.getRandomString(Constants.LENGTH_5));
		}
		share.setShareId(StringTools.getRandomString(Constants.LENGTH_20));
		share.setShowCount(0);
		this.fileShareMapper.insert(share);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteFileShareBatch(String[] shareIdArray, String userId) {
		Integer count = this.fileShareMapper.deleteFileShareBatch(shareIdArray, userId);
		if (count != shareIdArray.length){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
	}

	@Override
	public SessionShareDto checkShareCode(String shareId, String code) {
		FileShare share = this.fileShareMapper.selectByShareId(shareId);
		if (null == share || (share.getExpireTime() != null && new Date().after(share.getExpireTime()))) {
			throw new BusinessException(ResponseCodeEnum.CODE_902.getMsg());
		}

		if (!share.getCode().equals(code)){
			throw new BusinessException("extract code incorrect");
		}
		// update view times
		this.fileShareMapper.updateShareShowCount(shareId);
		SessionShareDto shareSessionDto = new SessionShareDto();
		shareSessionDto.setShareId(shareId);
		shareSessionDto.setShareUserId(share.getUserId());
		shareSessionDto.setFileId(share.getFileId());
		shareSessionDto.setExpireTime(share.getExpireTime());
		return shareSessionDto;
	}
}