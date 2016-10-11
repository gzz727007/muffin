/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.util;

/**
 *
 * @author muffin
 */
public class CodeUtil {
    public static boolean isXingchuCode(String code) {
        boolean isXingchu = false;
        if(code.length() == 19) {
            try {
                Long.parseLong(code);
                isXingchu = true;
            }catch(Exception e) {
            }
        }
        return isXingchu;
    }
    
    public static String parseCode(String code) {
        String retCode = code;
        if(code.length() > 21 && code.contains("id")) {
            System.out.println(code);
            retCode = code.substring(code.indexOf("id")+ 1, code.length());
        }
        return retCode;
    }
}
