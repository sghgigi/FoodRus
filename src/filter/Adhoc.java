package filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.EFood;
import model.ItemBean;

/**
 * Servlet Filter implementation class Adhoc
 */
@WebFilter(dispatcherTypes = {
				DispatcherType.REQUEST, 
				DispatcherType.FORWARD
		}
					, urlPatterns = { "/Front/*" })
public class Adhoc implements Filter {

	private static final String recommended = "2002H712";
	private static final String addedItem = "1409S413";
	
    /**
     * Default constructor. 
     */
    public Adhoc() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		if(req.getParameter("addedItemID") != null){
			System.out.println("filter in add item="+req.getParameter("addedItemID"));
			HttpSession session = req.getSession();
			
			String itemID = req.getParameter("addedItemID").split("_")[1];
			System.out.println("filter id="+itemID);
			if(itemID.compareToIgnoreCase(addedItem) == 0){
				EFood efood = (EFood) req.getServletContext().getAttribute("model");
				try {
					System.out.println(recommended);
					List<ItemBean> adverlist = efood.retrieve(recommended);
					req.setAttribute("adverlist",adverlist);
					req.setAttribute("target", "/Items.jspx");
					req.getRequestDispatcher("/Front.jspx").forward(req,res);
				} catch (Exception e) {
					System.out.println("Error in Filter: " + e.getLocalizedMessage());
				}
			}
		}
		chain.doFilter(request, response);
		
	}//end doFilter

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
