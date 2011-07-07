/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControl;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Esta clase se encarga de administrar los servicios de detalle operación (alta, baja,
 * modificación y consulta) de las garantías del préstamo. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimPrestamoGarantiaCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
	 * @throws RemoteException Si se generá un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se generá un error dentro de la clase CON.
	 */
	public RegistroControl consulta(Registro parametros, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		//OBTIENE SOLO EL REGISTRO SOLICITADO
		if (iTipoOperacion == CON_CONSULTA_TABLA){
			parametros.addDefCampo("FILTRO",request.getParameter("Filtro"));
			parametros.addDefCampo("ID_PRESTAMO",request.getParameter("IdPrestamo"));
			parametros.addDefCampo("ID_PERSONA",request.getParameter("IdPersona"));
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimPrestamoGarantia", parametros));
			
			LinkedList lista = new LinkedList();
			String sLista = "";
			lista = catalogoSL.getRegistros("SimPrestamoGarantia", parametros);
			
			if(lista.isEmpty()){
				sLista = "no";
			}else{
				sLista = "si";
			}
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreGarReg.jsp?IdPrestamo="+request.getParameter("IdPrestamo")+"&IdPersona="+request.getParameter("IdPersona")+"&Lista="+sLista;	
		}
		return registroControl;
	}

	/**
	 * Valida los párametros entrada y ejecuta los servicios de alta, baja o cambio.
	 * @param registro Parametros que se recogen de la sesion del usuario y se le envian a la clase CON.
	 * Estos párametros son: CVE_GPO_EMPRESA (Clave del grupo empresa), CVE_USUARIO_BITACORA (clave
	 * del usuario que realiza la operacion), RegistroOriginal (registro leido originalmente y
	 * se utiliza cuando se ejecuta la Operación de modificacion y se verifica que no se hallan
	 * realizado modificaciones al registro).
	 * @param request Objeto que provee de información al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envía como un párametro a este método.
	 * @param response Objeto que provee de información del servlet sobre el response del cliente. El
	 * contenedor de servlets crea un objeto HttpServletResponse y lo envía como un párametro a este método.
	 * @param config Objeto que provee de información del servlet sobre el ServletConfig del cliente. El
	 * contenedor de servlets crea un objeto ServletConfig y lo envía como un párametro a este método.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON
	 * @param contexto Objeto que contiene información acerca del entorno del servidor de
	 * aplicaciones.
	 * @param iTipoOperacion Operación que debe ejecutar la clase CON. Las operaciones se encuentran
	 * especificadas en la clase {@link com.rapidsist.portal.cliente.CatalogoControl CatalogoControl}
	 * @return Respuesta del servicio de alta, baja o cambio y la página a donde
	 * se redirecciona el control.
	 * @throws RemoteException Si se generá un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se generá un error dentro de la clase CON.
	 */
	public RegistroControl actualiza(Registro registro, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		
		//RECUPERA LA LISTA DE LAS FUNCIONES QUE SE VAN A PROCESAR
		
		String sIdGarantia;
		Enumeration lista = request.getParameterNames();
		//RECORRE LA LISTA DE PARAMETROS QUE VIENEN EN EL REQUEST
		while (lista.hasMoreElements()){
			String sNombre = (String)lista.nextElement();
			//VERIFICA SI EL PARAMETRO TIENE EL PREFIJO "FuncionAlta"
			if (sNombre.startsWith("FuncionAlta")){
				//VERIFICA SI LA LISTA DE FUNCIONES ESTA INICIALIZADA
				
				sIdGarantia = sNombre.substring(11, sNombre.length());
				
				registro.addDefCampo("ID_PRESTAMO", request.getParameter("IdPrestamo"));
				registro.addDefCampo("ID_GARANTIA", sIdGarantia);
				
				//DA DE ALTA O BAJA LAS FUNCIONES EN LA BASE DE DATOS
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoGarantia", registro, iTipoOperacion);
				
			}
		}
		//INDICA A DONDE IRA AL TERMINAR LA ACTUALIZACION
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimPrestamo&OperacionCatalogo=CR&IdPrestamo="+request.getParameter("IdPrestamo")+"&Alta=No";
		
		return registroControl;
	}
}
