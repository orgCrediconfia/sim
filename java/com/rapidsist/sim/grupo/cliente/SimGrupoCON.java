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

/**
 * Esta clase se encarga de administrar los servicios de operación (alta, baja,
 * modificación y consulta) de grupos. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimGrupoCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			
			//VERIFICA SI SE ENVIO EL PARAMETRO ID DEL GRUPO
			if (request.getParameter("IdGrupo") != "" && !request.getParameter("IdGrupo").equals("")){	
				parametros.addDefCampo("ID_GRUPO", request.getParameter("IdGrupo"));
			}
			
			//VERIFICA SI SE ENVIO EL PARAMETRO NOMBRE DEL GRUPO
			if (request.getParameter("NomGrupo") != "" && !request.getParameter("NomGrupo").equals("")){	
				parametros.addDefCampo("NOM_GRUPO", request.getParameter("NomGrupo"));
			}
			
			//VERIFICA SI SE ENVIO EL PARAMETRO FECHA DE FORMACIÓN DEL GRUPO
			if (request.getParameter("FechaFormacion") != "" && !request.getParameter("FechaFormacion").equals("")){	
				parametros.addDefCampo("FECHA_FORMACION", request.getParameter("FechaFormacion"));
			}
			
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimGrupo", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Grupo/fSimGpoCon.jsp";
		}
		
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			
			String sCveAsesorCredito = "ninguno";
			String sNumAsesor = "";
			
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("ID_GRUPO",request.getParameter("IdGrupo"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimGrupo", parametros));
			registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimUsuarioSucursalAsignada", parametros));
			
			parametros.addDefCampo("EXISTE_ASESOR","PREGUNTA");	
			registroControl.respuesta.addDefCampo("registroAsesor", catalogoSL.getRegistro("SimGrupoIntegranteAsesor", parametros));
			Registro preguntaAsesor = (Registro)registroControl.respuesta.getDefCampo("registroAsesor");
			sNumAsesor = (String)preguntaAsesor.getDefCampo("NUM_ASESOR");
			
			if (!sNumAsesor.equals("0")){	
				parametros.addDefCampo("EXISTE_ASESOR","SI");
				registroControl.respuesta.addDefCampo("registroAsesor", catalogoSL.getRegistro("SimGrupoIntegranteAsesor", parametros));
				Registro registroAsesor = (Registro)registroControl.respuesta.getDefCampo("registroAsesor");
				sCveAsesorCredito = (String)registroAsesor.getDefCampo("CVE_ASESOR_CREDITO");
			}	
			
			parametros.addDefCampo("CONSULTA","ASIGNADOS");
			registroControl.respuesta.addDefCampo("ListaIntegrante", catalogoSL.getRegistros("SimGrupoIntegrante", parametros));
			parametros.addDefCampo("CONSULTA","BAJA");
			registroControl.respuesta.addDefCampo("ListaIntegranteBaja", catalogoSL.getRegistros("SimGrupoIntegrante", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Grupo/fSimGpoReg.jsp?Asesor="+sCveAsesorCredito;
		}
		
		if (iTipoOperacion == CON_INICIALIZACION){
				if (request.getParameter("Filtro").equals("Alta")){
					registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimUsuarioSucursalAsignada", parametros));
					registroControl.sPagina = "/Aplicaciones/Sim/Grupo/fSimGpoReg.jsp";
				}else if (request.getParameter("Filtro").equals("Inicio")){
					registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimUsuarioSucursalAsignada", parametros));
					registroControl.sPagina = "/Aplicaciones/Sim/Grupo/fSimGpoCon.jsp";
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
		
		String sIdGrupo = "";
		String sComentarioExcepcion = "NO";
		
		registro.addDefCampo("ID_GRUPO",request.getParameter("IdGrupo"));
		registro.addDefCampo("NOM_GRUPO",request.getParameter("NomGrupo"));
		registro.addDefCampo("FECHA_FORMACION",request.getParameter("FechaFormacion"));
		registro.addDefCampo("ID_SUCURSAL",request.getParameter("IdSucursal"));
		registro.addDefCampo("ID_COORDINADOR",request.getParameter("IdCoordinador"));
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS	
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimGrupo", registro, iTipoOperacion);
		
		if (iTipoOperacion == 1){
			sIdGrupo = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("ID_GRUPO");
			
		}
		if (iTipoOperacion == 3){
			sIdGrupo = request.getParameter("IdGrupo");
		}
		
		if (iTipoOperacion == 2){
			registroControl.sPagina = "/Aplicaciones/Sim/Grupo/fSimGpoCon.jsp";
		}else {
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimGrupo&OperacionCatalogo=CR&IdGrupo="+sIdGrupo+"&ComentarioExcepcion="+sComentarioExcepcion;
		}
		
		return registroControl;
	}
}
