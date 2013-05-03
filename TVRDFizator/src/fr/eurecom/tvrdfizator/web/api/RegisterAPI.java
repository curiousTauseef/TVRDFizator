package fr.eurecom.tvrdfizator.web.api;


import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class RegisterAPI extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();        
        
        classes.add(Serialization.class);
        classes.add(Metadata.class);
        classes.add(MediaResource.class);

        return classes;
    }
}
