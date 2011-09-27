/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para obtenr el identificador del préstamo en base a su clave.
 */
 
public class SimPrestamoObtieneIdentificadorDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		sSql =  "SELECT \n"+
				"G.CVE_PRESTAMO_GRUPO, \n"+
				"G.ID_PRESTAMO_GRUPO, \n"+ 
				"D.ID_PRESTAMO \n"+
				"FROM SIM_PRESTAMO_GRUPO G, \n"+
				"SIM_PRESTAMO_GPO_DET D, \n"+
				"SIM_CAT_ETAPA_PRESTAMO E \n"+
				"WHERE G.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND G.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND G.CVE_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("CVE_PRESTAMO") + "' \n"+
				"AND D.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n"+
				"AND D.CVE_EMPRESA = G.CVE_EMPRESA \n"+
				"AND D.ID_PRESTAMO_GRUPO = G.ID_PRESTAMO_GRUPO \n"+
				"AND D.ID_ETAPA_PRESTAMO != '18' \n"+
				"AND E.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n"+ 
				"AND E.CVE_EMPRESA = G.CVE_EMPRESA \n"+ 
				"AND E.ID_ETAPA_PRESTAMO = G.ID_ETAPA_PRESTAMO \n"+ 
				"AND E.B_CANCELADO != 'V' \n";
				
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
		
		sSql =  "SELECT \n"+
				"P.CVE_GPO_EMPRESA, \n"+
				"P.CVE_EMPRESA, \n"+
				"P.ID_PRESTAMO, \n"+
				"P.CVE_PRESTAMO \n"+
				"FROM \n"+
				"SIM_PRESTAMO P \n"+
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND P.CVE_PRESTAMO = '" + (String)parametros.getDefCampo("CVE_PRESTAMO") + "' \n"+
				"AND P.ID_ETAPA_PRESTAMO != '16' \n";
		
		System.out.println(sSql);
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}