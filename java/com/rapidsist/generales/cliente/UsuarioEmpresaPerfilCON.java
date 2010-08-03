/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.generales.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import com.rapidsist.portal.cliente.CatalogoControl;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.LinkedList;
import javax.servlet.http.HttpSession;

import com.rapidsist.portal.configuracion.Usuario;

/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificación y consulta) del catálogo de usuario-perfil. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class UsuarioEmpresaPerfilCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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

		HttpSession session = request.getSession(true);
		Usuario usuario = (Usuario)session.getAttribute("Usuario");
		
		//OBTIENE EL PORTAL DONDE SE ENCUENTRA AUTENTIFICADO EL USUARIO
		String sPortal = usuario.sCvePortal;
		String sCveGpoEmpresaUsuario = usuario.sCveGpoEmpresa;
		
		parametros.addDefCampo("CVE_PORTAL",sPortal);
		
		RegistroControl registroControl = new RegistroControl();
		if (iTipoOperacion == CatalogoControl.CON_CONSULTA_TABLA){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("CVE_USUARIO",request.getParameter("CveUsuario"));

			String sCveGpoEmpresa = request.getParameter("CveGpoEmpresa");
			if (sCveGpoEmpresa != null){
				if (!sCveGpoEmpresa.equals("")){
					parametros.eliminaCampo("CVE_GPO_EMPRESA");
					parametros.addDefCampo("CVE_GPO_EMPRESA", sCveGpoEmpresa);
				}
			}

			registroControl.respuesta.addDefCampo("ListaAplicaciones", catalogoSL.getRegistros("GeneralesUsuarioPerfil", parametros));
			registroControl.sPagina = "/Aplicaciones/Generales/fGralUsuEmpPerAlt.jsp";
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

		LinkedList listaAplicaciones = null;

		//RECUPERA LA LISTA DE LAS APLICACIONES QUE SE VAN A PROCESAR
		String sClaveAplicacionPerfil;
		String sCveAplicacion;
		String sCvePerfil;
		String sBotonBaja = request.getParameter("btnBaja");
		String sBotonModificar = request.getParameter("btnModificar");

		if (iTipoOperacion == CatalogoControl.CON_ALTA || sBotonBaja !=  null){
			Enumeration lista = request.getParameterNames();
			//RECORRE LA LISTA DE PARAMETROS QUE VIENEN EN EL REQUEST
			while (lista.hasMoreElements()) {
				String sNombre = (String) lista.nextElement();

				//VERIFICA SI EL PARAMETRO TIENE EL PREFIJO "Aplicacion"
				if (sNombre.startsWith("AplicacionPerfil")) {
					//VERIFICA SI LA LISTA DE APLICACIONES ESTA INICIALIZADA
					if (listaAplicaciones == null) {
						listaAplicaciones = new LinkedList();
					}
					sClaveAplicacionPerfil = sNombre.substring(16, sNombre.length());
					sCveAplicacion = sClaveAplicacionPerfil.substring(0, sClaveAplicacionPerfil.indexOf("___"));
					sCvePerfil = sClaveAplicacionPerfil.substring(sClaveAplicacionPerfil.indexOf("___") + 3);
					Registro aplicacionPerfil = new Registro();
					aplicacionPerfil.addDefCampo("CVE_APLICACION", sCveAplicacion);
					aplicacionPerfil.addDefCampo("CVE_PERFIL", sCvePerfil);
					listaAplicaciones.add(aplicacionPerfil);
				}
			}
		}
		else if (sBotonModificar !=  null){
			iTipoOperacion = CatalogoControl.CON_MODIFICACION;
			if (request.getParameterValues("ModificacionAplicacionPerfil")!=null){
				String[] controlAplicacionPerfil = request.getParameterValues("ModificacionAplicacionPerfil");
				for (int control = 0; control < controlAplicacionPerfil.length; control++){
					//VERIFICA SI LA LISTA DE APLICACIONES ESTA INICIALIZADA
					if (listaAplicaciones == null) {
						listaAplicaciones = new LinkedList();
					}
					sClaveAplicacionPerfil = controlAplicacionPerfil[control];
					sCveAplicacion = sClaveAplicacionPerfil.substring(0, sClaveAplicacionPerfil.indexOf("___"));
					sCvePerfil = sClaveAplicacionPerfil.substring(sClaveAplicacionPerfil.indexOf("___") + 3);
					Registro aplicacionPerfil = new Registro();
					aplicacionPerfil.addDefCampo("CVE_APLICACION", sCveAplicacion);
					aplicacionPerfil.addDefCampo("CVE_PERFIL", sCvePerfil);
					String sBloqueado = request.getParameter("Bloqueado" + sClaveAplicacionPerfil);
					if (sBloqueado == null){
						aplicacionPerfil.addDefCampo("B_BLOQUEADO", "F");
					}
					else{
						aplicacionPerfil.addDefCampo("B_BLOQUEADO", "V");
					}
					listaAplicaciones.add(aplicacionPerfil);
				}
			}
		}
		registro.addDefCampo("CVE_USUARIO",request.getParameter("CveUsuario"));
		registro.addDefCampo("OperacionInfraestructura", "DeshabilitaReRead");
		String sCveGpoEmpresa = request.getParameter("CveGpoEmpresa");
		if (sCveGpoEmpresa != null){
			if (!sCveGpoEmpresa.equals("")){
				registro.eliminaCampo("CVE_GPO_EMPRESA");
				registro.addDefCampo("CVE_GPO_EMPRESA", sCveGpoEmpresa);
				registroControl.sPagina = "/ProcesaCatalogo?Funcion=GeneralesUsuarioEmpresa&OperacionCatalogo=CR&CveUsuario=" + request.getParameter("CveUsuario") + "&CveGpoEmpresa=" + sCveGpoEmpresa;
			}
			else{
				registroControl.sPagina = "/ProcesaCatalogo?Funcion=GeneralesUsuarioEmpresa&OperacionCatalogo=CR&CveUsuario=" + request.getParameter("CveUsuario");
			}
		}
		else{
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=GeneralesUsuarioEmpresa&OperacionCatalogo=CR&CveUsuario=" + request.getParameter("CveUsuario");
		}

		//VERIFICA SI SE SELECCIONARON LOS PERFILES PARA PROCESAR
		if (listaAplicaciones != null){
			registro.addDefCampo("ListaAplicaciones", listaAplicaciones);
			registroControl.resultadoCatalogo = catalogoSL.modificacion("GeneralesUsuarioEmpresaPerfil", registro, iTipoOperacion);
		}
		else{
			ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
			registroControl.resultadoCatalogo = resultadoCatalogo;
		}
		return registroControl;
	}
}
