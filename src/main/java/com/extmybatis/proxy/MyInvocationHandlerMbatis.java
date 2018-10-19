package com.extmybatis.proxy;

import com.extmybatis.annotation.ExtInsert;
import com.extmybatis.annotation.ExtParam;
import com.extmybatis.annotation.ExtSelect;
import com.extmybatis.utils.JDBCUtils;
import com.extmybatis.utils.SQLUtils;

import java.lang.reflect.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MyInvocationHandlerMbatis implements InvocationHandler {

    private Object object;

    public MyInvocationHandlerMbatis(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("##############");
        //新增
        ExtInsert extInsert = method.getDeclaredAnnotation(ExtInsert.class);
        if (extInsert != null) {
            return insertSQL(extInsert, method, args);
        }
        ExtSelect extSelect = method.getDeclaredAnnotation(ExtSelect.class);
        if (extSelect != null) {
            return selectMybatis(extSelect, method, args);
        }
        return null;
    }

    public int insertSQL(ExtInsert extInsert, Method method, Object[] args) {
        // 获取注解上的sql
        String insertSql = extInsert.value();
        //获取方法上的参数
        Parameter[] parameters = method.getParameters();
        //将方法上的参数存放到map集合中
        ConcurrentHashMap<Object, Object> parameterMap = getExtParams(parameters, args);
        //获取sql语句上需要传递的参数
        String[] sqlParameter = SQLUtils.sqlInsertParamter(insertSql);
        List<Object> parameterValues = new ArrayList<>();
        for (int i = 0; i < sqlParameter.length; i++) {
            String str = sqlParameter[i];
            Object object = parameterMap.get(str);
            parameterValues.add(object);
        }
        //将sql语句替换为？号
        String newSql = SQLUtils.parameQuestion(insertSql, sqlParameter);
        System.out.println("newSql" + newSql);
        //调用jdbc，执行sql
        int insertResult = JDBCUtils.insert(newSql, false, parameterValues);
        return insertResult;

    }

    public Object selectMybatis(ExtSelect extSelect, Method method, Object[] args) {
        try {
            //获取查询的sql语句
            String selectSql = extSelect.value();
            //将方法上的参数存放到map集合中
            Parameter[] parameters = method.getParameters();
            //获取方法上的参数集合
            ConcurrentHashMap<Object, Object> parameterMap = getExtParams(parameters, args);
            //获取sql上传递的参数
            List<String> sqlSelectParameters = SQLUtils.sqlSelectParamerer(selectSql);
            //排序参数
            List<Object> paramValues = new ArrayList<>();
            for (int i = 0; i < sqlSelectParameters.size(); i++) {
                String parameterName = sqlSelectParameters.get(i);
                Object object = parameterMap.get(parameterName);
                paramValues.add(object);
            }

            //变为？
            String newSql = SQLUtils.parameQuestion(selectSql, sqlSelectParameters);
            System.out.println("执行SQL：" + newSql + "参数信息：" + paramValues.toString());
            //调用JDBC代码执行查询sql语句
            ResultSet rs = JDBCUtils.query(newSql, paramValues);
            //获取返回类型
            Class<?> returnType = method.getReturnType();
            if (!rs.next()) {
                //没有找到数据
                return null;
            }
            //向上移动
            rs.previous();
            //实例化对象
            Object newInstance = returnType.newInstance();
            while (rs.next()) {
                for (String parameterName : sqlSelectParameters) {
                    //获取结合中的数据
                    Object value = rs.getObject(parameterName);
                    //查询对应的属性
                    Field field = returnType.getDeclaredField(parameterName);
                    //设置允许私有访问
                    field.setAccessible(true);
                    //赋值参数
                    field.set(newInstance, value);
                }
                return newInstance;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ConcurrentHashMap<Object, Object> getExtParams(Parameter[] parameters, Object[] args) {
        // 获取方法上参数集合
        ConcurrentHashMap<Object, Object> parameterMap = new ConcurrentHashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            // 参数信息
            Parameter parameter = parameters[i];
            ExtParam extParam = parameter.getDeclaredAnnotation(ExtParam.class);
            // 参数名称
            String paramValue = extParam.value();
            // 参数值
            Object oj = args[i];
            parameterMap.put(paramValue, oj);
        }
        return parameterMap;
    }

}
