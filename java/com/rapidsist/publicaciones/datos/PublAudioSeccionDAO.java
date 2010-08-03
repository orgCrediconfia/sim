/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.publicaciones.datos;

import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.util.Iterator;
import java.sql.SQLException;

/**
 * Consulta a la base de datos para las publicaciones de audio.
 */
public class PublAudioSeccionDAO extends Conexion2 implements OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base a un filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
		sSql =  "SELECT \n"+
					"ID_PUBLICACION, \n"+
					"NOM_PUBLICACION, \n"+
					"TX_COMENTARIO, \n"+
					"DESC_PUBLICACION, \n"+
					"CONTROLADOR \n"+
			    "FROM  \n"+
					" RS_PUB_PUBLICACION \n"+
				"WHERE   \n"+
				"CVE_GPO_EMPRESA = '"+parametros.getDefCampo("CVE_GPO_EMPRESA")+"' \n"+
				"AND CVE_PORTAL = '"+parametros.getDefCampo("CVE_PORTAL")+"' \n"+
				"AND CVE_SECCION ='"+parametros.getDefCampo("CVE_SECCION")+"' \n"+
				"AND SYSDATE BETWEEN F_INI_VIGENCIA AND F_FIN_VIGENCIA  \n"+
				"ORDER BY ID_PRIORIDAD, ID_PUBLICACION DESC \n";
		ejecutaSql();
		return this.getConsultaLista();
	}

}
