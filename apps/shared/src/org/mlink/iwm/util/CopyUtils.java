package org.mlink.iwm.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.mlink.iwm.base.BaseVO;

/**
 * Created by Andrei Povodyrev Date: Jun 2, 2004
 */
public class CopyUtils {
	public enum NullAlias {
		Long, Double, Float, Integer, UtilDate, SQLDate, SQLTime
	}

	public static EnumMap<NullAlias, Object> NullAliasValues = new EnumMap<NullAlias, Object>(
			NullAlias.class);
	private static ThreadLocal tl = new ThreadLocal();

	static {
		NullAliasValues.put(NullAlias.Long, Long.MIN_VALUE);
		NullAliasValues.put(NullAlias.Double, Double.MIN_VALUE);
		NullAliasValues.put(NullAlias.Float, Float.MIN_VALUE);
		NullAliasValues.put(NullAlias.Integer, Integer.MIN_VALUE);
		NullAliasValues.put(NullAlias.UtilDate, new Date(0));
		NullAliasValues.put(NullAlias.SQLDate, new java.sql.Date(0));
		NullAliasValues.put(NullAlias.SQLTime, new java.sql.Timestamp(0));
	}

	/**
	 * Effective Jboss405, apache-beanutils, upgraded to v1.7.0 V1.7 has new
	 * functionality for isolated use of singleton for dif applications
	 * (classloaders) See ContextClassLoaderLocal.get() This is a
	 * pseudo-singleton - an single instance is provided per (thread) context
	 * classloader. This mechanism provides isolation for web apps deployed in
	 * the same container. However, ejb tier and web tier may use different
	 * classloaders to start with (not to mention hot deploy) Therefore need
	 * check on presence of the right converters
	 */
	public static void initConverters() {
		if (!(ConvertUtils.lookup(java.util.Date.class) instanceof UtilDateConverter)) {
			ConvertUtils
					.register(new UtilDateConverter(), java.util.Date.class);
			ConvertUtils.register(new SQLDateConverter(), java.sql.Date.class);
			ConvertUtils.register(new SQLTimestampConverter(),
					java.sql.Timestamp.class);
			ConvertUtils
					.register(new StringConverter(), java.lang.String.class);
			ConvertUtils.register(new LongConverter(), java.lang.Long.class);
			ConvertUtils
					.register(new DoubleConverter(), java.lang.Double.class);
			ConvertUtils.register(new FloatConverter(), java.lang.Float.class);
			ConvertUtils.register(new IntegerConverter(),
					java.lang.Integer.class);
		}
	}

	/**
	 * Similar to BeanUtils.copyProperties but operates on Collections
	 * 
	 * @param clazz
	 *            convertion target class
	 * @param col
	 *            collection of convertion sources
	 * @return col collection of targets
	 * @throws Exception
	 */
	public static <E> Collection<E> copyProperties(Class<E> clazz,
			Collection col) throws Exception {
		Collection<E> rtnCol = new ArrayList<E>();
		if (col != null) {
			for (Object source : col) {
				E target = clazz.newInstance();
				copyProperties(target, source);
				rtnCol.add(target);
			}
		}
		return rtnCol;
	}

	public static void copyProperties(Object target, Object source)
			throws Exception {
		initConverters();
		setBaseVOTarget(target);
		if (source instanceof HashMap) {
			HashMap sourceMap = (HashMap) source;
			Object value;
			String valueStr;
			if (sourceMap != null && !sourceMap.isEmpty()) {
				Set<String> keys = sourceMap.keySet();
				for (String key : keys) {
					value = sourceMap.get(key);
					if (value instanceof String) {
						valueStr = (String) value;
						if (valueStr.contains("- Select")) {
							sourceMap.put(key, "");
						}
					}
				}
			}
		}
		BeanUtils.copyProperties(target, source);
		/*if (source instanceof HashMap) {
			HashMap sourceMap = (HashMap) source;
			if (sourceMap.containsKey("id")) {
				try {
					Field fld = target.getClass().getDeclaredField("id");
					Object obj;
					if (fld != null) {
						obj = target.getClass().getDeclaredMethod("getId").invoke(target);
						if(obj==null){
							target.getClass().getDeclaredMethod("setId", Object.class).invoke(target, sourceMap.get("id"));
						}
					}
				} catch (NoSuchFieldException e) {
					// do nothing
				} catch (IllegalAccessException e1) {
					// do nothing
				} catch (ClassCastException e2) {
					// do nothing
				}
			}
		}*/
	}

	public static void copyProperty(Object bean, String name, Object value)
			throws Exception {
		initConverters();
		BeanUtils.copyProperty(bean, name, value);
	}

	private static void setBaseVOTarget(Object target) throws Exception {
		tl.set(target instanceof BaseVO);
	}

	public static boolean isBaseVOTarget() {
		return (Boolean) tl.get();
	}

	public static boolean isNullAlias(Date value) {
		return NullAliasValues.get(CopyUtils.NullAlias.UtilDate).equals(value);
	}

	public static boolean isNullAlias(java.sql.Date value) {
		return NullAliasValues.get(CopyUtils.NullAlias.SQLDate).equals(value);
	}

	public static boolean isNullAlias(Timestamp value) {
		return NullAliasValues.get(CopyUtils.NullAlias.SQLTime).equals(value);
	}

	public static boolean isNullAlias(Integer value) {
		return NullAliasValues.get(CopyUtils.NullAlias.Integer).equals(value);
	}

	public static boolean isNullAlias(Float value) {
		return NullAliasValues.get(CopyUtils.NullAlias.Float).equals(value);
	}

	public static boolean isNullAlias(Double value) {
		return NullAliasValues.get(CopyUtils.NullAlias.Double).equals(value);
	}

	public static boolean isNullAlias(Long value) {
		return NullAliasValues.get(CopyUtils.NullAlias.Long).equals(value);
	}

	public static boolean isNullAlias(String value) {
		return false;
	}

}
