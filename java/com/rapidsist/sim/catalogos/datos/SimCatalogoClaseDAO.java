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
 * Administra los accesos a la base de datos para el catálogo de giro.
 */
 
public class SimCatalogoClaseDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

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
			"	CVE_SECTOR, \n"+
			"	CVE_SUB_SECTOR, \n"+
			"	CVE_RAMA, \n"+
			"	CVE_SUB_RAMA, \n"+
			"	CVE_CLASE, \n"+
			"	NOM_CLASE \n"+
			"FROM SIM_CAT_CLASE \n"+
			"WHERE CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			"AND CVE_EMPRESA='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND CVE_SECTOR='" + (String)parametros.getDefCampo("CVE_SECTOR") + "' \n"+
			"AND CVE_SUB_SECTOR='" + (String)parametros.getDefCampo("CVE_SUB_SECTOR") + "' \n"+
			"AND CVE_RAMA='" + (String)parametros.getDefCampo("CVE_RAMA") + "' \n"+
			"AND CVE_SUB_RAMA='" + (String)parametros.getDefCampo("CVE_SUB_RAMA") + "' \n";
		
		sSql = sSql + " ORDER BY CVE_CLASE \n";
		
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
			"	CVE_GPO_EMPRESA, \n"+
			"	CVE_EMPRESA, \n"+
			"	CVE_SECTOR, \n"+
			"	CVE_SUB_SECTOR, \n"+
			"	CVE_RAMA, \n"+
			"	CVE_SUB_RAMA, \n"+
			"	CVE_CLASE, \n"+
			"	NOM_CLASE \n"+
			"FROM SIM_CAT_CLASE \n"+
			"WHERE CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			"AND CVE_EMPRESA='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND CVE_SECTOR='" + (String)parametros.getDefCampo("CVE_SECTOR") + "' \n"+
			"AND CVE_SUB_SECTOR='" + (String)parametros.getDefCampo("CVE_SUB_SECTOR") + "' \n"+
			"AND CVE_RAMA='" + (String)parametros.getDefCampo("CVE_RAMA") + "' \n"+
			"AND CVE_SUB_RAMA='" + (String)parametros.getDefCampo("CVE_SUB_RAMA") + "' \n"+
			"AND CVE_CLASE='" + (String)parametros.getDefCampo("CVE_CLASE") + "' \n";
			
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
		
		sSql =  "INSERT INTO SIM_CAT_CLASE ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"CVE_SECTOR, \n" +
				"CVE_SUB_SECTOR, \n" +
				"CVE_RAMA, \n" +
				"CVE_SUB_RAMA, \n" +
				"CVE_CLASE, \n" +
				"NOM_CLASE) \n" +
		        "VALUES ( \n"+
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_SECTOR") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_SUB_SECTOR") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_RAMA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_SUB_RAMA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_CLASE") + "', \n" +
			"'" + (String)registro.getDefCampo("NOM_CLASE") + "') \n" ;
			
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
		sSql = " UPDATE SIM_CAT_CLASE SET "+
			   " NOM_CLASE	 	 = '" + (String)registro.getDefCampo("NOM_CLASE")  + "' \n" +
			   " WHERE CVE_SECTOR    	 = '" + (String)registro.getDefCampo("CVE_SECTOR") + "' \n" +
			   " AND CVE_SUB_SECTOR   	 = '" + (String)registro.getDefCampo("CVE_SUB_SECTOR") + "' \n"+
			   " AND CVE_RAMA   	 = '" + (String)registro.getDefCampo("CVE_RAMA") + "' \n"+
			   " AND CVE_SUB_RAMA   	 = '" + (String)registro.getDefCampo("CVE_SUB_RAMA") + "' \n"+
			   " AND CVE_CLASE   	 = '" + (String)registro.getDefCampo("CVE_CLASE") + "' \n"+
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
		sSql =   " DELETE FROM SIM_CAT_CLASE " +
			 " WHERE CVE_SECTOR    	 = '" + (String)registro.getDefCampo("CVE_SECTOR") + "' \n" +
			   " AND CVE_SUB_SECTOR   	 = '" + (String)registro.getDefCampo("CVE_SUB_SECTOR") + "' \n"+
			   " AND CVE_RAMA   	 = '" + (String)registro.getDefCampo("CVE_RAMA") + "' \n"+
			   " AND CVE_SUB_RAMA   	 = '" + (String)registro.getDefCampo("CVE_SUB_RAMA") + "' \n"+
			   " AND CVE_CLASE   	 = '" + (String)registro.getDefCampo("CVE_CLASE") + "' \n"+
			   " AND CVE_EMPRESA   	 = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}