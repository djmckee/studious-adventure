package uk.co.dylanmckee.browser;

import java.io.IOException;
import java.util.ArrayList;

/**
 * An implementation of the PersistedItemManager abstract class, that manages Bookmark objects.
 * <p/>
 * In this implementation I use an ArrayList as the data structure to store bookmarks. I have chosen an ArrayList
 * specifically because the user is far more likely to visit their bookmarks significantly more frequently than adding/
 * removing them, and an ArrayList has a .get() time of O(1), whilst a LinkedList has a far slower .get() time of O(N).
 * <p/>
 * I considered making the BookmarkManager a singleton but in this particular project there was no point -
 * only one browser window (and only one instance of BrowserPane, and therefore only one instance of BookmarkManager,
 * will ever exist, so there is no need to worry about concurrency between browser window instances). However, if I did
 * add multiple window support, I'd definitely have to think about the implementation a singleton pattern instead.
 *
 * @author Dylan McKee
 *         Created 13/03/15
 */
// scope here has to be public so that it can be accessed from the tests package.
public class BookmarkManager extends PersistedURLItemManager<BookmarkItem> {

    /**
     * A constant string defining the file name for saved serialized bookmark data.
     */
    private static final String fileName = "bookmarks.ser";

    /**
     * Constructs a BookmarkManager instance.
     * @throws IOException thrown if a saved bookmarks file exists but cannot be read (due to data corruption etc.)
     */
    public BookmarkManager() throws IOException {
        // call the superclass' constructor, passing in the filename for bookmarks, and the blank bookmarks ArrayList.
        super(fileName, new ArrayList<BookmarkItem>());
    }

}
