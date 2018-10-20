package com.jackdelia.colonialism.day

import javax.swing.*

class DayController {

    DayModel dayModel
    DayLabelView dayLabelView

    DayController() {
        this(null)
    }

    DayController(DayModel dayModel) {
        if(dayModel == null) {
            dayModel = new DayModel()
        }

        this.dayModel = dayModel
        dayLabelView = new DayLabelView(observable: this.dayModel)
        dayLabelView.subscribe()
    }

    void setDay(int dayValue) {
        dayModel.day = dayValue
    }

    void addViewToPanel(JPanel panel) {
        panel?.add(dayLabelView)
    }

}
