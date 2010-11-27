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
 * Administra los accesos a la base de datos para obtenr el identificador del préstamo en base a su clave.
 */
 
public class SimPrestamoGrupoObtieneIdentificadorDAO extends Conexion2 implements OperacionConsultaRegistro {

	/**
	 * Obtiene un registro tomando como base la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		
		sSql =  "SELECT \n"+
				"P.CVE_GPO_EMPRESA, \n"+
				"P.CVE_EMPRESA, \n"+
				"P.ID_PRESTAMO_GRUPO, \n"+
				"P.CVE_PRESTAMO_GRUPO \n"+
				"FROM \n"+
				"SIM_PRESTAMO_GRUPO P, \n"+
				"SIM_CAT_ETAPA_PRESTAMO E \n"+
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND P.CVE_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
				"AND E.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+ 
				"AND E.CVE_EMPRESA = P.CVE_EMPRESA \n"+ 
				"AND E.ID_ETAPA_PRESTAMO = P.ID_ETAPA_PRESTAMO \n"+ 
				"AND E.B_CANCELADO != 'V' \n";
				
		ejecutaSql();
		
		return this.getConsultaRegistro();
	}
}