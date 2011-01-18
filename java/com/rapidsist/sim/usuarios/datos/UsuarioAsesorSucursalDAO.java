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
 
public class UsuarioAsesorSucursalDAO extends Conexion2 implements OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{

		sSql = "SELECT \n" +
		"U.CVE_USUARIO, \n" +
		"P.ID_PERSONA, \n" +
		"P.NOM_COMPLETO, \n" +
		"P.ID_SUCURSAL, \n" +
		"S.NOM_SUCURSAL \n" +
		"FROM RS_GRAL_USUARIO U, \n" +
		"RS_GRAL_PERSONA P, \n" +
		"SIM_CAT_SUCURSAL S \n" +
		"WHERE U.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
		"AND U.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
		"AND U.CVE_PUESTO = 'AseSuc' \n" +
		"AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n" +
		"AND P.CVE_EMPRESA = U.CVE_EMPRESA \n" +
		"AND P.ID_PERSONA = U.ID_PERSONA \n" +
		"AND S.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n" +
		"AND S.CVE_EMPRESA = P.CVE_EMPRESA \n" +
		"AND S.ID_SUCURSAL = P.ID_SUCURSAL \n" +
		"AND P.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n" ;
		
		ejecutaSql();
		return getConsultaLista();
	}
}