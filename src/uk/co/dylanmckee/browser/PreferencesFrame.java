package uk.co.dylanmckee.browser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * A class that creates and manages the user preferences window, where the user's home URL can be set.
 * <p/>
 * In the MVC design pattern, this class acts as a view class, being unaware of the content it is displaying, and
 * handing data over when collected from the user to an appropriate model class / user interaction over to a relevant
 * listener object.
 *
 * @author Dylan McKee
 *         Created 07/03/15
 */
// scope here is deliberately package local - because it's only ever used within the Browser package.
class PreferencesFrame extends JFrame {

    /**
     * A constant string that holds the name of the preferences window, for display in the title bar.
     */
    private static final String PREFERENCES_WINDOW_TITLE = "Preferences";

    /**
     * A constant defining the length of the URL text field, in characters.
     */
    private static final int URL_FIELD_LENGTH = 35;

    /**
     * A constant String with the text labelling the Homepage URL field.
     */
    private static final String HOMEPAGE_URL_LABEL_TEXT = "Homepage URL";

    /**
     * A constant String with the text labelling the Save homepage URL button.
     */
    private static final String SAVE_BUTTON_TEXT = "Save";

    /**
     * A constant String with the text labelling the Clear History button.
     */
    private static final String CLEAR_HISTORY_BUTTON_TEXT = "Clear History";

    /**
     * A constant String with the contents of the 'Are you sure you want to clear your history' message text.
     */
    private final static String CLEAR_HISTORY_WARNING_TEXT = "Are you sure you want to clear your history - this will delete all items forever?";

    /**
     * A listener object that must implement the PreferencesFrameListener, to receive listeners about items that have
     * happened in the frame, such as requests to clear browser history.
     * <p/>
     * The listener is set only once, in the constructor, and is therefore a final field.
     */
    private final PreferencesFrameListener preferencesFrameListener;

    /**
     * homePageUrlField is a private URLEntryField that will contain the current home URL of the user, and be editable to allow them to change it.
     * It needs to be a field so that it can be accessed from within methods other than the constructor, so that I can have a 'save' method
     * that reduced code duplication.
     * <p/>
     * The text field is instantiated only once, in the constructor, and therefore has a final modifier.
     */
    private final URLEntryField homePageUrlField;

    /**
     * The constructor creates and lays out the preferences window GUI in the JFrame, but does not (yet) make it visible.
     *
     * @param listener An object that implements PreferencesFrameListener, which will receive preference pane based
     *                 action event callbacks.
     */
    public PreferencesFrame(PreferencesFrameListener listener) {
        // Set the listener object to whatever we've been passed...
        this.preferencesFrameListener = listener;

        // Set the preferences window title
        setTitle(PREFERENCES_WINDOW_TITLE);

        // The browser window requires a flow layout, so everything goes next to each other initially (where possible).
        setLayout(new FlowLayout());

        // Get the saved homepage URL from the UserPreferences convenience class, so we can set our TextField's initial value.
        URL userHomeUrl = UserPreferences.getHomepageUrl();

        // Add a simple label to label the text field, so the the user knows what they're entering into.
        JLabel homePageFieldLabel = new JLabel();

        // Set the text.
        homePageFieldLabel.setText(HOMEPAGE_URL_LABEL_TEXT);

        // Add the label to the frame before the field that it's labelling...
        add(homePageFieldLabel);

        // Now create the URLEntryField instance, using an anonymous class to define our URLEntryFieldListener.
        homePageUrlField = new URLEntryField(new URLEntryFieldListener() {
            @Override
            public void urlFieldEnterKeyPressed() {
                // it's the enter key. Call the save method.
                saveHomepageUrl();
            }
        }, URL_FIELD_LENGTH);

        // Set the initial URL to the user's current home URL
        homePageUrlField.setUrl(userHomeUrl);

        // Add the URL text field to the frame...
        add(homePageUrlField);

        // Create the 'Save' button that users will click to save their new homepage preference
        JButton saveButton = new JButton(SAVE_BUTTON_TEXT);

        // Set an appropriate event handler...
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // call our save method.
                saveHomepageUrl();
            }
        });

        // Add the save button to the frame
        add(saveButton);

        // Create a 'Clear History' button to clear history items.
        JButton clearHistoryButton = new JButton(CLEAR_HISTORY_BUTTON_TEXT);

        // Set an appropriate event handler...
        clearHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // confirm that the user definitely wants to clear it first using the JOptionPane...
                int confirmation = JOptionPane.showConfirmDialog(PreferencesFrame.this, CLEAR_HISTORY_WARNING_TEXT);
                // only go ahead and clear if they've explicitly said yes...
                if (confirmation == JOptionPane.YES_OPTION) {
                    // okay, they've explicitly said yes.
                    // call the listener's clear history method.
                    preferencesFrameListener.clearBrowsingHistoryClicked();
                }
            }
        });

        // Add the clear history button to the frame
        add(clearHistoryButton);

        // Set the window's position relative to nothing, so it appears neatly in the center of the screen.
        setLocationRelativeTo(null);

        // Finally, pack the window.
        pack();

    }

    /**
     * A private method to firstly validate the contents of the url text field, and if it is valid,
     * then go ahead to save it as the user's new homepage URL.
     */
    private void saveHomepageUrl() {
        // get URL from the URLEntryField...
        URL desiredHomeUrl = homePageUrlField.getUrl();

        // if the URL is null, it's not a valid URL - return and do not save.
        if (desiredHomeUrl == null) {
            return;
        }

        // okay looks like we succeeded - now save it to the user preferences...
        UserPreferences.setHomepageUrl(desiredHomeUrl);

    }

}
