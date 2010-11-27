/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.configuracion.AplicacionUsuario;
import com.rapidsist.portal.configuracion.Perfil;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import com.rapidsist.comun.bd.Registro;

/**
 * Servlet que cambia la aplicación actual del usuario y salta a la URL de inicio de la
 * aplicación solicitada.
 */
public class CambiaAplicacionS extends HttpServlet {

	/**
	 * Cambia la aplicación actual del usuario y salta a la URL de inicio de la aplicación.
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
			//VERIFICAMOS SI SE ENVIARON LOS PARAMETROS
			if (request.getParameter("CveAplicacion") == null){
				System.out.println("Error, no se especifico el parametro CveAplicacion en el servlet de cambio de aplicacion");
				return;
			}
			String sCveAplicacion = request.getParameter("CveAplicacion");

			//OBTIENE LA CONFIGURACION DEL USUARIO
			Usuario usuario = (Usuario)session.getAttribute("Usuario");
			
			//VERIFICA SI ENCONTRO EL USUARIO
			if (usuario !=null){
				//VERIFICA SI LA APLICACION PERTENECE AL USUARIO Y OBTIENE LA URL PARA TRANSFERIR EL
				//CONTROL DEL REQUEST
				boolean bExito=false;
				AplicacionUsuario aplicacionUsuario = (AplicacionUsuario)usuario.configuracionAplicaciones.get(sCveAplicacion);

				//VERIFICA SI ENCONTRO LA APLICACION EN LA CONFIGURACION DEL USUARIO
				if (aplicacionUsuario != null){
					usuario.sAplicacionActual = aplicacionUsuario.sCveAplicacion;

					//OBTIENE  EL TIPO DE LETRA EN LA APLICACION ACTUAL
					Iterator ilistaAplicacionesSelector = usuario.listaAplicacionesSelector.iterator();
					while (ilistaAplicacionesSelector.hasNext()){
						Registro registroAplicacionesSelector = (Registro)ilistaAplicacionesSelector.next();
						String sAplicacion = (String) registroAplicacionesSelector.getDefCampo("CVE_APLICACION");
						if (usuario.sAplicacionActual.equals(sAplicacion)){
							usuario.sTipoLetraAplicacion = (String)registroAplicacionesSelector.getDefCampo("TIPO_LETRA_APLICACION");
						}
					}
					
					//ASIGNA EL TIPO DE LETRA AL USUARIO
					if (usuario.sTipoLetraAplicacion.equals("MA")){
					     usuario.sTipoLetra = "MA";	
					}
					else{
						if(usuario.sTipoLetraAplicacion.equals("NO")){
							usuario.sTipoLetra = "NO";	
						}
						else{
							if (usuario.sTipoLetraPortal.equals("MA")){
								usuario.sTipoLetra = "MA";	
							}
							else{
								if(usuario.sTipoLetraPortal.equals("NO")){
									usuario.sTipoLetra = "NO";	
								}
								else{
									if (usuario.sTipoLetraEmpresa.equals("MA")){
										usuario.sTipoLetra = "MA";	
									}
									else{
										if(usuario.sTipoLetraEmpresa.equals("NO")){
											usuario.sTipoLetra = "NO";	
										}
										else{
											usuario.sTipoLetra = "NO";
										}
									}
								}
							}							
						}
					}
					
					if (usuario.sTipoLetra.equals("MA")){
						usuario.bSoloMayusculas = true;
					}
					else{
						usuario.bSoloMayusculas =false;
					}
					
					
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
						response.sendRedirect(request.getContextPath() + aplicacionUsuario.sUrlAplicacion);
					}
				}//VERIFICA SI ENCONTRO LA APLICACION EN LA CONFIGURACION DEL USUARIO
			}//VERIFICA SI ENCONTRO EL USUARIO
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}