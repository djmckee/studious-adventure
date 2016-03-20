package uk.co.dylanmckee.browser;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * A class that creates and manages the main browser window, inheriting from JFrame, and managing UI layout,
 * listeners, etc.
 * <p/>
 * In the MVC design pattern, this class acts as a controller, linking together data from my data models (via their
 * respective manager classes, providing a good layer of abstraction) to the view in terms of the actual UI elements
 * that the user sees and interacts with.
 * <p/>
 * Unfortunately, due to lack of Swing automated testing tools, I was unable to provide unit tests for my view/
 * respective controller classes. However, I've provided 100% unit test coverage for all of my model classes and
 * their respective manager classes, so I feel that because the views are so obviously working from physical testing,
 * and my controller is simply relatively straightforward code that 'glues' together my model to my view classes
 * properly, I have not made any significant losses in test quality - and having full unit test coverage of my model
 * classes is extremely reassuring.
 *
 * @author Dylan McKee
 *         Created 07/03/15
 */
public class BrowserFrame extends JFrame implements BrowserToolbarListener, BrowserPaneListener, PersistedURLListFrameListener, PreferencesFrameListener {

    /**
     * A constant minimum width for the browser window, in pixels.
     */
    private static final int MINIMUM_WINDOW_WIDTH = 1200;

    /**
     * A constant minimum width for the browser height, in pixels.
     */
    private static final int MINIMUM_WINDOW_HEIGHT = 720;

    /**
     * A constant string that holds the name of the browser window, for display in the title bar.
     */
    private static final String BROWSER_WINDOW_TITLE = "Browser";

    /**
     * A constant string containing the text of the message that is displayed upon successful adding of a bookmark.
     */

    private static final String BOOKMARK_SUCCESS_MESSAGE_TEXT = "Bookmark added successfully.";

    /**
     * A constant string containing the textual prompt that asks the user to enter a bookmark title when adding a new
     * bookmark.
     */
    private static final String BOOKMARK_TITLE_ENTRY_MESSAGE = "Please enter a bookmark title:";

    /**
     * A constant string to be displayed to the user upon successful clearing of history data.
     */
    private final static String HISTORY_CLEARING_SUCCESS_MESSAGE_TEXT = "History cleared successfully";

    /**
     * A constant string to be displayed to the if an error occurs during clearing history data.
     */
    private final static String HISTORY_CLEARING_ERROR_MESSAGE_TEXT = "We're terribly sorry but your history data could not be cleared at this time. Please re-launch the browser and try again.";

    /**
     * A constant string to be displayed to the if an error occurs during instantiation of the history manager.
     */
    private final static String HISTORY_MANAGER_ERROR_TEXT = "A fatal error has occurred whilst opening browsing history. Please check your filesystem and try restarting the browser.";

    /**
     * A constant string to be displayed to the if an error occurs during instantiation of the bookmark manager.
     */
    private final static String BOOKMARK_MANAGER_ERROR_TEXT = "A fatal error has occurred whilst opening browser bookmarks. Please check your filesystem and try restarting the browser.";

    /**
     * A constant string to be displayed to the if an error occurs whilst adding a history item.
     */
    private final static String HISTORY_ITEM_ADDING_ERROR_TEXT = "Sorry, your history could not be saved. Please try restarting the browser.";

    /**
     * A constant string to be displayed to the if an error occurs whilst removing a history item.
     */
    private final static String HISTORY_ITEM_REMOVAL_ERROR_TEXT = "Sorry, history cannot be re-written that easily. Please try restarting the browser.";

    /**
     * A constant string to be displayed to the if an error occurs whilst adding a new bookmark.
     */
    private final static String BOOKMARK_ITEM_ADDING_ERROR_TEXT = "Sorry, the bookmark could not be added. Please try restarting the browser.";

    /**
     * A constant string to be displayed to the if an error occurs whilst removing a bookmark.
     */
    private final static String BOOKMARK_ITEM_REMOVAL_ERROR_TEXT = "Sorry, the bookmark could not be removed. Please try restarting the browser.";

    /**
     * The BrowserPane instance that will hold our web content, required as a field so that it has a sufficiently wide scope.
     */
    private BrowserPane browserPane;

    /**
     * The BrowserToolbar needs to be a field since class wide scope is required so that the URL TextField can be updated
     * programmatically when new URLs are visited - since they may not all be coming from users typing them in, but
     * also from other sources such as bookmarks being opened or links being clicked on.
     */
    private BrowserToolbar toolbar;

    /**
     * A private reference to the bookmark manager, to avoid concurrency issues in case multiple instances were created
     * and started writing to file at the same time.
     */
    private BookmarkManager bookmarkManager;

    /**
     * A private reference to the history manager, to avoid concurrency issues in case multiple instances were created
     * and started writing to file at the same time.
     */
    private HistoryManager historyManager;

    /**
     * A private reference to the BrowserSessionManager instance, so that the session has instance wide scope.
     */
    private BrowserSessionManager browserSessionManager;

    /**
     * A private reference to the preferences window where the home URL is set (an instance of PreferencesFrame).
     * This is here to prevent many preferences windows being opened by ridiculous users.
     */
    private PreferencesFrame preferencesFrame;

    /**
     * A private reference to the bookmarks window.
     * This is here to prevent many bookmarks windows being opened by ridiculous users.
     */
    private BookmarkListFrame bookmarksFrame;

    /**
     * A private reference to the history window.
     * This is here to prevent many bookmarks windows being opened by ridiculous users.
     */
    private HistoryListFrame historyFrame;

    /**
     * Constructs a new browser window, creating a JFrame and all of the necessary components within,
     * and laying them out.
     */
    public BrowserFrame() {
        // Create the browser window...

        // Initialise our history manager...
        try {
            historyManager = new HistoryManager();
        } catch (IOException e) {
            // we have no history manager - we cannot continue, because NullPointerExceptions will occur.
            // Programmatically close the frame and tell the user to check files then re-launch the program.
            setVisible(false);
            JOptionPane.showMessageDialog(this, HISTORY_MANAGER_ERROR_TEXT);
            return;
        }

        // and our bookmarks manager...
        try {
            bookmarkManager = new BookmarkManager();
        } catch (IOException e) {
            // we have no bookmarks manager - we cannot continue, because NullPointerExceptions will occur.
            // Programmatically close the frame and tell the user to check files then re-launch the program.
            setVisible(false);
            JOptionPane.showMessageDialog(this, BOOKMARK_MANAGER_ERROR_TEXT);
            return;
        }

        // Initialise the BrowserSessionManager so session based history can be tracked...
        browserSessionManager = new BrowserSessionManager();

        // The browser window requires a box layout manager, so the browser pane is arranged below the browser toolbar.
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Create an instance of the browser toolbar, passing this instance of this class as the 'listener'
        toolbar = new BrowserToolbar(this);

        // Add the toolbar to our window...
        add(toolbar);

        // Get the saved homepage URL from the UserPreferences convenience class, so we can pass it to our BrowserPane.
        URL userHomeUrl = UserPreferences.getHomepageUrl();

        // Create our browser pane
        browserPane = new BrowserPane(this);

        // Browse to our homepage...
        visitUrl(userHomeUrl);

        // Create a scroll pane to make our HTML document in the editor pane scrollable...
        JScrollPane scrollPane = new JScrollPane(browserPane);

        //and add the scroll pane to the window...
        add(scrollPane);

        // Give the window a title (from the constant string defined previously).
        setTitle(BROWSER_WINDOW_TITLE);

        // When the browser window's closed by the user, there's nothing left to do, quit the application.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Give the window a minimum size, using the constants defined previously in this class.
        setMinimumSize(new Dimension(MINIMUM_WINDOW_WIDTH, MINIMUM_WINDOW_HEIGHT));

        // Set the window's position relative to nothing, so it appears neatly in the center of the screen.
        setLocationRelativeTo(null);

        // Finally, pack the window, and make it visible on screen.
        pack();
        setVisible(true);
    }

    /**
     * A method to be called when the back button in the toolbar is clicked.
     */
    @Override
    public void backButtonClicked() {
        // firstly, check if there's items to go back to - if there's not, don't bother...
        if (!browserSessionManager.hasBackItems()) {
            // okay there's no pages to go back to, return to avoid any null pointer exceptions.
            return;
        }

        // okay, we're certain there's back URLs - retrieve the first one (passing in our current URL to the method so it gets added to the forward URLs)
        URL backUrl = browserSessionManager.nextBackUrl(browserPane.getPage());

        // load it up in the URL bar and browser pane...
        loadNewUrl(backUrl);
    }

    /**
     * A method to be called when the forward button in the toolbar is clicked.
     */
    @Override
    public void forwardButtonClicked() {
        // firstly, if there's nothing to go forwards to - don't bother...
        if (!browserSessionManager.hasForwardItems()) {
            // no forward URLs, return to avoid any null pointer exceptions.
            return;
        }

        // okay, we're certain there's forward URLs - retrieve the first one (passing in our current URL to the method
        // so it gets added to the back URLs)
        URL forwardsUrl = browserSessionManager.nextForwardUrl(browserPane.getPage());

        // load it up in the URL bar and browser pane...
        loadNewUrl(forwardsUrl);

    }

    /**
     * A method to be called when the bookmarks button in the toolbar is clicked.
     */
    @Override
    public void bookmarkButtonClicked() {
        // Get latest bookmark data
        List<BookmarkItem> bookmarks = bookmarkManager.getItemsList();

        // See if there's an existing instance of bookmarksFrame...
        if (bookmarksFrame == null) {
            //no instance yet - create one!
            bookmarksFrame = new BookmarkListFrame(bookmarks, this);
        }

        // re-load bookmark data from the BookmarkManager
        bookmarksFrame.setUrlList(bookmarks);

        // now make bookmarksFrame visible
        bookmarksFrame.setVisible(true);
    }

    /**
     * A method to be called when the history button in the toolbar is clicked.
     */
    @Override
    public void historyButtonClicked() {
        // Get the latest history data from HistoryManager....
        List<HistoryItem> historyList = historyManager.getItemsList();

        // See if there's an existing instance of historyFrame...
        if (historyFrame == null) {
            //no instance yet - create one!
            historyFrame = new HistoryListFrame(historyList, this);
        }

        // re-load history data from the HistoryManager
        historyFrame.setUrlList(historyList);

        // now make historyFrame visible
        historyFrame.setVisible(true);
    }

    /**
     * A method to be called when the refresh button in the toolbar is clicked.
     */
    @Override
    public void reloadButtonClicked() {
        // re-load the browser pane to it's current page (using its convenience reload method)
        browserPane.reloadPage();
    }

    /**
     * A method to be called when the settings button in the toolbar is clicked.
     */
    @Override
    public void settingsButtonClicked() {
        // See if there's an existing instance of preferencesFrame...
        if (preferencesFrame == null) {
            //no instance yet - create one!
            //passing ourselves in as the listener object.
            preferencesFrame = new PreferencesFrame(this);
        }

        // now make preferencesFrame visible
        preferencesFrame.setVisible(true);
    }

    /**
     * A method to be called when the home button in the toolbar is clicked.
     */
    @Override
    public void homeButtonClicked() {
        // go to the user's current homepage...
        // firstly, let's get their homepage...
        URL homeUrl = UserPreferences.getHomepageUrl();

        // now, let's visit it...
        visitUrl(homeUrl);

    }

    /**
     * A method to be called when the add bookmark button in the toolbar is clicked.
     */
    @Override
    public void addBookmarkButtonClicked() {
        // get the current URL...
        URL currentUrl = browserPane.getPage();

        // ask the user for a bookmark title using a suitable JOptionPane
        String title = JOptionPane.showInputDialog(this, BOOKMARK_TITLE_ENTRY_MESSAGE);

        // create a new BookmarkItem with the current URL and the title the user's entered
        BookmarkItem bookmarkItem = new BookmarkItem(title, currentUrl);

        // add the new bookmark to the bookmarkManager
        try {
            bookmarkManager.addItem(bookmarkItem);
        } catch (IOException e) {
            // bookmark item adding went horribly wrong - warn user in a JOptionPane
            JOptionPane.showMessageDialog(this, BOOKMARK_ITEM_ADDING_ERROR_TEXT);

        }

        // inform the user with a dialog.
        JOptionPane.showMessageDialog(this, BOOKMARK_SUCCESS_MESSAGE_TEXT);

    }

    /**
     * A method to be called when a URL has been entered into the URL field and either clicked the 'go' button, or pressed enter,
     * after it has been verified that it is indeed a genuine URL, not some 'mangled' rouge text.
     *
     * @param newUrl The new URL that the user has entered into the text field on the toolbar.
     *               Checked and verified by the java.net library to confirm that it has a valid URL structure.
     */
    @Override
    public void urlEntered(URL newUrl) {
        // go to the new URL that's been entered...
        visitUrl(newUrl);
    }


    /**
     * This listener method is called when a hyperlink is clicked within the BrowserPane browser.
     *
     * @param clickedLink A URL object containing the URL of the hyperlink that has been clicked.
     */
    @Override
    public void hyperlinkClicked(URL clickedLink) {
        // go to the hyperlink URL...
        visitUrl(clickedLink);
    }


    /**
     * A private convenience method that saves on code duplication. This method navigates the browserPane to
     * the new URL, and also sets the address bar content to the new URL too.
     * This is only invoked by either the user going to a URL manually through the address bar, or through
     * a link being clicked on, or through a history/favourites item being selected - it is NOT used when
     * the user uses the back/forward buttons, because it needs to clear the forwardStack and add current URL
     * (before the new one 'becomes' the current one) to the backStack so that the user can navigate back to it.
     *
     * @param newUrl A URL object containing the new URL to navigate to.
     */
    private void visitUrl(URL newUrl) {
        // clear any 'forwards' URLs - you cannot go forwards from something you've entered - human behaviour prediction just isn't that advanced (yet!)
        browserSessionManager.clearForwardsUrls();

        // get the current URL from the pane before the URL change...
        URL currentUrl = browserPane.getPage();


        // If the current URL is equal to the new one, then just re-load the page and return...
        // (we still want to do the 'forward' clearing above, because the user has deliberately chosen to navigate to the URL,
        //  but we don't want to add the same URL to the history again - it'll result in many redundant duplicates)
        if (newUrl.equals(currentUrl)) {
            // URLs are identical - just refresh the page.
            browserPane.reloadPage();

            // now return, we don't want duplication of the URL in history.
            return;
        }

        // Add it to the browser session history...
        browserSessionManager.addBackUrl(currentUrl);

        // we want to add new URL loads to the persisted history too...
        // Create a history item with the URL we're visiting
        HistoryItem item = new HistoryItem(newUrl);

        // Add it to the persistent history store manager
        try {
            historyManager.addItem(item);
        } catch (IOException e) {
            // history item adding went horribly wrong - warn user in a JOptionPane
            JOptionPane.showMessageDialog(this, HISTORY_ITEM_ADDING_ERROR_TEXT);

        }

        // update history list data (if necessary)
        updateHistoryList();

        // load it up in the URL bar and browser pane...
        loadNewUrl(newUrl);
    }

    /**
     * A private convenience method to cut down on code duplication. This method is called commonly by both 'visitUrl',
     * and from the back and forwards methods - and simply cuts down a little on code duplication.
     *
     * @param newUrl A URL object containing the new URL to navigate to.
     */
    private void loadNewUrl(URL newUrl) {
        // update the URL bar in the toolbar...
        toolbar.setUrlFieldValue(newUrl);

        // navigate the browser pane to the new URL...
        browserPane.navigateToUrl(newUrl);

        // some loading UI would be nice to add in here but the JEditPane doesn't have any kind of 'loading'/'load complete' action listener,
        // so it's just not possible sadly. Perhaps this could be added in future versions of the Java API though.
    }

    /**
     * This method is a listener from the PersistedURLListFrame class, meaning either a bookmark or favourite has been clicked on.
     * We should visit it.
     */
    public void listSelectedObject(PersistedURLItem selectedObject) {
        // visit the newly selected object's URL
        visitUrl(selectedObject.getUrl());

    }

    /**
     * This method is a listener from the PersistedURLListFrame class, meaning either a bookmark or favourite has been
     * selected by the user to be deleted.
     * Delete it from the relevant manager.
     */
    public void deleteSelectedObject(PersistedURLItem selectedObject) {
        // firstly, work out if it's a BookmarkItem or a HistoryItem

        if (selectedObject instanceof BookmarkItem) {
            // it's a bookmark - remove it from the bookmark manager
            // (a little very safe typecasting is required for type safety to be okay)
            try {
                bookmarkManager.removeItem((BookmarkItem) selectedObject);
            } catch (IOException e) {
                // bookmark removal went horribly wrong - display an error message in a JOptionPane
                JOptionPane.showMessageDialog(this, BOOKMARK_ITEM_REMOVAL_ERROR_TEXT);

            }

            // refresh the relevant list
            updateBookmarkList();
        }

        if (selectedObject instanceof HistoryItem) {
            // it's a piece of history - remove it from the history manager
            try {
                historyManager.removeItem((HistoryItem) selectedObject);
            } catch (IOException e) {
                // item removal went horribly wrong - display an error message in a JOptionPane
                JOptionPane.showMessageDialog(this, HISTORY_ITEM_REMOVAL_ERROR_TEXT);

            }

            // update history list
            updateHistoryList();
        }

    }

    /**
     * This is called after the user has confirmed that they wish to clear their history.
     * Get our history manager, and clear the history items.
     * Also, then refresh the history list, if it exists, so that it is not displaying outdated data.
     */
    public void clearBrowsingHistoryClicked() {
        // clear history in the history manager.
        try {
            historyManager.clearEntireList();
        } catch (IOException e) {
            // clearing history went horribly wrong - display an error message in a JOptionPane
            JOptionPane.showMessageDialog(this, HISTORY_CLEARING_ERROR_MESSAGE_TEXT);

            // hasn't worked so don't update the list - just return and give up gracefully.
            return;
        }

        // update history list data.
        updateHistoryList();

        // clear the local session only history too
        browserSessionManager.clearSessionHistory();

        // verify history was cleared and display appropriate feedback...
        if (historyManager.getItemsList().size() == 0 && (!browserSessionManager.hasBackItems())) {
            // both the persistent history list and the session history stack are empty.
            // the clearing was successful, inform the user with a friendly JOptionPane message...
            // (display pane from the PreferencesFrame so as not to take away focus from this window)
            JOptionPane.showMessageDialog(preferencesFrame, HISTORY_CLEARING_SUCCESS_MESSAGE_TEXT);
        } else {
            // clearing evidently not successful. Inform the user accordingly...
            // (display pane from the PreferencesFrame so as not to take away focus from this window)
            JOptionPane.showMessageDialog(preferencesFrame, HISTORY_CLEARING_ERROR_MESSAGE_TEXT);
        }

    }

    /**
     * A private convenience method that checks if a historyFrame exists, and if it is visible, and if so,
     * updates its data to the current history list provided by the HistoryManager.
     * This method cuts down on code duplication, as this function is required both on visits to new URLs,
     * and on clearing of the history.
     */
    private void updateHistoryList() {
        // if the HistoryListFrame exists, and it is actually visible, then update its data.
        if (historyFrame != null && historyFrame.isVisible()) {
            // it exists and is on screen - go ahead and re-set data.
            historyFrame.setUrlList(historyManager.getItemsList());
        }
    }

    /**
     * A private convenience method that checks if a bookmarksFrame exists, and if it is visible, and if so,
     * updates its data to the current bookmarks list provided by the BookmarkManager.
     * This method cuts down on code duplication, as this function is required both on adding and deleting bookmarks.
     * <p/>
     * This is really really similar to the updateHistoryList method above - if Java supported pointers, then this
     * logic duplication could be easily removed.
     */
    private void updateBookmarkList() {
        // if the bookmarksFrame exists, and it is actually visible, then update its data.
        if (bookmarksFrame != null && bookmarksFrame.isVisible()) {
            // it exists and is on screen - go ahead and re-set data.
            bookmarksFrame.setUrlList(bookmarkManager.getItemsList());
        }
    }

}
