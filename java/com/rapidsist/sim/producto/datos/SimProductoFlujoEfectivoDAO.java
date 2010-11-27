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
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de producto.
 */
 
public class SimProductoFlujoEfectivoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionBaja, OperacionModificacion {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  " SELECT \n"+
			" CVE_GPO_EMPRESA, \n" +
			" CVE_EMPRESA, \n" +
			" ID_PRODUCTO, \n"+
			" ID_ARCHIVO_FLUJO, \n" +
			" NOM_ARCHIVO \n" +
			" FROM SIM_PRODUCTO_FLUJO_EFECTIVO \n"+	
			" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_PRODUCTO ='" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n";
		
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
			" CVE_GPO_EMPRESA, \n" +
			" CVE_EMPRESA, \n" +
			" ID_PRODUCTO, \n"+
			" ID_ARCHIVO_FLUJO, \n" +
			" NOM_ARCHIVO \n" +
			" FROM SIM_PRODUCTO_FLUJO_EFECTIVO \n"+	
			" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND ID_PRODUCTO ='" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
			" AND ID_ARCHIVO_FLUJO ='" + (String)parametros.getDefCampo("ID_ARCHIVO_FLUJO") + "' \n";
			   
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
		
		String sIdArchivo = "";
		
		sSql = "SELECT SQ01_SIM_PROD_FLUJO_EFECTIVO.nextval AS ID_ARCHIVO_FLUJO FROM DUAL";
		
		ejecutaSql();
		if (rs.next()){
			sIdArchivo = rs.getString("ID_ARCHIVO_FLUJO");
		}
		
		sSql =  "INSERT INTO SIM_PRODUCTO_FLUJO_EFECTIVO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PRODUCTO, \n"+
				"ID_ARCHIVO_FLUJO, \n" +
				"NOM_ARCHIVO) \n" +
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
				sIdArchivo + ", \n" +
				"'" + (String)registro.getDefCampo("NOM_ARCHIVO") + "') \n" ;
				
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
		sSql = " UPDATE SIM_PRODUCTO_FLUJO_EFECTIVO SET "+
			   " NOM_ARCHIVO     				='" + (String)registro.getDefCampo("NOM_ARCHIVO")  + "' \n" +
			   " WHERE ID_ARCHIVO_FLUJO      	='" + (String)registro.getDefCampo("ID_ARCHIVO_FLUJO") + "' \n" +
			   " AND ID_PRODUCTO   		='" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
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
		//BORRA EL REGISTRO
		sSql = "DELETE FROM SIM_PRODUCTO_FLUJO_EFECTIVO" +
				" WHERE ID_PRODUCTO		='" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
				" AND ID_ARCHIVO_FLUJO	='" + (String)registro.getDefCampo("ID_ARCHIVO_FLUJO") + "' \n"+
				" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}

}