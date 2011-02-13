/**
 * Sistema de administraci�n de portales.
 *
 */

package com.rapidsist.portal.cliente;

import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.catalogos.CatalogoSLHome;
import com.rapidsist.portal.catalogos.LogicaNegocioException;
import com.rapidsist.portal.bitacora.datos.BitacoraSL;
import com.rapidsist.portal.bitacora.datos.BitacoraSLHome;
import com.rapidsist.portal.configuracion.Funcion;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.configuracion.PortalSLHome;
import com.rapidsist.portal.configuracion.PortalSL;
import com.rapidsist.portal.configuracion.Permisos;
import com.rapidsist.comun.bd.Registro;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Enumeration;
import java.io.BufferedReader;
import java.io.StringReader;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;
import java.util.Date;
import java.text.DateFormat;
import bsh.Interpreter;

/**
 * Servlet que lleva el control del componente de cat�logos.
 * <br/><br/>
 * Par�metros:
 * <ul>
 *	<li>
 * OperacionCatalogo.- Operaci�n que debe realizar el cat�logo<br>
 * Posibles valores: AL (alta), IN(inicializaci�n), MO (modificaci�n), BA (Baja),
 * CT (consulta todos), CR (consulta registro), CL (clonar registro).
 *  </li>
 *	<li>
 * Funcion.- Clave de la funci�n que se debe procesar
 *  </li>
 *	<li>
 * Filtro.- Clave del filtro que se debe procesar (este parametro solo es utilizado cuando
 * OperacionCatalogo=CT).
 *  </li>
 *	<li>
 * Ventana.- Le indica al servlet que el resultado se muestra en una ventana hija.
 *  </li>
 * </ul>
 */
public class ProcesaCatalogoS extends HttpServlet {
	private ServletConfig config;

	final public void init(ServletConfig config) throws ServletException {
		this.config = config;
	}

	final public ServletConfig getServletConfig() {
		return config;
	}

	/**
	 * Adminstra la operaci�n sobre los cat�logos del sistema.
	 * @param request Objeto que provee de informaci�n al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo env�a como un par�metro a este m�todo.
	 * @param response Objeto que asiste al servlet en enviar una respuesta HTTP al cliente. El
	 * contenedor  de servlets crea un objeto HttpServletResponse y lo env�a como par�metro a este m�todo.
	 */
	public void service (HttpServletRequest request, HttpServletResponse response){
		RegistroControl registroControl = new RegistroControl();
		CatalogoControl control = null;
		int iOperacionCatalogo = 0;
		String sVentana  = "";
		boolean bErrorBitacora = false;
		try{
			//SE VERIFICARA SI HA CADUCADO LA SESION DEL USUARIO
			HttpSession session = SesionUsuario.compruebaSesionUsuario(request, response);
			if (session == null){
				return;
			}

			//OBTIENE EL CONTEXTO DEL SERVIDOR DE APLICACIONES
			Context context = new InitialContext();

			//OBTIENE LA CLAVE DEL USUARIO QUE OPERA EL CATALOGO
			Usuario usuario = (Usuario)session.getAttribute("Usuario");
			//OBTIENE LA FUNCION A PROCESAR
			String sIdFuncion = request.getParameter("Funcion");

			//VERIFICA SI SE ENVIO EL PARAMETRO Funcion AL SERVLET
			if (sIdFuncion != null){

                //BUSCA LOS PERMISOS DE ACCESO PARA EL CATALOGO SELECCIONADO
				Object referencia = context.lookup("java:comp/env/ejb/PortalSL");
				PortalSLHome portalHome = (PortalSLHome)PortableRemoteObject.narrow(referencia, PortalSLHome.class);
				PortalSL configuracion = portalHome.create();

				//OBTIENE EL NOMBRE DE LA CLASE CONTROLADORA DE LA FUNCION
				Funcion funcion = configuracion.getFuncion(sIdFuncion);

				//VERIFICA SI ENCONTRO LA FUNCION
				if (funcion != null){

					//OBTIENE EL TIPO DE OPERACION A REALIZAR SOBRE EL CATALOGO
					String sOperacionCatalogo = request.getParameter("OperacionCatalogo");
					if (sOperacionCatalogo == null){
						//NO SE DEFINIO LA OPERACION A REALIZAR SOBRE EL CATALOGO
						response.sendRedirect(request.getContextPath() + usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=" + MensajeSistema.getMensajeSistema(101));
						return;
					}

					//OBTIENE LOS PERMISOS PARA LA PAGINA
					Permisos permiso = configuracion.validaAccesoFuncion(usuario.sAplicacionActual, usuario.sCvePerfilActual, sIdFuncion);

					//VERIFICA SI NO EXISTEN PERMISOS
					if (permiso == null){
						response.sendRedirect(request.getContextPath() + usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=" + MensajeSistema.getMensajeSistema(100));
						return;
					}

					//COMPARA LOS PERMISOS DE LA FUNCION CONTRA LA FUNCIONALIDAD SOLICITADA
					boolean bPermiso = true;

					//VERIFICA SI REALIZA LA CONSULTA DE UN REGISTRO
					if (sOperacionCatalogo.equals("CR") || sOperacionCatalogo.equals("CL")){
						if ( ! permiso.bConsulta){
							bPermiso = false;
						}
						else{
							iOperacionCatalogo = CatalogoControl.CON_CONSULTA_REGISTRO;
						}
					}
					else{
						//VERIFICA SI REALIZA UNA MODIFICACION
						if (sOperacionCatalogo.equals("MO")){
							if (! permiso.bModificacion) {
								bPermiso = false;
							}
							else{
								iOperacionCatalogo = CatalogoControl.CON_MODIFICACION;
							}
						}
						else {
							//VERIFICA SI REALIZA UN ALTA
							if (sOperacionCatalogo.equals("AL")){
								if ( !permiso.bAlta ){
									bPermiso = false;
								}
								else{
									iOperacionCatalogo = CatalogoControl.CON_ALTA;
								}
							}
							else{
								//VERIFICA SI REALIZA UNA BAJA
								if (sOperacionCatalogo.equals("BA")){
									if ( !permiso.bBaja) {
										bPermiso = false;
									}
									else{
										iOperacionCatalogo = CatalogoControl.CON_BAJA;
									}
								}
								else{
									//VERIFICA SI REALIZA UNA CONSULTA DE REGISTROS
									if (sOperacionCatalogo.equals("CT")){
										if ( !permiso.bConsulta) {
											bPermiso = false;
										}
										else{
											//VERIFICA SI NO SE ENVIO EL PARAMETRO DE FILTRO
											if (request.getParameter("Filtro") == null){
												response.sendRedirect(request.getContextPath() + usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=" + MensajeSistema.getMensajeSistema(104));
												return;
											}
											else{
												iOperacionCatalogo = CatalogoControl.CON_CONSULTA_TABLA;
											}
										}
									}
									else{
										//VERIFICA SI REALIZA UNA INICIALIZACION
										if (sOperacionCatalogo.equals("IN")){
											if ( !permiso.bConsulta) {
												bPermiso = false;
											}
											else{
												iOperacionCatalogo = CatalogoControl.CON_INICIALIZACION;
											}
										}
										else{
											//VERIFICA SI REALIZA UNA INICIALIZACION
											if (sOperacionCatalogo.equals("IA")){
												if ( !permiso.bAlta) {
													bPermiso = false;
												}
												else{
													iOperacionCatalogo = CatalogoControl.CON_INICIALIZACION_ALTA;
												}
											}
											else{
												//NO SE DEFINIO LA OPERACION A REALIZAR SOBRE EL CATALOGO
												response.sendRedirect(request.getContextPath() + usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=" + MensajeSistema.getMensajeSistema(102));
												return;
											}
										}
									}
								}
							}
						}
					}

					//VERIFICA SI SE DETECTO ALGUNA INFRACCION DE PERMISOS
					if (! bPermiso ) {
						response.sendRedirect(request.getContextPath() + usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=" + MensajeSistema.getMensajeSistema(100));
						return;
					}

					//INICIALIZA EL EJB DE CATALOGOS
					Object referenciaCatalogo = context.lookup("java:comp/env/ejb/CatalogoSL");
					CatalogoSLHome catalogoHome = (CatalogoSLHome)PortableRemoteObject.narrow(referenciaCatalogo, CatalogoSLHome.class);
					CatalogoSL catalogoEjb = catalogoHome.create();

					//VERIFICA SI LA FUNCION TIENE ASIGNADA UNA CLASE CONTROLADORA
					if (funcion.getClaseCon() != null && !funcion.getClaseCon().equals("")){
						//SE INSTANCIA LA CLASE CONTROLADORA
						Class cClase = null;
						Object instanciaObj=null;
						cClase = Class.forName(funcion.getClaseCon());
						instanciaObj=cClase.newInstance();
						control = (CatalogoControl)instanciaObj;
					}
					
					//BTIACORA LA LLAMADA A LA FUNCION POR PARTE DEL USUARIO
					Object referenciaBitacora = context.lookup("java:comp/env/ejb/BitacoraSL");
					BitacoraSLHome bitacoraH = (BitacoraSLHome)PortableRemoteObject.narrow(referenciaBitacora, BitacoraSLHome.class);
					BitacoraSL bitacoraEjb = bitacoraH.create();

					//SI NO ACTUALIZA LA BITACORA ENVIA UN MENSAJE DE ERROR A LA CONSOLA
					bitacoraEjb.registraVisitaFuncion(usuario.sCveGpoEmpresa, usuario.sCvePortal, sIdFuncion, funcion.getNombre(), usuario.sCveUsuario, usuario.sNomCompleto, request.getRequestURI(), request.getQueryString(), request.getRemoteAddr());
										
					Registro registroActualizacion = new Registro();
					//VERIFICA SI SE TRATA DE UNA CONSULTA
					if (sOperacionCatalogo.equals("CT") || sOperacionCatalogo.equals("CR") || sOperacionCatalogo.equals("IN") || sOperacionCatalogo.equals("CL")){

						//AGREGA LA CLAVE DE ENTIDAD A LOS PARAMETROS DE ENTRADA A LA CONSULTA
						Registro parametros = new Registro();
						parametros.addDefCampo("CVE_GPO_EMPRESA", usuario.sCveGpoEmpresa);
						parametros.addDefCampo("CVE_USUARIO", usuario.sCveUsuario);
						parametros.addDefCampo("CVE_EMPRESA", usuario.sCveEmpresa);

						//VERIFICA SI ES UNA CONSULTA CON UN FILTRO DE DATOS
						if (iOperacionCatalogo == CatalogoControl.CON_CONSULTA_TABLA){
							parametros.addDefCampo("Filtro", request.getParameter("Filtro"));
						}
						try{
							//EJECUTA EL METODO DE CONSULTA DE LA CLASE DE CONTROL

							//VERIIFICA SI LA OPERACION ES CLONAR-REGISTRO
							if (sOperacionCatalogo.equals("CL")){
								//REALIZA EL LLAMADO UTILIZANDO LA OPERACION CONSULTA-REGISTRO
								if (control != null){
									CatalogoControlConsultaIN catConsulta = (CatalogoControlConsultaIN)control;
									registroControl = catConsulta.consulta(parametros, request, response, config, catalogoEjb, context, CatalogoControl.CON_CONSULTA_REGISTRO);
								}
								else{
									registroControl = ejecutaOperacion(funcion, parametros, request, response, config, catalogoEjb, context, CatalogoControl.CON_CONSULTA_REGISTRO);
								}
								//LE ENVIA A LA PAGINA LA CLAVE DE OPERACION INICIALIZACION
								registroControl.sPagina = registroControl.sPagina + "?OperacionCatalogo=IN";
							}
							else{
								if (control != null){
									CatalogoControlConsultaIN catConsulta = (CatalogoControlConsultaIN)control;
									registroControl = catConsulta.consulta(parametros, request, response, config, catalogoEjb, context, iOperacionCatalogo);
								}
								else{
									registroControl = ejecutaOperacion(funcion, parametros, request, response, config, catalogoEjb, context, iOperacionCatalogo);
								}
							}
						}
						catch (RemoteException e) {
							registroControl.sPagina = usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=Se gener� un error interno en el servidor de l�gica de negocio";
							System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()) + " " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()) + " - Error en clase: " + funcion.getClaseCon() + ", mensaje: " + e.getMessage());
						}
						catch (Exception e) {
							registroControl.sPagina = usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=Se gener� un error interno en el servidor cliente";
							System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()) + " " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()) + " - Error en clase: " + funcion.getClaseCon() + ", mensaje: " + e.getMessage());
							e.printStackTrace();
						}
						//VERIFICA SI SOLO DESEA REALIZAR UNA CONSULTA Y NO MODIFICAR EL REGISTRO ORIGINAL
						if (request.getParameter("Consulta") == null) {
							Registro registroRespuesta = (Registro) registroControl.respuesta.getDefCampo("registro");
							if (registroRespuesta != null) {
								session.setAttribute("RegistroOriginal", registroRespuesta);
							}
						}
					}
					else{
						//AGREGA LA CLAVE DEL USUARIO QUE REALIZA LA ACTUALIZACION Y LA CLAVE DE
						//ENTIDAD A LOS PARAMETROS DE ENTRADA DE LA ACTUALIZACION
						registroActualizacion.addDefCampo("CVE_USUARIO_BITACORA", usuario.sCveUsuario);
						registroActualizacion.addDefCampo("CVE_GPO_EMPRESA", usuario.sCveGpoEmpresa);
						registroActualizacion.addDefCampo("CVE_USUARIO", usuario.sCveUsuario);
						registroActualizacion.addDefCampo("CVE_EMPRESA", usuario.sCveEmpresa);

						//AGREGA EL REGISTRO ORIGINAL QUE SE ENCUENTRA ALMACENADO EN LA SESION.
						//ESTA FUNCIONALIDAD ES PARA IMPLEMENTAR EL 'RE-READ' QUE DETECTA SI EL REGISTRO
						//FUE MODIFICADO POR OTRO USUARIO
						Registro registroOriginal = (Registro)session.getAttribute("RegistroOriginal");
						if (registroOriginal != null){
							registroActualizacion.addDefCampo("RegistroOriginal", registroOriginal);
						}

						try{

							if (control != null){
								//VERIFICA SI EXISTE EN LA SESION UN REGISTRO QUE HAYA SIDO DADO DE ALTA
								Registro registroPrimerSubmit = (Registro)session.getAttribute("RegistroPrimerSubmit");
								if (registroPrimerSubmit != null){
									registroActualizacion.addDefCampo("RegistroPrimerSubmit", registroPrimerSubmit);
								}
								
								CatalogoControlActualizaIN catActualiza = (CatalogoControlActualizaIN)control;
								registroControl = catActualiza.actualiza(registroActualizacion, request, response, config, catalogoEjb, context, iOperacionCatalogo);
								//ALMACENA EN LA SESION EL REGISTRO QUE HAYA SIDO DADO DE ALTA
								if (iOperacionCatalogo == CatalogoControl.CON_ALTA){
									session.setAttribute("RegistroPrimerSubmit", registroControl.resultadoCatalogo.Resultado.getDefCampo("RegistroPrimerSubmit"));
								}
							}
							else{
								registroControl = ejecutaOperacion(funcion, registroActualizacion, request, response, config, catalogoEjb, context, iOperacionCatalogo);
							}

							//VERIFICA SI SE OBTUVO UNA RESPUESTA DE LA OPERACION INDICADA
							if (registroControl.resultadoCatalogo == null) {
								registroControl.sPagina = usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=No se definieron registros para la operaci�n indicada";
							}
							else{

								//VERIFICA SI SE GENERO ALGUN ERROR DE LOGICA DE NEGOCIO EN EL EJB							
								if (registroControl.resultadoCatalogo.mensaje.getClave() != null) {
									registroControl.sPagina = usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=" + registroControl.resultadoCatalogo.mensaje.getTipo() + " - " + registroControl.resultadoCatalogo.mensaje.getDescripcion();
								}
								else{
									//VERIFICA SI EN LA OPERACI�N SE GENER� UN SEQUENCE Y LO ENV�A PARA SER USADO POR LA BIT�CORA
									if(registroControl.resultadoCatalogo.Resultado != null){
										if (registroControl.resultadoCatalogo.Resultado.getDefCampo("SEQUENCE") != null) {
											registroActualizacion.addDefCampo("SEQUENCE", registroControl.resultadoCatalogo.Resultado.getDefCampo("SEQUENCE"));
										}
									}
									// SE AGREGA EL TIPO DE OPERACI�N PARA GENERAR EL ENCABEZADO DE LA BIT�CORA
									registroActualizacion.addDefCampo("OPERACION", String.valueOf(iOperacionCatalogo));
	
									//SI NO SE GENERO NINGUN MENSAJE DE ERROR ENTOCES SE DEBE BITACORAR
									//SE LLAMA AL COMPONENTE DE BIT�CORA AL INICIALIZAR EL EJB DE BITACORA
									try{
										//SI NO ACTUALIZA LA BITACORA ENVIA UN MENSAJE DE ERROR A LA CONSOLA
										String sResultadoBitacora = bitacoraEjb.registraBitacora(sIdFuncion, registroActualizacion);
	
										// VERIFICA SI SE GENER� ALG�N ERROR AL ACTUALIZAR LA BITACORA
									   // PARA DESHABILITAR LA FUNCIONALIDAD DE QUE SE MUESTREN EN PANTALLA LOS ERRORES DE
									   // LA BIT�CORA SOLO DEBE COMENTAR EL SIGUIENTE M�TODO
	
									   // M�TODO A COMENTAR ***********************************************************************
										if (sResultadoBitacora != null && !sResultadoBitacora.equals("")){
											// SE GENER� UN ERROR
											bErrorBitacora = true;
											//VERIFICA EL PAR�METRO VENTANA
											if (sVentana != null) {
												if (sVentana.equals("Si")) {
													sVentana = "Ventana=Si";
												}
											}
											else {
												sVentana = "";
											}
	
											// SE RECUPERA LA URL ORIGINAL PARA REDIRECCIONAR LA P�GINA DESPU�S DE ENVIARLA A LA P�GINA DE ERROR
											String sUrlFinalErrorBitacora = Url.agregaParametro(request.getContextPath() + registroControl.sPagina, sVentana);
											// SE ENV�A LA URL ORIGINAL EN EL REQUEST
											request.setAttribute("UrlOriginal", sUrlFinalErrorBitacora);
											// SE REDIRECCIONA A LA P�GINA DE ERROR CON EL MENSAJE DE ERROR PROVENIENTE DE LA ACTUALIZACI�N DE LA BIT�CORA
											registroControl.sPagina = usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=" + sResultadoBitacora ;
											//SE ENV�A EL MENSAJE A LA CONSOLA
											//System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()) + " " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()) + " - " +sResultadoBitacora);
										} // M�TODO A COMENTAR *************************************************************************/
									}
									catch(LogicaNegocioException e) {
										System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()) + " " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()) + " - Error en la bitacora, mensaje: " + e.getMessage());
									}
									catch(Exception e){
										System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()) + " " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()) + " - Error en la bitacora, mensaje: " + e.getMessage());
									}
								}//VERIFICA SI SE GENERO ALGUN ERROR DE LOGICA DE NEGOCIO EN EL EJB
							}//VERIFICA SI SE OBTUVO UNA RESPUESTA DE LA OPERACION INDICADA

						}
						catch(LogicaNegocioException e) {
							registroControl.sPagina= usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=Se gener� un error interno en el servidor de l�gica de negocio";
							System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()) + " " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()) + " - Error en clase: " + funcion.getClaseCon() + ", mensaje: " + e.getMessage());
						}
						catch(Exception e){
							registroControl.sPagina= usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=Se gener� un error interno en el servidor cliente";
							System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()) + " " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()) + " - Error en clase: " + funcion.getClaseCon() + ", mensaje: " + e.getMessage());
							e.printStackTrace();
						}
					}//VERIFICA SI SE TRATA DE UNA CONSULTA

					//VERIFICA SI EL SERVLET REGRESA LA LISTA GENERICA DE OBJETOS
					if (registroControl.lista != null){
						//REGRESA UNA LISTA GENERICA DE OBJETOS
						request.setAttribute("Lista", registroControl.lista.iterator());
					}
					else{
						//REGRESA EN LA SESION TEMPORAL DE LA RESPUESTA (REQUEST) LOS OBJETOS
						//DEFINIDOS EN LA CLASE CONTROLADORA QUE SE EJECUTO
						Iterator listaNombresCampos = registroControl.respuesta.getCampos().keySet().iterator();
						while (listaNombresCampos.hasNext()){
							String sNombreCampo = (String)listaNombresCampos.next();
							Object objetoLista = registroControl.respuesta.getDefCampo(sNombreCampo);
							if (objetoLista != null){
								if (objetoLista instanceof LinkedList){
									LinkedList listaRespuesta = (LinkedList)objetoLista;
									request.setAttribute(sNombreCampo, listaRespuesta.iterator());
								}
								else{
									request.setAttribute(sNombreCampo, objetoLista);
								}
							}
						}
					}
					//ENVIA EL CONTROL A LA PAGINA SELECCIONADA
					if (sOperacionCatalogo.equals("CT") || sOperacionCatalogo.equals("CR") || sOperacionCatalogo.equals("IN") || sOperacionCatalogo.equals("CL")){
						request.getRequestDispatcher(registroControl.sPagina).forward(request,response);
					}
					else{
						sVentana = request.getParameter("Ventana");
						if (sVentana != null) {
							if (sVentana.equals("Si")) {
								sVentana = "Ventana=Si";
							}
						}
						else {
							sVentana = "";
						}
						String sUrlFinal = Url.agregaParametro(request.getContextPath() + registroControl.sPagina, sVentana);
						// SI SE PRODUJO ALG�N ERROR EN LA GENERACI�N DE LA BIT�CORA SE ENV�A EL REQUEST
						// CON LA URL ORIIGNAL A LA P�GINA DE ERROR
						if(bErrorBitacora){
							request.getRequestDispatcher(registroControl.sPagina).forward(request,response);
						}
						else{
							response.sendRedirect(sUrlFinal);
						}
					}
				}
				else{
					//NO SE ENCONTRO LA FUNCION SOLICITADA EN LA BASE DE DATOS
					response.sendRedirect(request.getContextPath() + usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=" + MensajeSistema.getMensajeSistema(107));
					return;
				}//FUNCION ENCONTRADA
			}
			else{
				//NO SE DEFINIO LA FUNCION QUE EL PROCESADOR DE CATALOGOS DEBE UTILIZAR
				response.sendRedirect(request.getContextPath() + usuario.getUrlDirectorioDefault() + "/SistemaErrorPagina.jsp?Mensaje=" + MensajeSistema.getMensajeSistema(103));
				return;
			}//VERIFICA SI SE ENVIO EL PARAMETRO Funcion AL SERVLET

		}
		catch(Exception e){
			System.out.println(DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()) + " " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()) + " - Error en ProcesaCatalogoS, mensaje: " + e.getMessage());
		}
	}
	
	/**
	 * @param funcion Objeto tipo Funcion.
	 * @param parametros Objeto Registro.
	 * @param request Objeto que provee de informaci�n al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo env�a como un par�metro a este m�todo.
	 * @param response Objeto que asiste al servlet en enviar una respuesta HTTP al cliente. El
	 * contenedor  de servlets crea un objeto HttpServletResponse y lo env�a como par�metro a este m�todo.
	 * @param config
	 * @param catalogoEjb
	 * @param context Objeto tipo Context.
	 * @param iOperacionCatalogo Tipo de operaci�n.
	 * @return registroControl
	 * @throws RemoteException Se gener� un error al ejecutar la operci�n.
	 * @throws java.lang.Exception Si hubo alg�n error en la clase controladora.
	 */

	private RegistroControl ejecutaOperacion(Funcion funcion, Registro parametros, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoEjb, Context context, int iOperacionCatalogo) throws RemoteException, Exception{

		//IMPRIME LOG EN CONSOLA
		if (funcion.getBRastreaCodigo().equals("V")){
			System.out.println("\nEjecuta las operaciones CON de la base de datos");
		}

		RegistroControl registroControl = null;
		RegistroControl registroControlsalida = new RegistroControl();

		Interpreter interprete = new Interpreter();
		interprete.set("request", request);
		interprete.set("response", response);
		interprete.set("config", config);
		interprete.set("parametros", parametros);
		interprete.set("catalogoSL", catalogoEjb);
		interprete.set("contexto", context);
		interprete.set("iTipoOperacion", iOperacionCatalogo);
		interprete.set("registroControl", registroControlsalida);
		Enumeration listaParametrosRequest = request.getParameterNames();
		String sParametroRequest = "";

		int iNumParametros = 0;
		while(listaParametrosRequest.hasMoreElements()){
			sParametroRequest = (String)listaParametrosRequest.nextElement();
			//IMPRIME LOG EN CONSOLA
			if (funcion.getBRastreaCodigo().equals("V")){
				if (iNumParametros == 0){
					System.out.println("Se agregan parametros del request");
					iNumParametros = 1;
				}
				System.out.println("parametro: " + "$" + sParametroRequest);
				System.out.println("valor: " + request.getParameter(sParametroRequest));
			}
			interprete.set("$" + sParametroRequest, request.getParameter(sParametroRequest));
		}

		String sOperacion = "";
		if (iOperacionCatalogo == CatalogoControl.CON_CONSULTA_TABLA){
			Registro operacion = (Registro)funcion.getListaOperaciones().get("CON_CONSULTA_TABLA");
			if (operacion != null){
				sOperacion = (String)operacion.getDefCampo("TX_OPERACION");

				//IMPRIME LOG EN CONSOLA
				if (funcion.getBRastreaCodigo().equals("V")){
					System.out.println("\nEl codigo a ejecutarse se muestra linea a linea.");
					interprete.eval(agregaComentarios(sOperacion));
				}
				else{
					interprete.eval(sOperacion);
				}
			}
		}
		else if (iOperacionCatalogo == CatalogoControl.CON_INICIALIZACION){
			Registro operacion = (Registro)funcion.getListaOperaciones().get("CON_INICIALIZACION");
			if (operacion != null) {
				sOperacion = (String)operacion.getDefCampo("TX_OPERACION");

				//IMPRIME LOG EN CONSOLA
				if (funcion.getBRastreaCodigo().equals("V")){
					System.out.println("\nEl codigo a ejecutarse se muestra linea a linea.");
					interprete.eval(agregaComentarios(sOperacion));
				}
				else{
					interprete.eval(sOperacion);
				}
			}
		}
		else if (iOperacionCatalogo == CatalogoControl.CON_CONSULTA_REGISTRO){
			Registro operacion = (Registro)funcion.getListaOperaciones().get("CON_CONSULTA_REGISTRO");
			if (operacion != null){
				sOperacion = (String)operacion.getDefCampo("TX_OPERACION");

				//IMPRIME LOG EN CONSOLA
				if (funcion.getBRastreaCodigo().equals("V")){
					System.out.println("\nEl codigo a ejecutarse se muestra linea a linea.");
					interprete.eval(agregaComentarios(sOperacion));
				}
				else{
					interprete.eval(sOperacion);
				}
			}
		}
		else if (iOperacionCatalogo == CatalogoControl.CON_ALTA
					|| iOperacionCatalogo == CatalogoControl.CON_BAJA
					|| iOperacionCatalogo == CatalogoControl.CON_MODIFICACION){
			Registro operacion = (Registro)funcion.getListaOperaciones().get("CON_ACTUALIZACION");
			if (operacion != null){
				sOperacion = (String)operacion.getDefCampo("TX_OPERACION");

				//IMPRIME LOG EN CONSOLA
				if (funcion.getBRastreaCodigo().equals("V")){
					System.out.println("\nEl codigo a ejecutarse se muestra linea a linea.");
					interprete.eval(agregaComentarios(sOperacion));
				}
				else{
					interprete.eval(sOperacion);
				}
			}
		}
		registroControl = (RegistroControl)interprete.get("registroControl");
		return registroControl;
	}

	/**
	 * @param sOperacion Tipo de operacion
	 */
	private String agregaComentarios (String sOperacion){
		StringBuffer sOperacionComentarios = new StringBuffer();
		try{
			BufferedReader operacion = new BufferedReader(new StringReader(sOperacion));
			String sLineaCodigo = operacion.readLine();
			while(sLineaCodigo != null){
				char caracterOriginal = 34;
				char caracterNuevo = 39;
				String sLineaCodigoTemporal = sLineaCodigo.replace(caracterOriginal, caracterNuevo);
				String sLineaCodigoComentado = "System.out.println(\"Se ejecuta: " + sLineaCodigoTemporal + "\");\n";
				sOperacionComentarios.append(sLineaCodigoComentado);
				sOperacionComentarios.append(sLineaCodigo + "\n");
				sLineaCodigo = operacion.readLine();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return sOperacionComentarios.toString();
	}
}