/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para elegir una caja.
 */
 
public class SimCajaDAO extends Conexion2 implements OperacionConsultaTabla {
	
	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  "SELECT \n"+
				"C.CVE_GPO_EMPRESA, \n"+
				"C.CVE_EMPRESA, \n"+
				"C.ID_SUCURSAL||'-'||C.ID_CAJA ID_CAJA, \n"+
				"S.NOM_SUCURSAL ||' - '|| 'CAJA ' || C.ID_CAJA NOM_CAJA \n"+
				"FROM SIM_SUCURSAL_CAJA C, \n"+
				"SIM_CAT_SUCURSAL S, \n"+
				"SIM_USUARIO_ACCESO_SUCURSAL US \n"+
				"WHERE C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND S.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
				"AND S.CVE_EMPRESA = C.CVE_EMPRESA \n"+
				"AND S.ID_SUCURSAL = C.ID_SUCURSAL \n"+
				"AND US.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA \n"+
                "AND US.CVE_EMPRESA = S.CVE_EMPRESA \n"+
                "AND US.ID_SUCURSAL = S.ID_SUCURSAL \n"+
                "AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
				"ORDER BY NOM_CAJA \n";
		
		ejecutaSql();
		return getConsultaLista();
	}
}