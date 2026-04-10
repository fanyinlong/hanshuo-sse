package com.shenmei.data.sse.dto;

import org.springframework.security.core.parameters.P;

import java.util.List;

public class ModelAddDo {
    private String type;

    private List<ParamDo> paramList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ParamDo> getParamList() {
        return paramList;
    }

    public void setParamList(List<ParamDo> paramList) {
        this.paramList = paramList;
    }
}
