/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
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
 * Administra los accesos a la base de datos para el catálogo de Almacen.
 */
 
public class SimCatalogoRechazoComiteDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionModificacion, OperacionBaja  {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql = "SELECT \n"+
				" CVE_GPO_EMPRESA, \n"+
				" CVE_EMPRESA, \n"+
				" ID_RECHAZO, \n"+
				" NOM_RECHAZO, \n"+
				" DESCRIPCION \n"+
				
				" FROM SIM_CAT_RECHAZO_COMITE \n"+
		
				"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
				"AND ID_RECHAZO = ID_RECHAZO \n";
				
				if (parametros.getDefCampo("ID_RECHAZO") != null) {
					sSql = sSql + " AND UPPER(ID_RECHAZO) LIKE'%" + ((String) parametros.getDefCampo("ID_RECHAZO")).toUpperCase()  + "%' \n";
				}
				
				if (parametros.getDefCampo("NOM_RECHAZO") != null) {
					sSql = sSql + " AND UPPER(NOM_RECHAZO) LIKE'%" + ((String) parametros.getDefCampo("NOM_RECHAZO")).toUpperCase()  + "%' \n";
				}
				
				if (parametros.getDefCampo("DESCRIPCION") != null) {
					sSql = sSql + " AND UPPER(DESCRIPCION) LIKE'%" + ((String) parametros.getDefCampo("DESCRIPCION")).toUpperCase()  + "%' \n";
				}
				ejecutaSql();
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
		sSql = " SELECT \n"+
			   " CVE_GPO_EMPRESA, \n"+
			   " CVE_EMPRESA, \n"+
			   " ID_RECHAZO, \n"+
			   " NOM_RECHAZO, \n"+
			   " DESCRIPCION \n"+
			   			   
			   " FROM SIM_CAT_RECHAZO_COMITE \n"+
			   
			   " WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND ID_RECHAZO = '" + (String)parametros.getDefCampo("ID_RECHAZO") + "' \n";
			   
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecuci¾n de este mÚtodo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		String sIdRechazo = "";
		
		sSql = "SELECT SQ01_SIM_CAT_RECHAZO_COMITE.nextval AS ID_RECHAZO FROM DUAL";
		
		
		ejecutaSql();
		if (rs.next()){
			sIdRechazo = rs.getString("ID_RECHAZO");
		}
		
		sSql =  "INSERT INTO SIM_CAT_RECHAZO_COMITE ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_RECHAZO, \n" +
				"NOM_RECHAZO, \n" +
				"DESCRIPCION) \n" +
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdRechazo + ", \n "+
				"'" + (String)registro.getDefCampo("NOM_RECHAZO") + "', \n" +
				"'" + (String)registro.getDefCampo("DESCRIPCION") + "') \n" ;
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
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
		ResultadoCatalogo resultadoCatalogo = new 	ResultadoCatalogo();
		sSql = " UPDATE SIM_CAT_RECHAZO_COMITE SET "+
			   " ID_RECHAZO     				='" + (String)registro.getDefCampo("ID_RECHAZO")  + "', \n" +
			   " NOM_RECHAZO     				='" + (String)registro.getDefCampo("NOM_RECHAZO")  + "', \n" +
			   " DESCRIPCION     				='" + (String)registro.getDefCampo("DESCRIPCION")  + "' \n" +
			   " WHERE ID_RECHAZO      	='" + (String)registro.getDefCampo("ID_RECHAZO") + "' \n" +
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
	 * @return Objeto que contiene el resultado de la ejecuci¾n de este mÚtodo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo baja(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		//BORRA LA FUNCION
		sSql = "DELETE FROM SIM_CAT_RECHAZO_COMITE " +
	 	   " WHERE ID_RECHAZO  ='" + (String)registro.getDefCampo("ID_RECHAZO") + "' \n" +
		   " AND CVE_GPO_EMPRESA  ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}

}