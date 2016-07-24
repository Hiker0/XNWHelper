package com.allen.xnwhelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.util.AtomicFile;

public class UserInfo {
	String username = null;
	String password = null;
	String phoneNum = null;
	
	UserInfo(){
	    
	}
	UserInfo(String name, String pwd){
	    phoneNum = name;
	    password = pwd;
	}
	
	public void setPassWord(String pwd){
	    password = pwd;
	}
	
	public void setUserName(String name){
	    username = name;
	}
	
	public void setPhoneNum(String num){
	    phoneNum = num;
	}
	public static boolean checkName(String name){
		return true;
	}
	
	public static boolean checkPassword(String pwd){
		return true;
	}
	
	public void doStore(File file){
	    BufferedWriter out = null;  
	    try {
	        FileOutputStream os = new FileOutputStream(file,true);
	        out = new BufferedWriter(new OutputStreamWriter(os));
	        out.write(phoneNum+"    "+password +"\n");
	        out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
	}
	
}
