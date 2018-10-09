package com.jackdelia.colonialism.input;

public interface IPromptUser {

    /**
     * Abstraction for Prompting the User for Text
     * @param promptText the Message Text to display on the Prompt Window
     * @return the String entered by the User
     */
    String forText(String promptText);

    /**
     * Abstraction for Providing the User with some Text
     * @param modalText the text to display on the Modal window
     */
    void withText(String modalText);



}
