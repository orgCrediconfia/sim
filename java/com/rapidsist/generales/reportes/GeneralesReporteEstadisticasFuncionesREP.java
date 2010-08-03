package com.rapidsist.generales.reportes;

import com.rapidsist.portal.cliente.reportes.ReporteControlIN;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import com.rapidsist.comun.util.Fecha2;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import com.rapidsist.portal.catalogos.CatalogoSL;
import javax.naming.Context;
import com.rapidsist.comun.bd.Registro;
import javax.servlet.http.HttpSession;
import com.rapidsist.portal.configuracion.Usuario;

/**
 * Realiza la consulta para obtener los datos que serán utilizados para generar el reporte estadística de acceso a funciones.
 */

public class GeneralesReporteEstadisticasFuncionesREP implements ReporteControlIN {

	/**
	 * Obtiene la consulta en la base de datos y parámetros que serán utilizados por el reporte.
	 * @param parametrosCatalogo Parámetros que se le envían al método .
	 * @param request Objeto que provee de información al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envía como un parámetro a este método.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON.
	 * @param contextoServidor Objeto que contiene información acerca del entorno del servidor de
	 * aplicaciones.
	 * @param contextoServlet Objeto que contiene información acerca del entorno del servlet. 
	 * @return Map objeto que contiene los datos para generar el reporte.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Map getParametros(Registro parametrosCatalogo, HttpServletRequest request, CatalogoSL catalogoSL, Context contextoServidor, ServletContext contextoServlet)  throws Exception{
		Map parametros = new HashMap();

		boolean bBanderaAnd = false;
		
		//OBTIENE LOS DATOS DEL USUARIO
		HttpSession session = request.getSession(true);
		Usuario usuario = (Usuario)session.getAttribute("Usuario");
		String sCveUsuario = usuario.sCveUsuario;
		String sCvePortal = usuario.sCvePortal;
		parametros.put("CveUsuario", sCveUsuario);
				
		//OBTIENE LOS DATOS DEL GRUPO DE EMPRESA
		Registro registroEmpresa = new Registro();
		registroEmpresa = catalogoSL.getRegistro("HerramientasConfiguracionGrupoEmpresas", parametrosCatalogo);
		String sNombreGrupoEmpresa = (String)registroEmpresa.getDefCampo("NOM_GPO_EMPRESA"); 
		parametros.put("NomEntidad", sNombreGrupoEmpresa);
		
		//OBTIENE LOS DATOS DEL PORTAL
		Registro registroPortal = new Registro();
		parametrosCatalogo.addDefCampo("CVE_PORTAL", sCvePortal);
		registroPortal = catalogoSL.getRegistro("HerramientasConfiguracionPortal", parametrosCatalogo);
        String sNombrePortal = (String)registroPortal.getDefCampo("NOM_PORTAL");
		parametros.put("NombrePortal", sNombrePortal);

		//REALIZA  LA CONSULTA
		String sSql = "SELECT \n"+
						"CVE_FUNCION, \n"+
						"CVE_GPO_EMPRESA, \n"+
						"TRUNC(F_REGISTRO) FECHA, \n"+
						"NOM_FUNCION, \n"+
						"COUNT(1) ACCESOS \n"+
					"FROM \n"+
						"RS_BITACORA_FUNCION \n"+
					"WHERE \n";

		if (request.getParameter("CveGpoEmpresa") != null && !request.getParameter("CveGpoEmpresa").equals("") && !request.getParameter("CveGpoEmpresa").equals("null") ){
			sSql = sSql + "CVE_GPO_EMPRESA = '" + request.getParameter("CveGpoEmpresa")+"'  \n";
			parametros.put("CveGpoEmpresa", request.getParameter("CveGpoEmpresa"));
			bBanderaAnd = true;
		}
	
		if (request.getParameter("CveFuncion") != null && !request.getParameter("CveFuncion").equals("") && !request.getParameter("CveFuncion").equals("null") ){
			if(bBanderaAnd){
				sSql = sSql + "AND ";
			}
			sSql = sSql + "CVE_FUNCION = '" + request.getParameter("CveFuncion")+"'  \n";
			parametros.put("CveFuncion", request.getParameter("CveFuncion"));
			bBanderaAnd = true;
		}
		
		if (request.getParameter("FechaIni") != null && !request.getParameter("FechaIni").equals("") && !request.getParameter("FechaIni").equals("null") ){
			if(bBanderaAnd){
				sSql = sSql + "AND ";
			}
			sSql = sSql + "F_REGISTRO >= TO_DATE('" +  request.getParameter("FechaIni") + ":00-00-00','dd/mm/yyyy:hh24-mi-ss') \n";
			parametros.put("FechaIni", request.getParameter("FechaIni"));
			bBanderaAnd = true;
		}
			
		if (request.getParameter("FechaFin") != null && !request.getParameter("FechaFin").equals("") && !request.getParameter("FechaFin").equals("null") ){
			if(bBanderaAnd){
				sSql = sSql + "AND ";
			}
			sSql = sSql + "F_REGISTRO < 1 + TO_DATE('" +  request.getParameter("FechaFin") + ":00-00-00','dd/mm/yyyy:hh24-mi-ss') \n";
			parametros.put("FechaFin", request.getParameter("FechaFin"));
		}	
		sSql = sSql + "GROUP BY CVE_FUNCION,CVE_GPO_EMPRESA,NOM_FUNCION,TRUNC(F_REGISTRO) \n"+
					  "ORDER BY CVE_FUNCION,FECHA,CVE_GPO_EMPRESA \n";

		//OBTIENE LA FECHA INICIO
			String sFechaInicio = request.getParameter("FechaIni");
			java.util.Date dFechaInicio = com.rapidsist.comun.util.Fecha2.toDate(sFechaInicio);

		//OBTIENE LA FECHA FIN
			String sFechaFin = request.getParameter("FechaFin");
			java.util.Date dFechaFin = com.rapidsist.comun.util.Fecha2.toDate(sFechaFin );

		//OBTIENE LA DIFERENCIA DE DIAS Y LOS CONVIERTE EN ENTEROS
		int iDias =  com.rapidsist.comun.util.Fecha2.diasEntreFechas(dFechaInicio,dFechaFin);
		//CONVIERTE LA DIFERENCIA EN STRING
		
		parametros.put("Dias", new Integer(iDias));
		parametros.put("Sql", sSql);
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Generales/GeneralesReporteEstadisticasFunciones.jasper");
		return parametros;
	}
}
