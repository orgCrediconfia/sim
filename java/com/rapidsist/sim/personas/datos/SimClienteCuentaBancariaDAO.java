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
 * Administra los cuentas bancarias asignados a los clientes.
 */
 
public class SimClienteCuentaBancariaDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

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
				"C.CVE_GPO_EMPRESA, \n"+
				"C.CVE_EMPRESA, \n"+
				"C.ID_PERSONA, \n"+	
				"C.ID_CUENTA, \n"+ 
				"C.CVE_BANCO, \n"+
				"C.NUMERO_CUENTA, \n"+
				"C.CLABE, \n"+
				"B.NOM_BANCO \n"+
			"FROM SIM_CLIENTE_CUENTA C, \n"+
			"     SIM_CAT_BANCO B \n"+
			" WHERE C.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND C.ID_PERSONA = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n"+
			" AND B.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			" AND B.CVE_EMPRESA = C.CVE_EMPRESA \n"+
			" AND B.CVE_BANCO = C.CVE_BANCO \n";
			
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
				"C.CVE_GPO_EMPRESA, \n"+
				"C.CVE_EMPRESA, \n"+
				"C.ID_PERSONA, \n"+	
				"C.ID_CUENTA, \n"+ 
				"C.CVE_BANCO, \n"+
				"C.NUMERO_CUENTA, \n"+
				"C.CLABE \n"+
			"FROM SIM_CLIENTE_CUENTA C \n"+
			" WHERE C.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND C.ID_PERSONA = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n"+
			" AND C.ID_CUENTA = '" + (String)parametros.getDefCampo("ID_CUENTA") + "' \n";
			
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
		
		String sIdCuenta = "";
		
		//OBTENEMOS EL SEQUENCE	   
		sSql = "SELECT SQ01_SIM_CLIENTE_CUENTA.nextval as ID_CUENTA FROM DUAL";
		ejecutaSql();
		if (rs.next()){
			sIdCuenta = rs.getString("ID_CUENTA");
		}
			
		sSql =  "INSERT INTO SIM_CLIENTE_CUENTA ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PERSONA, \n" +
				"ID_CUENTA, \n" +
				"CVE_BANCO, \n" +
				"NUMERO_CUENTA, \n" +
				"CLABE) \n" +
		          "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PERSONA") + "', \n" +
				sIdCuenta + ", \n "+
				"'" + (String)registro.getDefCampo("CVE_BANCO") + "', \n" +
				"'" + (String)registro.getDefCampo("NUMERO_CUENTA") + "', \n" +
				"'" + (String)registro.getDefCampo("CLABE") + "') \n" ;
				
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
		sSql = " UPDATE SIM_CLIENTE_CUENTA SET "+
			   " CVE_BANCO    	='" + (String)registro.getDefCampo("CVE_BANCO")  + "', \n" +
			   " NUMERO_CUENTA    	='" + (String)registro.getDefCampo("NUMERO_CUENTA")  + "', \n" +
			   " CLABE    	='" + (String)registro.getDefCampo("CLABE")  + "' \n" +
			   " WHERE ID_CUENTA  ='" + (String)registro.getDefCampo("ID_CUENTA") + "' \n" +
			   " AND ID_PERSONA ='" + (String)registro.getDefCampo("ID_PERSONA") + "'\n"+
			   " AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
			   " AND CVE_EMPRESA 	='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
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
		sSql =  "DELETE FROM SIM_CLIENTE_CUENTA " +
		 	" WHERE ID_CUENTA  ='" + (String)registro.getDefCampo("ID_CUENTA") + "' \n" +
			   " AND ID_PERSONA ='" + (String)registro.getDefCampo("ID_PERSONA") + "'\n"+
			   " AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
			   " AND CVE_EMPRESA 	='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}