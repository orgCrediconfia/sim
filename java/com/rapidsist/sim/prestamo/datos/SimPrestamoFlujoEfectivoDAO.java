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
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para las garantía del préstamo.
 */
 
public class SimPrestamoFlujoEfectivoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS DE BANCOS
	
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
		
		sSql =  " UPDATE SIM_PRESTAMO_FLUJO_EFECTIVO SET "+
				" URL_ARCHIVO		='" + (String)registro.getDefCampo("URL_ARCHIVO")  + "' \n" +
				" WHERE ID_ARCHIVO_FLUJO  	='" + (String)registro.getDefCampo("ID_ARCHIVO_FLUJO") + "' \n" +
				" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND ID_PRESTAMO   	='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
		System.out.println("alta flujo de efectivo:*******************"+sSql);
		
			//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
			
		return resultadoCatalogo;
	}
}