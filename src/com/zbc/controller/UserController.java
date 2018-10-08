package com.zbc.controller;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zbc.bean.User;
import com.zbc.service.UserService;

public class UserController {

	
	//模拟业务控制层。。。
	//spring-context-support这个jar包中含有Spring对于缓存功能的抽象封装接口。
	
	/**
	 * 获取服务层实例
	 * @param name
	 * @return
	 */
	public Object getBean(String name) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/ApplicationContext.xml");  
	    Object service = context.getBean((String) name);
	    return service;
	}
	
	
	@Test
	public void listAll() throws Exception {			 
		UserService userService = (UserService) getBean("userService");
		User user = new User();
		System.out.println("--------------------第一次查询开始--------------------------");
		List<User> list = userService.listAll(user);
		System.out.println("无缓存查询所有用户数据---"+list);
		System.out.println("--------------------第一次查询结束--------------------------");
		System.out.println("--------------------第二次查询开始--------------------------");
		List<User> list2 = userService.listAll(user);
		System.out.println("缓存用户数据---"+list2);
		System.out.println("--------------------第二次查询结束--------------------------");
	}
	
	@Test
	public void findUser() throws Exception {
		UserService userService = (UserService) getBean("userService");
		User user = new User();
		user.setUSER_ID("1");
		System.out.println("--------------------第一次查询开始--------------------------");
		User data1 = userService.findById(user);
		System.out.println("无缓存查询用户数据---"+data1);
		System.out.println("--------------------第一次查询结束--------------------------");
		System.out.println("--------------------第二次查询开始--------------------------");
		User data2 = userService.findById(user);
		System.out.println("无缓存查询用户数据---"+data2);
		System.out.println("--------------------第二次查询结束--------------------------");
	}
	
	@Test
	public void delCache() throws Exception {
		UserService userService = (UserService) getBean("userService");
		User user = new User();
		System.out.println("--------------------第一次查询开始--------------------------");
		List<User> list = userService.listAll(user);
		System.out.println("无缓存查询所有用户数据---"+list);
		System.out.println("--------------------第一次查询结束--------------------------");
		
		userService.delCache(user);
		System.out.println("------已删除缓存数据");
		
		System.out.println("--------------------第二次查询开始--------------------------");
		List<User> list2 = userService.listAll(user);
		System.out.println("无缓存查询所有用户数据---"+list2);
		System.out.println("--------------------第二次查询结束--------------------------");
	}
	
	
	
}
