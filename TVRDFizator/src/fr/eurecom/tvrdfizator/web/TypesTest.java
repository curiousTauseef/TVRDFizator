package fr.eurecom.tvrdfizator.web;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.eurecom.nerd.client.NERD;
import fr.eurecom.nerd.client.type.DocumentType;
import fr.eurecom.nerd.client.type.ExtractorType;
import fr.eurecom.nerd.client.type.GranularityType;
import fr.eurecom.nerd.normalization.DBpediaTypeNormalizer;
import fr.eurecom.tvrdfizator.core.datastructures.NERDEntity;

public class TypesTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
	    //NERD nerd = new NERD(apiKey);
		
		String text = "";
		try {
			text = FileUtils.readFileToString(new File("typetext.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //NERD nerd = new NERD("1g0stre00us2jjgtb2g80190rv7bpm4v");
		NERD nerd = new NERD("loq6asma69tgfq2aijbubh2t5klm7pk0");


		String json = nerd.annotateJSON(ExtractorType.NERDML,
	                                    DocumentType.PLAINTEXT,
	                                    text, GranularityType.OEN);
		
		System.out.println(json);
		
		Gson gson = new Gson();
		Type listtype = new TypeToken<List<NERDEntity>>() {}.getType();
		List<NERDEntity> entities = gson.fromJson(json,listtype);
		System.out.println("\t#entities:" + entities.size());
		
		
		for (NERDEntity entity: entities){
			System.out.println(entity.getExtractor() + ",    "+ entity.getNerdType() +  ",    "+entity.getExtractorType() + ",   "+entity.getLabel() + ",    " + entity.getUri());
			DBpediaTypeNormalizer tn = new DBpediaTypeNormalizer();
			List<String> normalizedTypes = tn.normalizeTypes(entity.getExtractorType(), entity.getExtractor());
			if (normalizedTypes!=null){
				for (String type : normalizedTypes){
					System.out.println("    --> "+type);
				}
			}

		
		}
		
		
	}

}
