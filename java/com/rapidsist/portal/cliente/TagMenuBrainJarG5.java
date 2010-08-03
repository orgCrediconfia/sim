/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.util.Iterator;
import com.rapidsist.comun.bd.Registro;

import com.rapidsist.portal.configuracion.PortalSL;
import com.rapidsist.portal.configuracion.PortalSLHome;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.configuracion.ConfiguracionPortalDAO;


/**
 * Imprime la barra de menú que se utiliza en una página
 */
public class TagMenuBrainJarG5 extends TagSupport {
	
	/**
	 * Un objeto de acceso a base de datos sobre la configuación del portal.
	 */
	private ConfiguracionPortalDAO configuracionPortalDao = new ConfiguracionPortalDAO();
	
	/**
	 * Nombre de la aplicación web.
	 */
	private String sNomAplicacionWeb = "";	

	String sMenu = "";
	String sSubMenu = "";
	int iNivelMenu = 0;
	/**
	 *
	 * @throws JspException Si hubo algún error durante la ejecución del TagLib.
	 */
	public int doStartTag() throws JspException {
		try{
			
			Usuario usuario = (Usuario)pageContext.getSession().getAttribute("Usuario");
			sNomAplicacionWeb = usuario.sNomAplicacionWeb;
			//OBTIENE EL CONTEXTO DEL SERVIDOR DE APLICACIONES
			Context context = new InitialContext();
			Object referencia = context.lookup("java:comp/env/ejb/PortalSL");
			PortalSLHome portalHome = (PortalSLHome)PortableRemoteObject.narrow(referencia, PortalSLHome.class);
			PortalSL configuracion = portalHome.create();
			
			sMenu = "";
			
			JspWriter out = pageContext.getOut();
			
			sMenu = sMenu + " <style type=\"text/css\"> \n";
			//COLOR DE FONDO Y BORDES DE LA TABLA DEL MENÚ
			sMenu = sMenu + " .menuBar {background-color: #999; border: 2px solid; border-color: #cccccc #666666 #666666 #cccccc; padding: 1px 4px; } \n";
			sMenu = sMenu + " .menuPad {background-color: #999; border: 2px solid; border-color: #CCCCCC #666666 #666666 #cccccc; padding:4px; } \n";
			//COLOR DEL BORDE QUE DIVIDE LOS ELEMENTOS DEL MENU (SELECCIONANDO EL MENU Y SIN SELECCIONAR) 
			sMenu = sMenu + " .menuItem {background-color: #999; border: 1px solid #999; color: #000000; cursor: default; font-weight: bold; padding: 2px 6px 2px 6px; } \n"; 
			sMenu = sMenu + " .menuItemOn {background-color: #999; border: 1px solid #999; color: #000000; cursor: default; font-weight: bold; padding: 2px 6px 2px 6px; border-color: #cccccc #666666 #666666 #cccccc; } \n";
			//COLOR DEL SUBMENU CON O SIN HIJOS
			sMenu = sMenu + " .menuItemDown {background-color: #999; border: 1px solid #999; color: #000000; cursor: default; font-weight: bold; padding: 2px 6px 2px 6px;  margin-top:1px; margin-left:1px; background-color: #666666; border-color: #545454 #cccccc #cccccc #545454;} \n";
			sMenu = sMenu + " .menuItemSub {background-color: #999; border: 1px solid #999; color: #000000;  cursor: default; font-weight: bold; padding: 2px 6px 2px 6px; border: 1px solid #666666;  background-color: #666666;} \n";
			sMenu = sMenu + " .holderSpace {border: 1px solid #999; color: #999; font-weight: bold; padding: 2px 6px 2px 6px; font-family: 'MS Sans Serif', Arial, sans-serif; font-size: 8pt; font-style: normal; font-weight: normal;} \n"; 
			//TIPO DE LETRA DEL MENU, COLOR Y TIPO DE LETRA DEL MENU Y SUBMENU SELECCIONADO Y SIN SELECCIONAR
			sMenu = sMenu + " .menuFont {font-family: 'MS Sans Serif', Arial, sans-serif; font-size: 8pt; font-style: normal; font-weight: normal; text-decoration: none;} \n"; 
			sMenu = sMenu + " .menuFontOff {font-family: 'MS Sans Serif', Arial, sans-serif; font-size: 8pt; font-style: normal; font-weight: normal; text-decoration: none; color: #000000;} \n";  
			sMenu = sMenu + " .menuFontOn {font-family: 'MS Sans Serif', Arial, sans-serif; font-size: 8pt; font-style: normal; font-weight: normal; text-decoration: none; color: White;} \n"; 
			sMenu = sMenu + " .menuFontOffBold {font-family: 'MS Sans Serif', Arial, sans-serif; font-size: 8pt; font-style: normal; font-weight: normal; text-decoration: none; color: #000000; font-weight: bold;} \n";  
			sMenu = sMenu + " .menuFontOnBold {font-family: 'MS Sans Serif', Arial, sans-serif; font-size: 8pt; font-style: normal; font-weight: normal; text-decoration: none; color: White; font-weight: bold;} \n"; 
			sMenu = sMenu + " .menuInfo { padding: 2px 6px 2px 6px; }  \n"; 
			//COLOR DE LA FLECHA EN EL SUBMENU CUANDO SE SELECCIONA Y CUANDO NO 
			sMenu = sMenu + " .tagOff { width:15px; height:15px; float:right; background:url('/portal/comun/lib/menuBrainJarG5/tagRN.gif') no-repeat bottom; }  \n";
			sMenu = sMenu + " .tagOn { width:15px; height:15px; float:right; background:url('/portal/comun/lib/menuBrainJarG5/tagRH.gif') no-repeat bottom; }  \n";
			//COLOR DE LA LINEA QUE SEPARA A CADA COMPONENTE DEL MENU
			sMenu = sMenu + " .separatorT { background-color:#666666; height:1px; margin-top:3px; } \n";
			sMenu = sMenu + " .separatorB { background-color:#cccccc; height:1px; margin-bottom:3px; } \n";
			sMenu = sMenu + " .usuariomenu {background-color: #333333; border: 2px solid; border-color: #333333 #333333 #333333 #333333; padding:4px; color: White;} \n";
			sMenu = sMenu + " </style> \n";
			sMenu = sMenu + " <script language=\'javascript\' src=\'/portal/comun/lib/menuBrainJarG5/menuG5LoaderX.js'></script>\n";
			
			//INICIA CODIGO QUE REEMPLAZA EL CONTENIDO DEL ARCHIVO brainjar-content.js
			sMenu = sMenu + " <script language='javascript'>\n" ;
			sMenu = sMenu + " addMenu('brainjar', 'm');\n";
			
			//INICIA SELECTOR DE APLICACIONES
			String sRutaContexto = ((HttpServletRequest)pageContext.getRequest()).getContextPath();
			if (usuario.listaAplicacionesSelector != null){
				Iterator lista = usuario.listaAplicacionesSelector.iterator();
				while (lista.hasNext()){
					Registro aplicacion = (Registro)lista.next();
					String sCveAplicacion = (String)aplicacion.getDefCampo("CVE_APLICACION");
					String sNomAplicacion = (String)aplicacion.getDefCampo("NOM_APLICACION");
					//VERIFICA SI SELECCIONA LA APLICACION ACTUAL DEL USUARIO
					if (sCveAplicacion.equals(usuario.sAplicacionActual)){
						sMenu = sMenu + " addSubMenu('m', '"+sNomAplicacion+"', '', '', 'm"+ iNivelMenu +"', '');\n";
						sSubMenu = configuracion.getMenuAplicacionHorizontal(usuario.sAplicacionActual, usuario.sCvePerfilActual, usuario.sNomAplicacionWeb);
						sMenu = sMenu + sSubMenu;
					}
					else{
						sMenu = sMenu + " addLink('m', '"+sNomAplicacion+"', '', '" + sRutaContexto + "/CambiaAplicacion?CveAplicacion=" + sCveAplicacion + "', '');\n";
					}
				}//END WHILE
			}


			sMenu = sMenu + " endMenu();\n";
			sMenu = sMenu + " </script>\n";
			//FIN DEL CODIGO QUE REEMPLAZA EL CONTENIDO DEL ARCHIVO brainjar-content.js
			
			//INICIA CODIGO QUE REEMPLAZA EL CONTENIDO DEL ARCHIVO brainjar-style.js
			sMenu = sMenu + " <script language='javascript'>\n" ;
			sMenu = sMenu + " addStylePad('padSub', 'pad-css:menuBar; offset-top:4; offset-left:6;');\n";
			sMenu = sMenu + " addStyleItem('itemTop', 'css:menuItem, menuItemOn, menuItemDown, menuItemDown; width:actual; sub-menu:mouse-click;');\n";
			sMenu = sMenu + " addStyleItem('itemSub', 'css:menuItem, menuItemSub; width:actual;');\n";
			sMenu = sMenu + " addStyleItem('itemInfo', 'css:menuInfo; width:actual;');\n";
			sMenu = sMenu + " addStyleFont('fontTop', 'css:menuFontOffBold, menuFontOnBold;');\n";
			sMenu = sMenu + " addStyleFont('fontSub', 'css:menuFontOff, menuFontOn;');\n";
			sMenu = sMenu + " addStyleFont('fontInfo', 'css:menuFontOffBold;');\n";
			sMenu = sMenu + " addStyleTag('tag', 'css:tagOff, tagOn;');\n";
			sMenu = sMenu + " addStyleSeparator('sep', 'css:separatorT, separatorB;');\n";

			sMenu = sMenu + " addStyleMenu('menu', '', 'itemTop', 'fontTop', '', '', 'sep');\n";
			sMenu = sMenu + " addStyleMenu('sub', 'padSub', 'itemSub', 'fontSub', 'tag', '', 'sep');\n";
			sMenu = sMenu + " addStyleMenu('info', '', 'itemInfo', 'fontInfo', '', '', '');\n";

			sMenu = sMenu + " addStyleGroup('group', 'menu', 'm');\n";
			sMenu = sMenu + " addStyleGroup('group', 'sub', 'm0', 'm1', 'm2', 'm3', 'm4', 'm5', 'm6', 'm7', 'm8', 'm9', 'm10', 'm11'  );\n";
			sMenu = sMenu + " addStyleGroup('group', 'info', 'info');\n";
			sMenu = sMenu + " addInstance('BrainJar', 'brainjar', 'position:relative holder; menu-form:bar; offset-top:3px; offset-left:5px; style:group; sticky:yes;');\n";
			sMenu = sMenu + " </script>\n";	
			//FIN DEL CODIGO QUE REEMPLAZA EL CONTENIDO DEL ARCHIVO brainjar-style.js			
			
			//INICIA EL CODIGO QUE PINTA LA TABLA DEL MENÚ Y LA DEL USUARIO
			sMenu = sMenu + " <script>\n";							
			sMenu = sMenu + " 	window.onload = function(){ \n";
			sMenu = sMenu + " 	fPosicionaPie(); \n";
			sMenu = sMenu + " 	showMenu('BrainJar'); \n";
			sMenu = sMenu + " 	} \n";
			sMenu = sMenu + " </script> \n";			
			sMenu = sMenu + " <table width='1100'  align='right' border='0' cellpadding='0' cellspacing='0' > \n";
			sMenu = sMenu + " 	<tr> \n";
			sMenu = sMenu + " 		<td width='300' id='usuarioprueba'> \n";
			sMenu = sMenu + " 			<div class='usuariomenu'> \n";
			sMenu = sMenu + " 				Bienvenido:" + usuario.sNomCompleto + "\n";
			sMenu = sMenu + " 			</div> \n";
			sMenu = sMenu + " 		</td> \n";
			sMenu = sMenu + " 		<td width='800' id='holder'> \n";
			sMenu = sMenu + " 			<div class='menuBar'> \n";
			sMenu = sMenu + " 				<div class='holderSpace'> \n";
			sMenu = sMenu + " 					&nbsp; \n";
			sMenu = sMenu + " 				</div> \n";
			sMenu = sMenu + " 			</div> \n";
			sMenu = sMenu + " 		</td> \n";
			sMenu = sMenu + " 	</tr> \n";
			sMenu = sMenu + " </table> \n";	
			sMenu = sMenu + " <br clear='all'> \n";
			sMenu = sMenu + " &nbsp; \n";
			sMenu = sMenu + " &nbsp; \n";
			//FIN DEL CODIGO QUE PINTA LA TABLA DEL MENÚ Y LA DEL USUARIO
			
			out.println(sMenu);
		}

		catch(Exception e){
			e.printStackTrace();
		}
		
		return(TagSupport.SKIP_BODY);
	}
	
}

