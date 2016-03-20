package uk.co.dylanmckee.browser;

import java.net.URL;
import java.util.Stack;

/**
 * The BrowserSessionManager class is a data manager, intended to create a good level of abstraction between browser implementation
 * (in BrowserFrame) and the actual implementation concerning the handling of in-session history.
 * <p/>
 * The data stored within BrowserSessionManager does not have to be persisted - it is only that of the current browser session.
 * <p/>
 * BrowserSessionManager uses two stack data structures, containing URL objects, to handle 'back' and 'forward' pages.
 * <p/>
 * In the MVC design pattern, this class acts as a controller, controlling model objects within its model data structures,
 * and providing a good level of abstraction between those data structures and the other controller/view classes that may
 * be using it.
 *
 * @author Dylan McKee
 *         Created 28/04/15
 */
// scope here has to be public so that it can be accessed from the tests package.
public class BrowserSessionManager {

    /**
     * The backStack is a Stack object that contains URL objects giving a non-persisted session history
     * (attached to this particular instance of BrowserWindow).
     * The previous page the user has visited is at the top of the stack, as the stack descends in chronological order.
     */
    private Stack<URL> backStack;

    /**
     * The forwardStack is a Stack object that contains URL objects giving that the user can go 'forward' to once
     * they've started navigating through the backStack.
     */
    private Stack<URL> forwardStack;

    /**
     * A simple public constructor, taking no parameters and instantiating the Stacks within it.
     */
    public BrowserSessionManager() {
        // instantiate our blank stacks...
        backStack = new Stack<URL>();
        forwardStack = new Stack<URL>();

    }

    /**
     * Returns true if there's items to navigate back to - and false if not.
     *
     * @return a boolean indicating whether or not there's URL items to navigate back to.
     */
    public boolean hasBackItems() {
        // negate the backStack's isEmpty result and return this
        // (Because isEmpty means nothing is there,  so negation is necessary)
        return !backStack.isEmpty();
    }

    /**
     * Returns true if there's items to navigate forwards to - and false if not.
     *
     * @return a boolean indicating whether or not there's URL items to navigate forwards to.
     */
    public boolean hasForwardItems() {
        // negate the forwardStack's isEmpty result and return this
        // (Because isEmpty means nothing is there,  so negation is necessary)
        return !forwardStack.isEmpty();
    }

    /**
     * A method that adds a new URL to go back to to the browser's session history.
     *
     * @param backUrl the new URL to be placed chronologically as most recent on the back stack.
     */
    public void addBackUrl(URL backUrl) {
        // perform some basic Null checking...
        if (backUrl == null) {
            // not really a URL - don't continue adding, just return.
            return;
        }

        // okay, we're sure the URL is real by this point - add it to the backStack
        backStack.push(backUrl);

    }

    /**
     * A method to be use when navigating backwards, the method accepts a URL parameter (containing the current URL
     * of the browser, to add to the forward stack whilst navigating back), and returns the next chronological back URL
     * from the top of the stack of back URLs (removing it from the stack in the process).
     *
     * @param currentUrl the current URL that the browser is visiting
     * @return the next chronological back URL - from the top of the back URL stack
     */
    public URL nextBackUrl(URL currentUrl) {
        // check we have items to go back to...
        if (!hasBackItems()) {
            // we don't - so return null.
            return null;
        }

        // there's definitely content on the backStack - retrieve the item on top...
        URL backUrl = backStack.pop();

        // now let's add the current URL that we've been passed to the forwardStack so that the user can go 'forwards' to it...
        forwardStack.push(currentUrl);

        // return the backUrl (URL objects are immutable so no defensive copying is needed).
        return backUrl;

    }

    /**
     * A method to be use when navigating forwards, the method accepts a URL parameter (containing the current URL
     * of the browser, to add to the back stack whilst navigating forward), and returns the next chronological URL
     * from the top of the stack of forward URLs (removing it from the stack in the process).
     *
     * Sadly, there's some logic duplication between this method and the nextBackUrl() method.
     * <p/>
     * I wanted to reduce this by using one method that passed in references to the 'source stack' and 'destination stack',
     * but Java sadly doesn't support pointers, which would be required for that to work.
     * <p/>
     * If Java had Pointers, or this project was written in C++, this logic duplication could be completely avoided.
     *
     * @param currentUrl the current URL that the browser is visiting
     * @return the next chronological forward URL - from the top of the forward URL stack, to navigate forwards to
     */
    public URL nextForwardUrl(URL currentUrl) {
        // check we have items to go forwards to...
        if (!hasForwardItems()) {
            // we don't - so return null.
            return null;
        }

        // okay, we're certain the stacks not empty - so there's something to go forwards to. Let's retrieve it...
        URL forwardsUrl = forwardStack.pop();

        // now let's add the current URL to the back stack so that the user can go 'back' to it...
        backStack.push(currentUrl);

        // return the forwardsUrl (URL objects are immutable so no defensive copying is needed).
        return forwardsUrl;
    }

    /**
     * A method that ensures that the forward URLs in the current session history are empty (and if not, empties them)
     */
    public void clearForwardsUrls() {
        // clear the forward stack by creating a new blank one, if it's not already empty...
        if (!forwardStack.isEmpty()) {
            // if forwardStack isn't yet empty, then set it to a new blank empty URL stack...
            forwardStack = new Stack<URL>();
        }
    }

    /**
     * A method that clears both back and forwards session histories,
     * for use when the browser's entire history has been reset.
     */
    public void clearSessionHistory() {
        // instantiate blank stacks...
        backStack = new Stack<URL>();
        forwardStack = new Stack<URL>();
    }

}
