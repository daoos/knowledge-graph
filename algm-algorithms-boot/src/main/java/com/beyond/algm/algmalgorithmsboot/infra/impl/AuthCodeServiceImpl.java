package com.beyond.algm.algmalgorithmsboot.infra.impl;

import com.beyond.algm.common.UUIDUtil;
import com.beyond.algm.algmalgorithmsboot.infra.AuthCodeService;
import com.beyond.algm.exception.AlgException;
import com.beyond.algm.mapper.AlgAuthCodeDomainMapper;
import com.beyond.algm.mapper.AlgAuthCodeMapper;
import com.beyond.algm.model.AlgAuthCode;
import com.beyond.algm.model.AlgAuthCodeDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author XianjieZhang E-mail:xj_zh@foxmail.com
 * @version Created in：2017/10/12 0012 下午 9:57
 */
@Service
public class AuthCodeServiceImpl implements AuthCodeService {

    @Autowired
    private AlgAuthCodeMapper algAuthCodeMapper;
    @Autowired
    private AlgAuthCodeDomainMapper algAuthCodeDomainMapper;

    @Override
    @Transactional(rollbackFor = AlgException.class)
    public void generateKey(AlgAuthCode algAuthCode,String[] addUrl) {
        //主键插入
        String acdSn = UUIDUtil.createUUID();
        algAuthCode.setAcdSn(acdSn);
        //授权码生成，后期看看用别的方法生成串号
        String acdId = "Beyond" + UUIDUtil.getRandomString(15) + "1";
        algAuthCode.setAcdId(acdId);
        algAuthCode.setCreateDate(new Date());
        //将对象AuthCode插入数据库
        algAuthCodeMapper.insert(algAuthCode);

        //插入默认的Url “algo://*”
        AlgAuthCodeDomain algAuthCodeDomainDefault  = new AlgAuthCodeDomain();
        String addSnDefault = UUIDUtil.createUUID();
        algAuthCodeDomainDefault.setAddSn(addSnDefault);
        algAuthCodeDomainDefault.setAcdSn(algAuthCode.getAcdSn());
        algAuthCodeDomainDefault.setAddUrl("algo://*");
        //Result resultDomainDefault = authCodeDomainService.createAuthCodeDomain(algAuthCodeDomainDefault);
        algAuthCodeDomainMapper.insert(algAuthCodeDomainDefault);

        //插入全部允许调用算法Url的路径
        if(addUrl != null){
            for (String anAddUrl : addUrl) {
                AlgAuthCodeDomain algAuthCodeDomain = new AlgAuthCodeDomain();
                String addSn = UUIDUtil.createUUID();
                algAuthCodeDomain.setAddSn(addSn);
                algAuthCodeDomain.setAcdSn(algAuthCode.getAcdSn());
                algAuthCodeDomain.setAddUrl(anAddUrl);
                //Result resultDomain = authCodeDomainService.createAuthCodeDomain(algAuthCodeDomain);
                algAuthCodeDomainMapper.insert(algAuthCodeDomain);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = AlgException.class)
    public void deleteAuthCode(String acdSn) {
        algAuthCodeMapper.deleteByPrimaryKey(acdSn);
        //List<AlgAuthCodeDomain> resultAuthDomain = authCodeDomainService.listAcdSnUrl(acdSn);
        List<AlgAuthCodeDomain> resultAuthDomain = algAuthCodeDomainMapper.listAcdSnUrl(acdSn);
        if (resultAuthDomain != null)
        {   //删除authCodeDomain表中内容
            //Result result = authCodeDomainService.deleteByAcdSn(acdSn);
            algAuthCodeDomainMapper.deleteByAcdSn(acdSn);
        }
    }

    @Override
    @Transactional(rollbackFor = AlgException.class)
    public void updateAuthCode(AlgAuthCode algAuthCode,String[] addUrl) {
        //首先更新AuthCode表
        algAuthCodeMapper.updateByPrimaryKey(algAuthCode);

        //更新authcodeDomain表之前先将表中这个key下Url全部删除
        //authCodeDomainService.deleteByAcdSn(algAuthCode.getAcdSn());
        algAuthCodeDomainMapper.deleteByAcdSn(algAuthCode.getAcdSn());
        //重新插入前端传过来的Url
        //插入默认的Url “algo://*”
        AlgAuthCodeDomain algAuthCodeDomainDefault  = new AlgAuthCodeDomain();
        String addSnDefault = UUIDUtil.createUUID();
        algAuthCodeDomainDefault.setAddSn(addSnDefault);
        algAuthCodeDomainDefault.setAcdSn(algAuthCode.getAcdSn());
        algAuthCodeDomainDefault.setAddUrl("algo://*");
        //Result resultDomainDefault = authCodeDomainService.createAuthCodeDomain(algAuthCodeDomainDefault);
        algAuthCodeDomainMapper.insert(algAuthCodeDomainDefault);
        /*if(resultDomainDefault.getMsg() != "成功"){
            return Result.failureResponse();
        }*/
        //插入传入自定义的Url
        if(addUrl != null){
            for (String anAddUrl : addUrl) {
                AlgAuthCodeDomain algAuthCodeDomain = new AlgAuthCodeDomain();
                String addSn = UUIDUtil.createUUID();
                algAuthCodeDomain.setAddSn(addSn);
                algAuthCodeDomain.setAcdSn(algAuthCode.getAcdSn());
                algAuthCodeDomain.setAddUrl(anAddUrl);
                //Result resultDomain = authCodeDomainService.createAuthCodeDomain(algAuthCodeDomain);
                algAuthCodeDomainMapper.insert(algAuthCodeDomain);
            }
        }
    }
    @Override
    public List<AlgAuthCode> listUserAuthCode(String usrSn) {
        List<AlgAuthCode> userAllAuthCode = algAuthCodeMapper.selectByUsrSnKey(usrSn);
        return userAllAuthCode;
    }

}