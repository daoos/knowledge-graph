package com.beyond.algo.algoalgorithmsboot.controller;

import com.beyond.algo.algoalgorithmsboot.base.BaseController;
import com.beyond.algo.algoalgorithmsboot.infra.AuthCodeDomainService;
import com.beyond.algo.algoalgorithmsboot.infra.AuthCodeService;
import com.beyond.algo.common.Result;
import com.beyond.algo.common.ResultEnum;
import com.beyond.algo.exception.AlgException;
import com.beyond.algo.model.AlgAuthCode;
import com.beyond.algo.model.AlgAuthCodeDomain;
import com.beyond.algo.model.AlgUser;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author XianjieZhang E-mail:xj_zh@foxmail.com
 * @version Created in：2017/10/12 0012 下午 9:58
 */
@Slf4j
@RestController
@RequestMapping("/authCode")
public class AuthCodeController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(AuthCodeController.class);

    @Autowired
    private AuthCodeService authCodeService;
    @Autowired
    private AuthCodeDomainService authCodeDomainService;

    @RequestMapping(value = "/listAuthCode" ,method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result listAuthCode() throws AlgException {
        AlgUser algUser = getUserInfo();
        List<AlgAuthCode> result= authCodeService.listUserAuthCode(algUser.getUsrSn());
        return Result.ok(result);
    }
    @RequestMapping(value="/listAuthCodeDomain/{acdSn}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result listAuthCodeDomain(@PathVariable("acdSn") String acdSn) throws AlgException {
        List<AlgAuthCodeDomain> result = authCodeDomainService.listAcdSnUrl(acdSn);
        return Result.ok(result);
    }

    @RequestMapping(value="/generateKey",method=RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result generateKey(AlgAuthCode algAuthCode,String[] addUrl) throws AlgException {
        AlgUser algUser = getUserInfo();
        algAuthCode.setUsrSn(algUser.getUsrSn());
        authCodeService.generateKey(algAuthCode, addUrl);
        return Result.successResponse();
    }
    @RequestMapping(value = "/deleteAuthCode/{acdSn}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result deleteAuthCode(@PathVariable("acdSn") String acdSn) throws AlgException {
        logger.info("主键:{}",acdSn);
        //删除authCode表中内容
        authCodeService.deleteAuthCode(acdSn);
        return Result.successResponse();
    }
    @RequestMapping(value = "/update",method= RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result update(AlgAuthCode algAuthCode,String[] addUrl) throws AlgException {
        logger.info("名字:{}",algAuthCode.getAcdName());
        AlgUser algUser = getUserInfo();
        algAuthCode.setUsrSn(algUser.getUsrSn());
        //更新authcode表
        authCodeService.updateAuthCode(algAuthCode,addUrl);
        return Result.successResponse();
    }
}