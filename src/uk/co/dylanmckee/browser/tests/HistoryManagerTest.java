package uk.co.dylanmckee.browser.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.dylanmckee.browser.HistoryItem;
import uk.co.dylanmckee.browser.HistoryManager;

import java.net.URL;

import static org.junit.Assert.*;

/**
 * A unit test class that instantiates a HistoryManager - ensuring the constructor works - and then also tests that the
 * respective methods to add items, remove items, clear all history, and get history items work properly.
 *
 * @author Dylan McKee
 *         Created 13/03/15
 */

public class HistoryManagerTest {

    /**
     * A constant String representation of the URL of the history item.
     */
    private static final String HISTORY_ITEM_URL_STRING = "http://example.com";


    /**
     * A private field to hold the test instance of the HistoryManager class, which is instantiated in the setUp()
     * method before each test and destroyed in the tearDown(), after each test. This saves on the code duplication that
     * would be involved if each test method were to create it's own local instance.
     */
    private HistoryManager historyManager;

    /**
     * A private field to hold an instance of HistoryItem, which is created before each test in the setUp() method,
     * and destroyed after each test with the tearDown() method. Having a consistent HistoryItem cuts down on code
     * duplication.
     */
    private HistoryItem historyItem;

    /**
     * A method that is called before each test. It instantiates the test HistoryManager and test HistoryItem objects.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Before
    public void setUp() throws Exception {
        // construct our HistoryManager, testing that the constructor works...
        historyManager = new HistoryManager();

        // and construct a piece of history to test with - this cuts down on code duplication vs. creating this
        // during every test method.
        historyItem = new HistoryItem(new URL(HISTORY_ITEM_URL_STRING));

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
        // destroy the manager
        historyManager = null;

        // and erase a piece of history.
        historyItem = null;

    }

    /**
     * A method to test HistoryManager's constructor, by ensuring that historyManager is not null after construction.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testConstructor() throws Exception {
        // test the constructor by asserting that the HistoryManager is not null.
        assertNotNull(historyManager);

    }

    /**
     * A method to test that the a list of history items is returned by the HistoryManager class - by checking that the
     * returned value is not null.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testGetItemsList() throws Exception {
        // get the history list, ensure that it's not null.
        assertNotNull(historyManager.getItemsList());

    }

    /**
     * A method to test that adding of HistoryItem to the HistoryManager works properly. It adds the item, then
     * ensures that it is present in the list of HistoryItems returned by the historyManager.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testAddItem() throws Exception {
        // add the history item, get the list, check that it's present.
        // (note: this test is dependant on the getItemsList() method working as expected)

        // add it.
        historyManager.addItem(historyItem);

        // get items and iterate through, making sure it's present - meaning it's been added properly.
        boolean added = false;

        // iterate the list using fast enumeration
        for (HistoryItem history : historyManager.getItemsList()) {
            // check if the current item is the desired historyItem test object...
            if (history.equals(historyItem)) {
                // if so, mark the 'item is present' boolean as true.
                added = true;
            }
        }

        // assert that it's been added...
        assertTrue(added);

    }

    /**
     * A method to test that clearing of all items works properly. An item gets added, then the clear all method gets
     * called, then the count of the history items list is checked to ensure that it is zero (as it should be after
     * the clearing of everything).
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testClearEntireList() throws Exception {
        // add the history test item, clear all items, get the list, check that it's size is zero.
        // (note: this test is dependant on the addItem() and getItemsList() methods working as expected)

        // add it.
        historyManager.addItem(historyItem);

        // clear all history.
        historyManager.clearEntireList();

        // assert that the size of the history list is zero...
        assertEquals(0, historyManager.getItemsList().size());

    }

    /**
     * A method to test that removal of individual HistoryItems works. A test item is added, then removed, then the
     * list of items is iterated to see if the test item is still there. If not, it has been removed successfully.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testRemoveItem() throws Exception {
        // add the test history item, remove it, and then check that it's not within the list of history.
        // (note: this test is dependant on the addItem() and getItemsList() methods working as expected)

        // add it.
        historyManager.addItem(historyItem);

        // remove it.
        historyManager.removeItem(historyItem);

        // get items and iterate through, making sure it's not there (ensuring that it has been removed properly)
        boolean itemIsPresent = false;
        for (HistoryItem h : historyManager.getItemsList()) {
            if (h.equals(historyItem)) {
                itemIsPresent = true;
            }
        }

        // assert that it's not present in the list.
        assertFalse(itemIsPresent);

    }

    /**
     * A method to test that removal of a HistoryItem that isn't already in the list. This is an edge case test, which
     * should just be ignored silently, but it's worth testing just to ensure no 'ugly' undesired behaviour occurs, such
     * as exceptions being thrown.
     *
     * @throws Exception an exception that is not expected to be thrown, but if thrown, will cause the method to be
     *                   marked as 'failed' in the JUnit tests panel, indicating the user to an evident issue in the test.
     */
    @Test
    public void testRemoveNonexistentItem() throws Exception {
        // remove the historyItem (without adding it! meaning it does *not* exist already in the list of history)
        historyManager.removeItem(historyItem);

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
        // the history manager list will already be empty on instantiation, let's try to clear it...
        historyManager.clearEntireList();

        // there's no assertion in this test - it merely needs to fail silently to pass the test.

    }


}