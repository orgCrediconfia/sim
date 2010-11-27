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



/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificación y consulta) de las direcciones del cónyuge o excónyuge del cliente. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimGaranteDepositarioExConyugeDireccionCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
		if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("IDENTIFICADOR",request.getParameter("IdPersona"));
			parametros.addDefCampo("ID_DOMICILIO",request.getParameter("IdDomicilio"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimGaranteDepositarioExConyugeDireccion", parametros));
			//registroControl.respuesta.addDefCampo("ListaTipoDomicilio", catalogoSL.getRegistros("SimCatalogoTipoDomicilio", parametros));
			//VERIFICA SI SOLO CONSULTA LOS DATOS DEL REGISTRO
			registroControl.sPagina = "/Aplicaciones/Sim/Personas/fSimExConDir.jsp?IdPersona="+request.getParameter("IdPersona");
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Alta")){
				registroControl.respuesta.addDefCampo("ListaTipoDomicilio", catalogoSL.getRegistros("SimCatalogoTipoDomicilio", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Personas/fSimGarDepExConDir.jsp";
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
		
		registro.addDefCampo("FECHA_INICIO_RESIDENCIA", request.getParameter("FechaInicioResidencia")!= null ? request.getParameter("FechaInicioResidencia") : "");
		registro.addDefCampo("ID_TIPO_DOMICILIO", request.getParameter("IdTipoDomicilio")!= null ? request.getParameter("IdTipoDomicilio") : "");
		
		registro.addDefCampo("ID_TIPO_DOMICILIO", request.getParameter("IdTipoDomicilio")!= null ? request.getParameter("IdTipoDomicilio") : "");
		registro.addDefCampo("FECHA_INICIO_RESIDENCIA", request.getParameter("FechaInicioResidencia")!= null ? request.getParameter("FechaInicioResidencia") : "");
		registro.addDefCampo("CVE_TIPO_IDENTIFICADOR","GARANTE");
		registro.addDefCampo("IDENTIFICADOR",request.getParameter("IdPersona"));
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		
		
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimGaranteDepositarioExConyugeDireccion", registro, iTipoOperacion);
		
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimGaranteDepositario&OperacionCatalogo=CR&IdPersona="+request.getParameter("IdPersona");
		return registroControl;
	}
}