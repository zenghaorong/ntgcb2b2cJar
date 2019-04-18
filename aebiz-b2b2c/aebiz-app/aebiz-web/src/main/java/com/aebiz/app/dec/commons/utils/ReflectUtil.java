package com.aebiz.app.dec.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 反射工具类
 * 
 * @author linq
 * 
 */
public class ReflectUtil {

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Map<String, Object> param, Class clazz) {

		Object value = null;

		Class[] paramTypes = new Class[1];

		Object obj = null;

		try {
			// 创建实例
			obj = clazz.newInstance();
			Field[] f = clazz.getDeclaredFields();
			List<Field[]> flist = new ArrayList<Field[]>();
			flist.add(f);

			Class superClazz = clazz.getSuperclass();
			while (superClazz != null) {
				f = superClazz.getFields();
				flist.add(f);
				superClazz = superClazz.getSuperclass();
			}

			for (Field[] fields : flist) {
				for (Field field : fields) {
					String fieldName = field.getName();
					value = param.get(fieldName);
					if (value != null) {
						paramTypes[0] = field.getType();
						Method method = null;
						// 调用相应对象的set方法
						StringBuffer methodName = new StringBuffer("set");
						methodName.append(fieldName.substring(0, 1)
								.toUpperCase());
						methodName.append(fieldName.substring(1,
								fieldName.length()));

						method = clazz.getMethod(methodName.toString(),
								paramTypes);
						method.invoke(obj, ConvertUtil.getValue(
								value.toString(), fieldName, paramTypes[0]));
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Class======"+clazz+" , param==="+param);
			e.printStackTrace();
		}
		return (T) obj;
	}

}
