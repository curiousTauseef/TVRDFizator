package fr.eurecom.nerd.client.type;



public class Extractor {

    public static ExtractorType getType(String extractor) throws Exception 
    {    
        if(extractor.equals("alchemyapi")) 
            return ExtractorType.ALCHEMYAPI;
        
        else if (extractor.equals("combined"))
            return ExtractorType.COMBINED;
        
        else if (extractor.equals("dbspotlight"))
            return ExtractorType.DBSPOTLIGHT;
        

        else if (extractor.equals("extractiv"))
            return ExtractorType.EXTRACTIV;
        
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
        
        else
            throw new Exception(extractor + " is not supported by the NERD platform yet. " +
            		"If you are interested to use this extractor through NERD, please send an " +
            		"email to giuseppe.rizzo@eurecom.fr\n");
        
    }
    
    public static String getName(ExtractorType extractor) 
    {
        switch(extractor) 
        {
        case ALCHEMYAPI: 
            return "alchemyapi";
        case DBSPOTLIGHT:
            return "dbspotlight";
        case EXTRACTIV:
            return "extractiv"; 
        case OPENCALAIS:
            return "opencalais";  
        case LUPEDIA:
            return "lupedia";
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
        default:
            break;        
        }
        
        return null;
    }
}
