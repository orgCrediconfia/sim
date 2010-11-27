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
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;

/**
 * Esta clase se encarga de administrar los servicios de operación (alta, baja,
 * modificación y consulta) del catálogo de giro. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimCatalogoGiroCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			
			//VERIFICA SI SE ENVIO EL PARAMETRO CVE OPERACION
			if (request.getParameter("CveCaracteristica") != null && !request.getParameter("CveCaracteristica").equals("")){
				parametros.addDefCampo("CVE_CARACTERISTICA", request.getParameter("CveCaracteristica"));
			}
			
			//VERIFICA SI SE ENVIO EL PARAMETRO DESC OPERACION
			if (request.getParameter("DescCaracteristica") != null && !request.getParameter("DescCaracteristica").equals("")){
				parametros.addDefCampo("DESC_CARACTERISTICA", request.getParameter("DescCaracteristica"));
			}
			
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SiispAcaCaracteristica", parametros));
			registroControl.sPagina = "/Aplicaciones/Siisp/Aca/fSiispCaracteristicaCon.jsp";
		}
		
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("CVE_CARACTERISTICA",request.getParameter("CveCaracteristica"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SiispAcaCaracteristica", parametros));

			//OBTIENE LA LISTA DE CONCEPTO A DESPLEGAR PARA EL CAPITULO
			
			LinkedList listaCaractPropiedad = catalogoSL.getRegistros("SiispAcaCaracteristicaPropiedad", parametros);
			registroControl.respuesta.addDefCampo("listaCaractPropiedad", listaCaractPropiedad);
		
			registroControl.sPagina = "/Aplicaciones/Siisp/Aca/fSiispCaracteristicaReg.jsp";
			
		}
		
		if (iTipoOperacion == CON_INICIALIZACION){
			
			if (request.getParameter("Filtro").equals("Inicio")){
				registroControl.sPagina = "/Aplicaciones/Siisp/Aca/fSiispCaracteristicaCon.jsp";
			}
			if (request.getParameter("Filtro").equals("Alta")){
				registroControl.sPagina = "/Aplicaciones/Siisp/Aca/fSiispCaracteristicaReg.jsp";
			}
		
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

		registro.addDefCampo("CVE_CARACTERISTICA",request.getParameter("CveCaracteristica"));
		registro.addDefCampo("DESC_CARACTERISTICA",request.getParameter("DescCaracteristica"));
		registro.addDefCampo("ORDEN",request.getParameter("Orden"));
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS		
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SiispAcaCaracteristica", registro, iTipoOperacion);
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SiispAcaCaracteristica&OperacionCatalogo=IN&Filtro=Inicio";

		return registroControl;
	}
}
