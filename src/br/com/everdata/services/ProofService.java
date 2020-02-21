package br.com.everdata.services;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.http.client.ClientProtocolException;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.payload.InstagramComment;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetMediaInfoResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;
import org.springframework.stereotype.Service;

import br.com.everdata.controllers.Request;
import br.com.everdata.instagram.InstagramActions;
import br.com.everdata.instagram.utils.KeyUtils;

@Service
public class ProofService {

	public List<InstagramComment> getComments(Request req) {
		Instagram4j instagram = null;
		try {
			instagram = authenticate(req);
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<InstagramComment>();
		}
		return InstagramActions.getComments(instagram, req.getPostId());
	}

	public InstagramGetMediaInfoResult getMediaInfo(Request req) {
		Instagram4j instagram = null;
		try {
			instagram = authenticate(req);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return InstagramActions.getMediaInfo(instagram, req.getPostId());
	}

	public List<InstagramUserSummary> getMediaLikers(Request req) {
		Instagram4j instagram = null;
		try {
			instagram = authenticate(req);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return InstagramActions.getMediaLikers(instagram, req.getPostId());
	}

	private static Instagram4j authenticate(Request req) throws ClientProtocolException, IOException {		
		Instagram4j instagram = null;		
		try {
			String login = KeyUtils.decrypt(req.getLogin());
			String password = KeyUtils.decrypt(req.getPassword());
			instagram = Instagram4j.builder().username(login).password(password).build();
			instagram.setup();
			instagram.login();
		} catch (IOException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException | InvalidKeySpecException e) {
			e.printStackTrace();
		} 		
		return instagram;
	}

}
