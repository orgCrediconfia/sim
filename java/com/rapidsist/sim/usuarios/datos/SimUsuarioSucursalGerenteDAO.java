/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.usuarios.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro; 
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para los usuarios del sistema.
 */
 
public class SimUsuarioSucursalGerenteDAO extends Conexion2 implements OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{

		sSql =   " SELECT \n"+
			 " 	U.CVE_GPO_EMPRESA, \n"+
			 " 	U.CVE_EMPRESA, \n"+
			 " 	U.CVE_USUARIO, \n"+
			 " 	U.ID_PERSONA, \n"+
			 " 	U.CVE_PUESTO, \n"+
			 " 	U.LOCALIDAD, \n" +
			 " 	U.NOM_ALIAS, \n" +
			 " 	U.PASSWORD, \n" +
			 " 	P.AP_PATERNO, \n" +
			 " 	P.AP_MATERNO, \n" +
			 " 	P.NOMBRE_1, \n" +
			 " 	P.NOMBRE_2, \n" +
			 " 	P.NOM_COMPLETO \n" +
			" FROM  RS_GRAL_USUARIO U, \n" +
			"	RS_GRAL_PERSONA P \n" +
			" WHERE U.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND U.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
			" AND P.CVE_GPO_EMPRESA (+)= U.CVE_GPO_EMPRESA \n" +
			" AND P.CVE_EMPRESA (+)= U.CVE_EMPRESA \n" +
			" AND P.ID_PERSONA (+)= U.ID_PERSONA \n" +
			" AND U.CVE_PUESTO = 'GerSuc' \n" +
			" AND P.FECHA_BAJA_LOGICA IS NULL \n" ;
			
		if (parametros.getDefCampo("CLAVE_USUARIO") != null) {
			sSql = sSql + " AND U.CVE_USUARIO = '" + (String) parametros.getDefCampo("CLAVE_USUARIO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_COMPLETO") != null) {
			sSql = sSql + " AND UPPER(P.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase() + "%' \n";
		}
		
		ejecutaSql();
		return getConsultaLista();
	}
}