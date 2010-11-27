/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */
package com.rapidsist.generales.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import com.rapidsist.portal.configuracion.Usuario;

import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;

/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificación y consulta) del catálogo de personas. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class PersonaEmpresaCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			if (request.getParameter("NomCompleto") != null && !request.getParameter("NomCompleto").equals("")){
				parametros.addDefCampo("NOM_COMPLETO", request.getParameter("NomCompleto"));
			}
			//VERIFICA SI SE ENVIO EL PARAMETRO RFC
			if (request.getParameter("Rfc") != null && !request.getParameter("Rfc").equals("")){
				parametros.addDefCampo("RFC", request.getParameter("Rfc"));
			}
			if (!request.getParameter("Filtro").equals("RegresaRegistro")){
				//VERIFICA SI SE ENVIO EL PARAMETRO PERSONALIDAD FISICA
				if (! (request.getParameter("PersonaFisica").equals("null"))) {
					parametros.addDefCampo("B_PERSONA_FISICA", request.getParameter("PersonaFisica"));
				}
			}
			String sCveGpoEmpresa = request.getParameter("CveGpoEmpresa");
			if (sCveGpoEmpresa != null){
				if (!sCveGpoEmpresa.equals("")){
					parametros.eliminaCampo("CVE_GPO_EMPRESA");
					parametros.addDefCampo("CVE_GPO_EMPRESA", sCveGpoEmpresa);
				}
			}

			if (request.getParameter("IdPersona") != null && !request.getParameter("IdPersona").equals("")){
				parametros.addDefCampo("RFC", request.getParameter("IdPersona"));
			}

			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("GeneralesPersonaEmpresa", parametros));
			registroControl.respuesta.addDefCampo("ListaPersonalidad", catalogoSL.getRegistros("GeneralesPersonalidadFiscal", parametros));
			registroControl.respuesta.addDefCampo("ListaPais", catalogoSL.getRegistros("GeneralesPais", parametros));
			registroControl.sPagina = "/Aplicaciones/Generales/fGralPerEmpCon.jsp";
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Alta")){
				registroControl.respuesta.addDefCampo("ListaPersonalidad", catalogoSL.getRegistros("GeneralesPersonalidadFiscal", parametros));
				registroControl.respuesta.addDefCampo("ListaPais", catalogoSL.getRegistros("GeneralesPais", parametros));
				registroControl.sPagina = "/Aplicaciones/Generales/fGralPerEmpReg.jsp";
			}
			else{
				String sCveGpoEmpresa = request.getParameter("CveGpoEmpresa");
				if (sCveGpoEmpresa == null){
					sCveGpoEmpresa = (String)parametros.getDefCampo("CVE_GPO_EMPRESA");
				}
				
				registroControl.respuesta.addDefCampo("ListaPersonalidad", catalogoSL.getRegistros("GeneralesPersonalidadFiscal", parametros));
				registroControl.respuesta.addDefCampo("ListaPais", catalogoSL.getRegistros("GeneralesPais", parametros));
				registroControl.sPagina = "/Aplicaciones/Generales/fGralPerEmpCon.jsp?CveGpoEmpresa="+sCveGpoEmpresa;
			}
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){

			String sCveGpoEmpresa = request.getParameter("CveGpoEmpresa");
			if (sCveGpoEmpresa != null){
				if (!sCveGpoEmpresa.equals("")){
					parametros.eliminaCampo("CVE_GPO_EMPRESA");
					parametros.addDefCampo("CVE_GPO_EMPRESA", sCveGpoEmpresa);
				}
			}

			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("ID_PERSONA",request.getParameter("IdPersona"));

			registroControl.respuesta.addDefCampo("ListaPersonalidad", catalogoSL.getRegistros("GeneralesPersonalidadFiscal", parametros));
			registroControl.respuesta.addDefCampo("ListaPais", catalogoSL.getRegistros("GeneralesPais", parametros));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("GeneralesPersonaEmpresa", parametros));
			//VERIFICA SI SOLO CONSULTA LOS DATOS DEL REGISTRO
			if (request.getParameter("Consulta")!=null){
				registroControl.sPagina = "/Aplicaciones/Generales/fGralPerEmpDat.jsp";
			}
			else{
				registroControl.sPagina = "/Aplicaciones/Generales/fGralPerEmpReg.jsp";
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

		//OBTIENE PARAMETROS DE PERSONA
		registro.addDefCampo("ID_DOMICILIO", request.getParameter("IdDomicilio")!= null ? request.getParameter("IdDomicilio") : "");

		if (!request.getParameter("CvePais").equals("MEX")){
			registro.addDefCampo("CVE_CODIGO_POSTAL", request.getParameter("CveCodigoPostal")!= null ? request.getParameter("CveCodigoPostal") : "");
			registro.addDefCampo("NOM_ASENTAMIENTO", request.getParameter("NomAsentamientoExt")!= null ? request.getParameter("NomAsentamientoExt") : "");
			registro.addDefCampo("TIPO_ASENTAMIENTO", request.getParameter("TipoAsentamientoExt")!= null ? request.getParameter("TipoAsentamientoExt") : "");
			registro.addDefCampo("NOM_CIUDAD", request.getParameter("NomCiudadExt")!= null ? request.getParameter("NomCiudadExt") : "");
			registro.addDefCampo("NOM_DELEGACION", request.getParameter("TipoAsentamientoExt")!= null ? request.getParameter("TipoAsentamientoExt") : "");
			registro.addDefCampo("NOM_ESTADO", request.getParameter("NomEstadoExt")!= null ? request.getParameter("NomEstadoExt") : "");
		}

		registro.addDefCampo("CODIGO_POSTAL", request.getParameter("CodigoPostal")!= null ? request.getParameter("CodigoPostal") : "");
		registro.addDefCampo("ID_REFER_POST", request.getParameter("IdReferPost")!= null ? request.getParameter("IdReferPost") : "");
		registro.addDefCampo("CVE_PAIS", request.getParameter("CvePais")!= null ? request.getParameter("CvePais") : "");
		registro.addDefCampo("CALLE", request.getParameter("Calle")!= null ? request.getParameter("Calle") : "");
		registro.addDefCampo("NUMERO", request.getParameter("Numero")!= null ? request.getParameter("Numero") : "");
		registro.addDefCampo("TX_REFERENCIA", request.getParameter("TxReferencia")!= null ? request.getParameter("TxReferencia") : "");

		registro.addDefCampo("ID_PERSONA", request.getParameter("IdPersona")!= null ? request.getParameter("IdPersona") : "" );
		registro.addDefCampo("AP_PATERNO", request.getParameter("ApPaterno") != null ? request.getParameter("ApPaterno") : "" );
		registro.addDefCampo("AP_MATERNO", request.getParameter("ApMaterno") != null ? request.getParameter("ApMaterno") : "" );
		registro.addDefCampo("NOM_PERSONA", request.getParameter("NomPersona") != null ? request.getParameter("NomPersona") : "");
		registro.addDefCampo("CURP", request.getParameter("Curp")!= null ? request.getParameter("Curp") : "");
		registro.addDefCampo("EMAIL", request.getParameter("EMail")!= null ? request.getParameter("EMail") : "");
		
		//OBTIENE LA CLAVE DE LA EMPRESA DEL USUARIO AUTENTIFICADO EN EL PORTAL
		HttpSession session = request.getSession(true);
		Usuario usuario = (Usuario)session.getAttribute("Usuario");
		String sCveEmpresa = usuario.sCveEmpresa;
		registro.addDefCampo("CVE_EMPRESA",sCveEmpresa);

		if (request.getParameter("PersonaFisicaDatos").equals("F")){
			registro.addDefCampo("NOM_COMPLETO", request.getParameter("RazonSocial"));
			registro.addDefCampo("B_PERSONA_FISICA",request.getParameter("PersonaFisicaDatos"));

		}else{
			if (request.getParameter("PersonaFisicaDatos").equals("V")){
				registro.addDefCampo("NOM_COMPLETO", request.getParameter("NomCompletoPersona"));
				registro.addDefCampo("B_PERSONA_FISICA",request.getParameter("PersonaFisicaDatos"));
			}
		}
		if (request.getParameter("PersonaFisicaDatos").equals("")){

				if (request.getParameter("PersonaFisica").equals("F")) {
					registro.addDefCampo("B_PERSONA_FISICA", request.getParameter("PersonaFisica"));
					registro.addDefCampo("NOM_COMPLETO", request.getParameter("RazonSocial"));
				}
				else {
					if (request.getParameter("PersonaFisica").equals("V")) {
						registro.addDefCampo("CURP", request.getParameter("Curp"));
						registro.addDefCampo("B_PERSONA_FISICA", request.getParameter("PersonaFisica"));
						registro.addDefCampo("NOM_COMPLETO", request.getParameter("NomCompletoPersona"));
					}
				}
			}
		String sNomCompleto =  (String)registro.getDefCampo("NOM_COMPLETO");
		registro.addDefCampo("RFC",request.getParameter("Rfc"));
		registro.addDefCampo("NUM_TEL",request.getParameter("NumTel"));
		String sCveGpoEmpresa = request.getParameter("CveGpoEmpresa");
		if (sCveGpoEmpresa != null){
			if (!sCveGpoEmpresa.equals("")){
				registro.eliminaCampo("CVE_GPO_EMPRESA");
				registro.addDefCampo("CVE_GPO_EMPRESA", sCveGpoEmpresa);
				registroControl.sPagina = "/ProcesaCatalogo?Funcion=GeneralesPersonaEmpresa&OperacionCatalogo=CT&CveGpoEmpresa=" + sCveGpoEmpresa + "&NomCompleto=" + sNomCompleto + "&Filtro=RegresaRegistro";
			}
			else{
				registroControl.sPagina = "/ProcesaCatalogo?Funcion=GeneralesPersonaEmpresa&OperacionCatalogo=IN&Filtro=Todos";
			}
		}
		else{
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=GeneralesPersonaEmpresa&OperacionCatalogo=IN&Filtro=Todos";
		}

		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("GeneralesPersonaEmpresa", registro, iTipoOperacion);
		return registroControl;
	}
}