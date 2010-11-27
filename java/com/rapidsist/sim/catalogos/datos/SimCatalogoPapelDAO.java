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
 
public class SimCatalogoPapelDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionModificacion, OperacionBaja  {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  "SELECT \n"+
				" PA.CVE_GPO_EMPRESA, \n"+
				" PA.CVE_EMPRESA, \n"+
				" PA.ID_TASA_REFERENCIA, \n"+
				" PA.NOM_TASA_REFERENCIA, \n"+
				" PA.DESCRIPCION, \n"+
				" PA.ID_PERIODICIDAD, \n"+
				" PE.NOM_PERIODICIDAD \n"+
			" FROM SIM_CAT_TASA_REFERENCIA PA, \n"+
		        "      SIM_CAT_PERIODICIDAD PE \n"+
			" WHERE PA.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND PA.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND PE.CVE_GPO_EMPRESA = PA.CVE_GPO_EMPRESA \n"+
			" AND PE.CVE_EMPRESA = PA.CVE_EMPRESA \n"+
			" AND PE.ID_PERIODICIDAD = PA.ID_PERIODICIDAD \n";
				
		if (parametros.getDefCampo("ID_TASA_REFERENCIA") != null) {
			sSql = sSql + " AND PA.ID_TASA_REFERENCIA = '" + (String) parametros.getDefCampo("ID_TASA_REFERENCIA") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_TASA_REFERENCIA") != null) {
			sSql = sSql + " AND UPPER(PA.NOM_TASA_REFERENCIA) LIKE'%" + ((String) parametros.getDefCampo("NOM_TASA_REFERENCIA")).toUpperCase()  + "%' \n";
		}
			
		sSql = sSql + " ORDER BY ID_TASA_REFERENCIA \n";
		
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
				" NOM_TASA_REFERENCIA, \n"+
				" DESCRIPCION, \n"+
				" ID_PERIODICIDAD \n"+
			" FROM SIM_CAT_TASA_REFERENCIA \n"+ 
			" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_TASA_REFERENCIA = '" + (String)parametros.getDefCampo("ID_TASA_REFERENCIA") + "' \n";
			   
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
		
		String sIdPapel = "";
		
		sSql = "SELECT SQ01_SIM_CAT_PAPEL.nextval AS ID_TASA_REFERENCIA FROM DUAL";
		
		ejecutaSql();
		if (rs.next()){
			sIdPapel = rs.getString("ID_TASA_REFERENCIA");
		}
		
		
		sSql =  "INSERT INTO SIM_CAT_TASA_REFERENCIA ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_TASA_REFERENCIA, \n" +
				"NOM_TASA_REFERENCIA, \n" +
				"DESCRIPCION, \n" +
				"ID_PERIODICIDAD) \n" +
			"VALUES ( \n"+
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			sIdPapel + ", \n" +
			"'" + (String)registro.getDefCampo("NOM_TASA_REFERENCIA") + "', \n" +
			"'" + (String)registro.getDefCampo("DESCRIPCION") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_PERIODICIDAD") + "') \n" ;
		
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
		sSql =  " UPDATE SIM_CAT_TASA_REFERENCIA SET "+
			" NOM_TASA_REFERENCIA     	='" + (String)registro.getDefCampo("NOM_TASA_REFERENCIA")  + "', \n" +
			" DESCRIPCION    	='" + (String)registro.getDefCampo("DESCRIPCION")  + "', \n" +
			" ID_PERIODICIDAD       ='" + (String)registro.getDefCampo("ID_PERIODICIDAD")  + "' \n" +
			" WHERE ID_TASA_REFERENCIA     	='" + (String)registro.getDefCampo("ID_TASA_REFERENCIA") + "' \n" +
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
		sSql =  " DELETE FROM SIM_CAT_TASA_REFERENCIA " +
		 	" WHERE ID_TASA_REFERENCIA 	='" + (String)registro.getDefCampo("ID_TASA_REFERENCIA") + "' \n" +
			" AND CVE_GPO_EMPRESA   ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA  	='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
		
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
}