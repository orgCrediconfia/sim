/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;

/**
 * Clase utilitaria que proporciona métodos para la construcción de url's.
 */
public class Url{

	/**
	 * Agrega un parámetro a una dirección Url que recibe datos de un formulario, p.e. una forma
	 * de captura de información (jsp) que envía datos a un servlet.
	 * @param sUrl Dirección Url.
	 * @param sParametro Parámetro.
	 * @return Dirección Url con el parámetro agregado en la dirección.
	 */
	static public String agregaParametro(String sUrl, String sParametro){
		String sUrlFinal = sUrl;
		//VERIFICA QUE EL PARAMETRO NO SEA NULO
		if (sParametro != null){
			//VERIFICA QUE EL PARAMETRO NO ESTE VACIO
			if (!sParametro.equals("")){
				//BUSCA EN LA URL EL CARACTER "?", EL CUAL INDICA QUE LA URL POR LO MENOS
				//TIENE UN PARAMETRO ASIGNADO
				int iPrimero = sUrl.indexOf("?");
				//VERIFICA SI ENCONTRO EL CARACTER "?"
				if (iPrimero != -1) {
					//AGREGA EL PARAMETRO AL FINAL DE LA LISTA
					sUrlFinal = sUrl + "&" + sParametro;
				}
				else {
					//AGREGA EL PARAMETRO COMO EL INICIAL
					sUrlFinal = sUrl + "?" + sParametro;
				}
			}
		}
		return sUrlFinal;
	}

	/**
	 * Construye la dirección URL del servlet ProcesaCatalogo con sus parámetros de operación
	 * y los datos del entorno del usuario.
	 * @param tag TagLib que manda llamar esta clase (generalmente se llama desde el TagLib como
	 * 'this').
	 * @param pageContext Contexto de la página JSP.
	 * @param sFuncion Clave de la función.
	 * @param sOperacion Clave de operacion.
	 * @param sFiltro Filtro de búsqueda.
	 * @param sParametros Parámetros que se le envian al catálogo.
	 * @return Dirección URL
	 */
	public static String getUrlProcesaCatalogo(Tag tag, PageContext pageContext, String sFuncion, String sOperacion, String sFiltro, String sParametros){
		String sUrlFinal ="";
		try {
			String sRutaContexto = ( (HttpServletRequest) pageContext.getRequest()).getContextPath();

			//VERIFICA SI TIENE QUE AGREGAR AL FINAL DE LA URL EL PARAMETRO DE VENTANA
			String sParametroVentana = "";
			String sVentana = pageContext.getRequest().getParameter("Ventana");
			if (sVentana != null) {
				if (sVentana.equals("Si")) {
					sVentana = "&Ventana=Si";
				}
				else{
					sVentana = "";
				}
			}
			else {
				sVentana = "";
			}

			if (!sOperacion.equals("")){
				sOperacion = "&OperacionCatalogo=" + sOperacion;
			}
			if (!sFiltro.equals("")) {
				sFiltro = "&Filtro=" + sFiltro;
			}
			if (!sParametros.equals("")){
				String sParam = (String) ExpressionUtil.evalNotNull("out", "parametros", sParametros + "x", Object.class, tag, pageContext);
				sParametros = "&" + sParam.substring(0, sParam.length() - 1);
			}

			sUrlFinal = sRutaContexto + "/ProcesaCatalogo?Funcion=" + sFuncion + sOperacion + sFiltro + sParametros + sVentana;
		}
		catch (JspException ex) {
			ex.printStackTrace();
			sUrlFinal = "";
		}
		return sUrlFinal;
	}

	/**
	 * Le agrega a la dirección url el nombre de la aplicación web y el parámetro de Ventana
	 * si es que viene en el request de la pagina que procesa esta clase.
	 * @param tag TagLib que manda llamar esta clase (generalmente se llama desde el TagLib como
	 * 'this').
	 * @param pageContext Contexto de la página JSP.
	 * @param sUrl Dirección URL.
	 * @return Dirección URL configurada dependiendo del entorno del usuario.
	 */
	public static String url(Tag tag, PageContext pageContext, String sUrl){
		String sUrlFinal ="";

		try {
			String url = (String) ExpressionUtil.evalNotNull("out", "url", sUrl + "x", Object.class, tag, pageContext);
			url = url.substring(0, url.length() - 1);
			String sRutaContexto = ( (HttpServletRequest) pageContext.getRequest()).getContextPath();

			//VERIFICA SI TIENE QUE AGREGAR AL FINAL DE LA URL EL PARAMETRO DE VENTANA
			String sParametroVentana = "";
			String sVentana = pageContext.getRequest().getParameter("Ventana");
			if (sVentana != null) {
				if (sVentana.equals("Si")) {
					sVentana = "Ventana=Si";
				}
			}
			else {
				sVentana = "";
			}
			sUrlFinal = agregaParametro(sRutaContexto + url, sVentana);
		}
		catch (JspException ex) {
			ex.printStackTrace();
			sUrlFinal = "";
		}
		return sUrlFinal;
	}

}