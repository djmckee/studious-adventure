package uk.co.dylanmckee.browser;

import java.io.Serializable;
import java.net.URL;

/**
 * An implementation of the PersistedURLItem abstract model class, specifically for Bookmark items.
 * This implementation adds an extra field that is not present in the PersistedURLItem class -
 * a title field, which is a String containing a human readable title for the bookmark.
 * <p/>
 * Because it is of type String, which itself implements the Serializable interface, I do not need to worry about
 * overriding serialization it will all be handled for me. I do however need to re-implement the Serializable interface
 * so that BookmarkItem objects can be properly serialized.
 * <p/>
 * Additionally, because this class adds an extra field in the form of hte title field, I implement the Cloneable again
 * in this class so that clones of the BookmarkItem have a clone of the title field in them too.
 *
 * @author Dylan McKee
 *         Created 13/03/15
 */
// scope here has to be public so that it can be accessed from the tests package.
public class BookmarkItem extends PersistedURLItem implements Serializable, Cloneable {
    /**
     * A private String field storing a human readable title for the BookmarkItem, set on creation in the constructor.
     */
    private String title;

    /**
     * Overriding the constructor so instances of this subclass can actually be created.
     *
     * @param title a human readable title string containing a title for the bookmark item - this will be persisted
     *              to disk along with the item.
     * @param url   the URL of the bookmark to persist.
     */
    public BookmarkItem(String title, URL url) {
        // Call the superclass' constructor, passing in the URL we've been passed...
        super(url);

        // perform basic null checking on the title we've been passed,
        // (no need to check URL, it's checked in the superclass' constructor)...
        if (title == null) {
            // throw an exception - titles are required!
            throw new IllegalArgumentException("BookmarkItem requires a title!");
        }

        // set the title to whatever we've been passed too...
        this.title = title;
    }

    /**
     * Overriding the toString method because users don't particularly care about what date they added a bookmark,
     * we just need either title's or URLs.
     *
     * @return a human readable String containing the bookmark's title string (if present) - if not, the bookmark's URL
     * as a string representation.
     */
    @Override
    public String toString() {
        // return the bookmark's title - if title is null or just nothing, fallback to returning
        // a String representation of the URL
        if (title == null || title.length() < 1) {
            // there's no nice title string - let's fall back to a string representation of the bookmark URL
            return getUrl().toString();
        } else {
            // there's a valid title string! Let's return it...
            return title;
        }
    }

    /**
     * Creates and returns a copy of this Bookmark item.
     *
     * @return a clone of this Bookmark Item.
     * @throws CloneNotSupportedException if the object's class does not
     *                                    support the {@code Cloneable} interface. Subclasses
     *                                    that override the {@code clone} method can also
     *                                    throw this exception to indicate that an instance cannot
     *                                    be cloned - (description taken from Java Docs).
     * @see Cloneable
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        // Create a copy of the item by cloning the superclass
        BookmarkItem itemCopy = (BookmarkItem) super.clone();

        // Set the clone's title to the current item's title...
        itemCopy.title = this.title;

        // return the cloned copy that we've created.
        return itemCopy;
    }
}
