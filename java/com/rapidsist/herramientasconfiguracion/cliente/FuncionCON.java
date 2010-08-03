/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.herramientasconfiguracion.cliente;

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
import java.util.LinkedList;

/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificación y consulta) del catálogo de funciones. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class FuncionCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			
			parametros.addDefCampo("Filtro","Todos");
			//VERIICA SI SE ENVIO LA CLAVE DE LA FUNCION
			if (request.getParameter("CveFuncion") != null && !request.getParameter("CveFuncion").equals("")){
				parametros.addDefCampo("CVE_FUNCION", request.getParameter("CveFuncion"));
			}
			//VERIFICA SI SE ENVIO EL PARAMETRO USUARIO
			if (request.getParameter("NomFuncion") != null && !request.getParameter("NomFuncion").equals("")){
				parametros.addDefCampo("NOM_FUNCION", request.getParameter("NomFuncion"));
			}
			//VERIFICA SI SE ENVIO EL PARAMETRO USUARIO
			if (request.getParameter("Bloqueado") != null && !request.getParameter("Bloqueado").equals("")){
				parametros.addDefCampo("B_BLOQUEADO", request.getParameter("Bloqueado"));
			}
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("HerramientasConfiguracionFuncion", parametros));
			registroControl.sPagina = "/Aplicaciones/HerramientasConfiguracion/fApliFunCon.jsp";
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("CVE_FUNCION",request.getParameter("CveFuncion"));
			System.out.println("paso 1");
			Registro registroFuncion = catalogoSL.getRegistro("HerramientasConfiguracionFuncion", parametros);
			System.out.println("paso 2");
			registroControl.respuesta.addDefCampo("registro", registroFuncion);
			System.out.println("paso 3");
			parametros.addDefCampo("Filtro", "Todos");
			System.out.println("paso 4");
			LinkedList listaOperacion = catalogoSL.getRegistros("HerramientasConfiguracionFuncionOperacion", parametros);
			System.out.println("paso 5");
			registroControl.respuesta.addDefCampo("listaOperacion", listaOperacion);
			System.out.println("paso 6");
			//parametros.addDefCampo("CVE_TABLA", registroFuncion.getDefCampo("CVE_TABLA"));
			System.out.println("paso 7");
			//registroControl.respuesta.addDefCampo("registroTabla", catalogoSL.getRegistro("HerramientasConfiguracionTabla", parametros));
			registroControl.sPagina = "/Aplicaciones/HerramientasConfiguracion/fApliFunReg.jsp";
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

		registro.addDefCampo("CVE_FUNCION",request.getParameter("CveFuncion"));
		registro.addDefCampo("NOM_FUNCION",request.getParameter("NomFuncion"));
		registro.addDefCampo("TX_DESC_FUNCION",request.getParameter("TxDescFuncion"));
		registro.addDefCampo("URL_FUNCION",request.getParameter("Url"));
		registro.addDefCampo("B_WEBSERVICE",request.getParameter("Webservice"));
		registro.addDefCampo("B_BLOQUEADO",request.getParameter("Bloqueado"));
		registro.addDefCampo("NOM_CLASE_CON",request.getParameter("NomClaseCon"));
		registro.addDefCampo("NOM_CLASE_DAO",request.getParameter("NomClaseDao"));
		registro.addDefCampo("NOM_CLASE_REPORTE",request.getParameter("NomClaseReporte"));
		registro.addDefCampo("B_BITACORA",request.getParameter("bBitacora"));
		registro.addDefCampo("B_PAGINACION",request.getParameter("bPaginacion"));
		registro.addDefCampo("B_RASTREA_CODIGO",request.getParameter("bRastreaCodigo"));
		registro.addDefCampo("CVE_TABLA",request.getParameter("CveTabla"));
		registro.addDefCampo("NOM_METODO_MENU",request.getParameter("NomMetodoMenu"));

		registroControl.sPagina = "/ProcesaCatalogo?Funcion=HerramientasConfiguracionFuncion&OperacionCatalogo=CT&Filtro=Todos";

		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("HerramientasConfiguracionFuncion", registro, iTipoOperacion);
		try {
			//DESTRUYE EL OBJETO FUNCION EN EL JNDI
			contexto.unbind("Funcion_" + request.getParameter("CveFuncion"));
		}
		catch (javax.naming.NameNotFoundException ex) {
			//ENTRAR A ESTA SECCION SIGNIFICA QUE NO ESTABA CARGADA LA FUNCION
			//EN EL JNDI Y PREVIENE QUE LA INFRAESTRUCTURA MUESTRE LA PANTALLA DE
			//ERRROR
		}
		return registroControl;
	}
}