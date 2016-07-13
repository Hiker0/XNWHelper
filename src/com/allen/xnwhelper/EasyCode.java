package com.allen.xnwhelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EasyCode extends Activity {
    static final String TAG = "allen";
    static String UserName = "wzj744717727";
    static String PassWord = "qiaohui521";
    static String XNW_ID = "3177";
    static String Country_id = "1";
    static String Province_id = "370000";
    
    private final String USER_AGENT = "Mozilla/5.0";
    private String token = null;
    private String number = null;
    private String code = null;
    TextView mStateView;
    TextView mNumView;
    TextView mCodeView;
    boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.easy_code);
        Button button = (Button) this.findViewById(R.id.login_btn);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                doLogin();
            }
        });

        mStateView = (TextView) this.findViewById(R.id.state);

        Button num_button = (Button) this.findViewById(R.id.getnum_btn);
        num_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                doGetNum();
            }
        });

        mNumView = (TextView) this.findViewById(R.id.num);

        Button code_button = (Button) this.findViewById(R.id.getcode_btn);
        code_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                code = null;
                count = 0;
                doGetCode();
            }
        });

        mCodeView = (TextView) this.findViewById(R.id.code);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    private HttpURLConnection conn;

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
                if (result != null) {
                    String[] str = result.split("\\|");
                    if (str.length >= 2) {
                        Log.d(TAG, "doLogin onPostExecute: " + str[0] + "::"
                                + str[1]);
                        if (str[0].equals("success")) {
                            token = str[1];
                            mStateView
                                    .setText("state=" + str[0] + "\n" + token);
                            return;
                        }
                    }
                }
                token = null;
                mStateView.setText("");
            }

        };

        myTask.execute("");
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
                if (result != null) {
                    String[] str = result.split("\\|");
                    if (str.length >= 2) {
                        Log.d(TAG, "doGetNum onPostExecute: " + str[0] + "::"
                                + str[1]);
                        if (str[0].equals("success")) {
                            number = str[1];
                            mNumView.setText("state=" + str[0] + "\n" + number);
                            return;
                        }
                    }
                }
                number = null;
                mNumView.setText("");
            }

        };

        myTask.execute("");
    }

    AsyncTask<String, Integer, String> codeTask = null;

    Handler mHandler = new Handler();
    int count=0;
    Runnable getCodeRunable = new Runnable(){

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Log.d(TAG, "getCodeRunable count:"+count);
            count++;
            if(count <= 20){
                doGetCode();
            }
        }
        
    };
    
    
    void doGetCode() {
        // while(code == null){

        if (!running) {
            codeTask = new AsyncTask<String, Integer, String>() {

                @Override
                protected String doInBackground(String... params) {
                    // TODO Auto-generated method stub
                    return doGet(
                            "http://api.51ym.me/UserInterface.aspx?action=getsms"
                                    + "&mobile=" + number + "&itemid=" + XNW_ID
                                    + "&token=" + token);
                }

                @Override
                protected void onPostExecute(String result) {
                    // TODO Auto-generated method stub
                    Log.d(TAG, "doGetCode onPostExecute: " + result);
                    running = false;
                    if (result != null) {
                        String[] str = result.split("\\|");
                        if (str.length >= 2) {
                            Log.d(TAG, "doGetCode onPostExecute: " + str[0]
                                    + "::" + str[1]);
                            if (str[0].equals("success")) {
                                code = str[1];
                                mCodeView.setText(
                                        "state=" + str[0] + "\n" + code);
                                return;
                            }
                        }
                    }
                    code = null;
                    mCodeView.setText("");
                    mHandler.postDelayed(getCodeRunable,5000);
                }

            };

            codeTask.execute("");
            running = true;
        }
        // }
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
