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
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;

/**
 * Imprime una dirección URL en una página HTML con la confiugración del portal del usuario.
 * <br><br>
 * Parámetros:
 * <ul>
 *	<li>
 * tipo.- Tipo de procesamiento que deberá tener la forma dirección URL especificiada en los
 * parámetros.<br>
 * Posibles valores: url (Coloca la dirección URL especificada en el parámetro url en la página
 * html), catalogo (Se presenta en la página una referencia con el tag "a"  y se llama al
 * componente de catálogos si la ventana no fue llamada con el parametro Ventana, de lo
 * contrario se hace una llamada a la funcion RegresaDatos, el cual regresa el control
 * a la ventana padre que hizo la llamada), catalogo-ventana (se abre en una nueva página
 * el catálogo).
 *  </li>
 *	<li>
 * url.- Dirección URL (este parámetro solo es utilizado si se emplea el parámetro tipo='url')
 *  </li>
 *	<li>
 * nombreliga.- Nombre de la liga que aparece en la página.
 *  </li>
 *	<li>
 * funcion.- Clave de la función (este parámetro solo es utilizado si se emplea el parámetro
 * tipo='catalogo')
 *  </li>
 *	<li>
 * operacion.- Clave de la operación a ejecutar sobre el componente de catálogos.(este parámetro
 *  solo es utilizado si se emplea el parámetro tipo='catalogo')
 *  </li>
 *	<li>
 * filtro.- Filtro de datos para el componente de catálogos (este parámetro
 *  solo es utilizado si se emplea el parámetro tipo='catalogo')
 *  </li>
 *	<li>
 * parametros.- Parámetros extras para el componente de catálogos (este parámetro
 *  solo es utilizado si se emplea el parámetro tipo='catalogo')
 *  </li>
 *	<li>
 * parametrosregreso.- Parámetros que se regresan a la ventana padre (solo se utiliza si la
 * página es llamada utilizando el parametro Ventana)
 *  </li>
 *	<li>
 * clonarregistro.- Indica que debe pintar junto a la liga el icono de clonación de registro
 * (este parámetro solo es utilizado si se emplea el parámetro tipo='catalogo'). <br/>
 * Posibles valores: true, false.
 *  </li>
 * </ul>
 */
public class TagUrl extends TagSupport {
	String sTipo = "";
	String sUrl ="";
	String sNombreLiga ="";
	String sFuncion="";
	String sOperacion="";
	String sFiltro="";
	String sParametros ="";
	String sParametrosregreso ="";
	String sNomventana= "ventana";
	boolean bClonarRegistro = false;

	/**
	 * @param sTipo Tipo de procesamiento que deberá tener la forma dirección URL especificiada en los
	 * parámetros.
	 */
	public void setTipo(String sTipo){
		this.sTipo = sTipo;
	}
	
	/**
	 * @param sUrl Dirección URL.
	 */
	public void setUrl(String sUrl){
		this.sUrl = sUrl;
	}

	/**
	 * @param sOperacion Clave de la operación a ejecutar sobre el componente de catálogos.
	 */
	public void setOperacion(String sOperacion){
		this.sOperacion = sOperacion;
	}

	/**
	 * @param sFuncion Clave de la función.
	 */
	public void setFuncion(String sFuncion){
		this.sFuncion = sFuncion;
	}

	/**
	 * @param sFiltro Filtro de datos para el componente de catálogos.
	 */
	public void setFiltro(String sFiltro){
		this.sFiltro = sFiltro;
	}

	/**
	 * @param sLiga Nombre de la liga que aparece en la página.
	 */
	public void setNombreliga(String sLiga){
		this.sNombreLiga = sLiga;
	}

	/**
	 * @param sParametros Parámetros extras para el componente de catálogos.
	 */
	public void setParametros(String sParametros){
		this.sParametros = sParametros;
	}

	/**
	 * @param sParametrosregreso Parámetros que se regresan a la ventana padre.
	 */
	public void setParametrosregreso(String sParametrosregreso){
		this.sParametrosregreso = sParametrosregreso;
	}

	/**
	 * @param sNomventana Nombre de la ventana.
	 */
	public void setNomventana(String sNomventana){
		this.sNomventana = sNomventana;
	}

	/**
	 * @param sClonarRegistro Indica que debe pintar junto a la liga el icono de clonación de registro.
	 */
	public void setClonarregistro(String sClonarRegistro){
		this.bClonarRegistro = sClonarRegistro.equals("true") ? true : false;
	}

	/**
	 * Método estandar de la interfaz de TagLibs, que es disparado por el contenedor
	 * de servlets al comenzar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como manejar el flujo
	 * de ejecución del TagLib y el contenido que pueda existir dentro del tag.
	 * @throws JspException Si hubo algún error durante la ejecución del TagLib.
	 */
	public int doStartTag() throws JspException {
		try{

			String sRutaContexto = ((HttpServletRequest)pageContext.getRequest()).getContextPath();
			String sUrlFinal = "";
			String sParam = "";

			if (!sNombreLiga.equals("")){
				String sParamNomLiga = (String) ExpressionUtil.evalNotNull("out", "nombreliga", sNombreLiga + "x", Object.class, this, pageContext);
				sNombreLiga = sParamNomLiga.substring(0, sParamNomLiga.length() - 1);
			}

			JspWriter out = pageContext.getOut();
			//VERIFICA SI EL TIPO ES URL
			if (sTipo.equals("url")){
				out.print( "<a href=\"" + Url.url(this, pageContext, sUrl) + "\">" + sNombreLiga + "</a>");
			}
			else if (sTipo.equals("catalogo")){
				String sVentana = ((HttpServletRequest)pageContext.getRequest()).getParameter("Ventana");
				//VERIFICA SI SE ENVIA A LA PAGINA EL PARAMETRO VENTANA
				if (sVentana != null){
					if (sVentana.equals("Si")) {
						//EVALUAMOS LOS PARAMETROS QUE SE REGRESAN A LA VENTANA PADRE
						sParam = (String) ExpressionUtil.evalNotNull("out", "parametrosregreso", sParametrosregreso + "x", Object.class, this, pageContext);
						sParametrosregreso = sParam.substring(0, sParam.length() - 1);
						sUrlFinal = "<a href=\"javascript:RegresaDatos(" + sParametrosregreso + ")\">" + sNombreLiga + "</a>";
					}
				}

				//VERIFICA SI NO SE ENVIO EL PARAMETRO VENTANA A LA PAGINA JSP.
				if (sUrlFinal.equals("")){
					sUrlFinal = "<a href=\"" + Url.getUrlProcesaCatalogo(this, pageContext, sFuncion, sOperacion, sFiltro, sParametros) + "\">" + sNombreLiga + "</a>";
					//VERIFICA SI CLONA EL REGISTRO
					if (bClonarRegistro){
						sUrlFinal = "<a href=\"" + Url.getUrlProcesaCatalogo(this, pageContext, sFuncion, "CL", sFiltro, sParametros) + "\"><img class='ClonarRegistroImg'></a>" + " " + sUrlFinal;
					}
				}
				out.print(sUrlFinal);
			}
			else if (sTipo.equals("catalogo-ventana")){
				String sUrlTemp = Url.url(this, pageContext, sUrl);
				sUrlFinal = "<a href=\"javascript:MM_openBrWindow('" + Url.agregaParametro(sUrlTemp, "Ventana=Si") + "','" + sNomventana + "','scrollbars=yes,resizable=yes,width=1000,height=500') \">" + sNombreLiga + "</a>";
				out.print(sUrlFinal);
			}
			else if (sTipo.equals("regresa-datos")){
				String sVentana = ((HttpServletRequest)pageContext.getRequest()).getParameter("Ventana");
				//VERIFICA SI SE ENVIA A LA PAGINA EL PARAMETRO VENTANA
				if (sVentana != null){
					if (sVentana.equals("Si")) {
						//EVALUAMOS LOS PARAMETROS QUE SE REGRESAN A LA VENTANA PADRE
						sParam = (String) ExpressionUtil.evalNotNull("out", "parametrosregreso", sParametrosregreso + "x", Object.class, this, pageContext);
						sParametrosregreso = sParam.substring(0, sParam.length() - 1);
						sUrlFinal = "<a href=\"javascript:RegresaDatos(" + sParametrosregreso + ")\">" + sNombreLiga + "</a>";
					}
				}
				else{
					sUrlFinal = sNombreLiga;
				}
				out.print(sUrlFinal);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return(TagSupport.SKIP_BODY);
	}
}