package fr.oltruong.teamag.webbean;

import fr.oltruong.teamag.entity.BusinessCase;
import fr.oltruong.teamag.entity.WorkLoad;

import java.util.List;

/**
 * @author Olivier Truong
 */
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
