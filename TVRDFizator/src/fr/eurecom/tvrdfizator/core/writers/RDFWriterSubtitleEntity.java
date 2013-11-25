package fr.eurecom.tvrdfizator.core.writers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.AbstractMap;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.Vector;


import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import fr.eurecom.tvrdfizator.core.datastructures.ItemLayer;
import fr.eurecom.tvrdfizator.core.datastructures.Layer;
import fr.eurecom.tvrdfizator.core.datastructures.NERDEntity;
import fr.eurecom.tvrdfizator.core.datastructures.Pair;
import fr.eurecom.tvrdfizator.core.datastructures.Subtitle;
import fr.eurecom.tvrdfizator.core.datastructures.VideoMetaData;

public class RDFWriterSubtitleEntity {
	
	
	private String file_entity_json = null;

	private List<NERDEntity> entities = null;
	private List<Subtitle> subtitles = null;
	
	private Hashtable <Float, Entry<Individual, Individual>> mediaFragmentsSub = null;

	private OntModel modelBBC = null;
	private OntModel model_linkedtv = null;

	
	private OntModel model_entity = null;
	private OntModel model_agents = null;


	private OntModel modelMA = null;
	private OntModel modelOA = null;
	private OntModel modelFOAF = null;
	private OntModel modelPROV = null;
	private OntModel modelLSCOM = null;
	private OntModel modelNSA = null;
	private OntModel modelDC = null;
	private OntModel modelNerd = null;
	private OntModel modelExmeralda = null;
	private OntModel modelString = null;


	

	

		
	private String BBC_ontology_URL = "http://purl.org/ontology/po/";
	private String Dublin_Core_URL = "http://purl.org/dc/elements/1.1/";
	private String Media_Resources_URL = "http://www.w3.org/ns/ma-ont#";
	private String Open_Annotation_URL = "http://www.w3.org/ns/oa#";
	private String Event_URL = "http://purl.org/NET/c4dm/event.owl/";
	private String Time_URL = "http://purl.org/NET/c4dm/timeline.owl/";
	private String LSCOM_URL = "http://vocab.linkeddata.es/lscom/";
	private String FOAF_URL = "http://xmlns.com/foaf/0.1/";
	private String PROV_URL = "http://www.w3.org/ns/prov#";
	private String LINKEDTV_URL = "http://data.linkedtv.eu/";
	private String LINKEDTV_URL_ONT = "http://data.linkedtv.eu/ontologies/core#";
	private String DBPEDIA_URL_ONT = "http://dbpedia.org/ontology/";
	private String NINSUNA_URL_ONT = "http://multimedialab.elis.ugent.be/organon/ontologies/ninsuna#";
	private String NERD_URL_ONT = "http://nerd.eurecom.fr/ontology#";
	private String STRING_URL_ONT = "http://nlp2rdf.lod2.eu/schema/string/";

	
		

	private String mediaResource = "e2899e7f-67c1-4a08-9146-5a205f6de457";
	//private String fileSRT = "rbbaktuell_120809.srt";
	private double threshold = 0.1; //in miliseconds.

	
	Individual documentI = null;
	
	public RDFWriterSubtitleEntity (String f_entity, List<Subtitle> subtitles, List<NERDEntity> entities, String media_resource_id, String namespace, String locator){
	
		file_entity_json = f_entity;
		this.entities = entities;
		this.subtitles = subtitles;
		this.mediaResource = media_resource_id;
		
		if (!namespace.equals("")){
			LINKEDTV_URL = namespace;
		}
		
	}
	
	public void create_subtitles_entities() throws FileNotFoundException{

			createModel();
			
			generateSubtitles();
			generateEntities();
			
			writeAll();

			

	}
	


	
	
	private void generateSubtitles() {
		

		//Recreate upper MediaResource
		OntClass mediaResourceOWL = modelMA.createClass( Media_Resources_URL + "MediaResource" );
		Individual mediaResouceI = modelExmeralda.createIndividual(LINKEDTV_URL + "media/" + mediaResource, mediaResourceOWL );
		
		//Create the Textualresource
		//OntClass documentOWL = modelString.createClass( STRING_URL_ONT + "Document" );
		//documentI = model_entity.createIndividual(LINKEDTV_URL + "text/" + UUID.randomUUID(), documentOWL );
		//OntProperty sourceURLProperty = modelString.createOntProperty(STRING_URL_ONT+"sourceURL");
		//documentI.addProperty(sourceURLProperty, fileSRT);
		
		
		mediaFragmentsSub = new Hashtable <Float, Entry<Individual, Individual>>();
		
		
		
		for (int i = 0; i < subtitles.size(); i++){
			//MEDIA FRAGMENT
			Subtitle sub = subtitles.get(i);
			float start = sub.getStartTmp();
			float end = sub.getEndTmp();

			OntClass mediaFragmentOWL = modelMA.createClass( Media_Resources_URL + "MediaFragment" );
			Individual mediaFragmentI = model_entity.createIndividual(LINKEDTV_URL + "media/" + mediaResource + "#t=" +start +","+end, mediaFragmentOWL );
			
			//Link with the mediaResource.
			OntProperty isfragmentofProperty = modelMA.createOntProperty(Media_Resources_URL+"isFragmentOf");
			mediaFragmentI.addProperty(isfragmentofProperty, mediaResouceI);
			
			//Ninsuna Ontology.
			mediaFragmentI.addProperty(RDF.type, modelNSA.createClass(NINSUNA_URL_ONT + "TemporalFragment"));

			Literal temporalStart = model_entity.createTypedLiteral(start);	
			OntProperty temporalStartProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalStart");
			mediaFragmentI.addProperty(temporalStartProperty, temporalStart);
		
			
			
			Literal temporalEnd = model_entity.createTypedLiteral(end);	
			OntProperty temporalEndProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalEnd");
			mediaFragmentI.addProperty(temporalEndProperty, temporalEnd);
			
			
			Literal duration = model_entity.createTypedLiteral(end-start);	
			OntProperty temporalDurationProperty = modelMA.createOntProperty(Media_Resources_URL+"duration");
			mediaFragmentI.addProperty(temporalDurationProperty, duration);
			
			
			
			OntProperty temporalUnitProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalUnit");
			mediaFragmentI.addProperty(temporalUnitProperty, "npt");
			
			
/*			//String class that represents the subtitle 
			OntClass offsetStringOWL = modelString.createClass( STRING_URL_ONT + "OffsetBasedString" );
			String offsetStringURL = documentI.getURI() + "#offset_"+entity.getStartChar() + "_"+entity.getEndChar()+"_"+entity.getEntity();
			offsetStringURL = offsetStringURL.replaceAll(" ", "_");
			Individual offsetStringI = model_entity.createIndividual(offsetStringURL, offsetStringOWL );
			
			//beginIndex
			OntProperty beginIndexProperty = modelString.createObjectProperty(STRING_URL_ONT + "beginIndex");
			Literal beginIndexLiteral = model_entity.createTypedLiteral(entity.getStartChar());
			offsetStringI.addProperty(beginIndexProperty, beginIndexLiteral);
			
			//endIndex
			OntProperty endIndexProperty = modelString.createObjectProperty(STRING_URL_ONT + "endIndex");
			Literal endIndexLiteral = model_entity.createTypedLiteral(entity.getEndChar());
			offsetStringI.addProperty(endIndexProperty, endIndexLiteral);
			
			//Relate with the original document
			OntProperty subStringProperty = modelString.createObjectProperty(STRING_URL_ONT + "subString");
			offsetStringI.addProperty(subStringProperty, documentI);*/
			
			
			//This string is a subclass of the document
			
			OntClass stringOWL = modelString.createClass( STRING_URL_ONT + "String" );
			Individual stringI = model_entity.createIndividual(LINKEDTV_URL + "text/" + UUID.randomUUID(), stringOWL );
			
			Literal subtitleText = model_entity.createTypedLiteral(sub.getText().replaceAll("\n", " "));	
			OntProperty labelProperty = modelString.createOntProperty(STRING_URL_ONT+"label");
			stringI.addProperty(labelProperty, subtitleText);
			stringI.addProperty(RDF.type, modelPROV.createClass(PROV_URL + "Entity"));	

			
			OntProperty titleProperty = model_linkedtv.createOntProperty(LINKEDTV_URL_ONT+"hasSubtitle");
			mediaFragmentI.addProperty(titleProperty, stringI);
			
			Entry <Individual, Individual> p = new AbstractMap.SimpleEntry<Individual, Individual> (mediaFragmentI,stringI);
			mediaFragmentsSub.put(sub.getStartTmp(), p);
			
		}
		
	}

	private void writeAll() throws FileNotFoundException {
		

			FileOutputStream out_File = new FileOutputStream(new File(file_entity_json) );
			
			model_entity.write(out_File, "TURTLE");


	}
	
	


	public void createModel() throws FileNotFoundException{
		 if (model_entity == null){
			 model_entity = ModelFactory.createOntologyModel();
			 model_agents = ModelFactory.createOntologyModel();
			 model_linkedtv = ModelFactory.createOntologyModel();

			 modelDC= ModelFactory.createOntologyModel();
			 modelBBC = ModelFactory.createOntologyModel();
			 modelMA = ModelFactory.createOntologyModel();
			 modelOA = ModelFactory.createOntologyModel();
			 modelFOAF= ModelFactory.createOntologyModel();
			 modelPROV= ModelFactory.createOntologyModel();
			 modelLSCOM= ModelFactory.createOntologyModel();
			 modelNSA= ModelFactory.createOntologyModel();
			 modelNerd= ModelFactory.createOntologyModel();
			 modelExmeralda= ModelFactory.createOntologyModel();
			 modelString= ModelFactory.createOntologyModel();

		 
			 model_entity.setNsPrefix( "po", BBC_ontology_URL );
			 model_entity.setNsPrefix( "ma", Media_Resources_URL );
			 model_entity.setNsPrefix( "oa", Open_Annotation_URL );
			 model_entity.setNsPrefix( "dc", Dublin_Core_URL );
			 model_entity.setNsPrefix( "event", Event_URL );
			 model_entity.setNsPrefix( "timeline", Time_URL );
			 model_entity.setNsPrefix( "foaf", FOAF_URL );
			 model_entity.setNsPrefix( "prov", PROV_URL );
			 model_entity.setNsPrefix( "lscom", LSCOM_URL );
			 model_entity.setNsPrefix( "linkedtv", LINKEDTV_URL_ONT );
			 model_entity.setNsPrefix( "dbpedia-owl", DBPEDIA_URL_ONT );
			 model_entity.setNsPrefix( "nsa", NINSUNA_URL_ONT );
			 model_entity.setNsPrefix( "nerd", NERD_URL_ONT );
			 model_entity.setNsPrefix( "str", STRING_URL_ONT );

			 
				 
				 


     	 FileInputStream bbcOntology_File = new FileInputStream(new File("ontologies/2009-09-07.rdf"));
     	 modelBBC.read( bbcOntology_File, "", "RDF/XML" );
     	      	
     	 FileInputStream Media_Resources_File = new FileInputStream(new File("ontologies/ma-ont.rdf"));
     	 modelMA.read( Media_Resources_File, "", "RDF/XML" );
     	 
     	 
     	 FileInputStream FOAF_File = new FileInputStream(new File("ontologies/FOAF.rdf"));
     	 modelFOAF.read( FOAF_File, "", "RDF/XML" );
     	 
     	 FileInputStream LSCOM_File = new FileInputStream(new File("ontologies/LSCOM.owl"));
     	 modelLSCOM.read( LSCOM_File, "", "RDF/XML" );
     	
		}
	}

	private void generateEntities() {
		//for (int i = 0; i<entities.size(); i++){


		

		//Create the organization.
		OntClass organizationOWL = modelPROV.getOntClass( PROV_URL + "Organization" );
		Individual organizationI = model_linkedtv.createIndividual(LINKEDTV_URL + "organization/"+"NERD", organizationOWL );
		organizationI.addProperty(RDF.type, modelPROV.createClass(PROV_URL + "Agent"));
		

		for (int i = 0; i<entities.size(); i++){

			NERDEntity entity = entities.get(i);



			//Align to the right Media Fragment
			float start = (entity.getStartNPT());
			float end = (entity.getEndNPT());

			Entry<Individual, Individual> mediasubtitle = getSubtitle(start);

			
			if (mediasubtitle != null){

				Individual mediaFragmentI = mediasubtitle.getKey();

				System.out.println("Entity Aligned with a Media Fragment. Smooooth. ");
				System.out.println(entity.getLabel() + " --> " + start + " --> " + mediaFragmentI.getURI());
				//NERD
				OntClass nerdClassOWL = modelMA.createClass( LINKEDTV_URL_ONT + "Entity" );
				Individual nerdI = model_entity.createIndividual(LINKEDTV_URL + "entity/" + UUID.randomUUID(), nerdClassOWL );

				//identifier (DC)
				OntProperty identifierProperty = modelDC.createOntProperty(Dublin_Core_URL+"identifier");
				nerdI.addProperty(identifierProperty, Integer.toString(entity.getIdEntity()));

				//Label
				System.out.println(entity.getLabel());
				nerdI.addProperty(RDFS.label, entity.getLabel());	
				
				if (entity.getUri() != null){
					if (!entity.getUri().equals("") && !entity.getUri().equals("null") && !entity.getUri().equals("NORDF")) {
						//Create Resource
						String desambiguationuri = entity.getUri().replaceAll(" ", "%20");
						Individual resource = modelExmeralda.createIndividual(desambiguationuri, RDFS.Resource );
						nerdI.addProperty(OWL.sameAs, resource);			
					}
				}


				//Type nerd
				nerdI.addProperty(RDF.type, modelNerd.createClass(entity.getNerdType()));

				//Type plaintext
				if (entity.getExtractorType() != null){
					OntProperty typeProperty = modelDC.createOntProperty(Dublin_Core_URL+"type");
					nerdI.addProperty(typeProperty, entity.getExtractorType());
				}

				//Source (extractor)
				OntProperty sourceProperty = modelDC.createOntProperty(Dublin_Core_URL+"source");
				nerdI.addProperty(sourceProperty, entity.getExtractor());

				//Confidence
				OntProperty confidenceProperty = model_linkedtv.createObjectProperty(LINKEDTV_URL_ONT + "hasConfidence");
				Literal confidenceLiteral = model_entity.createTypedLiteral(entity.getConfidence());
				nerdI.addProperty(confidenceProperty, confidenceLiteral);

				//Relevance
				OntProperty relevanceProperty = model_linkedtv.createObjectProperty(LINKEDTV_URL_ONT + "hasRelevance");
				Literal relevanceLiteral = model_entity.createTypedLiteral(entity.getRelevance());
				nerdI.addProperty(relevanceProperty, relevanceLiteral);


				
				//OntClass documentOWL = modelString.createClass( STRING_URL_ONT + "Document" );
				//documentI = model_entity.createIndividual(LINKEDTV_URL + "text/" + UUID.randomUUID(), documentOWL );
				//OntProperty sourceURLProperty = modelString.createOntProperty(STRING_URL_ONT+"sourceURL");
				//documentI.addProperty(sourceURLProperty, fileSRT);
				
				

				//TEXTUAL RESOURCE (String Ontology)				
				OntClass offsetStringOWL = modelString.createClass( STRING_URL_ONT + "OffsetBasedString" );
				

				Individual stringI = mediasubtitle.getValue();

				
				String encodedLabel = entity.getLabel();
				encodedLabel =  (encodedLabel.length() < 20) ? encodedLabel : encodedLabel.substring(0, 20);//Trim label
				encodedLabel = encodedLabel.replaceAll("\n", " ");
				try {
					encodedLabel = URLEncoder.encode(encodedLabel , "UTF-8").replaceAll("\\+","%20");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				
				String offsetStringURL = stringI.getURI() + "#offset_"+entity.getStartChar() + "_"+entity.getEndChar()+"_"+encodedLabel;
				offsetStringURL = offsetStringURL.replaceAll("\n", " ");
				
				
				Individual offsetStringI = model_entity.createIndividual(offsetStringURL, offsetStringOWL );

				
				
				//beginIndex
				OntProperty beginIndexProperty = modelString.createObjectProperty(STRING_URL_ONT + "beginIndex");
				Literal beginIndexLiteral = model_entity.createTypedLiteral(entity.getStartChar());
				offsetStringI.addProperty(beginIndexProperty, beginIndexLiteral);

				//endIndex
				OntProperty endIndexProperty = modelString.createObjectProperty(STRING_URL_ONT + "endIndex");
				Literal endIndexLiteral = model_entity.createTypedLiteral(entity.getEndChar());
				offsetStringI.addProperty(endIndexProperty, endIndexLiteral);


				OntProperty subStringProperty = modelString.createObjectProperty(STRING_URL_ONT + "subString");
				offsetStringI.addProperty(subStringProperty, stringI);



				//ANNOTATION
				//Anotation for the data ifself
				OntClass annotationOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
				Individual annotationEntity = model_entity.createIndividual(LINKEDTV_URL + "annotation/" + UUID.randomUUID(), annotationOWL );
				OntProperty  targetProperty= modelOA.createObjectProperty(Open_Annotation_URL + "hasTarget");
				annotationEntity.addProperty(targetProperty, mediaFragmentI);
				annotationEntity.addProperty(targetProperty, offsetStringI);
				OntProperty bodyProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasBody");
				annotationEntity.addProperty(bodyProperty, nerdI);

				//Provenance Ontology
				//Add info to the artifact
				annotationEntity.addProperty(RDF.type, modelPROV.createClass(PROV_URL + "Entity"));	


				OntProperty wasattributedtoOWL = modelPROV.createObjectProperty(PROV_URL + "wasAttributedTo");
				annotationEntity.addProperty(wasattributedtoOWL, organizationI);

				Calendar cal = GregorianCalendar.getInstance();
				Literal value = model_entity.createTypedLiteral(cal);		
				OntProperty startedattimeOWL = modelPROV.createObjectProperty(PROV_URL + "startedAtTime");
				annotationEntity.addProperty(startedattimeOWL, value);

				//DerivedFrom
				OntProperty wasderivedfromOWL = modelPROV.createObjectProperty(PROV_URL + "wasDerivedFrom");
				annotationEntity.addProperty(wasderivedfromOWL, stringI); //aniadir entidad a el subtitulo

				
			}
			else {
				System.out.println("The entity "+ i+" has not been aligned with any MediaFragment ("+start+"). ABORTING creation of it.");
				System.out.println("--> " + entity.getLabel());
			}
		}

	}
	
	

	private Entry<Individual, Individual> getSubtitle(float start) {
		Set<Float> keys = mediaFragmentsSub.keySet();
		Iterator <Float> itkeys = keys.iterator();
		Entry<Individual, Individual> result = null;
		while  (itkeys.hasNext() && result == null){
			Float key = itkeys.next();
			if (key-threshold < start && start < key+threshold){
				result = mediaFragmentsSub.get(key);
				//System.out.println("ALIGNED with " + key);
			}
			//check if start is aproximately 
		}
		return result;
	}
	
	
	
}
