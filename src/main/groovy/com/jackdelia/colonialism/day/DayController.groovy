package com.jackdelia.colonialism.day

import javax.swing.*

class DayController {

    DayModel dayModel
    DayLabelView dayLabelView

    DayController() {
        dayModel = new DayModel()
        dayLabelView = new DayLabelView(observable: dayModel)
        dayLabelView.subscribe()
    }

    void setDay(int dayValue) {
        dayModel.day = dayValue
    }

    void addViewToPanel(JPanel panel) {
        panel?.add(dayLabelView)
    }

}
