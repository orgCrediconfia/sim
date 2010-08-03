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
 * Realiza la consulta para obtener los datos que serán utilizados para generar el reporte de estadísticas del acceso de usuarios al sistema.
 */
 
public class GeneralesReporteEstadisticasUsuarioREP implements ReporteControlIN {
	
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

		//REALIZA  LA CONSULTA
		String sSql = "SELECT \n"+
						"CVE_GPO_EMPRESA,\n"+
						"NOM_GPO_EMPRESA,\n"+
						"DIA,\n"+
						"SUM(USHORA0) AS USHORA00,\n"+
						"SUM(USHORA1) AS USHORA01,\n"+
						"SUM(USHORA2) AS USHORA02,\n"+
						"SUM(USHORA3) AS USHORA03,\n"+
						"SUM(USHORA4) AS USHORA04,\n"+
						"SUM(USHORA5) AS USHORA05,\n"+
						"SUM(USHORA6) AS USHORA06,\n"+
						"SUM(USHORA7) AS USHORA07,\n"+
						"SUM(USHORA8) AS USHORA08,\n"+
						"SUM(USHORA9) AS USHORA09,\n"+
						"SUM(USHORA10) AS USHORA010,\n"+
						"SUM(USHORA11) AS USHORA011,\n"+
						"SUM(USHORA12) AS USHORA012,\n"+
						"SUM(USHORA13) AS USHORA013,\n"+
						"SUM(USHORA14) AS USHORA014,\n"+
						"SUM(USHORA15) AS USHORA015,\n"+
						"SUM(USHORA16) AS USHORA016,\n"+
						"SUM(USHORA17) AS USHORA017,\n"+
						"SUM(USHORA18) AS USHORA018,\n"+
						"SUM(USHORA19) AS USHORA019,\n"+
						"SUM(USHORA20) AS USHORA020,\n"+
						"SUM(USHORA21) AS USHORA021,\n"+
						"SUM(USHORA22) AS USHORA022,\n"+
						"SUM(USHORA23) AS USHORA023,\n"+
						"SUM(USHORA0)+SUM(USHORA1)+SUM(USHORA2)+SUM(USHORA3)+SUM(USHORA4)+SUM(USHORA5)+SUM(USHORA6)+SUM(USHORA7)+SUM(USHORA8)+SUM(USHORA9)+SUM(USHORA10)+SUM(USHORA11)+SUM(USHORA12)+SUM(USHORA13)+SUM(USHORA14)+SUM(USHORA15)+SUM(USHORA16)+SUM(USHORA17)+SUM(USHORA18)+SUM(USHORA19)+SUM(USHORA20)+SUM(USHORA21)+SUM(USHORA22)+SUM(USHORA23) SUMTOT_DIA\n"+
						"FROM\n"+
							"(SELECT\n"+
								"A.CVE_GPO_EMPRESA, \n"+
								"B.NOM_GPO_EMPRESA, \n"+
								"COUNT(DISTINCT (A.CVE_USUARIO)) AS USUARIOS, \n"+
								"to_char(trunc(A.F_REGISTRO),'dd/mm/yyyy') DIA,\n"+
								"trunc(A.F_REGISTRO) FECHA ,\n"+
								"COUNT(*) AS TOTAL,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),00,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA0,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),01,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA1,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),02,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA2,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),03,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA3,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),04,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA4,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),05,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA5,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),06,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA6,\n"+	
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),07,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA7,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),08,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA8,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),09,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA9,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),10,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA10,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),11,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA11,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),12,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA12,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),13,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA13,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),14,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA14,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),15,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA15,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),16,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA16,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),17,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA17,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),18,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA18,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),19,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA19,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),20,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA20,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),21,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA21,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),22,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA22,\n"+
								"DECODE(TO_CHAR((A.F_REGISTRO), 'HH24'),23,COUNT(DISTINCT (A.CVE_USUARIO)),0) AS USHORA23\n"+
					  "FROM\n"+
						"RS_BITACORA_FUNCION A, \n"+
						"RS_CONF_GPO_EMPRESA B \n"+
					  "WHERE \n"+
					  	"B.CVE_GPO_EMPRESA = A.CVE_GPO_EMPRESA\n";
							
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
		
		sSql = sSql + "GROUP BY A.CVE_GPO_EMPRESA, B.CVE_GPO_EMPRESA, B.NOM_GPO_EMPRESA,\n"+
					  "to_char(trunc(A.F_REGISTRO),'dd/mm/yyyy'), trunc(A.F_REGISTRO),\n"+
					  "TO_CHAR((A.F_REGISTRO), 'HH24')\n"+
					  "ORDER BY to_char(trunc(A.F_REGISTRO),'dd/mm/yyyy'))\n"+
					  "GROUP BY CVE_GPO_EMPRESA, DIA, FECHA, NOM_GPO_EMPRESA \n"+
					  "ORDER BY FECHA, CVE_GPO_EMPRESA \n";
		
		parametros.put("Sql", sSql);
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Generales/GeneralesReporteEstadisticasUsuario.jasper");
		return parametros;
	}
}
