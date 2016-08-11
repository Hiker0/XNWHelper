package com.allen.xnwhelper.autologin;

import java.io.File;
import java.util.ArrayList;

import com.allen.xnwhelper.R;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class FileSelector extends Activity implements OnClickListener {
	final static String TAG = "odmserver/FileSelector";
	final static String FILE_PATH = "file_path";

	private String mSdcardRootPath;
	private String mLastFilePath;

	private ArrayList<FileInfo> mFileLists;
	private FileChooserAdapter mAdatper;
	private GridView mGridview;
	private TextView mPathView;
	private View mBackView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		Log.d(TAG, "OnCreate");

		setContentView(R.layout.file_selector_layout);
		mSdcardRootPath = Environment.getExternalStorageDirectory().getPath();
		mGridview = (GridView) findViewById(R.id.grid_view);
		mPathView = (TextView) findViewById(R.id.path);
		mBackView = findViewById(R.id.back);
		mBackView.setOnClickListener(this);
		findViewById(R.id.cancel).setOnClickListener(this);
		setGridViewAdapter(mSdcardRootPath);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public PendingIntent createPendingResult(int requestCode, Intent data,
			int flags) {
		// TODO Auto-generated method stub
		return super.createPendingResult(requestCode, data, flags);
	}

	private void setGridViewAdapter(String filePath) {
		updateFileItems(filePath);
		mAdatper = new FileChooserAdapter(this, mFileLists);
		mGridview.setAdapter(mAdatper);
		mGridview.setOnItemClickListener(mItemClickListener);

	}

	private void updateFileItems(String filePath) {
		mLastFilePath = filePath;
		if (mLastFilePath.equals(mSdcardRootPath)) {
			mBackView.setEnabled(false);
		} else {
			mBackView.setEnabled(true);
		}
		Log.d(TAG, "-----" + filePath + "----- ");

		if (mFileLists == null)
			mFileLists = new ArrayList<FileInfo>();
		if (!mFileLists.isEmpty())
			mFileLists.clear();

		mPathView.setText(filePath);

		File file = new File(filePath);
		File[] files = file.listFiles();

		if (files == null)
			return;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isHidden()) // 不显示隐藏文件
				continue;

			String fileAbsolutePath = files[i].getAbsolutePath();
			String fileName = files[i].getName();
			int type = FileType.getFileType(fileAbsolutePath);

			Log.d(TAG, "==" + fileName + " : " + type);

			if (type >= FileType.FILE_TYPE_DIR) {
				FileInfo fileInfo = new FileInfo(fileAbsolutePath, fileName,
						type);

				mFileLists.add(fileInfo);
			}

		}

		if (mAdatper != null)
			mAdatper.notifyDataSetChanged(); // 重新刷新
	}

	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {
			FileInfo fileInfo = (FileInfo) (((FileChooserAdapter) adapterView
					.getAdapter()).getItem(position));
			if (fileInfo.getFileType() == FileType.FILE_TYPE_DIR) // 点击项为文件夹,
																	// 显示该文件夹下所有文件
				updateFileItems(fileInfo.getFilePath());
			else {
				Intent intent = new Intent();
				intent.putExtra(FILE_PATH, fileInfo.getFilePath());
				setResult(RESULT_OK, intent);
				finish();
			}

		}

	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			backProcess();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void backProcess() {

		if (!mLastFilePath.equals(mSdcardRootPath)) {
			File thisFile = new File(mLastFilePath);
			String parentFilePath = thisFile.getParent();
			updateFileItems(parentFilePath);
		} else {
			setResult(RESULT_CANCELED);
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();

		switch (id) {
		case R.id.back:
			backProcess();
			break;
		case R.id.cancel:
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
	}
}
