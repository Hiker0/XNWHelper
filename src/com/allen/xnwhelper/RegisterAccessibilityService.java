package com.allen.xnwhelper;

import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class RegisterAccessibilityService extends AccessibilityService {

	static int WINDOW_NONE = 0;
	static int WINDOW_LOGIN = 1;
	static int WINDOW_REGISTER = 2;
	static int WINDOW_DIALOG = 3;
	static int WINDOW_IDENTIFY= 4;
	
	int window = WINDOW_NONE;
	boolean enabled = true;
	static String PACKAGE="com.xnw.qun";
	
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		// TODO Auto-generated method stub
		processAccessibilityEnvent(event);
	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onServiceConnected() {
		// TODO Auto-generated method stub
		super.onServiceConnected();
		
		SharedPreferences sp = this.getSharedPreferences("settings", MODE_PRIVATE );
	}

	private void processAccessibilityEnvent(AccessibilityEvent event) {
		if(!enabled){
			return;
		}
	    Log.d("allen", event.eventTypeToString(event.getEventType()));
	    
	    int type = event.getEventType();
	    String classname = event.getClassName().toString();
	    Log.d("allen", "classname: "+classname);
	    if (type == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
	    	if(classname.contains("activity.LoginActivity")){
	    		Log.d("allen", "Enter LoginActivity");
	    		window = WINDOW_LOGIN;
	    		
	    		findAndPerformAction(PACKAGE+":id/btn_register");
	    		
	    	}else if(classname.contains("register.UserRegisterActivity")){
	    		Log.d("allen", "Enter UserRegisterActivity");
	    		window = WINDOW_REGISTER;
	    		
	    		findAndPerformSetText(PACKAGE+":id/et_mobile_or_email","13816456379");
	    		findAndPerformSetText(PACKAGE+":id/et_pwd","123456");
	    	}else if(classname.contains("android.app.Dialog")){
	    		window = WINDOW_DIALOG;
	    		findAndPerformAction(PACKAGE+":id/positive_btn");
	    	}else if(classname.contains("register.IdentifyingCodeActivity")){
	    		window = WINDOW_IDENTIFY;
	    		findAndPerformSetText(PACKAGE+":id/et_identifying_code","460532");
	    	}
	    	
	    }
	    if (type == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ){
	    	if( window == WINDOW_REGISTER){
	    		//findAndPerformAction(PACKAGE+":id/btn_next_step");
	    	}else if( window== WINDOW_IDENTIFY){
	    		findAndPerformAction(PACKAGE+":id/btn_submit");
	    	}
	    }
	}
	
	private void findAndPerformAction(String text){
		if(getRootInActiveWindow()==null) {
			Log.d("allen", "getRootInActiveWindow null");
			return;
		}
		List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByViewId(text);
		Log.d("allen", "find nodes: "+ nodes.size());
		for (int i = 0; i < nodes.size(); i++){
			
			AccessibilityNodeInfo node = nodes.get(i);
			if (node.isEnabled()){
				node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			}
		}
	}
	
	private void findAndPerformSetText(String id, String text){
		if(getRootInActiveWindow()==null) {
			Log.d("allen", "getRootInActiveWindow null");
			return;
		}
		List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByViewId(id);
		Log.d("allen", "find nodes: "+ nodes.size());
		
		Bundle arguments = new Bundle();
	    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,text);
		for (int i = 0; i < nodes.size(); i++){
			
			AccessibilityNodeInfo node = nodes.get(i);
			if (node.isEnabled()){
				node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);;
			}
		}
	}
}
