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
 * Administra los accesos a la base de datos para el cat??logo de Verificaci??n del pr??stamo.
 */
 
public class SimCatalogoVerificacionPrestamoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionModificacion, OperacionBaja  {

	/**
	 * Obtiene un conjunto de registros en base al filtro de b??squeda.
	 * @param parametros Par??metros que se le env??an a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  "SELECT \n"+
			"	CVE_GPO_EMPRESA, \n"+
			"	CVE_EMPRESA, \n"+
			"	ID_VERIFICACION_PRESTAMO, \n"+
			"	NOM_VERIFICACION_PRESTAMO \n"+
			"FROM SIM_CAT_VERIFICACION_PRESTAMO \n"+
			"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
			"AND ID_VERIFICACION_PRESTAMO = ID_VERIFICACION_PRESTAMO \n";
				
		if (parametros.getDefCampo("ID_VERIFICACION_PRESTAMO") != null) {
			sSql = sSql + " AND ID_VERIFICACION_PRESTAMO = '" + (String) parametros.getDefCampo("ID_VERIFICACION_PRESTAMO") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_VERIFICACION_PRESTAMO") != null) {
			sSql = sSql + " AND UPPER(NOM_VERIFICACION_PRESTAMO) LIKE'%" + ((String) parametros.getDefCampo("NOM_VERIFICACION_PRESTAMO")).toUpperCase()  + "%' \n";
		}
		
		sSql = sSql + " ORDER BY ID_VERIFICACION_PRESTAMO \n";
		
		ejecutaSql();
		return getConsultaLista();
	}
	
	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Par??metros que se le env??an a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql =  "SELECT \n"+
			"	CVE_GPO_EMPRESA, \n"+
			"	CVE_EMPRESA, \n"+
			"	ID_VERIFICACION_PRESTAMO, \n"+
			"	NOM_VERIFICACION_PRESTAMO \n"+   	   
			"FROM SIM_CAT_VERIFICACION_PRESTAMO \n"+
			"WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND ID_VERIFICACION_PRESTAMO = '" + (String)parametros.getDefCampo("ID_VERIFICACION_PRESTAMO") + "' \n";
			   
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecuci??n de este m??todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		String sIdVerificacionPrestamo = "";
		
		sSql = "SELECT SQ01_SIM_CAT_VER_PRESTAMO.nextval AS ID_VERIFICACION_PRESTAMO FROM DUAL";
				
		ejecutaSql();
		if (rs.next()){
			sIdVerificacionPrestamo = rs.getString("ID_VERIFICACION_PRESTAMO");
		}
		
		sSql =  "INSERT INTO SIM_CAT_VERIFICACION_PRESTAMO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_VERIFICACION_PRESTAMO, \n" +
				"NOM_VERIFICACION_PRESTAMO) \n" +
			"VALUES ( \n"+
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			sIdVerificacionPrestamo + ", \n "+
			"'" + (String)registro.getDefCampo("NOM_VERIFICACION_PRESTAMO") + "') \n" ;
			
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
		return resultadoCatalogo;
	}
	
	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecuci??n de este m??todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new 	ResultadoCatalogo();
		sSql =  " UPDATE SIM_CAT_VERIFICACION_PRESTAMO SET "+
			" NOM_VERIFICACION_PRESTAMO     	='" + (String)registro.getDefCampo("NOM_VERIFICACION_PRESTAMO")  + "' \n" +
			" WHERE ID_VERIFICACION_PRESTAMO        ='" + (String)registro.getDefCampo("ID_VERIFICACION_PRESTAMO") + "' \n" +
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
	 * @return Objeto que contiene el resultado de la ejecuci??n de este m??todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo baja(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		//BORRA LA FUNCION
		sSql =  " DELETE FROM SIM_CAT_VERIFICACION_PRESTAMO " +
	 	   	" WHERE ID_VERIFICACION_PRESTAMO  ='" + (String)registro.getDefCampo("ID_VERIFICACION_PRESTAMO") + "' \n" +
		   	" AND CVE_GPO_EMPRESA  ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA  ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}