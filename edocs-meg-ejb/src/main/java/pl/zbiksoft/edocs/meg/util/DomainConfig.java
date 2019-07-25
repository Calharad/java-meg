/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author ZbikKomp
 */
public class DomainConfig {

    public final static String DISC_DOMAIN_LOCATION = System.getProperty("com.sun.aas.instanceRoot");

    public static final String DOMAIN_XML_LOCATION = DISC_DOMAIN_LOCATION + "\\config\\domain.xml";

    private static Document xmlDomain;
    private static final Logger LOG = Logger.getLogger(DomainConfig.class.getName());

    static {
        try {
            xmlDomain = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(DOMAIN_XML_LOCATION);
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(DomainConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Node getConfig() {
        Node result = xmlDomain.getDocumentElement();
        result = getNodeByTagName(result.getChildNodes(), "configs");
        result = result.getChildNodes().item(1);
        return result;
    }

    public static Node getNetworkConfig() {
        Node result = getNodeByTagName(getConfig().getChildNodes(), "network-config");
        return result;
    }

    public static Node getNetworkListeners() {
        Node result = getNodeByTagName(getNetworkConfig().getChildNodes(), "network-listeners");
        return result;
    }

    public static String getNetworkPort() {
        Node result = getNodeByAttributeValue(getNetworkListeners().getChildNodes(), "name", "http-listener-1");
        result = result.getAttributes().getNamedItem("port");
        if (result == null) {
            LOG.log(Level.SEVERE, "Could not find http-listener-1 network listener");
            return null;
        }
        return result.getNodeValue();
    }

    public static List<String> getEndpoits(String appName) {
        Client client = ClientBuilder.newClient();
        WebTarget myTarget = client.target("http://localhost:" + getAdminPort() + "/management/domain/applications/application/"
                + appName + "/list-rest-endpoints");
        Response response = myTarget.request(MediaType.APPLICATION_JSON).get();
        response.bufferEntity();
        Map json = response.readEntity(Map.class);
        Map endpoints = (Map) ((Map) json.get("extraProperties")).get("endpoints");
        List<String> result = new ArrayList();
        endpoints.keySet().forEach(p -> result.add((String) p));
        return result;
    }

    public static String getAdminPort() {
        Node result = getNodeByAttributeValue(getNetworkListeners().getChildNodes(), "name", "admin-listener");
        result = result.getAttributes().getNamedItem("port");
        if (result == null) {
            LOG.log(Level.SEVERE, "Could not find admin-listener network listener");
            return null;
        }
        return result.getNodeValue();
    }

    public static List<String> getApplicationList() {
        return getApplicationRefs().stream().collect(Collectors.toMap((n -> n.getAttribute("ref")), (n -> n.getAttribute("enabled"))))
                .entrySet().stream().filter(x -> x.getValue().equals("")).map(x -> x.getKey())
                .collect(Collectors.toList());
    }

    public static NodeList getApplicationListNode() {
        return xmlDomain.getDocumentElement().getElementsByTagName("applications").item(0).getChildNodes();
    }

    public static String getURLString(String port, String endpoint) {
        return "http://localhost:" + port + "/" + endpoint;
    }
    
    public static Node getServerRefs() {
        return getNodeByTagNameAndAttributeValue(getNodeByTagName(xmlDomain.getDocumentElement().getChildNodes(), "servers").getChildNodes(), 
                "server", "config-ref", "server-config");
    }
    
    public static List<Element> getApplicationRefs() {
        List<Element> result = new ArrayList<>();
        NodeList nl = getServerRefs().getChildNodes();
        
        for(int i=0; i<nl.getLength();i++) {
            Node n = nl.item(i);
            if(n instanceof Element) {
                if("application-ref".equals(((Element) n).getTagName())) {
                    result.add((Element)n);
                }
            }
        }
        return result;
    }

    private static Node getNodeByTagName(NodeList list, String tag) {
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i).getNodeName().equals(tag)) {
                return list.item(i);
            }
        }
        return null;
    }
    
    private static Node getNodeByAttributeValue(NodeList list, String attributeName, String attributeValue) {
        for (int i = 0; i < list.getLength(); i++) {
            Node n = list.item(i);
            if (n instanceof Element && n.getAttributes().getNamedItem(attributeName).getNodeValue().equals(attributeValue)) {
                return list.item(i);
            }
        }
        return null;
    }

    private static Node getNodeByTagNameAndAttributeValue(NodeList list, String tag, String attributeName, String attributeValue) {
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i).getNodeName().equals(tag) && list.item(i).getAttributes().getNamedItem(attributeName).getNodeValue().equals(attributeValue)) {
                return list.item(i);
            }
        }
        return null;
    }

    public static class Application {

        public static String getController() {
            return getApplication("edocs-controller-ear");
        }
        
        public static String getMonitoring() {
            return getApplication("edocs-monitoring-ear");
        }
        
        private static String getApplication(String name) {
            List<String> apps = getApplicationList().stream().filter(a -> a.toLowerCase().startsWith(name))
                    .collect(Collectors.toList());
            if (apps.size() > 0) {
                return apps.get(0);
            } else {
                return null;
            }
        }
    }

}
