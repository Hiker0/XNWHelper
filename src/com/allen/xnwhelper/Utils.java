package com.allen.xnwhelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Random;

public class Utils {
    public static final String ENCODE_TYPE_GBK="GBK";
    public static final String ENCODE_TYPE_UTF8="UTF-8";
    public static final String ENCODE_TYPE_UNICODE="Unicode";
    public static final String ENCODE_TYPE_UTF16="UTF-16BE";
    public static final String ENCODE_TYPE_GB232="GB2312";
    public static final String ENCODE_TYPE_ISO="ISO-8859-1";
    
    
    static final char [] ALPHABET1 =  { 'a','b','c','d','e','f','g',
            'h','i','j','k','l','m','n',
            'o','p','q','r','s','t',
            'u','v','w','x','y','z'};
    static final char [] ALPHABET2 =  { 'A','B','C','D','E','F','G',
                'H','I','J','K','L','M','N',
                'O','P','Q','R','S','T',
                'U','V','W','X','Y','Z'};
    static final char [] NUMBER = {'0','1','2','3','4','5','6','7','8','9'};
    
    //StringBuffer pwd = new StringBuffer(“”);
    static Random r = new Random();
    
    static private char getRadomUpcaseLetter(){
         int i = Math.abs(r.nextInt(ALPHABET2.length));
         return ALPHABET2[i];
    }
    
    static private char getLowerUpcaseLetter(){
        int i = Math.abs(r.nextInt(ALPHABET1.length));
        return ALPHABET1[i];
   }
    
    static private char getOneNum(){
        int i = Math.abs(r.nextInt(NUMBER.length));
        return NUMBER[i];
   }
   
   static private char getOneLetter(){
       int i = Math.abs(r.nextInt(ALPHABET1.length+ALPHABET2.length));
       if(i< ALPHABET1.length){
           return ALPHABET1[i];
       }else{
           return ALPHABET2[i-ALPHABET1.length]; 
       }
   }
   
   
   //generate a password whose length is num
   static  public String generatePwd(int num){
       if(num < 0){
           return null;
       }
       StringBuffer pwd = new StringBuffer("");
       for(int i=0;i < num; i++){
           char ch ;
           if(i< num/2){
               ch = getOneLetter();
           }else{
               ch = getOneNum();
           }
           
           pwd.append(ch);
       }
       
       return pwd.toString();
   }
   //generate a password whose length is in range num1 to num2
   static public String generatePwd(int num1, int num2){
       if(num1 < 0 || num1 > num2){
           return null;
       }
       int length =  r.nextInt(num2-num1)+num1;
       
       return generatePwd(length);
   }  
   //generate a password whose length is num
   static  public String generateName(int num){
       if(num < 0){
           return null;
       }
       StringBuffer pwd = new StringBuffer("");
       for(int i=0;i < num; i++){
           char ch ;
           if(i< 2){
               ch = getOneLetter();
           }else{
               ch = getOneNum();
           }
           
           pwd.append(ch);
       }
       
       return pwd.toString();
   }
    
   //generate a password whose length is in range num1 to num2
   static public String generateName(int num1, int num2){
       if(num1 < 0 || num1 > num2){
           return null;
       }
       int length =  r.nextInt(num2-num1)+num1;
       
       return generateName(length);
   }
   
    public static String getEncoding(String str) {
        String encode = ENCODE_TYPE_GB232;
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = ENCODE_TYPE_ISO;
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = ENCODE_TYPE_UTF8;
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = ENCODE_TYPE_GBK;
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }


    /**
     * 判断文件的编码格式
     * 
     * @param fileName
     *            :file
     * @return 文件编码格式
     * @throws Exception
     */
    public static String codeString(File file) throws Exception {
        BufferedInputStream bin = new BufferedInputStream(
                new FileInputStream(file));
        int p = (bin.read() << 8) + bin.read();
        String code = null;

        switch (p) {
        case 0xefbb:
            code = ENCODE_TYPE_UTF8;
            break;
        case 0xfffe:
            code = ENCODE_TYPE_UNICODE;
            break;
        case 0xfeff:
            code = ENCODE_TYPE_UTF16;
            break;
        default:
            code = ENCODE_TYPE_GBK;
        }

        return code;
    }
}
