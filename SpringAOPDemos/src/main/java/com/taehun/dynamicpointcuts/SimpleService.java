package com.taehun.dynamicpointcuts;

public class SimpleService {
	
	public void performTask() {
		System.out.println("Performing a task");
		internalTask();
	}
	
	public void internalTask() {
		System.out.println("Performing internal task");
	}
}
