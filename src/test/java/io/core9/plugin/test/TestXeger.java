package io.core9.plugin.test;

import org.junit.Test;

import nl.flotsam.xeger.Xeger;

public class TestXeger {

	@Test
	public void testXeger(){
		String regex = "[ab]{4,6}c";
		Xeger generator = new Xeger(regex);
		String result = generator.generate();
		System.out.println(result);
		assert result.matches(regex);
	}
	
}
