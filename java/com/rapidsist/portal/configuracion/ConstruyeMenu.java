/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.configuracion;

import com.rapidsist.comun.bd.Conexion2;
import java.util.Iterator;
import java.util.TreeMap;

public class ConstruyeMenu {
	
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

	public ConstruyeMenu() {
	}

	/**
	 * Obtiene el menú.
	 * @param conexion Objeto tipo Conexion2.
	 * @param sCveAplicacion Clave de la aplicación.
	 * @param sCvePerfil Clave del perfil.
	 * @param sNomAplicacionWeb Nombre de la aplicación web.
	 */
	public String getMenu(Conexion2 conexion, String sCveAplicacion,  String sCvePerfil, String sNomAplicacionWeb){

		boolean bConectaMetodo = false;

		//INICIALIZA EL MENU
		iNivelMenu = 0;
		sMenu = "    <script language='javascript' src='" + sNomAplicacionWeb + "/comun/lib/bMenuConfigRuta.jsp'></script> \n" +
				"    <script language='javascript' src='" + sNomAplicacionWeb + "/comun/lib/menu/menuG3Loader.js'></script> \n" +
				"    <script language='javascript'> \n" +
				"      addMenu('Menu', 'm0'); \n";
		try{
			this.sNomAplicacionWeb = sNomAplicacionWeb;

			configuracionDAO.setConexion(conexion.getConexion());

			//OBTENEMOS LAS OPCIONES DEL MENU PARA EL PRIMER NIVEL
			TreeMap listaOpciones = configuracionDAO.getOpcionMenu(sCvePerfil, sCveAplicacion, " is null ");

			//VERIFICA SI ENCONTRO OPCIONES DEL MENU
			if (listaOpciones != null){
				construyeMenu(listaOpciones, sCvePerfil, sCveAplicacion, iNivelMenu);
			}
			//FINALIZA LA CONSTRUCCION DEL MENU
			sMenu = sMenu + "      endMenu();\n" +
							"      function buildMenu() {\n" +
							"        addInstance('Menu', 'Menu', 'static', 'absolute', 'holder', '', 'left', 'top', 10, 90, 'bar', 'right-down', 'show', '', 'stlMenu');\n" +
							"      }\n" +
							"    </script>\n";
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
		int iNuevoNivel = iNivelActual;
		//RECORRE LA LISTA DE OPCIONES ENCONTRADAS. PARA CADA OPCION SE BUSCARA SI TIENE A SU VEZ
		//OTRAS SUBOPCIONES ASIGNADAS
		Iterator lista = listaOpciones.keySet().iterator();
		while (lista.hasNext()){
			MenuOpcion opcion = (MenuOpcion)lista.next();
			//OBTIENE LAS SUBOPCIONES
			TreeMap listaSubOpciones = configuracionDAO.getOpcionMenu(sCvePerfil, sCveAplicacion, " = '" + opcion.sIdOpcion + "'");
			//VERIFICA SI ENCONTRO SUBOPCIONES
			if ( listaSubOpciones != null ){
				//CREA UNA OPCION DE MENU TIPO SUBMENU
				iNivelMenu ++;

				//SE PINTAL EL SUBMENU DE MANERA DISTINTA SI ESTA EN EL NIVEL SUPERIOR (0) O SI
				//FORMA PARTE DE ALGUN MENU LATERAL

				//VERIFICA SI ES EL NIVEL 0
				if (iNivelActual == 0){
					//PINTA EL SUBMENU
					sMenu = sMenu + "      addSubMenu('m" + Integer.toString(iNivelActual) + "', '" + opcion.sNomOpcion + "', '" + opcion.sNomOpcion + " ', '', 'm" + Integer.toString(iNivelMenu) + "', '');\n";
				}
				else{
					//PINTA EL SUBMENU AGREGANDO LA ETIQUETA ".." AL FINAL DEL
					//NOMBRE DE LA OPCION
					sMenu = sMenu + "      addSubMenu('m" + Integer.toString(iNivelActual) + "', '" + opcion.sNomOpcion +" ...', '" + opcion.sNomOpcion + " ', '', 'm" + Integer.toString(iNivelMenu) + "', '');\n";
				}
				//SE VUELVE A LLAMAR A ESTA FUNCION (RECURSIVIDAD) PARA CONSTRUIR EL SUBMENU
				construyeMenu(listaSubOpciones, sCvePerfil, sCveAplicacion, iNivelMenu);
			}
			else{
				//CREA UNA OPCION SIMPLE TIPO LINK
				sMenu = sMenu + "      addLink('m" + Integer.toString(iNivelActual) + "', '" + opcion.sNomOpcion +"', '" + opcion.sNomOpcion + " ', '" + sNomAplicacionWeb + opcion.sUrl + "', '');\n";
			}
		}
	}

}