/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;
import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.apache.taglibs.standard.tag.common.core.NullAttributeException;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;

/**
 * Imprime un botón html dentro de una página.
 */
public class TagBoton extends OutSupport {

	String sTipo ="";
	String sNombre = "btnSubmit";
	String sEtiqueta= "";
	String sUrl= "";
	String sFuncion="";
	String sOperacion="";
	String sFiltro="";
	String sParametros ="";


	public TagBoton(){
		super();
	}

	/**
	 *
	 * @param sTipo
	 */
	public void setTipo(String sTipo){
		this.sTipo = sTipo;
	}

	/**
	 *
	 * @param sNombre
	 */
	public void setNombre(String sNombre){
		this.sNombre = sNombre;
	}

	/**
	 *
	 * @param sEtiqueta
	 */
	public void setEtiqueta(String sEtiqueta){
		this.sEtiqueta = sEtiqueta;
	}

	/**
	 *
	 * @param sUrl
	 */
	public void setUrl(String sUrl){
		this.sUrl = sUrl;
	}

	/**
	 *
	 * @param sFuncion
	 */
	public void setFuncion(String sFuncion){
		this.sFuncion = sFuncion;
	}

	/**
	 *
	 * @param sOperacion
	 */
	public void setOperacion(String sOperacion){
		this.sOperacion = sOperacion;
	}

	/**
	 *
	 * @param sFiltro
	 */
	public void setFiltro(String sFiltro){
		this.sFiltro = sFiltro;
	}

	/**
	 *
	 * @param sParametros
	 */
	public void setParametros(String sParametros){
		this.sParametros = sParametros;
	}

	/**
	 *
	 * @throws JspException Si hubo algún error durante la ejecución del TagLib.
	 */
	public int doStartTag() throws JspException {
		try {
			String etiqueta = "";
			if (!sEtiqueta.equals("")){
				etiqueta = (String) ExpressionUtil.evalNotNull("out", "etiqueta", sEtiqueta + "x", Object.class, this, pageContext);
				etiqueta = etiqueta.substring(0, etiqueta.length() - 1);
			}

			String sRutaContexto = ((HttpServletRequest)pageContext.getRequest()).getContextPath();

			//VERIFICA SI EL TAG ABRE UN CATALOGO
			if ( sTipo.equals("catalogo")){
				String funcion = (String)ExpressionUtil.evalNotNull("out", "funcion", sFuncion + "x", Object.class, this, pageContext);
				funcion = funcion.substring(0, funcion.length()-1);

				String operacion = (String)ExpressionUtil.evalNotNull("out", "operacion", sOperacion + "x", Object.class, this, pageContext);
				operacion = operacion.substring(0, operacion.length()-1);

				//VERIFICA SI SE ENVIO EL FILTRO DE BUSQUEDA
				if (!sFiltro.equals("")){
					String filtro = (String)ExpressionUtil.evalNotNull("out", "filtro", sFiltro + "x", Object.class, this, pageContext);
					filtro = filtro.substring(0, filtro.length()-1);
					sFiltro = "&Filtro=" + filtro;
				}
				//VERIFICA SI SE ENVIARON PARAMETROS PARA LA CONSULTA DEL CATALOGO
				if (!sParametros.equals("")) {
					String sP1 = (String) ExpressionUtil.evalNotNull("out", "parametros", sParametros + "x", Object.class, this, pageContext);
					sParametros = sP1.substring(0, sP1.length() - 1);
				}
				//VERIFICA SI TIENE QUE AGREGAR AL FINAL DE LA URL EL PARAMETRO DE VENTANA
				String sParametroVentana = "";
				String sVentana = pageContext.getRequest().getParameter("Ventana");
				if (sVentana != null) {
					if (sVentana.equals("Si")) {
						sVentana = "&Ventana=Si";
					}
				}
				else {
					sVentana = "";
				}

				value = "<input type=\"button\" name=\"btnOperaciones\" value=\"" + sEtiqueta + "\" onClick=\"MM_goToURL('parent','" + sRutaContexto + "/ProcesaCatalogo?Funcion=" + funcion + "&OperacionCatalogo=" + operacion + sFiltro + sParametros + sVentana + "')\" >";
			}
			else if (sTipo.equals("url")){
				String sDirUrl = (String)ExpressionUtil.evalNotNull("out", "url", sUrl + "x", Object.class, this, pageContext);
				sDirUrl = sDirUrl.substring(0, sDirUrl.length()-1);

				//VERIFICA SI TIENE QUE AGREGAR AL FINAL DE LA URL EL PARAMETRO DE VENTANA
				String sUrlFinal = "";
				String sVentana = pageContext.getRequest().getParameter("Ventana");
				if (sVentana != null) {
					if (sVentana.equals("Si")) {
						sVentana = "Ventana=Si";
						sUrlFinal = Url.agregaParametro(sRutaContexto + sDirUrl, sVentana);
					}
				}
				else {
					sUrlFinal = sRutaContexto + sDirUrl;
				}

				value = "<input type=\"button\" name=\"btnOperaciones\" value=\"" + sEtiqueta + "\" onClick=\"MM_goToURL('parent','" + sUrlFinal + "')\" >";
			}
			else if (sTipo.equals("submit")){
				value = "<input type=\"submit\" name=\"" + sNombre + "\" value=\"" + sEtiqueta + "\" />";
			}
		}
		catch (NullAttributeException ex) {
			value = null;
		}
		escapeXml = false;
		return super.doStartTag();
	}
}