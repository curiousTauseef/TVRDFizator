package fr.eurecom.tvrdfizator.core.readers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.StringMap;
import com.google.gson.reflect.TypeToken;

import fr.eurecom.tvrdfizator.core.datastructures.NERDEntity;


public class EntityReader {

	
		private String file;
		
		public EntityReader(String f){
			file = f;
		}
		
		public  List<NERDEntity> read() throws FileNotFoundException{

			
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

			
			
			//GSON
			
			Gson gson = new Gson();
			Type listtype = new TypeToken<List<NERDEntity>>() {}.getType();
			List<NERDEntity> entities = gson.fromJson(bufferedReader,listtype);
			System.out.println("\t#entities:" + entities.size());
			
			
			
			return entities;
			
			
			
			
		}
		

}

