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
 * modificación y consulta) del catálogo de documentación. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimCatalogoDocumentacionCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			//VERIFICA SI SE ENVIO EL PARAMETRO CVE GARANTIA	
			if (request.getParameter("IdDocumento") != null && !request.getParameter("IdDocumento").equals("")){
				parametros.addDefCampo("ID_DOCUMENTO", request.getParameter("IdDocumento"));	
			}
			
			//VERIFICA SI SE ENVIO EL PARAMETRO NOM GARANTIA
			if (request.getParameter("NomDocumento") != null && !request.getParameter("NomDocumento").equals("")){	
				parametros.addDefCampo("NOM_DOCUMENTO", request.getParameter("NomDocumento"));
			}
			
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimCatalogoDocumentacion", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/CatalogosGenerales/fSimDocCon.jsp";
		}
		
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("ID_DOCUMENTO",request.getParameter("IdDocumento"));
			registroControl.respuesta.addDefCampo("ListaReporte", catalogoSL.getRegistros("SimCatalogoReporte", parametros));
			registroControl.respuesta.addDefCampo("ListaTipoDocumentacion", catalogoSL.getRegistros("SimCatalogoTipoDocumentacion", parametros));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimCatalogoDocumentacion", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/CatalogosGenerales/fSimDocReg.jsp";
		}
		
		if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Inicio")){
				registroControl.sPagina = "/Aplicaciones/Sim/CatalogosGenerales/fSimDocCon.jsp";
			}else if (request.getParameter("Filtro").equals("Alta")){
				registroControl.respuesta.addDefCampo("ListaTipoDocumentacion", catalogoSL.getRegistros("SimCatalogoTipoDocumentacion", parametros));
				registroControl.respuesta.addDefCampo("ListaReporte", catalogoSL.getRegistros("SimCatalogoReporte", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/CatalogosGenerales/fSimDocReg.jsp";
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
		
		registro.addDefCampo("ID_DOCUMENTO",request.getParameter("IdDocumento"));
		registro.addDefCampo("NOM_DOCUMENTO",request.getParameter("NomDocumento"));
		registro.addDefCampo("ID_TIPO_DOCUMENTACION",request.getParameter("IdTipoDocumentacion"));
		registro.addDefCampo("DESCRIPCION",request.getParameter("Descripcion"));
		
		if(request.getParameter("IdReporte").equals("null")){
			registro.addDefCampo("ID_REPORTE","");
		}else{
			registro.addDefCampo("ID_REPORTE",request.getParameter("IdReporte"));
		}
		
		if(request.getParameter("BReporte") != null){
			registro.addDefCampo("B_REPORTE","V");
		}else{
			registro.addDefCampo("B_REPORTE","F");
		}
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS		
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimCatalogoDocumentacion", registro, iTipoOperacion);
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimCatalogoDocumentacion&OperacionCatalogo=CT&Filtro=Todos";

		return registroControl;
	}
}
