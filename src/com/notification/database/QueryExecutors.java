package com.notification.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.notification.domain.Books;
import com.notification.domain.Conditions;
import com.notification.domain.Subscriber_Condition;
import com.notification.domain.Subscribers;

public class QueryExecutors {

	private static QueryExecutors queryExecutor;

	private QueryExecutors(){

	}

	public static Map<String,String> queryMap = new HashMap<String,String>();

	static{
		queryMap.put("insertBooks", "insert into Books(book_id, title, price, authors, publisher,title_modified_date,release_modified_date, price_modified_date,authors_modified_date,publisher_modified_date,release_date) values(?,?,?,?,?,?,?,?,?,?,?);");
		queryMap.put("getBooks", "select * from Books where book_id in");
		queryMap.put("updateBooks", "update Books set title=?,price=?,authors=?,publisher=?,title_modified_date,release_modified_date, price_modified_date,authors_modified_date,publisher_modified_date,release_date  where book_id=?;");
		queryMap.put("getsubscriberid", "select subscriber_id from subscriber_book where book_id =?;");
		queryMap.put("getsubscribers", "select * from subscribers where book_id =?;");
		queryMap.put("getModifiedDate", "select lastmodified from Book_Attributes_Last_Modified where book_id=? and attribute_name=?;");
	}

	public static QueryExecutors getInstance(){

		if(queryExecutor == null){
			synchronized (QueryExecutors.class) {
				queryExecutor = new QueryExecutors();
			}
		}
		return queryExecutor;

	}

	public void checkBooksInDB(List<Books> bookList,List<Books> existingBookList, List<Books> newBookList, Map<Integer, Map<String, Object>> attributesMap){

		//Checks the DB on the number of records that exists already
		PreparedStatement statement = null;		
		Connection connection = null;
		List<Books> totalBookList = new LinkedList<Books>(bookList);
		ResultSet resultSet = null;
		try{
			connection = DBConnection.getConnection();
			StringBuilder sb = new StringBuilder();
			Set<Integer> bookIds = attributesMap.keySet();
			List<Integer> bookIdList = new ArrayList<Integer>(bookIds);
			for(int i =0 ;i< bookIdList.size(); i++){
				sb.append("?,");
			}
			String sql = queryMap.get("getBooks")+"(" +sb.deleteCharAt( sb.length() -1 ).toString()+")";
			statement = connection.prepareStatement(sql);
			for(int i =0 ;i< bookIdList.size(); i++){
				statement.setInt(i+1, bookIdList.get(i));
			}
			resultSet = statement.executeQuery();
			existingBookList = setBooks(resultSet);
			totalBookList.removeAll(existingBookList);
			newBookList.addAll(totalBookList);
			System.out.println("newbooklist size "+newBookList.size());
			System.out.println("allbooks "+bookList.size());
			System.out.println("existing booklist "+existingBookList.size());
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try {
				if(resultSet!=null)
					resultSet.close();
				if(statement!=null)
					statement.close();
				if(connection!=null)

					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//public List<>

	private List<Books> setBooks(ResultSet resultSet){
		List<Books> bookList = new LinkedList<Books>();
		try {
			while(resultSet.next()){
				Books book = new Books();
				book.setId(resultSet.getInt(1));
				book.setTitle(resultSet.getString(2));
				book.setPrice(resultSet.getFloat(3));
				book.setAuthors(resultSet.getString(4));
				book.setPublisher(resultSet.getString(5));
				book.setTitle_modified_date(resultSet.getLong(6));
				book.setRelease_modified_date(resultSet.getLong(7));
				book.setPrice_modified_date(resultSet.getLong(8));
				book.setAuthors_modified_date(resultSet.getLong(9));
				book.setPublisher_modified_date(resultSet.getLong(10));
				book.setRelease_date(resultSet.getLong(11));
				bookList.add(book);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bookList;
	}
	
	public void updateBooks(List<Books> existingBookList){
		

		Connection connection = null;

		//Updating the books table
		PreparedStatement statement = null;
		try {
			connection = DBConnection.getConnection();
			statement = connection.prepareStatement(queryMap.get("updateBooks"));
			for(Books book : existingBookList){
				System.out.println("publisher "+book.getPublisher()+"id "+book.getId()+"title "+book.getTitle());
				statement.setString(1, book.getTitle() == null ? "" : book.getTitle());
				statement.setString(3, book.getAuthors() == null ? "" : book.getAuthors());
				statement.setString(4, book.getPublisher() == null ? "" : book.getPublisher());
				statement.setLong(5, book.getTitle_modified_date() == 0 ? 0 : book.getTitle_modified_date());
				statement.setLong(6, book.getRelease_modified_date() == 0 ? 0 : book.getTitle_modified_date());
				statement.setLong(7, book.getPrice_modified_date() == 0 ? 0 : book.getTitle_modified_date());
				statement.setLong(8, book.getAuthors_modified_date() == 0 ? 0 : book.getTitle_modified_date());
				statement.setLong(9, book.getPublisher_modified_date() == 0 ? 0 : book.getTitle_modified_date());
				statement.setLong(10, book.getRelease_date() == 0 ? 0 : book.getRelease_date());
				statement.setInt(11, book.getId());
				int updateRecordStatus = statement.executeUpdate();
				System.out.println("status "+updateRecordStatus);

			}
			//int[] recordsUpdated = statement.executeBatch();
			//connection.commit();
			//System.out.println("records inserted size "+recordsUpdated.length);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if(statement != null ) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(connection!=null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}



	}

	public void insertBooks(List<Books> newBookList){

		Connection connection = null;

		PreparedStatement statement = null;

		try {
			connection = DBConnection.getConnection();
			statement = connection.prepareStatement(queryMap.get("insertBooks"));
			for(Books book : newBookList){
				System.out.println("publisher insert "+book.getPublisher());
				statement.setInt(1, book.getId());
				statement.setString(2, book.getTitle() == null ? "" : book.getTitle());
				statement.setFloat(3, book.getPrice());
				statement.setString(4, book.getAuthors() == null ? "" : book.getAuthors());
				statement.setString(5, book.getPublisher() == null ? "" : book.getPublisher());
				statement.setLong(6, book.getTitle_modified_date() == 0 ? 0 : book.getTitle_modified_date());
				statement.setLong(7, book.getRelease_modified_date() == 0 ? 0 : book.getTitle_modified_date());
				statement.setLong(8, book.getPrice_modified_date() == 0 ? 0 : book.getTitle_modified_date());
				statement.setLong(9, book.getAuthors_modified_date() == 0 ? 0 : book.getTitle_modified_date());
				statement.setLong(10, book.getPublisher_modified_date() == 0 ? 0 : book.getTitle_modified_date());
				statement.setLong(11, book.getRelease_date() == 0 ? 0 : book.getRelease_date());
				int recordsInserted = statement.executeUpdate();	
				System.out.println("inserted status "+recordsInserted);			
			}

		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(statement != null ) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(connection!=null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public List<Integer> getSubscriberIds(int bookId) {
		Connection conn = null;
		PreparedStatement s = null;
		List <Integer> subscriberIds = new LinkedList<Integer>();
		try {
			conn = DBConnection.getConnection();
			s = conn.prepareStatement(queryMap.get("getsubscriberid"));
			s.setInt(1, bookId);
			ResultSet res = s.executeQuery();
			while(res.next()) {
				subscriberIds.add(res.getInt("subscriber_id"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		return subscriberIds;
	}

	public List<Subscribers> getSubscribers(int bookId) {
		List <Integer> subIds = getSubscriberIds(bookId);
		Connection conn = null;
		PreparedStatement s = null;
		ResultSet res = null;
		List <Subscribers> subscribers = new LinkedList<Subscribers>();
		try {
			conn = DBConnection.getConnection();
			s = conn.prepareStatement(queryMap.get("getsubscribers"));

			for(int subId:subIds) {
				s.setInt(1, subId);
				res = s.executeQuery();
				while(res.next()) {
					Subscribers sub = new Subscribers();
					sub.setSubscriber_id(res.getInt("subscriber_id"));
					sub.setSubscriber_emailid("subscriber_emailid");
					sub.setSubscriber_phone("subscriber_phone");
					subscribers.add(sub);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(res!=null)
					res.close();
				if(s!=null)
					s.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return subscribers;
	}

	public static List<Subscriber_Condition> getSubCondition(List <Integer>subIds, int book_id) {
		Connection conn = DBConnection.getConnection();
		Statement s = null;
		List <Subscriber_Condition> subCondition = new LinkedList<Subscriber_Condition>();
		try {
			s = conn.createStatement();
			for(int subId:subIds) {
				ResultSet res = s.executeQuery("select * from subscriber_condition where subscriber_id ="+subId+" and book_id ="+book_id);
				while(res.next()) {
					Subscriber_Condition sub = new Subscriber_Condition();
					sub.setCondition_id(res.getInt("condition_id"));
					sub.setValueToBeCompared(res.getString("valueToBeChecked"));
					subCondition.add(sub);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subCondition;
	}

	public static List<Conditions> getCondition(List <Subscriber_Condition> subConditions) {
		Connection conn = DBConnection.getConnection();
		Statement s = null;
		List <Conditions> conditions = new LinkedList<Conditions>();
		try {
			s = conn.createStatement();
			for(Subscriber_Condition subCondition:subConditions) {
				ResultSet res = s.executeQuery("select * from conditions where condition_id ="+subCondition.getCondition_id());
				while(res.next()) {
					Conditions con = new Conditions();
					con.setAttribute(res.getString("attribute"));
					con.setCondition_id(res.getInt("condition_id"));
					con.setOperator(res.getString("operator"));
					conditions.add(con);
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return conditions;
	}
}
