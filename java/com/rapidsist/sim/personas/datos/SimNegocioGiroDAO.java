/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.personas.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para los negocios del Cliente.
 */
 
public class SimNegocioGiroDAO extends Conexion2 implements OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		sSql =  "SELECT \n"+
			"        C.CVE_GPO_EMPRESA, \n"+
			"        C.CVE_EMPRESA, \n"+
			"        C.CVE_CLASE, \n"+
			"        C.NOM_CLASE, \n"+
			"        S.CVE_SINONIMO, \n"+
			"        S.NOM_SINONIMO \n"+
			"FROM SIM_CAT_CLASE C, \n"+
			"     SIM_CAT_CLASE_SINONIMO S \n"+
			"WHERE C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND (UPPER(S.NOM_SINONIMO) LIKE '%" + ((String)parametros.getDefCampo("NOM_SINONIMO")).toUpperCase() + "%' OR UPPER(C.NOM_CLASE) LIKE '%" + ((String)parametros.getDefCampo("NOM_SINONIMO")).toUpperCase() + "%') \n"+
			"AND C.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA (+) \n"+
			"AND C.CVE_EMPRESA = S.CVE_EMPRESA (+) \n"+
			"AND C.CVE_CLASE = S.CVE_CLASE (+) \n"+
			"ORDER BY NOM_SINONIMO \n";
			
		ejecutaSql();
		return getConsultaLista();
	}
}