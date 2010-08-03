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

/**
 * Esta clase se encarga de administrar los servicios de operación (alta, baja,
 * modificación y consulta) de los ciclo del producto. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimProductoCicloCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			
			//VERIFICA SI SE ENVIO EL PARAMETRO CLAVE DEL PRODUCTO	
			if (request.getParameter("IdProducto") != "" && !request.getParameter("IdProducto").equals("")){
				parametros.addDefCampo("ID_PRODUCTO", request.getParameter("IdProducto"));	
			}
			
			//VERIFICA SI SE ENVIO EL PARAMETRO NOMBRE DEL PRODUCTO
			if (request.getParameter("NomProducto") != "" && !request.getParameter("NomProducto").equals("")){	
				parametros.addDefCampo("NOM_PRODUCTO", request.getParameter("NomProducto"));
			}
			
			//VERIFICA SI SE ENVIO EL PARAMETRO FECHA INICIO DE ACTIVACION
			if (request.getParameter("FechaInicioActivacion") != "" && !request.getParameter("FechaInicioActivacion").equals("")){	
				parametros.addDefCampo("FECHA_INICIO_ACTIVACION", request.getParameter("FechaInicioActivacion"));
			}
			
			//VERIFICA SI SE ENVIO EL PARAMETRO FECHA FIN DE ACTIVACION
			if (request.getParameter("FechaFinActivacion") != "" && !request.getParameter("FechaFinActivacion").equals("")){	
				parametros.addDefCampo("FECHA_FIN_ACTIVACION", request.getParameter("FechaFinActivacion"));
			}
	
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimProducto", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Producto/fSimProCon.jsp";
		}
		
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			parametros.addDefCampo("ID_PRODUCTO",request.getParameter("IdProducto"));
			parametros.addDefCampo("NUM_CICLO",request.getParameter("NumCiclo"));
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimProductoCiclo", parametros));
			registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
			registroControl.respuesta.addDefCampo("ListaPapel", catalogoSL.getRegistros("SimCatalogoPapel", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Producto/fSimProCicReg.jsp";
		}
		
		if (iTipoOperacion == CON_INICIALIZACION){
			registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
			registroControl.respuesta.addDefCampo("ListaPapel", catalogoSL.getRegistros("SimCatalogoPapel", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Producto/fSimProCicReg.jsp?IdProducto="+request.getParameter("IdProducto");		
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
		
		registro.addDefCampo("ID_PRODUCTO",request.getParameter("IdProducto"));
		registro.addDefCampo("ID_FORMA_DISTRIBUCION",request.getParameter("FormaDistribucionPago"));
		registro.addDefCampo("NUM_CICLO",request.getParameter("NumCiclo") == null ? "" : request.getParameter("NumCiclo"));
		registro.addDefCampo("PLAZO",request.getParameter("Plazo") == null ? "" : request.getParameter("Plazo"));
		registro.addDefCampo("TIPO_TASA",request.getParameter("TipoTasa"));
		registro.addDefCampo("ID_PERIODICIDAD_TASA",request.getParameter("IdPeriodicidadTasa") == null ? "" : request.getParameter("IdPeriodicidadTasa"));
		registro.addDefCampo("VALOR_TASA",request.getParameter("ValorTasa") == null ? "" : request.getParameter("ValorTasa"));
		registro.addDefCampo("ID_TASA_REFERENCIA",request.getParameter("IdTasaReferencia") == null ? "" : request.getParameter("IdTasaReferencia"));
		registro.addDefCampo("MONTO_MAXIMO",request.getParameter("MontoMaximo") == null ? "" : request.getParameter("MontoMaximo"));
		registro.addDefCampo("PORC_FLUJO_CAJA",request.getParameter("PorcFlujoCaja") == null ? "" : request.getParameter("PorcFlujoCaja"));
		
		registro.addDefCampo("ID_TASA_REFERENCIA_RECARGO", request.getParameter("IdTasaReferenciaRecargo") != null ? request.getParameter("IdTasaReferenciaRecargo") : "");
		
		if (request.getParameter("TipoRecargo").equals("null")){
			registro.addDefCampo("ID_TIPO_RECARGO","");
		}else {
			registro.addDefCampo("ID_TIPO_RECARGO",request.getParameter("TipoRecargo"));
		}
		
		if (request.getParameter("TipoTasaRecargo").equals("null")){
			registro.addDefCampo("TIPO_TASA_RECARGO","");
		}else {
			registro.addDefCampo("TIPO_TASA_RECARGO",request.getParameter("TipoTasaRecargo"));
		}

		if (request.getParameter("IdPeriodicidadTasaRecargo").equals("null")){
			registro.addDefCampo("ID_PERIODICIDAD_TASA_RECARGO","");
		}else {
			registro.addDefCampo("ID_PERIODICIDAD_TASA_RECARGO",request.getParameter("IdPeriodicidadTasaRecargo"));
		}
		
		registro.addDefCampo("TASA_RECARGO",request.getParameter("TasaRecargo") == null ? "" : request.getParameter("TasaRecargo"));
		
		if (request.getParameter("IdTasaReferenciaRecargo").equals("null")){
			registro.addDefCampo("ID_TASA_REFERENCIA_RECARGO","");
		}else {
			registro.addDefCampo("ID_TASA_REFERENCIA_RECARGO",request.getParameter("IdTasaReferenciaRecargo") == null ? "" : request.getParameter("IdTasaReferenciaRecargo"));
		}
		
		registro.addDefCampo("FACTOR_TASA_RECARGO",request.getParameter("FactorTasaRecargo") == null ? "" : request.getParameter("FactorTasaRecargo"));
		registro.addDefCampo("MONTO_FIJO_PERIODO",request.getParameter("MontoFijoPeriodo") == null ? "" : request.getParameter("MontoFijoPeriodo"));
		
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS		
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimProductoCiclo", registro, iTipoOperacion);
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimProducto&OperacionCatalogo=CR&IdProducto="+request.getParameter("IdProducto");

		return registroControl;
	}
}
