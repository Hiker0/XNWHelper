package com.allen.xnwhelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.allen.xnwhelper.YMCompat.CompleteListener;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;

public class RegisterAccessibilityService extends AccessibilityService
        implements CompleteListener {

    final static String TAG = "XNWHelper";
    boolean enabled = true;
    boolean login = false;
    static String PACKAGE = "com.xnw.qun";
    static String UserName = "wzj744717727";
    static String PassWord = "qiaohui521";
    static String XNW_ID = "3177";
    static String Country_id = "1";
    static String Province_id = "370000";

    enum State {
        unkown, welcome, login, register, register_filled, dialog, identify, identify_filled, fillname, fillnamed, main
    };

    File tarFile;
    UserInfo mUserInfo;
    State mState = State.unkown;
    YMCompat mCompat = new YMCompat(UserName, PassWord, this);
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;
    private ViewGroup mFloatView;
    private TextView mStateView;
    private TextView mInfoView;
    private int statusBarHeight;
    
    
    
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        initFloatTool();
    }
    private void initFloatTool(){
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mWindowParams = new WindowManager.LayoutParams();
        
        statusBarHeight = getStatusBarHeight();
        mFloatView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.float_info, null);
        mStateView = (TextView) mFloatView.findViewById(R.id.state);
        mInfoView = (TextView) mFloatView.findViewById(R.id.info);
    }

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
        Log.d(TAG, "onServiceConnected");
        mCompat.setProjectId(XNW_ID);
        mCompat.setProvinceId(Country_id, Province_id);
        
        addFloatTool(0,0);
        setState(State.unkown);
        
        File root = this.getExternalFilesDir(null);
        File file = new File(root, "xnw_info");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "xnw_info exist");
        }

        tarFile = file;
        mCompat.doLogin();
        showInfo("正在登录易码账户");
        Log.d(TAG, "root:" + file.getAbsolutePath());
;

        SharedPreferences sp = this.getSharedPreferences("settings",
                MODE_PRIVATE);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        Log.d(TAG, "onUnbind");
        hideFloatTool();
        return super.onUnbind(intent);
    }

    void showInfo(String info){
        if(mInfoView != null){
            mInfoView.setText(info);
        }
    }
    
    void setState(State state) {
        mState = state;
        Log.d(TAG, "mState=" + stateToString(state));
        if (mStateView != null) {
            mStateView.setText(stateToString(state));
        }
    }

    String stateToString(State state) {
        StringBuilder sb = new StringBuilder();
        switch (state) {
        case unkown:
            sb.append("unkown");
            break;
        case welcome:
            sb.append("welcome");
            break;
        case login:
            sb.append("login");
            break;
        case register:
            sb.append("register");
            break;
        case register_filled:
            sb.append("register_filled");
            break;
        case dialog:
            sb.append("dialog");
            break;
        case identify:
            sb.append("identify");
            break;
        case identify_filled:
            sb.append("identify_filled");
            break;
        case fillname:
            sb.append("fillname");
            break;
        case fillnamed:
            sb.append("fillnamed");
            break;
        case main:
            sb.append("main");
            break;
        default:
            sb.append("unfound");
        }

        return sb.toString();
    }

    private void addFloatTool(int x, int y) {
        Log.d(TAG, "addFloatTool");
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON 
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD 
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        mWindowParams.gravity = Gravity.RIGHT | Gravity.TOP;
        mWindowParams.x = x - mFloatView.getWidth() / 2;
        mWindowParams.y = y - statusBarHeight - mFloatView.getHeight() / 2;
        mWindowParams.format = PixelFormat.RGBA_8888;
        mWindowManager.addView(mFloatView, mWindowParams);
    }

    private void hideFloatTool() {
        Log.d(TAG, "hideFloatTool");
        if (mWindowManager != null) {
            mWindowManager.removeView(mFloatView);
        }
    }
    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }
    private void processAccessibilityEnvent(AccessibilityEvent event) {
        if (!enabled || !login) {
            return;
        }
        Log.d(TAG, event.eventTypeToString(event.getEventType()));

        int type = event.getEventType();
        String classname = event.getClassName().toString();
        Log.d(TAG, "classname: " + classname);
        if (type == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (classname.contains("activity.LoginActivity")
                    || classname.contains("login.LoginActivity")) {
                if (mState != State.login) {
                    setState(State.login);
                    showInfo("开始注册");
                    findAndPerformAction(PACKAGE + ":id/btn_register");
                    
                }

            } else if (classname.contains("register.UserRegisterActivity")) {
                if (mState == State.login || mState == State.fillnamed) {
                    setState(State.register);
                    mUserInfo = new UserInfo();
                    mCompat.doGetNum();
                    showInfo("正在获取手机号码。。");
                }
                Log.d(TAG, "mState = State.register");

            } else if (classname.contains("android.app.Dialog")) {
                if (mState == State.register_filled) {
                    setState(State.dialog);
                    findAndPerformAction(PACKAGE + ":id/positive_btn");
                }
                Log.d(TAG, "mState =State.dialog");
            } else if (classname.contains("register.IdentifyingCodeActivity")) {
                if (mState == State.dialog) {
                    setState(State.identify);
                    mCompat.startGetCode();
                    showInfo("正在获取验证码。。");
                }
            } else if (classname.contains("register.UserNameFillInActivity")) {
                if (mState == State.identify) {
                    setState(State.fillname);
                    String name = Utils.generateName(5, 8);
                    findAndPerformSetText(PACKAGE + ":id/et_user_account",
                            name);
                    if (mUserInfo != null) {
                        mUserInfo.setUserName(name);
                        mUserInfo.doStore(tarFile);
                    }
                }
            } else if (classname.contains("activity.MainActivity")) {
                if (mState != State.main) {
                    setState(State.main);
                    mUserInfo = null;
                    findAndPerformAction(PACKAGE + ":id/rl_my");
                }

                Log.d(TAG, "mState = State.main");
                showInfo("手动注销");
                // try{
                // Intent intent = new Intent();
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // intent.setClassName(PACKAGE,
                // PACKAGE+".activity.LoginActivity");
                // this.startActivity(intent);
                // }catch(Exception e){
                // e.fillInStackTrace();
                // e.printStackTrace();
                // }
            }

        }
        if (type == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            if (mState == State.register_filled) {
                findAndPerformAction(PACKAGE + ":id/btn_next_step");
            } else if (mState == State.identify_filled) {
                findAndPerformAction(PACKAGE + ":id/btn_submit");
            } else if (mState == State.fillname) {
                findAndPerformAction(PACKAGE + ":id/btn_next");
                mState = State.fillnamed;
            } else if (mState == State.main) {
                findAndPerformAction(PACKAGE + ":id/sv_bg");
            }
        }
    }

    private void findAndPerformAction(String text) {
        if (getRootInActiveWindow() == null) {
            Log.d(TAG, "getRootInActiveWindow null");
            return;
        }
        List<AccessibilityNodeInfo> nodes = getRootInActiveWindow()
                .findAccessibilityNodeInfosByViewId(text);
        Log.d(TAG, "find nodes: " + nodes.size());
        if (nodes.size() > 0) {

            AccessibilityNodeInfo node = nodes.get(0);
            if (node.isEnabled()) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    private void findAndPerformSetText(String id, String text) {
        if (getRootInActiveWindow() == null) {
            Log.d(TAG, "getRootInActiveWindow null");
            return;
        }
        List<AccessibilityNodeInfo> nodes = getRootInActiveWindow()
                .findAccessibilityNodeInfosByViewId(id);
        Log.d(TAG, "find nodes: " + nodes.size());

        Bundle arguments = new Bundle();
        arguments.putCharSequence(
                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                text);
        if (nodes.size() > 0) {

            AccessibilityNodeInfo node = nodes.get(0);
            if (node.isEnabled()) {
                node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,
                        arguments);
                ;
            }
        }
    }

    @Override
    public void onLogin(boolean success, String tokenn) {
        // TODO Auto-generated method stub
        if(success){
            login = success;
            showInfo("登录成功");
        }else{
            showInfo("登录失败");
        }
        Log.d(TAG, "login:" + login);
    }

    @Override
    public void onGetPhoneNum(boolean success, String num) {
        // TODO Auto-generated method stub
        if (mState == State.register && success) {
            showInfo("获取手机号码成功");
            findAndPerformSetText(PACKAGE + ":id/et_mobile_or_email", num);
            String pwd = Utils.generatePwd(6, 10);
            setState(State.register_filled);
            findAndPerformSetText(PACKAGE + ":id/et_pwd", pwd);
            if (mUserInfo != null) {
                mUserInfo.setPhoneNum(num);
                mUserInfo.setPassWord(pwd);
            }
        } else {
            mUserInfo = null;
            showInfo("获取手机号码失败");
        }
    }

    @Override
    public void onGetVerifyCode(boolean success, String code) {
        // TODO Auto-generated method stub
        if (mState == State.identify && success) {
            showInfo("获取验证码成功");
            findAndPerformSetText(PACKAGE + ":id/et_identifying_code", code);
            setState(State.identify_filled);
        } else {
            mUserInfo = null;
            showInfo("获取验证码失败");
        }
    }
}
