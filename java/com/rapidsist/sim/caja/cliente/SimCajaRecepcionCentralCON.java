/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
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
 * modificación y consulta) para mostrar movimientos de Caja. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimCajaRecepcionCentralCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			
			if (request.getParameter("Fecha") != "" && !request.getParameter("Fecha").equals("")){	
				parametros.addDefCampo("FECHA", request.getParameter("Fecha"));
			}
			if (request.getParameter("Monto") != "" && !request.getParameter("Monto").equals("")){	
				parametros.addDefCampo("MONTO", request.getParameter("Monto"));
			}
			
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimCajaRecepcionCentral", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Caja/fSimCajaRecCenCon.jsp?IdCaja="+request.getParameter("IdCaja")+"&IdMovimientoOperacion=null";
		}	
		if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Inicio")){
				registroControl.sPagina = "/Aplicaciones/Sim/Caja/fSimCajaRecCenCon.jsp?IdCaja="+request.getParameter("IdCaja");
			}else if (request.getParameter("Filtro").equals("Alta")){
				registroControl.sPagina = "/Aplicaciones/Sim/Caja/fSimCajaRecCenReg.jsp?IdCaja="+request.getParameter("IdCaja");
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

		String sIdCaja = "";
		String sIdCajaSucursal = "";
		String sIdSucursal = "";
		String sIdMovimientoOperacion = "";
		
		//RECUPERA LA SESION DEL USUARIO
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("Usuario");
		//AGREGA LA CLAVE DEL PORTAL Y DEL USUARIO DE LA SESION DEL USUARIO
		registro.addDefCampo("CVE_USUARIO_CAJERO", usuario.sCveUsuario);
		registro.addDefCampo("MONTO", request.getParameter("Cantidad"));
		registro.addDefCampo("USUARIO_ENTREGA", request.getParameter("UsuarioEntrega"));
		
		sIdCajaSucursal = request.getParameter("IdCaja");
	
		sIdSucursal = sIdCajaSucursal.substring(0, sIdCajaSucursal.indexOf("-"));
		sIdCaja = sIdCajaSucursal.substring(sIdCajaSucursal.indexOf("-")+1, sIdCajaSucursal.length());
		
		registro.addDefCampo("ID_CAJA", sIdCaja);
		registro.addDefCampo("ID_SUCURSAL", sIdSucursal);
		registro.addDefCampo("CVE_MOVIMIENTO_CAJA", "RECCAJCEN");
		
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimCajaRecepcionCentral", registro, iTipoOperacion);
		sIdMovimientoOperacion = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("ID_MOVIMIENTO_OPERACION");
		
		
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimCajaRecepcionCentral&OperacionCatalogo=IN&Filtro=Inicio&IdCaja="+request.getParameter("IdCaja")+"&IdMovimientoOperacion="+sIdMovimientoOperacion;
		return registroControl;
	}
}