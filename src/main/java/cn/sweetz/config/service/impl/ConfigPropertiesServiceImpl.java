package cn.sweetz.config.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.sweetz.config.entity.ConfigProperties;
import cn.sweetz.config.service.ConfigPropertiesService;

@Service
public class ConfigPropertiesServiceImpl implements ConfigPropertiesService {
	
	private static final String SQL_CLOUMN=" id , pkey , pvalue , before_value as beforeValue , application , profile , "
			+ "label , is_publish as isPublish , create_time as createTime , update_time as updateTime , is_deleted as isDeleted";

	private static RowMapper<ConfigProperties> rowMapper = new BeanPropertyRowMapper<>(ConfigProperties.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public int insertOrUpdateConfigPeoperties(ConfigProperties configProperties) {
		
		//校验必填参数
		if(configProperties == null 
				|| StringUtils.isEmpty(configProperties.getApplication())
				|| StringUtils.isEmpty(configProperties.getProfile())
				|| StringUtils.isEmpty(configProperties.getPkey())
				|| StringUtils.isEmpty(configProperties.getPvalue())) {
			return 0;
		}
		
		String sql = "";
		int num = 0;
		if(configProperties.getId() != null && configProperties.getId().intValue() != 0) {
			String querySql = "select " + SQL_CLOUMN + " from conifg_properties where id = " + configProperties.getId();
			ConfigProperties old = jdbcTemplate.queryForObject(querySql, rowMapper);
			if(old != null) {
				sql = "update set pkey = ? , pvalue = ? ,  before_value = ? , application = ? , profile = ? , label = ? ,"
						+ " is_publish = ? , update_time = ? where id = ?";
				num = jdbcTemplate.update(sql, new Object[] {configProperties.getPkey() , configProperties.getPvalue() ,
							old.getPvalue() , configProperties.getApplication() ,configProperties.getProfile(),
							configProperties.getLabel() , "0" , new Date() , old.getId()});
			}
		}else {
			sql = "insert into (pkey , pvalue ,application ,profile,label,is_publish,create_time ) "
					+ "values( ? , ? , ? ,? ,  ? ,? , ? )";
			num = jdbcTemplate.update(sql, new Object[] {configProperties.getPkey() , configProperties.getPvalue() ,
						 configProperties.getApplication() ,configProperties.getProfile(),configProperties.getLabel() , 
						 "0" , new Date()});
		}
		return num;
	}

	@Override
	public List<ConfigProperties> selectByApplicationAndProfile(String application, String profile) {

		String sql = "select " + SQL_CLOUMN + " from conifg_properties where "
				+ "application = '" + application + "' and profile = '" + profile + "' and is_deleted = '0'";
		
		List<ConfigProperties> configList = jdbcTemplate.query(sql , rowMapper);
		return configList;
	}

	@Override
	public int deleteConfigPropertiesById(Integer id) {
		
		String querySql = "select " + SQL_CLOUMN + " from conifg_properties where id = " + id;
		//1.查询数据，判断参数是否有误
		ConfigProperties configProperties = jdbcTemplate.queryForObject(querySql, rowMapper);
		if(configProperties != null) {
			//2.设置删除标识和发布标识
			String updateSql = "update conifg_properties set is_deleted = '1' where id = " + id;
			//3.修改数据
			return jdbcTemplate.update(updateSql);
		}
		
		return 0;
	}

	@Override
	public int publishAllByApplicationAndProfile(String application, String profile) {
		String updateSql = "update conifg_properties set is_publish = '1' where application = '" + application + "' and profile = '" + profile + "'";
		return jdbcTemplate.update(updateSql);
	}

}
