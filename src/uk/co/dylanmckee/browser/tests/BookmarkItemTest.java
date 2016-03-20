package uk.co.dylanmckee.browser.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.dylanmckee.browser.BookmarkItem;

import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * A simple unit test that instantiates a BookmarkItem - ensuring the constructor works - and then also test that the
 * toString method works properly, and that the model object is storing the correct URL.
 *
 * @author Dylan McKee
 *         Created 13/03/15
 */
public class BookmarkItemTest {

    /**
     * A constant String representation of the URL of the bookmark.
     */
    private static final String BOOKMARK_URL_STRING = "http://example.com";

    /**
     * A constant String containing the bookmark's title.
     */
    private static final String BOOKMARK_TITLE_STRING = "Test";

    /**
     * A private field containing the BookmarkItem we're testing, instantiated in the setUp() method,
     * to reduce code duplication.
     */
    private BookmarkItem testItem;

    /**
     * A method ran before every test to create a fresh BookmarkItem object, with the url of
     * http://example.com and the name of 'Test'
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Before
    public void setUp() throws Exception {
        // create a test object.
        testItem = new BookmarkItem(BOOKMARK_TITLE_STRING, new URL(BOOKMARK_URL_STRING));

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
        testItem = null;

    }

    /**
     * A method that tests that BookmarkItems generate a correct toString representation of themselves
     * (i.e. just their title in a String).
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testToString() throws Exception {
        // toString should return a string that's equal to the title ("Test").
        assertEquals(BOOKMARK_TITLE_STRING, testItem.toString());

    }

    /**
     * A method to test that the getUrl method of the BookmarkItem class returns the correct URL.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testGetUrl() throws Exception {
        // create our known test url object.
        URL testUrl = new URL(BOOKMARK_URL_STRING);

        // assert equalities.
        assertEquals(testUrl, testItem.getUrl());

    }
}