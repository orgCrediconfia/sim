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
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra las cjas de la sucursal.
 */
 
public class SimSucursalCajaDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

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
			"ID_SUCURSAL, \n"+
			"ID_CAJA \n"+
			"FROM SIM_SUCURSAL_CAJA \n"+
			"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n";
			
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
				"T.CVE_GPO_EMPRESA, \n"+
				"T.CVE_EMPRESA, \n"+
				"T.ID_TELEFONO, \n"+	
				"T.IDENTIFICADOR, \n"+ 
				"T.CVE_TIPO_IDENTIFICADOR, \n"+
				"T.ID_DESC_TEL, \n"+
				"T.TELEFONO \n"+
			"FROM RS_GRAL_TELEFONO T \n"+
			" WHERE T.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND T.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND T.IDENTIFICADOR = '" + (String)parametros.getDefCampo("IDENTIFICADOR") + "' \n"+
			" AND T.CVE_TIPO_IDENTIFICADOR = 'REGION' \n"+
			" AND T.ID_TELEFONO = '" + (String)parametros.getDefCampo("ID_TELEFONO") + "' \n";
			
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
		
		String sCaja = "";
		int iCaja = 0;
		
		sSql =  "SELECT \n"+
				"MAX(ID_CAJA) ID_CAJA \n"+
				"FROM \n"+
				"SIM_SUCURSAL_CAJA \n"+
				"WHERE CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND ID_SUCURSAL = '" + (String)registro.getDefCampo("ID_SUCURSAL") + "' \n";
	
		ejecutaSql();
		if (rs.next()){
			
			sCaja = rs.getString("ID_CAJA") != null ? rs.getString("ID_CAJA") : "0"  ;
			
			iCaja = (Integer.parseInt(sCaja));
		
			iCaja ++;
			
			sCaja= String.valueOf(iCaja);
			registro.addDefCampo("ID_CAJA",sCaja);
			
			sSql = "INSERT INTO SIM_SUCURSAL_CAJA ( "+
			   "CVE_GPO_EMPRESA, \n" +
			   "CVE_EMPRESA, \n" +
			   "ID_SUCURSAL, \n" +
			   "ID_CAJA) \n" +
			" VALUES (" +
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_CAJA") + "') \n" ;
			
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
			
		}else {
			sSql = "INSERT INTO SIM_SUCURSAL_CAJA ( "+
			   "CVE_GPO_EMPRESA, \n" +
			   "CVE_EMPRESA, \n" +
			   "ID_SUCURSAL, \n" +
			   "ID_CAJA) \n" +
			" VALUES (" +
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
			"'1') \n" ;
			
			//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
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
		sSql = " UPDATE RS_GRAL_TELEFONO SET "+
			   " ID_DESC_TEL    	='" + (String)registro.getDefCampo("ID_DESC_TEL")  + "', \n" +
			   " TELEFONO    	='" + (String)registro.getDefCampo("TELEFONO")  + "' \n" +
			   " WHERE ID_TELEFONO  ='" + (String)registro.getDefCampo("ID_TELEFONO") + "' \n" +
			   " AND CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
			   " AND CVE_EMPRESA 	='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND IDENTIFICADOR 	='" + (String)registro.getDefCampo("IDENTIFICADOR") + "'\n"+
			   " AND CVE_TIPO_IDENTIFICADOR 	='REGION'\n";
			   
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
		sSql =  "DELETE FROM SIM_SUCURSAL_ASESOR " +
		 	" WHERE ID_ASESOR  ='" + (String)registro.getDefCampo("ID_ASESOR") + "' \n" +
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