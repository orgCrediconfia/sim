/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.cliente;

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
 * modificación y consulta) de los productos asigandos a los préstamos. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimPrestamoProductoCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			if (request.getParameter("IdPrestamo") != null && !request.getParameter("IdPrestamo").equals("")){
				parametros.addDefCampo("ID_PRESTAMO", request.getParameter("IdPrestamo"));
			}
			if (request.getParameter("IdProducto") != null && !request.getParameter("IdProducto").equals("")){
				parametros.addDefCampo("ID_PRODUCTO", request.getParameter("IdProducto"));
			}
			if (request.getParameter("NumCiclo") != null && !request.getParameter("NumCiclo").equals("")){
				parametros.addDefCampo("NUM_CICLO", request.getParameter("NumCiclo"));
			}
			if (request.getParameter("FechaInicioSolicitud") != null && !request.getParameter("FechaInicioSolicitud").equals("")){
				parametros.addDefCampo("FECHA_INICIO_SOLICITUD", request.getParameter("FechaInicioSolicitud"));
			}
			if (request.getParameter("FechaFinEntrega") != null && !request.getParameter("FechaFinEntrega").equals("")){
				parametros.addDefCampo("FECHA_FIN_ENTREGA", request.getParameter("FechaFinEntrega"));
			}
			if (request.getParameter("NomGrupo") != null && !request.getParameter("NomGrupo").equals("")){
				parametros.addDefCampo("NOM_GRUPO", request.getParameter("NomGrupo"));
			}
			if (request.getParameter("NomCompleto") != null && !request.getParameter("NomCompleto").equals("")){
				parametros.addDefCampo("NOM_COMPLETO", request.getParameter("NomCompleto"));
			}			
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimPrestamo", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreCon.jsp";
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("ID_PRESTAMO",request.getParameter("IdPrestamo"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimPrestamo", parametros));
			registroControl.respuesta.addDefCampo("ListaComite", catalogoSL.getRegistros("SimComite", parametros));
			registroControl.respuesta.addDefCampo("ListaVerificacionPrestamo", catalogoSL.getRegistros("SimCatalogoEstatusPrestamo", parametros));
			registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
			registroControl.respuesta.addDefCampo("ListaPapel", catalogoSL.getRegistros("SimCatalogoPapel", parametros));
			registroControl.respuesta.addDefCampo("ListaMetodo", catalogoSL.getRegistros("SimCatalogoMetodoCalculo", parametros));
			//VERIFICA SI SOLO CONSULTA LOS DATOS DEL REGISTRO
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreReg.jsp";
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Alta")){
				registroControl.respuesta.addDefCampo("ListaComite", catalogoSL.getRegistros("SimComite", parametros));
				registroControl.respuesta.addDefCampo("ListaVerificacionPrestamo", catalogoSL.getRegistros("SimCatalogoEstatusPrestamo", parametros));
				registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
				registroControl.respuesta.addDefCampo("ListaPapel", catalogoSL.getRegistros("SimCatalogoPapel", parametros));
				registroControl.respuesta.addDefCampo("ListaMetodo", catalogoSL.getRegistros("SimCatalogoMetodoCalculo", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreReg.jsp";
			}else if (request.getParameter("Filtro").equals("Inicio")){
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreCon.jsp";
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

		//OBTIENE PARAMETROS DE PERSONA
		registro.addDefCampo("ID_PRESTAMO", request.getParameter("IdPrestamo"));
		registro.addDefCampo("ID_GRUPO", request.getParameter("IdGrupo"));
		registro.addDefCampo("ID_PRODUCTO", request.getParameter("IdProducto") != null ? request.getParameter("IdProducto") : "" );
		registro.addDefCampo("DIA_SEMANA_PAGO", request.getParameter("DiaSemanaPago") != null ? request.getParameter("DiaSemanaPago") : "");
		registro.addDefCampo("FECHA_REAL", request.getParameter("FechaReal") != null ? request.getParameter("FechaReal") : "" );
		registro.addDefCampo("FECHA_ENTREGA", request.getParameter("Fecha") != null ? request.getParameter("Fecha") : "");
		registro.addDefCampo("ESTATUS_PRESTAMO", request.getParameter("IdEstatusPrestamo") != null ? request.getParameter("IdEstatusPrestamo") : "");
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoProducto", registro, iTipoOperacion);
		
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimPrestamo&OperacionCatalogo=CT&Filtro=Todos";
		return registroControl;
	}
}