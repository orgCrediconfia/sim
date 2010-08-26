/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.catalogos.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra los asesores para ser asignados a la sucursal.
 */
 
public class SimSucursalAsesorDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta, OperacionBaja {

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
			"U.CVE_GPO_EMPRESA, \n"+
			"U.CVE_EMPRESA, \n"+
			"U.CVE_USUARIO, \n"+
			"P.NOM_COMPLETO \n"+
			"FROM RS_GRAL_USUARIO U, \n"+
			"RS_GRAL_PERSONA P, \n"+
			"SIM_USUARIO_ACCESO_SUCURSAL US \n"+
			"WHERE U.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND U.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND U.CVE_PUESTO = 'AseSuc' \n"+
			"AND US.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
			"AND US.CVE_EMPRESA = U.CVE_EMPRESA \n"+
			"AND US.CVE_USUARIO = U.CVE_USUARIO \n"+
			"AND US.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
			"AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
			"AND P.CVE_EMPRESA = U.CVE_EMPRESA \n"+
			"AND P.ID_PERSONA = U.ID_PERSONA \n"+
			
			"MINUS \n"+
			
			"SELECT \n"+
			"A.CVE_GPO_EMPRESA, \n"+
			"A.CVE_EMPRESA, \n"+
			"A.CVE_USUARIO, \n"+
			"P.NOM_COMPLETO \n"+
			"FROM SIM_USUARIO_ACCESO_SUCURSAL A, \n"+
			"RS_GRAL_USUARIO U, \n"+
			"RS_GRAL_PERSONA P \n"+
			"WHERE A.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND A.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND A.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
			"AND U.CVE_GPO_EMPRESA = A.CVE_GPO_EMPRESA \n"+
			"AND U.CVE_EMPRESA = A.CVE_EMPRESA \n"+
			"AND U.CVE_USUARIO = A.CVE_USUARIO \n"+
			"AND U.CVE_PUESTO = 'AseSuc' \n"+
			"AND P.CVE_GPO_EMPRESA = U.CVE_GPO_EMPRESA \n"+
			"AND P.CVE_EMPRESA = U.CVE_EMPRESA \n"+
			"AND P.ID_PERSONA = U.ID_PERSONA \n"; 
	
		ejecutaSql();
		return getConsultaLista();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecuciï¿½n de este mï¿½todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		sSql =  "INSERT INTO SIM_USUARIO_ACCESO_SUCURSAL ( \n"+
			"CVE_GPO_EMPRESA, \n" +
			"CVE_EMPRESA, \n" +
			"ID_SUCURSAL, \n" +
			"CVE_USUARIO) \n" +
		        "VALUES ( \n"+
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_USUARIO") + "') \n" ;
		
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
		sSql =  "DELETE FROM SIM_USUARIO_ACCESO_SUCURSAL " +
		 	" WHERE CVE_USUARIO  ='" + (String)registro.getDefCampo("CVE_USUARIO") + "' \n" +
			" AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
			" AND CVE_EMPRESA 	='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_SUCURSAL 	='" + (String)registro.getDefCampo("ID_SUCURSAL") + "'\n";
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}