package fr.eurecom.tvrdfizator.web.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;

@Path("/serialization")
public class Serialization {

	@GET
	@Path("/legacy")
	@Produces({"application/rdf+xml"})
	public Response doGETSERIALIZATION(   @Context UriInfo ui, 
                        			@Context SecurityContext context,
                        			@QueryParam("mediaResourceID") String mediaResourceID) {
		
		if (mediaResourceID == null){
			return Response.serverError().build();
		}
		FileReader file = null;
		//System.out.println("Working Directory = " + System.getProperty("user.dir"));
		try {
			file = new FileReader(new File("./data/1/legacy_main.ttl"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        return Response.ok().entity(file).header("Access-Control-Allow-Origin", "*").build();

        
        

	}
}
	
	