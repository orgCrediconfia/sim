/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.cliente;

import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.cliente.SesionUsuario;
import com.rapidsist.publicaciones.datos.PublicacionSL;
import com.rapidsist.publicaciones.datos.PublicacionSLHome;
import com.rapidsist.comun.bd.Registro;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Date;
import java.text.DateFormat;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import javax.servlet.*;
import javax.rmi.PortableRemoteObject;
import com.rapidsist.sim.prestamo.datos.SimPrestamoFlujoEfectivoDAO;

/**
 * Servlet que lleva el control de la presentación de las publicaciones.
 */
public class MuestraArchivoS extends HttpServlet {

	/**
	 * Administra la presentación de las publicaciones públicas, privadas y aquellas
	 * que son confidenciales y que requieres de un password para poder accesar.
	 * @param request Objeto que provee de información al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envía como un parámetro a este método.
	 * @param response Objeto que asiste al servlet en enviar una respuesta HTTP al cliente. El
	 * contenedor  de servlets crea un objeto HttpServletResponse y lo envía como parámetro a este método.
	 * @throws ServletException Define cualquier excepción del servlet cuando encuentra dificultades.
	 * @throws IOException Permite obtener dificultades que puedan ocurrir en fallas de entradas o salidas
	 * de datos en el servlet.
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//SE VERIFICARA SI HA CADUCADO LA SESION DEL USUARIO
			HttpSession session = SesionUsuario.compruebaSesionUsuario(request, response);
			if (session == null) {
				return;
			}
			//OBTIENEN LOS DATOS NECESARIO DEL OBJETO USUARIO
			Usuario usuario = (Usuario) session.getAttribute("Usuario");

			//SE INSTANCIA EL OBJETO REGISTRO PARA ENVIARLO COMO PARAMETRO AL METODO
			//QUE OBTIENE LA PUBLICACIÓN
			Registro parametros = new Registro();
			parametros.addDefCampo("CVE_GPO_EMPRESA", usuario.sCveGpoEmpresa);
			parametros.addDefCampo("CVE_USUARIO", usuario.sCveUsuario);
			parametros.addDefCampo("CVE_PORTAL", usuario.sCvePortal);
			parametros.addDefCampo("ID_PRESTAMO", request.getParameter("IdPrestamo"));
			parametros.addDefCampo("ID_ARCHIVO_FLUJO", request.getParameter("IdArchivoFlujo"));
			parametros.addDefCampo("URL_ARCHIVO", request.getParameter("UrlArchivo"));
			String sIdPrestamo = request.getParameter("IdPrestamo");
			
			//OBTIENE EL CONTEXTO DEL SERVIDOR DE APLICACIONES
			/*Context context = new InitialContext();
			Object referencia = context.lookup("java:comp/env/ejb/PublicacionSL");
			PublicacionSLHome publicacionHome = (PublicacionSLHome)PortableRemoteObject.narrow(referencia, PublicacionSLHome.class);
			PublicacionSL publicacion = publicacionHome.create();
			*/
			//SI SE ELIGIÓ UNA SECCIÓN SE MUESTRAN LAS PUBLICACIONES DE ESA SECCIÓN
			/*if (request.getParameter("MostrarPublicaciones") != null) {
				//SE OBTIENE LA HISTORIA DE LA SECCIÓN QUE SE LLAMA
				String sHistoriaSeccion = publicacion.getHistoriaSeccion(parametros);
				request.getRequestDispatcher("/Aplicaciones/Publicaciones/ContenedorPublicaciones.jsp?Ventana=Si&CveSeccion=" + request.getParameter("CveSeccion") + "&NomSeccion=" + request.getParameter("NomSeccion") + "&HistoriaSeccion="+sHistoriaSeccion).forward(request, response);
			}
			*/
			//else {
				//DE OTRA FORMA SE DICE QUE SE DESEA MOSTRAR UNA PUBLICACIÓN
				//SE OBTIENE LA PUBLICACION VALIDA
				//SE OBTIENE LA RUTA DINAMICAMENTE DONDE SE ENCUENTRA EL ARCHIVO

				//ANTES DE OBTENER LA PUBLICACION SE INDICA CON UN PARAMETRO QUE
				//DETERMINE LA RUTA DE LA SECCION PARA PODER MOSTRAR LA IMAGEN
				//parametros.addDefCampo("DETERMINA_RUTA_SECCION", "SI");
				//Registro registroPublicacion = publicacion.getPublicacion(parametros);
				//SI EL registroPublicacion VIENE EN NULO SE DICE NO SE ENCONTRO LA PUBLICACIÓN
				//O SU NIVEL DE ACCESO NO ES EL QUE DEBIERA TENER PARA PODER VERLA
				//if (registroPublicacion != null) {
					//OBTENEMOS EL ARCHIVO
					String sArchivo = request.getParameter("UrlArchivo");
					//OBTENEMOS LA IMAGEN DEL registroPublicacion Y SI LA IMAGEN VIENE EN NULO, ENTONCES SE TOMA LA IMAGEN DEL REQUEST QUE SE ENVIO DESDE EL TAG LIB
					//String sImagen = registroPublicacion != null ? (String) registroPublicacion.getDefCampo("URL_IMAGEN") : "";
					//SE OBTIENE LA RUTA DE DONDE SE ENCUENTRA EL ARCHIVO FISICAMENTE
					//String sRutaRaizDestinoArchivo = "/Publicaciones" + "/" + ( (String) parametros.getDefCampo("CVE_GPO_EMPRESA")).toLowerCase() + "/" + ( (String) parametros.getDefCampo("CVE_PORTAL")).toLowerCase();
					//String sRutaRaizArchivosComunes = "/Publicaciones/comunes";
					//String sRutaArchivo = sRutaRaizDestinoArchivo + ( (registroPublicacion != null ? (String) registroPublicacion.getDefCampo("RUTA_SECCION") : "")).toLowerCase() + sArchivo;
					
					//OBTIENE EL DIRECTORIO RAIZ
					
					String sRutaArchivo = "C:" + System.getProperty("file.separator") + "ArchivosFE" + System.getProperty("file.separator") + sIdPrestamo + System.getProperty("file.separator") + sArchivo;
					
					//String sRutaImagen = "";
					/*if (sImagen != null) {
						//SI LAS IMAGENES SON LAS IMAGENES POR DEFAULT SE OBTIENE DEL DIRECTORIO DE PUBLICACIONES
						if (sImagen.equals("Pdf.gif") || sImagen.equals("Txt.gif") || sImagen.equals("Html.gif")) {
							sRutaImagen = sRutaRaizArchivosComunes + "/" + sImagen;
						}
						else {
							//SI LAS IMAGENES SON OTRAS SE TOMAN DEL DIRECTORIO CORRESPONDIENTE A SU SECCIÓN
							sRutaImagen = sRutaRaizDestinoArchivo + ( (registroPublicacion != null ? (String) registroPublicacion.getDefCampo("RUTA_SECCION") : "")).toLowerCase() + sImagen;
						}
					}
					else {
						sRutaImagen = "";
					}
					*/
					//DETERMINA QUE TIPO DE ARCHIVO SE VA A MOSTRAR PARA QUE SERVLET PUEDA
					//EJECUTAR LA APLICACIÓN QUE DEBE LEER
					String sTipoContenedor = "";
					File file = null;
					/*if (request.getParameter("MuestraImagen") != null) {
						sTipoContenedor = getServletContext().getMimeType(sRutaImagen); // OBTIENE EL TIPO DE ARCHIVO
						//ASOCIA LA CANDENA DEL NOMBRE DE LA IMAGEN A UN OBJETO DE TIPO ARCHIVO (FILE)
						file = new File(sRutaImagen);
				
					}*/
					//else {
						sTipoContenedor = getServletContext().getMimeType(sRutaArchivo); // OBTIENE EL TIPO DE ARCHIVO
						//ASOCIA LA CANDENA DEL NOMBRE DEL ARCHIVO A UN OBJETO DE TIPO ARCHIVO (FILE)
						sTipoContenedor = "application/octet-stream";
						file = new File(sRutaArchivo);
					//}

					//SE ASOCIA EL CONTENEDOR AL OBJETO RESPONSE
					response.setContentType( (sTipoContenedor == null ? "text/plain" : sTipoContenedor) + ";name=\"" + sArchivo + "\""); //DETERMINA QUE TIPO DE ARCHIVO SE VA A EJECUTAR
					response.setHeader("Content-Disposition", "attachment; filename=" + sArchivo);

					//CLASE ABSTRACTA QUE NOS PERMITE ENVIAR LA RESPUESTA DEL SERVLET COMO CADENAS
					//CONVERTIDAS EN DATOS BINARIOS AL CLIENTE
					ServletOutputStream out = response.getOutputStream();

					//SI LA CADENA NO ES UN ARCHIVO LO INDICA CON UN MENSAJE
					if (file == null) {
						out.println("No se puede ver el archivo...");
						return;
					}

					FileInputStream fis = null;
					//SI NO ENCUENTRA EL ARCHIVO GENERA UN ERROR Y SE MANIPULA PARA PODER PRESENTAR ALGO
					//try {
						fis = new FileInputStream(file);
					//}
					/*catch (IOException ioe) {
						// ENVIA UN ERROR SI NO SE PUDO ENCONTRAR EL ARCHIVO
						if (request.getParameter("MuestraImagen") != null) {
							sRutaImagen = sRutaRaizArchivosComunes + "/No_disponible.gif";
							file = new File(sRutaImagen);
							fis = new FileInputStream(file);
						}
						else {
							System.out.println("Se debe mostrar página que indique que el archivo no esta disponible");
						}
					}
					*/

					//CONVIERTE EL ARCHIVO EN BYTES PARA PODER DAR LECTURA Y PODER
					//PRESENTARLO
					try {
						byte[] buf = new byte[4 * 1024];
						int bytesRead;
						while ( (bytesRead = fis.read(buf, 0, buf.length)) != -1) {
							out.write(buf, 0, bytesRead);
						}
						response.setStatus(HttpServletResponse.SC_OK);
					}
					catch (IOException ioe) {
						ioe.printStackTrace();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					finally {
						if (fis != null) {
							fis.close();
						}
						out.close();
					}
				//}
				/*else {
					//DE OTRA FORMA SI EL REGISTRO NO SE ENCONTRO O SIMPLEMENTE NO SE TIENE PERMISOS SE REENVIA UNA PAGINA DE ERROR
					request.getRequestDispatcher(usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=Lo sentimos, necesita verificar su nivel de acceso con el administrador para poder consultar este archivo...").forward(request,response);
				}*/
			//}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()) + " " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()) + " - Se generó una IOException en MuestraPublicacionS, mensaje: " + ioe.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()) + " " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()) + " - Se generó una Exception en MuestraPublicacionS, mensaje: " + e.getMessage());
		}
	}
}
