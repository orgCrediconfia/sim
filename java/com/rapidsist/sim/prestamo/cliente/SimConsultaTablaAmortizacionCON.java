/**
 * Sistema de administraci�n de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControl;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import javax.naming.Context;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;

/**
 * Esta clase se encarga de administrar los servicios de operaci�n (alta, baja,
 * modificaci�n y consulta) de comites. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimConsultaTablaAmortizacionCON implements CatalogoControlConsultaIN {

	/**
	 * Ejecuta los servicios de consulta del cat�logo.
	 * @param parametros Par�metros que se recogen de la sesion del usuario y se le envian a la clase CON.
	 * Estos par�metros son: CVE_GPO_EMPRESA (Clave del grupo empresa), Filtro (el valor
	 * del filtro que se debe aplicar solo si se ejecuto el componente de cat�logos con
	 * OperacionCatalogo=CT)
	 * @param request Objeto que provee de informaci�n al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo env�a como un par�metro a este m�todo.
	 * @param response Objeto que provee de informaci�n del servlet sobre el response del cliente. El
	 * contenedor de servlets crea un objeto HttpServletResponse y lo env�a como un par�metro a este m�todo.
	 * @param config Objeto que provee de informaci�n del servlet sobre el ServletConfig del cliente. El
	 * contenedor de servlets crea un objeto ServletConfig y lo env�a como un par�metro a este m�todo.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON
	 * @param contexto Objeto que contiene informaci�n acerca del entorno del servidor de
	 * aplicaciones.
	 * @param iTipoOperacion Operaci�n que debe ejecutar la clase CON. Las operaciones se encuentran
	 * especificadas en la clase {@link com.rapidsist.portal.cliente.CatalogoControl CatalogoControl}
	 * @return Resultado de la consulta y la p�gina a donde se redirecciona el control.
	 * @throws RemoteException Si se gener� un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se gener� un error dentro de la clase CON.
	 */
	public RegistroControl consulta(Registro parametros, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		
		//VERIFICA SI BUSCA TODOS LOS REGISTROS
		if (iTipoOperacion == CON_CONSULTA_TABLA){
			if (request.getParameter("Consulta").equals("Prestamos")){
				parametros.addDefCampo("CVE_PRESTAMO", request.getParameter("CvePrestamo"));
				parametros.addDefCampo("NOMBRE", request.getParameter("Nombre"));
				parametros.addDefCampo("CONSULTA","PRESTAMOS");
				registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimConsultaTablaAmortizacion", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimConTabAmo.jsp";
			}
			if (request.getParameter("Consulta").equals("Tabla")){
				//VERIFICA SI SE ENVIO EL PARAMETRO CLAVE DEL COMITE	
				
				parametros.addDefCampo("ID_PRESTAMO", request.getParameter("IdPrestamo"));
				
				String sPrestamo = request.getParameter("IdPrestamo");
				String sCeros;
				String sGrupalIndividual;
				
				sCeros = sPrestamo.substring(2,8);
				sGrupalIndividual = sPrestamo.substring(8,16);
				
				System.out.println("sCeros:"+sCeros);
				System.out.println("sGrupalIndividual:"+sGrupalIndividual);
				if (sCeros.equals("000000")){
					//Es un cr�dito individual.
					parametros.addDefCampo("CVE_PRESTAMO", request.getParameter("IdPrestamo"));
					
					Registro idprestamo = new Registro ();
					parametros.addDefCampo("APLICA_A", "INDIVIDUAL");
					idprestamo = catalogoSL.getRegistro("SimPrestamoObtieneIdentificador", parametros);
					String sIdPrestamo = (String)idprestamo.getDefCampo("ID_PRESTAMO");
					parametros.addDefCampo("ID_PRESTAMO",sIdPrestamo);
					registroControl.respuesta.addDefCampo("ListaTituloAccesorio", catalogoSL.getRegistros("SimConsultaListaAccesorio", parametros));
					Registro accesorios = new Registro ();
					accesorios = catalogoSL.getRegistro("SimConsultaListaAccesorio", parametros);
					String sAccesorios = (String)accesorios.getDefCampo("ACCESORIOS");
					//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
					registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimCatalogoSucursal", parametros));
					registroControl.respuesta.addDefCampo("ListaAccesorios", catalogoSL.getRegistros("SimPrestamoAccesorioDatosTA", parametros));
					registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
					registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimPrestamo", parametros));
					parametros.addDefCampo("CONSULTA","TABLA");
					registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimConsultaTablaAmortizacion", parametros));
					registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimTabAmoCon.jsp?Accesorio="+sAccesorios;
				}else {
					if (sGrupalIndividual.equals("00000000")){
						//Es un cr�dito grupal.
						
						Registro idprestamo = new Registro ();
						idprestamo = catalogoSL.getRegistro("SimPrestamoGrupoObtieneIdentificador", parametros);
						String sIdPrestamoGrupo = (String)idprestamo.getDefCampo("ID_PRESTAMO_GRUPO");
						parametros.addDefCampo("ID_PRESTAMO_GRUPO",sIdPrestamoGrupo);
						
						registroControl.respuesta.addDefCampo("ListaTituloAccesorio", catalogoSL.getRegistros("SimConsultaListaAccesorioGrupo", parametros));
						Registro accesorios = new Registro ();
						accesorios = catalogoSL.getRegistro("SimConsultaListaAccesorioGrupo", parametros);
						String sAccesorios = (String)accesorios.getDefCampo("ACCESORIOS");
						
						registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimCatalogoSucursal", parametros));
						registroControl.respuesta.addDefCampo("ListaAccesorios", catalogoSL.getRegistros("SimPrestamoAccesorioDatosTAGrupo", parametros));
						registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
						registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimPrestamoGrupal", parametros));
						parametros.addDefCampo("CONSULTA","TABLA");
						registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimConsultaTablaAmortizacionGrupo", parametros));
						
						registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimTabAmoCon.jsp?Accesorio="+sAccesorios;
					}else {
						//Es un cr�dito grupal-individual.
						
						parametros.addDefCampo("CVE_PRESTAMO", request.getParameter("IdPrestamo"));
						
						Registro idprestamo = new Registro ();
						parametros.addDefCampo("APLICA_A", "INDIVIDUAL");
						idprestamo = catalogoSL.getRegistro("SimPrestamoObtieneIdentificador", parametros);
						String sIdPrestamo = (String)idprestamo.getDefCampo("ID_PRESTAMO");
						parametros.addDefCampo("ID_PRESTAMO",sIdPrestamo);
						registroControl.respuesta.addDefCampo("ListaTituloAccesorio", catalogoSL.getRegistros("SimConsultaListaAccesorio", parametros));
						Registro accesorios = new Registro ();
						accesorios = catalogoSL.getRegistro("SimConsultaListaAccesorio", parametros);
						String sAccesorios = (String)accesorios.getDefCampo("ACCESORIOS");
						//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
						registroControl.respuesta.addDefCampo("ListaSucursal", catalogoSL.getRegistros("SimCatalogoSucursal", parametros));
						registroControl.respuesta.addDefCampo("ListaAccesorios", catalogoSL.getRegistros("SimPrestamoAccesorioDatosTA", parametros));
						registroControl.respuesta.addDefCampo("ListaPeriodicidad", catalogoSL.getRegistros("SimCatalogoPeriodicidad", parametros));
						registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimPrestamo", parametros));
						parametros.addDefCampo("CONSULTA","TABLA");
						registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimConsultaTablaAmortizacion", parametros));
						registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimTabAmoCon.jsp?Accesorio="+sAccesorios;
						
					}
				}
			}
		}
		
		if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Inicio")){
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimTabAmoCon.jsp";
			}
		}
		
		return registroControl;
	}
}
