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
import java.util.ListIterator;
import java.util.Iterator;

/**
 * Administra los accesos a la base de datos para la consulta del cliente al que se le asignará el crédito.
 */
 
public class SimPrestamoAsignaClienteDAO extends Conexion2 implements OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		sSql =  "SELECT \n"+
				"P.CVE_GPO_EMPRESA, \n"+
				"P.CVE_EMPRESA, \n"+
				"P.ID_PERSONA, \n"+
				"P.NOM_COMPLETO, \n"+
				"P.CVE_ASESOR_CREDITO, \n"+
				"P.ID_SUCURSAL, \n"+
				"S.NOM_SUCURSAL, \n"+
				"NA.NOM_COMPLETO NOM_ASESOR_CREDITO, \n"+
				"TP.CVE_TIPO_PERSONA \n"+
				"FROM \n"+
				"RS_GRAL_PERSONA P, \n"+
				"SIM_TIPO_PERSONA TP, \n"+
				"RS_GRAL_USUARIO U, \n"+
				"RS_GRAL_PERSONA NA, \n"+
				"SIM_CAT_SUCURSAL S \n"+
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND P.LISTA_NEGRA = 'F' \n"+
				"AND TP.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND TP.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND TP.ID_PERSONA = P.ID_PERSONA \n"+
				"AND TP.CVE_TIPO_PERSONA = 'CLIENTE' \n"+
				"AND U.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND U.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND U.CVE_USUARIO (+)= P.CVE_ASESOR_CREDITO \n"+
				"AND NA.CVE_GPO_EMPRESA (+)= U.CVE_GPO_EMPRESA \n"+
				"AND NA.CVE_EMPRESA (+)= U.CVE_EMPRESA \n"+
				"AND NA.ID_PERSONA (+)= U.ID_PERSONA \n"+
				"AND S.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND S.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND S.ID_SUCURSAL = P.ID_SUCURSAL \n";
			
		if (parametros.getDefCampo("ID_PERSONA") != null) {
			sSql = sSql + "AND P.ID_PERSONA = '" + (String) parametros.getDefCampo("ID_PERSONA") + "' \n";
		}
		if (parametros.getDefCampo("NOM_COMPLETO") != null) {
			sSql = sSql + "AND UPPER(P.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase() + "%' \n";
		}
		
		sSql = sSql + "ORDER BY P.ID_PERSONA \n";
		
		ejecutaSql();
		return getConsultaLista();
	}
}