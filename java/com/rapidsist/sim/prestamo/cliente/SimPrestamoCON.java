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



/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificación y consulta) de los préstamos. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimPrestamoCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			registroControl.respuesta.addDefCampo("ListaVerificacionPrestamo", catalogoSL.getRegistros("SimCatalogoEstatusPrestamo", parametros));
			
			//VERIFICA SI SE ENVIO EL PARAMETRO NOMBRE
			if (request.getParameter("CvePrestamo") != null && !request.getParameter("CvePrestamo").equals("")){
				parametros.addDefCampo("CVE_PRESTAMO", request.getParameter("CvePrestamo"));
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
			if (request.getParameter("NomCompleto") != null && !request.getParameter("NomCompleto").equals("")){
				parametros.addDefCampo("NOM_COMPLETO", request.getParameter("NomCompleto"));
			}			
			if (request.getParameter("IdEstatusPrestamo") != null && !request.getParameter("IdEstatusPrestamo").equals("null")){
				parametros.addDefCampo("ID_ETAPA_PRESTAMO", request.getParameter("IdEstatusPrestamo"));
			}
			
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimPrestamo", parametros));
			
			//CUENTA CUANTAS PAGINAS SERÁN
			Registro paginas = new Registro ();
			paginas = catalogoSL.getRegistro("SimPrestamoPaginacion", parametros);
			String sPaginas = (String)paginas.getDefCampo("PAGINAS");
			
			System.out.println("sPaginas*******"+sPaginas);
			
			registroControl.respuesta.addDefCampo("ListaEstatus", catalogoSL.getRegistros("SimPrestamoEstatusGrupo", parametros));
													
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreCon.jsp?Paginas="+sPaginas+"&Superior="+100+"&CvePrestamo="+request.getParameter("CvePrestamo")+"&IdProducto="+request.getParameter("IdProducto")+"&NumCiclo="+request.getParameter("NumCiclo")+"&FechaInicioSolicitud="+request.getParameter("FechaInicioSolicitud")+"&FechaFinEntrega="+request.getParameter("FechaFinEntrega")+"&NomCompleto="+request.getParameter("NomCompleto")+"&IdEstatusPrestamo="+request.getParameter("IdEstatusPrestamo");
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			
			if (request.getParameter("Alta").equals("Si")){
				parametros.addDefCampo("ID_PERSONA",request.getParameter("IdPersona"));
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimClientes", parametros));
				registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimUsuarioSucursalAsignada", parametros));
				registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
				registroControl.respuesta.addDefCampo("ListaMetodo", catalogoSL.getRegistros("SimCatalogoMetodoCalculo", parametros));
				registroControl.respuesta.addDefCampo("ListaPapel", catalogoSL.getRegistros("SimCatalogoPapel", parametros));
			}
			if (request.getParameter("Alta").equals("No")){
				parametros.addDefCampo("ID_PRESTAMO",request.getParameter("IdPrestamo"));
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimPrestamo", parametros));
				registroControl.respuesta.addDefCampo("ListaParticipante", catalogoSL.getRegistros("SimPrestamoParticipante", parametros));
				registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimUsuarioSucursalAsignada", parametros));
				registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
				registroControl.respuesta.addDefCampo("ListaMetodo", catalogoSL.getRegistros("SimCatalogoMetodoCalculo", parametros));
				registroControl.respuesta.addDefCampo("ListaPapel", catalogoSL.getRegistros("SimCatalogoPapel", parametros));
				registroControl.respuesta.addDefCampo("ListaComite", catalogoSL.getRegistros("SimComite", parametros));
				registroControl.respuesta.addDefCampo("ListaEstatusPrestamo", catalogoSL.getRegistros("SimListaEstatusPrestamo", parametros));
				registroControl.respuesta.addDefCampo("ListaActividadRequisito", catalogoSL.getRegistros("SimPrestamoActividadRequisito", parametros));
				registroControl.respuesta.addDefCampo("ListaUnidad", catalogoSL.getRegistros("SimCatalogoUnidad", parametros));
				registroControl.respuesta.addDefCampo("ListaFormaAplicacion", catalogoSL.getRegistros("SimCatalogoFormaAplicacion", parametros));
				registroControl.respuesta.addDefCampo("ListaCargoComision", catalogoSL.getRegistros("SimPrestamoCargoComisionCliente", parametros));
				registroControl.respuesta.addDefCampo("ListaMontoCliente", catalogoSL.getRegistros("SimPrestamoMontoCliente", parametros));
				registroControl.respuesta.addDefCampo("ListaAccesorio", catalogoSL.getRegistros("SimPrestamoAccesorioOrdenCliente", parametros));
				registroControl.respuesta.addDefCampo("ListaDocumentacion", catalogoSL.getRegistros("SimPrestamoDocumentacion", parametros));
				parametros.addDefCampo("FILTRO","ASIGNADOS");
				registroControl.respuesta.addDefCampo("ListaGarantia", catalogoSL.getRegistros("SimPrestamoGarantia", parametros));
				parametros.addDefCampo("CONSULTA","ARCHIVOS");
				registroControl.respuesta.addDefCampo("ListaArchivo", catalogoSL.getRegistros("SimPrestamoFlujoEfectivo", parametros));
				parametros.addDefCampo("ETAPA_DOCUMENTOS","INDIVIDUAL");
				registroControl.respuesta.addDefCampo("registroEtapaDocumentos", catalogoSL.getRegistro("SimPrestamoDocumentacion", parametros));
				registroControl.respuesta.addDefCampo("ListaEstatusHistorico", catalogoSL.getRegistros("SimPrestamoEstatusHistorico", parametros));
				parametros.addDefCampo("PRESTAMO","INDIVIDUAL");
				registroControl.respuesta.addDefCampo("registroVerificarEtapa", catalogoSL.getRegistro("SimPrestamoVerificaEtapaAnteriorAutMonRie", parametros));
			}
			
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreReg.jsp";
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Alta")){
				registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimUsuarioSucursalAsignada", parametros));
				registroControl.respuesta.addDefCampo("ListaComite", catalogoSL.getRegistros("SimComite", parametros));
				registroControl.respuesta.addDefCampo("ListaVerificacionPrestamo", catalogoSL.getRegistros("SimCatalogoEstatusPrestamo", parametros));
				registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
				registroControl.respuesta.addDefCampo("ListaPapel", catalogoSL.getRegistros("SimCatalogoPapel", parametros));
				registroControl.respuesta.addDefCampo("ListaMetodo", catalogoSL.getRegistros("SimCatalogoMetodoCalculo", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreReg.jsp";
			}else if (request.getParameter("Filtro").equals("Inicio")){
				registroControl.respuesta.addDefCampo("ListaVerificacionPrestamo", catalogoSL.getRegistros("SimCatalogoEstatusPrestamo", parametros));
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

		registro.addDefCampo("ID_PRESTAMO", request.getParameter("IdPrestamo"));
		registro.addDefCampo("ID_PERSONA", request.getParameter("IdPersona"));
		registro.addDefCampo("ID_SUCURSAL", request.getParameter("IdSucursal") != null ? request.getParameter("IdSucursal") : "");
		registro.addDefCampo("CVE_ASESOR_CREDITO", request.getParameter("IdAsesorCredito") != null ? request.getParameter("IdAsesorCredito") : "" );
		registro.addDefCampo("DobleSubmit","Desabilitado");
		
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamo", registro, iTipoOperacion);
		
		String sIdPrestamo = "";
		String sIdPersona = "";
		
		if (iTipoOperacion == 1){
			sIdPersona = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("ID_PERSONA");
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimPrestamo&OperacionCatalogo=CR&IdPersona="+sIdPersona+"&Alta=Si";
		}else if (iTipoOperacion == 2){
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimPrestamo&OperacionCatalogo=CR&IdPrestamo="+request.getParameter("IdPrestamo")+"&Alta=No";
		}
		
		return registroControl;
	}
}