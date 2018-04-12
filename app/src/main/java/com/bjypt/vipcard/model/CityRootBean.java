package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class CityRootBean {
    private List<CITYS> CITYS ;

    public void setCITYS(List<CITYS> CITYS){
        this.CITYS = CITYS;
    }
    public List<CITYS> getCITYS(){
        return this.CITYS;
    }
}
