/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;
import com.rapidsist.portal.configuracion.Usuario;

/**
 * Clase que proporciona utilerías para el manejo de la sesión de usuario.
 */
public class SesionUsuario {

	/**
	 * Verifica que la sesión del usuario no haya terminado.
	 * @param request Objeto que provee de información al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envía como un parámetro a este método.
	 * @param response Objeto que asiste al servlet en enviar una respuesta HTTP al cliente. El
	 * contenedor  de servlets crea un objeto HttpServletResponse y lo envía como parámetro a este método.
	 * @return La sesión del usuario. Si regresa nulo significa que no se encontro activa
	 * la sesión del usuario.
	 */
	public static HttpSession compruebaSesionUsuario(HttpServletRequest request, HttpServletResponse response){

		HttpSession session = null;
		try {
			//VERIFICA SI HA CADUCADO LA SESION DEL USUARIO
			session = request.getSession(false);
			if (session != null) {

				//NOS ASEGURAMOS QUE EXISTA EL OBJETO USUARIO EN LA SESION
				Usuario usuario = (Usuario)session.getAttribute("Usuario");
				if (usuario == null){
					session = null;
				}
			}

			//VERIFICAMOS SI SE ENCONTRO LA SESION
			if (session == null){
				//LA SESION DEL USUARIO TERMINO

				//RECIBE TODAS LAS COOKIES  QUE VIENEN CON EL REQUEST
				Cookie[] cookies = request.getCookies();
				Cookie info = null;
				String sPaginaFinSesion = null;

				//OBTIENE LA COOKIE CON LA URL POR DEFAULT DEL PORTAL
				if (cookies != null) {
					for (int i = 0; i < cookies.length; i++) {
						info = cookies[i];

						if (info.getName().equals("UrlPortal")) {
							sPaginaFinSesion = info.getValue() + "/SistemaFinSesion.htm";
							break;
						}
					}
				}//OBTIENE LA COOKIE CON LA URL POR DEFAULT DEL PORTAL

				if (sPaginaFinSesion != null) {
					response.sendRedirect(request.getContextPath() + sPaginaFinSesion);
				}
				else{
					System.out.println("No esta definida la pagina de fin de sesion para el portal");
				}
				return null;
			}
			else{
			}
			//VERIFICA SI HA CADUCADO LA SESION DEL USUARIO
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return session;
	}
}