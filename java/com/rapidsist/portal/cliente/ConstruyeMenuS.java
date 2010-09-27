/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.catalogos.CatalogoSLHome;
import com.rapidsist.portal.catalogos.LogicaNegocioException;
import com.rapidsist.comun.bd.Registro;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.LinkedList;
import java.util.Iterator;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;

/**
 * Servlet que lleva el control del componente de catálogos.
 * <br/><br/>
 * Parámetros:
 * <ul>
 *	<li>
 * OperacionCatalogo.- Operación que debe realizar el catálogo<br>
 * Posibles valores: AL (alta), IN(inicialización), MO (modificación), BA (Baja),
 * CT (consulta todos), CR (consulta registro), CL (clonar registro).
 *  </li>
 *	<li>
 * Funcion.- Clave de la función que se debe procesar
 *  </li>
 *	<li>
 * Filtro.- Clave del filtro que se debe procesar (este parametro solo es utilizado cuando
 * OperacionCatalogo=CT).
 *  </li>
 *	<li>
 * Ventana.- Le indica al servlet que el resultado se muestra en una ventana hija.
 *  </li>
 * </ul>
 */
public class ConstruyeMenuS extends HttpServlet {
	private ServletConfig config;
	private String sMenu = "";
	private int iNivelMenu = 0;

	final public void init(ServletConfig config) throws ServletException {
		this.config = config;
	}

	final public ServletConfig getServletConfig() {
		return config;
	}

	/**
	 * Adminstra la operación sobre los catálogos del sistema.
	 * @param request Objeto que provee de información al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envía como un parámetro a este método.
	 * @param response Objeto que asiste al servlet en enviar una respuesta HTTP al cliente. El
	 * contenedor  de servlets crea un objeto HttpServletResponse y lo envía como parámetro a este método.
	 */
	public void service (HttpServletRequest request, HttpServletResponse response){
		try{
			String sCadenaMenu = "";
			sCadenaMenu = construyeMenu();
			
			request.setAttribute("MenuScript", sCadenaMenu);
			String sPagina = "/Portales/Aspid/Productos/listaProductosAspid.jsp";
			
			//ENVIA EL CONTROL A LA PAGINA SELECCIONADA
			request.getRequestDispatcher(sPagina).forward(request,response);			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return El menú
	 * @throws RemoteException Si hubo algún error en el EJB de catálogos.
	 * @throws java.lang.Exception Si hubo algún error en la clase controladora.
	 */
	private String construyeMenu() throws RemoteException, Exception{

		RegistroControl registroControl = new RegistroControl();
		Registro parametros = new Registro();
		
		//OBTIENE EL CONTEXTO DEL SERVIDOR DE APLICACIONES
		Context context = new InitialContext();
		//INICIALIZA EL EJB DE CATALOGOS
		Object referenciaCatalogo = context.lookup("java:comp/env/ejb/CatalogoSL");
		CatalogoSLHome catalogoHome = (CatalogoSLHome)PortableRemoteObject.narrow(referenciaCatalogo, CatalogoSLHome.class);
		CatalogoSL catalogoSL = catalogoHome.create();
		
		//INICIALIZA EL MENU
		iNivelMenu = 0;
		sMenu = "<script language='javascript' src='comun/lib/menuHorizontal/bMenuConfigRuta.js'></script>\n" +
				"<script language='javascript' src='comun/lib/menuHorizontal/menuG3Loader.js'></script>\n" +
				"<script language='javascript'>\n" +
				"addMenu('Menu', 'm0');\n";
		
		//OBTENEMOS LAS OPCIONES DEL MENU PARA EL PRIMER NIVEL

		String sIdGrupo = "0";
		String sIdAplicacion = "200";
		String sOpcionPadre = " is null ";		
		
		parametros.addDefCampo("ID_GRUPO",sIdGrupo);
		parametros.addDefCampo("ID_APLICACION",sIdAplicacion);
		parametros.addDefCampo("ID_OPCION_PADRE",sOpcionPadre);
		
		registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("AspidPortalMenu", parametros));
		LinkedList listaOpciones = (LinkedList)registroControl.respuesta.getDefCampo("ListaBusqueda");

		//VERIFICA SI ENCONTRO OPCIONES DEL MENU
		if (listaOpciones != null){
			construyeMenu(listaOpciones, sIdGrupo, sIdAplicacion, iNivelMenu, catalogoSL);
		}
		//FINALIZA LA CONSTRUCCION DEL MENU
		sMenu = sMenu + "endMenu();\n" +
						"function buildMenu() {\n" +
						"   addInstance('Menu', 'Menu', 'static', 'absolute', 'holder', '', 'left', 'top', 65, 71, 'bar', 'right-down', 'show', '', 'stlMenu');\n" +
						"}\n" +
						"</script>\n";
		//sMenu = "PRUEBA";
		return sMenu;
	}
	
	/**
	 * Obtiene las opciones de un menu. Esta función es recursiva si solo es llamada por getMenu()
	 * @param listaOpciones Objeto LinketList con la lista de opciones del menú.
	 * @param sIdGrupo Id del grupo.
	 * @param sIdAplicacion Id de la aprivación.
	 * @param iNivelActual Nivel actual.
	 */
	private void construyeMenu(LinkedList listaOpciones, String sIdGrupo, String sIdAplicacion, int iNivelActual, CatalogoSL catalogoSL){
		int iNuevoNivel = iNivelActual;
		RegistroControl registroControl = new RegistroControl();
		Registro parametros = new Registro();

		//RECORRE LA LISTA DE OPCIONES ENCONTRADAS. PARA CADA OPCION SE BUSCARA SI TIENE A SU VEZ
		//OTRAS SUBOPCIONES ASIGNADAS
		Iterator iteratorOpciones = listaOpciones.iterator();
		while (iteratorOpciones.hasNext()){
			Registro registroOpcionMenu = (Registro)iteratorOpciones.next();
			//OBTIENE LAS SUBOPCIONES

			parametros.addDefCampo("ID_GRUPO",sIdGrupo);
			parametros.addDefCampo("ID_APLICACION",sIdAplicacion);
			parametros.addDefCampo("ID_OPCION_PADRE"," = "+registroOpcionMenu.getDefCampo("ID_OPCION"));
				
			try{	
				registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("AspidPortalMenu", parametros));
				LinkedList listaSubOpciones = (LinkedList)registroControl.respuesta.getDefCampo("ListaBusqueda");
				
				//VERIFICA SI ENCONTRO SUBOPCIONES
				if ( listaSubOpciones != null ){
					//CREA UNA OPCION DE MENU TIPO SUBMENU
					iNivelMenu ++;
	
					//SE PINTAL EL SUBMENU DE MANERA DISTINTA SI ESTA EN EL NIVEL SUPERIOR (0) O SI
					//FORMA PARTE DE ALGUN MENU LATERAL
	
					//VERIFICA SI ES EL NIVEL 0
					if (iNivelActual == 0){
						//PINTA EL SUBMENU
						sMenu = sMenu + "addSubMenu('m" + Integer.toString(iNivelActual) + "', '" + registroOpcionMenu.getDefCampo("NOM_OPCION") + "', '" + registroOpcionMenu.getDefCampo("NOM_OPCION") + " ', '', 'm" + Integer.toString(iNivelMenu) + "', '');\n";
					}
					else{
						//PINTA EL SUBMENU AGREGANDO LA ETIQUETA ".." AL FINAL DEL
						//NOMBRE DE LA OPCION
						sMenu = sMenu + "addSubMenu('m" + Integer.toString(iNivelActual) + "', '" + registroOpcionMenu.getDefCampo("NOM_OPCION") +" ...', '" + registroOpcionMenu.getDefCampo("NOM_OPCION") + " ', '', 'm" + Integer.toString(iNivelMenu) + "', '');\n";
					}
					//SE VUELVE A LLAMAR A ESTA FUNCION (RECURSIVIDAD) PARA CONSTRUIR EL SUBMENU
					construyeMenu(listaSubOpciones, sIdGrupo, sIdAplicacion, iNivelMenu, catalogoSL);
				}
				else{
					//CREA UNA OPCION SIMPLE TIPO LINK
					sMenu = sMenu + "addLink('m" + Integer.toString(iNivelActual) + "', '" + registroOpcionMenu.getDefCampo("NOM_OPCION") +"', '" + registroOpcionMenu.getDefCampo("NOM_OPCION") + " ', '" + registroOpcionMenu.getDefCampo("URL") + "', '');\n";
				}
				
			}
			catch(LogicaNegocioException e) {
			}			
			catch (RemoteException e) {
			}
		}
	}	
	
}