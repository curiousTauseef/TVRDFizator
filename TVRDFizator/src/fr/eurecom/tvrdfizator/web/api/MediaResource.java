package fr.eurecom.tvrdfizator.web.api;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
            @PathParam("idMediaResource") String idMediaResourceString) {
		
		

		
		//Parse UUID
		UUID idMediaResource;
		try {
			idMediaResource = UUID.fromString(idMediaResourceString);
		} catch (IllegalArgumentException e) {
			System.out.println("Illegal UUID ");
			e.printStackTrace();
			return Response.status(501).build();
		}

		System.out.println("CREATION, parameters: "+ locator +", "+namespace);
		MongoClient client= MongoDBUtil.getSessionMongo();
		DB db = client.getDB("TVRDFizatorDB" );

		DBCollection mediaresources = db.getCollection("mediaResources");
		
		
		//Check if the Media resource already exists
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("_id", idMediaResource.toString());
		DBCursor cursor = mediaresources.find(whereQuery);
		DBObject mr = null;

		if (cursor.count() == 0 ){ //Create new MediaResource
			System.out.println("Creating MediaResource "+idMediaResource);
			mr= new BasicDBObject();
			mr.put("_id", idMediaResource.toString());
			mr.put("locator", locator);
			mr.put("namespace", namespace);
			mediaresources.insert(mr);

		}
		else{ //Update attributes of the resource.
			mr = cursor.next();
			if (locator != null){

				 mr.put("locator", locator);
				 mediaresources.save(mr);


			}
			if (namespace != null){

				 mr.put("namespace", namespace);
				 mediaresources.save(mr);

			}
			//relaunch Serializations with the new parameters
			executeAll(db, idMediaResource, mediaresources, mr);
		}
		
		return Response.status(200).entity("OK").build();
	}
	
	
	@GET
	@Path("/{idMediaResource}")
	public Response doGETRESOURCE(
            @PathParam("idMediaResource") String idMediaResourceString) {
		
		//Parse UUID
		UUID idMediaResource;
		try {
			idMediaResource = UUID.fromString(idMediaResourceString);
		} catch (IllegalArgumentException e) {
			System.out.println("Illegal UUID ");
			e.printStackTrace();
			return Response.status(501).build();
		}
		
		
		
		System.out.println("RETRIEVING");
		MongoClient client= MongoDBUtil.getSessionMongo();
		DB db = client.getDB("TVRDFizatorDB" );

		DBCollection mediaresources = db.getCollection("mediaResources");
		
		
		//Check if the Media resource already exists
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("_id", idMediaResource.toString());
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
            @PathParam("idMediaResource") String idMediaResourceString) {
 
		
		//Parse UUID
		UUID idMediaResource;
		try {
			idMediaResource = UUID.fromString(idMediaResourceString);
		} catch (IllegalArgumentException e) {
			System.out.println("Illegal UUID ");
			e.printStackTrace();
			return Response.status(501).build();
		}
		
		
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
		whereQuery.put("_id", idMediaResource.toString());
		DBCursor cursor = mediaresources.find(whereQuery);
		DBObject mr = null;

		if (cursor.count() == 0 ){ //Create new MediaResource
			System.out.println("MediaResource "+idMediaResource+" not found.");
	        return Response.status(404).build();
		}
		else{ //update existing mediaresource
			System.out.println("Attaching "+ metadataType + " file to existing MediaResource "+idMediaResource+".");
			mr = cursor.next();

			

			//Delete old file if exists
			GridFS gfsmrDel = new GridFS(db);
			
			GridFSDBFile fileMetadata = gfsmrDel.findOne(idMediaResource.toString()+"_"+metadataType);
			if (fileMetadata != null) gfsmrDel.remove(fileMetadata); //Delete old serialization results

					
			mr.removeField(metadataType);
			mediaresources.save(mr);

			File metadataFileDisk = new File("./data/"+idMediaResource+"_"+metadataType);
			try {
				FileUtils.writeStringToFile(metadataFileDisk, metadataFile, "UTF-8");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			GridFS gfsmrAdd = new GridFS(db);
			GridFSInputFile gfsFile = null;
			try {
				gfsFile = gfsmrAdd.createFile(metadataFileDisk);
				gfsFile.setFilename(idMediaResource.toString()+"_"+metadataType);
				gfsFile.save();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			metadataFileDisk.delete();
			
			mr.put(metadataType, gfsFile);
			mediaresources.save(mr);

			

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
                        			@PathParam("idMediaResource") String idMediaResourceString) {
		
		
		
		//Parse UUID
		UUID idMediaResource;
		try {
			idMediaResource = UUID.fromString(idMediaResourceString);
		} catch (IllegalArgumentException e) {
			System.out.println("Illegal UUID ");
			e.printStackTrace();
			return Response.status(501).build();
		}
		
		
		//We need to know the type of the data we are accessing
		if (metadataType == null || !isValidMetadataType(metadataType)){
			System.out.println("Wrong metadata type or not specified.");
			return Response.status(404).build();
		}
		
		
		MongoClient client= MongoDBUtil.getSessionMongo();
		DB db = client.getDB("TVRDFizatorDB" );
		
		DBCollection mediaresources = db.getCollection("mediaResources");

		//Check if the Media resource  exists
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("_id", idMediaResource.toString());
		DBCursor cursor = mediaresources.find(whereQuery);
		DBObject mr = null;
		

		if (cursor.count() == 0 ){ //MediaResource
			System.out.println("MediaResource "+idMediaResource+" not found.");
	        return Response.status(404).build();
		}
		else{ //Return metadata file if exists
			mr = cursor.next();
			BasicDBObject fileInformation = (BasicDBObject) mr.get(metadataType);
			if(fileInformation == null) return Response.status(404).build();
			else      {
				//String fileName = (String) fileInformation.get("filename");
				
				GridFS gfsmr = new GridFS(db);
				//System.out.println(fileName);
				GridFSDBFile fileText = gfsmr.findOne(fileInformation);
				String filebody = "";
				try {
					File fileMetadataDisk  = new File("./data/metadata"+ idMediaResource.toString() +".ttl");
					fileText.writeTo(fileMetadataDisk);
					filebody = FileUtils.readFileToString(fileMetadataDisk, "UTF-8");
					fileMetadataDisk.delete();

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

	private void executeSerialization(DB db, UUID idMediaResource, String metadataType, String metadataFile, DBCollection mediaresources, DBObject mr) {
		// TODO Auto-generated method stub
		
		//Retrieve parameters for serialization
		String locator = "";
		String namespace = "";
		if (mr.get("locator") != null) locator = (String) mr.get("locator");
		if (mr.get("namespace") != null) namespace = (String) mr.get("namespace");

		
/*		
		
		 BasicDBObject auxMediaResource = new BasicDBObject();
		 auxMediaResource.append("$unset", new BasicDBObject().append("Serization", 1));
		 mediaresources.update(mr,auxMediaResource);
		 mediaresources.save(mr);
		 
		 BasicDBObject auxMediaResource2 = new BasicDBObject();
		 auxMediaResource2.append("$unset", new BasicDBObject().append("Serializationnnnn", "BYE"));
		 mediaresources.update(mr,auxMediaResource2);
		 mediaresources.save(mr);*/
		 
		
		//Delete old serialization, it is not valid anymore even if the new one contains errors.
		GridFS gfsmrDel = new GridFS(db);
		GridFSDBFile fileSerialization = gfsmrDel.findOne(idMediaResource.toString()+"_"+metadataType+"_serialization");
		if (fileSerialization != null) gfsmrDel.remove(fileSerialization); //Delete old serialization results
		
		mr.removeField(metadataType+"Serialization");
		mediaresources.save(mr);
		
		
		
		switch(toIntMetadataType (metadataType)) {
		 case 0: 
			 System.out.println("Starting Legacy metadata serialization...");
			 LegacyProcessing threadlegacy = new LegacyProcessing(db, idMediaResource, metadataType, metadataFile, mediaresources, mr, namespace, locator);
			 threadlegacy.start();
			 break;
		 case 1: 
			 System.out.println("Starting Subtitle and Entities serialization...");
			 SubtitleProcessing threadsubtitle = new SubtitleProcessing(db, idMediaResource, metadataType, metadataFile, mediaresources, mr, namespace, locator);
			 threadsubtitle.start();
		     break;
		 case 2: 
			 System.out.println("Starting Exmeralda serialization...");
			 ExmeraldaProcessing threadexmeralda = new ExmeraldaProcessing(db, idMediaResource, metadataType, metadataFile, mediaresources, mr, namespace, locator);
			 threadexmeralda.start();
		     break;
		 }
		
	}
	
	private void executeAll(DB db, UUID idMediaResource, DBCollection mediaresources, DBObject mr){
		for (int i = 0; i<metadataTypes.length; i++){
			String metadataType = metadataTypes[i];
			
			BasicDBObject fileInformation = (BasicDBObject) mr.get(metadataType);
			if(fileInformation != null) {
				
				GridFS gfsmr = new GridFS(db);
				GridFSDBFile fileText = gfsmr.findOne(fileInformation);
				String filebody = "";
				try {
					File fileMetadataDisk  = new File("./data/metadata"+ idMediaResource +".ttl");
					fileText.writeTo(fileMetadataDisk);
					filebody = FileUtils.readFileToString(fileMetadataDisk, "UTF-8");
					fileMetadataDisk.delete();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				executeSerialization(db, idMediaResource, metadataType, filebody, mediaresources, mr);
			}
			
		}
	}


	
	@GET
	@Path("/{idMediaResource}/serialization")
	@Produces({"application/rdf+xml"})
	public Response doGETSERIALIZATION(   @Context UriInfo ui, 
                        			@Context SecurityContext context,
                        			@QueryParam("metadataType") String metadataType,
                        			@PathParam("idMediaResource") String idMediaResourceString) {
		
		
		//Parse UUID
		UUID idMediaResource;
		try {
			idMediaResource = UUID.fromString(idMediaResourceString);
		} catch (IllegalArgumentException e) {
			System.out.println("Illegal UUID ");
			e.printStackTrace();
			return Response.status(501).build();
		}
		

		//We need to know the type of the data we are
		if (metadataType != null && !isValidMetadataType(metadataType)){
			System.out.println("Wrong metadata type or not specified.");
			return Response.status(404).build();
		}
		
		
		MongoClient client= MongoDBUtil.getSessionMongo();
		DB db = client.getDB("TVRDFizatorDB" );
		
		
		DBCollection mediaresources = db.getCollection("mediaResources");

		//Check if the Media resource  exists
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("_id", idMediaResource.toString());
		DBCursor cursor = mediaresources.find(whereQuery);
		DBObject mr = null;
		


		
		if (cursor.count() == 0 ){ //MediaResource
			System.out.println("MediaResource "+idMediaResource+" not found.");
	        return Response.status(404).build();
		}
		else{ //Return metadata file if exists
			mr = cursor.next();
			
			String filebody = null;
			if (metadataType == null){
				
				 filebody = getAllSerialization(db, mr, idMediaResource);
				return Response.ok().entity(filebody).header("Access-Control-Allow-Origin", "*").header("filename","downloaded.pdf").build();
			}
			
			
			else  {    
				
				filebody = getSerialization(db, mr, metadataType, idMediaResource);
				
				return Response.ok().entity(filebody).header("Access-Control-Allow-Origin", "*").header("filename","downloaded.pdf").build();
			}

		}
		
		
	}


	private String getAllSerialization(DB db, DBObject mr, UUID idMediaResource) {
		String filebody = "";

		for (int i = 0; i<metadataTypes.length; i++){
			String metadataType = metadataTypes[i];
			String serializationPart = getSerialization(db, mr, metadataType, idMediaResource);
			System.out.println(serializationPart);
			if (i != 0){ //remove prefixes except the first time
				serializationPart = serializationPart.replaceAll("@prefix", "");
			}
			System.out.println(serializationPart);

			filebody = filebody + serializationPart;
		}
		
		return filebody;
	}


	private String getSerialization(DB db, DBObject mr, String metadataType, UUID idMediaResource) {
		BasicDBObject fileInformation = (BasicDBObject) mr.get(metadataType+"Serialization");

		GridFS gfsmr = new GridFS(db);
		//System.out.println(fileName);
		GridFSDBFile fileText = gfsmr.findOne(fileInformation);
		String filebody = "";
		try {
			File fileMetadataDisk  = new File("./data/serialization"+ idMediaResource +".ttl");
			fileText.writeTo(fileMetadataDisk);
			filebody = FileUtils.readFileToString(fileMetadataDisk, "UTF-8");
			fileMetadataDisk.delete();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return filebody;
	}

}