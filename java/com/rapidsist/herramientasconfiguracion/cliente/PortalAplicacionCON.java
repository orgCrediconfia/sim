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
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Enumeration;

/**
 * Esta clase se encarga de administrar  las operaciones (alta, baja,
 * modificación y consulta) del catálogo de portal - aplicaciones. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class PortalAplicacionCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
		if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("CVE_GPO_EMPRESA", request.getParameter("CveGpoEmpresa"));
			parametros.addDefCampo("CVE_APLICACION", request.getParameter("CveAplicacion"));
			parametros.addDefCampo("CVE_PORTAL", request.getParameter("CvePortal"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("HerramientasConfiguracionPortalAplicacion", parametros));
			parametros.addDefCampo("Filtro","Todos");
			registroControl.respuesta.addDefCampo("ListaAplicacionesEmpresa", catalogoSL.getRegistros("HerramientasConfiguracionGrupoEmpresasAplicacion", parametros));
			registroControl.sPagina = "/Aplicaciones/HerramientasConfiguracion/fPortGpoPorAplReg.jsp";
		}

		else if (iTipoOperacion == CON_INICIALIZACION){
			parametros.addDefCampo("CVE_GPO_EMPRESA",request.getParameter("CveGpoEmpresa"));
			parametros.addDefCampo("CVE_PORTAL", request.getParameter("CvePortal"));
			parametros.addDefCampo("Filtro", request.getParameter("Filtro"));
			registroControl.respuesta.addDefCampo("ListaAplicacionesEmpresa", catalogoSL.getRegistros("HerramientasConfiguracionPortalAplicacion", parametros));
			registroControl.sPagina = "/Aplicaciones/HerramientasConfiguracion/fPortGpoPorAplReg.jsp";
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
		LinkedList listaAplicaciones = null;
		
		// SI SE TRATA DE MODIFICAR EL TIPO DE LETRA SE OBTIENE EL ARREGLO DE APLICACIONES
		if(request.getParameter("btnModificarMayusculas")!=null){
			iTipoOperacion = CatalogoControl.CON_MODIFICACION;
		
				//RECUPERA LA LISTA DE LAS APLICACIONES QUE SE VAN A PROCESAR
				
				String sClaveAplicacion = new String();
				Registro registroTipoLetra = null;
					
				//OBTIENE EL ARREGLO CON LAS APLICACIONES A PROCESAR
				String[] sAplicaciones = request.getParameterValues("CveAplicacion");
				//VERIFICA SI ENCONTRO EL ARREGLO DE APLICACIONES
				if (sAplicaciones != null) {
					
					for (int iNumParametro = 0; iNumParametro < sAplicaciones.length; iNumParametro++) {
						//VERIFICA SI LA LISTA DE APLICACIONES ESTA INICIALIZADA
						if (listaAplicaciones == null) {
							listaAplicaciones = new LinkedList();
						}
						//OBTIENE LA CLAVE DE LA APLICACION
						sClaveAplicacion = sAplicaciones[iNumParametro];
						
						//VERIFICA SI DEBE REALIZAR MODIFICACION DE LAS APLICACIONES
						//AGREGA A LA LISTA LA DEFINICION DE LOS PERMISOS PARA UNA APLICACION
							registroTipoLetra  = new Registro();
							registroTipoLetra.addDefCampo("CVE_APLICACION", sClaveAplicacion);
							registroTipoLetra.addDefCampo("CVE_GPO_EMPRESA", request.getParameter("CveGpoEmpresa"));
							registroTipoLetra.addDefCampo("CVE_PORTAL", request.getParameter("CvePortal"));
							if (request.getParameter("Mayusculas" + sClaveAplicacion) != null) {
								registroTipoLetra.addDefCampo("TIPO_LETRA", "MA");
							}
							else {
								registroTipoLetra.addDefCampo("TIPO_LETRA", "DE");
							}
							listaAplicaciones.add(registroTipoLetra);
						}
				}
		}
		else{
			    //SI NO SE VA A MODIFICAR EL TIPO DE LETRA SE TRATA DE UNA BAJA
				//RECUPERA LA LISTA DE LAS FUNCIONES QUE SE VAN A PROCESAR
				String sClaveAplicacion;
				Enumeration lista = request.getParameterNames();
				//RECORRE LA LISTA DE PARAMETROS QUE VIENEN EN EL REQUEST
				while (lista.hasMoreElements()){
					String sNombre = (String)lista.nextElement();
					//VERIFICA SI EL PARAMETRO TIENE EL PREFIJO "Aplicacion"
					if (sNombre.startsWith("Aplicacion")){
						//VERIFICA SI LA LISTA DE FUNCIONES ESTA INICIALIZADA
						if (listaAplicaciones == null){
							listaAplicaciones = new LinkedList();
						}
						sClaveAplicacion = sNombre.substring(10, sNombre.length());
						listaAplicaciones.add(sClaveAplicacion);
					}
					
				}
				registro.addDefCampo("CVE_GPO_EMPRESA",request.getParameter("CveGpoEmpresa"));
				registro.addDefCampo("CVE_PORTAL",request.getParameter("CvePortal"));
		}
		
		//VERIFICA SI SE SELECCIONARON FUNCIONES PARA PROCESAR
		if (listaAplicaciones != null){
			//DESHABILITA EL COMPONENTE DE RE-READ
			registro.addDefCampo("OperacionInfraestructura", "DeshabilitaReRead");
			registro.addDefCampo("ListaAplicaciones", listaAplicaciones);
			
			//DA DE ALTA O BAJA LAS FUNCIONES EN LA BASE DE DATOS O MODIFICA EL TIPO DE LETRA
			registroControl.resultadoCatalogo = catalogoSL.modificacion("HerramientasConfiguracionPortalAplicacion", registro, iTipoOperacion);
			
		}
		else{
			 registroControl.resultadoCatalogo = new ResultadoCatalogo();
		}
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=HerramientasConfiguracionPortal&OperacionCatalogo=CR&CvePortal=" + request.getParameter("CvePortal")+"&CveGpoEmpresa=" + request.getParameter("CveGpoEmpresa")+"&CveAplicacion=" + request.getParameter("CveAplicacion");

		return registroControl;
	}
}
