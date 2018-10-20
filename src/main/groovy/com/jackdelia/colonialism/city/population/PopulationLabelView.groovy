package com.jackdelia.colonialism.city.population

import javax.swing.JLabel

class PopulationLabelView extends JLabel implements Observer {

    String text
    Observable observable

    PopulationLabelView() {
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

        def populationValue = (double) arg

        text = "Population: $populationValue"

        repaint()
    }
}
