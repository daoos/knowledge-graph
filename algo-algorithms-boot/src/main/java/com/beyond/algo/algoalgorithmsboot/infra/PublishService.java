package com.beyond.algo.algoalgorithmsboot.infra;

public interface PublishService {

    /**
     * 初始化Spring Boot工程.
     */
    void initBootProject(String username, String projectName, String projectDescription, String algoVersion) throws Exception;
}