/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import javax.naming.Context;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

import com.rapidsist.portal.configuracion.Usuario;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificación y consulta) para pagos en la caja. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimCajaPagoGrupalSaldoCON implements CatalogoControlActualizaIN{

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

		String sTxRespuesta = "";
		String sTxPregunta = "";
		String sPagoTotal = "";
		float fSaldo = 0;
		String sImporteR = "";
		String sTxValidaPagoLimite = "";
		
		registro.addDefCampo("IMPORTE",request.getParameter("Importe"));
		
		registro.addDefCampo("ID_PRESTAMO_GRUPO",request.getParameter("IdPrestamo"));
	
		registro.addDefCampo("CONSULTA","SALDO_FECHA");
		LinkedList lista = new LinkedList();
		lista = catalogoSL.getRegistros("SimPrestamoEstadoCuentaGrupo", registro);
		Iterator iLista = lista.iterator();
		
		while (iLista.hasNext()) {
			Registro Importe = (Registro) iLista.next();
			String sImporte = (String) Importe.getDefCampo("IMP_DESGLOSE");
			
			registro.addDefCampo("SALDO_FECHA",sImporte);
		}
		
		registro.addDefCampo("CONSULTA","SALDO_TOTAL");
		LinkedList listaR = new LinkedList();
		listaR = catalogoSL.getRegistros("SimPrestamoEstadoCuentaResumenGrupo", registro);
		
		Iterator iListaR = listaR.iterator();
		
		while (iListaR.hasNext()) {
			
			Registro ImporteR = (Registro) iListaR.next();
			sImporteR = (String) ImporteR.getDefCampo("SALDO_TOTAL");
			
			registro.addDefCampo("SALDO_TOTAL",sImporteR);
		
		}
		
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimCajaPagoGrupalSaldo", registro, 1);
		
		sTxRespuesta = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("DIFERENCIA");
		sPagoTotal = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("PAGO_TOTAL");
		fSaldo = (Float) registroControl.resultadoCatalogo.Resultado.getDefCampo("SALDO");
		sTxValidaPagoLimite = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("VALIDA_PAGO_LIMITE");
		
		if (sTxRespuesta == null){
			sTxRespuesta = "0";
			sTxPregunta = "1";
		}
		
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimCajaConsultaPagarCredito&OperacionCatalogo=CR&AplicaA=GRUPO&IdCaja="+request.getParameter("IdCaja")+"&IdPrestamo="+request.getParameter("IdPrestamo")+"&Importe="+request.getParameter("Importe")+"&FechaMovimiento="+request.getParameter("FechaMovimiento")+"&TxRespuesta="+sTxRespuesta+"&TxPregunta="+sTxPregunta+"&TxValidaPagoLimite="+sTxValidaPagoLimite+"&PagoTotal="+sPagoTotal+"&Saldo="+fSaldo+"&IdMovimientoOperacion=null";
		
		return registroControl;
	}
}