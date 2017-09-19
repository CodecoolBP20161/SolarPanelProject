package com.codecool.models.forms;

import lombok.Data;

@Data
public class DeviceForm {
    private String inverterId;
    private String panelId;

    public DeviceForm(String inverterId, String panelId) {
        this.inverterId = inverterId;
        this.panelId = panelId;
    }

    public DeviceForm() {
    }

    public boolean isValid(){return this.getInverterId() != null || this.getPanelId() != null; }
}
