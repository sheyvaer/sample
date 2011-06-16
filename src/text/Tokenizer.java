package text;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;


/**
 * General-purpose tokenizer using a {@link java.text.BreakIterator} word
 * instance. More specialized tokenizers can extend the current class and then 
 * override the {@link #isWordCharacter(Character)} method.
 * 
 * @author sh
 */
public class Tokenizer 
{
    /**
     * Creates a Tokenizer that will remove stopwords from the input text. Pass
     * in {@code null} if no words should be removed from the input. 
     * @param stopwords
     */
    public Tokenizer(final Stopwords stopwords) {
        if (stopwords != null) {
            this.stopwords = stopwords;
        }
    }
    
    /**
     * Removes punctuation attached to the left or right of each word in the
     * input, as well as stopwords (if defined by the constructor).
     * @param text
     * @return a list of words with any attached punctuation removed
     */
    public List<String> tokenize(final String text) {
        List<String> tokens = new ArrayList<String>();
        if (text == null) {
            return tokens;
        }
        BreakIterator boundary = BreakIterator.getWordInstance();
        boundary.setText(text);
        int start = boundary.first();
        for (int end = boundary.next(); 
                end != BreakIterator.DONE;
                start = end, end = boundary.next()) {
            if (isWordCharacter(text.charAt(start))) {
                String word = 
                    new String(text.substring(start, end).toLowerCase()).intern();
                if (stopwords != null && stopwords.isStopword(word)) {
                    continue;
                }
                tokens.add(word);
            }
        }
        return tokens;
    }
    
    /**
     * Determine if the character passed in is can legitimately start or end a
     * word. Override this method to specialize the behavior of this class.
     * @param ch
     * @return true if the character can start or end a word; false otherwise.
     */
    public boolean isWordCharacter(Character ch) {
        return Character.isLetter(ch) || Character.isDigit(ch);
    }
    
    private Stopwords stopwords = null;
}
