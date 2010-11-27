/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.catalogos.cliente;

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
 * modificación y consulta) del catálogo de código postal.
 * Esta clase es llamada por el servlet {@link CatalogoS CatalogoS}.
 */
public class SimCatalogoCodigoPostalCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
		boolean bParametrosFiltro = false;
		//VERIFICA SI BUSCA TODOS LOS REGISTROS
		if (iTipoOperacion == CON_CONSULTA_TABLA){

			//VERIFICA SI SE ENVIO EL PARAMETRO CODIGO_POSTAL
			if (request.getParameter("CodigoPostal") != null && !request.getParameter("CodigoPostal").equals("")){
				parametros.addDefCampo("CODIGO_POSTAL", request.getParameter("CodigoPostal"));
				bParametrosFiltro = true;
			}

			//VERIFICA SI SE ENVIO EL PARAMETRO NOM_ASENTAMIENTO
			if (request.getParameter("NomAsentamiento") != null && !request.getParameter("NomAsentamiento").equals("")){
				parametros.addDefCampo("NOM_ASENTAMIENTO", request.getParameter("NomAsentamiento"));
				bParametrosFiltro = true;
			}

            		//VERIFICA SI SE RECOLECTARON LOS PARAMETROS PARA OBTENER LA CONSULTA DE LOS CODIGOS POSTALES
			if (bParametrosFiltro){
				registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimCatalogoCodigoPostal", parametros));
			}
			registroControl.sPagina = "/Aplicaciones/Sim/CatalogosGenerales/fSimCodPosCon.jsp";
			
		}else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
		
			parametros.addDefCampo("CODIGO_POSTAL", request.getParameter("CodigoPostal"));
			
			parametros.addDefCampo("ID_REFER_POST", request.getParameter("IdReferPost"));
			
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimCatalogoCodigoPostal", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/CatalogosGenerales/fSimCodPosReg.jsp";
		}
		
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Inicio")){
				registroControl.sPagina = "/Aplicaciones/Sim/CatalogosGenerales/fSimCodPosCon.jsp";
			}else if (request.getParameter("Filtro").equals("Alta")){
				registroControl.sPagina = "/Aplicaciones/Sim/CatalogosGenerales/fSimCodPosReg.jsp";
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
		registro.addDefCampo("CODIGO_POSTAL",request.getParameter("CodigoPostal"));
		registro.addDefCampo("ID_REFER_POST",request.getParameter("IdReferPost"));
		registro.addDefCampo("NOM_ASENTAMIENTO",request.getParameter("NomAsentamiento"));
		registro.addDefCampo("TIPO_ASENTAMIENTO",request.getParameter("TipoAsentamiento"));
		registro.addDefCampo("NOM_DELEGACION",request.getParameter("NomDelegacion"));
		registro.addDefCampo("NOM_CIUDAD",request.getParameter("NomCiudad"));
		registro.addDefCampo("NOM_ESTADO",request.getParameter("NomEstado"));
		registro.addDefCampo("FILTRO_DATOS",request.getParameter("FiltroRegresaDatos"));

		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimCatalogoCodigoPostal&OperacionCatalogo=CT&Filtro=Todos&FiltroRegresaDatos="+request.getParameter("FiltroRegresaDatos");

		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimCatalogoCodigoPostal", registro, iTipoOperacion);
		return registroControl;
	}
}
