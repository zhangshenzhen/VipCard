package com.bjypt.vipcard.utils;

import android.os.Handler;
import android.os.Message;

import com.bjypt.vipcard.config.Constant;

public class VipHandler extends Handler implements Constant {
	private HandleStateInterFace listen; 
	public VipHandler(HandleStateInterFace listen){
		this.listen = listen;
	}


	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		switch(msg.what){
		case HandlerSuccessTure:
			if(listen != null)
				listen.HandlerSuccessTure();
			break;
		case HandlerFail:
			if(listen != null&&msg.obj!=null)
				listen.HandlerFail(msg.obj.toString());
			break;
		case HandlerException:
			if(listen != null)
				listen.HandlerException(msg.obj.toString());
			break;
		case HandlerSuccessFail:
			if(listen != null)
				listen.HandlerSuccessFail(msg.obj.toString());
			break;
		}
	}
	public interface HandleStateInterFace{
		void HandlerSuccessTure();
		void HandlerSuccessFail(String message);
		void HandlerException(String message);
		void HandlerFail(String message);
	}
	public HandleStateInterFace getListen() {
		return listen;
	}

	public void setListen(HandleStateInterFace listen) {
		this.listen = listen;
	}



}
