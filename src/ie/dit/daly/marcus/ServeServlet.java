package ie.dit.daly.marcus;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class ServeServlet extends HttpServlet {
    

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
    	//when called to display a blob, display that blob
            BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
            BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
            blobstoreService.serve(blobKey, resp);
          
        }
}
