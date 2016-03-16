package listener;

import java.util.HashMap;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * Application Lifecycle Listener implementation class OrderAdd
 *
 */
@WebListener
public class OrderAdd implements HttpSessionAttributeListener {

	/**
	 * Default constructor.
	 */
	public OrderAdd() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
	 */
	public void attributeRemoved(HttpSessionBindingEvent se) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
	 */
	public void attributeAdded(HttpSessionBindingEvent se) {
		// TODO Auto-generated method stub
		this.doStuff(se);
	}

	/**
	 * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
	 */
	public void attributeReplaced(HttpSessionBindingEvent se) {
		// TODO Auto-generated method stub
		this.doStuff(se);
	}

	private void doStuff(HttpSessionBindingEvent event) {
		HttpSession session = event.getSession();
		if (event.getName().equals("freshVisit")) {
			System.out.println("Fresh Visit");
			if (session.getAttribute("freshVisitTime") == null) {
				session.setAttribute("freshVisitTime",
						System.currentTimeMillis());
			}
		} else if (event.getName().equals("listenAddBtn")) {
			Long averageTimeToAddToCart = (System.currentTimeMillis() - (Long) session
					.getAttribute("freshVisitTime")) / 1000;
			HashMap<String, Long> addMap = (HashMap<String, Long>) session
					.getServletContext().getAttribute("averageAddHashmap");
			if (!addMap.containsKey(session.getId())) {
				addMap.put(session.getId(), averageTimeToAddToCart);
			}

			System.out.println("Adding to Cart Times");
			for (String key : addMap.keySet())
				System.out.println(key + " " + addMap.get(key));

		} else if (event.getName().equals("listenCheckOutBtn")) {
			Long averageTimeToCheckout = (System.currentTimeMillis() - (Long) session
					.getAttribute("freshVisitTime")) / 1000;
			HashMap<String, Long> checkoutmap = (HashMap<String, Long>) session
					.getServletContext().getAttribute("checkOutAddHashmap");
			if (!checkoutmap.containsKey(session.getId())) {
				checkoutmap.put(session.getId(), averageTimeToCheckout);
			}

			System.out.println("Checkout Times");
			for (String key : checkoutmap.keySet())
				System.out.println(key + " " + checkoutmap.get(key));
		}

	}// end doStuff

}
