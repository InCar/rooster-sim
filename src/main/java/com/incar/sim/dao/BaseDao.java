package com.incar.sim.dao;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.incar.sim.util.Constant;
import com.incar.sim.entity.PageVo;

/**
 * 继承JdbcDaoSupport,加上insert, update, find, delete对实体的映谢（对应数据库为mysql）
 * 
 * @author
 * @since 2014-7-4 12:10:30
 * @version 0.10a
 */
public abstract class BaseDao extends JdbcDaoSupport {

	private static final Logger log = LogManager.getLogger(BaseDao.class);
	
	/**
	 * 获取实体对应数据表名
	 * @param entityClass
	 * 
	 * @return
	 * 
	 * @author fanbeibei
	 * @since 2014年9月16日
	 */
	protected static String getEntityTableName(Class<?> entityClass){
		return " " + entityClass.getAnnotation(Table.class).name() +" ";
	}

	/**
	 * 
	 * 获取实体对应数据表的字段列表（逗号隔开,如：col1,col2,col3）
	 * 
	 * @param entityClass
	 *            实体类
	 * @return
	 * @author fanbeibei
	 * @since 2014年9月16日
	 */
	protected static String getEntityColumnSql(Class<?> entityClass) {

		Field[] fields = entityClass.getDeclaredFields();
		StringBuilder columnSql = new StringBuilder("");
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (null != column) {
				columnSql.append(",").append(column.name());
			}
		}

		if (columnSql.length() > 0) {
			return " " + columnSql.substring(1) + " ";
		}

		return null;
	}

	/**
	 * 获取实体对应数据表的带别名的字段列表（逗号隔开,如：t1.col1,t1.col2,t1.col3）
	 * 
	 * @param entityClass
	 *            实体类
	 * @param tableAlias
	 *            表别名
	 * @return
	 */
	protected static String getEntityColumnSql(Class<?> entityClass, String tableAlias) {

		Field[] fields = entityClass.getDeclaredFields();
		StringBuilder columnSql = new StringBuilder("");
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (null != column) {
				columnSql.append(",").append(tableAlias + "." + column.name());
			}
		}

		if (columnSql.length() > 0) {
			return " " + columnSql.substring(1) + " ";
		}

		return null;
	}

	/**
	 * 生成ID
	 * 
	 * @param tableName_Seq
	 *            数据库表名
	 * @return
	 * @throws SQLException
	 */
	protected Object getSequenceGeneratorId(String tableName_Seq) throws SQLException {
		String schema = null;
		Connection conn = this.getConnection();
		String url = conn.getMetaData().getURL();
		this.releaseConnection(conn);
		if (url != null && url.length() > 0) {
			String[] strs = url.split("[?]")[0].split("/");
			schema = strs[strs.length - 1];
		}
		StringBuilder sql = new StringBuilder(
				"SELECT Auto_increment FROM information_schema.tables  WHERE TABLE_SCHEMA = '" + schema
						+ "' AND table_name='");
		sql.append(tableName_Seq);
		sql.append("' limit 1");
		log.debug(sql);
		return getJdbcTemplate().queryForObject(sql.toString(),new Object[]{},Long.class);
		
	}

	/**
	 * 从属性或方法上获取注解（属性优先）
	 * 
	 * @param annotationClass
	 * @param field
	 * @param getMethod
	 * @return
	 */
	private <T extends Annotation> T getAnnotation(Class<T> annotationClass, Field field, Method getMethod) {
		T annotation = field.getAnnotation(annotationClass);
		if (annotation == null) {
			annotation = getMethod.getAnnotation(annotationClass);
		}
		return annotation;
	}

	/**
	 * 判断是否有属性或方法上是否有注解
	 * 
	 * @param annotationClass
	 *            注解Class对象
	 * @param field
	 *            属性
	 * @param getMethod
	 *            方法
	 * @return
	 */
	private <T extends Annotation> boolean hasAnnotation(Class<T> annotationClass, Field field, Method getMethod) {
		return field.isAnnotationPresent(annotationClass) || getMethod.isAnnotationPresent(annotationClass);
	}

	/**
	 * 获取占位符列表
	 * 
	 * @param size
	 * @return
	 */
	private String getQuestionStr(int size) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < size; i++) {
			b.append(",?");
		}
		return b.toString().replaceFirst(",", "(") + ")";
	}

	/**
	 * 插入记录，返回ID
	 * 
	 * @param obj
	 *            实体，实体ID字段可以为空,为空则ID必须为Auto_increment
	 * @return
	 * @throws Exception
	 */
	public Object insert(Object obj) throws Exception {
		return insert(obj, true);
	}

	/**
	 * 描述: 当nullIdValue为false时，id字段不能为空，否则IllegalArgumentException;
	 * 当nullIdValue为true时，实体ID字段可以为空,为空则ID必须为Auto_increment
	 * 
	 * @author fanbeibei
	 * @since 2015年2月9日
	 *
	 * @param obj
	 *            实体
	 * @param nullIdValue
	 *            id字段是否为空
	 * @return
	 * @throws Exception
	 */
	public Object insert(Object obj, boolean nullIdValue) throws Exception {
		if (null == obj) {
			throw new IllegalAccessException("the object for insert can not be null!!");
		}

		Class<?> entityClass = obj.getClass();

		List<String> colNamelist = new ArrayList<String>();// 列名列表
		final List<Object> objList = new ArrayList<Object>();// 列值列表

		Field[] fields = entityClass.getDeclaredFields();

		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers())) {// 跳过静态字段
				try {

					PropertyDescriptor pd = new PropertyDescriptor(field.getName(), entityClass);
					Method getMethod = pd.getReadMethod();
					Column column = this.getAnnotation(Column.class, field, getMethod);

					if (column != null && column.insertable() && getMethod != null) {
						Object o = getMethod.invoke(obj);

						if (this.hasAnnotation(Id.class, field, getMethod)) {// 对Id的处理
							if (!nullIdValue && null == o) {
								throw new IllegalArgumentException("Id column can not be  null");
							} else {
								colNamelist.add(column.name());
								objList.add(o);
							}
						} else if (o != null) {
							colNamelist.add(column.name());
							objList.add(o);
						}
					}
				} catch (Exception e) {
					log.debug(e.getMessage(), e);
					log.error("读取" + entityClass.getName() + " " + field.getName() + " 异常 ");
					throw e;
				}
			}
		}

		final StringBuilder sql = new StringBuilder("insert into ");
		sql.append(entityClass.getAnnotation(Table.class).name());
		sql.append("(");
		sql.append(colNamelist.toString().replace("[", "").replace("]", ""));
		sql.append(") values ");
		sql.append(getQuestionStr(colNamelist.size()));

		log.debug("sql:" + sql);
		log.debug("param:" + objList);

		KeyHolder key = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pstem = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				for (int i = 1; i <= objList.size(); i++) {
					pstem.setObject(i, objList.get(i - 1));
				}
				return pstem;
			}
		}, key);
		return key.getKey().longValue();
	}

	/**
	 * 更新所有的字段，包括实体里的空值字段
	 * 
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	public int updateAllFields(Object obj) throws IllegalAccessException {

		if (null == obj) {
			throw new IllegalAccessException("the object for updateAllFields can not be null!!");
		}

		int updateRows = 0;
		Class<?> entityClass = obj.getClass();
		List<String> colNamelist = new ArrayList<String>(); // 列名列表
		List<Object> objList = new ArrayList<Object>();// 参数列表
		List<String> idNameList = new ArrayList<String>();// Id列表
		List<Object> idObjList = new ArrayList<Object>();// id值列表
		try {
			Field[] fields = entityClass.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(Column.class)) {
					// 判断updatable属性
					if (!field.getAnnotation(Column.class).updatable()) {
						continue;
					}

					field.setAccessible(true);
					Object o = field.get(obj);
					Column colomn = field.getAnnotation(Column.class);
					if (field.isAnnotationPresent(Id.class)) {
						idNameList.add(colomn.name() + "=?");
						idObjList.add(o);
					} else {
						colNamelist.add(colomn.name() + "=?");
						objList.add(o);
					}
				}
			}
			objList.addAll(idObjList);

			StringBuilder sql = new StringBuilder("update ");
			sql.append(entityClass.getAnnotation(Table.class).name());
			sql.append(" set ").append(colNamelist.toString().replace("[", "").replace("]", ""));
			sql.append(" where ")
					.append(idNameList.toString().replace("[", "").replace("]", "").replaceAll(",", " and "));
			log.debug("sql:" + sql);
			log.debug("param:" + objList);
			updateRows = getJdbcTemplate().update(sql.toString(), objList.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			throw getExceptionTranslator().translate(null, null, new SQLException(e));
		}
		return updateRows;
	}

	/**
	 * 更新，空值字段更新为默认值
	 * 
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 */
	public int updateAllFieldWithNull(Object obj) throws IllegalAccessException {
		if (null == obj) {
			throw new IllegalAccessException("the object for updateAllFieldWithNull can not be null!!");
		}
		// Assert.notNull(obj, this.getClass() + "updateAllFieldWithNull method
		// obj param can not be null");
		int updateRows = 0;
		Class<?> entityClass = obj.getClass();
		List<String> colNamelist = new ArrayList<String>();
		List<Object> objList = new ArrayList<Object>();
		List<String> idNameList = new ArrayList<String>();
		List<Object> idObjList = new ArrayList<Object>();

		try {
			Field[] fields = entityClass.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(Column.class)) {
					Column column = field.getAnnotation(Column.class);
					if (!column.updatable()) {
						continue;
					}
					field.setAccessible(true);
					Object o = field.get(obj);
					if (field.isAnnotationPresent(Id.class)) {
						idNameList.add(column.name() + "=?");
						idObjList.add(o);
					} else {
						if (o != null) {
							colNamelist.add(column.name() + "=?");
							objList.add(o);
						} else {// 各种常见类型的处理
							Class<?> fieldType = field.getType();
							if (String.class.equals(fieldType)) {
								colNamelist.add(column.name() + "=?");
								objList.add(Constant.FieldDefaultValue.STRING_DEFAULT_VALUE);
							} else if (Date.class.equals(fieldType) || Timestamp.class.equals(fieldType)) {
								colNamelist.add(
										column.name() + "='" + Constant.FieldDefaultValue.DATE_DEFAULT_VALUE + "'");
							} else if (Integer.TYPE.equals(fieldType) || Integer.class.equals(fieldType)) {
								colNamelist.add(column.name() + "=?");
								objList.add(Constant.FieldDefaultValue.INTEGER_DEFAULT_VALUE);

							} else if (Long.TYPE.equals(fieldType) || Long.class.equals(fieldType)) {
								colNamelist.add(column.name() + "=?");
								objList.add(Constant.FieldDefaultValue.LONG_DEFAULT_VALUE);
							} else if (Float.TYPE.equals(fieldType) || Float.class.equals(fieldType)) {

								colNamelist.add(column.name() + "=?");
								objList.add(Constant.FieldDefaultValue.FLOAT_DEFAULT_VALUE);
							} else {
								continue;
							}
						}
					}
				}
			}
			objList.addAll(idObjList);

			StringBuilder sql = new StringBuilder("update ");
			sql.append(entityClass.getAnnotation(Table.class).name());
			sql.append(" set ").append(colNamelist.toString().replace("[", "").replace("]", ""));
			sql.append(" where ")
					.append(idNameList.toString().replace("[", "").replace("]", "").replaceAll(",", " and "));
			log.debug("sql:" + sql);
			log.debug("param:" + objList);
			updateRows = getJdbcTemplate().update(sql.toString(), objList.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			throw getExceptionTranslator().translate(null, null, new SQLException(e));
		}
		return updateRows;
	}

	/**
	 * 更新，空字段不更新
	 * 
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 */
	public int update(Object obj) throws IllegalAccessException {

		if (null == obj) {
			throw new IllegalAccessException("the object for update can not be null!!");
		}

		int updateRows = 0;
		Class<?> entityClass = obj.getClass(); // 表对应实体的Class
		List<String> colNamelist = new ArrayList<String>();// 列名称
		List<Object> objList = new ArrayList<Object>();// 各列值，与列名对应
		List<String> idNameList = new ArrayList<String>();// ID列名称
		List<Object> idObjList = new ArrayList<Object>();// ID列值

		try {
			Field[] fields = entityClass.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(Column.class)) {
					// 判断updatable属性
					if (!field.getAnnotation(Column.class).updatable()) {
						continue;
					}

					field.setAccessible(true);
					Object o = field.get(obj);
					Column colomn = field.getAnnotation(Column.class);
					if (field.isAnnotationPresent(Id.class)) {
						idNameList.add(colomn.name() + "=?");
						idObjList.add(o);
					} else {
						if (o != null) {
							colNamelist.add(colomn.name() + "=?");
							objList.add(o);
						}
					}
				}
			}
			objList.addAll(idObjList);

			StringBuilder sql = new StringBuilder("update ");
			sql.append(entityClass.getAnnotation(Table.class).name());
			sql.append(" set ").append(colNamelist.toString().replace("[", "").replace("]", ""));
			sql.append(" where ")
					.append(idNameList.toString().replace("[", "").replace("]", "").replaceAll(",", " and "));
			log.debug("sql:" + sql);
			log.debug("param:" + objList.toArray());
			updateRows = getJdbcTemplate().update(sql.toString(), objList.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			getExceptionTranslator().translate(null, null, new SQLException(e));
		}
		return updateRows;
	}

	/**
	 * 根据ID查询单个实体
	 * 
	 * @param entityClass
	 *            表对应实体的Class
	 * @param id
	 * @return
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	public <T> T find(Class<T> entityClass, Object id) throws IllegalAccessException {
		if (null == entityClass || null == id) {
			throw new IllegalAccessException("the entityClass or id for find can not be null!!");
		}

		T entity = null;
		try {
			String idName = entityClass.getAnnotation(Table.class).uniqueConstraints()[0].columnNames()[0];
			StringBuilder sql = new StringBuilder("select ");
			sql.append(getEntityColumnSql(entityClass));
			sql.append(" from ");
			sql.append(entityClass.getAnnotation(Table.class).name());
			sql.append(" where ").append(idName).append("=? limit 1");

			log.debug("sql:" + sql);
			log.debug("param:" + id);

			entity = (T) getJdbcTemplate().queryForObject(sql.toString(), new Object[] { id },
					new EntityMapper(entityClass));
		} catch (Exception e) {
			throw getExceptionTranslator().translate(null, null, new SQLException(e));
		}
		return entity;
	}

	/**
	 * 根据ID删除单个实体
	 * 
	 * @param entityClass
	 *            表对应实体的Class
	 * @param id
	 * @return
	 * @throws IllegalAccessException
	 */
	public int delete(Class<?> entityClass, Object id) throws IllegalAccessException {

		if (null == entityClass || null == id) {
			throw new IllegalAccessException("the entityClass or id for delete can not be null!!");
		}

		int deleteRows = 0;
		String idName = entityClass.getAnnotation(Table.class).uniqueConstraints()[0].columnNames()[0];
		StringBuffer sql = new StringBuffer("delete from ");
		sql.append(entityClass.getAnnotation(Table.class).name());
		sql.append(" where ").append(idName).append("=?");

		log.debug("sql:" + sql);
		log.debug("param:" + id);

		deleteRows = getJdbcTemplate().update(sql.toString(), new Object[] { id });
		return deleteRows;
	}

	/**
	 * 根据ID批量删除
	 * 
	 * @param entityClass
	 *            表对应实体的Class
	 * @param ids
	 * @return
	 * @throws IllegalAccessException
	 */
	public int batchDelete(Class<?> entityClass, Object[] ids) throws IllegalAccessException {

		if (null == entityClass || null == ids) {
			throw new IllegalAccessException("the entityClass or ids for batchDelete can not be null!!");
		}

		int deleteRows = 0;
		try {
			String idName = entityClass.getAnnotation(Table.class).uniqueConstraints()[0].columnNames()[0];
			StringBuilder idsSb = new StringBuilder();
			for (int i = 0; i < ids.length; i++) {
				idsSb.append(ids[i]).append(",");
			}
			if (idsSb.indexOf(",") != -1) {
				idsSb.deleteCharAt(idsSb.lastIndexOf(","));
			}

			StringBuilder sql = new StringBuilder("delete from ");
			sql.append(entityClass.getAnnotation(Table.class).name());
			sql.append(" where ").append(idName).append(" in (").append(idsSb).append(")");// in子集不能超过1000

			log.debug("sql:" + sql);
			log.debug("param:" + idsSb);

			deleteRows = getJdbcTemplate().update(sql.toString());
		} catch (Exception e) {
			getExceptionTranslator().translate(null, null, new SQLException(e));
		}
		return deleteRows;
	}
	
	
	/**
	 * 分页查询
	 * 
	 * @param pageNo
	 *            页号
	 * @param pageSize
	 *            页大小
	 * @param selectCountSql
	 *            查询总记录数的sql
	 * @param selectRecordSql
	 *            查询记录的sql
	 * @param entityClass
	 *            查询记录结果集对应的实体Class对象
	 * @param paramList
	 *            条件参数列表
	 * @return
	 */
	public PageVo queryPagination(int pageNo, int pageSize, String selectCountSql, String selectRecordSql,
			Class<?> entityClass, List<Object> paramList) {

		if (pageNo <= 0 || pageSize <= 0) {
			throw new IllegalArgumentException("the pageNo or pageSize can not be less than 0 !!");
		}

		if (StringUtils.isBlank(selectCountSql) || StringUtils.isBlank(selectRecordSql) || null == entityClass) {
			throw new IllegalArgumentException(
					"the selectCountSql or selectRecordSql or entityClass can not be null!!");
		}

		int count = getJdbcTemplate().queryForObject(selectCountSql, paramList.toArray(),Integer.class);
		PageVo pageVo = new PageVo();
		pageVo.setTotalRecords(count);
		pageVo.setPageSize(pageSize);

		if (0 == count) {
			pageVo.setTotalPage(0);
			pageVo.setPageNo(1);
			return pageVo;
		}

		int totalPage = (0 == count % pageSize) ? count / pageSize : (count / pageSize + 1);

		if (totalPage < pageNo) {// 校正页号，防止因多个用户操作引起总条数变化导致查不到数据
			pageNo = totalPage;
			pageVo.setTotalPage(totalPage);
			pageVo.setPageNo(pageNo);
		}

		paramList.add((pageNo - 1) * pageSize);
		paramList.add(pageSize);
		List<?> list = getJdbcTemplate().query("select * from (" + selectRecordSql + ") t999999999 limit ?,?",
				 paramList.toArray(),new EntityMapper(entityClass));
		pageVo.setList(list);

		return pageVo;
	}

}
