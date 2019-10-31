import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;

import org.apache.http.client.ClientProtocolException;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

public class Main {

	private static Map<String, InstagramUserSummary> requestSent = new HashMap<String, InstagramUserSummary>();
	private static Queue<String> targetAccounts = new LinkedList<String>();
	
	private static void readTargetAccounts(String path) {
		BufferedReader targetReader;
		try {
			targetReader = new BufferedReader(new FileReader(path));
			String line = targetReader.readLine();
			while (line != null) {
				targetAccounts.offer(line);
				System.out.println("Target account: "+line);
				line = targetReader.readLine();
			}
			targetReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int drawNumber(int start, int end) {
		Random rand = new Random();
		return rand.nextInt(end-start)+start;
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {

		FileReader reader = new FileReader(args[0]);//"config.properties"
		Properties p = new Properties();
		p.load(reader);

		Instagram4j instagram = authenticate(p.getProperty("login"), p.getProperty("senha"));
		readTargetAccounts(args[1]);	//"target.txt"
	
		InstagramSearchUsernameResult userResult = InstagramActions.getUserHandle(instagram, p.getProperty("login"));	
		
		while(true) {									
			double percent = drawNumber(1,3);
			double numFollowers = InstagramActions.mapFollowers(instagram, userResult).size();
			int limit = (int) (percent/100.0 * numFollowers);
			System.out.println("######### Starting a new following round! "+limit+" accounts will be requested!\n");
			String target = targetAccounts.poll();
			InstagramActions.followFollowersOf(instagram, target, requestSent, limit);
			targetAccounts.offer(target);
			
			int hoursToSleep = drawNumber(10,12);
			int minutesToSleep = drawNumber(0,60);
			System.out.println("######### Will sleep for "+hoursToSleep+" hours and "+minutesToSleep+" minutes!\n");
			try {
				Thread.sleep((hoursToSleep*1000*60*60) + (minutesToSleep*1000*60));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("######### Starting a new unfollowing round!\n");			
			percent = drawNumber(1,3);
			Map<String, InstagramUserSummary> unfollowers = InstagramActions.mapUnfollowers(instagram, userResult);
			double numUnfollowers = unfollowers.size();
			limit = (int) (percent/100.0 * numUnfollowers);
			InstagramActions.unfollow(instagram, unfollowers, limit);
			
			hoursToSleep = drawNumber(0,5);
			minutesToSleep = drawNumber(0,60);
			System.out.println("######### Will sleep for "+hoursToSleep+" hours and "+minutesToSleep+" minutes!\n");
			try {
				Thread.sleep((hoursToSleep*1000*60*60) + (minutesToSleep*1000*60));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

	}

	private static Instagram4j authenticate(String login, String password) throws ClientProtocolException, IOException {
		Instagram4j instagram = Instagram4j.builder().username(login).password(password).build();
		instagram.setup();
		instagram.login();
		return instagram;
	}
}
