/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.comite.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para comites.
 */
 
public class SimComiteIntegranteDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionModificacion, OperacionBaja  {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		if (parametros.getDefCampo("CONSULTA").equals("TODOS")) {
			sSql =  "SELECT \n"+
			   "U.CVE_GPO_EMPRESA, \n" +
			   "U.CVE_EMPRESA, \n" +
			   "U.CVE_USUARIO, \n"+
			   "P.NOM_COMPLETO \n"+	
			" FROM RS_GRAL_USUARIO U, \n"+
			"      RS_GRAL_PERSONA P \n"+
			" WHERE U.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND U.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
			" AND P.CVE_EMPRESA = U.CVE_EMPRESA \n"+
			" AND P.ID_PERSONA = U.ID_PERSONA \n"+
			" AND U.CVE_USUARIO != 'visitante' \n";
			
			if (parametros.getDefCampo("CLAVE_USUARIO") != null) {
				sSql = sSql + " AND UPPER(U.CVE_USUARIO) LIKE '%" + ((String) parametros.getDefCampo("CLAVE_USUARIO")).toUpperCase()  + "%' \n";
			}
			
			if (parametros.getDefCampo("NOM_COMPLETO") != null) {
				sSql = sSql + " AND UPPER(P.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase()  + "%' \n";
			}
			
			sSql = sSql + " MINUS \n";
			
			sSql = sSql + "SELECT \n"+
				   "C.CVE_GPO_EMPRESA, \n" +
				   "C.CVE_EMPRESA, \n" +
				   "C.CVE_INTEGRANTE CVE_USUARIO, \n"+
				   "P.NOM_COMPLETO \n"+	
				" FROM SIM_COMITE_INTEGRANTE C, \n"+
				"      RS_GRAL_USUARIO U, \n"+
				"      RS_GRAL_PERSONA P \n"+
				" WHERE C.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND C.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND C.ID_COMITE ='" + (String)parametros.getDefCampo("ID_COMITE") + "' \n"+
				" AND U.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
				" AND U.CVE_EMPRESA = C.CVE_EMPRESA \n"+
				" AND U.CVE_USUARIO = C.CVE_INTEGRANTE \n"+
				" AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
				" AND P.CVE_EMPRESA = U.CVE_EMPRESA \n"+
				" AND P.ID_PERSONA = U.ID_PERSONA \n"+
				" AND U.CVE_USUARIO != 'visitante' \n";
				
			if (parametros.getDefCampo("CLAVE_USUARIO") != null) {
				sSql = sSql + " AND UPPER(U.CVE_USUARIO) LIKE '%" + ((String) parametros.getDefCampo("CLAVE_USUARIO")).toUpperCase()  + "%' \n";
			}
			
			if (parametros.getDefCampo("NOM_COMPLETO") != null) {
				sSql = sSql + " AND UPPER(P.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase()  + "%' \n";
			}
			
		}else if (parametros.getDefCampo("CONSULTA").equals("ASIGNADOS")) {
			sSql =  "SELECT \n"+
				   "C.CVE_GPO_EMPRESA, \n" +
				   "C.CVE_EMPRESA, \n" +
				   "C.ID_COMITE, \n"+
				   "C.CVE_INTEGRANTE, \n"+
				   "P.NOM_COMPLETO \n"+	
				" FROM SIM_COMITE_INTEGRANTE C, \n"+
				"      RS_GRAL_USUARIO U, \n"+
				"      RS_GRAL_PERSONA P \n"+
				" WHERE C.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND C.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND C.ID_COMITE ='" + (String)parametros.getDefCampo("ID_COMITE") + "' \n"+
				" AND U.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
				" AND U.CVE_EMPRESA = C.CVE_EMPRESA \n"+
				" AND U.CVE_USUARIO = C.CVE_INTEGRANTE \n"+
				" AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
				" AND P.CVE_EMPRESA = U.CVE_EMPRESA \n"+
				" AND P.ID_PERSONA = U.ID_PERSONA \n";
				
		}
		
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
			   "CVE_GPO_EMPRESA, \n" +
			   "CVE_EMPRESA, \n" +
			   "ID_COMITE, \n"+
			   "ID_SUCURSAL, \n"+
			   "NOM_COMITE, \n"+
			   "FECHA \n" +	
			" FROM SIM_COMITE \n"+
			" WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_COMITE = '" + (String)parametros.getDefCampo("ID_COMITE") + "' \n";		
		
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
		
		sSql =  "INSERT INTO SIM_COMITE_INTEGRANTE ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_COMITE, \n"+
				"CVE_INTEGRANTE) \n"+
			"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_COMITE") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_INTEGRANTE") + "') \n";
				
		//VERIFICA SI DIO DE ALTA EL REGISTRO
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
		ResultadoCatalogo resultadoCatalogo = new 	ResultadoCatalogo();
		sSql = " UPDATE SIM_COMITE SET "+
			   " ID_SUCURSAL     		= '" + (String)registro.getDefCampo("ID_SUCURSAL")  + "', \n" +
			   " NOM_COMITE     		= '" + (String)registro.getDefCampo("NOM_COMITE")  + "', \n" +
			   " FECHA                      = TO_DATE('" + (String)registro.getDefCampo("FECHA")  + "','DD/MM/YYYY') \n" +
			   " WHERE ID_COMITE      	= '" + (String)registro.getDefCampo("ID_COMITE") + "' \n" +
			   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
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
		//BORRA EL REGISTRO
		sSql = "DELETE FROM SIM_COMITE" +
				" WHERE ID_COMITE		='" + (String)registro.getDefCampo("ID_COMITE") + "' \n" +
				" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}

}