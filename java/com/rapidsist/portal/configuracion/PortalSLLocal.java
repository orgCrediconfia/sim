/*
 * Generated by XDoclet - Do not edit!
 */
package com.rapidsist.portal.configuracion;

/**
 * Local interface for PortalSL.
 * @xdoclet-generated at 22-abril-05 05:33 PM
 */
public interface PortalSLLocal
   extends javax.ejb.EJBLocalObject
{
   /**
    * Verifica si el usuario tiene acceso a la funci�n.
    * @param sCveAplicacion Clave de la aplicaci�n.
    * @param sCvePerfil Clave del perfil.
	* @param sCveFuncion Clave de la funci�n
    * @return Los permisos asociados a una funci�n (alta, baja, cambio, consulta)
    */
   public com.rapidsist.portal.configuracion.Permisos validaAccesoFuncion( java.lang.String sCveAplicacion,java.lang.String sCvePerfil,java.lang.String sCveFuncion ) ;

   /**
    * Obtiene la informaci�n relacionada a una pagina
    * @param sCvePagina Clave de la p�gina
    * @return los datos asociados a la p�gina
    */
   public com.rapidsist.portal.configuracion.Pagina getPagina( java.lang.String sCvePagina ) ;

   /**
    * Obtiene la informaci�n relacionada a una funci�n.
    * @param sIdFuncion Clave de la funci�n.
    * @return Los datos asociados a una funci�n.
    */
   public com.rapidsist.portal.configuracion.Funcion getFuncion( java.lang.String sIdFuncion ) ;

   /**
    * Obtiene el men� asociado a un grupo - aplicaci�n
    * @param sCveAplicacion Clave de la aplicaci�n.
    * @param sCvePerfil Clave del perfil.
	* @param sNomAplicacionWeb Nombre de la aplicaci�n web
    * @return Men� del grupo aplicaci�n.
    */
   public java.lang.String getMenuAplicacion( java.lang.String sCveAplicacion,java.lang.String sCvePerfil,java.lang.String sNomAplicacionWeb ) ;

   /**
	* @param sClaveMensaje Clave del mensaje.
    */
   public com.rapidsist.portal.configuracion.Mensaje getMensaje( java.lang.String sClaveMensaje ) ;

   /**
    * Almacena los eventos del usuario en la base de datos
    * @param sCveGpoEmpresa Clave del grupo de la empresa.
    * @param sCveUsuario Clave del usuario.
    * @param sCvePagina Clave de la pagina.
    */
   public void almacenaPagina( java.lang.String sCveGpoEmpresa,java.lang.String sCveUsuario,java.lang.String sCvePagina ) ;

   /**
    * Obtiene la configuraci�n para un usuario.
    * @param sCvePortalDefault Clave del portal default.
	* @param sCveUsuario Clave del usuario.
	* @param sNomAplicacionWeb Nombre de la aplicaci�n web.
    * @return Configuraci�n del usuario.
    */
   public com.rapidsist.portal.configuracion.Usuario getConfiguracionUsuario( java.lang.String sCvePortalDefault,java.lang.String sCveUsuario,java.lang.String sNomAplicacionWeb ) ;

}