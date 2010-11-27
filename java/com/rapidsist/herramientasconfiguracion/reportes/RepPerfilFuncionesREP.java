package com.rapidsist.herramientasconfiguracion.reportes;

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
 * Realiza la consulta para obtener los datos que serán utilizados para generar el reporte de funciones asignadas a un perfil.
 */

public class RepPerfilFuncionesREP implements ReporteControlIN {

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
						"A.CVE_PERFIL, \n"+
						"A.NOM_PERFIL, \n"+
						"A.DESC_PERFIL, \n"+
						"A.CVE_APLICACION, \n"+
						"C.CVE_FUNCION, \n"+
						"B.B_CONSULTA, \n"+
						"B.B_ALTA, \n"+
						"B.B_BAJA, \n"+
						"B.B_MODIF, \n"+
						"B.B_BITACORA \n"+
					  "FROM \n"+
						"RS_CONF_PERFIL A, \n"+
						"RS_CONF_APLICACION_CONFIG B, \n"+
						"RS_CONF_FUNCION C \n"+
					  "WHERE \n"+ 
						"A.CVE_APLICACION = B.CVE_APLICACION \n"+
						"AND B.CVE_APLICACION = '"+ (String)request.getParameter("CveAplicacion") +"' \n" +
						"AND C.CVE_FUNCION = B.CVE_FUNCION \n"+
						"AND A.CVE_PERFIL = B.CVE_PERFIL \n"+
						"AND B.CVE_PERFIL = '"+ (String)request.getParameter("CvePerfil") +"' \n"+
					  "ORDER BY A.CVE_PERFIL, A.CVE_APLICACION, C.CVE_FUNCION \n";

		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("Portales/Sam/img/Imagentsys1.gif"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Herramientas/RepPerfilFunciones.jasper");
		return parametros;		
	}
}
