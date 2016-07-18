package com.allen.xnwhelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.allen.xnwhelper.YMCompat.CompleteListener;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class RegisterAccessibilityService extends AccessibilityService implements  CompleteListener{

	boolean enabled = true;
	boolean login = false;
	static String PACKAGE="com.xnw.qun";
    static String UserName = "wzj744717727";
    static String PassWord = "qiaohui521";
    static String XNW_ID = "3177";
    static String Country_id = "1";
    static String Province_id = "370000";
    
	enum State {unkown,welcome,login,register,register_filled,dialog,identify,identify_filled,fillname,fillnamed,main};
	File tarFile;
	UserInfo mUserInfo; 
	State mState = State.unkown;
	YMCompat mCompat = new YMCompat(UserName,PassWord,this);
	
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
		Log.d("allen", "onServiceConnected");
		mCompat.setProjectId(XNW_ID);
		mCompat.setProvinceId(Country_id, Province_id);
		mCompat.doLogin();
		
		File root = this.getExternalFilesDir(null);
        File file = new File(root,"xnw_info");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            Log.d("allen", "xnw_info exist");
        }
        
        tarFile = file;
        Log.d("allen", "root:"+file.getAbsolutePath());
		
		SharedPreferences sp = this.getSharedPreferences("settings", MODE_PRIVATE );
	}


    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        return super.onUnbind(intent);
    }

    private void processAccessibilityEnvent(AccessibilityEvent event) {
		if(!enabled || !login){
			return;
		}
	    Log.d("allen", event.eventTypeToString(event.getEventType()));
	    
	    int type = event.getEventType();
	    String classname = event.getClassName().toString();
	    Log.d("allen", "classname: "+classname);
	    if (type == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
	    	if(classname.contains("activity.LoginActivity")){
	    	    if(mState != State.login){
	    		 mState = State.login;
	    		findAndPerformAction(PACKAGE+":id/btn_register");
	    		Log.d("allen", "mState = State.login");
	    	    }
	    		
	    	}else if(classname.contains("register.UserRegisterActivity")){
	    		if(mState == State.login || mState == State.fillnamed){
    	    		mState = State.register;
    	    		mUserInfo = new UserInfo();
    	    		mCompat.doGetNum();
	    		}
	    		Log.d("allen", "mState = State.register");
	    		
	    	}else if(classname.contains("android.app.Dialog")){
	    	    if(mState == State.register_filled){
	    	        mState = State.dialog;
	    		    findAndPerformAction(PACKAGE+":id/positive_btn");
	    	    }
	    	    Log.d("allen", "mState =State.dialog");
	    	}else if(classname.contains("register.IdentifyingCodeActivity")){
	    	    if(mState == State.dialog){
    	    	    mState = State.identify;
    	    	    mCompat.doGetCode();
	    	    }
	    	    
	    	    Log.d("allen", "mState =State.identify");
	    	}else if(classname.contains("register.UserNameFillInActivity")){
	    	    if(mState != State.fillname){
    	    	    mState = State.fillname;
    	    	    String name = Utils.generateName(5, 8);
    	    	    findAndPerformSetText(PACKAGE+":id/et_user_account",name);
    	    	    if(mUserInfo != null){
    	    	        mUserInfo.setUserName(name);
                        mUserInfo.doStore(tarFile);
    	    	    }
	    	    }
	    	    Log.d("allen", "mState = State.fillname");
	    	}else if(classname.contains("activity.MainActivity")){
	    	    if(mState != State.main){
    	    	    mState = State.main;
    	    	    
    	    	    mUserInfo = null;
    	    	    findAndPerformAction(PACKAGE+":id/rl_my");
	    	    }
	    	    
	    	    Log.d("allen", "mState = State.main");
//	    	    try{
//    	    	    Intent intent = new Intent();
//    	    	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    	    	    intent.setClassName(PACKAGE, PACKAGE+".activity.LoginActivity");
//    	    	    this.startActivity(intent);
//	    	    }catch(Exception e){
//	    	        e.fillInStackTrace();
//	    	        e.printStackTrace();
//	    	    }
	    	}
	    	
	    }
	    if (type == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ){
	        if(mState == State.register_filled){
	    		findAndPerformAction(PACKAGE+":id/btn_next_step");
	    	}else if( mState == State.identify_filled){
	    		findAndPerformAction(PACKAGE+":id/btn_submit");
	    	}else if( mState == State.fillname){
	    	    findAndPerformAction(PACKAGE+":id/btn_next");
	    	    mState = State.fillnamed;
	    	}else if(mState == State.main){
	    	    findAndPerformAction(PACKAGE+":id/sv_bg");
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
		if( nodes.size() > 0){
			
			AccessibilityNodeInfo node = nodes.get(0);
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
	    if( nodes.size() > 0){
			
			AccessibilityNodeInfo node = nodes.get(0);
			if (node.isEnabled()){
				node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);;
			}
		}
	}

    @Override
    public void onLogin(boolean success, String tokenn) {
        // TODO Auto-generated method stub
        login = success;
        Log.d("allen", "login:"+login);
    }

    @Override
    public void onGetPhoneNum(boolean success, String num) {
        // TODO Auto-generated method stub
        if(mState == State.register && success){

            findAndPerformSetText(PACKAGE+":id/et_mobile_or_email",num);
            String pwd = Utils.generatePwd(6, 10);
            mState =State.register_filled;
            findAndPerformSetText(PACKAGE+":id/et_pwd",pwd);
            if(mUserInfo != null){
                mUserInfo.setPhoneNum(num);
                mUserInfo.setPassWord(pwd);
            }
            
            Log.d("allen", "mState =State.register_filled");
        }else{
            mUserInfo = null;
        }
    }

    @Override
    public void onGetVerifyCode(boolean success, String code) {
        // TODO Auto-generated method stub
       if( mState == State.identify && success ){
           
           findAndPerformSetText(PACKAGE+":id/et_identifying_code",code);
           mState =State.identify_filled;
           Log.d("allen", "mState =State.identify_filled");
       }else{
           mUserInfo = null;
       }
    }
}
