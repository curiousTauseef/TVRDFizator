package fr.eurecom.tvrdfizator.core;


import java.util.List;

import fr.eurecom.tvrdfizator.core.datastructures.NERDEntity;
import fr.eurecom.tvrdfizator.core.datastructures.Subtitle;
import fr.eurecom.tvrdfizator.core.datastructures.VideoMetaData;
import fr.eurecom.tvrdfizator.core.readers.EntityReader;
import fr.eurecom.tvrdfizator.core.readers.ExmaraldaReader;
import fr.eurecom.tvrdfizator.core.readers.LegacyReader;
import fr.eurecom.tvrdfizator.core.readers.SubtitleReader;
import fr.eurecom.tvrdfizator.core.writers.RDFWriterAnalisys;
import fr.eurecom.tvrdfizator.core.writers.RDFWriterLegacy;
import fr.eurecom.tvrdfizator.core.writers.RDFWriterSubtitleEntity;


public class Processing {

	
	
	public boolean legacy_process(String in_legacy_file_path, String out_legacy_file_path, String idMediaResource, String namespace, String locator){
		

		//Generate the data structures
		VideoMetaData mdata = new VideoMetaData();	
		
	
		//Read XML files
		
		
		
		// - Legacy Data
		if (!in_legacy_file_path.equals(""))
			try {
				LegacyReader lr = new LegacyReader (in_legacy_file_path, mdata);	
				lr.read();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("ERROR when generating the RDF model.");
				return false;
			}
		else System.out.println("No legacy information available. Continuing processing XML. ");
		
		
		//Write the RDF Data for Legacy information
		try {
			RDFWriterLegacy wt = new RDFWriterLegacy (out_legacy_file_path, "", mdata, idMediaResource,  namespace, locator);	
			wt.create_legacy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERROR when generating the RDF model.");

			return false;
		}
		
		
		System.out.println("RDF model generated sucessfully.");
		return true;
		
		
	}
	
	
	public boolean analisys_process(String in_exmaralda_file_path, String out_exmaralda_file_path, String media_item_id, String namespace, String locator){
		


		//Generate the data structures
		VideoMetaData mdata = new VideoMetaData();	
		
	
		//Read XML files
		
		

		// - EXMARaLDA file.
		if (!in_exmaralda_file_path.equals(""))
			try {
				ExmaraldaReader er = new ExmaraldaReader (in_exmaralda_file_path, mdata);	
				er.read();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ERROR when generating the RDF model.");
				return false;
			}
		else System.out.println("No analysis information available. Continuing processing XML. ");
		
		

		//Write the RDF Data for Legacy information
		try {
			RDFWriterAnalisys wt = new RDFWriterAnalisys (out_exmaralda_file_path, mdata, media_item_id, namespace, locator);	
			wt.create_exmeralda();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR when generating the RDF model.");
			return false;
		}

		
		System.out.println("RDF model generated sucessfully.");
		return true;
		
		
	}
	
	
	public boolean entity_process(String in_entity_file_path, String in_subtitle_file_path, String out_entity_file_path, String media_resource_id, String namespace, String locator, String subtitleFile){
		
		List <Subtitle> subtitles = null;
		// - Entities file.
		if (!in_subtitle_file_path.equals(""))
			try {
				SubtitleReader subtitler = new SubtitleReader (in_subtitle_file_path);	
				subtitles = subtitler.read();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ERROR when generating the RDF model.");
				return false;
			}
		else System.out.println("No subtitle information available. Continuing processing XML. ");
		System.out.println("The number of subtitles is " + subtitles.size());

		
		List <NERDEntity> entities = null;

		// - Entities file.
		if (!in_entity_file_path.equals(""))
			try {
				EntityReader entityr = new EntityReader (in_entity_file_path);	
				entities = entityr.read();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ERROR when generating the RDF model.");
				return false;
			}
		else System.out.println("No entity information available. Continuing processing XML. ");
		

		//Write the RDF Data from subtitles and entities
		try {
			RDFWriterSubtitleEntity wt = new RDFWriterSubtitleEntity (out_entity_file_path, subtitles, entities, media_resource_id, namespace, locator, subtitleFile);	
			wt.create_subtitles_entities();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR when generating the RDF model.");
			return false;
		}

		
		System.out.println("RDF model generated sucessfully.");
		return true;
		
		
	}
	
	
	
	
	
}
