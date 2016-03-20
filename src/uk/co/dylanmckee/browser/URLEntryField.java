package uk.co.dylanmckee.browser;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A class inherits JTextField and overrides appropriate methods, alongside adding new ones, to create a URL entry box
 * that provides URL validation, semi-intelligent URL scheme completion, and other useful features.
 * <p/>
 * URLEntryField takes care of all error handling to do with URL object creation, including showing messages to the
 * user in the form of JOptionPane dialogs when user errors occur.
 * <p/>
 * This URLEntryField is required in both the BrowserToolbar and the PreferencesFrame, so having this component as a
 * convenient class cuts down heavily on code duplication between the two.
 * <p/>
 * In the MVC design pattern, this class acts as a view class, being unaware of the content it is displaying, simply
 * validating and then passing that content on.
 *
 * @author Dylan McKee
 *         Created 18/03/15
 */
// scope here is deliberately package local - because it's only ever used within the Browser package. Scope modifier has not been accidentally forgotten!
class URLEntryField extends JTextField implements KeyListener {

    /**
     * A constant String with the contents of the 'invalid URL' message text.
     */
    private final static String INVALID_URL_WARNING_STRING = "The URL you entered is not valid, sorry. Please check it over before trying again.";

    /**
     * A constant defining the base of our desired URL scheme (in the current Browser's case, http), for our URL
     * scheme validation.
     */
    private final static String BASE_URL_SCHEME_STRING = "http";

    /**
     * A listener object that must implement URLEntryFieldListener, to receive listener events for example when actions
     * like the enter key being pressed happen within this URLEntryField instance.
     * <p/>
     * Final because it should only ever be set once per instance, in the constructor.
     */
    private final URLEntryFieldListener urlEntryFieldListener;

    /**
     * Constructs a new <code>URLEntryField</code>.  A default model is created,
     * the initial string is <code>null</code>,
     * and the number of columns is set to 0.
     *
     * @param urlEntryFieldListener a listener object - an instance of URLEntryFieldListener that wishes to receive
     *                              event callbacks from this URLEntryField instance.
     */
    public URLEntryField(URLEntryFieldListener urlEntryFieldListener) {
        // call the default superclass constructor.
        super();

        // set this instance's listener object to the URLEntryFieldListener object we've been passed
        this.urlEntryFieldListener = urlEntryFieldListener;

        // add ourselves as a key listener, so that we can have enter key support built in.
        addKeyListener(this);
    }

    /**
     * Constructs a new empty <code>URLEntryField</code> with the specified
     * number of columns.
     * A default model is created and the initial string is set to
     * <code>null</code>.
     *
     * @param urlEntryFieldListener a listener object - an instance of URLEntryFieldListener that wishes to receive
     *                              event callbacks from this URLEntryField instance.
     * @param columns               the number of columns to use to calculate
     *                              the preferred width; if columns is set to zero, the
     *                              preferred width will be whatever naturally results from
     *                              the component implementation
     */
    public URLEntryField(URLEntryFieldListener urlEntryFieldListener, int columns) {
        // call this subclass' common constructor to perform our standard setup stuff...
        // (passing forwards the URLEntryFieldListener object that we've been passed ourselves).
        this(urlEntryFieldListener);

        // then manually call the setColumns method with the parameter we've been passed...
        setColumns(columns);
    }


    /**
     * Invoked when a key has been typed.
     * See the class description for {@link java.awt.event.KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the KeyEvent instance.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // we're not interested - this is a blank implementation merely to satisfy interface requirements.
    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link java.awt.event.KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the KeyEvent instance.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // we are indeed interested - let's see if it's the enter key...
        // compare KeyEvent keyCode to see if it's the constant value for the enter key...
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            // it's the enter key. Verify it's a valid URL and then call the relevant listener method.
            URL currentUrl = getCurrentURLIfValid();

            // if the current valid URL is not null (meaning it is actually valid), call the relevant listener method...
            if (currentUrl != null) {
                // call the relevant URLEntryFieldListener method on our listener
                urlEntryFieldListener.urlFieldEnterKeyPressed();

            }

        }

    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link java.awt.event.KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the KeyEvent instance.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // we're not interested - this is a blank implementation merely to satisfy interface requirements.

    }

    /**
     * A private convenience method that gets the value of the text field, check that it has some valid URL scheme
     * prefixing it - and if not adds a valid scheme on (http:// in our browser implementation's case). Then
     * a URL object is formulated - if possible - and returned. If that formulation is not possible, the relevant
     * exception is caught internally, and a JOptionPane message to the user is then shown from the URLEntryField.
     * A null value is then returned by this method.
     * <p/>
     * This method is required to reduce code duplication - because both the getUrl method, and the 'on enter key press'
     * event require the exact same behaviour in terms of URL object formation and verification.
     *
     * @return a URL object containing a URL representation of the text that's been entered - if it's a valid URL.
     * Otherwise, null is returned.
     */
    private URL getCurrentURLIfValid() {
        // get the current value of the URL field as String, so we can see if it's a genuine URL...
        String urlString = getText();

        // check it has a http/https URL scheme on it - if not, assume http...
        // firstly, check the string's long enough...
        if (urlString.length() > 4) {
            // get the first four characters so we can compare scheme component in a simple string match...
            String urlSchemeStringComponent = urlString.substring(0, 4);

            // ensure they match the desired URL schema ('http') (since this pattern matching keeps https inclusive too)
            if (!urlSchemeStringComponent.equals(BASE_URL_SCHEME_STRING)) {
                // they don't match!!
                // add a URL scheme on, then update the text field value too.
                // (assuming http here isn't great but it has far wider support than https so we shall do it).
                urlString = String.format("%s://%s", BASE_URL_SCHEME_STRING, urlString);

                // update the text field to the newly modified 'corrected' string...
                setText(urlString);
            }
        }

        // attempt to make a URL out of it...
        try {
            // try creating a URL from the string and returning it if successful...
            // (because we're creating a new URL object to return here, we don't need to bother creating a defensive copy - it's not a field in the class, it's irrelevant if changed afterwards)
            return new URL(urlString);

        } catch (MalformedURLException e) {
            // the url is clearly not valid.
            // show the user an appropriate error.
            JOptionPane.showMessageDialog(this, INVALID_URL_WARNING_STRING);

            //return null! We have no valid URL.
            return null;
        }
    }

    /**
     * A public getter method, calls the private convenience method to get a valid URL from what's been entered,
     * then returns that value - or null if a valid URL cannot be formed from what's been entered.
     *
     * @return a URL object containing a URL representation of the text that's been entered - if it's a valid URL.
     * Otherwise, null is returned.
     */
    public URL getUrl() {
        // return the current url (if valid - otherwise null).
        return getCurrentURLIfValid();
    }

    /**
     * Sets the text field string value to a string representation of the URL that we've been passed.
     *
     * @param url the new URL that the URLEntryField should hold.
     * @throws NullPointerException if a the url parameter is null, a NullPointerException is thrown because toString cannot be called upon a null.
     */
    public void setUrl(URL url) {
        // do some simple null value checking...
        if (url == null) {
            // cannot call toString on a null... let's throw a NullPointerException
            throw new NullPointerException("setUrl was passed a null parameter.");
        }

        // set the TextField's value to the toString representation of url
        setText(url.toString());
    }
}
