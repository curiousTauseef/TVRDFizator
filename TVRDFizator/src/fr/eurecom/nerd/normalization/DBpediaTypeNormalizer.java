package fr.eurecom.nerd.normalization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.FileUtils;

import fr.eurecom.nerd.client.LookUp;
import fr.eurecom.nerd.client.type.ExtractorType;

public class DBpediaTypeNormalizer {
	
	Hashtable <String, ArrayList<String>> OpencalaisMappings = new Hashtable <String, ArrayList<String>> ();
	Hashtable <String, ArrayList<String>> AlchemyMappings = new Hashtable <String, ArrayList<String>>  ();
	Hashtable <String, ArrayList<String>> WikimetaMappings = new Hashtable <String, ArrayList<String>>  ();
	Hashtable <String, ArrayList<String>> SaploMappings = new Hashtable <String, ArrayList<String>>  ();
	Hashtable <String, ArrayList<String>> SemitagsMappings = new Hashtable <String, ArrayList<String>>  ();
	Hashtable <String, ArrayList<String>> THDMappings = new Hashtable <String, ArrayList<String>>  ();
	Hashtable <String, ArrayList<String>> YahooMappings = new Hashtable <String, ArrayList<String>>  ();
	Hashtable <String, ArrayList<String>> ZemantaMappings = new Hashtable <String, ArrayList<String>>  ();

	
	
	List<String> dbpediaClasses = new ArrayList<String>();
	
	String DBPEDIA_PREFIX = "http://dbpedia.org/ontology/";
	public DBpediaTypeNormalizer(){
		
		
		//Loading DBPedia Classes
		try {
			dbpediaClasses = FileUtils.readLines(new File("./mapping/DBPedia/classes"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Loading OPENCALAIS
		
		loadMappings("./mapping/DBPedia/opencalais", OpencalaisMappings);
		
		
		//CHARGE ALCHEMY
		
		loadMappings("./mapping/DBPedia/alchemy", AlchemyMappings);
	
		
		//NOTHING WITH SPOTLIGHT
		
		//NOTHING WITH DATATXT
		
		
		// WIKIMETA http://www.nlgbase.org/document/Conventions_EN_ESTER2_v01.pdf
		loadMappings("./mapping/DBPedia/wikimeta", WikimetaMappings);

		
		//SMALL SAPLO FILE
		loadMappings("./mapping/DBPedia/saplo", SaploMappings);
		
		
		//NOTHING WITH LUPEDIA
		
		// THD: get rid of resources and only keep classes
		loadMappings("./mapping/DBPedia/thd", THDMappings);
		//DONE
		
		
		//SEMITAG s	Location, Person, Organization, Misc
		loadMappings("./mapping/DBPedia/semitags", SemitagsMappings);

		
		//http://developer.zemanta.com/docs/entity_type/
		loadMappings("./mapping/DBPedia/zemanta", ZemantaMappings);

		
		// Yahoo
		loadMappings("./mapping/DBPedia/yahoo", YahooMappings);

		
		
		
		//Textrazor (DBPedia + Freebase)
		
		//CHARGE DBPEDIA FOR RECHECKING

		
		
		
	}

	private void loadMappings(String filename, Hashtable<String, ArrayList<String>> tableMappings) {



		List<String> StringsMappings = null;
		try {
			StringsMappings = FileUtils.readLines(new File(filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (String line:StringsMappings){
			String [] mappings = line.split("#");
			String originalClass= mappings[0];
			String [] mappedClassesString = mappings[1].split(",");

			ArrayList<String> mappedClasses = new ArrayList <String>();
			
			for (int i = 0; i < mappedClassesString.length ; i++){
				mappedClasses.add(mappedClassesString[i]);
			}
			
			tableMappings.put(originalClass, mappedClasses);
		}
		
	}

	public List<String> normalizeTypes(String rawTypes, String extractor){
		
		
		List<String> types = null;
		
		
		 //extractor is valid? throws the TypeException
        ExtractorType etype = null;
        try {
            etype = LookUp.mapExtractorStringType(extractor);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        

		switch (etype) {
        case ALCHEMYAPI:
        	types = breakTypesAlchemy(rawTypes);
			break;  
        case DBSPOTLIGHT:
        	types = breakTypesSpotlight(rawTypes);
            break;         
        case LUPEDIA:
        	types = breakTypesLupedia(rawTypes);
            break;
        case OPENCALAIS:
        	types = breakTypesOpencalais(rawTypes);
            break;
        case SAPLO:
        	types = breakTypesSaplo(rawTypes);
            break;            
        case SEMITAGS:
        	types = breakTypesSemitags(rawTypes);
            break;            
        case TEXTRAZOR:
        	types = breakTypesTextrazor(rawTypes);
            break;
        case THD:
        	types = breakTypesThd(rawTypes);
            break;
        case WIKIMETA:
        	types = breakTypesWikimeta(rawTypes);
            break;    
        case YAHOO:
        	types = breakTypesYahoo(rawTypes);
            break;
        case ZEMANTA:
        	types = breakTypesZemanta(rawTypes);
            break;
        case DANDELIONAPI:
        	types = breakTypesDandelionAPI(rawTypes);
            break;                
        default:
            break;
        }
		
		
		
		
		
		return types;
	}

	private List<String> breakTypesDandelionAPI(String rawTypes) {
		List<String> types = new ArrayList<String> ();
		if (rawTypes == null) return types;

		if (rawTypes.indexOf(" ") >= 0){
			rawTypes = rawTypes.substring(0, rawTypes.indexOf(" ")); 
		}
		
		if (rawTypes.equalsIgnoreCase("null")){
			return types;
		}

		String[] rawTypesbroken = rawTypes.split(",");
		for (int i = 0; i < rawTypesbroken.length; i++){

			types.add(DBPEDIA_PREFIX+rawTypes);
		}
		
		return types;
	}

	private  List<String> breakTypesSpotlight(String rawTypes) {
		
		List<String> types = new ArrayList<String> ();
		if (rawTypes == null) return types;

		if (rawTypes.indexOf(" ") >= 0){
			rawTypes = rawTypes.substring(0, rawTypes.indexOf(" ")); 
		}
		
		if (rawTypes.equalsIgnoreCase("null")){
			return types;
		}

		String[] rawTypesbroken = rawTypes.split(",");
		for (int i = 0; i < rawTypesbroken.length; i++){

			types.add(DBPEDIA_PREFIX+rawTypes);
		}
		
		return types;
	}

	private  List<String> breakTypesLupedia(String rawTypes) {
		List<String> types = new ArrayList<String> ();
		if (rawTypes == null) return types;
		
		
		if (rawTypes.indexOf(" ") >= 0){
			rawTypes = rawTypes.substring(0, rawTypes.indexOf(" ")); 
		}
		
		if (rawTypes.equalsIgnoreCase("null")){
			return types;
		}

		String[] rawTypesbroken = rawTypes.split(",");
		
		for (int i = 0; i < rawTypesbroken.length; i++){
			types.add(DBPEDIA_PREFIX+rawTypes);
		}
		
		return types;
	}

	private  List<String> breakTypesOpencalais(String rawTypes) {
		List<String> types = new ArrayList<String> ();
		if (rawTypes == null) return types;
		
		
		if (rawTypes.indexOf(" ") >= 0){
			rawTypes = rawTypes.substring(0, rawTypes.indexOf(" ")); 
		}
		
		if (rawTypes.equalsIgnoreCase("null")){
			return types;
		}

		String[] rawTypesbroken = rawTypes.split(",");
		
		for (int i = 0; i < rawTypesbroken.length; i++){
			
			ArrayList<String> mappedTypes = OpencalaisMappings.get(rawTypesbroken[i]);
			
			if (mappedTypes != null){
				for (String type : mappedTypes){
					types.add(DBPEDIA_PREFIX+ type);
				}
			}
		}
		
		return types;
	}

	//Persons
	//Organizations
	//Locations
	private  List<String> breakTypesSaplo(String rawTypes) {
		List<String> types = new ArrayList<String> ();
		if (rawTypes == null) return types;
		
		
		if (rawTypes.indexOf(" ") >= 0){
			rawTypes = rawTypes.substring(0, rawTypes.indexOf(" ")); 
		}
		
		if (rawTypes.equalsIgnoreCase("null")){
			return types;
		}

		String[] rawTypesbroken = rawTypes.split(",");
		
		for (int i = 0; i < rawTypesbroken.length; i++){
			
			ArrayList<String> mappedTypes = SaploMappings.get(rawTypesbroken[i]);	
			if (mappedTypes != null){
				for (String type : mappedTypes){
					types.add(DBPEDIA_PREFIX+ type);
				}
			}
		}
		
		return types;
	
	}

	private  List<String> breakTypesSemitags(String rawTypes) {
		List<String> types = new ArrayList<String> ();
		if (rawTypes == null) return types;
		
		
		if (rawTypes.indexOf(" ") >= 0){
			rawTypes = rawTypes.substring(0, rawTypes.indexOf(" ")); 
		}
		
		if (rawTypes.equalsIgnoreCase("null")){
			return types;
		}

		String[] rawTypesbroken = rawTypes.split(",");
		
		for (int i = 0; i < rawTypesbroken.length; i++){
			
			ArrayList<String> mappedTypes = SemitagsMappings.get(rawTypesbroken[i]);	
			if (mappedTypes != null){
				for (String type : mappedTypes){
					types.add(DBPEDIA_PREFIX+ type);
				}
			}
		}
		
		return types;
	}

	private  List<String> breakTypesTextrazor(String rawTypes) {
		List<String> types = new ArrayList<String> ();
		if (rawTypes == null) return types;
		
		
		if (rawTypes.indexOf(" ") >= 0){
			rawTypes = rawTypes.substring(0, rawTypes.indexOf(" ")); 
		}
		
		if (rawTypes.equalsIgnoreCase("null")){
			return types;
		}
		

		String[] rawKnowledgeBases = rawTypes.split(";");
		
		String DBPediaTypes = null;
		String FreebaseTypes = null;

		if (rawKnowledgeBases[0].startsWith("DBpedia:")) {
			DBPediaTypes = rawKnowledgeBases[0];
			if (rawKnowledgeBases.length > 1) FreebaseTypes = rawKnowledgeBases[1];
		}
		else{
			FreebaseTypes = rawKnowledgeBases[0];
			if (rawKnowledgeBases.length > 1) DBPediaTypes = rawKnowledgeBases[1];
		}
		
		//Cutting dbpedia types
		if (DBPediaTypes != null){
			DBPediaTypes = DBPediaTypes.substring(8); //Removing DBpedia:
			String[] rawtypesDB = DBPediaTypes.split(",");
			
			for (int i = 0; i < rawtypesDB.length; i++){
				String className = rawtypesDB[i];
				
				if (dbpediaClasses.contains(className)){
					types.add(DBPEDIA_PREFIX+ className);
				}
			}
		}
		
		
		//Cutting freebase types
		if (FreebaseTypes != null){
			System.out.println("FreebaseTypes ->> " + FreebaseTypes);
			//FreebaseTypes = FreebaseTypes.substring(9); //Removing DBpedia:
			String[] rawtypesFR = FreebaseTypes.split(",");
			
			
			for (int i = 0; i < rawtypesFR.length; i++){
				String[] domains = rawtypesFR[i].split("/");


				String className = domains[domains.length-1];

				ArrayList <String> subClassNames = new ArrayList <String> ();
				subClassNames.add(putStringTogheter(className));

				String[] subClassNamesSmall = className.split("_");
				subClassNames.add(subClassNamesSmall[subClassNamesSmall.length - 1]);
				//for(int j = 0; j < subClassNamesSmall.length; j++){
					//subClassNames.add(uppercaseFirstLetter(subClassNamesSmall[j]));
				//}	


				for(String possibleClass: subClassNames) {

					if (possibleClass.equals("Organization")) possibleClass = "Organisation"; //HACK

					if (dbpediaClasses.contains(possibleClass) && !types.contains(DBPEDIA_PREFIX+ possibleClass)){
						types.add(DBPEDIA_PREFIX+ possibleClass);
					}

				}

			}
		}
		

		
		return types;
	}

	private String putStringTogheter(String type) {
		
		String typeFinal = "";
		String[] tokens = type.split("_");
		for (int i = 0; i < tokens.length; i++){
			String uppercase = uppercaseFirstLetter(tokens[i]);
			typeFinal = typeFinal + uppercase;
		}
		return typeFinal;
	}

	private String uppercaseFirstLetter(String string) {
		

		string = string.toLowerCase();
		if (string.length() > 0){
			String firstletter = string.substring(0, 1).toUpperCase();
			String restletters = "";
			if (string.length() > 1) restletters = string.substring(1, string.length());
			string = firstletter + restletters;
			
			return string;
		}
		
		else return string;
	}

	private  List<String> breakTypesThd(String rawTypes) {
		List<String> types = new ArrayList<String> ();
		if (rawTypes == null) return types;
		
		
		if (rawTypes.indexOf(" ") >= 0){
			rawTypes = rawTypes.substring(0, rawTypes.indexOf(" ")); 
		}
		
		if (rawTypes.equalsIgnoreCase("null")){
			return types;
		}
		

		String[] rawTypesbroken = rawTypes.split(",");
		
		for (int i = 0; i < rawTypesbroken.length; i++){
			String rawType = rawTypesbroken[i];
			String[] brokenURL = rawType.split("/");

			String className = brokenURL[brokenURL.length-1];
			
			//Applies auxiliar mappings
			ArrayList<String> mappedTypes = THDMappings.get(rawTypesbroken[i]);	
			if (mappedTypes != null){
				for (String type : mappedTypes){
					types.add(DBPEDIA_PREFIX+ type);
				}
			}
			else{
				if (dbpediaClasses.contains(className)){
					types.add(DBPEDIA_PREFIX+ className);
				}
			}

		}
		
		return types;
	}

	private  List<String> breakTypesWikimeta(String rawTypes) {

		List<String> types = new ArrayList<String> ();
		if (rawTypes == null) return types;
		
		
		if (rawTypes.indexOf(" ") >= 0){
			rawTypes = rawTypes.substring(0, rawTypes.indexOf(" ")); 
		}
		
		if (rawTypes.equalsIgnoreCase("null")){
			return types;
		}

		
		//BUG IN WIKIMETA
		rawTypes = rawTypes.replaceAll(",", ".");
		
			
		ArrayList<String> mappedTypes = WikimetaMappings.get(rawTypes.toLowerCase());	
		if (mappedTypes != null){
			for (String type : mappedTypes){
				types.add(DBPEDIA_PREFIX+ type);
			}
		}
		
		return types;
	}

	private  List<String> breakTypesYahoo(String rawTypes) {
		List<String> types = new ArrayList<String> ();
		if (rawTypes == null) return types;
		
		
		if (rawTypes.indexOf(" ") >= 0){
			rawTypes = rawTypes.substring(0, rawTypes.indexOf(" ")); 
		}
		
		if (rawTypes.equalsIgnoreCase("null")){
			return types;
		}

		String[] rawTypesbroken = rawTypes.split(",");
		
		for (int i = 0; i < rawTypesbroken.length; i++){
			
			ArrayList<String> mappedTypes = YahooMappings.get(rawTypesbroken[i]);	
			if (mappedTypes != null){
				for (String type : mappedTypes){
					types.add(DBPEDIA_PREFIX+ type);
				}
			}
		}
		
		return types;
	}

	private  List<String> breakTypesZemanta(String rawTypes) {
		List<String> types = new ArrayList<String> ();
		if (rawTypes == null) return types;
		
		
		if (rawTypes.indexOf(" ") >= 0){
			rawTypes = rawTypes.substring(0, rawTypes.indexOf(" ")); 
		}
		
		if (rawTypes.equalsIgnoreCase("null")){
			return types;
		}

		String[] rawTypesbroken = rawTypes.split(",");
		
		for (int i = 0; i < rawTypesbroken.length; i++){
			
			ArrayList<String> mappedTypes = ZemantaMappings.get(rawTypesbroken[i]);	
			if (mappedTypes != null){
				for (String type : mappedTypes){
					types.add(DBPEDIA_PREFIX+ type);
				}
			}
			else{
				String className = rawTypesbroken[i].toLowerCase();
				if (className.length() > 0){
				String firstletter = className.substring(0, 1).toUpperCase();
				String restletters = "";
				if (className.length() > 1) restletters = className.substring(1, className.length());
				className = firstletter + restletters;
				if (dbpediaClasses.contains(className)){
					types.add(DBPEDIA_PREFIX+ className);
				}
				}
			}
		}
		
		return types;
	
	}

	private  List<String> breakTypesAlchemy(String rawTypes) {

		List<String> types = new ArrayList<String> ();
		if (rawTypes == null) return types;
		
		
		if (rawTypes.indexOf(" ") >= 0){
			rawTypes = rawTypes.substring(0, rawTypes.indexOf(" ")); 
		}
		
		if (rawTypes.equalsIgnoreCase("null")){
			return types;
		}

		String[] rawTypesbroken = rawTypes.split(",");
		
		for (int i = 0; i < rawTypesbroken.length; i++){
			
			ArrayList<String> mappedTypes = AlchemyMappings.get(rawTypesbroken[i]);	
			if (mappedTypes != null){
				for (String type : mappedTypes){
					types.add(DBPEDIA_PREFIX+ type);
				}
			}
		}
		
		return types;
	}
}
