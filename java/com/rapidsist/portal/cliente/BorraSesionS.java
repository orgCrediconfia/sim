/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

/**
 * Servlet que invalida la sesion del usuario y lo regresa a la página de inicio del portal.
 */
public class BorraSesionS extends HttpServlet {

	/**
	 * Invalida la sesion del usuario y lo regresa a la página de inicio del portal.
	 * @param request Objeto que provee de información al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envía como un parámetro a este método.
	 * @param response Objeto que asiste al servlet en enviar una respuesta HTTP al cliente. El
	 * contenedor  de servlets crea un objeto HttpServletResponse y lo envía como parámetro a este método.
	 */
	public void service (HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession(true);
		try{

			//INVALIDA LA SESION DEL USUARIO
			session.invalidate();

			//RECIBE TODAS LAS COOKIES  QUE VIENEN CON EL REQUEST
			Cookie[] cookies = request.getCookies();
			Cookie info = null;
			String sPaginaFinSesion = null;

			//OBTIENE LA COOKIE CON LA URL POR DEFAULT DEL PORTAL
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					info = cookies[i];

					if (info.getName().equals("UrlPortal")) {
						sPaginaFinSesion = info.getValue() + "/SistemaConfig.jsp";
						break;
					}
				}
			}//OBTIENE LA COOKIE CON LA URL POR DEFAULT DEL PORTAL

			if (sPaginaFinSesion != null) {
				response.sendRedirect(request.getContextPath() + sPaginaFinSesion);
			}
			else{
				System.out.println("No esta definida la cookie con la url del portal");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}