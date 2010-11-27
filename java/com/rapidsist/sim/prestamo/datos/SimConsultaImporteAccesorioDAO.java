/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para las actividades o requisitos del préstamo.
 */
 
public class SimConsultaImporteAccesorioDAO extends Conexion2 implements OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS DE BANCOS
		
		sSql =  " SELECT \n"+
			" 	 IMP_ACCESORIO \n"+
			"FROM    SIM_TABLA_AMORT_ACCESORIO \n"+
			"WHERE   CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			"AND     CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND     ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			"AND     ID_ACCESORIO = '6' \n"+
			"ORDER BY ID_ACCESORIO \n";
				
		ejecutaSql();
		return getConsultaLista();
	}
}