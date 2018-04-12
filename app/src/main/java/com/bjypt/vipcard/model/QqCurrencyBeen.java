package com.bjypt.vipcard.model;

import java.io.Serializable;

/**
 *     QQ币充值
 * @author Administrator
 *
 */
public class QqCurrencyBeen implements Serializable{
	private int cardmoney;//充值金额
	private String cardid;   //充值金额对应的id
	public int getCardmoney() {
		return cardmoney;
	}
	public void setCardmoney(int cardmoney) {
		this.cardmoney = cardmoney;
	}
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	public QqCurrencyBeen(int cardmoney,String cardid){
		this.cardmoney=cardmoney;
		this.cardid=cardid;
	}
}
