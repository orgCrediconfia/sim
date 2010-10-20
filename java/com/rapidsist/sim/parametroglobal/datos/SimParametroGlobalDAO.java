/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.parametroglobal.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para los parámetro globales del sistema.
 */
 
public class SimParametroGlobalDAO extends Conexion2 implements OperacionConsultaRegistro, OperacionModificacion, OperacionAlta {

	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql =  "SELECT \n"+
			"	CVE_GPO_EMPRESA, \n"+
			"	CVE_EMPRESA, \n"+
			"	PORC_INT_FUNDADORES, \n"+
			"	IMP_DEUDA_MINIMA, \n"+
			"	IMP_VAR_PROPORCION, \n"+
			"	CREDITOS_SIMULTANEOS \n"+
			"FROM SIM_PARAMETRO_GLOBAL \n"+
			"WHERE CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			"AND CVE_EMPRESA='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n";
			
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
		
		sSql =  "INSERT INTO SIM_PARAMETRO_GLOBAL ( \n"+
			"	CVE_GPO_EMPRESA, \n"+
			"	CVE_EMPRESA, \n"+
			"	PORC_INT_FUNDADORES, \n"+
			"	IMP_DEUDA_MINIMA, \n"+
			"	IMP_VAR_PROPORCION, \n"+
			"	CREDITOS_SIMULTANEOS) \n"+
		        "VALUES ( \n"+
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("PORC_INT_FUNDADORES") + "', \n" +
			"'" + (String)registro.getDefCampo("IMP_DEUDA_MINIMA") + "', \n" +
			"'" + (String)registro.getDefCampo("IMP_VAR_PROPORCION") + "', \n" +
			"'" + (String)registro.getDefCampo("CREDITOS_SIMULTANEOS") + "') \n" ;
			
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
		sSql =  " UPDATE SIM_PARAMETRO_GLOBAL SET "+
			" PORC_INT_FUNDADORES	 = '" + (String)registro.getDefCampo("PORC_INT_FUNDADORES")  + "', \n" +
			" IMP_DEUDA_MINIMA	 	 = '" + (String)registro.getDefCampo("IMP_DEUDA_MINIMA")  + "', \n" +
			" CREDITOS_SIMULTANEOS	 = '" + (String)registro.getDefCampo("CREDITOS_SIMULTANEOS")  + "', \n" +
			" IMP_VAR_PROPORCION	 = '" + (String)registro.getDefCampo("IMP_VAR_PROPORCION")  + "' \n" +
			" WHERE CVE_EMPRESA    	 = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
			" AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			   
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
}