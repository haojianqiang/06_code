/**
 * Register_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.gpdi.web.webclient;

public interface Register_PortType extends java.rmi.Remote {
    public String getRandom() throws java.rmi.RemoteException;
    public String setCallBackAddr(String uc, String pw, String rand, String url) throws java.rmi.RemoteException;
    public String setCallBackAddrV2(String uc, String pw, String rand, String url, String version) throws java.rmi.RemoteException;
}
