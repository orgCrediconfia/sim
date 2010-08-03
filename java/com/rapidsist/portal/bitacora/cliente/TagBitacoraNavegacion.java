package com.rapidsist.portal.bitacora.cliente;

/**
 * <p>Title: Bitácora</p>
 * <p>Description: Permite consultar los cambios ocurridos a un registro en el tiempo por el XML que se almacena en la bitácora</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: RapidSist, S.A. de C.V</p>
 * @author Luis Albeto Vázquez, Javier Sánchez
 * @version 1.0
 */

import java.util.*;
import java.text.SimpleDateFormat;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.comun.bd.Registro;


/**
 * Taglib que permite la navegación entre los diferentes registros de la bitacora
 */
public class TagBitacoraNavegacion extends TagSupport {
  String sNumeroPagina = "";
  String sNumeroRegistrosPagina = "";
  String sNumeroSetPaginas = "";

  /**
   * @param sNumeroPagina Numero de pagina solicitada para mostrar.
   */
  public void setNumeropagina(String sNumeroPagina) {
	this.sNumeroPagina = sNumeroPagina;
  }

  /**
   * @param sNumeroSetPaginas Numero de set de paginas solicitada para mostrar.
   */
  public void setNumerosetpaginas(String sNumeroSetPaginas) {
	this.sNumeroSetPaginas = sNumeroSetPaginas;
  }

  /**
   * @param sNumeroRegistrosPagina Total de registros que puede consultar
   */
  public void setNumeroregistrospagina(String sNumeroRegistrosPagina) {
	this.sNumeroRegistrosPagina = sNumeroRegistrosPagina;
  }

  /**
   * Atributo que controla el color de la tag lib
   * @param color Color que utiliza la Bitacora
   */
  private String color = "#FFFFFF";
  public void setColor(String color) {
	this.color = color;
  }
  public String getColor() {
	return color;
  }

  /**
   * @throws JspException Se genero un error al ejecutar la Tag
   */
  public int doStartTag() throws JspException{
	try{
	JspWriter out = pageContext.getOut();
	int iTotalRegistros = 0;
	int iRegistroInicial = 0;
	int iRegistroFinal = 0;
	int iTotalPaginas = 0;
	String sNomTabla = "";
	int iPaginaActual = 1;
	int iregistrospagina = 5;

	// SE OBTIENE EL USUARIO DE LA SESSIÓN
	Usuario usuario = (Usuario)pageContext.getSession().getAttribute("Usuario");

	//SE OBTIENE EL NUMERO TOTAL DE REGISTROS QUE TIENE LA LISTA
	Registro registroParametros = (Registro)pageContext.getSession().getAttribute("RegistroParametros");
	if (registroParametros!=null){
		iTotalRegistros = Integer.parseInt((String)registroParametros.getDefCampo("TOTAL_REGISTROS"));
		sNomTabla = (String)registroParametros.getDefCampo("CVE_TABLA");
	}
	iPaginaActual =  (int)Float.valueOf((String)pageContext.getRequest().getParameter("NumeroPagina")).floatValue();
	//SE OBTIENE EL NUMERO DE PAGINA SOLICITADA
	//SI LA PAGINA TIENE UN VALOR DE NULO O BLANCO PONE 1 DE OTRA FORMA PONE EL VALOR SOLICITADO
	int iNumeroPaginaSolicitada = Integer.parseInt(this.sNumeroPagina!=null ? ( this.sNumeroPagina.equals("") ? "1" : this.sNumeroPagina ) : "1"  );


	//SE OBTIENE EL RANGO DE LOS REGISTROS A CONSULTAR
	if(pageContext.getRequest().getParameter("NumeroRegistrosPagina")!= null){
		iregistrospagina = Integer.parseInt(pageContext.getRequest().getParameter("NumeroRegistrosPagina")); //ESTE ES UN VALOR QUE MAS ADELANTE PUEDE SER DINAMICO
	}

	// SE OBTIENE EL NÚMERO TOTAL DE PÁGINAS QUE SE PUEDEN DESPLEGAR EN LA JSP
	double dTotalPaginas = (iTotalRegistros % iregistrospagina) != 0 ? ((iTotalRegistros / iregistrospagina) +1) : iTotalRegistros / iregistrospagina;
	String sTotalPaginas = String.valueOf(dTotalPaginas);
	int iPunto = sTotalPaginas.indexOf(".");
	sTotalPaginas = sTotalPaginas.substring(0,iPunto);
	iTotalPaginas = (int)Float.valueOf(sTotalPaginas).floatValue();

	// POR DEFAULT SE INICIALIZA EN 1 EL NÚMERO DE SETS
	int iNumeroTotalSetPaginas = 1;

	// SE RECUPERA EL NÚMERO ACTUAL DE SET DE PÁGINAS, POR DEFAULT ES 1
	int iNumeroActualSetPaginas = 0;
	if(!sNumeroSetPaginas.equals("")){
		iNumeroActualSetPaginas = (int) Float.valueOf(sNumeroSetPaginas).floatValue();
	}

	// SE CALCULA EL NÚMERO TOTAL DE SETS DE PÁGINAS A MOSTRAR
	if(iTotalPaginas > 10){
		double dTotalSetPaginas = (iTotalPaginas % 10) != 0 ? ((iTotalPaginas / 10) +1) : iTotalPaginas / 10;
		String sTotalSetPaginas = String.valueOf(dTotalSetPaginas);
		iPunto = sTotalSetPaginas.indexOf(".");
		sTotalSetPaginas = sTotalSetPaginas.substring(0, iPunto);
		iNumeroTotalSetPaginas = (int) Float.valueOf(sTotalSetPaginas).floatValue();
	}
	//SE OBTIENE EL RANGO DE REGISTROS QUE SE DEBEN MOSTRAR ES 1 POR DEFAULT
	iRegistroInicial = (iNumeroPaginaSolicitada * iregistrospagina) - (iregistrospagina - 1);

	// SE CALCULA EL REGISTRO FINAL QUE SE DESPLEGARÁ EN PANTALLA
	if ( iRegistroInicial >= iTotalRegistros || ( ( iRegistroInicial + (iregistrospagina - 1 ) ) >= iTotalRegistros ) ){
		iRegistroFinal = iTotalRegistros;
	}
	else {
		iRegistroFinal = iRegistroInicial + ( iregistrospagina - 1 );
	}

	  //FECHA DE SISTEMA
	  Date dFecha = new Date();
	  SimpleDateFormat formatter1 = new SimpleDateFormat ("dd/MM/yyyy hh:mm");
	  String sFecha = formatter1.format(dFecha);

	  //LOGICA DE NAVEGACION
	  int a = 1;
	  int b = 5;


	  //SOLO SI EL TOTAL DE REGISTROS ES IGUAL O MENOR AL TOTAL DE REGISTROS POR PAGINA
	  if (iTotalRegistros <= iregistrospagina) {
		  b = iTotalRegistros;
		  pageContext.getOut().println("<br>");
		  pageContext.getOut().println("<form name=\"bitacora\">");
		  pageContext.getOut().println("        <table width=\"900\" border=\"0\">");
		  pageContext.getOut().println("          <tr> ");
		  pageContext.getOut().println("            <td width =\"200\" nowrap><strong>Tabla</strong></td>");
		  pageContext.getOut().println("            <td width =\"200\" nowrap>  " + sNomTabla + "</td>");
		  pageContext.getOut().println("          </tr>");
		  pageContext.getOut().println("          <tr> ");
		  pageContext.getOut().println("            <td width =\"200\" nowrap><strong>Fecha y hora de consulta</strong></td>");
		  pageContext.getOut().println("            <td width =\"200\" nowrap>   " + sFecha + "</td>");
		  pageContext.getOut().println("            <td width =\"200\" nowrap>Registros por p&aacute;gina <select name=\"registroPaginas\" onchange=\"location.href = '/portal/ConsultaBitacora?RefrescaDatos=NO&Pagina=1&NumSet=0&Ventana=Si&NumRegPorPag='+this.options[this.selectedIndex].text\">");
		  pageContext.getOut().println("                                            <option value=\"5\">5 ");
		  pageContext.getOut().println("                                            <option value=\"10\">10 ");
		  pageContext.getOut().println("                                            <option value=\"15\">15 ");
		  pageContext.getOut().println("                                            </select> </td> ");
		  pageContext.getOut().println("          </tr>");
		  pageContext.getOut().println("          <tr> ");
		  pageContext.getOut().println("            <td width =\"50\" nowrap><strong>P&aacute;ginas</strong></td>");
		  pageContext.getOut().println("            <td width =\"50\" nowrap>  " + sTotalPaginas + "</td>");
		  pageContext.getOut().println("            <td width =\"200\" nowrap>Registros " + a + " - " + b + " de " + iTotalRegistros + "</td>");
		  pageContext.getOut().println("          </tr>");
		  pageContext.getOut().println("        </table>");
		  pageContext.getOut().println("   </form>");
		  pageContext.getOut().println("   <script>");
		  pageContext.getOut().println("   BuscaSelectOpcion(document.bitacora.registroPaginas, " + iregistrospagina + ");");
		  pageContext.getOut().println("   </script>");
		  pageContext.getOut().println("<br>");
	  }
	  else {
		  //VALIDACIONES PARA LA ULTIMA PAGINA
		  if (iPaginaActual == iTotalPaginas) {
			  b = iTotalRegistros;
			  int c = iTotalPaginas * iregistrospagina;
			  int d = c - iTotalRegistros;
			  a = c - iregistrospagina + 1;
			  // SI EL TOTAL DE REGISTROS ES UN NUMERO PAR
			  if (iTotalRegistros == c) {
				  a = iTotalRegistros - iregistrospagina + 1;
			  }
		  }

		  if (iPaginaActual == 1) {
			  a = 1;
			  b = iregistrospagina;
		  }
		  else {
			  //VALIDACION DE NAVEGACION Y CALCULO DE REGISTROS
			  if (iPaginaActual < iTotalPaginas) {
				  int c = iTotalPaginas * iregistrospagina;
				  int d = c - iTotalRegistros;
				  b = iPaginaActual * c / iTotalPaginas;
				  a = b - iregistrospagina + 1;
			  }
		  }

		  // SE CONSTRUYE EL ENCABEZADO
		  int paginaant = iPaginaActual - 1;
		  int paginasig = iPaginaActual + 1;
		  int iSetSig = iNumeroActualSetPaginas + 1;
		  int iSetAnt = iNumeroActualSetPaginas - 1;
		  int iUltimoSet = iNumeroTotalSetPaginas - 1;
		  pageContext.getOut().println("<br>");
		  pageContext.getOut().println("<form name=\"bitacora\">");
		  pageContext.getOut().println("        <table width=\"900\" border=\"0\">");
		  pageContext.getOut().println("          <tr> ");
		  pageContext.getOut().println("            <td width =\"200\" nowrap><strong>Tabla</strong></td>");
		  pageContext.getOut().println("            <td width =\"200\" nowrap>" + sNomTabla + "</td>");
		  pageContext.getOut().println("          </tr>");
		  pageContext.getOut().println("          <tr> ");
		  pageContext.getOut().println("            <td width =\"200\" nowrap><strong>Fecha y hora de consulta</strong></td>");
		  pageContext.getOut().println("            <td width =\"200\" nowrap>   " + sFecha + "</td>");
		  pageContext.getOut().println("            <td width =\"200\" nowrap>Registros por p&aacute;gina <select name=\"registroPaginas\" onchange=\"location.href = '/portal/ConsultaBitacora?RefrescaDatos=NO&Pagina=1&NumSet=0&Ventana=Si&NumRegPorPag='+this.options[this.selectedIndex].text\">");
		  pageContext.getOut().println("                                            <option value=\"5\">5 ");
		  pageContext.getOut().println("                                            <option value=\"10\">10 ");
		  pageContext.getOut().println("                                            <option value=\"15\">15 ");
		  pageContext.getOut().println("                                            </select> </td> ");
		  pageContext.getOut().println("          </tr>");
		  pageContext.getOut().println("        </table>");


		  pageContext.getOut().println("        <table width=\"300\" border=\"0\">");
		  pageContext.getOut().println("          <tr> ");
		  pageContext.getOut().println("            <td width =\"50\" nowrap><strong>P&aacute;ginas</strong></td>");

		  // SE HACEN LOS CÁLCULOS PARA CONSTRUIR LAS LIGAS DE LAS PÁGINAS
		  int iPaginaInicio = iNumeroActualSetPaginas != 0 ? (iNumeroActualSetPaginas * 10) + 1 : 1;
		  int iPaginaFinal = ( (iPaginaInicio + 9) > iTotalPaginas) ? iTotalPaginas : iPaginaInicio + 9;
		  for (int i = iPaginaInicio; i <= iPaginaInicio + 9; i++) {
			  if (i <= iPaginaFinal) {
				  if (i != iPaginaActual) {
					  pageContext.getOut().println("            <td width =\"10\" nowrap><a href=/portal/ConsultaBitacora?RefrescaDatos=NO&Pagina=" + i + "&Ventana=Si&NumRegPorPag=" + this.sNumeroRegistrosPagina + "&NumSet=" + iNumeroActualSetPaginas + ">" + i + "</a></td>");
				  }
				  else {
					  pageContext.getOut().println("            <td width =\"10\" nowrap>" + i + "</td>");
				  }
			  }
			  else {
				  pageContext.getOut().println("            <td width =\"10\" nowrap>&nbsp;&nbsp;</td>");
			  }
		  }
		  pageContext.getOut().println("          </tr>");
		  pageContext.getOut().println("        </table>");
		  pageContext.getOut().println("        <table width=\"300\" border=\"0\">");
		  pageContext.getOut().println("          <tr> ");

		  //VALIDACIONES PARA EL CONTEO DE PÁGINAS
		  int iPaginaConteoInicio = iNumeroActualSetPaginas > 0 ? (iNumeroActualSetPaginas * 10) + 1 : 1;
		  int iPaginaConteoFinal = iNumeroActualSetPaginas == iNumeroTotalSetPaginas - 1 ? iTotalPaginas : iPaginaConteoInicio + 9;

		  //  VALIDACIONES PARA DETERMINAR LA PAGINA SIGUIENTE Y ANTERIOR
		  int iPaginaSetSig = 1;
		  int iPaginaSetAnt = 1;
		  int iPaginaUltimoSet = (iNumeroTotalSetPaginas * 10) - 9;

		  // SE CONSTRUYEN LAS LIGAS PARA NAVEGAR ENTRE LOS SETS DE PÁGINAS
		  if (iNumeroActualSetPaginas == 0 && iNumeroTotalSetPaginas > 1) {
			  iPaginaSetSig = iNumeroActualSetPaginas != 0 ? (iNumeroActualSetPaginas * 10) + 11 : 11;
			  pageContext.getOut().println("            <td width =\"80\" nowrap><font color='#999999'>Primero &nbsp;|&nbsp; </td>");
			  pageContext.getOut().println("            <td width =\"80\" nowrap><font color='#999999'>Anterior &nbsp;|&nbsp; </td>");
			  pageContext.getOut().println("            <td width =\"90\" nowrap><a href=/portal/ConsultaBitacora?RefrescaDatos=NO&Pagina=" + iPaginaSetSig + "&Ventana=Si&NumRegPorPag=" + this.sNumeroRegistrosPagina + "&NumSet=" + iSetSig + ">Siguiente</a> &nbsp;|&nbsp;</td>");
			  pageContext.getOut().println("            <td width =\"90\" nowrap><a href=/portal/ConsultaBitacora?RefrescaDatos=NO&Pagina=" + iPaginaUltimoSet + "&Ventana=Si&NumRegPorPag=" + this.sNumeroRegistrosPagina + "&NumSet=" + iUltimoSet + ">Último</a></td>");
			  pageContext.getOut().println("            <td width =\"200\" nowrap>Páginas " + iPaginaConteoInicio + " - " + iPaginaConteoFinal + " de " + iTotalPaginas + "</td>");
			  pageContext.getOut().println("            <td width =\"200\" nowrap>Registros " + a + " - " + b + " de " + iTotalRegistros + "</td>");
		  }
		  else {
			  if (iNumeroActualSetPaginas == 0 && iNumeroTotalSetPaginas == 1) {
				  pageContext.getOut().println("            <td width =\"200\" nowrap>Páginas " + iPaginaConteoInicio + " - " + iPaginaConteoFinal + " de " + iTotalPaginas + "</td>");
				  pageContext.getOut().println("            <td width =\"200\" nowrap>Registros " + a + " - " + b + " de " + iTotalRegistros + "</td>");
			  }
			  else {
				  if (iNumeroActualSetPaginas > 0 && iNumeroActualSetPaginas < iNumeroTotalSetPaginas - 1) {
					  iPaginaSetAnt = (iNumeroActualSetPaginas * 10) - 9;
					  iPaginaSetSig = iNumeroActualSetPaginas != 0 ? (iNumeroActualSetPaginas * 10) + 11 : 11;
					  pageContext.getOut().println("            <td width =\"80\" nowrap><a href=/portal/ConsultaBitacora?RefrescaDatos=NO&Pagina=1&Ventana=Si&NumRegPorPag=" + this.sNumeroRegistrosPagina + "&NumSet=0>Primero</a> &nbsp;|&nbsp;</td>");
					  pageContext.getOut().println("            <td width =\"80\" nowrap><a href=/portal/ConsultaBitacora?RefrescaDatos=NO&Pagina=" + iPaginaSetAnt + "&Ventana=Si&NumRegPorPag=" + this.sNumeroRegistrosPagina + "&NumSet=" + iSetAnt + ">Anterior</a> &nbsp;|&nbsp;</td>");
					  pageContext.getOut().println("            <td width =\"90\" nowrap><a href=/portal/ConsultaBitacora?RefrescaDatos=NO&Pagina=" + iPaginaSetSig + "&Ventana=Si&NumRegPorPag=" + this.sNumeroRegistrosPagina + "&NumSet=" + iSetSig + ">Siguiente</a> &nbsp;|&nbsp;</td>");
					  pageContext.getOut().println("            <td width =\"90\" nowrap><a href=/portal/ConsultaBitacora?RefrescaDatos=NO&Pagina=" + iPaginaUltimoSet + "&Ventana=Si&NumRegPorPag=" + this.sNumeroRegistrosPagina + "&NumSet=" + iUltimoSet + ">Último</a></td>");
					  pageContext.getOut().println("            <td width =\"200\" nowrap>Páginas " + iPaginaConteoInicio + " - " + iPaginaConteoFinal + " de " + iTotalPaginas + "</td>");
					  pageContext.getOut().println("            <td width =\"200\" nowrap>Registros " + a + " - " + b + " de " + iTotalRegistros + "</td>");
				  }
				  else {

					  iPaginaSetAnt = (iNumeroActualSetPaginas * 10) - 9;
					  pageContext.getOut().println("            <td width =\"80\" nowrap><a href=/portal/ConsultaBitacora?RefrescaDatos=NO&Pagina=1&Ventana=Si&NumRegPorPag=" + this.sNumeroRegistrosPagina + "&NumSet=0>Primero</a> &nbsp;|&nbsp;</td>");
					  pageContext.getOut().println("            <td width =\"80\" nowrap><a href=/portal/ConsultaBitacora?RefrescaDatos=NO&Pagina=" + iPaginaSetAnt + "&Ventana=Si&NumRegPorPag=" + this.sNumeroRegistrosPagina + "&NumSet=" + iSetAnt + ">Anterior</a> &nbsp;|&nbsp;</td>");
					  pageContext.getOut().println("            <td width =\"90\" nowrap>Siguiente &nbsp;|&nbsp;</td>");
					  pageContext.getOut().println("            <td width =\"90\" nowrap>Último</td>");
					  pageContext.getOut().println("            <td width =\"200\" nowrap>Páginas " + iPaginaConteoInicio + " - " + iPaginaConteoFinal + " de " + iTotalPaginas + "</td>");
					  pageContext.getOut().println("            <td width =\"200\" nowrap>Registros " + a + " - " + b + " de " + iTotalRegistros + "</td>");
				  }
			  }
		  }
		  pageContext.getOut().println("          </tr>");
		  pageContext.getOut().println("        </table>");
		  pageContext.getOut().println("   </form>");
		  pageContext.getOut().println("   <script>");
		  pageContext.getOut().println("   BuscaSelectOpcion(document.bitacora.registroPaginas, " + iregistrospagina + ");");
		  pageContext.getOut().println("   </script>");
		  pageContext.getOut().println("<br>");
	  }
  }
	catch(Exception e){
	  e.printStackTrace();
	}
	return javax.servlet.jsp.tagext.TagSupport.SKIP_BODY;
  }
  public int doEndTag(){
	return javax.servlet.jsp.tagext.TagSupport.EVAL_PAGE;
  }
}
