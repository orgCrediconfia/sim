/**
 * Sistema de administraciï¿½n de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.cliente;

import com.rapidsist.comun.bd.Registro;
import com.rapidsist.comun.util.Fecha2;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;

import javax.naming.Context;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Esta clase se encarga de administrar las operaciones (alta, baja,
 * modificaciï¿½n y consulta) de los productos asigandos a los prï¿½stamos. Esta clase es llamada por
 * el servlet {@link CatalogoS CatalogoS}.
 */
public class SimPrestamoMovimientoExtraordinarioCON implements CatalogoControlConsultaIN, CatalogoControlActualizaIN{

	/**
	 * Ejecuta los servicios de consulta del catï¿½logo.
	 * @param parametros Parï¿½metros que se recogen de la sesion del usuario y se le envian a la clase CON.
	 * Estos parï¿½metros son: CVE_GPO_EMPRESA (Clave del grupo empresa), Filtro (el valor
	 * del filtro que se debe aplicar solo si se ejecuto el componente de catï¿½logos con
	 * OperacionCatalogo=CT)
	 * @param request Objeto que provee de informaciï¿½n al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envï¿½a como un parï¿½metro a este mï¿½todo.
	 * @param response Objeto que provee de informaciï¿½n del servlet sobre el response del cliente. El
	 * contenedor de servlets crea un objeto HttpServletResponse y lo envï¿½a como un parï¿½metro a este mï¿½todo.
	 * @param config Objeto que provee de informaciï¿½n del servlet sobre el ServletConfig del cliente. El
	 * contenedor de servlets crea un objeto ServletConfig y lo envï¿½a como un parï¿½metro a este mï¿½todo.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON
	 * @param contexto Objeto que contiene informaciï¿½n acerca del entorno del servidor de
	 * aplicaciones.
	 * @param iTipoOperacion Operaciï¿½n que debe ejecutar la clase CON. Las operaciones se encuentran
	 * especificadas en la clase {@link com.rapidsist.portal.cliente.CatalogoControl CatalogoControl}
	 * @return Resultado de la consulta y la pï¿½gina a donde se redirecciona el control.
	 * @throws RemoteException Si se generï¿½ un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se generï¿½ un error dentro de la clase CON.
	 */
	public RegistroControl consulta(Registro parametros, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		//VERIFICA SI BUSCA TODOS LOS REGISTROS
		if (iTipoOperacion == CON_CONSULTA_TABLA){
			
			//VERIFICA SI SE ENVIO EL PARAMETRO CVE GARANTIA	
			if (request.getParameter("CvePrestamo") != null && !request.getParameter("CvePrestamo").equals("")){
				parametros.addDefCampo("CVE_PRESTAMO", request.getParameter("CvePrestamo"));	
			}
			
			//VERIFICA SI SE ENVIO EL PARAMETRO NOM GARANTIA
			if (request.getParameter("NomCompleto") != null && !request.getParameter("NomCompleto").equals("")){	
				parametros.addDefCampo("NOM_COMPLETO", request.getParameter("NomCompleto"));
			}
			
			//OBTIENE TODOS LOS REGISTROS DE LA CONSULTA
			registroControl.respuesta.addDefCampo("ListaBusqueda", catalogoSL.getRegistros("SimPrestamoMovimientoExtraordinario", parametros));
			
			registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreMovExt.jsp";
			
		}
		else if (iTipoOperacion == CON_CONSULTA_REGISTRO){
			
			parametros.addDefCampo("ID_PRESTAMO",request.getParameter("IdPrestamo"));
			
			if (request.getParameter("Consulta").equals("Registro")){
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimPrestamoMovimientoExtraordinario", parametros));
				parametros.addDefCampo("OPERACION", "EXTRAORDINARIO");
				registroControl.respuesta.addDefCampo("ListaOperaciones", catalogoSL.getRegistros("SimPrestamoCatalogoOperacion", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreMovExtReg.jsp?IdPrestamo="+request.getParameter("IdPrestamo")+"&Consulta=Registro";
			}else if (request.getParameter("Consulta").equals("Pantalla") || request.getParameter("Consulta").equals("PantallaFinal")){
				String sAccesorios = "";
				String sConceptos = "";
				String sNomMovtoExtra = "";
				registroControl.respuesta.addDefCampo("registro", catalogoSL.getRegistro("SimPrestamoMovimientoExtraordinario", parametros));
				parametros.addDefCampo("OPERACION", "EXTRAORDINARIO");
				registroControl.respuesta.addDefCampo("ListaOperaciones", catalogoSL.getRegistros("SimPrestamoCatalogoOperacion", parametros));
				
				if(request.getParameter("AplicaA").equals("GRUPO")){
					//Obtiene los accesorios del prestamo.
					parametros.addDefCampo("ID_PRESTAMO",request.getParameter("CvePrestamo"));
					Registro idprestamo = new Registro ();
					idprestamo = catalogoSL.getRegistro("SimPrestamoGrupoObtieneIdentificador", parametros);
					String sIdPrestamoGrupo = (String)idprestamo.getDefCampo("ID_PRESTAMO_GRUPO");
					parametros.addDefCampo("ID_PRESTAMO_GRUPO",sIdPrestamoGrupo);
					registroControl.respuesta.addDefCampo("ListaTituloAccesorio", catalogoSL.getRegistros("SimConsultaListaAccesorioGrupo", parametros));
					Registro accesorios = new Registro ();
					accesorios = catalogoSL.getRegistro("SimConsultaListaAccesorioGrupo", parametros);
					sAccesorios = (String)accesorios.getDefCampo("ACCESORIOS");
					parametros.addDefCampo("APLICA_A","GRUPO");
					//Obtiene los concepto que conforma el movimiento extraordinario.
					parametros.addDefCampo("CVE_OPERACION",request.getParameter("MovimientoExtraordinario"));
					registroControl.respuesta.addDefCampo("ListaTituloMontoAplicados", catalogoSL.getRegistros("SimPrestamoCatalogoOperacionConcepto", parametros));
					//Cuenta los Conceptos de la operaciï¿½n.
					Registro conceptos = new Registro ();
					conceptos = catalogoSL.getRegistro("SimPrestamoMovimientoExtraordinarioMontosAplicados", parametros);
					sConceptos = (String)conceptos.getDefCampo("CONCEPTOS");
					
				}else if (request.getParameter("AplicaA").equals("INDIVIDUAL")){
					//Obtiene los accesorios del prestamo.
					parametros.addDefCampo("CVE_PRESTAMO",request.getParameter("CvePrestamo"));
					Registro idprestamo = new Registro ();
					idprestamo = catalogoSL.getRegistro("SimPrestamoObtieneIdentificador", parametros);
					String sIdPrestamo = (String)idprestamo.getDefCampo("ID_PRESTAMO");
					parametros.addDefCampo("ID_PRESTAMO",sIdPrestamo);
					registroControl.respuesta.addDefCampo("ListaTituloAccesorio", catalogoSL.getRegistros("SimConsultaListaAccesorio", parametros));
					Registro accesorios = new Registro ();
					accesorios = catalogoSL.getRegistro("SimConsultaListaAccesorio", parametros);
					sAccesorios = (String)accesorios.getDefCampo("ACCESORIOS");
					parametros.addDefCampo("APLICA_A","INDIVIDUAL");
					//Obtiene los concepto que conforma el movimiento extraordinario.
					parametros.addDefCampo("CVE_OPERACION",request.getParameter("MovimientoExtraordinario"));
					registroControl.respuesta.addDefCampo("ListaTituloMontoAplicados", catalogoSL.getRegistros("SimPrestamoCatalogoOperacionConcepto", parametros));
					//Cuenta los Conceptos de la operaciï¿½n.
					Registro conceptos = new Registro ();
					conceptos = catalogoSL.getRegistro("SimPrestamoMovimientoExtraordinarioMontosAplicados", parametros);
					sConceptos = (String)conceptos.getDefCampo("CONCEPTOS");
				}
				
				//Obtiene el nombre del movimiento extraordinario
				Registro movtoExtra = new Registro ();
				movtoExtra = catalogoSL.getRegistro("SimPrestamoCatalogoOperacion", parametros);
				sNomMovtoExtra = (String)movtoExtra.getDefCampo("DESC_LARGA");
				
				//Obtiene los saldos actuales.
				parametros.addDefCampo("CONSULTA","SALDOS");
				registroControl.respuesta.addDefCampo("ListaSaldos", catalogoSL.getRegistros("SimPrestamoMovimientoExtraordinarioSaldos", parametros));
				//Obtiene los saldos totales actuales.
				parametros.addDefCampo("CONSULTA","SALDOS_TOTALES");
				registroControl.respuesta.addDefCampo("TotalSaldos", catalogoSL.getRegistros("SimPrestamoMovimientoExtraordinarioSaldos", parametros));
				//Obtiene los rubros del movimiento extraordinario.
				registroControl.respuesta.addDefCampo("ListaMontoAplicados", catalogoSL.getRegistros("SimPrestamoMovimientoExtraordinarioMontosAplicados", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreMovExtReg.jsp?IdPrestamo="+request.getParameter("IdPrestamo")+"&CvePrestamo="+request.getParameter("CvePrestamo")+"&CveOperacion="+request.getParameter("MovimientoExtraordinario")+"&NomMovtoExtra="+sNomMovtoExtra+"&AplicaA="+request.getParameter("AplicaA")+"&Accesorio="+sAccesorios+"&Conceptos="+sConceptos+"&Consulta="+request.getParameter("Consulta");
			}
		}
		else if (iTipoOperacion == CON_INICIALIZACION){
			if (request.getParameter("Filtro").equals("Inicio")){
				//parametros.addDefCampo("OPERACION", "EXTRAORDINARIO");
				//registroControl.respuesta.addDefCampo("ListaOperaciones", catalogoSL.getRegistros("SimPrestamoCatalogoOperacion", parametros));
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreMovExt.jsp";
			}else if (request.getParameter("Filtro").equals("Alta")){
				registroControl.sPagina = "/Aplicaciones/Sim/Prestamo/fSimPreMovExt.jsp";
			}
		}
		return registroControl;
	}

	/**
	 * Valida los pï¿½rametros de entrada y ejecuta los servicios de alta, baja o cambio.
	 * @param registro Parametros que se recogen de la sesion del usuario y se le envian a la clase CON.
	 * Estos parï¿½metros son: CVE_GPO_EMPRESA (Clave del grupo empresa), CVE_USUARIO_BITACORA (clave
	 * del usuario que realiza la operacion), RegistroOriginal (registro leido originalmente y
	 * se utiliza cuando se ejecuta la operaciï¿½n de modificacion y se verifica que no se hallan
	 * realizado modificaciones al registro).
	 * @param request Objeto que provee de informaciï¿½n al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envï¿½a como un parï¿½metro a este mï¿½todo.
	 * @param response Objeto que provee de informaciï¿½n del servlet sobre el response del cliente. El
	 * contenedor de servlets crea un objeto HttpServletResponse y lo envï¿½a como un parï¿½metro a este mï¿½todo.
	 * @param config Objeto que provee de informaciï¿½n del servlet sobre el ServletConfig del cliente. El
	 * contenedor de servlets crea un objeto ServletConfig y lo envï¿½a como un parï¿½metro a este mï¿½todo.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON
	 * @param contexto Objeto que contiene informaciï¿½n acerca del entorno del servidor de
	 * aplicaciones.
	 * @param iTipoOperacion Operaciï¿½n que debe ejecutar la clase CON. Las operaciones se encuentran
	 * especificadas en la clase {@link com.rapidsist.portal.cliente.CatalogoControl CatalogoControl}
	 * @return Respuesta del servicio de alta, baja o cambio y la pï¿½gina a donde
	 * se redirecciona el control.
	 * @throws RemoteException Si se generï¿½ un error en el Ejb CatalogoSL.
	 * @throws java.lang.Exception Si se generï¿½ un error dentro de la clase CON.
	 */
	public RegistroControl actualiza(Registro registro, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion)throws RemoteException, Exception{
		RegistroControl registroControl = new RegistroControl();
		registro.addDefCampo("DobleSubmit","Desabilitado");

		//Clave del movimento extraordinario.
		registro.addDefCampo("CVE_OPERACION",request.getParameter("CveOperacion"));
		
		//Obtiene los IdPrestamos.
		String[] IdPrestamo = request.getParameterValues("IdPrestamoIndGpo");

		String sNumConceptos = request.getParameter("Conceptos");
		int iNumConceptos = Integer.parseInt(sNumConceptos);
		
		System.out.println("Conceptos: "+iNumConceptos);
		System.out.println("Prestamos: "+(IdPrestamo.length));
		
		//Verifica si la operación es Ajuste de Movimiento.
		if (request.getParameter("CveOperacion").equals("AJUMOV")){
			registro.addDefCampo("NUM_CONCEPTOS",sNumConceptos);
			
			//Prepara los IdPrestamos a la DAO.
			registro.addDefCampo("DAO_ID_PRESTAMO",IdPrestamo);
			
			//Obtiene los CveConceptos.
	        for (int i = 0; i < iNumConceptos; i++) {
	            String[] Concepto = null;
	            Concepto = request.getParameterValues("CVE_CONCEPTO_"+i+"");
	            registro.addDefCampo("DAO_CONCEPTO_"+i+"",Concepto);
	        }
	        //Obtiene los Importes.
	        for (int j = 0; j < iNumConceptos; j++) {
	            String[] Importe = null;
	            Importe = request.getParameterValues("IMPORTE_"+j+"");
	            registro.addDefCampo("DAO_IMPORTE_"+j+"",Importe);
	        }
			
		}else{
			//Se trata de movimientos diferentes a Ajuste de Movimiento.
			int iNumeroElementos = iNumConceptos*(IdPrestamo.length);
			System.out.println("Elementos: "+iNumeroElementos);
	
			Object[] objetos = new Object[iNumeroElementos];
			int k = 0;
			//OBTIENE LOS CONCEPTOS DEL MOVIMIENTO EXTRAORDINARIO
			for (int i = 0; i < iNumConceptos; i++) {
				String[] Conceptos = request.getParameterValues("CVE_CONCEPTO_"+i);
				String[] Importes = request.getParameterValues("IMPORTE_"+i);			
				for (int j = 0; j < (IdPrestamo.length) ; j++) {
					//ASIGNA LA CANTIDAD DE CERO SI EL USUARIO NO ENVIO UN IMPORTE
					if(Importes[j].equals("")){
						Importes[j]="0";
					}
					Object[] objeto = {IdPrestamo[j],Conceptos[0],Importes[j]};
					objetos[k] = objeto;
					k++;
				}
			}
			registro.addDefCampo("MovExtraObjetos", objetos);
		}
		registroControl.resultadoCatalogo = catalogoSL.modificacion("SimPrestamoMovimientoExtraordinario", registro, iTipoOperacion);
		registroControl.sPagina = "/ProcesaCatalogo?Funcion=SimPrestamoMovimientoExtraordinario&OperacionCatalogo=CR&Consulta=PantallaFinal&IdPrestamo="+request.getParameter("IdPrestamo")+"&CvePrestamo="+request.getParameter("CvePrestamo")+"&MovimientoExtraordinario="+request.getParameter("CveOperacion")+"&AplicaA="+request.getParameter("AplicaA");
		
		return registroControl;
	}
}