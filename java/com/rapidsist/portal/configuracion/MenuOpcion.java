/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */


package com.rapidsist.portal.configuracion;

import java.io.Serializable;

/**
 * Almacena los atributos de una opción de un menú.
 */
public class MenuOpcion implements Serializable{

	/**
	 * Clave de opción de menú.
	 */
	public String sIdOpcion = "";

	/**
	 * Clave de función asociada a la opción.
	 */
	public String sIdFuncion = "";

	/**
	 * Nombre de la opción en el menú.
	 */
	public String sNomOpcion = "";

	/**
	 * Dirección a la que apunta la opción del menú.
	 */
	public String sUrl = "";

	/**
	 * Bandera que informa si la opción está bloqueada.
	 */
	public String sBloqueado = "";

	/**
	 * Clave de la opción padre.
	 */
	public String sIdOpcionPadre = "";

	/**
	 * Clave de la aplicación asociada a la opción.
	 */
	public String sIdAplicacion  = "";

	/**
	 * Posición de la opción dentro del menú.
	 */
	public String sNumPosicion   = "";

	/**
	 * Clave del folio de la bitácora.
	 */
	public String sFolBitacora   = "";

	/**
	 * Situación del registro.
	 */
	public String sSituacion     = "";

	/**
	 * Constructor del objeto MenuOpcion.
	 * @param sIdOpcion Clave opcion menu.
	 * @param sIdFuncion Clave función.
	 * @param sNomOpcion Nombre de la opción.
	 * @param sUrl Dirección URL.
	 */
	public MenuOpcion(String sIdOpcion, String sIdFuncion, String sNomOpcion, String sUrl){
		this.sIdOpcion = sIdOpcion;
		this.sIdFuncion = sIdFuncion;
		this.sNomOpcion = sNomOpcion;
		this.sUrl = sUrl;
	}
}