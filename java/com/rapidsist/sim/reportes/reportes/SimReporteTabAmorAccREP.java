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
public class SimReporteTabAmorAccREP implements ReporteControlIN {

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
		String sClave = request.getParameter("CvePrestamo");
		
		System.out.println("sClave:"+sClave);
		
		
		String sSql =   "SELECT\n"+
        "A.ID_PRESTAMO,\n"+
        "A.NUM_PAGO_AMORTIZACION,\n"+
        "P.CVE_PRESTAMO,\n"+
        "P.ID_CLIENTE,\n"+
        "C.NOM_COMPLETO,\n"+
        "P.ID_PRODUCTO,\n"+
        "O.NOM_PRODUCTO,\n"+
        "P.NUM_CICLO,\n"+            
        "A.ID_ACCESORIO,\n"+
        "N.NOM_ACCESORIO,\n"+

        "TO_CHAR(A.IMP_ACCESORIO,'999,999,999.9999') IMP_ACCESORIO,\n"+
        "TO_CHAR(A.IMP_IVA_ACCESORIO,'999,999,999.9999') IMP_IVA_ACCESORIO,\n"+
        "TO_CHAR(A.IMP_ACCESORIO_PAGADO,'999,999,999.9999') IMP_ACCESORIO_PAGADO,\n"+
        "TO_CHAR(A.IMP_IVA_ACCESORIO_PAGADO,'999,999,999.9999') IMP_IVA_ACCESORIO_PAGADO\n"+
        "FROM SIM_TABLA_AMORTIZACION T,\n"+
        "SIM_TABLA_AMORT_ACCESORIO A,\n"+
        "SIM_CAT_ACCESORIO N,\n"+

        "SIM_PRESTAMO P,\n"+
        "RS_GRAL_PERSONA C,\n"+
        "SIM_PRODUCTO O\n"+
        "WHERE T.CVE_GPO_EMPRESA ='" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
		"AND T.CVE_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "' \n"+
        "AND A.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA\n"+
        "AND A.CVE_EMPRESA = T.CVE_EMPRESA\n"+
        "AND A.ID_PRESTAMO = T.ID_PRESTAMO\n"+
        "AND A.NUM_PAGO_AMORTIZACION = T.NUM_PAGO_AMORTIZACION\n"+
        "AND N.CVE_GPO_EMPRESA = A.CVE_GPO_EMPRESA\n"+
        "AND N.CVE_EMPRESA = A.CVE_EMPRESA\n"+
        "AND N.ID_ACCESORIO = A.ID_ACCESORIO\n"+


        "AND P.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA\n"+
        "AND P.CVE_EMPRESA = T.CVE_EMPRESA\n"+
        "AND P.ID_PRESTAMO = T.ID_PRESTAMO\n"+
        "AND C.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA\n"+
        "AND C.CVE_EMPRESA = P.CVE_EMPRESA\n"+
        "AND C.ID_PERSONA = P.ID_CLIENTE\n"+
        "AND O.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA\n"+
        "AND O.CVE_EMPRESA = P.CVE_EMPRESA\n"+
        "AND O.ID_PRODUCTO = P.ID_PRODUCTO\n";   
        
		
		                    if (sClave != null && !sClave.equals("") && !sClave.equals("null") ){								
								sSql = sSql + "AND P.CVE_PRESTAMO = '" + sClave + "' \n";
							
		
							 sSql = sSql +
						
							 "ORDER BY T.ID_PRESTAMO,A.ID_ACCESORIO,A.NUM_PAGO_AMORTIZACION";
							
							 System.out.println("*****************Paso por aqui****************:"+sSql);
		
	    String sTipoReporte = request.getParameter("TipoReporte");
	    System.out.println("TipoReporte:"+sTipoReporte);
		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("Portales/Sam/img/Imagentsys1.gif"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReporteTabAmorAcc.jasper");
		parametros.put("NombreReporte", "rep"+sClave);
		                             
		                    }
		return parametros;		
	}
	
}
