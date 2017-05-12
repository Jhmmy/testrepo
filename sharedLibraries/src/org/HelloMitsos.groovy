package org.dzlitidis

class HelloMitsos {
	
	private final String name;
	
	public HelloMitsos(String name){
		this.name = name;
	}
	
	public String returnHello(){
		return "Hello" + name;
	}
}
