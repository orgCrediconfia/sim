/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para las actividades o requisitos del préstamo.
 */
 
public class SimPrestamoActividadEstatusDAO extends Conexion2 implements OperacionConsultaRegistro {

	
	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
	
	
		sSql = " SELECT \n"+
		       " ID_ETAPA_PRESTAMO \n"+
		       " FROM SIM_PRESTAMO \n"+
		       " WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
		       " AND CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
		       " AND ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n";
		ejecutaSql();
		if(rs.next()){
			parametros.addDefCampo("ID_ETAPA_PRESTAMO",rs.getString("ID_ETAPA_PRESTAMO"));
		}
	
		sSql =  "SELECT COUNT (*) ESTATUS FROM \n"+
			"( \n"+
			"SELECT DISTINCT \n"+
			"ESTATUS \n"+
			"FROM SIM_PRESTAMO_ETAPA \n"+
			"WHERE ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			"AND CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND ID_ETAPA_PRESTAMO = '" + (String)parametros.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
			"AND ESTATUS = 'Registrada' \n"+
			") \n";
			
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}