/**
 * Sistema de administraci�n de portales.
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
 * Esta clase se encarga de administrar los servicios de operaci�n (alta, baja,
 * modificaci�n y consulta) del cat�logo de fondeadores. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimCatalogoReporteCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

	/**
	 * Ejecuta los servicios de consulta del cat�logo.
	 * @param parametros Par�metros que se recogen de la sesion del usuario y se le envian a la clase CON.
	 * Estos par�metros son: CVE_GPO_EMPRESA (Clave del grupo empresa), Filtro (el valor
	 * del filtro que se debe aplicar solo si se ejecuto el componente de cat�logos con
	 * OperacionCatalogo=CT)
	 * @param request Objeto que provee de informaci�n al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo env�a como un par�metro a este m�todo.
	 * @param response Objeto que provee de informaci�n del servlet sobre el response del cliente. El
	 * contenedor de servlets crea un objeto HttpServletResponse y lo env�a como un par�metro a este m�todo.
	 * @param config Objeto que provee de informaci�n del servlet sobre el ServletConfig del cliente. El
	 * contenedor de servlets crea un objeto ServletConfig y lo env�a como un par�metro a este m�todo.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON
	 * @param contexto Objeto que contiene informaci�n acerca del entorno del servidor de
	 * aplicaciones.
	 * @param iTipoOperacion Operaci�n que debe ejecutar la clase CON. Las operaciones se encuentran
	 * especificadas en la clase {@link com.rapidsist.portal.cliente.CatalogoControl CatalogoControl}
	 * @return Resultado de la consulta y la p�gina a donde se redirecciona el control.
	 * @throws RemoteException Si se gener� un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se gener� un error dentro de la clase CON.
	 */
	public RegistroControl consulta(Registro parametros, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		//VERIFICA SI BUSCA TODOS LOS REGISTROS

		if (iTipoOperacion == CON_CONSULTA_TABLA){
			//VERIFICA SI SE ENVIO EL PARAMETRO ID BANCO
			if (request.getParameter("IdReporte") != null && !request.getParameter("IdReporte").equals("")){
				parametros.addDefCampo("ID_REPORTE", request.getParameter("IdReporte"));
			}
			//VERIFICA SI SE ENVIO EL PARAMETRO NOM BANCO
			if (request.getParameter("NomReporte") != null && !request.getParameter("NomReporte").equals("")){
				parametros.addDefCampo("NOM_REPORTE", request.getParameter("NomReporte"));
			}
			if (request.getParameter("AplicaA") != null && !request.getParameter("AplicaA").equals("")){
				parametros.addDefCampo("APLICA_A", request.getParameter("AplicaA"));
			}
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimCatalogoReporte", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/CatalogosGenerales/fSimRepCon.jsp";
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("ID_REPORTE",request.getParameter("IdReporte"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimCatalogoReporte", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/CatalogosGenerales/fSimRepReg.jsp";
		}
		return registroControl;
	}

	/**
	 * Valida los p�rametros entrada y ejecuta los servicios de alta, baja o cambio.
	 * @param registro Parametros que se recogen de la sesion del usuario y se le envian a la clase CON.
	 * Estos par�metros son: CVE_GPO_EMPRESA (Clave del grupo empresa), CVE_USUARIO_BITACORA (clave
	 * del usuario que realiza la operacion), RegistroOriginal (registro leido originalmente y
	 * se utiliza cuando se ejecuta la operaci�n de modificacion y se verifica que no se hallan
	 * realizado modificaciones al registro).
	 * @param request Objeto que provee de informaci�n al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo env�a como un par�metro a este m�todo.
	 * @param response Objeto que provee de informaci�n del servlet sobre el response del cliente. El
	 * contenedor de servlets crea un objeto HttpServletResponse y lo env�a como un par�metro a este m�todo.
	 * @param config Objeto que provee de informaci�n del servlet sobre el ServletConfig del cliente. El
	 * contenedor de servlets crea un objeto ServletConfig y lo env�a como un par�metro a este m�todo.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON
	 * @param contexto Objeto que contiene informaci�n acerca del entorno del servidor de
	 * aplicaciones.
	 * @param iTipoOperacion Operaci�n que debe ejecutar la clase CON. Las operaciones se encuentran
	 * especificadas en la clase {@link com.rapidsist.portal.cliente.CatalogoControl CatalogoControl}
	 * @return Respuesta del servicio de alta, baja o cambio y la p�gina a donde
	 * se redirecciona el control.
	 * @throws RemoteException Si se gener� un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se gener� un error dentro de la clase CON.
	 */
	public RegistroControl actualiza(Registro registro, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();

		//RECUPERA LOS CAMPOS DE LA MODIFICACION
		registro.addDefCampo("ID_REPORTE",request.getParameter("IdReporte"));
		registro.addDefCampo("NOM_REPORTE",request.getParameter("NomReporte"));
		registro.addDefCampo("APLICA_A",request.getParameter("AplicaA"));
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimCatalogoReporte", registro, iTipoOperacion);
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimCatalogoReporte&OperacionCatalogo=CT&Filtro=Todos";

		return registroControl;
	}
}
