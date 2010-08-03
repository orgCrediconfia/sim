/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.herramientasconfiguracion.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de configuración de perfiles.
 */
 
public class PerfilConfiguracionDAO extends Conexion2 implements OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros tomando como base el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//OBTENEMOS EL FILTRO DE BUSQUEDA
		String sFiltro = (String)parametros.getDefCampo("Filtro");
		if (sFiltro.equals("Todos")){
			sSql = "SELECT * \n" +
					" FROM RS_CONF_APLICACION_CONFIG AC \n" +
					" WHERE AC.CVE_APLICACION = '" + (String)parametros.getDefCampo("CVE_APLICACION") + "' \n" +
					  " AND AC.CVE_PERFIL = '" + (String)parametros.getDefCampo("CVE_PERFIL") + "' \n" ;
		}
		else if (sFiltro.equals("FuncionesDisponibles")){
			sSql =" SELECT FU.CVE_FUNCION, FU.NOM_FUNCION \n" +
				   " FROM RS_CONF_FUNCION FU, RS_CONF_APLICACION_FUNCION AF \n" +
				   " WHERE AF.CVE_FUNCION NOT IN( \n" +
						  " SELECT CVE_FUNCION FROM RS_CONF_APLICACION_CONFIG \n" +
						  "  WHERE CVE_APLICACION = '" + (String)parametros.getDefCampo("CVE_APLICACION") + "' \n" +
							"  AND CVE_PERFIL = '" + (String)parametros.getDefCampo("CVE_PERFIL") + "' \n" +
						  " ) \n" +
					"  AND AF.CVE_FUNCION = FU.CVE_FUNCION \n" +
					"  AND AF.CVE_APLICACION = '" + (String)parametros.getDefCampo("CVE_APLICACION") + "' \n" +
					"  ORDER BY FU.CVE_FUNCION \n";
		}
		ejecutaSql();
		return this.getConsultaLista();
	}


	/**
	 * Obtiene un registro en base a la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		return null;
	}

}