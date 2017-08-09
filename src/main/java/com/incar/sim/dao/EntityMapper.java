package com.incar.sim.dao;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.springframework.jdbc.core.RowMapper;

/**
 * 实体映射类
 * @author fanbeibei
 *
 */
public class EntityMapper implements RowMapper {

	/**
	 * 映射实体的class对象
	 */
	private Class<?> entityClass;

	/**
	 * @param entityClass 映射实体的class对象
	 */
	public EntityMapper(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	/** 
	 * Implementations must implement this method to map each row of data
	 * in the ResultSet. This method should not call <code>next()</code> on
	 * the ResultSet; it is only supposed to map values of the current row.
	 * 
	 * @param rs the ResultSet to map (pre-initialized for the current row)
	 * @param rowNum the number of the current row
	 * @return the result object for the current row
	 * @throws SQLException if a SQLException is encountered getting
	 * column values (that is, there's no need to catch SQLException)
	 */
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		Object obj = null;

		List<String> colName = new ArrayList<String>();
		ResultSetMetaData meta = rs.getMetaData();
		for (int i = 0; i < meta.getColumnCount(); i++) {
			colName.add(meta.getColumnLabel(i + 1).toUpperCase());
		}

		try {
			obj = entityClass.newInstance();

			Field[] fields = entityClass.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(Column.class)) {
					Column col = field.getAnnotation(Column.class);
					if (colName.contains(col.name().toUpperCase())) {
						setObject(obj, rs, field, col.name());
					}
				}
			}
		} catch (Exception e) {
			throw new SQLException(e);
		}
		return obj;
	}

	/**
	 * 给实体属性设值
	 * 
	 *  
	 * @param obj  实体对象
	 * @param rs  查询返回的结果集
	 * @param filed 属性
	 * @param colName 列名
	 * @throws Exception
	 */
	private static void setObject(Object obj, ResultSet rs, Field filed, String colName) throws Exception {
		if (rs.getObject(colName) == null)
			return;

		Class<?> filedType = filed.getType();
		filed.setAccessible(true);
		
		if (filedType.equals(String.class)) {
			filed.set(obj, rs.getString(colName));
		} else if (filedType.equals(Long.class) || filedType.equals(long.class)) {
			filed.set(obj, rs.getLong(colName));
		} else if (filedType.equals(Date.class) && rs.getTimestamp(colName) != null) {
			filed.set(obj, new Date(rs.getTimestamp(colName).getTime()));
		} else if (filedType.equals(Double.class) || filedType.equals(double.class)) {
			filed.set(obj, rs.getDouble(colName));
		} else if (filedType.equals(Float.class) || filedType.equals(float.class)) {
			filed.set(obj, rs.getFloat(colName));
		} else if (filedType.equals(Short.class) || filedType.equals(short.class)) {
			filed.set(obj, rs.getShort(colName));
		} else if (filedType.equals(Integer.class) || filedType.equals(int.class)) {
			filed.set(obj, rs.getInt(colName));
		} else if (filedType.equals(BigDecimal.class)) {
			filed.set(obj, rs.getBigDecimal(colName));
		} else {
			filed.set(obj, rs.getObject(colName));
		}
	}
}
