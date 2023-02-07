package com.imoob.hml.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imoob.hml.model.Grouping;
import com.imoob.hml.model.DTO.usuario.GroupingPermissionDTO;
import com.imoob.hml.service.GroupingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/groupings")
@RequiredArgsConstructor
public class GroupingController {

	private final GroupingService groupingService;
	
	@PostMapping("/")
	public ResponseEntity<Grouping> insertGrouping(@RequestBody GroupingPermissionDTO groupingPermission){
		Grouping grouping = groupingService.insertGrouping(groupingPermission);;
		return ResponseEntity.ok().body(grouping);
	}
}
