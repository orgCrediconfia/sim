/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.parametroglobal.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para el catálogo de giro.
 */
 
public class SimParametroGlobalMinMaxGrupoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

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
			"	NUM_INTEGRANTE, \n"+
			"	MAX_AMBULANTE, \n"+
			"	MAX_CATALOGO, \n"+
			"	MAXIMO_RIESGO \n"+
			"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n"+
			"ORDER BY NUM_INTEGRANTE \n";
			
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
	
		if (parametros.getDefCampo("VALOR").equals("Minimo")){
			
			sSql =  "SELECT \n"+
				"	MIN(NUM_INTEGRANTE) MINIMO_INTEGRANTES \n"+
				"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n";
				
		}else if (parametros.getDefCampo("VALOR").equals("Maximo")){
			sSql =  "SELECT \n"+
				"	MAX(NUM_INTEGRANTE) MAXIMO_INTEGRANTES \n"+
				"FROM SIM_PARAMETRO_GLOBAL_GRUPO \n";
				
		}
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
		
		sSql =  "INSERT INTO SIM_PARAMETRO_GLOBAL_GRUPO ( \n"+
			"	NUM_INTEGRANTE, \n"+
			"	MAX_AMBULANTE, \n"+
			"	MAX_CATALOGO, \n"+
			"	MAXIMO_RIESGO) \n"+
		        "VALUES ( \n"+
			"'" + (String)registro.getDefCampo("NUM_INTEGRANTE") + "', \n" +
			"'" + (String)registro.getDefCampo("MAX_AMBULANTE") + "', \n" +
			"'" + (String)registro.getDefCampo("MAX_CATALOGO") + "', \n" +
			"'" + (String)registro.getDefCampo("MAXIMO_RIESGO") + "') \n" ;
			
			//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
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
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		sSql = " UPDATE SIM_PARAMETRO_GLOBAL_GRUPO SET "+
			   " MAX_AMBULANTE	 = '" + (String)registro.getDefCampo("MAX_AMBULANTE")  + "', \n" +
			   " MAX_CATALOGO	 = '" + (String)registro.getDefCampo("MAX_CATALOGO")  + "', \n" +
			   " MAXIMO_RIESGO	 = '" + (String)registro.getDefCampo("MAXIMO_RIESGO")  + "' \n" +
			   " WHERE NUM_INTEGRANTE   = '" + (String)registro.getDefCampo("NUM_INTEGRANTE") + "' \n" ;
			  
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
		sSql =  " DELETE FROM SIM_PARAMETRO_GLOBAL_GRUPO " +
			" WHERE NUM_INTEGRANTE		='" + (String)registro.getDefCampo("NUM_INTEGRANTE") + "' \n" ;
			//" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			//" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}