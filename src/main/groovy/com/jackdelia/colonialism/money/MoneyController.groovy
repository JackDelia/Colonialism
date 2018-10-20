package com.jackdelia.colonialism.money

import javax.swing.JPanel

class MoneyController implements Observer {

    MoneyModel moneyModel
    MoneyLabelView moneyLabelView
    Observable observable

    MoneyController() {
        moneyModel = new MoneyModel()
        moneyLabelView = new MoneyLabelView(observable: moneyModel)
        moneyLabelView.subscribe()
    }

    void setMoney(double moneyValue) {
        moneyModel.money = moneyValue
    }

    void addViewToPanel(JPanel panel) {
        panel?.add(moneyLabelView)
    }

    void subscribe() {
        this.observable?.addObserver(this)
    }

    void unsubscribe() {
        this.observable?.deleteObserver(this)
    }

    @Override
    void update(Observable o, Object arg) {
        def moneyValue = (double) arg
        setMoney(moneyValue)
    }
}
