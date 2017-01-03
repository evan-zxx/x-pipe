package com.ctrip.xpipe.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.xpipe.api.monitor.Task;
import com.ctrip.xpipe.api.monitor.TransactionMonitor;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;

/**
 * @author wenchao.meng
 *
 * Jan 3, 2017
 */
public class CatTransactionMonitor implements TransactionMonitor{
	
	public static Logger logger = LoggerFactory.getLogger(CatTransactionMonitor.class);
	
	@Override
	public void logTransaction(String type, String name, Task task) throws Throwable {
		
		Transaction transaction = Cat.newTransaction(type, name);
		try{
			task.go();
			transaction.setStatus(Transaction.SUCCESS);
		}catch(Throwable th){
			transaction.setStatus(th);
			throw th;
		}finally{
			transaction.complete();
		}
	}

	@Override
	public void logTransactionSwallowException(String type, String name, Task task) {
		
		Transaction transaction = Cat.newTransaction(type, name);
		try{
			task.go();
			transaction.setStatus(Transaction.SUCCESS);
		}catch(Throwable th){
			transaction.setStatus(th);
			logger.error("[logTransaction]" + type + "," + name + "," + task, th);
		}finally{
			transaction.complete();
		}
	}

}
