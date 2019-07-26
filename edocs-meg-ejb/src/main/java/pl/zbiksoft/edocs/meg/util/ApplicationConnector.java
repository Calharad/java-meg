/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.util;

/**
 *
 * @author ZbikKomp
 */
public class ApplicationConnector {
    
    private static Boolean isController;
    
    private static Boolean isMonitoring;
    
    public static boolean isControllerAvailable() {
        if (isController == null) {
            isController = DomainConfig.Application.getController() != null;
        }
        return isController;
    }
    
    public static boolean isMonitoringAvailable() {
        if (isMonitoring == null) {
            isMonitoring = DomainConfig.Application.getMonitoring()!= null;
        }
        return isMonitoring;
    }
    
    public static final String PORT = DomainConfig.getNetworkPort();
    
    public static final String LOCALHOST = "http://localhost:" + PORT + "/";
    
}
