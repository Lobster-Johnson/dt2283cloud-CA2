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
public class PublicImages extends HttpServlet{
	@SuppressWarnings("deprecation")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		//declare the userservice for finding user, the data storage for accessing images and image service for using those images
		com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
		DatastoreService imagestorage = DatastoreServiceFactory.getDatastoreService();
		ImagesService imagesService = ImagesServiceFactory.getImagesService();
		
		//their email defaults to blank so it won't say they own any images
		String userEmail = "";
		
		//if they are logged in, get their email address
		if(userService.isUserLoggedIn())
		{
			userEmail = userService.getCurrentUser().getEmail();
		}
		
		//prepare a query to return images
		Query query = new Query("image");
		PreparedQuery pq = imagestorage.prepare(query);
	    
	    if (pq.equals(null)) {
	    	resp.getWriter().println("<p>No images here</p>");
	    }
	    else
	    {
	    	//start cycling through every item in the datastorage
	    	resp.getWriter().println("<table>");
	    	for(Entity image : pq.asIterable())
	    	{
	    		
	    		String check1 = "Public";
	    		String check2 = (String) image.getProperty("privacy");
	    		String owner = (String) image.getProperty("owner");
	    		//it's marked as public, print it
	    		if(check1.equals(check2))
	    		{
	    			//get the url of the image
	    			resp.getWriter().println("<tr><td>");
	    			String url = imagesService.getServingUrl((BlobKey) image.getProperty("key"));
	    			url = url.concat("=s").concat(Integer.toString(75));
	    			
	    			//display the image and give it a link to it's full sized counterpart
	    			BlobKey blobKey = (BlobKey) image.getProperty("key");
	    			String keystring =  blobKey.getKeyString();
	    			String redirect = "/serve?blob-key=" + keystring;
	    			resp.getWriter().println("<a href =\""+redirect+"\"><img src=\""+url+"\"> </img><a>");
	    			
	    			//if the view owns the image, give them the option to delete it (doesn't work)
	    			// they also get the option if they're on an administrator account
	    			if(userEmail.equals(owner) || userEmail.equals( "daly.marcus@gmail.com") || userEmail.equals("mark.deegan@dit.ie"))
	    			{
	    				
	    				resp.getWriter().println("<input type=\"submit\" value=\"Delete Image\">");
	    			}
	    			resp.getWriter().println("</td></tr>");
	    		}
		    }
	    	
	    }
	    //print a message to return to the home page
	    resp.getWriter().println("<p><a href=\"c12474932_cloud_assignment\">Return</a></p>");
	}
}