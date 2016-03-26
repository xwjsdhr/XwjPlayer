package com.hw.common.db;

import java.util.Map;
import java.util.WeakHashMap;
/**
 * bean管理器,单例,维护一个bean缓存池
 * @author Li
 *
 */
public class BeanManage {
	private static BeanManage manage;
	public static synchronized BeanManage self(){
		if(manage==null){
			manage=new BeanManage();
		}
		return manage;
	}
	
	
	private Map<Class<?>, BeanInfo> beanInfoCache;
	private BeanManage(){
		beanInfoCache = new WeakHashMap<Class<?>, BeanInfo>();
	}

	
	public BeanInfo getBeanInfo(Class<?> clazz) {
		BeanInfo beanInfo=this.beanInfoCache.get(clazz);
		if(beanInfo==null){
			beanInfo=new BeanInfo(clazz);
		}
		return beanInfo;
	}

	BeanInfo putBeanInfo(Class<?> clazz, BeanInfo info) {
		return this.beanInfoCache.put(clazz, info);
	}

	public void removeBeanInfo(Class<?> clazz) {
		if (this.beanInfoCache != null) {
			this.beanInfoCache.remove(clazz);
		}
	}

	public void clearBeanInfoCache() {
		if (this.beanInfoCache != null) {
			this.beanInfoCache.clear();
		}
	}
}
