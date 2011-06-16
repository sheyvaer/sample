package util;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import text.Stopwords;
import text.TwitterTokenizer;

public class TestTokenizer
{
    @Test
    public void testTokenize() {
        Stopwords stopwords = new Stopwords();
        stopwords.add("this");
        stopwords.add("that");
        stopwords.add("other");
        
        TwitterTokenizer tokenizer = new TwitterTokenizer(stopwords);
        List<String> tokList = 
            tokenizer.tokenize("It was neither this, that, nor any other thing.");
        assertEquals(6, tokList.size());
        assertEquals("it", tokList.get(0));
        assertEquals("thing", tokList.get(5));
        assertFalse(tokList.contains("other"));
    }

    @Test
    public void testTokenizeNoStop() {
        TwitterTokenizer tokenizer = new TwitterTokenizer(null);
        List<String> tokList = 
            tokenizer.tokenize("It was neither this, that, nor any other thing.");
        assertEquals(9, tokList.size());
        assertEquals("it", tokList.get(0));
        assertEquals("thing", tokList.get(8));
        assertEquals(7, tokList.indexOf("other"));
    }
}
