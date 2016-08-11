package com.allen.xnwhelper.autologin;

import java.util.ArrayList;

import com.allen.xnwhelper.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class FileChooserAdapter extends BaseAdapter {

	private ArrayList<FileInfo> mFileLists;
	private LayoutInflater mLayoutInflater = null;



	public FileChooserAdapter(Context context, ArrayList<FileInfo> fileLists) {
		super();
		mFileLists = fileLists;
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFileLists.size();
	}

	@Override
	public FileInfo getItem(int position) {
		// TODO Auto-generated method stub
		return mFileLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			view = mLayoutInflater.inflate(R.layout.filechooser_gridview_item,
					null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		FileInfo fileInfo = getItem(position);
        //TODO 
		
		holder.tvFileName.setText(fileInfo.getFileName());
		
		int type = fileInfo.getFileType(); 
		if(type == FileType.FILE_TYPE_DIR){      //鏂囦欢澶�?
			holder.imgFileIcon.setImageResource(R.drawable.fm_folder);
			holder.tvFileName.setTextColor(Color.GRAY);
		}else if(type == FileType.FILE_TYPE_VIDEO){
			holder.imgFileIcon.setImageResource(R.drawable.fm_video);
			holder.tvFileName.setTextColor(Color.RED);
		}else if(type == FileType.FILE_TYPE_IMAGE){                           //鏈煡鏂囦欢
			holder.imgFileIcon.setImageResource(R.drawable.fm_picture);
			holder.tvFileName.setTextColor(Color.RED);
		}else {
			holder.imgFileIcon.setImageResource(R.drawable.fm_unknown);
			holder.tvFileName.setTextColor(Color.GRAY);
		}
		
		return view;
	}

	public static class ViewHolder {
		ImageView imgFileIcon;
		TextView tvFileName;

		public ViewHolder(View view) {
			imgFileIcon = (ImageView) view.findViewById(R.id.imgFileIcon);
			tvFileName = (TextView) view.findViewById(R.id.tvFileName);
		}
	}

	


}
