import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.http.client.ClientProtocolException;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramFollowRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowersRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowingRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUnfollowRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

public class InstagramActions {

	

	public static Map<String, InstagramUserSummary> mapFollowers(Instagram4j instagram,
			InstagramSearchUsernameResult userResult) throws ClientProtocolException, IOException {
		Map<String, InstagramUserSummary> followersMap = new HashMap<String, InstagramUserSummary>();
		String nextMaxId = null;
		while (true) {
			InstagramGetUserFollowersResult followers = instagram
					.sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk(), nextMaxId));
			for (InstagramUserSummary follower : followers.getUsers())
				followersMap.put(follower.getUsername(), follower);
			nextMaxId = followers.getNext_max_id();
			if (nextMaxId == null) {
				break;
			}
		}
		return followersMap;
	}

//	private static Map<String, InstagramUserSummary> mapFollowers(Instagram4j instagram, InstagramSearchUsernameResult userResult) throws ClientProtocolException, IOException{
//		InstagramGetUserFollowersResult followers = instagram.sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk()));
//		List<InstagramUserSummary> followersUsers = followers.getUsers();
//		Map<String, InstagramUserSummary> followersMap = new HashMap<String, InstagramUserSummary>();
//		for(InstagramUserSummary user : followersUsers)
//			followersMap.put(user.getUsername(), user);
//		return followersMap;		
//	}

	public static Map<String, InstagramUserSummary> mapFollowings(Instagram4j instagram,
			InstagramSearchUsernameResult userResult) throws ClientProtocolException, IOException {
		Map<String, InstagramUserSummary> followingsMap = new HashMap<String, InstagramUserSummary>();
		String nextMaxId = null;
		while (true) {
			InstagramGetUserFollowersResult followings = instagram
					.sendRequest(new InstagramGetUserFollowingRequest(userResult.getUser().getPk(), nextMaxId));
			for (InstagramUserSummary following : followings.getUsers())
				followingsMap.put(following.getUsername(), following);
			nextMaxId = followings.getNext_max_id();
			if (nextMaxId == null) {
				break;
			}
		}
		return followingsMap;
	}

	public static Map<String, InstagramUserSummary> mapUnfollowers(Instagram4j instagram,
			InstagramSearchUsernameResult userResult) throws ClientProtocolException, IOException {
		Map<String, InstagramUserSummary> followers = mapFollowers(instagram, userResult);
		Map<String, InstagramUserSummary> followings = mapFollowings(instagram, userResult);
		Map<String, InstagramUserSummary> unfollowers = new HashMap<String, InstagramUserSummary>();

		Iterator<Entry<String, InstagramUserSummary>> followingsIt = followings.entrySet().iterator();
		while (followingsIt.hasNext()) {
			Entry<String, InstagramUserSummary> entry = followingsIt.next();
			if (!followers.containsKey(entry.getKey()))
				unfollowers.put(entry.getKey(), entry.getValue());
		}
		return unfollowers;
	}
	
	public static void unfollow(Instagram4j instagram, Map<String, InstagramUserSummary> toBeUnfollowed, int limitOfUsers) {
		//instagram.sendRequest(new InstagramUnfollowRequest(userResult.getUser().getPk()));
		Iterator<Entry<String, InstagramUserSummary>> itUsers = toBeUnfollowed.entrySet().iterator();
		int unfollowedCounter = 0;
		Random rand = new Random();
		while (itUsers.hasNext() && unfollowedCounter < limitOfUsers) {
			Entry<String, InstagramUserSummary> entry = itUsers.next();
			try {
				instagram.sendRequest(new InstagramUnfollowRequest(entry.getValue().getPk()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("######### "+instagram.getUsername() + " just unfollowed " + entry.getKey());
			unfollowedCounter++;
			int n = rand.nextInt(1000);
			try {
				System.out.println("######### Sleeping for "+n+" seconds!");
				Thread.sleep(n * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}						
		}
	}

	public static void followFollowersOf(Instagram4j instagram, String igAccount, 
			Map<String, InstagramUserSummary> followed, int limitOfUsers) {
		
		System.out.println("######### Started to follow the followers of "+igAccount+"!\n");
		
		int followedCounter = 0;
		InstagramSearchUsernameResult userResult = null;
		Map<String, InstagramUserSummary> hisFollowers = null;
		Map<String, InstagramUserSummary> myFollowings = null;
		try {
			userResult = getUserHandle(instagram, igAccount);
			hisFollowers = mapFollowers(instagram, userResult);
			myFollowings = mapFollowings(instagram, getUserHandle(instagram, instagram.getUsername()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Random rand = new Random();
		Iterator<Entry<String, InstagramUserSummary>> itUsers = hisFollowers.entrySet().iterator();
		while (itUsers.hasNext() && followedCounter < limitOfUsers) {
			Entry<String, InstagramUserSummary> entry = itUsers.next();
			
			if(entry.getValue().isHas_anonymous_profile_picture()) {
				System.out.println("######### "+entry.getKey() + " has anonymous picture! We wont send follow request!");
				continue;
			}			
			if (!myFollowings.containsKey(entry.getKey()) && 
					!entry.getValue().isHas_anonymous_profile_picture() &&
					!followed.containsKey(entry.getKey())) {
				try {
					instagram.sendRequest(new InstagramFollowRequest(entry.getValue().getPk()));
					followed.put(entry.getKey(),entry.getValue());
					followedCounter++;
					System.out.println("######### "+instagram.getUsername() + " just sent a follow request to " + entry.getKey());
					int n = rand.nextInt(1000);
					System.out.println("######### Sleeping for "+n+" seconds!");
					Thread.sleep(n * 1000);

				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("######### "+instagram.getUsername() + 
						" already follows (or had already sent a request to) " + 
						entry.getKey());
			}
		}
	}
	
	public static InstagramSearchUsernameResult getUserHandle(Instagram4j instagram, String igAccount)
			throws ClientProtocolException, IOException {
		InstagramSearchUsernameResult userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(igAccount));
		return userResult;
	}

}
