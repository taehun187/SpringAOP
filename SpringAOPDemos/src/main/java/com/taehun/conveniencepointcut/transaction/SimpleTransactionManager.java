package com.taehun.conveniencepointcut.transaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

public class SimpleTransactionManager implements PlatformTransactionManager {
	
	@Override
	public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException{
		System.out.println("Transaction started");
		return (TransactionStatus) new SimpleTransactionManager();
	}
	
	@Override
	public void commit(TransactionStatus status) throws TransactionException {
		System.out.println("Transaction committed");
	}
	
	@Override
	public void rollback(TransactionStatus status) throws TransactionException {
		System.out.println("Transaction rolled back");
	}
	

}
