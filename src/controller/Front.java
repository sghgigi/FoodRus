package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
 * Servlet implementation class Front
 */
@WebServlet("/Front/*")
public class Front extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Front() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();

		try {
			int id = 0;
			this.getServletContext().setAttribute("id", id);
			this.getServletContext().setAttribute("perClient",
					new HashMap<String, Integer>());
			this.getServletContext().setAttribute("model", new EFood());
			HashMap<String, Long> addMap = new HashMap<String, Long>();
			this.getServletContext().setAttribute("averageAddHashmap", addMap);
			HashMap<String, Long> checkOutMap = new HashMap<String, Long>();
			this.getServletContext().setAttribute("checkOutAddHashmap",
					checkOutMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		EFood efood = (EFood) this.getServletContext().getAttribute("model");
		List<ItemBean> itemlist = null;
		request.getSession().setAttribute("freshVisit", "freshVisit");
		ItemsType orderlist = (ItemsType) request.getSession().getAttribute(
				"orderlist");
		if (orderlist == null)
			orderlist = new ItemsType();
		// System.out.println("pth = " + request.getPathInfo());
		if (request.getPathInfo() != null
				&& request.getPathInfo().equals("/Catalog")) {
			request.setAttribute("ticket", "F-to-Catalog");
			this.getServletContext().getNamedDispatcher("Catalog")
					.forward(request, response);
		} else if (request.getPathInfo() != null
				&& request.getPathInfo().equals("/Cart")) {
			request.setAttribute("ticket", "F-to-Cart");
			this.getServletContext().getNamedDispatcher("Cart")
					.forward(request, response);
		} else if (request.getPathInfo() != null
				&& request.getPathInfo().equals("/Auth")) {
			request.setAttribute("target", "/Lonin.jspx");
			this.getServletContext().getNamedDispatcher("Auth")
					.forward(request, response);
		} else if (request.getPathInfo() != null
				&& request.getPathInfo().equals("/Admin")) {
			request.setAttribute("target", "/Admin.jspx");
			this.getServletContext().getNamedDispatcher("Admin")
					.forward(request, response);
		} else {
			//System.out.println("looking at para");
			if (request.getParameter("Search") != null) {
				String keyword = request.getParameter("keyword");
				System.out.println("keyword=" + keyword);
				try {
					itemlist = efood.retrieve(keyword);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("itemlist", itemlist);
				request.setAttribute("target", "/Items.jspx");
				request.getRequestDispatcher("/Front.jspx").forward(request,
						response);
			}

			else if (request.getParameter("Login") != null) {
				String url = "https://www.cse.yorku.ca/~cse03243/4413/ZZ_post.cgi";
				response.sendRedirect(url);
				
			}

			else if (request.getParameter("ViewCart") != null) {

				if (orderlist.getItem().size() > 0) {
					efood.caculate(request, orderlist);

					BigDecimal subtotal, hst, shipping, total;
					subtotal = (BigDecimal) request.getSession().getAttribute(
							"subtotal");
					shipping = (BigDecimal) request.getSession().getAttribute(
							"shipping");
					hst = (BigDecimal) request.getSession().getAttribute("hst");
					total = (BigDecimal) request.getSession().getAttribute(
							"total");
					request.setAttribute("subtotal", subtotal.doubleValue());
					request.setAttribute("shipping", shipping.doubleValue());
					request.setAttribute("hst", hst.doubleValue());
					request.setAttribute("total", total.doubleValue());
				}
				request.setAttribute("orderlist", orderlist);
				request.setAttribute("target", "/Cart.jspx");
				this.getServletContext().getNamedDispatcher("Cart")
						.forward(request, response);

			}

			else if (request.getParameter("CheckOut") != null) {
				System.out.println("trying check out");
				System.out.println("log in success:" +request.getSession().getAttribute("loginsuccess") );
				if (request.getSession().getAttribute("loginsuccess") != null) {
					

					if (orderlist.getItem().size() > 0) {
						request.getSession().setAttribute("listenCheckOutBtn",
								"listenCheckOutBtn");
						String virtualPath = efood.checkout(request, orderlist);
						request.setAttribute("filename", virtualPath);
						request.setAttribute("target", "/CheckOut.jspx");
						request.setAttribute("useraccount", request.getSession().getAttribute("useraccount"));
						request.setAttribute("username", request.getSession().getAttribute("username"));
						request.setAttribute("loginsuccess", request.getSession().getAttribute("loginsuccess"));

						request.getRequestDispatcher("/Front.jspx").forward(
								request, response);
					} else {
						
						request.setAttribute("ticket", "Front");
						request.getRequestDispatcher("/Front.jspx").forward(
								request, response);

					}
				} else {
					String url = "https://www.cse.yorku.ca/~cse03243/4413/ZZ_post.cgi";

					response.sendRedirect(url);
				}
			}

			else if (request.getParameter("additem") != null) {
				request.getSession().setAttribute("listenAddBtn",
						"listenAddBtn");
				String itemidstr = request.getParameter("addedItemID");
				// System.out.println("str=" + itemidstr);
				orderlist = efood.additem(request, orderlist, itemidstr);
				System.out.println("front orderlist size="
						+ orderlist.getItem().size());

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

				request.setAttribute("orderlist", orderlist);
				request.setAttribute("target", "/Cart.jspx");
				this.getServletContext().getNamedDispatcher("Cart")
						.forward(request, response);
			}
			else if (request.getParameter("logout") != null)
			{
				request.getSession().setAttribute("useraccount", null);
				request.getSession().setAttribute("username", null);
				request.getSession().setAttribute("loginsuccess", null);
				request.getRequestDispatcher("/Front.jspx").forward(
						request, response);
				
			}

			else {
				request.setAttribute("ticket", "Front");
				request.getRequestDispatcher("/Front.jspx").forward(request,
						response);
			}
		}// end if request.getPathInfo

	}// end doGet

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
