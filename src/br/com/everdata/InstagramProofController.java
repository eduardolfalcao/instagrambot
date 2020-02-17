package br.com.everdata;

import java.util.List;

import org.brunocvcunha.instagram4j.requests.payload.InstagramComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.everdata.services.ProofService;

@RestController
@RequestMapping("/api/proof")
public class InstagramProofController {
	
	@Autowired
	private ProofService service;
	
	@GetMapping("/comments/{postId}")
	public ResponseEntity<List<InstagramComment>> getComments(@PathVariable String postId) {
		return new ResponseEntity<List<InstagramComment>>(service.getComments(postId), HttpStatus.OK);
	}

}
