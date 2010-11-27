/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.HttpServletRequest;

/**
 * Agrega el bloque de nombre de pagina a una JSP.
 * <br><br>
 * Parametros:
 * <ul>
 *	<li>
 * titulo.- Título de la página
 *  </li>
 *	<li>
 * subtitulo.- Subtítulo de la página
 *  </li>
 *	<li>
 * subtituloalta.- Subtítulo de la página si esta se llama con el parámetro
 * OperacionCatalogo=AL u OperacionCatalogo=IN
 *  </li>
 * </ul>
 */
public class TagPaginaNombre extends TagSupport {
	String sTitulo ="";
	String sSubtitulo= "";
	String sSubtituloalta= "";

	/**
	 * @param sTitulo Título de la página.
	 */
	public void setTitulo(String sTitulo){
		this.sTitulo = sTitulo;
	}

	/**
	 * @param sSubtitulo Subtitulo de la página.
	 */
	public void setSubtitulo(String sSubtitulo){
		this.sSubtitulo = sSubtitulo;
	}

	/**
	 * @param sSubtituloalta Titulo cuando se da la alta.
	 */
	public void setSubtituloalta(String sSubtituloalta){
		this.sSubtituloalta = sSubtituloalta;
	}

	/**
	 * Método estándar de la interfaz de TagLibs, que es disparado por el contenedor
	 * de servlets al comenzar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como manejar el flujo
	 * de ejecución del TagLib y el contenido que pueda existir dentro del tag.
	 * @throws JspException Si hubo algún error durante la ejecución del TagLib.
	 */
	public int doStartTag() throws JspException {
		try{
			String sRutaContexto = ((HttpServletRequest)pageContext.getRequest()).getContextPath();
			JspWriter out = pageContext.getOut();
			out.println();

			//VERIFICA SI SE DEBE DE PONER EL SUBTITULO CUANDO ES UN CATALOGO DE ALTA DE DATOS
			if (!sSubtituloalta.equals("")){
				String sOperacionCatalogo = ((HttpServletRequest)pageContext.getRequest()).getParameter("OperacionCatalogo");
				if (sOperacionCatalogo != null){
					if (sOperacionCatalogo.equals("AL") || sOperacionCatalogo.equals("IN")){
						sSubtitulo = sSubtituloalta;
					}
				}
			}
			getDiseño3(out);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return(TagSupport.SKIP_BODY);
	}

	/**
	 * Obtiene el Diseño1
	 * @param out Objeto tipo JspWriter.
	 * @param sRutaContexto Ruta de contecto.
	 * @throws java.lang.Exception Si hubo algún error en la clase controladora.
	 */
	public void getDiseño1(JspWriter out, String sRutaContexto) throws Exception{
		/*
		out.println("<div align='right'> <span class='TituloPagina'>" + sTitulo + "</span></div>");
		out.println("<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
		out.println("<tr>");
		out.println("<TD height='2' background='img/DivisorTitPagina.png'><img src='img/transparent.gif'></TD>");
		out.println("</tr>");
		out.println("</table>");
		out.println("<div align='right'> <span class='Titulo1'>" + sSubtitulo + "</span></div>");
		out.println("<br>");
		out.println("<br>");
		*/
		out.println("<table width='100%' border='0' cellPadding=0 cellSpacing=0>");
		out.println("<tr>");
		out.println("<td align='right'>");
		out.println("<TABLE width='30%' border=0 cellPadding=0 cellSpacing=0>");
		out.println("<TBODY>");
		out.println("<tr>");
		out.println("<TD vAlign=bottom align=right width=9 height=7><IMG height=7 hspace=0 src='" + sRutaContexto + "/comun/img/set1/bar-top-left.gif' width=9 border=0></TD>");
		out.println("<TD nowrap background='" + sRutaContexto + "/comun/img/set1/bar-border-top.gif'><IMG height=5 hspace=0 src='" + sRutaContexto + "/comun/img/set1/void.gif' width=1 border=0></TD>");
		out.println("<TD vAlign=bottom align=left width=9 height=7><IMG height=7 hspace=0 src='" + sRutaContexto + "/comun/img/set1/bar-top-right.gif' width=9 border=0></TD>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<TD width=9 background='" + sRutaContexto + "/comun/img/set1/bar-border-left.gif'><IMG height=1 hspace=0 src='" + sRutaContexto + "/comun/img/set1/void.gif' width=9 border=0></TD>");
		out.println("<TD width='100%' align='center' nowrap bgColor=#0086b2 class=TituloPagina2><IMG height=5 hspace=0 src='" + sRutaContexto + "/comun/img/set1/void.gif' width=5 border=0>" + sTitulo + "</TD>");
		out.println("<TD width=9 background='" + sRutaContexto + "/comun/img/set1/bar-border-right.gif'><IMG height=1 hspace=0 src='" + sRutaContexto + "/comun/img/set1/void.gif' width=9 border=0></TD>");
		out.println("</TR>");
		out.println("<TR>");
		out.println("<TD vAlign=top align=right width=9 height=12><IMG height=12 hspace=0 src='" + sRutaContexto + "/comun/img/set1/bar-bottom-left.gif' width=9 border=0></TD>");
		out.println("<TD nowrap background='" + sRutaContexto + "/comun/img/set1/bar-border-bottom.gif'><IMG height=12 hspace=0 src='" + sRutaContexto + "/comun/img/set1/void.gif' border=0></TD>");
		out.println("<TD vAlign=top align=left width=9 height=12><IMG height=12 hspace=0 src='" + sRutaContexto + "/comun/img/set1/bar-bottom-right.gif' width=9 border=0></TD>");
		out.println("</TR>");
		out.println("</TBODY>");
		out.println("</TABLE>");
		out.println("</td>");
		out.println("<td width='5' nowrap>&nbsp;</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td align='right'>");
		out.println("<table width='30%' border='0' cellpadding='0' cellspacing='0'>");
		out.println("<tr>");

		//VERIFICA SI SE DEBE DE PONER EL SUBTITULO CUANDO ES UN CATALOGO DE ALTA DE DATOS
		if (!sSubtituloalta.equals("")){
			String sOperacionCatalogo = ((HttpServletRequest)pageContext.getRequest()).getParameter("OperacionCatalogo");
			if (sOperacionCatalogo != null){
				if (sOperacionCatalogo.equals("AL") || sOperacionCatalogo.equals("IN")){
					sSubtitulo = sSubtituloalta;
				}
			}
		}
		out.println("<td align='center' class='Subtitulo' nowrap>" + sSubtitulo + "</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</td>");
		out.println("<td width='5' nowrap>&nbsp;</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("<br>");
	}
	
	/**
	 * Obtiene el Diseño2.
	 * @param out Objeto tipo JspWriter.
	 * @throws java.lang.Exception Si hubo algún error en la clase controladora.
	 */

	public void getDiseño2(JspWriter out) throws Exception{
		out.println("    <!--NOMBRE DE LA PAGINA -->");
		out.println("    <table border=0 width='100%'>");
		out.println("      <tr>");
		out.println("        <td width='100%'>&nbsp;</td>");
		out.println("        <td align='center'>");
		out.println("          <table><tr><td nowrap>");
		out.println("             <h1 class='newsheading'><span class='newsdate'>");
		out.println("             &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + sTitulo + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;</h1>");
		out.println("          </td></tr></table>");
		out.println("        </td>");
		out.println("        <td nowrap width='30'>&nbsp;</td>");
		out.println("      </tr>");
		out.println("      <tr>");
		out.println("        <td >&nbsp;</td>");
		out.println("        <td align='center' nowrap>");
		out.println("          " + sSubtitulo);
		out.println("        </td>");
		out.println("        <td nowrap width='30'>&nbsp;</td>");
		out.println("      </tr>");
		out.println("    </table>");
		out.println("    <br>");
		out.println("    <br>");
	}

	/**
	 * Otiene le Diseño3.
	 * @param out Objeto tipo JspWriter.
	 * @throws java.lang.Exception Si hubo algún error en la clase controladora.
	 */
	public void getDiseño3(JspWriter out) throws Exception{
		out.println("\t\t\t\t\t\t<!--NOMBRE DE LA PAGINA -->");
		out.println("\t\t\t\t\t\t<h2>" + sTitulo + "<br/><em>" + sSubtitulo + "</em></h2>");
		out.println();
		out.println("\t\t\t\t\t\t<!--DIVISOR PROYECTO -->");
		out.println();
	}

}