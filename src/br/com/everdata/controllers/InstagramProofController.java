package br.com.everdata.controllers;

import java.util.List;

import org.brunocvcunha.instagram4j.requests.payload.InstagramComment;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetMediaInfoResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.everdata.services.ProofService;

@RestController
@RequestMapping("/api/proof")
public class InstagramProofController {
	
	@Autowired
	private ProofService service;
	
	@GetMapping("/comments/")
	public ResponseEntity<List<InstagramComment>> getComments(@RequestBody Request r) {
		return new ResponseEntity<List<InstagramComment>>(service.getComments(r), HttpStatus.OK);
	}
	
	@GetMapping("/media/")
	public ResponseEntity<InstagramGetMediaInfoResult> getMediaInfo(@RequestBody Request r) {
		return new ResponseEntity<InstagramGetMediaInfoResult>(service.getMediaInfo(r), HttpStatus.OK);
	}
	
	@GetMapping("/likers/")
	public ResponseEntity<List<InstagramUserSummary>> getMediaLikers(@RequestBody Request r) {
		return new ResponseEntity<List<InstagramUserSummary>>(service.getMediaLikers(r), HttpStatus.OK);
	}

}
