package com.easypan.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import com.easypan.component.RedisComponent;
import com.easypan.entity.config.AppConfig;
import com.easypan.entity.constants.Constants;
import com.easypan.entity.dto.SysSettingsDto;
import com.easypan.entity.po.UserInfo;
import com.easypan.entity.query.UserInfoQuery;
import com.easypan.exception.BusinessException;
import com.easypan.mappers.UserInfoMapper;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.easypan.entity.enums.PageSize;
import com.easypan.entity.query.EmailCodeQuery;
import com.easypan.entity.po.EmailCode;
import com.easypan.entity.vo.PaginationResultVO;
import com.easypan.entity.query.SimplePage;
import com.easypan.mappers.EmailCodeMapper;
import com.easypan.service.EmailCodeService;
import com.easypan.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.scanner.Constant;


/**
 * email auth code 业务接口实现
 */
@Service("emailCodeService")
public class EmailCodeServiceImpl implements EmailCodeService {

    private static final Logger logger = LoggerFactory.getLogger(EmailCodeServiceImpl.class);
    @Resource
    private EmailCodeMapper<EmailCode, EmailCodeQuery> emailCodeMapper;

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private AppConfig appConfig;

    @Resource
    private RedisComponent redisComponent;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<EmailCode> findListByParam(EmailCodeQuery param) {
        return this.emailCodeMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(EmailCodeQuery param) {
        return this.emailCodeMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<EmailCode> list = this.findListByParam(param);
        PaginationResultVO<EmailCode> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(EmailCode bean) {
        return this.emailCodeMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<EmailCode> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.emailCodeMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<EmailCode> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.emailCodeMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(EmailCode bean, EmailCodeQuery param) {
        StringTools.checkParam(param);
        return this.emailCodeMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(EmailCodeQuery param) {
        StringTools.checkParam(param);
        return this.emailCodeMapper.deleteByParam(param);
    }

    /**
     * 根据EmailAndCode获取对象
     */
    @Override
    public EmailCode getEmailCodeByEmailAndCode(String email, String code) {
        return this.emailCodeMapper.selectByEmailAndCode(email, code);
    }

    /**
     * 根据EmailAndCode修改
     */
    @Override
    public Integer updateEmailCodeByEmailAndCode(EmailCode bean, String email, String code) {
        return this.emailCodeMapper.updateByEmailAndCode(bean, email, code);
    }

    /**
     * 根据EmailAndCode删除
     */
    @Override
    public Integer deleteEmailCodeByEmailAndCode(String email, String code) {
        return this.emailCodeMapper.deleteByEmailAndCode(email, code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmailCode(String email, Integer type) {
        if (type == Constants.ZERO) {
            UserInfo userInfo = userInfoMapper.selectByEmail(email);
            if (null != userInfo) {
                throw new BusinessException("email has already existed!");
            }
        }

        String code = StringTools.getRandomNumber(Constants.LENGTH_5);
        // send auth code
        sendMailCode(email, code);
        // disable previous auth code
        emailCodeMapper.disableEmailCode(email);

        EmailCode emailCode = new EmailCode();
        emailCode.setCode(code);
        emailCode.setEmail(email);
        emailCode.setStatus(Constants.ZERO);
        emailCode.setCreateTime(new Date());
        emailCodeMapper.insert(emailCode);
    }

    public void sendMailCode(String toEmail, String code) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(appConfig.getSendUserName());
            helper.setTo(toEmail);
            SysSettingsDto sysSettingsDto = redisComponent.getSysSettingDto();
            helper.setSubject(sysSettingsDto.getRegisterEmailTitle());
            helper.setText(String.format(sysSettingsDto.getRegisterEmailContent(), code));
            helper.setSentDate(new Date());
            javaMailSender.send(message);
        } catch (Exception e) {
            logger.error("email send fail", e);
            throw new BusinessException("email send fail");
        }
    }

    @Override
    public void checkCode(String email, String code) {
        EmailCode emailCode = this.emailCodeMapper.selectByEmailAndCode(email, code);
        if (null == emailCode){
            throw new BusinessException("email auth code incorrect");
        }
        if (emailCode.getStatus() == 1 || System.currentTimeMillis() - emailCode.getCreateTime().getTime() > Constants.LENGTH_15 * 1000 * 60) {
            throw new BusinessException("email auth code is not valid any more");
        }
        emailCodeMapper.disableEmailCode(email);
    }
}