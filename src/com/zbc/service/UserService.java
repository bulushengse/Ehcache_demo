package com.zbc.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.zbc.bean.User;
import com.zbc.dao.impl.DaoSupport;


@Service("userService")
//@CacheConfig  //如果类的所有操作都是缓存操作，你可以使用@CacheConfig来指定类，省去一些配置。
public class UserService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	//目前Spring版本仅支持Ehcache2.5以上版本，不支持Ehcache3。
	
	/**
	 * 下面三个注解都是方法级别：
	 * 
	 * @Cacheable 当第一次调用这个方法时，它的结果会被缓存下来，在缓存的有效时间内，以后访问这个方法都直接返回缓存结果，不再执行方法中的代码段。
	 *            使用condition属性来设置条件，如果不满足条件，就不使用缓存能力，直接执行方法。
	 *            使用key属性来指定key的生成规则。
	 * 
	 * @CachePut 与@Cacheable不同，@CachePut不仅会缓存方法的结果，还会执行方法的代码段。
	 *           它支持的属性和用法都与@Cacheable一致。
	 * 
	 * @CacheEvict 与@Cacheable功能相反，@CacheEvict表明所修饰的方法是用来删除失效或无用的缓存数据。
	 *         
	 */
	
	/**
	 * 新增用户
	 * @param pd
	 * @throws Exception
	 */
	public void saveU(User user) throws Exception {
		dao.save("UserMapper.saveU", user);
	}

	/**
	 * 修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editU(User user) throws Exception {
		dao.update("UserMapper.editU", user);
	}


	/**
	 * 删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteU(User user) throws Exception {
		dao.delete("UserMapper.deleteU", user);
	}


	/**
	 * 用户列表(全部)
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	// @Cacheable({"userCache"})   //使用指定缓存实例
	// @Cacheable({"userCache", "userCache2"})  //使用多个缓存实例
	// @Cacheable(value = "userCache", key="#user.getUSER_ID()")  //设置键
	// @Cacheable(value = "userCache", condition = "#user.getUSER_ID() <= 2") //设置键并加条件
	
	//如果需要使用同一个缓存注解（@Cacheable、@CacheEvict或@CachePut）多次修饰一个方法，就需要用到@Caching。
    //@Caching(evict = { @CacheEvict("userCache"), @CacheEvict(cacheNames="userCache2", key="#user.getUSER_ID()") })
	
	@Cacheable({"userCache"})   //使用指定缓存实例  //使用默认缓存实例   如果有缓存不会执行方法中的代码
	public List<User> listAll(User user) throws Exception {
		System.out.println("正在执行方法listAll。。。");
		return (List<User>) dao.findForList("UserMapper.listAll", user);
		
	}


	/**
	 * 通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@CachePut(value = "userCache2", key = "#user.getUSER_ID()") //不管有没有缓存都会执行方法中的代码
	public User findById(User user) throws Exception {
		System.out.println("正在执行方法findById。。。");
		return (User) dao.findForObject("UserMapper.findById", user);
	}
	
	
	//删除缓存userCache数据  allEntries=true清除所有的缓存,key="xxx"清除指定key的缓存
	@CacheEvict(value = "userCache", allEntries=true) 
	public List<User> delCache(User user) throws Exception {
		return (List<User>) dao.findForList("UserMapper.listAll", user);
	}

	
	
	

}
