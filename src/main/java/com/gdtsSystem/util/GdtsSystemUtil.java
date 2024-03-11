package com.gdtsSystem.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Scanner;

public class GdtsSystemUtil {
    public static void getRequestData(HttpServletRequest request, HashMap<String,String> map){
        Enumeration<String> params=request.getParameterNames();
        while (params.hasMoreElements()){
            String paramName = params.nextElement();
            map.put(paramName,request.getParameter(paramName));
        }
    }

    public static int getInt(Scanner scanner) {
        int i = 0;
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            if (s.length() == 0) continue;
            try {
                i = Integer.parseInt(s);//用户输入的得整数
                break;
            } catch (Exception e) {
                System.err.print("无效的整数，请重新输入:");
            }
        }
        return i;
    }

    public static String getStr(Scanner scanner) {
        String s = null;
        while (scanner.hasNext()) {
            s = scanner.nextLine();
            if (s.length() == 0) continue;
            break;
        }
        return s;
    }

    public static boolean isPassStr(String pass){
        int len=pass.length();
        if(len<8||len>12) {
            return false;
        }
        boolean hasLetter=false,hasNumber=false;
        for (int i = 0; i < len; i++) {
            char c = pass.charAt(i);
            if (c>='a'&&c<='z'||c>='A'&&c<='Z') hasLetter=true;
            else if (c>='0'&&c<='9') hasNumber=true;
            else return false;

        }
        return hasLetter&&hasNumber;
    }
}
