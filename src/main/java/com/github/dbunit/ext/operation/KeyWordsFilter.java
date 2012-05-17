/*
* $$Id$$
* Copyright (c) 2011 Qunar.com. All Rights Reserved.
*/
package com.github.dbunit.ext.operation;

import com.github.dbunit.ext.util.StringUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * 描述：
 * Created by JarnTang at 12-4-24 下午9:26
 *
 * @author <a href="mailto:changjiang.tang@qunar.com">JarnTang</a>
 */
public class KeyWordsFilter {

    static final List<String> keywordList = new ArrayList<String>();

    static {
        try {
            Enumeration<URL> resources = KeyWordsFilter.class.getClassLoader().getResources("keywords.properties");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                Properties properties = new Properties();
                properties.load(url.openStream());
                String keywords = (String) properties.get("keywords");
                if (StringUtil.isNotBlank(keywords)) {
                    String[] split = keywords.trim().split(",");
                    for (String str : split) {
                        String keyword = StringUtil.trim(str);
                        if (!keywordList.contains(keyword)) {
                            keywordList.add(keyword);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查sql语句里的列名是否为字符串
     *
     * @param key 列名
     * @return 如果是数据库关键字，返回true，否则为false
     */
    public static boolean isKeyWords(String key){
        return keywordList.contains(key);
    }

}
