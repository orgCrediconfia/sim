/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import com.rapidsist.portal.catalogos.CatalogoSL;
import java.util.LinkedList;
import java.util.Iterator;
import com.rapidsist.comun.bd.Registro;
import java.rmi.RemoteException;
import com.rapidsist.portal.catalogos.LogicaNegocioException;

/**
 * Imprime la barra de menú que se utiliza en una página
 */
public class TagBarraMenu extends TagSupport {

	String sMenu = "";
	int iNivelMenu = 0;
	
	/**
	 *
	 * @throws JspException Si hubo algún error durante la ejecución del TagLib.
	 */
	public int doStartTag() throws JspException {
		try{
			JspWriter out = pageContext.getOut();
			
			/*
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
							"   addInstance('Menu', 'Menu', 'static', 'absolute', 'holder', '', 'left', 'top', 120, 85, 'bar', 'right-down', 'show', '', 'stlMenu');\n" +
							"}\n" +
							"</script>\n";
							
			out.println("<table width='95%' border='0'>");
			
			out.println("<tr>");
			out.println("<td width='9%' height='75'>&nbsp;</td>");
			out.println("<td width='84%'>");
			out.println("</td>");
			out.println("<td width='7%'>&nbsp;</td>");
			out.println("</tr>");
			
			out.println("<tr>");
			out.println("<td height='60'>&nbsp;</td>");
			
			out.println("<td><table width='100%' height='55' border='0' cellspacing='0' cellpadding='0' background='comun/img/menuHorizontal/MenuCen.gif'>");
			out.println("<tr>");
			out.println("<td width='7' nowrap>&nbsp;</td>");
			out.println("<td width='100%'>&nbsp;</td>");
			out.println("<td width='7' nowrap>&nbsp;</td>");
			out.println("</tr>");
			out.println("</table></td>");
			
			out.println("<td>&nbsp;</td>");
			out.println("</tr>");
			
			out.println("<tr>");
			out.println("<td height='5'>&nbsp;</td>");
			out.println("<td>&nbsp;</td>");
			out.println("<td>&nbsp;</td>");
			out.println("</tr>");
			
			out.println("</table>");
					
			*/

			sMenu = "";
			sMenu = sMenu + "<style type='text/css'>\n";
			sMenu = sMenu + ".padTop { background-color:#ffffff; } \n";
			sMenu = sMenu + ".itemTopOff { padding:5px 10px; background-color:#dce8fa; } \n";
			sMenu = sMenu + ".itemTopOn { cursor:default; padding:5px 10px; background-color:#9D7DDF; } \n";
			
			sMenu = sMenu + ".padSub { background-color:#ffffff; } \n";
			sMenu = sMenu + ".itemSubOff { padding:5px 10px; border:1px solid #6699cc; background-color:#dce8fa; } \n";
			sMenu = sMenu + ".itemSubOn { cursor:default; padding:5px 10px; border:1px solid #6699cc; background-color:#9D7DDF; border-color:#003366 #6699cc #6699cc #003366; } \n";
			
			sMenu = sMenu + ".itemSub2Off { width:90px; border:1px solid #000000; padding:0px 10px; background-color:#6699cc; } \n";
			sMenu = sMenu + ".itemSub2On { cursor:default; width:90px; border:1px solid #000000; padding:0px 10px; background-color:#336699; } \n";
			
			sMenu = sMenu + ".fontOff { font-family:verdana; font-size:10pt; color:#000000; } \n";
			sMenu = sMenu + ".fontOn { font-family:verdana; font-size:10pt; color:#ffffff; } \n";
			sMenu = sMenu + "</style> \n";
			
			sMenu = sMenu + "<script language='javascript' src='comun/lib/menuHorizontal5/bar-path.js'></script>\n";
			sMenu = sMenu + "<script language='javascript' src='comun/lib/menuHorizontal5/menuG5LoaderX.js'></script> \n";
			
			
			out.println(sMenu);
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return(TagSupport.SKIP_BODY);
	}
	
	/**
	 * Obtiene las opciones de un menu. Esta función es recursiva si solo es llamada por getMenu()
	 * @param listaOpciones Objeto LinkedList con las opciones del menú.
	 * @param sIdGrupo Id del grupo.
	 * @param sIdAplicacion Id de la aplicación.
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