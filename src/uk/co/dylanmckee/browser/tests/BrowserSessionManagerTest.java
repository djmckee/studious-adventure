package uk.co.dylanmckee.browser.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.dylanmckee.browser.BrowserSessionManager;

import java.net.URL;

import static org.junit.Assert.*;

/**
 * A unit test class that instantiates a BrowserSessionManager - ensuring the constructor works - and then also test that the
 * respective methods to navigate backwards and forwards properly - along with some edge cases.
 *
 * @author Dylan McKee
 *         Created 28/04/15
 */
public class BrowserSessionManagerTest {

    /**
     * A constant String representation of the URL to go back from.
     */
    private static final String BACK_URL_STRING = "http://example.com";

    /**
     * A constant String representation of the URL to go forwards to.
     */
    private static final String FORWARD_URL_STRING = "http://ncl.ac.uk/computing";

    /**
     * A private field containing the BrowserSessionManager we're testing, instantiated in the setUp() method,
     * to reduce code duplication.
     */
    private BrowserSessionManager testManager;

    /**
     * A method that is called before each test. It instantiates the test BrowserSessionManager.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Before
    public void setUp() throws Exception {
        // create the test object
        testManager = new BrowserSessionManager();

    }

    /**
     * A method called after each test, it destroys the test fields so that they can be freshly re-instantiated
     * by the setUp() method called before the next test with new 'blank' objects.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @After
    public void tearDown() throws Exception {
        // destroy testManager object
        testManager = null;
    }

    /**
     * A method to test BrowserSessionManager's constructor, by ensuring that testManager is not null after construction.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testConstructor() throws Exception {
        // test the constructor by asserting that the BrowserSessionManager instance is not null.
        assertNotNull(testManager);

    }

    /**
     * A method to test that BrowserSessionManager's hasBackItems method returns false for a blank session
     * with no back items.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testEmptyHasBackItems() throws Exception {
        // ensure it returns false for a blank instance...
        assertFalse("Empty browser session manager does not return false for hasBackItems!", testManager.hasBackItems());
    }

    /**
     * A method to test that BrowserSessionManager's hasBackItems method works, by
     * adding an item to the manager instance, and ensuring it returns true.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testHasBackItems() throws Exception {
        // add a test URL item...
        testManager.addBackUrl(new URL(BACK_URL_STRING));

        // ensure it now returns true (because a back item has been added)
        assertTrue("Loaded browser session manager does not return true for hasBackItems!", testManager.hasBackItems());

    }

    /**
     * A method to test that BrowserSessionManager's hasForwardItems method returns false for a blank session
     * with no forward items.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testEmptyHasForwardItems() throws Exception {
        // ensure it returns false for a blank instance...
        assertFalse("Empty browser session manager does not return false for hasForwardItems!", testManager.hasForwardItems());
    }

    /**
     * A method to test that BrowserSessionManager's hasForwardItems method returns true for a session where URL items
     * exist in the forward stack.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testHasForwardItems() throws Exception {
        // add a test URL item to go back to.
        testManager.addBackUrl(new URL(BACK_URL_STRING));

        // go back from the current URL (making the current URL a forward item)
        testManager.nextBackUrl(new URL(FORWARD_URL_STRING));

        // ensure it now returns true (because a back item has been added)
        assertTrue("Loaded browser session manager does not return true for hasForwardItems!", testManager.hasForwardItems());

    }

    /**
     * A method to ensure that the addBackUrl method works without throwing exceptions.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testAddBackUrl() throws Exception {
        // add a back item to ensure no exceptions are thrown (no asserts necessary here).
        testManager.addBackUrl(new URL(BACK_URL_STRING));
    }

    /**
     * A method to test the edge case where a null URL item is added, ensuring the add method does not
     * throw exceptions.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testAddNullBackUrl() throws Exception {
        // add null as a back item, ensuring our test does not throw exceptions... (should fail silently and not add anything)
        testManager.addBackUrl(null);

        // ensure nothing got added...
        assertFalse(testManager.hasBackItems());
    }

    /**
     * A method to test that the nextBackUrl method works properly, and that the correct back URL is indeed returned.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testNextBackUrl() throws Exception {
        // add a test URL item to go back to.
        testManager.addBackUrl(new URL(BACK_URL_STRING));

        // go back from the current URL (making the current URL a forward item)
        URL backUrl = testManager.nextBackUrl(new URL(FORWARD_URL_STRING));

        // check that the backUrl we've got returned is equal to that we added...
        assertEquals(new URL(BACK_URL_STRING), backUrl);
    }

    /**
     * An edge case test to ensure null is returned without issue when nextBackUrl is called on a browser session
     * where no back URLs exist.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testBlankNextBackUrl() throws Exception {
        // testManger is already blank, so go ahead and try..
        URL backUrl = testManager.nextBackUrl(new URL(BACK_URL_STRING));

        // check that backUrl is indeed null...
        assertNull(backUrl);

    }

    /**
     * A method to test that the nextForwardUrl method works properly, and that the correct forward URL is indeed returned.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testNextForwardUrl() throws Exception {
        // add a test URL item to go forwards to (eventually)...
        testManager.addBackUrl(new URL(FORWARD_URL_STRING));

        // add a test URL item to go back to.
        testManager.addBackUrl(new URL(BACK_URL_STRING));

        // go back from the current URL
        testManager.nextBackUrl(new URL(FORWARD_URL_STRING));

        // ensure that the initial URL item that we added is the next forwards URL...
        URL forwardsUrl = testManager.nextForwardUrl(new URL(FORWARD_URL_STRING));

        // check that the forwardsUrl we've got returned is equal to that we added initially...
        assertEquals(new URL(FORWARD_URL_STRING), forwardsUrl);
    }

    /**
     * An edge case test to ensure null is returned without issue when nextForwardUrl is called on a browser session
     * where no forward URLs exist.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testBlankNextForwardUrl() throws Exception {
        // testManger is already blank, so go ahead and try..
        URL forwardsUrl = testManager.nextForwardUrl(new URL(FORWARD_URL_STRING));

        // check that forwardsUrl is indeed null...
        assertNull(forwardsUrl);

    }

    /**
     * A method to test that the clearForwardsUrls method works, clearing the forward stack.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testClearForwardsUrls() throws Exception {
        // add a test URL item to go back to.
        testManager.addBackUrl(new URL(BACK_URL_STRING));

        // go back from the current URL (making the current URL a forward item)
        testManager.nextBackUrl(new URL(FORWARD_URL_STRING));

        // clear forwards URLs (since FORWARD_URL_STRING will be in there now)
        testManager.clearForwardsUrls();

        // check that there's no forward items left after the clear...
        assertFalse(testManager.hasForwardItems());

    }

    /**
     * An edge case test to ensure a blank forwards URL stack can be cleared without issue.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testClearBlankForwardsUrls() throws Exception {
        // initial testManger is already blank - try clearing...
        testManager.clearForwardsUrls();

        // no URLs should exist (and no exceptions should occur)...
        assertFalse(testManager.hasForwardItems());
    }

    /**
     * A method to test that the clearSessionHistory method works, clearing both the forward and the back stacks.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testClearSessionHistory() throws Exception {
        // add a test URL item to go back to.
        testManager.addBackUrl(new URL(FORWARD_URL_STRING));

        // add another test URL item to go back to.
        testManager.addBackUrl(new URL(BACK_URL_STRING));

        // go back from the current URL (making the current URL a forward item)
        testManager.nextBackUrl(new URL(FORWARD_URL_STRING));

        // clear both forwards and back URLs...
        testManager.clearSessionHistory();

        // is there any URLs left after a session history clear?
        boolean urlsExist = (testManager.hasBackItems() || testManager.hasForwardItems());

        // no URLs should still exist...
        assertFalse(urlsExist);
    }

    /**
     * An edge case test to ensure a blank session history can be cleared without issue.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testClearBlankSessionHistory() throws Exception {
        // initial testManger is already blank - try clearing...
        testManager.clearSessionHistory();

        // ensure it's clear...
        boolean urlsExist = (testManager.hasBackItems() || testManager.hasForwardItems());

        // no URLs should still exist...
        assertFalse(urlsExist);
    }
}