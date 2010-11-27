/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import com.rapidsist.portal.configuracion.Permisos;
import com.rapidsist.portal.configuracion.Usuario;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Coloca el botón de alta-modificación dentro de la forma de captura si el usuario
 * tiene los permisos de alta o modificación según sea el caso; así también este tag proporciona
 * al programador la posibilidad de colocar código javascript dentro del tag el cual se ejecuta
 * al oprimir este botón.<br>
 * El código javascript se utilizará para realizar validaciones sobre los campos de
 * captura, por lo que el código introducido deberá tener la sentencia return si no se desea
 * ejecutar el submit de la forma. El código javascript ejecuta automáticamente como paso inicial
 * la rutina de validación de campos obligatorios y como segunda operación ejecuta el código
 * introducido dentro de este tag.
 * <br><br>
 * Parámetros:
 * <ul>
 *	<li>
 * etiqueta.- Etiqueta del botón de alta-modificación.
 *  </li>
 * </ul>
 */
public class TagFormaBotonAltaModificacion extends TagSupport {
	
	int iRespuesta = TagSupport.EVAL_BODY_INCLUDE;
	String sEtiqueta = "Aceptar";
	String sIdIntegrante1="";
	String sIdIntegrante2="";
	String sIdUsuario="";
	String sNota="";
	
	public void setEtiqueta(String sEtiqueta){
		this.sEtiqueta = sEtiqueta;
	}
	public void setIdintegrante1(String sIdIntegrante1){
		this.sIdIntegrante1 = sIdIntegrante1;
	}

	public void setIdintegrante2(String sIdIntegrante2){
		this.sIdIntegrante2 = sIdIntegrante2;
	}

	public void setIdusuario(String sIdUsuario){
		this.sIdUsuario     = sIdUsuario;
	}
	
	public void setNota(String sNota){
		this.sNota = sNota;
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
			
			
			//OBTIENE LOS PERMISOS DE LA FUNCION
			iRespuesta = TagSupport.EVAL_BODY_INCLUDE;
			Permisos permiso = (Permisos) pageContext.getAttribute("permiso");
			
			//OBTIENE EL USUARIO
			Usuario usuario = (Usuario)pageContext.getSession().getAttribute("Usuario");
			
			//OBTIENE EL PARAMETRO OPERACION-CATALOGO
			String sOperacionCatalogo = pageContext.getRequest().getParameter("OperacionCatalogo");
			if (sOperacionCatalogo != null){
				if (sOperacionCatalogo.equals("AL") || sOperacionCatalogo.equals("IN")){
					if (!permiso.bAlta){
						iRespuesta = TagSupport.SKIP_BODY;
					}

				}
				else{
					if (!permiso.bModificacion){
						iRespuesta = TagSupport.SKIP_BODY;
					}
				}
			}
			else{
				iRespuesta = TagSupport.SKIP_BODY;
			}
			
			if (iRespuesta == TagSupport.EVAL_BODY_INCLUDE){
				JspWriter out = pageContext.getOut();
				out.println();
				
				out.println("        <input type=\"button\" name=\"btnOperaciones\" value=\"" + sEtiqueta + "\" onClick=\"javascript:ValidaPagina();\" >&nbsp;");
				
				// SI VA A VALIDAR LAS FIRMAS ELECTRÓNICAS GENERA UN INPUT ID_BITACORA QUE DEVOLVERÁ A LA CLASE CON
				if((sIdIntegrante1 != null && !sIdIntegrante1.equals("")) || (sIdUsuario != null && !sIdUsuario.equals(""))){
					out.println("<input type='hidden' name='IdBitacora' value='' >" );
				}
				
				// SE DEFINE EL MÉTODO VALIDA PÁGINA
				out.println("        <script languaje=\"javascript\">");
				out.println("          function ValidaPagina() {");
				out.println("            //COMPRUEBA QUE LOS CAMPOS OBLIGATORIOS SEAN CAPTURADOS");
				out.println("            bExito = fRevisaCampos();");
				out.println("            if (!bExito){");
				out.println("              return;");
				out.println("            }");
				
				// VE SI VERIFICA SI SE VALIDARÁN LAS FIRMAS ELECTRÓNICAS
				if(sIdIntegrante1 != null && !sIdIntegrante1.equals("")){
					
					out.println("MM_openBrWindow('/portal/ProcesaCatalogo?Funcion=SimGeneralesFirmaElectronica&OperacionCatalogo=CR&IdIntegrante1="+
						"'+ document.frmRegistro.IdIntegranteAutoriza.value+'"+"&IdIntegrante2=" + "'+ document.frmRegistro.IdIntegranteRecibe.value+'" + "&Ventana=Si&ValidaIntegrante1=SI&TxNota="+ sNota + "&IdUsuario="+
						usuario.sCveUsuario +  "&Url=' + window.location " +
						",'VentanaCp','scrollbars=yes,resizable=yes,width=500,height=300');");
					// SI ES SOLO EL USUARIO QUE SE VALIDARÁ SE MANDA SOLO EL PARÁMETRO ID_USUARIO
				}else if(sIdUsuario != null && !sIdUsuario.equals("")){
					out.println("MM_openBrWindow('/portal/ProcesaCatalogo?Funcion=SimGeneralesFirmaElectronica&OperacionCatalogo=CR&IdUsuario="+
					usuario.sCveUsuario + "&Ventana=Si&ValidaUsuario=SI&TxNota="+ sNota + "&Url=' + window.location,'VentanaCp','scrollbars=yes,resizable=yes,width=500,height=300');");
				}
				
			}
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
			if (iRespuesta == TagSupport.EVAL_BODY_INCLUDE){
				JspWriter out = pageContext.getOut();
				out.println("");
				
				// SI SE VALIDAN LAS FIRMAS ELECTRÓNICAS EL SUBMIT SE MANEJA EN LA JSP DE LA VENTANA QUE SE ABRE
				if(sIdIntegrante1.equals("") && sIdUsuario.equals("")){
					out.println("            document.frmRegistro.submit();");
				}
				out.println("      		}");
				out.println("        </script>");
			}
		}
		catch (IOException ex) {
		}
		return TagSupport.EVAL_PAGE;
	}
}