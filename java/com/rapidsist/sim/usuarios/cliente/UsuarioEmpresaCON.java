/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.usuarios.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import com.rapidsist.portal.configuracion.Usuario;
import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpSession;
import java.rmi.RemoteException;

/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificación y consulta) de los usuarios del sistema. Esta clase es llamada por
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

		//VERIFICA SI BUSCA TODOS LOS REGISTROS
		if (iTipoOperacion == CON_CONSULTA_TABLA){
			//VERIFICA SI SE ENVIO EL PARAMETRO NOMBRE
			if (request.getParameter("ClaveUsuario") != null && !request.getParameter("ClaveUsuario").equals("")){
				parametros.addDefCampo("CLAVE_USUARIO", request.getParameter("ClaveUsuario"));
			}
			if (request.getParameter("NomCompleto") != null && !request.getParameter("NomCompleto").equals("")){
				parametros.addDefCampo("NOM_COMPLETO", request.getParameter("NomCompleto"));
			}
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimUsuarioEmpresa", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Usuarios/fGralUsuEmpCon.jsp";
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Inicio")){
				registroControl.sPagina = "/Aplicaciones/Sim/Usuarios/fGralUsuEmpCon.jsp";
			}else if (request.getParameter("Filtro").equals("Alta")){
				registroControl.respuesta.addDefCampo("ListaPerfil", catalogoSL.getRegistros("SimCatalogoPerfil", parametros));
				registroControl.respuesta.addDefCampo("ListaPuesto", catalogoSL.getRegistros("SimCatalogoPuesto", parametros));
				registroControl.respuesta.addDefCampo("ListaSucursalPertenece", catalogoSL.getRegistros("SimCatalogoSucursal", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Usuarios/fGralUsuEmpReg.jsp";
			}
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			parametros.addDefCampo("CVE_USUARIO", request.getParameter("CveUsuario"));
			parametros.addDefCampo("ID_PERSONA", request.getParameter("IdPersona"));
			registroControl.respuesta.addDefCampo("ListaPerfil", catalogoSL.getRegistros("SimCatalogoPerfil", parametros));
			registroControl.respuesta.addDefCampo("ListaPuesto", catalogoSL.getRegistros("SimCatalogoPuesto", parametros));
			registroControl.respuesta.addDefCampo("ListaDireccion", catalogoSL.getRegistros("SimUsuarioDireccion", parametros));
			registroControl.respuesta.addDefCampo("ListaTelefono", catalogoSL.getRegistros("SimUsuarioTelefono", parametros));
			registroControl.respuesta.addDefCampo("ListaRegional", catalogoSL.getRegistros("SimUsuarioRegionalAsignada", parametros));
			registroControl.respuesta.addDefCampo("ListaSucursalPertenece", catalogoSL.getRegistros("SimCatalogoSucursal", parametros));
			registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimUsuarioSucursalAsignada", parametros));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimUsuarioEmpresa", parametros));
			//VERIFICA SI SOLO CONSULTA LOS DATOS DEL REGISTRO
			registroControl.sPagina = "/Aplicaciones/Sim/Usuarios/fGralUsuEmpReg.jsp?CveUsuario="+request.getParameter("CveUsuario");
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
		
		String sClaveUsuario = "";
		String sIdPersona = "";
		
		registro.addDefCampo("ID_PERSONA", request.getParameter("IdPersona")!= null ? request.getParameter("IdPersona") : "" );
		registro.addDefCampo("AP_PATERNO", request.getParameter("ApPaterno") != null ? request.getParameter("ApPaterno") : "" );
		registro.addDefCampo("AP_MATERNO", request.getParameter("ApMaterno") != null ? request.getParameter("ApMaterno") : "" );
		registro.addDefCampo("NOMBRE_1", request.getParameter("Nombre1") != null ? request.getParameter("Nombre1") : "");
		registro.addDefCampo("NOMBRE_2", request.getParameter("Nombre2") != null ? request.getParameter("Nombre2") : "");
		registro.addDefCampo("NOM_COMPLETO", request.getParameter("NomCompleto") != null ? request.getParameter("NomCompleto") : "");
		registro.addDefCampo("CLAVE_USUARIO", request.getParameter("ClaveUsuario") != null ? request.getParameter("ClaveUsuario") : "");
		registro.addDefCampo("PASSWORD", request.getParameter("Password"));
		registro.addDefCampo("LOCALIDAD", request.getParameter("Localidad"));
		registro.addDefCampo("CVE_PUESTO", request.getParameter("CvePuesto"));
		registro.addDefCampo("CVE_PERFIL", request.getParameter("CvePerfil"));
		registro.addDefCampo("NUM_NOMINA", request.getParameter("NumNomina"));
		registro.addDefCampo("FECHA_INGRESO_EMPRESA", request.getParameter("FechaIngresoEmpresa"));
		registro.addDefCampo("ID_SUCURSAL", request.getParameter("IdSucursal") != null ? request.getParameter("IdSucursal") : "");
		
		if(request.getParameter("BRegionales")!=null){
			registro.addDefCampo("B_REGIONALES","V");
		}else{
			registro.addDefCampo("B_REGIONALES","F");
		}
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimUsuarioEmpresa", registro, iTipoOperacion);
		
		if (iTipoOperacion == 1){
			String sTodasRegionales = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("TODAS_REGIONALES");
			sIdPersona = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("ID_PERSONA");
			sClaveUsuario = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("USUARIO");
			registro.addDefCampo("CLAVE_USUARIO",sClaveUsuario);
			if (sTodasRegionales.equals("V")){
				// SE ASIGNAN TODAS LAS SUCURSALES AL PRODUCTO
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimUsuarioRegionalTodas", registro, iTipoOperacion); //ALTA
			}
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimUsuarioEmpresa&OperacionCatalogo=CR&CveUsuario="+sClaveUsuario+"&IdPersona="+sIdPersona;
		}else if (iTipoOperacion == 2){
		
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimUsuarioEmpresa&OperacionCatalogo=IN&Filtro=Inicio";
		}else if (iTipoOperacion == 3){
			
			String sTodasRegionales = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("TODAS_REGIONALES");
			sClaveUsuario = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("USUARIO");
			sIdPersona = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("ID_PERSONA");
			registro.addDefCampo("CLAVE_USUARIO",sClaveUsuario);
			if (sTodasRegionales.equals("V")){
				registro.addDefCampo("REGIONALES","SI");
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimUsuarioRegionalTodas", registro, iTipoOperacion); 
			}else if (sTodasRegionales.equals("F")){
				registro.addDefCampo("REGIONALES","NO");
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimUsuarioRegionalTodas", registro, iTipoOperacion); 
			}
			
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimUsuarioEmpresa&OperacionCatalogo=CR&CveUsuario="+sClaveUsuario+"&IdPersona="+sIdPersona;
		}
		
		
		return registroControl;
	}
}