/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.herramientasconfiguracion.datos;

import com.rapidsist.portal.catalogos.OperacionAlta; import com.rapidsist.portal.catalogos.OperacionBaja; import com.rapidsist.portal.catalogos.OperacionModificacion; import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de portales default.
 */
public class PortalDefaultDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros tomando como base el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		sSql = " SELECT * FROM RS_CONF_PORTAL_DEFAULT \n" +
			   " ORDER BY CVE_PORTAL_DEFAULT \n";

		ejecutaSql();
		return this.getConsultaLista();
	}

	/**
	 * Obtiene un registro en base a la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql = " SELECT * \n " +
				 " FROM RS_CONF_PORTAL_DEFAULT PD, RS_GRAL_PERSONA PE, RS_GRAL_USUARIO US \n" +
			   " WHERE PD.CVE_PORTAL_DEFAULT='" + (String)parametros.getDefCampo("CVE_PORTAL_DEFAULT") + "' \n" +
				 " AND PD.CVE_GPO_EMPRESA = US.CVE_GPO_EMPRESA \n " +
				 " AND PD.CVE_USUARIO = US.CVE_USUARIO \n " +
				 " AND US.CVE_GPO_EMPRESA = PE.CVE_GPO_EMPRESA \n" +
				 " AND US.ID_PERSONA = PE.ID_PERSONA ";
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
		sSql = "INSERT INTO RS_CONF_PORTAL_DEFAULT ( \n"+
					"CVE_PORTAL_DEFAULT, \n" +
					"CVE_GPO_EMPRESA, \n" +
					"CVE_PORTAL, \n" +
					"CVE_ESTILO, \n" +
					"CVE_APLICACION, \n" +
					"CVE_USUARIO, \n" +
					"URL_DIR_DEFAULT) \n"+
				"VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_PORTAL_DEFAULT") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_PORTAL") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_ESTILO") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_APLICACION") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
					"'" + (String)registro.getDefCampo("URL_DIR_DEFAULT") + "') \n";
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
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		sSql = " UPDATE RS_CONF_PORTAL_DEFAULT SET "+
				 " CVE_PORTAL      ='" + (String)registro.getDefCampo("CVE_PORTAL") + "', \n" +
				 " CVE_ESTILO      ='" + (String)registro.getDefCampo("CVE_ESTILO") + "', \n" +
				 " CVE_APLICACION  ='" + (String)registro.getDefCampo("CVE_APLICACION") + "', \n" +
				 " CVE_USUARIO  ='" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
				 " URL_DIR_DEFAULT ='" + (String)registro.getDefCampo("URL_DIR_DEFAULT") + "' \n" +
			   " WHERE CVE_PORTAL_DEFAULT ='" + (String)registro.getDefCampo("CVE_PORTAL_DEFAULT") + "' \n";
		//VERIFICA SI MODIFICO EL REGISTRO
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

		sSql = "DELETE FROM RS_CONF_PORTAL_DEFAULT " +
				  " WHERE CVE_PORTAL_DEFAULT='" + (String)registro.getDefCampo("CVE_PORTAL_DEFAULT") + "' \n" ;

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}
