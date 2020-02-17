package br.com.everdata.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.payload.InstagramComment;
import org.springframework.stereotype.Service;

import br.com.everdata.instagram.InstagramActions;

@Service
public class ProofService {
	
	private static String login, password;
	
	public List<InstagramComment> getComments(String postId) {
		Instagram4j instagram = null;
		try {
			instagram = authenticate(ProofService.login, ProofService.password);
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<InstagramComment>();
		}
		
		return InstagramActions.getComments(instagram, postId);
	}
	
	private static Instagram4j authenticate(String login, String password) throws ClientProtocolException, IOException {
		Instagram4j instagram = Instagram4j.builder().username(login).password(password).build();
		instagram.setup();
		instagram.login();
		return instagram;
	}
	
	public static void setLogin(String login) {
		ProofService.login = login;
	}
	
	public static void setPassword(String password) {
		ProofService.password = password;
	}

}
