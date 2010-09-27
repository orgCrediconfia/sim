/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.configuracion;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.SortedMap;

public class ConstruyeMenu2 {
	
	/**
	 * Nombre del Menú.
	 */
	private String sMenu = "";
	
	/**
	 * Nivel del Menú.
	 */
	private int iNivelMenu = 0;
	
	/**
	 * Un objeto de acceso a base de datos sobre la configuación del portal.
	 */
	private ConfiguracionPortalDAO configuracionDAO = new ConfiguracionPortalDAO();
	
	/**
	 * Nombre de la aplicación web.
	 */
	private String sNomAplicacionWeb = "";

	/**
	 * Obtiene el menú.
	 * @param conexion Objeto tipo Conexion2.
	 * @param sCveAplicacion Clave de la aplicación.
	 * @param sCvePerfil Clave del perfil.
	 * @param sNomAplicacionWeb Nombre de la aplicación web.
	 */
	public String getMenuHorizontal(Conexion2 conexion, String sCveAplicacion,  String sCvePerfil, String sNomAplicacionWeb){
		Registro registroMenu = new Registro();
		String sNomEtiquetaMenu = new String();

		try{
			this.sNomAplicacionWeb = sNomAplicacionWeb;
			configuracionDAO.setConexion(conexion.getConexion());

			//OBTENEMOS LAS OPCIONES DEL MENU PARA EL PRIMER NIVEL
			TreeMap listaOpciones = configuracionDAO.getOpcionMenu(sCvePerfil, sCveAplicacion, " is null ");

			//VERIFICA SI ENCONTRO OPCIONES DEL MENU
			if (listaOpciones != null){
				construyeMenuHorizontal(listaOpciones, sCvePerfil, sCveAplicacion, iNivelMenu);
			}
			configuracionDAO.cierraConexion();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return sMenu;
	}

	/**
	 * Obtiene el menú.
	 * @param conexion Objeto tipo Conexion2.
	 * @param sCveAplicacion Clave de la aplicación.
	 * @param sCvePerfil Clave del perfil.
	 * @param sNomAplicacionWeb Nombre de la aplicación web.
	 */
	public String getMenu(Conexion2 conexion, String sCveAplicacion,  String sCvePerfil, String sNomAplicacionWeb){
		Registro registroMenu = new Registro();
		String sNomEtiquetaMenu = new String();


		try{
			configuracionDAO.setConexion(conexion.getConexion());
			registroMenu = (Registro)configuracionDAO.getMenuEtiqueta(sCveAplicacion);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		if ((String)registroMenu.getDefCampo("NOM_ETIQUETA_MENU")== null) {
			sNomEtiquetaMenu = "Menu";
		}
		else{
			sNomEtiquetaMenu = (String) registroMenu.getDefCampo("NOM_ETIQUETA_MENU");
		}

		//INICIALIZA EL MENU
		iNivelMenu = 0;
		sMenu = "\n" +
				"\t\t\t\t\t\t<div class='MenuAplicacionGrupo'>\n" +
				"\t\t\t\t\t\t\t<div class='MenuAplicacionTitulo'>\n" +
				"\t\t\t\t\t\t\t\t<strong>"+sNomEtiquetaMenu+"</strong>\n" +
				"\t\t\t\t\t\t\t</div>\n" +
				"\t\t\t\t\t\t\t<div class='MenuAplicacionItems'>\n" ;
		try{
			this.sNomAplicacionWeb = sNomAplicacionWeb;
			//configuracionDAO.setConexion(conexion.getConexion());

			//OBTENEMOS LAS OPCIONES DEL MENU PARA EL PRIMER NIVEL
			TreeMap listaOpciones = configuracionDAO.getOpcionMenu(sCvePerfil, sCveAplicacion, " is null ");

			//VERIFICA SI ENCONTRO OPCIONES DEL MENU
			if (listaOpciones != null){
				construyeMenu(listaOpciones, sCvePerfil, sCveAplicacion, iNivelMenu);
			}
			//FINALIZA LA CONSTRUCCION DEL MENU
			sMenu = sMenu + "\t\t\t\t\t\t\t</div>\n" +
							"\t\t\t\t\t\t</div><!--MenuAplicacionGrupo -->\n";
			configuracionDAO.cierraConexion();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return sMenu;
	}
	
	/**
	 * Obtiene las opciones de un menu. Esta función es recursiva si solo es llamada por getMenu()
	 * @param listaOpciones Objeto tipo TreeMap.
	 * @param sIdGrupo Id del grupo.
	 * @param sIdAplicacion Id de la aplicación.
	 * @param iNivelActual Nivel actual.
	 */
	private void construyeMenu(TreeMap listaOpciones, String sCvePerfil, String sCveAplicacion, int iNivelActual){
		String sUrl = "";
		//RECORRE LA LISTA DE OPCIONES ENCONTRADAS. PARA CADA OPCION SE BUSCARA SI TIENE A SU VEZ
		//OTRAS SUBOPCIONES ASIGNADAS
		SortedMap listaOrdenada = listaOpciones;
		Iterator lista = listaOrdenada.keySet().iterator();
		while (lista.hasNext()){
			String sLlaveOpcion = (String)lista.next();
			Registro opcion = (Registro)listaOpciones.get(sLlaveOpcion);

			//OBTIENE LAS SUBOPCIONES
			TreeMap listaSubOpciones = configuracionDAO.getOpcionMenu(sCvePerfil, sCveAplicacion, " = '" + opcion.getDefCampo("CVE_OPCION") + "'");
			//VERIFICA SI ENCONTRO SUBOPCIONES
			if ( listaSubOpciones != null ){
				//CREA UNA OPCION DE MENU TIPO SUBMENU
				iNivelMenu ++;

				//SE PINTA EL SUBMENU DE MANERA DISTINTA SI ESTA EN EL NIVEL SUPERIOR (0) O SI
				//FORMA PARTE DE ALGUN MENU LATERAL

				sMenu = sMenu + "\t\t\t\t\t\t\t\t<div>\n";
				sMenu = sMenu + "\t\t\t\t\t\t\t\t\t<a href='#'>" + opcion.getDefCampo("NOM_OPCION") + "</a>\n";

				//SE VUELVE A LLAMAR A ESTA FUNCION (RECURSIVIDAD) PARA CONSTRUIR EL SUBMENU
				construyeMenu(listaSubOpciones, sCvePerfil, sCveAplicacion, iNivelMenu);
				sMenu = sMenu + "\t\t\t\t\t\t\t\t</div>\n";
			}
			else{
				//CREA UNA OPCION SIMPLE TIPO LINK
				sMenu = sMenu + "\t\t\t\t\t\t\t\t<div>\n";
				if (opcion.getDefCampo("URL_FUNCION") != null){
					sUrl = sNomAplicacionWeb + (String)opcion.getDefCampo("URL_FUNCION");
				}
				else{
					sUrl = "#";
				}
				sMenu = sMenu + "\t\t\t\t\t\t\t\t\t<a href='" + sUrl + "'>" + (String)opcion.getDefCampo("NOM_OPCION") + "</a>\n";
				sMenu = sMenu + "\t\t\t\t\t\t\t\t</div>\n";
			}
		}
	}

	/**
	 * Obtiene las opciones de un menu. Esta función es recursiva si solo es llamada por getMenu()
	 * @param listaOpciones Objeto tipo TreeMap.
	 * @param sIdGrupo Id del grupo.
	 * @param sIdAplicacion Id de la aplicación.
	 * @param iNivelActual Nivel actual.
	 */
	private void construyeMenuHorizontal(TreeMap listaOpciones, String sCvePerfil, String sCveAplicacion, int iNivelActual){
		String sUrl = "";
		//RECORRE LA LISTA DE OPCIONES ENCONTRADAS. PARA CADA OPCION SE BUSCARA SI TIENE A SU VEZ
		//OTRAS SUBOPCIONES ASIGNADAS
		SortedMap listaOrdenada = listaOpciones;
		Iterator lista = listaOrdenada.keySet().iterator();
		while (lista.hasNext()){
			String sLlaveOpcion = (String)lista.next();
			Registro opcion = (Registro)listaOpciones.get(sLlaveOpcion);

			//OBTIENE LAS SUBOPCIONES
			TreeMap listaSubOpciones = configuracionDAO.getOpcionMenu(sCvePerfil, sCveAplicacion, " = '" + opcion.getDefCampo("CVE_OPCION") + "'");
			//VERIFICA SI ENCONTRO SUBOPCIONES
			if ( listaSubOpciones != null ){
				//CREA UNA OPCION DE MENU TIPO SUBMENU
				iNivelMenu ++;

				//SE PINTA EL SUBMENU DE MANERA DISTINTA SI ESTA EN EL NIVEL SUPERIOR (0) O SI
				//FORMA PARTE DE ALGUN MENU LATERAL
				sMenu = sMenu + "      addSubMenu('m" + Integer.toString(iNivelActual) + "', '" + opcion.getDefCampo("NOM_OPCION") + "', '', '', 'm" + Integer.toString(iNivelMenu) + "', '');\n";

				//SE VUELVE A LLAMAR A ESTA FUNCION (RECURSIVIDAD) PARA CONSTRUIR EL SUBMENU
				construyeMenuHorizontal(listaSubOpciones, sCvePerfil, sCveAplicacion, iNivelMenu);
			}
			else{
				//CREA UNA OPCION SIMPLE TIPO LINK
				sUrl = sNomAplicacionWeb + (String)opcion.getDefCampo("URL_FUNCION");
				sMenu = sMenu + "      addLink('m" + Integer.toString(iNivelActual) + "', '" + opcion.getDefCampo("NOM_OPCION") +"', '', '" + sUrl + "', '');\n";			
			}
		}
	}
}