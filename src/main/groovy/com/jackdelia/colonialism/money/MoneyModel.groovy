package com.jackdelia.colonialism.money

import com.jackdelia.colonialism.currency.Funding

class MoneyModel extends Observable {

    private double money

    MoneyModel() {
        money = Funding.DEFAULT_STARTING_CASH
    }

    void setMoney(double newMoneyValue) {
        if(money != newMoneyValue) {
            money = newMoneyValue
            setChanged()
            notifyObservers(newMoneyValue)
        }
    }

}
