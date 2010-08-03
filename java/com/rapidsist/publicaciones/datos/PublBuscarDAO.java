/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.publicaciones.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import java.util.LinkedList;
import java.sql.SQLException;
import java.util.*;

/**
 * Administra los accesos a la base de datos para el EJB de Publicaciones.
 */
public class PublBuscarDAO	extends Conexion2 implements OperacionConsultaTabla {


	/**
	 * Obtiene un conjunto de registros en base a un filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException {
		String sFiltro = (String)parametros.getDefCampo("Filtro");
		if (sFiltro.equals("Todos")){
			sSql = "SELECT \n"+
 	   			   "     PUB.ID_PUBLICACION, \n"+ 
 	   			   "	 PUB.CVE_SECCION, \n"+
 	   			   "	 PUB.ID_NIVEL_ACCESO, \n"+
 	   			   "	 PUB.NOM_PUBLICACION, \n"+
 	   			   "	 PUB.DESC_PUBLICACION, \n"+
 	   			   "	 PUB.TX_COMENTARIO,  \n"+
 	   			   "	 PUB.F_INI_VIGENCIA, \n"+
 	   			   "	 PUB.F_FIN_VIGENCIA, \n"+
 	   			   "	 PUB.CONTADOR \n"+
 	   			   "FROM RS_PUB_PUBLICACION PUB, \n"+ 
 	   			   "     RS_PUB_PERFIL_SECCION PER, \n"+ 
 	   			   "     RS_CONF_USUARIO_PORTAL USU_POR  \n"+
 	   			   "WHERE PUB.CVE_GPO_EMPRESA =  '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
 	   			   "AND   PUB.CVE_PORTAL =  '" + parametros.getDefCampo("CVE_PORTAL") + "' \n"+
 	   			   "AND   PER.CVE_GPO_EMPRESA = PUB.CVE_GPO_EMPRESA  \n"+
 	   			   "AND   PER.CVE_PORTAL = PUB.CVE_PORTAL  \n"+
 	   			   "AND   PER.CVE_SECCION = PUB.CVE_SECCION  \n"+
 	   			   "AND   PER.CVE_PERFIL_PUB =  (SELECT CVE_PERFIL_PUB \n"+  
 	   			   "                             FROM   RS_CONF_USUARIO_PORTAL \n"+ 
 	   			   "                             WHERE  CVE_GPO_EMPRESA= '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
 	   			   "                             AND    CVE_PORTAL ='" + parametros.getDefCampo("CVE_PORTAL") + "'  \n"+
 	   			   "                             AND    CVE_USUARIO ='" + parametros.getDefCampo("CVE_USUARIO") + "')  \n"+
 	   			   "AND   USU_POR.CVE_GPO_EMPRESA = PUB.CVE_GPO_EMPRESA  \n"+
 	   			   "AND   USU_POR.CVE_PORTAL = PUB.CVE_PORTAL \n"+
 	   			   "AND   USU_POR.CVE_USUARIO = '" + parametros.getDefCampo("CVE_USUARIO") + "' \n"+ 
 	   			   "AND   USU_POR.ID_NIVEL_ACCESO >= PUB.ID_NIVEL_ACCESO \n"+
 	   			   "AND   PUB.CONTROLADOR IS NULL \n"+
 	   			   "AND   (UPPER(PUB.DESC_PUBLICACION) LIKE '%"+ ((String) parametros.getDefCampo("DESC_PUBLICACION")).toUpperCase() +"%' \n"+
 	   			   "OR UPPER(PUB.NOM_PUBLICACION) LIKE '%"+ ((String) parametros.getDefCampo("NOM_PUBLICACION")).toUpperCase() +"%' \n"+
 	   			   "OR UPPER(PUB.TX_COMENTARIO) LIKE '%"+ ((String) parametros.getDefCampo("NOM_PUBLICACION")).toUpperCase() +"%') \n";
		}
		ejecutaSql();
		return this.getConsultaLista();
	
	}
}