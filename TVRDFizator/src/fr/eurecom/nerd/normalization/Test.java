package fr.eurecom.nerd.normalization;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String DBPEDIA_PREFIX = "http://dbpedia.org/resource/";
		String service = "http://dbpedia.org/sparql/";

		
		String candidate = "Obama";
		String resource = 	DBPEDIA_PREFIX+candidate;
		
		String query = "select ?b where { <"+resource+ "> ?a ?b } LIMIT 1";

		QueryExecution qe = QueryExecutionFactory.sparqlService(service, query);
		
		ResultSet results = qe.execSelect(); 
		
		
		if (results.hasNext()){ //At least one result
			System.out.println("FINE NOW");			//System.out.println("The resource " + resource + "has been desambiguated in " + desambiguationURL);
		}
	}

}
