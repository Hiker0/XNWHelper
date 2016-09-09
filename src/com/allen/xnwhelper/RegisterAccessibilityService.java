package com.allen.xnwhelper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.allen.xnwhelper.YMCompat.CompleteListener;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
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
    final static String PACKAGE = "com.xnw.qun";
    final static String PACKAGE_ID = "com.xnw.qun:id/";
    final static int CLICK_DELAY_MILLS = 200;
    static ArrayList<String> sGetedNums;

    enum State {
        unkown, welcome, login, register, register_filled, dialog, identify, identify_filled, fillname, fillnamed, main
    };

    File tarFile;
    UserInfo mUserInfo;
    State mState = State.unkown;
    YMCompat mCompat;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;
    private ViewGroup mFloatView;
    private TextView mStateView;
    private TextView mInfoView;
    private TextView mCountView;
    private int statusBarHeight;
    private int mCount = 0;
    private HashMap<Integer,String> mErrors;
    private Handler mHandler;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.d(TAG, "onCreate");
        if (sGetedNums == null) {
            sGetedNums = new ArrayList<String>();
        }

        mHandler = new Handler();
        
        initFloatTool();
        initErrors();
    }

    private void initErrors(){
        String[] erros = getResources().getStringArray(R.array.errors);
        int[] codes = getResources().getIntArray(R.array.error_code);
        
        mErrors = new HashMap<Integer,String>();
        for(int i=0;i< erros.length;i++){
            mErrors.put(codes[i], erros[i]);
        }
        
        mErrors.put(-1, "服务器端返回错误");
    }
    private void initFloatTool() {
        mWindowManager = (WindowManager) getSystemService(
                Context.WINDOW_SERVICE);
        mWindowParams = new WindowManager.LayoutParams();

        statusBarHeight = getStatusBarHeight();
        mFloatView = (ViewGroup) LayoutInflater.from(this)
                .inflate(R.layout.float_info, null);
        mStateView = (TextView) mFloatView.findViewById(R.id.state);
        mInfoView = (TextView) mFloatView.findViewById(R.id.info);
        mCountView = (TextView) mFloatView.findViewById(R.id.count);
    }

    void releaseNum(String num) {
        Log.d(TAG, "releaseNum:" +num);
        Log.d(TAG, "sGetedNums count:" +sGetedNums.size());
        for (String number : sGetedNums) {
            if (number.equals(num)) {
                sGetedNums.remove(number);
                mCompat.doReleaseNum(num);
                return;
            }
        }
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
        mCount = 0;
        if (mCompat != null && mCompat.getToken() != null) {
            mCompat.doReleaseAllNum();
        }

        mCompat = new YMCompat(mHandler, this);
        mCompat.doLogin();

        addFloatTool(0, 0);
        setState(State.unkown);
        showCount(mCount);

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
        if (mCompat.getToken() != null) {
            mCompat.doReleaseAllNum();
            sGetedNums.clear();
        }

        mCompat.doLogout();
        return super.onUnbind(intent);
    }

    void showCount(int cnt) {
        Log.d(TAG, "Count="+cnt);
        mCountView.setText("( " + cnt + " )");
    }

    void showInfo(String info) {
        if (mInfoView != null) {
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
                    mHandler.postDelayed(new ViewClicker(PACKAGE_ID + "btn_register"), CLICK_DELAY_MILLS);

                }

            } else if (classname.contains("register.UserRegisterActivity")) {
                if (mState == State.unkown || mState == State.login || mState == State.main) {
                    setState(State.register);
                    mUserInfo = new UserInfo();
                    mCompat.doGetNum();
                    showInfo("正在获取手机号码。。");
                }
                Log.d(TAG, "mState = State.register");

            } else if (classname.contains("android.app.Dialog")) {
                if (mState == State.register_filled) {
                    setState(State.dialog);
                    mHandler.postDelayed(new ViewClicker(PACKAGE_ID + "positive_btn"), CLICK_DELAY_MILLS);
                }
                Log.d(TAG, "mState =State.dialog");
            } else if (classname.contains("register.IdentifyingCodeActivity")) {
                if (mState == State.dialog) {
                    setState(State.identify);
                    mCompat.startGetCode();
                    showInfo("正在获取验证码。。");
                }
            } else if (classname.contains("register.UserNameFillInActivity")) {

                setState(State.fillname);
                String name = Utils.generateName(5, 12);
                findAndPerformSetText(PACKAGE_ID + "et_user_account", name);

            } else if (classname.contains("activity.main.MainActivity")) {
                if (mState != State.main) {
                    setState(State.main);
                    if (mUserInfo != null) {
                        mUserInfo.doStore(tarFile);
                        mCount += 1;
                        showCount(mCount);
                    }
//                    mHandler.postDelayed(new ViewClicker(PACKAGE_ID + "rl_my"), CLICK_DELAY_MILLS);
                }

                showInfo("手动注销");
                try{
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setClassName("com.xnw.qun", "com.xnw.qun.activity.register.UserRegisterActivity");
                    this.startActivity(intent);
                }catch(Exception e){
                    Log.d(TAG,e.toString());
                }
            }

        }
        if (type == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            if (mState == State.register_filled) {
                mHandler.postDelayed(new ViewClicker(PACKAGE_ID + "btn_next_step"), CLICK_DELAY_MILLS);
            } else if (mState == State.identify_filled) {
                mHandler.postDelayed(new ViewClicker(PACKAGE_ID + "btn_submit"), CLICK_DELAY_MILLS);
            } else if (mState == State.fillname) {
                mHandler.postDelayed(new ViewClicker(PACKAGE_ID + "btn_next"), CLICK_DELAY_MILLS);
                mState = State.fillnamed;
            }
        }
    }



    class ViewClicker implements Runnable{
        String mName;
        ViewClicker(String name){
            mName = name;
        }
        
        @Override
        public void run() {
            // TODO Auto-generated method stub
            AccessibilityNodeInfo info = getRootInActiveWindow();
            if (info == null) {
                Log.d(TAG, "getRootInActiveWindow null");
                return;
            }
            List<AccessibilityNodeInfo> nodes = info
                    .findAccessibilityNodeInfosByViewId(mName);

            if (nodes != null && nodes.size() > 0) {
                Log.d(TAG, "find nodes: " + nodes.size());
                AccessibilityNodeInfo node = nodes.get(0);
                if (node.isEnabled()) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
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
        if (success) {
            login = success;
            showInfo("登录成功");
        } else {
            showInfo("登录失败");
        }
        Log.d(TAG, "login:" + login);
    }

    @Override
    public void onGetPhoneNum(boolean success, String num) {
        // TODO Auto-generated method stub
        if (mState == State.register && success) {
            showInfo("获取手机号码成功");
            findAndPerformSetText(PACKAGE_ID + "et_mobile_or_email", num);
            String pwd = Utils.generatePwd(6, 10);
            findAndPerformSetText(PACKAGE_ID + "et_pwd", pwd);
            setState(State.register_filled);
            if (mUserInfo != null) {
                mUserInfo.setPhoneNum(num);
                mUserInfo.setPassWord(pwd);
                sGetedNums.add(num);
                
            }

        } else if(!success){
            int errorCode = -1;
            if(num != null){
                errorCode = Integer.parseInt(num);
            }
            String error = mErrors.get(errorCode);
            
            showInfo("获取手机号码失败:"+error);
            mUserInfo = null;
        }
    }

    @Override
    public void onGetVerifyCode(boolean success, String code) {
        // TODO Auto-generated method stub
        if (mUserInfo != null) {
            releaseNum(mUserInfo.phoneNum);
        }
        if (mState == State.identify && success) {
            showInfo("获取验证码成功");
            findAndPerformSetText(PACKAGE_ID + "et_identifying_code", code);
            setState(State.identify_filled);

        } else {
            int errorCode = -1;
            if(code != null){
                errorCode = Integer.parseInt(code);
            }
            String error = mErrors.get(errorCode);
            
            mUserInfo = null;
            showInfo("获取验证码失败:" + error);
            mState =State.unkown;
            
            try{
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setClassName("com.xnw.qun", "com.xnw.qun.activity.register.UserRegisterActivity");
                this.startActivity(intent);
            }catch(Exception e){
                Log.d(TAG,e.toString());
            }
        }

    }

    @Override
    public void onLogout() {
        // TODO Auto-generated method stub
        showInfo("登录超时，请重新登录");
    }
}
