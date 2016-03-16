package model;

import java.io.FileWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.stream.StreamResult;

import marshalling.CustomerType;
import marshalling.ItemType;
import marshalling.ItemsType;
import marshalling.OrderType;

public class EFood {
	private ItemDAO itemdao;
	private OrderDAO orderdao;
	
	public EFood() throws Exception {
		super();
		// TODO Auto-generated constructor stub
		this.itemdao = new ItemDAO();
		this.orderdao = new OrderDAO();
	}
	
	public List<ItemBean> retrieve(int catid) throws SQLException{
		List<ItemBean> itemlist = new ArrayList<ItemBean>();
		itemlist = itemdao.retrieve(catid);
		return itemlist;
	}
	
	public ItemsType additem(HttpServletRequest request, ItemsType orderlist, String itemidstr){
		
		ItemsType ol = orderdao.additem(orderlist, itemidstr);
		request.getSession().setAttribute("orderlist", ol);
		this.caculate(request, ol);
		return ol;
	}
	
		
	public int extractQty(String qty){
		int result;
		
		if (qty.matches("\\d+")){
	//		System.out.println("is integer");
			result = Integer.parseInt(qty);
		}else{
	//		System.out.println("not integer");
			throw new InvalidParameterException("Invalid Quantity");
		}
		return result;
	}//end extractQty()

	public void caculate(HttpServletRequest request, ItemsType orderlist) {
		// TODO Auto-generated method stub
		
		double subtotal = 0, hst, shipping = 5, total;
		List<ItemType> ol = orderlist.getItem();
		for (ItemType ob : ol)
			subtotal += ob.getExtended().doubleValue();
		
		if (subtotal >= 100)
			shipping = 0;
		hst = subtotal * 0.13;
		total = subtotal + shipping + hst;
		
		BigDecimal bsub = BigDecimal.valueOf(subtotal);
		bsub = bsub.setScale(2, RoundingMode.HALF_UP);
		BigDecimal bship = BigDecimal.valueOf(shipping);
		bship = bship.setScale(2, RoundingMode.HALF_UP);
		BigDecimal bhst = BigDecimal.valueOf(hst);
		bhst = bhst.setScale(2, RoundingMode.HALF_UP);
		BigDecimal btotal = BigDecimal.valueOf(total);
		btotal = btotal.setScale(2, RoundingMode.HALF_UP);
				
		request.getSession().setAttribute("subtotal", bsub);
		request.getSession().setAttribute("shipping", bship);
		request.getSession().setAttribute("hst", bhst);
		request.getSession().setAttribute("total", btotal);
			
	}//end caculate

	public List<ItemBean> retrieve(String keyword) throws SQLException {
		// TODO Auto-generated method stub
		List<ItemBean> itemlist = new ArrayList<ItemBean>();
		itemlist = itemdao.retrieve(keyword);
		return itemlist;
	}
	
	public void export(ItemsType orderlist, String account, String username, BigDecimal subtotal, BigDecimal shipping, 
			BigDecimal hst, BigDecimal total, int id, String filename)throws Exception{
		OrderType lw = new OrderType();
		lw = this.createOrderType(orderlist, account, username, subtotal, shipping, hst, total, id);
			
		JAXBContext jc = JAXBContext.newInstance(lw.getClass());
	//	System.out.println("call export here");
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		
		StringWriter sw = new StringWriter();
		
		sw.write("<?xml version=\"1.0\"?>\n");
		sw.write("<?xml-stylesheet type=\"text/xsl\" href=\"PO.xsl\"?>\n");
		
		marshaller.marshal(lw, new StreamResult(sw));
		
		System.out.println(sw.toString());	//for debugging
		System.out.println("filename= " + filename);
		
		FileWriter fw = new FileWriter(filename);
		fw.write(sw.toString());	
		fw.close();
	
	}//end export
	
	private OrderType createOrderType(ItemsType orderlist, String account, String username, BigDecimal subtotal, BigDecimal shipping, 
			BigDecimal hst, BigDecimal total, int id) throws Exception {
		OrderType lw = new OrderType();
		
		CustomerType customer = new CustomerType();
		customer.setAccount(account);
		customer.setName(username);
		lw.setCustomer(customer);
	/*	
		ItemsType items = new ItemsType();
		for (ItemType item : orderlist)
			items.getItem().add(item);
	*/	
		lw.setItems(orderlist);
		
		subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
		shipping = shipping.setScale(2, RoundingMode.HALF_UP);
		hst = hst.setScale(2, RoundingMode.HALF_UP);
		total = total.setScale(2, RoundingMode.HALF_UP);
		
		lw.setTotal(subtotal);
		lw.setShipping(shipping);
		lw.setHST(hst);
		lw.setGrandTotal(total);
		
		BigInteger bi = BigInteger.valueOf(id);
		lw.setId(bi);
			
		Date now = new Date();
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(now);
		XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		lw.setSubmitted(date);
		
		return lw;
		
	}//end createOrderType

	public String checkout(HttpServletRequest request, ItemsType orderlist) {
		// TODO Auto-generated method stub
		//???fake account
		String filename, account, username;
		BigDecimal subtotal, shipping, hst, total;
		int perclientNum, id;
		id = (int) request.getServletContext().getAttribute("id");
		id += 1;
		HashMap<String, Integer> perclient = 
				(HashMap<String, Integer>) request.getServletContext().getAttribute("perclient");
		account = (String) request.getSession().getAttribute("useraccount");
		username = (String) request.getSession().getAttribute("username");
		if (perclient == null){
			System.out.println("map is empty");
			perclient = new HashMap<String, Integer>();
			perclientNum = 1;
			perclient.put(account, perclientNum);
		}else{
			if (perclient.get(account) != null){
				perclientNum = perclient.get(account);
			}else{
				perclientNum = 1;
			}
			perclient.put(account, perclientNum);
		}
		request.getServletContext().setAttribute("perclient", perclient);
		
		filename = "po"+ account + "_" + perclientNum + ".xml";
		String realPath = request.getServletContext().getRealPath("/export/"+filename);
	//	System.out.println("realpath=" + realPath);
		subtotal =  (BigDecimal) request.getSession().getAttribute("subtotal");
		shipping =  (BigDecimal) request.getSession().getAttribute("shipping");
		hst =  (BigDecimal) request.getSession().getAttribute("hst");
		total =  (BigDecimal) request.getSession().getAttribute("total");
		//call export()
		try {
			this.export(orderlist, account, username, subtotal, shipping, hst, total, id, realPath);
		} catch (Exception e) {
			String errmsg = e.getMessage();
			request.setAttribute("error", errmsg);
		}
   	
		//generate the virtual path of the file and set to the request scope
		String virtualPath = request.getServletContext().getContextPath()+"/export/"+filename;
	//	System.out.println("virtualPath="+virtualPath);
		return virtualPath;
   		
	}//end checkout

	public ItemsType update(HttpServletRequest request, ItemsType orderlist) {
		// TODO Auto-generated method stub
		List<ItemType> ol = orderlist.getItem();
		Iterator<ItemType> i = ol.iterator();
		while (i.hasNext()) {
			ItemType ob = i.next();
			String str = "orderqty_" + ob.getNumber();
			String qty = request.getParameter(str);
			int orderqty = -1; 
			try {
				orderqty = this.extractQty(qty);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (orderqty == 0)
				i.remove();
							
			if (orderqty > 0){
				BigInteger bo = BigInteger.valueOf(orderqty);
				ob.setQuantity(bo);
				double subprice = ob.getPrice().doubleValue() * orderqty;
				BigDecimal b = BigDecimal.valueOf(subprice);
				ob.setExtended(b);
			}
		}//end while
		
		return orderlist;
	}//end update
	
	
	
}//end class EFood
