package com.bjypt.vipcard.activity.shangfeng.data.interactor;

import com.bjypt.vipcard.activity.shangfeng.data.RequestCallBack;
import com.bjypt.vipcard.activity.shangfeng.data.bean.BannerBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MenuBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MenuSecondLevelBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MerchantListBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.PageBean;

import java.util.List;

/**
 * 主界面实际请求契约类
 */
public interface MainInteractor {
    /**
     * 获取菜单数据(menu、banner)
     * @param callBack
     * @param versioncode
     * @param city_code
     * @param app_type
     */
    void loadMenuData(final RequestCallBack callBack, String versioncode, String city_code, String app_type);

    /**
     * 获取 banner 数据
     * MenuSecondLevelBean 二次解析
     * @return
     */
    List<BannerBean> getBannerData(MenuSecondLevelBean menuSecondLevelBean);

    /**
     * 获取 菜单数据
     * MenuSecondLevelBean 二次解析
     */
    List<MenuBean> getMenuDtat(MenuSecondLevelBean menuSecondLevelBean);

    /**
     * 获取商家列表
     * @param callBack
     * @param cityCode
     * @param latitude
     * @param longitude
     * @param pageNo
     * @param pageSize
     */
    void loadMerchantList(final RequestCallBack callBack, String cityCode, String latitude, String longitude, String pageNo, String pageSize);

    /**
     * 二次解析数据(list) 获取商家列表
     * @param pageBean
     * @return
     */
    List<MerchantListBean> getMerchants(PageBean pageBean);

    /**
     * 版本校验
     * @param type 1用户端 2商户端 3手机商户端
     */
//    void loadVersionMessage(RequestCallBack callBack, int type);



}
