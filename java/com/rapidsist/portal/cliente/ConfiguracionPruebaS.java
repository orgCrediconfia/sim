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
import java.io.*;



/**
 * Servlet que obtiene los datos por default para un portal.
 */
public class ConfiguracionPruebaS extends HttpServlet {

	/**
	 * Obtiene los datos por default para un portal.
	 * @param request Objeto que provee de información al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envía como un parámetro a este método.
	 * @param response Objeto que asiste al servlet en enviar una respuesta HTTP al cliente. El
	 * contenedor  de servlets crea un objeto HttpServletResponse y lo envía como parámetro a este método.
	 */
	public void service (HttpServletRequest request, HttpServletResponse response){

		//OBTENEMOS OBJETO SESSION
		HttpSession session = request.getSession(true);
		try{

			File archivo = new File("kikolito.txt");
			archivo.createNewFile();
			
			
			
			//VERIFICAMOS SI SE ENVIARON LOS PARAMETROS DE INICIALIZACION DEL PORTAL
			if (request.getParameter("PortalDefault") == null){
				System.out.println("Error, no se especifico el parametro PortalDefault en la inicialización del portal");
				return;
			}

			if (request.getParameter("Datasource") == null){
				System.out.println("Error, no se especifico el parametro Datasource en la inicialización del portal");
				return;
			}

			//VERIFICA SI EL USUARIO NO ESTA CONFIUGURADO DENTRO DEL PORTAL
			Usuario usuario = (Usuario)session.getAttribute("Usuario");

			if (usuario ==null){
				Context ctx = new InitialContext();			
				Object ref = ctx.lookup("java:comp/env/ejb/PortalSL");			
				PortalSLHome portalHome = (PortalSLHome)PortableRemoteObject.narrow(ref, PortalSLHome.class);			
				PortalSL configuracion = portalHome.create();

				//OBTIENE LA CONFIGURACION DEL PORTAL
				usuario = configuracion.getConfiguracionUsuario(request.getParameter("PortalDefault"), "", request.getContextPath());

				//VERIFICA SI ENCONTRO LA CONFIGURACION DEL USUARIO EN LA BASE DE DATOS
				if (usuario != null){

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
					System.out.println("Brincará a la url: " + usuario.sAplicacionActualUrl);
					//response.sendRedirect(request.getContextPath() + usuario.sAplicacionActualUrl);
				}
				else{
					System.out.println("Error, no se encontró en la base de datos la confiugración del portal");
					return;
				} //USUARIO CONFIGURADO

			}//VERIFICA SI EL USUARIO NO ESTA CONFIGURADO EN EL PORTAL
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}