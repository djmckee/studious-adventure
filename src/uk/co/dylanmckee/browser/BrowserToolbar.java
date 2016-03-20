package uk.co.dylanmckee.browser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * A class that creates lays out a toolbar GUI, with back and forward buttons, address bar, go button, and buttons
 * for bookmarks/history too.
 * <p/>
 * In the MVC design pattern, this class acts as a view class, being unaware of model data, and simply forwarding view
 * events through to the controller object to be handled appropriately.
 *
 * @author Dylan McKee
 *         Created 08/03/15
 */
// scope here is deliberately package local - because it's only ever used within the Browser package.
class BrowserToolbar extends JPanel {

    /**
     * The height of the toolbar, in pixels. Fixed throughout lifecycle of the toolbar.
     */
    private static final int TOOLBAR_HEIGHT = 40;

    /**
     * The length of the URL bar, in pixels.
     */
    private static final int MINIMUM_URL_BAR_LENGTH = 120;

    /**
     * A constant string containing the back button's title.
     */
    private static final String BACK_BUTTON_TITLE = "Back";

    /**
     * A constant string containing the forward button's title.
     */
    private static final String FORWARD_BUTTON_TITLE = "Forward";

    /**
     * A constant string containing the reload button's title.
     */
    private static final String RELOAD_BUTTON_TITLE = "Reload";

    /**
     * A constant string containing the home button's title.
     */
    private static final String HOME_BUTTON_TITLE = "Home";

    /**
     * A constant string containing the go button's title.
     */
    private static final String GO_BUTTON_TITLE = "Go!";

    /**
     * A constant string containing the bookmarks button's title.
     */
    private static final String BOOKMARKS_BUTTON_TITLE = "Bookmarks";

    /**
     * A constant string containing the history button's title.
     */
    private static final String HISTORY_BUTTON_TITLE = "History";

    /**
     * A constant string containing the settings button's title.
     */
    private static final String SETTINGS_BUTTON_TITLE = "Settings";

    /**
     * A constant string containing the add bookmark button's title.
     */
    private static final String ADD_BOOKMARK_BUTTON_TITLE = "Add Bookmark";

    /**
     * A private field that stores a reference to the instance of the class that implements BrowserToolbarListener, to
     * be used as a 'listener', so that relevant events can be passed on properly.
     * BrowserToolbarListener must be implemented by this listener.
     * <p/>
     * The listener is set only once, in the constructor, and is therefore a final field.
     */
    private final BrowserToolbarListener browserToolbarListener;

    /**
     * The TextField that contains the current URL needs to be a private field, since it needs a class-wide
     * scope so that it can be updated from within other methods.
     * <p/>
     * The urlField is instantiated only once, in the constructor, and is therefore a final field.
     */
    private final URLEntryField urlField;

    /**
     * The constructor simply creates the toolbar using a JToolbar and relevant buttons/text fields.
     * Layout is done as per the constants above. It needs to be passed an instance of a class that implements BrowserToolbarListener,
     * since this class acts as a 'listener', so that events that happen on the toolbar - such as button clicks - can
     * be passed properly to the relevant instance of the right class, with a good level of abstraction.
     *
     * @param listenerInstance An instance of the class that wants to receive event listener callbacks, for example
     *                         about when buttons are clicked, etc. This listener class must implement
     *                         BrowserToolbarListener.
     */
    public BrowserToolbar(BrowserToolbarListener listenerInstance) {
        // do some null checking - a BrowserToolbarListener is required...
        if (listenerInstance == null) {
            throw new IllegalArgumentException("BrowserToolbar requires a BrowserToolbarListener!");
        }

        // Set this class' reference to the listener instance to be the value we've been passed.
        browserToolbarListener = listenerInstance;

        // The toolbar requires a flow layout so elements are arranged side by side neatly
        setLayout(new FlowLayout());

        // Set a minimum size (to the fixed height, and to 0px wide - the parent container can take care of a proper value)
        setMinimumSize(new Dimension(0, TOOLBAR_HEIGHT));

        // It needs a maximum height set too, since we never want to exceed the toolbars fixed height,
        // but keeping width flexible would be advantageous because then buttons could become wider on large displays, etc.
        // so, I shall set maximum width to whatever the parent container's maximum width is.
        setMaximumSize(new Dimension(Integer.MAX_VALUE, TOOLBAR_HEIGHT));

        // Create the back button...
        JButton backButton = new JButton(BACK_BUTTON_TITLE);
        // Set action listener so it calls the listener's relevant method when clicked.
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // call the relevant listener method on click...
                browserToolbarListener.backButtonClicked();
            }
        });
        // Add the back button to the toolbar...
        add(backButton);

        // Create the forward button...
        JButton forwardButton = new JButton(FORWARD_BUTTON_TITLE);
        // Set action listener so it calls the listener's relevant method when clicked.
        forwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // call the relevant listener method on click...
                browserToolbarListener.forwardButtonClicked();
            }
        });
        // Add the forward button to the toolbar...
        add(forwardButton);

        // Create the reload button...
        JButton reloadButton = new JButton(RELOAD_BUTTON_TITLE);
        // Set action listener so it calls the listener's relevant method when clicked.
        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // call the relevant listener method on click...
                browserToolbarListener.reloadButtonClicked();
            }
        });
        // Add the reload button to the toolbar...
        add(reloadButton);


        // Create the home button...
        JButton homeButton = new JButton(HOME_BUTTON_TITLE);
        // Set action listener so it calls the listener's relevant method when clicked.
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // call the relevant listener method on click...
                browserToolbarListener.homeButtonClicked();
            }
        });
        // Add the home button to the toolbar...
        add(homeButton);

        // Now create the URLEntryField instance, using an anonymous class to define our URLEntryFieldListener.
        urlField = new URLEntryField(new URLEntryFieldListener() {
            @Override
            public void urlFieldEnterKeyPressed() {
                // call our private internal urlInput method...
                urlInput();
            }
        });
        // Set the minimum size...
        urlField.setMinimumSize(new Dimension(MINIMUM_URL_BAR_LENGTH, TOOLBAR_HEIGHT));
        // Add the URL Field to the toolbar.
        add(urlField);

        // Create the go button...
        JButton goButton = new JButton(GO_BUTTON_TITLE);
        // Set action listener so it calls the listener's relevant method when clicked.
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // call the relevant listener method on click...
                urlInput();
            }
        });
        // Add the go button to the toolbar...
        add(goButton);

        // Create the bookmarks button...
        JButton bookmarksButton = new JButton(BOOKMARKS_BUTTON_TITLE);
        // Set action listener so it calls the listener's relevant method when clicked.
        bookmarksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // call the relevant listener method on click...
                browserToolbarListener.bookmarkButtonClicked();
            }
        });
        // Add the bookmarks button to the toolbar...
        add(bookmarksButton);

        // Create the add bookmark button...
        JButton addBookmarkButton = new JButton(ADD_BOOKMARK_BUTTON_TITLE);
        // Set action listener so it calls the listener's relevant method when clicked.
        addBookmarkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // call the relevant listener method on click...
                browserToolbarListener.addBookmarkButtonClicked();
            }
        });
        // Add the bookmarks button to the toolbar...
        add(addBookmarkButton);

        // Create the history button...
        JButton historyButton = new JButton(HISTORY_BUTTON_TITLE);
        // Set action listener so it calls the listener's relevant method when clicked.
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // call the relevant listener method on click...
                browserToolbarListener.historyButtonClicked();
            }
        });
        // Add the history button to the toolbar...
        add(historyButton);

        // Create the settings button...
        JButton settingsButton = new JButton(SETTINGS_BUTTON_TITLE);
        // Set action listener so it calls the listener's relevant method when clicked.
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // call the relevant listener method on click...
                browserToolbarListener.settingsButtonClicked();
            }
        });

        // Add the history button to the toolbar...
        add(settingsButton);

    }

    /**
     * A private method to abstract away and cut down on code duplication, to be called when the 'go' button is clicked,
     * or when the enter key is pressed within the URL entry field.
     * This method validates the entered text to ensure that it is indeed a proper URL, and if so,
     * the relevant 'listener' method is called, passing in the URL, as a URL.
     */
    private void urlInput() {
        // get the entered URL from the URLEntryField
        URL enteredUrl = urlField.getUrl();

        // if the value is null - entry has failed. The URLEntryField takes care of error message UI, so we just
        // need to return and ensure no to attempt to navigate there.
        if (enteredUrl == null) {
            // we don't have a valid URL, give up.
            return;
        }

        // now that we can be absolutely sure the URL is valid, call the 'listener' method, passing over the URL.
        browserToolbarListener.urlEntered(enteredUrl);
    }

    /**
     * A public method that allows the value of the URL field to be set programmatically to a specific URL.
     *
     * @param newUrl A URL object containing the URL that the URL field should be updated to contain.
     * @throws NullPointerException if the URL passed to this method is null, a null pointer exception is thrown.
     */
    public void setUrlFieldValue(URL newUrl) {
        // perform some basic input validation
        if (newUrl == null) {
            // don't accept the input! Throw null pointer exception...
            throw new NullPointerException("setUrlFieldValue was passed a null value.");
        }

        //set the URLEntryField's URL...
        urlField.setUrl(newUrl);
    }

}
