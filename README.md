TVRDFizator is a Web service for serializing different video metadata files into RDF according to an ontology data model. It has been developed by Eurecom under the scope of the European Project LinkedTV FP7.

== Introduction to Television to RDF (TV2RDF) REST API ==

This service takes as input a MediaResource and its corresponding metadata files (available in different formats, see section 2.1 for more details), and produces a RDF representation of the whole data according to the LinkedTV ontology. This LinkedTV model defines a list of classes that can be relevant in the vast domain of television content, like for example Chapters, Scenes, Concepts, Objects... and allows to create links with information available in external datasets.

The results include mainly (1) legacy information from the providers, (2) subtitles and extracted name entities, and (3) data obtained after the execution of certain analysis techniques like shot segmentation, concept detection, or face recognition. The knowledge is represented in a graph that can be interlinked with other relevant content in the Web and allows the execution of complex queries that can bring the viewers a new Television experience.

Please, note that the content discovery and enrichment processes over the resulting graph is out of the scope of this REST service. Those functionalities will be provided by WP2 in separate modules that will operate over the generated RDF information. The REST API calls supported by TV2RDF are shown below:

== Creating a Media Resource == 
This request creates a new MediaResource to be processed in TV2RDF. It is necessary to provide the UUIDs of the media resource as path parameter, and the locator URL and base URL that should be used for generating the MediaFragments in the form of query parameters.

* Parameters:
** locator=locatorURL (mandatory)
** namespace=namespaceURL (optional, by default http://data.linkedtv.eu/)

* Examples:
curl -X POST http://linkedtv.eurecom.fr/tv2rdf/api/mediaresource/19a73f0a-d023-49f8-9203-cbd721053c55 --header "Content-Type:text/xml" -v

* Example:
curl -X POST "http://linkedtv.eurecom.fr/tv2rdf/api/mediaresource/19a73f0a-d023-49f8-9203-cbd721053c55?locator=http://stream6.noterik.com/progressive/stream6/domain/linkedtv/user/rbb/video/59/rawvideo/2/raw.mp4&namespace=http://data.linkedtv.eu/" --header "Content-Type:text/xml" -v

== Define Metadata files for every Media Resource == 
Those requests indicate which are the metadata files that describe a particular Media Resource: the legacy file, the subtitles file and the analysis results.
Immediately after the storage of the files in the server, the corresponding serialization process is automatically launched, so the RDF results will be available as soon as possible by performing the REST requests included in section 3. If those requests are executed various times, the files uploaded in the past are substituted by the ones specified in the current operation, and the serialization processes are started again.

=== Uploading Legacy Metadata === 
This request upload the file that contains the information provided by the broadcasters for a particular Media Resource, and launch the process of converting it into RDF.
Up to now, the format supported by TV2RDF is TVAnytime (http://tech.ebu.ch/tvanytime).
*Parameters:
**metadataType=legacy (mandatory)
*Example:
curl -X POST --data-binary @tva.tvahandle.\!etc\!medialib\!rbb\!rbb\!rbbaktuell\!rbbaktuell_2145uhr\!rbbaktuell_20120809_sdg.xml  http://linkedtv.eurecom.fr/tv2rdf/api/mediaresource/19a73f0a-d023-49f8-9203-cbd721053c55/metadata?metadataType=legacy --header "Content-Type:text/xml" -v

=== Uploading Subtitles === 
This request upload the file that contains the subtitles for a particular MediaResource,  and launch the process of extracting Named Entities from the time text an serialize them into RDF.
Up to now, the format supported by TV2RDF is SRT (http://es.wikipedia.org/wiki/SRT). It is possible to specify the API key to be used when invoking NERD and the name of the NERD extractor for spotting the named entities. 
*Parameters:
**metadataType=subtitle  (mandatory)
**extractor=extractorName  (optional, default: combined)
***Values: "alchemyapi", "dbspotlight", "textrazor", "extractiv", "lupedia", "opencalais", "saplo", "semitags", "wikimeta", "yahoo", "zemanta", "combined", "thd".
**apiKey=userapikey  (optional, default: tv2rdf account)
*Example:
curl -X POST --data-binary @rbbaktuell_120809.srt  http://linkedtv.eurecom.fr/tv2rdf/api/mediaresource/19a73f0a-d023-49f8-9203-cbd721053c55/metadata?metadataType=subtitle --header "Content-Type:text/xml" -v

=== Uploading Analysis Results === 
This request upload the file with the results from the execution of various analysis techniques over a particular MediaResource. 
Up to now, the format supported by TV2RDF is Exmeralda file (http://www.exmaralda.org/).
*Parameters:
**metadataType=exmaralda (mandatory)
* Example:
curl -X POST --data-binary @rbbaktuell_20120809_sdg_m_16_9_512x288.exb  http://linkedtv.eurecom.fr/tv2rdf/api/mediaresource/19a73f0a-d023-49f8-9203-cbd721053c55/metadata?metadataType=exmaralda --header "Content-Type:text/xml"

== Serialization results ==

Those requests allow to retrieve the RDF files generated after the serialization of the different metadata files. Those results will be available after the transformation of the uploaded metadata files by performing the GET requests to the TV2RDF server. Hence, a client can repeatedly send these calls to the REST service until the resource is available (the 404 responses turn into a  200 OK and the file is retrieved from the server). It is also possible to see if a serialization result is available by using the REST call explained in section 5.1.

=== Obtaining Legacy Metadata serialization === 
This request allows to retrieve the serialization file that includes the legacy information from the providers. The main ontology behind is the BBC Programmes Ontology.
* Parameters:
** metadataType=legacy (mandatory)
* Example:
curl -X GET http://linkedtv.eurecom.fr/tv2rdf/api/mediaresource/19a73f0a-d023-49f8-9203-cbd721053c55/serialization?metadataType=legacy --header "Content-Type:text/xml" -v

=== Obtaining Subtitles and Entities serialization === 
This request allows to retrieve the serialization file that includes the subtitles of the video and the named entities extracted by using NERD framework. The main ontologies behind are the NERD Ontology, The Open Annotation ontology, and the NIF ontology.
* Parameters:
** metadataType=subtitle (mandatory)
* Example:
curl -X GET http://linkedtv.eurecom.fr/tv2rdf/api/mediaresource/19a73f0a-d023-49f8-9203-cbd721053c55/serialization?metadataType=subtitle --header "Content-Type:text/xml" -v

=== Obtaining Analysis Results serialization === 
This request allows to retrieve the serialization file that includes the analysis results generated by WP1 over a particular video. The main ontologies behind are the Ontology for MediaResources, The Open Annotation ontology, and the LSCOM ontology.
* Parameters:
** metadataType=exmaralda (mandatory)
* Example:
curl -X GET http://linkedtv.eurecom.fr/tv2rdf/api/mediaresource/19a73f0a-d023-49f8-9203-cbd721053c55/serialization?metadataType=exmaralda --header "Content-Type:text/xml" -v

=== Obtaining complete serialization === 
This request allows to retrieve the serialization file that includes the whole information available about a certain MediaResource. This operation is still buggy, so its use is discouraged until the release of the new version of this REST API service. 
* Example:
curl -X GET http://linkedtv.eurecom.fr/tv2rdf/api/mediaresource/19a73f0a-d023-49f8-9203-cbd721053c55/serialization --header "Content-Type:text/xml" -v

== Retrieving Metadata files ==

At every moment, it is possible to retrieve also the original metadata files that were uploaded to TV2RDF for a certain MediaResource, by performing a GET request instead of the corresponding POST operations shown in section 2. This way it is possible to check for example which version of metadata files were considered to be serialized. 

=== Retrieving Legacy Metadata file === 
*Parameters:
**metadataType=legacy (mandatory)
*Example:
curl -X GET  http://linkedtv.eurecom.fr/tv2rdf/api/mediaresource/19a73f0a-d023-49f8-9203-cbd721053c55/metadata?metadataType=legacy --header "Content-Type:text/xml" -v

=== Retrieving Subtitles file === 
*Parameters:
**metadataType=subtitle (mandatory)
*Example:
curl -X GET  http://linkedtv.eurecom.fr/tv2rdf/api/mediaresource/19a73f0a-d023-49f8-9203-cbd721053c55/metadata?metadataType=subtitle --header "Content-Type:text/xml" -v

=== Retrieving Analysis Results file === 
*Parameters:
**metadataType=exmaralda (mandatory)
*Example:
curl -X GET  http://linkedtv.eurecom.fr/tv2rdf/api/mediaresource/19a73f0a-d023-49f8-9203-cbd721053c55/metadata?metadataType=exmaralda --header "Content-Type:text/xml" -v

== Other Requests == 

=== Get MediaResource's description === 
With this request it is possible to obtain a JSON serialization of the data available in the TV2RDF about a certain MediaResource: id, metadata files that have been uploaded, serialization files available, and base URL used for generating the different data instances of the knowledge graph. 
* Example:
curl -X GET http://linkedtv.eurecom.fr/tv2rdf/api/mediaresource/19a73f0a-d023-49f8-9203-cbd721053c55 --header "Content-Type:text/xml" -v

=== Modify MediaResource's parameters === 
If some of the parameters that (locator or namespace) need to be modified, we can used the same procedure as for creating the MediaResource. The parameters will be overwritten in the server side, and the serialization processes will be automatically relaunched again (if the corresponding metadata files are available).

*Example:
curl -X POST "http://linkedtv.eurecom.fr/tv2rdf/api/mediaresource/19a73f0a-d023-49f8-9203-cbd721053c55?locator=http://stream6.noterik.com/progressive/stream6/domain/linkedtv/user/rbb/video/59/rawvideo/2/raw.mp4&namespace=http://data.linkedtv.eu/" --header "Content-Type:text/xml" -v

=== Get a List of Media Resources === 
This operation returns the list of MediaResource's that have been created inside TV2RDF REST service.

*Example:
curl -X GET http://linkedtv.eurecom.fr/tv2rdf/api/mediaresource/list --header "Content-Type:text/xml" -v

[[Category:REST Services]]
