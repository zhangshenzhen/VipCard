package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/13 0013.
 */

public class ListTestData {
    private String name;
    private List<GridViewTestData> gridData;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<GridViewTestData> getGridData() {
        return gridData;
    }
    public void setGridData(List<GridViewTestData> gridData) {
        this.gridData = gridData;
    }
}
