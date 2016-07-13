package com.allen.xnwhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.button_test);
		Button button1= (Button) this.findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent intent = Main.this.getIntent();
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), Main2.class);
				Main.this.startActivity(intent);
				Main.this.setResult(RESULT_OK, intent);
			    finish();
			}
		});
		
		Intent intent = Main.this.getIntent();
		Main.this.setResult(RESULT_CANCELED, intent);
	    //finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("allen", "onResume");

		//}
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
