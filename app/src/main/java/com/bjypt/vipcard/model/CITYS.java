package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class CITYS {
    private String province;

    private List<CitysBean> citys ;

    public void setProvince(String province){
        this.province = province;
    }
    public String getProvince(){
        return this.province;
    }
    public void setCitys(List<CitysBean> citys){
        this.citys = citys;
    }
    public List<CitysBean> getCitys(){
        return this.citys;
    }


}
