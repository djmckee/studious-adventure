package uk.co.dylanmckee.browser;

import java.net.URL;

/**
 * An implementation of the PersistedURLItem abstract model class, specifically for persisted History items.
 *
 * @author Dylan McKee
 *         Created 13/03/15
 *
 */
// scope here has to be public so that it can be accessed from the tests package.
public class HistoryItem extends PersistedURLItem {

    /**
     * Overriding the constructor so instances of this subclass can actually be created.
     *
     * @param url the URL of the history item to be saved.
     */
    public HistoryItem(URL url) {
        // Call the superclass' constructor, passing in the URL we've been passed...
        super(url);
    }

    /**
     * Overriding the toString method to make phrasing of the string a little more user friendly.
     *
     * @return a human readable String representation of the URL of the history URL and visited date.
     */
    @Override
    public String toString() {
        // return a string with the date/time first, then the history item's URL...
        return String.format("%s - %s", getCreationDateString(), getUrl());
    }

}
