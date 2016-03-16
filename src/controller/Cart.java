package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import marshalling.ItemType;
import marshalling.ItemsType;
import model.EFood;
import model.ItemBean;

/**
 * Servlet implementation class Cart
 */
// @WebServlet("/Cart")
public class Cart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Cart() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String ftarget = "/Front.jspx";
		EFood efood = (EFood) this.getServletContext().getAttribute("model");

		ItemsType orderlist = (ItemsType) request.getSession().getAttribute(
				"orderlist");
		if (orderlist == null)
			orderlist = new ItemsType();

		System.out.println("cart orderlist size=" + orderlist.getItem().size());

		if (request.getParameter("Update") != null) {
			if (orderlist.getItem().size() > 0) {

				orderlist = efood.update(request, orderlist);
				request.setAttribute("orderlist", orderlist);

				if (orderlist.getItem().size() > 0)
					efood.caculate(request, orderlist);
				BigDecimal subtotal, hst, shipping, total;
				subtotal = (BigDecimal) request.getSession().getAttribute(
						"subtotal");
				shipping = (BigDecimal) request.getSession().getAttribute(
						"shipping");
				hst = (BigDecimal) request.getSession().getAttribute("hst");
				total = (BigDecimal) request.getSession().getAttribute("total");
				request.setAttribute("subtotal", subtotal.doubleValue());
				request.setAttribute("shipping", shipping.doubleValue());
				request.setAttribute("hst", hst.doubleValue());
				request.setAttribute("total", total.doubleValue());

			}// end if size > 0
			request.setAttribute("target", "/Cart.jspx");
			request.getRequestDispatcher(ftarget).forward(request, response);
		}

		else if (request.getParameter("CheckitOut") != null) {
			if (request.getSession().getAttribute("loginsuccess") != null) {
				if (orderlist.getItem().size() > 0) {
					request.getSession().setAttribute("listenCheckOutBtn",
							"listenCheckOutBtn");
					String virtualPath = efood.checkout(request, orderlist);
					request.setAttribute("filename", virtualPath);
					request.setAttribute("target", "/CheckOut.jspx");

					request.getRequestDispatcher("/Front.jspx").forward(
							request, response);
				} else {
					request.setAttribute("target", "/Cart.jspx");
					request.getRequestDispatcher(ftarget).forward(request,
							response);
				}
			} else {
				String url = "https://www.cse.yorku.ca/~cse03243/4413/ZZ_post.cgi";
				response.sendRedirect(url);
			}
		}

		else if (request.getParameter("ContinueShopping") != null) {

			request.getRequestDispatcher(ftarget).forward(request, response);
		}

		else {
			request.setAttribute("target", "/Cart.jspx");
			request.getRequestDispatcher(ftarget).forward(request, response);
		}

	}// end doGet()

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}

}
