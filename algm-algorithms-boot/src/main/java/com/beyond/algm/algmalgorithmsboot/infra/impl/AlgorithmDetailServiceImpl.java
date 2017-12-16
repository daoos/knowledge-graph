package com.beyond.algm.algmalgorithmsboot.infra.impl;

import com.beyond.algm.algmalgorithmsboot.infra.AlgorithmDetailService;
import com.beyond.algm.common.Assert;
import com.beyond.algm.exception.AlgException;
import com.beyond.algm.mapper.AlgModuleMapper;
import com.beyond.algm.vo.AlgModuleVo;
import com.beyond.algm.vo.AlgorithmDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author ：zhangchuanzhi
 * @Description:算法详情
 * @date ：13:33 2017/10/24
 */
@Service
public class AlgorithmDetailServiceImpl implements AlgorithmDetailService {
    @Autowired
    private AlgModuleMapper algModuleMapper;
    @Override
    public AlgModuleVo getAlgorithmDetail(AlgorithmDetailVo algorithmDetailVo)throws AlgException {
        int count = algModuleMapper.getCollectArticle(algorithmDetailVo);
        AlgModuleVo algModuleVo=algModuleMapper.getAlgorithmDetail(algorithmDetailVo);
        if(count==0){
            algModuleVo.setIsCollection("0");
        }else{
            algModuleVo.setIsCollection("1");
        }
        if(Assert.isNotNULL(algModuleVo)) {
            if(Assert.isNotEmpty( algModuleVo.getVerCode())) {
                StringBuffer str = new StringBuffer(algModuleVo.getVerCode());
                str.insert(1, ".");
                str.insert(3, ".");
                algModuleVo.setVerCode(str.toString());
            }
            String sourceCodeUrl= "/git/"+algorithmDetailVo.getUsrCode()+"/"+algorithmDetailVo.getModId()+".git";

            String callAlgorithmUrl="algm://"+algorithmDetailVo.getUsrCode()+"/"+algorithmDetailVo.getModId()+"/"+algModuleVo.getVerCode();
            algModuleVo.setSourceCodeUrl(sourceCodeUrl);
            algModuleVo.setCallAlgorithmUrl(callAlgorithmUrl);
            algModuleVo.setUrl("algm"+"/"+algorithmDetailVo.getUsrCode() +"/" + algorithmDetailVo.getModId());
            algModuleVo.setEditUrl("algm"+"/"+algorithmDetailVo.getUsrCode() + "/" + algorithmDetailVo.getModId()+"/" + "edit");
            algModuleVo.setUsrCode(algorithmDetailVo.getUsrCode());
        }
        return algModuleVo;
    }
}
