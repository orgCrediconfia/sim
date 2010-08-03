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
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para las garantía del préstamo.
 */
 
public class SimPrestamoFlujoEfectivoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta, OperacionBaja, OperacionModificacion {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS DE BANCOS
		if (parametros.getDefCampo("CONSULTA").equals("PRESTAMOS")) {
			sSql =  "SELECT \n"+
					"P.ID_PRESTAMO, \n"+
					"P.FECHA_SOLICITUD, \n"+
					"P.FECHA_ENTREGA, \n"+
					"P.ID_CLIENTE, \n"+
					"P.ID_GRUPO, \n"+
					"C.NOM_COMPLETO, \n"+
					"G.NOM_GRUPO, \n"+
					"P.ID_PRODUCTO, \n"+
					"P.NUM_CICLO, \n"+
					"P.ID_ETAPA_PRESTAMO, \n"+
					"E.NOM_ESTATUS_PRESTAMO \n"+
				"FROM SIM_PRESTAMO P, \n"+
					"RS_GRAL_PERSONA C, \n"+
					"SIM_GRUPO G, \n" +
					"SIM_CAT_ETAPA_PRESTAMO E \n" +
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND C.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND C.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND C.ID_PERSONA (+)= P.ID_CLIENTE \n"+
				"AND G.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND G.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+ 
				"AND G.ID_GRUPO (+)= P.ID_GRUPO \n"+
				"AND E.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND E.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+ 
				"AND E.ID_ETAPA_PRESTAMO (+)= P.ID_ETAPA_PRESTAMO \n";
				
			if (parametros.getDefCampo("ID_PRESTAMO") != null) {
				sSql = sSql + "AND P.ID_PRESTAMO = '" + (String) parametros.getDefCampo("ID_PRESTAMO") + "' \n";
			}
			if (parametros.getDefCampo("ID_PRODUCTO") != null) {
				sSql = sSql + "AND P.ID_PRODUCTO = '" + (String) parametros.getDefCampo("ID_PRODUCTO") + "' \n";
			}
			if (parametros.getDefCampo("NUM_CICLO") != null) {
				sSql = sSql + "AND P.NUM_CICLO = '" + (String) parametros.getDefCampo("NUM_CICLO") + "' \n";
			}
			if (parametros.getDefCampo("FECHA_SOLICITUD") != null) {
				sSql = sSql + "AND P.FECHA_SOLICITUD >= TO_DATE('" + (String) parametros.getDefCampo("FECHA_SOLICITUD") + "','DD/MM/YYYY') \n";
			}
			if (parametros.getDefCampo("FECHA_ENTREGA") != null) {
				sSql = sSql + "AND P.FECHA_ENTREGA <= TO_DATE('" + (String) parametros.getDefCampo("FECHA_ENTREGA") + "','DD/MM/YYYY') \n";
			}
			if (parametros.getDefCampo("NOM_GRUPO") != null) {
				sSql = sSql + "AND UPPER(G.NOM_GRUPO) LIKE '%" + ((String) parametros.getDefCampo("NOM_GRUPO")).toUpperCase() + "%' \n";
			}
			if (parametros.getDefCampo("NOM_COMPLETO") != null) {
				sSql = sSql + " AND UPPER(C.NOM_COMPLETO) LIKE '%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase() + "%' \n";
			}
			if (parametros.getDefCampo("ID_ETAPA_PRESTAMO") != null) {
				sSql = sSql + "AND P.ID_ETAPA_PRESTAMO = '" + (String) parametros.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
			}
			
			sSql = sSql + "ORDER BY P.ID_PRESTAMO \n";
		}else if (parametros.getDefCampo("CONSULTA").equals("ARCHIVOS")) {
			sSql =  "SELECT \n"+
					"CVE_GPO_EMPRESA, \n"+
					"CVE_EMPRESA, \n"+
					"ID_PRESTAMO, \n"+
					"ID_ARCHIVO_FLUJO, \n"+
					"NOM_ARCHIVO, \n"+
					"URL_ARCHIVO \n"+
				"FROM SIM_PRESTAMO_FLUJO_EFECTIVO \n"+
				"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n";
				
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
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		sSql =  " UPDATE SIM_PRESTAMO_FLUJO_EFECTIVO SET "+
			" URL_ARCHIVO		='" + (String)registro.getDefCampo("URL_ARCHIVO")  + "' \n" +
			" WHERE ID_ARCHIVO_FLUJO  	='" + (String)registro.getDefCampo("ID_ARCHIVO_FLUJO") + "' \n" +
			" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND ID_PRESTAMO   	='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
			
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