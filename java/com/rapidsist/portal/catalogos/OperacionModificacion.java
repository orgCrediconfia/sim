/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.catalogos;

import com.rapidsist.comun.bd.Registro;
import java.sql.SQLException;

/**
 * Define las operaciones básicas que se pueden realizar en una clase DAO
 */
public interface OperacionModificacion {

	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Clave del mensaje en caso de que la operación fallara<br>
	 * 0 - Si la operación tuvo éxito.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException;
}