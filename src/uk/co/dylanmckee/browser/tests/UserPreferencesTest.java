package uk.co.dylanmckee.browser.tests;

import org.junit.Test;
import uk.co.dylanmckee.browser.UserPreferences;

import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * A simple unit test to test the two methods of the UserPreferences class by saving and then retrieving a known URL.
 *
 * @author Dylan McKee
 *         Created 07/03/15
 */
public class UserPreferencesTest {

    /**
     * A constant String representation of a URL to be used during the test.
     */
    private static final String URL_STRING = "http://www.example.com";

    /**
     * Test the homepage saving and retrieving methods by saving in a test homepage, getting the homepage, and ensuring
     * that the retrieved value is equal to that one saved.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testSetThenGetHomepageUrl() throws Exception {
        // create a known URL to set.
        URL testUrl = new URL(URL_STRING);

        // try setting it.
        UserPreferences.setHomepageUrl(testUrl);

        // now try getting it.
        URL savedUrl = UserPreferences.getHomepageUrl();

        // check for equality between the two - assert that they're equal.
        assertEquals(savedUrl, testUrl);

    }

}