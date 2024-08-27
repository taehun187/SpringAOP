package com.taehun.springadvices.advices.introduction;

public class LockedException extends RuntimeException {

	public LockedException()   {
		super("Other is locked. ");
	}
	
	
}
