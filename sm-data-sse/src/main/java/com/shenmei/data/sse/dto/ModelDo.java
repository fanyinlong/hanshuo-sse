package com.shenmei.data.sse.dto;

import java.util.List;

public class ModelDo {
    private String modelId;
    private String modelName;
    private String description;
    private List<ModelAddDo> modelAddDoList;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ModelAddDo> getModelAddDoList() {
        return modelAddDoList;
    }

    public void setModelAddDoList(List<ModelAddDo> modelAddDoList) {
        this.modelAddDoList = modelAddDoList;
    }
}
