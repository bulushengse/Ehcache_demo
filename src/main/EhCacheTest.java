package main;

import java.io.File;
import java.net.URL;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.PersistentUserManagedCache;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.builders.UserManagedCacheBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.spi.service.LocalPersistenceService;
import org.ehcache.impl.config.persistence.DefaultPersistenceConfiguration;
import org.ehcache.impl.config.persistence.UserManagedPersistenceContext;
import org.ehcache.impl.persistence.DefaultLocalPersistenceService;
import org.ehcache.xml.XmlConfiguration;
import org.junit.Test;

public class EhCacheTest {
	
	//由于ehcache2.x与ehcache3.x版本使用变化较大，不适用ehcache2.x版本
	//以下介绍3.4新版本的使用
	//1.编程方式配置CacheManager	
	//创建缓存管理器
	public CacheManager createCacheManager() {
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
				.withCache("myCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
						ResourcePoolsBuilder.heap(10)))
				.build();
		// 初始化缓存管理器
		cacheManager.init();
		return cacheManager;
	}
	
	//2.xml配置CacheManager	
	//创建缓存管理器
	public CacheManager createCacheManager2() {
		URL location = getClass().getResource("/ehcache.xml");
		//System.out.println(location.toString());
		XmlConfiguration xmlConfiguration = new XmlConfiguration(location);
		CacheManager cacheManager = CacheManagerBuilder.newCacheManager(xmlConfiguration);
		// 初始化缓存管理器
		cacheManager.init();
		//Cache cache = cacheManager.getCache("mytest", String.class, String.class);
		return cacheManager;
	}
	
	
	//由缓存管理器获取缓存实例 
	@Test
	public void getCache() {
		
		CacheManager cacheManager = createCacheManager();
		//CacheManager cacheManager = createCacheManager2();
		
		Cache cache = cacheManager.getCache("myCache", String.class, String.class);
		
		//使用缓存
		cache.put("q1", "zheng pens");
		cache.put("q2", "pens");// 覆盖上一个值
		
		System.out.println(cache.get("q1"));  
		System.out.println(cache.containsKey("q1")); 
		// 不存在的key-value返回null
		System.out.println(cache.get("q2"));
		// 不允许值为null，报空指针错误
		System.out.println(cache.get("q3"));
		
		//清空缓存，关闭缓存管理器
		cache.clear();
		cacheManager.close();
	}
	
	// 由缓存管理器直接创建缓存实例
	@Test
	public void createCache() {
		Cache cache3 = createCacheManager().createCache("myCache3",CacheConfigurationBuilder
						.newCacheConfigurationBuilder(Integer.class, String.class, ResourcePoolsBuilder.heap(5))
						// .withEvictionAdvisor(new CustomEvictionAdvisor())
						.build());

		cache3.put(100, "hello");
		
		System.out.println(cache3.get(100));
	}
	 
	//缓存服务
    @Test
    public void testPersistenceCache() throws Exception {  
        LocalPersistenceService persistenceService = new DefaultLocalPersistenceService(  
                new DefaultPersistenceConfiguration(new File("E:\\")));  
  
        PersistentUserManagedCache<Long, String> cache = UserManagedCacheBuilder.newUserManagedCacheBuilder(Long.class, String.class)  
                .with(new UserManagedPersistenceContext<Long, String>("persistentCache", persistenceService))  
                .withResourcePools(ResourcePoolsBuilder.newResourcePoolsBuilder()  
                        .disk(10L, MemoryUnit.MB, true))  
                .build(true);  
        // 把缓存只存进硬盘里，只要persistenceService相同，即使关闭，再次启动还是可以读取数据  
        cache.put(42L, "The Answer!");  
        System.out.println(cache.get(42L));  
        //cache.remove(44L);  
        System.out.println("??????????????");
        // 手动关闭和销毁  
        cache.close();  
        // 删除硬盘上的缓存  
        //cache.destroy();  
  
        // 停止服务  
        persistenceService.stop();  
    }  
    
}
