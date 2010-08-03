/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.producto.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de producto.
 */
 
public class SimProductoActividadRequisitoDisponibleDAO extends Conexion2 implements OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		//*******S  E    U S A
		sSql =  " SELECT \n"+
			" 	CAT.CVE_GPO_EMPRESA, \n"+
			" 	CAT.CVE_EMPRESA, \n"+
			" 	CAT.ID_ACTIVIDAD_REQUISITO, \n"+
			" 	CAT.NOM_ACTIVIDAD_REQUISITO, \n"+
			" 	CAT.DESCRIPCION, \n"+
			" 	CAT.APLICA_A, \n"+
			" 	CAT.TIPO, \n"+
			" 	CAT.APLICA_CICLO \n"+
			" FROM SIM_CAT_ACTIVIDAD_REQUISITO CAT \n"+
			" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND APLICA_A ='" + (String)parametros.getDefCampo("APLICA_A") + "' \n";
			
		if (parametros.getDefCampo("ID_ACTIVIDAD_REQUISITO") != null) {
			sSql = sSql + " AND CAT.ID_ACTIVIDAD_REQUISITO = '" + (String) parametros.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_ACTIVIDAD_REQUISITO") != null) {
			sSql = sSql + " AND UPPER(CAT.NOM_ACTIVIDAD_REQUISITO) LIKE'%" + ((String) parametros.getDefCampo("NOM_ACTIVIDAD_REQUISITO")).toUpperCase()  + "%' \n";
		}
		
		if (parametros.getDefCampo("TIPO") != null) {
			sSql = sSql + " AND CAT.TIPO = '" + (String) parametros.getDefCampo("TIPO") + "' \n";
		}
		
		if (parametros.getDefCampo("APLICA_CICLO") != null) {
			sSql = sSql + " AND CAT.APLICA_CICLO = '" + (String) parametros.getDefCampo("APLICA_CICLO") + "' \n";
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
		sSql = "SELECT \n"+
				"AR.CVE_GPO_EMPRESA, \n" +
				"AR.CVE_EMPRESA, \n" +
				"AR.ID_ACTIVIDAD_REQUISITO, \n"+
				"CAR.NOM_ACTIVIDAD_REQUISITO, \n"+
				"AR.ID_PRODUCTO, \n"+
				"AR.ID_ETAPA_PRESTAMO, \n" +
				"CEP.NOM_ESTATUS_PRESTAMO \n" +
				
				" FROM SIM_PRODUCTO_ETAPA_PRESTAMO AR, \n"+
				"      SIM_CAT_ACTIVIDAD_REQUISITO CAR, \n"+
				"      SIM_CAT_ETAPA_PRESTAMO CEP \n"+
		
				" WHERE AR.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND AR.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND AR.ID_PRODUCTO ='" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
				" AND AR.ID_ACTIVIDAD_REQUISITO ='" + (String)parametros.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "' \n"+
				" AND CAR.CVE_GPO_EMPRESA = AR.CVE_GPO_EMPRESA \n"+
				" AND CAR.CVE_EMPRESA = AR.CVE_EMPRESA \n"+
				" AND CAR.ID_ACTIVIDAD_REQUISITO = AR.ID_ACTIVIDAD_REQUISITO \n"+
				" AND CEP.CVE_GPO_EMPRESA = AR.CVE_GPO_EMPRESA \n"+
				" AND CEP.CVE_EMPRESA = AR.CVE_EMPRESA \n"+
				" AND CEP.ID_ETAPA_PRESTAMO = AR.ID_ETAPA_PRESTAMO \n";		
				
			   
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
		
		sSql =  "INSERT INTO SIM_PRODUCTO_ETAPA_PRESTAMO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_ACTIVIDAD_REQUISITO, \n"+
				"ID_PRODUCTO, \n"+
				"ID_ETAPA_PRESTAMO) \n" +
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "') \n" ;
				
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
		sSql = " UPDATE SIM_PRODUCTO_ETAPA_PRESTAMO SET "+
			   " ID_ETAPA_PRESTAMO     		= '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO")  + "' \n" +
			   
			   " WHERE ID_PRODUCTO      		= '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
			   " AND ID_ACTIVIDAD_REQUISITO   	= '" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "' \n" +
			   " AND CVE_GPO_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_EMPRESA   			= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
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
		sSql = "DELETE FROM SIM_PRODUCTO_ETAPA_PRESTAMO" +
				" WHERE ID_PRODUCTO		='" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
				" AND ID_ACTIVIDAD_REQUISITO	='" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "' \n"+
				" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}

}