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
 * Administra los accesos a la base de datos de las sucursales que tiene acceso al producto.
 */
 
public class SimProductoSucursalDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta, OperacionBaja  {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		if (parametros.getDefCampo("CONSULTA").equals("PARA_ALTA")) {
			sSql =  "SELECT \n"+
					" CVE_GPO_EMPRESA, \n"+
					" CVE_EMPRESA, \n"+
					" ID_SUCURSAL, \n"+
					" NOM_SUCURSAL \n"+
				" FROM SIM_CAT_SUCURSAL \n"+
				" WHERE CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				" AND CVE_EMPRESA='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				
				" MINUS \n"+
				
				"SELECT \n"+
					" PS.CVE_GPO_EMPRESA, \n"+
					" PS.CVE_EMPRESA, \n"+
					" PS.ID_SUCURSAL, \n"+
					" CS.NOM_SUCURSAL \n"+
				" FROM SIM_PRODUCTO_SUCURSAL PS, \n"+
				"      SIM_CAT_SUCURSAL CS \n"+
				" WHERE "+
				" PS.CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				" AND PS.CVE_EMPRESA='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND PS.ID_PRODUCTO='" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
				" AND CS.CVE_GPO_EMPRESA = PS.CVE_GPO_EMPRESA \n"+
				" AND CS.CVE_EMPRESA = PS.CVE_EMPRESA \n"+
				" AND CS.ID_SUCURSAL = PS.ID_SUCURSAL \n"+
				"  ORDER BY ID_SUCURSAL \n";
				
		}else if (parametros.getDefCampo("CONSULTA").equals("ASIGNADAS")) {
			sSql =  "SELECT \n"+
					" PS.CVE_GPO_EMPRESA, \n"+
					" PS.CVE_EMPRESA, \n"+
					" PS.ID_SUCURSAL, \n"+
					" PS.ID_PRODUCTO, \n"+
					" CS.NOM_SUCURSAL \n"+
				" FROM SIM_PRODUCTO_SUCURSAL PS, \n"+
				"      SIM_CAT_SUCURSAL CS \n"+
				" WHERE "+
				" PS.CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				" AND PS.CVE_EMPRESA='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND PS.ID_PRODUCTO='" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
				" AND CS.CVE_GPO_EMPRESA = PS.CVE_GPO_EMPRESA \n"+
				" AND CS.CVE_EMPRESA = PS.CVE_EMPRESA \n"+
				" AND CS.ID_SUCURSAL = PS.ID_SUCURSAL \n";
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
		
		sSql =  "INSERT INTO SIM_PRODUCTO_SUCURSAL ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_SUCURSAL, \n"+
				"ID_PRODUCTO) \n"+
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
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
		sSql = "DELETE FROM SIM_PRODUCTO_SUCURSAL" +
				" WHERE ID_SUCURSAL		='" + (String)registro.getDefCampo("ID_SUCURSAL") + "' \n" +
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