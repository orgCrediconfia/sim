/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.herramientasconfiguracion.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Consulta la base de datos para el catálogo de funciones.
 */
public class FuncionDAO extends Conexion2 implements OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros tomando como base el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
		sSql = "SELECT \n" +
					"F.CVE_FUNCION, \n" +
					"F.CVE_TABLA, \n" +
					"F.NOM_FUNCION, \n" +
					"F.TX_DESC_FUNCION, \n" +
					"F.URL_FUNCION, \n" +
					"F.NOM_CLASE_CON, \n" +
					"F.NOM_CLASE_DAO, \n" +
					"F.NOM_CLASE_REPORTE, \n" +
					"F.B_WEBSERVICE, \n" +
					"F.B_BLOQUEADO, \n" +
					"F.B_BITACORA, \n" +
					"F.B_PAGINACION, \n" +
					"T.NOM_TABLA \n" +
				"FROM RS_CONF_FUNCION F, \n" +
				"     RS_CONF_TABLA T \n" +
				"WHERE F.CVE_TABLA = T.CVE_TABLA(+) \n";
				
				if (parametros.getDefCampo("CVE_FUNCION") != null && !parametros.getDefCampo("CVE_FUNCION").equals("") && !parametros.getDefCampo("CVE_FUNCION").equals("null") ){
						  sSql = sSql + " AND UPPER(CVE_FUNCION) LIKE  '%" + ((String)parametros.getDefCampo("CVE_FUNCION")).toUpperCase() +"%'  \n";
				}
				
				if (parametros.getDefCampo("NOM_FUNCION") != null && !parametros.getDefCampo("NOM_FUNCION").equals("") && !parametros.getDefCampo("NOM_FUNCION").equals("null") ){
						sSql = sSql + "AND UPPER(NOM_FUNCION) LIKE '%" + ((String)parametros.getDefCampo("NOM_FUNCION")).toUpperCase() +"%' \n" ;
				}
				 
				if (parametros.getDefCampo("B_BLOQUEADO") != null && !parametros.getDefCampo("B_BLOQUEADO").equals("") && !parametros.getDefCampo("B_BLOQUEADO").equals("null") ){
						sSql = sSql + "AND B_BLOQUEADO = '" + parametros.getDefCampo("B_BLOQUEADO")+"' \n" ;
				}
				
				sSql = sSql + " ORDER BY CVE_FUNCION \n";
				System.out.println("fuera"+sSql);
		ejecutaSql();
		return this.getConsultaLista();
	}
}