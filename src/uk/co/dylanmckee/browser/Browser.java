package uk.co.dylanmckee.browser;

/**
 * A very simple 'main' Java class for the Browser application,
 * which contains a main method that creates a new instance of the browser on run.
 *
 * @author Dylan McKee
 *         Created 07/03/15
 */

public class Browser {

    /**
     * The Browser class main method is intended to be ran once, on the initial launch of the Browser application.
     * It creates an instance of the BrowserFrame.
     *
     * @param args no command line arguments for this program exist.
     */
    public static void main(String[] args) {
        // This main method should be called only once, on application launch.
        // Make a new instance of our browser frame class...
        // (suppressing compiler warnings about this unused assignment - it's clearly never going to be used, since we
        //  only instantiate it once per browser application run instance - warning is annoying and not helpful).
        @SuppressWarnings("UnusedAssignment")
        BrowserFrame browser = new BrowserFrame();

    }

}
