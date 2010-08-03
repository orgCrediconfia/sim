/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.generales.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControl;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
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
 * modificación y consulta) de la firma electrónica. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimGeneralesFirmaElectronicaCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN {

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
		
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("Usuario");
		parametros.addDefCampo("CVE_USUARIO_CAJERO", usuario.sCveUsuario);
		if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			if (request.getParameter("Modulo").equals("Caja")){
				parametros.addDefCampo("MODULO","CAJA");
				parametros.addDefCampo("ID_CAJA",request.getParameter("IdCaja"));
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimGeneralesFirmaElectronica", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Generales/fSimFirmaElectronica.jsp?Modulo=Caja&IdCaja="+request.getParameter("IdCaja")+
				"&IdBitacora=" + request.getParameter("IdBitacora") + "&TituloVentana=Validación del coordinador";	
			}else if (request.getParameter("Modulo").equals("Cierre")){
				parametros.addDefCampo("MODULO","CIERRE");
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimGeneralesFirmaElectronica", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Generales/fSimFirmaElectronica.jsp?Modulo=Cierre&IdCaja="+request.getParameter("IdCaja")+
				"&IdBitacora=" + request.getParameter("IdBitacora") + "&TituloVentana=Proceso de cierre";	
			}
			
			
			/*
			// SI SE ENVIA EL PARÁMETRO DE VALIDA USUARIO SOLO VALIDA EL USUARIO
			if(request.getParameter("ValidaGerente")!=null){
				// SOLO VALIDA EL USUARIO
				parametros.addDefCampo("CVE_INTEGRANTE", "GERENTE");
				parametros.addDefCampo("ID_CAJA",request.getParameter("IdCaja"));
				System.out.println("2*futuro***"+request.getParameter("IdCaja"));
				parametros.addDefCampo("ID_INTEGRANTE", request.getParameter("IdGerente"));
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimGeneralesFirmaElectronica", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Generales/fSimFirmaElectronica.jsp?IdCajero=" + 
				request.getParameter("IdCajero")+ "&IdCoordinador=" + request.getParameter("IdCoordinador")+ 
				"&IdCaja="+request.getParameter("IdCaja")+"&IdBitacora=" + request.getParameter("IdBitacora") + "&CveIntegrante=GERENTE&IdGerente=" + 
				request.getParameter("IdGerente") + "&TxNota=" + request.getParameter("TxNota")
				+"&TituloVentana=Validación del gerente&Url=" + request.getParameter("Url");
				// SI SE ENVIA EL PARÁMETRO DE VALIDA INTEGRANTE1 VALIDA AMBOS INTEGRANTES
			}else if(request.getParameter("ValidaCajero")!=null){
				// VALIDA EL INTEGRANTE 1
				parametros.addDefCampo("CVE_INTEGRANTE", "CAJERO");
				parametros.addDefCampo("ID_INTEGRANTE", request.getParameter("IdCajero"));
				parametros.addDefCampo("USUARIO", request.getParameter("Usuario"));
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimGeneralesFirmaElectronica", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Generales/fSimFirmaElectronica.jsp?IdCajero=" + 
				request.getParameter("IdCajero")+ "&IdCoordinador=" + request.getParameter("IdCoordinador")+ "&IdCaja="+request.getParameter("IdCaja")+"&IdUsuario=" + 
				request.getParameter("IdUsuario") + "&IdBitacora=" + request.getParameter("IdBitacora") + "&CveIntegrante=CAJERO" + 
				"&TxNota=" + request.getParameter("TxNota")+"&TituloVentana=Validación del cajero&Url=" + request.getParameter("Url");
				System.out.println("#Url#"+request.getParameter("Url"));
			}else if(request.getParameter("ValidaCoordinador")!=null){
				System.out.println("Como aprobo la validadcion del cajero ahora muestra la jsp ara validar el coordinador");
				// VALIDA EL INTEGRANTE 2
				parametros.addDefCampo("CVE_INTEGRANTE","COORDINADOR");
				parametros.addDefCampo("ID_CAJA",request.getParameter("IdCaja"));
				System.out.println("2****");
				parametros.addDefCampo("ID_INTEGRANTE", request.getParameter("IdCoordinador"));
				System.out.println("3****");
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimGeneralesFirmaElectronica", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Generales/fSimFirmaElectronica.jsp?IdCajero=" + 
				request.getParameter("IdCajero")+ "&IdCoordinador=" + request.getParameter("IdCoordinador")+ 
				"&IdCaja="+request.getParameter("IdCaja")+"&IdBitacora=" + request.getParameter("IdBitacora") + "&CveIntegrante=COORDINADOR" +  
				"&TituloVentana=Validación el coordinador";			
			} 
			*/
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
		
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("Usuario");
		registro.addDefCampo("CVE_USUARIO_CAJERO", usuario.sCveUsuario);
	
		String sIdBitacora = "";
		/*
		// AGREGA LOS PARAMETROS AL REGISTRO
		if(request.getParameter("CveIntegrante").equals("GERENTE")){
			registro.addDefCampo("ID_INTEGRANTE",			request.getParameter("IdGerente"));
			registro.addDefCampo("ID_USUARIO",	request.getParameter("IdUsuario"));
		}else if (request.getParameter("CveIntegrante").equals("CAJERO")){
			registro.addDefCampo("ID_INTEGRANTE",			request.getParameter("IdCajero"));
			registro.addDefCampo("ID_USUARIO",	request.getParameter("IdUsuario"));
		}else if (request.getParameter("CveIntegrante").equals("COORDINADOR")){
			registro.addDefCampo("ID_INTEGRANTE",			request.getParameter("IdCoordinador"));
			registro.addDefCampo("ID_USUARIO","");
		}
		*/
		
		registro.addDefCampo("MODULO",   			request.getParameter("Modulo"));
		registro.addDefCampo("URL",   			request.getParameter("Url"));
		registro.addDefCampo("FH_DESPLIEGUE",   request.getParameter("FHDespliegue"));
		//registro.addDefCampo("CVE_INTEGRANTE",  request.getParameter("CveIntegrante"));
		//registro.addDefCampo("ID_INTEGRANTE1",	request.getParameter("IdCajero"));
		//registro.addDefCampo("ID_INTEGRANTE2",  request.getParameter("IdIntegrante2"));
		registro.addDefCampo("PASSWORD_FIRMA",  request.getParameter("PasswordFirma"));         
		registro.addDefCampo("TX_NOTA", 	    request.getParameter("TxNota"));         
		registro.addDefCampo("ID_BITACORA",     request.getParameter("IdBitacora"));
		registro.addDefCampo("ID_CAJA",     	request.getParameter("IdCaja"));
		
		// DESHABILITA EL REREAD
		registro.addDefCampo("OperacionInfraestructura", "DeshabilitaReRead");
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimGeneralesFirmaElectronica", registro, iTipoOperacion);
		
		// RECUPERA EL ID_BITACORA PARA ENVIARLO COMO PARÁMETRO
		if(request.getParameter("IdBitacora").equals("null")){
			sIdBitacora = (String)registroControl.resultadoCatalogo.Resultado.getDefCampo("ID_BITACORA");
		}
		else{
			sIdBitacora = request.getParameter("IdBitacora");
		}
		
		if(registroControl.resultadoCatalogo.Resultado.getDefCampo("VALIDACION_CORRECTA").equals("SI")){
				registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimGeneralesFirmaElectronica&OperacionCatalogo=CR"+
				"&Modulo="+request.getParameter("Modulo")+"&IdCaja="+request.getParameter("IdCaja")+"&RegresaControl=SI&IdBitacora=" + sIdBitacora;
				
		}else{
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimGeneralesFirmaElectronica&OperacionCatalogo=CR"+
			"&Modulo="+request.getParameter("Modulo")+"&IdCaja="+request.getParameter("IdCaja")+"&ValidaCoordinador=SI"+ "&PasswordIncorrecto=Si&IdBitacora=" +	sIdBitacora;
		}
		
		
		/*
		if(request.getParameter("CveIntegrante").equals("GERENTE")){
			// VALIDA EL USUARIO 
			if(registroControl.resultadoCatalogo.Resultado.getDefCampo("VALIDACION_CORRECTA").equals("SI")){
				registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimGeneralesFirmaElectronica&OperacionCatalogo=CR"+
				"&IdCaja="+request.getParameter("IdCaja")+"&IdCoordinador=" + request.getParameter("IdCoordinador") +
				"&IdGerente="+request.getParameter("IdGerente")+"&ValidaGerente=SI&RegresaControl=SI&IdBitacora=" + sIdBitacora;
			}else{
				registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimGeneralesFirmaElectronica&OperacionCatalogo=CR"+
				"&IdCaja="+request.getParameter("IdCaja")+"&IdCoordinador=" + request.getParameter("IdCoordinador") +
				"&IdGerente="+request.getParameter("IdGerente")+"&ValidaGerente=SI"+ "&PasswordIncorrecto=Si&IdBitacora=" +	sIdBitacora;
				
			}
			
		}else{
			// VALIDA LOS INTEGRANTES
			if(request.getParameter("CveIntegrante").equals("CAJERO")){
				if(registroControl.resultadoCatalogo.Resultado.getDefCampo("VALIDACION_CORRECTA").equals("SI")){
					registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimGeneralesFirmaElectronica&OperacionCatalogo=CR"+
					"&IdCajero=" +	request.getParameter("IdCajero") + "&IdCoordinador=" + request.getParameter("IdCoordinador") +
					"&IdGerente="+request.getParameter("IdGerente")+"&IdCaja="+request.getParameter("IdCaja")+"&ValidaCoordinador=SI&IdBitacora=" + sIdBitacora;
				}else{
					registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimGeneralesFirmaElectronica&OperacionCatalogo=CR"+
					"&IdCaja="+request.getParameter("IdCaja")+"&IdCajero=" +	request.getParameter("IdCajero") + "&IdCoordinador=" + request.getParameter("IdCoordinador") +
					"&IdGerente="+request.getParameter("IdGerente")+"&ValidaCajero=SI"+ "&PasswordIncorrecto=Si&IdBitacora=" +	sIdBitacora;
				}
			}else{
				if(request.getParameter("CveIntegrante").equals("COORDINADOR")){
					if(registroControl.resultadoCatalogo.Resultado.getDefCampo("VALIDACION_CORRECTA").equals("SI")){
						if (request.getParameter("IdGerente").equals("No")){
							registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimGeneralesFirmaElectronica&OperacionCatalogo=CR"+
							"&IdCaja="+request.getParameter("IdCaja")+"&IdCoordinador=" + request.getParameter("IdCoordinador") +
							"&IdGerente="+request.getParameter("IdGerente")+"&ValidaCoordinador=SI&RegresaControl=SI&IdBitacora=" + sIdBitacora;
						}else if (request.getParameter("IdGerente").equals("Si")){
							registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimGeneralesFirmaElectronica&OperacionCatalogo=CR"+
							"&IdCaja="+request.getParameter("IdCaja")+"&IdCoordinador=" + request.getParameter("IdCoordinador") +
							"&IdGerente="+request.getParameter("IdGerente")+"&ValidaGerente=SI&IdBitacora=" +	sIdBitacora;
						}
					}else{
						registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimGeneralesFirmaElectronica&OperacionCatalogo=CR"+
						"&IdCaja="+request.getParameter("IdCaja")+"&IdCoordinador=" + request.getParameter("IdCoordinador") +
						"&IdGerente="+request.getParameter("IdGerente")+"&ValidaCoordinador=SI"+ "&PasswordIncorrecto=Si&IdBitacora=" +	sIdBitacora;
					}
				}
			}
		}
		*/
		return registroControl;
	}
	
}
