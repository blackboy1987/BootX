
package com.bootx.util;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BeanUtils {

	private static final Map<Class<?>, Field[]> DECLARED_FIELDS_CACHE = new ConcurrentHashMap<>();

	private static final Map<Class<?>, Method[]> DECLARED_METHODS_CACHE = new ConcurrentHashMap<>();


	private static final Map<Class<?>, PropertyDescriptor[]> PROPERTY_DESCRIPTORS_CACHE = new ConcurrentHashMap<>();

	private BeanUtils() {
	}

	public static void setAccessible(Field field) {
		if (field != null && !field.isAccessible() && (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers()))) {
			field.setAccessible(true);
		}
	}

	public static void setAccessible(Method method) {
		if (method != null && !method.isAccessible() && (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))) {
			method.setAccessible(true);
		}
	}

	public static void setField(Field field, Object target, Object value) {
		Assert.notNull(field,"");
		Assert.notNull(target,"");

		try {
			field.set(target, value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static Object invokeMethod(Method method, Object target, Object... args) {
		Assert.notNull(method,"");
		Assert.notNull(target,"");

		try {
			return method.invoke(target, args);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static List<Field> findFields(Class<?> type, Class<? extends Annotation> annotationType) {
		Assert.notNull(type,"");
		Assert.notNull(annotationType,"");

		List<Field> result = new ArrayList<>();
		Class<?> targetClass = type;
		while (targetClass != null && !Object.class.equals(targetClass)) {
			for (Field field : getDeclaredFields(targetClass)) {
				if (getAnnotation(field, annotationType) != null) {
					result.add(field);
				}
			}
			targetClass = targetClass.getSuperclass();
		}
		return result;
	}

	public static List<Method> findMethods(Class<?> type, Class<? extends Annotation> annotationType) {
		Assert.notNull(type,"");
		Assert.notNull(annotationType,"");

		List<Method> result = new ArrayList<>();
		Class<?> targetClass = type;
		while (targetClass != null && !Object.class.equals(targetClass)) {
			for (Method method : getDeclaredMethods(targetClass)) {
				if (getAnnotation(method, annotationType) != null) {
					result.add(method);
				}
			}
			targetClass = targetClass.getSuperclass();
		}
		return result;
	}

	public static List<PropertyDescriptor> getPropertyDescriptors(Class<?> type, Class<? extends Annotation> annotationType) {
		Assert.notNull(type,"");
		Assert.notNull(annotationType,"");

		List<PropertyDescriptor> result = new ArrayList<>();
		for (PropertyDescriptor propertyDescriptor : getPropertyDescriptors(type)) {
			Method readMethod = propertyDescriptor.getReadMethod();
			Method writeMethod = propertyDescriptor.getWriteMethod();
			for (Method method : Arrays.asList(readMethod, writeMethod)) {
				if (method == null) {
					continue;
				}
				if (getAnnotation(method, annotationType) != null) {
					result.add(propertyDescriptor);
				}
			}
		}
		return result;
	}

	private static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
		Assert.notNull(annotatedElement,"");
		Assert.notNull(annotationType,"");

		A annotation = annotatedElement.getAnnotation(annotationType);
		if (annotation == null) {
			for (Annotation metaAnnotation : annotatedElement.getAnnotations()) {
				annotation = metaAnnotation.annotationType().getAnnotation(annotationType);
				if (annotation != null) {
					break;
				}
			}
		}
		return annotation;
	}

	private static Field[] getDeclaredFields(Class<?> type) {
		Assert.notNull(type,"");

		Field[] result = DECLARED_FIELDS_CACHE.get(type);
		if (result == null) {
			result = type.getDeclaredFields();
			DECLARED_FIELDS_CACHE.put(type, result != null ? result : new Field[0]);
		}
		return result;
	}

	private static Method[] getDeclaredMethods(Class<?> type) {
		Assert.notNull(type,"");

		Method[] result = DECLARED_METHODS_CACHE.get(type);
		if (result == null) {
			Method[] declaredMethods = type.getDeclaredMethods();
			Method[] defaultMethods = findConcreteMethodsOnInterfaces(type);
			result = (Method[]) ArrayUtils.addAll(declaredMethods, defaultMethods);
			DECLARED_METHODS_CACHE.put(type, result != null ? result : new Method[0]);
		}
		return result;
	}

	private static Method[] findConcreteMethodsOnInterfaces(Class<?> type) {
		Assert.notNull(type,"");

		List<Method> foundMethods = new ArrayList<>();
		for (Class<?> ifc : type.getInterfaces()) {
			for (Method method : ifc.getMethods()) {
				if (!Modifier.isAbstract(method.getModifiers())) {
					foundMethods.add(method);
				}
			}
		}
		return foundMethods.toArray(new Method[foundMethods.size()]);
	}

	private static PropertyDescriptor[] getPropertyDescriptors(Class<?> type) {
		Assert.notNull(type,"");

		PropertyDescriptor[] result = PROPERTY_DESCRIPTORS_CACHE.get(type);
		if (result == null) {
			try {
				result = Introspector.getBeanInfo(type).getPropertyDescriptors();
			} catch (IntrospectionException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			PROPERTY_DESCRIPTORS_CACHE.put(type, result != null ? result : new PropertyDescriptor[0]);
		}
		return result;
	}

}