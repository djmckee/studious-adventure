package uk.co.dylanmckee.browser;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * A class that manages user preferences within the Browser application - such as home page URL -
 * so as to provide a good level of abstraction between views and data storage.
 * <p/>
 * This class uses only static methods, so instances never need to exist - to cut down on the amount of code
 * required to retrieve information from settings and keep things as simple and uncluttered as possible.
 * <p/>
 * In the MVC design pattern, this class acts as a model class, with the prime aim of holding user data.
 *
 * @author Dylan McKee
 *         Created 07/03/15
 */
// scope here has to be public so that it can be accessed from the tests package.
public class UserPreferences {

    /**
     * A constant static string that contains the configuration file name.
     * Private scope because it only needs to be accessed within this class.
     */
    private final static String CONFIGURATION_FILE_NAME = "configuration.txt";

    /**
     * A default home page URL that will be returned when the user has yet to set a custom one.
     */
    private final static String DEFAULT_HOMEPAGE_URL = "http://www.ncl.ac.uk/computing/";

    /**
     * A constant string containing the error message text to display when the preferences 'config' file cannot be closed.
     */
    private final static String FILE_CLOSE_ERROR_TEXT = "Something went wrong. Please re-launch Browser to ensure stability.";

    /**
     * A constant string containing the error message text to display when the preferences 'config' file cannot be saved.
     */
    private final static String FILE_SAVE_ERROR_TEXT = "Sorry, your homepage could not be saved at this time. Please check disk space and file permissions before trying again";


    /**
     * A convenience method that gets a suitable file path, and returns it as a string, so that the methods in this class know consistently where to save/read homepage preference data from.
     * Private scope because it is only for methods in this class to use.
     *
     * @return a string containing the path to the configuration file.
     */
    private static String configurationFilePath() {
        // get the file path
        File configFile = new File(CONFIGURATION_FILE_NAME);

        // now return the path to that file...
        return configFile.getAbsolutePath();
    }

    /**
     * Gets the users current home page URL by reading it from the configuration file.
     *
     * @return Returns the current home page URL, as a URL object.
     */
    public static URL getHomepageUrl() {
        // create our file reference.
        FileReader file;

        //using a try/catch statement in case for example the config file doesn't yet exist.
        try {
            //open the configuration file, getting its path from the relevant private method within this class...
            file = new FileReader(configurationFilePath());

            //create a scanner to parse the file with.
            Scanner fileScanner = new Scanner(file);

            //home page URL will always be the first line...
            String urlString = fileScanner.nextLine();

            //now try to make a URL out of it...
            URL homepageUrl = new URL(urlString);

            try {
                // close the file
                file.close();
            } catch (IOException e) {
                // Leaving the file open would be dangerous and could cause writing issues later on. We should get the user to re-launch the app.
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, FILE_CLOSE_ERROR_TEXT);
            }

            // and close the scanner too...
            fileScanner.close();

            // return the homepageUrl!
            // (no defensive copying needed - homepageUrl is not a field).
            return homepageUrl;

        } catch (FileNotFoundException e) {
            // No stored home page! Allow the program to carry on and return the default.

        } catch (MalformedURLException e1) {
            // For some reason their home page URL wasn't valid. Allow the program to carry on and return the default.

        }

        //A URL hasn't yet been returned.
        //Either they didn't have a homepage set, or something went horribly wrong.
        //In either case, let's try to return the default...
        try {
            // Create a URL from the default URL string constant, and return it.
            return new URL(DEFAULT_HOMEPAGE_URL);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            // Something has gone horribly wrong. Return null to escape.
            // This code should never ever be reachable, because the DEFAULT_HOMEPAGE_URL constant will ALWAYS be a valid URL.
            return null;
        }
    }

    /**
     * Save the new home page URL to the configuration file.
     *
     * @param homepageUrl The user's new home page URL.
     * @throws NullPointerException if the homepageUrl parameter is null, then an exception is thrown.
     */
    public static void setHomepageUrl(URL homepageUrl) {
        // do some simple null value checking...
        if (homepageUrl == null) {
            // cannot save a null... let's throw a NullPointerException
            throw new NullPointerException("setHomepageUrl was passed a null value parameter.");
        }

        // use of the PrintWriter method requires a try/catch statement, even though the file should always just get written.
        // there could however be permissions issues in writing the file, theoretically.
        try {
            // create a PrintWriter to write into our config file...
            PrintWriter fileWriter = new PrintWriter(configurationFilePath());

            // convert the URL to a string
            String urlString = homepageUrl.toString();

            // write the urlString.
            fileWriter.println(urlString);

            // close stream to complete writing.
            fileWriter.close();

        } catch (Exception e) {
            // being quite general in this catch clause because it could be a plethora of issues.
            // show an error to the user informing them to check permissions and free space.
            JOptionPane.showMessageDialog(null, FILE_SAVE_ERROR_TEXT);
        }
    }

}
