/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.producto.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta; 
import com.rapidsist.portal.catalogos.OperacionBaja; 
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos de los roles de las personas.
 */
 
public class SimProductoParticipantesDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		if (parametros.getDefCampo("PARTICIPANTES").equals("DISPONIBLES")){
			sSql =  "SELECT \n"+
				"CVE_GPO_EMPRESA, \n"+
				"CVE_EMPRESA, \n"+
				"CVE_TIPO_PERSONA, \n"+
				"NOM_TIPO_PERSONA \n"+
				"FROM SIM_CAT_TIPO_PERSONA \n"+
				"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND CVE_TIPO_PERSONA != 'EXCONYUGE' \n"+
				"AND CVE_TIPO_PERSONA != 'USUARIO' \n"+
				"MINUS \n"+
				"SELECT \n"+
				"	PP.CVE_GPO_EMPRESA, \n"+
				"	PP.CVE_EMPRESA, \n"+
				"	PP.CVE_TIPO_PERSONA, \n"+
				"	C.NOM_TIPO_PERSONA \n"+
				"FROM SIM_PRODUCTO_PARTICIPANTE PP, \n"+
				"     SIM_CAT_TIPO_PERSONA C \n"+
				"WHERE PP.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND PP.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND PP.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
				"AND C.CVE_GPO_EMPRESA = PP.CVE_GPO_EMPRESA \n"+
				"AND C.CVE_EMPRESA = PP.CVE_EMPRESA \n"+
				"AND C.CVE_TIPO_PERSONA = PP.CVE_TIPO_PERSONA \n"+
				"ORDER BY NOM_TIPO_PERSONA \n";
		}else if (parametros.getDefCampo("PARTICIPANTES").equals("ASIGNADOS")){
			sSql =  "SELECT \n"+
				"	PP.CVE_GPO_EMPRESA, \n"+
				"	PP.CVE_EMPRESA, \n"+
				"	PP.CVE_TIPO_PERSONA, \n"+
				"	C.NOM_TIPO_PERSONA \n"+
				"FROM SIM_PRODUCTO_PARTICIPANTE PP, \n"+
				"     SIM_CAT_TIPO_PERSONA C \n"+
				"WHERE PP.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND PP.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND PP.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
				"AND C.CVE_GPO_EMPRESA = PP.CVE_GPO_EMPRESA \n"+
				"AND C.CVE_EMPRESA = PP.CVE_EMPRESA \n"+
				"AND C.CVE_TIPO_PERSONA = PP.CVE_TIPO_PERSONA \n"+
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
		
		sSql = "INSERT INTO SIM_PRODUCTO_PARTICIPANTE ( "+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PRODUCTO, \n" +
				"CVE_TIPO_PERSONA) \n" +
			" VALUES (" +
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_TIPO_PERSONA") + "') \n" ;
				
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
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
		sSql =  " DELETE FROM SIM_PRODUCTO_PARTICIPANTE " +
			" WHERE ID_PRODUCTO		='" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
			" AND CVE_TIPO_PERSONA		='" + (String)registro.getDefCampo("CVE_TIPO_PERSONA") + "' \n"+
			" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
}