package uk.co.dylanmckee.browser;

import java.io.IOException;
import java.util.LinkedList;

/**
 * An implementation of the PersistedItemManager abstract class, that manages History objects.
 * <p/>
 * In this implementation, I used a LinkedList. I chose the LinkedList specifically because the user is much more likely
 * to be very regularly adding items to their history as they browse around, and only needing to view their history
 * occasionally, so, the fact that the LinkedList has a .add() runtime of O(1) - as apposed to the ArrayList's O(n) -
 * is extremely advantageous.
 * <p/>
 * I considered making the HistoryManager a singleton but in this particular project there was no point -
 * only one browser window (and only one instance of BrowserPane, and therefore only one instance of HistoryManager,
 * will ever exist, so there is no need to worry about concurrency between browser window instances). However, if I did
 * add multiple window support, I'd definitely have to think about the implementation a singleton pattern instead.
 *
 * @author Dylan McKee
 *         Created 13/03/15
 */
// scope here has to be public so that it can be accessed from the tests package.
public class HistoryManager extends PersistedURLItemManager<HistoryItem> {

    /**
     * A constant string defining the file name for saved serialized history data.
     */
    private static final String fileName = "history.ser";

    /**
     * Constructs a HistoryManager instance.
     * @throws IOException thrown if a saved history file exists but cannot be read (due to data corruption etc.)
     */
    public HistoryManager() throws IOException {
        // call the superclass' constructor, passing in the filename for history, and the blank history LinkedList.
        super(fileName, new LinkedList<HistoryItem>());
    }

}
