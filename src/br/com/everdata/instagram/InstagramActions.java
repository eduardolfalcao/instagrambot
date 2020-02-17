package br.com.everdata.instagram;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetMediaCommentsRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramComment;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetMediaCommentsResult;

import br.com.everdata.instagram.utils.SimpleGetRequest;

public class InstagramActions {	
	
	public static List<InstagramComment> getComments(Instagram4j instagram, String postId){
		
		List<InstagramComment> allComments = new ArrayList<InstagramComment>();
		String nextMaxId = null;
		
		do {
            InstagramGetMediaCommentsRequest request = new InstagramGetMediaCommentsRequest(String.valueOf(getMediaIdFromCode(instagram,postId)), nextMaxId);
            InstagramGetMediaCommentsResult commentsResult = null;
			try {
				commentsResult = instagram.sendRequest(request);
			} catch (IOException e) {
				e.printStackTrace();
			}

            List<InstagramComment> comments = commentsResult.getComments();
            allComments.addAll(comments);

            String lastComment = null;

            for (InstagramComment comment : comments) {
                    //  do something
                    if(lastComment == null) lastComment = String.valueOf(comment.getPk());
            }

            nextMaxId = commentsResult.getNext_max_id();
            if(nextMaxId != null) nextMaxId = lastComment;
        } while(nextMaxId != null);
		
		return allComments;
	}
	
	private static long getMediaIdFromCode(Instagram4j instagram, String postId) {
		
		String pagesource = null;
        try {
			pagesource = getURLSource(instagram, postId);
		} catch (IOException e) {
			e.printStackTrace();
		}
        if(pagesource.contains("media?id="))
        {
            int startpos ,endpos = 0;
            int strlength = "media?id=".length();
            startpos = pagesource.indexOf("media?id=");
            endpos = "media?id=".lastIndexOf('=');
            String result = "";
            startpos = startpos + strlength;
            char c = pagesource.charAt(startpos);
            while(c != '"')
            {
              result = result + c;
              startpos = startpos+1;
              c = pagesource.charAt(startpos);
            }
            return Long.parseLong(result);            
        }
        return -1;
	}
	
	private static String getURLSource(Instagram4j instagram, String postId) throws IOException{		
		return instagram.sendRequest(new SimpleGetRequest(postId));
    }

}