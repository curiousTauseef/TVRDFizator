package fr.eurecom.tvrdfizator.web.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.FileUtils;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import fr.eurecom.tvrdfizator.core.ExmeraldaProcessing;
import fr.eurecom.tvrdfizator.core.LegacyProcessing;
import fr.eurecom.tvrdfizator.core.SubtitleProcessing;
import fr.eurecom.tvrdfizator.db.MongoDBUtil;


@Path("/mediaresource")
public class MediaResource {

	//Those are the current types 
	public String[] metadataTypes = {"legacy", "subtitle", "exmeralda"};
	
	
	@POST
	@Path("/{idMediaResource}")
	public Response doCREATERESOURCE(
			@QueryParam("locator") String locator,
			@QueryParam("namespace") String namespace,
            @PathParam("idMediaResource") long idMediaResource) {
		
		
		System.out.println("CREATION");
		MongoClient client= MongoDBUtil.getSessionMongo();
		DB db = client.getDB("TVRDFizatorDB" );

		DBCollection mediaresources = db.getCollection("mediaResources");
		
		
		//Check if the Media resource already exists
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("_id", idMediaResource);
		DBCursor cursor = mediaresources.find(whereQuery);
		DBObject mr = null;

		if (cursor.count() == 0 ){ //Create new MediaResource
			System.out.println("Creating MediaResource "+idMediaResource);
			mr= new BasicDBObject();
			mr.put("_id", idMediaResource);
			mr.put("locator", locator);
			mr.put("namespace", namespace);
			mediaresources.insert(mr);
		}
		else{
			return Response.status(409).entity("Resource already exists").build();
		}
		
		return Response.status(200).entity("OK").build();
	}
	
	
	@GET
	@Path("/{idMediaResource}")
	public Response doGETRESOURCE(
            @PathParam("idMediaResource") long idMediaResource) {
		
		
		System.out.println("RETRIEVING");
		MongoClient client= MongoDBUtil.getSessionMongo();
		DB db = client.getDB("TVRDFizatorDB" );

		DBCollection mediaresources = db.getCollection("mediaResources");
		
		
		//Check if the Media resource already exists
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("_id", idMediaResource);
		DBCursor cursor = mediaresources.find(whereQuery);
		DBObject mr = null;

		if (cursor.count() == 0 ){ //Create new MediaResource
			System.out.println("MediaResource "+idMediaResource+" not found.");
	        return Response.status(404).build();
		}
		else{ //update existing mediaresource
			mr = cursor.next();
		}
		
        return Response.ok().entity(mr.toString()).header("Access-Control-Allow-Origin", "*").build();

       	}
	
	
	@POST
	@Path("/{idMediaResource}/metadata")
	@Consumes(MediaType.TEXT_XML)
	public Response doPOSTMETADATA(String metadataFile,
			@QueryParam("metadataType") String metadataType,
			@QueryParam("metadataResource") String metadataResource,
            @PathParam("idMediaResource") long idMediaResource) {
 
		System.out.println("Received metadata \"" + metadataType + "\" for MediaResource " + idMediaResource);
		
		//We need to know the type of the data we are 
		if (metadataType == null || !isValidMetadataType(metadataType)){
			System.out.println("Wrong metadata type or not specified.");
			return Response.status(404).build();
		}
		
		//If the metadata is comming from a file
		if (metadataResource != null){
			
		}
		
		MongoClient client= MongoDBUtil.getSessionMongo();
		DB db = client.getDB("TVRDFizatorDB" );

		DBCollection mediaresources = db.getCollection("mediaResources");
		
		//Check if the Media resource already exists
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("_id", idMediaResource);
		DBCursor cursor = mediaresources.find(whereQuery);
		DBObject mr = null;

		if (cursor.count() == 0 ){ //Create new MediaResource
			System.out.println("MediaResource "+idMediaResource+" not found.");
	        return Response.status(404).build();
		}
		else{ //update existing mediaresource
			System.out.println("Attaching "+ metadataType + " file to existing MediaResource "+idMediaResource+".");
			
	

			File metadataFileDisk = new File("./data/"+idMediaResource+"_"+metadataType);
			try {
				FileUtils.writeStringToFile(metadataFileDisk, metadataFile, "UTF-8");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GridFS gfsmr = new GridFS(db, "mediaResources");
			GridFSInputFile gfsFile = null;
			try {
				gfsFile = gfsmr.createFile(metadataFileDisk);
				gfsFile.setFilename(idMediaResource+"_"+metadataType);
				gfsFile.save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			metadataFileDisk.delete();
			
			BasicDBObject auxMediaResource = new BasicDBObject();
			auxMediaResource.append("$set", new BasicDBObject().append(metadataType, gfsFile));
			mr = cursor.next();
			mediaresources.update(mr,auxMediaResource);
		}
	
		//Launch Serialization process 
		executeSerialization(db, idMediaResource, metadataType, metadataFile, mediaresources, mr);
	
		
		return Response.status(200).entity("OK").build();
	}



	@GET
	@Path("/{idMediaResource}/metadata")
	@Produces({"application/rdf+xml"})
	public Response doGETMETADATA(   @Context UriInfo ui, 
                        			@Context SecurityContext context,
                        			@QueryParam("metadataType") String metadataType,
                        			@PathParam("idMediaResource") long idMediaResource) {
		
		//We need to know the type of the data we are 
		if (metadataType == null || !isValidMetadataType(metadataType)){
			System.out.println("Wrong metadata type or not specified.");
			return Response.status(404).build();
		}
		
		
		MongoClient client= MongoDBUtil.getSessionMongo();
		DB db = client.getDB("TVRDFizatorDB" );
		
		DBCollection mediaresources = db.getCollection("mediaResources");

		//Check if the Media resource  exists
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("_id", idMediaResource);
		DBCursor cursor = mediaresources.find(whereQuery);
		DBObject mr = null;
		

		if (cursor.count() == 0 ){ //MediaResource
			System.out.println("MediaResource "+idMediaResource+" not found.");
	        return Response.status(404).build();
		}
		else{ //Return metadata file if exists
			mr = cursor.next();
			GridFSDBFile file = (GridFSDBFile) mr.get(metadataType);
			if(file == null) return Response.status(404).build();
			else      {
				String filebody = "";
				try {
					file.writeTo(filebody);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return Response.ok().entity(filebody).header("Access-Control-Allow-Origin", "*").build();
			}

		}

	}
	
	
	private boolean isValidMetadataType(String metadataType) {
		
		Vector<String> metadataTypesV = new Vector <String>(Arrays.asList(metadataTypes));	
		return  (metadataTypesV.contains(metadataType));
			
	}
	
	private int toIntMetadataType(String metadataType) {
		
		Vector<String> metadataTypesV = new Vector <String>(Arrays.asList(metadataTypes));	
		System.out.println(metadataTypesV.size());
		System.out.println(metadataType);
		return  (metadataTypesV.indexOf(metadataType));
			
	}

	private void executeSerialization(DB db, long idMediaResource, String metadataType, String metadataFile, DBCollection mediaresources, DBObject mr) {
		// TODO Auto-generated method stub
		switch(toIntMetadataType (metadataType)) {
		 case 0: 
			 System.out.println("Starting Legacy metadata serialization...");
			 LegacyProcessing threadlegacy = new LegacyProcessing(idMediaResource, metadataType, metadataFile, mediaresources, mr);
			 threadlegacy.start();
			 break;
		 case 1: 
			 System.out.println("Starting Subtitle and Entities serialization...");
			 SubtitleProcessing threadsubtitle = new SubtitleProcessing(db,idMediaResource, metadataType, metadataFile, mediaresources, mr);
			 threadsubtitle.start();
		     break;
		 case 2: 
			 System.out.println("Starting Exmeralda serialization...");
			 ExmeraldaProcessing threadexmeralda = new ExmeraldaProcessing(idMediaResource, metadataType, metadataFile, mediaresources, mr);
			 threadexmeralda.start();
		     break;
		 }
		
	}


	
	@GET
	@Path("/{idMediaResource}/serialization")
	@Produces({"application/rdf+xml"})
	public Response doGETSERIALIZATION(   @Context UriInfo ui, 
                        			@Context SecurityContext context,
                        			@QueryParam("metadataType") String metadataType,
                        			@PathParam("idMediaResource") long idMediaResource) {
		

		//We need to know the type of the data we are 
		if (metadataType == null || !isValidMetadataType(metadataType)){
			System.out.println("Wrong metadata type or not specified.");
			return Response.status(404).build();
		}
		
		
		MongoClient client= MongoDBUtil.getSessionMongo();
		DB db = client.getDB("TVRDFizatorDB" );
		
		
		DBCollection mediaresources = db.getCollection("mediaResources");

		//Check if the Media resource  exists
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("_id", idMediaResource);
		DBCursor cursor = mediaresources.find(whereQuery);
		DBObject mr = null;
		

		if (cursor.count() == 0 ){ //MediaResource
			System.out.println("MediaResource "+idMediaResource+" not found.");
	        return Response.status(404).build();
		}
		else{ //Return metadata file if exists
			mr = cursor.next();
			String filename = (String) mr.get((metadataType+"Serialization"));
			if(filename == null) return Response.status(404).build();
			else      return Response.ok().entity(filename).header("Access-Control-Allow-Origin", "*").build();

		}

	}

}