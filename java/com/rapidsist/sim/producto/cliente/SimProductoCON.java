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
 * modificación y consulta) del catálogo de producto. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimProductoCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			parametros.addDefCampo("Filtro","");
			registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimProducto", parametros));
			registroControl.respuesta.addDefCampo("ListaMetodo", catalogoSL.getRegistros("SimCatalogoMetodoCalculo", parametros));
			registroControl.respuesta.addDefCampo("ListaCiclo", catalogoSL.getRegistros("SimProductoCiclo", parametros));
			parametros.addDefCampo("PARTICIPANTES","ASIGNADOS");
			registroControl.respuesta.addDefCampo("ListaParticipantes", catalogoSL.getRegistros("SimProductoParticipantes", parametros));
			parametros.addDefCampo("CONSULTA","ASIGNADAS");
			registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimProductoSucursal", parametros));
			registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
			registroControl.respuesta.addDefCampo("ListaPapel", catalogoSL.getRegistros("SimCatalogoPapel", parametros));
			registroControl.respuesta.addDefCampo("ListaCicloAccesorio", catalogoSL.getRegistros("SimProductoCicloAccesorio", parametros));
			registroControl.respuesta.addDefCampo("ListaCargoComision", catalogoSL.getRegistros("SimProductoCargoComision", parametros));
			registroControl.respuesta.addDefCampo("ListaActividadRequisito", catalogoSL.getRegistros("SimProductoActividadRequisito", parametros));
			registroControl.respuesta.addDefCampo("ListaDocumentacion", catalogoSL.getRegistros("SimProductoDocumentacion", parametros));
			registroControl.respuesta.addDefCampo("ListaArchivo", catalogoSL.getRegistros("SimProductoFlujoEfectivo", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Producto/fSimProReg.jsp";
		}
		
		if (iTipoOperacion == CON_INICIALIZACION){
				if (request.getParameter("Filtro").equals("Alta")){
					registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
					registroControl.respuesta.addDefCampo("ListaMetodo", catalogoSL.getRegistros("SimCatalogoMetodoCalculo", parametros));
					registroControl.sPagina = "/Aplicaciones/Sim/Producto/fSimProReg.jsp";
				}else if (request.getParameter("Filtro").equals("Inicio")){
					registroControl.sPagina = "/Aplicaciones/Sim/Producto/fSimProCon.jsp";
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
		
		registro.addDefCampo("ID_PRODUCTO",request.getParameter("IdProducto"));
		registro.addDefCampo("NOM_PRODUCTO",request.getParameter("NomProducto"));
		registro.addDefCampo("APLICA_A",request.getParameter("AplicaA"));
		registro.addDefCampo("CVE_METODO",request.getParameter("CveMetodo"));
		registro.addDefCampo("ID_PERIODICIDAD",request.getParameter("IdPeriodicidad"));
		registro.addDefCampo("MONTO_MINIMO",request.getParameter("MontoMinimo"));
		registro.addDefCampo("FECHA_INICIO_ACTIVACION",request.getParameter("FechaInicioActivacion"));
		registro.addDefCampo("FECHA_FIN_ACTIVACION",request.getParameter("FechaFinActivacion"));
		registro.addDefCampo("FORMA_ENTREGA",request.getParameter("FormaEntrega"));
		registro.addDefCampo("ID_TIPO_RECARGO",request.getParameter("TipoRecargo") == null ? "" : request.getParameter("TipoRecargo"));
		registro.addDefCampo("ID_PERIODICIDAD_TASA",request.getParameter("IdPeriodicidadTasa") == null ? "" : request.getParameter("IdPeriodicidadTasa"));
		registro.addDefCampo("TIPO_TASA",request.getParameter("TipoTasa") == null ? "" : request.getParameter("TipoTasa"));
		registro.addDefCampo("TASA_RECARGO",request.getParameter("TasaRecargo") == null ? "" : request.getParameter("TasaRecargo"));
		registro.addDefCampo("ID_TASA_REFERENCIA",request.getParameter("IdPapel")  == null ? "" : request.getParameter("IdPapel"));
		registro.addDefCampo("NUM_VECES",request.getParameter("NumVeces")  == null ? "" : request.getParameter("NumVeces"));
		registro.addDefCampo("FACTOR_TASA",request.getParameter("FactorTasa") == null ? "" : request.getParameter("FactorTasa"));
		registro.addDefCampo("MONTO_FIJO_PERIODO",request.getParameter("MontoFijoPeriodo") == null ? "" : request.getParameter("MontoFijoPeriodo"));
		
		if(request.getParameter("GarantiaNo")!=null){
			registro.addDefCampo("B_GARANTIA","F");
		}else{
			registro.addDefCampo("B_GARANTIA","V");
		}

		if(request.getParameter("GarantiaSi")!=null){
			registro.addDefCampo("B_GARANTIA","V");
		}else{
			registro.addDefCampo("B_GARANTIA","F");
		}
		
		if(request.getParameter("BSucursales")!=null){
			registro.addDefCampo("B_SUCURSALES","V");
		}else{
			registro.addDefCampo("B_SUCURSALES","F");
		}
		
		//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS		
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimProducto", registro, iTipoOperacion);
		
		String sIdProducto = "";
		String sTodasSucursales = "";
		
		if (iTipoOperacion == 1){
			sIdProducto = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("ID_PRODUCTO");
			sTodasSucursales = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("TODAS_SUCURSALES");
			if (sTodasSucursales.equals("V")){
				registro.addDefCampo("ID_PRODUCTO",sIdProducto);
				// SE ASIGNAN TODAS LAS SUCURSALES AL PRODUCTO ("TODAS LAS SUCURSALES" SON LAS SUCURSALES QUE TIENE EL USUARIO.
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimProductoSucursalTodas", registro, iTipoOperacion); //ALTA
			}		
		}else if (iTipoOperacion == 3){
			sTodasSucursales = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("TODAS_SUCURSALES");
			sIdProducto = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("ID_PRODUCTO");
			
			registro.addDefCampo("ID_PRODUCTO",sIdProducto);
			if (sTodasSucursales.equals("V")){
				registro.addDefCampo("SUCURSALES","SI");
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimProductoSucursalTodas", registro, iTipoOperacion); 
			}else if (sTodasSucursales.equals("F")){
				registro.addDefCampo("SUCURSALES","NO");
				registroControl.resultadoCatalogo = catalogoSL.modificacion("SimProductoSucursalTodas", registro, iTipoOperacion); 
			}
		}
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimProducto&OperacionCatalogo=CR&IdProducto="+sIdProducto;
		return registroControl;
	}
}
