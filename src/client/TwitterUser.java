//=============================================================================
// Coding sample for Stefaan Heyvaert.
//=============================================================================
package client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.Similarity;

/**
 * A wrapper around useful data retrieved about a user through the
 * {@link winterwell.jtwitter.Twitter} API.
 * 
 * Uses Java's default serialization mechanism to be able to write and read
 * objects during testing.
 * 
 * @author sh
 */
public class TwitterUser implements Comparable<TwitterUser>, Serializable
{
    /**
     * Note that this is the only way to set the user's login name.
     * Use {@link #setScreenName(String)} and {@link #getScreenName()} for the 
     * user's display name
     * @param loginName user's login
     */
    public TwitterUser(String loginName) {
        userName = loginName;
    }

    /**
     * Test if the current user follows another one.
     * @param other
     * @return true if this user follows the other; false otherwise
     */
    public boolean follows(TwitterUser other) {
        return other.getFollowerIds().contains(id);
    }
    
    /**
     * Test if the current user is followed by another one.
     * @param other
     * @return true if this user is followed by the other; false otherwise
     */
    public boolean isFollowedBy(TwitterUser other) {
        return followers_.contains(other.getId());
    }
    
    public String getUserName() {
        return userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the user's display name (not user's login)
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * @param screenName the user's display name (not user's login)
     */
    public void setScreenName(final String screenName) {
        this.screenName = screenName;
    }

    public String getDescription() {
        return bio;
    }

    public void setDescription(final String description) {
        bio = description;
    }

    public Map<String, Integer> getMentions() {
        return mentions_;
    }
    
    public Collection<String> getTweets() {
        return tweets_;
    }
    
    public Collection<String> getFavorites() {
        return favorites_;
    }

    public Collection<Long> getFollowerIds() {
        return followers_;
    }
    
    public Collection<Long> getFriendIds() {
        return friends_;
    }

    public void addTweet(final String tweet) {
        tweets_.add(tweet);
    }
    
    public void addTweets(final List<String> tweets) {
        tweets_.addAll(tweets);
    }
    
    public void addFavorite(final String favorite) {
        favorites_.add(favorite);
    }

    public void addFriends(final List<Long> friends) {
        friends_.addAll(friends);
    }

    public void addFollowers(final List<Long> followers) {
        followers_.addAll(followers);
    }

    public void addMentions(final List<String> mentions) {
        Similarity.addAllToMap(mentions, mentions_);
    }
    
    public String toString() {
        return id + "|" + userName + "|" + bio;
    }
    
    public boolean equals(final TwitterUser other) {
        return id == other.getId();
    }

    /**
     * Given that user ID values are assigned sequentially, this method imposes
     * a chronological order on users (which may or not be useful). 
     */
    @Override
    public int compareTo(final TwitterUser other) {
        return id < other.getId() ? -1 : id > other.getId() ? 1 : 0;
    }

    /** Tweets (status messages) sent by the current user */ 
    private final List<String> tweets_ = new ArrayList<String>();

    /** current user's favorite tweets */
    private final List<String> favorites_ = new ArrayList<String>();
    
    /** Users mentioned by the current user in one or more tweets */
    private final Map<String, Integer> mentions_ = new HashMap<String, Integer>();
    
    /** Users followed by th current user */
    private final Set<Long> friends_ = new HashSet<Long>();
    
    /** Users following th current user */
    private final Set<Long> followers_ = new HashSet<Long>();

    /** Required for Serializable */
    private static final long serialVersionUID = 1L;

    private String userName;
    private long id;
    private String screenName;
    private String bio;
}
