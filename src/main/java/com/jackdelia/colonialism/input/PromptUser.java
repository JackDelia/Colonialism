package com.jackdelia.colonialism.input;

/**
 * <p>Facade for Prompting the User for Input</p>
 * <p>
 * Change Log:
 * </p>
 * <ul>
 *     <li>v0.5 - Initial File Creation. Extracted logic from {@link com.jackdelia.colonialism.Game}.</li>
 * </ul>
 *
 * @author <a href="mailto:andrewparise1994@gmail.com">Andrew Parise</a>
 * @since 0.5
 * @version 0.5
 */
public class PromptUser {

    private static IPromptUser instance;

    /**
     * Static call-through for getting text input from the User
     * @param promptText text to display on the prompt window
     * @return the String entered by the User
     */
    public static String forText(String promptText) {
        return getInstance().forText(promptText);
    }

    public static void withText(String modalText) {
        getInstance().withText(modalText);
    }

    /**
     * Factory Method to Support Lazy Loading the underlying Implementation as a singleton
     * @return a concrete implementation of IPromptUser
     */
    private static IPromptUser getInstance() {
        if(instance == null) {
           instance = new PromptUserSwingImpl();
        }

        return instance;
    }


}
