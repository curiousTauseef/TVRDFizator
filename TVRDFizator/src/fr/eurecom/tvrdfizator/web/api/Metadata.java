package fr.eurecom.tvrdfizator.web.api;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;


@Path("/metadata")
public class Metadata {



	
	
	@POST
	@Path("/{idMediaResource}")
	@Consumes(MediaType.TEXT_XML)
	public Response uploadFile(String metadataFile,
			@QueryParam("metadataType") String metadataType,
            @PathParam("idMediaResource") int idMediaResource) {
 
		System.out.println("Received " + metadataType + idMediaResource);
		
		//Save File in disk
		try {
			FileUtils.writeStringToFile(new File("./data/1/legacy.txt"), metadataFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return Response.status(200).entity("OK").build();
	}

	

}