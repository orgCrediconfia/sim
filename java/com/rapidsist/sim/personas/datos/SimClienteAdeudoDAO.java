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
 * Administra de los adeudos del Cliente.
 */
 
public class SimClienteAdeudoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

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
				"CVE_GPO_EMPRESA, \n"+
				"CVE_EMPRESA, \n"+
				"ID_PERSONA, \n"+	
				"ID_ADEUDO, \n"+ 
				"ADEUDO_A, \n"+
				"SALDO_ACTUAL, \n"+
				"FRECUENCIA_PAGO, \n"+
				"MONTO_PAGO, \n"+
				"FECHA \n"+
			"FROM SIM_CLIENTE_ADEUDO \n"+
			" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_PERSONA = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n";
			
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
				"CVE_GPO_EMPRESA, \n"+
				"CVE_EMPRESA, \n"+
				"ID_PERSONA, \n"+	
				"ID_ADEUDO, \n"+ 
				"ADEUDO_A, \n"+
				"SALDO_ACTUAL, \n"+
				"FRECUENCIA_PAGO, \n"+
				"MONTO_PAGO, \n"+
				"FECHA \n"+
			"FROM SIM_CLIENTE_ADEUDO \n"+
			" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_PERSONA = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n"+
			" AND ID_ADEUDO = '" + (String)parametros.getDefCampo("ID_ADEUDO") + "' \n";
			
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
		
		String sIdAdeudo = "";
		
		//OBTENEMOS EL SEQUENCE	   
		sSql = "SELECT SQ01_SIM_CLIENTE_ADEUDO.nextval as ID_ADEUDO FROM DUAL";
		ejecutaSql();
		if (rs.next()){
			sIdAdeudo = rs.getString("ID_ADEUDO");
		}
			
		sSql =  "INSERT INTO SIM_CLIENTE_ADEUDO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PERSONA, \n" +
				"ID_ADEUDO, \n" +
				"ADEUDO_A, \n" +
				"SALDO_ACTUAL, \n" +
				"FRECUENCIA_PAGO, \n" +
				"MONTO_PAGO, \n" +
				"FECHA) \n" +
		          "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PERSONA") + "', \n" +
				sIdAdeudo + ", \n "+
				"'" + (String)registro.getDefCampo("ADEUDO_A") + "', \n" +
				"'" + (String)registro.getDefCampo("SALDO_ACTUAL") + "', \n" +
				"'" + (String)registro.getDefCampo("FRECUENCIA_PAGO") + "', \n" +
				"'" + (String)registro.getDefCampo("MONTO_PAGO") + "', \n" +
				"TO_DATE('" + (String)registro.getDefCampo("FECHA") + "','DD/MM/YYYY')) \n" ;
		
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
		sSql = " UPDATE SIM_CLIENTE_ADEUDO SET "+
			   " ADEUDO_A    	='" + (String)registro.getDefCampo("ADEUDO_A")  + "', \n" +
			   " SALDO_ACTUAL    	='" + (String)registro.getDefCampo("SALDO_ACTUAL")  + "', \n" +
			   " FRECUENCIA_PAGO    	='" + (String)registro.getDefCampo("FRECUENCIA_PAGO")  + "', \n" +
			   " MONTO_PAGO    	='" + (String)registro.getDefCampo("MONTO_PAGO")  + "', \n" +
			   " FECHA    	= TO_DATE('" + (String)registro.getDefCampo("FECHA")  + "','DD/MM/YYYY') \n" +
			   " WHERE ID_ADEUDO  ='" + (String)registro.getDefCampo("ID_ADEUDO") + "' \n" +
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
		sSql =  " DELETE FROM SIM_CLIENTE_ADEUDO " +
		 	" WHERE ID_ADEUDO  ='" + (String)registro.getDefCampo("ID_ADEUDO") + "' \n" +
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