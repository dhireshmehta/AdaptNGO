package org.adapt.domain;

public enum TypeEnum{
	DOCUMENT("DOCUMENT"),VIDEO("VIDEO"), AUDIO("AUDIO");
	
	private String name;
	
	private TypeEnum(String nameval) {
		name = nameval;
		
	}
	public static TypeEnum getEnumbyString(String name) {
		name= name.toUpperCase();
		switch(name) {
			case "DOCUMENT":
				return DOCUMENT;
			case "VIDEO":
				return VIDEO;
			case "AUDIO":
				return VIDEO;
			default:
				return DOCUMENT;
		}
	}
}