package br.com.jdt.jdtspringboot.model.enums;

public enum SexoEnum {
	
	MASCULINO("MASC", "Masculino"),
	FEMININO("FEM", "Feminino");
	
	private String value;
	private String description;
	
	private SexoEnum(String value, String description) {
		this.value = value;
		this.description = description;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getDescription() {
		return description;
	}
	
}
