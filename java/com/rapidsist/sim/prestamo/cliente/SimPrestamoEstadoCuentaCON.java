/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.cliente;

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
 * Esta clase se encarga de administrar las operaciones 
 * de consulta del estado de cuenta del crédito. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimPrestamoEstadoCuentaCON implements CatalogoControlConsultaIN {

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
			if (request.getParameter("Consulta").equals("Prestamos")){
				parametros.addDefCampo("CVE_PRESTAMO", request.getParameter("CvePrestamo"));
				parametros.addDefCampo("NOMBRE", request.getParameter("Nombre"));
				parametros.addDefCampo("CONSULTA","PRESTAMOS");
				registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimPrestamoEstadoCuenta", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreEstCue.jsp";
			}
			if (request.getParameter("Consulta").equals("EstadoCuenta")){
			
				//VERIFICA SI SE ENVIO EL PARAMETRO CLAVE DEL COMITE	
				
				int iCvePrestamo = request.getParameter("CvePrestamo").length();
				parametros.addDefCampo("CONSULTAR","ESTADO_CUENTA");
				if (iCvePrestamo == 10){
					//Es un crédito grupal.
					parametros.addDefCampo("ID_PRESTAMO", request.getParameter("CvePrestamo"));
					Registro idprestamo = new Registro ();
					idprestamo = catalogoSL.getRegistro("SimPrestamoGrupoObtieneIdentificador", parametros);
					String sIdPrestamoGrupo = (String)idprestamo.getDefCampo("ID_PRESTAMO_GRUPO");
					parametros.addDefCampo("ID_PRESTAMO_GRUPO",sIdPrestamoGrupo);
					
					registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimCatalogoSucursal", parametros));
					registroControl.respuesta.addDefCampo("ListaAccesorios", catalogoSL.getRegistros("SimPrestamoAccesorioDatosTAGrupo", parametros));
					registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
					registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimPrestamoGrupal", parametros));
					parametros.addDefCampo("CONSULTA","MOVIMIENTOS");
					registroControl.respuesta.addDefCampo("ListaEstadoCuenta", catalogoSL.getRegistros("SimPrestamoEstadoCuentaGrupo", parametros));
					parametros.addDefCampo("CONSULTA","SALDO_FECHA");
					registroControl.respuesta.addDefCampo("SaldoFecha", catalogoSL.getRegistros("SimPrestamoEstadoCuentaGrupo", parametros));
					parametros.addDefCampo("CONSULTA","RESUMEN");
					registroControl.respuesta.addDefCampo("ListaEstadoCuentaResumenGrupo", catalogoSL.getRegistros("SimPrestamoEstadoCuentaResumenGrupo", parametros));
					parametros.addDefCampo("CONSULTA","SALDO_TOTAL");
					registroControl.respuesta.addDefCampo("SaldoTotal", catalogoSL.getRegistros("SimPrestamoEstadoCuentaResumenGrupo", parametros));
					registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreEstCueGpoReg.jsp";
				}else if (iCvePrestamo == 18){
					parametros.addDefCampo("CVE_PRESTAMO", request.getParameter("CvePrestamo"));
					Registro idprestamo = new Registro ();
					idprestamo = catalogoSL.getRegistro("SimPrestamoObtieneIdentificador", parametros);
					String sIdPrestamo = (String)idprestamo.getDefCampo("ID_PRESTAMO");
					
					parametros.addDefCampo("ID_PRESTAMO",sIdPrestamo);
					
					
					registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
					registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimCatalogoSucursal", parametros));
					registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimPrestamo", parametros));
					registroControl.respuesta.addDefCampo("registroFechaPago", catalogoSL.getRegistro("SimPrestamoConsultaFechaPrimerPago", parametros));
					registroControl.respuesta.addDefCampo("registroMontoAutorizado", catalogoSL.getRegistro("SimPrestamoMontoCliente", parametros));
					parametros.addDefCampo("CONSULTA","MOVIMIENTOS");
					registroControl.respuesta.addDefCampo("ListaEstadoCuenta", catalogoSL.getRegistros("SimPrestamoEstadoCuenta", parametros));
					parametros.addDefCampo("CONSULTA","SALDO_FECHA");
					registroControl.respuesta.addDefCampo("SaldoFecha", catalogoSL.getRegistros("SimPrestamoEstadoCuenta", parametros));
					parametros.addDefCampo("CONSULTA","RESUMEN");
					registroControl.respuesta.addDefCampo("ListaEstadoCuentaResumen", catalogoSL.getRegistros("SimPrestamoEstadoCuentaResumen", parametros));
					parametros.addDefCampo("CONSULTA","SALDO_TOTAL");
					registroControl.respuesta.addDefCampo("SaldoTotal", catalogoSL.getRegistros("SimPrestamoEstadoCuentaResumen", parametros));
					
					registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreEstCueReg.jsp";
				}
				
				parametros.addDefCampo("CVE_PRESTAMO",request.getParameter("CvePrestamo"));
			}
			
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Inicio")){
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreEstCue.jsp";
			}
		}
		return registroControl;
	}
}