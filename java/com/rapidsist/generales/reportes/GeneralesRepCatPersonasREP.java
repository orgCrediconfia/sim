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
 * Realiza la consulta para obtener los datos que serán utilizados para generar el reporte de personas.
 */
public class GeneralesRepCatPersonasREP implements ReporteControlIN {

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
		Registro resultado = new Registro();
        resultado.addDefCampo("RegistroGrupoEmpresa", catalogoSL.getRegistro("HerramientasConfiguracionGrupoEmpresas", parametrosCatalogo));
		String sNombreGrupoEmpresa  = (String)((Registro)resultado.getDefCampo("RegistroGrupoEmpresa")).getDefCampo("NOM_GPO_EMPRESA");
		parametros.put("NomEntidad", sNombreGrupoEmpresa);

		//OBTIENE LOS DATOS DEL PORTAL
		Registro registroPortal = new Registro();
		parametrosCatalogo.addDefCampo("CVE_PORTAL", sCvePortal);
		registroPortal = catalogoSL.getRegistro("HerramientasConfiguracionPortal", parametrosCatalogo);
        String sNombrePortal = (String)registroPortal.getDefCampo("NOM_PORTAL");
		parametros.put("NombrePortal", sNombrePortal);

		//REALIZA  LA CONSULTA
		String sSql = "SELECT \n"+
						"ID_PERSONA, \n"+
						"RFC, \n"+
						"NOM_PERSONA, \n"+
						"CVE_GPO_EMPRESA, \n"+
						"NUM_TEL, \n"+
						"EMAIL, \n"+
						"NOM_COMPLETO \n"+
					"FROM RS_GRAL_PERSONA \n"+
					"WHERE 1=1 \n";						
		if (request.getParameter("CveGpoEmpresa") != null && !request.getParameter("CveGpoEmpresa").equals("") && !request.getParameter("CveGpoEmpresa").equals("null") ){
			sSql = sSql + "AND CVE_GPO_EMPRESA = '" + request.getParameter("CveGpoEmpresa")+"'  \n";
			parametros.put("CveGpoEmpresa", request.getParameter("CveGpoEmpresa"));
		}	
		
		if (request.getParameter("NomCompleto") != null && !request.getParameter("NomCompleto").equals("") && !request.getParameter("NomCompleto").equals("null") ){
			sSql = sSql + "AND UPPER(NOM_COMPLETO) LIKE '%" + ((String)request.getParameter("NomCompleto")).toUpperCase() +"%'  \n";
			parametros.put("NomCompleto", request.getParameter("NomCompleto"));
		}
		
		if (request.getParameter("PersonaFisica") != null && !request.getParameter("PersonaFisica").equals(" ") && !request.getParameter("PersonaFisica").equals("null") ){
			sSql = sSql + "AND B_PERSONA_FISICA = '"+ request.getParameter("PersonaFisica")+"'  \n";
			parametros.put("PersonaFisica", request.getParameter("PersonaFisica"));
			if (request.getParameter("PersonaFisica").equals("V")){
				String sPersonalidad = "Persona física";
				parametros.put("Personalidad", sPersonalidad);
			}
			else{ 
				String sPersonalidad = "Persona moral";
				parametros.put("Personalidad", sPersonalidad);
			}
		}

										
		if (request.getParameter("Rfc") != null && !request.getParameter("Rfc").equals("") && !request.getParameter("Rfc").equals("null") ){
			sSql = sSql + "AND UPPER(RFC) LIKE '%" +  ((String)request.getParameter("Rfc")).toUpperCase() +"%'  \n";
			parametros.put("Rfc", request.getParameter("Rfc"));
		}
		
		sSql = sSql + "ORDER BY CVE_GPO_EMPRESA, NOM_COMPLETO \n";
		
		parametros.put("Sql", sSql);
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Generales/GeneralesPersonaReporte.jasper");
		return parametros;
	}
}
