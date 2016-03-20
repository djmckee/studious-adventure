package uk.co.dylanmckee.browser.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.dylanmckee.browser.BookmarkItem;
import uk.co.dylanmckee.browser.BookmarkManager;

import java.net.URL;

import static org.junit.Assert.*;

/**
 * A unit test class that instantiates a BookmarkManager - ensuring the constructor works - and then also test that the
 * respective methods to add items, remove items, clear all items, and get items work properly.
 *
 * @author Dylan McKee
 *         Created 13/03/15
 */
public class BookmarkManagerTest {

    /**
     * A constant String representation of the URL of the bookmark.
     */
    private static final String BOOKMARK_URL_STRING = "http://example.com";

    /**
     * A constant String containing the bookmark's title.
     */
    private static final String BOOKMARK_TITLE_STRING = "Test";

    /**
     * A private field to hold the test instance of the BookmarkManager class, which is instantiated in the setUp()
     * method before each test and destroyed in the tearDown(), after each test. This saves on the code duplication that
     * would be involved if each test method were to create it's own local instance.
     */
    private BookmarkManager testManager;

    /**
     * A private field to hold an instance of BookmarkItem, which is created before each test in the setUp() method,
     * and destroyed after each test with the tearDown() method. Having a consistent BookmarkItem cuts down on code
     * duplication.
     */
    private BookmarkItem testItem;

    /**
     * A method that is called before each test. It instantiates the test BookmarkManager and test BookmarkItem objects.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Before
    public void setUp() throws Exception {
        // create our test object - testing the constructor.
        testManager = new BookmarkManager();

        // create the test bookmark
        testItem = new BookmarkItem(BOOKMARK_TITLE_STRING, new URL(BOOKMARK_URL_STRING));

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
        // destroy the test object.
        testManager = null;

        // and the test bookmark.
        testItem = null;

    }

    /**
     * A method to test HistoryManager's constructor, by ensuring that testManager is not null after construction.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testConstructor() throws Exception {
        // test the constructor by asserting that the BookmarkManager instance is not null.
        assertNotNull(testManager);

    }

    /**
     * A method to test that the a list of bookmarks is returned by the BookmarkManager class - by checking that the
     * returned value is not null.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testGetItemsList() throws Exception {
        // get the items list, ensure that it's not null.
        assertNotNull(testManager.getItemsList());

    }

    /**
     * A method to test that adding of BookmarkItems to the BookmarkManager works properly. It adds the item, then
     * ensures that it is present in the list of bookmarks returned by the testManager.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testAddItem() throws Exception {
        // add a bookmark, get the list, check that it's present.
        // (note: this test is dependant on the getItemsList() method working as expected)

        // add it.
        testManager.addItem(testItem);

        // get items and iterate through, making sure it's present - meaning it's been added properly.
        boolean added = false;
        for (BookmarkItem b : testManager.getItemsList()) {
            if (b.equals(testItem)) {
                added = true;
            }
        }

        // assert that it's been added...
        assertTrue(added);

    }

    /**
     * A method to test that clearing of all items works properly. An item gets added, then the clear all method gets
     * called, then the count of the bookmark list is checked to ensure that it is zero (as it should be after
     * the clearing of everything).
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testClearEntireList() throws Exception {
        // add a bookmark, clear all items, get the list, check that it's size is zero.
        // (note: this test is dependant on the addItem() and getItemsList() methods working as expected)

        // add it.
        testManager.addItem(testItem);

        // clear all bookmarks.
        testManager.clearEntireList();

        // assert that the size of the bookmark items list is zero...
        assertEquals(0, testManager.getItemsList().size());

    }

    /**
     * A method to test that removal of individual BookmarkItems works. A test item is added, then removed, then the
     * list of items is iterated to see if the test item is still there. If not, it has been removed successfully.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testRemoveItem() throws Exception {
        // add a bookmark, remove it, and then check that it's not within the bookmarks list.
        // (note: this test is dependant on the addItem() and getItemsList() methods working as expected)

        // add it.
        testManager.addItem(testItem);

        // remove it.
        testManager.removeItem(testItem);

        // get items and iterate through, making sure it's not there (ensuring that it has been removed properly)
        boolean itemIsPresent = false;

        // iterate the list using fast enumeration
        for (BookmarkItem b : testManager.getItemsList()) {
            // check if the current item is the desired testItem...
            if (b.equals(testItem)) {
                // if so, mark the 'item is present' boolean as true.
                itemIsPresent = true;
            }
        }

        // assert that it's not present in the list.
        assertFalse(itemIsPresent);

    }

    /**
     * A method to test that removal of a BookmarkItem that isn't already in the list. This is an edge case test, which
     * should just be ignored silently, but it's worth testing just to ensure no 'ugly' undesired behaviour occurs, such
     * as exceptions being thrown.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testRemoveNonexistentItem() throws Exception {
        // remove the testItem (without adding it! meaning it does *not* exist already in the bookmarks list)
        testManager.removeItem(testItem);

        // there's no assertion in this test - it merely needs to fail silently to pass the test.

    }

    /**
     * An edge case test to ensure that clearing an already empty list does not cause an exception to be thrown, or
     * any other bad behaviour that is undesired to occur.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testClearEmptyList() throws Exception {
        // the BookmarkManager list will already be empty on instantiation, let's try to clear it...
        testManager.clearEntireList();

        // there's no assertion in this test - it merely needs to fail silently to pass the test.

    }


}