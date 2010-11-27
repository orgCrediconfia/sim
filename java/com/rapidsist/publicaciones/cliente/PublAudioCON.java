/**
 * Sistema de administración de portales.
 *
 * Copyright (0c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.publicaciones.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.publicaciones.datos.PublicacionSL;
import com.rapidsist.publicaciones.datos.PublicacionSLHome;
import com.rapidsist.publicaciones.cliente.BorraPublicacion;
//LIBRERÍAS QUE SE ENCARGAN DE TRABAJAR CON
//EL ARCHIVO PARA PODER ALMACENARLO
import com.jspsmart.upload.File;
import com.jspsmart.upload.Files;
import com.jspsmart.upload.SmartUpload;
import java.util.Calendar;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.naming.*;
import java.rmi.RemoteException;
import java.util.Date;
import com.rapidsist.comun.util.Fecha2;
import javax.rmi.PortableRemoteObject;
import java.lang.Object;

/**
 * Esta clase se encarga de administrar los servicios de operación (alta, baja,
 * modificación y consulta) del catálogo de publicaciones. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class PublAudioCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN {

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
		//RECUPERA LA SESION DEL USUARIO
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("Usuario");
		//AGREGA LA CLAVE DEL PORTAL Y DEL USUARIO DE LA SESION DEL USUARIO
		parametros.addDefCampo("CVE_PORTAL", usuario.sCvePortal);
		parametros.addDefCampo("CVE_USUARIO", usuario.sCveUsuario);
		boolean bParametrosFiltro = false;
		//VERIFICA SI BUSCA TODOS LOS REGISTROS
		if (iTipoOperacion == CON_CONSULTA_TABLA){
			//VERIFICA SI SE ENVIO EL PARAMETRO NOMBRE PUBLICACION
				if ( !request.getParameter("NomPublicacion").equals("")){
					parametros.addDefCampo("NOM_PUBLICACION", request.getParameter("NomPublicacion"));
					bParametrosFiltro = true;
				}
				//VERIFICA SI SE ENVIO EL PARAMETRO CATEGORIA
				if ( !request.getParameter("FIniVigencia").equals("")){
					parametros.addDefCampo("F_INI_VIGENCIA", request.getParameter("FIniVigencia"));
					bParametrosFiltro = true;
				}
				//VERIFICA SI SE ENVIO EL PARAMETRO CATEGORIA
				if ( !request.getParameter("FFinVigencia").equals("")){
					parametros.addDefCampo("F_FIN_VIGENCIA", request.getParameter("FFinVigencia"));
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
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("PublicacionesAudio", parametros));
			registroControl.respuesta.addDefCampo("ListaSeccion", catalogoSL.getRegistros("PublicacionesSeccion", parametros));
			registroControl.respuesta.addDefCampo("ListaNivel", catalogoSL.getRegistros("PublicacionesNivelAcceso", parametros));
			registroControl.sPagina = "/Aplicaciones/Publicaciones/fPublAudioCon.jsp";
			

		}

		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.eliminaCampo("Filtro");
			parametros.addDefCampo("Filtro", "Total");
			parametros.addDefCampo("ID_PUBLICACION",request.getParameter("IdPublicacion"));

			// VERIFICA SI LA SECCIÓN DE LA PUBLICACION ESTA DADA DE ALTA EN EL PERFIL DE USUARIO
			Registro registroPublicacion = null;
			registroPublicacion = catalogoSL.getRegistro("PublicacionesPublicacionUsuario", parametros);
			if (registroPublicacion == null){
				// NO PUEDE VER LA PUBLICACIÓN
				registroControl.sPagina = "/Portales/Icmar/SistemaErrorPagina.jsp?Mensaje=Su perfil no tiene acceso a la sección de la publicación";
			}
			else {
				registroControl.respuesta.addDefCampo("ListaSeccion", catalogoSL.getRegistros("PublicacionesSeccion", parametros));
				registroControl.respuesta.addDefCampo("ListaNivel", catalogoSL.getRegistros("PublicacionesNivelAcceso", parametros));
				registroControl.respuesta.addDefCampo("ListaPrioridad", catalogoSL.getRegistros("PublicacionesPrioridad", parametros));
				Registro registroConsulta = null;
				registroConsulta = catalogoSL.getRegistro("PublicacionesAudio", parametros);
				//CUANDO EL VALOR DE LA URL DE LA IMAGEN ES NULO SE AGREGA LA LEYENDA "No incluir imagen"
				//registroConsulta.addDefCampo("URL_IMAGEN", registroConsulta.getDefCampo("URL_IMAGEN")!=null ? registroConsulta.getDefCampo("URL_IMAGEN") : "ninguna");
				registroControl.respuesta.addDefCampo("registro", registroConsulta);
				registroControl.sPagina = "/Aplicaciones/Publicaciones/fPublAudioReg.jsp";
			}
			
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Alta")){
				parametros.eliminaCampo("Filtro");
				parametros.addDefCampo("Filtro", "Total");
				String sTxComentarioDefault = usuario.sNomCompleto.trim() + ", " + Fecha2.nombreMes(Fecha2.getSysMes()) + " de " + String.valueOf(Fecha2.getSysAño());
				//SE AUMENTA UN MES A LA FECHA DEL CALENDARIO
				Date dFecha = new Date();
				Calendar cal;
				cal = Calendar.getInstance();
				cal.setTime(dFecha);
				cal.add(Calendar.MONTH, 1);

				String sMesSiguiente = String.valueOf( cal.get(Calendar.MONTH)+1 );
				//SI EL MES ES DE UN DIGITO SE ANTEPONE UN CERO POR EJEMPLO SI EL MES ES "1" SE
				//LE AGREGA 0 Y ENTONCES EL MES QUEDARÍA "01"
				if (sMesSiguiente.length()<=1){
					sMesSiguiente = "0"+sMesSiguiente;
				}
				String sAnioSiguiente = String.valueOf( cal.get(Calendar.YEAR) );
				String sFFinVigencia = String.valueOf(Fecha2.getSysDia()) +"/"+ sMesSiguiente + "/" + sAnioSiguiente;
				
				Registro registroinicio = new Registro();
				registroinicio.addDefCampo("TX_COMENTARIO", sTxComentarioDefault);
				registroinicio.addDefCampo("F_FIN_VIGENCIA", sFFinVigencia);
				//registroinicio.addDefCampo("NOM_PUBLICACION",request.getParameter("NomPublicacion"));
			
				registroControl.respuesta.addDefCampo("registroinicio", registroinicio);
				registroControl.respuesta.addDefCampo("ListaSeccion", catalogoSL.getRegistros("PublicacionesSeccion", parametros));
				registroControl.respuesta.addDefCampo("ListaNivel", catalogoSL.getRegistros("PublicacionesNivelAcceso", parametros));
				registroControl.respuesta.addDefCampo("ListaPrioridad", catalogoSL.getRegistros("PublicacionesPrioridad", parametros));
				registroControl.sPagina = "/Aplicaciones/Publicaciones/fPublAudioReg.jsp?OperacionCatalogo=AL";

			}else{
				// VERIFICA SI EL USUARIO ES UN USUARIO DE PUBLICACIÓN
				parametros.eliminaCampo("Filtro");
				parametros.addDefCampo("Filtro", "Total");
				Registro registroUsuario = null;
				registroUsuario = catalogoSL.getRegistro("PublicacionesUsuario", parametros);
				if (registroUsuario == null){
					// EL USUARIO NO ES USUARIO DE PUBLICACION
					registroControl.sPagina = "/Portales/Icmar/SistemaErrorPagina.jsp?Mensaje=El usuario no esta dado de alta como usuario de publicaciones y no tiene asignado un perfil de publicación";
				}
				else {
					// EL USUARIO ES USUARIO DE PUBLICACIÓN
					// VERIFICA SI EL USUARIO DE PUBLICACIÓN TIENE UN PERFIL DE PUBLICACIÓN
					String sCvePerfilPub = "";
					sCvePerfilPub = (String) registroUsuario.getDefCampo("CVE_PERFIL_PUB");
			
					if (sCvePerfilPub == null){
						//EL USUARIO DE PUBLICACIÓN NO TIENE UN PERFIL DE PUBLICACIÓN
						registroControl.sPagina = "/Portales/Icmar/SistemaErrorPagina.jsp?Mensaje=El usuario no tiene asignado un perfil de publicación";
					}
					else {
						//EL USUARIO DE PUBLICACIÓN TIENE UN PERFIL DE PUBLICACIÓN
						registroControl.respuesta.addDefCampo("ListaSeccion",  catalogoSL.getRegistros("PublicacionesSeccion", parametros));
						registroControl.respuesta.addDefCampo("ListaNivel", catalogoSL.getRegistros("PublicacionesNivelAcceso", parametros));
						
						registroControl.sPagina = "/Aplicaciones/Publicaciones/fPublAudioCon.jsp";
					}	
				}
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
		
		Object referencia = contexto.lookup("java:comp/env/ejb/PublicacionSL");
		PublicacionSLHome publicacionHome = (PublicacionSLHome)PortableRemoteObject.narrow(referencia, PublicacionSLHome.class);
		PublicacionSL publicacion = publicacionHome.create();
		
		//OBTIENE LOS DATOS DEL USUARIO
		HttpSession session = request.getSession(true);
		Usuario usuario = (Usuario)session.getAttribute("Usuario");
		registro.addDefCampo("CVE_PORTAL", usuario.sCvePortal);
		registro.addDefCampo("CVE_USUARIO", usuario.sCveUsuario);
		
		Date dFecha = new Date();
		//ESTE OBJETO ES NECESARIO PARA INICIALIZAR EL OBJETO QUE PERMITE GUARDAR LOS ARCHIVOS
		int iResultadoSave = 0; //VARIABLE CON EL VALOR RESULTANTE DE GUARDAR EL ARCHIVO
		//INSTANCIA LA CLASE SE REALIZA EL UPLOAD
		SmartUpload mySmartUpload = new SmartUpload();
		// INICIALIZA EL UPLOAD
		mySmartUpload.initialize(config, request, response);
		// REALIZA LA CARGA DEL O DE LOS ARCHIVOS
		mySmartUpload.upload();
		//SALVA EL ARCHIVO CON EL NOMBRE ORIGINAL DENTRO DEL SERVIDOR
		Files archivos = mySmartUpload.getFiles();
		
		// OBTIENE LOS VALORES DE LOS PARAMETROS
		java.util.Enumeration e = mySmartUpload.getRequest().getParameterNames();

		String sUrlControladorAnterior = "";
		String sUrlStreamOriginal = "";
		
		// OBTIENE LOS PARAMETROS DE LA PAGINA
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			String value = mySmartUpload.getRequest().getParameter(name);
			//ASIGNA EL VALOR AL REGISTRO QUE SE VA A DAR DE ALTA EN LA
			//BASE DE DATOS
			
			if (name.equals("CveSeccion")) {
				registro.addDefCampo("CVE_SECCION", value);
			}
			if (name.equals("IdPublicacion")) {
				registro.addDefCampo("ID_PUBLICACION", value);
			}
			if (name.equals("IdNivelAcceso")) {
				registro.addDefCampo("ID_NIVEL_ACCESO", value);
			}
			if (name.equals("NomPublicacion")) {
				registro.addDefCampo("NOM_PUBLICACION", value);
			}
			if (name.equals("TxComentario")) {
				registro.addDefCampo("TX_COMENTARIO", value);
			}
			if (name.equals("DescPublicacion")) {
				registro.addDefCampo("DESC_PUBLICACION", value);
			}
			if (name.equals("UrlControladorAnterior")) {
				sUrlControladorAnterior = value;
			}
			if (name.equals("UrlStreamOriginal")) {
				sUrlStreamOriginal = value;
			}
			if (name.equals("IdPrioridad")) {
				registro.addDefCampo("ID_PRIORIDAD", value);
			}
			if (name.equals("FIniVigencia")) {
				dFecha = Fecha2.toDate(value);
				registro.addDefCampo("F_INI_VIGENCIA", Fecha2.formatoBDStatic(dFecha));
			}
			if (name.equals("FFinVigencia")) {
				dFecha = Fecha2.toDate(value);
				registro.addDefCampo("F_FIN_VIGENCIA", Fecha2.formatoBDStatic(dFecha));
			}
			if (name.equals("OperacionCatalogo")) {
				registro.addDefCampo("OPERACION_CATALOGO", value);
			}
		}
				

		//OBTIENE EL DIRECTORIO RAIZ
		String sRaiz = getRoot(System.getProperty("user.dir"));
		String sOS = getRoot(System.getProperty("os.name"));
		String sRutaCompleta = "";
				
		// RUTA DONDE ALMACENA EL ARCHIVO				
		if (sOS.equals("Linux")){
			sRutaCompleta = sRaiz + "usr" + System.getProperty("file.separator") + "desarrollo" + System.getProperty("file.separator") + "cuentas" + System.getProperty("file.separator") + "rapidsist" + System.getProperty("file.separator") + "servidores" + System.getProperty("file.separator") + "jboss" + System.getProperty("file.separator") + "Portal" + System.getProperty("file.separator") + "deploy" + System.getProperty("file.separator") + "Portal" + System.getProperty("file.separator") + "portal.war" + System.getProperty("file.separator") + "Portales" + System.getProperty("file.separator") + "Icmar" + System.getProperty("file.separator") + "stream" + System.getProperty("file.separator") + "Audio";
		
		}else{
			//RUTA DONDE ALMACENA EL ARCHIVO
			sRutaCompleta = sRaiz + "Desarrollo" + System.getProperty("file.separator") + "Cuentas" + System.getProperty("file.separator") + "Rapidsist" + System.getProperty("file.separator") + "Servidores" + System.getProperty("file.separator") + "jboss" + System.getProperty("file.separator") + "Portal" + System.getProperty("file.separator") + "deploy" + System.getProperty("file.separator") + "Portal" + System.getProperty("file.separator") + "portal.war" + System.getProperty("file.separator") + "Portales" + System.getProperty("file.separator") + "Icmar" + System.getProperty("file.separator") + "stream" + System.getProperty("file.separator") + "Audio";	
		}
		
		//SE INICIALIZA LA VARIABLE DEL NOMBRE DEL ARCHIVO QUE CONTIENE EL CONTROLADOR
		String sNombreArchivo = null;
		
		//SE OBTIENE EL NOMBRE DEL CONTROL FILE
		File archivo = archivos.getFile(0);
		
		//SE OBTIENE EL NOMBRE DEL PRIMER ARCHIVO QUE SE ENVIA EN EL FORMULARIO
		sNombreArchivo = archivo.getFileName();
		
		//ALMACENA EL ARCHIVO		
		iResultadoSave = mySmartUpload.save(sRutaCompleta);
		
		//SE INICIALIZA LA VARIABLE DEL NOMBRE DEL ARCHIVO QUE CONTIENE EL STREAM
		String sNombreArchivoStream = null;
		
		//SE OBTIENE EL NOMBRE DEL CONTROL FILE
		File archivo_stream = archivos.getFile(1);
		
		//SE OBTIENE EL NOMBRE DEL ARCHIVO QUE CONTIEN EL STREAM
		sNombreArchivoStream = archivo_stream.getFileName();
		
		registro.addDefCampo("STREAM", sNombreArchivoStream);
		if (sNombreArchivoStream.equals("")){
			sNombreArchivoStream = sUrlStreamOriginal;
			registro.addDefCampo("STREAM",sUrlStreamOriginal);
		}
		
		registro.addDefCampo("CONTROLADOR",sNombreArchivo);
		registro.addDefCampo("URL_PUBLICACION",sNombreArchivo);
		if (sNombreArchivo.equals("")){
			sNombreArchivo = sUrlControladorAnterior;
			registro.addDefCampo("URL_PUBLICACION",sNombreArchivo);
			registro.addDefCampo("CONTROLADOR",sNombreArchivo);
		}
		
		//ES BAJA
		if(registro.getDefCampo("OPERACION_CATALOGO").equals("BA") ){				
			String sArchivoControlador = sRutaCompleta + System.getProperty("file.separator") + (String)registro.getDefCampo("CONTROLADOR");
			String sArchivoStream = sRutaCompleta + System.getProperty("file.separator") + (String)registro.getDefCampo("STREAM");
			
			//SE BORRAN LOS ARCHIVOS DE AUDIO
			BorraPublicacion borraPublicacion = new BorraPublicacion();
			borraPublicacion.BorraArchivo(sArchivoControlador);
			borraPublicacion.BorraArchivo(sArchivoStream);
		
			boolean bExito = publicacion.BorraObjetoJNDI(usuario.sCveGpoEmpresa, usuario.sCvePortal, usuario.sCveUsuario);
		}
		
		//DIRECCIONA A LA JSP
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=PublicacionesAudio&OperacionCatalogo=IN&Filtro=Todos";
		
		registroControl.resultadoCatalogo = catalogoSL.modificacion("PublicacionesAudio", registro, iTipoOperacion);

		return registroControl;
	}
	
	//OBTIENE EL DIRECTORIO RAIZ
	private String getRoot(String path){
		java.io.File roots[] = java.io.File.listRoots();
		for (int i = 0; i < roots.length; i++)
			if (path.startsWith(roots[i].getPath())) return roots[i].getPath();
		return path;
	}
}
