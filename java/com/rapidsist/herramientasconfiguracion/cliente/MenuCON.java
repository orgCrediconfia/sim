/**
 * Sistema de administración de portales.
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

/**
 * Esta clase se encarga de administrar  las operaciones (alta, baja,
 * modificación y consulta) del catálogo de menús. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class MenuCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
		if (iTipoOperacion == CON_CONSULTA_REGISTRO){

			//OBTIENE EL REGISTRO DE LA OPCION DEL MENU
			parametros.addDefCampo("CVE_OPCION",request.getParameter("CveOpcion"));
			parametros.addDefCampo("CVE_APLICACION",request.getParameter("CveAplicacion"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("HerramientasConfiguracionMenu", parametros));

			//OBTIENE LAS FUNCIONES DISPONIBLES QUE SE PUEDEN ASIGNAR A LA OPCION DEL MENU
			parametros.addDefCampo("Filtro","FuncionesDisponiblesMenu");
			Registro resultadoConsulta = (Registro)registroControl.respuesta.getDefCampo("registro");
			String sCveFuncion = (String)resultadoConsulta.getDefCampo("CVE_FUNCION");
			parametros.addDefCampo("CVE_FUNCION", sCveFuncion);
			registroControl.respuesta.addDefCampo("ListaFunciones", catalogoSL.getRegistros("HerramientasConfiguracionAplicacionFuncion", parametros));
			parametros.eliminaCampo("Filtro");

			//OBTIENE LAS OPCIONES PADRE
			parametros.addDefCampo("Filtro","OpcionesPadreExcluyendoRegistro");
			String sCveOpcion = (String)resultadoConsulta.getDefCampo("CVE_OPCION");
			registroControl.respuesta.addDefCampo("ListaMenu", catalogoSL.getRegistros("HerramientasConfiguracionMenu", parametros));
		}
		else if (iTipoOperacion == CatalogoControl.CON_INICIALIZACION){
			parametros.addDefCampo("CVE_APLICACION",request.getParameter("CveAplicacion"));

			//OBTIENE LAS FUNCIONES DISPONIBLES QUE SE PUEDEN ASIGNAR A LA OPCION DEL MENU
			parametros.addDefCampo("Filtro","FuncionesDisponiblesMenu");
			registroControl.respuesta.addDefCampo("ListaFunciones", catalogoSL.getRegistros("HerramientasConfiguracionAplicacionFuncion", parametros));
			parametros.eliminaCampo("Filtro");

			//OBTIENE LAS OPCIONES PADRE
			parametros.addDefCampo("Filtro","Todas");
			registroControl.respuesta.addDefCampo("ListaMenu", catalogoSL.getRegistros("HerramientasConfiguracionMenu", parametros));
		}
		registroControl.sPagina = "/Aplicaciones/HerramientasConfiguracion/fApliAplMenReg.jsp";
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
		registro.addDefCampo("CVE_APLICACION",request.getParameter("CveAplicacion"));
		registro.addDefCampo("CVE_OPCION",request.getParameter("CveOpcion"));
		registro.addDefCampo("CVE_FUNCION",request.getParameter("CveFuncion"));
		registro.addDefCampo("NOM_OPCION",request.getParameter("NomOpcion"));
		registro.addDefCampo("CVE_OPCION_PADRE",request.getParameter("CveOpcionPadre"));
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=HerramientasConfiguracionAplicacion&OperacionCatalogo=CR&CveAplicacion=" + request.getParameter("CveAplicacion");

		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("HerramientasConfiguracionMenu", registro, iTipoOperacion);
		return registroControl;
	}
}