//   nerd4java - A java library which provides a programmable interface to NERD
//               http://nerd.eurecom.fr
//
//   Copyright 2012
//
//   Authors:
//      Giuseppe Rizzo <giuse.rizzo@gmail.com>
//
// This program is free software; you can redistribute it and/or modify it
// under the terms of the GNU General Public License published by
// the Free Software Foundation, either version 3 of the License, or (at 
// your option) any later version. See the file Documentation/GPL3 in the
// original distribution for details. There is ABSOLUTELY NO warranty.

package fr.eurecom.nerd.client;

import fr.eurecom.nerd.client.type.ExtractorType;
import fr.eurecom.nerd.client.type.GranularityType;

public class LookUp {
    
    public static String mapExtractor(ExtractorType extractor) 
    {
        switch(extractor) 
        {
        case ALCHEMYAPI: 
            return "alchemyapi";
        case DANDELIONAPI:
        	return "dandelionapi";
        case DBSPOTLIGHT:
            return "dbspotlight";
        case LUPEDIA:
            return "lupedia";           
        case OPENCALAIS:
            return "opencalais";
        case SAPLO:
            return "saplo";
        case SEMITAGS:
            return "semitags"; 
        case TEXTRAZOR:
            return "textrazor";
        case THD: 
        	return "thd";
        case WIKIMETA: 
            return "wikimeta";
        case YAHOO:
            return "yahoo";
        case ZEMANTA:
            return "zemanta";
        case COMBINED:
            return "combined";
        case NERDML:
        	return "nerdml";
        }
        return null;
    }

    public static String mapGranularity(GranularityType granType) 
    {       
        switch(granType)
        {
        case OEN:
            return "oen";
        case OED:
            return "oed";
        }
        return null;
    }
    
    public static ExtractorType mapExtractorStringType(String extractor) throws Exception 
    {    
        if(extractor.equals("alchemyapi")) 
            return ExtractorType.ALCHEMYAPI;
        
        else if (extractor.equals("combined"))
            return ExtractorType.COMBINED;
        
        else if (extractor.equals("dbspotlight"))
            return ExtractorType.DBSPOTLIGHT;
        
        else if (extractor.equals("lupedia"))
            return ExtractorType.LUPEDIA;
        
        else if (extractor.equals("opencalais"))
            return ExtractorType.OPENCALAIS;
        
        else if (extractor.equals("saplo"))
            return ExtractorType.SAPLO;        
                
        else if (extractor.equals("semitags"))
            return ExtractorType.SEMITAGS;
        
        else if (extractor.equals("textrazor"))
            return ExtractorType.TEXTRAZOR;
        
        else if (extractor.equals("wikimeta"))
            return ExtractorType.WIKIMETA;
        
        else if(extractor.equals("yahoo"))
            return ExtractorType.YAHOO;
        
        else if(extractor.equals("zemanta"))
            return ExtractorType.ZEMANTA;
        
        else if(extractor.equals("thd"))
            return ExtractorType.THD;
        
        else if(extractor.equals("nerdml"))
            return ExtractorType.NERDML;
        
        else if(extractor.equals("dandelionapi"))
            return ExtractorType.DANDELIONAPI;
        else
            throw new Exception(extractor + " is not supported by the NERD platform yet. " +
            		"If you are interested to use this extractor through NERD, please send an " +
            		"email to giuseppe.rizzo@eurecom.fr\n");
        
    }
    
}
