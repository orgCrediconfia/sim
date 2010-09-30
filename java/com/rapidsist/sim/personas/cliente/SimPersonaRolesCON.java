/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.personas.cliente;

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
 * y consulta) de los roles de las personas. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimPersonaRolesCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			parametros.addDefCampo("ID_PERSONA",request.getParameter("IdPersona"));
			if (request.getParameter("Filtro").equals("Todos")){
				if (request.getParameter("Roles").equals("Disponibles")){
					parametros.addDefCampo("ROLES","DISPONIBLES");
					registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimPersonaRoles", parametros));
					registroControl.sPagina = "/Aplicaciones/Sim/Personas/fSimPerRol.jsp";
				}
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
		
		LinkedList listaTipoPersona = null;
		String sClaveTipoPersona = new String();
		Registro registroTipoPersona = null;
		
		registro.addDefCampo("ID_PERSONA", request.getParameter("IdPersona"));
		String sIdExConyuge = request.getParameter("IdExConyuge");
		String[] sCveTipoPersona = request.getParameterValues("CveTipoPersona");
		
		
		if (sCveTipoPersona != null) {
			for (int iNumParametro = 0; iNumParametro < sCveTipoPersona.length; iNumParametro++) {
				//VERIFICA SI LA LISTA DE APLICACIONES ESTA INICIALIZADA
				if (listaTipoPersona == null) {
					listaTipoPersona = new LinkedList();
				}
				//OBTIENE LA CLAVE DE LA APLICACION
				sClaveTipoPersona = sCveTipoPersona[iNumParametro];
				
				if (request.getParameter("RolAlta" + sClaveTipoPersona) != null) {
					registroTipoPersona = new Registro();
					registroTipoPersona.addDefCampo("CVE_TIPO_PERSONA", sClaveTipoPersona);
					listaTipoPersona.add(registroTipoPersona);
				}else if (request.getParameter("RolBaja" + sClaveTipoPersona) != null) {
					registroTipoPersona = new Registro();
					registroTipoPersona.addDefCampo("CVE_TIPO_PERSONA", sClaveTipoPersona);
					listaTipoPersona.add(registroTipoPersona);
				}
			}
		}
		
		if (listaTipoPersona != null) {
			registro.addDefCampo("ListaTipoPersona", listaTipoPersona);

			//DA DE ALTA O BAJA LAS FUNCIONES EN LA BASE DE DATOS
			registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPersonaRoles", registro, iTipoOperacion);
		}
		
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimClientes&OperacionCatalogo=CR&IdPersona="+request.getParameter("IdPersona")+"&IdExConyuge="+sIdExConyuge;
		
		return registroControl;
	}
}