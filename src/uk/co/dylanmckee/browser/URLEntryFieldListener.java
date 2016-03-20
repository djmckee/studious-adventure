package uk.co.dylanmckee.browser;

/**
 * An interface that has some listener methods for actions involving the URL entry text field, such as when a URL is
 * entered and the user requests to visit it by pressing the enter key.
 *
 * @author Dylan McKee
 *         Created 18/03/15
 */

// scope here is deliberately package local - because it's only ever used within the Browser package.
interface URLEntryFieldListener {

    /**
     * A method to be called when the user has pressed the enter key indicating they wish to navigate to the URL they've
     * entered. Called only after URL verification is complete, so the url object is factually known to be a
     * genuine (by schema at least) URL that we can try to navigate to.
     */
    void urlFieldEnterKeyPressed();


}
