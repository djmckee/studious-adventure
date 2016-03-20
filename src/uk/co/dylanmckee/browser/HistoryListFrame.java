package uk.co.dylanmckee.browser;

import java.util.List;

/**
 * An implementation of the PersistedURLListFrame abstract class, that is responsible for the history window.
 *
 * @author Dylan McKee
 *         Created 13/03/15
 */
// scope here is deliberately package local - because it's only ever used within the Browser package.
class HistoryListFrame extends PersistedURLListFrame<HistoryItem> {

    /**
     * A constant string that holds the name of the history window, for display in the title bar.
     */
    private static final String HISTORY_WINDOW_TITLE = "History";


    /**
     * The overridden constructor allows passing of a list of history items to display, and a listener object.
     *
     * @param historyList a List containing HistoryItem's.
     * @param listener    the object that should receive listener events (such as item click updates),
     *                    must implement
     */
    public HistoryListFrame(List<HistoryItem> historyList, PersistedURLListFrameListener listener) {
        // call the superclass' constructor, passing in the parameters we've received
        // (null checking is performed by the superclass).
        super(HISTORY_WINDOW_TITLE, historyList, listener);

    }

}
