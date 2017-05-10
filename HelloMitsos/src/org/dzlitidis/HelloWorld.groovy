package org.dzlitidis

public class HelloWorld {
	
	private final String name;
	
	public HelloWorld(String name){
		this.name = name;
	}
	
	public String returnHello(){
		return "Hello" + name;
	}
	
}
