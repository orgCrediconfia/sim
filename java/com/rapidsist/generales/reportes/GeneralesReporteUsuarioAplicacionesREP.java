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
 * Realiza la consulta para obtener los datos que serán utilizados para generar el reporte de aplicaciones asignadas a un usuario.
 */
public class GeneralesReporteUsuarioAplicacionesREP implements ReporteControlIN {
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
						"B.NOM_ALIAS, \n"+
				  		"D.NOM_PERFIL, \n"+
						"D.CVE_PERFIL, \n"+
						"A.CVE_APLICACION, \n"+
						"C.NOM_APLICACION, \n"+
						"B.LOCALIDAD, \n"+
						"A.CVE_USUARIO \n"+
					  "FROM \n"+
						"RS_GRAL_USUARIO_PERFIL A, \n"+
						"RS_GRAL_USUARIO B, \n"+
						"RS_CONF_APLICACION C, \n"+
						"RS_CONF_PERFIL D \n" +
					  "WHERE \n"+
						"B.CVE_GPO_EMPRESA = A.CVE_GPO_EMPRESA \n"+
						"AND B.CVE_USUARIO = A.CVE_USUARIO \n"+
						"AND D.CVE_PERFIL = A.CVE_PERFIL \n"+
						"AND C.CVE_APLICACION = A.CVE_APLICACION \n"+
						"AND D.CVE_APLICACION = A.CVE_APLICACION \n"+
						"AND A.CVE_GPO_EMPRESA = '" +(String)request.getParameter("CveGpoEmpresa")+"'  \n"+
						"AND A.CVE_USUARIO = '" + (String)request.getParameter("CveUsuario")+"'  \n"+
						"ORDER BY D.CVE_PERFIL, D.NOM_PERFIL, A.CVE_APLICACION \n";
		
		parametros.put("Sql", sSql);
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Generales/GeneralesReporteUsuarioAplicaciones.jasper");
		return parametros;
	}
}
