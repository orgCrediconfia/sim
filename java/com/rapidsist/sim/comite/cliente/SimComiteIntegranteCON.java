/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.comite.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControl;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import javax.naming.Context;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;
import java.util.Enumeration;

/**
 * Esta clase se encarga de administrar los servicios de operación (alta, baja,
 * modificación y consulta) de los integrantes de un comité. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimComiteIntegranteCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			
			//VERIFICA SI SE ENVIO EL PARAMETRO CLAVE DEL COMITE	
			if (request.getParameter("ClaveUsuario") != "" && !request.getParameter("ClaveUsuario").equals("")){
				parametros.addDefCampo("CLAVE_USUARIO", request.getParameter("ClaveUsuario"));	
			}
			
			//VERIFICA SI SE ENVIO EL PARAMETRO NOMBRE DEL COMITE
			if (request.getParameter("NomCompleto") != "" && !request.getParameter("NomCompleto").equals("")){	
				parametros.addDefCampo("NOM_COMPLETO", request.getParameter("NomCompleto"));
			}
			
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			parametros.addDefCampo("CONSULTA","TODOS");
			parametros.addDefCampo("ID_COMITE", request.getParameter("IdComite"));
			registroControl.respuesta.addDefCampo("ListaIntegrante", catalogoSL.getRegistros("SimComiteIntegrante", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Comite/fSimComIntCon.jsp?IdComite="+request.getParameter("IdComite");
		}
		
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("ID_COMITE",request.getParameter("IdComite"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimComite", parametros));
			registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimUsuarioSucursalAsignada", parametros));
			parametros.addDefCampo("CONSULTA","ASIGNADOS");
			registroControl.respuesta.addDefCampo("ListaIntegrante", catalogoSL.getRegistros("SimComiteIntegrante", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Comite/fSimComReg.jsp";
		}
		
		if (iTipoOperacion == CON_INICIALIZACION){
			registroControl.sPagina = "/Aplicaciones/Sim/Comite/fSimComIntCon.jsp";	
		}
		
		return registroControl;
	}

	/**
	 * Valida los párametros entrada y ejecuta los servicios de alta, baja o cambio.
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
		
		//RECUPERA LA LISTA DE LAS FUNCIONES QUE SE VAN A PROCESAR
		LinkedList listaIntegrantes = null;
		String sCveIntegrante;
		Enumeration lista = request.getParameterNames();
		//RECORRE LA LISTA DE PARAMETROS QUE VIENEN EN EL REQUEST
		while (lista.hasMoreElements()){
			String sNombre = (String)lista.nextElement();
			//VERIFICA SI EL PARAMETRO TIENE EL PREFIJO "Alta"
			if (sNombre.startsWith("Alta")){
				
				if (listaIntegrantes == null){
					listaIntegrantes = new LinkedList();
				}
				sCveIntegrante = sNombre.substring(4, sNombre.length());
				
				registro.addDefCampo("ID_COMITE", request.getParameter("IdComite"));
				registro.addDefCampo("CVE_INTEGRANTE", sCveIntegrante);
				
				//DA DE ALTA O BAJA LAS FUNCIONES EN LA BASE DE DATOS
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimComiteIntegrante", registro, iTipoOperacion);
				
			}
		}
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimComite&OperacionCatalogo=CR&IdComite="+request.getParameter("IdComite");
	
		return registroControl;
	}
}
