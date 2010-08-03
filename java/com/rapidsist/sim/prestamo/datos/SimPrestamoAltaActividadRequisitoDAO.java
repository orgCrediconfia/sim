/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para los montos por cliente del préstamo.
 */
 
public class SimPrestamoAltaActividadRequisitoDAO extends Conexion2 implements OperacionAlta, OperacionConsultaTabla, OperacionConsultaRegistro, OperacionModificacion, OperacionBaja {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS DE BANCOS
		
		sSql =  " SELECT \n"+
			" 	M.CVE_GPO_EMPRESA, \n"+
			" 	M.CVE_EMPRESA, \n"+
			" 	M.ID_PRESTAMO, \n"+
			" 	M.ID_CLIENTE, \n"+
			" 	M.MONTO_SOLICITADO, \n"+
			" 	M.MONTO_AUTORIZADO, \n"+
			" 	P.NOM_COMPLETO \n"+
			" FROM SIM_CLIENTE_MONTO M, \n"+
			"      RS_GRAL_PERSONA P \n"+
			" WHERE M.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND M.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND M.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND P.CVE_GPO_EMPRESA = M.CVE_GPO_EMPRESA \n" +
			" AND P.CVE_EMPRESA = M.CVE_EMPRESA \n"+
			" AND P.ID_PERSONA = M.ID_CLIENTE \n";
		
		sSql = sSql + " ORDER BY ID_CLIENTE \n";
		
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
			" 	M.CVE_GPO_EMPRESA, \n"+
			" 	M.CVE_EMPRESA, \n"+
			" 	M.ID_PRESTAMO, \n"+
			" 	M.ID_CLIENTE, \n"+
			" 	M.MONTO_SOLICITADO, \n"+
			" 	M.MONTO_AUTORIZADO, \n"+
			" 	P.NOM_COMPLETO \n"+
			" FROM SIM_CLIENTE_MONTO M, \n"+
			"      RS_GRAL_PERSONA P \n"+
			" WHERE M.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND M.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND M.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND P.CVE_GPO_EMPRESA = M.CVE_GPO_EMPRESA \n" +
			" AND P.CVE_EMPRESA = M.CVE_EMPRESA \n"+
			" AND P.ID_PERSONA = M.ID_CLIENTE \n";
			
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
		
		sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
		
		ejecutaSql();
		if (rs.next()){
			registro.addDefCampo("ID_HISTORICO",rs.getString("ID_HISTORICO"));
			
			sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_HISTORICO, \n" +
				"ID_PRESTAMO, \n" +
				"ID_ACTIVIDAD_REQUISITO, \n" +
				"ID_ETAPA_PRESTAMO, \n"+
				"FECHA_REGISTRO, \n"+
				"ORDEN_ETAPA, \n"+
				"ESTATUS) \n" +
				" VALUES (" +
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
				"'" + (String)registro.getDefCampo("PRESTAMO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
				"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
				"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "', \n" +
				"'" + (String)registro.getDefCampo("ESTATUS") + "') \n" ;
				
		}
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
		sSql =  " UPDATE SIM_CLIENTE_MONTO SET "+
			" MONTO_SOLICITADO		='" + (String)registro.getDefCampo("MONTO_SOLICITADO") + "' \n" +
			" WHERE ID_PRESTAMO  		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
			" AND ID_CLIENTE   		='" + (String)registro.getDefCampo("ID_CLIENTE") + "' \n";

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
		sSql =  " DELETE FROM SIM_PRESTAMO_ETAPA " +
			" WHERE ID_ACTIVIDAD_REQUISITO  ='" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "' \n" +
			" AND ID_PRESTAMO   		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
}