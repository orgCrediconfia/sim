/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.configuracion;

import java.io.Serializable;

/**
 * Almacena los permisos que se pueden tener para una función (alta, baja, cambio, consulta).
 */
public class Permisos implements Serializable{

	/**
	 * Bandera que define el permiso de alta.
	 */
	public boolean bAlta = false;

	/**
	 * Bandera que define el permiso de baja.
	 */
	public boolean bBaja = false;

	/**
	 * Bandera que define el permiso de modificación.
	 */
	public boolean bModificacion = false;

	/**
	 * Bandera que define el permiso de consulta.
	 */
	public boolean bConsulta = false;

	/**
	 * Bandera que define el permiso de consulta de bitácora.
	 */
	public boolean bBitacora = false;

}