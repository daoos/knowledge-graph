package com.beyond.algo.algoconsoleboot.infra.impl;

/**
 * @author ：zhangchuanzhi
 * @Description:用户登录注册server层
 * @date ：13:26 2017/9/25
 */

import com.beyond.algo.algoconsoleboot.infra.PayCashService;
import com.beyond.algo.exception.AlgException;
import com.beyond.algo.mapper.AlgCashTransMapper;

import com.beyond.algo.model.*;
import com.beyond.algo.vo.PayRecordVo;
import com.beyond.algo.vo.PayVo;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PayCashServiceImpl implements PayCashService {
    private final static Logger logger = LoggerFactory.getLogger(PayCashServiceImpl.class);
    @Autowired
    private AlgCashTransMapper algCashTransMapper;

    /**
     * @author ：zhangchuanzhi
     * @Description:用户充值记录
     * @param：User
     * @Modify By :zhangchuanzhi
     * @date ：15:09 2017/10/09
     */
    @Override
    public List<AlgCashTrans> payRecord(PayRecordVo payRecordVo)throws AlgException {
       //分页处理
        PageHelper.startPage(payRecordVo.getPage(), payRecordVo.getRows());
        List<AlgCashTrans> algCashTransList=algCashTransMapper.payRecord(payRecordVo.getUsrSn());
        return algCashTransList;
    }

    /**
     * @author ：zhangchuanzhi
     * @Description: 购买积分
     * @param：PayVo
     * @Modify By :zhangchuanzhi
     * @date ：15:43 2017/10/16
     */
    @Override
     // 购买积分
    public void buyIntegral(PayVo payVo)throws AlgException {
      // 调用充值接口未完成


    }


}

