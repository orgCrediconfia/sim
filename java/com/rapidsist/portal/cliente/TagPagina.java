/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.configuracion.Permisos;
import com.rapidsist.portal.configuracion.PortalSLHome;
import com.rapidsist.portal.configuracion.PortalSL;
import com.rapidsist.comun.bd.Registro;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.StringTokenizer;
import javax.rmi.PortableRemoteObject;

/**
 * Taglib que valida la sesion del usuario, pinta el template html para la página JSP y verifica
 * si el usuario tiene permisos para ejecutar la página.
 * <br><br>
 * Parametros:
 * <ul>
 *	<li>
 * funcion.- Clave de la función.
 *  </li>
 *	<li>
 * menu.- Banderá que indica si se imprime la barra de menú<br>
 * Posibles valores: true, false.
 *  </li>
 *	<li>
 * precarga.- Clave de las funciones de las que se deberán precargar sus correspondientes
 * permisos de ejecución con respecto al usuario. La funciones se deberán separar por
 * espacios.
 *  </li>
 *  <li>
 * clavepagina.- Clave de la pagina JSP que se deberá bitacorar.
 *  </li>
 *  <li>
 * urlestilo.- Url de la pagina de estilo de la pagina JSP
 *  </li>
 * </ul>
*/

public class TagPagina extends TagSupport {

	private String sCveFuncion = "";
	private String sImprimeMenu = "true";
	private String sPrecargaFunciones = "";
	private Usuario usuario = null;
	boolean bVentana = false;
	boolean bSaltaPagina = false;
	private String sCvePagina = "";
	private String sUrlEstilo = "";

	/**
	 * @param sCveFuncion Clave de la función
	 */
	public void setFuncion(String sCveFuncion){
		this.sCveFuncion = sCveFuncion;
	}

	/**
	 * Indica si se debe imprimir el menú de la aplicación.
	 * @param sMenu "true" o "false".
	 */
	public void setMenu(String sMenu){
		this.sImprimeMenu = sMenu;
	}

	/**
	 * Precarga los datos de la funciones indicadas.
	 * @param sPrecargaFunciones Lista de claves de funciones (separados por comas).
	 */
	public void setPrecarga(String sPrecargaFunciones){
		this.sPrecargaFunciones = sPrecargaFunciones;
	}

	/**
	 * Indica la pagina que se va a bitacorar.
	 * @param sCvePagina Clave de la pagina.
	 */
	public void setClavepagina(String sCvePagina){
		this.sCvePagina = sCvePagina;
	}

	/**
	 * Indica la url de la pagina de estilo.
	 * @param sUrlEstilo Clave de la pagina.
	 */
	public void setUrlestilo(String sUrlEstilo){
		this.sUrlEstilo = sUrlEstilo;
	}
	
	
	/**
	 * Método estándar de la interfaz de TagLibs, que es disparado por el contenedor
	 * de servlets al comenzar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como manejar el flujo
	 * de ejecución del TagLib y el contenido que pueda existir dentro del tag.
	 * @throws JspException Si hubo algún error durante la ejecución del TagLib.
	 */
	public int doStartTag() throws JspException {
		int iEvaluaCuerpo = TagSupport.EVAL_BODY_INCLUDE;
		try{
			//SE VERIFICARA SI HA CADUCADO LA SESION DEL USUARIO
			HttpSession session = SesionUsuario.compruebaSesionUsuario((HttpServletRequest)pageContext.getRequest(), (HttpServletResponse)pageContext.getResponse());
			if (session == null){
				iEvaluaCuerpo = TagSupport.SKIP_BODY;
			}
			else{
				//OBTENEMOS LOS DATOS DE LA SESION DEL USUARIO
				usuario=(Usuario)pageContext.getSession().getAttribute("Usuario");
				String sVentana = pageContext.getRequest().getParameter("Ventana");

				if (sVentana != null){
					if (sVentana.equals("Si")){
						bVentana = true;
					}
					else{
						bVentana = false;
					}
				}
				else{
					bVentana = false;
				}
				String sRutaContexto = ((HttpServletRequest)pageContext.getRequest()).getContextPath();

				//OBTIENE EL CONTEXTO DEL SERVIDOR DE APLICACIONES
				Context context = new InitialContext();
				Object referencia = context.lookup("java:comp/env/ejb/PortalSL");
				PortalSLHome portalHome = (PortalSLHome)PortableRemoteObject.narrow(referencia, PortalSLHome.class);
				PortalSL configuracion = portalHome.create();				
				
				//VERIFICA SI SE PASO A ESTA PAGINA LA CLAVE DE LA FUNCION
				//PARA VALIDAR SEGURIDAD
				if ( ! sCveFuncion.equals("")){
					//OBTIENE LOS PERMISOS PARA LA PAGINA
					Permisos permiso = configuracion.validaAccesoFuncion(usuario.sAplicacionActual, usuario.sCvePerfilActual, sCveFuncion);
					//VERIFICA SI NO EXISTEN PERMISOS
					if (permiso == null){
						((HttpServletResponse)pageContext.getResponse()).sendRedirect(sRutaContexto + usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=" + MensajeSistema.getMensajeSistema(100));
						iEvaluaCuerpo = TagSupport.SKIP_BODY;
					}
					else{
						//HACE DISPONIBLE LA LISTA DE PERMISOS EN LA PAGINA JSP
						pageContext.setAttribute("permiso", permiso);
					}
				}//VERIFICA ACCESO A FUNCION

				if ( ! sCvePagina.equals("")){
				   //BITACORA LA VISITA DEL USUARIO A LA PAGINA
				   configuracion.almacenaPagina(usuario.sCveGpoEmpresa, usuario.sCveUsuario, sCvePagina);
				}

				//AGREGA LA CLAVE DE LA FUNCION A LA SESION DE LA PAGINA PARA QUE OTROS COMPONENTES
				//PUEDAN CONOCER LA FUNCION DE LA PAGINA.
				pageContext.setAttribute("CveFuncion", sCveFuncion);
				
				//VERIFICA SI SE DEBEN PRECARGAR LOS PERMISOS DE FUNCIONES
				if (!sPrecargaFunciones.equals("")){

					Registro funcionesPrecargadas = null;
					//RECORRE LA LISTA DE FUNCIONES A PRECARGAR
					StringTokenizer st = new StringTokenizer(sPrecargaFunciones);
					while (st.hasMoreTokens()) {
						String sFuncion = st.nextToken();
						//BUSCA EN LA BASE DE DATOS LA FUNCION
						Permisos permisoFuncion = configuracion.validaAccesoFuncion(usuario.sAplicacionActual, usuario.sCvePerfilActual, sFuncion);
						if (permisoFuncion != null){
							if (funcionesPrecargadas == null){
								funcionesPrecargadas = new Registro();
							}
							funcionesPrecargadas.addDefCampo(sFuncion, permisoFuncion);
						}
					}

					//AGREGA LA LISTA DE FUNCIONES PRECARGADAS A LA SESION DE LA PAGINA
					if (funcionesPrecargadas != null){
						pageContext.setAttribute("PermisosFunciones", funcionesPrecargadas);
					}

				}//PRECARGA PERMISOS

				if (iEvaluaCuerpo != SKIP_BODY){
					JspWriter out = pageContext.getOut();

					//CONSTRUYE EL HEADER
					//out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
					out.println("<!DOCTYPE html PUBLIC \"-//Tigris//DTD XHTML 1.0 Transitional//EN\"");
					out.println("\"http://style.tigris.org/tigris_transitional.dtd\">");
					out.println("<html>");
					out.println("\t<head>");
					out.println("\t\t<title>" + usuario.sNombreVentanaBrowser + "</title>");
					out.println("\t\t<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'/>");
					out.println("\t\t<meta http-equiv=\"Content-style-type\" content=\"text/css\" />");
					out.println("\t\t<meta name=\"version\" content=\"Rapidsist 1.0\" />");
					out.println("\t\t<meta name=\"keywords\" content=\"\" />");
					out.println("\t\t<meta name=\"description\" content=\"Descripcion del sitio\" />");
					if (sUrlEstilo.equals("")){
						out.println("\t\t<link rel='stylesheet' type='text/css' href='" + sRutaContexto + usuario.sUrlHojaEstilo +"' media='screen' />");
					}
					else{
						out.println("\t\t<link rel='stylesheet' type='text/css' href='" + sRutaContexto + sUrlEstilo +"' media='screen' />");
					}
					out.println("\t\t<script language='javascript' src='" + sRutaContexto + "/comun/lib/bBibliotecas.js' ></script>");
					
					out.println("\t</head>");
					if (sImprimeMenu.equals("true") && !bVentana){
						out.println("\t<body marginwidth='0' marginheight='0' onload='fPosicionaPie();'>");
					}
					else{
						out.println("\t<body marginwidth='0' marginheight='0' class='composite'>");
					}
					//out.println("\t\t<table height='100%' width='100%'><tr><td>");


					//INCLUYE PAGINA DE ENCABEZADO
					if (!bVentana){
						pageContext.include(usuario.sUrlEncabezado);
					}
					//out.println("\t\t</td></tr><tr><td>");
					out.println("\t\t<table cellspacing='0' cellpadding='4' width='100%' id='main'>");
					out.println("\t\t\t<tr valign='top'>");

					//INCLUYE PAGINA DE MENU APLICACION
					if (!sImprimeMenu.equals("false")){
						if (!bVentana) {
							pageContext.include(usuario.sUrlMenuAplicacion);
						}
					}

					out.println();
					pageContext.include(usuario.sUrlContenidoInicio);
					out.println();
				}
			}//sesion == null
		}
		catch(Exception e){
			e.printStackTrace();
		}
		if (iEvaluaCuerpo == TagSupport.SKIP_BODY){
			bSaltaPagina = true;
		}
		return(iEvaluaCuerpo);
	}

	/**
	 * Método estándar de la interfaz de TagLibs, el cual es disparado por el contenedor
	 * de servlets al finalizar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como debe evaluar el resto
	 * de una página JSP una vez terminado el TagLib.
	 */
	public int doEndTag(){
		int iEvaluaSeccion = TagSupport.EVAL_PAGE;
		try{
			if (bSaltaPagina){
				iEvaluaSeccion = TagSupport.SKIP_PAGE;
			}
			else{

				if (!bVentana) {
					JspWriter out = pageContext.getOut();
					pageContext.include(usuario.sUrlContenidoFin);
					out.println("\t\t\t</tr>");
					out.println("\t\t</table>");
					//out.println("\t\t</td></tr><tr><td>");
					pageContext.include(usuario.sUrlPiePagina);
					//out.println("\t\t</td></tr><table>");
					out.println("\t</body>");
					out.println("</html>");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return iEvaluaSeccion;
	}
}