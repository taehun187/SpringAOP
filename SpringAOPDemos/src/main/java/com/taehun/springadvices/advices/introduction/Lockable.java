package com.taehun.springadvices.advices.introduction;

public interface Lockable {  
	  void lock();  
	  void unlock();  
	  boolean locked(); // 락킹 상태를 알려주는 메서드 
	  
}
