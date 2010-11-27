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
 * Administra los accesos a la base de datos para el catálogo de documentación.
 */
 
public class SimCatalogoDocumentacionDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionModificacion, OperacionBaja  {

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
				" D.CVE_GPO_EMPRESA, \n"+
				" D.CVE_EMPRESA, \n"+
				" D.ID_DOCUMENTO, \n"+
				" D.NOM_DOCUMENTO, \n"+
				" D.ID_REPORTE, \n"+
				" R.NOM_REPORTE, \n"+
				" D.DESCRIPCION, \n"+
				" D.ID_TIPO_DOCUMENTACION, \n"+
				" C.NOM_TIPO_DOCUMENTACION \n"+
				" FROM SIM_CAT_DOCUMENTO D, \n"+
				"      SIM_CAT_TIPO_DOCUMENTACION C, \n"+
				"      SIM_CAT_REPORTE R \n"+
				" WHERE D.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND D.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND C.CVE_GPO_EMPRESA = D.CVE_GPO_EMPRESA \n"+
				" AND C.CVE_EMPRESA = D.CVE_EMPRESA \n"+
				" AND C.ID_TIPO_DOCUMENTACION = D.ID_TIPO_DOCUMENTACION \n"+
				" AND R.CVE_GPO_EMPRESA (+)= D.CVE_GPO_EMPRESA \n"+
				" AND R.CVE_EMPRESA (+)= D.CVE_EMPRESA \n"+
				" AND R.ID_REPORTE (+)= D.ID_REPORTE \n";
				
		if (parametros.getDefCampo("ID_DOCUMENTO") != null) {
			sSql = sSql + " AND D.ID_DOCUMENTO = '" + (String) parametros.getDefCampo("ID_DOCUMENTO") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_DOCUMENTO") != null) {
			sSql = sSql + " AND UPPER(D.NOM_DOCUMENTO) LIKE'%" + ((String) parametros.getDefCampo("NOM_DOCUMENTO")).toUpperCase()  + "%' \n";
		}
		
		sSql = sSql + "ORDER BY ID_DOCUMENTO \n";
				
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
			   " ID_DOCUMENTO, \n"+
			   " NOM_DOCUMENTO, \n"+
			   " DESCRIPCION, \n"+
			   " ID_REPORTE, \n"+
			   " B_REPORTE, \n"+
			   " ID_TIPO_DOCUMENTACION \n"+
			   " FROM SIM_CAT_DOCUMENTO \n"+
			   
			   " WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND ID_DOCUMENTO = '" + (String)parametros.getDefCampo("ID_DOCUMENTO") + "' \n";
			   
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
		
		String sIdDocumento = "";
		
		sSql = "SELECT SQ01_SIM_CAT_DOCUMENTO.nextval AS ID_DOCUMENTO FROM DUAL";
		
		ejecutaSql();
		if (rs.next()){
			sIdDocumento = rs.getString("ID_DOCUMENTO");
		}
		
		
		sSql =  "INSERT INTO SIM_CAT_DOCUMENTO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_DOCUMENTO, \n"+
				"NOM_DOCUMENTO, \n"+
				"DESCRIPCION, \n"+
				"ID_REPORTE, \n"+
				"ID_TIPO_DOCUMENTACION, \n"+
				"B_REPORTE) \n"+
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdDocumento + ", \n" +
				"'" + (String)registro.getDefCampo("NOM_DOCUMENTO") + "', \n" +
				"'" + (String)registro.getDefCampo("DESCRIPCION") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_REPORTE") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_TIPO_DOCUMENTACION") + "', \n" +
				"'" + (String)registro.getDefCampo("B_REPORTE") + "') \n" ;
		
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
		sSql = " UPDATE SIM_CAT_DOCUMENTO SET "+
			   " NOM_DOCUMENTO     		='" + (String)registro.getDefCampo("NOM_DOCUMENTO")  + "', \n" +
			   " ID_TIPO_DOCUMENTACION     	='" + (String)registro.getDefCampo("ID_TIPO_DOCUMENTACION")  + "', \n" +
			   " ID_REPORTE     		='" + (String)registro.getDefCampo("ID_REPORTE")  + "', \n" +
			   " DESCRIPCION     		='" + (String)registro.getDefCampo("DESCRIPCION")  + "', \n" +
			   " B_REPORTE     		='" + (String)registro.getDefCampo("B_REPORTE")  + "' \n" +
			   " WHERE ID_DOCUMENTO      	='" + (String)registro.getDefCampo("ID_DOCUMENTO") + "' \n" +
			   " AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
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
		sSql = "DELETE FROM SIM_CAT_DOCUMENTO " +
	 	   " WHERE ID_DOCUMENTO  ='" + (String)registro.getDefCampo("ID_DOCUMENTO") + "' \n" +
		   " AND CVE_GPO_EMPRESA  ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
		   " AND CVE_EMPRESA  ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
		
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}

}