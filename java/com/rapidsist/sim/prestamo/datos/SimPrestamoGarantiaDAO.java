/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para las garantía del préstamo.
 */
 
public class SimPrestamoGarantiaDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta, OperacionBaja {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS DE BANCOS
		
		if(parametros.getDefCampo("FILTRO").equals("Disponibles")){
			sSql =  " SELECT \n"+ 
				"CVE_GPO_EMPRESA, \n"+
				"CVE_EMPRESA, \n"+
				"ID_GARANTIA, \n"+
				"DESCRIPCION \n"+
				"FROM SIM_CLIENTE_GARANTIA \n"+
				"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
				"AND ID_PERSONA = '" + (String)parametros.getDefCampo("ID_PERSONA") + "' \n"+
				"UNION \n"+
				"SELECT \n"+
				"G.CVE_GPO_EMPRESA, \n"+
				"G.CVE_EMPRESA, \n"+
				"G.ID_GARANTIA, \n"+
				"G.DESCRIPCION \n"+
				"FROM SIM_PRESTAMO_PARTICIPANTE P, \n"+
				"SIM_CLIENTE_GARANTIA G \n"+
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
				"AND P.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
				"AND P.CVE_TIPO_PERSONA = 'GARANTE' \n"+
				"AND G.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND G.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND G.ID_PERSONA = P.ID_PERSONA \n"+
				"MINUS \n"+ 
				"SELECT \n"+
				"P.CVE_GPO_EMPRESA, \n"+
				"P.CVE_EMPRESA, \n"+
				"P.ID_GARANTIA, \n"+
				"C.DESCRIPCION \n"+
				"FROM SIM_PRESTAMO_GARANTIA P, \n"+
				"SIM_CLIENTE_GARANTIA C \n"+
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
				"AND P.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
				"AND C.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND C.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND C.ID_GARANTIA = P.ID_GARANTIA \n";
				
		}else if (parametros.getDefCampo("FILTRO").equals("ASIGNADOS")){
			sSql =  " SELECT \n"+ 
				"P.CVE_GPO_EMPRESA, \n"+
				"P.CVE_EMPRESA, \n"+
				"P.ID_GARANTIA, \n"+
				"C.DESCRIPCION \n"+
				"FROM SIM_PRESTAMO_GARANTIA P, \n"+
				"SIM_CLIENTE_GARANTIA C \n"+
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
				"AND P.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
				"AND C.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND C.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND C.ID_GARANTIA = P.ID_GARANTIA \n";
		}
		
		ejecutaSql();
		return getConsultaLista();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
			sSql =  "INSERT INTO SIM_PRESTAMO_GARANTIA ( \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_GARANTIA, \n" +
					"ID_PRESTAMO) \n" +
			        "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_GARANTIA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "') \n" ;
				
			//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
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
		sSql =  " DELETE FROM SIM_PRESTAMO_GARANTIA " +
			" WHERE CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_PRESTAMO ='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND ID_GARANTIA ='" + (String)registro.getDefCampo("ID_GARANTIA") + "' \n";
			
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
}