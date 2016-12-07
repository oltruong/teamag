package com.oltruong.teamag.webbean;

import com.oltruong.teamag.model.BusinessCase;
import com.oltruong.teamag.model.WorkLoad;

import java.util.List;


public class WorkLoadContainer {


    private BusinessCase businessCase;

    private List<WorkLoad> workLoadList;


    public BusinessCase getBusinessCase() {
        return businessCase;
    }

    public void setBusinessCase(BusinessCase businessCase) {
        this.businessCase = businessCase;
    }

    public List<WorkLoad> getWorkLoadList() {
        return workLoadList;
    }

    public void setWorkLoadList(List<WorkLoad> workLoadList) {
        this.workLoadList = workLoadList;
    }
}
