/*
 * Generated by XDoclet - Do not edit!
 */
package com.rapidsist.portal.configuracion;

/**
 * Home interface for PortalSL.
 * @xdoclet-generated at 22-abril-05 05:33 PM
 */
public interface PortalSLHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/PortalSL";
   public static final String JNDI_NAME="PortalSL";

   public com.rapidsist.portal.configuracion.PortalSL create()
      throws javax.ejb.CreateException,java.rmi.RemoteException;

}
