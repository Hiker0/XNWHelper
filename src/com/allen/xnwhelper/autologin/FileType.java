package com.allen.xnwhelper.autologin;

import java.io.File;
import java.util.ArrayList;

public class FileType {

	public static int FILE_TYPE_NULL = -2;
	public static int FILE_TYPE_OTHER = -1;
	public static int FILE_TYPE_DIR = 0;
	public static int FILE_TYPE_IMAGE = 1;
	public static int FILE_TYPE_VIDEO = 2;
	public static int FILE_TYPE_TXT = 3;
	private static ArrayList<String> VIDEO_SUFFIX = new ArrayList<String>();
	private static ArrayList<String> IMAGE_SUFFIX = new ArrayList<String>();
	private static ArrayList<String> TXT_SUFFIX = new ArrayList<String>();

	static {
		VIDEO_SUFFIX.add("MPEG");
		VIDEO_SUFFIX.add("3GP");
		VIDEO_SUFFIX.add("MPG");
		VIDEO_SUFFIX.add("MP4");
		VIDEO_SUFFIX.add("AVI");
		VIDEO_SUFFIX.add("3GPP");
		VIDEO_SUFFIX.add("M4V");
		VIDEO_SUFFIX.add("3GPP2");
		
		IMAGE_SUFFIX.add("JPG");
		IMAGE_SUFFIX.add("JPEG");
		IMAGE_SUFFIX.add("GIF");
		IMAGE_SUFFIX.add("png");
		IMAGE_SUFFIX.add("BMP");
		IMAGE_SUFFIX.add("WEBP");
		IMAGE_SUFFIX.add("WBMP");
		
		TXT_SUFFIX.add("TXT");
		TXT_SUFFIX.add("XML");
		TXT_SUFFIX.add("INFO");
	}
	
	
	public static int getFileType(String path){
		if(path == null){
			return FILE_TYPE_NULL;
		}
		File file = new File(path);
		if(!file.exists()){
			return FILE_TYPE_NULL;
		}
		if(file.isDirectory()){
			return FILE_TYPE_DIR;
		}
		int lastDot = path.lastIndexOf(".");
        if (lastDot < 0)
            return FILE_TYPE_OTHER;
        
        String suffix = path.substring(lastDot + 1).toUpperCase();
		for(String lable: VIDEO_SUFFIX){
			if(suffix.equals(lable)){
				
				return FILE_TYPE_VIDEO;
			}
		}
		
		for(String lable: IMAGE_SUFFIX){
			if(suffix.equals(lable)){
				return FILE_TYPE_IMAGE;
			}
		}
		
        for(String lable: TXT_SUFFIX){
            if(suffix.equals(lable)){
                return FILE_TYPE_TXT;
            }
        }
		
		return FILE_TYPE_OTHER;
	}
	
	public static boolean isVideo(String path){
		int lastDot = path.lastIndexOf(".");
        if (lastDot < 0)
            return false;
        
        String suffix = path.substring(lastDot + 1).toUpperCase();
		for(String lable: VIDEO_SUFFIX){
			if(suffix.equals(lable)){
				return true;
			}
		}

		
		return false;
	}
	
	public static boolean isImage(String path){
		int lastDot = path.lastIndexOf(".");
        if (lastDot < 0)
            return false;
        
        String suffix = path.substring(lastDot + 1).toUpperCase();
		for(String lable: IMAGE_SUFFIX){
			if(suffix.equals(lable)){
				return true;
			}
		}

		return false;
	}
	
	   public static boolean isTxt(String path){
	        int lastDot = path.lastIndexOf(".");
	        if (lastDot < 0)
	            return false;
	        
	        String suffix = path.substring(lastDot + 1).toUpperCase();
	        for(String lable: TXT_SUFFIX){
	            if(suffix.equals(lable)){
	                return true;
	            }
	        }

	        return false;
	    }
}
