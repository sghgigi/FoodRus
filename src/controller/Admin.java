package controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Admin
 */
//@WebServlet("/Admin")
public class Admin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Admin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String ftarget = "/Front.jspx";
		
		int addtoCartCount = 0;
		Long addtoCartAverage = new Long(0);
		int checkoutCount = 0;
		Long checkoutAverage = new Long(0);

		HashMap<String, Long> addMap = (HashMap<String, Long>) this.getServletContext().getAttribute("averageAddHashmap");
		HashMap<String, Long> checkoutMap = (HashMap<String, Long>) this.getServletContext().getAttribute("checkOutAddHashmap");
		System.out.println("map size = " + addMap.size());
		for (String key : addMap.keySet()) {
			
			addtoCartCount++;
			addtoCartAverage += addMap.get(key);
			System.out.println("addtoCartCount="+addtoCartCount);
		}

		for (String key : checkoutMap.keySet()) {
			checkoutCount++;
			checkoutAverage += checkoutMap.get(key);
		}

		if (checkoutCount != 0) {
			request.setAttribute("checkoutTime", checkoutAverage / checkoutCount);
			request.setAttribute("checkoutCustomer", checkoutCount);
		}
		if (addtoCartCount != 0) {
			request.setAttribute("addToCartTimes", addtoCartAverage / addtoCartCount);
			request.setAttribute("cartCustomers", addtoCartCount);
		}
		request.setAttribute("target", "/admin.jspx");
		request.getRequestDispatcher(ftarget).forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}

}
