/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.publicaciones.datos;

import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.util.Iterator;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de autorización de publicaciones.
 */
public class PublUsuarioDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base a un filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		boolean bNomPublicacion = false;
		String sFiltro = (String) parametros.getDefCampo("Filtro");
		sSql =  "SELECT   \n"+
						" UP.CVE_GPO_EMPRESA,  \n"+ 
						" UP.CVE_USUARIO, \n"+
						" UP.CVE_PORTAL,  \n"+
						" UP.ID_NIVEL_ACCESO,  \n"+
						" NA.NOM_TIPO_ACCESO, \n"+
						" NA.DESC_TIPO_ACCESO, \n"+
						" UP.CVE_PERFIL_PUB,  \n"+
						" PE.NOM_PERFIL, \n"+
						" GU.CVE_USUARIO,  \n"+
						" GU.ID_PERSONA,  \n"+
						" GP.ID_PERSONA,  \n"+
						" GP.NOM_COMPLETO  \n"+
						" FROM  \n"+
						" RS_CONF_USUARIO_PORTAL UP, RS_GRAL_USUARIO GU, RS_GRAL_PERSONA GP, RS_PUB_NIVEL_ACCESO NA, RS_PUB_PERFIL PE  \n"+
						" WHERE   \n"+
						" UP.CVE_GPO_EMPRESA = GU.CVE_GPO_EMPRESA  \n"+
						" AND UP.CVE_GPO_EMPRESA = GP.CVE_GPO_EMPRESA  \n"+
						" AND UP.CVE_GPO_EMPRESA = NA.CVE_GPO_EMPRESA  \n"+
						" AND UP.CVE_GPO_EMPRESA = PE.CVE_GPO_EMPRESA (+)  \n"+
						" AND UP.CVE_PORTAL = PE.CVE_PORTAL (+) \n"+
						" AND UP.CVE_PORTAL = NA.CVE_PORTAL \n"+
						" AND UP.CVE_USUARIO = GU.CVE_USUARIO  \n"+
						" AND GU.ID_PERSONA = GP.ID_PERSONA  \n"+
						" AND UP.ID_NIVEL_ACCESO = NA.ID_NIVEL_ACCESO \n"+
						" AND UP.CVE_PERFIL_PUB = PE.CVE_PERFIL_PUB (+) \n"+
						" AND  UP.CVE_PORTAL ='"+parametros.getDefCampo("CVE_PORTAL")+"'";
						
		if (parametros.getDefCampo("NOM_COMPLETO") != null) {
			sSql = sSql + "AND GP.NOM_COMPLETO LIKE'%" + (String) parametros.getDefCampo("NOM_COMPLETO") +"%' \n";
			bNomPublicacion = true;
		}
						
		if (parametros.getDefCampo("CVE_PERFIL_PUB") != null) {      
			sSql = sSql + "AND UP.CVE_PERFIL_PUB ='" + (String) parametros.getDefCampo("CVE_PERFIL_PUB") + "' \n";
			bNomPublicacion = true;
		}                    
		if (parametros.getDefCampo("ID_NIVEL_ACCESO") != null) {
			sSql = sSql + " AND UP.ID_NIVEL_ACCESO ='" + (String) parametros.getDefCampo("ID_NIVEL_ACCESO") + "' \n";
			bNomPublicacion = true;
		}
		sSql = sSql  + " ORDER BY GP.NOM_COMPLETO ";
		ejecutaSql();
		return this.getConsultaLista();
	}

	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql =  	" SELECT \n"+
						" UP.CVE_GPO_EMPRESA, \n"+
						" UP.CVE_USUARIO, \n"+
						" UP.CVE_PORTAL, \n"+
						" UP.ID_NIVEL_ACCESO, \n"+
						" NA.NOM_TIPO_ACCESO, \n"+
						" NA.DESC_TIPO_ACCESO, \n"+
						" UP.CVE_PERFIL_PUB, \n"+
						" PE.NOM_PERFIL, \n"+
						" GU.CVE_USUARIO, \n"+
						" GU.ID_PERSONA, \n"+
						" GP.ID_PERSONA, \n"+
						" GP.NOM_COMPLETO \n"+
						" FROM \n"+
						" RS_CONF_USUARIO_PORTAL UP, RS_GRAL_USUARIO GU, RS_GRAL_PERSONA GP, RS_PUB_NIVEL_ACCESO NA, RS_PUB_PERFIL PE \n"+
						" WHERE \n"+
						" UP.CVE_USUARIO ='" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
						" AND UP.CVE_PORTAL='" + (String)parametros.getDefCampo("CVE_PORTAL") + "' \n"+
						" AND UP.CVE_GPO_EMPRESA = GU.CVE_GPO_EMPRESA \n"+
						" AND UP.CVE_GPO_EMPRESA = GP.CVE_GPO_EMPRESA \n"+
						" AND UP.CVE_GPO_EMPRESA = NA.CVE_GPO_EMPRESA \n"+
						" AND UP.CVE_GPO_EMPRESA = PE.CVE_GPO_EMPRESA (+) \n"+
						" AND UP.CVE_PORTAL = PE.CVE_PORTAL (+) \n"+
						" AND UP.CVE_PORTAL = NA.CVE_PORTAL \n"+
						" AND UP.CVE_USUARIO = GU.CVE_USUARIO \n"+
						" AND GU.ID_PERSONA = GP.ID_PERSONA \n"+
						" AND UP.ID_NIVEL_ACCESO = NA.ID_NIVEL_ACCESO \n"+
						" AND UP.CVE_PERFIL_PUB = PE.CVE_PERFIL_PUB(+)  \n";

		ejecutaSql();
		
		return this.getConsultaRegistro();
	}

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		String sIdPublicacion = "";
		LinkedList listaPublicaciones = (LinkedList)registro.getDefCampo("ListaPublicaciones");
		if (listaPublicaciones != null){
			Iterator lista = listaPublicaciones.iterator();
			while (lista.hasNext()){
				sIdPublicacion = (String)lista.next();
				sSql = "UPDATE RS_PUB_PUBLICACION SET " +
					" B_AUTORIZADO           ='V' \n" +
					" WHERE CVE_GPO_EMPRESA  ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" AND CVE_PORTAL         ='" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n"+
					" AND ID_PUBLICACION    ='" + sIdPublicacion + "' \n";
				//VERIFICA SI NO SE ACTUALIZO EL REGISTRO
				if (ejecutaUpdate() == 0) {
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
		}
		return resultadoCatalogo;
	}

	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		sSql = "UPDATE RS_CONF_USUARIO_PORTAL SET " +
					" ID_NIVEL_ACCESO           			='" + (String)registro.getDefCampo("ID_NIVEL_ACCESO") + "' ,\n" +
					" CVE_PERFIL_PUB						='" + (String)registro.getDefCampo("CVE_PERFIL_PUB") + "' \n" +
					" WHERE CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" AND CVE_PORTAL         				='" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n"+
					" AND CVE_USUARIO   					='"  + (String)registro.getDefCampo("CVE_USUARIO") + "' \n";
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}	
		return resultadoCatalogo;
	}

	/**
	 * Borra un registro.
	 * @param registro Llave primaria.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo baja(Registro registro) throws SQLException{				
			ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
			
			//VERIFICA SI ESTA RELACIONADO CON EL CATÁLOGO DE PARTES
			sSql = " SELECT  CVE_USUARIO FROM RS_PUB_GPO_RESP_SECCION \n"+
		     " WHERE CVE_GPO_EMPRESA='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			 " AND CVE_PORTAL='" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n"+
             " AND CVE_USUARIO='" + (String)registro.getDefCampo("CVE_USUARIO") + "' \n"; 
			 
			 this.ejecutaSql();
			 
			 if(rs.next()){
			// SI ESTA RELACIONADO SE ENVIA UN MENSAJE DE QUE NO SE PUEDE BORRAR
			resultadoCatalogo.mensaje.setClave("Registro relacionado");
			resultadoCatalogo.mensaje.setDescripcion("El registro está relacionado con  los grupos responsables.");
			resultadoCatalogo.mensaje.setTipo("Error");
			 }
			else{
					sSql = "DELETE FROM  RS_CONF_USUARIO_PORTAL  " +
						" WHERE CVE_GPO_EMPRESA  ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						" AND CVE_PORTAL         ='" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n"+
						" AND CVE_USUARIO    ='" + (String)registro.getDefCampo("CVE_USUARIO") + "' \n";
					 //VERIFICA SI NO SE ACTUALIZO EL REGISTRO
					 if (ejecutaUpdate() == 0) {			
						 resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
					 }
			}
		 return resultadoCatalogo;
	}
}
