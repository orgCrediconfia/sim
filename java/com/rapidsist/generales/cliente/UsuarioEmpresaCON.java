/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.generales.cliente; 

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;

/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificación y consulta) del catálogo de usuarios. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */

public class UsuarioEmpresaCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
		//VARIABLE PARA UTILIZAR EL FILTRO DE BUSQUEDA
		boolean bParametrosFiltro = false;

		if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			parametros.addDefCampo("CVE_USUARIO", request.getParameter("CveUsuario"));
			parametros.eliminaCampo("CVE_GPO_EMPRESA");
			parametros.addDefCampo("CVE_GPO_EMPRESA", request.getParameter("CveGpoEmpresa"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("GeneralesUsuarioEmpresa", parametros));
			parametros.addDefCampo("Filtro", "Todos");
			registroControl.respuesta.addDefCampo("ListaUsuarioPerfil", catalogoSL.getRegistros("GeneralesUsuarioPerfil", parametros));
			registroControl.sPagina = "/Aplicaciones/Generales/fGralUsuEmpReg.jsp";
		}
		else if (iTipoOperacion == CON_CONSULTA_TABLA){

			String sCveGpoEmpresa = request.getParameter("CveGpoEmpresa");
			if (sCveGpoEmpresa != null){
				if (!sCveGpoEmpresa.equals("")){
					parametros.eliminaCampo("CVE_GPO_EMPRESA");
					parametros.addDefCampo("CVE_GPO_EMPRESA", sCveGpoEmpresa);
				}
			}
			parametros.addDefCampo("Filtro", request.getParameter("Filtro"));
			parametros.addDefCampo("CVE_APLICACION", request.getParameter("CveAplicacion"));
			//OBTIENE LOS USUARIOS QUE CONCUERDEN CON EL PATRON DE BUSQUEDA
			//VERIICA SI SE ENVIO EL PARAMETRO NOMBRE
			if (request.getParameter("NomCompleto") != null && !request.getParameter("NomCompleto").equals("")){
				parametros.addDefCampo("NOM_COMPLETO", request.getParameter("NomCompleto"));
			}
			//VERIFICA SI SE ENVIO EL PARAMETRO USUARIO
			if (request.getParameter("CveUsuarioBusqueda") != null && !request.getParameter("CveUsuarioBusqueda").equals("")){
				parametros.addDefCampo("CVE_USUARIO_BUSQUEDA", request.getParameter("CveUsuarioBusqueda"));
			}
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("GeneralesUsuarioEmpresa", parametros));

			registroControl.sPagina = "/Aplicaciones/Generales/fGralUsuEmpCon.jsp";
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			registroControl.sPagina = "/Aplicaciones/Generales/fGralUsuEmpReg.jsp";
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
		registro.addDefCampo("CVE_USUARIO", request.getParameter("CveUsuario"));
		registro.addDefCampo("LOCALIDAD", request.getParameter("Localidad"));
		registro.addDefCampo("PASSWORD", request.getParameter("Password"));
		registro.addDefCampo("NOM_ALIAS", request.getParameter("NomAlias"));
		registro.addDefCampo("ID_PERSONA", request.getParameter("IdPersona"));
		registro.addDefCampo("CVE_EMPRESA", request.getParameter("CveEmpresa"));

		String sCveGpoEmpresa = request.getParameter("CveGpoEmpresa");
		if (sCveGpoEmpresa != null){
			if (!sCveGpoEmpresa.equals("")){
				registro.eliminaCampo("CVE_GPO_EMPRESA");
				registro.addDefCampo("CVE_GPO_EMPRESA", sCveGpoEmpresa);
				registroControl.sPagina = "/Aplicaciones/Generales/fGralUsuEmpCon.jsp?CveGpoEmpresa=" + sCveGpoEmpresa;
			}
			else{
				registroControl.sPagina = "/Aplicaciones/Generales/fGralUsuEmpCon.jsp";
			}
		}
		else{
			registroControl.sPagina = "/Aplicaciones/Generales/fGralUsuEmpCon.jsp";
		}

		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("GeneralesUsuarioEmpresa", registro, iTipoOperacion);
		return registroControl;

	}
}