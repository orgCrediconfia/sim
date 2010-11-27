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
 * Administra los accesos a la base de datos para el catálogo de periodicidad.
 */
 
public class SimCatalogoPeriodicidadDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionModificacion, OperacionBaja  {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql = "SELECT \n"+
				" CVE_GPO_EMPRESA, \n"+
				" CVE_EMPRESA, \n"+
				" ID_PERIODICIDAD, \n"+
				" NOM_PERIODICIDAD, \n"+
				" CANTIDAD_PAGOS, \n"+
				" DIAS \n"+
				
				" FROM SIM_CAT_PERIODICIDAD \n"+
		
				" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n";
				
		if (parametros.getDefCampo("ID_PERIODICIDAD") != null) {
			sSql = sSql + " AND ID_PERIODICIDAD = '" + (String) parametros.getDefCampo("ID_PERIODICIDAD") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_PERIODICIDAD") != null) {
			sSql = sSql + " AND UPPER(NOM_PERIODICIDAD) LIKE'%" + ((String) parametros.getDefCampo("NOM_PERIODICIDAD")).toUpperCase()  + "%' \n";
		}
			
		sSql = sSql + " ORDER BY NOM_PERIODICIDAD \n";	
				
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
		sSql = "SELECT \n"+
			" CVE_GPO_EMPRESA, \n"+
			" CVE_EMPRESA, \n"+
			" ID_PERIODICIDAD, \n"+
			" NOM_PERIODICIDAD, \n"+
			" CANTIDAD_PAGOS CANTIDAD_PAGOS, \n"+
			" DIAS DIAS \n"+
			
			" FROM SIM_CAT_PERIODICIDAD \n"+
			" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_PERIODICIDAD ='" + (String)parametros.getDefCampo("ID_PERIODICIDAD") + "' \n";
			   
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
		
		String sIdPeriodicidad = "";
		
		sSql = "SELECT SQ01_SIM_CAT_PERIODICIDAD.nextval AS ID_PERIODICIDAD FROM DUAL";
		
		ejecutaSql();
		if (rs.next()){
			sIdPeriodicidad = rs.getString("ID_PERIODICIDAD");
		}
		
		
		sSql =  "INSERT INTO SIM_CAT_PERIODICIDAD ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PERIODICIDAD, \n" +
				"NOM_PERIODICIDAD, \n" +
				"CANTIDAD_PAGOS, \n" +
				"DIAS) \n" +
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdPeriodicidad + ", \n" +
				"'" + (String)registro.getDefCampo("NOM_PERIODICIDAD") + "', \n" +
				"'" + (String)registro.getDefCampo("CANTIDAD_PAGOS") + "', \n" +
				"'" + (String)registro.getDefCampo("DIAS") + "') \n" ;
		
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
		sSql = " UPDATE SIM_CAT_PERIODICIDAD SET "+
			   " NOM_PERIODICIDAD     				='" + (String)registro.getDefCampo("NOM_PERIODICIDAD")  + "', \n" +
			   " CANTIDAD_PAGOS     				='" + (String)registro.getDefCampo("CANTIDAD_PAGOS")  + "', \n" +
			   " DIAS     			                ='" + (String)registro.getDefCampo("DIAS")  + "' \n" +
			   " WHERE ID_PERIODICIDAD      	='" + (String)registro.getDefCampo("ID_PERIODICIDAD") + "' \n" +
			   " AND CVE_GPO_EMPRESA   		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_EMPRESA   			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
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
		sSql = "DELETE FROM SIM_CAT_PERIODICIDAD " +
	 	   " WHERE ID_PERIODICIDAD  ='" + (String)registro.getDefCampo("ID_PERIODICIDAD") + "' \n" +
		   " AND CVE_GPO_EMPRESA  ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
		   " AND CVE_EMPRESA  ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
		
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}

}