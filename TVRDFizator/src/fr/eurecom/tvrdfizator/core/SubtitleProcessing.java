package fr.eurecom.tvrdfizator.core;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

import fr.eurecom.nerd.client.NERD;
import fr.eurecom.nerd.client.type.DocumentType;
import fr.eurecom.nerd.client.type.ExtractorType;

public class SubtitleProcessing extends Thread {

	long idMediaResource;
	String metadataType;
	String metadataFile;
	DBCollection mediaresources;
	DBObject mr;
	DB db;
	
	public SubtitleProcessing (DB db, long idMediaResource, String metadataType, String metadataFile, DBCollection mediaresources, DBObject mr){

		this.db = db;
		this.idMediaResource = idMediaResource;
		this.metadataType = metadataType;
		this.metadataFile = metadataFile;
		this.mediaresources = mediaresources;
		this.mr=mr;
		
	}
	
	
    public void run() {
        System.out.println("Entering Processing!");
        
        
		 //Create inputFile & outputFile
		File metadataFileDisk = new File("./data/subtitle.str");
		try {
			FileUtils.writeStringToFile(metadataFileDisk, metadataFile, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    NERD nerd = new NERD("loq6asma69tgfq2aijbubh2t5klm7pk0");
	    String json = nerd.annotateJSON(ExtractorType.ALCHEMYAPI, 
	                                    DocumentType.TIMEDTEXT,
	                                    metadataFile);
	    

	    try {
			FileUtils.writeStringToFile(new File("./data/entities.json"), json, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//SERIALIZATION
		 Processing p = new Processing();
		 p.entity_process("./data/entities.json", "./data/subtitle.str", "./data/entities.ttl", Long.toString(idMediaResource));
		 
		 
		 //Storing in the database
		 File fileSerializationDisk  = new File("./data/entities.ttl");
		

		GridFS gfsmr = new GridFS(db, "mediaResources");
		GridFSInputFile gfsFile = null;
		try {
			gfsFile = gfsmr.createFile(fileSerializationDisk);
			gfsFile.setFilename(idMediaResource+"_"+metadataType+"_serialization");
			gfsFile.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		metadataFileDisk.delete();
		 
		
		 BasicDBObject auxMediaResource = new BasicDBObject();
		 auxMediaResource.append("$set", new BasicDBObject().append(metadataType+"Serialization", gfsFile));
		 mediaresources.update(mr,auxMediaResource);
		 
		 //Delete both created files
		 metadataFileDisk.delete();
		 
		 
        System.out.println("Finishing Processing!");
    }

}
