/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.catalogos.cliente;

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



/**
 * Esta clase se encarga de administrar los servicios de operación (alta, baja,
 * modificación y consulta) del catálogo regional. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimCatalogoRegionalCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			if (request.getParameter("Filtro").equals("Todos")){
				//VERIFICA SI SE ENVIO EL PARAMETRO ID BANCO
				if (request.getParameter("IdRegional") != null && !request.getParameter("IdRegional").equals("")){
					parametros.addDefCampo("ID_REGIONAL", request.getParameter("IdRegional"));
				}
				//VERIFICA SI SE ENVIO EL PARAMETRO NOM BANCO
				if (request.getParameter("NomRegional") != null && !request.getParameter("NomRegional").equals("")){
					parametros.addDefCampo("NOM_REGIONAL", request.getParameter("NomRegional"));
				}
			}
			
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimCatalogoRegional", parametros));
			
			registroControl.sPagina = "/Aplicaciones/Sim/CatalogosGenerales/fSimRegionalCon.jsp";
		}
		
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("ID_REGIONAL",request.getParameter("IdRegional"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimCatalogoRegional", parametros));
			registroControl.respuesta.addDefCampo("ListaTelefono", catalogoSL.getRegistros("SimRegionalTelefono", parametros));
			registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimRegionalSucursalAsignada", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/CatalogosGenerales/fSimRegionalReg.jsp";
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

		String sIdRegional = "";
		String sIdDomicilio = "";

		//RECUPERA LOS CAMPOS DE LA MODIFICACION
		registro.addDefCampo("ID_REGIONAL",request.getParameter("IdRegional"));
		registro.addDefCampo("NOM_REGIONAL",request.getParameter("NomRegional"));
		registro.addDefCampo("CVE_USUARIO_GERENTE",request.getParameter("CveUsuarioGerente"));
		registro.addDefCampo("CVE_USUARIO_COORDINADOR",request.getParameter("CveUsuarioCoordinador"));
		
		
		registro.addDefCampo("ID_DOMICILIO", request.getParameter("IdDomicilio"));
		registro.addDefCampo("CALLE", request.getParameter("Calle")!= null ? request.getParameter("Calle") : "");
		registro.addDefCampo("NUMERO_INT", request.getParameter("NumeroInt")!= null ? request.getParameter("NumeroInt") : "");
		registro.addDefCampo("NUMERO_EXT", request.getParameter("NumeroExt")!= null ? request.getParameter("NumeroExt") : "");
		registro.addDefCampo("ID_REFER_POST", request.getParameter("IdReferPost")!= null ? request.getParameter("IdReferPost") : "");
		registro.addDefCampo("CODIGO_POSTAL", request.getParameter("CodigoPostal")!= null ? request.getParameter("CodigoPostal") : "");
		
		registro.addDefCampo("NOM_ASENTAMIENTO", request.getParameter("Colonia")!= null ? request.getParameter("Colonia") : "");
		registro.addDefCampo("TIPO_ASENTAMIENTO", request.getParameter("TipoAsentamiento")!= null ? request.getParameter("TipoAsentamiento") : "");
		registro.addDefCampo("NOM_DELEGACION", request.getParameter("Municipio")!= null ? request.getParameter("Municipio") : "");
		registro.addDefCampo("NOM_CIUDAD", request.getParameter("Ciudad")!= null ? request.getParameter("Ciudad") : "");
		registro.addDefCampo("NOM_ESTADO", request.getParameter("Estado")!= null ? request.getParameter("Estado") : "");
		
		registro.addDefCampo("CVE_TIPO_IDENTIFICADOR","REGION");
		registro.addDefCampo("IDENTIFICADOR",request.getParameter("IdRegional"));
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimCatalogoRegional", registro, iTipoOperacion);
		
		
		if (iTipoOperacion == 1){
			sIdRegional = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("ID_REGIONAL");
			sIdDomicilio = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("ID_DOMICILIO");
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimCatalogoRegional&OperacionCatalogo=CR&IdRegional="+sIdRegional+"&IdDomicilio="+sIdDomicilio;
		}
		if (iTipoOperacion == 2){
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimCatalogoRegional&OperacionCatalogo=CT&Filtro=Todos";
		}
		if (iTipoOperacion == 3){
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimCatalogoRegional&OperacionCatalogo=CT&Filtro=Todos";
		}
		
		return registroControl;
	}
}
