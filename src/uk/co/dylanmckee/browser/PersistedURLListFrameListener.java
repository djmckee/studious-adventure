package uk.co.dylanmckee.browser;

/**
 * An interface that has some listener methods for actions involving the URL List, such as when a list item is
 * clicked on.
 *
 * @author Dylan McKee
 *         Created 13/03/15
 */
// scope here is deliberately package local - because it's only ever used within the Browser package.
interface PersistedURLListFrameListener {

    /**
     * A method to be called when an object is selected in the list.
     *
     * @param selectedObject the PersistedURLItem selected from the list.
     */
    void listSelectedObject(PersistedURLItem selectedObject);

    /**
     * A method to be called when an object in the list has been chosen to be deleted.
     *
     * @param selectedObject the PersistedURLItem selected from the list.
     */
    void deleteSelectedObject(PersistedURLItem selectedObject);

}
