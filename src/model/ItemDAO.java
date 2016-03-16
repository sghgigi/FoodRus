package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.jdbc.pool.DataSource;

public class ItemDAO {
	private DataSource ds;

	public ItemDAO() throws Exception {
		super();
		// TODO Auto-generated constructor stub
		this.ds = (DataSource)(new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
	}
	
	public List<ItemBean> retrieve(int catid) throws SQLException{
		List<ItemBean> itemlist = new ArrayList<ItemBean>();
		String query;
		
		query = "select * from item where catid=" + catid;
			
		itemlist = this.execute(query);
		return itemlist;
	}//end retrieve(catid)
	
	public List<ItemBean> execute(String query) throws SQLException{
		List<ItemBean> itemlist = new ArrayList<ItemBean>();
			
		Connection con = this.ds.getConnection();
		Statement s = con.createStatement();
		s.executeUpdate("set schema roumani");
		ResultSet r = s.executeQuery(query);
		
		while(r.next()){
			
			String number = r.getString("number");
			String name = r.getString("name"); 
			int qty = r.getInt("qty"); 
			double price = r.getDouble("price");
			ItemBean item = new ItemBean(number, name, qty, price);
			itemlist.add(item);
		}
		con.close();
		
		return itemlist;
		
	}//end execute

	public List<ItemBean> retrieve(String keyword) throws SQLException {
		// TODO Auto-generated method stub
		List<ItemBean> itemlist = new ArrayList<ItemBean>();
		String query, capKeyword;
		//capKeyword = keyword.
		
		query = "select * from item where number like \'%" + keyword + "%\'"
				+ " or name like \'%" + keyword + "%\'" + " or lower(name) like \'%" + keyword + "%\'";
		System.out.println("sql="+query);
		itemlist = this.execute(query);
		return itemlist;
	}
	
	
	
	
	
}//end class ItemDAO
