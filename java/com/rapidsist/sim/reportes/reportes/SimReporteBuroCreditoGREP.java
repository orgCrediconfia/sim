package com.rapidsist.sim.reportes.reportes;

import com.rapidsist.portal.cliente.reportes.ReporteControlIN;

import java.sql.SQLException;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import com.rapidsist.comun.util.Fecha2;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import com.rapidsist.portal.catalogos.CatalogoSL;
import javax.naming.Context;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;

import javax.servlet.http.HttpSession;
import com.rapidsist.portal.configuracion.Usuario;

/**
 * Realiza la consulta para obtener los datos que ser�n utilizados para generar el reporte de cat�logo de aplicaciones.
 */
public class SimReporteBuroCreditoGREP implements ReporteControlIN {

	/**
	 * Obtiene la consulta en la base de datos y par�metros que ser�n utilizados por el reporte.
	 * @param parametrosCatalogo Par�metros que se le env�an al m�todo .
	 * @param request Objeto que provee de informaci�n al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo env�a como un par�metro a este m�todo.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON.
	 * @param contextoServidor Objeto que contiene informaci�n acerca del entorno del servidor de
	 * aplicaciones.
	 * @param contextoServlet Objeto que contiene informaci�n acerca del entorno del servlet. 
	 * @return Map objeto que contiene los datos para generar el reporte.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public  Map getParametros(Registro parametrosCatalogo, HttpServletRequest request, CatalogoSL catalogoSL, Context contextoServidor, ServletContext contextoServlet)  throws Exception{
		Map parametros = new HashMap();

		String sCveGpoEmpresa = request.getParameter("CveGpoEmpresa");
		String sCveEmpresa = request.getParameter("CveEmpresa");
		String sClaveGrupo = request.getParameter("CvePrestamoGrupo");
		
		System.out.println("sClaveGrupo:"+sClaveGrupo);
		
        String sSql ="SELECT\n"+
				        "'@' AS INICIO,\n"+
				        "GP.RFC,\n"+
				        "GP.NOMBRE_1,\n"+
				        "NVL(GP.NOMBRE_2, ' ') AS NOMBRE_2,\n"+
				        "GP.AP_PATERNO,\n"+
				        "GP.AP_MATERNO,\n"+
				        "GD.CALLE ||' '|| GD.NUMERO_EXT AS CALLE_NUMERO,\n"+
				        "GD.NOM_ASENTAMIENTO,\n"+
				        "GD.NOM_DELEGACION,\n"+
				        "GE.SIGLA_ESTADO1,\n"+
				        "GD.CODIGO_POSTAL,\n"+
				        "GE.SIGLA_ESTADO2,\n"+
				        "0 AS ID_GRUPO,\n"+
				        "' ' AS NOM_GRUPO,\n"+
				        "SP.ID_CLIENTE,\n"+
				        "SP.NUM_CICLO\n"+
				       "FROM SIM_PRESTAMO SP,\n"+
				        "RS_GRAL_PERSONA GP,\n"+
				        "RS_GRAL_DOMICILIO GD,\n"+
				        "RS_GRAL_ESTADO GE\n"+
				       "WHERE SP.CVE_GPO_EMPRESA        = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
				        "AND SP.CVE_EMPRESA             = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "'\n";
				        
				        if(!sClaveGrupo.isEmpty())
				            sSql = sSql + "AND SP.CVE_PRESTAMO            = '" + (String)request.getParameter("CvePrestamoGrupo") + "'\n";
          
                        
				        sSql = sSql + "AND SP.ID_ETAPA_PRESTAMO       = 14\n"+
				        "AND SP.APLICA_A                = 'Individual'\n"+
				        "AND GP.CVE_GPO_EMPRESA         = SP.CVE_GPO_EMPRESA\n"+
				        "AND GP.CVE_EMPRESA             = SP.CVE_EMPRESA\n"+
				        "AND GP.ID_PERSONA              = SP.ID_CLIENTE\n"+
				        "AND GD.CVE_GPO_EMPRESA         = GP.CVE_GPO_EMPRESA\n"+ 
				        "AND GD.CVE_EMPRESA             = GP.CVE_EMPRESA \n"+
				        "AND GD.IDENTIFICADOR           = GP.ID_PERSONA \n"+
				        "AND GD.CVE_TIPO_IDENTIFICADOR  = 'CLIENTE'\n"+
				        "AND GD.NOM_ESTADO              = GE.NOMBRE_ESTADO\n"+
				       "UNION ALL\n"+
				       "SELECT\n"+
				        "'@' AS INICIO,\n"+
				        "GP.RFC,\n"+
				        "GP.NOMBRE_1,\n"+
				        "NVL(GP.NOMBRE_2, ' ') AS NOMBRE_2,\n"+
				        "GP.AP_PATERNO,\n"+
				        "GP.AP_MATERNO,\n"+
				        "GD.CALLE ||' '|| GD.NUMERO_EXT AS CALLE_NUMERO,\n"+
				        "GD.NOM_ASENTAMIENTO,\n"+
				        "GD.NOM_DELEGACION,\n"+
				        "GE.SIGLA_ESTADO1,\n"+
				        "GD.CODIGO_POSTAL,\n"+
				        "GE.SIGLA_ESTADO2,\n"+
				        "SP.ID_GRUPO,\n"+
				        "SG.NOM_GRUPO,\n"+
				        "SP.ID_CLIENTE,\n"+
				        "SP.NUM_CICLO\n"+
				       "FROM SIM_PRESTAMO SP,\n"+
				        "RS_GRAL_PERSONA GP,\n"+
				        "RS_GRAL_DOMICILIO GD,\n"+
				        "SIM_GRUPO SG,\n"+
				        "RS_GRAL_ESTADO GE\n"+
				       "WHERE SP.CVE_GPO_EMPRESA        = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
				        "AND SP.CVE_EMPRESA             = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "'\n";
        
				        if(!sClaveGrupo.isEmpty())
				            sSql = sSql + "AND SP.CVE_PRESTAMO            = '" + (String)request.getParameter("CvePrestamoGrupo") + "'\n";
          
                        
				        sSql = sSql + "AND SP.ID_ETAPA_PRESTAMO       = 14\n"+
				        "AND SP.APLICA_A                = 'Grupo'\n"+
				        "AND GP.CVE_GPO_EMPRESA         = SP.CVE_GPO_EMPRESA\n"+
				        "AND GP.CVE_EMPRESA             = SP.CVE_EMPRESA\n"+
				        "AND GP.ID_PERSONA              = SP.ID_CLIENTE\n"+
				        "AND GD.CVE_GPO_EMPRESA         = GP.CVE_GPO_EMPRESA\n"+ 
				        "AND GD.CVE_EMPRESA             = GP.CVE_EMPRESA \n"+
				        "AND GD.IDENTIFICADOR           = GP.ID_PERSONA \n"+
				        "AND GD.CVE_TIPO_IDENTIFICADOR  = 'CLIENTE'\n"+
				        "AND SG.CVE_GPO_EMPRESA         = GD.CVE_GPO_EMPRESA\n"+
				        "AND SG.CVE_EMPRESA             = GD.CVE_EMPRESA\n"+
				        "AND SG.ID_GRUPO                = SP.ID_GRUPO\n"+
				        "AND GD.NOM_ESTADO              = GE.NOMBRE_ESTADO\n";
        
							 System.out.println("*****************Paso por aqui****************:"+sSql);
		
	    String sTipoReporte = request.getParameter("TipoReporte");
	    System.out.println("TipoReporte:"+sTipoReporte);
		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("Portales/Sam/img/Imagentsys1.gif"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReporteBuroCredito.jasper");
		parametros.put("NombreReporte", "Reporte"+sClaveGrupo);
		
		return parametros;		
	}
}
