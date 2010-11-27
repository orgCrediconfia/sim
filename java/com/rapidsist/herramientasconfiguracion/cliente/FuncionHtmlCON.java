/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */
package com.rapidsist.herramientasconfiguracion.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import com.rapidsist.portal.configuracion.Usuario;

import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;



/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificación y consulta) del editor de HTML para funciones. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class FuncionHtmlCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
	 * especificadas en la clase {@link com.rap|sist.portal.cliente.CatalogoControl CatalogoControl}
	 * @return Resultado de la consulta y la página a donde se redirecciona el control.
	 * @throws RemoteException Si se generó un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se generó un error dentro de la clase CON.
	 */
	public RegistroControl consulta(Registro parametros, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		
		//OBTIENE LOS DATOS DEL USUARIO
		HttpSession session = request.getSession(true);
		Usuario usuario = (Usuario)session.getAttribute("Usuario");
		parametros.addDefCampo("CVE_PORTAL", usuario.sCvePortal);

		//VERIFICA SI BUSCA TODOS LOS REGISTROS
		if (iTipoOperacion == CON_CONSULTA_TABLA){

			//VERIFICA SI SE ENVIO EL PARAMETRO CVE PUBLICACION 
			if (request.getParameter("CveHtml") != null && !request.getParameter("CveHtml").equals("")){
				parametros.addDefCampo("CVE_HTML", request.getParameter("CveHtml"));
			}

			//VERIFICA SI SE ENVIO EL PARAMETRO NOMBRE PUBLICACION
			if (request.getParameter("NomHtml") != null && !request.getParameter("NomHtml").equals("")){
				parametros.addDefCampo("NOM_HTML", request.getParameter("NomHtml"));
			}
			
			//INDICA QUE VA A LA DAO PARA HACER LA CONSULTA
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("HerramientasConfiguracionFuncionHtml", parametros));
			
			//INDICA A DONDE IRÁ AL TERMINAR LA CONSULTA
			registroControl.sPagina = "/Aplicaciones/HerramientasConfiguracion/fApliFunHtmlCon.jsp";

		}
		
		else if (iTipoOperacion == CON_INICIALIZACION){
			//DIRECCCIONA A LA JSP DONDE SE MOSTRARÁ LA PANTALLA DE CAPTRUA
			registroControl.sPagina = "/Aplicaciones/HerramientasConfiguracion/fApliFunHtmlReg.jsp";
		}
		
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){

			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("CVE_HTML",request.getParameter("CveHtml"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("HerramientasConfiguracionFuncionHtml", parametros));
			registroControl.sPagina = "/Aplicaciones/HerramientasConfiguracion/fApliFunHtmlReg.jsp";

		}

		return registroControl;
	}

	/**
	 * ValCvea los párametros de entrada y ejecuta los servicios de alta, baja o cambio.
	 * @param registro Parametros que se recogen de la sesion del usuario y se le envian a la clase CON.
	 * Estos parámetros son: CVE_GPO_EMPRESA (Clave del grupo empresa), CVE_USUARIO_BITACORA (clave
	 * del usuario que realiza la operacion), RegistroOriginal (registro leCveo originalmente y
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
	 * @param contexto Objeto que contiene información acerca del entorno del servCveor de
	 * aplicaciones.
	 * @param iTipoOperacion Operación que debe ejecutar la clase CON. Las operaciones se encuentran
	 * especificadas en la clase {@link com.rapCvesist.portal.cliente.CatalogoControl CatalogoControl}
	 * @return Respuesta del servicio de alta, baja o cambio y la página a donde
	 * se redirecciona el control.
	 * @throws RemoteException Si se generó un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se generó un error dentro de la clase CON.
	 */
	
	public RegistroControl actualiza(Registro registro, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		
		//DESABILITA COMPONENTE RE-READ
		registro.addDefCampo("OperacionInfraestructura","DeshabilitaReRead");
				
		//OBTIENE LOS DATOS DEL USUARIO
		HttpSession session = request.getSession(true);
		Usuario usuario = (Usuario)session.getAttribute("Usuario");
		registro.addDefCampo("CVE_PORTAL", usuario.sCvePortal);
	
		//OBTIENE PARAMETROS DE LA PUBLICACION HTML
		
		//OBTIENE LA CVE PUBLICACIÓN
		registro.addDefCampo("CVE_HTML", request.getParameter("CveHtml")!= null ? request.getParameter("CveHtml") : "");
		//OBTIENE EL NOMBRE PUBLICACION
		registro.addDefCampo("NOM_HTML", request.getParameter("NomHtml")!= null ? request.getParameter("NomHtml") : "");

		registro.addDefCampo("CODIGO_HTML", request.getParameter("CodigoHtml")!= null ? request.getParameter("CodigoHtml") : "");
		
		int iLongitud = request.getParameter("CodigoHtml").length();
		
		if(iLongitud==0){
			com.rapidsist.portal.catalogos.ResultadoCatalogo resultadoCatalogoControlado = new com.rapidsist.portal.catalogos.ResultadoCatalogo();
			resultadoCatalogoControlado.mensaje.setClave("CODIGO_HTML_NULO");
			resultadoCatalogoControlado.mensaje.setTipo("Error");
			resultadoCatalogoControlado.mensaje.setDescripcion("El editor html no puede estar vacío. Por favor ingrese algún texto.");
			registroControl.resultadoCatalogo = resultadoCatalogoControlado;
		}
		
		else{
			//INDICA A DONDE IRÁ AL TERMINAR LA ACTUALIZACIÓN
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=HerramientasConfiguracionFuncionHtml&OperacionCatalogo=CT&Filtro=SinFiltro";
			//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
			registroControl.resultadoCatalogo = catalogoSL.modificacion("HerramientasConfiguracionFuncionHtml", registro, iTipoOperacion);
		}
			return registroControl;
	}
}
