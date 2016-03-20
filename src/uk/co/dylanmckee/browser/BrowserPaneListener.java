package uk.co.dylanmckee.browser;

import java.net.URL;

/**
 * An interface that has some listener methods for actions involving the BrowserPane, such as
 * Hyperlink clicks.
 *
 * @author Dylan McKee
 *         Created 08/03/15
 */
// scope here is deliberately package local - because it's only ever used within the Browser package.
interface BrowserPaneListener {
    /**
     * This listener method is called when a hyperlink is clicked within the BrowserPane browser.
     *
     * @param clickedLink A URL object containing the URL of the hyperlink that has been clicked.
     */
    void hyperlinkClicked(URL clickedLink);
}
