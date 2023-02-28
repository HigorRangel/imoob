package com.imoob.hml.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum BrazillianState {
	@JsonProperty("name")
	AC("Acre", "AC"), @JsonProperty("name")
	AL("Alagoas", "AL"), @JsonProperty("name")
	AP("Amapá", "AP"), @JsonProperty("name")
	AM("Amazonas", "AM"), @JsonProperty("name")
	BA("Bahia", "BA"), @JsonProperty("name")
	CE("Ceará", "CE"), @JsonProperty("name")
	DF("Distrito Federal", "DF"), @JsonProperty("name")
	ES("Espírito Santo", "ES"), @JsonProperty("name")
	GO("Goiás", "GO"), @JsonProperty("name")
	MA("Maranhão", "MA"), @JsonProperty("name")
	MT("Mato Grosso", "MT"), @JsonProperty("name")
	MS("Mato Grosso do Sul", "MS"), @JsonProperty("name")
	MG("Minas Gerais", "MG"), @JsonProperty("name")
	PA("Pará", "PA"), @JsonProperty("name")
	PB("Paraíba", "PB"), @JsonProperty("name")
	PR("Paraná", "PR"), @JsonProperty("name")
	PE("Pernambuco", "PE"), @JsonProperty("name")
	PI("Piauí", "PI"), @JsonProperty("name")
	RJ("Rio de Janeiro", "RJ"), @JsonProperty("name")
	RN("Rio Grande do Norte", "RN"), @JsonProperty("name")
	RS("Rio Grande do Sul", "RS"), @JsonProperty("name")
	RO("Rondônia", "RO"), @JsonProperty("name")
	RR("Roraima", "RR"), @JsonProperty("name")
	SC("Santa Catarina", "SC"), @JsonProperty("name")
	SP("São Paulo", "SP"), @JsonProperty("name")
	SE("Sergipe", "SE"), @JsonProperty("name")
	TO("Tocantins", "TO");

	private final String name;
	private final String acronym;

	BrazillianState(String name, String acronym) {
		this.name = name;
		this.acronym = acronym;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("acronym")
	public String getAcronym() {
		return acronym;
	}

	public static BrazillianState valueOfAcronym(String acronym) {
		for (BrazillianState state : BrazillianState.values()) {
			if (state.acronym.equalsIgnoreCase(acronym)) {
				return state;
			}
		}
		throw new IllegalArgumentException("Nenhum estado encontrado para a acronym informada: " + acronym);
	}

	public static BrazillianState valueOfName(String name) {
		for (BrazillianState state : BrazillianState.values()) {
			if (state.name.equalsIgnoreCase(name)) {
				return state;
			}
		}
		throw new IllegalArgumentException("Nenhum estado encontrado para o name informado: " + name);
	}
}
