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
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra los documentos asignados a los clientes.
 */
 
public class SimClienteDocumentacionDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

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
				"CD.CVE_GPO_EMPRESA, \n"+
				"CD.CVE_EMPRESA, \n"+
				"CD.ID_PERSONA, \n"+	
				"CD.ID_DOCUMENTO, \n"+
				"D.NOM_DOCUMENTO, \n"+ 
				"CD.FECHA_RECIBIDO, \n"+
				"CD.CVE_VERIFICADOR, \n"+
				"P.NOM_COMPLETO, \n"+
				"CD.DOCUMENTO_CORRECTO \n"+
			" FROM SIM_CLIENTE_DOCUMENTO CD, \n"+
			"      SIM_CAT_DOCUMENTO D, \n"+
			"      RS_GRAL_USUARIO U, \n"+
			"      RS_GRAL_PERSONA P \n"+
			" WHERE CD.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CD.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CD.ID_PERSONA = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n"+
			" AND D.CVE_GPO_EMPRESA = CD.CVE_GPO_EMPRESA \n"+
			" AND D.CVE_EMPRESA = CD.CVE_EMPRESA \n"+
			" AND D.ID_DOCUMENTO = CD.ID_DOCUMENTO \n"+
			" AND U.CVE_GPO_EMPRESA (+)= CD.CVE_GPO_EMPRESA \n"+
			" AND U.CVE_EMPRESA (+)= CD.CVE_EMPRESA \n"+
			" AND U.CVE_USUARIO (+)= CD.CVE_VERIFICADOR \n"+
			" AND P.CVE_GPO_EMPRESA (+)= U.CVE_GPO_EMPRESA \n"+
			" AND P.CVE_EMPRESA (+)= U.CVE_EMPRESA \n"+
			" AND P.ID_PERSONA (+)= U.ID_PERSONA \n";
			
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
				"CD.CVE_GPO_EMPRESA, \n"+
				"CD.CVE_EMPRESA, \n"+
				"CD.ID_PERSONA, \n"+	
				"CD.ID_DOCUMENTO, \n"+
				"D.NOM_DOCUMENTO, \n"+ 
				"CD.FECHA_RECIBIDO, \n"+
				"CD.CVE_VERIFICADOR, \n"+
				"U.ID_PERSONA, \n"+
				"P.NOM_COMPLETO, \n"+
				"CD.DOCUMENTO_CORRECTO \n"+
			" FROM SIM_CLIENTE_DOCUMENTO CD, \n"+
			"      SIM_CAT_DOCUMENTO D, \n"+
			"      RS_GRAL_USUARIO U, \n"+
			"      RS_GRAL_PERSONA P \n"+
			" WHERE CD.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CD.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CD.ID_PERSONA = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n"+
			" AND CD.ID_DOCUMENTO = '" + (String)parametros.getDefCampo("ID_DOCUMENTO") + "' \n"+
			" AND D.CVE_GPO_EMPRESA = CD.CVE_GPO_EMPRESA \n"+
			" AND D.CVE_EMPRESA = CD.CVE_EMPRESA \n"+
			" AND D.ID_DOCUMENTO = CD.ID_DOCUMENTO \n"+
			" AND U.CVE_GPO_EMPRESA (+)= CD.CVE_GPO_EMPRESA \n"+
			" AND U.CVE_EMPRESA (+)= CD.CVE_EMPRESA \n"+
			" AND U.CVE_USUARIO (+)= CD.CVE_VERIFICADOR \n"+
			" AND P.CVE_GPO_EMPRESA (+)= U.CVE_GPO_EMPRESA \n"+
			" AND P.CVE_EMPRESA (+)= U.CVE_EMPRESA \n"+
			" AND P.ID_PERSONA (+)= U.ID_PERSONA \n";
			
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
		
		sSql =  "INSERT INTO SIM_CLIENTE_DOCUMENTO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PERSONA, \n" +
				"ID_DOCUMENTO, \n" +
				"FECHA_RECIBIDO, \n" +
				"CVE_VERIFICADOR, \n" +
				"DOCUMENTO_CORRECTO) \n" +
		          "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PERSONA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_DOCUMENTO") + "', \n" +
				"TO_DATE('" + (String)registro.getDefCampo("FECHA_RECIBIDO") + "','DD/MM/YYYY'), \n" +
				"'" + (String)registro.getDefCampo("CVE_VERIFICADOR") + "', \n" +
				"'" + (String)registro.getDefCampo("DOCUMENTO_CORRECTO") + "') \n" ;
		
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
		sSql = " UPDATE SIM_CLIENTE_DOCUMENTO SET "+
			   " FECHA_RECIBIDO    	 =TO_DATE('" + (String)registro.getDefCampo("FECHA_RECIBIDO") + "','DD/MM/YYYY'), \n" +
			   " CVE_VERIFICADOR     ='" + (String)registro.getDefCampo("CVE_VERIFICADOR")  + "', \n" +
			   " DOCUMENTO_CORRECTO  ='" + (String)registro.getDefCampo("DOCUMENTO_CORRECTO")  + "' \n" +
			   " WHERE ID_DOCUMENTO  ='" + (String)registro.getDefCampo("ID_DOCUMENTO") + "' \n" +
			   " AND ID_PERSONA 	 ='" + (String)registro.getDefCampo("ID_PERSONA") + "'\n"+
			   " AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
			   " AND CVE_EMPRESA 	 ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			  
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
		sSql =  " DELETE FROM SIM_CLIENTE_DOCUMENTO " +
		 	" WHERE ID_DOCUMENTO  ='" + (String)registro.getDefCampo("ID_DOCUMENTO") + "' \n" +
			" AND ID_PERSONA 	 ='" + (String)registro.getDefCampo("ID_PERSONA") + "'\n"+
			" AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
			" AND CVE_EMPRESA 	 ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}