package com.allen.xnwhelper;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends Activity {
    File tarFile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.button_test);
		
		Button btn = (Button) this.findViewById(R.id.button1);
		btn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.setAction("com.xnw.qun.activity.LoginActivity");
                //intent.addCategory("android.intent.category.DEFAULT");
                intent.setClassName("com.xnw.qun", "com.xnw.qun.activity.SetActivity");
                SettingsActivity.this.startActivity(intent);
            }
        });
		
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
		
//		for (int i=0;i<100;i++){
//		    String pwd = Utils.generatePwd(6, 10);
//		    Log.d("allen","pwd:"+pwd);
//		    UserInfo info = new UserInfo("13816456378",pwd);
//		    info.setUserName(Utils.generateName(5, 8));
//		    info.doStore(tarFile);
//		}
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
