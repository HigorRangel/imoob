//package com.imoob.hml.model.pk;
//
//import java.io.Serializable;
//
//import com.imoob.hml.model.Grouping;
//import com.imoob.hml.model.Permission;
//
//import jakarta.persistence.Embeddable;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Embeddable
//@AllArgsConstructor
//@NoArgsConstructor
//@EqualsAndHashCode
//@Getter
//@Setter
//@Builder	
//public class GroupingPermissionPK implements Serializable{
//	 
//	private static final long serialVersionUID = 1L;
//	
//	@ManyToOne
//	@JoinColumn(name = "grouping_id")
//	private Grouping grouping;
//	
//	@ManyToOne
//	@JoinColumn(name = "permission_id")
//	private Permission permission;
//
//}
