/**
 * Sistema de administraci�n de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.personas.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControl;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;

/**
 * Esta clase se encarga de administrar los servicios de detalle operaci�n (alta, baja,
 * modificaci�n y consulta) de los adeudos del Cliente. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimClienteAdeudoCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
		//OBTIENE SOLO EL REGISTRO SOLICITADO
		if (iTipoOperacion == CON_CONSULTA_TABLA){
			parametros.addDefCampo("ID_PERSONA",request.getParameter("IdPersona"));
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimClienteAdeudo", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Personas/fSimCliAdeCon.jsp?IdPersona="+request.getParameter("IdPersona");	
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//RECUPERA CAMPOS
			parametros.addDefCampo("ID_PERSONA",request.getParameter("IdPersona"));
			parametros.addDefCampo("ID_ADEUDO",request.getParameter("IdAdeudo"));
			registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimClienteAdeudo", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Personas/fSimCliAdeReg.jsp";
		}
		else if (iTipoOperacion == CatalogoControl.CON_INICIALIZACION){
			//RECUPERA CAMPOS
			if (request.getParameter("Filtro").equals("Alta")){
				parametros.addDefCampo("ID_PERSONA",request.getParameter("IdPersona"));
				registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Personas/fSimCliAdeReg.jsp?IdPersona="+request.getParameter("IdPersona");	
			}
		}
		return registroControl;
	}

	/**
	 * Valida los p�rametros entrada y ejecuta los servicios de alta, baja o cambio.
	 * @param registro Parametros que se recogen de la sesion del usuario y se le envian a la clase CON.
	 * Estos p�rametros son: CVE_GPO_EMPRESA (Clave del grupo empresa), CVE_USUARIO_BITACORA (clave
	 * del usuario que realiza la operacion), RegistroOriginal (registro leido originalmente y
	 * se utiliza cuando se ejecuta la Operaci�n de modificacion y se verifica que no se hallan
	 * realizado modificaciones al registro).
	 * @param request Objeto que provee de informaci�n al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo env�a como un p�rametro a este m�todo.
	 * @param response Objeto que provee de informaci�n del servlet sobre el response del cliente. El
	 * contenedor de servlets crea un objeto HttpServletResponse y lo env�a como un p�rametro a este m�todo.
	 * @param config Objeto que provee de informaci�n del servlet sobre el ServletConfig del cliente. El
	 * contenedor de servlets crea un objeto ServletConfig y lo env�a como un p�rametro a este m�todo.
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
		
		//RECUPERA LOS CAMPOS DE LA MODIFICACION
		registro.addDefCampo("ID_PERSONA", request.getParameter("IdPersona"));
		registro.addDefCampo("ID_ADEUDO", request.getParameter("IdAdeudo"));
		registro.addDefCampo("ADEUDO_A", request.getParameter("AdeudoA"));
		registro.addDefCampo("SALDO_ACTUAL", request.getParameter("SaldoActual"));
		registro.addDefCampo("FRECUENCIA_PAGO", request.getParameter("FrecuenciaPago"));
		registro.addDefCampo("MONTO_PAGO", request.getParameter("MontoPago"));
		registro.addDefCampo("FECHA", request.getParameter("Fecha"));
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimClienteAdeudo", registro, iTipoOperacion);
		//INDICA A DONDE IRA AL TERMINAR LA ACTUALIZACION
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimClientes&OperacionCatalogo=CR&IdPersona="+request.getParameter("IdPersona")+"&IdExConyuge="+request.getParameter("IdExConyuge");
		
		return registroControl;
	}
}