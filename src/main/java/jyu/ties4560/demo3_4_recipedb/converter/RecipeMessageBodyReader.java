package jyu.ties4560.demo3_4_recipedb.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import jyu.ties4560.demo3_4_recipedb.domain.Recipe;

/**
 * Not functional yet
 * @author aleks
 *
 */
//@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class RecipeMessageBodyReader implements MessageBodyReader<Recipe> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		System.out.println("isReadable called");
		// TODO Auto-generated method stub
		return Recipe.class.isAssignableFrom(type);
	}


	@Override
	public Recipe readFrom(Class<Recipe> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		
		System.out.println("readFrom called");
		System.out.println(new BufferedReader(new InputStreamReader(entityStream))
				  .lines().collect(Collectors.joining("\n")));
		// TODO Auto-generated method stub
		return null;
	}

}