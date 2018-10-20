package com.jackdelia.colonialism.city.population

import javax.swing.JPanel

class PopulationController implements Observer {

    PopulationModel populationModel
    PopulationLabelView populationLabelView
    Observable observable

    PopulationController() {
        populationModel = new PopulationModel()
        populationLabelView = new PopulationLabelView(observable: populationModel)
        populationLabelView.subscribe()
    }

    void setPopulation(double populationValue) {
        populationModel.population = populationValue
    }

    void addViewToPanel(JPanel panel) {
        panel?.add(populationLabelView)
    }

    void subscribe() {
        this.observable?.addObserver(this)
    }

    void unsubscribe() {
        this.observable?.deleteObserver(this)
    }

    @Override
    void update(Observable o, Object arg) {
        def populationValue = (double) arg
        setPopulation(populationValue)
    }
}
