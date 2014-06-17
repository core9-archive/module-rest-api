package io.core9.plugin.test;

import io.core9.plugin.rest.PojoMapper;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.wordnik.swagger.sample.exception.NotFoundException;
import com.wordnik.swagger.sample.resource.PetResource;

public class TestPetResource {

	//@Test
	public void test() throws NotFoundException, JsonMappingException,
			JsonGenerationException, IOException {

		PetResource petResource = new PetResource();

		Response pet = petResource.getPetById("1");

		System.out.println(pet.getStatus());

		Object entity = pet.getEntity();

		String pojoAsString = PojoMapper.toJson(entity, true);

		System.out.println(pojoAsString);

	}

	//@Test
	public void printMethodAnnotations() {

		Method[] methods = PetResource.class.getMethods();

		for (Method method : methods) {
			System.out.println("\n");
			System.out.println(method);
			Annotation[] annotations = method.getAnnotations();
			for (Annotation annotation : annotations) {
				System.out.println(annotation);
			}

		}

	}

	//@Test
	public void invokeMethod() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, JsonMappingException,
			JsonGenerationException, IOException {

		PetResource petResource = new PetResource();
		Object method = null;

		method = petResource.getClass().getMethod("getPetById", String.class);

		Response pet = (Response) ((Method) method).invoke(petResource, "1");

		System.out.println(pet.getStatus());

		Object entity = pet.getEntity();

		String pojoAsString = PojoMapper.toJson(entity, true);

		System.out.println(pojoAsString);

	}

}
