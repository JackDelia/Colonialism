package com.jackdelia.colonialism.city.population

class PopulationModel extends Observable {

    private double population

    PopulationModel() {
        population = 0
    }

    void setPopulation(double newPopulation) {
        if(population != newPopulation) {
            population = newPopulation
            setChanged()
            notifyObservers(newPopulation)
        }
    }

}
