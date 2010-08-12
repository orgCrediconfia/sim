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
public class SimReporteTabAmorXlsREP implements ReporteControlIN {

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
		
		
		String sSql = "SELECT \n"+
        "T.CVE_GPO_EMPRESA,\n"+
        "T.CVE_EMPRESA,\n"+
        "T.ID_PRESTAMO,\n"+
        "T.NUM_PAGO_AMORTIZACION,\n"+
        "P.CVE_PRESTAMO,\n"+
        "P.ID_CLIENTE,\n"+
        "C.NOM_COMPLETO,\n"+
        "P.ID_PRODUCTO,\n"+
        "O.NOM_PRODUCTO,\n"+
        "P.NUM_CICLO,\n"+
        "T.NUM_PAGO_AMORTIZACION,\n"+
        "T.FECHA_AMORTIZACION,\n"+
        "TO_CHAR(T.IMP_SALDO_INICIAL,'999,999,999.9999') IMP_SALDO_INICIAL,\n"+
        "TO_CHAR(T.TASA_INTERES,'999,999,999.9999') TASA_INTERES,\n"+
        "TO_CHAR(T.IMP_INTERES,'999,999,999.9999') IMP_INTERES,\n"+
        "TO_CHAR(T.IMP_IVA_INTERES,'999,999,999.9999') IMP_IVA_INTERES,\n"+
        "TO_CHAR(T.IMP_INTERES_EXTRA,'999,999,999.9999') IMP_INTERES_EXTRA,\n"+
        "TO_CHAR(T.IMP_IVA_INTERES_EXTRA,'999,999,999.9999') IMP_IVA_INTERES_EXTRA,\n"+
        "TO_CHAR(T.IMP_CAPITAL_AMORT,'999,999,999.9999') IMP_CAPITAL_AMORT,\n"+
        "TO_CHAR(T.IMP_PAGO,'999,999,999.9999') IMP_PAGO,\n"+
        "TO_CHAR(T.IMP_SALDO_FINAL,'999,999,999.9999') IMP_SALDO_FINAL,\n"+
        "T.B_PAGO_PUNTUAL,\n"+
        "TO_CHAR(T.IMP_INTERES_PAGADO,'999,999,999.9999') IMP_INTERES_PAGADO,\n"+
        "TO_CHAR(T.IMP_IVA_INTERES_PAGADO,'999,999,999.9999') IMP_IVA_INTERES_PAGADO,\n"+
        "TO_CHAR(T.IMP_INTERES_EXTRA_PAGADO,'999,999,999.9999') IMP_INTERES_EXTRA_PAGADO,\n"+
        "TO_CHAR(T.IMP_IVA_INTERES_EXTRA_PAGADO,'999,999,999.9999') IMP_IVA_INTERES_EXTRA_PAGADO,\n"+
        "TO_CHAR(T.IMP_CAPITAL_AMORT_PAGADO,'999,999,999.9999') IMP_CAPITAL_AMORT_PAGADO,\n"+
        "TO_CHAR(T.IMP_PAGO_PAGADO,'999,999,999.9999') IMP_PAGO_PAGADO,\n"+
        "T.B_PAGADO,\n"+
        "T.FECHA_AMORT_PAGO_ULTIMO,\n"+
        "T.NUM_DIA_ATRASO,\n"+
        "TO_CHAR(T.IMP_PAGO_TARDIO,'999,999,999.9999') IMP_PAGO_TARDIO,\n"+
        "TO_CHAR(T.IMP_PAGO_TARDIO_PAGADO,'999,999,999.9999') IMP_PAGO_TARDIO_PAGADO,\n"+
        "TO_CHAR(T.IMP_INTERES_MORA,'999,999,999.9999') IMP_INTERES_MORA,\n"+
        "TO_CHAR(T.IMP_INTERES_MORA_PAGADO,'999,999,999.9999') IMP_INTERES_MORA_PAGADO,\n"+
        "TO_CHAR(T.IMP_IVA_INTERES_MORA,'999,999,999.9999') IMP_IVA_INTERES_MORA,\n"+
        "TO_CHAR(T.IMP_IVA_INTERES_MORA_PAGADO,'999,999,999.9999') IMP_IVA_INTERES_MORA_PAGADO,\n"+
        "T.F_VALOR_CALCULO,\n"+
        "TO_CHAR(T.IMP_INTERES_DEV_X_DIA,'999,999,999.9999') IMP_INTERES_DEV_X_DIA\n"+
        "FROM SIM_TABLA_AMORTIZACION T,\n"+
        "SIM_PRESTAMO P,\n"+
        "RS_GRAL_PERSONA C,\n"+
        "SIM_PRODUCTO O\n"+
        "WHERE T.CVE_GPO_EMPRESA ='" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
		"AND T.CVE_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "' \n"+
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
						
							 "ORDER BY T.ID_PRESTAMO, T.NUM_PAGO_AMORTIZACION\n";
							
							 System.out.println("*****************Paso por aqui****************:"+sSql);
		
	    String sTipoReporte = request.getParameter("TipoReporte");
	    System.out.println("TipoReporte:"+sTipoReporte);
		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("Portales/Sam/img/Imagentsys1.gif"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReporteTabAmorXls.jasper");
		parametros.put("NombreReporte", "rep"+sClave);
		                             
		                    }
		return parametros;		
	}
	
}
