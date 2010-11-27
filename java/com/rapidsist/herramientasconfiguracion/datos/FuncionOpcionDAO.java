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
 * Administra los accesos a la base de datos para el catálogo de operaciones.
 */
public class FuncionOpcionDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros tomando como base el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		LinkedList lista = new LinkedList();
		Registro registro = new Registro();
		registro.addDefCampo("Clave", "CON_INICIALIZACION");
		registro.addDefCampo("Descripcion", "Controlador inicializacion");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("Clave", "CON_CONSULTA_REGISTRO");
		registro.addDefCampo("Descripcion", "Controlador consulta registro");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("Clave", "CON_CONSULTA_TABLA");
		registro.addDefCampo("Descripcion", "Controlador consulta tabla");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("Clave", "CON_ALTA");
		registro.addDefCampo("Descripcion", "Controlador alta");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("Clave", "CON_BAJA");
		registro.addDefCampo("Descripcion", "Controlador baja");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("Clave", "CON_MODIFICACION");
		registro.addDefCampo("Descripcion", "Controlador modificación");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("Clave", "CON_ACTUALIZACION");
		registro.addDefCampo("Descripcion", "Controlador actualización");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("Clave", "DAO_CONSULTA_REGISTRO");
		registro.addDefCampo("Descripcion", "DAO consulta registro");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("Clave", "DAO_CONSULTA_TABLA");
		registro.addDefCampo("Descripcion", "DAO consulta tabla");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("Clave", "DAO_ALTA");
		registro.addDefCampo("Descripcion", "DAO alta");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("Clave", "DAO_BAJA");
		registro.addDefCampo("Descripcion", "DAO baja");
		lista.add(registro);
		registro = new Registro();
		registro.addDefCampo("Clave", "DAO_MODIFICACION");
		registro.addDefCampo("Descripcion", "DAO modificación");
		lista.add(registro);
		return lista;


	}


	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql = "SELECT * FROM RS_CONF_APLICACION \n"+
			   "WHERE CVE_APLICACION='" + (String)parametros.getDefCampo("CVE_APLICACION") + "' \n" ;
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
		sSql = "INSERT INTO RS_CONF_APLICACION( \n"+
					   "CVE_APLICACION, \n"+
					   "NOM_APLICACION, \n"+
					   "TX_DESC_APLICACION, \n"+
					   "URL_APLICACION, \n"+
					   "NOM_ETIQUETA_MENU, \n"+
					   "B_BLOQUEADO) \n"+
				"VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_APLICACION") + "',\n" +
					"'" + (String)registro.getDefCampo("NOM_APLICACION") + "',\n" +
					"'" + (String)registro.getDefCampo("TX_DESC_APLICACION") + "',\n" +
					"'" + (String)registro.getDefCampo("URL_APLICACION") + "',\n" +
					"'" + (String)registro.getDefCampo("NOM_ETIQUETA_MENU") + "',\n" +
					"'" + (String)registro.getDefCampo("B_BLOQUEADO") + "')\n" ;
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
		sSql = "UPDATE RS_CONF_APLICACION SET \n" +
					 "NOM_APLICACION      ='" + (String)registro.getDefCampo("NOM_APLICACION") + "',\n" +
					 "TX_DESC_APLICACION  ='" + (String)registro.getDefCampo("TX_DESC_APLICACION") + "', \n" +
					 "URL_APLICACION      ='" + (String)registro.getDefCampo("URL_APLICACION") + "', \n" +
					 "NOM_ETIQUETA_MENU      ='" + (String)registro.getDefCampo("NOM_ETIQUETA_MENU") + "', \n" +
					 "B_BLOQUEADO         ='" + (String)registro.getDefCampo("B_BLOQUEADO") + "' \n" +
				" WHERE CVE_APLICACION  = '" + (String)registro.getDefCampo("CVE_APLICACION") + "'";

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
		sSql =  "SELECT CVE_APLICACION FROM RS_CONF_APLICACION_FUNCION \n"+
				"WHERE CVE_APLICACION= '" + (String)registro.getDefCampo("CVE_APLICACION") + "' \n";
		ejecutaSql();
		if (rs.next()){
			resultadoCatalogo.mensaje.setClave("NO_BORRAR_REGISTRO_RELACIONADO");
		}
		else{
			//BORRA LA FUNCION
			sSql = "DELETE FROM RS_CONF_APLICACION " +
					  " WHERE CVE_APLICACION='" + (String)registro.getDefCampo("CVE_APLICACION") + "' \n";

			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
		}
		return resultadoCatalogo;
	}
}