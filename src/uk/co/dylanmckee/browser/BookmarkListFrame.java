package uk.co.dylanmckee.browser;

import java.util.List;

/**
 * An implementation of the PersistedURLListFrame abstract class, that is responsible for the bookmarks window.
 *
 * @author Dylan McKee
 *         Created 13/03/15
 */
// scope here is deliberately package local - because it's only ever used within the Browser package.
class BookmarkListFrame extends PersistedURLListFrame<BookmarkItem> {

    /**
     * A constant string that holds the name of the bookmarks window, for display in the title bar.
     */
    private static final String BOOKMARKS_WINDOW_TITLE = "Bookmarks";

    /**
     * The overridden constructor allows passing of a list of bookmarks to display, and a listener object.
     *
     * @param bookmarksList a List containing BookmarkItems.
     * @param listener      the object that should receive listener events (such as item click updates),
     *                      must implement
     */
    public BookmarkListFrame(List<BookmarkItem> bookmarksList, PersistedURLListFrameListener listener) {
        // call the superclass' constructor, passing in the parameters we've received
        super(BOOKMARKS_WINDOW_TITLE, bookmarksList, listener);
    }
}
