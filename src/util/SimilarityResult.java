//=============================================================================
// Coding sample for Stefaan Heyvaert.
//=============================================================================
package util;

/**
 * Wrapper
 * @author sh
 */
public class SimilarityResult 
{
    public SimilarityResult() {}

    /**
     * Computes the final score by multiplying together all the non-zero
     * individual scores.
     */
    public void computeFinalScore() {
        StringBuilder sb = new StringBuilder("\n");
        boolean haveCommonality = false;
        double score = 1.0;
        if (sharedFriends > 0.0) {
            sb.append(String.format("%6.4f", sharedFriends) + 
                    " (similarity between friends [" + t1Friends +
                    " / " + t2Friends + "])\n");
            score *= sharedFriends;
            haveCommonality = true;
        }
        if (sharedFollowers > 0.0) {
            sb.append(String.format("%6.4f", sharedFollowers) +
                    " (similarity between followers [" + t1Followers +
                    " / " + t2Followers + "])\n");
            score *= sharedFollowers;
            haveCommonality = true;
        }
        if (sharedMentions > 0.0) {
            sb.append(String.format("%6.4f", sharedMentions) +
                    " (similarity between users/topics mentioned)\n");
            score *= sharedMentions;
            haveCommonality = true;
        }
        if (bioSimilarity > 0.0) {
            sb.append(String.format("%6.4f", bioSimilarity) +
                    " (similarity between biographies)");
            score *= bioSimilarity;
            haveCommonality = true;
        }
        
        if (favoriteSimilarity > 0.0) {
            sb.append(String.format("%6.4f", favoriteSimilarity) + 
                    " (similarity between favorite tweets)\n");
            score *= favoriteSimilarity;
            haveCommonality = true;
        }
        if (tweetSimilarity > 0.0) {
            sb.append(String.format("%6.4f", tweetSimilarity) + 
                    " (similarity between regular tweets)\n");
            score *= tweetSimilarity;
            haveCommonality = true;
        }
        if (haveCommonality) {
            setScore(score);
            reason = sb.toString();
        }
        else {
            reason = "Users have nothing in common.";
        }
    }

    public void setScore(final double score) {
        this.score = score;
    }
    
    public double getScore() {
        return score;
    }
    
    public void setReason(final String reason) {
        this.reason = reason;
    }
    
    public String toString() {
        return "Similarity score: " + String.format("%6.4f", score) 
                +  "\n" + reason;
    }
    
    public double getBioSimilarity() {
        return bioSimilarity;
    }
    
    public void setBioSimilarity(final double similarity) {
        this.bioSimilarity = similarity;
    }
    
    public double getSharedFollowers() {
        return sharedFollowers;
    }

    public void setSharedFollowers(final double sharedFollowers) {
        this.sharedFollowers = sharedFollowers;
    }

    public double getSharedFriends() {
        return sharedFriends;
    }

    public void setSharedFriends(final double sharedFriends) {
        this.sharedFriends = sharedFriends;
    }

    public double getSharedMentions() {
        return sharedMentions;
    }

    public void setSharedMentions(final double sharedMentions) {
        this.sharedMentions = sharedMentions;
    }

    public double getFavoriteSimilarity() {
        return favoriteSimilarity;
    }

    public void setFavoriteSimilarity(final double favoriteSimilarity) {
        this.favoriteSimilarity = favoriteSimilarity;
    }

    public double getTweetSimilarity() {
        return tweetSimilarity;
    }

    public void setTweetSimilarity(final double tweetSimilarity) {
        this.tweetSimilarity = tweetSimilarity;
    }
    
    public void setFollowerCount(final int count1, final int count2) {
        t1Followers = count1;
        t2Followers = count2;
    }
    
    public void setFriendCount(final int count1, final int count2) {
        t1Friends = count1;
        t2Friends = count2;
    }
    
    private double score = 0.0;
    private String reason;
    private double bioSimilarity;
    private double sharedFollowers; 
    private double sharedFriends; 
    private double sharedMentions;
    private double favoriteSimilarity;
    private double tweetSimilarity;
    private int t1Friends;
    private int t2Friends;
    private int t1Followers;
    private int t2Followers;
}
