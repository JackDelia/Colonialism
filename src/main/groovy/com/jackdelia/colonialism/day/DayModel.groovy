package com.jackdelia.colonialism.day

class DayModel extends Observable {

    private int day

    void setDay(int newDayValue) {
        if(day != newDayValue) {
            day = newDayValue
            setChanged()
            notifyObservers(newDayValue)
        }
    }

}
