package fr.eurecom.nerd.normalization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;

public class DBpediaURLNormalizer {

	
	
	String DBPEDIA_ONT_PREFIX = "http://dbpedia.org/ontology/";
	String DBPEDIA_PREFIX = "http://dbpedia.org/resource/";
	String WIKIPEDIA_PREFIX = "http://en.wikipedia.org/wiki/";
	
	private String service = "http://dbpedia.org/sparql";

	public DBpediaURLNormalizer(){
		
		
		
	}


	public String normalizeUrls(String url){
		
		String dbNormalized = null; 
		
		
	
		if (url.startsWith(this.DBPEDIA_PREFIX)){//It is already a DBpedia URL
			return null;
		}
		else{
			
			//Calculate instance name
			String[] brokenURL = url.split("/");
			String instanceName = brokenURL[brokenURL.length-1];
			
			//is wikipedia?
			if (url.startsWith(this.WIKIPEDIA_PREFIX)){
				dbNormalized = this.DBPEDIA_PREFIX + instanceName; 
			}
			else{
				//Generate candidates
				List <String> candidates = generateCandidates(instanceName);
				
				//Check candidates
				for (int i = 0; i < candidates.size() && dbNormalized == null; i ++){
					String candidate = candidates.get(i);
					if (isDBpediaInstance(candidate)){
						dbNormalized = this.DBPEDIA_PREFIX + candidate;
					}
				}
			}
		}
		
		
		
		
		return dbNormalized;
	}


	private boolean isDBpediaInstance(String candidate) {
		
		String resource = 	this.DBPEDIA_PREFIX+candidate;
		
		String query = "PREFIX dbpedia: <http://dbpedia.org/resource/> select ?b where { <"+resource+ "> ?a ?b } LIMIT 1";

		if (candidate == null){
			System.out.println("ERROR, null label to desambiguate");
			//return false;
		}
		
		
		if(candidate.equals("")){
			System.out.println("ERROR, Empty label to desambiguate");
			//return false;
		}
		
		
		System.out.println("Resource: " + resource);
		System.out.println("Query: " + query);
	 
		try {
			QueryExecution qe = QueryExecutionFactory.sparqlService(service, query);
			
			ResultSet results = qe.execSelect(); 
			
			if (results.hasNext()){ //At least one result
				return true;
				//System.out.println("The resource " + resource + "has been desambiguated in " + desambiguationURL);
			}
		
		}
		catch (QueryParseException e){
			System.out.println("(Exception) The resource "+ resource + " has been not desambiguated.");
		}
		
		return false;
	}


	private List<String> generateCandidates(String instanceName) {

		//IN ORDER OF IMPORTANCE
		ArrayList <String> candidateList = new ArrayList<String> ();
		

		//the token itself
		candidateList.add(instanceName);
		
		//cammel
		candidateList.add(uppercaseFirstLetter(instanceName));

		
		candidateList.add(putStringTogheter(instanceName));

		if (instanceName.indexOf("query=") >= 0){
			String candidate = instanceName.substring(instanceName.indexOf("query=") + 6);

			if (candidate.indexOf("&") >=0){
				 candidate = candidate.substring(0, candidate.indexOf("&") );
			}
			candidateList.add(candidate);
			candidateList.add(candidate.replace(" ", "_"));

		}

		if (instanceName.indexOf("q=") >= 0){
			String candidate = instanceName.substring(instanceName.indexOf("q=") + 2);

			if (candidate.indexOf("&") >=0){
				 candidate = candidate.substring(0, candidate.indexOf("&"));
			}
			candidateList.add(candidate);
			candidateList.add(candidate.replace(" ", "_"));

		}
		

		if (instanceName.indexOf("?") >= 0){
			String candidate = instanceName.substring(0, instanceName.indexOf("?"));

			candidateList.add(candidate);
			candidateList.add(candidate.replace(" ", "_"));
		}
		
		return candidateList;
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
	
	
	public boolean DPPediaIsUp() {


		String resource = 	"<http://dbpedia.org/resource/DBPedia>";

		String query = "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> select ?o where { "+resource+" dbpedia-owl:wikiPageRedirects ?o .}";
		try {
			QueryExecution qe = QueryExecutionFactory.sparqlService(service, query);
			qe.execSelect(); 
		}
		catch (QueryParseException e){
			return false;
		}
        catch (RuntimeException e) {
            return false;
        } 

		return true;
	}

}
