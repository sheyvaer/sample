package util;

import static org.junit.Assert.*;
import org.junit.Test;

import text.Stopwords;
import text.Tokenizer;
import text.TwitterTokenizer;

import client.TwitterUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestSimilarity 
{
    @Test
    public void testJaccardCoefficient() {
        Collection<String> listA = new ArrayList<String>();
        Collection<String> listB = new ArrayList<String>();
        assertEquals(0.0, Similarity.jaccardCoefficient(listA, listB), 0.01);
        listA.add("ABC");
        listB.add("ABC");
        assertEquals(1.0, Similarity.jaccardCoefficient(listA, listB), 0.01);
        listA.add("DEF");
        assertEquals(0.5, Similarity.jaccardCoefficient(listA, listB), 0.01);
        listB.add("GHI");
        assertEquals(0.333, Similarity.jaccardCoefficient(listA, listB), 0.01);
        listA.remove("ABC");
        assertEquals(0.0, Similarity.jaccardCoefficient(listA, listB), 0.01);
    }

    @Test
    public void testPseudoCosine() {
        Map<String, Integer> mapA = new HashMap<String, Integer>();
        Map<String, Integer> mapB = new HashMap<String, Integer>();
        assertEquals(0.0, Similarity.pseudoCosine(mapA, mapB), 0.01);
        mapA.put("ABC", 1);
        mapB.put("ABC", 1);
        assertEquals(1.0, Similarity.pseudoCosine(mapA, mapB), 0.01);
        mapA.put("ABC", 2);
        assertEquals(0.5, Similarity.pseudoCosine(mapA, mapB), 0.01);
        mapB.put("DEF", 2);
        assertEquals(0.25, Similarity.pseudoCosine(mapA, mapB), 0.01);
    }
    
    @Test
    public void testUserSimilarity() throws Exception {
        Stopwords stopwords = new Stopwords("data/stopwords.txt");
        Tokenizer tokenizer = new TwitterTokenizer(stopwords);
        TwitterUser t1 = new TwitterUser("abc");
        TwitterUser t2 = new TwitterUser("def");
        t1.setId(123);
        t1.setId(456);
        SimilarityResult r = Similarity.computeSumilarity(t1, t2, tokenizer);
        assertEquals(0.0, r.getScore(), 0.01);
        List<Long> t1Followers = new ArrayList<Long>();
        t1Followers.add(1L);
        t1.addFollowers(t1Followers);
        List<Long> t2Followers = new ArrayList<Long>();
        t2Followers.add(1L);
        t2.addFollowers(t2Followers);
        SimilarityResult r2 = Similarity.computeSumilarity(t1, t2, tokenizer);
        assertEquals(1.0, r2.getScore(), 0.01);
        t1Followers.add(2L);
        t1.addFollowers(t1Followers);
        SimilarityResult r3 = Similarity.computeSumilarity(t1, t2, tokenizer);
        assertEquals(0.5, r3.getScore(), 0.01);
        t1.setDescription("abc def ghi");
        SimilarityResult r4 = Similarity.computeSumilarity(t1, t2, tokenizer);
        assertEquals(0.5, r4.getScore(), 0.01);
        t2.setDescription("abc def ghi abc");
        SimilarityResult r5 = Similarity.computeSumilarity(t1, t2, tokenizer);
        assertEquals(0.375, r5.getScore(), 0.01);
    }
}
