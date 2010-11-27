/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.herramientasconfiguracion.cliente;

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
import java.util.LinkedList;

/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificación y consulta) del catálogo de operaciones. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class FuncionOperacionCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			parametros.addDefCampo("Filtro","Todos");
			registroControl.respuesta.addDefCampo("ListaOpciones", catalogoSL.getRegistros("HerramientasConfiguracionFuncionOpcion", parametros));
			registroControl.sPagina = "/Aplicaciones/HerramientasConfiguracion/fApliFunOpcionAlt.jsp";
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("Filtro","Todos");
			registroControl.respuesta.addDefCampo("ListaOpciones", catalogoSL.getRegistros("HerramientasConfiguracionFuncionOpcion", parametros));
			registroControl.sPagina = "/Aplicaciones/HerramientasConfiguracion/fApliFunOpcionAlt.jsp";
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			parametros.addDefCampo("CVE_FUNCION",request.getParameter("CveFuncion"));
			parametros.addDefCampo("CVE_OPERACION",request.getParameter("CveOperacion"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("HerramientasConfiguracionFuncionOperacion", parametros));
			parametros.addDefCampo("Filtro","Todos");
			registroControl.respuesta.addDefCampo("ListaOpciones", catalogoSL.getRegistros("HerramientasConfiguracionFuncionOpcion", parametros));
			registroControl.sPagina = "/Aplicaciones/HerramientasConfiguracion/fApliFunOpcionAlt.jsp";


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
		registro.addDefCampo("CVE_OPERACION",request.getParameter("CveOperacion"));
		registro.addDefCampo("TX_OPERACION",  request.getParameter("TxOperacion"));
		registro.addDefCampo("B_DEPURAR_CODIGO",request.getParameter("bDepurarCodigo"));

	if (iTipoOperacion ==CatalogoControl.CON_BAJA){


		//RECUPERA LA LISTA DE LAS APLICACIONES QUE SE VAN A PROCESAR
			LinkedList listaOperacion = null;
			String sClaveOperacion = new String();
			Registro registroPermiso = null;
			Enumeration lista = request.getParameterNames();

			//OBTIENE EL ARREGLO CON LAS APLICACIONES A PROCESAR
			String[] sOperacion = request.getParameterValues("CveOperacion");
			//VERIFICA SI ENCONTRO EL ARREGLO DE APLICACIONES
			//PARAMETRO PARA DAR DE BAJA INDIVIDUALMENTE DESDE LA CONSULTA
			if (request.getParameter("BajaIndividual")==(null)){

				if (sOperacion != null) {

					for (int iNumParametro = 0; iNumParametro < sOperacion.length; iNumParametro++) {
						//VERIFICA SI LA LISTA DE APLICACIONES ESTA INICIALIZADA
						if (listaOperacion == null) {
							listaOperacion = new LinkedList();
						}
						//listaOperacion = new LinkedList();
						//OBTIENE LA CLAVE DE LA APLICACION
						sClaveOperacion = sOperacion[iNumParametro];

						if (request.getParameter("BajaOperacion" + sClaveOperacion) != null) {
							registroPermiso = new Registro();
							registroPermiso.addDefCampo("CVE_OPERACION", sClaveOperacion);
							registroPermiso.addDefCampo("CVE_FUNCION", request.getParameter("CveFuncion"));
							listaOperacion.add(registroPermiso);
						}

						//VERIFICA SI SE SELECCIONARON FUNCIONES PARA PROCESAR
						if (listaOperacion != null) {
							registro.addDefCampo("ListaRegistros", listaOperacion);
						}
					}
				}
			}
		}


		registroControl.sPagina = "/ProcesaCatalogo?Funcion=HerramientasConfiguracionFuncion&OperacionCatalogo=CR&Filtro=Todos&CveFuncion=" + request.getParameter("CveFuncion");
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("HerramientasConfiguracionFuncionOperacion", registro, iTipoOperacion);
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