package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Auth
 */
//@WebServlet("/Auth")
public class Auth extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Auth() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String ftarget = "/Front.jspx";
		String account, username;	
		if (request.getParameter("user") != null){
			String str = request.getParameter("user");
			/*
			 * call decrypt method, the decrypt method has not created
			 * if succeed, return user account, user name
			 * save account, username to the session scope
			 * save login success status to the session scope
			 * 
			 */
			
			
			request.setAttribute("useraccount", str.substring(3));
			request.setAttribute("username", str);
			request.setAttribute("loginsuccess", "ok");
			request.getSession().setAttribute("useraccount", str.substring(3));
			request.getSession().setAttribute("username", str);
			request.getSession().setAttribute("loginsuccess", "ok");
			request.setAttribute("target", "/Login.jspx");			
			
		}
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
