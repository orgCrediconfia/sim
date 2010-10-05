/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.personas.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta; 
import com.rapidsist.portal.catalogos.OperacionBaja; 
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;

import java.util.Iterator;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos de los roles de las personas.
 */
 
public class SimPersonaRolesDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		if (parametros.getDefCampo("ROLES").equals("DISPONIBLES")){
			sSql =  "SELECT \n"+
				"CVE_GPO_EMPRESA, \n"+
				"CVE_EMPRESA, \n"+
				"CVE_TIPO_PERSONA, \n"+
				"NOM_TIPO_PERSONA \n"+
				"FROM SIM_CAT_TIPO_PERSONA \n"+
				"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND CVE_TIPO_PERSONA != 'PERSONA' \n"+
				"AND CVE_TIPO_PERSONA != 'EXCONYUGE' \n"+
				"MINUS \n"+
				"SELECT \n"+
				"	TP.CVE_GPO_EMPRESA, \n"+
				"	TP.CVE_EMPRESA, \n"+
				"	TP.CVE_TIPO_PERSONA, \n"+
				"	C.NOM_TIPO_PERSONA \n"+
				"FROM SIM_TIPO_PERSONA TP, \n"+
				"     SIM_CAT_TIPO_PERSONA C \n"+
				"WHERE TP.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND TP.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND TP.ID_PERSONA = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n"+
				"AND C.CVE_GPO_EMPRESA = TP.CVE_GPO_EMPRESA \n"+
				"AND C.CVE_EMPRESA = TP.CVE_EMPRESA \n"+
				"AND C.CVE_TIPO_PERSONA = TP.CVE_TIPO_PERSONA \n"+
				"ORDER BY NOM_TIPO_PERSONA \n";
		}else if (parametros.getDefCampo("ROLES").equals("ASIGNADOS")){
			sSql =  "SELECT \n"+
				"	TP.CVE_GPO_EMPRESA, \n"+
				"	TP.CVE_EMPRESA, \n"+
				"	TP.CVE_TIPO_PERSONA, \n"+
				"	C.NOM_TIPO_PERSONA \n"+
				"FROM SIM_TIPO_PERSONA TP, \n"+
				"     SIM_CAT_TIPO_PERSONA C \n"+
				"WHERE TP.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND TP.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND TP.ID_PERSONA = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n"+
				"AND C.CVE_GPO_EMPRESA = TP.CVE_GPO_EMPRESA \n"+
				"AND C.CVE_EMPRESA = TP.CVE_EMPRESA \n"+
				"AND C.CVE_TIPO_PERSONA = TP.CVE_TIPO_PERSONA \n"+
				"AND C.CVE_TIPO_PERSONA != 'PERSONA' \n"+
				"ORDER BY NOM_TIPO_PERSONA \n";
		}
	
		ejecutaSql();
		return getConsultaLista();
	}

	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();

		LinkedList listaTipoPersona = (LinkedList)registro.getDefCampo("ListaTipoPersona");
		
		if (listaTipoPersona != null){
			Iterator lista = listaTipoPersona.iterator();
			while (lista.hasNext()){
				Registro registroTipoPersona = (Registro)lista.next();
				sSql = "INSERT INTO SIM_TIPO_PERSONA ( "+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_PERSONA, \n" +
						"CVE_TIPO_PERSONA) \n" +
					" VALUES (" +
						"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_PERSONA") + "', \n" +
						"'" + (String)registroTipoPersona.getDefCampo("CVE_TIPO_PERSONA") + "') \n" ;
						
				//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
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
		
		LinkedList listaTipoPersona = (LinkedList)registro.getDefCampo("ListaTipoPersona");
		
		if (listaTipoPersona != null){
			Iterator lista = listaTipoPersona.iterator();
			while (lista.hasNext()){
				Registro registroTipoPersona = (Registro)lista.next();
		
				sSql =  " DELETE FROM SIM_TIPO_PERSONA " +
					" WHERE ID_PERSONA		='" + (String)registro.getDefCampo("ID_PERSONA") + "' \n" +
					" AND CVE_TIPO_PERSONA		='" + (String)registroTipoPersona.getDefCampo("CVE_TIPO_PERSONA") + "' \n"+
					" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
					
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
		}
		return resultadoCatalogo;
	}
}