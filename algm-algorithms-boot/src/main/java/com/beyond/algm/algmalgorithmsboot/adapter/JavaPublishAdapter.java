package com.beyond.algm.algmalgorithmsboot.adapter;

import com.beyond.algm.algmalgorithmsboot.adapter.infra.PublishAdapter;
import com.beyond.algm.algmalgorithmsboot.model.PublishConfigModel;
import com.beyond.algm.algmalgorithmsboot.util.FreemarkerUtil;
import com.beyond.algm.common.Assert;
import com.beyond.algm.common.FileUtil;
import com.beyond.algm.constant.Constant;
import com.beyond.algm.exception.AlgException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: gaohaijun
 * @Description:
 * @Date: create in
 */
@Slf4j
public class JavaPublishAdapter implements PublishAdapter {

    @Override
    public void initBootProject(String userCode, String projectName, String projectDescription, String algoVersion, PublishConfigModel publishConfigModel, String active,String modPath,String publishPath) throws AlgException {

        //TODO：先判断有没有项目，项目下有没有压缩包


        // 获取根目录
        FileUtil.createDir(publishConfigModel.getLocalBasePath());

        // 以用户名作为用户子目录名
        String userPath = publishConfigModel.getLocalBasePath() + File.separator + userCode;
        FileUtil.createDir(userPath);

        // 以项目名作为项目子目录名
        String projectPath = userPath + File.separator + projectName;
        FileUtil.createDir(projectPath);

        // 创建一个空的dist文件夹
        FileUtil.createDir(projectPath + File.separator + publishConfigModel.getDistFolder());

        // 拼装参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userCode", userCode);
        paramMap.put("projectName", projectName);
        paramMap.put("projectDescription", projectDescription);
        paramMap.put("algoVersion", algoVersion);
        paramMap.put("javaVersion", publishConfigModel.getJavaVersion());
        paramMap.put("distFolder", publishConfigModel.getDistFolder());
        paramMap.put("packetName", publishConfigModel.getPackageName());

        try {

            String templatePath = null;
            if (Assert.isNotEmpty(active) && active.equals("dev")) {
                try {
                    templatePath = "file:" + new ClassPathResource("/templates/boot/java/").getFile().getPath() + File.separator;
                } catch (Exception e) {
                    log.debug("调试Spring Boot发布取模板失败,错误:{}", e.getMessage());
                }
            } else {
                templatePath = "jar:" + this.getClass().getClassLoader().getResource("/templates/boot/java/").getPath();
            }

            // 生成pom.xml文件
            FreemarkerUtil.createFile(templatePath, "pom.xml.ftl", projectPath, "pom.xml", paramMap);
            // 生成src基本结构
            String srcPath = projectPath + File.separator + "src";
            FileUtil.createDir(srcPath);
            String testPath = srcPath + File.separator + "test";
            FileUtil.createDir(testPath);
            String testJavaPath = testPath + File.separator + "java";
            FileUtil.createDir(testJavaPath);

            String mainPath = srcPath + File.separator + "main";
            FileUtil.createDir(mainPath);
            String mainResourcePath = mainPath + File.separator + "resources";
            FileUtil.createDir(mainResourcePath);
            String appFileName = mainResourcePath + File.separator + "application.properties";
            FileUtil.createDir(appFileName);

            String mainJavaPath = mainPath + File.separator + "java";
            FileUtil.createDir(mainJavaPath);

            // 生成boot运行主类
            String comPath = mainJavaPath + File.separator + "com";
            FileUtil.createDir(comPath);
            String packetPath = comPath + File.separator + publishConfigModel.getPackageName();
            FileUtil.createDir(packetPath);
            String packetUserPath = packetPath + File.separator + userCode;
            FileUtil.createDir(packetUserPath);
            FreemarkerUtil.createFile(templatePath, "AlgmarketPublishApplication.java.ftl", packetUserPath, "AlgmarketPublishApplication.java", paramMap);
        } catch (Exception e) {
            throw new AlgException(e);
        }
        //生成jar包到dist文件夹
        makeDistJar(modPath,publishConfigModel.getPackageName());
        //将项目中的zip压缩包直接解压缩到dist文件夹下
        unzipPublishMod(modPath + File.separator + publishConfigModel.getPackageName() + Constant.ZIP_SUFFIX ,publishPath + File.separator + publishConfigModel.getDistFolder());
    }




    private void makeDistJar(String path,String packageName) throws AlgException{
        File buildFile = new File(path + File.separator + Constant.map.get("Java"));
        Project project = new Project();
        DefaultLogger consoleLogger = new DefaultLogger();
        consoleLogger.setErrorPrintStream(System.err);
        consoleLogger.setOutputPrintStream(System.out);
        consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
        project.addBuildListener(consoleLogger);

        try {
            project.fireBuildStarted();  //项目开始构建
            project.init();

            ProjectHelper helper = ProjectHelper.getProjectHelper();
            helper.parse(project, buildFile);
            Task task = project.getTargets().get("compile").getTasks()[1];
            task.getRuntimeConfigurableWrapper().setAttribute("fork","true");

            project.executeTarget("stage");
            File zipFile = new File(path + File.separator + packageName + Constant.ZIP_SUFFIX);
            if(zipFile.exists()){
                zipFile.delete();
            }
            Zip zip = new Zip();
            zip.setProject(project);
            zip.setDestFile(zipFile);
            FileSet fileSet = new FileSet();
            fileSet.setProject(project);
            fileSet.setDir(new File(path + File.separator + "dist"));
            zip.addFileset(fileSet);
            zip.execute();

            project.executeTarget("clean");

            project.fireBuildFinished(null);  //构建结束

        } catch (BuildException e) {
            log.error("构建错误", e);
            project.fireBuildFinished(e);  //构建抛出异常
            throw new AlgException(e);
        }
    }

    private void unzipPublishMod(String zipPath ,String publishDistPath){
        Project project=new Project();
        Expand expand = new Expand();
        expand.setProject(project);
        expand.setSrc(new File(zipPath));
        expand.setOverwrite(true);
        expand.setDest(new File(publishDistPath));
        expand.execute();
    }
}