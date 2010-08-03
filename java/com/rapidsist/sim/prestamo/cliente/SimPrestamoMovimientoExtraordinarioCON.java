/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.cliente;

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
 * modificación y consulta) de los productos asigandos a los préstamos. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimPrestamoMovimientoExtraordinarioCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			
			//VERIFICA SI SE ENVIO EL PARAMETRO CVE GARANTIA	
			if (request.getParameter("CvePrestamo") != null && !request.getParameter("CvePrestamo").equals("")){
				parametros.addDefCampo("CVE_PRESTAMO", request.getParameter("CvePrestamo"));	
			}
			
			//VERIFICA SI SE ENVIO EL PARAMETRO NOM GARANTIA
			if (request.getParameter("NomCompleto") != null && !request.getParameter("NomCompleto").equals("")){	
				parametros.addDefCampo("NOM_COMPLETO", request.getParameter("NomCompleto"));
			}
			
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimPrestamoMovimientoExtraordinario", parametros));
			
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreMovExt.jsp";
			
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			
			parametros.addDefCampo("ID_PRESTAMO",request.getParameter("IdPrestamo"));
			
			if (request.getParameter("Consulta").equals("Registro")){
				parametros.addDefCampo("OPERACION", "EXTRAORDINARIO");
				registroControl.respuesta.addDefCampo("ListaOperaciones", catalogoSL.getRegistros("SimPrestamoCatalogoOperacion", parametros));
				
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimPrestamoMovimientoExtraordinario", parametros));
				
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreMovExtReg.jsp?IdPrestamo="+request.getParameter("IdPrestamo");
			}else if (request.getParameter("Consulta").equals("Concepto")){
				
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimPrestamoMovimientoExtraordinario", parametros));
				parametros.addDefCampo("CVE_OPERACION", request.getParameter("CveOperacion"));
				parametros.addDefCampo("FECHA_MOVIMIENTO", request.getParameter("FechaMovimiento"));
				registroControl.respuesta.addDefCampo("ListaConcepto", catalogoSL.getRegistros("SimPrestamoCatalogoOperacionConcepto", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreMovExtConcep.jsp?IdPrestamo="+request.getParameter("IdPrestamo")+"&FechaMovimiento="+request.getParameter("FechaMovimiento")+"&NumAmort="+request.getParameter("NumAmort")+"&CveOperacion="+request.getParameter("CveOperacion");
				
			}
				
				
			
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Inicio")){
				//parametros.addDefCampo("OPERACION", "EXTRAORDINARIO");
				//registroControl.respuesta.addDefCampo("ListaOperaciones", catalogoSL.getRegistros("SimPrestamoCatalogoOperacion", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreMovExt.jsp";
			}else if (request.getParameter("Filtro").equals("Alta")){
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreMovExt.jsp";
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

		//RECUPERA LA SESION DEL USUARIO
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("Usuario");
		//AGREGA LA CLAVE DEL PORTAL Y DEL USUARIO DE LA SESION DEL USUARIO
		registro.addDefCampo("CVE_USUARIO", usuario.sCveUsuario);
		registro.addDefCampo("ID_PRESTAMO", request.getParameter("IdPrestamo"));
		
		registro.addDefCampo("CVE_OPERACION", request.getParameter("CveOperacion"));
		registro.addDefCampo("FECHA_MOVIMIENTO", request.getParameter("FechaMovimiento"));
		registro.addDefCampo("NUM_AMORT", request.getParameter("NumAmort"));
		
		float fSuma = 0;
		
		Enumeration suma = request.getParameterNames();
		while (suma.hasMoreElements()){
			
			String sNombre = (String)suma.nextElement();
			//VERIFICA SI EL PARAMETRO TIENE EL PREFIJO "FuncionAlta"
			if (sNombre.startsWith("CveConcepto")){
				//VERIFICA SI LA LISTA DE FUNCIONES ESTA INICIALIZADA
				String sConcepto = sNombre.substring(11, sNombre.length());
				float fSumaConcepto = Float.parseFloat((String)request.getParameter("Importe" + sConcepto));
				fSuma = fSuma + fSumaConcepto;
			}
		}
		
		String sSuma= String.valueOf(fSuma);
		registro.addDefCampo("IMP_NETO", sSuma);
		
		registro.addDefCampo("OPERACION", "PREMOVTO");
		
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoMovimientoExtraordinario", registro, iTipoOperacion);
	
		String sIdPreMovto = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("ID_PRE_MOVTO");
		
		registro.addDefCampo("ID_PREMOVTO", sIdPreMovto);
		
		//RECUPERA LA LISTA DE LAS FUNCIONES QUE SE VAN A PROCESAR
		String sCveConcepto;
		Enumeration lista = request.getParameterNames();
		//RECORRE LA LISTA DE PARAMETROS QUE VIENEN EN EL REQUEST
		while (lista.hasMoreElements()){
			
			String sNombre = (String)lista.nextElement();
			//VERIFICA SI EL PARAMETRO TIENE EL PREFIJO "FuncionAlta"
			if (sNombre.startsWith("CveConcepto")){
				
				//VERIFICA SI LA LISTA DE FUNCIONES ESTA INICIALIZADA
				sCveConcepto = sNombre.substring(11, sNombre.length());
				//registro.addDefCampo("ID_PRESTAMO", request.getParameter("IdPrestamo"));
				//registro.addDefCampo("CVE_OPERACION", request.getParameter("CveOperacion"));
				registro.addDefCampo("CVE_CONCEPTO", sCveConcepto);
				registro.addDefCampo("IMPORTE_CONCEPTO", request.getParameter("Importe" + sCveConcepto));
				
				registro.addDefCampo("OPERACION", "PREMOVTO_DET");
				//DA DE ALTA O BAJA LAS FUNCIONES EN LA BASE DE DATOS
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoMovimientoExtraordinario", registro, iTipoOperacion);
				
			}
		}
		
		registro.addDefCampo("OPERACION", "PROCESA_MOVTO");
		//DA DE ALTA O BAJA LAS FUNCIONES EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoMovimientoExtraordinario", registro, iTipoOperacion);
		
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimPrestamoMovimientoExtraordinario&OperacionCatalogo=IN&Filtro=Inicio";
		return registroControl;
	}
}