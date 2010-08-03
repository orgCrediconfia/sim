/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.catalogos.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para el catálogo de fondeadores.
 */
 
public class SimCatalogoReporteDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS DE BANCOS
		
		sSql =  "SELECT \n"+
			"	CVE_GPO_EMPRESA, \n"+
			"	CVE_EMPRESA, \n"+
			"	ID_REPORTE, \n"+
			"	NOM_REPORTE, \n"+
			"	APLICA_A \n"+
			"FROM SIM_CAT_REPORTE \n"+
			"WHERE CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			"AND CVE_EMPRESA='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n";
		
		if (parametros.getDefCampo("ID_REPORTE") != null) {
			sSql = sSql + " AND ID_REPORTE = '" + (String) parametros.getDefCampo("ID_REPORTE") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_REPORTE") != null) {
			sSql = sSql + " AND UPPER(NOM_REPORTE) LIKE'%" + ((String) parametros.getDefCampo("NOM_REPORTE")).toUpperCase()  + "%' \n";
		}
		
		if (parametros.getDefCampo("APLICA_A") != null) {
			sSql = sSql + " AND APLICA_A = '" + (String) parametros.getDefCampo("APLICA_A") + "' \n";
		}
		
		sSql = sSql + " ORDER BY NOM_REPORTE \n";
		
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
		sSql = " SELECT \n"+
			   " CVE_GPO_EMPRESA, \n"+
			   " CVE_EMPRESA, \n"+
			   " ID_REPORTE, \n"+
			   " NOM_REPORTE, \n"+
			   " APLICA_A, \n"+
			   " CVE_FUNCION \n"+
			   " FROM SIM_CAT_REPORTE\n"+
			   " WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND ID_REPORTE = '" + (String)parametros.getDefCampo("ID_REPORTE") + "' \n";
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		String sNomReporte = "";
		
		//OBTIENE CLAVE DEL PRÉSTAMO GRUPAL.
		sSql =  "SELECT REPLACE(INITCAP('" + (String)registro.getDefCampo("NOM_REPORTE") + "'),' ','') NOM_REPORTE FROM DUAL \n";
		ejecutaSql();
		if (rs.next()){
			sNomReporte = rs.getString("NOM_REPORTE");
		}
		
		sSql =  "INSERT INTO RS_CONF_FUNCION ( \n"+
				"CVE_FUNCION, \n"+
				"NOM_FUNCION, \n"+
				"NOM_CLASE_REPORTE, \n"+
				"B_WEBSERVICE, \n"+
				"B_BLOQUEADO, \n"+
				"B_BITACORA, \n"+
				"B_PAGINACION, \n"+
				"B_RASTREA_CODIGO) \n"+
				"VALUES ( \n"+
				"'SimReporte" + sNomReporte + "', \n"+
				"'Reporte de " + (String)registro.getDefCampo("NOM_REPORTE") + "', \n"+
				"'com.rapidsist.sim.prestamo.reportes."+"SimReporte" + sNomReporte + "REP', \n"+
				"'F', \n"+
				"'F', \n"+
				"'F', \n"+
				"'F', \n"+
				"'F') \n";
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		String sIdReporte = "";
		
		sSql = "SELECT SQ01_SIM_CAT_REPORTE.nextval AS ID_REPORTE FROM DUAL";
		
		ejecutaSql();
		if (rs.next()){
			sIdReporte = rs.getString("ID_REPORTE");
		}
		
		sSql =  "INSERT INTO SIM_CAT_REPORTE ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				" ID_REPORTE, \n"+
				" NOM_REPORTE, \n"+
				" APLICA_A, \n"+
				" CVE_FUNCION) \n"+
		        "VALUES ( \n"+
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			sIdReporte + ", \n" +
			"'" + (String)registro.getDefCampo("NOM_REPORTE") + "', \n" +
			"'" + (String)registro.getDefCampo("APLICA_A") + "', \n" +
			"'SimReporte" + sNomReporte + "') \n";
			
			//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
		
		return resultadoCatalogo;
	}
	
	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		sSql = " UPDATE SIM_CAT_REPORTE SET "+
			   " NOM_REPORTE    	 = '" + (String)registro.getDefCampo("NOM_REPORTE")  + "', \n" +
			   " APLICA_A    	 = '" + (String)registro.getDefCampo("APLICA_A")  + "' \n" +
			   " WHERE ID_REPORTE    	 = '" + (String)registro.getDefCampo("ID_REPORTE") + "' \n" +
			   " AND CVE_EMPRESA   	 = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
	
	/**
	 * Borra un registro.
	 * @param registro Llave primaria.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo baja(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		//BORRA LA FUNCION
		sSql =  " DELETE FROM SIM_CAT_REPORTE " +
			" WHERE ID_REPORTE		='" + (String)registro.getDefCampo("ID_REPORTE") + "' \n" +
			" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}