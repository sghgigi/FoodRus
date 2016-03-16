package model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

import marshalling.ItemType;
import marshalling.ItemsType;

public class OrderDAO {

	public OrderDAO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ItemsType additem(ItemsType orderlist, String itemidstr){
		int orderqty = 0;
		double subprice = 0;
		int flag = 0;
		
		String[] part = itemidstr.split("_");
		String number = part[1];
		String name = part[2];
		int qty = Integer.parseInt(part[3]);
		double price = Double.parseDouble(part[4]);
	//	System.out.println(number + ":" + name + ":" + qty + ":" + price);
		List<ItemType> ol = orderlist.getItem();
		for (ItemType order : ol){
			if (order.getNumber().equals(number)){
				orderqty = order.getQuantity().intValue() + 1;
				BigInteger bo = BigInteger.valueOf(orderqty);
				order.setQuantity(bo);
				subprice = orderqty * price;
				BigDecimal b = BigDecimal.valueOf(subprice);
				order.setExtended(b);
				flag = 1;
				
			}
		}
		
		
		if (flag == 0){
			orderqty = 1;
			subprice = orderqty * price;
			BigInteger bo = BigInteger.valueOf(orderqty);
			BigDecimal bs = BigDecimal.valueOf(subprice);
			bs = bs.setScale(2, RoundingMode.HALF_UP);
			BigDecimal bp = BigDecimal.valueOf(price);
			bp = bp.setScale(2, RoundingMode.HALF_UP);
			ItemType ob = new ItemType();
			ob.setName(name);
			ob.setNumber(number);
			ob.setPrice(bp);
			ob.setQuantity(bo);
			ob.setExtended(bs);
			orderlist.getItem().add(ob);
		}
		return orderlist;
	}//end addOrder
	
	
}//end clsaa OrderDAO
