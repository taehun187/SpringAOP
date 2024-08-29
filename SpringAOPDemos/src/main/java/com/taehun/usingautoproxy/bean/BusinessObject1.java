package com.taehun.usingautoproxy.bean;

import org.springframework.transaction.annotation.Transactional;

import com.taehun.usingautoproxy.service.BusinessService;

public class BusinessObject1 implements BusinessService {
	
	@Transactional
	public void process() {
		System.out.println("Processing in BusinessObject1");
	}
	
}
