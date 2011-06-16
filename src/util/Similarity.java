//=============================================================================
// Coding sample for Stefaan Heyvaert.
//=============================================================================
package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import text.Tokenizer;

import client.TwitterUser;

/**
 * Calculates the overall similarity between two Twitter users by computing and
 * multiplying together the similarity between various pieces of information:
 * <l>
 * <li>- the Jaccard similarity coefficient between the users each of them is
 * following ("friends") </li>
 * <li>- the Jaccard similarity coefficient between the users that follow each of 
 * them ("followers") </li>
 * <li>- the text similarity between their biographies, using an approximation of
 * the cosine distance (see {@link #pseudoCosine(Map, Map)})</li>
 * <li>- the text similarity between the users they mention (using the same pseudo-
 * cosine distance)</li>
 * <li>- the text similarity between their favorite tweets</li>
 * <li>- the text similarity between their regular tweets</li>
 * </l>
 * * 
 * All these individual simlarity scores are given equal weight. Experiments or
 * better knowledge of Twitter should make it possible to assign each a different
 * weight and thus obtain perhaps a more reliable overall score.
 * 
 * @author sh
 */
public final class Similarity
{
    /**
     * Computes the similarity between two Twitter users according to the 
     * principles outlined in the class comment.
     * @param t1 first Twitter user instance
     * @param t2 second Twitter user instance
     * @param tokenizer tokenizer to analize the text fields collected for each
     * user.
     * @return {@link util.SimilarityResult} describing the similarity of the
     * two users
     */
    public static SimilarityResult computeSumilarity(final TwitterUser t1,
    		final TwitterUser t2, final Tokenizer tokenizer) {
        SimilarityResult result = new SimilarityResult();
        // Users are 100% similar to themselves
        if (t1.equals(t2)) {
            result.setScore(1.0);
            result.setReason("User ID values are identical");
        }
        else {
            // List similarities
            result.setSharedFollowers( 
                jaccardCoefficient(t1.getFollowerIds(), t2.getFollowerIds()));
            result.setFollowerCount(
                    t1.getFollowerIds().size(), t2.getFollowerIds().size());
            result.setSharedFriends(
                jaccardCoefficient(t1.getFriendIds(), t2.getFriendIds()));
            result.setFriendCount(
                    t1.getFriendIds().size(), t2.getFriendIds().size());
            
            // Text-basec similarities
            // 1. Descriptions ("bios")
            Map<String, Integer> bio1 = new HashMap<String, Integer>();
            Map<String, Integer> bio2 = new HashMap<String, Integer>();
            // Descriptions are simple string, so use regular tokenizer
            addAllToMap(tokenizer.tokenize(t1.getDescription()), bio1);
            addAllToMap(tokenizer.tokenize(t2.getDescription()), bio2);
            result.setBioSimilarity(pseudoCosine(bio1, bio2));
            // 2. Mentions
            result.setSharedMentions(
                pseudoCosine(t1.getMentions(), t2.getMentions()));
            // 3. Favorite tweets
            Map<String, Integer> favorites1 = new HashMap<String, Integer>();
            Map<String, Integer> favorites2 = new HashMap<String, Integer>();
            addAllToMap(tokenize(t1.getFavorites(), tokenizer), favorites1);
            addAllToMap(tokenize(t2.getFavorites(), tokenizer), favorites2);
            result.setFavoriteSimilarity(pseudoCosine(favorites1, favorites2));
            // 4. Regular tweets
            Map<String, Integer> tweets1 = new HashMap<String, Integer>();
            Map<String, Integer> tweets2 = new HashMap<String, Integer>();
            addAllToMap(tokenize(t1.getTweets(), tokenizer), tweets1);
            addAllToMap(tokenize(t2.getTweets(), tokenizer), tweets2);
            result.setTweetSimilarity(pseudoCosine(tweets1, tweets2));
            
            result.computeFinalScore();
        }
        return result;
    }
    
    /**
     * The Jaccard similarity coefficient is the size of the intersection divided
     * by the size of the union of two sets. If two sets have nothing in common,
     * the coefficient is 0; if the sets are identical, the coefficient is 1.
     * @param collA a collection of {@link java.lang.Comparable} instances
     * @param collB another such collection
     * @return a value between 0 and 1 indicating the similarity of the
     * collections passed in
     */
    public static <T> double jaccardCoefficient(
            Collection<T> collA, Collection<T> collB) {
        if (collA == null || collB == null || 
                collA.size() == 0 || collB.size() == 0) {
            return 0.0;
        }
        Set<Object> unionSet = new HashSet<Object>(); 
        int intersection = 0;
        for (Object obj : collA) {
            if (collB.contains(obj)) {
                intersection += 1;
            }
            unionSet.add(obj);
        }
        int union = unionSet.size();
        // Add remaining elements from the second collection to the union.
        for (Object obj : collB) {
            if (!unionSet.contains(obj)) {
                union += 1;
            }
        }
        return (double)(intersection / (double)union);
    }
    
    /**
     * Simulates the cosine metric used in vector-based information-retrieval
     * systems by taking into account the frequency of each element in the sets
     * passed in (as opposed to just their occurrence as in the method 
     * {@link #jaccardCoefficient(Collection, Collection)}).
     * @param mapA a map with strings as values and integers representing the
     * frequency of their corresponding string as values
     * @param mapB a second such map
     * @return a value between 0 and 1 indicating the similarity of the sets
     * represented by the maps passed in.
     */
    public static double pseudoCosine(Map<String, Integer> mapA,
            Map<String, Integer> mapB) {
        if (mapA == null || mapB == null || 
                mapA.size() == 0 || mapB.size() == 0) {
            return 0.0;
        }
        Map<String, Integer> unionMap = new HashMap<String, Integer>();
        int unionCount = 0;
        int intersection = 0;
        for (Map.Entry<String, Integer> entry : mapA.entrySet()) {
            if (mapB.containsKey(entry.getKey())) {
                Integer countB = mapB.get(entry.getKey());
                if (countB > entry.getValue()) {
                    unionMap.put(entry.getKey(), countB);
                    // Largest value is added to the union, smallest to the
                    // intersection
                    intersection += entry.getValue();
                    unionCount += countB;
                }
                else {
                    intersection += countB;
                    unionCount += entry.getValue();
                    unionMap.put(entry.getKey(), entry.getValue());
                }
            }
            else {
                unionCount += entry.getValue();
                unionMap.put(entry.getKey(), entry.getValue());
            }
        }
        // Add remaining elements from the second set to the union.
        for (String str : mapB.keySet()) {
            if (!unionMap.containsKey(str)) {
                unionCount += mapB.get(str);
            }
        }
        return (double)(intersection / (double)unionCount);
    }

    /**
     * Takes a collection of strings and increments the frequency of each item
     * in the list in the corresponding map of String/Integer pairs.
     * @param coll a colection of stringa
     * @param map a properly initialized map of String/Integer pairs
     */
    public static void addAllToMap(final Collection<String> coll, 
    		final Map<String, Integer> map) {
        if (coll == null || coll.size() == 0 || map == null) {
            return;
        }
        for (String m : coll) {
            int count = 1;
            Integer mentioned = map.get(m);
            if (mentioned != null) {
                count = mentioned.intValue() + 1;
            }
            map.put(m, count);
        }
    }
    
    /**
     * Utility method for tokenizing a list of strings rather than a single 
     * string.
     * @param textList a list of strings
     * @param tokenizer tokenizer to use on the strings
     * @return
     */
    private static Collection<String> tokenize(final Collection<String> textList,
            Tokenizer tokenizer) {
        List<String> tokList = new ArrayList<String>();
        for (String str : textList) {
            tokList.addAll(tokenizer.tokenize(str));
        }
        return tokList;
    }
    
    /**
     * Constructor is private, since the class contains static methods only and
     * does not need to be instantiated.
     */
    private Similarity() {}
}
