/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.personas.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para la paginación del catálogo de personas.
 */
 
public class SimPersonaPaginacionDAO extends Conexion2 implements OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		sSql =  "SELECT DISTINCT \n"+
			"P.ID_PERSONA, \n"+
			"P.NOM_COMPLETO, \n"+
			"P.ID_SUCURSAL, \n"+
			"S.NOM_SUCURSAL \n"+
			"FROM \n"+
			"(SELECT * FROM RS_GRAL_PERSONA ORDER BY ID_PERSONA) P, \n"+
			"(SELECT * FROM SIM_TIPO_PERSONA ORDER BY ID_PERSONA) TP, \n"+
			"      SIM_CAT_SUCURSAL S, \n"+
			"      SIM_USUARIO_ACCESO_SUCURSAL US \n"+
			"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND TP.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
			"AND TP.CVE_EMPRESA = P.CVE_EMPRESA \n"+
			"AND TP.ID_PERSONA = P.ID_PERSONA \n"+
			"AND P.FECHA_BAJA_LOGICA IS NULL \n"+
			"AND TP.CVE_TIPO_PERSONA != 'EXCONYUGE' \n"+
			"AND S.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
			"AND S.CVE_EMPRESA = P.CVE_EMPRESA \n"+
			"AND S.ID_SUCURSAL = P.ID_SUCURSAL \n"+
			"AND US.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
			"AND US.CVE_EMPRESA = P.CVE_EMPRESA \n"+
			"AND US.ID_SUCURSAL = P.ID_SUCURSAL \n"+
			"AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
			" AND ROWNUM <= '"+ (String) parametros.getDefCampo("SUPERIOR") +"' \n";  
				
		if (parametros.getDefCampo("ID_PERSONA") != null) {
			sSql = sSql + "AND P.ID_PERSONA = '" + (String) parametros.getDefCampo("ID_PERSONA") + "' \n";
		}
		if (parametros.getDefCampo("NOM_COMPLETO") != null) {
			sSql = sSql + "AND UPPER(P.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase()  + "%' \n";
		}
		if (parametros.getDefCampo("RFC") != null) {
			sSql = sSql + " AND P.RFC LIKE '%" + (String) parametros.getDefCampo("RFC") + "%' \n";
		}
	
		sSql = sSql + " MINUS \n"+
						"SELECT DISTINCT \n"+
						"P.ID_PERSONA, \n"+
						"P.NOM_COMPLETO, \n"+
						"P.ID_SUCURSAL, \n"+
						"S.NOM_SUCURSAL \n"+
						"FROM \n"+
						"(SELECT * FROM RS_GRAL_PERSONA ORDER BY ID_PERSONA) P, \n"+
						"(SELECT * FROM SIM_TIPO_PERSONA ORDER BY ID_PERSONA) TP, \n"+
						"      SIM_CAT_SUCURSAL S, \n"+
						"      SIM_USUARIO_ACCESO_SUCURSAL US \n"+
						"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND TP.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
						"AND TP.CVE_EMPRESA = P.CVE_EMPRESA \n"+
						"AND TP.ID_PERSONA = P.ID_PERSONA \n"+
						"AND P.FECHA_BAJA_LOGICA IS NULL \n"+
						"AND TP.CVE_TIPO_PERSONA != 'EXCONYUGE' \n"+
						"AND S.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
						"AND S.CVE_EMPRESA = P.CVE_EMPRESA \n"+
						"AND S.ID_SUCURSAL = P.ID_SUCURSAL \n"+
						"AND US.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
						"AND US.CVE_EMPRESA = P.CVE_EMPRESA \n"+
						"AND US.ID_SUCURSAL = P.ID_SUCURSAL \n"+
						"AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
						" AND ROWNUM <= '"+ (String) parametros.getDefCampo("INFERIOR") +"' \n"; 
						
					if (parametros.getDefCampo("ID_PERSONA") != null) {
						sSql = sSql + "AND P.ID_PERSONA = '" + (String) parametros.getDefCampo("ID_PERSONA") + "' \n";
					}
					if (parametros.getDefCampo("NOM_COMPLETO") != null) {
						sSql = sSql + "AND UPPER(P.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase()  + "%' \n";
					}
					if (parametros.getDefCampo("RFC") != null) {
						sSql = sSql + " AND P.RFC LIKE '%" + (String) parametros.getDefCampo("RFC") + "%' \n";
					}
						
				
		sSql = sSql + " ORDER BY ID_PERSONA \n";
		
		System.out.println("paginacion de personas"+sSql);
	
		ejecutaSql();
		return getConsultaLista();
	}

	/**
	 * Obtiene un registro tomando como base la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		
		sSql =	"SELECT TRUNC (HOJAS) PAGINAS FROM ( \n"+
				"SELECT PAGINACION / 100 AS HOJAS FROM ( \n"+
				"SELECT COUNT(*) PAGINACION FROM \n"+
				"( \n"+
				"SELECT DISTINCT \n"+
				"P.ID_PERSONA, \n"+
				"P.NOM_COMPLETO, \n"+
				"P.ID_SUCURSAL, \n"+
				"S.NOM_SUCURSAL \n"+
				"FROM RS_GRAL_PERSONA P, \n"+
				"      SIM_TIPO_PERSONA TP, \n"+
				"      SIM_CAT_SUCURSAL S, \n"+
				"      SIM_USUARIO_ACCESO_SUCURSAL US \n"+
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND TP.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND TP.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND TP.ID_PERSONA = P.ID_PERSONA \n"+
				"AND P.FECHA_BAJA_LOGICA IS NULL \n"+
				"AND TP.CVE_TIPO_PERSONA != 'EXCONYUGE' \n"+
				"AND S.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND S.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND S.ID_SUCURSAL = P.ID_SUCURSAL \n"+
				"AND US.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND US.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND US.ID_SUCURSAL = P.ID_SUCURSAL \n"+
				"AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n";
				
			if (parametros.getDefCampo("ID_PERSONA") != null) {
				sSql = sSql + "AND P.ID_PERSONA = '" + (String) parametros.getDefCampo("ID_PERSONA") + "' \n";
			}
			if (parametros.getDefCampo("NOM_COMPLETO") != null) {
				sSql = sSql + "AND UPPER(P.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase()  + "%' \n";
			}
			if (parametros.getDefCampo("RFC") != null) {
				sSql = sSql + " AND P.RFC LIKE '%" + (String) parametros.getDefCampo("RFC") + "%' \n";
			}
		
			sSql = sSql +	" ))) \n";
			
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}