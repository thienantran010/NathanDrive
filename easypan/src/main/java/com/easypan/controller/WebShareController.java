package com.easypan.controller;

import com.easypan.annotation.GlobalInterceptor;
import com.easypan.annotation.VerifyParam;
import com.easypan.entity.constants.Constants;
import com.easypan.entity.dto.SessionShareDto;
import com.easypan.entity.dto.SessionWebUserDto;
import com.easypan.entity.dto.UploadResultDto;
import com.easypan.entity.enums.FileCategoryEnums;
import com.easypan.entity.enums.FileDelFlagEnums;
import com.easypan.entity.enums.ResponseCodeEnum;
import com.easypan.entity.po.FileInfo;
import com.easypan.entity.po.FileShare;
import com.easypan.entity.po.UserInfo;
import com.easypan.entity.query.FileInfoQuery;
import com.easypan.entity.vo.FileInfoVO;
import com.easypan.entity.vo.PaginationResultVO;
import com.easypan.entity.vo.ResponseVO;
import com.easypan.entity.vo.ShareInfoVO;
import com.easypan.exception.BusinessException;
import com.easypan.service.FileInfoService;
import com.easypan.service.FileShareService;
import com.easypan.service.UserInfoService;
import com.easypan.utils.CopyTools;
import com.easypan.utils.StringTools;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;
import java.io.File;
import java.util.Date;

@RestController("webShareController")
@RequestMapping("/showShare")
public class WebShareController extends CommonFileController{

    @Resource
    private FileShareService fileShareService;

    @Resource
    private FileInfoService fileInfoService;

    @Resource
    private UserInfoService userInfoService;

    @RequestMapping("/getShareLoginInfo")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public ResponseVO getShareLoginInfo(HttpSession session, @VerifyParam(required = true) String shareId) {
        SessionShareDto sessionShareDto = getSessionShareFromSession(session, shareId);
        if (sessionShareDto == null) {
            return getSuccessResponseVO(null);
        }
        ShareInfoVO shareInfoVO = getShareInfoCommon(shareId);
        // verify whether file is a shared file by current user
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if (userDto != null && userDto.getUserId().equals(sessionShareDto.getShareUserId())) {
            shareInfoVO.setCurrentUser(true);
        }else {
            shareInfoVO.setCurrentUser(false);
        }

        return getSuccessResponseVO(shareInfoVO);
    }

    @RequestMapping("/getShareInfo")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public ResponseVO getShareInfo(@VerifyParam(required = true) String shareId) {
        return getSuccessResponseVO(getShareInfoCommon(shareId));
    }

    private ShareInfoVO getShareInfoCommon(String shareId) {
        FileShare share = fileShareService.getFileShareByShareId(shareId);
        if (null == share || (share.getExpireTime() != null && new Date().after(share.getExpireTime()))) {
            throw new BusinessException(ResponseCodeEnum.CODE_902.getMsg());
        }
        ShareInfoVO shareInfoVO = CopyTools.copy(share, ShareInfoVO.class);
        FileInfo fileInfo = fileInfoService.getFileInfoByFileIdAndUserId(share.getFileId(), share.getUserId());
        if (fileInfo == null || !FileDelFlagEnums.USING.getFlag().equals(fileInfo.getDelFlag())) {
            throw new BusinessException(ResponseCodeEnum.CODE_902.getMsg());
        }
        shareInfoVO.setFileName(fileInfo.getFileName());
        UserInfo userInfo = userInfoService.getUserInfoByUserId(share.getUserId());
        shareInfoVO.setNickName(userInfo.getNickName());
        shareInfoVO.setAvatar(userInfo.getQqAvatar());
        shareInfoVO.setUserId(userInfo.getUserId());
        return shareInfoVO;
    }

    @RequestMapping("/checkShareCode")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public ResponseVO checkShareCode(HttpSession session,
                                     @VerifyParam(required = true) String shareId,
                                     @VerifyParam(required = true) String code) {
        SessionShareDto sessionShareDto = fileShareService.checkShareCode(shareId, code);
        session.setAttribute(Constants.SESSION_SHARE_KEY + shareId, sessionShareDto);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadFileList")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public ResponseVO loadFileList(HttpSession session,
                                   @VerifyParam(required = true) String shareId,
                                   String filePid){
        SessionShareDto shareSessionDto = checkShare(session, shareId);

        FileInfoQuery query = new FileInfoQuery();
        if (!StringTools.isEmpty(filePid) && !Constants.ZERO_STR.equals(filePid)) {
            fileInfoService.checkFootFilePid(shareSessionDto.getFileId(), shareSessionDto.getShareUserId(), filePid);
            query.setFilePid(filePid);
        } else{
            query.setFileId(shareSessionDto.getFileId());
        }
        query.setUserId(shareSessionDto.getShareUserId());
        query.setOrderBy("last_update_time desc");
        query.setDelFlag(FileDelFlagEnums.USING.getFlag());
        PaginationResultVO result = fileInfoService.findListByPage(query);
        return getSuccessResponseVO(convert2PaginationVO(result, FileInfoVO.class));
    }

    private SessionShareDto checkShare(HttpSession session, String shareId){
        SessionShareDto shareSessionDto = getSessionShareFromSession(session, shareId);
        if (null == shareSessionDto){
            throw new BusinessException(ResponseCodeEnum.CODE_903);
        }
        if (shareSessionDto.getExpireTime() != null && new Date().after(shareSessionDto.getExpireTime())) {
            throw new BusinessException(ResponseCodeEnum.CODE_902);
        }
        return shareSessionDto;
    }

    @RequestMapping("/getFolderInfo")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public ResponseVO getFolderInfo(HttpSession session,
                                    @VerifyParam(required = true) String shareId,
                                    @VerifyParam(required = true) String path){
        SessionShareDto shareDto = checkShare(session, shareId);
        return super.getFolderInfo(path, shareDto.getShareUserId());
    }

    @RequestMapping("/getFile/{shareId}/{fileId}")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public void getFile(HttpServletResponse response,
                        HttpSession session,
                        @PathVariable("shareId") String shareId,
                        @PathVariable("fileId") String fileId){
        SessionShareDto shareDto = checkShare(session, shareId);
        super.getFile(response, fileId, shareDto.getShareUserId());
    }

    @RequestMapping("/ts/getVideoInfo/{shareId}/{fileId}")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public void getImage(HttpServletResponse response,
                         HttpSession session,
                         @PathVariable("shareId") String shareId,
                         @PathVariable("fileId") String fileId){
        SessionShareDto shareDto = checkShare(session, shareId);
        super.getFile(response, fileId, shareDto.getShareUserId());
    }

    @RequestMapping("/createDownloadUrl/{shareId}/{fileId}")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public ResponseVO createDownloadUrl(HttpSession session,
                                        @PathVariable("shareId") String shareId,
                                        @PathVariable("fileId") String fileId){
        SessionShareDto shareDto = checkShare(session, shareId);
        return super.createDownloadUrl(fileId, shareDto.getShareUserId());
    }

    @RequestMapping("/download/{code}")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public void download(HttpServletRequest request, HttpServletResponse response,
                         @VerifyParam(required = true) @PathVariable("code") String code) throws Exception {
        super.download(request, response, code);
    }

    @RequestMapping("/saveShare")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public ResponseVO saveShare(HttpSession session,
                                @VerifyParam(required = true) String shareId,
                                @VerifyParam(required = true) String shareFileIds,
                                @VerifyParam(required = true) String myFolderId) {
        SessionShareDto shareSessionDto = checkShare(session, shareId);
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        if (shareSessionDto.getShareUserId().equals(webUserDto.getUserId())){
            throw new BusinessException("file shared by yourself can't be saved to your drive");
        }
        fileInfoService.saveShare(shareSessionDto.getFileId(), shareFileIds, myFolderId, shareSessionDto.getShareUserId(), webUserDto.getUserId());
        return getSuccessResponseVO(null);
    }
}
