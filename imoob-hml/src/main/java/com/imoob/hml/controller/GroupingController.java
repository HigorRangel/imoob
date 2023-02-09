package com.imoob.hml.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.fge.jsonpatch.JsonPatch;
import com.imoob.hml.model.Grouping;
import com.imoob.hml.model.DTO.usuario.GroupingPermissionDTO;
import com.imoob.hml.service.GroupingService;
import com.imoob.hml.service.SystemActivityService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/groupings")
@RequiredArgsConstructor
public class GroupingController {

	private final SystemActivityService systemService;

	private final GroupingService groupingService;

	@PostMapping("/")
	public ResponseEntity<Grouping> insertGrouping(@RequestBody GroupingPermissionDTO groupingPermission,
			HttpServletRequest request) {
		Grouping grouping = groupingService.insertGrouping(groupingPermission);

		systemService.insertOk(request.getRequestURI(), grouping.getId(), request);

		return ResponseEntity.ok().body(grouping);
	}

	@PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
	public ResponseEntity<Grouping> patchUpdate(@PathVariable Long id, @RequestBody JsonPatch patch) {
		Grouping grouping = groupingService.patchUpdate(id, patch);
		return ResponseEntity.ok(grouping);
	}
}
