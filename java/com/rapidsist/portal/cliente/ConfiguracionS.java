/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import com.rapidsist.portal.configuracion.PortalSLHome;
import com.rapidsist.portal.configuracion.PortalSL;
import com.rapidsist.portal.configuracion.Usuario;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

/**
 * Servlet que obtiene los datos por default para un portal.
 */
public class ConfiguracionS extends HttpServlet {

	/**
	 * Obtiene los datos por default para un portal.
	 * @param request Objeto que provee de información al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envía como un parámetro a este método.
	 * @param response Objeto que asiste al servlet en enviar una respuesta HTTP al cliente. El
	 * contenedor  de servlets crea un objeto HttpServletResponse y lo envía como parámetro a este método.
	 */
	public void service (HttpServletRequest request, HttpServletResponse response){

		/*
		//RECIBE TODAS LAS COOKIES  QUE VIENEN CON EL REQUEST
		Cookie[] cookies = request.getCookies();
		Cookie info = null;
		String sCveUsuario = null;

		//VERIFICA SI HAY COOKIES EN EL NAVEGADORA QUE NOS AYUDEN A IDENTIFICAR EL USUARIO
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				info = cookies[i];

				//BUSCA ENTRE LAS COOKIES SI EXISTE UNA CON LA CLAVE DEL USUARIO
				if (info.getName().equals("Usuario")) {
					sCveUsuario = info.getValue();
					break;
				}
			}
		} //VERIFICA SI HAY COOKIES EN EL NAVEGADORA QUE NOS AYUDEN A IDENTIFICAR EL USUARIO
		*/

		//OBTENEMOS OBJETO SESSION
		HttpSession session = request.getSession(true);
		try{
			System.out.println("****START**** ConfiguracionS");
			System.out.println("PortalDefault es "+request.getParameter("PortalDefault")+". ConfiguracionS");
			System.out.println("Datasource es "+request.getParameter("Datasource")+". ConfiguracionS");
			
			//VERIFICAMOS SI SE ENVIARON LOS PARAMETROS DE INICIALIZACION DEL PORTAL
			if (request.getParameter("PortalDefault") == null){
				System.out.println("Error, no se especifico el parametro PortalDefault en la inicialización del portal");
				return;
			}

			if (request.getParameter("Datasource") == null){
				System.out.println("Error, no se especifico el parametro Datasource en la inicialización del portal");
				return;
			}

			//VERIFICA SI EL USUARIO NO ESTA CONFIGURADO DENTRO DEL PORTAL
			Usuario usuario = (Usuario)session.getAttribute("Usuario");
			System.out.println("usuario "+usuario+". ConfiguracionS");
			
			//OBTIENE EL CONTEXTO DEL SERVIDOR DE APLICACIONES
			Context context = new InitialContext();
			
			Object referencia = context.lookup("java:comp/env/ejb/PortalSL");			
			System.out.println("PASO 2");
			PortalSLHome portalHome = (PortalSLHome)PortableRemoteObject.narrow(referencia, PortalSLHome.class);			
			System.out.println("PASO 3");
			PortalSL configuracion = portalHome.create();
			System.out.println("PASO 4");
			
			//OBTIENE LA CONFIGURACION DEL PORTAL
			System.out.println("llama al metodo getConfiguracionUsuario. ConfiguracionS");
			usuario = configuracion.getConfiguracionUsuario(request.getParameter("PortalDefault"), "", request.getContextPath());
			System.out.println("*finaliza metodo getConfiguracionUsuario. ConfiguracionS");
			
			System.out.println("usuario es :"+usuario+".ConfiguracionS");
			
			//VERIFICA SI ENCONTRO LA CONFIGURACION DEL USUARIO EN LA BASE DE DATOS
			if (usuario != null){
				System.out.println("ENCONTRO LA CONFIGURACION DEL USUARIO EN LA BASE DE DATOS. ConfiguracionS");
				//AGREGA LA COOKIE CON LA CLAVE DEL USUARIO
				Cookie infoUsuario = new Cookie("Usuario", usuario.sCveUsuario);
				infoUsuario.setMaxAge(10 * 24 * 60 * 60);
				infoUsuario.setPath("/");
				response.addCookie(infoUsuario);
				//AGREGA LA COOKIE DEL SUBDIRECTORIO POR DEFAULT
				Cookie infoDirectorio = new Cookie("UrlPortal", usuario.sUrlDirectorioDefault);
				infoDirectorio.setMaxAge(10 * 24 * 60 * 60);
				infoDirectorio.setPath("/");
				response.addCookie(infoDirectorio);

				//ALMACENA LA INFORMACION EN LA SESION DEL USUARIO
				session.setAttribute("Usuario", usuario);

				//TRANSFIERE EL CONTROL A LA URL DE LA APLICACION
				response.sendRedirect(request.getContextPath() + usuario.sAplicacionActualUrl);
			}
			else{
				System.out.println("**ERROR** NO ENCONTRO LA CONFIGURACION DEL USUARIO EN LA BASE DE DATOS. ConfiguracionS");
				System.out.println("Error, no se encontró en la base de datos la confiugración del portal");
				return;
			} //USUARIO CONFIGURADO

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}