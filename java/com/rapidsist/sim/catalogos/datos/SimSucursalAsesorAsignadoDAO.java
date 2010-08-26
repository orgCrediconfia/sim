/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.catalogos.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra los asesores de las sucursales.
 */
 
public class SimSucursalAsesorAsignadoDAO extends Conexion2 implements OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  "SELECT \n"+
				"U.CVE_GPO_EMPRESA, \n"+
				"U.CVE_EMPRESA, \n"+
				"U.CVE_USUARIO, \n"+
				"U.CVE_PUESTO, \n"+
				"P.NOM_COMPLETO, \n"+
				"P.ID_SUCURSAL \n"+
				"FROM \n"+
				"RS_GRAL_USUARIO U, \n"+
				"RS_GRAL_PERSONA P \n"+
				"WHERE U.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND U.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND U.CVE_PUESTO = 'AseSuc' \n"+
				"AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
				"AND P.CVE_EMPRESA = U.CVE_EMPRESA \n"+
				"AND P.ID_PERSONA = U.ID_PERSONA \n"+
				"AND P.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n";
		
		ejecutaSql();
		return getConsultaLista();
	}
}