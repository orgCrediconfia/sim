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
import java.io.*;
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
public class PublPublicacionCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			registroControl.respuesta.addDefCampo("ListaSeccion",  catalogoSL.getRegistros("PublicacionesSeccion", parametros));
			registroControl.respuesta.addDefCampo("ListaNivel", catalogoSL.getRegistros("PublicacionesNivelAcceso", parametros));
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("PublicacionesSolicitud", parametros));
			registroControl.sPagina = "/Aplicaciones/Publicaciones/fPublSolicitudCon.jsp";

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
				parametros.addDefCampo("ID_PRIORIDAD",request.getParameter("IdPrioridad"));
				registroControl.respuesta.addDefCampo("ListaSeccion", catalogoSL.getRegistros("PublicacionesSeccion", parametros));
				registroControl.respuesta.addDefCampo("ListaNivel", catalogoSL.getRegistros("PublicacionesNivelAcceso", parametros));
				registroControl.respuesta.addDefCampo("ListaPrioridad", catalogoSL.getRegistros("PublicacionesPrioridad", parametros));
				Registro registroConsulta = null;
				registroConsulta = catalogoSL.getRegistro("PublicacionesSolicitud", parametros);
				//CUANDO EL VALOR DE LA URL DE LA IMAGEN ES NULO SE AGREGA LA LEYENDA "No incluir imagen"
				registroConsulta.addDefCampo("URL_IMAGEN", registroConsulta.getDefCampo("URL_IMAGEN")!=null ? registroConsulta.getDefCampo("URL_IMAGEN") : "ninguna");
				registroControl.respuesta.addDefCampo("registro", registroConsulta);
				registroControl.sPagina = "/Aplicaciones/Publicaciones/fPublSolicitudReg.jsp";
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
				registroinicio.addDefCampo("ID_PRIORIDAD",request.getParameter("IdPrioridad"));
				registroControl.respuesta.addDefCampo("registroinicio", registroinicio);
				registroControl.respuesta.addDefCampo("ListaSeccion", catalogoSL.getRegistros("PublicacionesSeccion", parametros));
				registroControl.respuesta.addDefCampo("ListaNivel", catalogoSL.getRegistros("PublicacionesNivelAcceso", parametros));
				registroControl.respuesta.addDefCampo("ListaPrioridad", catalogoSL.getRegistros("PublicacionesPrioridad", parametros));
				registroControl.sPagina = "/Aplicaciones/Publicaciones/fPublSolicitudReg.jsp";

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
						
						registroControl.sPagina = "/Aplicaciones/Publicaciones/fPublSolicitudCon.jsp";	
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
		RegistroControl registroControlRutaDestino = new RegistroControl();
		//RECUPERA LA SESION DEL USUARIO
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("Usuario");
		//AGREGA LA CLAVE DEL PORTAL Y DEL USUARIO DE LA SESION DEL USUARIO
		registro.addDefCampo("CVE_PORTAL", usuario.sCvePortal);
		registro.addDefCampo("CVE_USUARIO", usuario.sCveUsuario);
		//ESTOS OBJETOS SON NECESARIOS PARA INICIALIZAR EL OBJETO QUE PERMITE GUARDAR LOS ARCHIVOS
		//SE CREA UN FORMATO DE FECHA QUE ACEPTA LA BASE DE DATOS
		Date dFecha = new Date();
		int iResultadoSave = 0; //VARIABLE CON EL VALOR RESULTANTE DE GUARDAR EL ARCHIVO
		//SE INICIALIZA parametros PARA ALMACENAR LOS DATOS QUE SE UTILIZA EL SERVLET
		registro.addDefCampo("CVE_USUARIO", (String)registro.getDefCampo("CVE_USUARIO_BITACORA"));
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

		String sUrlPublicacionAnterior = "";
		String sUrlImagenAnterior = "";
		String sUrlImagenOriginal = "";

		// OBTIENE LOS PARAMETROS DE LA PAGINA
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			String value = mySmartUpload.getRequest().getParameter(name);
			//ASIGNA EL VALOR AL REGISTRO QUE SE VA A DAR DE ALTA EN LA
			//BASE DE DATOS
			if (name.equals("IdPublicacion")) {
				registro.addDefCampo("ID_PUBLICACION", value);
			}
			if (name.equals("CveSeccion")) {
				registro.addDefCampo("CVE_SECCION", value);
				registro.addDefCampo("SECCION_ANTERIOR", value);
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
			if (name.equals("IdNivelAcceso")) {
				registro.addDefCampo("ID_NIVEL_ACCESO", value);
			}
			if (name.equals("OperacionCatalogo")) {
				registro.addDefCampo("OPERACION_CATALOGO", value);
			}
			if (name.equals("UrlPublicacionAnterior")) {
				sUrlPublicacionAnterior = value;
			}
			if (name.equals("UrlImagenAnterior")) {
				sUrlImagenAnterior = value;
			}
			if (name.equals("UrlImagenOriginal")) {
				sUrlImagenOriginal = value;
			}
		}
		
		//-SE DETERMINA EL NOMBRE DE LA IMAGEN
		//-SI EL NOMBRE DE LA IMAGEN ES IGUAL A ALGUNA DE LAS PREDIFINIDAS NO SE GUARDA EL CONTENIDO
		//DEL SEGUNDO ARCHIVO
		//-SE INICIALIZA LA VARIABLE DEL NOMBRE DE LA IMAGEN
		String sNombreArchivoImagen = null;
		int iTamanoArchivoImagen = 0;
		if (sUrlImagenAnterior.equals("Pdf.gif") || sUrlImagenAnterior.equals("Txt.gif") || sUrlImagenAnterior.equals("Html.gif") || sUrlImagenAnterior.equals("Ninguna")){
			registro.addDefCampo("URL_IMAGEN", sUrlImagenAnterior);
		} else {
			//SI sUrlImagenAnterior ES ninguna O BLANCO
			if ( sUrlImagenAnterior.equals("ninguna") || sUrlImagenAnterior.equals("") ){
				registro.addDefCampo("URL_IMAGEN", "");
			} else {
				if (sUrlImagenAnterior.equals("Otra")){
					if (sUrlImagenAnterior.equals("")){
						registro.addDefCampo("URL_IMAGEN", "");
					} else {
						//SI EL NOMRE DE LA IMAGEN NO CORRESPONDE A Pdf.gif, Txt.gif, Html.gif o blanco
						//OBTIENE EL ARCHIVO NUMERO 1 (EL DE LA IMAGEN)
						com.jspsmart.upload.File archivo_imagen = archivos.getFile(1);
						iTamanoArchivoImagen = archivo_imagen.getSize();
						//SE OBTIENE EL NOMBRE DEL ARCHIVO DE LA IMAGEN
						sNombreArchivoImagen = archivo_imagen.getFileName();
						registro.addDefCampo("URL_IMAGEN", sNombreArchivoImagen);
					}
				} else{
					registro.addDefCampo("URL_IMAGEN", sUrlImagenOriginal);
				}
			}
		}

		//SE DETERMINA LA RUTA DONDE SE ALMACENARA EL ARCHIVO
		//ESTA CLASE DETERMINA LA RUTA SOLO CON LOS PARAMETROS CVE_GPO_EMPRESA, CVE_PORTAL, CVE_SECCION
		Context context = new InitialContext();
		Object referencia = contexto.lookup("java:comp/env/ejb/PublicacionSL");
		PublicacionSLHome publicacionHome = (PublicacionSLHome)PortableRemoteObject.narrow(referencia, PublicacionSLHome.class);
		PublicacionSL publicacion = publicacionHome.create();
		String sRutaCompletaOrigen = "";
		String sRutaCompletaDestino = "";

		//SE INICIALIZA LA VARIABLE DEL NOMBRE DEL ARCHIVO
		String sNombreArchivo = null;
		//SE OBTIENE EL NOMBRE DEL CONTROL FILE
		com.jspsmart.upload.File archivo = archivos.getFile(0);
		
		//SE OBTIENE EL NOMBRE DEL PRIMER ARCHIVO QUE SE ENVIA EN EL FORMULARIO
		sNombreArchivo = archivo.getFileName();
		registro.addDefCampo("URL_PUBLICACION", sNombreArchivo);
		registro.addDefCampo("REEMPLAZAR_ARCHIVO", "SI");
		//SI EL NOMBRE DEL ARCHIVO DEL CONTROL FILE VIENE BLANCO
		//ENTONCES SE TOMA EL NOMBRE DEL ARCHIVO DE LA VARIABLE OCULTA
		if (sNombreArchivo.equals("")){
			sNombreArchivo = sUrlPublicacionAnterior;
			registro.addDefCampo("URL_PUBLICACION",sNombreArchivo);
			registro.addDefCampo("REEMPLAZAR_ARCHIVO", "NO");
		}
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=PublicacionesSolicitud&OperacionCatalogo=IN&Filtro=Todos";
		//SI EL NOMBRE DEL ARCHIVO ESTA EN BLANCO SE GENERA UN MENSAJE
		//DE ERROR PARA QUE INDIQUE QUE DEBE SELECCIONAR UN ARCHIVO
		if (sNombreArchivo.equals("")){
			//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
			com.rapidsist.portal.catalogos.ResultadoCatalogo resultadoCatalogoControlado = new com.rapidsist.portal.catalogos.ResultadoCatalogo();
			resultadoCatalogoControlado.mensaje.setClave("PUBLICACION_NO_ENVIA_ARCHIVO");
			resultadoCatalogoControlado.mensaje.setTipo("Error");
			resultadoCatalogoControlado.mensaje.setDescripcion("Por favor seleccione el archivo que desea publicar ...");
			registroControl.resultadoCatalogo = resultadoCatalogoControlado;
		} else {
						
			//ES BAJA
			if(registro.getDefCampo("OPERACION_CATALOGO").equals("BA") ){				
				registroControl.resultadoCatalogo = catalogoSL.modificacion("PublicacionesSolicitud", registro, iTipoOperacion);
				sRutaCompletaOrigen = registroControl.resultadoCatalogo.mensaje.getDescripcion();
				String sArchivo = sRutaCompletaOrigen+(String)registro.getDefCampo("URL_PUBLICACION");
				String sRaiz = getRoot(System.getProperty("user.dir"));
				String sPath = sRaiz + sArchivo;
				String sRutaImagen = sRutaCompletaOrigen + (String)registro.getDefCampo("URL_IMAGEN");
				String sPathImagen = sRaiz + sRutaImagen;
				
				//BORRAMOS ARCHIVO
				BorraPublicacion borraPublicacion = new BorraPublicacion();
				borraPublicacion.BorraArchivo(sPath);
				borraPublicacion.BorraArchivo(sPathImagen);
			
				boolean bExito = publicacion.BorraObjetoJNDI(usuario.sCveGpoEmpresa, usuario.sCvePortal, usuario.sCveUsuario);
			}
			else{
				//VALIDA QUE EL TAMAÑO DEL ARCHIVO IMAGEN NO SEA MAYOR A 10 KB
				if (iTamanoArchivoImagen > 11000){
					com.rapidsist.portal.catalogos.ResultadoCatalogo resultadoCatalogoControlado = new com.rapidsist.portal.catalogos.ResultadoCatalogo();
					resultadoCatalogoControlado.mensaje.setClave("PUBLICACION_IMAGEN_TAMANO");
					resultadoCatalogoControlado.mensaje.setTipo("Error");
					resultadoCatalogoControlado.mensaje.setDescripcion("El tamaño del archivo imagen debe ser menor a 10 KB");
					registroControl.resultadoCatalogo = resultadoCatalogoControlado;
				}
				else{
					if (registro.getDefCampo("OPERACION_CATALOGO").equals("AL")){
			
					//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
						registroControl.resultadoCatalogo = catalogoSL.modificacion("PublicacionesSolicitud", registro, iTipoOperacion);
						sRutaCompletaOrigen = registroControl.resultadoCatalogo.mensaje.getDescripcion();
						String sRaiz = getRoot(System.getProperty("user.dir"));
						String sRuta = sRaiz + sRutaCompletaOrigen;
						iResultadoSave = mySmartUpload.save(sRuta);		
					}
					
					else {
						// LA OPERACION CATALOGO ES MODIFICACIÓN.
						registroControl.resultadoCatalogo = catalogoSL.modificacion("PublicacionesSolicitud", registro, iTipoOperacion);
					
						sRutaCompletaOrigen = registroControl.resultadoCatalogo.mensaje.getDescripcion();
						String sRaiz = getRoot(System.getProperty("user.dir"));
						String sPathOrigen = sRutaCompletaOrigen + (String)registro.getDefCampo("URL_PUBLICACION");
						String sPathOrigenImagen = sRutaCompletaOrigen + (String)registro.getDefCampo("URL_IMAGEN");
						String sRutaOrigen = sRaiz + sPathOrigen;
						String sRutaOrigenImagen = sRaiz + sPathOrigenImagen;
							
						//Directorio anterior SECCION_ANTERIOR
						java.io.File fileorigen = new java.io.File(sRutaOrigen);
						java.io.File fileorigenimagen = new java.io.File(sRutaOrigenImagen);
							
						//DESHABILITA EL COMPONENTE DE RE-READ
						registro.addDefCampo("OperacionInfraestructura", "DeshabilitaReRead");
					
						registroControlRutaDestino.resultadoCatalogo = catalogoSL.modificacion("PublicacionesSolicitudSeccion", registro, iTipoOperacion);
							
						sRutaCompletaDestino = registroControlRutaDestino.resultadoCatalogo.mensaje.getDescripcion();
						String sPathDestino = sRutaCompletaDestino;
						String sPathDestinoImagen = sRutaCompletaDestino;
						String sRutaDestino = sRaiz + sPathDestino;
						String sRutaDestinoImagen = sRaiz + sPathDestinoImagen;
						
						//Directorio destino
						java.io.File filedestino = new java.io.File(sRutaDestino);
						java.io.File filedestinoimagen = new java.io.File(sRutaDestinoImagen);
							
							
						//Moviendo los archivo a la nueva ruta
						boolean success = fileorigen.renameTo(new java.io.File(filedestino, fileorigen.getName()));
						boolean exito = fileorigenimagen.renameTo(new java.io.File(filedestinoimagen, fileorigenimagen.getName()));
					}

					//CUALQUIER MODIFICACIÓN A LAS SECCIONES RELACIONADAS A UN PERFIL EL OBJETO EN EL
					//JDNI SE BORRA Y SE RECONSTRUYE CUANDO EL USUARIO DESEA CONSULTAR ENTRA A PEQUES-CAMPEONES
					//LOS VALORES QUE SE UTILIZAN PARA BORRAR EL OBJETO EN EL JNDI SON:
					//-CVE_GPO_EMPRESA
					//-CVE_PORTAL
					//-CVE_USUARIO
					//ESTE METODO SE ENCARGA DE DETERMINAR EL PERFIL DE PUBLICACION DE LA PERSONA
					//QUE ESTA FIRMADA PARA BUSCAR EL OBJETO EN EL JNDI Y BORRARLO
					
					boolean bExito = publicacion.BorraObjetoJNDI(usuario.sCveGpoEmpresa, usuario.sCvePortal, usuario.sCveUsuario);
				}
			}
			//boolean bExito = publicacion.BorraObjetoJNDI(usuario.sCveGpoEmpresa, usuario.sCvePortal, usuario.sCveUsuario);
			
		}
		
		
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
