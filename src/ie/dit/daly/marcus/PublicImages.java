package ie.dit.daly.marcus;

import java.io.IOException;
import java.security.Principal;

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

@SuppressWarnings("serial")
public class PublicImages extends HttpServlet{
	@SuppressWarnings("deprecation")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		DatastoreService imagestorage = DatastoreServiceFactory.getDatastoreService();
		ImagesService imagesService = ImagesServiceFactory.getImagesService();
		
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
	    		String check1 = "Public";
	    		String check2 = (String) image.getProperty("privacy");
	    		String owner = (String) image.getProperty("owner");
	    		if(check1.equals(check2))
	    		{
	    			resp.getWriter().println("<tr><td>");
	    			String url = imagesService.getServingUrl((BlobKey) image.getProperty("key"));
	    			url = url.concat("=s").concat(Integer.toString(75));
	    			resp.getWriter().println("<img src=\""+url+"\"> </img>");
	    			resp.getWriter().println("</td></tr>");
	    		}
		    }
	    }
	}
}