/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.herramientasconfiguracion.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta; import com.rapidsist.portal.catalogos.OperacionBaja; import com.rapidsist.portal.catalogos.OperacionModificacion; import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para el catálogo de portales.
 */
 
public class PortalDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros tomando como base el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	 
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		if (((String)parametros.getDefCampo("Filtro")).equals("Todos")){
			sSql = "SELECT * FROM RS_CONF_PORTAL \n" +
				" WHERE CVE_GPO_EMPRESA = '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "'";
		}
		else{
			//FILTRO = PortalesEstilos
			//(PORTALES CON SUS RESPECTIVOS ESTILOS PARA UN GRUPO DE EMPRESAS)
			sSql = "SELECT ES.CVE_PORTAL, ES.CVE_ESTILO \n" +
					 " FROM RS_CONF_PORTAL_ESTILO ES \n" +
					" WHERE ES.CVE_GPO_EMPRESA = '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" ORDER BY CVE_PORTAL, CVE_ESTILO ";

		}
		System.out.println("obtiene el portal"+sSql);
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
		sSql = "SELECT * FROM RS_CONF_PORTAL \n " +
				 " WHERE CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				   " AND CVE_PORTAL ='" + (String)parametros.getDefCampo("CVE_PORTAL") + "'";
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
		ResultadoCatalogo resultadoCatalogo= new ResultadoCatalogo();
		
		sSql = "INSERT INTO RS_CONF_PORTAL( \n"+
					   "CVE_GPO_EMPRESA, \n"+
					   "CVE_PORTAL, \n"+
					   "NOM_PORTAL, \n"+
					   "URL_ENCABEZADO, \n"+
					   "URL_PIE_PAGINA, \n"+
					   "URL_MENU_APLICACION, \n"+
					   "URL_CONTENIDO_INICIO, \n"+
					   "URL_CONTENIDO_FIN, \n"+
					   "TIPO_LETRA, \n"+
					   "TIPO_MENU, \n"+
					   "NOM_VENTANA) \n"+
				"VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "',\n" +
					"'" + (String)registro.getDefCampo("CVE_PORTAL") + "',\n" +
					"'" + (String)registro.getDefCampo("NOM_PORTAL") + "',\n" +
					"'" + (String)registro.getDefCampo("URL_ENCABEZADO") + "',\n" +
					"'" + (String)registro.getDefCampo("URL_PIE_PAGINA") + "',\n" +
					"'" + (String)registro.getDefCampo("URL_MENU_APLICACION") + "',\n" +
					"'" + (String)registro.getDefCampo("URL_CONTENIDO_INICIO") + "',\n" +
					"'" + (String)registro.getDefCampo("URL_CONTENIDO_FIN") + "',\n" +
					"'" + (String)registro.getDefCampo("TIPO_LETRA") + "',\n" +
					"'" + (String)registro.getDefCampo("TIPO_MENU") + "',\n" +
					"'" + (String)registro.getDefCampo("NOM_VENTANA") + "')\n" ;
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
		sSql = "UPDATE RS_CONF_PORTAL SET \n" +
					 "URL_ENCABEZADO  ='" + (String)registro.getDefCampo("URL_ENCABEZADO") + "',\n" +
					 "NOM_PORTAL  ='" + (String)registro.getDefCampo("NOM_PORTAL") + "',\n" +
					 "URL_PIE_PAGINA  ='" + (String)registro.getDefCampo("URL_PIE_PAGINA") + "', \n" +
					 "URL_MENU_APLICACION  ='" + (String)registro.getDefCampo("URL_MENU_APLICACION") + "', \n" +
					 "URL_CONTENIDO_INICIO  ='" + (String)registro.getDefCampo("URL_CONTENIDO_INICIO") + "', \n" +
					 "URL_CONTENIDO_FIN  ='" + (String)registro.getDefCampo("URL_CONTENIDO_FIN") + "', \n" +
					 "TIPO_LETRA  ='" + (String)registro.getDefCampo("TIPO_LETRA") + "', \n" +
					 "NOM_VENTANA     ='" + (String)registro.getDefCampo("NOM_VENTANA") + "', \n" +
					 "TIPO_MENU     ='" + (String)registro.getDefCampo("TIPO_MENU") + "' \n" +
			"WHERE CVE_GPO_EMPRESA  = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CVE_PORTAL = '" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n" ;

		//VERIFICA SI NO SE MODIFICO EL REGISTRO
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

		//VERIFICA SI LA FUNCION EXISTE EN LA TABLA RS_CONF_MENU
		sSql =  "SELECT CVE_APLICACION FROM RS_CONF_PORTAL_APL \n"+
				" WHERE CVE_GPO_EMPRESA= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					" AND CVE_PORTAL= '" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n";
		ejecutaSql();
		if (rs.next()){
			resultadoCatalogo.mensaje.setClave("NO_BORRAR_REGISTRO_RELACIONADO");
		}
		else{
			//BORRA LA FUNCION
			sSql = "DELETE FROM RS_CONF_PORTAL " +
				" WHERE CVE_GPO_EMPRESA='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND CVE_PORTAL='" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n";

			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
		}
		return resultadoCatalogo;
	}
}
