//=============================================================================
// Coding sample for Stefaan Heyvaert.
//=============================================================================
package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.TwitterClient;
import client.TwitterUser;

import text.Stopwords;
import text.Tokenizer;
import text.TwitterTokenizer;
import util.Similarity;
import util.SimilarityResult;


/**
 * Interactive command-line driver for the similarity application. Must be
 * invoked with a valid Twitter login name and password on the command line.
 * The user will be prompted to enter two user names and, if all goes well, the
 * similarity between the users will be printed to standard out, with a summary
 * justofocation of the score.
 * <p>
 * Sample command line:
 * <br><code>
 *   java -cp "./lib/*;similarity.jar" app.Compare login password
 * </code><br>
 * <p>
 * Uses the {@link winterwell.jtwitter.Twitter} API for communicating with the
 * Twitter web service.
 * @author sh
 */
public class Compare 
{
    public static void main(final String[] args) {
        if (args.length < 2) {
            System.err.println("Program requires 2 arguments: screen_name password");
            System.exit(1);
        }
        initialize(args[0], args[1]);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(
            "\nThe programm will attempt to compute the similarity between two" +
            "\nTwitter users on a scale of zero (completely distinct) to 1" + 
            "\n(identical).\n");
        boolean play = true;
        String username = null;
        while (play) {
            try {
                TwitterUser t1 = null;
                TwitterUser t2 = null;
                // It is possible that one or both of the users does not exist
                // so keep trying until we have two valid names.
                while (t1 == null || t2 == null) {
                    try {
                        if (t1 == null) {
                            System.out.println(
                                "\nPlease enter the login name of the first user:\n");
                            username = br.readLine();
                            System.out.println("\nGetting information for " +
                                    username + ". Please wait...");
                            t1 = client.createUser(username);
                        }
                        if (t2 == null) {
                            System.out.println(
                                "\nPlease enter the login name of the second user:\n");
                            username = br.readLine();
                            System.out.println("\nGetting information for " +
                                    username + ". Please wait...");
                            t2 = client.createUser(username);
                            System.out.println("\nComputing similarity...\n");
                        }
                    }
                    catch(Exception e) {
                        System.out.println("ERROR: could not obtain information "
                                + "for user '" + username + "'\n'");
                        continue;
                    }
                }
                SimilarityResult result = 
                    Similarity.computeSumilarity(t1, t2, tokenizer);
                System.out.println("User bio for " + t1.getScreenName() +
                        "\n" + t1.getDescription());
                System.out.println("\nUser bio for " + t2.getScreenName() +
                        "\n" + t2.getDescription());
                System.out.println(result.toString());
                if (t1.follows(t2)) {
                    System.out.println(t1.getUserName() + " follows " 
                            + t2.getUserName());
                }
                if (t2.follows(t1)) {
                    System.out.println(t2.getUserName() + " follows " 
                            + t1.getUserName());
                }
                System.out.println("\n\nTry another pair? (Y/N)\n");
                String response = br.readLine();
                if (response.length() == 0 || response.matches("(N|n)")) {
                    play = false;
                }
                t1 = null;
                t2 = null;
            }
            catch (IOException e) {
                System.out.println("\nInput error. Exiting.");
                play = false;
            }
        }
        System.out.println("\nGood-bye.\n");
    }
    
    /**
     * Initializes the twitter client, stopword list, and tokenizer
     * @param username valid twitter login name
     * @param password password for the login name
     */
    private static void initialize(final String username, final String password) {
        try {
            stopwords = new Stopwords("data/stopwords.txt");
        } catch (Exception e) {
            System.err.println("WARNING: could not instantiate stopwords class.");
            System.err.println("WARNING: proceeding without stopwords.");
        }
        tokenizer = new TwitterTokenizer(stopwords);
        try {
            client = new TwitterClient(username, password);
        } catch (Exception e) {
            System.err.println("ERROR: could not instantiate twitter client: \n"
                    + e.getMessage());
            System.exit(1);
        }
    }
    
    // To tokenize the text contained in tweets and other strings associated 
    // with a twitter user 
    private static Tokenizer tokenizer;
    
    // Stop words to be used for the text-base similarity
    private static Stopwords stopwords = null;
    
    // Client for retrieving data from twitter.com (using the jtwitter API)
    private static TwitterClient client;
}
