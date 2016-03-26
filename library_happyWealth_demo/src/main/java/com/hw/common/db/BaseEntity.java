package com.hw.common.db;

public class BaseEntity {
	private Integer primaryKeyId;// 自增id
	private Long createDate;// 创建日期
	private Long modifyDate;// 修改日期

	public Integer getPrimaryKeyId() {
		return primaryKeyId;
	}

	public void setPrimaryKeyId(Integer primaryKeyId) {
		this.primaryKeyId = primaryKeyId;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public Long getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Long modifyDate) {
		this.modifyDate = modifyDate;
	}
}
