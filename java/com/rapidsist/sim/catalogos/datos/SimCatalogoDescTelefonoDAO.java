/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.catalogos.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para el catálogo de tipo de teléfono.
 */
 
public class SimCatalogoDescTelefonoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS DE BANCOS
		
		sSql =  "SELECT \n"+
				" CVE_GPO_EMPRESA, \n"+
				" CVE_EMPRESA, \n"+
				" ID_DESC_TEL, \n"+
				" NOM_DESC_TEL \n"+
			" FROM RS_GRAL_DESC_TEL \n"+
			" WHERE CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CVE_EMPRESA='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n";
		
		if (parametros.getDefCampo("ID_DESC_TEL") != null) {
			sSql = sSql + " AND ID_DESC_TEL = '" + (String) parametros.getDefCampo("ID_DESC_TEL") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_DESC_TEL") != null) {
			sSql = sSql + " AND UPPER(NOM_DESC_TEL) LIKE'%" + ((String) parametros.getDefCampo("NOM_DESC_TEL")).toUpperCase()  + "%' \n";
		}
		
		sSql = sSql + " ORDER BY ID_DESC_TEL \n";
		
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
		sSql =  " SELECT \n"+
				" CVE_GPO_EMPRESA, \n"+
				" CVE_EMPRESA, \n"+
				" ID_DESC_TEL, \n"+
				" NOM_DESC_TEL \n"+
			" FROM RS_GRAL_DESC_TEL \n"+
			" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_DESC_TEL = '" + (String)parametros.getDefCampo("ID_DESC_TEL") + "' \n";
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecuciï¿½n de este mï¿½todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		String sIdDescTel = "";
		
		//SE OBTIENE EL SEQUENCE
					   
		sSql = "SELECT SQ01_RS_GRAL_DESC_TEL.nextval as ID_DESC_TEL FROM DUAL";
		ejecutaSql();
		if (rs.next()){
			sIdDescTel = rs.getString("ID_DESC_TEL");
		}
		
		
		sSql =  "INSERT INTO RS_GRAL_DESC_TEL ( \n"+
			"CVE_GPO_EMPRESA, \n" +
			"CVE_EMPRESA, \n" +
			"ID_DESC_TEL, \n" +
			"NOM_DESC_TEL) \n" +
		        "VALUES ( \n"+
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			sIdDescTel + ", \n "+
			"'" + (String)registro.getDefCampo("NOM_DESC_TEL") + "') \n" ;

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
		sSql =  " UPDATE RS_GRAL_DESC_TEL SET "+
			" NOM_DESC_TEL  	='" + (String)registro.getDefCampo("NOM_DESC_TEL")  + "' \n" +
			" WHERE ID_DESC_TEL    	='" + (String)registro.getDefCampo("ID_DESC_TEL") + "' \n" +
			" AND CVE_EMPRESA   	='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA   ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
	
	/**
	 * Borra un registro.
	 * @param registro Llave primaria.
	 * @return Objeto que contiene el resultado de la ejecuciï¿½n de este mï¿½todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo baja(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		//BORRA LA FUNCION
		sSql =  "DELETE FROM RS_GRAL_DESC_TEL " +
			" WHERE ID_DESC_TEL		='" + (String)registro.getDefCampo("ID_DESC_TEL") + "' \n" +
			" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}