/*
* $$Id$$
* Copyright (c) 2011 Qunar.com. All Rights Reserved.
*/
package com.github.dbunit.ext.util;

/**
 * 描述：
 * Created by JarnTang at 12-5-14 上午10:14
 *
 * @author <a href="mailto:changjiang.tang@qunar.com">JarnTang</a>
 */
public class StringUtil {

    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String trim(String str) {
        return str == null ? str : str.trim();
    }

}
