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
public class QuejaSugerenciaCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			
			//VERIFICA SI SE ENVIO EL NUMERO DE QUEJA/SUGERENCIA
			if (request.getParameter("NumQuejaSugerencia") != null && !request.getParameter("NumQuejaSugerencia").equals("") && !request.getParameter("NumQuejaSugerencia").equals("null") ){
				parametros.addDefCampo("NUM_QUEJA_SUGERENCIA", request.getParameter("NumQuejaSugerencia"));
			}

			//VERIFICA SI SE ENVIO LA FECHA DE COMPRA
			if (request.getParameter("FCompra") != null && !request.getParameter("FCompra").equals("") && !request.getParameter("FCompra").equals("null") ){
				parametros.addDefCampo("F_COMPRA", request.getParameter("FCompra"));
			}
			//VERIFICA SI SE CONSULTA LOS REGISTROS CONTACTADOS
			if (request.getParameter("Estatus") != null && !request.getParameter("Estatus").equals("") && !request.getParameter("Estatus").equals("null") ){
				parametros.addDefCampo("ESTATUS", request.getParameter("Estatus"));
			}

			parametros.addDefCampo("Filtro","Todos");
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("AspidPortalQuejaConsulta", parametros));
			registroControl.sPagina = "/Aplicaciones/AspidIntranet/fAspidQuejaSugerenciaCon.jsp?"+request.getParameter("CveEmpresa");
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			if ((request.getParameter("Filtro")).equals("Folio")){
				//OBTIENE SOLO EL REGSITRO SOLICITADO POR EL USUARIO PUBLICO
				parametros.addDefCampo("NUM_QUEJA_SUGERENCIA",request.getParameter("NumQuejaSugerencia"));
				parametros.addDefCampo("CVE_EMPRESA",request.getParameter("CveEmpresa"));
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("AspidPortalQuejaConsulta", parametros));
				
				Registro resultado = (Registro)registroControl.respuesta.getDefCampo("registro");
				if (resultado == null){
					registroControl.sPagina = "/Portales/Aspid/SistemaErrorPagina.jsp?Mensaje= <font color='#000000' size='4'>El número de folio:<strong>"+request.getParameter("NumQuejaSugerencia")+"</strong> no existe</font>";				}
				else{
					registroControl.sPagina = "/Portales/Aspid/FolioQuejaSugerenciaReg.jsp";
				}
			}
			else{
				//OBTIENE SOLO EL REGISTRO SOLICITADO POR EL PERSONAL DE LA APLICACION
				parametros.addDefCampo("NUM_QUEJA_SUGERENCIA",request.getParameter("NumQuejaSugerencia"));
				parametros.addDefCampo("CVE_EMPRESA",request.getParameter("CveEmpresa"));
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("AspidPortalQuejaConsulta", parametros));
				registroControl.sPagina = "/Aplicaciones/AspidIntranet/fAspidQuejaSugerenciaReg.jsp";
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
		registro.addDefCampo("ID_QUEJA",request.getParameter("IdQueja"));
		registro.addDefCampo("NUM_QUEJA_SUGERENCIA",request.getParameter("NumQuejaSugerencia"));
		registro.addDefCampo("NOMBRE_COMPLETO",request.getParameter("NombreCompleto"));
		registro.addDefCampo("CLAVE_PRODUCTO",request.getParameter("ClaveProducto"));
        registro.addDefCampo("F_COMPRA",request.getParameter("FCompra"));
		registro.addDefCampo("CLAVE_LOTE",request.getParameter("ClaveLote"));
		registro.addDefCampo("EMAIL",request.getParameter("Email"));
		registro.addDefCampo("NOMBRE_DISTRIBUIDOR",request.getParameter("NombreDistribuidor"));
		registro.addDefCampo("QUEJA_SUGERENCIA",request.getParameter("QuejaSugerencia"));
		registro.addDefCampo("SOLUCION",request.getParameter("Solucion"));
		registro.addDefCampo("ESTATUS",request.getParameter("Estatus"));
		registro.addDefCampo("F_REGISTRO",request.getParameter("FRegistro"));
		
		if(request.getParameter("Aplicacion")!=null){
			registro.addDefCampo("F_REGISTRO",request.getParameter("FRegistro"));
		}
		else{
			registro.addDefCampo("F_REGISTRO",new Date());
		}
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("AspidPortalQuejaConsulta", registro, iTipoOperacion);
	
		
		if(request.getParameter("Aplicacion")!=null){
			registroControl.sPagina = "/Aplicaciones/AspidIntranet/fAspidQuejaSugerenciaCon.jsp?CveEmpresa="+request.getParameter("CveEmpresa");	
		}
		else{
			Registro resultado = (Registro)registroControl.resultadoCatalogo.Resultado;
			String sNumQuejaSugerencia = (String)resultado.getDefCampo("NUM_QUEJA_SUGERENCIA");
			String sNombreCompleto = (String)resultado.getDefCampo("NOMBRE_COMPLETO");
			registroControl.sPagina = "/Portales/Aspid/SistemaErrorPagina.jsp?Mensaje= <font color='#000000' size='4'>Gracias<strong> "+sNombreCompleto+".</strong> Nuestro departamento de desarrollo le responderá a la brevedad, favor de consultar con su número de folio:<strong>"+sNumQuejaSugerencia+"</strong></font>";
		}
		
		return registroControl;
	}
}
