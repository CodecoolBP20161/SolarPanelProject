package com.codecool.models.forms;

import lombok.Data;

@Data
public class DeviceForm {
    private Integer inverterId;
    private Integer panelId;

    public DeviceForm(Integer inverterId, Integer panelId) {
        this.inverterId = inverterId;
        this.panelId = panelId;
    }

    public DeviceForm() {
    }

    public boolean isValid(){return this.getInverterId() != null || this.getPanelId() != null; }
}
