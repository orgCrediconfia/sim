/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import com.rapidsist.comun.bd.Registro;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import com.rapidsist.portal.configuracion.Permisos;

/**
 * Permite la ejecución del código contenido dentro del taglib si el usuario
 * tiene los permisos especificados para la función. Para que este tag busque los permisos de
 * la función, esta se deberá precargar utilizando el tag Pagina.
 * <br><br>
 * Parametros:
 * <ul>
 *	<li>
 * funcion.- Clave de la función.
 *  </li>
 *	<li>
 * operacion.- Tipo de operación a verificar sobre la función<br>
 * Posibles valores: consulta, modificacion, alta, baja
 *  </li>
 * </ul>
 */
public class TagPermiso extends TagSupport {
	private String sIdFuncion = "";
	private String sOperacion = "";

	/**
	 * Asigna al TagLib la función que debe verificar permisos.
	 * @param sIdFuncion Clave de función.
	 */
	public void setFuncion(String sIdFuncion){
		this.sIdFuncion = sIdFuncion;
	}

	/**
	 * Asigna la operación que se realiza sobre la función.
	 * @param sOperacion Clave de la operación a realizar ('consulta', 'modificacion', 'alta', 'baja').
	 */
	public void setOperacion(String sOperacion){
		this.sOperacion = sOperacion;
	}

	/**
	 * Método estandar de la interfaz de TagLibs, que es disparado por el contenedor
	 * de servlets al comenzar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como manejar el flujo
	 * de ejecución del TagLib y el contenido que pueda existir dentro del tag.
	 * @throws JspException Si hubo algún error durante la ejecución del TagLib.
	 */
	public int doStartTag() throws JspException {
		int iRespuesta = TagSupport.SKIP_BODY;

		Registro funcionesPrecargadas = (Registro)pageContext.getAttribute("PermisosFunciones");

		//VERIFICA SI ENCONTRO EL OBJETO DE FUNCIONES PRECARGADAS EN LA PAGINA
		if (funcionesPrecargadas != null){
			Permisos permisoFuncion = (Permisos) funcionesPrecargadas.getDefCampo(this.sIdFuncion);

			//VERIFICA SI ENCONTRO LOS PERMISOS DE LA FUNCION
			if (permisoFuncion != null) {
				//VERIFICA SI TIENEN PERMISO DE CONSULTA
				if (sOperacion.equals("consulta")) {
					if (permisoFuncion.bConsulta) {
						iRespuesta = TagSupport.EVAL_BODY_INCLUDE;
					}
				}
				//VERIFICA SI TIENEN PERMISO DE MODIFICACION
				else if (sOperacion.equals("modificacion")) {
					if (permisoFuncion.bModificacion) {
						iRespuesta = TagSupport.EVAL_BODY_INCLUDE;
					}
				}
				//VERIFICA SI TIENEN PERMISO DE BAJA
				else if (sOperacion.equals("baja")) {
					if (permisoFuncion.bBaja) {
						iRespuesta = TagSupport.EVAL_BODY_INCLUDE;
					}
				}
				//VERIFICA SI TIENEN PERMISO DE ALTA
				else if (sOperacion.equals("alta")) {
					if (permisoFuncion.bAlta) {
						iRespuesta = TagSupport.EVAL_BODY_INCLUDE;
					}
				}
			} //VERIFICA SI ENCONTRO LOS PERMISOS DE LA FUNCION
		}//VERIFICA SI ENCONTRO EL OBJETO DE FUNCIONES PRECARGADAS EN LA PAGINA
		return(iRespuesta);
	}

	/**
	 * Método estándar de la interfaz de TagLibs, el cual es disparado por el contenedor
	 * de servlets al finalizar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como debe evaluar el resto
	 * de una página JSP una vez terminado el TagLib.
	 */
	public int doEndTag(){
		return TagSupport.EVAL_PAGE;
	}
}