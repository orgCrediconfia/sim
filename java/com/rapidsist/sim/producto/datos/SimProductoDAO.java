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
 
public class SimProductoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionModificacion, OperacionBaja  {

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
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PRODUCTO, \n"+
				"NOM_PRODUCTO, \n" +
				"APLICA_A, \n" +
				"CVE_METODO, \n"+
				"ID_PERIODICIDAD, \n" +
				"MONTO_MINIMO, \n" +
				"FECHA_INICIO_ACTIVACION, \n"+
				"FECHA_FIN_ACTIVACION, \n" +
				"FORMA_ENTREGA, \n"+
				"B_GARANTIA \n" +
				
				" FROM SIM_PRODUCTO \n"+
		
				" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND FECHA_BAJA_LOGICA IS NULL  \n";
				
		if (parametros.getDefCampo("ID_PRODUCTO") != null) {
			sSql = sSql + " AND ID_PRODUCTO = '" + (String) parametros.getDefCampo("ID_PRODUCTO") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_PRODUCTO") != null) {
			sSql = sSql + " AND UPPER(NOM_PRODUCTO) LIKE'%" + ((String) parametros.getDefCampo("NOM_PRODUCTO")).toUpperCase()  + "%' \n";
		}
		
		if (parametros.getDefCampo("FECHA_INICIO_ACTIVACION") != null) {
			sSql = sSql + " AND FECHA_INICIO_ACTIVACION >= TO_DATE('" + (String) parametros.getDefCampo("FECHA_INICIO_ACTIVACION") + "','DD/MM/YYYY') \n";
		}
		
		if (parametros.getDefCampo("FECHA_FIN_ACTIVACION") != null) {
			sSql = sSql + " AND FECHA_FIN_ACTIVACION <= TO_DATE('" + (String) parametros.getDefCampo("FECHA_FIN_ACTIVACION") + "','DD/MM/YYYY') \n";
		}
		
		sSql = sSql + "ORDER BY ID_PRODUCTO \n";
		
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
				"PR.CVE_GPO_EMPRESA, \n" +
				"PR.CVE_EMPRESA, \n" +
				"PR.ID_PRODUCTO, \n"+
				"PR.NOM_PRODUCTO, \n" +
				"PR.APLICA_A, \n" +
				"PR.CVE_METODO, \n"+
				"PR.ID_PERIODICIDAD, \n" +
				"PE.DIAS, \n" +
				"PR.MONTO_MINIMO, \n" +
				"PR.FECHA_INICIO_ACTIVACION, \n"+
				"PR.FECHA_FIN_ACTIVACION, \n" +
				"PR.FORMA_ENTREGA, \n"+
				"PR.B_GARANTIA, \n" +
			  	"PR.B_SUCURSALES \n" +
			   " FROM SIM_PRODUCTO PR, \n"+
			   "      SIM_CAT_PERIODICIDAD PE\n"+
			   
			   " WHERE PR.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND PR.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND PR.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
			   " AND PE.CVE_GPO_EMPRESA = PR.CVE_GPO_EMPRESA \n"+
			   " AND PE.CVE_EMPRESA = PR.CVE_EMPRESA \n"+
			   " AND PE.ID_PERIODICIDAD = PR.ID_PERIODICIDAD \n";
			   
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
		resultadoCatalogo.Resultado = new Registro();
		
		String sIdProducto = "";
		
		sSql = "SELECT SQ01_SIM_PRODUCTO.nextval AS ID_PRODUCTO FROM DUAL";
		
		ejecutaSql();
		if (rs.next()){
			sIdProducto = rs.getString("ID_PRODUCTO");
		}
		
		sSql =  "INSERT INTO SIM_PRODUCTO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PRODUCTO, \n"+
				"NOM_PRODUCTO, \n" +
				"APLICA_A, \n" +
				"CVE_METODO, \n"+
				"ID_PERIODICIDAD, \n" +
				"MONTO_MINIMO, \n" +
				"FECHA_INICIO_ACTIVACION, \n"+
				"FECHA_FIN_ACTIVACION, \n" +
				"FORMA_ENTREGA, \n"+
				"B_GARANTIA, \n" +
				"B_SUCURSALES) \n" +
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdProducto + ", \n" +
				"'" + (String)registro.getDefCampo("NOM_PRODUCTO") + "', \n" +
				"'" + (String)registro.getDefCampo("APLICA_A") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_METODO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PERIODICIDAD") + "', \n" +
				"'" + (String)registro.getDefCampo("MONTO_MINIMO") + "', \n" +
				"TO_DATE('" + (String)registro.getDefCampo("FECHA_INICIO_ACTIVACION") + "','DD/MM/YYYY HH24:MI:SS'), \n" +
				"TO_DATE('" + (String)registro.getDefCampo("FECHA_FIN_ACTIVACION") + "','DD/MM/YYYY HH24:MI:SS'), \n" +
				"'" + (String)registro.getDefCampo("FORMA_ENTREGA") + "', \n" +
				"'" + (String)registro.getDefCampo("B_GARANTIA") + "', \n" +
				"'" + (String)registro.getDefCampo("B_SUCURSALES") + "') \n" ;
				
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		resultadoCatalogo.Resultado.addDefCampo("ID_PRODUCTO", sIdProducto);
		resultadoCatalogo.Resultado.addDefCampo("TODAS_SUCURSALES", (String)registro.getDefCampo("B_SUCURSALES"));
		
		if (registro.getDefCampo("B_GARANTIA").equals("V")){
			sSql =  "INSERT INTO SIM_PRODUCTO_PARTICIPANTE ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PRODUCTO, \n"+
				"CVE_TIPO_PERSONA) \n" +
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdProducto + ", \n" +
				"'GARANTE') \n" ;
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
			
			sSql =  "INSERT INTO SIM_PRODUCTO_PARTICIPANTE ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PRODUCTO, \n"+
				"CVE_TIPO_PERSONA) \n" +
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdProducto + ", \n" +
				"'DEPOSIT') \n" ;
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
		ResultadoCatalogo resultadoCatalogo = new 	ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		Registro registroOriginal = new Registro();
		
		sSql = " UPDATE SIM_PRODUCTO SET "+
			   " NOM_PRODUCTO     		= '" + (String)registro.getDefCampo("NOM_PRODUCTO")  + "', \n" +
			   " APLICA_A     		= '" + (String)registro.getDefCampo("APLICA_A")  + "', \n" +
			   " CVE_METODO                 = '" + (String)registro.getDefCampo("CVE_METODO")  + "', \n" +
			   " ID_PERIODICIDAD     	= '" + (String)registro.getDefCampo("ID_PERIODICIDAD")  + "', \n" +
			   " MONTO_MINIMO     		= '" + (String)registro.getDefCampo("MONTO_MINIMO")  + "', \n" +
			   " FECHA_INICIO_ACTIVACION    = TO_DATE('" + (String)registro.getDefCampo("FECHA_INICIO_ACTIVACION")  + "','DD/MM/YYYY'), \n" +
			   " FECHA_FIN_ACTIVACION     	= TO_DATE('" + (String)registro.getDefCampo("FECHA_FIN_ACTIVACION")  + "','DD/MM/YYYY'), \n" +
			   " FORMA_ENTREGA     		= '" + (String)registro.getDefCampo("FORMA_ENTREGA")  + "', \n" +
			   " B_GARANTIA     		= '" + (String)registro.getDefCampo("B_GARANTIA")  + "', \n" +
			   " B_SUCURSALES     	        = '" + (String)registro.getDefCampo("B_SUCURSALES")  + "' \n" +  
			   " WHERE ID_PRODUCTO      	= '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
			   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		resultadoCatalogo.Resultado.addDefCampo("TODAS_SUCURSALES", (String)registro.getDefCampo("B_SUCURSALES"));
		resultadoCatalogo.Resultado.addDefCampo("ID_PRODUCTO", (String)registro.getDefCampo("ID_PRODUCTO"));
		
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
		sSql = " UPDATE SIM_PRODUCTO SET "+
		 	   " FECHA_BAJA_LOGICA     	= SYSDATE \n" +
		 	   " WHERE ID_PRODUCTO      ='" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
			   " AND CVE_GPO_EMPRESA    ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND CVE_EMPRESA        ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
}