/**
 * Sistema de administraci�n de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente.reportes;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import it.businesslogic.ireport.export.JRTxtExporter;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.bitacora.datos.BitacoraSL;
import com.rapidsist.portal.bitacora.datos.BitacoraSLHome;
import com.rapidsist.portal.configuracion.Funcion;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.configuracion.PortalSLHome;
import com.rapidsist.portal.configuracion.PortalSL;
import com.rapidsist.portal.configuracion.Permisos;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.catalogos.CatalogoSLHome;
import com.rapidsist.portal.cliente.MensajeSistema;
import com.rapidsist.portal.cliente.SesionUsuario;
import javax.rmi.PortableRemoteObject;

public class ProcesaReporteS extends HttpServlet{
	
	/**
	 *
	 * @param request Objeto que provee de informaci�n al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo env�a como un par�metro a este m�todo.
	 * @param response Objeto que asiste al servlet en enviar una respuesta HTTP al cliente. El
	 * contenedor  de servlets crea un objeto HttpServletResponse y lo env�a como par�metro a este m�todo.
	 */

	public void service(HttpServletRequest request,	HttpServletResponse response){
		Conexion2 conexion = new Conexion2();
		Connection conexionBd = null;
		try{
			//SE VERIFICARA SI HA CADUCADO LA SESION DEL USUARIO
			HttpSession session = SesionUsuario.compruebaSesionUsuario(request, response);
			if (session == null){
				return;
			}

			//OBTIENE LA CLAVE DEL USUARIO QUE OPERA EL CATALOGO
			Usuario usuario = (Usuario)session.getAttribute("Usuario");

			//OBTIENE LA FUNCION A PROCESAR
			String sIdFuncion = request.getParameter("Funcion");
			
			//NOMBRE DEL REPORTE
			String sNombreReporte = null;
			String sNombreExtension = null;

			//VERIFICA SI SE ENVIO EL PARAMETRO Funcion AL SERVLET
			if (sIdFuncion != null){

				//OBTIENE EL CONTEXTO DEL SERVIDOR DE APLICACIONES
				Context context = new InitialContext();
				Object referencia = context.lookup("java:comp/env/ejb/PortalSL");			
				PortalSLHome portalHome = (PortalSLHome)PortableRemoteObject.narrow(referencia, PortalSLHome.class);			
				PortalSL configuracion = portalHome.create();

				//OBTIENE EL NOMBRE DE LA CLASE CONTROLADORA DE LA FUNCION
				Funcion funcion = configuracion.getFuncion(sIdFuncion);

				//VERIFICA SI ENCONTRO LA FUNCION
				if (funcion != null){
					
					//OBTIENE LOS PERMISOS PARA LA PAGINA
					Permisos permiso = configuracion.validaAccesoFuncion(usuario.sAplicacionActual, usuario.sCvePerfilActual, sIdFuncion);

					//VERIFICA SI NO EXISTEN PERMISOS
					if (permiso == null){
						response.sendRedirect(request.getContextPath() + "/comun/FormasInfraestructura/fGralMen.jsp?Mensaje=" + MensajeSistema.getMensajeSistema(100));
						return;
					}

					//VERIFICA SI SE DETECTO ALGUNA INFRACCION DE PERMISOS
					if (! permiso.bConsulta ) {
						response.sendRedirect( request.getContextPath() + "/comun/FormasInfraestructura/fGralMen.jsp?Mensaje=" + MensajeSistema.getMensajeSistema(100));
						return;
					}
					
					//BTIACORA LA LLAMADA A LA FUNCION POR PARTE DEL USUARIO
					Object referenciaBitacora = context.lookup("java:comp/env/ejb/BitacoraSL");
					BitacoraSLHome bitacoraH = (BitacoraSLHome)PortableRemoteObject.narrow(referenciaBitacora, BitacoraSLHome.class);
					BitacoraSL bitacoraEjb = bitacoraH.create();

					//SI NO ACTUALIZA LA BITACORA ENVIA UN MENSAJE DE ERROR A LA CONSOLA
					bitacoraEjb.registraVisitaFuncion(usuario.sCveGpoEmpresa, usuario.sCvePortal, sIdFuncion, funcion.getNombre(), usuario.sCveUsuario, usuario.sNomCompleto, request.getRequestURI(), request.getQueryString(), request.getRemoteAddr());
					

					//INICIALIZA EL EJB DE CATALOGOS
					Object referenciaCatalogo = context.lookup("java:comp/env/ejb/CatalogoSL");			
					CatalogoSLHome catalogoH = (CatalogoSLHome)PortableRemoteObject.narrow(referenciaCatalogo, CatalogoSLHome.class);			
					CatalogoSL catalogoEjb = catalogoH.create();

					//OBTIENE EL CONTEXTO DE LA APLICACION WEB
					ServletContext contextoServlet = this.getServletConfig().getServletContext();
					Map parametros = null;
					try{
					
						//SE INSTANCIA LA CLASE CONTROLADORA DEL REPORTE
						
						Class claseErp = this.getClass().getClassLoader().loadClass(funcion.getNomClaseReporte());
						Object instanciaObj = claseErp.newInstance();
						
						//EJECUTA LA CLASE CONTROLADORA DEL REPORTE Y OBTIENE LOS PARAMETROS
						ReporteControlIN parametrosReporte = (ReporteControlIN)instanciaObj;
						Registro parametrosCatalogo = new Registro();
						parametrosCatalogo.addDefCampo("CVE_GPO_EMPRESA", usuario.sCveGpoEmpresa);
						parametrosCatalogo.addDefCampo("CVE_USUARIO", usuario.sCveUsuario);
						parametrosCatalogo.addDefCampo("CVE_EMPRESA", usuario.sCveEmpresa);
						parametros = parametrosReporte.getParametros(parametrosCatalogo, request, catalogoEjb, context, contextoServlet);
						
					}
					catch(Exception e){
						System.out.println("Se encontr� un error en la clase controladora del reporte: " + funcion.getNomClaseReporte() + " \n exception: " + e.getClass().getName() + ", mensaje:  " + e.getMessage());
						e.printStackTrace();
						response.sendRedirect( request.getContextPath() + "/comun/FormasInfraestructura/fGralMen.jsp?Mensaje=" + MensajeSistema.getMensajeSistema(110));
						return;
					}

					if (request.getParameter("TipoReporte") != null){
						
						//ABRE CONEXION A BASE DE DATOS
						if(request.getParameter("Bd")!=null){
							//ABRE CONEXION A MYSQL
							try{
								System.out.println("Conexion a Mysql");
								Class.forName("com.mysql.jdbc.Driver");
								conexionBd = DriverManager.getConnection("jdbc:mysql://localhost:3307/sim", "root","system");
								
							}catch(Exception e){
					            System.out.println("******************************************************");
					            System.out.println("Error al abrir la conexion a la base de datos Mysql");
					            System.out.println("");
					            System.out.println("Descripcion del error:" + e.getMessage());
					            System.out.println("******************************************************");
					            e.printStackTrace();
					            conexionBd.close();
					            throw new SQLException();		
							}							
						}else{
							//ABRE CONEXION A BASE DE DATOS
							conexion.abreConexion("java:comp/env/jdbc/PortalDs");
							conexionBd = conexion.getConexion();
						}
						
						ByteArrayOutputStream reporte = new ByteArrayOutputStream();

						//VERIFICA EL TIPO DE REPORTE QUE DEBE GENERAR
						if(request.getParameter("TipoReporte").equals("Pdf")){
							//TIPO DE REPORTE PDF
							reporte.write(JasperRunManager.runReportToPdf(contextoServlet.getRealPath((String)parametros.get("NomReporte")), parametros, conexionBd));
						}
						else if (request.getParameter("TipoReporte").equals("Xls")){
							//TIPO DE REPORTE XLS
							JasperPrint jasperPrint = null;
							jasperPrint = JasperFillManager.fillReport(contextoServlet.getRealPath((String)parametros.get("NomReporte")), parametros, conexionBd);
							JRXlsExporter exporter = new JRXlsExporter();
							//ASIGNA PARAMETROS DE EXPORTACION PARA LA HOJA DE CALCULO
							
							exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
							exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, reporte);
							exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
							exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
							exporter.exportReport();
						}
						else if (request.getParameter("TipoReporte").equals("Txt")){
							//TIPO DE REPORTE TXT
							JasperPrint jasperPrint = null;
							jasperPrint = JasperFillManager.fillReport(contextoServlet.getRealPath((String)parametros.get("NomReporte")), parametros, conexionBd);
							JRTxtExporter exporter = new JRTxtExporter();
							//ASIGNA PARAMETROS DE EXPORTACION PARA EL ARCHIVO TEXTO
							exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
							exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, reporte);
							exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
							exporter.exportReport();
						}
						else if (request.getParameter("TipoReporte").equals("Html")){
							//TIPO DE REPORTE HTML
							JasperPrint jasperPrint = null;
							jasperPrint = JasperFillManager.fillReport(contextoServlet.getRealPath((String)parametros.get("NomReporte")), parametros, conexionBd);
							JRHtmlExporter exporter = new JRHtmlExporter();
							//ASIGNA PARAMETROS DE EXPORTACION PARA EL ARCHIVO HTML
							exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
							exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, reporte);
							exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
							exporter.exportReport();
						}
						else if (request.getParameter("TipoReporte").equals("Csv")){
							//TIPO DE REPORTE CSV
							JasperPrint jasperPrint = null;
							jasperPrint = JasperFillManager.fillReport(contextoServlet.getRealPath((String)parametros.get("NomReporte")), parametros, conexionBd);
							JRCsvExporter exporter = new JRCsvExporter();
							//ASIGNA PARAMETROS DE EXPORTACION PARA EL ARCHIVO HTML
							exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
							exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, reporte);
							exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
							exporter.exportReport();
						}
						//VERIFICA SI SE GENERO EL REPORTE
						if (reporte.size() > 0){
							ServletOutputStream ouputStream = null;
							
							sNombreReporte = (String)parametros.get("NombreReporte");
							sNombreExtension = (String)parametros.get("NombreExtension");
							
							if (sNombreReporte == null){
								sNombreReporte = "reporte";
							}
						
							//VERIFICA EL TIPO DE REPORTE A IMPRIMIR
							if (request.getParameter("TipoReporte").equals("Pdf")){
								response.setContentType("application/pdf");
							}
							else if (request.getParameter("TipoReporte").equals("Xls")){
								response.setHeader("Content-Disposition", "attachment; filename=\" "+sNombreReporte +".xls \"");
								response.setContentType("application/octet-stream");
							}
							else if (request.getParameter("TipoReporte").equals("Txt")){
								response.setHeader("Content-Disposition", "attachment; filename=\" "+sNombreReporte +".txt \"");
								response.setContentType("application/octet-stream");
							}
							else if (request.getParameter("TipoReporte").equals("Html")){
								response.setHeader("Content-Disposition", "attachment; filename=\" "+sNombreReporte +".html \"");
								response.setContentType("application/octet-stream");
							}
							else if (request.getParameter("TipoReporte").equals("Csv")){

								//VERIFICA SI SE ENVIO EL NOMBRE DE LA EXTENCION								
								if (sNombreExtension == null){
									sNombreExtension = "csv";
								}

								response.setHeader("Content-Disposition", "attachment; filename=\" "+sNombreReporte +"."+sNombreExtension+" \"");
								response.setContentType("application/octet-stream");
							}							
							
							response.setContentLength(reporte.size());
							ouputStream = response.getOutputStream();
							ouputStream.write(reporte.toByteArray());
							reporte.close();
							ouputStream.close();
						}
						else{
						
							response.sendRedirect( request.getContextPath() + "/comun/FormasInfraestructura/fGralMen.jsp?Mensaje=" + MensajeSistema.getMensajeSistema(109));
							return;
						}
					}
					else{
					
						response.sendRedirect( request.getContextPath() + "/comun/FormasInfraestructura/fGralMen.jsp?Mensaje=" + MensajeSistema.getMensajeSistema(108));
						return;
					}//VERFICA SI SE DEFINIO EL TIPO DE REPORTE A IMPRIMIR
				}
				else{
			
					//NO SE ENCONTRO LA FUNCION SOLICITADA EN LA BASE DE DATOS
					response.sendRedirect(request.getContextPath() + "/comun/FormasInfraestructura/fGralMen.jsp?Mensaje=" + MensajeSistema.getMensajeSistema(107));
					return;
				}
			}
			else{
		
				//NO SE DEFINIO LA FUNCION QUE EL PROCESADOR DE CATALOGOS DEBE UTILIZAR
				response.sendRedirect(request.getContextPath() + "/comun/FormasInfraestructura/fGralMen.jsp?Mensaje=" + MensajeSistema.getMensajeSistema(106));
				return;
			}//VERIFICA SI SE ENVIO EL PARAMETRO Funcion AL SERVLET
		}
		catch (JRException e){
			try {
				
				String sPagina="/comun/FormasInfraestructura/fGralMen.jsp?Mensaje=" + "Se gener� un error al ejecutar el reporte";
				response.sendRedirect(request.getContextPath() + sPagina);
				e.printStackTrace();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		catch(Exception e){
			e.printStackTrace();
			
			System.out.println("Se genero un error en el servlet ProcesaReporteS, exception: " + e.getClass().getName() + ", mensaje:  " + e.getMessage());
		}
		finally{
			//Verifica que se haya cerrado la conexion a mysql
			try {
				if (conexionBd.isClosed()){
					conexionBd.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Verifica que se cierre la conexion en la base de datos
			if (conexion.isConectado()){
				conexion.cierraConexion();
			}

		}
	}
}

