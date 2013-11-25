


Television RDFizator REST API 


TVRDFizator is a Web service for serializing various kinds of traditional video content metadata into RDF according to an ontology data model. It has been developed by Eurecom under the scope of the European Proyect LinkedTV FP7.

This service takes as input a MediaResource and its corresponding metadata files (available in different formats, see section 2.1 for more details), and produces a RDF representation of the whole information according to the LinkedTV ontology. This LinkedTV model defines a list of classes that can be relevant in the vast domain of television content, like for example Chapters, Scenes, Concepts, Objects... and allows to create links with information in external datasets.

The resultant information includes mainly (1) legacy information from the providers, (2) subtitles and extracted name entities, and (3) data obtained after the execution of various analysis techniques like shot segmentation, concept detection, or face recognition. This way the knowledge is represented in a graph that can be interlinked with other relevant content in the Web and allows the execution of complex queries that will bring the viewers a new Television experience.

Please, note that the content discovery and enrichment phase over the resulting graph is out of the scope of this REST service. Those funcionalities will be provided by WP2 in separate services that will operate over the generated RDF information.


The REST API calls that can be executed are basically:

1. Creating a media resource.

Type: POST
Content: URL of the video resource (locator). 
URL: http://localhost:8889/api/metadata/353533328776

Example:

curl -X POST --data-binary @rbbaktuell_20120809_sdg_related_content.ttl http://localhost:8889/api/metadata/353533328776?metadataType=legacy --header "Content-Type:text/xml" -v



2. Metadata files for media resource. 
The result will start processing.


2.1
curl -X POST --data-binary @rbbaktuell_20120809_sdg_related_content.ttl http://localhost:8889/api/metadata/353533328776?metadataType=legacy --header "Content-Type:text/xml" -v

curl -X POST --data-binary @tva.tvahandle.\!etc\!medialib\!rbb\!rbb\!rbbaktuell\!rbbaktuell_2145uhr\!rbbaktuell_20120809_sdg.xml  http://localhost:8889/api/mediaresource/353533328776?metadataType=legacy --header "Content-Type:text/xml" -v

curl -X POST --data-binary @tva.tvahandle.\!etc\!medialib\!rbb\!rbb\!rbbaktuell\!rbbaktuell_2145uhr\!rbbaktuell_20120809_sdg.xml  http://localhost:8889/api/mediaresource/353533328776?metadataType=legacy --header "Content-Type:text/xml" -v


2.2

curl -X POST --data-binary @rbbaktuell_20120809_sdg_related_content.ttl http://localhost:8889/api/metadata/353533328776?metadataType=legacy --header "Content-Type:text/xml" -v

curl -X POST --data-binary @tva.tvahandle.\!etc\!medialib\!rbb\!rbb\!rbbaktuell\!rbbaktuell_2145uhr\!rbbaktuell_20120809_sdg.xml  http://localhost:8889/api/mediaresource/353533328776?metadataType=legacy --header "Content-Type:text/xml" -v

curl -X POST --data-binary @tva.tvahandle.\!etc\!medialib\!rbb\!rbb\!rbbaktuell\!rbbaktuell_2145uhr\!rbbaktuell_20120809_sdg.xml  http://localhost:8889/api/mediaresource/353533328776?metadataType=legacy --header "Content-Type:text/xml" -v




3. Get serialization results:

curl -X POST --data-binary @rbbaktuell_20120809_sdg_related_content.ttl http://localhost:8889/api/metadata/353533328776?metadataType=legacy --header "Content-Type:text/xml" -v

curl -X POST --data-binary @tva.tvahandle.\!etc\!medialib\!rbb\!rbb\!rbbaktuell\!rbbaktuell_2145uhr\!rbbaktuell_20120809_sdg.xml  http://localhost:8889/api/mediaresource/353533328776?metadataType=legacy --header "Content-Type:text/xml" -v

curl -X POST --data-binary @tva.tvahandle.\!etc\!medialib\!rbb\!rbb\!rbbaktuell\!rbbaktuell_2145uhr\!rbbaktuell_20120809_sdg.xml  http://localhost:8889/api/mediaresource/353533328776?metadataType=legacy --header "Content-Type:text/xml" -v

Get the entire thing




