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
 * modificación y consulta) de los préstamos grupales. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimPrestamoGrupalCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

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
			
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimPrestamoGrupal", parametros));
			
			//CUENTA CUANTAS PAGINAS SERÁN
			Registro paginas = new Registro ();
			paginas = catalogoSL.getRegistro("SimPrestamoGrupalPaginacion", parametros);
			String sPaginas = (String)paginas.getDefCampo("PAGINAS");
			
			System.out.println("sPaginas*******"+sPaginas);
			
			
			registroControl.respuesta.addDefCampo("ListaVerificacionPrestamo", catalogoSL.getRegistros("SimCatalogoEstatusPrestamo", parametros));
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreGpoCon.jsp?Paginas="+sPaginas+"&Superior="+100+"&CvePrestamo="+request.getParameter("CvePrestamo")+"&IdProducto="+request.getParameter("IdProducto")+"&NumCiclo="+request.getParameter("NumCiclo")+"&FechaInicioSolicitud="+request.getParameter("FechaInicioSolicitud")+"&FechaFinEntrega="+request.getParameter("FechaFinEntrega")+"&NomGrupo="+request.getParameter("NomGrupo")+"&IdEstatusPrestamo="+request.getParameter("IdEstatusPrestamo");
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			//OBTIENE SOLO EL REGISTRO SOLICITADO
			if (request.getParameter("Filtro").equals("Alta")){
				
				String sCiclo = "igual";
				
				parametros.addDefCampo("ID_PRODUCTO",request.getParameter("IdProducto"));
				parametros.addDefCampo("ID_GRUPO",request.getParameter("IdGrupo"));
				parametros.addDefCampo("NUM_CICLO",request.getParameter("NumCiclo"));
				
				if (request.getParameter("BExisteCicloSig").equals("F")){
					parametros.addDefCampo("PRESTAMO","GRUPAL");
					
					registroControl.respuesta.addDefCampo("registroCiclo", catalogoSL.getRegistro("SimPrestamoCicloSiguiente", parametros));
					
					Registro ciclo = new Registro();
				
					ciclo = catalogoSL.getRegistro("SimPrestamoCicloSiguiente", parametros);
					
					sCiclo = (String) ciclo.getDefCampo("NUMERO_CICLO");
					
				}
				
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimProductoCiclo", parametros));
				
				registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
				
				registroControl.respuesta.addDefCampo("ListaCargoComision", catalogoSL.getRegistros("SimProductoCargoComision", parametros));
				parametros.addDefCampo("PANTALLA","ALTA");
				
				registroControl.respuesta.addDefCampo("ListaMontoEtapa", catalogoSL.getRegistros("SimPrestamoGrupalMontoEtapa", parametros));
			
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreGpoMonEta.jsp?Ciclo="+sCiclo;
			}else if (request.getParameter("Filtro").equals("Modificacion")){
				
				parametros.addDefCampo("ID_PRODUCTO",request.getParameter("IdProducto"));
				parametros.addDefCampo("ID_PRESTAMO_GRUPO",request.getParameter("IdPrestamoGrupo"));
				parametros.addDefCampo("NUM_CICLO",request.getParameter("NumCiclo"));
				registroControl.respuesta.addDefCampo("ListaUnidad", catalogoSL.getRegistros("SimCatalogoUnidad", parametros));
				registroControl.respuesta.addDefCampo("ListaFormaAplicacion", catalogoSL.getRegistros("SimCatalogoFormaAplicacion", parametros));
				registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
				registroControl.respuesta.addDefCampo("ListaPapel", catalogoSL.getRegistros("SimCatalogoPapel", parametros));
				registroControl.respuesta.addDefCampo("registroEtapaGrupal", catalogoSL.getRegistro("SimPrestamoGrupalEtapaGrupal", parametros));
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimPrestamoGrupal", parametros));
				registroControl.respuesta.addDefCampo("ListaCargoComision", catalogoSL.getRegistros("SimPrestamoGrupalCargoComision", parametros));
				registroControl.respuesta.addDefCampo("ListaDocumentoImprimir", catalogoSL.getRegistros("SimPrestamoDocumentoImprimir", parametros));
				parametros.addDefCampo("PANTALLA","MODIFICACION");
				registroControl.respuesta.addDefCampo("ListaMontoEtapa", catalogoSL.getRegistros("SimPrestamoGrupalMontoEtapa", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreGpoMonEtaMod.jsp";
			}
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Alta")){
				registroControl.respuesta.addDefCampo("ListaProducto", catalogoSL.getRegistros("SimAsignaProducto", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreGpoReg.jsp";
			}else if (request.getParameter("Filtro").equals("Inicio")){
				registroControl.respuesta.addDefCampo("ListaVerificacionPrestamo", catalogoSL.getRegistros("SimCatalogoEstatusPrestamo", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreGpoCon.jsp";
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

		String sNumCiclo = "";
		String sBExisteCicloSig = "";
		registro.addDefCampo("ID_PRODUCTO",request.getParameter("IdProducto"));
		
		if (iTipoOperacion == 1){
			
			registro.addDefCampo("ID_GRUPO",request.getParameter("IdGrupo"));
			
			//ACTUALIZA EL REGISTRO EN LA BASE DE DATOS
			registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoGrupal", registro, iTipoOperacion);
			
			sNumCiclo = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("NUM_CICLO");
			System.out.println("sNumCiclo"+sNumCiclo);
			sBExisteCicloSig = (String) registroControl.resultadoCatalogo.Resultado.getDefCampo("B_EXISTE_CICLO_SIG");
			
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimPrestamoGrupal&OperacionCatalogo=CR&BExisteCicloSig="+sBExisteCicloSig+"&IdProducto="+request.getParameter("IdProducto")+"&IdGrupo="+request.getParameter("IdGrupo")+"&NumCiclo="+sNumCiclo+"&Filtro=Alta";
			
		}else if (iTipoOperacion == 3){
			sNumCiclo = request.getParameter("NumCiclo");
			registro.addDefCampo("ID_PRESTAMO_GRUPO",request.getParameter("IdPrestamoGrupo"));
			registro.addDefCampo("NUM_CICLO",request.getParameter("NumCiclo"));
			registro.addDefCampo("PLAZO",request.getParameter("Plazo"));
			registro.addDefCampo("ID_PERIODICIDAD",request.getParameter("IdPeriodicidad"));
			registro.addDefCampo("VALOR_TASA",request.getParameter("ValorTasa"));
			registro.addDefCampo("ID_PERIODICIDAD_TASA",request.getParameter("IdPeriodicidadTasa"));
			
			if (request.getParameter("IdTasaReferencia").equals("hola")){
				registro.addDefCampo("ID_TASA_REFERENCIA","");
			}else {
				registro.addDefCampo("ID_TASA_REFERENCIA",request.getParameter("IdTasaReferencia") == null ? "" : request.getParameter("IdTasaReferencia"));
				registro.addDefCampo("VALOR_TASA","");
				registro.addDefCampo("ID_PERIODICIDAD_TASA","");
			}
			
			
			registro.addDefCampo("MONTO_MAXIMO",request.getParameter("MontoMaximo"));

			//Datos de los recargos para hacer a modificación.
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
			registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoGrupal", registro, iTipoOperacion);
			
			registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimPrestamoGrupal&OperacionCatalogo=CR&IdPrestamoGrupo="+request.getParameter("IdPrestamoGrupo")+"&IdProducto="+request.getParameter("IdProducto")+"&NumCiclo="+sNumCiclo+"&Filtro=Modificacion";
		}
		return registroControl;
	}
}