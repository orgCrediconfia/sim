/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.herramientasconfiguracion.datos;

import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de tablas relacionadas para bitacorar.
 */
public class TablaBitacoraDAO extends Conexion2 implements OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros tomando como base el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//OBTIENE EL FILTRO DE BUSQUEDA
		String sFiltro = (String)parametros.getDefCampo("Filtro");

		if (sFiltro.equals("TablasDisponibles")){
			sSql = "SELECT * FROM RS_CONF_TABLA \n" +
					" WHERE CVE_TABLA NOT IN ( \n" +
							  " SELECT CVE_TABLA_BITACORA FROM RS_CONF_TABLA_BITACORA \n"+
							  "   WHERE CVE_TABLA='" + parametros.getDefCampo("CVE_TABLA") + "' \n" +
							  " ) \n" +
					" ORDER BY CVE_TABLA \n";
		}
		else if (sFiltro.equals("Todos")){
			sSql = "SELECT * FROM RS_CONF_TABLA_BITACORA \n" +
					" WHERE CVE_TABLA='" + parametros.getDefCampo("CVE_TABLA") + "' \n" +
					" ORDER BY CVE_TABLA_BITACORA \n";
		}
		ejecutaSql();
		return this.getConsultaLista();
	}
}