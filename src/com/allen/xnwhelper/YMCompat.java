package com.allen.xnwhelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class YMCompat {
    static final String TAG = "YMCompat";
    private final String USER_AGENT = "Mozilla/5.0";

    final static String UserName = "wzj744717727";
    final static String PassWord = "qiaohui521";
    final static String XNW_ID = "3177";
    final static String Country_id = "1";
    final static String Province_id = "370000";
    final static long TIMEOUT_MILLS=600*1000;
    
//    private String UserName;
//    private String PassWord;
//    private String XNW_ID;
//    private String Country_id;
//    private String Province_id;
    private String token = null;
    private HttpURLConnection conn;
    private CompleteListener mListener;
    private String phoneNumber;
    private String verifyCode;
    Handler mHandler ;
    int count = 0;
    boolean waitingCode = false;
    
    Runnable timeoutRunnable = new Runnable(){

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if(mListener != null){
                mListener.onLogout();  
            }
        }
        
    };
    
    void freshTimer(){
        mHandler.removeCallbacks(timeoutRunnable);
        mHandler.postDelayed(timeoutRunnable, TIMEOUT_MILLS);
    }
    
    public interface CompleteListener {
        void onLogin(boolean success, String tokenn);
        void onLogout();
        void onGetPhoneNum(boolean success, String num);

        void onGetVerifyCode(boolean success, String code);
    }

    YMCompat(Handler handler, CompleteListener listener) {
        mHandler = handler;
        mListener = listener;
    }

    void setProjectId(String id) {
//        XNW_ID = id;
    }

    void setProvinceId(String country, String province) {
//        Country_id = country;
//        Province_id = province;
    }

    String getToken() {
        return token;
    }

    void doLogout() {
        if (token == null) {
            return;
        }
        // http://api.51ym.me/UserInterface.aspx?action=logout&token=token
        final AsyncTask<String, Integer, String> myTask = new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {
                // TODO Auto-generated method stub
                return doGet(
                        "http://api.51ym.me/UserInterface.aspx?action=logout&token="
                                + token);
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                Log.d(TAG, "doLogout onPostExecute: " + result);
                boolean success = false;
                timeoutRunnable.run();

            }
        };

        myTask.execute("");
        Log.d(TAG, "doLogout");
    }

    void doLogin() {
        final AsyncTask<String, Integer, String> myTask = new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {
                // TODO Auto-generated method stub
                freshTimer();
                return doGet(
                        "http://api.51ym.me/UserInterface.aspx?action=login&username="
                                + UserName + "&password=" + PassWord);
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                Log.d(TAG, "doLogin onPostExecute: " + result);
                boolean success = false;
                token = null;
                if (result != null) {
                    String[] str = result.split("\\|");
                    if (str.length >= 2) {
                        Log.d(TAG, "doLogin onPostExecute: " + str[0] + "::"
                                + str[1]);
                        if (str[0].equals("success")) {
                            token = str[1];
                            success = true;
                        }
                    }
                }
                mListener.onLogin(success, token);

            }
        };

        myTask.execute("");
        Log.d(TAG, "doLogin");
    }

    void doGetNum() {
        if (token == null) {
            mListener.onGetPhoneNum(false, null);
            return;
        }
        final AsyncTask<String, Integer, String> myTask = new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {
                // TODO Auto-generated method stub
                freshTimer();
                return doGet(
                        "http://api.51ym.me/UserInterface.aspx?action=getmobile"
                                + "&itemid=" + XNW_ID + "&country=" + Country_id
                                + "&province=" + Province_id + "&token="
                                + token);
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                Log.d(TAG, "doGetNum onPostExecute: " + result);
                boolean success = false;
                phoneNumber = null;
                if (result != null) {
                    String[] str = result.split("\\|");
                    if (str.length >= 2) {
                        Log.d(TAG, "doGetNum onPostExecute: " + str[0] + "::"
                                + str[1]);
                        if (str[0].equals("success")) {
                            phoneNumber = str[1];
                            success = true;
                            mListener.onGetPhoneNum(success, phoneNumber);
                        }
                    }
                }

                mListener.onGetPhoneNum(success, result);
            }
        };

        myTask.execute("");
        Log.d(TAG, "doGetNum");
    }

    void doReleaseNum(final String num) {

        final AsyncTask<String, Integer, String> myTask = new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {
                // TODO Auto-generated method stub
                freshTimer();
                return doGet(
                        "http://api.51ym.me/UserInterface.aspx?action=release"
                                + "&mobile=" + num + "&itemid=" + XNW_ID
                                + "&token=" + token);
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                Log.d(TAG, "doReleaseNum onPostExecute: " + result);
                boolean success = false;
                phoneNumber = null;
                if (result != null) {
                    String[] str = result.split("\\|");
                    if (str.length >= 2) {
                        Log.d(TAG, "doReleaseNum onPostExecute: " + str[0]
                                + "::" + str[1]);
                        if (str[0].equals("success")) {
                            phoneNumber = str[1];
                            success = true;
                        }
                    }
                }

            }
        };

        myTask.execute("");
        Log.d(TAG, "doReleaseNum");
    }

    void doReleaseAllNum() {

        final AsyncTask<String, Integer, String> myTask = new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {
                // TODO Auto-generated method stub
                freshTimer();
                return doGet(
                        "http://api.51ym.me/UserInterface.aspx?action=releaseall"
                                + "&token=" + token);
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                Log.d(TAG, "doReleaseNum onPostExecute: " + result);
                boolean success = false;
                phoneNumber = null;
                if (result != null) {
                    String[] str = result.split("\\|");
                    if (str.length >= 2) {
                        Log.d(TAG, "doReleaseNum onPostExecute: " + str[0]
                                + "::" + str[1]);
                        if (str[0].equals("success")) {
                            phoneNumber = str[1];
                            success = true;
                        }
                    }
                }
            }
        };

        myTask.execute("");
        Log.d(TAG, "doReleaseNum");
    }

    AsyncTask<String, Integer, String> codeTask = null;

    Runnable getCodeRunable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Log.d(TAG, "getCodeRunable count:" + count);
            count++;
            if (count <= 20) {
                doGetCode();
            } else {
                count = 0;
                mListener.onGetVerifyCode(false, null);
                waitingCode = false;
                if (!codeTask.isCancelled()) {
                    codeTask.cancel(true);
                }
                codeTask = null;
            }
        }
    };

    public String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    void startGetCode() {
        count = 0;
        doGetCode();
    }

    void doGetCode() {
        if (token == null) {
            mListener.onGetVerifyCode(false, null);
            return;
        }
        if (waitingCode) {
            return;
        }
        codeTask = new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {
                // TODO Auto-generated method stub
                freshTimer();
                return doGet(
                        "http://api.51ym.me/UserInterface.aspx?action=getsms"
                                + "&mobile=" + phoneNumber + "&itemid=" + XNW_ID
                                + "&token=" + token);
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                Log.d(TAG, "doGetCode onPostExecute: " + result);
                verifyCode = null;
                waitingCode = false;
                if (result != null) {
                    String[] str = result.split("\\|");
                    if (str.length >= 2) {
                        Log.d(TAG, "doGetCode onPostExecute: " + str[0] + "::"
                                + str[1]);
                        if (str[0].equals("success")) {

                            verifyCode = getNumbers(str[1]);
                            mListener.onGetVerifyCode(true, verifyCode);
                            return;
                        }
                    }
                }

                mHandler.postDelayed(getCodeRunable, 3000);
            }

        };

        codeTask.execute("");
        waitingCode = true;
        Log.d(TAG, "doGetCode");
    }

    String doGet(String surl) {
        Log.d(TAG, "doGet:" + surl);
        try {
            URL url = new URL(surl);
            conn = (HttpURLConnection) url.openConnection();
            // Acts like a browser
            conn.setRequestMethod("GET");

            conn.setUseCaches(false);

            // act like a browser
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "\nSending 'GET' request to URL : " + url);
            Log.d(TAG, "Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            conn.disconnect();
            // Get the response cookies

            Log.d(TAG, "GetPageContent: end>>" + response);
            return response.toString().intern();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
