/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.catalogos.datos;

import com.rapidsist.portal.catalogos.OperacionAlta; 
import com.rapidsist.portal.catalogos.OperacionBaja; 
import com.rapidsist.portal.catalogos.OperacionModificacion; 
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de código postal
 * del modulo de aci.
 */
public class SimCatalogoCodigoPostalDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
		sSql =  "SELECT * FROM RS_GRAL_CODIGO_POSTAL \n" +
			" WHERE 1=1 \n ";
		
		if (parametros.getDefCampo("CODIGO_POSTAL") != null){
			sSql = sSql + " AND CODIGO_POSTAL = '" + (String)parametros.getDefCampo("CODIGO_POSTAL") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_ASENTAMIENTO") != null){
			sSql = sSql + " AND NOM_ASENTAMIENTO = '" + (String)parametros.getDefCampo("NOM_ASENTAMIENTO") + "' \n";
		}
		
		sSql = sSql + " ORDER BY NOM_ASENTAMIENTO \n";
	
		ejecutaSql();
		return this.getConsultaLista();
	}

	/**
	 * Obtiene un registro tomando como base la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		
		if (parametros.getDefCampo("ID_REFER_POST") == null){
			sSql =  "SELECT DISTINCT \n" +
					"CODIGO_POSTAL, \n" +
					"NOM_DELEGACION, \n" +
					"NOM_CIUDAD, \n" +
					"NOM_ESTADO \n" +
					"FROM RS_GRAL_CODIGO_POSTAL \n" +
					"WHERE CODIGO_POSTAL='" + (String)parametros.getDefCampo("CODIGO_POSTAL") + "' \n";
		}else if (parametros.getDefCampo("ID_REFER_POST") != null){
			sSql =  "SELECT DISTINCT \n" +
					"CODIGO_POSTAL, \n" +
					"ID_REFER_POST, \n" +
					"NOM_ASENTAMIENTO, \n" +
					"TIPO_ASENTAMIENTO, \n" +
					"NOM_DELEGACION, \n" +
					"NOM_CIUDAD, \n" +
					"NOM_ESTADO \n" +
					"FROM RS_GRAL_CODIGO_POSTAL \n" +
					"WHERE CODIGO_POSTAL = '" + (String)parametros.getDefCampo("CODIGO_POSTAL") + "' \n"+
					"AND ID_REFER_POST = '" + (String)parametros.getDefCampo("ID_REFER_POST") + "' \n";
		}
		
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
		
		sSql = "SELECT MAX(ID_REFER_POST) + 1 AS ID_REFER_POST \n" +
				"FROM RS_GRAL_CODIGO_POSTAL \n" +
				"WHERE CODIGO_POSTAL='" + (String)registro.getDefCampo("CODIGO_POSTAL") + "' \n";
		ejecutaSql();
		if (rs.next()){
			registro.addDefCampo("ID_REFER_POST",rs.getString("ID_REFER_POST"));
		}
		
		sSql = "INSERT INTO RS_GRAL_CODIGO_POSTAL ( \n"+
					"CODIGO_POSTAL, \n" +
					"ID_REFER_POST, \n" +
					"NOM_ASENTAMIENTO, \n" +
					"TIPO_ASENTAMIENTO, \n" +
					"NOM_DELEGACION, \n" +
					"NOM_CIUDAD, \n" +
					"NOM_ESTADO) \n" +
				"VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CODIGO_POSTAL")       + "', \n" +
					"'" + (String)registro.getDefCampo("ID_REFER_POST") + "', \n" +
					"'" + (String)registro.getDefCampo("NOM_ASENTAMIENTO") + "', \n" +
					"'" + (String)registro.getDefCampo("TIPO_ASENTAMIENTO") + "', \n" +
					"'" + (String)registro.getDefCampo("NOM_DELEGACION") + "', \n" +
					"'" + (String)registro.getDefCampo("NOM_CIUDAD") + "', \n" +
					"'" + (String)registro.getDefCampo("NOM_ESTADO")  + "') \n";
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
		sSql = "UPDATE RS_GRAL_CODIGO_POSTAL SET "+
					"NOM_ASENTAMIENTO      ='" + (String)registro.getDefCampo("NOM_ASENTAMIENTO")   + "', \n" +
					"TIPO_ASENTAMIENTO     ='" + (String)registro.getDefCampo("TIPO_ASENTAMIENTO")+ "', \n" +
					"NOM_DELEGACION        ='" + (String)registro.getDefCampo("NOM_DELEGACION")+ "', \n" +
					"NOM_CIUDAD            ='" + (String)registro.getDefCampo("NOM_CIUDAD")+ "', \n" +
					"NOM_ESTADO            ='" + (String)registro.getDefCampo("NOM_ESTADO")+ "' \n" +
				" WHERE CODIGO_POSTAL    ='" +  (String)registro.getDefCampo("CODIGO_POSTAL")   + "'\n"+
				" AND ID_REFER_POST ='" +  (String)registro.getDefCampo("ID_REFER_POST")   + "'\n";

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

		//BORRA EL MENSAJE
		sSql = "DELETE FROM RS_GRAL_CODIGO_POSTAL " +
			" WHERE CODIGO_POSTAL    ='" +  (String)registro.getDefCampo("CODIGO_POSTAL")   + "'\n"+
			" AND ID_REFER_POST ='" +  (String)registro.getDefCampo("ID_REFER_POST")   + "'\n";

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}


		return resultadoCatalogo;
	}
}
