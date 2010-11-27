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
 * Realiza la consulta para obtener los datos que serán utilizados para generar el reporte de estadísticas de uso del sistema.
 */
public class GeneralesReporteEstadisticasREP implements ReporteControlIN {

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
		
		String sSql = "SELECT \n"+
				  		"A.CVE_GPO_EMPRESA, \n"+
						"A.CVE_USUARIO, \n"+
						"A.NOM_PERSONA, \n"+
						"A.NOM_FUNCION, \n"+
						"A.PARAMETROS, \n"+
						"A.F_REGISTRO, \n"+
						"B.NOM_GPO_EMPRESA\n"+
					  "FROM \n"+
						"RS_BITACORA_FUNCION A,\n"+
						"RS_CONF_GPO_EMPRESA B \n"+ 
					  "WHERE \n"+
						"B.CVE_GPO_EMPRESA = A.CVE_GPO_EMPRESA \n";
							
		if (request.getParameter("CveGpoEmpresa") != null && !request.getParameter("CveGpoEmpresa").equals("") && !request.getParameter("CveGpoEmpresa").equals("null") ){
			sSql = sSql + "AND A.CVE_GPO_EMPRESA = '" + request.getParameter("CveGpoEmpresa")+"'  \n";
			parametros.put("CveGpoEmpresa", request.getParameter("CveGpoEmpresa"));
		}
		
		if (request.getParameter("FechaIni") != null && !request.getParameter("FechaIni").equals("") && !request.getParameter("FechaIni").equals("null") ){
			sSql = sSql + "AND F_REGISTRO >= TO_DATE('" +  request.getParameter("FechaIni") + ":00-00-00','dd/mm/yyyy:hh24-mi-ss') \n";
			parametros.put("FechaIni", request.getParameter("FechaIni"));
		}
		
		if (request.getParameter("FechaFin") != null && !request.getParameter("FechaFin").equals("") && !request.getParameter("FechaFin").equals("null") ){
			sSql = sSql + "AND F_REGISTRO < 1 + TO_DATE('" +  request.getParameter("FechaFin") + ":00-00-00','dd/mm/yyyy:hh24-mi-ss') \n";
			parametros.put("FechaFin", request.getParameter("FechaFin"));
		}
		
		if (request.getParameter("CveUsuario") != null && !request.getParameter("CveUsuario").equals("") && !request.getParameter("CveUsuario").equals("null") ){
			sSql = sSql + "AND CVE_USUARIO = '" + request.getParameter("CveUsuario")+"'  \n";
			parametros.put("CveUsuario", request.getParameter("CveUsuario"));
		}
		
		sSql = sSql + "ORDER BY F_REGISTRO, CVE_GPO_EMPRESA \n";
										
		parametros.put("Sql", sSql);
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Generales/GeneralesReporteEstadisticas.jasper");
		return parametros;
	}
}
