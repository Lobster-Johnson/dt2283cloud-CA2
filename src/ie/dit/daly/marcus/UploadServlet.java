package ie.dit.daly.marcus;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;









import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserServiceFactory;

public class UploadServlet extends HttpServlet {
	
	//create blobstore for blobs, datastore for saving blobs and userservice for finding out who's doing the uploading
	private static final long serialVersionUID = 1L;
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    DatastoreService imagestorage = DatastoreServiceFactory.getDatastoreService();
    com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
 
   
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    		//find their email address, assuming they're logged in
    		//which they should be as they can't get this far without being logged in
    		String userEmail = null;
    		
    		if(userService.isUserLoggedIn())
    		{
    			userEmail = userService.getCurrentUser().getEmail();
    		}
    		
    		//add file to blob
            @SuppressWarnings("deprecation")
            Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
            BlobKey blobKey = blobs.get("myFile");
            
           //if no file has been selected and the user tries to submit, redirect them back here
            if (blobKey == null)
            {
                    resp.sendRedirect("/");
            }
            else
            {		//get values from the form and insert them into the entity as well
                    try { Thread.sleep(5000); } catch (InterruptedException e) {}
                    System.out.println("Uploaded a file with blobKey:"+blobKey.getKeyString());
                    
                    //create new entity
                    Entity newimage = new Entity("image");
                    
                    //get privacy
                    String privacy = req.getParameter("radios");
                    
                    //default to private for an admin
                    if(userEmail .equals( "daly.marcus@gmail.com") || userEmail .equals("mark.deegan@dit.ie"))
                    {
                    	privacy = "Private";
                    }
                    else
                    {
                    	privacy = "Public";
                    }
                    
                    //add key, owner and privacy to entity
                    newimage.setProperty("key",blobKey);
                    newimage.setProperty("owner", userEmail);
                    newimage.setProperty("privacy", privacy);
                    
                    //store entity
                    imagestorage.put(newimage);
                    
                    
                    //redirect depending on whether the uploaded image was intended to be public or private
                    if(privacy.equals("Public"))
                    {
                    	resp.sendRedirect("/publicimages");
                    }
                    else
                    {
                    	resp.sendRedirect("/privateimages");
                    }
            }
    	
    }
}