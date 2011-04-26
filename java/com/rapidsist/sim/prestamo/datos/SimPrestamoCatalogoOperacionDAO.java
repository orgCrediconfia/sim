/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.catalogos.OperacionAlta; 
import com.rapidsist.portal.catalogos.OperacionBaja; 
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.Iterator;


/**
 * Administra los accesos a la base de datos para los préstamos.
 */
 
public class SimPrestamoCatalogoOperacionDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionConsultaRegistro, OperacionConsultaTabla, OperacionModificacion {
	

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		if (parametros.getDefCampo("OPERACION").equals("TODAS")){
			sSql =  "SELECT \n"+
					"CVE_GPO_EMPRESA, \n"+
					"CVE_EMPRESA, \n"+
					"CVE_OPERACION, \n"+
					"DESC_CORTA, \n"+
					"DESC_LARGA, \n"+
					"CVE_AFECTA_SALDO, \n"+
					"CVE_AFECTA_CREDITO \n"+
				"FROM PFIN_CAT_OPERACION \n"+
				"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n";
			
			if (parametros.getDefCampo("CVE_OPERACION") != null) {
				sSql = sSql + "AND UPPER(CVE_OPERACION) LIKE '%" + ((String) parametros.getDefCampo("CVE_OPERACION")).toUpperCase() + "%' \n";
			}
			if (parametros.getDefCampo("DESC_CORTA") != null) {
				sSql = sSql + "AND UPPER(DESC_CORTA) LIKE '%" + ((String) parametros.getDefCampo("DESC_CORTA")).toUpperCase() + "%' \n";
			}	
		}else if (parametros.getDefCampo("OPERACION").equals("EXTRAORDINARIO")){
			sSql =  "SELECT \n"+
					"CVE_GPO_EMPRESA, \n"+
					"CVE_EMPRESA, \n"+
					"CVE_OPERACION, \n"+
					"DESC_CORTA, \n"+
					"DESC_LARGA, \n"+
					"CVE_AFECTA_SALDO, \n"+
					"CVE_AFECTA_CREDITO \n"+
				"FROM PFIN_CAT_OPERACION \n"+
				"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND B_MOVTO_EXTRAORDINARIO = 'V' \n";
		}
		
		sSql = sSql + "ORDER BY CVE_OPERACION \n";
		
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
				"CVE_GPO_EMPRESA, \n"+
				"CVE_EMPRESA, \n"+
				"CVE_OPERACION, \n"+
				"DESC_CORTA, \n"+
				"DESC_LARGA, \n"+
				"CVE_AFECTA_SALDO, \n"+
				"CVE_AFECTA_CREDITO \n"+
			"FROM PFIN_CAT_OPERACION \n"+
			"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND CVE_OPERACION = '" + (String) parametros.getDefCampo("CVE_OPERACION") + "' \n";				
				
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		sSql =  "INSERT INTO PFIN_CAT_OPERACION ( "+
			"CVE_GPO_EMPRESA, \n" +
			"CVE_EMPRESA, \n" +
			"CVE_OPERACION, \n"+
			"DESC_CORTA, \n"+
			"DESC_LARGA, \n"+
			"CVE_AFECTA_SALDO, \n"+
			"CVE_AFECTA_CREDITO) \n"+
			" VALUES (" +
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_OPERACION") + "', \n" +
			"'" + (String)registro.getDefCampo("DESC_CORTA") + "', \n" +
			"'" + (String)registro.getDefCampo("DESC_LARGA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_AFECTA_SALDO") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_AFECTA_CREDITO") + "') \n" ;
			
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
		sSql =  " UPDATE PFIN_CAT_OPERACION SET "+
			" DESC_CORTA		='" + (String)registro.getDefCampo("DESC_CORTA") + "', \n" +
			" DESC_LARGA		='" + (String)registro.getDefCampo("DESC_LARGA") + "', \n" +
			" CVE_AFECTA_SALDO	='" + (String)registro.getDefCampo("CVE_AFECTA_SALDO") + "', \n" +
			" CVE_AFECTA_CREDITO	='" + (String)registro.getDefCampo("CVE_AFECTA_CREDITO") + "' \n" +
			" WHERE CVE_OPERACION  	='" + (String)registro.getDefCampo("CVE_OPERACION") + "' \n" +
			" AND CVE_EMPRESA   	='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA   ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
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
		sSql =  " DELETE FROM PFIN_CAT_OPERACION " +
			" WHERE CVE_OPERACION		='" + (String)registro.getDefCampo("CVE_OPERACION") + "' \n" +
			" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
	
		return resultadoCatalogo;
	}

}