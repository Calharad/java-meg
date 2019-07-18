/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edocs.meg.spec.dto.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Tomek
 */
public class ControllerEventTO implements Serializable {
    private int eventTypeId;
    private int eventNumber;

    private int operatorBarCode;
    private String operatorBarCodeStr;
    private int groupId;

    private String plcTime;
    private long productionQuantity;

    private int machineId;

    private List<ControllerEventParameterTO> additionalParameters;

    public ControllerEventTO() {
        additionalParameters = new ArrayList<ControllerEventParameterTO>();
    }

    public int getEventNumber() {
        return eventNumber;
    }

    public void setEventNumber(int eventNumber) {
        this.eventNumber = eventNumber;
    }

    public int getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(int eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public int getOperatorBarCode() {
        return operatorBarCode;
    }

    public void setOperatorBarCode(int operatorBarCode) {
        this.operatorBarCode = operatorBarCode;
    }

    public String getPlcTime() {
        return plcTime;
    }

    public void setPlcTime(String plcTime) {
        this.plcTime = plcTime;
    }

    public long getProductionQuantity() {
        return productionQuantity;
    }

    public void setProductionQuantity(long productionQuantity) {
        this.productionQuantity = productionQuantity;
    }

    public List<ControllerEventParameterTO> getAdditionalParameters() {
        return additionalParameters;
    }

    public void setAdditionalParameters(List<ControllerEventParameterTO> additionalParameters) {
        this.additionalParameters = additionalParameters;
    }

    public String getOperatorBarCodeStr() {
        return operatorBarCodeStr;
    }

    public void setOperatorBarCodeStr(String operatorBarCodeStr) {
        this.operatorBarCodeStr = operatorBarCodeStr;
    }

    @Override
    public String toString() {
        return "ControllerEventTO{" + "eventTypeId=" + eventTypeId + ", eventNumber=" + eventNumber + ", operatorBarCode=" + operatorBarCode + ", operatorBarCodeStr=" + operatorBarCodeStr + ", groupId=" + groupId + ", plcTime=" + plcTime + ", productionQuantity=" + productionQuantity + ", machineId=" + machineId + ", additionalParameters=" + additionalParameters + '}';
    }
}
