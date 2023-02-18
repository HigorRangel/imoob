package com.imoob.hml.model.DTO.realEstate;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.imoob.hml.model.RealEstate;
import com.imoob.hml.service.validators.RealEstateValue;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RealEstateDTO {
	private long id;
    private String corporateName;
    
    
	@NotNull
	private String tradingName;

	

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
	private Instant created;

	
	
	@NotNull
	@RealEstateValue
	private Character status;
    // Adicione outros campos relevantes aqui
	

    public RealEstateDTO(RealEstate realEstate) {
        this.id = realEstate.getId();
        this.corporateName = realEstate.getCorporateName();
        this.tradingName = realEstate.getTradingName();
        this.created = realEstate.getCreated();
        this.status =  realEstate.getStatus() != null ? realEstate.getStatus().getValue() : null;
        
        
        
        // Atribua outros campos relevantes aqui
    }
}
