package com.mipo.db.util;

import org.dozer.DozerBeanMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BeanMapper {

    /**
     * dozer单例
     */
    private static DozerBeanMapper dozer = new DozerBeanMapper();

    /**
     * 对象间的值拷贝
     * @param source
     * @param destination
     * @param <T>
     * @return
     */
    public static <T> T map(Object source, Class<T> destination){
        return dozer.map(source, destination);
    }

    /**
     * list之间的值拷贝
     * @param sourceList
     * @param destinationClass
     * @param <T>
     * @return
     */
    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        List<T> destinationList = new ArrayList<>();
        for (Object sourceObject : sourceList) {
            T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 基本类型之间的赋值
     * @param source
     * @param destinationObject
     */
    public static void copy(Object source, Object destinationObject) {
        dozer.map(source, destinationObject);
    }

    /**
     * Comment is created by lyl on 2017/11/10 下午3:25.
     *
     * 转换字段类型
     */
    private static Object convertFieldValue(Field field, String value) throws IllegalArgumentException, IllegalAccessException, ParseException {
        field.setAccessible(true);
        String fieldType = field.getType().getName();
        if (fieldType.equals("java.lang.String")) {
            return value;
        }
        if (fieldType.equals("java.lang.Integer") || fieldType.equals("int")) {
            return Integer.valueOf(value);
        }
        if (fieldType.equals("java.lang.Long") || fieldType.equals("long")) {
            return Long.valueOf(value);
        }
        if (fieldType.equals("java.lang.Float") || fieldType.equals("float")) {
            return Float.valueOf(value);
        }
        if (fieldType.equals("java.lang.Double") || fieldType.equals("double")) {
            return Double.valueOf(value);
        }
        if (fieldType.equals("java.util.Date")) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.parse(value);
        }
        if (fieldType.equals("java.math.BigDecimal")) {
            return new BigDecimal(value);
        }
        if (fieldType.equals("java.lang.Boolean") || fieldType.equals("boolean")) {
            return Boolean.valueOf(value);
        }
        if (fieldType.equals("java.lang.Byte") || fieldType.equals("byte")) {
            return Byte.valueOf(value);
        }
        if (fieldType.equals("java.lang.Short") || fieldType.equals("short")) {
            return Short.valueOf(value);
        }
        return null;
    }

    public static String upperFirstLetter(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * @description 转换javabean ,将class2中非空的属性值赋值给class1，
     * @param objectBase 基准类,被赋值对象
     * @param objectUpdate 提供数据的对象
     */
    public static void updateObeject(Object objectBase, Object objectUpdate) {
        try {
            Class<?> clazz1 = objectBase.getClass();
            Class<?> clazz2 = objectUpdate.getClass();
            // 得到method方法
            Method[] method1 = clazz1.getMethods();
            Method[] method2 = clazz2.getMethods();

            int length1 = method1.length;
            int length2 = method2.length;
            if (length1 != 0 && length2 != 0) {
                // 创建一个get方法数组，专门存放objectUpdate的get方法。
                Method[] get = new Method[length2];
                for (int i = 0, j = 0; i < length2; i++) {
                    if (method2[i].getName().indexOf("get") == 0
                        && !method2[i].getName().equals("getClass")) {
                        get[j] = method2[i];
                        ++j;
                    }
                }

                for (int i = 0; i < get.length; i++) {
                    if (get[i] == null)// 数组初始化的长度多于get方法，所以数组后面的部分是null
                        break;
                    // 得到get方法的值，判断时候为null，如果为null则进行下一个循环
                    Object value = get[i].invoke(objectUpdate, new Object[] {});
                    if (null == value)
                        continue;
                    // 得到get方法的名称 例如：getXxxx
                    String getName = get[i].getName();
                    // 得到set方法的时候传入的参数类型，就是get方法的返回类型
                    Class<?> paramType = get[i].getReturnType();
                    Method getMethod = null;
                    try {
                        // 判断在class1中时候有class2中的get方法，如果没有则抛异常继续循环
                        getMethod = clazz1.getMethod(getName, new Class[] {});
                    } catch (NoSuchMethodException e) {
                        continue;
                    }

                    // 通过getName 例如getXxxx 截取后得到Xxxx，然后在前面加上set，就组装成set的方法名
                    String setName = "set" + getName.substring(3);
                    // 得到class1的set方法，并调用
                    Method setMethod = clazz1.getMethod(setName, paramType);
                    setMethod.invoke(objectBase, value);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
