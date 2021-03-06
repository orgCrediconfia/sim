/**
 * Sistema de administraci�n de portales.
 *
 * Copyright (c) 2008 Rapidsist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import javax.naming.Context;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;
import java.util.Enumeration;
import com.rapidsist.portal.configuracion.Usuario;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificaci�n y consulta) para mostrar movimientos de Caja. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimCajaCorteCON implements CatalogoControlConsultaIN {

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
			registroControl.respuesta.addDefCampo("ListaRegional", catalogoSL.getRegistros("SimCatalogoRegional", parametros));
			parametros.addDefCampo("ID_REGIONAL","");
			registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimRegionalSucursal", parametros));
			
			parametros.addDefCampo("ID_REGIONAL", request.getParameter("IdRegional"));
			parametros.addDefCampo("ID_SUCURSAL", request.getParameter("IdSucursal"));
			parametros.addDefCampo("FECHA_INICIAL", request.getParameter("FechaInicial"));
			parametros.addDefCampo("FECHA_FINAL", request.getParameter("FechaFinal"));
			
			registroControl.respuesta.addDefCampo("ListaMovimiento", catalogoSL.getRegistros("SimCajaCorte", parametros));
			parametros.addDefCampo("SALDO","INICIAL");
			registroControl.respuesta.addDefCampo("registroFechaInicial", catalogoSL.getRegistro("SimCajaCorte", parametros));
			parametros.addDefCampo("SALDO","FINAL");
			registroControl.respuesta.addDefCampo("registroFechaFinal", catalogoSL.getRegistro("SimCajaCorte", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Caja/fSimCajaCor.jsp?IdCaja="+request.getParameter("IdCaja");
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Inicio")){
				registroControl.respuesta.addDefCampo("ListaRegional", catalogoSL.getRegistros("SimCatalogoRegional", parametros));
				parametros.addDefCampo("ID_REGIONAL","");
				registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimRegionalSucursal", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Caja/fSimCajaCor.jsp?IdCaja="+request.getParameter("IdCaja")+"&IdMovimiento="+request.getParameter("IdMovimiento");
			}else if (request.getParameter("Filtro").equals("Sucursal")){
				registroControl.respuesta.addDefCampo("ListaRegional", catalogoSL.getRegistros("SimCatalogoRegional", parametros));
				parametros.addDefCampo("ID_REGIONAL",request.getParameter("IdRegional"));
				registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimRegionalSucursal", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Caja/fSimCajaCor.jsp";	
			}
		}
		return registroControl;
	}
}