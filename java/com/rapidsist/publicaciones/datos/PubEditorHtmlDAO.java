/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.publicaciones.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta; import com.rapidsist.portal.catalogos.OperacionBaja; import com.rapidsist.portal.catalogos.OperacionModificacion; import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de personas.
 */


public class PubEditorHtmlDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

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
				" A.CVE_GPO_EMPRESA, \n"+
				" A.CVE_PUB_HTML, \n"+
				" A.NOM_PUB_HTML, \n"+
				" A.CVE_USUARIO \n"+
				" FROM RS_PUB_HTML A \n"+
				" WHERE ";
				
		sSql = sSql + " A.CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" ;
		
		if (parametros.getDefCampo("CVE_PUB_HTML") != null) {
			sSql = sSql + " AND A.CVE_PUB_HTML LIKE'%" + (String) parametros.getDefCampo("CVE_PUB_HTML") + "%' \n";
		}
		
		if (parametros.getDefCampo("NOM_PUB_HTML") != null) {
			sSql = sSql + " AND UPPER(A.NOM_PUB_HTML) LIKE'%" + ((String) parametros.getDefCampo("NOM_PUB_HTML")).toUpperCase()  + "%' \n";
		}
		
		sSql = sSql + " ORDER BY A.CVE_PUB_HTML, A.NOM_PUB_HTML \n";
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
				 " A.CVE_GPO_EMPRESA, \n"+
				 " A.CVE_PUB_HTML, \n"+
				 " A.NOM_PUB_HTML, \n"+
				 " A.CODIGO_HTML, \n"+
				 " A.CVE_USUARIO \n" +
			" FROM RS_PUB_HTML A \n" +
			" WHERE A.CVE_PUB_HTML = '" + (String) parametros.getDefCampo("CVE_PUB_HTML") + "' \n" +
				" AND A.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" ;
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
		
		sSql = "INSERT INTO RS_PUB_HTML ( "+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_PORTAL, \n" +
					"CVE_USUARIO, \n" +
					"F_REGISTRO, \n" +
					"CODIGO_HTML, \n" +
					"CVE_PUB_HTML, \n" +
					"NOM_PUB_HTML) \n" +
				" VALUES (" +
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_PORTAL") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
					"SYSDATE, \n" +
					"'" + (String)registro.getDefCampo("CODIGO_HTML") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_PUB_HTML") + "', \n" +
					"'" + (String)registro.getDefCampo("NOM_PUB_HTML") + "')";
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

		sSql = "UPDATE RS_PUB_HTML SET "+
					" CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', " +
					" CVE_PUB_HTML ='" + (String)registro.getDefCampo("CVE_PUB_HTML") + "', " +
					" NOM_PUB_HTML ='" + (String)registro.getDefCampo("NOM_PUB_HTML") + "', " +
					" CVE_PORTAL ='" + (String)registro.getDefCampo("CVE_PORTAL") + "', " +
					" F_REGISTRO =SYSDATE, " +
					" CODIGO_HTML ='" + (String)registro.getDefCampo("CODIGO_HTML") + "', " +
					" CVE_USUARIO ='" + (String)registro.getDefCampo("CVE_USUARIO") + "' " +
				" WHERE CVE_PUB_HTML = '" + (String)registro.getDefCampo("CVE_PUB_HTML") + "' " +
				" AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA")+"'";
		//VERIFICA SI NO SE ACTUALIZÓ EL REGISTRO
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

		//BORRA LA PUBLICACION
		sSql = "DELETE FROM RS_PUB_HTML \n " +
					" WHERE CVE_PUB_HTML = '" + (String)registro.getDefCampo("CVE_PUB_HTML")+ "'"+
					" AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA")+"'";
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}		
}