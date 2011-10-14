/**
 * Sistema de administraci�n de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControl;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import javax.naming.Context;
import java.util.LinkedList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;

/**
 * Esta clase se encarga de administrar los servicios de operaci�n alta para generar 
 * la tabla de amortizaci�n. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimGenerarTablaAmortizacionCON implements CatalogoControlActualizaIN{

	/**
	 * Valida los p�rametros entrada y ejecuta los servicios de alta, baja o cambio.
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
		
		registro.addDefCampo("CVE_PRESTAMO",request.getParameter("CvePrestamo"));
		
		String sPrestamo = request.getParameter("CvePrestamo");
		String sCeros;
		String sGrupalIndividual;
		
		sCeros = sPrestamo.substring(2,8);
		sGrupalIndividual = sPrestamo.substring(8,16);
		
		System.out.println("sCeros:"+sCeros);
		System.out.println("sGrupalIndividual:"+sGrupalIndividual);
		if (sCeros.equals("000000")){
			//Es un cr�dito individual.
			Registro idprestamo = new Registro ();
			
			registro.addDefCampo("APLICA_A", "INDIVIDUAL");
			idprestamo = catalogoSL.getRegistro("SimPrestamoObtieneIdentificador", registro);
			String sIdPrestamo = (String)idprestamo.getDefCampo("ID_PRESTAMO");
			
			registro.addDefCampo("ID_PRESTAMO",sIdPrestamo);
			
			//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS		
			registroControl.resultadoCatalogo = catalogoSL.modificacion("SimGenerarTablaAmortizacion", registro, iTipoOperacion);
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimConsultaTablaAmortizacion&OperacionCatalogo=CT&Filtro=Todos&Consulta=Tabla&IdPrestamo="+sPrestamo;
			
		}else{
			if (sGrupalIndividual.equals("00000000")){
			//Es un cr�dito grupal.
			
			LinkedList lista = new LinkedList();
			lista = catalogoSL.getRegistros("SimPrestamoObtieneIdentificador", registro);
			
			Iterator iLista = lista.iterator();
			
			while (iLista.hasNext()) {
				//Genera LA TA de cada prestamo que confoma el grupal.
				Registro IdPrestamo = (Registro) iLista.next();
				
				String sIdPrestamo = (String) IdPrestamo.getDefCampo("ID_PRESTAMO");
			
				registro.addDefCampo("ID_PRESTAMO",sIdPrestamo);
				
				//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS		
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimGenerarTablaAmortizacion", registro, iTipoOperacion);
			}
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimConsultaTablaAmortizacion&OperacionCatalogo=CT&Filtro=Todos&Consulta=Tabla&IdPrestamo="+sPrestamo;
			}else {
				//Es un cr�dito grupal-individual.
				Registro idprestamo = new Registro ();
				registro.addDefCampo("APLICA_A", "INDIVIDUAL");
				idprestamo = catalogoSL.getRegistro("SimPrestamoObtieneIdentificador", registro);
				String sIdPrestamo = (String)idprestamo.getDefCampo("ID_PRESTAMO");
				
				registro.addDefCampo("ID_PRESTAMO",sIdPrestamo);
				
				//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS		
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimGenerarTablaAmortizacion", registro, iTipoOperacion);
				registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimConsultaTablaAmortizacion&OperacionCatalogo=CT&Filtro=Todos&Consulta=Tabla&IdPrestamo="+sPrestamo;
				
			}
		}
		return registroControl;
	}
}