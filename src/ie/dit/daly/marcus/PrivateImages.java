package ie.dit.daly.marcus;

import java.io.IOException;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class PrivateImages extends HttpServlet{
	@SuppressWarnings("deprecation")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
		DatastoreService imagestorage = DatastoreServiceFactory.getDatastoreService();
		ImagesService imagesService = ImagesServiceFactory.getImagesService();
		
		String userEmail = "";
		if(userService.isUserLoggedIn())
		{
			userEmail = userService.getCurrentUser().getEmail();
		}
		
		Query query = new Query("image");
		PreparedQuery pq = imagestorage.prepare(query);
	    
	    if (pq.equals(null)) {
	    	resp.getWriter().println("<p>No images here</p>");
	    }
	    else
	    {
	    	
	    	resp.getWriter().println("<table>");
	    	for(Entity image : pq.asIterable())
	    	{
	    		String check1 = "Private";
	    		String check2 = (String) image.getProperty("privacy");
	    		String owner = (String) image.getProperty("owner");
	    		if(check1.equals(check2) && userEmail.equals(owner))
	    		{
	    			resp.getWriter().println("<tr><td>");
	    			String url = imagesService.getServingUrl((BlobKey) image.getProperty("key"));
	    			url = url.concat("=s").concat(Integer.toString(75));
	    			resp.getWriter().println("<img src=\""+url+"\"> </img>");
	    			resp.getWriter().println("<p>DELETE</p>");	    			
	    			resp.getWriter().println("</td></tr>");
	    		}
		    }
	    }
	    resp.getWriter().println("<p><a href=\"c12474932_cloud_assignment\">Return</a></p>");
	}
}