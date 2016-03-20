package uk.co.dylanmckee.browser.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.dylanmckee.browser.HistoryItem;

import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * A simple unit test that instantiates a HistoryItem - ensuring the constructor works - and then also test that the
 * toString method works properly, and that the model object is storing the correct URL.
 *
 * @author Dylan McKee
 *         Created 13/03/15
 */
public class HistoryItemTest {

    /**
     * A constant String representation of the URL of the history item.
     */
    private static final String HISTORY_ITEM_URL_STRING = "http://example.com";

    /**
     * A private field containing the HistoryItem we're testing, instantiated in the setUp() method,
     * to reduce code duplication.
     */
    private HistoryItem testObject;

    /**
     * A method ran before every test to create a fresh HistoryItem object, with the url of http://example.com
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Before
    public void setUp() throws Exception {
        // create our test object - testing the constructor.
        testObject = new HistoryItem(new URL(HISTORY_ITEM_URL_STRING));
    }

    /**
     * A method ran after every test to destroy the 'test item' used, allowing for new instances of it to be safely
     * created by the next call of the setUp() method.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @After
    public void tearDown() throws Exception {
        // destroy the test object.
        testObject = null;
    }

    /**
     * A method that tests that the HistoryItem class can generate a correct toString representation,
     * (i.e. the visited date/time, as a human readable formatted string, and then the item's URL, as a string -
     * i.e. "dd/mm/yyyy - http://url.com")
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testToString() throws Exception {
        // the HistoryItem should return a toString with a formatted date, then the URL in it
        // separated by spaces and a hyphen (i.e. "dd/mm/yyyy - http://url.com")

        // formulate our test toString...
        String testString = String.format("%s - %s", testObject.getCreationDateString(), testObject.getUrl().toString());

        // assert equality...
        assertEquals(testString, testObject.toString());
    }

    /**
     * A method to test that the getUrl method of the HistoryItem class returns the correct URL.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testGetUrl() throws Exception {
        // create our known test url object.
        URL testUrl = new URL(HISTORY_ITEM_URL_STRING);

        // assert equalities.
        assertEquals(testUrl, testObject.getUrl());
    }

}