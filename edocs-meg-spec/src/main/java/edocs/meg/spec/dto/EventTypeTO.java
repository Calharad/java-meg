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
public class EventTypeTO {
    
    private Integer id;
        
    private String acronim;
    
    private String shortDescription;

    public EventTypeTO() {
    }

    public EventTypeTO(Integer id) {
        this.id = id;
    }

    public EventTypeTO(Integer id, String acronim, String shortDescription) {
        this.id = id;
        this.acronim = acronim;
        this.shortDescription = shortDescription;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAcronim() {
        return acronim;
    }

    public void setAcronim(String acronim) {
        this.acronim = acronim;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    
    //</editor-fold>
    
}
