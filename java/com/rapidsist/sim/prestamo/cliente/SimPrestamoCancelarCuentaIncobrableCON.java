/**
 * Sistema de administraciï¿½n de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.cliente;

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
 * Esta clase se encarga de administrar los servicios de operaciï¿½n (alta, baja,
 * modificaciï¿½n y consulta) de la cancelacion de cuenta incobrable de un préstamo. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimPrestamoCancelarCuentaIncobrableCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

	/**
	 * Ejecuta los servicios de consulta del catï¿½logo.
	 * @param parametros Parï¿½metros que se recogen de la sesion del usuario y se le envian a la clase CON.
	 * Estos parï¿½metros son: CVE_GPO_EMPRESA (Clave del grupo empresa), Filtro (el valor
	 * del filtro que se debe aplicar solo si se ejecuto el componente de catï¿½logos con
	 * OperacionCatalogo=CT)
	 * @param request Objeto que provee de informaciï¿½n al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envï¿½a como un parï¿½metro a este mï¿½todo.
	 * @param response Objeto que provee de informaciï¿½n del servlet sobre el response del cliente. El
	 * contenedor de servlets crea un objeto HttpServletResponse y lo envï¿½a como un parï¿½metro a este mï¿½todo.
	 * @param config Objeto que provee de informaciï¿½n del servlet sobre el ServletConfig del cliente. El
	 * contenedor de servlets crea un objeto ServletConfig y lo envï¿½a como un parï¿½metro a este mï¿½todo.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON
	 * @param contexto Objeto que contiene informaciï¿½n acerca del entorno del servidor de
	 * aplicaciones.
	 * @param iTipoOperacion Operaciï¿½n que debe ejecutar la clase CON. Las operaciones se encuentran
	 * especificadas en la clase {@link com.rapidsist.portal.cliente.CatalogoControl CatalogoControl}
	 * @return Resultado de la consulta y la pï¿½gina a donde se redirecciona el control.
	 * @throws RemoteException Si se generï¿½ un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se generï¿½ un error dentro de la clase CON.
	 */
	public RegistroControl consulta(Registro parametros, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		
		if (iTipoOperacion == CON_CONSULTA_TABLA){
			
			//VERIFICA SI SE ENVIO EL PARAMETRO CVE GARANTIA	
			if (request.getParameter("CvePrestamo") != null && !request.getParameter("CvePrestamo").equals("")){
				parametros.addDefCampo("CVE_PRESTAMO", request.getParameter("CvePrestamo"));	
			}
			
			//VERIFICA SI SE ENVIO EL PARAMETRO NOM GARANTIA
			if (request.getParameter("Nombre") != null && !request.getParameter("Nombre").equals("")){	
				parametros.addDefCampo("NOM_COMPLETO", request.getParameter("Nombre"));
			}
			
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimPrestamoCancelarCuentaIncobrable", parametros));
			
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreCanCtaInc.jsp";
			
		}
		if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Inicio")){	
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreCanCtaInc.jsp";
			}
		}
		
		return registroControl;
	}
	
	/**
	 * Valida los pï¿½rametros entrada y ejecuta los servicios de alta, baja o cambio.
	 * @param registro Parametros que se recogen de la sesion del usuario y se le envian a la clase CON.
	 * Estos parï¿½metros son: CVE_GPO_EMPRESA (Clave del grupo empresa), CVE_USUARIO_BITACORA (clave
	 * del usuario que realiza la operacion), RegistroOriginal (registro leido originalmente y
	 * se utiliza cuando se ejecuta la operaciï¿½n de modificacion y se verifica que no se hallan
	 * realizado modificaciones al registro).
	 * @param request Objeto que provee de informaciï¿½n al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envï¿½a como un parï¿½metro a este mï¿½todo.
	 * @param response Objeto que provee de informaciï¿½n del servlet sobre el response del cliente. El
	 * contenedor de servlets crea un objeto HttpServletResponse y lo envï¿½a como un parï¿½metro a este mï¿½todo.
	 * @param config Objeto que provee de informaciï¿½n del servlet sobre el ServletConfig del cliente. El
	 * contenedor de servlets crea un objeto ServletConfig y lo envï¿½a como un parï¿½metro a este mï¿½todo.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON
	 * @param contexto Objeto que contiene informaciï¿½n acerca del entorno del servidor de
	 * aplicaciones.
	 * @param iTipoOperacion Operaciï¿½n que debe ejecutar la clase CON. Las operaciones se encuentran
	 * especificadas en la clase {@link com.rapidsist.portal.cliente.CatalogoControl CatalogoControl}
	 * @return Respuesta del servicio de alta, baja o cambio y la pï¿½gina a donde
	 * se redirecciona el control.
	 * @throws RemoteException Si se generï¿½ un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se generï¿½ un error dentro de la clase CON.
	 */
	public RegistroControl actualiza(Registro registro, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		
		//registro.addDefCampo("DobleSubmit","Desabilitado");
		String sRespuesta = "";
		
		registro.addDefCampo("ID_PRESTAMO",request.getParameter("IdPrestamo"));
		registro.addDefCampo("APLICA_A",request.getParameter("AplicaA"));
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoCancelarCuentaIncobrable", registro, iTipoOperacion);
		
		sRespuesta = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("RESPUESTA");
		
		registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreCanCtaInc.jsp?Respuesta="+sRespuesta;
		
		return registroControl;
	}
}
