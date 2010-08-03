/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.bitacora.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.bitacora.datos.BitacoraSL;
import com.rapidsist.portal.bitacora.datos.BitacoraSLHome;
import com.rapidsist.portal.cliente.RegistroControl;
import com.rapidsist.portal.cliente.SesionUsuario;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.Iterator;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Date;
import java.text.DateFormat;
import javax.rmi.PortableRemoteObject;

/**
 * Servlet que tiene la funcionalidad de mostrar la consulta de las bitácoras en XML
 */
public class ConsultaBitacoraS
	extends HttpServlet {
	RegistroControl registroControl = new RegistroControl();

	/**
	 * Método estándar para servlets que procesa cualquier petición del browser
	 * @param request Objeto que provee de información al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envía como un parámetro a este método.
	 * @param response Objeto que asiste al servlet en enviar una respuesta HTTP al cliente. El
	 * contenedor  de servlets crea un objeto HttpServletResponse y lo envía como parámetro a este método.
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) {
		RegistroControl registroControl = new RegistroControl();
		String sTotalRegistros = "0";
		String sTotalPaginas = "1";
		String sNomTabla = "Sin datos";
		String sPaginaActual = "1";
		Registro parametros = new Registro();

		try {
			//SE VERIFICARA SI HA CADUCADO LA SESION DEL USUARIO
			HttpSession session = SesionUsuario.compruebaSesionUsuario(request, response);
			if (session == null) {
				return;
			}

			//OBTIENE EL CONTEXTO DEL SERVIDOR DE APLICACIONES
			Context context = new InitialContext();

			//OBTIENE LA CLAVE DEL USUARIO QUE OPERA EL CATALOGO
			Usuario usuario = (Usuario) session.getAttribute("Usuario");

			//ESTE PARAMETRO ES NECESARIO PARA QUE PUEDA FUNCIONAR CORRECTAMENTE EL EJB
			Registro registroOriginal = (Registro)session.getAttribute("RegistroOriginal");

			if(registroOriginal != null){
				//EL PARAMETRO sCveFuncion ES NECESARIO PARA PODER DETERMINAR LA TABLA QUE CONSULTA LA BITACORA
				String sCveFuncion = request.getParameter("CveFuncion");
				System.out.println("Cve funcion:"+ sCveFuncion);
				String sNumSet = request.getParameter("NumSet") != null ? request.getParameter("NumSet") : "0";
				sPaginaActual = request.getParameter("Pagina");
				int iNumRegPorPag = 5;
				if (request.getParameter("NumRegPorPag") != null) {
					iNumRegPorPag = (int) Float.valueOf(request.getParameter("NumRegPorPag")).floatValue();
				}

				// SE AGREGAN AL REGISTRO ORIGINAL LOS CAMPOS REQUERIDOS POR OTRAS POSIBLES TABLAS QUE SE PUDIERAN BITACORAR
				registroOriginal.addDefCampo("CVE_GPO_EMPRESA", usuario.sCveGpoEmpresa);
				registroOriginal.addDefCampo("PAGINA_SOLICITADA", request.getParameter("Pagina") != null ? request.getParameter("Pagina") : "1");

				// SE INICIALIZA LA LISTA EN NULL
				LinkedList listaBitacora = null;

				// SE VALIDA SI SE VA A GENERAR LA LISTA DESDE EL EJB O SE VA A RECUPERAR DE LA SESIÓN
				if (request.getParameter("RefrescaDatos") == null) {

					//INICIALIZA EL EJB DE BITACORA
					Object referencia = context.lookup("java:comp/env/ejb/BitacoraSL");
					BitacoraSLHome bitacoraHome = (BitacoraSLHome) PortableRemoteObject.narrow(referencia, BitacoraSLHome.class);
					BitacoraSL bitacoraEjb = bitacoraHome.create();

					// SE OBTIENE DEL EJB UNA LISTA CON LO SIGUIENTE:
					// - LA LISTA QUE SE DESPLEGARÁ EN LA JSP
					// - REGISTRO CON EL NUMERO DE REGISTROS
					Registro registroPropiedades = new Registro();
					LinkedList listaConsultaBitacora = bitacoraEjb.consultaBitacora(sCveFuncion, registroOriginal);
					Registro registroParametros = new Registro();
					String sMensajeError = "";

					// VALIDA SI LA LISTA QUE SE RECUPERA ES NULA PARA REDIRECCIONAR LA PAGINA A LA JSP DE MENSAJES DEL SISTEMA
					if (listaConsultaBitacora != null) {
						Iterator ilistaConsultaBitacora = listaConsultaBitacora.iterator();
						if (ilistaConsultaBitacora.hasNext()) {
							//SE OBTIENE EL REGISTRO DE PARAMETROS
							registroParametros = (Registro) ilistaConsultaBitacora.next();
							//SE OBTIENE LA LISTA DE LA CONSULTA DE LA BITACORA
							if (ilistaConsultaBitacora.hasNext()) {
								listaBitacora = (LinkedList) ilistaConsultaBitacora.next();
							}
							// SI LA LISTA NO CONTIENE LA CONSULTA DE BITÁCORA ENTONCES SE GENERÓ ALGÚN ERROR
							// Y SE RECUPERA EL MENSAJE DE ERROR DE EL REGISTRO registroParametros
							else {
								sMensajeError = (String) registroParametros.getDefCampo("MENSAJE");
							}
						}
						// SI listaBitacora NO ES NULA SE AGREGA EL REGISTRO PARAMETROS Y LA LISTA A LA SESIÓN
						if (listaBitacora != null) {
							registroParametros.addDefCampo("PAGINA_ACTUAL", sPaginaActual != null ? sPaginaActual : "1");
							session.setAttribute("RegistroParametros", registroParametros);
							request.setAttribute("Lista", listaBitacora);
							session.setAttribute("Lista", listaBitacora);
						}
					}
					else {
						// LA LISTA ES NULA Y SE ENVÍA UN MENSAJE A LA PÁGINA DE ERROR
						sMensajeError = "Se produjo un error interno.";
					}
					if (listaBitacora == null) {
						// SI OCURRE ALGÚN ERROR SE REDIRECCIONA LA PÁGINA A LA JSP DE MENSAJES DEL SISTEMA
						response.sendRedirect(request.getContextPath() + usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=" + sMensajeError + "&Ventana=Si ");
						return;
					}
					else {
						//EL PARAMETRO SIGUIENTE EN LA URL DETERMINA SI SE HACE UNA REDIRECCIOM O UN DESPACHADO CON TODO Y ARGUMENTOS
						//UTIL PARA PANTALLAS DE CAPTURA DE ALTA RECURSIVAS CON VERIFICACION DE EXISTENCIA
						registroControl.sPagina = "/comun/FormasInfraestructura/fBitaCon.jsp?Ventana=Si&TotRegistros=" + (String) registroParametros.getDefCampo("TOTAL_REGISTROS") + "&NumeroPagina=1&NumeroRegistrosPagina=" + iNumRegPorPag + "&NumSet=" + sNumSet;
						request.getRequestDispatcher(registroControl.sPagina).forward(request, response);
					}
				}
				else {
					// SI NO SE REFRESCAN DATOS SE RECUPERA LA LISTA DE LA SESIÓN PARA ENVIARLA EN EL REQUEST A LA JSP
					request.setAttribute("Lista", session.getAttribute("Lista"));
					Registro registroParametros = (Registro) session.getAttribute("RegistroParametros");
					registroControl.sPagina = "/comun/FormasInfraestructura/fBitaCon.jsp?Ventana=Si&TotRegistros=" + (String) registroParametros.getDefCampo("TOTAL_REGISTROS") + "&NumeroPagina=" + sPaginaActual + "&NumeroRegistrosPagina=" + iNumRegPorPag + "&NumSet=" + sNumSet;
					request.getRequestDispatcher(registroControl.sPagina).forward(request, response);
				}
			}
			else{
				// SI NO EXISTE EL REGISTRO ORIGINAL SE REDIRECCIONA LA PÁGINA A LA JSP DE MENSAJES DEL SISTEMA
				response.sendRedirect(request.getContextPath() + usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=La información no está disponible.&Ventana=Si ");
				return;

			}
		}
		catch (Exception e) {
			System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()) + " " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()) + " - Error en ProcesaCatalogoS, mensaje: " + e.getMessage());
			e.printStackTrace();
		}

	}
}