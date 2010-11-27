/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.publicaciones.cliente;

import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.publicaciones.datos.PublicacionSL;
import com.rapidsist.publicaciones.datos.PublicacionSLHome;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.http.HttpServletRequest;

/**
 * Imprime una tabla html que muestra el resultado de una consulta.
 * <br><br>
 * Parametros:
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
 * tipo.- Tipo de tabla a pintar.<br>
 * Posibles valores: Alta (Coloca el botón de alta si el usuario tiene permisos de alta
 * para la función.), consulta (Suprime la región para los botones).
 *  </li>
 *	<li>
 * botontipo.- Define la operación que se ejecuta al pintar la barra de botones de la tabla.<br>
 * Posibles valores: catalogo (ejecuta el componente de catálogos)
 *  </li>
 *	<li>
 * nombre.- Título de la tabla.
 *  </li>
 *	<li>
 * funcion.- Clave de la función (este parámetro solo es utilizado si se emplea el parámetro
 * botontipo='catalogo')
 *  </li>
 *	<li>
 * operacion.- Clave de la operación a ejecutar sobre el componente de catálogos.(este parámetro
 *  solo es utilizado si se emplea el parámetro botontipo='catalogo')
 *  </li>
 *	<li>
 * filtro.- Filtro de datos para el componente de catálogos (este parámetro
 *  solo es utilizado si se emplea el parámetro botontipo='catalogo')
 *  </li>
 *	<li>
 * parametros.- Parámetros extras para el componente de catálogos (este parámetro
 *  solo es utilizado si se emplea el parámetro botontipo='catalogo')
 *  </li>
 * </ul>
 */
public class TagPublTablaSeccion
	extends TagSupport {

	String orientacionvertical = "";
	String presentacionjerarquica = "";
	String consultatodo = "";
	String manipularpresentacion = "";
	String manipularconsulta = "";

	public void setOrientacionvertical(String sOrientacionVertical) {
		this.orientacionvertical = sOrientacionVertical;
	}

	public void setPresentacionjerarquica(String sPresentacionJerarquica) {
		this.presentacionjerarquica = sPresentacionJerarquica;
	}

	public void setManipularpresentacion(String sManipularPresentacion) {
		this.manipularpresentacion = sManipularPresentacion;
	}

	public void setManipularconsulta(String sManipularConsulta) {
		this.manipularconsulta = sManipularConsulta;
	}

	public void setConsultatodo(String sConsultaTodo) {
		this.consultatodo = sConsultaTodo;
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
			JspWriter out = pageContext.getOut();
			//OBTIENE EL CONTEXTO DEL SERVIDOR DE APLICACIONES
			Context context = new InitialContext();
			Object referencia = context.lookup("java:comp/env/ejb/PublicacionSL");
			PublicacionSLHome publicacionHome = (PublicacionSLHome) PortableRemoteObject.narrow(referencia, PublicacionSLHome.class);
			PublicacionSL publicacion = publicacionHome.create();
			String sMenuCuerpo = "";
			String sMenuExpandible = "";
			//******************************************************************************************
			 //*** DETERMINA SI LA BUSQUEDA SE HACE PARA TODAS LAS EMPRESAS DEL GRUPO O SOLO PARA UNA ***
			  //******************************************************************************************
			boolean bJerarquica = (pageContext.getRequest().getParameter("chkbJerarquica") == null ? false : true);
			bJerarquica = true;
			//DETERMINA SI LA PRESENTACIÓN DE LAS PUBLICACIONES ES DE MANERA LINEAL O CON SU ESTRUCTURA
			boolean bConsultaTodo = (pageContext.getRequest().getParameter("chkbConsultaTodo") == null ? false : true);
			//FORMA LA CADENA CON LA TABLA SECCIÓN DE BÚSQUEDA
			//SI EN EL TAGLIB SE INDICA QUE SE DESEA MANIPULAR LA CONSULTA, SE DEBE MOSTRAR LA TABLA
			String sTablaBusquedaForma = "";
			if (manipularpresentacion.equals("true") || manipularconsulta.equals("true")) {
				sTablaBusquedaForma += " <table width=\"90%\" border=\"0\"> \n ";
				sTablaBusquedaForma += " <form name=\"frmPublicaciones\" method=\"post\" action=\"PequesCampeones.jsp\"> \n";
			}
			if (manipularpresentacion.equals("true")) {
				sTablaBusquedaForma += "    <tr> \n";
				sTablaBusquedaForma += "       <td>Presentar estructura: <input type=\"checkbox\" name=\"chkbJerarquica\" value=\"checkbox\" " + ( (bJerarquica == true) ? "checked" : "") + "></td> \n";
				sTablaBusquedaForma += "    </tr> \n";
			}
			if (manipularconsulta.equals("true")) {
				sTablaBusquedaForma += "    <tr> \n";
				sTablaBusquedaForma += "       <td>Todas las empresas: <input type=\"checkbox\" name=\"chkbConsultaTodo\" value=\"checkbox\" " + ( (bConsultaTodo == true) ? "checked" : "") + "></td> \n";
				sTablaBusquedaForma += "    </tr> \n";
			}
			if (manipularpresentacion.equals("true") || manipularconsulta.equals("true")) {
				sTablaBusquedaForma += "    <tr> \n";
				sTablaBusquedaForma += "       <input type=\"submit\" name=\"btnConsultaPublicaciones\" value=\"Consultar\"> \n";
				sTablaBusquedaForma += "    </tr> \n";
				sTablaBusquedaForma += "</form> \n";
			}

			//***********************************************
			 //*** SE DETERMINA SI EL USUARIO ESTA FIRMADO ***
			  //***********************************************
			   boolean bUsuarioFirmado = false; //VARIABLE QUE INDICA SI EL USUARIO ESTA FIRMADO
			Usuario usuario = (Usuario) pageContext.getSession().getAttribute("Usuario");
			//NO EXISTE EL OBJETO usuario
			if (usuario == null) {
				//NO EXISTE EL OBJETO usuario PORQUE NO SE HA FIRMADO NI UNA VEZ AL PORTAL
				//SE MUESTRAN PUBLICACIONES PUBLICAS
				bUsuarioFirmado = false;
			}
			//SI EXISTE EL OBJETO usuario
			else {
				//EL USUARIO ESTA FIRMADO AL PORTAL Y SU SESION ESTA ACTIVA
				//SE MUESTRAN PUBLICACIONES PUBLICAS Y PRIVADAS
				if (usuario.bValidado) {
					bUsuarioFirmado = true;
				}
				else {
					//EL USUARIO ESTA FIRMADO AL PORTAL Y SU SESION NO ESTA ACTIVA
					//SE MUESTRAN PUBLICACIONES PUBLICAS
					bUsuarioFirmado = false;
				}
			}

			//*******************************************************
			 //*** SE DETERMINA EL ENCABEZADO Y EL PIE DE LA TABLA ***
			  //*******************************************************
			   String sJavaScript = "<script language=\"JavaScript\"> \n" +
				   "function AbreVentanaPublicaciones(theURL,winName,features, bPublicaciones) { //v2.0 \n" +
				   "   if (bPublicaciones=='true'){ \n" +
				   "      window.open(theURL,winName,features); \n" +
				   "   } \n" +
				   "} \n" +
				   "function AbreSeccionNuevaVentana(theURL,winName,features) { //v2.0 \n" +
				   "   window.open(theURL,winName,features); \n" +
				   "} \n" +

				   " img1=new Image(); \n"+
				   " img1.src=\""+((HttpServletRequest)pageContext.getRequest()).getContextPath() +"/comun/img/publicaciones/FolderCerrado.gif\"; \n"+
				   " sImagenFolderCerrado = \""+((HttpServletRequest)pageContext.getRequest()).getContextPath() +"/comun/img/publicaciones/FolderCerrado.gif\"; \n"+
				   " img2=new Image(); \n"+
				   " img2.src=\""+((HttpServletRequest)pageContext.getRequest()).getContextPath() +"/comun/img/publicaciones/FolderAbierto.gif\"; \n"+
				   " sImagenFolderAbierto = \""+((HttpServletRequest)pageContext.getRequest()).getContextPath() +"/comun/img/publicaciones/FolderAbierto.gif\"; \n"+

				   "</script> \n" +
				   "<script language='javascript' src='" + ((HttpServletRequest)pageContext.getRequest()).getContextPath() + "/comun/lib/bPublicaciones.js'></script>";

			String sMenuEncabezado =
				"\t\t\t\t\t\t<div class='MenuAplicacionGrupo'>\n" +
				"\t\t\t\t\t\t\t<div class='MenuAplicacionTitulo'>\n" +
				"\t\t\t\t\t\t\t\t<strong>Publicaciones</strong>\n" +
				"\t\t\t\t\t\t\t</div>\n" +
				"\t\t\t\t\t\t<div class='MenuAplicacionGrupo'>\n";
			sMenuEncabezado += "\t\t\t\t\t\t\t<div class='MenuPublicacionItems'>\n";
			String sMenuPiePagina = "\t\t\t\t\t\t\t</div>\n" +
				"\t\t\t\t\t\t</div><!--MenuAplicacionGrupo -->\n";
			if (bUsuarioFirmado) {
				sMenuPiePagina +=
					"\t\t\t\t\t\t<div class='MenuAplicacionGrupo'>\n" +
					"\t\t\t\t\t\t\t<div class='MenuAplicacionTitulo'>\n" +
					"\t\t\t\t\t\t\t\t<strong><a href='/portal/ProcesaCatalogo?Funcion=PublicacionesSolicitud&OperacionCatalogo=IN&Filtro=Todos'>Solicitar publicar </a></strong>\n" +
					"\t\t\t\t\t\t\t</div>\n";
			}
			
			
			Registro parametros = new Registro();
			//PARAMETRO QUE SE REQUIERE PARA PODER OBTENER LAS PROPIEDADES DEL USUARIO
			parametros.addDefCampo("CVE_GPO_EMPRESA", usuario.sCveGpoEmpresa);
			//PARAMETRO QUE SE REQUIERE PARA PODER OBTENER LAS PROPIEDADES DEL USUARIO
			parametros.addDefCampo("CVE_PORTAL", usuario.sCvePortal);

			//*****************************************************************************
			 //*** SE OBTIENEN LAS PROPIEDADES DEL USUARIO, CVE_PERFIL_PUB Y ID_NIVEL_ACCESO ***
			  //*****************************************************************************
			String sIdNivelAcceso = "";
			String sCvePerfilPub = "";
			if (bUsuarioFirmado) {
				//PARAMETRO QUE SE REQUIERE PARA PODER OBTENER LAS PROPIEDADES DEL USUARIO
				parametros.addDefCampo("CVE_USUARIO", usuario.sCveUsuario);
				Registro propiedadesUsuario = publicacion.getPropiedadesUsuarioPublicaciones(parametros);
				//SI LA CONSULTA DE LA PROPIEDADES DEL USUARIO ES NULA
				//SE DICE QUE EL USUARIO NO TIENE ASOCIADO EL NIVEL DE ACCESO
				//NI EL PERFIL Y SE TRATA COMO UN USUARIO PUBLICO
				if (propiedadesUsuario == null) {
					bUsuarioFirmado = false;
				}
				else {
					sIdNivelAcceso = (String) propiedadesUsuario.getDefCampo("ID_NIVEL_ACCESO");
					sCvePerfilPub = (String) propiedadesUsuario.getDefCampo("CVE_PERFIL_PUB");
				}
			}
			else{
				parametros.addDefCampo("CVE_USUARIO", usuario.sCveUsuario);
				Registro propiedadesUsuario = publicacion.getPropiedadesUsuarioPublicaciones(parametros);
				sCvePerfilPub = (String) propiedadesUsuario.getDefCampo("CVE_PERFIL_PUB");
			}

			//***************************************************************************
			 //*** SE FORMAN LAS CONDICIONES PARA LAS PUBLICACIONS PUBLICAS Y PRIVADAS ***
			  //***************************************************************************
			   if (bUsuarioFirmado) {
				   //OBTIENE LAS SECCIONES Y PUBLICACIONES PUBLICAS Y PRIVADAS
				   //PARA CUANDO UN USUARIO SI ESTA FIRMADO AL PORTAL
				   parametros.addDefCampo("USUARIO_FIRMADO", "SI");
				   parametros.addDefCampo("PUBLICA_CONTENIDOS", "SI");
				   parametros.addDefCampo("ID_NIVEL_ACCESO", sIdNivelAcceso);
				   parametros.addDefCampo("CVE_PERFIL_PUB", sCvePerfilPub);
			   } //TERMINA if (bUsuarioFirmado)
			   else {
				   //OBTIENE LAS SECCIONES Y PUBLICACIONES PUBLICAS
				   //PARA CUANDO UN USUARIO NO ESTA FIRMADO AL PORTAL
				   parametros.addDefCampo("USUARIO_FIRMADO", "NO");
				   parametros.addDefCampo("PUBLICA_CONTENIDOS", "SI");
				   parametros.addDefCampo("CVE_PERFIL_PUB", sCvePerfilPub);
			   } //TERMINA if-else (bUsuarioFirmado)

			Registro registropublicacion = new Registro();
			//PARAMETRO QUE SE REQUIERE PARA PODER OBTENER LAS PROPIEDADES DEL USUARIO
			parametros.addDefCampo("CVE_GPO_EMPRESA", usuario.sCveGpoEmpresa);
			//PARAMETRO QUE SE REQUIERE PARA PODER OBTENER LAS PROPIEDADES DEL USUARIO
			parametros.addDefCampo("CVE_PORTAL", usuario.sCvePortal);
			parametros.addDefCampo("NOM_APLICACION_WEB", usuario.sNomAplicacionWeb);
			String sMenuPublicaciones = (publicacion.getMenuPublicaciones(parametros)!=null ? publicacion.getMenuPublicaciones(parametros) : "");
			sMenuCuerpo = sMenuCuerpo + sMenuPublicaciones;
			out.println(sTablaBusquedaForma);
			String sMenu = sMenuEncabezado + sMenuCuerpo + sMenuPiePagina;
			out.println(sMenu);
			out.println(sJavaScript);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return javax.servlet.jsp.tagext.TagSupport.SKIP_BODY;
	}

	/**
	 * Método estándar de la interfaz de TagLibs, el cual es disparado por el contenedor
	 * de servlets al finalizar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como debe evaluar el resto
	 * de una página JSP una vez terminado el TagLib.
	 */
	public int doEndTag() {
		return javax.servlet.jsp.tagext.TagSupport.EVAL_PAGE;
	}
}