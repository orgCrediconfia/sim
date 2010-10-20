/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;



/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificación y consulta) de la paginación en los préstamos grupales. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimPrestamoGrupalPaginacionCON implements CatalogoControlConsultaIN {

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
			if (request.getParameter("CvePrestamo") != null && !request.getParameter("CvePrestamo").equals("")){
				parametros.addDefCampo("CVE_PRESTAMO", request.getParameter("CvePrestamo"));
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
			if (request.getParameter("IdEstatusPrestamo") != null && !request.getParameter("IdEstatusPrestamo").equals("null")){
				parametros.addDefCampo("ID_ETAPA_PRESTAMO", request.getParameter("IdEstatusPrestamo"));
			}
			
			String sPagina = request.getParameter("Paginas");
			int iPagina = Integer.parseInt(sPagina);
			iPagina --;
			String sPaginas = String.valueOf(iPagina);
			String sSuperior = request.getParameter("Superior");
			int iSuperior = Integer.parseInt(sSuperior);
			int iInferior = iSuperior;
			iSuperior = iSuperior + 100;
			String sInferior = String.valueOf(iInferior);
			sSuperior = String.valueOf(iSuperior);
			
			parametros.addDefCampo("SUPERIOR", sSuperior);
			parametros.addDefCampo("INFERIOR", sInferior);
			
			registroControl.respuesta.addDefCampo("ListaPrestamoGrupalPaginacion", catalogoSL.getRegistros("SimPrestamoGrupalPaginacion", parametros));
			
			registroControl.respuesta.addDefCampo("ListaVerificacionPrestamo", catalogoSL.getRegistros("SimCatalogoEstatusPrestamo", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreGpoPagCon.jsp?Paginas="+sPaginas+"&Superior="+sSuperior+"&CvePrestamo="+request.getParameter("CvePrestamo")+"&IdProducto="+request.getParameter("IdProducto")+"&NumCiclo="+request.getParameter("NumCiclo")+"&FechaInicioSolicitud="+request.getParameter("FechaInicioSolicitud")+"&FechaFinEntrega="+request.getParameter("FechaFinEntrega")+"&NomGrupo="+request.getParameter("NomGrupo")+"&IdEstatusPrestamo="+request.getParameter("IdEstatusPrestamo");
		}
			
		return registroControl;
	}
}