package com.notification.send;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.notification.database.QueryExecutors;
import com.notification.domain.Books;
import com.notification.domain.Conditions;
import com.notification.domain.Subscriber_Condition;

public class SendNotification implements Runnable {
	
	private final BlockingQueue<Books> queue;
	public SendNotification(BlockingQueue<Books>queue) {
		// TODO Auto-generated constructor stub
		this.queue = queue;
	}
	
	public void sendNotification(BlockingQueue<Books>bookList){
		System.out.println("sendnotification method "+bookList.size());
		List <Subscriber_Condition> subCon = null;
		List <Integer> subIds = null;
		List <Conditions> conditions = null;
		QueryExecutors queryExecutors=QueryExecutors.getInstance();
		while(true) {
			 try {
				 Books book = bookList.take();
				 subIds = queryExecutors.getSubscriberIds(book.getId());
				 if(subIds == null || subIds.isEmpty()) {
					 continue;
				 }
				 subCon = QueryExecutors.getSubCondition(subIds, book.getId());
				 if(subCon == null || subCon.isEmpty()) {
					 continue;
				 }
				 conditions = QueryExecutors.getCondition(subCon);
				 if(conditions == null || conditions.isEmpty()) {
					 continue;
				 }
				 System.out.println(Books.checkCondition(book, conditions.get(0).getAttribute(), conditions.get(0).getOperator(), subCon.get(0).getValueToBeCompared()));
			 } catch(Exception e) {
				 e.printStackTrace();
				 break;
			 }
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		sendNotification(queue);
		
	}

}
