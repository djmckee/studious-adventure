package uk.co.dylanmckee.browser;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/**
 * A JFrame containing a JList that displays URL items from a List (containing objects of generic type E) and features
 * listeners for when an item is clicked (to an object which must implement the PersistedURLListFrameListener)
 * <p/>
 * This class implements the ListSelectionListener to get listeners that it can forward on efficiently.
 * <p/>
 * This is an abstract class, which is intended to be implemented by subclasses for each type of persistent URL item,
 * so that code that needs to differ, such as window title, can be kept neatly, and with a good level of abstraction in
 * the relevant subclasses.
 * <p/>
 * In the MVC design pattern, this class (and its subclass implementations) act as a view ,
 * being unaware of model data, and being controlled by the BrowserFrame controller class, whilst interacting
 * directly with the user.
 *
 * @author Dylan McKee
 *         Created 13/03/15
 */
// scope here is deliberately package local - because it's only ever used within the Browser package.
abstract class PersistedURLListFrame<E extends PersistedURLItem> extends JFrame {

    /**
     * A constant minimum width for the list window, in pixels.
     */
    private static final int MINIMUM_WINDOW_WIDTH = 320;

    /**
     * A constant minimum width for the list height, in pixels.
     */
    private static final int MINIMUM_WINDOW_HEIGHT = 420;

    /**
     * A private field that stores a reference to the instance of the class that implements
     * PersistedURLListFrameListener, to be used as a 'listener', so that relevant events can be passed on properly.
     * <p/>
     * The listener object is set only once, in the constructor, and therefore uses the final modifier.
     */
    private final PersistedURLListFrameListener persistedURLListFrameListener;

    /**
     * The JList GUI object, with generic type String, so that it can display the toString representations of the
     * PersistedURLItems.
     * <p/>
     * The JList is instantiated only once, in the constructor, and therefore has a final modifier.
     */
    private final JList<String> jStringList;

    /**
     * A private field that holds a list of objects of generic type E. The list itself is passed in via the constructor,
     * and can be any implementation of the List interface. This list contains the data that the JList displays
     * the String representations of.
     */
    private List<E> urlList;

    /**
     * The constructor creates the JFrame and adds the JList GUI element, but does not make the frame visible on screen.
     * <p/>
     * I made this constructor protected because PersistedURLListFrame is an abstract class, so only subclass
     * implementations of this class will ever need to or be able to call the constructor - and using the narrowest
     * scope possible for methods is recommended practice as we've been taught in lectures. And because the class itself
     * is package local, subclasses outside of the package cannot call the constructor anyway.
     *
     * @param title          a string that contains the title for the frame.
     * @param data           a List of generic type E objects, to be displayed in the JList GUI
     * @param listenerObject an object that implements PersistedURLListFrameListener to receive listener events
     *                       when the user clicks on and selects an item in the list. Of generic type T.
     */
    protected PersistedURLListFrame(String title, final List<E> data, final PersistedURLListFrameListener listenerObject) {
        // perform some null checking...
        if (title == null) {
            throw new IllegalArgumentException("PersistedURLListFrame instances require a title string!");
        }

        if (data == null) {
            throw new IllegalArgumentException("PersistedURLListFrame instances require a List to display!");
        }

        if (listenerObject == null) {
            throw new IllegalArgumentException("PersistedURLListFrame instances require a PersistedURLListFrameListener instance.");
        }

        // create the window

        // set up the minimum size
        setMinimumSize(new Dimension(MINIMUM_WINDOW_WIDTH, MINIMUM_WINDOW_HEIGHT));

        // set the title to whatever we've been passed
        setTitle(title);

        // set listener reference
        persistedURLListFrameListener = listenerObject;

        // create the JList...
        jStringList = new JList<String>();

        // now add the selection listener, in an anonymous class
        jStringList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Get the new selection index
                int newSelection = jStringList.getSelectedIndex();

                // Do some basic bounds checking - we particularly don't want to accept -1 since this is a possible
                // selection index if nothing is selected, but is physically impossible to be an array index.
                if (newSelection >= 0 && newSelection < urlList.size()) {
                    // The selection is within sensible bounds, continue.
                    // Get the object (of generic type E) from the data list at the selected index
                    E selectedObject = urlList.get(newSelection);

                    // Call the relevant listener method, passing in the selected object as a parameter, along with the
                    // current instance of PersistedURLListFrame.
                    persistedURLListFrameListener.listSelectedObject(selectedObject);

                    // clear selection
                    jStringList.clearSelection();
                }
            }
        });

        // add a mouse listener so that we can detect right click events.
        jStringList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Check to see if it was a right click - if so, display the menu
                // I looked up how to do right click detection here (since I was not aware of SwingUtilities)...
                // http://stackoverflow.com/questions/4525733/java-mouse-event-right-click
                if (SwingUtilities.isRightMouseButton(e)) {
                    // the event was a right click!
                    // work out what we've clicked on first...
                    // get the click coordinates
                    Point locationOnScreen = e.getLocationOnScreen();

                    // convert the coordinate from being global screen-wide to being list-wide, so we can work out what it is within the list
                    // (method found at http://stackoverflow.com/questions/22726291/jlist-locationtoindex-always-returning-the-same-index-for-mouse-location-in )
                    SwingUtilities.convertPointFromScreen(locationOnScreen, jStringList);

                    // now get the selected for that point from the jStringList (made final for anonymous class use)
                    final int clickedIndex = jStringList.locationToIndex(locationOnScreen);

                    // validate the click index - if it's out of our bounds, give up and return.
                    if (clickedIndex < 0 || clickedIndex > (urlList.size() - 1)) {
                        // not valid! it's either less than 0 (the first list item),
                        // or greater than the last item in the list (i.e. the size - 1, because of zero indexing).
                        // return from this method.
                        return;
                    }

                    // okay, we're sure it's a valid selection. Now let's make a menu appear...
                    // now make a right click popup menu appear with the option to delete the item...
                    final JPopupMenu rightClickMenu = new JPopupMenu();

                    // create a delete item for our menu...
                    JMenuItem deleteItem = new JMenuItem("Delete");

                    // add an appropriate actionListener to run when the menu item is clicked.
                    deleteItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // the delete item's been clicked
                            // get the clicked item (via the clickedIndex)
                            E clickedItem = urlList.get(clickedIndex);

                            // now call the listener method, passing in the clickedItem...
                            listenerObject.deleteSelectedObject(clickedItem);

                            // close the menu!
                            rightClickMenu.setVisible(false);
                        }
                    });

                    // add the deleteItem to the menu...
                    rightClickMenu.add(deleteItem);

                    // set it's location to the click event's component and location (x & y) and show it...
                    // using the show method, as per... http://docs.oracle.com/javase/7/docs/api/javax/swing/JPopupMenu.html
                    // because it means that the menu gets hidden automatically when another list item is clicked.
                    rightClickMenu.show(e.getComponent(), e.getX(), e.getY());

                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // not of interest to our implementation, but implemented because it is required by interface.

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // not of interest to our implementation, but implemented because it is required by interface.

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // not of interest to our implementation, but implemented because it is required by interface.

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // not of interest to our implementation, but implemented because it is required by interface.

            }
        });

        // set the list we've been passed as the current data.
        setUrlList(data);

        // create a JScrollPane to make our list scrollable...
        JScrollPane scrollPane = new JScrollPane(jStringList);

        // add the scroll pane containing the list to our frame.
        add(scrollPane);

    }

    /**
     * A setter method that sets the value of urlList, and then, if the list GUI element is not null, performs a data
     * refresh.
     *
     * @param urlList A List of objects of generic type E, to be displayed in the list GUI.
     * @throws NullPointerException if the urlList parameter is null, a NullPointerException is thrown.
     */
    public void setUrlList(List<E> urlList) {
        // perform null value checking...
        if (urlList == null) {
            // urlList is null - throw relevant exception!
            throw new NullPointerException("setUrlList was passed a null value.");
        }

        // set urlList field.
        this.urlList = urlList;

        // check the JList object is not null...
        if (jStringList != null) {
            // clear the current list selection
            jStringList.clearSelection();

            // create a blank array of strings, as large as the urlList
            String[] titlesArray = new String[urlList.size()];

            // iterate over the urlList, calling toString on each entry, and adding that value to the appropriate
            // value in the array of strings
            for (int i = 0; i < urlList.size(); i++) {
                // get the string value, add it to the array of strings.
                titlesArray[i] = urlList.get(i).toString();
            }

            // and reload data using the array of strings we've just created.
            jStringList.setListData(titlesArray);
        }
    }

}
