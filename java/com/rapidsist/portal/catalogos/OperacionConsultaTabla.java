/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.catalogos;

import com.rapidsist.comun.bd.Registro;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Define las operaciones básicas que se pueden realizar en una clase DAO
 */
public interface OperacionConsultaTabla {
	/**
	 * Obtiene un conjunto de registros en base a un filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados. En la lista de parámetros se envía por default el parámetro
	 *  Filtro y CVE_GPO_EMPRESA.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException;
}