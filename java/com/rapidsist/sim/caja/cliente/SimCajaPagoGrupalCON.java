/**
 * Sistema de administraci�n de portales.
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
import java.math.BigDecimal;

/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificaci�n y consulta) para pagos en la caja. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimCajaPagoGrupalCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
		
		if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			/*
			parametros.addDefCampo("ID_GRUPO",request.getParameter("IdGrupo"));
			parametros.addDefCampo("ID_PRODUCTO",request.getParameter("IdProducto"));
			parametros.addDefCampo("NUM_CICLO",request.getParameter("NumCiclo"));
			
			Registro prestamogrupo = new Registro();
			prestamogrupo = catalogoSL.getRegistro("SimCajaPagoIdPrestamoGrupal", parametros);
			String sIdPrestamoGrupo = (String)prestamogrupo.getDefCampo("ID_PRESTAMO_GRUPO");
			*/ 
			parametros.addDefCampo("IMPORTE",request.getParameter("Importe"));
			parametros.addDefCampo("ID_PRESTAMO_GRUPO",request.getParameter("IdPrestamo"));
			parametros.addDefCampo("FECHA_MOVIMIENTO",request.getParameter("FechaMovimiento"));
			
			//Obtiene el rango
			Registro rango = new Registro();
			rango = catalogoSL.getRegistro("SimCajaDistribucionPago", parametros);
			String sRango =(String)rango.getDefCampo("IMP_VAR_PROPORCION");
			
			//En la ListaImporte debe de esta el cursor de la cantidades en propociones.
			registroControl.respuesta.addDefCampo("ListaImportes", catalogoSL.getRegistros("SimCajaDistribucionPago", parametros));
			
			parametros.addDefCampo("CONSULTA","MOVIMIENTOS");
			
			registroControl.respuesta.addDefCampo("ListaEstadoCuenta", catalogoSL.getRegistros("SimPrestamoEstadoCuentaGrupo", parametros));
			
			parametros.addDefCampo("CONSULTA","SALDO_FECHA");
			
			registroControl.respuesta.addDefCampo("SaldoFecha", catalogoSL.getRegistros("SimPrestamoEstadoCuentaGrupo", parametros));
			
			parametros.addDefCampo("CONSULTA","RESUMEN");
			
			registroControl.respuesta.addDefCampo("ListaEstadoCuentaResumenGrupo", catalogoSL.getRegistros("SimPrestamoEstadoCuentaResumenGrupo", parametros));
			
			parametros.addDefCampo("CONSULTA","SALDO_TOTAL");
			
			registroControl.respuesta.addDefCampo("SaldoTotal", catalogoSL.getRegistros("SimPrestamoEstadoCuentaResumenGrupo", parametros));
			
			registroControl.sPagina = "/Aplicaciones/Sim/Caja/fSimCajaPagoGpo.jsp?TxRespuesta="+request.getParameter("TxRespuesta")+"&TxPregunta="+request.getParameter("TxPregunta")+"&Pregunta="+request.getParameter("Pregunta")+"&PagoTotal="+request.getParameter("PagoTotal")+"&Saldo="+request.getParameter("Saldo")+"&Importe="+request.getParameter("Importe")+"&IdPrestamoGrupo="+request.getParameter("IdPrestamo")+"&FechaMovimiento="+request.getParameter("FechaMovimiento")+"&Rango="+sRango;
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Inicio")){
				parametros.addDefCampo("ID_PRESTAMO_GRUPO",request.getParameter("IdPrestamo"));
				
				registroControl.sPagina = "/Aplicaciones/Sim/Caja/fSimCajaPagCreGpo.jsp?TxRespuesta="+request.getParameter("TxRespuesta")+"&TxPregunta="+request.getParameter("TxPregunta")+"&PagoTotal="+request.getParameter("PagoTotal")+"&Saldo="+request.getParameter("Saldo")+"&TotalImporte="+request.getParameter("Importe")+"&IdPrestamo="+request.getParameter("IdPrestamo");
			}else if (request.getParameter("Filtro").equals("Alta")){
				
			}
		}
		return registroControl;
	}

	/**
	 * Valida los p�rametros de entrada y ejecuta los servicios de alta, baja o cambio.
	 * @param registro Parametros que se recogen de la sesion del usuario y se le envian a la clase CON.
	 * Estos par�metros son: CVE_GPO_EMPRESA (Clave del grupo empresa), CVE_USUARIO_BITACORA (clave
	 * del usuario que realiza la operacion), RegistroOriginal (registro leido originalmente y
	 * se utiliza cuando se ejecuta la operaci�n de modificacion y se verifica que no se hallan
	 * realizado modificaciones al registro).
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
	 * @return Respuesta del servicio de alta, baja o cambio y la p�gina a donde
	 * se redirecciona el control.
	 * @throws RemoteException Si se gener� un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se gener� un error dentro de la clase CON.
	 */
	public RegistroControl actualiza(Registro registro, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();

		String sIdCaja = "";
		String sIdCajaSucursal = "";
		String sIdSucursal = "";
		String sIdMovimientoOperacion = "";
		String sRespuesta = "";
		
		String sMontoPago = "";
		String sImporte = "";
		String sRango = "";
		
		//RECUPERA LA SESION DEL USUARIO
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("Usuario");
		//AGREGA LA CLAVE DEL PORTAL Y DEL USUARIO DE LA SESION DEL USUARIO
		registro.addDefCampo("CVE_USUARIO", usuario.sCveUsuario);
		
		registro.addDefCampo("FECHA_MOVIMIENTO", request.getParameter("FechaMovimiento"));
		
		registro.addDefCampo("ID_CAJA", request.getParameter("IdCaja"));
		registro.addDefCampo("IMPORTE",request.getParameter("Importe"));
		registro.addDefCampo("ID_PRESTAMO_GRUPO",request.getParameter("IdPrestamoGrupo"));
		
		registro.addDefCampo("PAGO_TOTAL",request.getParameter("PagoTotal"));
		registro.addDefCampo("SALDO",request.getParameter("Saldo"));
	
		sIdCajaSucursal = request.getParameter("IdCaja");
		
		sIdSucursal = sIdCajaSucursal.substring(0, sIdCajaSucursal.indexOf("-"));
		sIdCaja = sIdCajaSucursal.substring(sIdCajaSucursal.indexOf("-")+1, sIdCajaSucursal.length());
		
		registro.addDefCampo("ID_CAJA", sIdCaja);
		registro.addDefCampo("ID_SUCURSAL", sIdSucursal);
		registro.addDefCampo("CVE_MOVIMIENTO_CAJA", "PAGOGPO");
		
		
		String sMontoAutorizado = new String();
		String sCliente = new String();
		String sPrestamoIndividual = new String();
	
		//OBTIENE EL ARREGLO CON LAS APLICACIONES A PROCESAR
		String[] sMontos = request.getParameterValues("MontoAutorizado");
		String[] sIdCliente = request.getParameterValues("IdCliente");
		String[] sIdPrestamoIndividual = request.getParameterValues("IdPrestamoIndividual");
		
		registro.addDefCampo("DAO_MONTOS", sMontos);
		registro.addDefCampo("DAO_CLIENTE", sIdCliente);
		registro.addDefCampo("DAO_ID_PRESTAMO_IND", sIdPrestamoIndividual);
		
		System.out.println("Rango"+sRango);
		sRango = request.getParameter("Rango");
		System.out.println("Importe"+sImporte);
		sImporte = request.getParameter("Importe");
		java.math.BigDecimal dRangoInferior = new java.math.BigDecimal("0.00");
		
		java.math.BigDecimal dRangoSuperior = new java.math.BigDecimal("0.00");
	
		java.math.BigDecimal dRango = new BigDecimal(sRango);
	
		java.math.BigDecimal dImporte = new BigDecimal(sImporte);

		java.math.BigDecimal dPago = new java.math.BigDecimal("0.00");
	
		if (sMontos != null) {
			for (int iNumParametro = 0; iNumParametro < sMontos.length; iNumParametro++) {
				sMontoPago = sMontos[iNumParametro];
				
				java.math.BigDecimal dMontoPago = new BigDecimal(sMontoPago); 
				
				dPago = dPago.add(dMontoPago);
				
			}
		}
		
		dRangoSuperior = dImporte.add(dRango);
		dRangoInferior = dImporte.subtract(dRango);
		
		if (sMontos != null) {
			/*
			if (dPago.doubleValue() > dRangoSuperior.doubleValue()){
				System.out.println("dPago"+dPago.doubleValue());
				System.out.println("dRangoSuperior"+dRangoSuperior.doubleValue());
				com.rapidsist.portal.catalogos.ResultadoCatalogo resultadoCatalogoControlado = new com.rapidsist.portal.catalogos.ResultadoCatalogo();
				resultadoCatalogoControlado.mensaje.setClave("SUMA_IMPORTE_INCORRECTO");
				resultadoCatalogoControlado.mensaje.setTipo("Aviso");
				resultadoCatalogoControlado.mensaje.setDescripcion("La suma de todo los importes individuales debe ser igual a la ingresada anteriormente");
				registroControl.resultadoCatalogo = resultadoCatalogoControlado;
			}else if (dPago.doubleValue() < dRangoInferior.doubleValue()){
				System.out.println("dPago"+dPago.doubleValue());
				System.out.println("dRangoInferior"+dRangoInferior.doubleValue());
				com.rapidsist.portal.catalogos.ResultadoCatalogo resultadoCatalogoControlado = new com.rapidsist.portal.catalogos.ResultadoCatalogo();
				resultadoCatalogoControlado.mensaje.setClave("SUMA_IMPORTE_INCORRECTO");
				resultadoCatalogoControlado.mensaje.setTipo("Aviso");
				resultadoCatalogoControlado.mensaje.setDescripcion("La suma de todo los importes individuales debe ser igual a la ingresada anteriormente");
				registroControl.resultadoCatalogo = resultadoCatalogoControlado;
			}else{
			*/
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimCajaPagoGrupal", registro, 1);//MODIFICACION
				sRespuesta = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("RESPUESTA");
				
				if (sRespuesta == null){
					sIdMovimientoOperacion = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("ID_MOVIMIENTO_OPERACION");
				}else {
					sIdMovimientoOperacion = "null";
				}
				registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimCajaConsultaPagarCredito&OperacionCatalogo=CR&AplicaA=GRUPO&IdPrestamo="+request.getParameter("IdPrestamoGrupo")+"&TxRespuesta=0&TxPregunta=0&PagoTotal=0&IdCaja="+request.getParameter("IdCaja")+"&Importe="+request.getParameter("Importe")+"&IdMovimientoOperacion="+sIdMovimientoOperacion+"&Respuesta="+sRespuesta;
				
			//}
		}
		
		return registroControl;
	}
}