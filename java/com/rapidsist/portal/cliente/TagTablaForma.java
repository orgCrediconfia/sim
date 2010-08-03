/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import com.rapidsist.comun.bd.Registro;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import com.rapidsist.portal.configuracion.Permisos;

/**
 * Imprime el componente tabla forma.
 * <br><br>
 * Parámetros:
 * <ul>
 *	<li>
 * maestrodetallefuncion.- Clave de la función que representa la tabla. Indica que esta tabla
 * es parte de un página tipo maestro-detalle, por lo
 * que si se encuentra en modo de alta o inicialización (OperacionCatalogo=AL o IN) no se
 * mostrará el contenido de la tabla, así también si el usuario no tiene los sufientes
 * permisos no mostrará la información. Para que evaluación de los permisos sobre la clave
 * de función se ejecute, esta función deberá ser precargada en el tag Pagina. Este atributo
 * puede ir vació por lo que no se considerará la tabla como maestro-detalle.<br>
 *  </li>
 *	<li>
 * nombre.- Título de la tabla.
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
 * agregaentorno.- Indica si se agrega a la dirección url el parámetro de Ventana y el nombre
 * de la aplicación web. Este parametro se utiliza cuando deseamos ir a una dirección fuera de la
 * aplicación web en donde se ejecuta el portal.<br>
 * Valor por default: true.
 *  </li>
 * </ul>
 */
public class TagTablaForma extends TagSupport {

	int iRespuesta =0;
	String sMaestroDetalleFuncion ="";
	String sNombre="";
	String sFuncion="";
	String sOperacion="";
	String sFiltro="";
	String sParametros ="";
	String sAgregaentorno ="true";
	String sOperacionCatalogo = "";
	Permisos permisoFuncion = null;
	
	/**
	 * @param sMaestroDetalleFuncion Clave de la función que representa la tabla.
	 */
	public void setMaestrodetallefuncion(String sMaestroDetalleFuncion){
		this.sMaestroDetalleFuncion = sMaestroDetalleFuncion;
	}

	/**
	 * @param sNombre Título de la tabla.
	 */
	public void setNombre(String sNombre){
		this.sNombre = sNombre;
	}

	/**
	 * @param sFuncion Clave de la función.
	 */
	public void setFuncion(String sFuncion){
		this.sFuncion = sFuncion;
	}

	/**
	 * @param sOperacion Clave de la operación a ejecutar sobre el componente de catálogos.
	 */
	public void setOperacion(String sOperacion){
		this.sOperacion = sOperacion;
	}

	/**
	 * @param sFiltro Filtro de datos para el componente de catálogos.
	 */
	public void setFiltro(String sFiltro){
		this.sFiltro = sFiltro;
	}

	/**
	 * @param sParametros Parámetros extras para el componente de catálogos.
	 */
	public void setParametros(String sParametros){
		this.sParametros = sParametros;
	}

	/**
	 * @param sAgregaentorno 
	 */
	public void setAgregaentorno(String sAgregaentorno){
		this.sAgregaentorno = sAgregaentorno;
	}

	/**
	 * Método estándar de la interfaz de TagLibs, que es disparado por el contenedor
	 * de servlets al comenzar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como manejar el flujo
	 * de ejecución del TagLib y el contenido que pueda existir dentro del tag.
	 * @throws JspException Si hubo algún error durante la ejecución del TagLib.
	 */
	public int doStartTag() throws JspException {
		iRespuesta = TagSupport.EVAL_BODY_INCLUDE;
		String sRutaContexto = ((HttpServletRequest)pageContext.getRequest()).getContextPath();
		try {
			sOperacionCatalogo = pageContext.getRequest().getParameter("OperacionCatalogo");

			//VERIFICA EL TIPO DE TABLA Y LOS PERMISOS DE EJECUCION
			if (!sMaestroDetalleFuncion.equals("")){
				String sOperacionCatalogo =pageContext.getRequest().getParameter("OperacionCatalogo");
				//VERIFICA SI LA PAGINA TIENE EL ATRIBUTO OperacionCatalogo
				if (sOperacionCatalogo != null){
					//VERIFICA SI ES ALTA O INICILIZACION
					if (sOperacionCatalogo.equals("AL") || sOperacionCatalogo.equals("IN")){
						iRespuesta= TagSupport.SKIP_BODY;
					}
					else{
						Registro funcionesPrecargadas = (Registro)pageContext.getAttribute("PermisosFunciones");
						//VERIFICA SI ENCONTRO EL OBJETO DE FUNCIONES PRECARGADAS EN LA PAGINA
						if (funcionesPrecargadas != null){
							permisoFuncion = (Permisos) funcionesPrecargadas.getDefCampo(sMaestroDetalleFuncion);
							if (permisoFuncion != null){
								if (permisoFuncion.bConsulta == false) {
									iRespuesta = TagSupport.SKIP_BODY;
								}
							}
							else{
								iRespuesta = TagSupport.SKIP_BODY;
							}
						}
						else{
							iRespuesta= TagSupport.SKIP_BODY;
						}//VERIFICA SI ENCONTRO EL OBJETO DE FUNCIONES PRECARGADAS EN LA PAGINA
					}//VERIFICA SI ES ALTA O INICILIZACION
				}//VERIFICA SI LA PAGINA TIENE EL ATRIBUTO OperacionCatalogo
			}//VERIFICA EL TIPO DE TABLA Y LOS PERMISOS DE EJECUCION

			//VERIFICA SI SE PUEDE IMPRIMIR LA TABLA
			if (iRespuesta ==TagSupport.EVAL_BODY_INCLUDE){
				JspWriter out = pageContext.getOut();
				out.println("");
				out.println("");
				out.println("\t\t\t\t\t\t\t<!--TABLA FORMA DE CAPTURA-->");
				out.println("\t\t\t\t\t\t\t<form name=\"frmTablaForma\" method=\"post\" action=\"" + Url.getUrlProcesaCatalogo(this, pageContext, sFuncion, sOperacion, sFiltro, sParametros) + "\">");
				out.println("\t\t\t\t\t\t\t\t<h3>" + sNombre + "</h3>");
				out.println("\t\t\t\t\t\t\t<table border='0'>");
				pageContext.setAttribute("PintaRenglonColor", new Boolean(false));
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		return iRespuesta;
	}

	/**
	 * Método estándar de la interfaz de TagLibs, el cual es disparado por el contenedor
	 * de servlets al finalizar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como debe evaluar el resto
	 * de una página JSP una vez terminado el TagLib.
	 */
	public int doEndTag(){
		try {
			if (iRespuesta == TagSupport.SKIP_BODY){
				return TagSupport.EVAL_PAGE;
			}

			JspWriter out = pageContext.getOut();
			out.println();

			out.println("\t\t\t\t\t\t\t</form>");
			out.println("\t\t\t\t\t\t\t<br/>");

		}
		catch (IOException ex) {
			ex.printStackTrace();
		}

		return TagSupport.EVAL_PAGE;
	}
}