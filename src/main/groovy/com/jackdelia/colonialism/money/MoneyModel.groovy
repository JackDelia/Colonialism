package com.jackdelia.colonialism.money

class MoneyModel extends Observable {

    private double money

    MoneyModel() {
        money = 0
    }

    void setMoney(double newMoneyValue) {
        if(money != newMoneyValue) {
            money = newMoneyValue
            setChanged()
            notifyObservers(newMoneyValue)
        }
    }

}
