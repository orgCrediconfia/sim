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

import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;

import java.util.Iterator;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.PortalSL;
import com.rapidsist.portal.configuracion.PortalSLHome;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.catalogos.CatalogoSLHome;


/**
 * Imprime la barra de menú que se utiliza en una página
 * <br><br>
 * Parámetros:
 * <ul>
 *	<li>
 * normalcolor.- Parámetro que define el color del menú
 *  </li>
 *	<li>
 * <br><br>
 * oncolor.- Parámetro que define el color del menú cuando es señalado por el ratón
 *  </li>
 *	<li>
 * <br><br>
 * downcolor.- Parámetro que define el color del menú cuando se le da click con el ratón
 *  </li>
 *	<li>
 * <br><br>
 * bordecolordark.- Párametro que define el color del borde obscuro
 *  </li>
 *	<li>
 * <br><br>
 * bordecolorlight.- Párametro que define el color del borde claro
 *  </li>
 *	<li>
 * <br><br>
 * offsettop.- Párametro que define la posición superior del menú
 *  </li>
 *	<li>
 * <br><br>
 * fontlight.- Párametro que define el color claro de la letra (down y on)
 *  </li>
 *	<li>
 * <br><br>
 * fontdark.- Párametro que define el color obscuro de la letra (normal)
 *  </li>
 *	<li>
 * <br><br>
 */

public class TagMenuFiltrosG5 extends TagSupport {

	String snormalcolor = "";
	String soncolor = "";
	String sdowncolor = "";
	String sbordercolordark = "";
	String sbordercolorlight = "";
	String sfontlight = "";
	String sfontdark = "";
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
		
		//OBTIENE EL CONTEXTO DEL SERVIDOR DE APLICACIONES
		Context context = new InitialContext();
		//INICIALIZA EL EJB DE CATALOGOS
		Object referenciaCatalogo = context.lookup("java:comp/env/ejb/CatalogoSL");
		CatalogoSLHome catalogoHome = (CatalogoSLHome)PortableRemoteObject.narrow(referenciaCatalogo, CatalogoSLHome.class);
		CatalogoSL catalogoSL = catalogoHome.create();
		
		Registro parametros = new Registro();
		parametros.addDefCampo("CVE_GPO_EMPRESA", usuario.sCveGpoEmpresa);
		parametros.addDefCampo("CVE_PORTAL", usuario.sCvePortal);
		
	
		Registro menucolor = new Registro();
		menucolor = catalogoSL.getRegistro("HerramientasConfiguracionCatalogoColor", parametros);
		
		 if (menucolor !=null ){
			 snormalcolor = (String)menucolor.getDefCampo("NORMAL_COLOR");
			 soncolor = (String)menucolor.getDefCampo("ON_COLOR");
			 sdowncolor = (String)menucolor.getDefCampo("DOWN_COLOR");
			 sbordercolordark = (String)menucolor.getDefCampo("BORDER_COLOR_DARK");
			 sbordercolorlight = (String)menucolor.getDefCampo("BORDER_COLOR_LIGHT");
			 sfontlight = (String)menucolor.getDefCampo("FONT_LIGHT");
			 sfontdark = (String)menucolor.getDefCampo("FONT_DARK");	 
		 }
		
			snormalcolor = (String) ExpressionUtil.evalNotNull("out", "snormalcolor", snormalcolor, Object.class, this, pageContext);
			soncolor = (String) ExpressionUtil.evalNotNull("out", "soncolor", soncolor, Object.class, this, pageContext);
			sdowncolor = (String) ExpressionUtil.evalNotNull("out", "sdowncolor", sdowncolor, Object.class, this, pageContext);
			sbordercolordark = (String) ExpressionUtil.evalNotNull("out", "sbordercolordark", sbordercolordark, Object.class, this, pageContext);
			sbordercolorlight = (String) ExpressionUtil.evalNotNull("out", "sbordercolorlight", sbordercolorlight, Object.class, this, pageContext);
			sfontlight = (String) ExpressionUtil.evalNotNull("out", "sfontlight", sfontlight, Object.class, this, pageContext);
			sfontdark = (String) ExpressionUtil.evalNotNull("out", "sfontdark", sfontdark, Object.class, this, pageContext);
			
			
			//OBTIENE EL CONTEXTO DEL SERVIDOR DE APLICACIONES
			Object referencia = context.lookup("java:comp/env/ejb/PortalSL");
			PortalSLHome portalHome = (PortalSLHome)PortableRemoteObject.narrow(referencia, PortalSLHome.class);
			PortalSL configuracion = portalHome.create();
			
			sMenu = "";
			
			JspWriter out = pageContext.getOut();
			
			sMenu = sMenu + "<style type=\"text/css\"> \n" +
								//COLOR DE FONDO, COLOR DEL BORDE Y ANCHO EN EL ELEMENTO QUE MUESTRA EL NOMBRE DEL USUARIO
								".usuariomenu {background-color: " + snormalcolor + "; border: 1px solid; border-color: " + sbordercolorlight + " " + sbordercolordark + " " + sbordercolordark + " " + sbordercolorlight + "; padding:2px;} \n" +
								//COLOR, TAMAÑO, ESTILO Y TIPO DE LETRA EN EL MENÚ
								".fontUsuario {font-family: Arial; font-size:12px; color:#ffffff; font-weight: bold; } \n" +
								//TIPO DE EFECTO Y DURACIÓN DEL MISMO
								".subholder1 { filter: progid:DXImageTransform.Microsoft.GradientWipe(GradientSize=0.00,wipestyle=1,motion=forward) progid:DXImageTransform.Microsoft.Shadow(direction=135,color=#333333,strength=3);} \n" +
								".subholder2 { filter: progid:DXImageTransform.Microsoft.RandomDissolve(duration=0.3) progid:DXImageTransform.Microsoft.Shadow(direction=135,color=#333333,strength=3);} \n" +
								".subholder3 { filter: progid:DXImageTransform.Microsoft.RadialWipe(wipestyle=RADIAL) progid:DXImageTransform.Microsoft.Shadow(direction=135,color=#333333,strength=3);} \n" +
								".subholderx { filter: progid:DXImageTransform.Microsoft.GradientWipe(GradientSize=0.00,wipestyle=0,motion=forward) progid:DXImageTransform.Microsoft.Shadow(direction=135,color=#333333,strength=3); } \n" +
								//COLOR DE FONDO Y TIPO Y COLOR DEL BORDE DEL SUBMENÚ
								".bar {padding:4px; border-width:1px; border-style:solid; border-color:" + sbordercolorlight + " " + sbordercolordark + " " + sbordercolordark + " " + sbordercolorlight + "; background-color:" + snormalcolor + ";} \n" +
								//DEFINICIÓN DE LOS EFECTOS
								".itemTopL1 { filter: progid:DXImageTransform.Microsoft.Iris(irisstyle=CROSS,motion=out);cursor:default; padding:2px 2px 2px 12px; border-width:1px; border-style:solid; text-align:left;} \n" +
								".itemTopS1 { filter: progid:DXImageTransform.Microsoft.GradientWipe(GradientSize=0.00,wipestyle=1,motion=forward);  cursor:default; padding:2px; border-width:1px; border-style:solid; text-align:left;} \n" +
								".itemTopS2 { filter: progid:DXImageTransform.Microsoft.RandomDissolve(duration=0.1); cursor:default; padding:2px; border-width:1px; border-style:solid; text-align:left;} \n" +
								".itemTopS3 { filter: progid:DXImageTransform.Microsoft.RadialWipe(wipestyle=RADIAL); cursor:default; padding:2px; border-width:1px; border-style:solid; text-align:left;} \n" +
								".itemTopL2 { FILTER: progid:DXImageTransform.Microsoft.Strips(motion=rightdown); cursor:default; padding:2px 2px 2px 12px; border-width:1px; border-style:solid; text-align:left;} \n" +
								".itemSub { filter: progid:DXImageTransform.Microsoft.Slide(slidestyle=PUSH,Bands=1,duration=0.1); cursor:default; padding:2px 2px 2px 12px; border-width:1px; border-style:solid; text-align:left;} \n" +
								//COLOR DE FONDO Y COLOR Y TIPO DE BORDE EN EL MENÚ PRINCIPAL
								".itemNormal { cursor:default; border-color:" + sbordercolorlight + " " + sbordercolordark + " " + sbordercolordark + " " + sbordercolorlight + "; background-color:" + snormalcolor + "; } \n" +
								//COLOR DE FONDO Y COLOR Y TIPO DE BORDE EN EL MENÚ PRINCIPAL CUANDO SE LE SEÑALA CON EL MOUSE								
								".itemOn { cursor:default; border-color:" + sbordercolorlight + " " + sbordercolordark + " " + sbordercolordark + " " + sbordercolorlight + "; background-color:" + soncolor + "; } \n" +
								//COLOR DE FONDO Y COLOR Y TIPO DE BORDE EN EL MENÚ PRINCIPAL CUANDO SE DA CLICK EN EL ELEMENTO
								".itemDown { cursor:default; border: 1px solid; border-color:" + sbordercolordark + " " +  sbordercolorlight + " " + sbordercolorlight + " " + sbordercolordark + "; background-color:" + sdowncolor + "; } \n" +
								//COLOR, TIPO, ALINEACIÓN Y TAMAÑO DE LA LETRA EN EL ITEM NORMAL
								".fontNormal { font-family: Arial; text-align:left; font-size:12px; color:" + sfontdark + "; font-weight: bold; } \n" +
								//COLOR, TIPO, ALINEACIÓN Y TAMAÑO DE LA LETRA EN EL ITEM ON
								".fontOn { font-family: Arial; text-align:left; font-size:12px; color:" + sfontlight + ";  font-weight: bold;} \n" +
								//COLOR, TIPO, ALINEACIÓN Y TAMAÑO DE LA LETRA EN EL ITEM DOWN
								".fontDown { font-family: Arial; font-size:12px; text-align:left; color:" + sfontlight + "; font-weight: bold;} \n" +
								//DIRECCIÓN Y AJUSTES DE LA IMAGEN QUE SE MUESTRA EN EL MENÚ (PARTE SUPERIOR DERECHA)
								".tagNormal { float:right; width:10px; height:10px; background:url('/portal/comun/lib/menuFiltrosG5/tagRN2.gif') no-repeat bottom; } \n" +
								".tagOn { float:right; width:10px; height:10px; background:url('/portal/comun/lib/menuFiltrosG5/tagRH2.gif') no-repeat bottom; } \n" +
								".tagDown { float:right; width:8px; height:10px; background:url('/portal/comun/lib/menuFiltrosG5/tagRD2.gif') no-repeat bottom; } \n" + 
   					       "</style> \n";
			
			
			//SE MANDA LLAMAR A MENUBRAINJARG5 QUE ES UNA LIBRERIA
			sMenu = sMenu + " <script language=\'javascript\' src=\'/portal/comun/lib/menuFiltrosG5/menuG5LoaderX.js'></script>\n";
			
			//INICIA CODIGO QUE REEMPLAZA EL CONTENIDO DEL ARCHIVO brainjar-content.js
			sMenu = sMenu + " <script language='javascript'>\n" ;
			sMenu = sMenu + " addMenu('Filtros', 'm'); \n";

			//INICIA SELECTOR DE APLICACIONES
			String sRutaContexto = ((HttpServletRequest)pageContext.getRequest()).getContextPath();
			if (usuario.listaAplicacionesSelector != null){
				Iterator lista = usuario.listaAplicacionesSelector.iterator();
				iNivelMenu = 0;
				while (lista.hasNext()){
					Registro aplicacion = (Registro)lista.next();
					String sCveAplicacion = (String)aplicacion.getDefCampo("CVE_APLICACION");
					String sNomAplicacion = (String)aplicacion.getDefCampo("NOM_APLICACION");
					//VERIFICA SI SELECCIONA LA APLICACION ACTUAL DEL USUARIO
					if (sCveAplicacion.equals(usuario.sAplicacionActual)){
						sMenu = sMenu + " addSubMenu('m', '"+sNomAplicacion+"', '', '', 'm"+ iNivelMenu +"', 's2');\n";
						sSubMenu = configuracion.getMenuAplicacionHorizontal(usuario.sAplicacionActual, usuario.sCvePerfilActual, usuario.sNomAplicacionWeb);
						sMenu = sMenu + sSubMenu;
					}
					else{
						sMenu = sMenu + " addLink('m', '"+sNomAplicacion+"', '', '" + sRutaContexto + "/CambiaAplicacion?CveAplicacion=" + sCveAplicacion + "', 'l1');\n";
					}
				}//END WHILE
			}
			sMenu = sMenu + " endMenu();\n";
			sMenu = sMenu + " </script>\n";
			
			//FIN DEL CODIGO QUE REEMPLAZA EL CONTENIDO DEL ARCHIVO brainjar-content.js
			
			//INICIA CODIGO QUE REEMPLAZA EL CONTENIDO DEL ARCHIVO brainjar-style.js
			sMenu = sMenu + " <script language='javascript'> \n" +
						"addStylePad('padSub', 'pad-css:menuBar; offset-top:2; offset-left:6;'); \n" +
						"addStylePad('sub1', 'holder-css:subholder1; pad-css:bar; width:actual;');  \n" +
						"addStylePad('sub2', 'holder-css:subholder2; pad-css:bar; width:actual; filters:yes,yes;'); \n" +
						"addStylePad('sub3', 'holder-css:subholder3; pad-css:bar; width:actual; filters:yes,yes;'); \n" +
						"addStylePad('subx', 'holder-css:subholderx; pad-css:bar; width:actual; width:actual; offset-left:-2;'); \n" +
						"addStyleItem('itemTopL1', 'css:itemNormal itemTopL1, itemOn itemTopL1, itemDown itemTopL1; width:actual; filters:yes,no;'); \n" +
						"addStyleItem('itemTopS1', 'css:itemNormal itemTopS1, itemOn itemTopS1, itemDown itemTopS1; width:actual; filters:yes;'); \n" +
						"addStyleItem('itemTopS2', 'css:itemNormal itemTopS2, itemOn itemTopS2, itemDown itemTopS2; width:actual; filters:yes;'); \n" +
						"addStyleItem('itemTopS3', 'css:itemNormal itemTopS3, itemOn itemTopS3, itemDown itemTopS3; width:actual; filters:yes;'); \n" +
						"addStyleItem('itemTopL2', 'css:itemNormal itemTopL2, itemOn itemTopL2, itemDown itemTopL2; width:actual; filters:no, no, yes;'); \n" +
						"addStyleItem('itemSub', 'css:itemNormal itemSub, itemOn itemSub, itemDown itemSub; width:actual;'); \n" +
						"addStyleFont('font', 'css:fontNormal, fontOn, fontDown;'); \n" +
						"addStyleTag('tag', 'css:tagNormal, tagOn, tagDown;'); \n" +
						"addStyleMenu('menu', 'bar', '', 'font', 'tag', '', ''); \n" +
						"addStyleMenu('L1', '', 'itemTopL1', 'font', 'tag', '', ''); \n" +
						"addStyleMenu('S1', '', 'itemTopS1', 'font', 'tag', '', ''); \n" +
						"addStyleMenu('S2', '', 'itemTopS2', 'font', 'tag', '', ''); \n" +
						"addStyleMenu('S3', '', 'itemTopS3', 'font', 'tag', '', ''); \n" +
						"addStyleMenu('L2', '', 'itemTopL2', 'font', 'tag', '', ''); \n" +
						"addStyleMenu('sub1', 'sub1', 'itemSub', 'font', 'tag', '', ''); \n" +
						"addStyleMenu('sub2', 'sub2', 'itemSub', 'font', 'tag', '', ''); \n" +
						"addStyleMenu('sub3', 'sub3', 'itemSub', 'font', 'tag', '', ''); \n" +
						"addStyleMenu('subx', 'subx', 'itemSub', 'font', 'tag', '', ''); \n" +
						"addStyleGroup('group', 'menu', 'm'); \n" +
						"addStyleGroup('group', 'L1', 'l1'); \n" +
						"addStyleGroup('group', 'S1', 's1'); \n" +
						"addStyleGroup('group', 'S2', 's2'); \n" +
						"addStyleGroup('group', 'S3', 's3'); \n" +
						"addStyleGroup('group', 'L2', 'l2'); \n" +
						"addStyleGroup('group', 'sub2', 'm0', 'm1', 'm2', 'm3', 'm4', 'm5', 'm6', 'm7', 'm8', 'm9', 'm10', 'm11'  );\n"+
						//DEFINE EL NOMBRE DEL MENÚ, LA POSICIÓN, DONDE SE MOSTRARÁ EL MENÚ (IZQUIERDA), LA ALINEACIÓN, EL ESTILO DEL MENÚ, LA DIRECCIÓN EN LA QUE SE MUESTRA EL SUBMENÚ.
						"addInstance('Filtros', 'Filtros', 'position:relative holder; offset-left:3px; align:right;  menu-form:bar; style:group; direction:left-down; sticky::yes;'); \n" +
					"</script>\n";	
			//FIN DEL CODIGO QUE REEMPLAZA EL CONTENIDO DEL ARCHIVO brainjar-style.js

			//INICIA EL CODIGO QUE PINTA LA TABLA DEL MENÚ Y LA DEL USUARIO
			sMenu = sMenu + "<script type=\"text/javascript\">\n" +
							" 	window.onload = function(){ \n" +
							" 	fPosicionaPie(); \n" +
							" 	showMenu('Filtros', 'right'); \n" +
							" 	} \n" +
							"</script> \n" +
							"<table width='100%' valign='top' align='right' border='0' cellpadding='0' cellspacing='0' >\n" +
							"	<tr>\n" +
							"		<td width='100%' id='usuarioprueba'>\n" +
							"			<div class='usuariomenu'>\n" +
							"				<div class='fontUsuario'>\n" +
							"					Bienvenido:" + usuario.sNomCompleto + 
							"				</div>\n" +
							"			</div>\n" +
							" 		</td> \n"+
							" 		<td width='800' id='holder'> \n"+
							" 					&nbsp; \n"+
							" 		</td> \n"+							
							"	</tr>\n" +
							"</table>\n" +
							"&nbsp; \n" +
							"&nbsp; \n";
							
			//FIN DEL CODIGO QUE PINTA LA TABLA DEL MENÚ Y LA DEL USUARIO
			
			out.println(sMenu);
			
		}

		catch(Exception e){
			e.printStackTrace();
		}
		
		return(TagSupport.SKIP_BODY);
	}
	/**
	 * Método estándar de la interfaz de TagLibs, el cual es disparado por el contenedor
	 * de servlets al finalizar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como debe evaluar el resto
	 * de una página JSP una vez terminado el TagLib.
	 */
	public int doEndTag() {
		return javax.servlet.jsp.tagext.TagSupport.EVAL_PAGE;
	}
}
