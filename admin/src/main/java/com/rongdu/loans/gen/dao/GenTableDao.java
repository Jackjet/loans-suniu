/**
 *Copyright 2014-2017 聚宝钱包 All rights reserved.
 */
package com.rongdu.loans.gen.dao;

import com.rongdu.common.persistence.CrudDao;
import com.rongdu.common.persistence.annotation.MyBatisDao;
import com.rongdu.loans.gen.entity.GenTable;

/**
 * 业务表DAO接口
 * @author sunda
 * @version 2013-10-15
 */
@MyBatisDao
public interface GenTableDao extends CrudDao<GenTable> {
	
}
