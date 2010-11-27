/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.catalogos.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de papeles.
 */
 
public class SimCatalogoPapelDetalleDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionModificacion, OperacionBaja  {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  " SELECT \n"+
				" CVE_GPO_EMPRESA, \n"+
				" CVE_EMPRESA, \n"+
				" ID_TASA_REFERENCIA, \n"+
				" ID_TASA_REFERENCIA_DETALLE, \n"+
				" FECHA_PUBLICACION, \n"+
				" VALOR \n"+
			" FROM SIM_CAT_TASA_REFER_DETALLE \n"+
			" WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_TASA_REFERENCIA = '" + (String)parametros.getDefCampo("ID_TASA_REFERENCIA") + "' \n";
						
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
				" ID_TASA_REFERENCIA, \n"+
				" ID_TASA_REFERENCIA_DETALLE, \n"+
				" FECHA_PUBLICACION, \n"+
				" VALOR \n"+
			" FROM SIM_CAT_TASA_REFER_DETALLE \n"+
			" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_TASA_REFERENCIA = '" + (String)parametros.getDefCampo("ID_TASA_REFERENCIA") + "' \n"+
			" AND ID_TASA_REFERENCIA_DETALLE = '" + (String)parametros.getDefCampo("ID_TASA_REFERENCIA_DETALLE") + "' \n";
			   
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
		
		String sIdPapelDetalle = "";
		
		sSql = "SELECT SQ01_SIM_CAT_PAPEL_DETALLE.nextval AS ID_TASA_REFERENCIA_DETALLE FROM DUAL";
		
		ejecutaSql();
		if (rs.next()){
			sIdPapelDetalle = rs.getString("ID_TASA_REFERENCIA_DETALLE");
		}
		
		sSql =  "INSERT INTO SIM_CAT_TASA_REFER_DETALLE ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_TASA_REFERENCIA, \n" +
				"ID_TASA_REFERENCIA_DETALLE, \n" +
				"FECHA_PUBLICACION, \n" +
				"VALOR) \n" +
			"VALUES ( \n"+
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_TASA_REFERENCIA") + "', \n" +
			sIdPapelDetalle + ", \n" +
			"TO_DATE('" + (String)registro.getDefCampo("FECHA_PUBLICACION") + "','DD/MM/YYYY'), \n" +
			"'" + (String)registro.getDefCampo("VALOR") + "') \n" ;
		
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
		ResultadoCatalogo resultadoCatalogo = new 	ResultadoCatalogo();
		sSql =  " UPDATE SIM_CAT_TASA_REFER_DETALLE SET "+
			" FECHA_PUBLICACION	=TO_DATE('" + (String)registro.getDefCampo("FECHA_PUBLICACION") + "','DD/MM/YYYY'), \n" +
			" VALOR    		='" + (String)registro.getDefCampo("VALOR")  + "' \n" +
			" WHERE ID_TASA_REFERENCIA     	='" + (String)registro.getDefCampo("ID_TASA_REFERENCIA") + "' \n" +
			" AND ID_TASA_REFERENCIA_DETALLE  ='" + (String)registro.getDefCampo("ID_TASA_REFERENCIA_DETALLE") + "' \n"+
			" AND CVE_GPO_EMPRESA   ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CVE_EMPRESA   	='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
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
		sSql =  " DELETE FROM SIM_CAT_TASA_REFER_DETALLE " +
		 	" WHERE ID_TASA_REFERENCIA  ='" + (String)registro.getDefCampo("ID_TASA_REFERENCIA") + "' \n" +
		 	" AND ID_TASA_REFERENCIA_DETALLE  ='" + (String)registro.getDefCampo("ID_TASA_REFERENCIA_DETALLE") + "' \n"+
			" AND CVE_GPO_EMPRESA  ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA  ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
}