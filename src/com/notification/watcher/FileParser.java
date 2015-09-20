package com.notification.watcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Arrays;
import java.util.Comparator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.util.Comparators;

import com.notification.database.QueryExecutors;
import com.notification.domain.Books;
import com.notification.send.SendNotification;
import com.notification.util.Constants;
import com.notification.util.Util;

public class FileParser {

		
	public static void createBooks(List<File> files){
		try {
			//parse the file and convert into java book object
			Map<Integer, Map<String, Object>> attributesMap = parseFiles(files);
			ObjectMapper objMapper = new ObjectMapper();
			List<Books> fileBookList = new LinkedList<Books>();
			Map<String, Object> attrMap = null;
			for(Entry<Integer, Map<String, Object>> attributeMap : attributesMap.entrySet()){
				attrMap = attributeMap.getValue();
				Books book = objMapper.convertValue(attrMap, Books.class);
				System.out.println(book.toString());
				fileBookList.add(book);
			}
			List<Books> existingBookList = new LinkedList<Books>();
			List<Books> newBookList = new LinkedList<Books>();
			QueryExecutors queryExecutors = QueryExecutors.getInstance();
			queryExecutors.checkBooksInDB(fileBookList, existingBookList, newBookList, attributesMap);
			if(existingBookList.size()>0){
				queryExecutors.updateBooks(existingBookList);
				System.out.println("updated book size "+existingBookList.size());
			}
			else if(newBookList.size()>0){
				queryExecutors.insertBooks(newBookList);
				System.out.println("new book list size "+newBookList.size());
			}
			
			int threadCount = files.size()/2 == 0 ? 1 : files.size()/2;
			ExecutorService service = Executors.newFixedThreadPool(10);
			for(int i=0;i<fileBookList.size();i++) {
				ArrayBlockingQueue<Books> queue = new ArrayBlockingQueue<>(1);
				queue.put(fileBookList.get(i));
				service.submit(new SendNotification(queue));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Map<Integer, Map<String, Object>> parseFiles(List<File> files){

		/*File read operation is performed and and csv is converted to Map
		 * Eg){13579={authors=Levitt & Dubner, title=freakonomicss, release date=20-09-2011, list price=7 USD, publisher=William Morrow} 
		 */
		BufferedReader br = null;

		String line = null;
		Map<Integer, Map<String, Object>> fileAttributesMap = new HashMap<Integer, Map<String, Object>>();
		Map<String,Object> attributeMap = new HashMap<String, Object>();
		
		try {
			
			
			
			for(File file : files){
				
				br = new BufferedReader(new FileReader(Constants.FILE_PATH+"/"+file.getName()));
				while((line = br.readLine())!=null){
					System.out.println("csv line "+line);
					String[] attributes = line.split(Constants.COMMA);
					if(attributes.length%2 == 0){
						System.err.println("Invalid csv entry");
						continue;
					}
					if(!fileAttributesMap.containsKey(Integer.parseInt(attributes[0]))){
						attributeMap = new HashMap<String,Object>();
						attributeMap.put("id", attributes[0]);
					}
				//	for(i= i+1; i< attributes.length-1; i++){
						System.out.println(attributes[1]);
						if(!attributes[1].equalsIgnoreCase("release_date"))
							attributeMap.put(attributes[1], attributes[2]);
						if(attributes[1].equalsIgnoreCase("title")){
							attributeMap.put("title_modified_date", file.lastModified());
						}
						else if(attributes[1].equalsIgnoreCase("publisher")){
							attributeMap.put("publisher_modified_date", file.lastModified());
						}
						else if(attributes[1].equalsIgnoreCase("authors")){
							attributeMap.put("authors_modified_date", file.lastModified());
						}
						else if(attributes[1].equalsIgnoreCase("price")){
							attributeMap.put("price_modified_date", file.lastModified());
						}
						else if(attributes[1].equalsIgnoreCase("release_date")){
							attributeMap.put("release_modified_date", file.lastModified());
							attributeMap.put(attributes[1], Util.convertDateToLong(attributes[2]));
							System.out.println( Util.convertDateToLong(attributes[2]));
						}
						
						System.out.println(attributes[1]+" "+attributes[2]);
						
					//}
					fileAttributesMap.put(Integer.parseInt(attributes[0]), attributeMap);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(fileAttributesMap.toString());
		return fileAttributesMap;
	}


}
