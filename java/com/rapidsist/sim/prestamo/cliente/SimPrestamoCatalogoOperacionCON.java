/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.cliente;

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
 * modificación y consulta) de los productos asigandos a los préstamos. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimPrestamoCatalogoOperacionCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			
			if (request.getParameter("CveOperacion") != null && !request.getParameter("CveOperacion").equals("")){
				parametros.addDefCampo("CVE_OPERACION", request.getParameter("CveOperacion"));
			}
			if (request.getParameter("DescCorta") != null && !request.getParameter("DescCorta").equals("")){
				parametros.addDefCampo("DESC_CORTA", request.getParameter("DescCorta"));
			}
			if (request.getParameter("DescLarga") != null && !request.getParameter("DescLarga").equals("")){
				parametros.addDefCampo("DESC_LARGA", request.getParameter("DescLarga"));
			}
			if (request.getParameter("CveAfectaSaldo") != null && !request.getParameter("CveAfectaSaldo").equals("")){
				parametros.addDefCampo("CVE_AFECTA_SALDO", request.getParameter("CveAfectaSaldo"));
			}
			if (request.getParameter("CveAfectaCredito") != null && !request.getParameter("CveAfectaCredito").equals("")){
				parametros.addDefCampo("CVE_AFECTA_CREDITO", request.getParameter("CveAfectaCredito"));
			}
			parametros.addDefCampo("OPERACION", "TODAS");
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimPrestamoCatalogoOperacion", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreCatOpeCon.jsp";
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("CVE_OPERACION",request.getParameter("CveOperacion"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimPrestamoCatalogoOperacion", parametros));
			registroControl.respuesta.addDefCampo("ListaConcepto", catalogoSL.getRegistros("SimPrestamoCatalogoOperacionConcepto", parametros));
			//VERIFICA SI SOLO CONSULTA LOS DATOS DEL REGISTRO
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreCatOpeReg.jsp";
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Alta")){
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreCatOpeReg.jsp";
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
		registro.addDefCampo("CVE_OPERACION", request.getParameter("CveOperacion"));
		registro.addDefCampo("DESC_CORTA", request.getParameter("DescCorta"));
		registro.addDefCampo("DESC_LARGA", request.getParameter("DescLarga"));
		registro.addDefCampo("CVE_AFECTA_SALDO", request.getParameter("CveAfectaSaldo"));
		registro.addDefCampo("CVE_AFECTA_CREDITO", request.getParameter("CveAfectaCredito"));
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoCatalogoOperacion", registro, iTipoOperacion);
		
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimPrestamoCatalogoOperacion&OperacionCatalogo=CT&Filtro=Todos";
		return registroControl;
	}
}