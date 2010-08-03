/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.catalogos.OperacionAlta; 
import com.rapidsist.portal.catalogos.OperacionBaja; 
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.Iterator;


/**
 * Administra los accesos a la base de datos para los préstamos.
 */
 
public class SimPrestamoCatalogoOperacionConceptoDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionConsultaRegistro, OperacionConsultaTabla {
	

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		sSql =  "SELECT \n"+
				"OP.CVE_GPO_EMPRESA, \n"+
				"OP.CVE_EMPRESA, \n"+
				"OP.CVE_OPERACION, \n"+
				"OP.CVE_CONCEPTO, \n"+
				"C.DESC_LARGA, \n"+
				"DECODE(OP.CVE_AFECTA,'I','Incrementa','D','Decrementa','N','No Afecta') CVE_AFECTA \n"+
			"FROM PFIN_CAT_OPERACION_CONCEPTO OP, \n"+
			"     PFIN_CAT_CONCEPTO C \n"+
			"WHERE OP.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND OP.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND OP.CVE_OPERACION = '" + (String)parametros.getDefCampo("CVE_OPERACION") + "' \n"+
			"AND C.CVE_GPO_EMPRESA = OP.CVE_GPO_EMPRESA \n"+
			"AND C.CVE_EMPRESA = OP.CVE_EMPRESA \n"+
			"AND C.CVE_CONCEPTO = OP.CVE_CONCEPTO \n"+
			"ORDER BY OP.CVE_CONCEPTO \n";
		
		ejecutaSql();
		return getConsultaLista();
	}

	/**
	 * Obtiene un registro tomando como base la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql =  "SELECT \n"+
				"OP.CVE_GPO_EMPRESA, \n"+
				"OP.CVE_EMPRESA, \n"+
				"OP.CVE_OPERACION, \n"+
				"OP.CVE_CONCEPTO, \n"+
				"C.DESC_LARGA DESC_LARGA_CONCEPTO, \n"+
				"O.DESC_LARGA DESC_LARGA_OPERACION, \n"+
				"OP.CVE_AFECTA \n"+
			"FROM PFIN_CAT_OPERACION_CONCEPTO OP, \n"+
			"     PFIN_CAT_CONCEPTO C, \n"+
			"     PFIN_CAT_OPERACION O \n"+
			"WHERE OP.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND OP.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND OP.CVE_OPERACION = '" + (String)parametros.getDefCampo("CVE_OPERACION") + "' \n"+
			"AND OP.CVE_CONCEPTO = '" + (String)parametros.getDefCampo("CVE_CONCEPTO") + "' \n"+
			"AND C.CVE_GPO_EMPRESA = OP.CVE_GPO_EMPRESA \n"+
			"AND C.CVE_EMPRESA = OP.CVE_EMPRESA \n"+
			"AND C.CVE_CONCEPTO = OP.CVE_CONCEPTO \n"+
			"AND O.CVE_GPO_EMPRESA = OP.CVE_GPO_EMPRESA \n"+
			"AND O.CVE_EMPRESA = OP.CVE_EMPRESA \n"+
			"AND O.CVE_OPERACION = OP.CVE_OPERACION \n";
				
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		sSql =  "INSERT INTO PFIN_CAT_OPERACION_CONCEPTO ( "+
			"CVE_GPO_EMPRESA, \n" +
			"CVE_EMPRESA, \n" +
			"CVE_OPERACION, \n"+
			"CVE_CONCEPTO) \n"+
			" VALUES (" +
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_OPERACION") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_CONCEPTO") + "') \n" ;
			
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
		sSql =  " DELETE FROM PFIN_CAT_OPERACION_CONCEPTO " +
			" WHERE CVE_OPERACION	='" + (String)registro.getDefCampo("CVE_OPERACION") + "' \n" +
			" AND CVE_CONCEPTO   	='" + (String)registro.getDefCampo("CVE_CONCEPTO") + "' \n"+
			" AND CVE_EMPRESA	='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
	
		return resultadoCatalogo;
	}

}