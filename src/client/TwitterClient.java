//=============================================================================
// Coding sample for Stefaan Heyvaert.
//=============================================================================
package client;

import java.util.ArrayList;
import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import winterwell.jtwitter.Twitter.User;
import winterwell.jtwitter.Twitter.Status;

/**
 * Uses the {@link winterwell.jtwitter.Twitter} API to create a client to 
 * communicate with the Twitter web service, gather information about users, and
 * return {@link client.TwitterUser} instances containing the information most 
 * useful for similarity computations.
 * <p>
 * Requires a valid Twitter user name and password to be instantiated.
 *
 * @author sh
 */
public final class TwitterClient
{
    /**
     * Constructs a {@link winterwell.jtwitter.Twitter} API instance.
     * @param username a valid user name registered with twitter.com
     * @param password password for the given user name
     * @throws Exception if something went wrong, most likely because the login
     * name or password was wrong.
     */
    public TwitterClient(final String username, final String password) 
            throws Exception {
        try {
            twitter = new Twitter(username, password);
        }
        catch(TwitterException t) {
            throw new Exception(t.getMessage());
        }
    }
    
    /**
     * Collects information for a user given a login name.
     * @param userName
     * @return wrapper containing a selection of the information available
     * about a user
     * @throws Exception if a user with the given login name does not exist or
     * something else went wrong either on the client side (unauthorized request,
     * overstaying one's welcome) or the server (twitter.com) side.
     */
    public TwitterUser createUser(final String userName) throws Exception {
        if (callsLeft() < 0) {
            throw new Exception("Rate limit exceeded for the current hour.");
        }
        User user = null;
        try {
            user = twitter.getUser(userName);
        } 
        catch(TwitterException e) {
            String message;
            Throwable cause = e.getCause();
            if (cause instanceof TwitterException.RateLimit) {
                message = "Request Went over twitter's limit: please wait 1 hour and try again";
            } else if (cause instanceof TwitterException.E401) {
                message = "Wrong password or missing authorization";
            } else if (cause instanceof TwitterException.E403) {
                message = "Unauthorized request";
            } else if (cause instanceof TwitterException.E50X) {
                message = "Twitter server error. Please try again later";
            } else if (cause instanceof TwitterException.E404) {
                message = "User '" + userName + "' does not exist.";
            } else {
                message = "Unknown exception";
            }
            throw new Exception(message);
        }

        // Create the user object and set some basic information
        TwitterUser tu = new TwitterUser(userName);
        tu.setId(user.getId());
        tu.setScreenName(user.getScreenName());
        tu.setDescription(user.getDescription());
        
        // Collect the user's available tweets
        List<Status> statuses = twitter.getUserTimeline(userName);
        for (Status status : statuses) {
            tu.addMentions(status.getMentions());
            tu.addTweet(status.getText());
        }
        
        // Collect user's friends and followers
        tu.addFollowers(twitter.getFollowerIDs(userName));
        tu.addFriends(twitter.getFriendIDs(userName));
        
        // Collect the user's favorite tweets
        for (Status status : twitter.getFavorites(userName)) {
            tu.addFavorite(status.getText());
        }
        return tu;
    }

    /**
     * Returns the 10 most discussed topics on Twitter at the time of the call. 
     * @return a list of topics
     */
    public List<String> getTrends() {
        return twitter.getTrends();
    }
    
    /**
     * @return the 20 most recent tweets from non-protected users
     */
    public List<String> getPublicTimeLine() {
        List<String> tweets = new ArrayList<String>();
        for (Status status : twitter.getPublicTimeline()) {
            tweets.add(status.getText());
        }
        return tweets;
    }
    
    private int callsLeft() {
        return twitter.getRateLimitStatus();
    }

    /** The {@link winterwell.jtwitter.Twitter;} API to be used throughout. */
    private final Twitter twitter;
}
