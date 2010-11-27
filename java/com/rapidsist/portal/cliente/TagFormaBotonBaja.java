/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Permisos;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Coloca el botón de baja dentro de la forma de captura si el usuario
 * tiene ese permiso, y ejecuta automáticamente un código javascript que solicita al usuario
 * la confirmación de baja del registro; así también este tag proporciona
 * al programador la posibilidad de colocar código javascript dentro del tag el cual se ejecuta
 * al oprimir este botón.<br>
 * El código javascript se utilizará para realizar validaciones sobre los campos de
 * captura, por lo que el código introducido deberá tener la sentencia return si no se desea
 * ejecutar la rutina de confirmación de baja del registro. El código javascript ejecuta
 * primeramente el código introducido dentro de este tag y posteriormente ejecuta la rutina de
 * confirmación de baja.
 * <br><br>
 * Parámetros:
 * <ul>
 *	<li>
 * etiqueta.- Etiqueta del botón de baja.
 *  </li>
 *	<li>
 * clavemensaje.- Clave del mensaje que se envía al usuario para solicitar la confirmación
 * del baja.
 *  </li>
 * </ul>
 */
public class TagFormaBotonBaja extends TagSupport {
	int iRespuesta = TagSupport.SKIP_BODY;
	String sEtiqueta = "Baja";
	String sClavemensaje ="";
	Registro registro = null;
	Permisos permiso = null;

	public void setEtiqueta(String sEtiqueta){
		this.sEtiqueta = sEtiqueta;
	}

	public void setClavemensaje(String sClavemensaje){
		this.sClavemensaje = sClavemensaje;
	}

	/**
	 * Método estándar de la interfaz de TagLibs, que es disparado por el contenedor
	 * de servlets al comenzar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como manejar el flujo
	 * de ejecución del TagLib y el contenido que pueda existir dentro del tag.
	 * @throws JspException Si hubo algún error durante la ejecución del TagLib.
	 */
	public int doStartTag() throws JspException {
		try {
			registro = (Registro)pageContext.getRequest().getAttribute("registro");
			//VERIFICA SI VIENE EL OBJETO REGISTRO EN EL REQUEST
			if (registro != null){
				//VERFIICA SI EL USUARIO TIENE PERMISOS DE BAJA
				permiso = (Permisos)pageContext.getAttribute("permiso");
				if (permiso.bBaja) {
					iRespuesta = TagSupport.EVAL_BODY_INCLUDE;
					JspWriter out = pageContext.getOut();
					out.println();
					out.println("        <input type=\"button\" name=\"btnBaja\" value=\"" + sEtiqueta + "\" onClick=\"javascript:ValidaBaja();\" >&nbsp;");
					out.println("        <script languaje=\"javascript\">");
					out.println("          //*******************************************************************");
					out.println("          //COMFIRMA LA BAJA DEL REGISTRO");
					out.println("          //*******************************************************************");
					out.println("          function ValidaBaja() {");
					out.println("            var sResult=false;");
				}//VERIFICA SI TIENE PERMISO DE BAJA
			}//VERIFICA SI SE ENCUENTRA EL OBJETO REGISTRO EN EL REQUEST
		}
		catch (IOException ex) {
		}
		return(iRespuesta);
	}

	/**
	 * Método estándar de la interfaz de TagLibs, el cual es disparado por el contenedor
	 * de servlets al finalizar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como debe evaluar el resto
	 * de una página JSP una vez terminado el TagLib.
	 */
	public int doEndTag(){
		try {
			if (registro != null){
				if (permiso.bBaja) {	
					JspWriter out = pageContext.getOut();
					out.println("");
					out.println("            //VERIFICA SI EL USUARIO ACEPTO LA BAJA");
					out.println("            sResult=confirm('¿Desea borrar el registro?');");
					out.println("            //VERIFICA SI NO HAY NINGUNA CONDICION QUE IMPIDA DAR DE BAJA");
					out.println("            if (sResult) {");
					out.println("              document.frmRegistro.OperacionCatalogo.value=\"BA\";");
					out.println("              document.frmRegistro.submit();");
					out.println("            }");
					out.println("          }");
					out.println("        </script>");
				}//VERIFICA SI TIENE PERMISO DE BAJA
			}//VERIFICA SI SE ENCUENTRA EL OBJETO REGISTRO EN EL REQUEST
		}
		catch (IOException ex) {
		}
		return TagSupport.EVAL_PAGE;
	}
}