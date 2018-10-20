package com.jackdelia.colonialism.player

import javax.swing.*
import java.awt.Graphics

class PlayerLabelView extends JLabel implements Observer {

    String text
    Observable observable

    void subscribe() {
        this.observable.addObserver(this)
    }

    void unsubscribe() {
        this.observable.deleteObserver(this)
    }

    @Override
    void update(Observable o, Object arg) {

        def nameValue = (String) arg

        text = "$nameValue"

        repaint()
    }
}
