package com.jackdelia.colonialism.money

import javax.swing.JLabel

class MoneyLabelView extends JLabel implements Observer {

    String text
    Observable observable

    MoneyLabelView() {
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

        def moneyValue = (double) arg

        text = "Money: $moneyValue"

        repaint()
    }
}
