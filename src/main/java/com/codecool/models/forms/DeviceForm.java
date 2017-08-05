package com.codecool.models.forms;

import lombok.Data;

@Data
public class DeviceForm {
    private String inverterId;
    private String panelId;


    public boolean isValid(){return this.getInverterId() != null || this.getPanelId() != null; }
}
