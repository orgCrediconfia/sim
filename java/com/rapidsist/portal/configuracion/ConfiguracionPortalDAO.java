/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.configuracion;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.*;

public class ConfiguracionPortalDAO extends Conexion2{

	static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("MensajesInfraestructura");

	/**
	 * Obtiene los permisos.
	 * @param sCveAplicacion Clave de la aplicación.
	 * @param sCvePerfil Clave del perfil.
	 * @param sCveFuncion  Clave de la función.
	 * @return La consulta de los registros.
	 * @throws SQLException se generó un error al obtener los permisos.
	 */
	public Registro getPermisos(String sCveAplicacion, String sCvePerfil, String sCveFuncion) throws SQLException{
		sSql = "SELECT B_ALTA, B_BAJA, B_MODIF, B_CONSULTA, AC.B_BITACORA \n" +
				 " FROM RS_CONF_APLICACION_CONFIG AC, RS_CONF_FUNCION FU, \n " +
				 "	 RS_CONF_APLICACION AP, RS_CONF_PERFIL PR \n " +
				 " WHERE AC.CVE_APLICACION = '" + sCveAplicacion + "' \n" +
				   " AND AC.CVE_PERFIL = '" + sCvePerfil + "' \n" +
				   " AND AC.CVE_FUNCION = '" + sCveFuncion + "' \n" +
				   " AND AC.CVE_FUNCION = FU.CVE_FUNCION \n" +
				   " AND FU.B_BLOQUEADO ='F' \n" +
				   " AND AC.CVE_APLICACION = AP.CVE_APLICACION \n" +
				   " AND AP.B_BLOQUEADO = 'F' \n" +
				   " AND AC.CVE_PERFIL = PR.CVE_PERFIL \n" +
				   " AND AC.CVE_APLICACION = PR.CVE_APLICACION \n" +
				   " AND PR.B_BLOQUEADO='F' \n";
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Obtiene la función.
	 * @param Obtiene la función.
	 * @param sCveFuncion
	 * @return La consulta del registro.
	 * @throws SQLException Se generó un error al obtener la función.
	 */
	public Registro getFuncion(String sCveFuncion) throws SQLException{
		sSql = "SELECT * FROM RS_CONF_FUNCION where CVE_FUNCION = '" + sCveFuncion + "'";
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Obtiene las operaciones de la función.
	 * @param sCveFuncion Clave de la función.
	 * @return La lista de operaciones.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public HashMap getFuncionOperaciones(String sCveFuncion) throws SQLException{
		HashMap listaOperaciones = null;
		sSql = "SELECT * FROM RS_CONF_FUNCION_OPERACION where CVE_FUNCION = '" + sCveFuncion + "'";
		ejecutaSql();
		while (rs.next()){
			if (listaOperaciones == null){
				listaOperaciones = new HashMap();
			}
			Registro registro = new Registro();
			registro.addDefCampo("CVE_FUNCION", rs.getString("CVE_FUNCION"));
			registro.addDefCampo("CVE_OPERACION", rs.getString("CVE_OPERACION"));
			registro.addDefCampo("TX_OPERACION", rs.getString("TX_OPERACION"));
			registro.addDefCampo("B_DEPURAR_CODIGO", rs.getString("B_DEPURAR_CODIGO"));
			listaOperaciones.put(rs.getString("CVE_OPERACION"), registro);
		}
		return listaOperaciones;
	}

	/**
	 * Obtiene la página.
	 * @param sCvePagina Clave de la página.
	 * @return La pagina.
	 * @throws SQLException Se generó un error al obtener la pagina.
	 */
	public Registro getPagina(String sCvePagina) throws SQLException{
		sSql = "SELECT * FROM RS_CONF_PAGINAWEB where CVE_PAGINA = '" + sCvePagina + "'";
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Inserta el registro con el evento del usuario
	 * @param sCveGpoEmpresa Clave del grupo de empresa
	 * @param sCveFuncion Clave de la función
	 * @param sCveUsuario Clave del usuario
	 * @return Resultado de la alta.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(String sCveGpoEmpresa, String sCveUsuario, String sCvePagina) throws SQLException{
		ResultadoCatalogo resultadoCatalogo= new ResultadoCatalogo();

		sSql = "INSERT INTO RS_BITACORA_PAGINA( \n"+
					   "CVE_GPO_EMPRESA, \n"+
					   "ID_BITACORA_PAGINA, \n"+
					   "CVE_USUARIO, \n"+
					   "CVE_PAGINA, \n"+
					   "F_ACCESO) \n"+
				"VALUES ( \n"+
					"'" + sCveGpoEmpresa + "',\n" +
					"SQ01_RS_BITACORA_PAGINA.nextval, \n" +
					"'" + sCveUsuario + "',\n" +
					"'" + sCvePagina + "',\n" +
					"SYSDATE)\n";

		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}

	/**
	 * Obtiene el mensaje.
	 * @param sCveMensaje Clave del mensaje.
	 * @throws SQLException Se generó un error al obtener el mensaje.
	 */
	public Registro getMensaje(String sCveMensaje) throws SQLException{
		sSql = "SELECT * FROM RS_CONF_MENSAJE where CVE_MENSAJE = '" + sCveMensaje + "'";
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * @param sCveAplicacion Clave de la aplicación.
	 * @throws SQLException
	 */
	public Registro getMenuEtiqueta (String sCveAplicacion) throws SQLException{
		sSql = "SELECT * FROM RS_CONF_APLICACION WHERE CVE_APLICACION = '" + sCveAplicacion + "'";
		ejecutaSql();
		return this.getConsultaRegistro();
	}


	/**
	 * Se obtienen las opciones que forman parte de un menú.
	 * @param sIdGrupo Clave grupo.
	 * @param sIdAplicacion Clave aplicación.
	 * @param sIdOpcionPadre Clave opción padre.
	 * @return lista de opciones que forman parte de un menú.
	 */
	public TreeMap getOpcionMenu(String sCvePerfil, String sCveAplicacion, String sCveOpcionPadre){
		boolean bInicializa=true;
		TreeMap mapaOpciones = null;
		try{
			//SE OBTIENEN LAS OPCIONES DE UN MENU
			sSql ="SELECT ME.CVE_OPCION, ME.CVE_FUNCION, ME.NOM_OPCION, FU.URL_FUNCION, \n" +
					   " NUM_POSICION \n" +
					" FROM  RS_CONF_MENU ME, RS_CONF_FUNCION FU, RS_CONF_APLICACION_CONFIG AC \n" +
					" WHERE ME.CVE_APLICACION = '" + sCveAplicacion + "' \n" +
						" AND ME.CVE_OPCION_PADRE " + sCveOpcionPadre + " \n" +
						" AND ME.CVE_FUNCION = FU.CVE_FUNCION \n" +
						" AND ME.CVE_FUNCION = AC.CVE_FUNCION \n" +
						" AND ME.CVE_APLICACION = AC.CVE_APLICACION \n" +
						" AND AC.CVE_PERFIL = '" + sCvePerfil + "' \n" +
						" AND FU.B_BLOQUEADO = 'F' \n" +
					" ORDER BY ME.NUM_POSICION \n";
			ejecutaSql();
			while (rs.next()){
				//VERIFICA SI ES NECESARIO INICIALIZAR LA LISTA
				if (bInicializa){
					bInicializa = false;
					mapaOpciones = new TreeMap();
				}
				Registro registroOpcion = new Registro();
				registroOpcion.addDefCampo("URL_FUNCION", rs.getString("URL_FUNCION"));
				registroOpcion.addDefCampo("CVE_OPCION", rs.getString("CVE_OPCION"));
				registroOpcion.addDefCampo("CVE_FUNCION", rs.getString("CVE_FUNCION"));
				registroOpcion.addDefCampo("NOM_OPCION", rs.getString("NOM_OPCION"));
				registroOpcion.addDefCampo("NUM_POSICION", rs.getString("NUM_POSICION"));
				mapaOpciones.put(rs.getString("NUM_POSICION"), registroOpcion);
			}

			//SE OBTIENEN LAS OPCIONES DE UN MENU
			sSql ="SELECT ME.CVE_OPCION, ME.NOM_OPCION, NUM_POSICION \n" +
					" FROM  RS_CONF_MENU ME \n" +
					" WHERE ME.CVE_APLICACION = '" + sCveAplicacion + "' \n" +
						" AND ME.CVE_OPCION_PADRE " + sCveOpcionPadre + " \n" +
						" AND ME.CVE_FUNCION IS NULL \n" +
					" ORDER BY ME.NUM_POSICION \n";
			ejecutaSql();
			while (rs.next()){

				//VERIFICA SI ES NECESARIO INICIALIZAR LA LISTA
				if (bInicializa){
					bInicializa = false;
					mapaOpciones = new TreeMap();
				}

				Registro registroOpcion = new Registro();
				registroOpcion.addDefCampo("CVE_OPCION", rs.getString("CVE_OPCION"));
				registroOpcion.addDefCampo("NOM_OPCION", rs.getString("NOM_OPCION"));
				registroOpcion.addDefCampo("NUM_POSICION", rs.getString("NUM_POSICION"));
				mapaOpciones.put(rs.getString("NUM_POSICION"), registroOpcion);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapaOpciones;
	}

	/**
	 * Obtiene el usuario.
	 * @param sCveGpoEmpresa Clave del grupo de empresa
	 * @param sCveUsuario Clave del usuario
	 * @throws SQLException Se generó un error al obtener el usuario.
	 */
	public Registro getUsuario(String sCveGpoEmpresa, String sCveUsuario) throws SQLException{
		sSql = "SELECT US.CVE_USUARIO, US.CVE_GPO_EMPRESA, US.CVE_EMPRESA, \n" +
					 " PER.ID_PERSONA, PER.NOM_COMPLETO, US.NOM_ALIAS, US.LOCALIDAD \n" +
				 " FROM RS_GRAL_USUARIO US, RS_GRAL_PERSONA PER \n" +
				 " WHERE US.CVE_USUARIO_AUTENTIFICACION = '" + sCveUsuario + "' \n" +
				   " AND US.CVE_GPO_EMPRESA = '" + sCveGpoEmpresa + "'" +
				   " AND US.ID_PERSONA = PER.ID_PERSONA \n";
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Obtiene el Portal Default.
	 * @param sCvePortalDefault Nombre del Portal Default. 
	 * @throws SQLException Se generó un error al obtener el Portal.
	 */
	public PortalDefault getPortalDefault(String sCvePortalDefault) throws SQLException{
		PortalDefault portalDefault= null;
		sSql =  "SELECT DISTINCT \n" +
				" PD.CVE_USUARIO, \n" + 
				" PD.URL_DIR_DEFAULT, \n" +
				" PD.cve_aplicacion, \n" +
		        	" PD.cve_gpo_empresa, \n" + 
		        	" us.cve_empresa, \n" +
				" url_encabezado, \n" +
				" url_pie_pagina, \n" +
				" url_menu_aplicacion, \n" +
				" nom_ventana, url_estilo, \n" +
				" url_contenido_inicio, \n" +
				" url_contenido_fin, \n" +
				" PD.CVE_ESTILO, \n" +
				" PD.CVE_PORTAL, \n" +
				" PER.ID_PERSONA, \n" +
				" PER.NOM_COMPLETO, \n" +
				" US.NOM_ALIAS, \n" +
				" CVE_PORTAL_DEFAULT, \n" + 
				" USU_PER.CVE_PERFIL, \n" +
				" GPO_EMP.TIPO_LETRA TIPO_LETRA_EMPRESA, \n" +
				" PO.TIPO_LETRA TIPO_LETRA_PORTAL, \n" +
				" PO.TIPO_MENU \n" +
			" FROM rs_conf_portal_default PD, \n" +
				" rs_conf_portal PO, \n" +
				" rs_conf_portal_estilo PE, \n" +
				" RS_GRAL_USUARIO US, \n" +
				" RS_GRAL_PERSONA PER,  \n" +
				" RS_GRAL_USUARIO_PERFIL USU_PER, \n" +
				" RS_CONF_GPO_EMPRESA GPO_EMP \n" +
			"WHERE PD.CVE_PORTAL_DEFAULT = '" + sCvePortalDefault + "' \n" +
			"AND PD.CVE_GPO_EMPRESA = PO.CVE_GPO_EMPRESA \n" +
			" AND PD.CVE_PORTAL = PO.CVE_PORTAL \n" +
			" AND PD.CVE_GPO_EMPRESA = PE.CVE_GPO_EMPRESA \n " +
			" AND PD.CVE_PORTAL = PE.CVE_PORTAL \n " +
			" AND PD.CVE_ESTILO = PE.CVE_ESTILO \n " +
			" AND PD.CVE_GPO_EMPRESA = US.CVE_GPO_EMPRESA \n" +
			" AND PD.CVE_USUARIO = US.CVE_USUARIO \n" +
			" AND US.ID_PERSONA = PER.ID_PERSONA \n " +
			" AND US.CVE_GPO_EMPRESA = PER.CVE_GPO_EMPRESA \n " +
			" AND US.CVE_GPO_EMPRESA = USU_PER.CVE_GPO_EMPRESA \n " +
			" AND US.CVE_USUARIO = USU_PER.CVE_USUARIO \n"+
			" AND PD.CVE_GPO_EMPRESA = GPO_EMP.CVE_GPO_EMPRESA ";
		
		ejecutaSql();
		logger.debug(sSql);
		if (rs.next()){
			portalDefault = new PortalDefault();
			portalDefault.sCvePortalDefault = rs.getString("CVE_PORTAL_DEFAULT");
			portalDefault.sCveEstilo = rs.getString("CVE_ESTILO");
			portalDefault.sCveGpoEmpresa = rs.getString("CVE_GPO_EMPRESA");
			portalDefault.sCvePortal = rs.getString("CVE_PORTAL");
			portalDefault.sCveUsuario = rs.getString("CVE_USUARIO");
			portalDefault.sCveEmpresa = rs.getString("CVE_EMPRESA");
			portalDefault.sNomVentana = rs.getString("nom_ventana");
			portalDefault.sUrlEncabezado = rs.getString("url_encabezado");
			portalDefault.sUrlPiePagina = rs.getString("url_pie_pagina");
			portalDefault.sUrlMenuAplicacion = rs.getString("url_menu_aplicacion");
			portalDefault.sUrlContenidoInicio = rs.getString("url_contenido_inicio");
			portalDefault.sUrlContenidoFin = rs.getString("url_contenido_fin");
			portalDefault.sUrlEstilo = rs.getString("url_estilo");
			portalDefault.sCveAplicacion = rs.getString("cve_aplicacion");
			portalDefault.sIdPersona = rs.getString("ID_PERSONA");
			portalDefault.sNomCompleto = rs.getString("NOM_COMPLETO");
			portalDefault.sNomAlias = rs.getString("NOM_ALIAS");
			portalDefault.sUrlDirectorioDefault = rs.getString("URL_DIR_DEFAULT");
			portalDefault.sCvePerfil = rs.getString("CVE_PERFIL");
			portalDefault.sTipoLetraEmpresa = rs.getString("TIPO_LETRA_EMPRESA");
			portalDefault.sTipoLetraPortal = rs.getString("TIPO_LETRA_PORTAL");	
			portalDefault.sTipoMenu = rs.getString("TIPO_MENU");	
		}
		System.out.println("**inicia metodo getPortalDefault. ConfiguracionPortal"+sSql);
		return portalDefault;
	}

	/**
	 * Obtiene las aplicaciones.
	 * @param sCveGpoEmpresa Clave del grupo de empresa
	 * @param sCveUsuario Clave del usuario.
	 * @throws SQLException Se generó un error al obtener las aplicaciones.
	 */
	public LinkedList getAplicacionesUsuario(String sCveGpoEmpresa, String sCveUsuario) throws SQLException{
		sSql = "SELECT APL.cve_aplicacion, APL.NOM_APLICACION, PEF.CVE_PERFIL, PEF.NOM_PERFIL, \n" +
					 " APL.URL_APLICACION, USU.B_BLOQUEADO, APL.NOM_ETIQUETA_MENU \n" +
				 " FROM RS_GRAL_USUARIO_perfil USU, RS_CONF_APLICACION APL, RS_CONF_PERFIL PEF \n" +
				 " WHERE USU.CVE_USUARIO = '" + sCveUsuario + "' \n" +
				   " AND USU.CVE_GPO_EMPRESA = '" + sCveGpoEmpresa + "' \n" +
				   " AND USU.B_BLOQUEADO ='F' \n" +
				   " AND USU.CVE_APLICACION = APL.CVE_APLICACION \n" +
				   " AND USU.CVE_APLICACION = PEF.CVE_APLICACION \n " +
				   " AND USU.CVE_PERFIL = PEF.CVE_PERFIL \n " +
				 " order by NOM_APLICACION, DESC_PERFIL \n" ;
				 
				 System.out.println("Lista de aplicaciones"+sSql);
				 
		ejecutaSql();
		logger.debug(sSql);
		return this.getConsultaLista();
	}
	
	
	/**
	 * Obtiene los colores del menú.
	 * @param sCveGpoEmpresa Clave del grupo de empresa
	 * @param sCvePortal Clave del portal.
	 * @throws SQLException Se generó un error al obtener las aplicaciones.
	 */
	public LinkedList getMenuColor(String sCveGpoEmpresa, String sCvePortal) throws SQLException{
		sSql = "SELECT * FROM RS_CONF_MENU_COLOR \n" +
				 " WHERE CVE_PORTAL = '" + sCvePortal + "' \n" +
				 " AND CVE_GPO_EMPRESA = '" + sCveGpoEmpresa + "' \n" ;
		ejecutaSql();
		logger.debug(sSql);
		return this.getConsultaLista();
	} 
	

	/**
	 * Obtiene el tipo de letra de la aplicación.
	 * @param sCveGpoEmpresa Clave del grupo de empresa
	 * @param sCveFuncion Clave de la función
	 * @param sCveUsuario Clave del usuario
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getAplicacionesUsuarioSelector(String sCveGpoEmpresa, String sCveUsuario, String sCvePortal) throws SQLException{
		
		sSql = "SELECT APL.cve_aplicacion, APL.NOM_APLICACION, PEF.CVE_PERFIL, PEF.NOM_PERFIL, \n" +
					 " APL.URL_APLICACION, USU.B_BLOQUEADO, APL.NOM_ETIQUETA_MENU,  \n" +
					 " POA.TIPO_LETRA TIPO_LETRA_APLICACION \n" +
				 " FROM RS_GRAL_USUARIO_perfil USU, RS_CONF_APLICACION APL, RS_CONF_PERFIL PEF, \n" +
					  " RS_CONF_PORTAL_APL POA \n" +
				 " WHERE USU.CVE_USUARIO = '" + sCveUsuario + "' \n" +
				   " AND USU.CVE_GPO_EMPRESA = '" + sCveGpoEmpresa + "' \n" +
				   " AND USU.B_BLOQUEADO ='F' \n" +
				   " AND USU.CVE_APLICACION = APL.CVE_APLICACION \n" +
				   " AND USU.CVE_APLICACION = PEF.CVE_APLICACION \n " +
				   " AND USU.CVE_PERFIL = PEF.CVE_PERFIL \n " +
				   " AND USU.CVE_GPO_EMPRESA = POA.CVE_GPO_EMPRESA \n" +
				   " AND POA.CVE_APLICACION = USU.CVE_APLICACION \n" +
				   " AND POA.CVE_PORTAL = '" + sCvePortal + "' \n" +
				 " order by NOM_APLICACION, DESC_PERFIL \n" ;
	
		ejecutaSql();
		return this.getConsultaLista();
	}
}