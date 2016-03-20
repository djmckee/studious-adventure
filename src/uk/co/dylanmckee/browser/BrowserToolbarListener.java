package uk.co.dylanmckee.browser;// import the URL class so we can pass URLs within our interface's methods.

import java.net.URL;

/**
 * An interface that has some listener methods for actions involving the toolbar,
 * such as button clicks and URLs being entered in the address bar.
 *
 * @author Dylan McKee
 *         Created 08/03/15
 */
// scope here is deliberately package local - because it's only ever used within the Browser package.
interface BrowserToolbarListener {

    /**
     * A method to be called when the back button in the toolbar is clicked.
     */
    void backButtonClicked();

    /**
     * A method to be called when the forward button in the toolbar is clicked.
     */
    void forwardButtonClicked();

    /**
     * A method to be called when the bookmarks button in the toolbar is clicked.
     */
    void bookmarkButtonClicked();

    /**
     * A method to be called when the add bookmark button in the toolbar is clicked.
     */
    void addBookmarkButtonClicked();

    /**
     * A method to be called when the history button in the toolbar is clicked.
     */
    void historyButtonClicked();

    /**
     * A method to be called when the settings button in the toolbar is clicked.
     */
    void settingsButtonClicked();

    /**
     * A method to be called when the reload button in the toolbar is clicked.
     */
    void reloadButtonClicked();

    /**
     * A method to be called when the home button in the toolbar is clicked.
     */
    void homeButtonClicked();

    /**
     * A method to be called when a URL has been entered into the URL field and either clicked the 'go' button, or pressed enter,
     * after it has been verified that it is indeed a genuine URL, not some 'mangled' rouge text.
     *
     * @param newUrl The new URL that the user has entered into the text field on the toolbar.
     *               Checked and verified by the java.net library to confirm that it has a valid URL structure.
     */
    void urlEntered(URL newUrl);

}
