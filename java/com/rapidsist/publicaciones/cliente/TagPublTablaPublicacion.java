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
import java.util.LinkedList;
import java.util.Iterator;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;
import javax.rmi.PortableRemoteObject;

/**
 * Imprime una tabla html que muestra el resultado de una consulta.
 * <br><br>
 * Parametros:
 * <ul>
 *	<li>
 * cveseccion.- Parametro que indica la sección de las páginas que se vaa a presentar en la jsp
 *  </li>
 *	<li>
 * manipularpresentacion.- Parametro que puede manipularse para noticias o cualquier otra funcionalidad
 *  </li>
 *	<li>
 * manipularconsulta.- Parametro que puede manipularse para noticias o cualquier otra funcionalidad
 *  </li>
 * </ul>
 */
public class TagPublTablaPublicacion
	extends TagSupport {

	String cveseccion = "";
	String manipularpresentacion = "";
	String manipularconsulta = "";

	public void setManipularpresentacion (String sManipularPresentacion) {
		this.manipularpresentacion = sManipularPresentacion;
	}

	public void setManipularconsulta(String sManipularConsulta) {
		this.manipularconsulta = sManipularConsulta;
	}

	public void setCveseccion(String sCveSeccion) {
		this.cveseccion = sCveSeccion;
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
			PublicacionSLHome publicacionHome = (PublicacionSLHome)PortableRemoteObject.narrow(referencia, PublicacionSLHome.class);
			PublicacionSL publicacion = publicacionHome.create();
			//******************************************************************************************
			//*** DETERMINA SI LA BUSQUEDA SE HACE PARA TODAS LAS EMPRESAS DEL GRUPO O SOLO PARA UNA ***
			//******************************************************************************************
			//DETERMINA SI LA PRESENTACIÓN DE LAS PUBLICACIONES ES DE MANERA LINEAL O CON SU ESTRUCTURA
			boolean bConsultaTodo = (pageContext.getRequest().getParameter("chkbConsultaTodo")==null ? false:true);
			//FORMA LA CADENA CON LA TABLA SECCIÓN DE BÚSQUEDA
			//SI EN EL TAGLIB SE INDICA QUE SE DESEA MANIPULAR LA CONSULTA, SE DEBE MOSTRAR LA TABLA
			String sTablaBusquedaForma = "";
			if (manipularpresentacion.equals("true") || manipularconsulta.equals("true")) {
				sTablaBusquedaForma += " <table width=\"90%\" border=\"0\"> \n ";
				sTablaBusquedaForma += " <form name=\"frmPublicaciones\" method=\"post\" action=\"PequesCampeones.jsp\"> \n";
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
				if (usuario.bValidado) {
					bUsuarioFirmado = true;
				}
				else {
					//EL USUARIO ESTA FIRMADO AL PORTAL Y SU SESION NO ESTA ACTIVA
					bUsuarioFirmado = false;
				}
			}

			//*******************************************************
			//*** SE DETERMINA EL ENCABEZADO Y EL PIE DE LA TABLA ***
			//*******************************************************
			 String sJavaScript = "<script language=\"JavaScript\"> \n"+
				 "function MM_goToURL() { //v3.0 \n"+
				 "   var i, args=MM_goToURL.arguments; document.MM_returnValue = false; \n"+
				 "   for (i=0; i<(args.length-1); i+=2) eval(args[i]+\".location='\"+args[i+1]+\"'\"); \n"+
				 "} \n"+
				 "function MM_openBrWindow(theURL,winName,features) { //v2.0 \n"+
				 "   window.open(theURL,winName,features); \n"+
				 "} \n"+
				 "function fVerVideo(sIdPublicacion){ \n"+
			 	 "MM_openBrWindow('/portal/ProcesaCatalogo?Funcion=PublicacionesVideo&OperacionCatalogo=CR&Filtro=Inicia&IdPublicacion='+sIdPublicacion+'&Ventana=Si','Acepta','scrollbars=yes,resizable=yes,width=750,height=350');\n" +
	             "} \n"+
				 " window.focus(); \n"+	 
				 "</script> \n";
			 
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
			 if (bUsuarioFirmado){
				 //PARAMETRO QUE SE REQUIERE PARA PODER OBTENER LAS PROPIEDADES DEL USUARIO
				 parametros.addDefCampo("CVE_USUARIO", usuario.sCveUsuario);
				 Registro propiedadesUsuario = publicacion.getPropiedadesUsuarioPublicaciones(parametros);
				 //SI LA CONSULTA DE LA PROPIEDADES DEL USUARIO ES NULA
				 //SE DICE QUE EL USUARIO NO TIENE ASOCIADO EL NIVEL DE ACCESO
				 //NI EL PERFIL Y SE TRATA COMO UN USUARIO PUBLICO
				 if (propiedadesUsuario==null){
					 bUsuarioFirmado = false;
				} else {
					sIdNivelAcceso = (String)propiedadesUsuario.getDefCampo("ID_NIVEL_ACCESO");
					sCvePerfilPub = (String)propiedadesUsuario.getDefCampo("CVE_PERFIL_PUB");
				}
			 }
			 //***************************************************************************
			 //*** SE FORMAN LAS CONDICIONES PARA LAS PUBLICACIONS PUBLICAS Y PRIVADAS ***
			 //***************************************************************************
			  if (bUsuarioFirmado){
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
			  } //TERMINA if-else (bUsuarioFirmado)

			  //ESTE PARAMETROS SE OBTIENE DEL TAGLIB
			  //EL ORIGEN DE ESTE VALOR ES DESDE ContruyeSeccion2 QUE SE ENCARGA
			  //DE PINTAR EL MENU Y ESTE A SU VEZ LLAMA AL SERVLET MuestraPublicacionS
			  //QUE A SU VEZ REENVIA EL CONTROL A UNA JSP QUE EJECUTA ESTE TAGLIB
			  if (!cveseccion.equals("")){
				  cveseccion = (String) ExpressionUtil.evalNotNull("out", "cveseccion", cveseccion + "x", Object.class, this, pageContext);
				  cveseccion = cveseccion.substring(0, cveseccion.length() - 1);
			  }
			  parametros.addDefCampo("CVE_SECCION", this.cveseccion);

		   Registro registropublicacion = new Registro();
		   //PARAMETRO QUE SE REQUIERE PARA PODER OBTENER LAS PROPIEDADES DEL USUARIO
		   parametros.addDefCampo("CVE_GPO_EMPRESA", usuario.sCveGpoEmpresa);
		   //PARAMETRO QUE SE REQUIERE PARA PODER OBTENER LAS PROPIEDADES DEL USUARIO
		   parametros.addDefCampo("CVE_PORTAL", usuario.sCvePortal);
		   parametros.addDefCampo("NOM_APLICACION_WEB", usuario.sNomAplicacionWeb);
		   
			//SE DETERMINAN LAS PUBLICACIONES QUE PUEDE VER EL USUARIO PARA ESTA SECCIÓN
		   LinkedList listaPublicaciones = publicacion.getPublicaciones(parametros);
		   Iterator ilistaPublicaciones = listaPublicaciones.iterator();
		   String sMenuCuerpo="";
		   sMenuCuerpo = sMenuCuerpo + " 			   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">";
		   while (ilistaPublicaciones.hasNext()){
			   Registro registro = new Registro();
			   registro = (Registro)ilistaPublicaciones.next();
			   
			   String sControlador="";
			   sControlador = (String)registro.getDefCampo("CONTROLADOR");
			   String sVideo = (String)registro.getDefCampo("VIDEO");
			   
			   //Muestra una publicación de un documento.
			   String sHref = "<a class=\"SmallErr\" href= \"/portal/MuestraPublicacion?CvePortal='"+(String)registro.getDefCampo("CVE_PORTAL")+"'&IdPublicacion="+(String)registro.getDefCampo("ID_PUBLICACION")+"&CveSeccion="+(String)registro.getDefCampo("CVE_SECCION")+"\"> ver o bajar publicación</a> ";
			   
			   //Muestra una publicación de audio
			   String sMuestraControlador = "<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0\" width=80 height=27 align=\"absmiddle\">" ;
			   sMuestraControlador = sMuestraControlador + "<param name=movie value=\"/portal/Portales/Icmar/stream/Audio/MapaIglesias.swf\">";
			   sMuestraControlador = sMuestraControlador + "<embed src=\"/portal/Portales/Icmar/stream/Audio/"+(String)registro.getDefCampo("CONTROLADOR")+"\" quality=best wmode=transparent bgcolor=#FFFFFF loop=true width=80 height=27 type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash\" align=\"absmiddle\">"; 
			   sMuestraControlador = sMuestraControlador + "</embed> </object>";
			   
          	   
          	   //Muestra una publicación de video.
          	   String sMuestraVideo = "<a href='javascript:fVerVideo("+registro.getDefCampo("ID_PUBLICACION")+")'>Ver video</a> \n";	
          	                          
			   sMenuCuerpo = sMenuCuerpo + " 				   <tr> \n";
			   sMenuCuerpo = sMenuCuerpo + " 					   <td colspan=\"3\" height=\"10\"> \n";
			   sMenuCuerpo = sMenuCuerpo + " 					   </td> \n";
			   sMenuCuerpo = sMenuCuerpo + " 				   </tr> \n";
			   sMenuCuerpo = sMenuCuerpo + " 				   <tr> \n";
			   sMenuCuerpo = sMenuCuerpo + " 					   <td> \n";
			   sMenuCuerpo = sMenuCuerpo + " 					   </td> \n";
			   sMenuCuerpo = sMenuCuerpo + " 					   <td><!-- ARTICLE_BEGIN --> \n";
			   sMenuCuerpo = sMenuCuerpo + " 						   <div class=\"SubHdDk\">"+registro.getDefCampo("NOM_PUBLICACION")+"</div> \n";
			   sMenuCuerpo = sMenuCuerpo + " 						   <div class=\"SmallDk\">"+registro.getDefCampo("TX_COMENTARIO")+"</div> \n";
			   sMenuCuerpo = sMenuCuerpo + " 						   <br/> \n";
			   sMenuCuerpo = sMenuCuerpo + " 						   <div class=\"ContenidoPublicacion\">"+registro.getDefCampo("DESC_PUBLICACION")+" ..."+"</div> \n";
			   sMenuCuerpo = sMenuCuerpo + " 					   </td> \n";
			   sMenuCuerpo = sMenuCuerpo + " 					   <td width='15%'> \n";
			   
			   //Verifica si es una publicación de un documento.
			   if ((sControlador==null) && (sVideo==null)) {
				   if (registro.getDefCampo("URL_IMAGEN")!=null){
					   if (!((String)registro.getDefCampo("URL_IMAGEN")).equals("") ){
						   sMenuCuerpo = sMenuCuerpo + " 						   <img src=\"/portal/MuestraPublicacion?MuestraImagen=true&IncrementaContador=false&CvePortal="+(String)registro.getDefCampo("CVE_PORTAL")+"&CveSeccion="+(String)registro.getDefCampo("CVE_SECCION")+"&IdPublicacion="+(String)registro.getDefCampo("ID_PUBLICACION")+"\"/> \n";
					   }
				   }   
			   sMenuCuerpo = sMenuCuerpo + " 						   <div align=\"left\">"+sHref+"</div> \n"; 
			   }
			   
			   //Verifica si es una publicación de audio.
			   if (sControlador!=null){
				   sMenuCuerpo = sMenuCuerpo + " 						   <div align=\"left\">"+sMuestraControlador+"</div> \n";
			   }
			   //Verifica si es una publicación de video.
			   if (sVideo!=null){
				   sMenuCuerpo = sMenuCuerpo + "<div align=\"left\">"+sMuestraVideo+"</div> \n";
			   }
				   
			   sMenuCuerpo = sMenuCuerpo + " 					   </td>";
			   sMenuCuerpo = sMenuCuerpo + " 				   </tr>";
			}
			sMenuCuerpo = sMenuCuerpo + " 			   </table>";
			out.println(sTablaBusquedaForma);
			out.println(sMenuCuerpo);
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
