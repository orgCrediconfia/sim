/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.producto.cliente;

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
import java.util.Enumeration;
import java.util.LinkedList;
import javax.servlet.ServletContext;

/**
 * Esta clase se encarga de administrar los servicios de operación (alta, baja,
 * modificación y consulta) para controlar las actividades que se agregan al producto. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimProductoActividadRequisitoCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			if (request.getParameter("IdActividadRequisito") != "" && !request.getParameter("IdActividadRequisito").equals("")){
				parametros.addDefCampo("ID_ACTIVIDAD_REQUISITO", request.getParameter("IdActividadRequisito"));	
			}
			
			if (request.getParameter("NomActividadRequisito") != "" && !request.getParameter("NomActividadRequisito").equals("")){	
				parametros.addDefCampo("NOM_ACTIVIDAD_REQUISITO", request.getParameter("NomActividadRequisito"));
			}
			
			if (request.getParameter("AplicaA") != "" && !request.getParameter("AplicaA").equals("")){	
				parametros.addDefCampo("APLICA_A", request.getParameter("AplicaA"));
			}
			
			if (request.getParameter("Tipo") != "null" && !request.getParameter("Tipo").equals("null")){	
				parametros.addDefCampo("TIPO", request.getParameter("Tipo"));
			}
			
			if (request.getParameter("AplicaCiclo") != "null" && !request.getParameter("AplicaCiclo").equals("null")){	
				parametros.addDefCampo("APLICA_CICLO", request.getParameter("AplicaCiclo"));
			}
		
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			parametros.addDefCampo("ID_PRODUCTO", request.getParameter("IdProducto"));	
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimProductoActividadRequisitoDisponible", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Producto/fSimProActReqCon.jsp";
		}
		
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("ID_PRODUCTO",request.getParameter("IdProducto"));
			parametros.addDefCampo("ID_ACTIVIDAD_REQUISITO",request.getParameter("IdActividadRequisito"));
			parametros.addDefCampo("ID_ETAPA_PRESTAMO",request.getParameter("IdEstatusPrestamo"));
			registroControl.respuesta.addDefCampo("ListaEstatusPrestamo", catalogoSL.getRegistros("SimCatalogoEstatusPrestamo", parametros));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimProductoActividadRequisito", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Producto/fSimProActReqReg.jsp";
		}
		
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Alta")){
				registroControl.respuesta.addDefCampo("ListaEstatusPrestamo", catalogoSL.getRegistros("SimCatalogoEstatusPrestamo", parametros));
				registroControl.respuesta.addDefCampo("ListaActividadRequisito", catalogoSL.getRegistros("SimCatalogoActividadRequisito", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Producto/fSimProActReq.jsp?IdProducto="+request.getParameter("IdProducto")+"&AplicaA="+request.getParameter("AplicaA");		
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
		
		if (iTipoOperacion == 1){
			String sOrdenEtapa = "";
			String sEtapaPrestamo = "";
			
			String[] sOrden = request.getParameterValues("Orden");
			String[] sEtapa = request.getParameterValues("Etapa");
			
			sOrdenEtapa = sOrden[0];
			sEtapaPrestamo = sEtapa[0];
			
			registro.addDefCampo("ORDEN_ETAPA", sOrdenEtapa);
			registro.addDefCampo("ID_ETAPA_PRESTAMO", sEtapaPrestamo);
			
			registro.addDefCampo("ID_PRODUCTO", request.getParameter("IdProducto"));
			
			//RECUPERA LA LISTA DE LAS FUNCIONES QUE SE VAN A PROCESAR
			String sIdActividad;
			Enumeration lista = request.getParameterNames();
			//RECORRE LA LISTA DE PARAMETROS QUE VIENEN EN EL REQUEST
			while (lista.hasMoreElements()){
				String sNombre = (String)lista.nextElement();
				//VERIFICA SI EL PARAMETRO TIENE EL PREFIJO "FuncionAlta"
				if (sNombre.startsWith("FuncionAlta")){
					sIdActividad = sNombre.substring(11, sNombre.length());
					registro.addDefCampo("ID_ACTIVIDAD_REQUISITO", sIdActividad);
					
					//DA DE ALTA O BAJA LAS FUNCIONES EN LA BASE DE DATOS
					registroControl.resultadoCatalogo = catalogoSL.modificacion("SimProductoActividadRequisito", registro, iTipoOperacion);	
				}
			}
		}else if (iTipoOperacion == 2){
			registro.addDefCampo("ID_PRODUCTO",request.getParameter("IdProducto"));
			registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",request.getParameter("IdActividadRequisito"));
			registro.addDefCampo("ID_ETAPA_PRESTAMO",request.getParameter("IdEstatusPrestamo"));
			registroControl.resultadoCatalogo = catalogoSL.modificacion("SimProductoActividadRequisito", registro, iTipoOperacion);
		}
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS		
		
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimProducto&OperacionCatalogo=CR&IdProducto="+request.getParameter("IdProducto");

		return registroControl;
	}
}
