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
 * modificación y consulta) del catálogo de cargos y comisiones. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimCatalogoCargoComisionCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			if (request.getParameter("IdCargoComision") != null && !request.getParameter("IdCargoComision").equals("")){
				parametros.addDefCampo("ID_ACCESORIO", request.getParameter("IdCargoComision"));	
			}
			
			//VERIFICA SI SE ENVIO EL PARAMETRO NOM GARANTIA
			if (request.getParameter("NomCargoComision") != null && !request.getParameter("NomCargoComision").equals("")){	
				parametros.addDefCampo("NOM_ACCESORIO", request.getParameter("NomCargoComision"));
			}
			
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimCatalogoCargoComision", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/CatalogosGenerales/fSimCarComCon.jsp";
		}
		
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("ID_ACCESORIO",request.getParameter("IdCargoComision"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimCatalogoCargoComision", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/CatalogosGenerales/fSimCarComReg.jsp";
		}
		
		if (iTipoOperacion == CON_INICIALIZACION){
				if (request.getParameter("Filtro").equals("Inicio")){
					registroControl.sPagina = "/Aplicaciones/Sim/CatalogosGenerales/fSimCarComCon.jsp";
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
		
		registro.addDefCampo("ID_ACCESORIO",request.getParameter("IdCargoComision"));
		registro.addDefCampo("NOM_ACCESORIO",request.getParameter("NomCargoComision"));

		if(request.getParameter("BeneficiarioNo")!=null){
			registro.addDefCampo("BENEFICIARIO","F");
		}else{
			registro.addDefCampo("BENEFICIARIO","V");
		}

		if(request.getParameter("BeneficiarioSi")!=null){
			registro.addDefCampo("BENEFICIARIO","V");
		}else{
			registro.addDefCampo("BENEFICIARIO","F");
		}
		
		if(request.getParameter("AccesorioNo")!=null){
			registro.addDefCampo("B_ACCESORIO","F");
		}else{
			registro.addDefCampo("B_ACCESORIO","V");
		}

		if(request.getParameter("AccesorioSi")!=null){
			registro.addDefCampo("B_ACCESORIO","V");
		}else{
			registro.addDefCampo("B_ACCESORIO","F");
		}
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS		
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimCatalogoCargoComision", registro, iTipoOperacion);
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimCatalogoCargoComision&OperacionCatalogo=CT&Filtro=Todos";

		return registroControl;
	}
}
