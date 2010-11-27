	/**
 * Sistema de administración de portales.
 *
 * Copyright (0c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.publicaciones.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.publicaciones.datos.PublicacionSL;
import com.rapidsist.publicaciones.datos.PublicacionSLHome;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import com.rapidsist.portal.configuracion.Usuario;
import javax.naming.Context;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Enumeration;
import javax.rmi.PortableRemoteObject;

/**
 * Esta clase se encarga de administrar los servicios de operación (alta, baja,
 * modificación y consulta) del catálogo de Autorización de Publicaciones. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class PublAutorizacionCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
		parametros.addDefCampo("CVE_GPO_EMPRESA", usuario.sCveGpoEmpresa);
		parametros.addDefCampo("CVE_PORTAL", usuario.sCvePortal);
		parametros.addDefCampo("CVE_USUARIO", usuario.sCveUsuario);

		//VERIFICA SI BUSCA TODOS LOS REGISTROS
		if (iTipoOperacion == CON_CONSULTA_TABLA){

				//VERIFICA SI SE ENVIO EL PARAMETRO NOMBRE PUBLICACION
				if ( !request.getParameter("NomPublicacion").equals("")){
					parametros.addDefCampo("NOM_PUBLICACION", request.getParameter("NomPublicacion"));
					bParametrosFiltro = true;
				}
				//VERIFICA SI SE ENVIO EL PARAMETRO CVE_SECCION
				if ( !request.getParameter("CveSeccion").equals("null")){
					parametros.addDefCampo("CVE_SECCION", request.getParameter("CveSeccion"));
					bParametrosFiltro = true;
				}
				//VERIFICA SI SE ENVIO EL PARAMETRO ID_NIVEL_ACCESO
				if ( !request.getParameter("IdNivelAcceso").equals("null")){
					parametros.addDefCampo("ID_NIVEL_ACCESO", request.getParameter("IdNivelAcceso"));
					bParametrosFiltro = true;
				}
				parametros.eliminaCampo("Filtro");
				parametros.addDefCampo("Filtro", "Total");
				registroControl.respuesta.addDefCampo("ListaSeccion",  catalogoSL.getRegistros("PublicacionesSeccion", parametros));
				registroControl.respuesta.addDefCampo("ListaNivel", catalogoSL.getRegistros("PublicacionesNivelAcceso", parametros));
				registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("PublicacionesAutorizacion", parametros));
				registroControl.sPagina = "/Aplicaciones/Publicaciones/fPublAutorizacionCon.jsp";

			}

			else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
				//OBTIENE SOLO EL REGISTRO SOLICITADO
				parametros.eliminaCampo("Filtro");
				parametros.addDefCampo("Filtro", "Total");
				parametros.addDefCampo("ID_PUBLICACION",request.getParameter("IdPublicacion"));
				registroControl.respuesta.addDefCampo("ListaSeccion", catalogoSL.getRegistros("PublicacionesSeccion", parametros));
				registroControl.respuesta.addDefCampo("ListaNivel", catalogoSL.getRegistros("PublicacionesNivelAcceso", parametros));
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("PublicacionesSolicitud", parametros));
				registroControl.sPagina = "/Aplicaciones/Publicaciones/fPublAutorizacionReg.jsp";
			}
			else if (iTipoOperacion == CON_INICIALIZACION){

			if (request.getParameter("Filtro").equals("Alta")){
				parametros.eliminaCampo("Filtro");
				parametros.addDefCampo("Filtro", "Total");
				registroControl.respuesta.addDefCampo("ListaSeccion",  catalogoSL.getRegistros("PublicacionesSeccion", parametros));
				registroControl.respuesta.addDefCampo("ListaNivel", catalogoSL.getRegistros("PublicacionesNivelAcceso", parametros));
				registroControl.sPagina = "/Aplicaciones/Publicaciones/fPublAutorizacionReg.jsp";

			}else{
				parametros.eliminaCampo("Filtro");
				parametros.addDefCampo("Filtro", "Total");
				registroControl.respuesta.addDefCampo("ListaSeccion",  catalogoSL.getRegistros("PublicacionesSeccion", parametros));
				registroControl.respuesta.addDefCampo("ListaNivel", catalogoSL.getRegistros("PublicacionesNivelAcceso", parametros));
				//registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("PublicacionesSeccion", parametros));
				registroControl.sPagina = "/Aplicaciones/Publicaciones/fPublAutorizacionCon.jsp";
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
		//RECUPERA LA SESION DEL USUARIO
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("Usuario");
		//AGREGA LA CLAVE DEL PORTAL Y DEL USUARIO DE LA SESION DEL USUARIO
		registro.addDefCampo("CVE_GPO_EMPRESA", usuario.sCveGpoEmpresa);
		registro.addDefCampo("CVE_PORTAL", usuario.sCvePortal);
		registro.addDefCampo("CVE_USUARIO", usuario.sCveUsuario);

		//RECUPERA LA LISTA DE LOS USUARIOS QUE SE VAN A PROCESAR
		LinkedList listaPublicaciones = null;
		String sIdPublicacion;
		Enumeration lista = request.getParameterNames();

		//RECORRE LA LISTA DE PARAMETROS QUE VIENEN EN EL REQUEST
		while (lista.hasMoreElements()){
			String sNombre = (String)lista.nextElement();
			//VERIFICA SI EL PARAMETRO TIENE EL PREFIJO "ProductoAlta"
			if (sNombre.startsWith("Publicacion")){
				//VERIFICA SI LA LISTA DE FUNCIONES ESTA INICIALIZADA
				if (listaPublicaciones == null){
					listaPublicaciones = new LinkedList();
				}
				sIdPublicacion = sNombre.substring(11, sNombre.length());
				listaPublicaciones.add(sIdPublicacion);
			}
		}

		//VERIFICA SI SE SELECCIONARON FUNCIONES PARA PROCESAR
		if (listaPublicaciones != null){
			registro.addDefCampo("ListaPublicaciones", listaPublicaciones);
			//DA DE ALTA O BAJA LAS FUNCIONES EN LA BASE DE DATOS
			registroControl.resultadoCatalogo = catalogoSL.modificacion("PublicacionesAutorizacion", registro, iTipoOperacion);
		}
		else{
			ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
			registroControl.resultadoCatalogo = resultadoCatalogo;
		}

		//CUALQUIER MODIFICACIÓN A LAS SECCIONES RELACIONADAS A UN PERFIL EL OBJETO EN EL
		//JDNI SE BORRA Y SE RECONSTRUYE CUANDO EL USUARIO DESEA CONSULTAR ENTRA A PEQUES-CAMPEONES
		Object referencia = contexto.lookup("java:comp/env/ejb/PublicacionSL");			
		PublicacionSLHome publicacionHome = (PublicacionSLHome)PortableRemoteObject.narrow(referencia, PublicacionSLHome.class);			
		PublicacionSL publicacion = publicacionHome.create();
		
		//LOS VALORES QUE SE UTILIZAN PARA BORRAR EL OBJETO EN EL JNDI SON:
		//-CVE_GPO_EMPRESA
		//-CVE_PORTAL
		//-CVE_USUARIO
		//ESTE METODO SE ENCARGA DE DETERMINAR EL PERFIL DE PUBLICACION DE LA PERSONA
		//QUE ESTA FIRMADA PARA BUSCAR EL OBJETO EN EL JNDI Y BORRARLO
		boolean bExito = publicacion.BorraObjetoJNDI(usuario.sCveGpoEmpresa, usuario.sCvePortal, usuario.sCveUsuario);
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=PublicacionesAutorizacion&OperacionCatalogo=IN&Filtro=Todos";
		return registroControl;

	}
}
