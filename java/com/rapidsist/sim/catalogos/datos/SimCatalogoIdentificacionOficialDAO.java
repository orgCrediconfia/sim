/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.catalogos.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de documentación.
 */
 
public class SimCatalogoIdentificacionOficialDAO extends Conexion2 implements OperacionConsultaTabla {

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
				" D.CVE_GPO_EMPRESA, \n"+
				" D.CVE_EMPRESA, \n"+
				" D.ID_DOCUMENTO, \n"+
				" D.NOM_DOCUMENTO, \n"+
				" D.DESCRIPCION, \n"+
				" D.ID_TIPO_DOCUMENTACION \n"+
				
				" FROM SIM_CAT_DOCUMENTO D \n"+
				
				" WHERE D.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND D.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND D.ID_TIPO_DOCUMENTACION ='1' \n";
				

		ejecutaSql();
		return getConsultaLista();
	}
}