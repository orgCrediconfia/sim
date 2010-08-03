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
 * modificación y consulta) de los préstamos grupales. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimPrestamoCambioAsesorCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimUsuarioSucursalAsignada", parametros));
			if (request.getParameter("NomAsesor") != null && !request.getParameter("NomAsesor").equals("")){
				parametros.addDefCampo("NOM_COMPLETO", request.getParameter("NomAsesor"));
			}
			if (request.getParameter("IdSucursal") != null && !request.getParameter("IdSucursal").equals("null")){
				parametros.addDefCampo("ID_SUCURSAL", request.getParameter("IdSucursal"));
			}
			parametros.addDefCampo("CONSULTA","ASESOR");
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimPrestamoCambioAsesor", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreCamAseCon.jsp";
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("CONSULTA","CAMBIO_ASESOR");
			parametros.addDefCampo("ID_SUCURSAL",request.getParameter("IdSucursal"));
			registroControl.respuesta.addDefCampo("ListaAsesor", catalogoSL.getRegistros("SimPrestamoCambioAsesor", parametros));
			parametros.addDefCampo("CONSULTA","PRESTAMO");
			parametros.addDefCampo("CVE_USUARIO",request.getParameter("CveUsuario"));
			
			registroControl.respuesta.addDefCampo("ListaPrestamo", catalogoSL.getRegistros("SimPrestamoCambioAsesor", parametros));
			
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreCamAseReg.jsp";
			
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Inicio")){
				registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimUsuarioSucursalAsignada", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreCamAseCon.jsp";
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
		
		String sAsesor = new String();
		String sPrestamo = new String();
		String sAplicaA = new String();
		
		//OBTIENE EL ARREGLO CON LAS APLICACIONES A PROCESAR
		String[] sCveAsesorCredito = request.getParameterValues("CveAsesorCredito");
		String[] sIdPrestamo = request.getParameterValues("IdPrestamo");
		String[] sAplica = request.getParameterValues("Aplica");
		
		//VERIFICA SI ENCONTRO EL ARREGLO DE APLICACIONES
		if (sCveAsesorCredito != null) {
			
			for (int iNumParametro = 0; iNumParametro < sCveAsesorCredito.length; iNumParametro++) {
				//VERIFICA SI LA LISTA DE APLICACIONES ESTA INICIALIZADA
				
				//OBTIENE LA CLAVE DE LA APLICACION
				sPrestamo = sIdPrestamo[iNumParametro];
				sAsesor = sCveAsesorCredito[iNumParametro];
				sAplicaA = sAplica[iNumParametro];
				
				registro.addDefCampo("ID_PRESTAMO",sPrestamo);
				registro.addDefCampo("CVE_ASESOR_CREDITO",sAsesor);
				registro.addDefCampo("APLICA",sAplicaA);
			
				//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoCambioAsesor", registro, 1);//MODIFICACION
				
			}
		}
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimPrestamoCambioAsesor&OperacionCatalogo=IN&Filtro=Inicio";
	
		return registroControl;
	}
}