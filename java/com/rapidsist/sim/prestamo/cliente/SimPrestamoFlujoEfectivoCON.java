/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.cliente;

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
import java.util.Enumeration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletConfig;

import java.io.*;
import com.jspsmart.upload.*;
import com.rapidsist.comun.util.Fecha2;
import javax.servlet.GenericServlet;

/**
 * Esta clase se encarga de administrar los servicios de detalle operación (alta, baja,
 * modificación y consulta) de las garantías del préstamo. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimPrestamoFlujoEfectivoCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
	 * @throws RemoteException Si se generá un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se generá un error dentro de la clase CON.
	 */
	public RegistroControl consulta(Registro parametros, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		//OBTIENE SOLO EL REGISTRO SOLICITADO
		if (iTipoOperacion == CON_CONSULTA_TABLA){
			registroControl.respuesta.addDefCampo("ListaVerificacionPrestamo", catalogoSL.getRegistros("SimCatalogoEstatusPrestamo", parametros));
			
			//VERIFICA SI SE ENVIO EL PARAMETRO NOMBRE
			if (request.getParameter("IdPrestamo") != null && !request.getParameter("IdPrestamo").equals("")){
				parametros.addDefCampo("ID_PRESTAMO", request.getParameter("IdPrestamo"));
			}
			if (request.getParameter("IdProducto") != null && !request.getParameter("IdProducto").equals("")){
				parametros.addDefCampo("ID_PRODUCTO", request.getParameter("IdProducto"));
			}
			if (request.getParameter("NumCiclo") != null && !request.getParameter("NumCiclo").equals("")){
				parametros.addDefCampo("NUM_CICLO", request.getParameter("NumCiclo"));
			}
			if (request.getParameter("FechaInicioSolicitud") != null && !request.getParameter("FechaInicioSolicitud").equals("")){
				parametros.addDefCampo("FECHA_INICIO_SOLICITUD", request.getParameter("FechaInicioSolicitud"));
			}
			if (request.getParameter("FechaFinEntrega") != null && !request.getParameter("FechaFinEntrega").equals("")){
				parametros.addDefCampo("FECHA_FIN_ENTREGA", request.getParameter("FechaFinEntrega"));
			}
			if (request.getParameter("NomGrupo") != null && !request.getParameter("NomGrupo").equals("")){
				parametros.addDefCampo("NOM_GRUPO", request.getParameter("NomGrupo"));
			}
			if (request.getParameter("NomCompleto") != null && !request.getParameter("NomCompleto").equals("")){
				parametros.addDefCampo("NOM_COMPLETO", request.getParameter("NomCompleto"));
			}			
			if (request.getParameter("IdEstatusPrestamo") != null && !request.getParameter("IdEstatusPrestamo").equals("null")){
				parametros.addDefCampo("ID_ETAPA_PRESTAMO", request.getParameter("IdEstatusPrestamo"));
			}
			
			parametros.addDefCampo("CONSULTA", "PRESTAMOS");
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimPrestamoFlujoEfectivo", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreFluEfe.jsp";
		}else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Inicio")){
				registroControl.respuesta.addDefCampo("ListaVerificacionPrestamo", catalogoSL.getRegistros("SimCatalogoEstatusPrestamo", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreFluEfe.jsp";
			}else if (request.getParameter("Filtro").equals("Alta")){
				parametros.addDefCampo("CONSULTA", "ARCHIVOS");
				parametros.addDefCampo("ID_PRESTAMO", request.getParameter("IdPrestamo"));
				registroControl.respuesta.addDefCampo("ListaArchivo", catalogoSL.getRegistros("SimPrestamoFlujoEfectivo", parametros));
				registroControl.respuesta.addDefCampo("ListaVerificacionPrestamo", catalogoSL.getRegistros("SimCatalogoEstatusPrestamo", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreFluEfeReg.jsp";
			}
			
		}
		return registroControl;
	}

	/**
	 * Valida los párametros entrada y ejecuta los servicios de alta, baja o cambio.
	 * @param registro Parametros que se recogen de la sesion del usuario y se le envian a la clase CON.
	 * Estos párametros son: CVE_GPO_EMPRESA (Clave del grupo empresa), CVE_USUARIO_BITACORA (clave
	 * del usuario que realiza la operacion), RegistroOriginal (registro leido originalmente y
	 * se utiliza cuando se ejecuta la Operación de modificacion y se verifica que no se hallan
	 * realizado modificaciones al registro).
	 * @param request Objeto que provee de información al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envía como un párametro a este método.
	 * @param response Objeto que provee de información del servlet sobre el response del cliente. El
	 * contenedor de servlets crea un objeto HttpServletResponse y lo envía como un párametro a este método.
	 * @param config Objeto que provee de información del servlet sobre el ServletConfig del cliente. El
	 * contenedor de servlets crea un objeto ServletConfig y lo envía como un párametro a este método.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON
	 * @param contexto Objeto que contiene información acerca del entorno del servidor de
	 * aplicaciones.
	 * @param iTipoOperacion Operación que debe ejecutar la clase CON. Las operaciones se encuentran
	 * especificadas en la clase {@link com.rapidsist.portal.cliente.CatalogoControl CatalogoControl}
	 * @return Respuesta del servicio de alta, baja o cambio y la página a donde
	 * se redirecciona el control.
	 * @throws RemoteException Si se generá un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se generá un error dentro de la clase CON.
	 */
	public RegistroControl actualiza(Registro registro, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		
		registro.addDefCampo("ID_PRESTAMO", request.getParameter("IdPrestamo"));
		String sIdPrestamo = request.getParameter("IdPrestamo");
		
		int iArchivo = 0;
		int iFoto = 0;
		
		String sIdArchivoFlujo = "";
		int iResultadoSave = 0; //VARIABLE CON EL VALOR RESULTANTE DE GUARDAR EL ARCHIVO
		//String sUrlFotoAnterior = "";
		
		//INSTANCIA LA CLASE SE REALIZA EL UPLOAD
		SmartUpload mySmartUpload = new SmartUpload();
		
		// INICIALIZA EL UPLOAD
		mySmartUpload.initialize(config, request, response);
		
		// REALIZA LA CARGA DEL O DE LOS ARCHIVOS
		mySmartUpload.upload();

		//SALVA EL ARCHIVO CON EL NOMBRE ORIGINAL DENTRO DEL SERVIDOR
		com.jspsmart.upload.Files archivos = mySmartUpload.getFiles();
		
		//OBTIENE LOS VALORES DE LOS PARAMETROS
		java.util.Enumeration e = mySmartUpload.getRequest().getParameterNames();
		
		//OBTIENE EL DIRECTORIO RAIZ
		String sRaiz = getRoot(System.getProperty("user.dir"));
 		String sBaseDir = "ArchivosFE";
 		
		String sRealBaseDir = sRaiz + sBaseDir;
		java.io.File baseFile = new java.io.File(sRealBaseDir);
		if(!baseFile.exists()){
			baseFile.mkdir();
		}
		
		sRealBaseDir = sRaiz + sBaseDir + System.getProperty("file.separator") + sIdPrestamo;
 		java.io.File baseFile2 = new java.io.File(sRealBaseDir);
		if(!baseFile2.exists()){
			baseFile2.mkdir();
		}
		
		// RUTA DONDE ALMACENA EL ARCHIVO
		String sRutaCompleta = sRealBaseDir;
		iResultadoSave = mySmartUpload.save(sRutaCompleta);
		
		//OBTIENE LOS PARAMETROS DE LA PAGINA
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			String value = mySmartUpload.getRequest().getParameter(name);
			System.out.println("name"+name);
			System.out.println("value"+value);
			if (name.startsWith("UrlAnterior")){
				
				sIdArchivoFlujo = name.substring(11, name.length());
				registro.addDefCampo("ID_ARCHIVO_FLUJO", sIdArchivoFlujo);
				System.out.println("cual archvio esta seleccionando"+sIdArchivoFlujo);
				//SE INICIALIZA LA VARIABLE DEL NOMBRE DEL ARCHIVO QUE CONTIENE LA FOTO
				String sFotoArchivoFlujo = null;
				
				//SE OBTIENE EL PRIMER ARCHIVO
				System.out.println("archivo no."+iArchivo);
				com.jspsmart.upload.File archivo_flujo = archivos.getFile(iArchivo);
				iArchivo++;
				
				//SE OBTIENE EL NOMBRE DEL PRIMER ARCHIVO QUE SE ENVIA EN EL FORMULARIO
				sFotoArchivoFlujo = archivo_flujo.getFileName();
				
				if (sFotoArchivoFlujo.equals("")){
					System.out.println("no inserta");
					registro.addDefCampo("URL_ARCHIVO", value);
				}else{
					registro.addDefCampo("URL_ARCHIVO", sFotoArchivoFlujo);
					System.out.println("selecciono la foto"+sFotoArchivoFlujo);
				}
				//DA DE ALTA O BAJA LAS FUNCIONES EN LA BASE DE DATOS
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoFlujoEfectivo", registro, 1);
			}
			//INDICA A DONDE IRA AL TERMINAR LA ACTUALIZACION
			
		}
	
		//INDICA A DONDE IRA AL TERMINAR LA ACTUALIZACION
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimPrestamo&OperacionCatalogo=CR&IdPrestamo="+request.getParameter("IdPrestamo")+"&Alta=No";
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
