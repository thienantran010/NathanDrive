package com.easypan.controller;

import java.util.List;

import com.easypan.annotation.GlobalInterceptor;
import com.easypan.entity.enums.FileCategoryEnums;
import com.easypan.entity.enums.FileDelFlagEnums;
import com.easypan.entity.query.FileInfoQuery;
import com.easypan.entity.po.FileInfo;
import com.easypan.entity.vo.FileInfoVO;
import com.easypan.entity.vo.PaginationResultVO;
import com.easypan.entity.vo.ResponseVO;
import com.easypan.service.FileInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * file information Controller
 */
@RestController("fileInfoController")
@RequestMapping("/file")
public class FileInfoController extends ABaseController{

	@Resource
	private FileInfoService fileInfoService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	@GlobalInterceptor
	public ResponseVO loadDataList(HttpSession session, FileInfoQuery query, String category){
		FileCategoryEnums categoryEnum = FileCategoryEnums.getByCode(category);

		if (null != categoryEnum){
			query.setFileCategory(categoryEnum.getCategory());
		}
		query.setUserId(getUserInfoFromSession(session).getUserId());
		query.setOrderBy("last_update_time desc");
		query.setDelFlag(FileDelFlagEnums.USING.getFlag());
		PaginationResultVO result = fileInfoService.findListByPage(query);
		return getSuccessResponseVO(convert2PaginationVO(result, FileInfoVO.class));
	}

}