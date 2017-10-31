package com.beyond.algo.algoconsoleboot.adapter;

import com.beyond.algo.algoconsoleboot.adapter.infra.ModuleAdapter;
import com.beyond.algo.algoconsoleboot.model.GitConfigModel;
import com.beyond.algo.algoconsoleboot.model.GitUser;
import com.beyond.algo.algoconsoleboot.model.ProjectConfigModel;
import com.beyond.algo.algoconsoleboot.util.FreemarkerUtil;
import com.beyond.algo.common.FileUtil;
import com.beyond.algo.exception.AlgException;
import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: qihe
 * @Description:
 * @Date: create in
 */
public class JavaModuleAdapter implements ModuleAdapter {

    @Override
    public void createModule(String username, String projectName,GitConfigModel gitConfigModel,ProjectConfigModel projectConfigModel) throws Exception {
        FileUtil.createDir(gitConfigModel.getLocalBasePath());

        // 以用户名作为用户子目录名
        String userPath = gitConfigModel.getLocalBasePath() + File.separator + username;
        FileUtil.createDir(userPath);

        // 以项目名作为项目子目录名
        String projectPath = userPath + File.separator + projectName;
        FileUtil.createDir(projectPath);

        // 先将直接拷贝的文件复制到目录下
        String templatePath = new ClassPathResource("templates/project/java").getFile().getPath();
        String[] cloneFileArr = projectConfigModel.getCloneFiles().split(",");
        for (String cloneFileName : cloneFileArr) {
            File srcFile = new File(templatePath + File.separator + cloneFileName);
            File destFile = new File(projectPath + File.separator + cloneFileName);
            FileUtils.copyFile(srcFile, destFile);
        }

        // 拼装参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("username", username);
        paramMap.put("projectName", projectName);
        paramMap.put("language", "java");
        paramMap.put("currentTime", new Date().getTime() + "");

        // Freemarker生成配置文件
        String[] ftlFileArr = projectConfigModel.getFtlFiles().split(",");
        for (String ftlFileName : ftlFileArr) {
            String destFileName = ftlFileName.substring(0, ftlFileName.lastIndexOf("."));
            FreemarkerUtil.createFile(templatePath, ftlFileName, projectPath, destFileName, paramMap);
        }

        // 生成算法主类
        String srcPath = projectPath + File.separator + "src";
        FileUtil.createDir(srcPath);
        String packetPath = srcPath + File.separator + projectConfigModel.getPackageName();
        FileUtil.createDir(packetPath);
        String mainClassPath = packetPath + File.separator + projectName;
        FileUtil.createDir(mainClassPath);
        FreemarkerUtil.createFile(templatePath, "Main.java.ftl", mainClassPath, projectName + ".java", paramMap);
    }
    @Override
    public boolean moduleAntBuild(String  path)throws Exception{
        File buildFile = new File(path);
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
            project.executeTarget(project.getDefaultTarget());
            project.fireBuildFinished(null);  //构建结束

        //    moduleAntZip("E:\\repo\\qihe\\TestJava2\\");
        } catch (BuildException e) {
            project.fireBuildFinished(e);  //构建抛出异常
            return false;
        }
        return true;

    }


}