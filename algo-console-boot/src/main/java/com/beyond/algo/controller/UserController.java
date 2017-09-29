package com.beyond.algo.controller;

import com.beyond.algo.common.Result;
import com.beyond.algo.common.ResultEnum;
import com.beyond.algo.model.User;
import com.beyond.algo.infra.UserServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;




/**
 * @author ：zhangchuanzhi
 * @Description:实现用户登录注册功能
 * @date ：13:16 2017/9/25
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServer userService;

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    /**
     * @author ：zhangchuanzhi
     * @Description:实现用户注册功能
     * @param：User
     * @Modify By :zhangchuanzhi
     * @date ：13:16 2017/9/25
     */
    @RequestMapping(value="/register", method=RequestMethod.POST)
    public @ResponseBody
    Result<Object> register(User user){
        logger.info("用户名:{},用户密码:{},用户邮箱:{}", user.getUsrname(), user.getPasswd(),user.getEmail());
        try {
            Result result = userService.createUser(user);
            return result;
        } catch (Exception e) {
            logger.info("注册失败",e);
            return new Result<Object>(ResultEnum.FAILURE.code, e.getMessage());
        }
    }
    /**
     * @author ：zhangchuanzhi
     * @Description:用户登录处理
     * @param：User
     * @Modify By :zhangchuanzhi
     * @date ：8:49 2017/9/27
     */
    @RequestMapping(value="/login", method=RequestMethod.POST)
    @ResponseBody
    public Result userLogin(User user) {
        logger.info("用户名:{},用户密码:{}", user.getUsrname(), user.getPasswd());
        try {

            Result result = userService.userLogin(user);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<Object>(ResultEnum.FAILURE.code, e.getMessage());
        }
    }

}
