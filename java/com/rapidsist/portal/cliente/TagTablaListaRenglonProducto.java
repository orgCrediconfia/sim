/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Imprime un renglón de una tabla alternando los colores de las mismas dependiendo si es
 * par o impar.
 */
public class TagTablaListaRenglonProducto extends TagSupport {

	boolean bPintaRenglonColor = false;

	/**
	 * Método estandar de la interfaz de TagLibs, que es disparado por el contenedor
	 * de servlets al comenzar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como manejar el flujo
	 * de ejecución del TagLib y el contenido que pueda existir dentro del tag.
	 * @throws JspException Si hubo algún error durante la ejecución del TagLib.
	 */
	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			out.println();

			bPintaRenglonColor = ((Boolean)pageContext.getAttribute("PintaRenglonColor")).booleanValue();
			if (bPintaRenglonColor){
				out.println("\t\t\t\t\t\t\t\t<tr class=''>");
				pageContext.setAttribute("PintaRenglonColor", new Boolean(false));
			}
			else{
				out.println("\t\t\t\t\t\t\t\t<tr class=''>");
				pageContext.setAttribute("PintaRenglonColor", new Boolean(true));
			}
		}
		catch (IOException ex) {
		}
		return(EVAL_BODY_INCLUDE);
	}

	/**
	 * Método estándar de la interfaz de TagLibs, el cual es disparado por el contenedor
	 * de servlets al finalizar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como debe evaluar el resto
	 * de una página JSP una vez terminado el TagLib.
	 */
	public int doEndTag(){
		try {
			JspWriter out = pageContext.getOut();
			out.println();
			out.println("\t\t\t\t\t\t\t\t</tr>");
		}
		catch (IOException ex) {
		}
		return TagSupport.EVAL_PAGE;
	}
}