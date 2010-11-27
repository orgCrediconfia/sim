/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import java.util.Iterator;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.HttpServletRequest;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.comun.bd.Registro;

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
public class TagSelectorAplicaciones extends TagSupport {

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
			Usuario usuario = (Usuario)pageContext.getSession().getAttribute("Usuario");
			JspWriter out = pageContext.getOut();
			out.println();
			out.println("\t\t\t<table class='tabs'>");
			out.println("\t\t\t\t<tr>");
			out.println("\t\t\t\t\t<td id='Usuario'>Bienvenido: " + usuario.sNomCompleto  + "</td>");
			if (usuario.listaAplicacionesSelector != null){
				Iterator lista = usuario.listaAplicacionesSelector.iterator();
				while (lista.hasNext()){
					Registro aplicacion = (Registro)lista.next();
					String sCveAplicacion = (String)aplicacion.getDefCampo("CVE_APLICACION");
					String sNomAplicacion = (String)aplicacion.getDefCampo("NOM_APLICACION");
					//VERIFICA SI SELECCIONA LA APLICACION ACTUAL DEL USUARIO
					if (sCveAplicacion.equals(usuario.sAplicacionActual)){
						out.println("\t\t\t\t\t<th nowrap><a href='" + sRutaContexto + "/CambiaAplicacion?CveAplicacion=" + sCveAplicacion + "'>" + sNomAplicacion + "</a></th>");
					}
					else{
						out.println("\t\t\t\t\t<td nowrap><a href='" + sRutaContexto + "/CambiaAplicacion?CveAplicacion=" + sCveAplicacion + "'>" + sNomAplicacion + "</a></td>");
					}
				}
			}
			out.println("\t\t\t\t</tr>");
			out.println("\t\t\t</table>");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return(TagSupport.SKIP_BODY);
	}
}