package com.beyond.algo.algodataboot.impl;

import com.beyond.algo.algodataboot.AlgoDataBootApplication;
import com.beyond.algo.algodataboot.infra.ModelSetService;
import com.beyond.algo.model.AlgModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ：
 * @Description:
 * @date ：10:16 2017/11/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AlgoDataBootApplication.class)
public class ModelSetServiceTests {
    @Autowired
    private ModelSetService modelSetService;


    @Test
    public void deleteModel() throws Exception {
        modelSetService.deleteModel("3");
    }

    @Test
    public void deleteModelSet() throws Exception {
        AlgModel algModel=new AlgModel();
        algModel.setModelSetSn("4");
        algModel.setUsrSn("1");
        int count =modelSetService.deleteModelSet(algModel);

    }
}