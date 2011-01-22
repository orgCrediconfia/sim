/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.comite.cliente;

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
import java.util.Enumeration;

/**
 * Esta clase se encarga de administrar los servicios de operación (alta, baja,
 * modificación y consulta) de los préstamos de un comité. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimComitePrestamoMontoAutorizadoCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			parametros.addDefCampo("CONSULTA","TODOS");
			parametros.addDefCampo("ID_COMITE", request.getParameter("IdComite"));
			registroControl.respuesta.addDefCampo("ListaPrestamos", catalogoSL.getRegistros("SimComitePrestamo", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Comite/fSimComPreReg.jsp?IdComite="+request.getParameter("IdComite");
		}
		
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("ID_PRESTAMO", request.getParameter("IdPrestamo"));
			parametros.addDefCampo("PRESTAMO", request.getParameter("Prestamo"));
			parametros.addDefCampo("ID_COMITE", request.getParameter("IdComite"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimComitePrestamoMontoAutorizado", parametros));
			registroControl.respuesta.addDefCampo("ListaDiaSemanaPago", catalogoSL.getRegistros("SimCatalogoDiaSemanaPago", parametros));
			registroControl.respuesta.addDefCampo("ListaClienteMonto", catalogoSL.getRegistros("SimComitePrestamoMontoAutorizado", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Comite/fSimComPreMonAut.jsp?Prestamo="+request.getParameter("Prestamo")+"&IdPrestamo="+request.getParameter("IdPrestamo");
		}
		
		if (iTipoOperacion == CON_INICIALIZACION){
			registroControl.sPagina = "/Aplicaciones/Sim/Comite/fSimComIntCon.jsp";	
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
		
		String sMontoAutorizado = new String();
		String sCliente = new String();
		String sPrestamoIndividual = new String();
		float fMontoAutorizado = 0;
		float fMonto = 0;
		String sMontoMaximo = "";
		String sMontoMinimo = "";
		
		float fMontoMaximo = 0;
		float fMontoMinimo = 0;
		int iRegistros = 0;
		int iMonMinPer = 0;
		int iMonMaxPer = 0;
		
		//Obtiene los montos autorizados que ingresa el usuario,
		//trayendo tambien los Id de los cliente, y sus IdPrestamoIndividual.
		String[] sMontos = request.getParameterValues("MontoAutorizado");
		String[] sIdCliente = request.getParameterValues("IdCliente");
		String[] sIdPrestamoIndividual = request.getParameterValues("IdPrestamoIndividual");
		
		registro.addDefCampo("DAO_MONTOS", sMontos);
		registro.addDefCampo("DAO_CLIENTE", sIdCliente);
		registro.addDefCampo("DAO_ID_PRESTAMO_IND", sIdPrestamoIndividual);
		registro.addDefCampo("ID_PRESTAMO_IND_GPO", request.getParameter("IdPrestamo"));
		registro.addDefCampo("PRESTAMO", request.getParameter("Prestamo"));
		registro.addDefCampo("FECHA_ENTREGA", request.getParameter("FechaDesembolso"));
		registro.addDefCampo("DIA_SEMANA_PAGO", request.getParameter("DiaSemanaPago"));
		registro.addDefCampo("APLICA", request.getParameter("Prestamo"));
		
		sMontoMaximo = request.getParameter("MontoMaximo");
		sMontoMinimo = request.getParameter("MontoMinimo");

		fMontoMaximo = (Float.parseFloat(sMontoMaximo));
		fMontoMinimo = (Float.parseFloat(sMontoMinimo));
		
		for (int iNumParametro = 0; iNumParametro < sMontos.length; iNumParametro++) {
			iRegistros = sMontos.length;
			if (sMontos[iNumParametro] != "") {
				sMontoAutorizado = sMontos[iNumParametro];
				fMonto = (Float.parseFloat(sMontoAutorizado));
				if (fMonto >= fMontoMinimo){
					iMonMinPer++;
				}
				if (fMonto <= fMontoMaximo){
					iMonMaxPer++;
				}
			}
		}
		if (iRegistros != iMonMinPer){
			com.rapidsist.portal.catalogos.ResultadoCatalogo resultadoCatalogoControlado = new com.rapidsist.portal.catalogos.ResultadoCatalogo();
			resultadoCatalogoControlado.mensaje.setClave("MONTO_MINIMO_INCORRECTO_GPO");
			resultadoCatalogoControlado.mensaje.setTipo("Aviso");
			resultadoCatalogoControlado.mensaje.setDescripcion("Al menos un monto es menor al monto mínimo permitido");
			registroControl.resultadoCatalogo = resultadoCatalogoControlado;
		}else if (iRegistros != iMonMaxPer){
			com.rapidsist.portal.catalogos.ResultadoCatalogo resultadoCatalogoControlado = new com.rapidsist.portal.catalogos.ResultadoCatalogo();
			resultadoCatalogoControlado.mensaje.setClave("MONTO_MAXIMO_INCORRECTO_GPO");
			resultadoCatalogoControlado.mensaje.setTipo("Aviso");
			resultadoCatalogoControlado.mensaje.setDescripcion("Al menos un monto es mayor al monto máximo permitido");
			registroControl.resultadoCatalogo = resultadoCatalogoControlado;
		}else {
			registroControl.resultadoCatalogo = catalogoSL.modificacion("SimComitePrestamoMontoAutorizado", registro, 1);
		}	
		
		
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimComite&OperacionCatalogo=CR&IdComite="+request.getParameter("IdComite");
		
		return registroControl;
	}
}
