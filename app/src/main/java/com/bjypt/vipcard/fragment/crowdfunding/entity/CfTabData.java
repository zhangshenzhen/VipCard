package com.bjypt.vipcard.fragment.crowdfunding.entity;

import java.io.Serializable;

public class CfTabData implements Serializable {

    public enum TYPE{
        Sort(0),Where(1);

        private TYPE(int value){
            this.value = value;
        }
        private int value;

        public int getValue() {
            return value;
        }
    }

    private Integer tabType;
    private Integer tabValue;
    private Boolean isGridDisplay;

    public Boolean getGridDisplay() {
        return isGridDisplay;
    }

    public void setGridDisplay(Boolean gridDisplay) {
        isGridDisplay = gridDisplay;
    }

    public Integer getTabType() {
        return tabType;
    }

    public void setTabType(Integer tabType) {
        this.tabType = tabType;
    }

    public Integer getTabValue() {
        return tabValue;
    }

    public void setTabValue(Integer tabValue) {
        this.tabValue = tabValue;
    }
}
