package fr.eurecom.tvrdfizator.core.writers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;


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
import fr.eurecom.tvrdfizator.core.datastructures.Pair;
import fr.eurecom.tvrdfizator.core.datastructures.VideoMetaData;

public class RDFWriterAnalisys {
	
	
	private String file_exmaralda = null;

	private VideoMetaData mdata = null;
	private OntModel modelBBC = null;
	private OntModel model_linkedtv = null;

	
	private OntModel model_exmaralda = null;
	private OntModel model_agents = null;


	private OntModel modelMA = null;
	private OntModel modelOA = null;
	private OntModel modelFOAF = null;
	private OntModel modelPROV = null;
	private OntModel modelLSCOM = null;
	private OntModel modelNSA = null;

	

	
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
	private String DBPEDIA_URL_ONT = "http://dbpedia.org/ontology/";
	private String NINSUNA_URL_ONT = "http://multimedialab.elis.ugent.be/organon/ontologies/ninsuna#";
	private String LINKEDTV_URL_ONT = "http://data.linkedtv.eu/ontologies/core#";

	
	
	private String mediaResourceID = "e2899e7f-67c1-4a08-9146-5a205f6de457";
	private String exmeraldaFile = "ftp://ftp.condat.de/Processing/SV/12_02_SV/FhG/EXMARaLDA/12_11_07";
	private String vsubtitleFile = "http://stream6.noterik.com/progressive/stream6/domain/linkedtv/user/SV/subtitle/TUSSEN_KUNST_AVR000080E2.srt";

	
	//private String mediaResourceID = "6026703a-c02c-41bc-9ac3-9923b23ef8f5";
	//private String exmeraldaFile = "ftp://ftp.condat.de/Processing/RBB/12_06_RBB/EXMARaLDA_FhG/";
	//private String subtitleFile = "http://stream6.noterik.com/progressive/stream6/domain/linkedtv/user/rbb/subtitle/rbbaktuell_120809.srt";

	
	
	public RDFWriterAnalisys(String f_analisys, VideoMetaData md, String media_item_id, String namespace, String locator){
		
		file_exmaralda = f_analisys;
		mediaResourceID = media_item_id;
		
		mdata = md;

	
		if (!namespace.equals("")){
			LINKEDTV_URL = namespace;
		}
		
		if (!locator.equals("")){
			mdata.setVideoURL(locator);
		}
	}
	
	
	
	public void create_exmeralda() throws FileNotFoundException{

			createModel();
			generateExmaralda();
			writeExmaralda();


	}
	
	
	private void writeExmaralda() throws FileNotFoundException {
		
			FileOutputStream out_File = new FileOutputStream(new File(file_exmaralda));
			model_exmaralda.write(out_File, "TURTLE");


	}
	
	


	public void createModel() throws FileNotFoundException{
		 if (model_exmaralda == null){
			 model_exmaralda = ModelFactory.createOntologyModel();
			 model_agents = ModelFactory.createOntologyModel();
			 model_linkedtv = ModelFactory.createOntologyModel();

			 
			 modelBBC = ModelFactory.createOntologyModel();
			 modelMA = ModelFactory.createOntologyModel();
			 modelOA = ModelFactory.createOntologyModel();
			 modelFOAF= ModelFactory.createOntologyModel();
			 modelPROV= ModelFactory.createOntologyModel();
			 modelLSCOM= ModelFactory.createOntologyModel();
			 modelNSA= ModelFactory.createOntologyModel();

		 
			 model_exmaralda.setNsPrefix( "po", BBC_ontology_URL );
			 model_exmaralda.setNsPrefix( "ma", Media_Resources_URL );
			 model_exmaralda.setNsPrefix( "oa", Open_Annotation_URL );
			 model_exmaralda.setNsPrefix( "dc", Dublin_Core_URL );
			 model_exmaralda.setNsPrefix( "event", Event_URL );
			 model_exmaralda.setNsPrefix( "timeline", Time_URL );
			 model_exmaralda.setNsPrefix( "foaf", FOAF_URL );
			 model_exmaralda.setNsPrefix( "prov", PROV_URL );
			 model_exmaralda.setNsPrefix( "lscom", LSCOM_URL );
			 model_exmaralda.setNsPrefix( "linkedtv", LINKEDTV_URL_ONT );
			 model_exmaralda.setNsPrefix( "dbpedia-owl", DBPEDIA_URL_ONT );
			 model_exmaralda.setNsPrefix( "nsa", NINSUNA_URL_ONT );

			

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
	
	private void generateExmaralda() {
		//createSpeakers();
		translateLayers();
	}
	
	
	private void translateLayers() {
		Hashtable <Pair, Individual>shots = new Hashtable <Pair, Individual> ();
		Hashtable <Pair, Individual>scenes = new Hashtable <Pair, Individual> ();

		
		//Create the main Media Resource
		OntClass mediaResourceOWL = modelMA.createClass( Media_Resources_URL + "MediaResource" );
		

		//Individual mediaResource = model_exmaralda.createIndividual(LINKEDTV_URL + "media/" + mediaResourceID, mediaResourceOWL);
		
		Individual mediaResource = model_exmaralda.createIndividual(LINKEDTV_URL + "media/" + mediaResourceID, mediaResourceOWL);

		
		OntProperty locatorProperty = modelMA.createOntProperty(Media_Resources_URL+"locator");
		Individual locatorResource = modelMA.createIndividual(mdata.getVideoURL(), RDFS.Resource );	
		mediaResource.addProperty(locatorProperty, locatorResource);
		
		//OntProperty subtitleProperty = modelMA.createOntProperty(Media_Resources_URL+"hasSubtitling");
		//Individual subtitleResource = modelMA.createIndividual(subtitleFile, mediaResourceOWL );	
		//mediaResource.addProperty(subtitleProperty, subtitleResource);
		
		

		
		
		
		System.out.println("Processing Analysis for video: "+mdata.getVideoURL());
		
		mdata.printLayersNames();
		
		//Create Shots.
		Layer layer;
		
		//OLD IMPLEMENTATION
		layer = mdata.getLayer("CERTH_Shot-1");
		List <Layer> certh_segments = null;
		if (layer == null){
			
			//NEW IMPLEMENTATION
			certh_segments = mdata.getLayersStartingBy("CERTH_Segments-");
			
			layer = extractShotLayer(certh_segments);
			
			if (layer == null) {
				System.out.println("No information about Shots. ABORTING");
				return;
			}
		}

			
			
			
			createShots(layer, mediaResource, shots);
			System.out.println("Shots parsed.");
			
			
			//Processing other Temporal segments:
			layer = extractChapterLayer(certh_segments);
			if (layer != null){
				createChapters(layer, mediaResource);
				System.out.println("Chapters parsed.");
			}
			else System.out.println("No information about Chapters");
			//Scenes
			layer = extractSceneLayer(certh_segments);
			if (layer != null){
				createScenes(layer, mediaResource, shots, scenes);
				System.out.println("Scenes parsed.");
			}
			else System.out.println("No information about Scenes");
			

			List <Layer> layers_faces = mdata.getLayersStartingBy("EURECOM_FaceAnalysis");
			if (layers_faces.size() > 0){
				createFaces(layers_faces, mediaResource);
				System.out.println("Faces parsed.");
			}
			else System.out.println("No information about Faces");
			

			//Keywords
			layer = mdata.getLayer("UEP_Keywords-1");
			if (layer != null){
				createKeywords(layer, shots);
				System.out.println("Keywords parsed.");
			}
			else System.out.println("No information about Keywords");
	
			//Concepts
			layer = mdata.getLayer("CERTH_Concept-1_best-1");
			if (layer != null){
				createConcepts(layer, shots);
				System.out.println("CERTH_Concept-1_best-1 parsed.");
			}
			else System.out.println("No information about CERTH_Concept-1_best-1");
			
			layer = mdata.getLayer("CERTH_Concept-1_best-2");
			if (layer != null){
				createConcepts(layer, shots);
				System.out.println("CERTH_Concept-1_best-2 parsed.");
			}
			else System.out.println("No information about CERTH_Concept-1_best-2");		
			
			layer = mdata.getLayer("CERTH_Concept-1_best-3");
			if (layer != null){
				createConcepts(layer, shots);
				System.out.println("CERTH_Concept-1_best-3 parsed.");
			}
			else System.out.println("No information about CERTH_Concept-1_best-3");	
			
			layer = mdata.getLayer("CERTH_Concept-1_best-4");
			if (layer != null){
				createConcepts(layer, shots);
				System.out.println("CERTH_Concept-1_best-4 parsed.");
			}
			else System.out.println("No information about CERTH_Concept-1_best-4");	
			
			layer = mdata.getLayer("CERTH_Concept-1_best-5");
			if (layer != null){
				createConcepts(layer, shots);
				System.out.println("CERTH_Concept-1_best-5 parsed.");
			}
			else System.out.println("No information about CERTH_Concept-1_best-5");	
			
			layer = mdata.getLayer("CERTH_Concept-1_best-6");
			if (layer != null){
				createConcepts(layer, shots);
				System.out.println("CERTH_Concept-1_best-6 parsed.");
			}
			else System.out.println("No information about CERTH_Concept-1_best-6");	
			
			layer = mdata.getLayer("CERTH_Concept-1_best-7");
			if (layer != null){
				createConcepts(layer, shots);
				System.out.println("CERTH_Concept-1_best-7 parsed.");
			}
			else System.out.println("No information about CERTH_Concept-1_best-7");	
			
			layer = mdata.getLayer("CERTH_Concept-1_best-8");
			if (layer != null){
				createConcepts(layer, shots);
				System.out.println("CERTH_Concept-1_best-8 parsed.");
			}
			else System.out.println("No information about CERTH_Concept-1_best-8");	
			
			//createConcepts(mdata.getLayer("CERTH_Concept-1_best-2"), shots);
			//createConcepts(mdata.getLayer("CERTH_Concept-1_best-3"), shots);
			//createConcepts(mdata.getLayer("CERTH_Concept-1_best-4"), shots);
			//createConcepts(mdata.getLayer("CERTH_Concept-1_best-5"), shots);
			//createConcepts(mdata.getLayer("CERTH_Concept-1_best-6"), shots);
			//createConcepts(mdata.getLayer("CERTH_Concept-1_best-7"), shots);
			//createConcepts(mdata.getLayer("CERTH_Concept-1_best-8"), shots);
	
	
	

			
			//Spacial Objects
			int object = 1;
			mdata.existLayer("CERTH_Object-"+ object);
			while (mdata.existLayer("CERTH_Object-"+ object)){
				layer = mdata.getLayer("CERTH_Object-"+ object);
				createSpatialObject(layer, mediaResource);

				object++;
			}
			if (object == 1) System.out.println("No information about Spacial Objects");
			
			
			
/*			layer = mdata.getLayer("FhG_ASR-1_utterance");
			if (layer != null){
				createASR(layer, scenes);
				System.out.println("ASR parsed.");
			}
			else System.out.println("No information about ASR");*/

	}





	private void createFaces(List<Layer> layers_faces, Individual mediaResource) {
		

		
		  /*  <tier category="face" display-name="EURECOM_FaceAnalysis-1_0" id="TIE12" speaker="X" type="a">
		      <ud-tier-information/>
		      <event end="T303" start="T302">
		        <ud-information attribute-name="occurrence-779"> 39,84 19,44 18,16 32,29 55.68</ud-information>
		        <ud-information attribute-nam */
		System.out.println("Number of face layers: " + layers_faces.size());
		
		for (Layer layer : layers_faces){
			System.out.println(layer.getName());
			if(layer.getFragments().size() >0){
				ItemLayer face_appearance = layer.getFragments().get(0);
				float start = face_appearance.getStart();
				float end = face_appearance.getEnd();

				//System.out.println(face_appearance.getStart() + "  " + face_appearance.getEnd());
				Set<Entry<String, String>> faces_keyframes = face_appearance.get_table_ud_information();
				//System.out.println("     Number of elements:" + faces_keyframes.size());
				if (faces_keyframes.iterator().hasNext()){
					Entry<String, String> firstFaceframe = faces_keyframes.iterator().next();
					//System.out.println(firstFaceframe.getValue());
					String[] Faceframe_features = firstFaceframe.getValue().split(" ");
					if (Faceframe_features.length >= 5){
						//Ignoring whitespaces at the beginning
						int i = 0;
						while (Faceframe_features[i].equals("")) i++;
						
						String x = Faceframe_features[i].replace(",", ".");
						String y = Faceframe_features[i+1].replace(",", ".");
						String w = Faceframe_features[i+2].replace(",", ".");
						String h = Faceframe_features[i+3].replace(",", ".");
						String t = Faceframe_features[i+4].replace(",", ".");
						//System.out.println(Float.parseFloat(x) + "  " + Float.parseFloat(y) + "  " + Float.parseFloat(w) + "  " + Float.parseFloat(h) + "  " + Float.parseFloat(t));
					
					
					
					
						//Generating MediaFragment
						//Create upper mediafragment.
						OntClass mediaFragmentOWL = modelMA.createClass( Media_Resources_URL + "MediaFragment" );
						String boundingBox = x + "," + y + "," + w + "," + h + "," + t;

						Individual mediaFragmentFace = model_exmaralda.createIndividual(mediaResource + "#t=" +start +","+end + "&xywh=" + boundingBox, mediaFragmentOWL );

						mediaFragmentFace.addProperty(RDF.type, modelNSA.createClass(NINSUNA_URL_ONT + "TemporalFragment"));

						Literal temporalStart = model_exmaralda.createTypedLiteral(start);	
						OntProperty temporalStartProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalStart");
						mediaFragmentFace.addProperty(temporalStartProperty, temporalStart);
						
						Literal temporalEnd = model_exmaralda.createTypedLiteral(end);	
						OntProperty temporalEndProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalEnd");
						mediaFragmentFace.addProperty(temporalEndProperty, temporalEnd);
						
						Literal duration = model_exmaralda.createTypedLiteral(end-start);	
						OntProperty temporalDurationProperty = modelMA.createOntProperty(Media_Resources_URL+"duration");
						mediaFragmentFace.addProperty(temporalDurationProperty, duration);
						
						
						OntProperty temporalUnitProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalUnit");
						mediaFragmentFace.addProperty(temporalUnitProperty, "npt");
					
						OntProperty isfragmentofProperty = modelMA.createOntProperty(Media_Resources_URL+"isFragmentOf");
						mediaFragmentFace.addProperty(isfragmentofProperty, mediaResource);
						
						
						
						//Generating annotation and linkedtv:Face class

						
						OntClass faceclass = model_linkedtv.createClass( LINKEDTV_URL_ONT + "Face" );
						Individual face = model_exmaralda.createIndividual(LINKEDTV_URL + "face/"+  UUID.randomUUID(), faceclass );
						face.addProperty(RDFS.label, layer.getName());

						//ANNOTATION
						//Anotation for the data ifself
						OntClass annotationOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
						Individual annotationAppearance = model_exmaralda.createIndividual(LINKEDTV_URL + "annotation/" + UUID.randomUUID(), annotationOWL );
						OntProperty  targetProperty= modelOA.createObjectProperty(Open_Annotation_URL + "hasTarget");
						annotationAppearance.addProperty(targetProperty, mediaFragmentFace);

						OntProperty bodyProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasBody");
						annotationAppearance.addProperty(bodyProperty, face);
						
						//Provenance Ontology
						//Add info to the artifact
						annotationAppearance.addProperty(RDF.type, modelPROV.createClass(PROV_URL + "Entity"));	


						OntClass organizationOWL = modelFOAF.getOntClass( FOAF_URL + "Organization" );
						Individual organizationI = model_linkedtv.createIndividual(LINKEDTV_URL + "organization/"+"EURECOM", organizationOWL );
						organizationI.addProperty(RDF.type, modelPROV.createClass(PROV_URL + "Agent"));
						OntProperty wasattributedtoOWL = modelPROV.createObjectProperty(PROV_URL + "wasAttributedTo");
						annotationAppearance.addProperty(wasattributedtoOWL, organizationI);

						Calendar cal = GregorianCalendar.getInstance();
						Literal value = model_exmaralda.createTypedLiteral(cal);		
						OntProperty startedattimeOWL = modelPROV.createObjectProperty(PROV_URL + "startedAtTime");
						annotationAppearance.addProperty(startedattimeOWL, value);

						//DerivedFrom
						OntProperty wasderivedfromOWL = modelPROV.createObjectProperty(PROV_URL + "wasDerivedFrom");
						Individual exmeraldaResource = modelPROV.createIndividual(exmeraldaFile, RDFS.Resource );
						annotationAppearance.addProperty(wasderivedfromOWL, exmeraldaResource);
						
						
					}
				}
			}
			
		}
		
	}



	private Layer extractSceneLayer(List<Layer> certh_segments) {
		if (certh_segments == null)  return null;
		for (Layer l : certh_segments){
			
			if (l.getFragments().size() > 0) {
				if (l.getFragments().get(0).getValue().contains("Sc")){
					certh_segments.remove(l);
					return l;
				}
			}
		}
		return null;
	}



	private Layer extractChapterLayer(List<Layer> certh_segments) {

		if (certh_segments == null)  return null;
		for (Layer l : certh_segments){
			
			if (l.getFragments().size() > 0) {
				if (l.getFragments().get(0).getValue().contains("Ch")){
					certh_segments.remove(l);
					return l;
				}
			}
		}
		return null;
	}

	private Layer extractShotLayer(List<Layer> certh_segments) {

		if (certh_segments == null)  return null;
		for (Layer l : certh_segments){
			
			if (l.getFragments().size() > 0) {
				if (l.getFragments().get(0).getValue().contains("Sh")){
					certh_segments.remove(l);
					return l;
				}
			}
		}
		return null;
	}



	private void createSpatialObject(Layer layer, Individual mediaResource) {
		// TODO Auto-generated method stub
		
		
		//Create object
		OntClass spatialobjectclass = model_linkedtv.createClass( LINKEDTV_URL_ONT + "SpatialObject" );
		Individual spatialobject = model_exmaralda.createIndividual(LINKEDTV_URL + "spatial_object/"+  UUID.randomUUID(), spatialobjectclass );
		spatialobject.addProperty(RDFS.label, layer.getName());
		
		//For every event
		for (int i = 0; i < layer.getFragments().size(); i ++){
			ItemLayer appearance = layer.getFragments().get(i);
			
			//Create an Appearance.
			//OntClass appearanceclass = model_linkedtv.createClass( LINKEDTV_URL_ONT + "ObjectAppearance" );
			//Individual appearanceinstance = model_exmaralda.createIndividual(LINKEDTV_URL + "object_appearance/"+  UUID.randomUUID(), appearanceclass );
			

			
			
			//Create upper mediafragment.
			float startAppearance = appearance.getStart();
			float endAppearance = appearance.getEnd();
			OntClass mediaFragmentOWL = modelMA.createClass( Media_Resources_URL + "MediaFragment" );
			Individual mediaFragmentAppearance = model_exmaralda.createIndividual(mediaResource + "#t=" +startAppearance +","+endAppearance , mediaFragmentOWL );
			
			mediaFragmentAppearance.addProperty(RDF.type, modelNSA.createClass(NINSUNA_URL_ONT + "TemporalFragment"));

			Literal temporalStart = model_exmaralda.createTypedLiteral(startAppearance);	
			OntProperty temporalStartProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalStart");
			mediaFragmentAppearance.addProperty(temporalStartProperty, temporalStart);
			
			Literal temporalEnd = model_exmaralda.createTypedLiteral(endAppearance);	
			OntProperty temporalEndProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalEnd");
			mediaFragmentAppearance.addProperty(temporalEndProperty, temporalEnd);
			
			Literal duration = model_exmaralda.createTypedLiteral(endAppearance-startAppearance);	
			OntProperty temporalDurationProperty = modelMA.createOntProperty(Media_Resources_URL+"duration");
			mediaFragmentAppearance.addProperty(temporalDurationProperty, duration);
			
			
			OntProperty temporalUnitProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalUnit");
			mediaFragmentAppearance.addProperty(temporalUnitProperty, "npt");
		
			OntProperty isfragmentofProperty = modelMA.createOntProperty(Media_Resources_URL+"isFragmentOf");
			mediaFragmentAppearance.addProperty(isfragmentofProperty, mediaResource);
			
			
			
			
			//Create a annotation.

			//ANNOTATION
			//Anotation for the data ifself
			OntClass annotationOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
			Individual annotationAppearance = model_exmaralda.createIndividual(LINKEDTV_URL + "annotation/" + UUID.randomUUID(), annotationOWL );
			OntProperty  targetProperty= modelOA.createObjectProperty(Open_Annotation_URL + "hasTarget");
			annotationAppearance.addProperty(targetProperty, mediaFragmentAppearance);

			OntProperty bodyProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasBody");
			annotationAppearance.addProperty(bodyProperty, spatialobject);

			/*Provenance Ontology
			//Add info to the artifact
			annotationAppearance.addProperty(RDF.type, modelOPMV.createClass(OPMV_URL + "Artifact"));	
			//Create the organization.
			OntClass organizationOWL = modelFOAF.getOntClass( FOAF_URL + "Organization" );
			Individual organizationI = model_linkedtv.createIndividual(LINKEDTV_URL + "organization/"+"CERTH", organizationOWL );
			organizationI.addProperty(RDF.type, modelOPMV.createClass(OPMV_URL + "Agent"));

			OntClass processOWL = modelOPMV.createClass(OPMV_URL + "Process" );
			Individual processI = model_exmaralda.createIndividual(processOWL);
			OntProperty wasperformedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasPerformedBy");
			processI.addProperty(wasperformedbyOWL, organizationI);	

			OntProperty wasgeneratedbyOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedBy");
			annotationAppearance.addProperty(wasgeneratedbyOWL, processI);

			Calendar cal = GregorianCalendar.getInstance();
			Literal value = model_exmaralda.createTypedLiteral(cal);		
			OntProperty wasgeneratedatOWL = modelOPMV.createObjectProperty(OPMV_URL + "wasGeneratedAt");
			annotationAppearance.addProperty(wasgeneratedatOWL, value);
			*/
			
			

			//Provenance Ontology
			//Add info to the artifact
			annotationAppearance.addProperty(RDF.type, modelPROV.createClass(PROV_URL + "Entity"));	


			OntClass organizationOWL = modelFOAF.getOntClass( FOAF_URL + "Organization" );
			Individual organizationI = model_linkedtv.createIndividual(LINKEDTV_URL + "organization/"+"CERTH", organizationOWL );
			organizationI.addProperty(RDF.type, modelPROV.createClass(PROV_URL + "Agent"));
			OntProperty wasattributedtoOWL = modelPROV.createObjectProperty(PROV_URL + "wasAttributedTo");
			annotationAppearance.addProperty(wasattributedtoOWL, organizationI);

			Calendar cal = GregorianCalendar.getInstance();
			Literal value = model_exmaralda.createTypedLiteral(cal);		
			OntProperty startedattimeOWL = modelPROV.createObjectProperty(PROV_URL + "startedAtTime");
			annotationAppearance.addProperty(startedattimeOWL, value);

			//DerivedFrom
			OntProperty wasderivedfromOWL = modelPROV.createObjectProperty(PROV_URL + "wasDerivedFrom");
			Individual exmeraldaResource = modelPROV.createIndividual(exmeraldaFile, RDFS.Resource );
			annotationAppearance.addProperty(wasderivedfromOWL, exmeraldaResource);
			
			
			
			
			//Create the list of mediaFragments for bounding boxes

			int offset = Integer.parseInt(appearance.get_ud_information("offset"));
			String boundingBoxes = appearance.get_ud_information("area");
			String[] boundingboxes = StringUtils.splitByWholeSeparator(boundingBoxes, " - ");
			
			
			float timeInterval = ((float)offset)/25;
			float bbStart = startAppearance;
			float bbEnd = startAppearance + timeInterval;

			for (int bb = 0; bb < boundingboxes.length; bb ++) {
				//String boundinbox = boundingboxes[bb].replace(".", "");
				String boundinbox = boundingboxes[bb];
				
	
				Individual mediaFragmentBoundingbox = model_exmaralda.createIndividual(mediaResource + "#t=" +bbStart +","+bbEnd + "&xywh=percent:" + boundinbox, mediaFragmentOWL );
				
				mediaFragmentBoundingbox.addProperty(RDF.type, modelNSA.createClass(NINSUNA_URL_ONT + "TemporalFragment"));
				mediaFragmentBoundingbox.addProperty(RDF.type, model_linkedtv.createClass( LINKEDTV_URL_ONT + "BoundingBox" ));

				Literal temporalBBStart = model_exmaralda.createTypedLiteral(bbStart);	
				mediaFragmentBoundingbox.addProperty(temporalStartProperty, temporalBBStart);
				Literal temporalBBEnd = model_exmaralda.createTypedLiteral(bbEnd);	
				mediaFragmentBoundingbox.addProperty(temporalEndProperty, temporalBBEnd);
				mediaFragmentBoundingbox.addProperty(temporalUnitProperty, "npt");
			
				mediaFragmentBoundingbox.addProperty(isfragmentofProperty, mediaFragmentAppearance);
				
				
				bbStart = bbStart + timeInterval;
				bbEnd = bbEnd + timeInterval;

			}
			
			
			

		}
	}



	private void createASR(Layer layer, Hashtable<Pair, Individual> scenes) {
		
		
/*		Iterator<Entry<Pair, Individual>> it = scenes.entrySet().iterator();
		while (it.hasNext()){
			Entry<Pair, Individual> a = it.next();
			System.out.println("Scene: "+a.getValue().getURI() + " "+ a.getKey().getStart()+" "+ a.getKey().getEnd());
		}*/
		
		int numErrors = 0;
		System.out.println("Number of ASR fragment "+ layer.getFragments().size());
		for (int i = 0; i < layer.getFragments().size(); i ++){
			ItemLayer mf = layer.getFragments().elementAt(i);	
	
			Vector <Individual> candidateScenes = findScenes (scenes, mf.getStart(), mf.getEnd());
			if (candidateScenes.isEmpty()){
				System.out.println("ERROR ASR in " + mf.getStart() + " " + mf.getEnd());
				numErrors ++;
			}
			
			for (int j = 0; j <candidateScenes.size(); j ++) {
				Individual scene = candidateScenes.get(j);
				

				//Create Annotation for the ASR
				OntClass annotationOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
				Individual annotationASR = model_exmaralda.createIndividual(LINKEDTV_URL + "annotation/" + UUID.randomUUID(), annotationOWL );
				OntProperty  targetProperty= modelOA.createObjectProperty(Open_Annotation_URL + "hasTarget");
				annotationASR.addProperty(targetProperty, scene);
				OntProperty bodyProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasBody");
				
				//Create the instance of a ASR class
				OntClass ASR_OWL = model_linkedtv.createClass( LINKEDTV_URL_ONT + "ASR" );
				Individual asr = model_exmaralda.createIndividual(LINKEDTV_URL + "asr/"+  UUID.randomUUID(), ASR_OWL );
				asr.addProperty(RDFS.label, mf.getValue());			

				annotationASR.addProperty(bodyProperty, asr);

				
				
				//Provenance Ontology
				//Add info to the artifact
				annotationASR.addProperty(RDF.type, modelPROV.createClass(PROV_URL + "Entity"));	


				OntClass organizationOWL = modelFOAF.getOntClass( FOAF_URL + "Organization" );
				Individual organizationI = model_linkedtv.createIndividual(LINKEDTV_URL + "organization/"+"FhG", organizationOWL );
				organizationI.addProperty(RDF.type, modelPROV.createClass(PROV_URL + "Agent"));
				OntProperty wasattributedtoOWL = modelPROV.createObjectProperty(PROV_URL + "wasAttributedTo");
				annotationASR.addProperty(wasattributedtoOWL, organizationI);

				Calendar cal = GregorianCalendar.getInstance();
				Literal value = model_exmaralda.createTypedLiteral(cal);		
				OntProperty startedattimeOWL = modelPROV.createObjectProperty(PROV_URL + "startedAtTime");
				annotationASR.addProperty(startedattimeOWL, value);

				//DerivedFrom
				OntProperty wasderivedfromOWL = modelPROV.createObjectProperty(PROV_URL + "wasDerivedFrom");
				Individual exmeraldaResource = modelPROV.createIndividual(exmeraldaFile, RDFS.Resource );
				annotationASR.addProperty(wasderivedfromOWL, exmeraldaResource);
				
				
				//System.out.println("ASR " + mf.getStart() + " " + mf.getEnd() + " is inside " + scene.getURI());
				//else  System.out.println("Scene " + mf.getValue() + " is already one shot");
			}
		}
		
		System.out.println("There have been " + numErrors +" ASR fragments not mapped with any scene.");
	}



	private void createScenes(Layer layer, Individual mediaResource, Hashtable<Pair, Individual> shots, Hashtable<Pair, Individual> scenes) {
		for (int i = 0; i < layer.getFragments().size(); i ++){
			ItemLayer mf = layer.getFragments().elementAt(i);

			//Create Scene Media Fragment. Relate it with the media resource.
			OntClass mediaFragmentOWL = modelMA.createClass( Media_Resources_URL + "MediaFragment" );
			Individual mediaFragmentScene = model_exmaralda.createIndividual(mediaResource + mf.getMediaFragmentURL(), mediaFragmentOWL );
			
			
			//Ninsuna Ontology.
			mediaFragmentScene.addProperty(RDF.type, modelNSA.createClass(NINSUNA_URL_ONT + "TemporalFragment"));

			Literal temporalStart = model_exmaralda.createTypedLiteral(mf.getStart());	
			OntProperty temporalStartProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalStart");
			mediaFragmentScene.addProperty(temporalStartProperty, temporalStart);
			
			Literal temporalEnd = model_exmaralda.createTypedLiteral(mf.getEnd());	
			OntProperty temporalEndProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalEnd");
			mediaFragmentScene.addProperty(temporalEndProperty, temporalEnd);
			
			Literal duration = model_exmaralda.createTypedLiteral(mf.getEnd()-mf.getStart());	
			OntProperty temporalDurationProperty = modelMA.createOntProperty(Media_Resources_URL+"duration");
			mediaFragmentScene.addProperty(temporalDurationProperty, duration);
			
			OntProperty temporalUnitProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalUnit");
			mediaFragmentScene.addProperty(temporalUnitProperty, "npt");
			
			
			
			//Link with the mediaResource.
			OntProperty isfragmentofProperty = modelMA.createOntProperty(Media_Resources_URL+"isFragmentOf");
			mediaFragmentScene.addProperty(isfragmentofProperty, mediaResource);
			
			
			//Add it to the list of scenes
			Pair p = new Pair();
			p.setStart(mf.getStart());
			p.setEnd(mf.getEnd());
			scenes.put(p, mediaFragmentScene);
			
			
			//Create Annotation for the scene
			OntClass annotationOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
			Individual annotationScene = model_exmaralda.createIndividual(LINKEDTV_URL + "annotation/" + UUID.randomUUID(), annotationOWL );
			OntProperty  targetProperty= modelOA.createObjectProperty(Open_Annotation_URL + "hasTarget");
			annotationScene.addProperty(targetProperty, mediaFragmentScene);
			OntProperty bodyProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasBody");
			
			//Create the instance scene
			OntClass sceneOWL = model_linkedtv.createClass( LINKEDTV_URL_ONT + "Scene" );
			Individual scene = model_exmaralda.createIndividual(LINKEDTV_URL + "scene/"+  UUID.randomUUID(), sceneOWL );
			scene.addProperty(RDFS.label, mf.getValue());			

			annotationScene.addProperty(bodyProperty, scene);

			
			
			
			//Provenance Ontology
			//Add info to the artifact
			annotationScene.addProperty(RDF.type, modelPROV.createClass(PROV_URL + "Entity"));	


			OntClass organizationOWL = modelFOAF.getOntClass( FOAF_URL + "Organization" );
			Individual organizationI = model_linkedtv.createIndividual(LINKEDTV_URL + "organization/"+"CERTH", organizationOWL );
			organizationI.addProperty(RDF.type, modelPROV.createClass(PROV_URL + "Agent"));
			OntProperty wasattributedtoOWL = modelPROV.createObjectProperty(PROV_URL + "wasAttributedTo");
			annotationScene.addProperty(wasattributedtoOWL, organizationI);

			Calendar cal = GregorianCalendar.getInstance();
			Literal value = model_exmaralda.createTypedLiteral(cal);		
			OntProperty startedattimeOWL = modelPROV.createObjectProperty(PROV_URL + "startedAtTime");
			annotationScene.addProperty(startedattimeOWL, value);

			//DerivedFrom
			OntProperty wasderivedfromOWL = modelPROV.createObjectProperty(PROV_URL + "wasDerivedFrom");
			Individual exmeraldaResource = modelPROV.createIndividual(exmeraldaFile, RDFS.Resource );
			annotationScene.addProperty(wasderivedfromOWL, exmeraldaResource);
			
			
			
			
			//See which shots are related with this Scene.

			Vector <Individual> candidateShots = findShots (shots, mf.getStart(), mf.getEnd());

			//For every shot: Update isFragmentOf
			if (candidateShots.isEmpty()) System.out.println("Scene ERROR " + mf.getStart() + " " + mf.getEnd());
			for (int j = 0; j <candidateShots.size(); j ++) {
				Individual shot = candidateShots.get(j);
				if (!shot.getURI().equals(mediaFragmentScene.getURI())) {	
					shot.addProperty(isfragmentofProperty, mediaFragmentScene);
				}
				//else  System.out.println("Scene " + mf.getValue() + " is already one shot");
			}
		}
	}

	
	


	private void createKeywords(Layer layer, Hashtable<Pair, Individual> shots) {
		for (int i = 0; i < layer.getFragments().size(); i ++){
			ItemLayer mf = layer.getFragments().elementAt(i);
			Vector <Individual> candidateShots = findShots (shots, mf.getStart(), mf.getEnd());
			
			if (candidateShots.isEmpty()) System.out.println("Keyword lost " + mf.getStart() + " " + mf.getEnd());

			
			for (int j = 0; j <candidateShots.size(); j ++) {
				Individual shot = candidateShots.get(j);
					
					
					OntClass keywordOWL = model_linkedtv.createClass( LINKEDTV_URL_ONT + "Keyword" );
					OntProperty isfragmentofProperty = modelMA.createOntProperty(Media_Resources_URL+"hasKeyword");
					Individual keywordI = model_exmaralda.createIndividual(keywordOWL);
					keywordI.addProperty(RDFS.label, mf.getValue());
					shot.addProperty(isfragmentofProperty, keywordI);
					
				
			}
		}		
	}



	private void createConcepts(Layer layer, Hashtable <Pair, Individual> shots) {
		for (int i = 0; i < layer.getFragments().size(); i ++){
			ItemLayer mf = layer.getFragments().elementAt(i);
			//System.out.println("BUSCANDO POR "+mf.getStart() +"  "+mf.getEnd());
			Vector <Individual> candidateShots = findShots (shots, mf.getStart(), mf.getEnd());
			
			
			if (candidateShots.isEmpty()) System.out.println("Content not aligned with any shot " + mf.getStart() + " " + mf.getEnd());

			for (int j = 0; j <candidateShots.size(); j ++) {
				Individual shot = candidateShots.get(j);
					
				//Anotation for the data ifself
				OntClass annotationDataOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
				Individual annotationConcept = model_exmaralda.createIndividual(LINKEDTV_URL + "annotation/" + UUID.randomUUID(), annotationDataOWL );
				OntProperty  targetProperty= modelOA.createObjectProperty(Open_Annotation_URL + "hasTarget");
				annotationConcept.addProperty(targetProperty, shot);
				OntProperty bodyProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasBody");
					
				//CONFIDENCE
				OntProperty confidenceProperty = model_linkedtv.createObjectProperty(LINKEDTV_URL_ONT + "hasConfidence");
				Literal confidenceLiteral = model_exmaralda.createTypedLiteral(Float.parseFloat(mf.get_first_ud_information()));
				annotationConcept.addProperty(confidenceProperty, confidenceLiteral);
				
				//Create the concept itself
				OntClass conceptOWL = model_linkedtv.createClass( LINKEDTV_URL_ONT + "Concept" );
				Individual concept = model_exmaralda.createIndividual( LSCOM_URL + mf.getValue(), conceptOWL );
				concept.addProperty(RDFS.label, mf.getValue());
					
				Individual dbpediaClass = modelMA.createIndividual(DBPEDIA_URL_ONT+mf.getValue(), RDFS.Resource );	
				concept.addProperty(OWL.sameAs, dbpediaClass);
					
				annotationConcept.addProperty(bodyProperty, concept);

				
				//Provenance Ontology
				//Add info to the artifact
				annotationConcept.addProperty(RDF.type, modelPROV.createClass(PROV_URL + "Entity"));	


				OntClass organizationOWL = modelFOAF.getOntClass( FOAF_URL + "Organization" );
				Individual organizationI = model_linkedtv.createIndividual(LINKEDTV_URL + "organization/"+"CERTH", organizationOWL );
				organizationI.addProperty(RDF.type, modelPROV.createClass(PROV_URL + "Agent"));
				OntProperty wasattributedtoOWL = modelPROV.createObjectProperty(PROV_URL + "wasAttributedTo");
				annotationConcept.addProperty(wasattributedtoOWL, organizationI);

				Calendar cal = GregorianCalendar.getInstance();
				Literal value = model_exmaralda.createTypedLiteral(cal);		
				OntProperty startedattimeOWL = modelPROV.createObjectProperty(PROV_URL + "startedAtTime");
				annotationConcept.addProperty(startedattimeOWL, value);

				//DerivedFrom
				OntProperty wasderivedfromOWL = modelPROV.createObjectProperty(PROV_URL + "wasDerivedFrom");
				Individual exmeraldaResource = modelPROV.createIndividual(exmeraldaFile, RDFS.Resource );
				annotationConcept.addProperty(wasderivedfromOWL, exmeraldaResource);
				
				
				
				
			}
		}
	}


	

	private Vector <Individual> findScenes(Hashtable <Pair, Individual> scenes, float start, float end) {
		Iterator<Entry<Pair, Individual>> keys = scenes.entrySet().iterator();
		Vector <Individual> candidateScenes = new Vector <Individual> ();
		
		while (keys.hasNext()){
			Entry<Pair, Individual> key = keys.next();
			System.out.println("COMPARANDO CON "+ key.getKey().getStart() +"  "+ key.getKey().getEnd());

			if (key.getKey().getStart() <= start && end <= key.getKey().getEnd()){
				candidateScenes.add(key.getValue());
			}			
		}
		return candidateScenes;
	}





	private Vector <Individual> findShots(Hashtable <Pair, Individual> shots, float start, float end) {
		Iterator<Entry<Pair, Individual>> keys = shots.entrySet().iterator();
		Vector <Individual> candidateShots = new Vector <Individual> ();
		
		while (keys.hasNext()){
			Entry<Pair, Individual> key = keys.next();
			//System.out.println("COMPARANDO CON "+ key.getKey().getStart() +"  "+ key.getKey().getEnd());


			if (key.getKey().getStart() <= start && start < key.getKey().getEnd()){
				candidateShots.add(key.getValue());
				//if (end >= key.getKey().getEnd()) System.out.println("keyword Problematico " + start + " " + end);
			}
			else{
				if (key.getKey().getStart() < end && end <= key.getKey().getEnd()){
					candidateShots.add(key.getValue());
					//if (start < key.getKey().getStart()) System.out.println("keyword Problematico " + start + " " + end);
				}
				else {
					//Fragment contains the shot.
					if (key.getKey().getStart() >= start && end >= key.getKey().getEnd()){
						candidateShots.add(key.getValue());
					}
					
				}
			}

			
		}
		return candidateShots;
	}


	

	private void createChapters(Layer layer, Individual mediaResource) {


	for (int i = 0; i < layer.getFragments().size(); i ++){
			
			ItemLayer mf = layer.getFragments().elementAt(i);
			OntClass mediaFragmentOWL = modelMA.createClass( Media_Resources_URL + "MediaFragment" );
			Individual mediaFragmentI = model_exmaralda.createIndividual(mediaResource + mf.getMediaFragmentURL(), mediaFragmentOWL );
			
			
			//System.out.println("LOCAL NAME: "+mediaResource.getLocalName() +" // "+mediaResource);
			
			//Ninsuna Ontology.
			mediaFragmentI.addProperty(RDF.type, modelNSA.createClass(NINSUNA_URL_ONT + "TemporalFragment"));

			Literal temporalStart = model_exmaralda.createTypedLiteral(mf.getStart());	
			OntProperty temporalStartProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalStart");
			mediaFragmentI.addProperty(temporalStartProperty, temporalStart);
			
			Literal temporalEnd = model_exmaralda.createTypedLiteral(mf.getEnd());	
			OntProperty temporalEndProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalEnd");
			mediaFragmentI.addProperty(temporalEndProperty, temporalEnd);
			
			
			Literal duration = model_exmaralda.createTypedLiteral(mf.getEnd()-mf.getStart());	
			OntProperty temporalDurationProperty = modelMA.createOntProperty(Media_Resources_URL+"duration");
			mediaFragmentI.addProperty(temporalDurationProperty, duration);
			
			OntProperty temporalUnitProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalUnit");
			mediaFragmentI.addProperty(temporalUnitProperty, "npt");
			

			//Link with the mediaResource.
			OntProperty isfragmentofProperty = modelMA.createOntProperty(Media_Resources_URL+"isFragmentOf");
			mediaFragmentI.addProperty(isfragmentofProperty, mediaResource);
			
			//keyframes in case they are:
			createKeyframes(mf, mediaResource, mediaFragmentI);
			
			//Anotation for the data ifself
			OntClass annotationDataOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
			Individual annotationData1 = model_exmaralda.createIndividual(LINKEDTV_URL + "annotation/" + UUID.randomUUID(), annotationDataOWL );
			OntProperty  targetProperty= modelOA.createObjectProperty(Open_Annotation_URL + "hasTarget");
			annotationData1.addProperty(targetProperty, mediaFragmentI);
			OntProperty bodyProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasBody");
			
			//Create the chapter itself
			OntClass chapterOWL = model_linkedtv.createClass( LINKEDTV_URL_ONT + "Chapter" );
			Individual chapter = model_exmaralda.createIndividual(LINKEDTV_URL + "chapter/"+  UUID.randomUUID(), chapterOWL );
			chapter.addProperty(RDFS.label, mf.getValue());			

			annotationData1.addProperty(bodyProperty, chapter);

			
			//Provenance Ontology
			//Add info to the artifact
			annotationData1.addProperty(RDF.type, modelPROV.createClass(PROV_URL + "Entity"));	


			OntClass organizationOWL = modelFOAF.getOntClass( FOAF_URL + "Organization" );
			Individual organizationI = model_linkedtv.createIndividual(LINKEDTV_URL + "organization/"+"CERTH", organizationOWL );
			organizationI.addProperty(RDF.type, modelPROV.createClass(PROV_URL + "Agent"));
			OntProperty wasattributedtoOWL = modelPROV.createObjectProperty(PROV_URL + "wasAttributedTo");
			annotationData1.addProperty(wasattributedtoOWL, organizationI);

			Calendar cal = GregorianCalendar.getInstance();
			Literal value = model_exmaralda.createTypedLiteral(cal);		
			OntProperty startedattimeOWL = modelPROV.createObjectProperty(PROV_URL + "startedAtTime");
			annotationData1.addProperty(startedattimeOWL, value);

			//DerivedFrom
			OntProperty wasderivedfromOWL = modelPROV.createObjectProperty(PROV_URL + "wasDerivedFrom");
			Individual exmeraldaResource = modelPROV.createIndividual(exmeraldaFile, RDFS.Resource );
			annotationData1.addProperty(wasderivedfromOWL, exmeraldaResource);

			
		}
	}
	
	
	

	private void createKeyframes(ItemLayer mf, Individual mediaResource, Individual mediaFragment) {
		// TODO Auto-generated method stub
		//<ud-information attribute-name="keyframes">41811 46259 46406 47805 48158</ud-information>
		String keyframes = mf.get_ud_information("keyframes");
		if (keyframes!=null){
			System.out.println("Generating keyframes for Chapter " + mf.getValue());

			String[] individual_keyframes = keyframes.split(" ");
			for (int i = 0; i < individual_keyframes.length; i++){
				float temporalReference = Float.parseFloat(individual_keyframes[i]);
						
				
				//Link to image URL
				//hack while waiting for materialized images
				String URLkeyframe = "null";
				if (true) URLkeyframe = "http://data.linkedtv.eu/media/keyframe_"+mf.getValue().replaceAll("\n", "").replaceAll("\r", "").replaceAll(" ", "").replaceAll("\t", "")+"_" + i+".jpeg";
				

				OntProperty relatedimageProperty = modelMA.createOntProperty(Media_Resources_URL+"hasRelatedImage");
				Individual keyframeResource = model_exmaralda.createIndividual(URLkeyframe, RDFS.Resource );	
				mediaFragment.addProperty(relatedimageProperty, keyframeResource);
				
				
				//Annotate as keyframe
				OntClass keyframeOWL = model_linkedtv.createClass( LINKEDTV_URL_ONT + "Keyframe" );
				keyframeResource.addProperty(RDF.type, keyframeOWL);

				//Time
				Literal value = model_exmaralda.createTypedLiteral(temporalReference);		
				OntProperty startedattimeOWL = modelPROV.createObjectProperty(PROV_URL + "startedAtTime");
				keyframeResource.addProperty(startedattimeOWL, value);

				
				
			}
		}
	}



	private void createShots(Layer layer, Individual mediaResource, Hashtable  <Pair, Individual> shots) {
		for (int i = 0; i < layer.getFragments().size(); i ++){
			
			ItemLayer mf = layer.getFragments().elementAt(i);
			OntClass mediaFragmentOWL = modelMA.createClass( Media_Resources_URL + "MediaFragment" );
			Individual mediaFragmentI = model_exmaralda.createIndividual(mediaResource + mf.getMediaFragmentURL(), mediaFragmentOWL );
			
			
			//System.out.println("LOCAL NAME: "+mediaResource.getLocalName() +" // "+mediaResource);
			
			//Ninsuna Ontology.
			mediaFragmentI.addProperty(RDF.type, modelNSA.createClass(NINSUNA_URL_ONT + "TemporalFragment"));

			Literal temporalStart = model_exmaralda.createTypedLiteral(mf.getStart());	
			OntProperty temporalStartProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalStart");
			mediaFragmentI.addProperty(temporalStartProperty, temporalStart);
			
			Literal temporalEnd = model_exmaralda.createTypedLiteral(mf.getEnd());	
			OntProperty temporalEndProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalEnd");
			mediaFragmentI.addProperty(temporalEndProperty, temporalEnd);
			
			
			Literal duration = model_exmaralda.createTypedLiteral(mf.getEnd()-mf.getStart());	
			OntProperty temporalDurationProperty = modelMA.createOntProperty(Media_Resources_URL+"duration");
			mediaFragmentI.addProperty(temporalDurationProperty, duration);
			
			OntProperty temporalUnitProperty = modelNSA.createOntProperty(NINSUNA_URL_ONT+"temporalUnit");
			mediaFragmentI.addProperty(temporalUnitProperty, "npt");
			
			
			Pair p = new Pair();
			p.setStart(mf.getStart());
			p.setEnd(mf.getEnd());

			shots.put(p, mediaFragmentI);
			
			//Link with the mediaResource.
			OntProperty isfragmentofProperty = modelMA.createOntProperty(Media_Resources_URL+"isFragmentOf");
			mediaFragmentI.addProperty(isfragmentofProperty, mediaResource);
			
			//Anotation for the data ifself
			OntClass annotationDataOWL = modelOA.createClass( Open_Annotation_URL + "Annotation" );
			Individual annotationData1 = model_exmaralda.createIndividual(LINKEDTV_URL + "annotation/" + UUID.randomUUID(), annotationDataOWL );
			OntProperty  targetProperty= modelOA.createObjectProperty(Open_Annotation_URL + "hasTarget");
			annotationData1.addProperty(targetProperty, mediaFragmentI);
			OntProperty bodyProperty = modelOA.createObjectProperty(Open_Annotation_URL + "hasBody");
			
			//Create the shot itself
			OntClass shotOWL = model_linkedtv.createClass( LINKEDTV_URL_ONT + "Shot" );
			Individual shot = model_exmaralda.createIndividual(LINKEDTV_URL + "shot/"+  UUID.randomUUID(), shotOWL );
			shot.addProperty(RDFS.label, mf.getValue());			

			annotationData1.addProperty(bodyProperty, shot);

			
			
			//Provenance Ontology
			//Add info to the artifact
			annotationData1.addProperty(RDF.type, modelPROV.createClass(PROV_URL + "Entity"));	


			OntClass organizationOWL = modelFOAF.getOntClass( FOAF_URL + "Organization" );
			Individual organizationI = model_linkedtv.createIndividual(LINKEDTV_URL + "organization/"+"CERTH", organizationOWL );
			organizationI.addProperty(RDF.type, modelPROV.createClass(PROV_URL + "Agent"));
			OntProperty wasattributedtoOWL = modelPROV.createObjectProperty(PROV_URL + "wasAttributedTo");
			annotationData1.addProperty(wasattributedtoOWL, organizationI);

			Calendar cal = GregorianCalendar.getInstance();
			Literal value = model_exmaralda.createTypedLiteral(cal);		
			OntProperty startedattimeOWL = modelPROV.createObjectProperty(PROV_URL + "startedAtTime");
			annotationData1.addProperty(startedattimeOWL, value);

			//DerivedFrom
			OntProperty wasderivedfromOWL = modelPROV.createObjectProperty(PROV_URL + "wasDerivedFrom");
			Individual exmeraldaResource = modelPROV.createIndividual(exmeraldaFile, RDFS.Resource );
			annotationData1.addProperty(wasderivedfromOWL, exmeraldaResource);

			
		}
	}
	
	
}
