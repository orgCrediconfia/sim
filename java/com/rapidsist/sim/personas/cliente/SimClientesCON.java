/**
 * Sistema de administraci�n de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.personas.cliente;

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
 * modificaci�n y consulta) de los clientes. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimClientesCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

	/**
	 * Ejecuta los servicios de consulta del cat�logo.
	 * @param parametros Par�metros que se recogen de la sesion del usuario y se le envian a la clase CON.
	 * Estos par�metros son: CVE_GPO_EMPRESA (Clave del grupo empresa), Filtro (el valor
	 * del filtro que se debe aplicar solo si se ejecuto el componente de cat�logos con
	 * OperacionCatalogo=CT)
	 * @param request Objeto que provee de informaci�n al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo env�a como un par�metro a este m�todo.
	 * @param response Objeto que provee de informaci�n del servlet sobre el response del cliente. El
	 * contenedor de servlets crea un objeto HttpServletResponse y lo env�a como un par�metro a este m�todo.
	 * @param config Objeto que provee de informaci�n del servlet sobre el ServletConfig del cliente. El
	 * contenedor de servlets crea un objeto ServletConfig y lo env�a como un par�metro a este m�todo.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON
	 * @param contexto Objeto que contiene informaci�n acerca del entorno del servidor de
	 * aplicaciones.
	 * @param iTipoOperacion Operaci�n que debe ejecutar la clase CON. Las operaciones se encuentran
	 * especificadas en la clase {@link com.rapidsist.portal.cliente.CatalogoControl CatalogoControl}
	 * @return Resultado de la consulta y la p�gina a donde se redirecciona el control.
	 * @throws RemoteException Si se gener� un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se gener� un error dentro de la clase CON.
	 */
	public RegistroControl consulta(Registro parametros, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		//VERIFICA SI BUSCA TODOS LOS REGISTROS
		if (iTipoOperacion == CON_CONSULTA_TABLA){
			//VERIFICA SI SE ENVIO EL PARAMETRO NOMBRE
			
			if (request.getParameter("IdPersona") != null && !request.getParameter("IdPersona").equals("")){
				parametros.addDefCampo("ID_PERSONA", request.getParameter("IdPersona"));
			}
			if (request.getParameter("NomCompleto") != null && !request.getParameter("NomCompleto").equals("")){
				parametros.addDefCampo("NOM_COMPLETO", request.getParameter("NomCompleto"));
			}
			//VERIFICA SI SE ENVIO EL PARAMETRO RFC
			if (request.getParameter("Rfc") != null && !request.getParameter("Rfc").equals("")){
				parametros.addDefCampo("RFC", request.getParameter("Rfc"));
			}			
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimClientes", parametros));
			
			//CUENTA CUANTAS PAGINAS SER�N
			Registro paginas = new Registro ();
			paginas = catalogoSL.getRegistro("SimPersonaPaginacion", parametros);
			String sPaginas = (String)paginas.getDefCampo("PAGINAS");
			
			registroControl.sPagina = "/Aplicaciones/Sim/Personas/fSimCliCon.jsp?TipoPersona="+request.getParameter("Sentencia")+"&Paginas="+sPaginas+"&Superior="+100+"&IdPersona="+request.getParameter("IdPersona")+"&NomCompleto="+request.getParameter("NomCompleto")+"&Rfc="+request.getParameter("Rfc");
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("ID_PERSONA",request.getParameter("IdPersona"));
			parametros.addDefCampo("ID_EX_CONYUGE",request.getParameter("IdExConyuge"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimClientes", parametros));
			
			registroControl.respuesta.addDefCampo("registroExConyuge", catalogoSL.getRegistro("SimClienteExConyuge", parametros));	
			parametros.addDefCampo("ROLES","ASIGNADOS");
			registroControl.respuesta.addDefCampo("ListaRoles", catalogoSL.getRegistros("SimPersonaRoles", parametros));
			registroControl.respuesta.addDefCampo("ListaTelefono", catalogoSL.getRegistros("SimClienteTelefono", parametros));
			registroControl.respuesta.addDefCampo("ListaTelefonoExConyuge", catalogoSL.getRegistros("SimClienteExConyugeTelefono", parametros));
			registroControl.respuesta.addDefCampo("ListaDireccion", catalogoSL.getRegistros("SimClienteDireccion", parametros));
			registroControl.respuesta.addDefCampo("ListaDireccionExConyuge", catalogoSL.getRegistros("SimClienteExConyugeDireccion", parametros));
			registroControl.respuesta.addDefCampo("ListaCuentaBancaria", catalogoSL.getRegistros("SimClienteCuentaBancaria", parametros));
			registroControl.respuesta.addDefCampo("ListaEscolaridad", catalogoSL.getRegistros("SimCatalogoEscolaridad", parametros));
			registroControl.respuesta.addDefCampo("ListaIdentificacionOficial", catalogoSL.getRegistros("SimCatalogoIdentificacionOficial", parametros));
			registroControl.respuesta.addDefCampo("ListaDocumentacion", catalogoSL.getRegistros("SimClienteDocumentacion", parametros));
			registroControl.respuesta.addDefCampo("ListaReferencia", catalogoSL.getRegistros("SimClienteReferencia", parametros));
			registroControl.respuesta.addDefCampo("ListaNegocio", catalogoSL.getRegistros("SimClienteNegocio", parametros));
			registroControl.respuesta.addDefCampo("ListaIntegranteUEF", catalogoSL.getRegistros("SimClienteIntegranteUEF", parametros));
			registroControl.respuesta.addDefCampo("ListaAdeudo", catalogoSL.getRegistros("SimClienteAdeudo", parametros));
			registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimUsuarioSucursalAsignada", parametros));
			registroControl.respuesta.addDefCampo("ListaGarantia", catalogoSL.getRegistros("SimClienteGarantia", parametros));	
			//VERIFICA SI SOLO CONSULTA LOS DATOS DEL REGISTRO
			registroControl.sPagina = "/Aplicaciones/Sim/Personas/fSimCliReg.jsp";
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Alta")){
				registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimUsuarioSucursalAsignada", parametros));
				registroControl.respuesta.addDefCampo("ListaEscolaridad", catalogoSL.getRegistros("SimCatalogoEscolaridad", parametros));
				registroControl.respuesta.addDefCampo("ListaIdentificacionOficial", catalogoSL.getRegistros("SimCatalogoIdentificacionOficial", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Personas/fSimCliReg.jsp";
			}else if (request.getParameter("Filtro").equals("Inicio")){
				registroControl.sPagina = "/Aplicaciones/Sim/Personas/fSimCliCon.jsp";
			}
		}
		return registroControl;
	}

	/**
	 * Valida los p�rametros de entrada y ejecuta los servicios de alta, baja o cambio.
	 * @param registro Parametros que se recogen de la sesion del usuario y se le envian a la clase CON.
	 * Estos par�metros son: CVE_GPO_EMPRESA (Clave del grupo empresa), CVE_USUARIO_BITACORA (clave
	 * del usuario que realiza la operacion), RegistroOriginal (registro leido originalmente y
	 * se utiliza cuando se ejecuta la operaci�n de modificacion y se verifica que no se hallan
	 * realizado modificaciones al registro).
	 * @param request Objeto que provee de informaci�n al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo env�a como un par�metro a este m�todo.
	 * @param response Objeto que provee de informaci�n del servlet sobre el response del cliente. El
	 * contenedor de servlets crea un objeto HttpServletResponse y lo env�a como un par�metro a este m�todo.
	 * @param config Objeto que provee de informaci�n del servlet sobre el ServletConfig del cliente. El
	 * contenedor de servlets crea un objeto ServletConfig y lo env�a como un par�metro a este m�todo.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON
	 * @param contexto Objeto que contiene informaci�n acerca del entorno del servidor de
	 * aplicaciones.
	 * @param iTipoOperacion Operaci�n que debe ejecutar la clase CON. Las operaciones se encuentran
	 * especificadas en la clase {@link com.rapidsist.portal.cliente.CatalogoControl CatalogoControl}
	 * @return Respuesta del servicio de alta, baja o cambio y la p�gina a donde
	 * se redirecciona el control.
	 * @throws RemoteException Si se gener� un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se gener� un error dentro de la clase CON.
	 */
	public RegistroControl actualiza(Registro registro, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		
		String sIdPersona = "";
		String sIdExConyuge = "";
		
		//OBTIENE PARAMETROS DE PERSONA
		registro.addDefCampo("ID_PERSONA", request.getParameter("IdPersona"));
		registro.addDefCampo("AP_PATERNO", request.getParameter("ApPaterno") != null ? request.getParameter("ApPaterno") : "" );
		registro.addDefCampo("AP_MATERNO", request.getParameter("ApMaterno") != null ? request.getParameter("ApMaterno") : "" );
		registro.addDefCampo("NOMBRE_1", request.getParameter("Nombre1") != null ? request.getParameter("Nombre1") : "");
		registro.addDefCampo("NOMBRE_2", request.getParameter("Nombre2") != null ? request.getParameter("Nombre2") : "");
		registro.addDefCampo("NOM_COMPLETO", request.getParameter("NomCompleto") != null ? request.getParameter("NomCompleto") : "");
		registro.addDefCampo("NOMBRE_ALTERNO", request.getParameter("NombreAlterno") != null ? request.getParameter("NombreAlterno") : "");
		registro.addDefCampo("SEXO", request.getParameter("Sexo") != null ? request.getParameter("Sexo") : "");
		registro.addDefCampo("ESTADO_CIVIL", request.getParameter("EstadoCivil")!= null ? request.getParameter("EstadoCivil") : "");
		registro.addDefCampo("REGIMEN_MARITAL", request.getParameter("RegimenMarital")!= null ? request.getParameter("RegimenMarital") : "");
		registro.addDefCampo("FECHA_NACIMIENTO", request.getParameter("FechaNacimiento")!= null ? request.getParameter("FechaNacimiento") : "");
		registro.addDefCampo("IDENTIFICACION_OFICIAL", request.getParameter("IdentificacionOficial")!= null ? request.getParameter("IdentificacionOficial") : "");
		registro.addDefCampo("NUM_IDENTIFICACION_OFICIAL", request.getParameter("NumIdentificacionOficial")!= null ? request.getParameter("NumIdentificacionOficial") : "");
		registro.addDefCampo("RFC",request.getParameter("Rfc")!= null ? request.getParameter("Rfc") : "");
		registro.addDefCampo("CURP", request.getParameter("Curp")!= null ? request.getParameter("Curp") : "");
		registro.addDefCampo("NUM_DEPENDIENTES_ECONOMICOS",request.getParameter("NumDependientesEconomicos")!= null ? request.getParameter("NumDependientesEconomicos") : "");
		registro.addDefCampo("ID_ESCOLARIDAD",request.getParameter("IdEscolaridad")!= null ? request.getParameter("IdEscolaridad") : "");
		registro.addDefCampo("ID_ESCOLARIDAD",request.getParameter("IdEscolaridad")!= null ? request.getParameter("IdEscolaridad") : "");
		registro.addDefCampo("CVE_ASESOR_CREDITO",request.getParameter("IdAsesorCredito")!= null ? request.getParameter("IdAsesorCredito") : "");
		registro.addDefCampo("ID_SUCURSAL",request.getParameter("IdSucursal")!= null ? request.getParameter("IdSucursal") : "");
		
		if(request.getParameter("ListaNegraSi")!=null){
			registro.addDefCampo("LISTA_NEGRA","V");
		}else{
			registro.addDefCampo("LISTA_NEGRA","F");
		}
		
		registro.addDefCampo("ID_EX_CONYUGE", request.getParameter("IdExConyuge"));
		registro.addDefCampo("ID_PERSONA_EX_CONYUGE", request.getParameter("IdPersonaExConyuge"));
		registro.addDefCampo("AP_PATERNO_EX_CONYUGE", request.getParameter("ApPaternoExConyuge") != null ? request.getParameter("ApPaternoExConyuge") : "" );
		registro.addDefCampo("AP_MATERNO_EX_CONYUGE", request.getParameter("ApMaternoExConyuge") != null ? request.getParameter("ApMaternoExConyuge") : "" );
		registro.addDefCampo("NOMBRE_1_EX_CONYUGE", request.getParameter("Nombre1ExConyuge") != null ? request.getParameter("Nombre1ExConyuge") : "");
		registro.addDefCampo("NOMBRE_2_EX_CONYUGE", request.getParameter("Nombre2ExConyuge") != null ? request.getParameter("Nombre2ExConyuge") : "");
		registro.addDefCampo("NOM_COMPLETO_EX_CONYUGE", request.getParameter("NomCompletoExConyuge") != null ? request.getParameter("NomCompletoExConyuge") : "");
		registro.addDefCampo("SEXO_EX_CONYUGE", request.getParameter("SexoExConyuge") != null ? request.getParameter("SexoExConyuge") : "");
		registro.addDefCampo("FECHA_NACIMIENTO_EX_CONYUGE", request.getParameter("FechaNacimientoExConyuge")!= null ? request.getParameter("FechaNacimientoExConyuge") : "");
		registro.addDefCampo("IDENTIFICACION_OFICIAL_EX_CONYUGE", request.getParameter("IdentificacionOficialExConyuge"));
		registro.addDefCampo("NUM_IDENTIFICACION_OFICIAL_EX_CONYUGE", request.getParameter("NumIdentificacionOficialExConyuge"));
		
		if (request.getParameter("EstadoCivil").equals("Casado") || request.getParameter("EstadoCivil").equals("Divorciado")){
			if(request.getParameter("IdPersona") == ""){
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimClientes", registro, iTipoOperacion);
			}
			else if(request.getParameter("IdPersona") != ""  && request.getParameter("IdExConyuge") == ""){
				registro.addDefCampo("MODIFICACION_CONYUGE","NO");
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimClientes", registro, iTipoOperacion);
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimClienteExConyuge", registro, 1);
				sIdExConyuge = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("ID_EX_CONYUGE");
			}else if(request.getParameter("IdPersona") != "" && request.getParameter("IdExConyuge") != ""){
				registro.addDefCampo("MODIFICACION_CONYUGE","SI");
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimClientes", registro, iTipoOperacion);
			}
		}else {
			registro.addDefCampo("MODIFICACION_CONYUGE","NO");
			registroControl.resultadoCatalogo = catalogoSL.modificacion("SimClientes", registro, iTipoOperacion);
		}
		
		if (iTipoOperacion == 1){
			sIdPersona = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("ID_PERSONA");
		}
		if (iTipoOperacion == 3){
			sIdPersona = request.getParameter("IdPersona");
			if(sIdExConyuge.equals("")){
				sIdExConyuge = request.getParameter("IdExConyuge");
			}
		}
		
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimClientes&OperacionCatalogo=CR&IdPersona="+sIdPersona+"&IdExConyuge="+sIdExConyuge;
		
		return registroControl;
	}
}