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
 * modificación y consulta) del catálogo de configuración de permisos para perfiles. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class PerfilConfiguracionCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			parametros.addDefCampo("CVE_APLICACION",request.getParameter("CveAplicacion"));
			parametros.addDefCampo("CVE_PERFIL",request.getParameter("CvePerfil"));
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("HerramientasConfiguracionPerfilConfiguracion", parametros));
			registroControl.sPagina = "/Aplicaciones/HerramientasConfiguracion/fApliAplPefFunAlt.jsp";
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

		//RECUPERA LA LISTA DE LAS FUNCIONES QUE SE VAN A PROCESAR
		LinkedList listaFunciones = null;
		String sClaveFuncion;
		Registro registroPermiso = null;
		//Enumeration lista = request.getParameterNames();

		//IDENTIFICAMOS QUE TIPO DE OPERACION SE DEBE REALIZAR SOBRE LA BASE DE DATOS
		if (request.getParameter("btnBaja") != null){
			iTipoOperacion = CatalogoControl.CON_BAJA;
		}
		if (request.getParameter("btnModificar") != null){
			iTipoOperacion = CatalogoControl.CON_MODIFICACION;
		}
		if (request.getParameter("btnAlta") != null){
			iTipoOperacion = CatalogoControl.CON_ALTA;
		}

		//OBTIENE EL ARREGLO CON LAS FUNCIONES A PROCESAR
		String[] sFunciones = request.getParameterValues("CveFuncion");
		//VERIFICA SI ENCONTRO EL ARREGLO DE FUNCIONESF
		if (sFunciones != null){
			for (int iNumParametro = 0; iNumParametro < sFunciones.length; iNumParametro++){
				//VERIFICA SI LA LISTA DE FUNCIONES ESTA INICIALIZADA
				if (listaFunciones == null){
					listaFunciones = new LinkedList();
				}
				//OBTIENE LA CLAVE DE LA FUNCION
				sClaveFuncion = sFunciones[iNumParametro];

				//VERIFICA SI DEBE REALIZAR MODIFICACION DE LAS FUNCIONES
				if (iTipoOperacion == CatalogoControl.CON_MODIFICACION){

					//AGREGA A LA LISTA LA DEFINICION DE LOS PERMISOS PARA UNA FUNCION
					registroPermiso = new Registro();
					registroPermiso.addDefCampo("CVE_FUNCION", sClaveFuncion);
					registroPermiso.addDefCampo("CVE_APLICACION", request.getParameter("CveAplicacion"));
					registroPermiso.addDefCampo("CVE_PERFIL", request.getParameter("CvePerfil"));
					if (request.getParameter("Consulta" + sClaveFuncion) != null) {
						registroPermiso.addDefCampo("B_CONSULTA", "V");
					}
					else {
						registroPermiso.addDefCampo("B_CONSULTA", "F");
					}
					if (request.getParameter("Alta" + sClaveFuncion) != null) {
						registroPermiso.addDefCampo("B_ALTA", "V");
					}
					else {
						registroPermiso.addDefCampo("B_ALTA", "F");
					}
					if (request.getParameter("Baja" + sClaveFuncion) != null) {
						registroPermiso.addDefCampo("B_BAJA", "V");
					}
					else {
						registroPermiso.addDefCampo("B_BAJA", "F");
					}
					if (request.getParameter("Modificacion" + sClaveFuncion) != null) {
						registroPermiso.addDefCampo("B_MODIF", "V");
					}
					else {
						registroPermiso.addDefCampo("B_MODIF", "F");
					}
					if (request.getParameter("Bitacora" + sClaveFuncion) != null) {
						registroPermiso.addDefCampo("B_BITACORA", "V");
					}
					else {
						registroPermiso.addDefCampo("B_BITACORA", "F");
					}
					listaFunciones.add(registroPermiso);
				}
				//VERIFICA SI LA FUNCION ACTUAL SE DEBE DAR DE BAJA
				else if (request.getParameter("FuncionBaja" + sClaveFuncion) != null){
					registroPermiso = new Registro();
					registroPermiso.addDefCampo("CVE_FUNCION", sClaveFuncion);
					registroPermiso.addDefCampo("CVE_APLICACION", request.getParameter("CveAplicacion"));
					registroPermiso.addDefCampo("CVE_PERFIL", request.getParameter("CvePerfil"));
					listaFunciones.add(registroPermiso);
				}
				//VERIFICA SI LA FUNCION ACTUAL SE DEBE DAR DE ALTA
				else if (request.getParameter("FuncionAlta" + sClaveFuncion) != null){
					registroPermiso = new Registro();
					registroPermiso.addDefCampo("CVE_FUNCION", sClaveFuncion);
					registroPermiso.addDefCampo("CVE_APLICACION", request.getParameter("CveAplicacion"));
					registroPermiso.addDefCampo("CVE_PERFIL", request.getParameter("CvePerfil"));
					registroPermiso.addDefCampo("B_ALTA","F");
					registroPermiso.addDefCampo("B_BAJA","F");
					registroPermiso.addDefCampo("B_MODIF","F");
					registroPermiso.addDefCampo("B_CONSULTA","F");
					registroPermiso.addDefCampo("B_BITACORA","F");

					listaFunciones.add(registroPermiso);
				}//VERIFICA SI SE DEBEN MODIFICAR LAS FUNCIONES
			}//CICLO FOR
		}//VERIFICA SI ENCONTRO EL ARREGLO DE FUNCIONES

		//VERIFICA SI SE SELECCIONARON FUNCIONES PARA PROCESAR
		if (listaFunciones != null){
			registro.addDefCampo("ListaRegistros", listaFunciones);
			//DESHABILITA EL COMPONENTE DE RE-READ
			registro.addDefCampo("OperacionInfraestructura", "DeshabilitaReRead");

			//DA DE ALTA O BAJA LAS FUNCIONES EN LA BASE DE DATOS
			registroControl.resultadoCatalogo = catalogoSL.modificacion("HerramientasConfiguracionPerfilConfiguracion", registro, iTipoOperacion);
		}
		else{
			ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
			registroControl.resultadoCatalogo = resultadoCatalogo;
		}
		if (iTipoOperacion == CatalogoControl.CON_ALTA){
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=HerramientasConfiguracionPerfil&OperacionCatalogo=CR&CveAplicacion=" + request.getParameter("CveAplicacion") + "&CvePerfil=" + request.getParameter("CvePerfil");
		}
		else{
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=HerramientasConfiguracionAplicacion&OperacionCatalogo=CR&CveAplicacion=" + request.getParameter("CveAplicacion");
		}

		return registroControl;
	}
}