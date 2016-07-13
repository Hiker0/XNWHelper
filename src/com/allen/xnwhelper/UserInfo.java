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
	

	UserInfo(String name, String pwd){
	    username = name;
	    password = pwd;
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
	        out.write(username+"    "+password+"\n");
	        out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
	}
	
}
