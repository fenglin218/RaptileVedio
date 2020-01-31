package com.itlaoqi.bsbdj.common;

import ognl.BooleanExpression;
import ognl.Ognl;
import ognl.OgnlException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OgnlUtils {
    public static String getString(String ognl , Map root){
        try {
            return Ognl.getValue(ognl , root).toString();
        } catch (OgnlException e) {
            throw new RuntimeException(e);
        }
    }
    //Number是所有数字的父类
    public static Number getNumber(String ognl , Map root){
        Number result = null;
        try {
            Object val = Ognl.getValue(ognl , root);
            if(val != null){
                if(val instanceof  Number){
                    result = (Number)val;
                }else if(val instanceof  String){
                    result = new BigDecimal((String)val);
                }
            }

        } catch (OgnlException e) {
            throw new RuntimeException(e);
        }
        return  result;
    }

    //获取Boolean类型
    public static Boolean getBoolean(String ognl , Map root){
        Boolean result = null;
        try {
            Object val = Ognl.getValue(ognl , root);
            if(val != null){
                if(val instanceof  Boolean){
                    result = (Boolean) val;
                }else if(val instanceof  String){
                    result = ((String)val).equalsIgnoreCase("true")?true:false;
                }else if(val instanceof Number){
                    if(((Number)val).intValue() == 1){
                        result = true;
                    }else{
                        result = false;
                    }
                }
            }
        } catch (OgnlException e) {
            throw new RuntimeException(e);
        }
        return  result;
    }

    /**
     * 获取List集合，里面每一个元素都是Map
     * @param ognl
     * @param root
     * @return
     */
    public static List<Map<String,Object>> getListMap(String ognl , Map root){
        List<Map<String,Object>> list = null;
        try {
            list = (List)Ognl.getValue(ognl , root);
            if(list == null){
                list = new ArrayList();
            }

        } catch (OgnlException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * 获取list集合，里面每一个元素都是String
     * @param ognl
     * @param root
     * @return
     */
    public static List<String> getListString(String ognl , Map root){
        List<String> list = null;
        try {
            list = (List)Ognl.getValue(ognl , root);
            if(list == null){
                list = new ArrayList();
            }
        } catch (OgnlException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
