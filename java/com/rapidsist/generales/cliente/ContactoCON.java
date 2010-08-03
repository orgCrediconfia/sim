/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.generales.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import com.rapidsist.comun.util.Fecha2;
import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificación y consulta) del catálogo de contactos. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class ContactoCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
		//ESTE CODIGO SE QUEDO COMO PRUEBA PARA ESTUDIAR LA CON		
		else if (iTipoOperacion == CON_CONSULTA_TABLA){
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			
			//OBTIENE LA CLAVE DE LA EMPRESA
			parametros.addDefCampo("CVE_EMPRESA", request.getParameter("CveEmpresa"));
			
			//VERIFICA SI SE ENVIO EL NOMBRE DE LA PERSONA
			if (request.getParameter("NombreCompleto") != null && !request.getParameter("NombreCompleto").equals("") && !request.getParameter("NombreCompleto").equals("null") ){
				parametros.addDefCampo("NOMBRE_COMPLETO", request.getParameter("NombreCompleto"));
			}

			//VERIFICA SI SE ENVIO LA FECHA DE ALTA
			if (request.getParameter("FAlta") != null && !request.getParameter("FAlta").equals("") && !request.getParameter("FAlta").equals("null") ){
				parametros.addDefCampo("F_ALTA", request.getParameter("FAlta"));
			}
			//VERIFICA SI SE CONSULTA LOS REGISTROS CONTACTADOS
			if (request.getParameter("BContactado") != null && !request.getParameter("BContactado").equals("") && !request.getParameter("BContactado").equals("null") ){
				parametros.addDefCampo("B_CONTACTADO", request.getParameter("BContactado"));
			}

			
			
			parametros.addDefCampo("Filtro","Todos");
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("AspidPortalContacto", parametros));
			registroControl.sPagina = "/Aplicaciones/AspidIntranet/fAspidContactoCon.jsp?"+request.getParameter("CveEmpresa");
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("ID_CONTACTO",request.getParameter("IdContacto"));
			parametros.addDefCampo("CVE_EMPRESA",request.getParameter("CveEmpresa"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("AspidPortalContacto", parametros));
			registroControl.sPagina = "/Aplicaciones/AspidIntranet/fAspidContactoReg.jsp";
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
		Fecha2 fecha = new Fecha2();
		
		registro.addDefCampo("CVE_EMPRESA",request.getParameter("CveEmpresa"));		
		registro.addDefCampo("ID_CONTACTO",request.getParameter("IdContacto"));
		registro.addDefCampo("NOMBRE_COMPLETO",request.getParameter("NombreCompleto"));
		registro.addDefCampo("EMAIL",request.getParameter("Email"));
        registro.addDefCampo("DIRECCION",request.getParameter("Direccion"));
		registro.addDefCampo("COLONIA",request.getParameter("Colonia"));
		registro.addDefCampo("CIUDAD",request.getParameter("Ciudad"));
		registro.addDefCampo("ESTADO",request.getParameter("Estado"));
		registro.addDefCampo("CODIGO_POSTAL",request.getParameter("CodigoPostal"));
		registro.addDefCampo("TELEFONO",request.getParameter("Telefono"));
		registro.addDefCampo("FAX",request.getParameter("Fax"));
		registro.addDefCampo("TX_COMENTARIOS",request.getParameter("TxComentarios"));
		registro.addDefCampo("B_CONTACTADO",request.getParameter("BContactado"));
		registro.addDefCampo("TX_OBSERVACIONES",request.getParameter("TxObservaciones"));
							
		if(request.getParameter("Aplicacion")!=null){
			registro.addDefCampo("F_ALTA",request.getParameter("FAlta"));
			registroControl.sPagina = "/Aplicaciones/AspidIntranet/fAspidContactoCon.jsp?CveEmpresa="+request.getParameter("CveEmpresa");
		}
		else{
			registro.addDefCampo("F_ALTA",new Date());
			registroControl.sPagina = "/Portales/Aspid/SistemaErrorPagina.jsp?Mensaje= <font color='#000000' size='4'>Gracias. <br> Pronto nos pondremos en contacto con usted.</font>";
		}
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("AspidPortalContacto", registro, iTipoOperacion);
		return registroControl;
	}
}
