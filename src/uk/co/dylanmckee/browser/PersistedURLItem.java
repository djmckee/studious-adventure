package uk.co.dylanmckee.browser;

import java.io.Serializable;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An abstract model class to be used by both bookmark and history items, to model and store basic data about the items:
 * namely the URL of the item and the date/time that the item was created.
 * <p/>
 * The class implements Serializable so that URL items can be serialized and persisted to disk properly in byte form,
 * and as such, all fields within the class use types that are also serializable.
 * <p/>
 * It also implements Comparable in a chronological manner, allowing easy sorting/ordering of items by date created
 * (newest first).
 * <p/>
 * Equals and hashCode are also overridden to give equality checking, since my unit tests require the ability to use the
 * equals method, and potentially future implementations of the class may also use it.
 * <p/>
 * In the MVC design pattern, this class (and its subclass implementations) acts as a model class, with the prime aim
 * of modelling user data, whilst being entirely unaware of and abstracted away from any actual interaction with the
 * user, through the means of a controller.
 * <p/>
 * This class implements the Cloneable interface, so that it can be used within defensive copying scenarios - this
 * interface implementation is particularly useful for model classes, and is used in action within my project in my
 * PersistedURLItemManager class.
 *
 * @author Dylan McKee
 *         Created 13/03/15
 */
// scope here is deliberately package local because I only ever want PersistedURLItem subclasses to exist in my Browser package.
abstract class PersistedURLItem implements Serializable, Comparable<PersistedURLItem>, Cloneable {
    /**
     * A constant String prototype specifying how dates should be formatted to make them pleasantly human readable.
     */
    private static final String DATE_STRING_FORMAT_PROTOTYPE = "dd/MM/yyyy HH:mm:ss";

    /**
     * A private field that holds the URL of the item. The URL class itself implements Serializable
     */
    private URL url;

    /**
     * A private field that holds the date/time that the item was created. The Date class itself implements Serializable
     */
    private Date dateCreated;


    /**
     * A constructor that takes the URL of the item, and sets the creation date to the current date.
     * It's safe to set the creation date to the current date, because de-serialization doesn't use the constructor.
     * <p/>
     * I made this constructor protected because PersistedURLItem is an abstract class, so only subclass
     * implementations of this class will ever need to or be able to call the constructor - and using the narrowest
     * scope possible for methods is recommended practice as we've been taught in lectures.
     *
     * @param url A URL object containing the URL to be persisted by the model item.
     */
    PersistedURLItem(URL url) {
        // perform basic null checking on the url we've been passed...
        if (url == null) {
            // throw an exception - urls are required!
            throw new IllegalArgumentException("PersistedURLItem requires a URL!");
        }

        //set the URL of the current item to the URL we've been passed...
        this.url = url;

        //set the creation date to the current date - new date object instances contain the current date/time by default.
        dateCreated = new Date();

    }

    /**
     * A simple getter method that returns the URL of the persisted URL item.
     *
     * @return A URL object containing the URL of the persisted URL item.
     */
    public URL getUrl() {
        // no need to implement defensive copying here, because the URL class is immutable - just return it.
        return url;
    }

    /**
     * A convenience method to return a nicely formatted human readable String object containing the original creation
     * date and time of the current URL object.
     *
     * @return A String object containing a human readable date and time that the persisted URL item was originally created
     */
    public String getCreationDateString() {
        // I looked up how to format dates here... http://www.mkyong.com/java/java-how-to-get-current-date-time-date-and-calender/
        DateFormat dateFormatter = new SimpleDateFormat(DATE_STRING_FORMAT_PROTOTYPE);

        // Format the date and return the result...
        // (no defensive copying need because the String class is immutable)
        return dateFormatter.format(dateCreated);
    }

    /**
     * Returns a human readable string representation of the URL item (i.e. the URL, then its original creation date)
     *
     * @return a human readable String representation of the URL item (i.e. the URL, then its original creation date)
     */
    public String toString() {
        // concatenate string representations in a formatted String, then return it
        return String.format("%s - %s", url.toString(), getCreationDateString());
    }

    /**
     * Compares the current item's creation date to that of the item it is being compared with,
     * to allow simple chronological sorting of persisted URL items.
     *
     * @param otherItem the persisted URL item to compare.
     * @return a negative integer if the item is older, zero if it is the same age, a positive if it is newer.
     */
    public int compareTo(PersistedURLItem otherItem) {
        //check for date equality first...
        if (dateCreated.equals(otherItem.dateCreated)) {
            // if dates are equal, return 0...
            return 0;

        } else if (dateCreated.compareTo(otherItem.dateCreated) > 0) {
            // other date's instance's date is bigger - return -1.
            return -1;
        } else {
            // this instance's date must be bigger - return 1.
            return 1;
        }

    }

    /**
     * Overriding the hashcode to add URL and dateCreated fields to it.
     *
     * @return an integer unique to the field values of this particular PersistedURLItem instance.
     */
    @Override
    public int hashCode() {
        //pick a prime number to multiply all of the fields by
        int prime = 31;

        //declare the hashcode, set it equal to a prime number
        int hashcode = 17;

        //multiply the prime by the hashcode (a prime to begin with too), then add the URLs hashcode...
        hashcode = prime * hashcode + url.hashCode();

        //now multiply the prime by the hashcode again and add on the creation date's hashcode...
        hashcode = prime * hashcode + dateCreated.hashCode();

        //and finally return our hashcode
        return hashcode;

    }


    /**
     * Overriding the standard equals method to check for equality between PersistedURLItems, checking both their
     * URL field and their 'creation date' field for equality. Only if both of these fields are equal is the
     * PersistedURLItem considered equal. Overriding hashcode also to facilitate this field equality checking.
     *
     * @param o the object to compare for equality.
     * @return a boolean which will be true if the objects are equal, and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        //firstly, if the memory addresses are entirely equal, then obviously return yes...
        if (this == o) {
            return true;
        }

        //make sure the other object's not null, because they cannot be equal if so...
        if (o == null) {
            return false;
        }

        //now check the instance types, if they're not the same, there's no chance of them being equal
        if (!(o instanceof PersistedURLItem)) {
            //definitely definitely cannot be equal then...
            return false;
        }

        //okay, now let's cast object to a PersistedURLItem, and check its fields...
        PersistedURLItem otherUrlItem = (PersistedURLItem) o;

        //use the overridden hashCode method of PersistedURLItem to check for field value equality...
        if (this.hashCode() == otherUrlItem.hashCode()) {
            //definitely equal then...
            return true;
        }

        //otherwise, it's just not equal.
        return false;

    }

    /**
     * Creates and returns a copy of this URL item.
     *
     * @return a clone of this URL Item.
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
        PersistedURLItem itemCopy = (PersistedURLItem)super.clone();

        // set the copy's URL to be the URL of the current instance.
        // URL objects are immutable so no clone necessary here.
        itemCopy.url = this.url;

        // set the copy's creation date to be this instance's creation date
        // Date objects are mutable so a clone of this object is required (along with some casting to keep types happy)
        itemCopy.dateCreated = (Date)this.dateCreated.clone();

        // return the cloned copy that we've created.
        return itemCopy;
    }

}
