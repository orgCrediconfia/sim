/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.producto.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de producto.
 */
 
public class SimProductoCicloAccesorioDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		if (parametros.getDefCampo("CONSULTA").equals("ASIGNADAS")) {
			sSql =  "SELECT DISTINCT \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_PRODUCTO, \n"+
					"NUM_CICLO \n"+
				"FROM SIM_PRODUCTO_CICLO_ACCESORIO \n"+
				"WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND ID_PRODUCTO ='" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
				"ORDER BY NUM_CICLO \n";
		}else if (parametros.getDefCampo("CONSULTA").equals("PARA_ALTA")) {
			sSql =  "SELECT  \n"+
					"CA.CVE_GPO_EMPRESA, \n" +
					"CA.CVE_EMPRESA, \n" +
					"CA.ID_PRODUCTO, \n"+
					"CA.NUM_CICLO, \n"+
					"CA.ID_ACCESORIO, \n"+
					"CA.ORDEN, \n"+
					"A.NOM_ACCESORIO \n"+
				"FROM SIM_PRODUCTO_CICLO_ACCESORIO CA, \n"+
				"     SIM_CAT_ACCESORIO A \n"+
				"WHERE CA.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CA.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND CA.ID_PRODUCTO  ='" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
				"AND CA.NUM_CICLO = '" + (String)parametros.getDefCampo("NUM_CICLO") + "' \n"+
				"AND A.CVE_GPO_EMPRESA = CA.CVE_GPO_EMPRESA \n"+
				"AND A.CVE_EMPRESA = CA.CVE_EMPRESA \n"+
				"AND A.ID_ACCESORIO = CA.ID_ACCESORIO \n"+
				"ORDER BY CA.ID_ACCESORIO \n";
		}
			
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
	
		
			sSql =  "SELECT  \n"+
					"CA.CVE_GPO_EMPRESA, \n" +
					"CA.CVE_EMPRESA, \n" +
					"CA.ID_PRODUCTO, \n"+
					"CA.NUM_CICLO, \n"+
					"CA.ID_ACCESORIO, \n"+
					"CA.ORDEN, \n"+
					"A.NOM_ACCESORIO \n"+
				"FROM SIM_PRODUCTO_CICLO_ACCESORIO CA, \n"+
				"     SIM_CAT_ACCESORIO A \n"+
				"WHERE CA.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CA.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND CA.ID_PRODUCTO  ='" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
				"AND CA.NUM_CICLO = '" + (String)parametros.getDefCampo("NUM_CICLO") + "' \n"+
				"AND A.CVE_GPO_EMPRESA = CA.CVE_GPO_EMPRESA \n"+
				"AND A.CVE_EMPRESA = CA.CVE_EMPRESA \n"+
				"AND A.ID_ACCESORIO = CA.ID_ACCESORIO \n"+
				"ORDER BY CA.ID_ACCESORIO \n";
		
			
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
		
		String sOrdenAccesorio = new String();
		String sAccesorio = new String();
		String[] sOrden = (String[]) registro.getDefCampo("DAO_ORDEN");
		String[] sIdAccesorio = (String[]) registro.getDefCampo("DAO_ID_ACCESORIO");
		
		if (sOrden != null) {
			for (int iNumParametro = 0; iNumParametro < sOrden.length; iNumParametro++) {
				
				//OBTIENE LA CLAVE DE LA APLICACION
				sOrdenAccesorio = sOrden[iNumParametro];
				sAccesorio= sIdAccesorio[iNumParametro];
				
				registro.addDefCampo("ORDEN",sOrdenAccesorio);
				registro.addDefCampo("ID_ACCESORIO",sAccesorio);
				
				sSql =  " UPDATE SIM_PRODUCTO_CICLO_ACCESORIO SET "+
					" ORDEN			='" + (String)registro.getDefCampo("ORDEN") + "' \n" +
					" WHERE ID_PRODUCTO  	='" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
					" AND NUM_CICLO   	='" + (String)registro.getDefCampo("NUM_CICLO") + "' \n"+
					" AND ID_ACCESORIO   	='" + (String)registro.getDefCampo("ID_ACCESORIO") + "' \n"+
					" AND CVE_EMPRESA	='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					" AND CVE_GPO_EMPRESA	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";

				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
		}
		return resultadoCatalogo;
	}
}