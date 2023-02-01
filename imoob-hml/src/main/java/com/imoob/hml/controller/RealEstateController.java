package com.imoob.hml.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.fge.jsonpatch.JsonPatch;
import com.imoob.hml.model.RealEstate;
import com.imoob.hml.service.RealEstateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/realestate")
@RequiredArgsConstructor
public class RealEstateController {

	private final RealEstateService realEstateService;

	@GetMapping
	public List<RealEstate> findAll(Pageable pageable) {
		return realEstateService.findAll(pageable);
	}

	@GetMapping("/{id}")
	public RealEstate findById(@PathVariable Long id) {
		return realEstateService.findById(id);
	}

	@PostMapping
	public RealEstate create(@RequestBody RealEstate realEstate) {
		return realEstateService.save(realEstate);
	}

	@PutMapping("/{id}")
	public RealEstate update(@PathVariable Long id, @RequestBody RealEstate realEstate) {
		realEstate.setId(id);
		return realEstateService.save(realEstate);
	}

	@PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
	public ResponseEntity<RealEstate> patchUpdate(@PathVariable Long id, @RequestBody JsonPatch patch) {
		RealEstate realEstate = realEstateService.patchUpdate(id, patch);
		return ResponseEntity.ok(realEstate);
	}
//	@PatchMapping("/{id}")
//	public RealEstate updateFields(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
//		RealEstate realEstate = realEstateService.findById(id);
//		fields.forEach((k, v) -> {
//			Field field = ReflectionUtils.findField(RealEstate.class, k);
//			field.setAccessible(true);
//			ReflectionUtils.setField(field, realEstate, v);
//		});
//		return realEstateService.save(realEstate);
//	}
}
