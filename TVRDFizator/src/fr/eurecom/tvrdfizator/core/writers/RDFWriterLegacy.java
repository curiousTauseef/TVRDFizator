package fr.eurecom.tvrdfizator.core.writers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import fr.eurecom.tvrdfizator.core.datastructures.Brand;
import fr.eurecom.tvrdfizator.core.datastructures.Episode;
import fr.eurecom.tvrdfizator.core.datastructures.ItemLayer;
import fr.eurecom.tvrdfizator.core.datastructures.Layer;
import fr.eurecom.tvrdfizator.core.datastructures.Material;
import fr.eurecom.tvrdfizator.core.datastructures.Speaker;
import fr.eurecom.tvrdfizator.core.datastructures.Version;
import fr.eurecom.tvrdfizator.core.datastructures.VideoMetaData;


public class RDFWriterLegacy {
	private String file_legacy = null;
	private String file_exmaralda = null;
	private String idMediaResource = "e2899e7f-67c1-4a08-9146-5a205f6de457";
	
	private VideoMetaData mdata = null;
	private OntModel modelBBC = null;
	private OntModel model_linkedtv = null;

	
	private OntModel model_legacy = null;
	private OntModel model_exmaralda = null;
	private OntModel model_agents = null;


	private OntModel modelMA = null;
	private OntModel modelOA = null;
	private OntModel modelFOAF = null;
	private OntModel modelOPMV = null;
	private OntModel modelLSCOM = null;
	
	

	
	private String BBC_ontology_URL = "http://purl.org/ontology/po/";
	private String Dublin_Core_URL = "http://purl.org/dc/elements/1.1/";
	private String Media_Resources_URL = "http://www.w3.org/ns/ma-ont/";
	private String Open_Annotation_URL = "http://www.w3.org/ns/openannotation/core/";
	private String Event_URL = "http://purl.org/NET/c4dm/event.owl/";
	private String Time_URL = "http://purl.org/NET/c4dm/timeline.owl/";
	private String LSCOM_URL = "http://vocab.linkeddata.es/lscom/";
	private String FOAF_URL = "http://xmlns.com/foaf/0.1/";
	private String OPMV_URL = "http://purl.org/net/opmv/ns/";
	private String LINKEDTV_URL = "http://data.linkedtv.eu/";
	private String LINKEDTV_URL_ONT = "http://data.linkedtv.eu/ontology/";


	
	public RDFWriterLegacy(String f_legacy, String f_exmaralda, VideoMetaData md, String idMediaResource, String namespace, String locator){
		file_legacy = f_legacy;
		file_exmaralda = f_exmaralda;
		this.idMediaResource = idMediaResource;
		mdata = md;
		
		
		if (!namespace.equals("")){
			LINKEDTV_URL = namespace;
		}
		
		if (!locator.equals("")){
			mdata.setVideoURL(locator);
		}
		
		
	}
	
	public void create_legacy() throws FileNotFoundException{

			createModel();
			generateLegacyMetadata();
			writeLegacy();
					

	}
	
	public void create_exmeralda() throws FileNotFoundException{

			createModel();
			generateExmaralda();
			writeExmaralda();
			

	}
	


	public void createModel() throws FileNotFoundException{
		 if (model_legacy == null){
			 model_legacy = ModelFactory.createOntologyModel();
			 model_exmaralda = ModelFactory.createOntologyModel();
			 model_agents = ModelFactory.createOntologyModel();
			 model_linkedtv = ModelFactory.createOntologyModel();

			 
			 modelBBC = ModelFactory.createOntologyModel();
			 modelMA = ModelFactory.createOntologyModel();
			 modelOA = ModelFactory.createOntologyModel();
			 modelFOAF= ModelFactory.createOntologyModel();
			 modelOPMV= ModelFactory.createOntologyModel();
			 modelLSCOM= ModelFactory.createOntologyModel();
	
			 
			 model_legacy.setNsPrefix( "po", BBC_ontology_URL );
			 model_legacy.setNsPrefix( "ma", Media_Resources_URL );
			 model_legacy.setNsPrefix( "oa", Open_Annotation_URL );
			 model_legacy.setNsPrefix( "dc", Dublin_Core_URL );
			 model_legacy.setNsPrefix( "event", Event_URL );
			 model_legacy.setNsPrefix( "timeline", Time_URL );
			 model_legacy.setNsPrefix( "foaf", FOAF_URL );
			 model_legacy.setNsPrefix( "opmv", OPMV_URL );
			 model_legacy.setNsPrefix( "lscom", LSCOM_URL );
			 model_legacy.setNsPrefix( "linkedtv", LINKEDTV_URL_ONT );

		 
			 model_exmaralda.setNsPrefix( "po", BBC_ontology_URL );
			 model_exmaralda.setNsPrefix( "ma", Media_Resources_URL );
			 model_exmaralda.setNsPrefix( "oa", Open_Annotation_URL );
			 model_exmaralda.setNsPrefix( "dc", Dublin_Core_URL );
			 model_exmaralda.setNsPrefix( "event", Event_URL );
			 model_exmaralda.setNsPrefix( "timeline", Time_URL );
			 model_exmaralda.setNsPrefix( "foaf", FOAF_URL );
			 model_exmaralda.setNsPrefix( "opmv", OPMV_URL );
			 model_exmaralda.setNsPrefix( "lscom", LSCOM_URL );
			 model_exmaralda.setNsPrefix( "linkedtv", LINKEDTV_URL_ONT );



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
	
	
	private void generateLegacyMetadata() {
		generateBrand();
		generateEpisode();
		
	}

	private void generateBrand() {
		//Generate the brand
		Brand b = mdata.getBrand();
		if (b != null){	
			OntClass brandOWL = modelBBC.getOntClass( BBC_ontology_URL + "Brand" );
			Individual brand1 = model_legacy.createIndividual(LINKEDTV_URL + "brand/"+ extractId(b.getId()), brandOWL );
			
			
			//microsites
			OntProperty microsites = modelBBC.createOntProperty(BBC_ontology_URL + "microsites");
			for (int i = 0; i < b.get_memberOf().size(); i ++){
				
				OntClass pageOWL = modelFOAF.getOntClass( FOAF_URL + "Page" );
				Individual page = modelFOAF.createIndividual(b.get_memberOf().elementAt(i), pageOWL );	
				brand1.addProperty(microsites, page);
			}
			
			//Episodes
			OntProperty episodesProperty = modelBBC.getOntProperty(BBC_ontology_URL + "episode");
			for (int i = 0; i < b.get_episodes().size(); i ++){
				Individual episode = model_legacy.getIndividual(LINKEDTV_URL + "episode/"  +extractId(b.get_episodes().elementAt(i)));
				if (episode == null){
					OntClass episodeOWL = modelBBC.getOntClass( BBC_ontology_URL + "Episode" );
					episode= model_legacy.createIndividual(LINKEDTV_URL + "episode/"  +extractId(b.get_episodes().elementAt(i)), episodeOWL );
				}
				brand1.addProperty(episodesProperty, episode);

			}
			
			//Title
			OntProperty titleProperty = modelBBC.createOntProperty(Dublin_Core_URL + "title");
			brand1.addProperty(titleProperty, b.getTitle());
			
			//ID
			OntProperty idProperty = modelBBC.createOntProperty(BBC_ontology_URL + "id");
			String id = b.getId();
			brand1.addProperty(idProperty, id);
		}		
	}


	private void generateEpisode() {
		Episode e = mdata.getEpisode();
		if (e != null){
			Individual episode1 = model_legacy.getIndividual(LINKEDTV_URL + "episode/"  +extractId(e.getId()));
			if (episode1 == null){
				OntClass episodeOWL = modelBBC.getOntClass( BBC_ontology_URL + "Episode" );
				episode1= model_legacy.createIndividual(LINKEDTV_URL + "episode/"  + extractId(e.getId()), episodeOWL );
			}
			
			//Version
			for (int i = 0; i < e.getVersions().size(); i++){
				Version v = e.getVersions().elementAt(i);
				String versionId = UUID.randomUUID().toString();
				

				
				//Create the Version instance
				OntClass versionOWL = modelBBC.getOntClass( BBC_ontology_URL + "Version" );
				Individual version1 = model_legacy.createIndividual(LINKEDTV_URL + "version/"+ versionId, versionOWL );
				
				
				//Include reference to the version in the episode
				OntProperty versionProperty = modelBBC.getOntProperty(BBC_ontology_URL + "version");
				episode1.addProperty(versionProperty, version1);
				
				
				//Include reference to the media fragment
				OntProperty hasmediaresourceProperty = model_linkedtv.createOntProperty(LINKEDTV_URL_ONT + "hasMediaResource");
				OntClass mrOWL = modelMA.getOntClass( Media_Resources_URL + "MediaResource" );
				Individual mr = model_linkedtv.createIndividual(LINKEDTV_URL + "media/"+ idMediaResource, mrOWL );
				version1.addProperty(hasmediaresourceProperty, mr);
				
				if (v.getFormat() != null){
					OntProperty aspectratioProperty = modelBBC.getOntProperty(BBC_ontology_URL + "aspect_ratio");
					version1.addProperty(aspectratioProperty, v.getFormat());
				}
				
				if (v.getStartTime() != null){
					OntClass IntervalOWL = modelMA.createClass( Event_URL + "Interval" );
					Individual interval1 = model_legacy.createIndividual( IntervalOWL );	
					
					OntProperty startProperty = modelBBC.createOntProperty(Event_URL + "start");
					//Create the literal
					Calendar cal = GregorianCalendar.getInstance();
					cal.setTime(v.getStartTime());
					Literal value_datestart = model_legacy.createTypedLiteral(cal);		
					interval1.addProperty(startProperty, value_datestart);
					
					if (v.getEndTime() != null){
						OntProperty endProperty = modelBBC.createOntProperty(Event_URL + "end");
						Calendar calstop = GregorianCalendar.getInstance();
						cal.setTime(v.getEndTime());
						Literal value_datestop = model_legacy.createTypedLiteral(calstop);		
						interval1.addProperty(endProperty, value_datestop);
					}
					
					OntProperty timeProperty = modelBBC.getOntProperty(BBC_ontology_URL + "time");
					version1.addProperty(timeProperty, interval1);
				}
				//Create the Broadcast instance
				String broadcastId = UUID.randomUUID().toString();
				OntClass broadcastOWL = modelBBC.getOntClass( BBC_ontology_URL + "Broadcast" );
				Individual broadcast1 = model_legacy.createIndividual(LINKEDTV_URL + "broadcast/"+broadcastId, broadcastOWL );	
				
				OntProperty broadcastofProperty = modelBBC.getOntProperty(BBC_ontology_URL + "broadcast_of");
				broadcast1.addProperty(broadcastofProperty, version1);
				
				if (v.getService() != null){
					
					OntClass ServiceOWL = modelBBC.getOntClass( BBC_ontology_URL + "Service" );
					Individual service1 = model_legacy.createIndividual(v.getService(), ServiceOWL );	

					//service1.addProperty(RDFS.label, v.getService());
					
					OntProperty broadcastonProperty = modelBBC.getOntProperty(BBC_ontology_URL + "broadcast_on");
					broadcast1.addProperty(broadcastonProperty, service1);
				}
			}
			
			OntProperty microsites = modelBBC.getOntProperty(BBC_ontology_URL + "microsites");
			for (int i = 0; i < e.get_membersOf().size(); i ++){
				OntClass agentOWL = modelFOAF.getOntClass( FOAF_URL + "Agent" );
				Individual agent = modelFOAF.createIndividual(e.get_membersOf().elementAt(i), agentOWL );	
				episode1.addProperty(microsites, agent);
			}
			
			//RelatedMaterial
			for (int i = 0; i < e.getMaterials().size(); i++){
				//Create Material
				Material m= e.getMaterials().elementAt(i);
				
				OntClass mediaResourceOWL = modelMA.createClass( Media_Resources_URL + "MediaResource" );
				Individual mediaResource1 = model_legacy.createIndividual(LINKEDTV_URL + "media/"+UUID.randomUUID(), mediaResourceOWL );
				
			
				
				//Format
				OntProperty formatProperty = modelMA.createOntProperty(Media_Resources_URL+"hasFormat");
				mediaResource1.addProperty(formatProperty, m.getHowRelated());
				
				//Locator
				OntProperty locatorProperty = modelMA.createOntProperty(Media_Resources_URL+"locator");
				Individual locatorResource = modelMA.createIndividual(m.getMediaUri(), RDFS.Resource );	
				mediaResource1.addProperty(locatorProperty, locatorResource);
				
				//Create annotation
				OntClass annotationOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
				Individual annotation1 = model_legacy.createIndividual(LINKEDTV_URL + "annotation/"+UUID.randomUUID(), annotationOWL );
				OntProperty bodyProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasBody");
				annotation1.addProperty(bodyProperty, mediaResource1);
				OntProperty targetProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasTarget");
				annotation1.addProperty(targetProperty, episode1);
				
		
				//Provenance
				OntClass organizationOWL = modelFOAF.getOntClass( FOAF_URL + "Organization" );
				Individual organizationI = modelOPMV.createIndividual("http://data.linkedtv.eu/organization/RBB", organizationOWL );
				organizationI.addProperty(RDF.type, modelOPMV.createClass(OPMV_URL + "Agent"));	
				
				//Add info to the artifact
				annotation1.addProperty(RDF.type, modelOPMV.createClass(OPMV_URL + "Artifact"));
				//annotation1.addProperty(RDF.type, "opmv:Artifact");	
				
				OntClass processOWL = modelOPMV.createClass(OPMV_URL + "Process" );
				Individual processI = model_legacy.createIndividual(processOWL);

				OntProperty wasperformedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasPerformedBy");
				processI.addProperty(wasperformedbyOWL, organizationI);	
				
				OntProperty wasgeneratedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedBy");
				annotation1.addProperty(wasgeneratedbyOWL, processI);
				
				Calendar cal = GregorianCalendar.getInstance();
				Literal value = model_legacy.createTypedLiteral(cal);		
				OntProperty wasgeneratedatOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedAt");
				annotation1.addProperty(wasgeneratedatOWL, value);
				
			}
			
			//Broadcaster
			//OntClass broadcasterOWL = modelBBC.getOntClass( BBC_ontology_URL + "Broadcaster" );
			//Individual broadcaster1 = model.createIndividual(e.getBroadcaster().replace(" ", ""), broadcasterOWL );
			if (e.getBroadcaster() !=null){
				OntProperty masterbrandProperty = modelBBC.getOntProperty(BBC_ontology_URL + "masterbrand");
				episode1.addProperty(masterbrandProperty, e.getBroadcaster());
			}
			
			//Keywords
			OntProperty episodesProperty = modelBBC.getOntProperty(BBC_ontology_URL + "subject");
			for (int i = 0; i < e.get_keywords().size(); i ++){
				episode1.addProperty(episodesProperty, e.get_keywords().elementAt(i));
			}

			//Long sysnopsis
			if (e.getLongSynopsis() != null){
				OntProperty synopsisLong = modelBBC.getOntProperty(BBC_ontology_URL + "long_synopsis");
				episode1.addProperty(synopsisLong, e.getLongSynopsis());
			}
			
			//Medium sysnopsis
			if (e.getMediumSynopsis() != null){
				OntProperty synopsisMedium = modelBBC.getOntProperty(BBC_ontology_URL + "medium_synopsis");
				episode1.addProperty(synopsisMedium, e.getMediumSynopsis());
			}
			
			//Short sysnopsis
			if (e.getShortSynopsis() != null){
				OntProperty synopsisShort = modelBBC.getOntProperty(BBC_ontology_URL + "short_synopsis");
				episode1.addProperty(synopsisShort, e.getShortSynopsis());
			}	
			//Id
			OntProperty idProperty = modelBBC.createOntProperty(BBC_ontology_URL + "id");
			String id = e.getId();
			episode1.addProperty(idProperty, id);
			
			//Title
			OntProperty titleProperty = modelBBC.getOntProperty(Dublin_Core_URL + "title");
			episode1.addProperty(titleProperty, e.getTitle());
		}
	}

	private void generateExmaralda() {
		createSpeakers();
		createLayers();
	}
	

	private void createSpeakers() {
		for (int i  = 0; i  < mdata.getSpeakers().size(); i++){
			Speaker spk = mdata.getSpeakers().get(i);
			OntClass personOWL = modelFOAF.getOntClass( FOAF_URL + "Person" );
			Individual personI = model_exmaralda.createIndividual(spk.getId(), personOWL );
			
			
			if (spk.getName() != null){
				OntProperty speakername = modelFOAF.getOntProperty(FOAF_URL + "name");
				personI.addProperty(speakername, spk.getName());
			}
			
			if (spk.getSex() != null){
				OntProperty gendername = modelFOAF.getOntProperty(FOAF_URL + "gender");
				personI.addProperty(gendername, spk.getSex());
			}
			
			if (spk.getAbbreviation() != null){
				OntProperty abbreviation = modelFOAF.getOntProperty(FOAF_URL + "nick");
				personI.addProperty(abbreviation, spk.getAbbreviation());
			}
			
			if (spk.getComment() != null){
				personI.addProperty(RDFS.comment, spk.getComment());
			}
			else{
				personI.addProperty(RDFS.comment, "");			
			}
			
			if (!spk.getLanguages().isEmpty()){
				OntProperty languajeSpeaker = modelFOAF.createOntProperty(FOAF_URL + "lang");
				for (int j = 0; j < spk.getLanguages().size(); j++){
					personI.addProperty(languajeSpeaker, spk.getLanguages().elementAt(i));
				}
			}
	
		}
	}
	
	
	private void createLayers() {
		//Generate the RDF of every of the layers: 
		/*
		for (int i = 0; i < mdata.getLayers().size(); i++){
			Layer l = mdata.getLayers().get(i);
			
			if (l.getName().equals("FhG_ASR-1_utterance")){
				createLayer_ARS_FhG_1_utterance(l);
			}
			
			if (l.getName().equals("FhG_ASR-1_word")){
				createLayer_ARS_FhG_1_word(l);
			}
			if (l.getName().equals("UEP_Keywords-1")){
				createLayer_UEP_Keywords_1(l);
			}
			if (l.getName().equals("CERTH_Shot")){
				createLayer_CERTH_Shot(l);
			}
			
			if (l.getName().equals("CERTH_Scene")){
				createLayer_CERTH_Scene(l);
			}
			
			if (l.getName().equals("CERTH_Concept-1_best-1")){
				createLayer_CERTH_Concept_1_best(l, 1);
			}
			
			if (l.getName().equals("CERTH_Concept-1_best-2")){
				createLayer_CERTH_Concept_1_best(l, 2);
			}
			
		}*/
	}

	
	private void createLayer_CERTH_Concept_1_best(Layer l, int id) {
		
		for (int i = 0; i < l.getFragments().size(); i ++){
			ItemLayer mf = l.getFragments().elementAt(i);
			OntClass mediaFragmentOWL = modelMA.createClass( Media_Resources_URL + "MediaFragment" );
			Individual mediaFragmentI = model_exmaralda.createIndividual(mf.getMediaFragmentURL(), mediaFragmentOWL );
			
			
			//Annotation for the Speaker 
			//Create annotation
			OntClass annotationOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
			Individual annotation1 = model_exmaralda.createIndividual(Open_Annotation_URL+ "Anno_"+i+"_Speaker_CERTH_Concept_1_best_"+id, annotationOWL );
			OntProperty bodyProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasBody");
			annotation1.addProperty(bodyProperty, mediaFragmentI);
			OntProperty targetProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasTarget");
			annotation1.addProperty(targetProperty, l.getSpeaker());
			
			//Anotation for the data ifself
			
			OntClass annotationDataOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
			Individual annotationData1 = model_exmaralda.createIndividual(Open_Annotation_URL+ "Anno_"+i+"_Data_CERTH_Concept_1_best_"+id, annotationDataOWL );
			annotationData1.addProperty(bodyProperty, mediaFragmentI);

			OntClass lscomclassOWL = modelLSCOM.createClass( LSCOM_URL + mf.getValue() );
			annotationData1.addProperty(targetProperty, lscomclassOWL);
			
			OntProperty typeAnnotationProperty = modelOA.createOntProperty(Open_Annotation_URL+"type");
			annotationData1.addProperty(typeAnnotationProperty, "Concept");
			
			OntProperty confidenceAnnotationProperty = modelOA.createOntProperty(Open_Annotation_URL+"confidence");
			annotationData1.addProperty(confidenceAnnotationProperty, mf.get_first_ud_information());
			
			//Provenance Ontology
			//Create the organization.
			OntClass organizationOWL = modelFOAF.getOntClass( FOAF_URL + "Organization" );
			Individual organizationI = model_exmaralda.createIndividual("CERTH", organizationOWL );
			organizationI.addProperty(RDF.type, "opmv:Agent");			
			//Add info to the artifact
			annotationData1.addProperty(RDF.type, "opmv:Artifact");	
			
			OntClass processOWL = modelOPMV.createClass(OPMV_URL + "Process" );
			Individual processI = model_exmaralda.createIndividual(processOWL);

			OntProperty wasperformedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasPerformedBy");
			processI.addProperty(wasperformedbyOWL, organizationI);	
			
			OntProperty wasgeneratedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedBy");
			annotationData1.addProperty(wasgeneratedbyOWL, processI);
			
			
			Calendar cal = GregorianCalendar.getInstance();
			Literal value = model_exmaralda.createTypedLiteral(cal);		
			OntProperty wasgeneratedatOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedAt");
			annotationData1.addProperty(wasgeneratedatOWL, value);

		}
		
	}

	private void createLayer_CERTH_Scene(Layer l) {
		for (int i = 0; i < l.getFragments().size(); i ++){
			ItemLayer mf = l.getFragments().elementAt(i);
			OntClass mediaFragmentOWL = modelMA.createClass( Media_Resources_URL + "MediaFragment" );
			Individual mediaFragmentI = model_exmaralda.createIndividual(mf.getMediaFragmentURL(), mediaFragmentOWL );
			
			
			//Annotation for the Speaker 
			//Create annotation
			OntClass annotationOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
			Individual annotation1 = model_exmaralda.createIndividual(Open_Annotation_URL+ "Anno_"+i+"_Speaker_CERTH_Scene", annotationOWL );
			OntProperty bodyProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasBody");
			annotation1.addProperty(bodyProperty, mediaFragmentI);
			OntProperty targetProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasTarget");
			annotation1.addProperty(targetProperty, l.getSpeaker());
			
			//Anotation for the data ifself
			
			OntClass annotationDataOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
			Individual annotationData1 = model_exmaralda.createIndividual(Open_Annotation_URL+ "Anno_"+i+"_Data_CERTH_Scene", annotationDataOWL );
			annotationData1.addProperty(bodyProperty, mediaFragmentI);
			

			annotationData1.addProperty(targetProperty, model_exmaralda.createTypedLiteral(mf.getValue()));
			
			OntProperty typeAnnotationProperty = modelOA.createOntProperty(Open_Annotation_URL+"type");
			annotationData1.addProperty(typeAnnotationProperty, "Scene");
			
			//Provenance Ontology
			//Create the organization.
			OntClass organizationOWL = modelFOAF.getOntClass( FOAF_URL + "Organization" );
			Individual organizationI = model_exmaralda.createIndividual("CERTH", organizationOWL );
			organizationI.addProperty(RDF.type, "opmv:Agent");			
			//Add info to the artifact
			annotationData1.addProperty(RDF.type, "opmv:Artifact");	
			
			OntClass processOWL = modelOPMV.createClass(OPMV_URL + "Process" );
			Individual processI = model_exmaralda.createIndividual(processOWL);

			OntProperty wasperformedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasPerformedBy");
			processI.addProperty(wasperformedbyOWL, organizationI);	
			
			OntProperty wasgeneratedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedBy");
			annotationData1.addProperty(wasgeneratedbyOWL, processI);
			
			
			Calendar cal = GregorianCalendar.getInstance();
			Literal value = model_exmaralda.createTypedLiteral(cal);		
			OntProperty wasgeneratedatOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedAt");
			annotationData1.addProperty(wasgeneratedatOWL, value);
			
		}
		
	}

	private void createLayer_CERTH_Shot(Layer l) {
		for (int i = 0; i < l.getFragments().size(); i ++){
			ItemLayer mf = l.getFragments().elementAt(i);
			OntClass mediaFragmentOWL = modelMA.createClass( Media_Resources_URL + "MediaFragment" );
			Individual mediaFragmentI = model_exmaralda.createIndividual(mf.getMediaFragmentURL(), mediaFragmentOWL );
			
			
			//Annotation for the Speaker 
			//Create annotation
			OntClass annotationOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
			Individual annotation1 = model_exmaralda.createIndividual(Open_Annotation_URL+ "Anno_"+i+"_Speaker_CERTH_Shot", annotationOWL );
			OntProperty bodyProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasBody");
			annotation1.addProperty(bodyProperty, mediaFragmentI);
			OntProperty targetProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasTarget");
			annotation1.addProperty(targetProperty, l.getSpeaker());
			
			//Anotation for the data ifself
			
			OntClass annotationDataOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
			Individual annotationData1 = model_exmaralda.createIndividual(Open_Annotation_URL+ "Anno_"+i+"_Data_CERTH_Shot", annotationDataOWL );
			annotationData1.addProperty(bodyProperty, mediaFragmentI);
			

			annotationData1.addProperty(targetProperty, model_exmaralda.createTypedLiteral(mf.getValue()));
			
			OntProperty typeAnnotationProperty = modelOA.createOntProperty(Open_Annotation_URL+"type");
			annotationData1.addProperty(typeAnnotationProperty, "Shot");
			
			//Provenance Ontology
			//Create the organization.
			OntClass organizationOWL = modelFOAF.getOntClass( FOAF_URL + "Organization" );
			Individual organizationI = model_exmaralda.createIndividual("CERTH", organizationOWL );
			organizationI.addProperty(RDF.type, "opmv:Agent");			
			//Add info to the artifact
			annotationData1.addProperty(RDF.type, "opmv:Artifact");	
			
			OntClass processOWL = modelOPMV.createClass(OPMV_URL + "Process" );
			Individual processI = model_exmaralda.createIndividual(processOWL);

			OntProperty wasperformedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasPerformedBy");
			processI.addProperty(wasperformedbyOWL, organizationI);	
			
			OntProperty wasgeneratedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedBy");
			annotationData1.addProperty(wasgeneratedbyOWL, processI);
			
			
			Calendar cal = GregorianCalendar.getInstance();
			Literal value = model_exmaralda.createTypedLiteral(cal);		
			OntProperty wasgeneratedatOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedAt");
			annotationData1.addProperty(wasgeneratedatOWL, value);
			
		}
		
	}

	private void createLayer_UEP_Keywords_1(Layer l) {
		for (int i = 0; i < l.getFragments().size(); i ++){
			ItemLayer mf = l.getFragments().elementAt(i);
			OntClass mediaFragmentOWL = modelMA.createClass( Media_Resources_URL + "MediaFragment" );
			Individual mediaFragmentI = model_exmaralda.createIndividual(mf.getMediaFragmentURL(), mediaFragmentOWL );
			
			
			//Annotation for the Speaker 
			//Create annotation
			OntClass annotationOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
			Individual annotation1 = model_exmaralda.createIndividual(Open_Annotation_URL+ "Anno_"+i+"_Speaker_UEP_Keywords_1", annotationOWL );
			OntProperty bodyProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasBody");
			annotation1.addProperty(bodyProperty, mediaFragmentI);
			OntProperty targetProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasTarget");
			annotation1.addProperty(targetProperty, l.getSpeaker());
			
			//Anotation for the data ifself
			
			OntClass annotationDataOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
			Individual annotationData1 = model_exmaralda.createIndividual(Open_Annotation_URL+ "Anno_"+i+"_Data_UEP_Keywords_1", annotationDataOWL );
			annotationData1.addProperty(bodyProperty, mediaFragmentI);
			

			annotationData1.addProperty(targetProperty, model_exmaralda.createTypedLiteral(mf.getValue()));
			
			OntProperty typeAnnotationProperty = modelOA.createOntProperty(Open_Annotation_URL+"type");
			annotationData1.addProperty(typeAnnotationProperty, "Keyword");
			
			//Provenance Ontology
			//Create the organization.
			OntClass organizationOWL = modelFOAF.getOntClass( FOAF_URL + "Organization" );
			Individual organizationI = model_exmaralda.createIndividual("UEP", organizationOWL );
			organizationI.addProperty(RDF.type, "opmv:Agent");			
			//Add info to the artifact
			annotationData1.addProperty(RDF.type, "opmv:Artifact");	
			
			OntClass processOWL = modelOPMV.createClass(OPMV_URL + "Process" );
			Individual processI = model_exmaralda.createIndividual(processOWL);

			OntProperty wasperformedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasPerformedBy");
			processI.addProperty(wasperformedbyOWL, organizationI);	
			
			OntProperty wasgeneratedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedBy");
			annotationData1.addProperty(wasgeneratedbyOWL, processI);
			
			
			Calendar cal = GregorianCalendar.getInstance();
			Literal value = model_exmaralda.createTypedLiteral(cal);		
			OntProperty wasgeneratedatOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedAt");
			annotationData1.addProperty(wasgeneratedatOWL, value);
			
		}
	}

	private void createLayer_ARS_FhG_1_word(Layer l) {
		for (int i = 0; i < l.getFragments().size(); i ++){
			ItemLayer mf = l.getFragments().elementAt(i);
			OntClass mediaFragmentOWL = modelMA.createClass( Media_Resources_URL + "MediaFragment" );
			Individual mediaFragmentI = model_exmaralda.createIndividual(mf.getMediaFragmentURL(), mediaFragmentOWL );
			
			
			//Annotation for the Speaker 
			//Create annotation
			OntClass annotationOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
			Individual annotation1 = model_exmaralda.createIndividual(Open_Annotation_URL+ "Anno_"+i+"_Speaker_ARS_FhG_1_word", annotationOWL );
			OntProperty bodyProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasBody");
			annotation1.addProperty(bodyProperty, mediaFragmentI);
			OntProperty targetProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasTarget");
			annotation1.addProperty(targetProperty, l.getSpeaker());
			
			//Anotation for the data ifself
			
			OntClass annotationDataOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
			Individual annotationData1 = model_exmaralda.createIndividual(Open_Annotation_URL+ "Anno_"+i+"_Data_ARS_FhG_1_word", annotationDataOWL );
			annotationData1.addProperty(bodyProperty, mediaFragmentI);

			annotationData1.addProperty(targetProperty, model_exmaralda.createTypedLiteral(mf.getValue()));
			
			OntProperty typeAnnotationProperty = modelOA.createOntProperty(Open_Annotation_URL+"type");
			annotationData1.addProperty(typeAnnotationProperty, "ASR_word");
			
			OntProperty confidenceAnnotationProperty = modelOA.createOntProperty(Open_Annotation_URL+"confidence");
			annotationData1.addProperty(confidenceAnnotationProperty, mf.get_first_ud_information());
			
			//Provenance Ontology
			//Create the organization.
			OntClass organizationOWL = modelFOAF.getOntClass( FOAF_URL + "Organization" );
			Individual organizationI = model_exmaralda.createIndividual("FhG", organizationOWL );
			organizationI.addProperty(RDF.type, "opmv:Agent");			
			//Add info to the artifact
			annotationData1.addProperty(RDF.type, "opmv:Artifact");	
			
			OntClass processOWL = modelOPMV.createClass(OPMV_URL + "Process" );
			Individual processI = model_exmaralda.createIndividual(processOWL);

			OntProperty wasperformedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasPerformedBy");
			processI.addProperty(wasperformedbyOWL, organizationI);	
			
			OntProperty wasgeneratedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedBy");
			annotationData1.addProperty(wasgeneratedbyOWL, processI);
			
			
			Calendar cal = GregorianCalendar.getInstance();
			Literal value = model_exmaralda.createTypedLiteral(cal);		
			OntProperty wasgeneratedatOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedAt");
			annotationData1.addProperty(wasgeneratedatOWL, value);

		}
	}

	private void createLayer_ARS_FhG_1_utterance(Layer l) {
		for (int i = 0; i < l.getFragments().size(); i ++){
			ItemLayer mf = l.getFragments().elementAt(i);
			OntClass mediaFragmentOWL = modelMA.createClass( Media_Resources_URL + "MediaFragment" );
			Individual mediaFragmentI = model_exmaralda.createIndividual(mf.getMediaFragmentURL(), mediaFragmentOWL );
			
			
			//Annotation for the Speaker 
			//Create annotation
			OntClass annotationOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
			Individual annotation1 = model_exmaralda.createIndividual(Open_Annotation_URL+ "Anno_"+i+"_Speaker_ARS_FhG_1_utterance", annotationOWL );
			OntProperty bodyProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasBody");
			annotation1.addProperty(bodyProperty, mediaFragmentI);
			OntProperty targetProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasTarget");
			annotation1.addProperty(targetProperty, l.getSpeaker());
			
			//Anotation for the data ifself
			
			OntClass annotationDataOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
			Individual annotationData1 = model_exmaralda.createIndividual(Open_Annotation_URL+ "Anno_"+i+"_Data_ARS_FhG_1_utterance", annotationDataOWL );
			annotationData1.addProperty(bodyProperty, mediaFragmentI);
			

			annotationData1.addProperty(targetProperty, model_exmaralda.createTypedLiteral(mf.getValue()));
			
			OntProperty typeAnnotationProperty = modelOA.createOntProperty(Open_Annotation_URL+"type");
			annotationData1.addProperty(typeAnnotationProperty, "ASR");
			
			//Provenance Ontology
			//Create the organization.
			OntClass organizationOWL = modelFOAF.getOntClass( FOAF_URL + "Organization" );
			Individual organizationI = model_exmaralda.createIndividual("FhG", organizationOWL );
			organizationI.addProperty(RDF.type, "opmv:Agent");			
			//Add info to the artifact
			annotationData1.addProperty(RDF.type, "opmv:Artifact");	
			
			OntClass processOWL = modelOPMV.createClass(OPMV_URL + "Process" );
			Individual processI = model_exmaralda.createIndividual(processOWL);

			OntProperty wasperformedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasPerformedBy");
			processI.addProperty(wasperformedbyOWL, organizationI);	
			
			OntProperty wasgeneratedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedBy");
			annotationData1.addProperty(wasgeneratedbyOWL, processI);
			
			
			Calendar cal = GregorianCalendar.getInstance();
			Literal value = model_exmaralda.createTypedLiteral(cal);		
			OntProperty wasgeneratedatOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedAt");
			annotationData1.addProperty(wasgeneratedatOWL, value);
			
			//Data
			/*
			OntProperty roleMF_OWL = modelMA.createOntProperty( Media_Resources_URL + "role" );
			mediaFragmentI.addProperty(roleMF_OWL, "ASR");

			OntProperty captioning_OWL = modelMA.createOntProperty( Media_Resources_URL + "hasCaptioning" );
			mediaFragmentI.addProperty(captioning_OWL, mf.getValue());*/
		}
	}
	private void writeLegacy() throws FileNotFoundException {

			FileOutputStream out_File = new FileOutputStream(new File(file_legacy));
			model_legacy.write(out_File, "TURTLE");

		//model.write(System.out, "TURTLE");
	}
	
	private void writeExmaralda() throws FileNotFoundException {
		

			FileOutputStream out_File = new FileOutputStream(new File(file_exmaralda));
			model_exmaralda.write(out_File, "TURTLE");


		//model.write(System.out, "TURTLE");
	}

	private String extractId(String crid){
		String id = crid;
		while (id.indexOf('/') != -1) id = id.substring(id.indexOf('/')+1);
		return id;
	}

	
	/*
	for (int i = 0; i < l.getFragments().size(); i ++){
		MediaFragment mf = l.getFragments().elementAt(i);
		OntClass mediaFragmentOWL = modelMA.createClass( Media_Resources_URL + "MediaFragment" );
		Individual mediaFragmentI = model.createIndividual(mf.getMediaFragmentURL(), mediaFragmentOWL );
		
		
		//Annotation for the Speaker 
		//Create annotation
		OntClass annotationOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
		Individual annotation1 = model.createIndividual(Open_Annotation_URL+ "Anno_"+i+"_Speaker", annotationOWL );
		OntProperty bodyProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasBody");
		annotation1.addProperty(bodyProperty, mediaFragmentI);
		OntProperty targetProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasTarget");
		annotation1.addProperty(targetProperty, l.getSpeaker());
		
		
		//Provenance Ontology
		//Create the organization.
		OntClass organizationOWL = modelFOAF.getOntClass( FOAF_URL + "Organization" );
		Individual organizationI = model.createIndividual("FhG", organizationOWL );
		organizationI.addProperty(RDF.type, "opmv:Agent");			
		//Add info to the artifact
		mediaFragmentI.addProperty(RDF.type, "opmv:Artifact");	
		
		OntClass processOWL = modelOPMV.createClass(OPMV_URL + "Process" );
		Individual processI = model.createIndividual(processOWL);

		OntProperty wasperformedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasPerformedBy");
		processI.addProperty(wasperformedbyOWL, organizationI);	
		
		OntProperty wasgeneratedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedBy");
		mediaFragmentI.addProperty(wasgeneratedbyOWL, processI);
		
		
		Calendar cal = GregorianCalendar.getInstance();
		Literal value = model.createTypedLiteral(cal);		
		OntProperty wasgeneratedatOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedAt");
		mediaFragmentI.addProperty(wasgeneratedatOWL, value);
		
		//Data
		
		OntProperty roleMF_OWL = modelMA.createOntProperty( Media_Resources_URL + "role" );
		mediaFragmentI.addProperty(roleMF_OWL, "ASR");

		OntProperty captioning_OWL = modelMA.createOntProperty( Media_Resources_URL + "hasCaptioning" );
		mediaFragmentI.addProperty(captioning_OWL, mf.getValue());
		
		OntProperty confidence_OWL = modelMA.createOntProperty( Media_Resources_URL + "confidence" );
		mediaFragmentI.addProperty(confidence_OWL, mf.getUd_information());
	}*/

}
