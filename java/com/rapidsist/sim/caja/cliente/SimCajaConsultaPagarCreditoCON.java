/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
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
 * modificación y consulta) para pagos en la caja. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimCajaConsultaPagarCreditoCON implements CatalogoControlConsultaIN {

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
			
			String sIdCaja = "";
			String sIdCajaSucursal = "";
			String sIdSucursal = "";
			
			sIdCajaSucursal = request.getParameter("IdCaja");
			
			sIdSucursal = sIdCajaSucursal.substring(0, sIdCajaSucursal.indexOf("-"));
			sIdCaja = sIdCajaSucursal.substring(sIdCajaSucursal.indexOf("-")+1, sIdCajaSucursal.length());
			
			parametros.addDefCampo("ID_CAJA", sIdCaja);
			parametros.addDefCampo("ID_SUCURSAL", sIdSucursal);
			parametros.addDefCampo("APLICA_A", request.getParameter("AplicaA"));
			
			if (request.getParameter("CvePrestamo") != null && !request.getParameter("CvePrestamo").equals("")){
				parametros.addDefCampo("CVE_PRESTAMO", request.getParameter("CvePrestamo"));
			}
			if (request.getParameter("Nombre") != null && !request.getParameter("Nombre").equals("")){
				parametros.addDefCampo("NOMBRE", request.getParameter("Nombre"));
			}
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimCajaConsultaPagarCredito", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Caja/fSimCajaConPagCre.jsp";
		}else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			parametros.addDefCampo("ID_PRESTAMO", request.getParameter("IdPrestamo"));
			
			
			if (request.getParameter("AplicaA").equals("INDIVIDUAL")){
				parametros.addDefCampo("APLICA_A","INDIVIDUAL");
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimCajaConsultaPagarCredito", parametros));
				parametros.addDefCampo("CONSULTA","MOVIMIENTOS");
				registroControl.respuesta.addDefCampo("ListaEstadoCuenta", catalogoSL.getRegistros("SimPrestamoEstadoCuenta", parametros));
				parametros.addDefCampo("CONSULTA","SALDO_FECHA");
				registroControl.respuesta.addDefCampo("SaldoFecha", catalogoSL.getRegistros("SimPrestamoEstadoCuenta", parametros));
				parametros.addDefCampo("CONSULTA","RESUMEN");
				registroControl.respuesta.addDefCampo("ListaEstadoCuentaResumen", catalogoSL.getRegistros("SimPrestamoEstadoCuentaResumen", parametros));
				parametros.addDefCampo("CONSULTA","SALDO_TOTAL");
				registroControl.respuesta.addDefCampo("SaldoTotal", catalogoSL.getRegistros("SimPrestamoEstadoCuentaResumen", parametros));
				
				//Consulta si no tiene saldo positivo en la cuenta del cliente.
				parametros.addDefCampo("CONSULTA","SALDO_INDIVIDUAL");
				Registro saldocliente = new Registro ();
				saldocliente = catalogoSL.getRegistro("SimPrestamoSaldoCuenta", parametros);
				String sSaldocliente = (String)saldocliente.getDefCampo("SDO_EFECTIVO");
				registroControl.sPagina = "/Aplicaciones/Sim/Caja/fSimCajaPagCreInd.jsp?IdCaja="+request.getParameter("IdCaja")+"&FechaMovimiento="+request.getParameter("FechaMovimiento")+"&Respuesta="+request.getParameter("Respuesta")+"&SaldoCliente="+sSaldocliente;
			}else if (request.getParameter("AplicaA").equals("GRUPO")){
				parametros.addDefCampo("APLICA_A","GRUPO");
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimCajaConsultaPagarCredito", parametros));
				
				//Consulta si no tiene saldo positivo en la cuenta del cliente.
				parametros.addDefCampo("CONSULTA","SALDO_GRUPAL");
				parametros.addDefCampo("ID_PRESTAMO_GRUPO",request.getParameter("IdPrestamo"));
				
				Registro saldocliente = new Registro ();
				saldocliente = catalogoSL.getRegistro("SimPrestamoSaldoCuenta", parametros);
				String sSaldocliente = (String)saldocliente.getDefCampo("SDO_EFECTIVO");
				registroControl.sPagina = "/Aplicaciones/Sim/Caja/fSimCajaPagCreGpo.jsp?IdCaja="+request.getParameter("IdCaja")+"&FechaMovimiento="+request.getParameter("FechaMovimiento")+"&Respuesta="+request.getParameter("Respuesta")+"&SaldoCliente="+sSaldocliente;
			}
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Inicio")){
				registroControl.sPagina = "/Aplicaciones/Sim/Caja/fSimCajaConPagCre.jsp";
			}
		}
		return registroControl;
	}
}