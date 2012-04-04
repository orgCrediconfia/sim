/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.grupo.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControl;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import javax.naming.Context;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;
import java.util.Enumeration;

/**
 * Esta clase se encarga de administrar los servicios de operación (alta, baja,
 * modificación y consulta) de los integrantes del grupo. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimGrupoIntegranteCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
		
		//VERIFICA SI BUSCA TODOS LOS REGISTROS
		if (iTipoOperacion == CON_CONSULTA_TABLA){
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			if (request.getParameter("Filtro").equals("Todos")){
				
				if (request.getParameter("IdPersona") != "" && !request.getParameter("IdPersona").equals("")){
					parametros.addDefCampo("ID_PERSONA", request.getParameter("IdPersona"));	
				}
			
				if (request.getParameter("NomCompleto") != "" && !request.getParameter("NomCompleto").equals("")){	
					parametros.addDefCampo("NOM_COMPLETO", request.getParameter("NomCompleto"));
				}
				parametros.addDefCampo("ID_SUCURSAL", request.getParameter("IdSucursal"));
				parametros.addDefCampo("ID_GRUPO", request.getParameter("IdGrupo"));
				parametros.addDefCampo("CONSULTA","PARA_ALTA");
				registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimGrupoIntegrante", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Grupo/fSimGpoInt.jsp?IdGrupo="+request.getParameter("IdGrupo");			
			}
		}
		
		if (iTipoOperacion == CON_INICIALIZACION){
			registroControl.sPagina = "/Aplicaciones/Sim/Grupo/fSimGpoInt.jsp?Asesor="+request.getParameter("Asesor")+"&IdSucursal="+request.getParameter("IdSucursal");		
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
		
		String sComentarioExcepcion = "NO";
		
		if (iTipoOperacion == 1){
			
			String sVariable1 = "";
			int iVariable2 = 0;
			String sVariable3 = "";
			int iVariable4 = 0;
			String sVariable5 = "";
			String sAsesor = request.getParameter("Asesor");
			int iRegistros = 0;
			int iAsesores = 0;
			String CveAsesor = "";
			registro.addDefCampo("ID_GRUPO", request.getParameter("IdGrupo"));
			
			Enumeration lista = request.getParameterNames();
			//RECORRE LA LISTA DE PARAMETROS QUE VIENEN EN EL REQUEST
			while (lista.hasMoreElements()){
				String sNombre = (String)lista.nextElement();
				//VERIFICA SI EL PARAMETRO TIENE EL PREFIJO "FuncionAlta"
				if (sNombre.startsWith("FuncionAlta")){
					iRegistros++;
				}
			}
			
			if (sAsesor.equals("ninguno")){
				Enumeration lista1 = request.getParameterNames();
				while (lista1.hasMoreElements()){
					String sNombre = (String)lista1.nextElement();
					//VERIFICA SI EL PARAMETRO TIENE EL PREFIJO "FuncionAlta"
					if (sNombre.startsWith("FuncionAlta")){
						sVariable1 = sNombre.substring(11, sNombre.length());
						
						iVariable2 = sVariable1.indexOf("-");
						iVariable4 = sVariable1.length();
					
						sVariable3 = sVariable1.substring(0,iVariable2);
						
						sVariable5 = sVariable1.substring(iVariable2+1,iVariable4);
						
						if (iAsesores == 0){
							CveAsesor = sVariable5;
							iAsesores++;
						}
						
						if (iAsesores != 0){
							if (sVariable5.equals(CveAsesor)){
								iAsesores++;
							}
						}
					}
				}
				
				if (iAsesores == iRegistros + 1){
					Enumeration lista2 = request.getParameterNames();
					while (lista2.hasMoreElements()){
						String sNombre = (String)lista2.nextElement();
						//VERIFICA SI EL PARAMETRO TIENE EL PREFIJO "FuncionAlta"
						if (sNombre.startsWith("FuncionAlta")){
							sVariable1 = sNombre.substring(11, sNombre.length());
							iVariable2 = sVariable1.indexOf("-");
							iVariable4 = sVariable1.length();
							sVariable3 = sVariable1.substring(0,iVariable2);
							sVariable5 = sVariable1.substring(iVariable2+1,iVariable4);
							registro.addDefCampo("ID_INTEGRANTE", sVariable3);
							registro.addDefCampo("EXCEPCION", "NO");
							registroControl.resultadoCatalogo = catalogoSL.modificacion("SimGrupoIntegrante", registro, iTipoOperacion);
						}
					}
				}else {
					sComentarioExcepcion = "Todos los integrantes deben de tener el mismo asesor";
					registro.addDefCampo("EXCEPCION", "SI");
					registroControl.resultadoCatalogo = catalogoSL.modificacion("SimGrupoIntegrante", registro, iTipoOperacion);
				}
			}else {
				Enumeration lista1 = request.getParameterNames();
				while (lista1.hasMoreElements()){
					String sNombre = (String)lista1.nextElement();
					//VERIFICA SI EL PARAMETRO TIENE EL PREFIJO "FuncionAlta"
					if (sNombre.startsWith("FuncionAlta")){
						sVariable1 = sNombre.substring(11, sNombre.length());
						
						iVariable2 = sVariable1.indexOf("-");
						iVariable4 = sVariable1.length();
					
						sVariable3 = sVariable1.substring(0,iVariable2);
						
						sVariable5 = sVariable1.substring(iVariable2+1,iVariable4);
						
						if (sVariable5.equals(sAsesor)){
						iAsesores++;
						
						}
					}
				}
				
				if (iAsesores == iRegistros){
					Enumeration lista2 = request.getParameterNames();
					while (lista2.hasMoreElements()){
						String sNombre = (String)lista2.nextElement();
						//VERIFICA SI EL PARAMETRO TIENE EL PREFIJO "FuncionAlta"
						if (sNombre.startsWith("FuncionAlta")){
							sVariable1 = sNombre.substring(11, sNombre.length());
							iVariable2 = sVariable1.indexOf("-");
							iVariable4 = sVariable1.length();
							sVariable3 = sVariable1.substring(0,iVariable2);
							sVariable5 = sVariable1.substring(iVariable2+1,iVariable4);
							registro.addDefCampo("ID_INTEGRANTE", sVariable3);
							registro.addDefCampo("EXCEPCION", "NO");
							registroControl.resultadoCatalogo = catalogoSL.modificacion("SimGrupoIntegrante", registro, iTipoOperacion);
						}
					}
				}else {
					sComentarioExcepcion = "Todos los integrantes deben de tener el mismo asesor";
					registro.addDefCampo("EXCEPCION", "SI");
					registroControl.resultadoCatalogo = catalogoSL.modificacion("SimGrupoIntegrante", registro, iTipoOperacion);
				}
			}
			
			
		}else if (iTipoOperacion == 2){ 
			String sIdIntegrante;
			Enumeration lista = request.getParameterNames();
			//RECORRE LA LISTA DE PARAMETROS QUE VIENEN EN EL REQUEST
			while (lista.hasMoreElements()){
				String sNombre = (String)lista.nextElement();
				//VERIFICA SI EL PARAMETRO TIENE EL PREFIJO "FuncionAlta"
				if (sNombre.startsWith("FuncionAlta")){
					//VERIFICA SI LA LISTA DE FUNCIONES ESTA INICIALIZADA
					
					sIdIntegrante = sNombre.substring(11, sNombre.length());
					registro.addDefCampo("ID_INTEGRANTE", sIdIntegrante);
					registro.addDefCampo("ID_GRUPO", request.getParameter("IdGrupo"));
					//DA DE ALTA O BAJA LAS FUNCIONES EN LA BASE DE DATOS
					registroControl.resultadoCatalogo = catalogoSL.modificacion("SimGrupoIntegrante", registro, iTipoOperacion);
				}
			}
		}
		//RECUPERA LA LISTA DE LAS FUNCIONES QUE SE VAN A PROCESAR
		/*
		registro.addDefCampo("ID_GRUPO", request.getParameter("IdGrupo"));
		String sAsesor = request.getParameter("IdGrupo");
		
		if (sAsesor.equals("ninguno")){
			System.out.println("Tiene que cuidar que sean tengan iguales todos los asesores que eligen");
		}else {
			int iPunto = sTotalPaginas.indexOf(".");
			sTotalPaginas = sTotalPaginas.substring(0,iPunto);
			System.out.println("los que eligen debe tener asesores igual a sAsesor");
		}
		/*
		
		*/
		
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimGrupo&OperacionCatalogo=CR&ComentarioExcepcion="+sComentarioExcepcion+"&IdGrupo="+request.getParameter("IdGrupo");
		return registroControl;
	}
}
