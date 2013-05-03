package fr.eurecom.tvrdfizator.core;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class LegacyProcessing extends Thread {

	long idMediaResource;
	String metadataType;
	String metadataFile;
	DBCollection mediaresources;
	DBObject mr;
	
	public LegacyProcessing (long idMediaResource, String metadataType, String metadataFile, DBCollection mediaresources, DBObject mr){

		this.idMediaResource = idMediaResource;
		this.metadataType = metadataType;
		this.metadataFile = metadataFile;
		this.mediaresources = mediaresources;
		this.mr=mr;
		
	}
	
	
    public void run() {
        System.out.println("Entering Processing!");
        
        
		 //Create inputFile & outputFile
		 File metadataFileDisk = new File("./data/legacy.tva");
		try {
			FileUtils.writeStringToFile(metadataFileDisk, metadataFile, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//SERIALIZATION
		 Processing p = new Processing();
		 p.legacy_process( "./data/legacy.tva", "./data/legacy.ttl", Long.toString(idMediaResource));
		 
		 
		 //Storing in the database
		 File fileSerializationDisk  = new File("./data/legacy.ttl");
		 String textSerialization = null;
		try {
			textSerialization = FileUtils.readFileToString(fileSerializationDisk, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 BasicDBObject auxMediaResource = new BasicDBObject();
		 auxMediaResource.append("$set", new BasicDBObject().append(metadataType+"Serialization", textSerialization));
		 mediaresources.update(mr,auxMediaResource);
		 
		 //Delete both created files
		 metadataFileDisk.delete();
		 fileSerializationDisk.delete();
		 
		 
        System.out.println("Finishing Processing!");
    }

}
