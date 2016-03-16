package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.EFood;
import model.ItemBean;



/**
 * Servlet implementation class Catalog
 */
//@WebServlet("/Catalog")
public class Catalog extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Catalog() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String ftarget = "/Front.jspx";
		EFood efood = (EFood) this.getServletContext().getAttribute("model");
		List<ItemBean> itemlist = null;
		int catid;
					
		if(request.getParameter("Meat") != null){
			catid = 3;
			try {
				itemlist = efood.retrieve(catid);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("itemlist", itemlist);	
			request.setAttribute("target", "/Items.jspx");
		/*
			System.out.println("list size = " + itemlist.size());
			for (ItemBean item : itemlist){
				System.out.println(item.getName());
			}
		*/				
			
		}
		
		if(request.getParameter("Cheese") != null){
			catid = 4;
			try {
				itemlist = efood.retrieve(catid);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			request.setAttribute("itemlist", itemlist);			
			request.setAttribute("target", "/Items.jspx");
		}
		
		if(request.getParameter("IceCream") != null){
			catid = 5;
			try {
				itemlist = efood.retrieve(catid);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			request.setAttribute("itemlist", itemlist);			
			request.setAttribute("target", "/Items.jspx");
		}
		
		if(request.getParameter("Cereal") != null){
			catid = 6;
			try {
				itemlist = efood.retrieve(catid);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			request.setAttribute("itemlist", itemlist);			
			request.setAttribute("target", "/Items.jspx");
		}
		
			
	//	request.setAttribute("target", "/Items.jspx");
		RequestDispatcher rd = request.getRequestDispatcher(ftarget);
		rd.forward(request, response);
		
	}//end doGet

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}

}
