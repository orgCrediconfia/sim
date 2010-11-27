/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.herramientasconfiguracion.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControl;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Enumeration;

/**
 * Esta clase se encarga de administrar  las operaciones (alta, baja,
 * modificación y consulta) del catálogo de aplicaciones asignadas a grupo de empresas. 
 * Esta clase es llamada por el servlet {@link CatalogoS CatalogoS}.
 */
public class GrupoEmpresasAplicacionCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
	public RegistroControl consulta(Registro parametros, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion) throws RemoteException, Exception {
		RegistroControl registroControl = new RegistroControl();

		parametros.addDefCampo("CVE_GPO_EMPRESA", request.getParameter("CveGpoEmpresa"));

		if (iTipoOperacion == CON_CONSULTA_REGISTRO) {
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("CVE_APLICACION", request.getParameter("CveAplicacion"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("HerramientasConfiguracionGrupoEmpresasAplicacion", parametros));
			parametros.addDefCampo("Filtro", "Todos");
			registroControl.respuesta.addDefCampo("ListaAplicaciones", catalogoSL.getRegistros("HerramientasConfiguracionAplicacion", parametros));
			registroControl.sPagina = "/Aplicaciones/HerramientasConfiguracion/fPortGpoAplReg.jsp";
		}
		else if (iTipoOperacion == CON_INICIALIZACION) {

			parametros.addDefCampo("Filtro", request.getParameter("Filtro"));

			//VERIFICA SI SE ENVIO EL PARAMETRO NOMBRE DE APLICACION
			if (request.getParameter("NomAplicacion") != null && !request.getParameter("NomAplicacion").equals("")) {
				parametros.addDefCampo("NOM_APLICACION", request.getParameter("NomAplicacion"));
			}
			registroControl.respuesta.addDefCampo("ListaAplicaciones", catalogoSL.getRegistros("HerramientasConfiguracionGrupoEmpresasAplicacion", parametros));
			registroControl.sPagina = "/Aplicaciones/HerramientasConfiguracion/fPortGpoAplReg.jsp";
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
	public RegistroControl actualiza(Registro registro, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion) throws RemoteException, Exception {
		RegistroControl registroControl = new RegistroControl();

		//RECUPERA LA LISTA DE LAS APLICACIONES QUE SE VAN A PROCESAR
		LinkedList listaAplicaciones = null;
		String sClaveAplicacion = new String();
		Registro registroPermiso = null;
		Enumeration lista = request.getParameterNames();

		if (request.getParameter("btnModificar") != null) {
			iTipoOperacion = CatalogoControl.CON_MODIFICACION;
		}
		
		//OBTIENE EL ARREGLO CON LAS APLICACIONES A PROCESAR
		String[] sAplicaciones = request.getParameterValues("CveAplicacion");
		//VERIFICA SI ENCONTRO EL ARREGLO DE APLICACIONES
		if (sAplicaciones != null) {
			
			for (int iNumParametro = 0; iNumParametro < sAplicaciones.length; iNumParametro++) {
				//VERIFICA SI LA LISTA DE APLICACIONES ESTA INICIALIZADA
				if (listaAplicaciones == null) {
					listaAplicaciones = new LinkedList();
				}
				//OBTIENE LA CLAVE DE LA APLICACION
				sClaveAplicacion = sAplicaciones[iNumParametro];
				
				//VERIFICA SI DEBE REALIZAR MODIFICACION DE LAS APLICACIONES
				if (iTipoOperacion == CatalogoControl.CON_MODIFICACION) {
					
					//AGREGA A LA LISTA LA DEFINICION DE LOS PERMISOS PARA UNA APLICACION
					registroPermiso = new Registro();
					registroPermiso.addDefCampo("CVE_APLICACION", sClaveAplicacion);
					registroPermiso.addDefCampo("CVE_GPO_EMPRESA", request.getParameter("CveGpoEmpresa"));
					if (request.getParameter("Bloqueado" + sClaveAplicacion) != null) {
						registroPermiso.addDefCampo("B_BLOQUEADO", "V");
					}
					else {
						registroPermiso.addDefCampo("B_BLOQUEADO", "F");
					}
					listaAplicaciones.add(registroPermiso);
				}
				//VERIFICA SI LA APLICACION ACTUAL SE DEBE DAR DE ALTA
				else if (request.getParameter("AplicacionBaja" + sClaveAplicacion) != null) {
					registroPermiso = new Registro();
					registroPermiso.addDefCampo("CVE_APLICACION", sClaveAplicacion);
					registroPermiso.addDefCampo("CVE_GPO_EMPRESA", request.getParameter("CveGpoEmpresa"));
					registroPermiso.addDefCampo("B_BLOQUEADO","F");
					listaAplicaciones.add(registroPermiso);
				}
				//VERIFICA SI LA APLICACION ACTUAL SE DEBE DAR DE BAJA
				else if (request.getParameter("AplicacionAlta" + sClaveAplicacion) != null) {
					registroPermiso = new Registro();
					registroPermiso.addDefCampo("CVE_APLICACION", sClaveAplicacion);
					registroPermiso.addDefCampo("CVE_GPO_EMPRESA", request.getParameter("CveGpoEmpresa"));
					listaAplicaciones.add(registroPermiso);
				} //VERIFICA SI SE DEBEN MODIFICAR LAS APLICACIONES
			}
		}
		//VERIFICA SI SE SELECCIONARON FUNCIONES PARA PROCESAR
		if (listaAplicaciones != null) {
			registro.addDefCampo("ListaAplicaciones", listaAplicaciones);

			//DESHABILITA EL COMPONENTE DE RE-READ
			registro.addDefCampo("OperacionInfraestructura", "DeshabilitaReRead");

			//DA DE ALTA O BAJA LAS FUNCIONES EN LA BASE DE DATOS
			registroControl.resultadoCatalogo = catalogoSL.modificacion("HerramientasConfiguracionGrupoEmpresasAplicacion", registro, iTipoOperacion);
			
		}
		else {
			
			ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
			registroControl.resultadoCatalogo = resultadoCatalogo;
		}
		if (iTipoOperacion == CatalogoControl.CON_ALTA) {
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=HerramientasConfiguracionGrupoEmpresas&OperacionCatalogo=CR&CveGpoEmpresa=" + request.getParameter("CveGpoEmpresa");
		}
		else {
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=HerramientasConfiguracionGrupoEmpresas&OperacionCatalogo=CR&CveGpoEmpresa=" + request.getParameter("CveGpoEmpresa");
		}

		return registroControl;
	}
}