/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de estatus del préstamo.
 */
 
public class SimListaEstatusPrestamoDAO extends Conexion2 implements OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  " SELECT DISTINCT \n"+
			" 	P.CVE_GPO_EMPRESA, \n"+
			" 	P.CVE_EMPRESA, \n"+
			" 	P.ID_ETAPA_PRESTAMO, \n"+
			" 	C.NOM_ESTATUS_PRESTAMO, \n"+
			" 	P.ORDEN_ETAPA \n"+
			" FROM SIM_PRESTAMO_ETAPA P, \n"+
			" SIM_CAT_ETAPA_PRESTAMO C \n"+
			" WHERE P.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND P.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND P.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND C.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
			" AND C.CVE_EMPRESA = P.CVE_EMPRESA \n"+
			" AND C.ID_ETAPA_PRESTAMO = P.ID_ETAPA_PRESTAMO \n"+
			" ORDER BY P.ORDEN_ETAPA \n";
		
		ejecutaSql();
		return getConsultaLista();
	}
}