/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.publicaciones.datos;


import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de personalidad fiscal.
 */
 
public class PublPublicacionUsuarioDAO extends Conexion2 implements OperacionConsultaRegistro  {


	/**
	 * Obtiene un registro tomando como base la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql = "SELECT PP.NOM_PUBLICACION, \n"+
			   "       PP.CVE_SECCION \n"+
			   "FROM RS_PUB_PUBLICACION PP, \n"+
			   "RS_CONF_USUARIO_PORTAL UP, \n"+
			   "RS_PUB_PERFIL_SECCION PS \n"+
			   "WHERE PP.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   "AND   PP.CVE_PORTAL = '" + (String)parametros.getDefCampo("CVE_PORTAL") + "' \n"+
			   "AND   PP.ID_PUBLICACION = '" + (String)parametros.getDefCampo("ID_PUBLICACION") + "' \n"+
			   "AND   UP.CVE_GPO_EMPRESA = PP.CVE_GPO_EMPRESA \n"+ 
			   "AND   UP.CVE_PORTAL = PP.CVE_PORTAL  \n"+
			   "AND   UP.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
			   "AND   PS.CVE_GPO_EMPRESA = PP.CVE_GPO_EMPRESA \n"+
			   "AND   PS.CVE_PORTAL = PP.CVE_PORTAL \n"+
			   "AND   PS.CVE_SECCION = PP.CVE_SECCION \n"+
			   "AND   PS.CVE_PERFIL_PUB = UP.CVE_PERFIL_PUB \n";
		ejecutaSql();
		return this.getConsultaRegistro();
		
	}
}