/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.personas.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra de los integrantes de la UEF del Cliente.
 */
 
public class SimClienteIntegranteUEFDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  "SELECT \n"+
				"I.CVE_GPO_EMPRESA, \n" +
				"I.CVE_EMPRESA, \n" +
				"I.ID_PERSONA, \n" +
				"I.ID_INTEGRANTE, \n" +
				"I.AP_PATERNO, \n" +
				"I.AP_MATERNO, \n" +
				"I.NOMBRE_1, \n" +
				"I.NOMBRE_2, \n" +
				"I.NOM_COMPLETO, \n" +
				"I.ID_PARENTESCO, \n" +
				"P.NOM_PARENTESCO \n" +
			"FROM SIM_CLIENTE_INTEGRANTE_UEF I, \n"+
			"     SIM_CAT_PARENTESCO P \n"+
			" WHERE I.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND I.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND I.ID_PERSONA = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n"+
			" AND P.CVE_GPO_EMPRESA = I.CVE_GPO_EMPRESA \n"+
			" AND P.CVE_EMPRESA = I.CVE_EMPRESA \n"+
			" AND P.ID_PARENTESCO = I.ID_PARENTESCO \n";
			
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
		
		sSql =  "SELECT \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PERSONA, \n" +
				"ID_INTEGRANTE, \n" +
				"AP_PATERNO, \n" +
				"AP_MATERNO, \n" +
				"NOMBRE_1, \n" +
				"NOMBRE_2, \n" +
				"NOM_COMPLETO, \n" +
				"ID_PARENTESCO \n" +
			"FROM SIM_CLIENTE_INTEGRANTE_UEF \n"+
			" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_PERSONA = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n"+
			" AND ID_INTEGRANTE = '" + (String)parametros.getDefCampo("ID_INTEGRANTE") + "' \n";
		
			
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
		
		String sIdIntegrante = "";
		
		//OBTENEMOS EL SEQUENCE	   
		sSql = "SELECT SQ01_SIM_INTEGRANTE_UEF.nextval as ID_INTEGRANTE FROM DUAL";
		ejecutaSql();
		if (rs.next()){
			sIdIntegrante = rs.getString("ID_INTEGRANTE");
		}
			
		sSql =  "INSERT INTO SIM_CLIENTE_INTEGRANTE_UEF ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PERSONA, \n" +
				"ID_INTEGRANTE, \n" +
				"AP_PATERNO, \n" +
				"AP_MATERNO, \n" +
				"NOMBRE_1, \n" +
				"NOMBRE_2, \n" +
				"NOM_COMPLETO, \n" +
				"ID_PARENTESCO) \n" +
		          "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PERSONA") + "', \n" +
				sIdIntegrante + ", \n "+
				"'" + (String)registro.getDefCampo("AP_PATERNO") + "', \n" +
				"'" + (String)registro.getDefCampo("AP_MATERNO") + "', \n" +
				"'" + (String)registro.getDefCampo("NOMBRE_1") + "', \n" +
				"'" + (String)registro.getDefCampo("NOMBRE_2") + "', \n" +
				"'" + (String)registro.getDefCampo("NOM_COMPLETO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PARENTESCO") + "') \n" ;
		
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
		sSql = " UPDATE SIM_CLIENTE_INTEGRANTE_UEF SET "+
			   " AP_PATERNO    	='" + (String)registro.getDefCampo("AP_PATERNO")  + "', \n" +
			   " AP_MATERNO    	='" + (String)registro.getDefCampo("AP_MATERNO")  + "', \n" +
			   " NOMBRE_1    	='" + (String)registro.getDefCampo("NOMBRE_1")  + "', \n" +
			   " NOMBRE_2    	='" + (String)registro.getDefCampo("NOMBRE_2")  + "', \n" +
			   " NOM_COMPLETO    	='" + (String)registro.getDefCampo("NOM_COMPLETO")  + "', \n" +
			   " ID_PARENTESCO    	='" + (String)registro.getDefCampo("ID_PARENTESCO")  + "' \n" +
			" WHERE CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_PERSONA = '" + (String)registro.getDefCampo("ID_PERSONA") + "' \n"+
			" AND ID_INTEGRANTE = '" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n";
			   
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
		sSql =  " DELETE FROM SIM_CLIENTE_INTEGRANTE_UEF " +
		 	" WHERE CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_PERSONA = '" + (String)registro.getDefCampo("ID_PERSONA") + "' \n"+
			" AND ID_INTEGRANTE = '" + (String)registro.getDefCampo("ID_INTEGRANTE") + "' \n";

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}