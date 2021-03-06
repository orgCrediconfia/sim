/**
 * Sistema de administraci�n de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.herramientasconfiguracion.cliente;

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
import java.util.LinkedList;
import java.util.Enumeration;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;

/**
 * Esta clase se encarga de administrar  las operaciones (alta, baja,
 * modificaci�n y consulta) del cat�logo de tablas relacionadas. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class TablaDependienteCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
		if (iTipoOperacion == CON_CONSULTA_TABLA){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("CVE_TABLA",request.getParameter("CveTabla"));
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("HerramientasConfiguracionTablaDependiente", parametros));
			registroControl.sPagina = "/Aplicaciones/HerramientasConfiguracion/fTablTabConFil.jsp?Funcion=HerramientasConfiguracionTablaDependiente&CveTabla=" + request.getParameter("CveTabla");
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
		LinkedList listaTablas = null;
		
		if (request.getParameter("btnBaja") != null){
			iTipoOperacion = CatalogoControl.CON_BAJA;
		}
		if (request.getParameter("btnAlta") != null){
			iTipoOperacion = CatalogoControl.CON_ALTA;
		}
		Enumeration lista = request.getParameterNames();
		String sClaveTabla;
		//RECORRE LA LISTA DE PARAMETROS QUE VIENEN EN EL REQUEST
		while (lista.hasMoreElements()){
			String sNombre = (String)lista.nextElement();
			//VERIFICA SI EL PARAMETRO TIENE EL PREFIJO "Tabla"
			//ESTA FUNCIONALIDAD ES PARA BAJA Y ALTA
			if (sNombre.startsWith("Tabla")){
				//VERIFICA SI LA LISTA DE TABLAS ESTA INICIALIZADA
				if (listaTablas == null){
					listaTablas = new LinkedList();
				}
				sClaveTabla = sNombre.substring(5, sNombre.length());
				Registro registroTabla = new Registro();
				registroTabla.addDefCampo("CVE_TABLA", request.getParameter("CveTabla"));
				registroTabla.addDefCampo("CVE_TABLA_DEPENDIENTE", sClaveTabla);
				registroTabla.addDefCampo("B_CAMPOS_DIFERENTES", "F");
				listaTablas.add(registroTabla);
			}
		}

		if (request.getParameter("btnModificar") != null){
			iTipoOperacion = CatalogoControl.CON_MODIFICACION;
			
			//DESHABILITA EL COMPONENTE DE RE-READ
			registro.addDefCampo("OperacionInfraestructura", "DeshabilitaReRead");			
			
			//OBTIENE EL ARREGLO CON LAS TABLAS DEPENDIENTES A PROCESAR
			String[] sCveTablasDependientes = request.getParameterValues("CveTablaDependiente");
			//INICIALIZA LA LISTA DE TABLAS
			listaTablas = new LinkedList();

			//VERIFICA SI ENCONTRO EL ARREGLO DE TABLAS DEPENDIENTES
			if (sCveTablasDependientes != null){
				// INICIALIZA LA LISTA DE TABLAS DEPENDIENTES
				for (int iNumParametro = 0; iNumParametro < sCveTablasDependientes.length; iNumParametro++){
					//OBTIENE LA CLAVE DE LA TABLA DEPENDIENTE
					String sClaveTablaDependiente = sCveTablasDependientes[iNumParametro];			
					Registro registroTabla = new Registro();
					registroTabla.addDefCampo("CVE_TABLA", request.getParameter("CveTabla"));
					registroTabla.addDefCampo("CVE_TABLA_DEPENDIENTE", sClaveTablaDependiente);
					if (request.getParameter("Modificacion" + sClaveTablaDependiente) != null) {
						registroTabla.addDefCampo("B_CAMPOS_DIFERENTES", "V");
					}
					else {
						registroTabla.addDefCampo("B_CAMPOS_DIFERENTES", "F");
					}
					listaTablas.add(registroTabla);
				}// INICIALIZA LA LISTA DE TABLAS DEPENDIENTES
			}//VERIFICA SI ENCONTRO EL ARREGLO DE TABLAS DEPENDIENTES
		}
		
		//VERIFICA SI SE SELECCIONARON FUNCIONES PARA PROCESAR
		if (listaTablas != null){
			registro.addDefCampo("ListaRegistros", listaTablas);
			//DA DE ALTA O BAJA LAS FUNCIONES EN LA BASE DE DATOS
			registroControl.resultadoCatalogo = catalogoSL.modificacion("HerramientasConfiguracionTablaDependiente", registro, iTipoOperacion);
		}
		else{
			ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
			registroControl.resultadoCatalogo = resultadoCatalogo;
		}
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=HerramientasConfiguracionTabla&OperacionCatalogo=CR&CveTabla=" + request.getParameter("CveTabla");

		try {
			//DESTRUYE EL OBJETO FUNCION EN EL JNDI
			contexto.unbind("Tabla_"+(String)(request.getParameter("CveTabla")));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return registroControl;
	}
}

