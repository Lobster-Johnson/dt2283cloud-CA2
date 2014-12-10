package ie.dit.daly.marcus;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;

import javax.servlet.http.*;

import com.google.appengine.api.users.UserServiceFactory;
@SuppressWarnings("serial")
public class C12474932_Cloud_AssignmentServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		
		com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
		Principal myPrincipal = req.getUserPrincipal();
		String emailAddress = null;
		String thisURL = req.getRequestURI();
		String loginURL = userService.createLoginURL(thisURL);
		String logoutURL = userService.createLogoutURL(thisURL);
		String permission = "";
		String downloads = "";
		String uploads = "";
		String[] members = {" ", " "};
		resp.setContentType("text/html");
		
		if(myPrincipal == null) 
		{
			writer.println("<p>You are not logged in at this time</p>");
			writer.println("<p>You can <a href=\""+loginURL+ "\">sign in here if you are a registered user</a>.</p>");
			writer.println("<p>Click here to view public images</p>");
			permission = "guest";
		} // end if not logged in
		
		//if the user logs in
		if(myPrincipal !=null) 
		{
			emailAddress = myPrincipal.getName();
			writer.println("<p>You are logged in as (email): "+emailAddress+"</p>");
			
			//permissions
			if(emailAddress .equals( "daly.marcus@gmail.com") || emailAddress .equals("mark.deegan@dit.ie"))
			{
				writer.println("<p>You are currently logged in as an admin</p>");
				permission = "admin";
			}
			else
			{
				writer.println("<p>You are currently logged in as a user</p>");
				permission = "user";
			}
			
			//general functions
			writer.println("<p>Click here to view your private images</p>");
			writer.println("<p>Click here to view public images</p>");
			writer.println("<p>Click here to <a href=\"upload.jsp\">upload images</a></p>");
			writer.println("<p><a href=\"" + logoutURL + "\">Sign out</a>.</p>");
			}
		} // end if logged in
	}

