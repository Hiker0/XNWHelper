package com.allen.xnwhelper;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingsActivity extends Activity {
    File tarFile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main_layout);
		
		TextView mInfo = (TextView) this.findViewById(R.id.info_position);
		

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
            mInfo.setText("Éú³ÉÃÜÂëÂ·¾¶:"+"\n"+tarFile.getPath());
            // TODO Auto-generated catch block
		Log.d("allen", "root:"+root.getAbsolutePath());
		
		Button button = (Button) findViewById(R.id.btn_start_xnw);
		button.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try{
                    Intent intent = new Intent();
                    intent.setClassName("com.xnw.qun", "com.xnw.qun.activity.register.UserRegisterActivity");
                    SettingsActivity.this.startActivity(intent);
                }catch(Exception e){
                    Log.d("allen",e.toString());
                }
            }
        });
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
