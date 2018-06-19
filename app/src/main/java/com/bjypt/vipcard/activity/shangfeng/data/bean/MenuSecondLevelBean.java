package com.bjypt.vipcard.activity.shangfeng.data.bean;

/**
 * 菜单二级 (menu、banner)
 */
public class MenuSecondLevelBean {
    /**
     * 菜单
     */
    private Object menu;
    /**
     * banner
     */
    private Object banner;

    public MenuSecondLevelBean(Object menu, Object banner) {
        this.menu = menu;
        this.banner = banner;
    }

    public Object getMenu() {
        return menu;
    }

    public void setMenu(Object menu) {
        this.menu = menu;
    }

    public Object getBanner() {
        return banner;
    }

    public void setBanner(Object banner) {
        this.banner = banner;
    }

    @Override
    public String toString() {
        return "MenuSecondLevelBean{" +
                "menu=" + menu +
                ", banner=" + banner +
                '}';
    }
}
