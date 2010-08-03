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
 
public class SimCatalogoLineaFondeoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

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
			"	ID_LINEA, \n"+
			"	NUM_LINEA, \n"+
			"	MONTO, \n"+
			"	TASA, \n"+
			"	FECHA_INICIO, \n"+
			"	FECHA_VIGENCIA \n"+
			"FROM SIM_CAT_LINEA \n"+
			"WHERE CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			"AND CVE_EMPRESA='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n";
		
		if (parametros.getDefCampo("NUM_LINEA") != null) {
			sSql = sSql + " AND NUM_LINEA = '" + (String) parametros.getDefCampo("NUM_LINEA") + "' \n";
		}
		
		if (parametros.getDefCampo("MONTO") != null) {
			sSql = sSql + " AND MONTO = '" + (String) parametros.getDefCampo("MONTO") + "' \n";
		}
		
		if (parametros.getDefCampo("TASA") != null) {
			sSql = sSql + " AND TASA = '" + (String) parametros.getDefCampo("TASA") + "' \n";
		}
		
		if (parametros.getDefCampo("FECHA_INICIO") != null) {
			sSql = sSql + " AND FECHA_INICIO >= TO_DATE('" + (String) parametros.getDefCampo("FECHA_INICIO") + "','DD/MM/YYYY') \n";
		}
		
		if (parametros.getDefCampo("FECHA_VIGENCIA") != null) {
			sSql = sSql + " AND FECHA_VIGENCIA <= TO_DATE('" + (String) parametros.getDefCampo("FECHA_VIGENCIA") + "','DD/MM/YYYY') \n";
		}
		
		sSql = sSql + " ORDER BY NUM_LINEA \n";
		
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
				"	CVE_GPO_EMPRESA, \n"+
				"	CVE_EMPRESA, \n"+
				"	ID_LINEA, \n"+
				"	NUM_LINEA, \n"+
				"	MONTO, \n"+
				"	TASA, \n"+
				"	FECHA_INICIO, \n"+
				"	FECHA_VIGENCIA \n"+
			   " FROM SIM_CAT_LINEA \n"+
			   " WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND ID_LINEA = '" + (String)parametros.getDefCampo("ID_LINEA") + "' \n";
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
		
		String sIdLinea = "";
		
		sSql = "SELECT SQ01_SIM_CAT_LINEA.nextval AS ID_LINEA FROM DUAL";
		
		ejecutaSql();
		if (rs.next()){
			sIdLinea = rs.getString("ID_LINEA");
		}
		
		sSql =  "INSERT INTO SIM_CAT_LINEA ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"	ID_LINEA, \n"+
				"	NUM_LINEA, \n"+
				"	MONTO, \n"+
				"	TASA, \n"+
				"	FECHA_INICIO, \n"+
				"	FECHA_VIGENCIA) \n"+
		        "VALUES ( \n"+
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			sIdLinea + ", \n" +
			"'" + (String)registro.getDefCampo("NUM_LINEA") + "', \n" +
			"'" + (String)registro.getDefCampo("MONTO") + "', \n" +
			"'" + (String)registro.getDefCampo("TASA") + "', \n" +
			"TO_DATE('" + (String)registro.getDefCampo("FECHA_INICIO") + "','DD/MM/YYYY HH24:MI:SS'), \n" +
			"TO_DATE('" + (String)registro.getDefCampo("FECHA_VIGENCIA") + "','DD/MM/YYYY HH24:MI:SS')) \n" ;
			
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
		sSql = " UPDATE SIM_CAT_LINEA SET \n" +
			   " TASA    	 = '" + (String)registro.getDefCampo("TASA")  + "', \n" +
			   " FECHA_INICIO    = TO_DATE('" + (String)registro.getDefCampo("FECHA_INICIO")  + "','DD/MM/YYYY'), \n" +
			   " FECHA_VIGENCIA     = TO_DATE('" + (String)registro.getDefCampo("FECHA_VIGENCIA")  + "','DD/MM/YYYY') \n" +
			   " WHERE ID_LINEA    	 = '" + (String)registro.getDefCampo("ID_LINEA") + "' \n" +
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
		sSql =  " DELETE FROM SIM_CAT_LINEA " +
			" WHERE ID_LINEA		='" + (String)registro.getDefCampo("ID_LINEA") + "' \n" +
			" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}