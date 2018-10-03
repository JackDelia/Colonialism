package com.jackdelia.colonialism.input;

public interface IPromptUser {

    /**
     * Abstraction for Prompting the User for Text
     * @param promptText the Message Text to display on the Prompt Window
     * @return the String entered by the User
     */
    String forText(String promptText);

}
