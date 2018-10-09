package com.jackdelia.colonialism.input;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;

/**
 * JavaX Swing Implementation of Prompting for User Input
 */
public class PromptUserSwingImpl implements IPromptUser {

    public String forText(String promptText) {

        // validate user input
        if(StringUtils.isEmpty(promptText)) {
            promptText = "";
        }

        return JOptionPane.showInputDialog(promptText);
    }

    @Override
    public void withText(String modalText) {
        JOptionPane.showMessageDialog(null, modalText);
    }


}
