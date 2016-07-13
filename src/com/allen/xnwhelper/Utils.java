package com.allen.xnwhelper;

import java.util.Random;

public class Utils {
    static final char [] ALPHABET1 =  { 'a','b','c','d','e','f','g',
            'h','i','j','k','l','m','n',
            'o','p','q','r','s','t',
            'u','v','w','x','y','z'};
    static final char [] ALPHABET2 =  { 'A','B','C','D','E','F','G',
                'H','I','J','K','L','M','N',
                'O','P','Q','R','S','T',
                'U','V','W','X','Y','Z'};
    static final char [] NUMBER = {'0','1','2','3','4','5','6','7','8','9'};
    
    //StringBuffer pwd = new StringBuffer(¡°¡±);
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
}
