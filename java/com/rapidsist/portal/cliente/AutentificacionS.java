/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import com.rapidsist.portal.configuracion.PortalSLHome;
import com.rapidsist.portal.configuracion.PortalSL;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.configuracion.AplicacionUsuario;
import com.rapidsist.portal.configuracion.Perfil;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Iterator;
import javax.rmi.PortableRemoteObject;

/**
 * Servlet que obtiene los datos de un usuario autentificado.
 */
public class AutentificacionS extends HttpServlet {

	/**
	 * Obtiene la configuración para el usuario autentificado y la coloca en la sesión
	 * @param request Objeto que provee de información al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envía como un parámetro a este método.
	 * @param response Objeto que asiste al servlet en enviar una respuesta HTTP al cliente. El
	 * contenedor  de servlets crea un objeto HttpServletResponse y lo envía como parámetro a este método.
	 */
	public void service (HttpServletRequest request, HttpServletResponse response){
		
		try{

			//SE VERIFICARA SI HA CADUCADO LA SESION DEL USUARIO
			HttpSession session = SesionUsuario.compruebaSesionUsuario(request, response);
			if (session == null){
				return;
			}

			//VERIFICA SI YA SE CARGO LA CONFIGURACION DEL USUARIO
			Usuario usuario = (Usuario)session.getAttribute("Usuario");
			if (usuario != null) {

				//VERIFICA SI EL USUARIO POR DEFAULT NO ES EL MISMO QUE EL USUARIO QUE SE AUTENTIFICO
				if (!usuario.sCveUsuario.equals(request.getUserPrincipal().getName())){

					//OBTIENE EL CONTEXTO DEL SERVIDOR DE APLICACIONES
					Context context = new InitialContext();
					Object referencia = context.lookup("java:comp/env/ejb/PortalSL");			
					PortalSLHome portalHome = (PortalSLHome)PortableRemoteObject.narrow(referencia, PortalSLHome.class);			
					PortalSL configuracion = portalHome.create();

					//OBTIENE LA CONFIGURACION DEL USUARIO
					System.out.println("Usuario (original) para autentificacion:  " + request.getUserPrincipal().getName());
					String sUsuarioAutentificacion = request.getUserPrincipal().getName();
					sUsuarioAutentificacion = sUsuarioAutentificacion.replaceAll("jazn.com/", "");
					System.out.println("Usuario (modificado) para autentificacion:  " + sUsuarioAutentificacion);
					usuario = configuracion.getConfiguracionUsuario(usuario.sCvePortalDefault, sUsuarioAutentificacion, request.getContextPath());
					System.out.println("usuario encontrado en el portal:  " + usuario.sCveUsuario);
					System.out.println("empresa del usuario encontrado en el portal:  " + usuario.sCveEmpresa);
					//VERIFICA SI ENCONTRO LA CONFIGURACION DEL USUARIO EN LA BASE DE DATOS
					if (usuario != null) {
						
						AplicacionUsuario aplicacionUsuario = (AplicacionUsuario)usuario.configuracionAplicaciones.get(usuario.sAplicacionActual);
						//VERIFICA SI ENCONTRO LA APLICACION EN LA CONFIGURACION DEL USUARIO
						if (aplicacionUsuario != null){
							
							usuario.sAplicacionActual = aplicacionUsuario.sCveAplicacion;

							//OBTIENE EL NUMERO DE PERFILES ASIGNADOS A UN USUARIO
							int iNumPerfiles = aplicacionUsuario.listaPerfiles.size();
							if (iNumPerfiles > 2){
								//TRANSFIERE EL CONTROL A LA PAGINA DE ASIGNACION DE PERFIL PARA UNA APLICACION
								response.sendRedirect(request.getContextPath() + aplicacionUsuario.sUrlAplicacion);
							}
							else if (iNumPerfiles < 10){
								//LE ASIGNA AL USUARIO EL PERFIL QUE TIENE CONFIGURADO PARA LA APLICACION
								Iterator listaAplicaciones = aplicacionUsuario.listaPerfiles.iterator();
								Perfil perfil = (Perfil)listaAplicaciones.next();
								usuario.sCvePerfilActual = perfil.sCvePerfil;
								//TRANSFIERE EL CONTROL A LA URL DE LA APLICACION
								usuario.sAplicacionActualUrl = aplicacionUsuario.sUrlAplicacion;
							}
							
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
							
							
							usuario.bValidado = true;
							//TRANSFIERE EL CONTROL A LA URL DE LA APLICACION
							response.sendRedirect(request.getContextPath() + usuario.sAplicacionActualUrl);
							
						}else{
							System.out.println("usuario.sUrlDirectorioDefault"+usuario.sUrlDirectorioDefault);
							response.sendRedirect(request.getContextPath() + usuario.sUrlDirectorioDefault + "/SistemaMantenimiento.jsp");
							session.invalidate();
						}
					} //USUARIO CONFIGURADO
				}//VERIFICA SI EL USUARIO POR DEFAULT ES EL MISMO QUE EL USUARIO QUE SE AUTENTIFICO
				
			}
			else{
				System.out.println("Se ha intentado accesar el portal sin utilizar la pagina de inicio");
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}