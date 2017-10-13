package com.beyond.algo.infra;


import com.beyond.algo.common.Result;
import com.beyond.algo.model.AlgAuthCodeDomain;

/**
 * @author XianjieZhang E-mail:xj_zh@foxmail.com
 * @Description:接口定义
 * @version Created in：2017/9/28 0028 上午 10:59
 */
public interface AuthCodeDomainService {
    Result createAuthCodeDomain(AlgAuthCodeDomain algAuthCodeDomain);
    Result deleteAuthCodeDomain(String addSn_id);
    Result updataAuthCodeDomain(AlgAuthCodeDomain algAuthCodeDomain);
    Result selectAuthCodeDomain(String addSn_id);
    Result selectAll();
}