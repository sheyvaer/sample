package text;

import java.util.ArrayList;
import java.util.List;


/**
 * Extends the general tokenizer by adding {@code @} and {@code #} as valid word 
 * characters because of their special meaning in Twitter text strings.
 
 * @author sh
 */
public class TwitterTokenizer extends Tokenizer
{
    public TwitterTokenizer(final Stopwords stopwords) {
        super(stopwords);
    }
    
    /**
     * Tokenizes the text with the specialized {@link #isWordCharacter(Character)}
     * method, then glues the special chacartes back onto the following word. 
     */
    public List<String> tokenize(final String text) {
        List<String> tokens = super.tokenize(text);
        for (int i = 0; i < tokens.size(); i++) {
            if (specialCharacters.contains(tokens.get(i).charAt(0)) && 
                    i < tokens.size() - 1) {
                tokens.set(i, new String(tokens.get(i) + tokens.remove(i + 1)));
            }
        }
        return tokens;
    }
    /**
     * Adds {@code @} and {@code #} as valid word characters.
     */
    @Override
    public boolean isWordCharacter(final Character ch) {
        return super.isWordCharacter(ch) || specialCharacters.contains(ch);
    }
    
    // Characters that deserve special attention
    private static List<Character> specialCharacters = new ArrayList<Character>();
    static {
        specialCharacters.add('@');
        specialCharacters.add('#');
    }
}
