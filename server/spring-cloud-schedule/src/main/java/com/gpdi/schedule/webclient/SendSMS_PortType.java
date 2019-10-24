/**
 * SendSMS_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.gpdi.schedule.webclient;

public interface SendSMS_PortType extends java.rmi.Remote {
    public String sendSMS(String uc, String pw, String rand, String[] callee, String isreturn, String cont, int msgid, String connID) throws java.rmi.RemoteException;
    public String sendSMSV2(String uc, String pw, String rand, String[] callee, String isreturn, String cont, int msgID, String connID, int charset) throws java.rmi.RemoteException;
    public String sendSMSV3(String uc, String pw, String rand, String[] callee, String isreturn, String cont, int msgID, String connID, int charset, String signCont) throws java.rmi.RemoteException;
    public String qryOffLineSMS(String ucNumber, String ucPinNum, String rand, String connID) throws java.rmi.RemoteException;
    public String qrySendSMSStat(String ucNumber, int smsFlag, String ucPinNum, String rand, String connID) throws java.rmi.RemoteException;
}
