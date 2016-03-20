package uk.co.dylanmckee.browser;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;
import java.io.IOException;
import java.net.URL;

/**
 * A class that extends the JEditorPane for web browsing purposes. Editing is disabled, because web pages shouldn't
 * normally be editable in a browser, and the hyperlink listener must be implemented properly so that hyperlinks
 * can be clicked and browsed within the pane.
 * <p/>
 * In the MVC design pattern, this class acts as a view class, being unaware of model data, and being controlled by the
 * BrowserFrame controller class, whilst interacting directly with the user.
 *
 * @author Dylan McKee
 *         Created 08/03/15
 */
// Extending the editor pane and implementing the HyperlinkListener to make links clickable
// scope here is deliberately package local - because it's only ever used within the Browser package.
class BrowserPane extends JEditorPane implements HyperlinkListener {

    /**
     * A constant containing the String prototype for the 'cannot load page' error message. This prototype then needs to
     * be formatted to contain a String representation of the URL where the %s placeholder is.
     */
    private final static String URL_LOAD_ERROR_TEXT_PROTOTYPE_STRING = "Sorry, we cannot navigate to %s at the moment. Please check your network connection and the URL over before trying again.";

    /**
     * A private variable containing a reference to the 'listener' object.
     * It must implement the BrowserPaneListener interface.
     * <p/>
     * The listener object is set only once, in the constructor method, and is therefore a final field.
     */
    private final BrowserPaneListener browserPaneListener;

    /**
     * A constructor, overriding the superclass' relevant method.
     *
     * @param browserPaneListener The listener object, must implement the BrowserPaneListener interface.
     */
    public BrowserPane(BrowserPaneListener browserPaneListener) {
        // call the superclass' constructor...
        super();

        // do some null checking - a browserPaneListener is required...
        if (browserPaneListener == null) {
            throw new IllegalArgumentException("BrowserPane requires a BrowserPaneListener!");
        }

        // we do not want the web browser content to be user-editable
        setEditable(false);

        // set ourselves as a Hyperlink Listener.
        addHyperlinkListener(this);

        // set the listener reference object to whatever we've been passed...
        this.browserPaneListener = browserPaneListener;
    }

    /**
     * A public method to update the page being displayed within the browser pane.
     *
     * @param newUrl A URL object containing the URL that the pane should browse to.
     */
    public void navigateToUrl(URL newUrl) {
        //browse the pane to the new URL that we've been passed...
        //or at least try to...
        try {
            //set the pane's URL to the URL we've been passed...
            setPage(newUrl);
        } catch (IOException ie) {
            //if we can't access the page for some reason, show an appropriate error in the form of a JOptionPane...
            //format the error text prototype string...
            String errorText = String.format(URL_LOAD_ERROR_TEXT_PROTOTYPE_STRING, newUrl.toString());

            //show the error message with the formatted string
            JOptionPane.showMessageDialog(this, errorText);
        }
    }

    /**
     * A public method that re-loads the current page that the browser is on. No parameters necessary.
     */
    public void reloadPage() {
        /* As the documentation at http://docs.oracle.com/javase/6/docs/api/javax/swing/JEditorPane.html#setPage(java.net.URL)
           describes, you can't simply re-set the page of the JEditorPane to the current page to force a refresh, sadly.
           Firstly, we must clear the stream...
         */

        // Get the current URL before we clear things...
        URL currentUrl = getPage();

        // I took the following 2 lines of code from the Java Documentation,
        // found at http://docs.oracle.com/javase/6/docs/api/javax/swing/JEditorPane.html#setPage(java.net.URL)
        // and then modified them slightly to fit my implementation.
        Document doc = getDocument();
        doc.putProperty(Document.StreamDescriptionProperty, null);

        // Now that we've cleared the stream, we're safe to reset the page...

        // re-set the current page to the current page (forcing a reload)
        navigateToUrl(currentUrl);
    }

    /**
     * Called when a hypertext link is updated.
     *
     * @param e the event responsible for the update
     */
    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        // Verify that it is indeed a proper link click, not just a link being hovered over (as per the HyperlinkListener docs)
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {

            // Get the clicked URL.
            URL clickedUrl = e.getURL();

            // Call the listener object's relevant method, passing the URL that's been clicked on...
            browserPaneListener.hyperlinkClicked(clickedUrl);
        }
    }
}
