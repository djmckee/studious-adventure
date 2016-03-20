package uk.co.dylanmckee.browser;

/**
 * An interface that has some listener methods for actions involving the Preferences frame.
 *
 * @author Dylan McKee
 *         Created 14/03/15
 */
// scope here is deliberately package local - because it's only ever used within the Browser package.
interface PreferencesFrameListener {

    /**
     * A method to be called when the user has confirmed that they wish to clear their browsing history.
     */
    void clearBrowsingHistoryClicked();

}
