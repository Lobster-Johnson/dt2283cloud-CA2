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
		//set text type and create writer for printing html
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		resp.setContentType("text/html");
		
		//create user service, principal and blank email string for determining who the user is
		com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
		Principal myPrincipal = req.getUserPrincipal();
		String emailAddress = null;
		
		//get URLS for logining the user in/out
		String thisURL = req.getRequestURI();
		String loginURL = userService.createLoginURL(thisURL);
		String logoutURL = userService.createLogoutURL(thisURL);
		
		//string to determine level of access user has
		String permission = "";
		
		//if user isn't logged in or registered with this app, error will be returned
		if(myPrincipal == null) 
		{
			//user has option to either log in or look at public photos
			writer.println("<p>You are not logged in at this time</p>");
			writer.println("<p>You can <a href=\""+loginURL+ "\">sign in here if you are a registered user</a>.</p>");
			writer.println("<p>Click here to view <a href=\"publicimages\">public images</a></p>");
			permission = "guest";
		} // end if not logged in
		
		//if the user is logged into a registered account
		if(myPrincipal !=null) 
		{
			//set their email address to this string
			emailAddress = myPrincipal.getName();
			
			//display who they are
			writer.println("<p>You are logged in as (email): "+emailAddress+"</p>");
			
			//permissions
			//if they have an admin email address, they get admin privileges
			//they are informed of this
			if(emailAddress .equals( "daly.marcus@gmail.com") || emailAddress .equals("mark.deegan@dit.ie"))
			{
				writer.println("<p>You are currently logged in as an admin</p>");
				permission = "admin";
			}
			else//if not admin, they are told they are a user
			{
				writer.println("<p>You are currently logged in as a user</p>");
				permission = "user";
			}
			
			//general functions
			//private images
			writer.println("<p>Click here to view your <a href=\"privateimages\">private images</a></p>");
			
			//public images
			writer.println("<p>Click here to view <a href=\"publicimages\">public images</a></p>");
			
			//option to upload an image
			writer.println("<p>Click here to <a href=\"upload.jsp\">upload images</a></p>");
			
			//option to logout
			writer.println("<p><a href=\"" + logoutURL + "\">Sign out</a>.</p>");
			}
		} // end if logged in
	}

