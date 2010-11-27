/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.personas.cliente;

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
 * Esta clase se encarga de administrar los servicios de detalle operación (alta, baja,
 * modificación y consulta) de los negocios secundarios del Cliente. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimClienteNegocioSecundarioCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			parametros.addDefCampo("ID_PERSONA",request.getParameter("IdPersona"));
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimClienteNegocio", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Personas/fSimCliNegCon.jsp?IdPersona="+request.getParameter("IdPersona");	
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//RECUPERA CAMPOS
			parametros.addDefCampo("ID_PERSONA",request.getParameter("IdPersona"));
			parametros.addDefCampo("ID_NEGOCIO",request.getParameter("IdNegocio"));
			parametros.addDefCampo("ID_NEGOCIO_SECUNDARIO",request.getParameter("IdNegocioSecundario"));
			registroControl.respuesta.addDefCampo("ListaTipoNegocio", catalogoSL.getRegistros("SimCatalogoTipoNegocio", parametros));
			registroControl.respuesta.addDefCampo("ListaGiro", catalogoSL.getRegistros("SimCatalogoGiro", parametros));
			registroControl.respuesta.addDefCampo("ListaTelefono", catalogoSL.getRegistros("SimNegocioSecundarioTelefono", parametros));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimClienteNegocioSecundario", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Personas/fSimCliNegSecReg.jsp?IdNegocio="+request.getParameter("IdNegocio")+"&IdNegocioSecundario="+request.getParameter("IdNegocioSecundario")+"&IdPersona="+request.getParameter("IdPersona");
		}
		else if (iTipoOperacion == CatalogoControl.CON_INICIALIZACION){
			//RECUPERA CAMPOS
			if (request.getParameter("Filtro").equals("Alta")){
				parametros.addDefCampo("ID_PERSONA",request.getParameter("IdPersona"));
				parametros.addDefCampo("ID_NEGOCIO", request.getParameter("IdNegocio"));
				registroControl.respuesta.addDefCampo("ListaTipoNegocio", catalogoSL.getRegistros("SimCatalogoTipoNegocio", parametros));
				registroControl.respuesta.addDefCampo("ListaGiro", catalogoSL.getRegistros("SimCatalogoGiro", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Personas/fSimCliNegSecReg.jsp?IdPersona="+request.getParameter("IdPersona")+"&IdNegocio="+request.getParameter("IdNegocio");	
			}
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
		
		//RECUPERA LOS CAMPOS DE LA MODIFICACION
		registro.addDefCampo("ID_PERSONA", request.getParameter("IdPersona"));
		registro.addDefCampo("ID_NEGOCIO", request.getParameter("IdNegocio"));
		registro.addDefCampo("NOM_NEGOCIO", request.getParameter("NomNegocio"));
		registro.addDefCampo("ID_TIPO_NEGOCIO", request.getParameter("IdTipoNegocio"));
		registro.addDefCampo("RFC", request.getParameter("Rfc"));
		registro.addDefCampo("CVE_CLASE", request.getParameter("IdGiro"));
		registro.addDefCampo("FECHA_INICIO_OPERACION", request.getParameter("FechaInicioOperacion"));
		registro.addDefCampo("NEGOCIO_PRINCIPAL", request.getParameter("NegocioPrincipal"));
		
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
		
		registro.addDefCampo("IDENTIFICADOR", request.getParameter("IdNegocio"));
		registro.addDefCampo("CVE_TIPO_IDENTIFICADOR","NEGOCIO");
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimClienteNegocioSecundario", registro, iTipoOperacion);
		//INDICA A DONDE IRA AL TERMINAR LA ACTUALIZACION
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimClienteNegocio&OperacionCatalogo=CR&IdPersona="+request.getParameter("IdPersona")+"&IdNegocio="+request.getParameter("IdNegocio");
		
		return registroControl;
	}
}
