package com.beyond.algm.algmalgorithmsboot.infra.impl;

import com.beyond.algm.algmalgorithmsboot.infra.MvnService;
import com.beyond.algm.algmalgorithmsboot.model.PublishConfigModel;
import com.beyond.algm.common.Assert;
import com.beyond.algm.exception.AlgException;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.shared.invoker.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;

/**
 * @Author: qihe
 * @Description:
 * @Date: create in 2017/11/23 11:03
 */
@Service
@Slf4j
public class MvnServiceImpl implements MvnService {

    @Autowired
    private PublishConfigModel publishConfigModel;

    private static String mavenHomePath;

    static {
        mavenHomePath = System.getenv("MAVEN_HOME");
        if (Assert.isEmpty(mavenHomePath) || "".equals(mavenHomePath)) {
            mavenHomePath = System.getenv("M2_HOME");
        }
        if (Assert.isEmpty(mavenHomePath) || "".equals(mavenHomePath)) {
            log.error("Can not get maven env,please install apache maven and configuring environment variables");
        }
    }

    public void mvnPackageMod(String userCode, String modId) throws AlgException {
        if (Assert.isEmpty(mavenHomePath) || "".equals(mavenHomePath)) {
            throw new AlgException("发布项目失败，获取不到 maven 路径！");
        }
        InvocationRequest request = new DefaultInvocationRequest();
        String path = publishConfigModel.getLocalBasePath() + File.separator + userCode + File.separator + modId + File.separator + "pom.xml";
        request.setPomFile(new File(path));
        request.setGoals(Arrays.asList("clean", "package -Dmaven.test.skip=true"));

        Invoker invoker = new DefaultInvoker();

        invoker.setMavenHome(new File(mavenHomePath));
        try {
            invoker.execute(request);
            InvocationResult result = invoker.execute(request);

            if (result.getExitCode() != 0) {
                throw new IllegalStateException("Build failed.");
            }
        } catch (MavenInvocationException e) {
            e.printStackTrace();
        }
    }
}
