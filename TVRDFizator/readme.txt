


Television RDFizator REST API 


TVRDFizator is a Web service for serializing video content metadata in RDF according to an ontolog model. It has been developed by Eurecom under the scope of the European Proyect LinkedTV FP7.

This service takes as input a MediaResource and its corresponding metadata files (available in different formats, see section 2.1 for more details), and produces a RDF representation of the whole information according to the LinkedTV ontology. This LinkedTV model defines a list of classes that can be relevant in the vast domain of television content, like for example Chapters, Scenes, Concepts, Objects... and allows to create links with information in external datasets.


The knowledge graph obtained as result is suitable to be interlinked with other data in the Web, and allow the execution of complex queries that can bring the viewers a new way of enjoy Television. 

It is for generating the . The enrichment in this o a separate service..





The main actions are:

1. Creating a media resource.





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




