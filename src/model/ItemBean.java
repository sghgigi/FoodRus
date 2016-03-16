package model;

public class ItemBean {
	private String number;
	private String name;
	private int qty;
	private double price;
	
	public ItemBean(String number, String name, int qty, double price) {
		super();
		this.number = number;
		this.name = name;
		this.qty = qty;
		this.price = price;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	
}