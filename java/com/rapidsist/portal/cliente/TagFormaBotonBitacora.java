/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import com.rapidsist.portal.configuracion.Permisos;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Coloca el botón de bitácora dentro de la forma de captura si el usuario
 * tiene ese permiso, y ejecuta el componente de bitácora para la función especificada en la
 * página.
 * <br><br>
 * Parámetros:
 * <ul>
 *	<li>
 * etiqueta.- Etiqueta del botón de baja.
 *  </li>
 * </ul>
 */
public class TagFormaBotonBitacora extends TagSupport {

	String sEtiqueta = "Bitacora";

	public void setEtiqueta(String sEtiqueta){
		this.sEtiqueta = sEtiqueta;
	}

	/**
	 * Método estándar de la interfaz de TagLibs, que es disparado por el contenedor
	 * de servlets al comenzar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como manejar el flujo
	 * de ejecución del TagLib y el contenido que pueda existir dentro del tag.
	 * @throws JspException Si hubo algún error durante la ejecución del TagLib.
	 */
	public int doStartTag() throws JspException {
		int iRespuesta = TagSupport.SKIP_BODY;
		try {
			//VERIFICA SI EL USUARIO TIENE PERMISOS DE BITACORA 
			Permisos permiso = (Permisos)pageContext.getAttribute("permiso");
			String sOperacionCatalogo = pageContext.getRequest().getParameter("OperacionCatalogo");
			if(!sOperacionCatalogo.equals("AL")){
				if(!sOperacionCatalogo.equals("IN")){
					if (permiso.bBitacora) {
						JspWriter out = pageContext.getOut();
						out.println();
						out.println("        <input type=\"button\" name=\"btnBaja\" value=\"" + sEtiqueta + "\" onClick=\"javascript:Bitacora();\" >&nbsp;");
						out.println("        <script languaje=\"javascript\">");
						out.println("          function MM_openBrWindow(theURL,winName,features) {");
						out.println("             window.open(theURL,winName,features);");
						out.println("          }");
						out.println("          //*******************************************************************");
						out.println("          //PRESENTA LA BITACORA DEL REGISTRO EN UNA VENTANA");
						out.println("          //*******************************************************************");
						out.println("          function Bitacora() {");
						out.println("             MM_openBrWindow('/portal/ConsultaBitacora?CveFuncion=" + pageContext.getAttribute("CveFuncion") + "','Bitacora','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');");
						out.println("          }");
						out.println("        </script>");
					}
				}
			}
		}
		catch (IOException ex) {
		}
		return(iRespuesta);
	}
}