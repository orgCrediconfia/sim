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
public class SimTablaAmortAcceGpoREP implements ReporteControlIN {

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

        String sClaveGrupo = request.getParameter("CvePrestamoGrupo");
		
		System.out.println("sClaveGrupo:"+sClaveGrupo);
		
		String sSql = "SELECT \n"+
							 "T.CVE_GPO_EMPRESA,\n"+
							 "T.CVE_EMPRESA,\n"+
							 "GT.ID_PRESTAMO_GRUPO,\n"+
							 "PG.CVE_PRESTAMO_GRUPO,\n"+
							 "G.NOM_GRUPO,\n"+
							 "P.NOM_PRODUCTO,\n"+
							 "PG.NUM_CICLO,\n"+
							 "T.NUM_PAGO_AMORTIZACION,\n"+
							 "A.ID_ACCESORIO,\n"+
							 "N.NOM_ACCESORIO,\n"+
							 "SUM(A.IMP_ACCESORIO) IMP_ACCESORIO,\n"+
							 "SUM(A.IMP_IVA_ACCESORIO) IMP_IVA_ACCESORIO,\n"+
							 "SUM(A.IMP_ACCESORIO_PAGADO) IMP_ACCESORIO_PAGADO,\n"+
							 "SUM(A.IMP_IVA_ACCESORIO_PAGADO) IMP_IVA_ACCESORIO_PAGADO\n"+
							 "FROM SIM_PRESTAMO_GPO_DET GT,\n"+
							 "SIM_TABLA_AMORTIZACION T,\n"+
							 "SIM_TABLA_AMORT_ACCESORIO A,\n"+
							 "SIM_CAT_ACCESORIO N,\n"+
							 "SIM_PRESTAMO_GRUPO PG,\n"+
							 "SIM_GRUPO G,\n"+
							 "SIM_PRODUCTO P\n"+
							 "WHERE GT.CVE_GPO_EMPRESA = 'SIM'\n"+
							 "AND GT.CVE_EMPRESA = 'CREDICONFIA'\n"+
							 "AND T.CVE_GPO_EMPRESA = GT.CVE_GPO_EMPRESA\n"+
							 "AND T.CVE_EMPRESA = GT.CVE_EMPRESA\n"+
							 "AND T.ID_PRESTAMO = GT.ID_PRESTAMO\n"+
							 "AND A.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA\n"+
							 "AND A.CVE_EMPRESA = T.CVE_EMPRESA\n"+
							 "AND A.ID_PRESTAMO = T.ID_PRESTAMO\n"+
							 "AND A.NUM_PAGO_AMORTIZACION = T.NUM_PAGO_AMORTIZACION\n"+
							 "AND N.CVE_GPO_EMPRESA = A.CVE_GPO_EMPRESA\n"+
							 "AND N.CVE_EMPRESA = A.CVE_EMPRESA\n"+
							 "AND N.ID_ACCESORIO = A.ID_ACCESORIO\n"+
							 "AND PG.CVE_GPO_EMPRESA = GT.CVE_GPO_EMPRESA\n"+
							 "AND PG.CVE_EMPRESA = GT.CVE_EMPRESA\n"+
							 "AND PG.ID_PRESTAMO_GRUPO = GT.ID_PRESTAMO_GRUPO\n"+
							 "AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA\n"+
							 "AND G.CVE_EMPRESA = PG.CVE_EMPRESA\n"+
							 "AND G.ID_GRUPO = PG.ID_GRUPO\n"+
							 "AND P.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA\n"+
							 "AND P.CVE_EMPRESA = PG.CVE_EMPRESA\n"+
							 "AND P.ID_PRODUCTO = PG.ID_PRODUCTO\n";
							
	                            if (sClaveGrupo != null && !sClaveGrupo.equals("") && !sClaveGrupo.equals("null") ){								
									sSql = sSql + "AND PG.CVE_PRESTAMO_GRUPO = '" + sClaveGrupo + "' \n";
								}	
					
							 sSql = sSql+
							 
							 "GROUP BY\n"+
							 "T.CVE_GPO_EMPRESA,\n"+
							 "T.CVE_EMPRESA,\n"+
							 "GT.ID_PRESTAMO_GRUPO,\n"+
							 "PG.CVE_PRESTAMO_GRUPO,\n"+
							 "G.NOM_GRUPO,\n"+
							 "P.NOM_PRODUCTO,\n"+
							 "PG.NUM_CICLO,\n"+
							 "T.NUM_PAGO_AMORTIZACION,\n"+
							 "T.FECHA_AMORTIZACION,\n"+
							 "T.TASA_INTERES,\n"+
							 "A.ID_ACCESORIO,\n"+
							 "N.NOM_ACCESORIO\n"+
							 "ORDER BY GT.ID_PRESTAMO_GRUPO,A.ID_ACCESORIO, T.NUM_PAGO_AMORTIZACION \n";
							 
							 System.out.println("*****************Paso por aqui****************:"+sSql);
		
			    String sTipoReporte = request.getParameter("TipoReporte");
			    System.out.println("TipoReporte:"+sTipoReporte);
				parametros.put("Sql", sSql);
				parametros.put("PathLogotipo", contextoServlet.getRealPath("Portales/Sam/img/Imagentsys1.gif"));
				parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
				parametros.put("NomReporte", "/Reportes/Sim/reportes/SimTablaAmortAcceGpoXLS.jasper");
				parametros.put("NombreReporte", "rep"+sClaveGrupo);
		
		return parametros;		
	}
}
