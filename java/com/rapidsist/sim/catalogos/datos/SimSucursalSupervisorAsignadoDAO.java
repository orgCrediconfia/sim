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
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra los supervisores de las sucursales.
 */
 
public class SimSucursalSupervisorAsignadoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  "SELECT \n"+
			"S.CVE_GPO_EMPRESA, \n"+
			"S.CVE_EMPRESA, \n"+
			"S.ID_SUPERVISOR, \n"+
			"P.NOM_COMPLETO \n"+
			"FROM SIM_SUCURSAL_SUPERVISOR S, \n"+
			"RS_GRAL_PERSONA P \n"+
			"WHERE S.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND S.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND S.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
			"AND P.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA \n"+
			"AND P.CVE_EMPRESA = S.CVE_EMPRESA \n"+
			"AND P.ID_PERSONA = S.ID_SUPERVISOR \n"; 
			
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
				"T.CVE_GPO_EMPRESA, \n"+
				"T.CVE_EMPRESA, \n"+
				"T.ID_TELEFONO, \n"+	
				"T.IDENTIFICADOR, \n"+ 
				"T.CVE_TIPO_IDENTIFICADOR, \n"+
				"T.ID_DESC_TEL, \n"+
				"T.TELEFONO \n"+
			"FROM RS_GRAL_TELEFONO T \n"+
			" WHERE T.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND T.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND T.IDENTIFICADOR = '" + (String)parametros.getDefCampo("IDENTIFICADOR") + "' \n"+
			" AND T.CVE_TIPO_IDENTIFICADOR = 'REGION' \n"+
			" AND T.ID_TELEFONO = '" + (String)parametros.getDefCampo("ID_TELEFONO") + "' \n";
			
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
		
		sSql =  "INSERT INTO SIM_SUCURSAL_SUPERVISOR ( \n"+
			"CVE_GPO_EMPRESA, \n" +
			"CVE_EMPRESA, \n" +
			"ID_SUCURSAL, \n" +
			"ID_SUPERVISOR) \n" +
		        "VALUES ( \n"+
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_SUPERVISOR") + "') \n" ;
		
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
		sSql = " UPDATE RS_GRAL_TELEFONO SET "+
			   " ID_DESC_TEL    	='" + (String)registro.getDefCampo("ID_DESC_TEL")  + "', \n" +
			   " TELEFONO    	='" + (String)registro.getDefCampo("TELEFONO")  + "' \n" +
			   " WHERE ID_TELEFONO  ='" + (String)registro.getDefCampo("ID_TELEFONO") + "' \n" +
			   " AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
			   " AND CVE_EMPRESA 	='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND IDENTIFICADOR 	='" + (String)registro.getDefCampo("IDENTIFICADOR") + "'\n"+
			   " AND CVE_TIPO_IDENTIFICADOR 	='REGION'\n";
			   
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
}