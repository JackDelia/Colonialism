package com.jackdelia.colonialism.day

import javax.swing.*

class DayLabelView extends JLabel implements Observer {

    String text
    Observable observable

    DayLabelView() {
        text = ''
    }

    void subscribe() {
        this.observable.addObserver(this)
    }

    void unsubscribe() {
        this.observable.deleteObserver(this)
    }

    @Override
    void update(Observable o, Object arg) {

        def dayValue = (double) arg

        text = "Day: $dayValue"

        repaint()
    }
}
