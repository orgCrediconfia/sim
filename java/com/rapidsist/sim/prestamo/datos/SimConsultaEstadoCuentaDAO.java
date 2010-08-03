/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para la consulta de la tabla de amortización.
 */
 
public class SimConsultaEstadoCuentaDAO extends Conexion2 implements OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
			
			sSql = "SELECT \n"+
				   "CVE_PRESTAMO, \n"+
				   "ID_PRODUCTO, \n"+
				   "NOMBRE, \n"+
				   "NUM_CICLO \n"+
				   "FROM V_CREDITO \n"+
				   "WHERE 1 = 1 \n"+
				   "AND APLICA_A != 'INDIVIDUAL_GRUPO' \n";
				   
			if (!parametros.getDefCampo("CVE_PRESTAMO").equals("")) {
				sSql = sSql + " AND CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
			}
			
			if (!parametros.getDefCampo("NOMBRE").equals("")) {
				sSql = sSql + " AND UPPER(NOMBRE) LIKE '%" + ((String) parametros.getDefCampo("NOMBRE")).toUpperCase()  + "%' \n";
			}
			
			sSql = sSql + "ORDER BY ID_PRESTAMO \n";
		
		ejecutaSql();
		return getConsultaLista();
	}
}