/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.catalogos;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Mensaje;
import java.io.Serializable;

/**
 * Resultado de la ejecución de los métodos alta, baja y modificación de la clase DAO
 * sobre la base de datos.
 */
public class ResultadoCatalogo implements Serializable{
	/**
	 * Posibles valores generados por los métodos alta, baja o módificación
	 */
	public Registro Resultado = null;
	/**
	 * Posible mensaje generado al ejecutar los métodos de alta, baja o modificación.
	 */
	public Mensaje mensaje = new Mensaje();
}