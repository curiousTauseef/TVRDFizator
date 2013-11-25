package fr.eurecom.tvrdfizator.core;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

public class LegacyProcessing extends Thread {

	UUID idMediaResource;
	String metadataType;
	String metadataFile;
	DBCollection mediaresources;
	DBObject mr;
	DB db;
	String namespace;
	String locator;

	
	public LegacyProcessing (DB db,UUID idMediaResource, String metadataType, String metadataFile, DBCollection mediaresources, DBObject mr,  String namespace, String locator){

		
		this.namespace = namespace;
		this.locator = locator;
		this.db = db;
		this.idMediaResource = idMediaResource;
		this.metadataType = metadataType;
		this.metadataFile = metadataFile;
		this.mediaresources = mediaresources;
		this.mr=mr;
		
	}
	
	
    public void run() {
        System.out.println("Entering Processing!");
        
		GridFS gfsmr = new GridFS(db);
		
		UUID idtransaction = UUID.randomUUID();

		
		 //Create inputFile & outputFile
		 File metadataFileDisk = new File("./data/legacy_"+idMediaResource.toString()+idtransaction+".tva");
		 File fileSerializationDisk  = new File("./data/legacy_"+idMediaResource.toString()+idtransaction+".ttl");

		try {
			FileUtils.writeStringToFile(metadataFileDisk, metadataFile, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//SERIALIZATION
		 Processing p = new Processing();
		 //System.out.println("INICIANDO LEGACY ");
		 if (p.legacy_process( "./data/legacy_"+idMediaResource.toString()+idtransaction+".tva", "./data/legacy_"+idMediaResource.toString()+idtransaction+".ttl", idMediaResource.toString(), namespace, locator)){
		 
			 //Storing in the database
			 
			 
			GridFSInputFile gfsFile = null;
			try {
				gfsFile = gfsmr.createFile(fileSerializationDisk);
				gfsFile.setFilename(idMediaResource.toString()+"_"+metadataType+"_serialization");
				gfsFile.save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			 //System.out.println("FINALIZANDO LEGACY ");

			mr.put(metadataType+"Serialization", gfsFile);
			mediaresources.save(mr);

		 }
			metadataFileDisk.delete();
			fileSerializationDisk.delete();


			
        System.out.println("Finishing Processing!");
    }

}
