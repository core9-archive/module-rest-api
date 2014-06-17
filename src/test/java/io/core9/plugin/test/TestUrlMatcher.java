package io.core9.plugin.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.junit.Test;

import com.sun.jersey.api.uri.UriTemplate;
import com.sun.jersey.api.uri.UriTemplateParser;

public class TestUrlMatcher {


	
	
	
	@Test
	public void testJerseyUriTemplates(){
		String path = "/foos/foo/bars/bar";

		Map<String, String> map = new HashMap<String, String>();
		UriTemplate template = new UriTemplate("/foos/{fooId}/bars/{barId}");
		if( template.match(path, map) ) {
		    System.out.println("Matched, " + map);
		} else {
		    System.out.println("Not matched, " + map);
		}  
	}

	@Test
	public void testJerseyUriPattern(){

		
		UriTemplateParser parser = new UriTemplateParser("/foos/{fooId : [a-z] }/bars/{barId}");
		
		Map<String, Pattern> tmp = parser.getNameToPattern();
		
		for(Entry<String, Pattern> item : tmp.entrySet()){
			System.out.println(item.getKey() + " : " + item.getValue());
		}
	}

	@Test
	public void testJerseyGetTemplateFromUriPattern(){

		
		UriTemplateParser parser = new UriTemplateParser("/foos/{fooId : [a-z] }/bars/{barId}");
		
		String template = parser.getNormalizedTemplate();
		
		System.out.println("normalized template : " + template);
	}


	
}