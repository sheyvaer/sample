package util;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import text.TwitterTokenizer;


public class TestTwitterTokeizer 
{
    @Test
    public void testTokenizeTweet() {
        TwitterTokenizer tokenizer = new TwitterTokenizer(null);
        String tweet = "At least I can get your humor through tweets. RT @abdur: I don't mean this in a bad way, but genetically speaking your a cul-de-sac.";
        List<String> tokList = tokenizer.tokenize(tweet);
        assertEquals(25, tokList.size());
        assertEquals("@abdur", tokList.get(10));
        assertEquals("cul-de-sac", tokList.get(24));
    }
}
