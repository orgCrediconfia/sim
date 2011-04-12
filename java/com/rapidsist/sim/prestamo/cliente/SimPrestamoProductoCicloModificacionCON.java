/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;

/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificación y consulta) de los préstamos. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimPrestamoProductoCicloModificacionCON implements CatalogoControlActualizaIN{

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
		registro.addDefCampo("ID_PERSONA", request.getParameter("IdPersona") != null ? request.getParameter("IdPersona") : "" );
		registro.addDefCampo("ID_GRUPO", request.getParameter("IdGrupo") != null ? request.getParameter("IdGrupo") : "" );
		registro.addDefCampo("ID_SUCURSAL", request.getParameter("IdSucursal") != null ? request.getParameter("IdSucursal") : "");
		registro.addDefCampo("ID_COMITE", request.getParameter("IdComite") != null ? request.getParameter("IdComite") : "");
		registro.addDefCampo("CVE_ASESOR_CREDITO", request.getParameter("IdAsesorCredito") != null ? request.getParameter("IdAsesorCredito") : "" );
		registro.addDefCampo("FECHA_ASESOR", request.getParameter("FechaAsesor") != null ? request.getParameter("FechaAsesor") : "");
		//registro.addDefCampo("CVE_RECUPERADOR", request.getParameter("IdRecuperador") != null ? request.getParameter("IdRecuperador") : "" );
		//registro.addDefCampo("FECHA_RECUPERADOR", request.getParameter("FechaRecuperador") != null ? request.getParameter("FechaRecuperador") : "");
		registro.addDefCampo("ID_ETAPA_PRESTAMO", request.getParameter("IdEstatusPrestamo") != null ? request.getParameter("IdEstatusPrestamo") : "");
		//registro.addDefCampo("ID_TASA_REFERENCIA", request.getParameter("IdTasaReferencia") != null ? request.getParameter("IdTasaReferencia") : "");
		
		if (request.getParameter("IdTasaReferencia").equals("null")){
			registro.addDefCampo("ID_TASA_REFERENCIA","");
		}else {
			registro.addDefCampo("ID_TASA_REFERENCIA",request.getParameter("IdTasaReferencia"));
		}
		
		//Características del producto-ciclo a modificar
		registro.addDefCampo("ID_PERIODICIDAD_PRODUCTO", request.getParameter("IdPeriodicidadProducto") != null ? request.getParameter("IdPeriodicidadProducto") : "" );
		registro.addDefCampo("CVE_METODO", request.getParameter("CveMetodo") != null ? request.getParameter("CveMetodo") : "" );
		registro.addDefCampo("ID_FORMA_DISTRIBUCION", request.getParameter("IdFormaDistribucion") != null ? request.getParameter("IdFormaDistribucion") : "" );
		registro.addDefCampo("PLAZO", request.getParameter("Plazo") != null ? request.getParameter("Plazo") : "" );
		registro.addDefCampo("TIPO_TASA", request.getParameter("TipoTasa") != null ? request.getParameter("TipoTasa") : "" );
		registro.addDefCampo("VALOR_TASA", request.getParameter("ValorTasa") != null ? request.getParameter("ValorTasa") : "" );
		
		if (request.getParameter("IdPeriodicidadTasa").equals("null")){
			registro.addDefCampo("ID_PERIODICIDAD_TASA","");
		}else {
			registro.addDefCampo("ID_PERIODICIDAD_TASA",request.getParameter("IdPeriodicidadTasa"));
		}
		
		registro.addDefCampo("MONTO_MAXIMO", request.getParameter("MontoMaximo") != null ? request.getParameter("MontoMaximo") : "" );
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoProductoCicloModificacion", registro, iTipoOperacion);
		
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimPrestamo&OperacionCatalogo=CR&IdPrestamo="+request.getParameter("IdPrestamo")+"&Alta=No";
		return registroControl;
	}
}