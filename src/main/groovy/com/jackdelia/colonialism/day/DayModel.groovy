package com.jackdelia.colonialism.day

class DayModel extends Observable {

    private int day

    DayModel() {
        day = 0
    }

    void setDay(int newDayValue) {
        if(day != newDayValue) {
            day = newDayValue
            setChanged()
            notifyObservers(newDayValue)
        }
    }

}
