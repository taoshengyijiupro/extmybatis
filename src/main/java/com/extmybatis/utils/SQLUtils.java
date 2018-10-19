package com.extmybatis.utils;

import java.util.ArrayList;
import java.util.List;

public class SQLUtils {

    /**
     * 获取insert语句value后面的入参
     *
     * @param sql
     * @return
     */
    public static String[] sqlInsertParamter(String sql) {
        int startIndex = sql.indexOf("values");
        int endIndex = sql.length();
        String subString = sql.substring(startIndex + 6, endIndex).replace("(", "").replace(")", "")
                .replace("#{", "").replace("}", "");
        String[] split = subString.split(",");
        return split;
    }

    /**
     * 获取select里面where后面的数据  参数
     *
     * @param sql
     * @return
     */
    public static List<String> sqlSelectParamerer(String sql) {
        int startIndex = sql.indexOf("where");
        int endIndex = sql.length();
        String substring = sql.substring(startIndex + 5, endIndex);
        String[] split = substring.split("and");
        List<String> listArr = new ArrayList<>();
        for (String string : split) {
            String[] split1 = string.split("=");
            listArr.add(split1[1].trim().replace("#{","").replace("}",""));
        }
        return listArr;
    }

    /**
     * 将SQL语句的参数替换变为?<br>
     *
     * @param sql
     * @param parameterName
     * @return
     */
    public static String parameQuestion(String sql, String[] parameterName) {
        for (int i = 0; i < parameterName.length; i++) {
            String string = parameterName[i];
            sql = sql.replace("#{" + string + "}", "?");
        }
        return sql;
    }

    public static String parameQuestion(String sql, List<String> parameterName) {
        for (int i = 0; i < parameterName.size(); i++) {
            String string = parameterName.get(i);
            sql = sql.replace("#{" + string + "}", "?");
        }
        return sql;
    }

}
