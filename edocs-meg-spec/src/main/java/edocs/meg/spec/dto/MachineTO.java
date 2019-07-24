/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edocs.meg.spec.dto;

/**
 *
 * @author ZbikKomp
 */
public class MachineTO {
    
    private Integer id;
    
    private String shortDescription;
    
    private String longComment;
    
    private String idDescription;
    
    private String objectName;

    public MachineTO() {
    }

    public MachineTO(Integer id) {
        this.id = id;
    }

    public MachineTO(String shortDescription, String longComment) {
        this.shortDescription = shortDescription;
        this.longComment = longComment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongComment() {
        return longComment;
    }

    public void setLongComment(String longComment) {
        this.longComment = longComment;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getIdDescription() {
        return idDescription;
    }

    public void setIdDescription(String idDescription) {
        this.idDescription = idDescription;
    }
}
