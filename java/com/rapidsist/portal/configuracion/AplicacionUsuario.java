/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.configuracion;

import java.util.LinkedList;
import java.io.Serializable;

public class AplicacionUsuario implements Serializable{

	/**
	 * Clave de la aplicación.
	 */
	public String sCveAplicacion="";
	
	/**
	 * Nombre de la aplicación.
	 */
	public String sNomAplicacon ="";
	
	/**
	 * Url de la aplicación.
	 */
	public String sUrlAplicacion="";
	
	/**
	 * Lista de perfiles.
	 */
	public LinkedList listaPerfiles = new LinkedList();
}
