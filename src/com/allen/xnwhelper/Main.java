package com.allen.xnwhelper;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.AtomicFile;
import android.util.Log;

public class Main extends Activity {
    File tarFile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.button_test);
		File root = this.getExternalFilesDir(null);
		File file = new File(root,"xnw_info");
		if(!file.exists()){
		    try {
		        file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		}
		
		tarFile = file;
		Log.d("allen", "root:"+root.getAbsolutePath());
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("allen", "onResume");
		
		for (int i=0;i<100;i++){
		    String pwd = Utils.generatePwd(6, 10);
		    Log.d("allen","pwd:"+pwd);
		    UserInfo info = new UserInfo("12144e23",pwd);
		    info.doStore(tarFile);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	
}
