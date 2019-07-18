/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edocs.meg.spec.dto.controller;

import java.io.Serializable;

/**
 *
 * @author Tom
 */
public class ControllerEventParameterTO implements Serializable {
    private int parameterId;
    private String parameterValue;

    public ControllerEventParameterTO() {
    }

    public int getParameterId() {
        return parameterId;
    }

    public void setParameterId(int parameterId) {
        this.parameterId = parameterId;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }
}
