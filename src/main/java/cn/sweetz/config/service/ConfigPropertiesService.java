package cn.sweetz.config.service;

import java.util.List;

import cn.sweetz.config.entity.ConfigProperties;

public interface ConfigPropertiesService {

	/**
	 * 添加配置信息
	 * @param configProperties
	 * @return
	 */
	int insertOrUpdateConfigPeoperties(ConfigProperties configProperties);
	
	/**
	 * 根据应用名和profile来查询
	 * @param application 应用名
	 * @param profile
	 * @return
	 */
	List<ConfigProperties> selectByApplicationAndProfile(String application , String profile);
	
	/**
	 * 根据id删除配置信息
	 * @param id
	 * @return
	 */
	int deleteConfigPropertiesById(Integer id);
	
	/**
	 * 根据应用名和profile来发布所有未发布的配置
	 * @param application 应用名
	 * @param profile 
	 * @return
	 */
	int publishAllByApplicationAndProfile(String application , String profile);
}
