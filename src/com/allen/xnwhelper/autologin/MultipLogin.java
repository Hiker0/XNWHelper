package com.allen.xnwhelper.autologin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class MultipLogin extends Activity {

    private final String TAG = "MultipLogin";
    private final String url = "http://xnw.com/user/log_in.php";
    private final String USER_AGENT = "Mozilla/5.0";
    
    String data = "act=login&password=123456&account=wangzhenjun%40xnw.com&type=login_info&remember_me=1";
    String home = "http://www.xnw.com/";
            
    private List<String> mCookies;
    private HttpURLConnection conn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
        CookieHandler.setDefault(new CookieManager());
        MyTask mSyncTask = new MyTask();
        mSyncTask.execute();
        
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    
    class MyTask extends AsyncTask<String, Integer, String>{
        

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String page = null;
                String postParams = null;
                String result = null;
                
                page = GetPageContent(url);
                //postParams = getFormParams(page, "wangzhenjun@xnw.com", "123456");
                // 2. Construct above post's content and then send a POST request for
                // authentication
                sendPost(url, data);
                // 3. success then go to gmail.            
                result = GetPageContent(home);
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        
        
    }
    
    private void sendPost(String url, String postParams) throws Exception {
        Log.d(TAG,"sendPost: begin>>" + url+"::"+postParams);
        URL obj = new URL("http://www.xnw.com/user/log_in.php?act=do_login");
        conn = (HttpURLConnection) obj.openConnection();

        // Acts like a browser
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Host", "www.xnw.com");
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept",
            "application/json, text/javascript, */*; q=0.01");
        //conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        for (String cookie : mCookies) {
            conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
        }
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Referer", "http://www.xnw.com/user/log_in.php?next_url=/");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));

        conn.setDoOutput(true);
        conn.setDoInput(true);

        // Send post request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        Log.d(TAG,"\nSending 'POST' request to URL : " + url);
        Log.d(TAG,"Post parameters : " + postParams);
        Log.d(TAG,"Response Code : " + responseCode);

        BufferedReader in = 
                 new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        
        StringBuffer response = new StringBuffer();
       
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        
        setCookies(conn.getHeaderFields().get("Set-Cookie"));
        
        Log.d(TAG,"response : " + response);
        Log.d(TAG,"sendPost: end>>" +responseCode);

      }

      private String GetPageContent(String url) throws Exception {
        Log.d(TAG,"GetPageContent: begin>>" + url);

        URL obj = new URL(url);
        conn = (HttpURLConnection) obj.openConnection();

        // default is GET
        conn.setRequestMethod("GET");

        conn.setUseCaches(false);

        // act like a browser
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        if (mCookies != null) {
            for (String cookie : mCookies) {
                conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }
        }
        int responseCode = conn.getResponseCode();
        Log.d(TAG,"\nSending 'GET' request to URL : " + url);
        Log.d(TAG,"Response Code : " + responseCode);

        BufferedReader in = 
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Get the response cookies
        setCookies(conn.getHeaderFields().get("Set-Cookie"));

        Log.d(TAG,"GetPageContent: end>>" + response);
        return response.toString();

      }

      public String getFormParams(String html, String username, String password)
            throws UnsupportedEncodingException {

         Log.d(TAG,"getFormParams begin:");

        Document doc = Jsoup.parse(html);

        // Google form id
        Element loginform = doc.getElementById("login_form");
        Elements inputElements = loginform.getElementsByTag("input");
        List<String> paramList = new ArrayList<String>();
        for (Element inputElement : inputElements) {
            String key = inputElement.attr("name");
            String value = inputElement.attr("value");

            if (key.equals("account"))
                value = username;
            else if (key.equals("password"))
                value = password;
            paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
        }

        // build parameters list
        StringBuilder result = new StringBuilder();
        for (String param : paramList) {
            if (result.length() == 0) {
                result.append(param);
            } else {
                result.append("&" + result);
            }
        }
        Log.d(TAG,"getFormParams end:"+result);
        return result.toString();
      }

      public List<String> getCookies() {
        return mCookies;
      }

      public void setCookies(List<String> cookies) {
         if( cookies == null){
             return;
         }
        mCookies = cookies;
        
        for (String cookie : mCookies) {
            Log.d(TAG,"cookie{{"+cookie);
        }
      }



}
