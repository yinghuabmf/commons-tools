package com.renmaitech.common.desensitization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据脱敏
 *
 * @author : banxiaohua
 * @date : 2020/3/17 10:12 AM
 */
public class DesensitizationUtils {

    private static final Logger logger = LoggerFactory.getLogger(DesensitizationUtils.class);

    /**
     * 脱敏
     *
     * @param list List<T>
     * @return T
     */
    public static <T> List<T> desensitizationList(List<T> list) {
        List<T> objList = new ArrayList<T>();
        for (T obj : list) {
            objList.add(desensitization(obj));
        }
        return objList;
    }
    /**
     * 脱敏
     * @param obj obj
     * @return T
     */
    public static <T> T desensitization(T obj) {
        Class<?> clazz = obj.getClass();
        ReflectionUtils.doWithFields(clazz, field -> {
            try {
                if (!field.getType().getName().equals(String.class.getName())) {
                    return;
                }
                Desensitization columnField = field.getAnnotation(Desensitization.class);
                if (null == columnField) {
                    return;
                }
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                Method rM = pd.getReadMethod();
                String oldValue = (String)rM.invoke(obj);
                Object newValue;
                if (null != oldValue) {
                    Method wM = pd.getWriteMethod();
                    DesensitizationEnum desensitizationValue = columnField.value();
                    newValue = desensitizationValue.getFunction().apply(oldValue);
                    wM.invoke(obj, newValue);
                }
            } catch (Exception e) {
                logger.error("脱敏失败", e);
            }
        });
        return obj;
    }
}
