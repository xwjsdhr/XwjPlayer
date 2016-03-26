package com.hw.common.db;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hw.common.utils.basicUtils.MLogUtil;
import com.hw.common.utils.basicUtils.StringUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressWarnings("unchecked")
public class DbManage {
	/**
	 * 获取数据库管理器
	 * 
	 * @param ctx
	 *            上下文
	 * @return 数据库管理器
	 */
	public static DbManage getDbManage(Context ctx) {
		if (ctx == null) {
			return null;
		}
		MLogUtil.e("获取一个数据库管理器");
		return new DbManage(ctx);
	}

	// 主键
	public static final String KEY_NAME = "primaryKeyId";
	// 数据库适配器
	private static DbAdapter dbAdapter = null;

	public static void setDbAdapter(DbAdapter dbAdapter) {
		DbManage.dbAdapter = dbAdapter;
	}

	// 上下文
	private Context context;
	// 数据库Helper
	private DatabaseHelper DBHelper;
	// 数据库操作对象
	private SQLiteDatabase db;

	private DbManage(Context ctx) {
		context = ctx;
		DBHelper = new DatabaseHelper(context);
		db = DBHelper.getWritableDatabase();
	}

	/**
	 * 获取数据库操作对象
	 */
	public SQLiteDatabase getSQLite() {
		return db;
	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		if(db != null) db.close(); MLogUtil.e("db.close()");
		if(DBHelper != null) DBHelper.close(); MLogUtil.e("DBHelper.close()");
	}
	
	/**
	 * 向数据库中插入数据bean
	 */
	public <T extends BaseEntity> long replace(BaseEntity entity) {
		if (entity == null) {
			return 0;
		}
		Class<T> clazz = (Class<T>) entity.getClass();
		Table t = clazz.getAnnotation(Table.class);
		if (t == null) {
			return 0;
		}
		Date date = new Date();
		entity.setCreateDate(date.getTime());
		entity.setModifyDate(date.getTime());

		ContentValues cv = new ContentValues();
		BeanInfo info = BeanManage.self().getBeanInfo(clazz);
		PropertyInfo[] pis = info.getPropertyInfos();
		for (PropertyInfo pi : pis) {
			try {
				Object result = pi.getReadMethod().invoke(entity, new Object[0]);
				if (result != null) {
					cv.put(pi.getName(), result.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		MLogUtil.e("数据库表插入：TableName:" + t.name() + " values:" + cv);
		try {
			return db.replace(t.name(), null, cv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 向数据库中插入数据bean
	 */
	public <T extends BaseEntity> long insert(BaseEntity entity) {
		if (entity == null) {
			return 0;
		}
		Class<T> clazz = (Class<T>) entity.getClass();
		Table t = clazz.getAnnotation(Table.class);
		if (t == null) {
			return 0;
		}
		Date date = new Date();
		entity.setCreateDate(date.getTime());
		entity.setModifyDate(date.getTime());

		ContentValues cv = new ContentValues();
		BeanInfo info = BeanManage.self().getBeanInfo(clazz);
		PropertyInfo[] pis = info.getPropertyInfos();
		for (PropertyInfo pi : pis) {
			try {
				Object result = pi.getReadMethod().invoke(entity, new Object[0]);
				if (result != null) {
					cv.put(pi.getName(), result.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		MLogUtil.e("数据库表插入：TableName:" + t.name() + " values:" + cv);
		try {
			return db.insert(t.name(), null, cv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 更新一个bean,如果id为null,则失败
	 * 
	 * @param entity
	 * @param where
	 *            修改条件，如"id=1"
	 * @return boolean
	 */
	public <T extends BaseEntity> boolean update(BaseEntity entity, String where) {
		if (entity == null) {
			return false;
		}
		Class<T> clazz = (Class<T>) entity.getClass();
		Table t = clazz.getAnnotation(Table.class);
		if (t == null) {
			return false;
		}
		entity.setModifyDate(new Date().getTime());

		ContentValues cv = new ContentValues();
		BeanInfo info = BeanManage.self().getBeanInfo(clazz);
		PropertyInfo[] pis = info.getPropertyInfos();
		for (PropertyInfo pi : pis) {
			try {
				Object result = pi.getReadMethod().invoke(entity, new Object[0]);
				if (result != null) {
					cv.put(pi.getName(), result.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		MLogUtil.e("数据库表更新：TableName:" + t.name() + " values:" + cv + " where " + where);
		try {
			return db.update(t.name(), cv, where, null) > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
	
	/**
	 * 向数据库中插入数据bean
	 */
	public <T extends BaseEntity> Boolean replace(BaseEntity entity, String where) {
		if (entity == null) {
			return false;
		}
		
		Class<T> clazz = (Class<T>) entity.getClass();
		Table t = clazz.getAnnotation(Table.class);
		if (t == null) {
			return false;
		}
		
		if(getBean(clazz, where) == null){
			insert(entity);
		}else{
			update(entity, where);
		}
		return true;
	}

	/**
	 * 通用删除一个bean
	 * 
	 * @param where
	 *            删除条件，如"id=1"
	 * @return boolean
	 */
	public <T extends BaseEntity> boolean delete(Class<T> clazz, String where) {
		if (clazz == null) {
			return false;
		}
		Table t = clazz.getAnnotation(Table.class);
		if (t == null) {
			return false;
		}
		MLogUtil.e("数据库表删除：TableName:" + t.name() + " where " + where);
		try {
			return db.delete(t.name(), where, null) > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// 删除所有表内数据
	public <T extends BaseEntity> boolean clearAll(Class<T> clazz) {
		if (clazz == null) {
			return false;
		}
		Table t = clazz.getAnnotation(Table.class);
		if (t == null) {
			return false;
		}
		MLogUtil.e("数据库表删除：TableName:" + t.name());
		try {
			return db.delete(t.name(),null,null) > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除一个bean
	 * 
	 * @param id
	 * @return boolean
	 */
	public <T extends BaseEntity> boolean delete(Class<T> clazz, Integer id) {
		if (clazz == null || id < 0) {
			return false;
		}
		Table t = clazz.getAnnotation(Table.class);
		if (t == null) {
			return false;
		}
		MLogUtil.e("数据库表删除：TableName:" + t.name() + " where primaryKeyId=" + id);
		try {
			return db.delete(t.name(), "primaryKeyId=?", new String[] { Integer.toString(id) }) > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 查询所有
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public List<Map<String, Object>> getList(String sql, String[] selectionArgs) {
		// 查询
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Cursor cur = db.rawQuery(sql, selectionArgs);
			while (cur.moveToNext()) {
				Map<String, Object> map = new HashMap<String, Object>();
				String[] names = cur.getColumnNames();
				for (String n : names) {
					map.put(n, cur.getString(cur.getColumnIndex(n)));
				}
				list.add(map);
			}
			cur.close();
			MLogUtil.e("数据库表查询列表：getList数量:" + list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 查询所有
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public <T> List<T> getList(Class<T> clazz, String where, String[] selectionArgs) {
		// 查询
		BeanInfo info = BeanManage.self().getBeanInfo(clazz);
		if (info == null) {
			return null;
		}
		Table t = clazz.getAnnotation(Table.class);
		if (t == null) {
			return null;
		}

		PropertyInfo[] pis = info.getPropertyInfos();
		String sql = "select * from " + t.name();
		if (!StringUtils.isEmpty(where)) {
			sql = sql + " where " + where;
		}
		List<T> list = new ArrayList<T>();
		try {
			Cursor cur = db.rawQuery(sql, selectionArgs);
			while (cur.moveToNext()) {
				Map<String, Object> map = new HashMap<String, Object>();
				String[] names = cur.getColumnNames();
				for (String n : names) {
					map.put(n, cur.getString(cur.getColumnIndex(n)));
				}

				Object obj = clazz.newInstance();
				for (PropertyInfo pi : pis) {
					String propertyName = pi.getName();
					if (map.containsKey(propertyName)) {
						Object value = map.get(propertyName);
						pi.getWriteMethod().invoke(obj, getValueByType(pi.getField().getType(), value));
					}
				}
				list.add((T) obj);
			}
			cur.close();
			MLogUtil.e("数据库表查询列表：getList数量:" + list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 查询所有
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public <T> List<T> getList(String sql,Class<T> clazz, String[] selectionArgs) {
		// 查询
		BeanInfo info = BeanManage.self().getBeanInfo(clazz);
		if (info == null) {
			return null;
		}
		PropertyInfo[] pis = info.getPropertyInfos();
		List<T> list = new ArrayList<T>();
		try {
			Cursor cur = db.rawQuery(sql, selectionArgs);
			while (cur.moveToNext()) {
				Map<String, Object> map = new HashMap<String, Object>();
				String[] names = cur.getColumnNames();
				for (String n : names) {
					map.put(n, cur.getString(cur.getColumnIndex(n)));
				}

				Object obj = clazz.newInstance();
				for (PropertyInfo pi : pis) {
					String propertyName = pi.getName();
					if (map.containsKey(propertyName)) {
						Object value = map.get(propertyName);
						pi.getWriteMethod().invoke(obj, getValueByType(pi.getField().getType(), value));
					}
				}
				list.add((T) obj);
			}
			cur.close();
			MLogUtil.e("数据库表查询列表：getList数量:" + list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 查询map对象
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public Map<String, Object> getMap(String sql, String[] selectionArgs) {
		try {
			Cursor cur = db.rawQuery(sql, selectionArgs);
			if (cur.getCount() > 0) {
				cur.moveToFirst();
				Map<String, Object> map = new HashMap<String, Object>();
				String[] names = cur.getColumnNames();
				for (String n : names) {
					map.put(n, cur.getString(cur.getColumnIndex(n)));
				}
				cur.close();
				return map;
			}
			cur.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询条数
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public Integer getCount(String sql, String[] selectionArgs) {
		try {
			Cursor cur = db.rawQuery("select count(*) as count_tmp from " + sql + " ", selectionArgs);
			if (cur.moveToFirst()) {
				Integer r = cur.getInt(0);
				cur.close();
				MLogUtil.e("数据库查询条数： " + r);
				return r;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 分页查询
	 * 
	 * @param pager
	 *            (第几页，每页个数)
	 * @param tableName
	 *            表名称
	 * @param
	 * @return
	 */
	public Pager getPager(Pager pager, String tableName) {
		String sql = "select * from " + tableName + " order by " + pager.getSort() + " " + pager.getOrder() + " limit ?,?";
		pager.setList(getList(sql, new String[] { String.valueOf(pager.getStartItem()), String.valueOf(pager.getRows()) }));
		return pager;
	}

	/**
	 * 查询单个bean
	 * 
	 * @param clazz
	 * @param id
	 * @param selectionArgs
	 * @return
	 */
	public <T> T getBean(Class<T> clazz, Integer id) {
		if (clazz == null || id < 0) {
			return null;
		}
		Table t = clazz.getAnnotation(Table.class);
		if (t == null) {
			return null;
		}
		String sql = "select * from " + t.name() + " where primaryKeyId = ? ";
		Map<String, Object> map = getMap(sql, new String[] { Integer.toString(id) });
		if (map == null)
			return null;
		try {
			BeanInfo info = BeanManage.self().getBeanInfo(clazz);
			Object obj = clazz.newInstance();
			PropertyInfo[] pis = info.getPropertyInfos();
			for (PropertyInfo pi : pis) {
				String propertyName = pi.getName();
				if (map.containsKey(propertyName)) {
					Object value = map.get(propertyName);
					pi.getWriteMethod().invoke(obj, getValueByType(pi.getField().getType(), value));
				}
			}
			return (T) obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public <T> T getBean(Class<T> clazz, String sql, Integer id) {
		if (clazz == null) {
			return null;
		}
		Map<String, Object> map = getMap(sql, null);
		if (map == null)
			return null;
		try {
			BeanInfo info = BeanManage.self().getBeanInfo(clazz);
			Object obj = clazz.newInstance();
			PropertyInfo[] pis = info.getPropertyInfos();
			for (PropertyInfo pi : pis) {
				String propertyName = pi.getName();
				if (map.containsKey(propertyName)) {
					Object value = map.get(propertyName);
					pi.getWriteMethod().invoke(obj, getValueByType(pi.getField().getType(), value));
				}
			}
			return (T) obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T> T getBean(Class<T> clazz, String where) {
		if (clazz == null) {
			return null;
		}
		Table t = clazz.getAnnotation(Table.class);
		if (t == null) {
			return null;
		}
		String sql = "select * from " + t.name() + " where " + where;
		Map<String, Object> map = getMap(sql, null);
		if (map == null)
			return null;
		try {
			BeanInfo info = BeanManage.self().getBeanInfo(clazz);
			Object obj = clazz.newInstance();
			PropertyInfo[] pis = info.getPropertyInfos();
			for (PropertyInfo pi : pis) {
				String propertyName = pi.getName();
				if (map.containsKey(propertyName)) {
					Object value = map.get(propertyName);
					pi.getWriteMethod().invoke(obj, getValueByType(pi.getField().getType(), value));
				}
			}
			return (T) obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, dbAdapter.getDbName(), null, dbAdapter.getDbVersion());
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			dbAdapter.onCreate(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			dbAdapter.onUpgrade(db, oldVersion, newVersion);
		}
	}

	public static abstract class DbAdapter {
		public abstract String getDbName();

		public abstract int getDbVersion();

		public abstract Class<?>[] getBeanClass();

		public void onCreate(SQLiteDatabase db) {
			Class<?>[] clazzes = getBeanClass();
			for (Class<?> clazz : clazzes) {
				Table t = clazz.getAnnotation(Table.class);

				BeanInfo beanInfo = BeanManage.self().getBeanInfo(clazz);
				PropertyInfo[] pis = beanInfo.getPropertyInfos();

				Cursor tableInf = db.query("sqlite_master", new String[] { "sql" }, "type='table' and name='" + t.name() + "'", null, null, null, null);
				if (tableInf.getCount() > 0) {
					tableInf.moveToFirst();
					String csql = tableInf.getString(tableInf.getColumnIndex("sql"));
					String[] tempRows = csql.substring(csql.indexOf("(") + 1, csql.indexOf(")")).split(",");
					for (int i = 0; i < pis.length; i++) {
						boolean have = false;
						PropertyInfo descriptor = pis[i];
						Class<?> pt = descriptor.getField().getClass();
						if (descriptor.getReadMethod() == null || descriptor.getWriteMethod() == null) {
							continue;
						}

						for (String row : tempRows) {
							if (row.split(" ")[0].equals(descriptor.getName())) {
								have = true;
								break;
							}
						}
						if (!have) {
							String addsql = descriptor.getName() + " " + DbManage.getDbType(pt);
							if (addsql != null) {
								db.execSQL("ALTER TABLE " + t.name() + " ADD COLUMN " + addsql);
							}
						}
					}
				} else {
					StringBuffer sql = new StringBuffer("CREATE TABLE IF NOT EXISTS " + t.name() + " (");
					for (int i = 0; i < pis.length; i++) {
						PropertyInfo pi = pis[i];
						if (DbManage.KEY_NAME.equals(pi.getName())) {
							sql.append(DbManage.KEY_NAME + " integer primary key AUTOINCREMENT");
						} else {
							sql.append(pi.getName() + " " + DbManage.getDbType(pi.getField().getType()));
						}
						if (i == pis.length - 1) {
							sql.append(")");
						} else {
							sql.append(",");
						}
					}
					db.execSQL(sql.toString());
				}
				tableInf.close();
			}
		}

		public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	}

	public static String getDbType(Class<?> clazz) {
		if (clazz == null) {
			return "TEXT";
		}
		if (clazz.equals(Integer.class) || clazz.equals(int.class) || clazz.equals(Long.class) || clazz.equals(long.class) || clazz.equals(Short.class) || clazz.equals(short.class)) {
			return "INTEGER";
		} else if (clazz.equals(Double.class) || clazz.equals(double.class) || clazz.equals(Float.class) || clazz.equals(float.class)) {
			return "REAL";
		} else {
			return "TEXT";
		}
	}

	public static Object getValueFromCursor(Class<?> clazz, Cursor cur, String name) {
		if (clazz == null || cur == null || name == null) {
			return null;
		}
		if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
			return cur.getInt(cur.getColumnIndex(name));
		} else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
			return cur.getDouble(cur.getColumnIndex(name));
		} else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
			return cur.getFloat(cur.getColumnIndex(name));
		} else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
			return cur.getLong(cur.getColumnIndex(name));
		} else if (clazz.equals(Short.class) || clazz.equals(short.class)) {
			return cur.getShort(cur.getColumnIndex(name));
		} else if (clazz.equals(String.class)) {
			return cur.getString(cur.getColumnIndex(name));
		} else {
			return null;
		}
	}

	public static Object getValueByType(Class<?> clazz, Object value) {
		if (clazz == null || value == null) {
			return null;
		}
		if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
			return Integer.parseInt(value + "");
		} else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
			return Double.parseDouble(value + "");
		} else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
			return Float.parseFloat(value + "");
		} else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
			return Long.parseLong(value + "");
		} else if (clazz.equals(Short.class) || clazz.equals(short.class)) {
			return Short.parseShort(value + "");
		} else if (clazz.equals(String.class)) {
			return String.valueOf(value);
		}else if (clazz.equals(Date.class)) {
			Date date = new Date();
			date.setTime(Date.parse(String.valueOf(value)));
			return date;
		} else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class) ) {
			return Boolean.valueOf(value.toString());
		} else {
			return null;
		}

	}
}
