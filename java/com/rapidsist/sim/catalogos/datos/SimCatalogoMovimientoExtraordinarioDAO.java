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
 * Administra los accesos a la base de datos para el catálogo de periodicidad.
 */
 
public class SimCatalogoMovimientoExtraordinarioDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionModificacion, OperacionBaja  {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql = "SELECT \n"+
				" M.CVE_GPO_EMPRESA, \n"+
				" M.CVE_EMPRESA, \n"+
				" M.ID_MOVIMIENTO_EXTRA, \n"+
				" M.NOM_MOVIMIENTO_EXTRA, \n"+
				" M.ID_TIPO_MOVIMIENTO, \n"+
				" TM.NOM_TIPO_MOVIMIENTO \n"+
			" FROM SIM_CAT_MOVIMIENTO_EXTRA M, \n"+
			"      SIM_CAT_TIPO_MOVIMIENTO_EXTRA TM \n"+
				" WHERE M.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND M.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND TM.CVE_GPO_EMPRESA = M.CVE_GPO_EMPRESA \n"+
				" AND TM.CVE_EMPRESA = M.CVE_EMPRESA \n"+
				" AND TM.ID_TIPO_MOVIMIENTO = M.ID_TIPO_MOVIMIENTO \n";
				
		if (parametros.getDefCampo("ID_MOVIMIENTO_EXTRA") != null) {
			sSql = sSql + " AND M.ID_MOVIMIENTO_EXTRA = '" + (String) parametros.getDefCampo("ID_MOVIMIENTO_EXTRA") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_MOVIMIENTO_EXTRA") != null) {
			sSql = sSql + " AND UPPER(M.NOM_MOVIMIENTO_EXTRA) LIKE'%" + ((String) parametros.getDefCampo("NOM_MOVIMIENTO_EXTRA")).toUpperCase()  + "%' \n";
		}
			
		sSql = sSql + " ORDER BY M.ID_MOVIMIENTO_EXTRA \n";	
				
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
		sSql = "SELECT \n"+
				" M.CVE_GPO_EMPRESA, \n"+
				" M.CVE_EMPRESA, \n"+
				" M.ID_MOVIMIENTO_EXTRA, \n"+
				" M.NOM_MOVIMIENTO_EXTRA, \n"+
				" M.ID_TIPO_MOVIMIENTO, \n"+
				" TM.NOM_TIPO_MOVIMIENTO \n"+
			" FROM SIM_CAT_MOVIMIENTO_EXTRA M, \n"+
			"      SIM_CAT_TIPO_MOVIMIENTO_EXTRA TM \n"+
			" WHERE M.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND M.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND M.ID_MOVIMIENTO_EXTRA = '" + (String)parametros.getDefCampo("ID_MOVIMIENTO_EXTRA") + "' \n"+
			" AND TM.CVE_GPO_EMPRESA = M.CVE_GPO_EMPRESA \n"+
			" AND TM.CVE_EMPRESA = M.CVE_EMPRESA \n"+
			" AND TM.ID_TIPO_MOVIMIENTO = M.ID_TIPO_MOVIMIENTO \n";
			
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
		
		String sIdMovimientoExtra = "";
		
		sSql = "SELECT SQ01_SIM_CAT_MOVIMIENTO_EXTRA.nextval AS ID_MOVIMIENTO_EXTRA FROM DUAL";
		
		ejecutaSql();
		if (rs.next()){
			sIdMovimientoExtra = rs.getString("ID_MOVIMIENTO_EXTRA");
		}
		
		
		sSql =  "INSERT INTO SIM_CAT_MOVIMIENTO_EXTRA ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_MOVIMIENTO_EXTRA, \n" +
				"NOM_MOVIMIENTO_EXTRA, \n" +
				"ID_TIPO_MOVIMIENTO) \n" +
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdMovimientoExtra + ", \n" +
				"'" + (String)registro.getDefCampo("NOM_MOVIMIENTO_EXTRA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_TIPO_MOVIMIENTO") + "') \n" ;
		
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
		sSql = " UPDATE SIM_CAT_MOVIMIENTO_EXTRA SET "+
			   " NOM_MOVIMIENTO_EXTRA     		='" + (String)registro.getDefCampo("NOM_MOVIMIENTO_EXTRA")  + "', \n" +
			   " ID_TIPO_MOVIMIENTO     		='" + (String)registro.getDefCampo("ID_TIPO_MOVIMIENTO")  + "' \n" +
			   " WHERE ID_MOVIMIENTO_EXTRA      	='" + (String)registro.getDefCampo("ID_MOVIMIENTO_EXTRA") + "' \n" +
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
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo baja(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		//BORRA LA FUNCION
		sSql = "DELETE FROM SIM_CAT_MOVIMIENTO_EXTRA " +
	 	   " WHERE ID_MOVIMIENTO_EXTRA  ='" + (String)registro.getDefCampo("ID_MOVIMIENTO_EXTRA") + "' \n" +
		   " AND CVE_GPO_EMPRESA  ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
		   " AND CVE_EMPRESA  ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
		
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}

}