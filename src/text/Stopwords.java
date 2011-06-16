package text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

/**
 * A stop word is a word that should not be used for indexing or similarity
 * calculations. The current class creates a stop word list from a file and 
 * provides the method {@link #isStopword(String)} to determine if a given word
 * is a stop word or not.
 * 
 * @author sh
 */
public class Stopwords 
{
    /**
     * Creates the instance from the contents of a file (one word per line)
     * @param filename
     * @throws Exception if an I/O Exception occurs while reading the file, or
     * if the file does not exists or is not readable.
     */
    public Stopwords(final String filename) throws Exception {
        stopwords = new HashSet<String>();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine()) != null) {
                stopwords.add(line.trim());
            }
        } catch (IOException e) {
            throw new Exception("Error reading stopword file " + filename, e);
        }
    }

    /**
     * Checks if the word passed in is a stop word or not.
     * @param word the word to check
     * @return true if the word is a stopword, false otherwise.
     */
    public boolean isStopword(final String word) {
        if (word == null || word.length() == 0) {
            return false;
        }
        return stopwords.contains(word.toLowerCase());
    }
    
    /**
     * No-arg constructor creates an empty list, which can then be added to by
     * the {@link #add(String)} method.
     */
    public Stopwords() {
        stopwords = new HashSet<String>();
    }
    
    /**
     * Adds a word to the stopword list.
     * @param stopword
     */
    public void add(final String stopword) {
        stopwords.add(stopword);
    }
    
    private HashSet<String> stopwords;
}
