package com.bjypt.vipcard.model;

/**
 * Created by 崔龙 on 2018/1/19.
 */

public class FinanceAmountWhereaboutsInfoBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"finance_desc":"1、 优惠券到期后，如选择购买金转入平台余额或购买下期\n     优惠券，不送优惠金，可得到收益金，可提现；    \n2、优惠券到期后，如选择购买金进入卡余额，可得到\n     收益金及优惠金，均不可提现，可在加油时使用；","whereAbouts":{"merchant_balance":{"merchant_balance_check":"0","use":"1","name":"优惠卡余额","merchant_balance_desc":"不可提现，赠送优惠金，可得收益金"},"platform_banance":{"use":"1","platform_balance_check":"1","name":"平台余额","platform_balance_desc":"不赠送优惠金，可得收益金"},"buy_next":{"use":"1","name":"购买下一期","buy_next_check":"0","buy_next_desc":"不赠送优惠金，可得收益金"}}}
     */

    private int resultStatus;
    private String msg;
    private ResultDataBean resultData;

    public int getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(int resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultDataBean getResultData() {
        return resultData;
    }

    public void setResultData(ResultDataBean resultData) {
        this.resultData = resultData;
    }

    public static class ResultDataBean {
        /**
         * finance_desc : 1、 优惠券到期后，如选择购买金转入平台余额或购买下期
         优惠券，不送优惠金，可得到收益金，可提现；
         2、优惠券到期后，如选择购买金进入卡余额，可得到
         收益金及优惠金，均不可提现，可在加油时使用；
         * whereAbouts : {"merchant_balance":{"merchant_balance_check":"0","use":"1","name":"优惠卡余额","merchant_balance_desc":"不可提现，赠送优惠金，可得收益金"},"platform_banance":{"use":"1","platform_balance_check":"1","name":"平台余额","platform_balance_desc":"不赠送优惠金，可得收益金"},"buy_next":{"use":"1","name":"购买下一期","buy_next_check":"0","buy_next_desc":"不赠送优惠金，可得收益金"}}
         */

        private String finance_desc;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        private String title;
        private WhereAboutsBean whereAbouts;

        public String getFinance_desc() {
            return finance_desc;
        }

        public void setFinance_desc(String finance_desc) {
            this.finance_desc = finance_desc;
        }

        public WhereAboutsBean getWhereAbouts() {
            return whereAbouts;
        }

        public void setWhereAbouts(WhereAboutsBean whereAbouts) {
            this.whereAbouts = whereAbouts;
        }

        public static class WhereAboutsBean {
            /**
             * merchant_balance : {"merchant_balance_check":"0","use":"1","name":"优惠卡余额","merchant_balance_desc":"不可提现，赠送优惠金，可得收益金"}
             * platform_banance : {"use":"1","platform_balance_check":"1","name":"平台余额","platform_balance_desc":"不赠送优惠金，可得收益金"}
             * buy_next : {"use":"1","name":"购买下一期","buy_next_check":"0","buy_next_desc":"不赠送优惠金，可得收益金"}
             */

            private MerchantBalanceBean merchant_balance;
            private PlatformBananceBean platform_banance;
            private BuyNextBean buy_next;

            public MerchantBalanceBean getMerchant_balance() {
                return merchant_balance;
            }

            public void setMerchant_balance(MerchantBalanceBean merchant_balance) {
                this.merchant_balance = merchant_balance;
            }

            public PlatformBananceBean getPlatform_banance() {
                return platform_banance;
            }

            public void setPlatform_banance(PlatformBananceBean platform_banance) {
                this.platform_banance = platform_banance;
            }

            public BuyNextBean getBuy_next() {
                return buy_next;
            }

            public void setBuy_next(BuyNextBean buy_next) {
                this.buy_next = buy_next;
            }

            public static class MerchantBalanceBean {
                /**
                 * merchant_balance_check : 0
                 * use : 1
                 * name : 优惠卡余额
                 * merchant_balance_desc : 不可提现，赠送优惠金，可得收益金
                 */

                private String merchant_balance_check;
                private String use;
                private String name;
                private String merchant_balance_desc;

                public String getMerchant_balance_check() {
                    return merchant_balance_check;
                }

                public void setMerchant_balance_check(String merchant_balance_check) {
                    this.merchant_balance_check = merchant_balance_check;
                }

                public String getUse() {
                    return use;
                }

                public void setUse(String use) {
                    this.use = use;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getMerchant_balance_desc() {
                    return merchant_balance_desc;
                }

                public void setMerchant_balance_desc(String merchant_balance_desc) {
                    this.merchant_balance_desc = merchant_balance_desc;
                }
            }

            public static class PlatformBananceBean {
                /**
                 * use : 1
                 * platform_balance_check : 1
                 * name : 平台余额
                 * platform_balance_desc : 不赠送优惠金，可得收益金
                 */

                private String use;
                private String platform_balance_check;
                private String name;
                private String platform_balance_desc;

                public String getUse() {
                    return use;
                }

                public void setUse(String use) {
                    this.use = use;
                }

                public String getPlatform_balance_check() {
                    return platform_balance_check;
                }

                public void setPlatform_balance_check(String platform_balance_check) {
                    this.platform_balance_check = platform_balance_check;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getPlatform_balance_desc() {
                    return platform_balance_desc;
                }

                public void setPlatform_balance_desc(String platform_balance_desc) {
                    this.platform_balance_desc = platform_balance_desc;
                }
            }

            public static class BuyNextBean {
                /**
                 * use : 1
                 * name : 购买下一期
                 * buy_next_check : 0
                 * buy_next_desc : 不赠送优惠金，可得收益金
                 */

                private String use;
                private String name;
                private String buy_next_check;
                private String buy_next_desc;

                public String getUse() {
                    return use;
                }

                public void setUse(String use) {
                    this.use = use;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getBuy_next_check() {
                    return buy_next_check;
                }

                public void setBuy_next_check(String buy_next_check) {
                    this.buy_next_check = buy_next_check;
                }

                public String getBuy_next_desc() {
                    return buy_next_desc;
                }

                public void setBuy_next_desc(String buy_next_desc) {
                    this.buy_next_desc = buy_next_desc;
                }
            }
        }
    }
}
