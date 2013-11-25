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

public class ExmeraldaProcessing extends Thread {

	UUID idMediaResource;
	String metadataType;
	String metadataFile;
	DBCollection mediaresources;
	DBObject mr;
	DB db;
	String namespace;
	String locator;
	
	public ExmeraldaProcessing (DB db, UUID idMediaResource, String metadataType, String metadataFile, DBCollection mediaresources, DBObject mr, String namespace, String locator){

		this.locator = locator;
		this.namespace = namespace;
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
		 File metadataFileDisk = new File("./data/exmeralda_"+idMediaResource+idtransaction+".exb");
		try {
			FileUtils.writeStringToFile(metadataFileDisk, metadataFile, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 File fileSerializationDisk  = new File("./data/exmeralda_"+idMediaResource+idtransaction+".ttl");


		//SERIALIZATION
		 Processing p = new Processing();
		 if (p.analisys_process( "./data/exmeralda_"+idMediaResource+idtransaction+".exb",  "./data/exmeralda_"+idMediaResource+idtransaction+".ttl", idMediaResource.toString(), namespace, locator)){

		 

			
			 //Storing in the database
	
				GridFSInputFile gfsFile = null;
				try {
					gfsFile = gfsmr.createFile(fileSerializationDisk);
					gfsFile.setFilename(idMediaResource+"_"+metadataType+"_serialization");
					gfsFile.save();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				mr.put(metadataType+"Serialization", gfsFile);
				mediaresources.save(mr);
		 }
		 
		 
			metadataFileDisk.delete();
			fileSerializationDisk.delete();


        System.out.println("Finishing Processing!");
    }

}
