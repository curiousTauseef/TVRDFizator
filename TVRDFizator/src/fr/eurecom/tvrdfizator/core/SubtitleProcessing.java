package fr.eurecom.tvrdfizator.core;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

import fr.eurecom.nerd.client.LookUp;
import fr.eurecom.nerd.client.NERD;
import fr.eurecom.nerd.client.type.DocumentType;
import fr.eurecom.nerd.client.type.Extractor;
import fr.eurecom.nerd.client.type.ExtractorType;

public class SubtitleProcessing extends Thread {

	UUID idMediaResource;
	String metadataType;
	String metadataFile;
	DBCollection mediaresources;
	DBObject mr;
	DB db;
	String locator;
	String namespace;

	
	public SubtitleProcessing (DB db, UUID idMediaResource, String metadataType, String metadataFile, DBCollection mediaresources, DBObject mr,  String namespace, String locator){

		
		this.namespace = namespace;
		this.locator = locator;
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
		String subtitleFile = null;
		String apiKey = "loq6asma69tgfq2aijbubh2t5klm7pk0";
		ExtractorType extractor = ExtractorType.COMBINED;

		
		UUID idtransaction = UUID.randomUUID();
		 //Create inputFile & outputFile
		File metadataFileDisk = new File("./data/subtitle_"+idMediaResource+idtransaction+".str");
		try {
			FileUtils.writeStringToFile(metadataFileDisk, metadataFile, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		if (mr.containsField("subtitleURL")){
			subtitleFile = (String) mr.get("subtitleURL");
			System.out.println("Subtitle resource available");
		}
		if (mr.containsField("apiKey")){
			apiKey = (String) mr.get("apiKey");
		}
		if (mr.containsField("extractor")){
			try {
				extractor = Extractor.getType((String) mr.get("extractor"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Launching NERD extraction, ("+apiKey+", "+ LookUp.mapExtractor(extractor)+")");
	    NERD nerd = new NERD(apiKey);
	    //NERD nerd = new NERD("1g0stre00us2jjgtb2g80190rv7bpm4v");


		String json = nerd.annotateJSON(extractor, 
	                                    DocumentType.TIMEDTEXT,
	                                    metadataFile);
	    

	    File entitiesNerd = new File("./data/entities_"+idMediaResource+idtransaction+".json");
	    try {
			FileUtils.writeStringToFile(entitiesNerd, json, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 File fileSerializationDisk  = new File("./data/entities_"+idMediaResource+idtransaction+".ttl");

		 
		//SERIALIZATION
		 Processing p = new Processing();
		 if (p.entity_process("./data/entities_"+idMediaResource+idtransaction+".json", "./data/subtitle_"+idMediaResource+idtransaction+".str", "./data/entities_"+idMediaResource+idtransaction+".ttl", idMediaResource.toString(), namespace, locator, subtitleFile)){
		 
		 
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
			//entitiesNerd.delete();
        System.out.println("Finishing Processing!");
    }

}
