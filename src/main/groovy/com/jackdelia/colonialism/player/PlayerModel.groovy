package com.jackdelia.colonialism.player

class PlayerModel extends Observable {

    private String name

    void setName(String newNameValue) {
        if(name != newNameValue) {
            name = newNameValue
            setChanged()
            notifyObservers(newNameValue)
        }
    }

}
