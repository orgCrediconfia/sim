/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.Iterator;
import com.rapidsist.sim.grupo.datos.SimGrupoIntegranteDAO;


/**
 * Administra los accesos a la base de datos para la consulta del grupo al que se le asignará el crédito.
 */
 
public class SimPrestamoAsignaGrupoDAO extends Conexion2 implements OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		sSql =  "SELECT DISTINCT \n"+
			"ID_GRUPO, \n"+
			"NOM_GRUPO, \n"+
			"CVE_ASESOR_CREDITO, \n"+
			"NOM_ASESOR_CREDITO, \n"+
			"ID_SUCURSAL, \n"+
			"NOM_SUCURSAL \n"+
			"FROM ( \n"+
			"SELECT  \n"+
			"G.CVE_GPO_EMPRESA, \n"+
			"G.CVE_EMPRESA, \n"+
			"G.ID_GRUPO, \n"+
			"G.NOM_GRUPO, \n"+
			"G.ID_SUCURSAL, \n"+
			"S.NOM_SUCURSAL, \n"+
			"I.ID_INTEGRANTE, \n"+
			"P.CVE_ASESOR_CREDITO, \n"+
			"U.CVE_USUARIO, \n"+
			"A.NOM_COMPLETO NOM_ASESOR_CREDITO \n"+
			"FROM \n"+
			"SIM_GRUPO G, \n"+
			"SIM_GRUPO_INTEGRANTE I, \n"+
			"RS_GRAL_PERSONA P, \n"+
			"RS_GRAL_USUARIO U, \n"+
			"RS_GRAL_PERSONA A, \n"+
			"SIM_CAT_SUCURSAL S \n"+
			"WHERE G.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+ 
			"AND G.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+ 
			"AND G.FECHA_BAJA_LOGICA IS NULL \n";
		
		if (parametros.getDefCampo("ID_GRUPO") != null) {
			sSql = sSql + "AND G.ID_GRUPO = '" + (String) parametros.getDefCampo("ID_GRUPO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_GRUPO") != null) {
			sSql = sSql + "AND UPPER(G.NOM_GRUPO) LIKE '%" + ((String) parametros.getDefCampo("NOM_GRUPO")).toUpperCase() + "%' \n";
		}
			
		sSql = sSql + "AND I.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n"+
			"AND I.CVE_EMPRESA = G.CVE_EMPRESA \n"+
			"AND I.ID_GRUPO = G.ID_GRUPO \n"+
			"AND I.FECHA_BAJA_LOGICA IS NULL \n"+
			"AND P.CVE_GPO_EMPRESA = I.CVE_GPO_EMPRESA \n"+
			"AND P.CVE_EMPRESA = I.CVE_EMPRESA \n"+
			"AND P.ID_PERSONA = I.ID_INTEGRANTE \n"+
			"AND U.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
			"AND U.CVE_EMPRESA = P.CVE_EMPRESA \n"+
			"AND U.CVE_USUARIO = P.CVE_ASESOR_CREDITO \n"+
			"AND A.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
			"AND A.CVE_EMPRESA = U.CVE_EMPRESA \n"+
			"AND A.ID_PERSONA = U.ID_PERSONA \n"+
			"AND S.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n"+
			"AND S.CVE_EMPRESA = G.CVE_EMPRESA \n"+
			"AND S.ID_SUCURSAL = G.ID_SUCURSAL) \n"+
			"ORDER BY ID_GRUPO  \n";
		
		ejecutaSql();
		return getConsultaLista();
	}
}