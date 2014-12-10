package ie.dit.daly.marcus;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
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
	private static final long serialVersionUID = 1L;
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    DatastoreService imagestorage = DatastoreServiceFactory.getDatastoreService();
 
    /*public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		
		com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
		Principal myPrincipal = req.getUserPrincipal();
		
		String emailAddress = myPrincipal.getName();
		writer.println("Uploads page for " + emailAddress);
		doPost(req, resp);
    }*/
   
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	//try
    	//{
    		String userEmail = null;
    		Principal myPrincipal = req.getUserPrincipal();
    		/*if(myPrincipal == null) 
    		{
    			resp.sendRedirect("/C12474932_Cloud_Assignment");
    		}
    		else
    		{*/
    			userEmail = myPrincipal.getName();
    		//}
            @SuppressWarnings("deprecation")
            Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
            BlobKey blobKey = blobs.get("myFile");
            BlobKey blobName = blobs.get("myFileName");
           
            if (blobKey == null)
            {
                    resp.sendRedirect("/");
            }
            else
            {
                    try { Thread.sleep(5000); } catch (InterruptedException e) {}
                    System.out.println("Uploaded a file with blobKey:"+blobKey.getKeyString());
                    //add to image storage
                    Entity newimage = new Entity("image");
                    //get privacy
                    String privacy = req.getParameter("radios");
                    if(privacy ==null)
                    {
                    	privacy = "Public";
                    }
                    
                    newimage.setProperty("name", blobName);
                    newimage.setProperty("key",blobKey);
                    newimage.setProperty("owner", userEmail);
                    newimage.setProperty("privacy", privacy);
                    imagestorage.put(newimage);
                    resp.sendRedirect("/serve?blob-key=" + blobKey.getKeyString());
            }
    	/*}catch (Exception e) {
    		resp.sendRedirect("/");
            System.out.println("Something caused to picture to fail to upload. You have been redirected to the home page");
    	}*/
    }
}