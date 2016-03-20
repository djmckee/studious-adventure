package uk.co.dylanmckee.browser;

import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * An abstract class that will be used to manage persisted URL items, in terms of actually saving them,
 * retrieving them, etc. This gives a good level of abstraction between the browser view/controller and the
 * 'lower level' file I/O that looks after the model objects.
 * <p/>
 * This will be used for Bookmarks and for persistent History items too, so this is an abstract class needs to be inherited.
 * <p/>
 * The following tutorial was useful for understanding how the serialization/de-serialization that I use within this
 * class actually works... http://www.tutorialspoint.com/java/java_serialization.htm
 * I used the methods found within it to serialize and de-serialize my data, along with the advice and classes found
 * in the Java documentation at... http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
 * <p/>
 * In the MVC design pattern, this class (and its subclass implementations) act between the model and the controller,
 * allowing a good level of abstraction between model objects and the handling of them (such as saving to disk etc.)
 * and the controller with its use of model objects (such as getting them to hand them to a view to be displayed).
 * This class throws exceptions - rather than showing users errors directly - to improve code re-use potential and provide
 * a good level of abstraction.
 * <p/>
 * At first I contemplated using a simple form of Caesar cipher encryption on my persisted data, however I realised that
 * encrypting application specific data is a poor design pattern on the whole - since it prohibits other applications
 * from reading it easily - not promoting choice or openness. However, because I have deliberately not included encryption
 * users can freely migrate from my browser to another (for example, Google Chrome could have an 'import' system that would
 * read, deserialize and import my user's bookmarks, should my user decide to switch browser). Additionally, a
 * Caesar cipher is a very weak form of encryption - if the user cares about data security they should be using full
 * disk encryption, and encrypting the volume that the Browser application is stored on/writes data to with a proper
 * industrial strength encryption standard.
 *
 * @author Dylan McKee
 *         Created 13/03/15
 */
// scope here is deliberately package local - because it's only ever used within the Browser package.
abstract class PersistedURLItemManager<E extends PersistedURLItem> {

    /**
     * A private String field, this is the file name for the persisted data.
     * Each and every subclass of this manager must set this to its own unique value, by passing their desired file name
     * to the constructor method of the superclass.
     * <p/>
     * I made this field final because it should only ever be set once per instance of PersistedURLItemManager -
     * re-setting it during an instance's lifetime could have dire consequences because saved data could end up
     * 'out of sync', and other bad/unexpected behaviours may well occur.
     */
    private final String fileName;

    /**
     * A private List field - this will hold the actual persisted items, uses the List interface, rather than a specific
     * implementation, because different implementations of this particular abstract class will wish to use different
     * List implementations depending upon their needs.
     * (i.e. in some cases I'll need fast regular access so I'll choose an ArrayList, in others I'll need to add items
     * regularly, so I'll go for a LinkedList which has better object adding speed).
     * All implementations of the List interface are serializable, so they can all be safely written to disk using
     * the object serialization classes/methods.
     * <p/>
     * <p/>
     * Using the generic type, which must inherit from the PersistedURLItem, as the list's data type.
     * <p/>
     * Because both History and Bookmark items inherit from the PersistedURLItem, I can declare that as the generic
     * type for the list items.
     */
    private List<E> itemsList;

    /**
     * I'm making the constructor protected because I want to force inheriting classes to override it - as this is where
     * they'll need to initialise their list implementations.
     * <p/>
     * Protected so that only subclasses can inherit it (also, because this class is package local, subclasses
     * inside of other packages could not inherit this, giving good scope).
     *
     * @param blankList a blank list instance of the desired list implementation that this class will use.
     * @param fileName  the local file name for persisted serialized URL data, where it should be read/written from.
     * @throws IOException thrown if an existing persisted file exists, but cannot be read (for example due to data corruption)
     */
    protected PersistedURLItemManager(String fileName, List<E> blankList) throws IOException {
        // set local file name reference to the name we've been passed...
        this.fileName = fileName;

        // blankList is required so that the List uses the correct type of List implementation
        itemsList = blankList;

        // let's load persisted items from store.
        loadItemsListFromFile();
    }

    /**
     * A private method that loads, reads and de-serializes a saved list of items from disk.
     *
     * @throws IOException thrown if the existing file cannot be read from disk, for example due to data corruption.
     */
    private void loadItemsListFromFile() throws IOException {
        // Initialise the file object.
        File file = new File(fileName);

        // Check that it actually exists, and is a file - otherwise don't bother trying to read from it.
        if (file.exists() && file.isFile()) {
            // Okay, it's definitely there and an actual file, let's try reading persisted data from it.
            try {
                // Initialise the file as a stream of raw bytes
                FileInputStream fileInputStream = new FileInputStream(file);

                // now initialise an ObjectInputStream so we can de-serialize the List object from raw bytes.
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                try {
                    // Get the List object...
                    try {
                        // Get our list (it *should* be of the List<E> type, casting shall save us here)...
                        // (safe to ignore the unchecked cast warning because we're creating the file in the first place,
                        //  and so can be absolutely certain about the type of the contents when de-serialized)
                        @SuppressWarnings("unchecked")
                        List<E> savedList = (List<E>) objectInputStream.readObject();

                        // Perform some quick null checking
                        if (savedList == null) {
                            // don't set it, return.
                            // (ensure to perform stream close before returning)
                            objectInputStream.close();
                            fileInputStream.close();
                            return;
                        }

                        // Go ahead and set the list...
                        itemsList = savedList;

                    } catch (ClassNotFoundException cnfE) {
                        // Reading the saved list evidently hasn't worked - don't set itemsList to anything.
                        // Throw a generic IO Exception from this method...
                        throw new IOException("Persisted URL Items cannot be read");
                    }

                } catch (EOFException eE) {
                    // The file ended before expected - it's probably damaged...
                    // Throw a generic IO Exception from this method...
                    throw new IOException("Persisted URL Items cannot be read");
                }

                // close the object stream.
                objectInputStream.close();

                // and close the file too.
                fileInputStream.close();


            } catch (FileNotFoundException fnE) {
                // there's nothing we can realistically do here - although given that we check existence previously,
                // we should never really hit this error.
                // Throw a generic IO Exception from this method...
                throw new IOException("Persisted URL Items cannot be read");

            } catch (IOException e) {
                // an actual exception we probably need to warn the user about.
                // Throw a generic IO Exception from this method...
                throw new IOException("Persisted URL Items cannot be read");

            }
        }

    }

    /**
     * A private method to save the list of items to disk. This should be called after each list item addition/deletion.
     * It serializes the list, then saves the serialization.
     *
     * @throws IOException thrown if the an error occurs during the list saving process.
     */
    private void saveItemsListToFile() throws IOException {
        // Initialise the file object.
        File file = new File(fileName);

        // Initialise a file output stream so we can begin writing...
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        // Now initialise an ObjectOutputStream so we can serialize into bytes to write out to the fileOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        // Write our itemsList, serializing it in the process...
        objectOutputStream.writeObject(itemsList);

        // Close the object stream
        objectOutputStream.close();

        // and the file stream too
        fileOutputStream.close();

    }

    /**
     * A simple getter method that returns the itemsList, sorted with the most recent item at the top.
     *
     * @return the list of items, they're all of type E.
     */
    public List<E> getItemsList() {
        // sort the list using the PersistedURLItem compareTo method (which ranks chronologically, from newest down)
        Collections.sort(itemsList);

        // and return a copy (for defensive copying purposes).
        // Because List doesn't implement the Cloneable interface, and I'm unsure exactly what type of list it is, I'm
        // going to have to instantiate a new list from the list class (via getClass()), and then fill the new list up
        // with objects from itemsList, and then return it.

        // create a new list of the correct instance type.
        try {
            List<E> defensiveCopyList = itemsList.getClass().newInstance();

            // iterate through the itemList, adding clones of each item to the defensive copy of the list...
            for (PersistedURLItem item : itemsList) {
                // create a clone...
                E clone = (E) item.clone();

                // add it to the defensive copy of the list...
                defensiveCopyList.add(clone);
            }

            // and return the defensive copy of the list...
            return defensiveCopyList;

        } catch (InstantiationException e) {
            e.printStackTrace();
            // somethings gone horribly wrong, let's return null. This should never happen.
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            // somethings gone horribly wrong, let's return null. This should never happen.
            return null;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            // somethings gone horribly wrong, let's return null. This should never ever happen because PersistedURLItem
            // implements the Cloneable interface, but still.
            return null;
        }

    }

    /**
     * Adds a new list item of generic type E to the list, then re-saves the list to update changes.
     *
     * @param newItem the list item to be added, of generic type E.
     * @throws IOException thrown if the changes to the list cannot be persisted to disk successfully.
     */
    public void addItem(E newItem) throws IOException {
        // add the item
        itemsList.add(newItem);

        // sort the list using the PersistedURLItem compareTo method (which ranks chronologically, from newest down)
        Collections.sort(itemsList);

        // and re-save...
        saveItemsListToFile();
    }

    /**
     * Clears the itemsList, and saves the empty list, overwriting previous persisted list data.
     *
     * @throws IOException thrown if the changes to the list cannot be persisted to disk successfully.
     */
    public void clearEntireList() throws IOException {
        // clear the list.
        itemsList.clear();

        // save changes.
        saveItemsListToFile();

    }

    /**
     * Removes a specific item from the list (if present, if not it simply fails silently), and then
     * re-saves the list to persist changes to disk.
     *
     * @param newItem the PersistedURLItem instance to remove from the list (if present)
     * @throws IOException thrown if the changes to the list cannot be persisted to disk successfully.
     */
    public void removeItem(E newItem) throws IOException {
        // attempt to remove the item from the list - this fails silently if the item is not present, and returns
        // a boolean which indicates if the item was removed.
        boolean hasRemoved = itemsList.remove(newItem);

        // check that the item was removed before bothering to re-save the list.
        if (hasRemoved) {
            // an item has actually been removed. Re-save the list to disk to persist the removal.
            saveItemsListToFile();
        }
    }

}