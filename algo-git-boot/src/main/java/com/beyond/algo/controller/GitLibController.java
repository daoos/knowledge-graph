package com.beyond.algo.controller;

import com.beyond.algo.common.AlgoplatResult;
import com.beyond.algo.common.BaseEnum;
import com.beyond.algo.infra.BuildAntProjectService;
import com.beyond.algo.infra.GitLibService;
import com.beyond.algo.infra.JGitService;
import com.beyond.algo.model.GitUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * @author ：zhangchuanzhi
 * @Description:gitlib操作
 * @date ：8:51 2017/9/28
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/git")
public class GitLibController {
    @Autowired
    private GitLibService gitLibService;
    @Autowired
    private JGitService jGitService;
    @Autowired
    private BuildAntProjectService buildAntProjectService;
    private final static Logger logger = LoggerFactory.getLogger(GitLibController.class);
    /**
     * @author ：zhangchuanzhi
     * @Description:实现gitlib上创建用户
     * @param：User
     * @Modify By :zhangchuanzhi
     * @date ：9:00 2017/9/28
     */
    @RequestMapping(value="/addGitLibUser", method= RequestMethod.POST)
    public @ResponseBody
     AlgoplatResult addGitLibUser(GitUser gitUser)  {
        logger.info("用户名：{}用户全称：{} 用户密码：{} 用户邮箱：{}",gitUser.getUsername(),gitUser.getFullName(),gitUser.getPassword(),gitUser.getEmail());
        boolean  result = false;
        try {
            result = gitLibService.addGitLibUser(gitUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new AlgoplatResult<Object>(BaseEnum.FAILURE.code, e.getMessage());
        }
        if(result){
            return AlgoplatResult.successResponse();
        }
        return AlgoplatResult.failureResponse();
    }

    /**
     * @author ：zhangchuanzhi
     * @Description:在gitlib上创建用户
     * @param：GitUser
     * @Modify By :zhangchuanzhi
     * @date ：9:31 2017/9/28
     */

    @RequestMapping(value="/createProject", method=RequestMethod.POST)
    public @ResponseBody AlgoplatResult createGitLibProject(GitUser gitUser)  {
        logger.info("用户名：{} 用户密码：{} ",gitUser.getUsername(),gitUser.getPassword());
        boolean result= false;
        try {
            result = gitLibService.createGitLibProject(gitUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new AlgoplatResult<Object>(BaseEnum.FAILURE.code, e.getMessage());
        }
        if(result){
            return AlgoplatResult.successResponse();
        }
        return AlgoplatResult.failureResponse();

    }
    /**
     * @author ：zhangchuanzhi
     * @Description:从git首次克隆项目
     * @param：GitUser
     * @Modify By :zhangchuanzhi
     * @date ：9:43 2017/9/28
     */
    @RequestMapping(value="/cloneProject", method=RequestMethod.POST)
    public @ResponseBody AlgoplatResult gitCloneProject(GitUser gitUser)  {
        logger.info("gitCloneProject方法用户名：{} 用户密码{} 项目名称：{}",gitUser.getUsername(),gitUser.getPassword(),gitUser.getProjectName());
        try {
            jGitService.gitCloneProject(gitUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new AlgoplatResult<Object>(BaseEnum.FAILURE.code, e.getMessage());
        }
        return AlgoplatResult.successResponse();

    }

    /**
     * @author ：zhangchuanzhi
     * @Description:初始化时提交本地所有代码到远端仓库
     * @param :GitUser
     * @return
     */
    @RequestMapping(value="/commit", method=RequestMethod.POST)
    public @ResponseBody AlgoplatResult initCommitAndPushAllFiles(GitUser gitUser) {
            boolean result= jGitService.initCommitAndPushAllFiles(gitUser);
            if(result){
                return AlgoplatResult.successResponse();
            }
            return AlgoplatResult.failureResponse();
    }
    /**
     * @Description:删除本地文件同时同步服务器
     * author:zhangchuanzhi
     * @param :gitUser
     * @return
     */
    @RequestMapping(value="/del", method=RequestMethod.POST)
    public @ResponseBody AlgoplatResult commitAndPushDelAllFiles(GitUser gitUser) {
        try {
            boolean result= jGitService.commitAndPushDelAllFiles(gitUser);
            if(result){
                return AlgoplatResult.successResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new AlgoplatResult<Object>(BaseEnum.FAILURE.code, e.getMessage());
        }
        return AlgoplatResult.failureResponse();
    }

    /**
     * 查看文件状态
     * author:zhangchuanzhi
     * @param :gitUser
     * @return
     */
    @RequestMapping(value="/showStatus", method=RequestMethod.POST)
    public @ResponseBody AlgoplatResult gitShowStatus(String path) {
        try {
            File repoDir =new File(path);
            jGitService.gitShowStatus(repoDir);
        } catch (Exception e) {
            e.printStackTrace();
            return new AlgoplatResult<Object>(BaseEnum.FAILURE.code, e.getMessage());
        }
        return AlgoplatResult.successResponse();
    }

    /**
     * @author ：zhangchuanzhi
     * @Description:ant项目进行编译打包同时解压到指定目录并且代码上传git上
     * @param：User
     * @Modify By :zhangchuanzhi
     * @date ：13:12 2017/9/28
     */
    @RequestMapping(value="/buildProject", method=RequestMethod.POST)
    public @ResponseBody void buildAndUpLoadProject(GitUser gitUser){
        try {
            buildAntProjectService.buildAndUpLoadProject(gitUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
