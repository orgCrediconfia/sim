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
 * Administra los accesos a la base de datos para los documentos del préstamo.
 */
 
public class SimPrestamoVerificaEtapaAnteriorAutMonRieDAO extends Conexion2 implements OperacionConsultaRegistro {

	/**
	 * Obtiene un registro tomando como base la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		if (parametros.getDefCampo("PRESTAMO").equals("INDIVIDUAL")) {
			sSql =  "SELECT \n"+
					"ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_PRESTAMO \n"+
					"WHERE ID_ETAPA_PRESTAMO IN ( \n"+
					"SELECT \n"+
					"ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_PRESTAMO_ETAPA \n"+
					"WHERE ORDEN_ETAPA < ( \n"+
					"SELECT \n"+
					"PE.ORDEN_ETAPA \n"+ 
					"FROM SIM_PRODUCTO_ETAPA_PRESTAMO PE, \n"+
					"SIM_CAT_ETAPA_PRESTAMO E \n"+
					"WHERE PE.ID_PRODUCTO = (select id_producto from sim_prestamo \n"+
											"where id_prestamo = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
											"AND CVE_GPO_EMPRESA = 	'" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
											"AND CVE_EMPRESA = 	'" + (String)parametros.getDefCampo("CVE_EMPRESA") + "') \n"+
					"AND E.CVE_GPO_EMPRESA = PE.CVE_GPO_EMPRESA \n"+
					"AND E.CVE_EMPRESA = PE.CVE_EMPRESA \n"+
					"AND E.ID_ETAPA_PRESTAMO = PE.ID_ETAPA_PRESTAMO \n"+
					"AND E.ID_ETAPA_PRESTAMO = '25') \n"+
					"and id_prestamo = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
					"AND CVE_GPO_EMPRESA = 	'" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = 	'" + (String)parametros.getDefCampo("CVE_EMPRESA") + "') \n"+
					"AND ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
					"AND CVE_GPO_EMPRESA = 	'" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = 	'" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n";
			
			System.out.println("verificar la etapa anterior"+sSql);
		}else {
			sSql =  "SELECT \n"+
					"ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_PRESTAMO_GRUPO \n"+
					"WHERE ID_ETAPA_PRESTAMO IN ( \n"+
					"SELECT DISTINCT \n"+
					"ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_PRESTAMO_ETAPA \n"+
					"WHERE ORDEN_ETAPA < ( \n"+
					"SELECT \n"+
					"PE.ORDEN_ETAPA \n"+ 
					"FROM SIM_PRODUCTO_ETAPA_PRESTAMO PE, \n"+
					"SIM_CAT_ETAPA_PRESTAMO E \n"+
					"WHERE PE.ID_PRODUCTO = (SELECT ID_PRODUCTO FROM SIM_PRESTAMO_GRUPO " +
											"WHERE ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
											"AND CVE_GPO_EMPRESA = 	'" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
											"AND CVE_EMPRESA = 	'" + (String)parametros.getDefCampo("CVE_EMPRESA") + "') \n"+
					"AND E.CVE_GPO_EMPRESA = PE.CVE_GPO_EMPRESA \n"+
					"AND E.CVE_EMPRESA = PE.CVE_EMPRESA \n"+
					"AND E.ID_ETAPA_PRESTAMO = PE.ID_ETAPA_PRESTAMO \n"+
					"AND E.ID_ETAPA_PRESTAMO = '25') \n"+
					"and id_prestamo IN \n"+
					"(SELECT \n"+
					"ID_PRESTAMO \n"+
					"FROM \n"+
					"SIM_PRESTAMO_GPO_DET \n"+
					"WHERE ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
					"AND CVE_GPO_EMPRESA = 	'" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = 	'" + (String)parametros.getDefCampo("CVE_EMPRESA") + "') \n"+
					"AND ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
					"AND CVE_GPO_EMPRESA = 	'" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = 	'" + (String)parametros.getDefCampo("CVE_EMPRESA") + "') \n";
		}
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}