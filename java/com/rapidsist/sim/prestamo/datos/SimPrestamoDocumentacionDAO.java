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
 * Administra los accesos a la base de datos para los documentos del préstamo.
 */
 
public class SimPrestamoDocumentacionDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS DE BANCOS
		
		sSql = " SELECT \n"+
			"	PD.CVE_GPO_EMPRESA, \n"+
			"	PD.CVE_EMPRESA, \n"+
			"	PD.ID_PRESTAMO, \n"+
			"	PD.ID_DOCUMENTO, \n"+
			"	CD.NOM_DOCUMENTO, \n"+
			"	CD.ID_REPORTE, \n"+
			"	R.NOM_REPORTE, \n"+
			"	R.CVE_FUNCION \n"+
			" FROM SIM_PRESTAMO_DOCUMENTACION PD, \n"+
			"      SIM_CAT_DOCUMENTO CD, \n"+
			"      SIM_CAT_REPORTE R \n"+
			" WHERE PD.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND PD.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND PD.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND CD.CVE_GPO_EMPRESA = PD.CVE_GPO_EMPRESA \n" +
			" AND CD.CVE_EMPRESA = PD.CVE_EMPRESA \n"+
			" AND CD.ID_DOCUMENTO = PD.ID_DOCUMENTO \n"+
			" AND R.CVE_GPO_EMPRESA = CD.CVE_GPO_EMPRESA \n"+
			" AND R.CVE_EMPRESA = CD.CVE_EMPRESA \n"+
			" AND R.ID_REPORTE = CD.ID_REPORTE \n";
		
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
		if (parametros.getDefCampo("ETAPA_DOCUMENTOS").equals("INDIVIDUAL")) {
			sSql =  "SELECT \n"+
					"ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_PRESTAMO \n"+
					"WHERE ID_ETAPA_PRESTAMO IN ( \n"+
					"SELECT \n"+
					"ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_PRESTAMO_ETAPA \n"+
					"WHERE ORDEN_ETAPA >= ( \n"+
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
					"AND E.B_DESEMBOLSO = 'V') \n"+
					"and id_prestamo = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
					"AND CVE_GPO_EMPRESA = 	'" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = 	'" + (String)parametros.getDefCampo("CVE_EMPRESA") + "') \n"+
					"AND ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
					"AND CVE_GPO_EMPRESA = 	'" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = 	'" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n";
		}else {
			sSql =  "SELECT \n"+
					"ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_PRESTAMO_GRUPO \n"+
					"WHERE ID_ETAPA_PRESTAMO IN ( \n"+
					"SELECT DISTINCT \n"+
					"ID_ETAPA_PRESTAMO \n"+
					"FROM SIM_PRESTAMO_ETAPA \n"+
					"WHERE ORDEN_ETAPA >= ( \n"+
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
					"AND E.B_DESEMBOLSO = 'V') \n"+
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