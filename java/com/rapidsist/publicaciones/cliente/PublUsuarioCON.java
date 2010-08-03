/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.publicaciones.cliente;
 
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
 * Esta clase se encarga de administrar los servicios de operación (alta, baja,
 * modificación y consulta) del catálogo de usuarios. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class PublUsuarioCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
		boolean bParametrosFiltro = false;
		//RECUPERA LA SESION DEL USUARIO
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("Usuario");
		//AGREGA LA CLAVE DEL PORTAL Y DEL USUARIO DE LA SESION DEL USUARIO
		parametros.addDefCampo("CVE_PORTAL", usuario.sCvePortal);
		parametros.addDefCampo("CVE_USUARIO", usuario.sCveUsuario);
		
		Registro registroPortal = new Registro();
		registroPortal.addDefCampo("CVE_PORTAL", usuario.sCvePortal);

		//VERIFICA SI BUSCA TODOS LOS REGISTROS
		if (iTipoOperacion == CON_CONSULTA_TABLA){

			//VERIICA SI SE ENVIO EL PARAMETRO NOMBRE
			if (request.getParameter("NomCompleto") != null && !request.getParameter("NomCompleto").equals("")){
				parametros.addDefCampo("NOM_COMPLETO", request.getParameter("NomCompleto"));
				bParametrosFiltro = true;
			}
			//VERIFICA SI SE ENVIO EL PARAMETRO PERFIL
			if ( !request.getParameter("CvePerfilPub").equals("null")){
				parametros.addDefCampo("CVE_PERFIL_PUB", request.getParameter("CvePerfilPub"));
				bParametrosFiltro = true;
			}
			//VERIFICA SI SE ENVIO EL PARAMETRO PLANTILLA
			if ( !request.getParameter("IdNivelAcceso").equals("null")){
				parametros.addDefCampo("ID_NIVEL_ACCESO", request.getParameter("IdNivelAcceso"));
				bParametrosFiltro = true;
			}
			

			if (bParametrosFiltro){
				//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
				registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("PublicacionesUsuario", parametros));
			}
			
			
			
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			registroControl.respuesta.addDefCampo("registroPortal", registroPortal);
			registroControl.respuesta.addDefCampo("ListaPerfiles", catalogoSL.getRegistros("PublicacionesPerfil", parametros));
			registroControl.respuesta.addDefCampo("ListaNiveles", catalogoSL.getRegistros("PublicacionesNivelAcceso", parametros));
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("PublicacionesUsuario", parametros));
			registroControl.sPagina = "/Aplicaciones/Publicaciones/fPubliUsuarioCon.jsp";
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			registroControl.respuesta.addDefCampo("registroPortal", registroPortal);
			parametros.addDefCampo("CVE_USUARIO", request.getParameter("CveUsuario"));
			registroControl.respuesta.addDefCampo("ListaPerfiles", catalogoSL.getRegistros("PublicacionesPerfil", parametros));
			registroControl.respuesta.addDefCampo("ListaNiveles", catalogoSL.getRegistros("PublicacionesNivelAcceso", parametros));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("PublicacionesUsuario", parametros));
			registroControl.sPagina = "/Aplicaciones/Publicaciones/fPubliUsuarioReg.jsp";
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			

			if (request.getParameter("Filtro").equals("Alta")){
				registroControl.respuesta.addDefCampo("registroPortal", registroPortal);
				registroControl.respuesta.addDefCampo("ListaPerfiles", catalogoSL.getRegistros("PublicacionesPerfil", parametros));
				registroControl.respuesta.addDefCampo("ListaNiveles", catalogoSL.getRegistros("PublicacionesNivelAcceso", parametros));
				registroControl.sPagina = "/Aplicaciones/Publicaciones/fPubliUsuarioAlt.jsp";

			}else{
				registroControl.respuesta.addDefCampo("registroPortal", registroPortal);
				registroControl.respuesta.addDefCampo("ListaPerfiles", catalogoSL.getRegistros("PublicacionesPerfil", parametros));
				registroControl.respuesta.addDefCampo("ListaNiveles", catalogoSL.getRegistros("PublicacionesNivelAcceso", parametros));
				registroControl.sPagina = "/Aplicaciones/Publicaciones/fPubliUsuarioCon.jsp";
			} 
		}
		return registroControl;
	}

	/**
	 * Valida los párametros entrada y ejecuta los servicios de alta, baja o cambio.
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
		registro.addDefCampo("CVE_PORTAL",request.getParameter("CvePortal"));
		registro.addDefCampo("CVE_USUARIO",request.getParameter("CveUsuario"));
		registro.addDefCampo("CVE_PERFIL_PUB",request.getParameter("CvePerfilPub"));
		registro.addDefCampo("ID_NIVEL_ACCESO",request.getParameter("IdNivelAcceso"));
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=PublicacionesUsuario&OperacionCatalogo=IN&Filtro=Todos";
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("PublicacionesUsuario", registro, iTipoOperacion);
		return registroControl;
	}
}
