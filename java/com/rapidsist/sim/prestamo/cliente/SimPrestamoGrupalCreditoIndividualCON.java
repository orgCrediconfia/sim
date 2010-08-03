/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.LinkedList;



/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificación y consulta) de los productos asigandos a los préstamos. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimPrestamoGrupalCreditoIndividualCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

	/**
	 * Ejecuta los servicios de consulta del catálogo.
	 * @param parametros Parámetros que se recogen de la sesion del usuario y se le envian a la clase CON.
	 * Estos parámetros son: CVE_GPO_EMPRESA (Clave del grupo empresa), Filtro (el valor
	 * del filtro que se debe aplicar solo si se ejecuto el componente de catálogos con
	 * OperacionCatalogo=CT)
	 * @param request Objeto que provee de información al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envía como un parámetro a este método.
	 * @param response Objeto que provee de información del servlet sobre el response del cliente. El
	 * contenedor de servlets crea un objeto HttpServletResponse y lo envía como un parámetro a este método.
	 * @param config Objeto que provee de información del servlet sobre el ServletConfig del cliente. El
	 * contenedor de servlets crea un objeto ServletConfig y lo envía como un parámetro a este método.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON
	 * @param contexto Objeto que contiene información acerca del entorno del servidor de
	 * aplicaciones.
	 * @param iTipoOperacion Operación que debe ejecutar la clase CON. Las operaciones se encuentran
	 * especificadas en la clase {@link com.rapidsist.portal.cliente.CatalogoControl CatalogoControl}
	 * @return Resultado de la consulta y la página a donde se redirecciona el control.
	 * @throws RemoteException Si se generó un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se generó un error dentro de la clase CON.
	 */
	public RegistroControl consulta(Registro parametros, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		//VERIFICA SI BUSCA TODOS LOS REGISTROS
		if (iTipoOperacion == CON_CONSULTA_TABLA){
			//VERIFICA SI SE ENVIO EL PARAMETRO NOMBRE
			if (request.getParameter("IdPrestamo") != null && !request.getParameter("IdPrestamo").equals("")){
				parametros.addDefCampo("ID_PRESTAMO", request.getParameter("IdPrestamo"));
			}
			if (request.getParameter("IdProducto") != null && !request.getParameter("IdProducto").equals("")){
				parametros.addDefCampo("ID_PRODUCTO", request.getParameter("IdProducto"));
			}
			if (request.getParameter("NumCiclo") != null && !request.getParameter("NumCiclo").equals("")){
				parametros.addDefCampo("NUM_CICLO", request.getParameter("NumCiclo"));
			}
			if (request.getParameter("FechaInicioSolicitud") != null && !request.getParameter("FechaInicioSolicitud").equals("")){
				parametros.addDefCampo("FECHA_INICIO_SOLICITUD", request.getParameter("FechaInicioSolicitud"));
			}
			if (request.getParameter("FechaFinEntrega") != null && !request.getParameter("FechaFinEntrega").equals("")){
				parametros.addDefCampo("FECHA_FIN_ENTREGA", request.getParameter("FechaFinEntrega"));
			}
			if (request.getParameter("NomGrupo") != null && !request.getParameter("NomGrupo").equals("")){
				parametros.addDefCampo("NOM_GRUPO", request.getParameter("NomGrupo"));
			}
			if (request.getParameter("NomCompleto") != null && !request.getParameter("NomCompleto").equals("")){
				parametros.addDefCampo("NOM_COMPLETO", request.getParameter("NomCompleto"));
			}			
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimPrestamo", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreCon.jsp";
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("ID_PRESTAMO",request.getParameter("IdPrestamo"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimPrestamo", parametros));
			registroControl.respuesta.addDefCampo("ListaComite", catalogoSL.getRegistros("SimComite", parametros));
			registroControl.respuesta.addDefCampo("ListaVerificacionPrestamo", catalogoSL.getRegistros("SimCatalogoEstatusPrestamo", parametros));
			registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
			registroControl.respuesta.addDefCampo("ListaPapel", catalogoSL.getRegistros("SimCatalogoPapel", parametros));
			registroControl.respuesta.addDefCampo("ListaMetodo", catalogoSL.getRegistros("SimCatalogoMetodoCalculo", parametros));
			//VERIFICA SI SOLO CONSULTA LOS DATOS DEL REGISTRO
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreReg.jsp";
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Alta")){
				registroControl.respuesta.addDefCampo("ListaComite", catalogoSL.getRegistros("SimComite", parametros));
				registroControl.respuesta.addDefCampo("ListaVerificacionPrestamo", catalogoSL.getRegistros("SimCatalogoEstatusPrestamo", parametros));
				registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
				registroControl.respuesta.addDefCampo("ListaPapel", catalogoSL.getRegistros("SimCatalogoPapel", parametros));
				registroControl.respuesta.addDefCampo("ListaMetodo", catalogoSL.getRegistros("SimCatalogoMetodoCalculo", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreReg.jsp";
			}else if (request.getParameter("Filtro").equals("Inicio")){
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreCon.jsp";
			}
		}
		return registroControl;
	}

	/**
	 * Valida los párametros de entrada y ejecuta los servicios de alta, baja o cambio.
	 * @param registro Parametros que se recogen de la sesion del usuario y se le envian a la clase CON.
	 * Estos parámetros son: CVE_GPO_EMPRESA (Clave del grupo empresa), CVE_USUARIO_BITACORA (clave
	 * del usuario que realiza la operacion), RegistroOriginal (registro leido originalmente y
	 * se utiliza cuando se ejecuta la operación de modificacion y se verifica que no se hallan
	 * realizado modificaciones al registro).
	 * @param request Objeto que provee de información al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envía como un parámetro a este método.
	 * @param response Objeto que provee de información del servlet sobre el response del cliente. El
	 * contenedor de servlets crea un objeto HttpServletResponse y lo envía como un parámetro a este método.
	 * @param config Objeto que provee de información del servlet sobre el ServletConfig del cliente. El
	 * contenedor de servlets crea un objeto ServletConfig y lo envía como un parámetro a este método.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON
	 * @param contexto Objeto que contiene información acerca del entorno del servidor de
	 * aplicaciones.
	 * @param iTipoOperacion Operación que debe ejecutar la clase CON. Las operaciones se encuentran
	 * especificadas en la clase {@link com.rapidsist.portal.cliente.CatalogoControl CatalogoControl}
	 * @return Respuesta del servicio de alta, baja o cambio y la página a donde
	 * se redirecciona el control.
	 * @throws RemoteException Si se generó un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se generó un error dentro de la clase CON.
	 */
	public RegistroControl actualiza(Registro registro, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		
		String sIdPrestamoGrupo = "";
		String sNumCiclo = request.getParameter("NumCiclo");
		
		registro.addDefCampo("ID_PRODUCTO", request.getParameter("IdProducto"));
		
		if (iTipoOperacion == 1){
		
			registro.addDefCampo("ID_PRODUCTO", request.getParameter("IdProducto"));
			registro.addDefCampo("ID_GRUPO", request.getParameter("IdGrupo"));
			registro.addDefCampo("NUM_CICLO", request.getParameter("NumCiclo"));
			registro.addDefCampo("CICLO", request.getParameter("Ciclo"));
			
			
			registro.addDefCampo("ALTA","CREDITO_GRUPAL");
			
			registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoGrupalCreditoIndividual", registro, iTipoOperacion);
			
			sIdPrestamoGrupo = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("ID_PRESTAMO_GRUPO");
			registro.addDefCampo("ID_PRESTAMO_GRUPO",sIdPrestamoGrupo);
			
			LinkedList listaMontos = null;
			String sMontoSolicitado = new String();
			String sCliente = new String();
			Enumeration lista = request.getParameterNames();
	
			//OBTIENE EL ARREGLO CON LAS APLICACIONES A PROCESAR
			String[] sMontos = request.getParameterValues("MontoSolicitado");
			String[] sIdCliente = request.getParameterValues("IdCliente");
			//VERIFICA SI ENCONTRO EL ARREGLO DE APLICACIONES
			if (sMontos != null) {
				
				for (int iNumParametro = 0; iNumParametro < sMontos.length; iNumParametro++) {
					//VERIFICA SI LA LISTA DE APLICACIONES ESTA INICIALIZADA
					if (listaMontos == null) {
						listaMontos = new LinkedList();
					}
					//OBTIENE LA CLAVE DE LA APLICACION
					sMontoSolicitado = sMontos[iNumParametro];
					sCliente = sIdCliente[iNumParametro];
					
					registro.addDefCampo("MONTO_SOLICITADO",sMontoSolicitado);
					registro.addDefCampo("ID_CLIENTE",sCliente);
					registro.addDefCampo("ALTA","CREDITO_INDIVIDUAL");
					//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
					
					registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoGrupalCreditoIndividual", registro, 1);//MODIFICACION
					
					
				}
			}
			
		}else if (iTipoOperacion == 3){
			if (request.getParameter("Aceptar") != null){
				registro.addDefCampo("ID_PRESTAMO_GRUPO",request.getParameter("IdPrestamoGrupo"));
				registro.addDefCampo("ID_PRESTAMO",request.getParameter("IdPrestamo"));
				sIdPrestamoGrupo = request.getParameter("IdPrestamoGrupo");
				LinkedList listaMontos = null;
				String sMontoSolicitado = new String();
				String sCliente = new String();
				String sComentario = new String();
				String sPrestamo = new String();
				String sEtapa = new String();
				Enumeration lista = request.getParameterNames();
		
				//OBTIENE EL ARREGLO CON LAS APLICACIONES A PROCESAR
				String[] sMontos = request.getParameterValues("MontoSolicitado");
				String[] sIdCliente = request.getParameterValues("IdCliente");
				String[] sComentarioEtapa = request.getParameterValues("Comentario");
				String[] sIdPrestamo = request.getParameterValues("IdPrestamo");
				String[] sIdEtapa = request.getParameterValues("IdEtapaPrestamo");
				//VERIFICA SI ENCONTRO EL ARREGLO DE APLICACIONES
				if (sMontos != null) {
					
					for (int iNumParametro = 0; iNumParametro < sMontos.length; iNumParametro++) {
						//VERIFICA SI LA LISTA DE APLICACIONES ESTA INICIALIZADA
						if (listaMontos == null) {
							listaMontos = new LinkedList();
						}
						//OBTIENE LA CLAVE DE LA APLICACION
						sMontoSolicitado = sMontos[iNumParametro];
						sCliente = sIdCliente[iNumParametro];
						sComentario = sComentarioEtapa[iNumParametro];
						sPrestamo = sIdPrestamo[iNumParametro];
						sEtapa = sIdEtapa[iNumParametro];
						if (request.getParameter("Avanzar" + sCliente) != null) {
							registro.addDefCampo("B_AVANZAR", "V");
						}
						else {
							registro.addDefCampo("B_AVANZAR", "F");
						}
						
						registro.addDefCampo("MONTO_SOLICITADO",sMontoSolicitado);
						registro.addDefCampo("ID_CLIENTE",sCliente);
						registro.addDefCampo("COMENTARIO",sComentario);
						registro.addDefCampo("ID_PRESTAMO",sPrestamo);
						registro.addDefCampo("ID_ETAPA_PRESTAMO",sEtapa);
						
						//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
						registro.addDefCampo("MOVIMIENTO","MODIFICACION");
						registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoGrupalCreditoIndividual", registro, iTipoOperacion);//MODIFICACION	
					}
				}
			}else if (request.getParameter("RegresarEtapa") != null){
				registro.addDefCampo("ID_PRESTAMO_GRUPO",request.getParameter("IdPrestamoGrupo"));
				sIdPrestamoGrupo = request.getParameter("IdPrestamoGrupo");
				LinkedList listaMontos = null;
				
				String sCliente = new String();
				String sPrestamo = new String();
				String sEtapa = new String();
				Enumeration lista = request.getParameterNames();
		
				//OBTIENE EL ARREGLO CON LAS APLICACIONES A PROCESAR
				String[] sMontos = request.getParameterValues("MontoSolicitado");
				String[] sIdCliente = request.getParameterValues("IdCliente");
				String[] sComentarioEtapa = request.getParameterValues("Comentario");
				String[] sIdPrestamo = request.getParameterValues("IdPrestamo");
				String[] sIdEtapa = request.getParameterValues("IdEtapaPrestamo");
				//VERIFICA SI ENCONTRO EL ARREGLO DE APLICACIONES
				if (sMontos != null) {
					
					for (int iNumParametro = 0; iNumParametro < sMontos.length; iNumParametro++) {
						//VERIFICA SI LA LISTA DE APLICACIONES ESTA INICIALIZADA
						if (listaMontos == null) {
							listaMontos = new LinkedList();
						}
						//OBTIENE LA CLAVE DE LA APLICACION
						
						sCliente = sIdCliente[iNumParametro];
						sPrestamo = sIdPrestamo[iNumParametro];
						sEtapa = sIdEtapa[iNumParametro];
						
						registro.addDefCampo("ID_CLIENTE",sCliente);
						registro.addDefCampo("ID_PRESTAMO",sPrestamo);
						registro.addDefCampo("ID_ETAPA_PRESTAMO",sEtapa);
						
						//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
						registro.addDefCampo("MOVIMIENTO","REGRESAR_ETAPA");
						registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoGrupalCreditoIndividual", registro, iTipoOperacion);//MODIFICACION	
					}
				}
				
				registro.addDefCampo("MOVIMIENTO","ACTUALIZA_A_ETAPA_ANTERIOR");
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoGrupalCreditoIndividual", registro, iTipoOperacion);//MODIFICACION	
				
			}else if (request.getParameter("AvanzarEtapaGrupal") != null){
				registro.addDefCampo("ID_PRESTAMO_GRUPO",request.getParameter("IdPrestamoGrupo"));
				sIdPrestamoGrupo = request.getParameter("IdPrestamoGrupo");
				LinkedList listaMontos = null;
				
				String sCliente = new String();
				
				String sPrestamo = new String();
				String sEtapa = new String();
				Enumeration lista = request.getParameterNames();
		
				//OBTIENE EL ARREGLO CON LAS APLICACIONES A PROCESAR
				String[] sMontos = request.getParameterValues("MontoSolicitado");
				String[] sIdCliente = request.getParameterValues("IdCliente");
				String[] sComentarioEtapa = request.getParameterValues("Comentario");
				String[] sIdPrestamo = request.getParameterValues("IdPrestamo");
				String[] sIdEtapa = request.getParameterValues("IdEtapaPrestamo");
				//VERIFICA SI ENCONTRO EL ARREGLO DE APLICACIONES
				if (sMontos != null) {
					
					for (int iNumParametro = 0; iNumParametro < sMontos.length; iNumParametro++) {
						//VERIFICA SI LA LISTA DE APLICACIONES ESTA INICIALIZADA
						if (listaMontos == null) {
							listaMontos = new LinkedList();
						}
						//OBTIENE LA CLAVE DE LA APLICACION
						
						sCliente = sIdCliente[iNumParametro];
						
						sPrestamo = sIdPrestamo[iNumParametro];
						sEtapa = sIdEtapa[iNumParametro];
						
						registro.addDefCampo("ID_CLIENTE",sCliente);
						
						registro.addDefCampo("ID_PRESTAMO",sPrestamo);
						registro.addDefCampo("ID_ETAPA_PRESTAMO",sEtapa);
						
						//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
						registro.addDefCampo("MOVIMIENTO","AVANZAR_ETAPA_GRUPAL");
						registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoGrupalCreditoIndividual", registro, iTipoOperacion);//MODIFICACION	
					}
				}
				
				registro.addDefCampo("MOVIMIENTO","ACTUALIZA_A_ETAPA_POSTERIOR");
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoGrupalCreditoIndividual", registro, iTipoOperacion);//MODIFICACION	
				
			}else if (request.getParameter("ReconformaPrestamo") != null){
				registro.addDefCampo("ID_PRESTAMO_GRUPO",request.getParameter("IdPrestamoGrupo"));
				registro.addDefCampo("ID_GRUPO",request.getParameter("IdGrupo"));
				sIdPrestamoGrupo = request.getParameter("IdPrestamoGrupo");
						
				//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
				registro.addDefCampo("MOVIMIENTO","RECONFORMAR_PRESTAMO");
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoGrupalCreditoIndividual", registro, iTipoOperacion);//MODIFICACION	
			}else if (request.getParameter("CancelarPrestamo") != null){
				registro.addDefCampo("ID_PRESTAMO_GRUPO",request.getParameter("IdPrestamoGrupo"));
				registro.addDefCampo("ID_GRUPO",request.getParameter("IdGrupo"));
				
				sIdPrestamoGrupo = request.getParameter("IdPrestamoGrupo");
						
				//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
				registro.addDefCampo("MOVIMIENTO","CANCELAR_PRESTAMO");
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoGrupalCreditoIndividual", registro, iTipoOperacion);//MODIFICACION	
			}
				
		}
		//INDICA A DONDE IRA AL TERMINAR LA ACTUALIZACION
	
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimPrestamoGrupal&OperacionCatalogo=CR&Filtro=Modificacion&IdPrestamoGrupo="+sIdPrestamoGrupo+"&IdProducto="+request.getParameter("IdProducto")+"&NumCiclo="+sNumCiclo;
		
		return registroControl;
	}
}