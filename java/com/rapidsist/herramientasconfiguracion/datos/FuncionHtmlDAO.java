/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.herramientasconfiguracion.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta; 
import com.rapidsist.portal.catalogos.OperacionBaja; 
import com.rapidsist.portal.catalogos.OperacionModificacion; 
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el editor de HTML de funciones.
 */


public class FuncionHtmlDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	String sCvePublicacion;
	
	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		sSql = "SELECT \n"+
				" CVE_HTML, \n"+
				" NOM_HTML, \n"+
				" CVE_USUARIO \n"+
				" FROM RS_CONF_FUNCION_HTML \n"+
				" WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"   AND CVE_PORTAL = '" + (String)parametros.getDefCampo("CVE_PORTAL") + "' \n";
				
		if (parametros.getDefCampo("CVE_HTML") != null) {
			sSql = sSql + " AND UPPER(CVE_HTML) LIKE'%" + ((String) parametros.getDefCampo("CVE_HTML")).toUpperCase() + "%' \n";
		}
		
		if (parametros.getDefCampo("NOM_HTML") != null) {
			sSql = sSql + " AND UPPER(NOM_HTML) LIKE'%" + ((String) parametros.getDefCampo("NOM_HTML")).toUpperCase()  + "%' \n";
		}
		
		sSql = sSql + " ORDER BY CVE_HTML, NOM_HTML \n";
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
	
	//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER UN REGISTRO.	 
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql = "SELECT \n"+
				 " CVE_HTML, \n"+
				 " NOM_HTML, \n"+
				 " CODIGO_HTML, \n"+
				 " CVE_USUARIO \n" +
			" FROM RS_CONF_FUNCION_HTML \n" +
			" WHERE CVE_HTML = '" + (String) parametros.getDefCampo("CVE_HTML") + "' \n" +
			"   AND CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"   AND CVE_PORTAL = '" + (String)parametros.getDefCampo("CVE_PORTAL") + "' \n";
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
		
		sSql = "INSERT INTO RS_CONF_FUNCION_HTML (  \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_PORTAL, \n" +
					"CVE_USUARIO, \n" +
					"F_REGISTRO, \n" +
					"CODIGO_HTML, \n"+
					"CVE_HTML, \n" +
					"NOM_HTML) \n" +
				" VALUES ( \n" +
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_PORTAL") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
					"SYSDATE, \n"+ 
					"? , \n"+
					"'" + (String)registro.getDefCampo("CVE_HTML") + "', \n" +
					"'" + (String)registro.getDefCampo("NOM_HTML") + "') \n";

		//DA DE ALTA EL CODIGO HTML EN LA BASE DE DATOS
		PreparedStatement ps = this.conn.prepareStatement(sSql);
		String sCodigoHtml = (String)registro.getDefCampo("CODIGO_HTML");
		ByteArrayInputStream byteArray = new ByteArrayInputStream((sCodigoHtml).getBytes());
		ps.setAsciiStream(1, byteArray, sCodigoHtml.length());
		//EJECUTA EL PreparedStatement
		int iNumRegistros = ps.executeUpdate();
		
		//VERIFICA SI NO SE MODIFICO EL REGISTRO
		if (iNumRegistros == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		ps.close();
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
		sSql = "UPDATE RS_CONF_FUNCION_HTML SET  \n"+
					" CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "',  \n" +
					" CVE_PORTAL ='" + (String)registro.getDefCampo("CVE_PORTAL") + "',  \n" +
					" CVE_HTML ='" + (String)registro.getDefCampo("CVE_HTML") + "',  \n" +
					" NOM_HTML ='" + (String)registro.getDefCampo("NOM_HTML") + "',  \n" +
					" F_REGISTRO =SYSDATE,  \n" +
					" CODIGO_HTML = ? , \n" + 
					" CVE_USUARIO ='" + (String)registro.getDefCampo("CVE_USUARIO") + "'  \n" +
				" WHERE CVE_HTML = '" + (String)registro.getDefCampo("CVE_HTML") + "'  \n" +
				" AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "'  \n"+
				" AND CVE_PORTAL = '" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n";
		//DA DE ALTA EL DOCUMENTO EN LA BASE DE DATOS
		PreparedStatement ps = this.conn.prepareStatement(sSql);
		String sCodigoHtml = (String)registro.getDefCampo("CODIGO_HTML");
		ByteArrayInputStream byteArray = new ByteArrayInputStream((sCodigoHtml).getBytes());
		ps.setAsciiStream(1, byteArray, sCodigoHtml.length());
		//EJECUTA EL PreparedStatement
		int iNumRegistros = ps.executeUpdate();
		//VERIFICA SI NO SE MODIFICO EL REGISTRO
		if (iNumRegistros == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		ps.close();
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

		//BORRA LA PUBLICACION
		sSql = "DELETE FROM RS_CONF_FUNCION_HTML \n " +
					" WHERE CVE_HTML = '" + (String)registro.getDefCampo("CVE_HTML") + "' \n" +
					" AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					" AND CVE_PORTAL = '" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n";
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}		
}