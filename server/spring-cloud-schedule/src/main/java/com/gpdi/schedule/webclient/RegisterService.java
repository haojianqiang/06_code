/**
 * RegisterService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.gpdi.schedule.webclient;

public interface RegisterService extends javax.xml.rpc.Service {
    public String getRegisterAddress();

    public Register_PortType getRegister() throws javax.xml.rpc.ServiceException;

    public Register_PortType getRegister(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
