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
    static final String TAG = "allen";
    private final String USER_AGENT = "Mozilla/5.0";
    
    private String UserName ;
    private String PassWord;
    private String XNW_ID;
    private String Country_id;
    private String Province_id;
    private String token = null;
    private HttpURLConnection conn;
    private CompleteListener mListener;
    private String phoneNumber;
    private String verifyCode;
    
    public interface CompleteListener{
        void onLogin(boolean  success,String tokenn);
        void onGetPhoneNum(boolean  success,String num);
        void onGetVerifyCode(boolean  success,String code);
    }
    
    YMCompat(String user,String pwd, CompleteListener listener ){
        UserName = user;
        PassWord = pwd;
        mListener = listener;
    }
    
    void setProjectId(String id){
        XNW_ID = id;
    }
    
    void setProvinceId(String country,String province){
        Country_id = country;
        Province_id = province;
    }
    
    String getToken(){
        return token;
    }
    
    
    
    void doLogout(){
        if(token == null){
            return;
        }
        //http://api.51ym.me/UserInterface.aspx?action=logout&token=token
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
            if (result != null) {
                String[] str = result.split("\\|");
                if (str.length >= 2) {
                    Log.d(TAG, "doLogin onPostExecute: " + str[0] + "::"
                            + str[1]);
                    if (str[0].equals("success")) {
                        token = null;
                        success = true;
                    }
                }
            }
            //mListener .onLogin(success, token);
            
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
                mListener .onLogin(success, token);
                
            }
        };

        myTask.execute("");
        Log.d(TAG, "doLogin");
    }


    void doGetNum() {

        final AsyncTask<String, Integer, String> myTask = new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {
                // TODO Auto-generated method stub
                return doGet(
                        "http://api.51ym.me/UserInterface.aspx?action=getmobile"
                                +"&itemid="+ XNW_ID 
                                +"&country="+Country_id
                                +"&province="+Province_id
                                + "&token=" + token);
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
                        }
                    }
                }
               
                mListener.onGetPhoneNum(success, phoneNumber);
            }
        };

        myTask.execute("");
        Log.d(TAG, "doGetNum");
    }

    AsyncTask<String, Integer, String> codeTask = null;

    Handler mHandler = new Handler();
    int count=0;
    boolean waitingCode = false;
    Runnable getCodeRunable = new Runnable(){

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Log.d(TAG, "getCodeRunable count:"+count);
            count++;
            if(count <= 20){
                doGetCode();
            }else{
                mListener.onGetVerifyCode(false, null);
                waitingCode = false;
                if(!codeTask.isCancelled()){
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
    
    void doGetCode() {
        if(waitingCode){
            return;
        }
            codeTask = new AsyncTask<String, Integer, String>() {

                @Override
                protected String doInBackground(String... params) {
                    // TODO Auto-generated method stub
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
                            Log.d(TAG, "doGetCode onPostExecute: " + str[0]
                                    + "::" + str[1]);
                            if (str[0].equals("success")) {

                                verifyCode = getNumbers(str[1]);
                                mListener.onGetVerifyCode(true, verifyCode);
                                return;
                            }
                        }
                    }
                    
                    mHandler.postDelayed(getCodeRunable,5000);
                }

            };

            codeTask.execute("");
            waitingCode = true;
            Log.d(TAG, "doGetNum");
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
