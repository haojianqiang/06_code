/**
 * SendSMSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.gpdi.web.webclient;

public class SendSMSServiceLocator extends org.apache.axis.client.Service implements SendSMSService {

    public SendSMSServiceLocator() {
    }


    public SendSMSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SendSMSServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SendSMS
    private String SendSMS_address = "http://202.105.212.146:8080/jboss-net/services/SendSMS";

    public String getSendSMSAddress() {
        return SendSMS_address;
    }

    // The WSDD service name defaults to the port name.
    private String SendSMSWSDDServiceName = "SendSMS";

    public String getSendSMSWSDDServiceName() {
        return SendSMSWSDDServiceName;
    }

    public void setSendSMSWSDDServiceName(String name) {
        SendSMSWSDDServiceName = name;
    }

    public SendSMS_PortType getSendSMS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SendSMS_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSendSMS(endpoint);
    }

    public SendSMS_PortType getSendSMS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            SendSMSSoapBindingStub _stub = new SendSMSSoapBindingStub(portAddress, this);
            _stub.setPortName(getSendSMSWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSendSMSEndpointAddress(String address) {
        SendSMS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (SendSMS_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                SendSMSSoapBindingStub _stub = new SendSMSSoapBindingStub(new java.net.URL(SendSMS_address), this);
                _stub.setPortName(getSendSMSWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("SendSMS".equals(inputPortName)) {
            return getSendSMS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://202.105.212.146:8080/jboss-net/services/SendSMS", "SendSMSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://202.105.212.146:8080/jboss-net/services/SendSMS", "SendSMS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("SendSMS".equals(portName)) {
            setSendSMSEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
