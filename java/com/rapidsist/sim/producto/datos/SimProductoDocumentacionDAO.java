/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.producto.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el los documentos de un producto.
 */
 
public class SimProductoDocumentacionDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta, OperacionBaja  {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		if (parametros.getDefCampo("Filtro").equals("Todos")){
			sSql = "SELECT \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_DOCUMENTO, \n"+
					"NOM_DOCUMENTO, \n"+
					"DESCRIPCION \n"+
					
					" FROM SIM_CAT_DOCUMENTO \n"+
					
					" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					" AND CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n";
					
			if (parametros.getDefCampo("ID_DOCUMENTO") != null) {
				sSql = sSql + " AND ID_DOCUMENTO = '" + (String) parametros.getDefCampo("ID_DOCUMENTO") + "' \n";
			}
			
			if (parametros.getDefCampo("NOM_DOCUMENTO") != null) {
				sSql = sSql + " AND UPPER(NOM_DOCUMENTO) LIKE'%" + ((String) parametros.getDefCampo("NOM_DOCUMENTO")).toUpperCase()  + "%' \n";
			}
		}else{
			sSql = "SELECT \n"+
					"PD.CVE_GPO_EMPRESA, \n" +
					"PD.CVE_EMPRESA, \n" +
					"PD.ID_DOCUMENTO, \n"+
					"PD.ID_PRODUCTO, \n"+
					"DO.ID_REPORTE, \n"+
					"R.NOM_REPORTE, \n"+
					"DO.NOM_DOCUMENTO \n"+
					" FROM SIM_PRODUCTO_DOCUMENTACION PD, \n"+
					" SIM_CAT_DOCUMENTO DO, \n"+
					" SIM_CAT_REPORTE R \n"+
					" WHERE PD.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					" AND PD.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND PD.ID_PRODUCTO ='" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
					" AND DO.CVE_GPO_EMPRESA = PD.CVE_GPO_EMPRESA \n"+
					" AND DO.CVE_EMPRESA = PD.CVE_EMPRESA \n"+
					" AND DO.ID_DOCUMENTO = PD.ID_DOCUMENTO \n"+
					" AND R.CVE_GPO_EMPRESA (+)= DO.CVE_GPO_EMPRESA \n"+
					" AND R.CVE_EMPRESA (+)= DO.CVE_EMPRESA \n"+
					" AND R.ID_REPORTE (+)= DO.ID_REPORTE \n";
			
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
		
		sSql =  "INSERT INTO SIM_PRODUCTO_DOCUMENTACION ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_DOCUMENTO, \n"+
				"ID_PRODUCTO) \n"+
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_DOCUMENTO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "') \n" ;
				
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
		//BORRA EL REGISTRO
		sSql = "DELETE FROM SIM_PRODUCTO_DOCUMENTACION" +
				" WHERE ID_DOCUMENTO		='" + (String)registro.getDefCampo("ID_DOCUMENTO") + "' \n" +
				" AND ID_PRODUCTO		='" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
				" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}

}