/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.cliente;

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

/**
 * Esta clase se encarga de administrar los servicios de detalle operación (alta, baja,
 * modificación y consulta) de los cargo y comisiones del préstamo. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimPrestamoGrupalCargoComisionCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
	 * @throws RemoteException Si se generá un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se generá un error dentro de la clase CON.
	 */
	public RegistroControl consulta(Registro parametros, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		//OBTIENE SOLO EL REGISTRO SOLICITADO
		if (iTipoOperacion == CON_CONSULTA_TABLA){
			parametros.addDefCampo("ID_PRESTAMO",request.getParameter("IdPrestamo"));
			parametros.addDefCampo("ID_CLIENTE",request.getParameter("IdCliente"));
			parametros.addDefCampo("ID_PERSONA",request.getParameter("IdCliente"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimClientes", parametros));
			registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
			registroControl.respuesta.addDefCampo("ListaUnidad", catalogoSL.getRegistros("SimCatalogoUnidad", parametros));
			registroControl.respuesta.addDefCampo("ListaFormaAplicacion", catalogoSL.getRegistros("SimCatalogoFormaAplicacion", parametros));
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimPrestamoCargoComisionCliente", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreCarComCon.jsp?IdPrestamo="+request.getParameter("IdPrestamo");	
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//RECUPERA CAMPOS
			parametros.addDefCampo("ID_PRODUCTO",request.getParameter("IdProducto"));
			parametros.addDefCampo("ID_PRESTAMO",request.getParameter("IdPrestamo"));
			parametros.addDefCampo("ID_ACTIVIDAD_REQUISITO",request.getParameter("IdActividadRequisito"));
			registroControl.respuesta.addDefCampo("ListaEstatusPrestamo", catalogoSL.getRegistros("SimCatalogoEstatusPrestamo", parametros));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimPrestamoActividadRequisito", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreActReqReg.jsp?IdPrestamo="+request.getParameter("IdPrestamo")+"&IdProducto="+request.getParameter("IdProducto");
		}
		else if (iTipoOperacion == CatalogoControl.CON_INICIALIZACION){
			//RECUPERA CAMPOS
			if (request.getParameter("Filtro").equals("Alta")){
				parametros.addDefCampo("ID_PERSONA",request.getParameter("IdPersona"));
				registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Personas/fSimCliAdeReg.jsp?IdPersona="+request.getParameter("IdPersona");	
			}
		}
		return registroControl;
	}

	/**
	 * Valida los párametros entrada y ejecuta los servicios de alta, baja o cambio.
	 * @param registro Parametros que se recogen de la sesion del usuario y se le envian a la clase CON.
	 * Estos párametros son: CVE_GPO_EMPRESA (Clave del grupo empresa), CVE_USUARIO_BITACORA (clave
	 * del usuario que realiza la operacion), RegistroOriginal (registro leido originalmente y
	 * se utiliza cuando se ejecuta la Operación de modificacion y se verifica que no se hallan
	 * realizado modificaciones al registro).
	 * @param request Objeto que provee de información al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envía como un párametro a este método.
	 * @param response Objeto que provee de información del servlet sobre el response del cliente. El
	 * contenedor de servlets crea un objeto HttpServletResponse y lo envía como un párametro a este método.
	 * @param config Objeto que provee de información del servlet sobre el ServletConfig del cliente. El
	 * contenedor de servlets crea un objeto ServletConfig y lo envía como un párametro a este método.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON
	 * @param contexto Objeto que contiene información acerca del entorno del servidor de
	 * aplicaciones.
	 * @param iTipoOperacion Operación que debe ejecutar la clase CON. Las operaciones se encuentran
	 * especificadas en la clase {@link com.rapidsist.portal.cliente.CatalogoControl CatalogoControl}
	 * @return Respuesta del servicio de alta, baja o cambio y la página a donde
	 * se redirecciona el control.
	 * @throws RemoteException Si se generá un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se generá un error dentro de la clase CON.
	 */
	public RegistroControl actualiza(Registro registro, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		
		String sCargoC = new String();
		String sForma = new String();
		String sCargoI = new String();
		String sPorcentaje = new String();
		String sCantidad = new String();
		String sValorT = new String();
		String sUnidad = new String();
		String sPeriodicidad = new String();

		//OBTIENE EL ARREGLO CON LAS APLICACIONES A PROCESAR
		String[] sIdCargoComision = request.getParameterValues("IdCargoComision");
		
		String[] sIdFormaAplicacion = request.getParameterValues("IdFormaAplicacion");
		
		String[] sCargoInicial = request.getParameterValues("CargoInicial");
		
		String[] sPorcentajeMonto = request.getParameterValues("PorcentajeMonto");
		
		String[] sCantidadFija = request.getParameterValues("CantidadFija");
	
		String[] sValor = request.getParameterValues("Valor");
		
		String[] sIdUnidad = request.getParameterValues("IdUnidad");
		
		String[] sIdPeriodicidad = request.getParameterValues("IdPeriodicidad");
		
		//VERIFICA SI ENCONTRO EL ARREGLO DE APLICACIONES
		if (sIdCargoComision != null) {
			
			for (int iNumParametro = 0; iNumParametro < sIdCargoComision.length; iNumParametro++) {
				
				//OBTIENE LA CLAVE DE LA APLICACION
				sCargoC = sIdCargoComision[iNumParametro];
				
				sForma = sIdFormaAplicacion[iNumParametro];
			
				sCargoI = sCargoInicial[iNumParametro];
				
				sPorcentaje = sPorcentajeMonto[iNumParametro];
				
				sCantidad = sCantidadFija[iNumParametro];
				
				sValorT = sValor[iNumParametro];
				
				sUnidad= sIdUnidad[iNumParametro];
			
				sPeriodicidad = sIdPeriodicidad[iNumParametro];
				
				registro.addDefCampo("ID_CARGO_COMISION",sCargoC == null ? "" : sCargoC);
				
				registro.addDefCampo("ID_FORMA_APLICACION",sForma == null ? "" : sForma);
				
				registro.addDefCampo("CARGO_INICIAL",sCargoI == null ? "" : sCargoI);
				
				registro.addDefCampo("PORCENTAJE_MONTO",sPorcentaje == null ? "" : sPorcentaje);
			
				registro.addDefCampo("CANTIDAD_FIJA",sCantidad == null ? "" : sCantidad);
				
				registro.addDefCampo("VALOR",sValorT == null ? "" : sValorT);
				
				
				if (sUnidad.equals("null")){
				
					registro.addDefCampo("ID_UNIDAD","");
				}else {
					
					registro.addDefCampo("ID_UNIDAD",sUnidad);
				}
				
				if (sPeriodicidad.equals("null")){
					
					registro.addDefCampo("ID_PERIODICIDAD","");
				}else {
				
					registro.addDefCampo("ID_PERIODICIDAD",sPeriodicidad);
				}				
				
				registro.addDefCampo("ID_PRESTAMO_GRUPO", request.getParameter("IdPrestamoGrupo"));
				
				//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoGrupalCargoComision", registro, 1);
				
			}
		}
		
		//INDICA A DONDE IRA AL TERMINAR LA ACTUALIZACION
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimPrestamoGrupal&OperacionCatalogo=CR&IdPrestamoGrupo="+request.getParameter("IdPrestamoGrupo")+"&IdGrupo="+request.getParameter("IdGrupo")+"&IdProducto="+request.getParameter("IdProducto")+"&Filtro=Modificacion";
		
		return registroControl;
	}
}
