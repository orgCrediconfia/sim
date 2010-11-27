/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para comites.
 */
 
public class SimPrestamoRecuperadorDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro {

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
				   "U.CVE_GPO_EMPRESA, \n" +
				   "U.CVE_EMPRESA, \n" +
				   "U.CVE_USUARIO, \n"+
				   "P.NOM_COMPLETO \n"+	
				" FROM RS_GRAL_USUARIO U, \n"+
				"      RS_GRAL_PERSONA P \n"+
				" WHERE U.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND U.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
				" AND P.CVE_EMPRESA = U.CVE_EMPRESA \n"+
				" AND P.ID_PERSONA = U.ID_PERSONA \n";
			
			if (parametros.getDefCampo("CLAVE_USUARIO") != null) {
				sSql = sSql + " AND UPPER(U.CVE_USUARIO) LIKE '%" + ((String) parametros.getDefCampo("CLAVE_USUARIO")).toUpperCase()  + "%' \n";
			}
			
			if (parametros.getDefCampo("NOM_COMPLETO") != null) {
				sSql = sSql + " AND UPPER(P.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase()  + "%' \n";
			}
			
		ejecutaSql();
		return getConsultaLista();
	}
	
	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql =  "SELECT \n"+
				   "U.CVE_GPO_EMPRESA, \n" +
				   "U.CVE_EMPRESA, \n" +
				   "U.CVE_USUARIO, \n"+
				   "P.NOM_COMPLETO \n"+	
				" FROM RS_GRAL_USUARIO U, \n"+
				"      RS_GRAL_PERSONA P \n"+
				" WHERE U.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND U.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
				" AND P.CVE_EMPRESA = U.CVE_EMPRESA \n"+
				" AND P.ID_PERSONA = U.ID_PERSONA \n"+
				" AND U.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n";		
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}