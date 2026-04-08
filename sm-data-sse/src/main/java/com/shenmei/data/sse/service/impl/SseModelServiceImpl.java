package com.shenmei.data.sse.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.shenmei.data.common.utils.DateUtils;
import com.shenmei.data.sse.domain.SseModelParam;
import com.shenmei.data.sse.domain.SseParam;
import com.shenmei.data.sse.dto.ModelAddDo;
import com.shenmei.data.sse.dto.ModelDo;
import com.shenmei.data.sse.dto.ParamDo;
import com.shenmei.data.sse.mapper.SseModelParamMapper;
import com.shenmei.data.sse.mapper.SseParamMapper;
import com.shenmei.data.sse.util.ParamCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shenmei.data.sse.mapper.SseModelMapper;
import com.shenmei.data.sse.domain.SseModel;
import com.shenmei.data.sse.service.ISseModelService;

/**
 * 数据模型Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
@Service
public class SseModelServiceImpl implements ISseModelService 
{
    @Autowired
    private SseModelMapper sseModelMapper;

    @Autowired
    private SseParamMapper sseParamMapper;

    @Autowired
    private SseModelParamMapper sseModelParamMapper;

    /**
     * 查询数据模型
     * 
     * @param modelId 数据模型主键
     * @return 数据模型
     */
    @Override
    public SseModel selectSseModelByModelId(String modelId)
    {
        return sseModelMapper.selectSseModelByModelId(modelId);
    }

    /**
     * 查询数据模型列表
     * 
     * @param sseModel 数据模型
     * @return 数据模型
     */
    @Override
    public List<SseModel> selectSseModelList(SseModel sseModel)
    {
        return sseModelMapper.selectSseModelList(sseModel);
    }

    /**
     * 新增数据模型
     * 
     * @param sseModel 数据模型
     * @return 结果
     */
    @Override
    public int insertSseModel(SseModel sseModel)
    {
        sseModel.setCreateTime(DateUtils.getNowDate());
        return sseModelMapper.insertSseModel(sseModel);
    }

    /**
     * 修改数据模型
     * 
     * @param sseModel 数据模型
     * @return 结果
     */
    @Override
    public int updateSseModel(SseModel sseModel)
    {
        sseModel.setUpdateTime(DateUtils.getNowDate());
        return sseModelMapper.updateSseModel(sseModel);
    }

    /**
     * 批量删除数据模型
     * 
     * @param modelIds 需要删除的数据模型主键
     * @return 结果
     */
    @Override
    public int deleteSseModelByModelIds(String[] modelIds)
    {
        return sseModelMapper.deleteSseModelByModelIds(modelIds);
    }

    /**
     * 删除数据模型信息
     * 
     * @param modelId 数据模型主键
     * @return 结果
     */
    @Override
    public int deleteSseModelByModelId(String modelId)
    {
        return sseModelMapper.deleteSseModelByModelId(modelId);
    }

    @Override
    public List<ModelAddDo> getParamList() {
        List<SseParam> paramList = sseParamMapper.selectSseParamList(new SseParam());

        if(paramList!=null && paramList.size()>0){
            List<ModelAddDo> modelAddDoList = new java.util.ArrayList<>();
            ParamCategory paramCategory [] = ParamCategory.values();
            for(ParamCategory paramCategory1 : paramCategory){
                ModelAddDo
                        modelAddDo = new ModelAddDo();
                modelAddDo.setType(paramCategory1.name());
                modelAddDo.setParamList(paramList.stream().filter(param -> param.getParamCategory().equals(paramCategory1.name())).map(param -> {
                    ParamDo paramDo = new ParamDo();
                    paramDo.setParamValue(param.getParamId());
                    paramDo.setParamName(param.getParamName());
                    return paramDo;
                }).collect(Collectors.toList()));
                modelAddDoList.add(modelAddDo);


            }
            return modelAddDoList;

        }
        return Collections.emptyList();
    }

    @Override
    public ModelDo getModel(String modelId) {
        SseModel model = sseModelMapper.selectSseModelByModelId(modelId);
        if(model!=null){
            ModelDo modelDo = new ModelDo();
            modelDo.setModelId(model.getModelId());
            modelDo.setModelName(model.getModelName());
            modelDo.setDescription(model.getDescription());
            SseModelParam modelParam = new SseModelParam();
            modelParam.setModelId(modelId);

            List<SseModelParam> paramList = sseModelParamMapper.selectSseModelParamList(modelParam);
            if(paramList!=null && paramList.size()>0){
                List<ModelAddDo> modelAddDoList = new ArrayList<>();
                for(SseModelParam param : paramList){
                    SseParam param1 = sseParamMapper.selectSseParamByParamId(param.getParamId());
                    ParamDo pDo = new ParamDo();
                    pDo.setParamValue(param1.getParamId());
                    pDo.setParamName(param1.getParamName());
                    Optional<ModelAddDo> modelAddDoOptional = modelAddDoList.stream().filter(m->m.getType().equals(param1.getParamCategory())).findFirst();
                    if(modelAddDoOptional.isPresent()){

                        modelAddDoOptional.get().getParamList().add(pDo);
                    }else{
                        ModelAddDo modelAddDo = new ModelAddDo();
                        modelAddDo.setType(param1.getParamCategory());
                        List<ParamDo> paraList = new ArrayList<>();
                        paraList.add(pDo);
                        modelAddDo.setParamList(paraList);
                        modelAddDoList.add(modelAddDo);

                    }

                }
                modelDo.setModelAddDoList(modelAddDoList);
                return modelDo;
            }


        }
        return null;
    }
}
