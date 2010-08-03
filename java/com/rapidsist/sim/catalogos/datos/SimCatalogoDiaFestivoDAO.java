/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.catalogos.datos;

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
 * Administra los accesos a la base de datos para el catálogo Dias Festivos.
 */
 
public class SimCatalogoDiaFestivoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS DE DIAS FESTIVOS
		
		sSql =  "SELECT \n"+
			"	CVE_GPO_EMPRESA, \n"+
			"	CVE_EMPRESA, \n"+
			"	CVE_PAIS, \n"+
			"	F_DIA_FESTIVO, \n"+
			"   DESC_DIA_FESTIVO \n"+
			"FROM PFIN_DIA_FESTIVO \n"+
			"WHERE CVE_GPO_EMPRESA='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			"AND CVE_EMPRESA='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n";
		 
		if (parametros.getDefCampo("F_DIA_FESTIVO") != null) {
			
			sSql = sSql + " AND F_DIA_FESTIVO = '" + (String) parametros.getDefCampo("F_DIA_FESTIVO") + "' \n";
		}
		
		if (parametros.getDefCampo("DESC_DIA_FESTIVO") != null) {
			sSql = sSql + " AND UPPER(DESC_DIA_FESTIVO) LIKE'%" + ((String) parametros.getDefCampo("DESC_DIA_FESTIVO")).toUpperCase()  + "%' \n";
		}
		
				sSql = sSql + " ORDER BY F_DIA_FESTIVO \n";
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
			   " CVE_PAIS, \n"+
			   " F_DIA_FESTIVO, \n"+
			   " DESC_DIA_FESTIVO \n"+
			   " FROM PFIN_DIA_FESTIVO\n"+
			   " WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND F_DIA_FESTIVO = TO_DATE('"  + (String)parametros.getDefCampo("F_DIA_FESTIVO") + "','DD/MM/YYYY') \n";
		
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
		
		sSql =  "INSERT INTO PFIN_DIA_FESTIVO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"CVE_PAIS, \n" +
				"F_DIA_FESTIVO, \n" +
				" DESC_DIA_FESTIVO) \n"+
		        "VALUES ( \n"+
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			"'MX', \n" +
				"TO_DATE('" + (String)registro.getDefCampo("F_DIA_FESTIVO") + "','DD/MM/YYYY'), \n"+ 
			"'" + (String)registro.getDefCampo("DESC_DIA_FESTIVO") + "') \n" ;
		
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
		

				sSql = " UPDATE PFIN_DIA_FESTIVO SET "+
			   " DESC_DIA_FESTIVO  	 = '" + (String)registro.getDefCampo("DESC_DIA_FESTIVO")  + "' \n" +
			   " WHERE F_DIA_FESTIVO   = TO_DATE('"+(String)registro.getDefCampo("F_DIA_FESTIVO") + "','DD/MM/YYYY')\n";
				
							
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
		sSql =  " DELETE FROM PFIN_DIA_FESTIVO " +
			" WHERE F_DIA_FESTIVO   = TO_DATE('"+(String)registro.getDefCampo("F_DIA_FESTIVO") + "','DD/MM/YYYY')\n";
			

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}