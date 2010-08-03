/**
 *  Sistema de   administración de portales.
 *
 *  Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.generales.cliente;

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
import java.util.Date;

/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificación y consulta) del catálogo de quejas y sugerencias. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class DonadorCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
		if (iTipoOperacion == CON_INICIALIZACION){
		}
		else if (iTipoOperacion == CON_CONSULTA_TABLA){
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			
			//OBTIENE LA CLAVE DE LA EMPRESA
			parametros.addDefCampo("CVE_EMPRESA", request.getParameter("CveEmpresa"));
			
			//VERIFICA SI SE ENVIO EL NUMERO DE DONACION
			if (request.getParameter("NumDonativo") != null && !request.getParameter("NumDonativo").equals("") && !request.getParameter("NumDonativo").equals("null") ){
				parametros.addDefCampo("NUM_DONATIVO", request.getParameter("NumDonativo"));
			}

			//VERIFICA SI SE ENVIO LA FECHA DE LA DONACION
			if (request.getParameter("FDonacion") != null && !request.getParameter("FDonacion").equals("") && !request.getParameter("FDonacion").equals("null") ){
				parametros.addDefCampo("F_DONACION", request.getParameter("FDonacion"));
			}
			//VERIFICA SI SE CONSULTA LOS REGISTROS CONTACTADOS
			if (request.getParameter("Estatus") != null && !request.getParameter("Estatus").equals("") && !request.getParameter("Estatus").equals("null") ){
				parametros.addDefCampo("ESTATUS", request.getParameter("Estatus"));
			}

			parametros.addDefCampo("Filtro","Todos");
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("HopePortalDonacionConsulta", parametros));
			registroControl.sPagina = "/Aplicaciones/ProgramasAyuda/fHopeDonacionCon.jsp?"+request.getParameter("CveEmpresa");
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			if ((request.getParameter("Filtro")).equals("Folio")){
				//OBTIENE SOLO EL REGISTRO SOLICITADO POR EL USUARIO PUBLICO
				parametros.addDefCampo("NUM_DONATIVO",request.getParameter("NumDonativo"));
				parametros.addDefCampo("CVE_EMPRESA",request.getParameter("CveEmpresa"));
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("HopePortalDonacionConsulta", parametros));
				
				Registro resultado = (Registro)registroControl.respuesta.getDefCampo("registro");
				if (resultado == null){
					registroControl.sPagina = "/Portales/Hope/SistemaErrorPagina.jsp?Mensaje= <font color='#000000' size='4'>El número de folio:<strong>"+request.getParameter("NumDonativo")+"</strong> no existe</font>";				}
				else{
					registroControl.sPagina = "/Portales/Hope/FolioDonacionReg.jsp";
				}
			}
			else{
				//OBTIENE SOLO EL REGISTRO SOLICITADO POR EL PERSONAL DE LA APLICACION
				parametros.addDefCampo("NUM_DONATIVO",request.getParameter("NumDonativo"));
				parametros.addDefCampo("CVE_EMPRESA",request.getParameter("CveEmpresa"));
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("HopePortalDonacionConsulta", parametros));
				registroControl.sPagina = "/Aplicaciones/ProgramasAyuda/fHopeDonacionReg.jsp";
			}
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
		
		registro.addDefCampo("CVE_EMPRESA",request.getParameter("CveEmpresa"));	
		registro.addDefCampo("ID_DONACION",request.getParameter("IdDonacion"));
		registro.addDefCampo("NUM_DONATIVO",request.getParameter("NumDonativo"));
		registro.addDefCampo("NOM_COMPLETO",request.getParameter("NomCompleto"));
		registro.addDefCampo("DOMICILIO",request.getParameter("Domicilio"));
		registro.addDefCampo("PAIS",request.getParameter("Pais"));
		registro.addDefCampo("CIUDAD",request.getParameter("Ciudad"));
		registro.addDefCampo("EMAIL",request.getParameter("Email"));
		registro.addDefCampo("NUM_TEL",request.getParameter("NumTel"));
		registro.addDefCampo("FAX",request.getParameter("Fax"));
		registro.addDefCampo("DESEAS_DONAR",request.getParameter("DeseasDonar"));
		registro.addDefCampo("F_DONACION",request.getParameter("FDonacion"));
		registro.addDefCampo("MSJ_RESPUESTA",request.getParameter("MsjRespuesta"));
		registro.addDefCampo("ESTATUS",request.getParameter("Estatus"));
		registro.addDefCampo("F_REGISTRO",request.getParameter("FRegistro"));
		
		if(request.getParameter("Aplicacion")!=null){
			registro.addDefCampo("F_REGISTRO",request.getParameter("FRegistro"));
		}
		else{
			registro.addDefCampo("F_REGISTRO",new Date());
		}
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("HopePortalDonacionConsulta", registro, iTipoOperacion);
	
		
		if(request.getParameter("Aplicacion")!=null){
			registroControl.sPagina = "/Aplicaciones/ProgramasAyuda/fHopeDonacionCon.jsp?CveEmpresa="+request.getParameter("CveEmpresa");	
		}
		else{
			Registro resultado = (Registro)registroControl.resultadoCatalogo.Resultado;
			String sNumDonativo = (String)resultado.getDefCampo("NUM_DONATIVO");
			String sNomCompleto = (String)resultado.getDefCampo("NOM_COMPLETO");
			registroControl.sPagina = "/Portales/Hope/SistemaErrorPagina.jsp?Mensaje= <font color='#000000' size='4'>Estimado<strong> "+sNomCompleto+",</strong> gracias por tu donativo. Nuestro personal te responderá a la brevedad,se te asigno el número de folio:<strong>"+sNumDonativo+" </strong>, recuerdalo y asi podras consultar en la página <strong>Consulta donativo.</strong></font>";
		}
		
		return registroControl;
	}
}
