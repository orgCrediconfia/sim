package com.rapidsist.publicaciones.reportes;

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

public class PublicacionesReporteEstadisticasREP implements ReporteControlIN {

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
		String sSql =	"SELECT \n"+
							"A.CVE_GPO_EMPRESA, \n"+
							"A.NOM_PUBLICACION, \n"+
							"A.ID_PUBLICACION, \n"+
							"A.F_INI_VIGENCIA, \n"+
							"A.CONTADOR, \n"+							
							"A.F_FIN_VIGENCIA, \n"+
							"B.CVE_SECCION, \n"+
							"B.NOM_SECCION, \n"+
							"C.NOM_TIPO_ACCESO, \n"+
							"C.ID_NIVEL_ACCESO \n"+
						"FROM \n"+
							"RS_PUB_PUBLICACION A, \n"+
							"RS_PUB_SECCION B, \n"+
							"RS_PUB_NIVEL_ACCESO C \n"+
						"WHERE \n"+
							"C.CVE_GPO_EMPRESA = A.CVE_GPO_EMPRESA \n"+
							"AND B.CVE_GPO_EMPRESA = A.CVE_GPO_EMPRESA \n"+
							"AND C.CVE_PORTAL = A.CVE_PORTAL \n"+
							"AND B.CVE_PORTAL = A.CVE_PORTAL \n"+
							"AND B.CVE_SECCION = A.CVE_SECCION \n"+
							"AND C.ID_NIVEL_ACCESO = A.ID_NIVEL_ACCESO \n";

		if (request.getParameter("CveGpoEmpresa") != null && !request.getParameter("CveGpoEmpresa").equals("") && !request.getParameter("CveGpoEmpresa").equals("null") ){
			sSql = sSql + "AND B.CVE_GPO_EMPRESA = '" + request.getParameter("CveGpoEmpresa")+"'  \n";
			parametros.put("CveGpoEmpresa", request.getParameter("CveGpoEmpresa"));
		}

		if (request.getParameter("CveSeccion") != null && !request.getParameter("CveSeccion").equals("") && !request.getParameter("CveSeccion").equals("null") ){
			sSql = sSql + "AND B.CVE_SECCION = '" + request.getParameter("CveSeccion")+"'  \n";
			parametros.put("CveSeccion", request.getParameter("CveSeccion"));
		}	

		if (request.getParameter("NivAcceso") != null && !request.getParameter("NivAcceso").equals("") && !request.getParameter("NivAcceso").equals("null") ){
			sSql = sSql + "AND C.ID_NIVEL_ACCESO = '" + request.getParameter("CveGpoEmpresa")+"'  \n";
			parametros.put("NivAcceso", request.getParameter("NivAcceso"));
		}	
		
		if (request.getParameter("FechaIni") != null && !request.getParameter("FechaIni").equals("") && !request.getParameter("FechaIni").equals("null") ){
			sSql = sSql + "AND A.F_INI_VIGENCIA >= TO_DATE('" +  request.getParameter("FechaIni") + ":00-00-00','dd/mm/yyyy:hh24-mi-ss') \n";
			parametros.put("FechaIni", request.getParameter("FechaIni"));
		}
			
		if (request.getParameter("FechaFin") != null && !request.getParameter("FechaFin").equals("") && !request.getParameter("FechaFin").equals("null") ){
			sSql = sSql + "AND A.F_FIN_VIGENCIA < 1 + TO_DATE('" +  request.getParameter("FechaFin") + ":00-00-00','dd/mm/yyyy:hh24-mi-ss') \n";
			parametros.put("FechaFin", request.getParameter("FechaFin"));
		}	
		sSql = sSql + "GROUP BY A.CVE_GPO_EMPRESA, A.NOM_PUBLICACION, A.ID_PUBLICACION,A.F_INI_VIGENCIA,A.F_FIN_VIGENCIA,B.CVE_SECCION,C.NOM_TIPO_ACCESO,C.ID_NIVEL_ACCESO,B.NOM_SECCION,A.CONTADOR \n"+
					  "ORDER BY A.CVE_GPO_EMPRESA \n";
		
		parametros.put("Sql", sSql);
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Publicaciones/PublicacionesReporteEstadisticas.jasper");
		return parametros;
	}
}
