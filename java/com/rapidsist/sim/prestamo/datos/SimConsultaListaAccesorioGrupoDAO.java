/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para la consulta de los accesorios de un préstamo grupal.
 */
 
public class SimConsultaListaAccesorioGrupoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS DE BANCOS
		
		sSql =  " SELECT DISTINCT \n"+
                    	"A.ID_CARGO_COMISION ID_ACCESORIO, \n"+
                    	"C.NOM_ACCESORIO  \n"+
			"FROM    SIM_PRESTAMO_GPO_CARGO A, \n"+
			"        SIM_CAT_ACCESORIO C \n"+
			"WHERE   A.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			"AND     A.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
			"AND     A.ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
			"AND     C.CVE_GPO_EMPRESA = A.CVE_GPO_EMPRESA \n"+
			"AND     C.CVE_EMPRESA = A.CVE_EMPRESA \n"+
			"AND     C.ID_ACCESORIO = A.ID_CARGO_COMISION \n"+
			"AND     A.ID_FORMA_APLICACION != '1' \n"+
			"ORDER BY A.ID_CARGO_COMISION \n";
		
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
		sSql =  "SELECT COUNT (*) ACCESORIOS \n"+
 				"FROM ( \n"+
				 			" SELECT DISTINCT \n"+
				        	"A.ID_CARGO_COMISION ID_ACCESORIO, \n"+
				        	"C.NOM_ACCESORIO  \n"+
				"FROM    SIM_PRESTAMO_GPO_CARGO A, \n"+
				"        SIM_CAT_ACCESORIO C \n"+
				"WHERE   A.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				"AND     A.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
				"AND     A.ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
				"AND     C.CVE_GPO_EMPRESA = A.CVE_GPO_EMPRESA \n"+
				"AND     C.CVE_EMPRESA = A.CVE_EMPRESA \n"+
				"AND     C.ID_ACCESORIO = A.ID_CARGO_COMISION \n"+
				"AND     A.ID_FORMA_APLICACION != '1' \n"+
				"ORDER BY A.ID_CARGO_COMISION ) \n";
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}