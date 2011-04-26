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
 * Realiza la consulta para obtener los datos que serï¿½n utilizados para generar el reporte de catï¿½logo de aplicaciones.
 */
public class SimReporteMovimientosREP implements ReporteControlIN {

	/**
	 * Obtiene la consulta en la base de datos y parï¿½metros que serï¿½n utilizados por el reporte.
	 * @param parametrosCatalogo Parï¿½metros que se le envï¿½an al mï¿½todo .
	 * @param request Objeto que provee de informaciï¿½n al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envï¿½a como un parï¿½metro a este mï¿½todo.
	 * @param catalogoSL Instancia del Ejb CatalogoSL que ejecuta en la base de datos las
	 * operaciones especificadas en la clase CON.
	 * @param contextoServidor Objeto que contiene informaciï¿½n acerca del entorno del servidor de
	 * aplicaciones.
	 * @param contextoServlet Objeto que contiene informaciï¿½n acerca del entorno del servlet. 
	 * @return Map objeto que contiene los datos para generar el reporte.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public  Map getParametros(Registro parametrosCatalogo, HttpServletRequest request, CatalogoSL catalogoSL, Context contextoServidor, ServletContext contextoServlet)  throws Exception{
		Map parametros = new HashMap();

		String sCveGpoEmpresa = request.getParameter("CveGpoEmpresa");
		String sCveEmpresa = request.getParameter("CveEmpresa");
		String sClaveGrupo = request.getParameter("IdPrestamo");
		
		System.out.println("sClaveGrupo:"+sClaveGrupo);
		
        String sSql ="Select \n"+
						"M.Cve_Gpo_Empresa,\n"+
						"M.Cve_Empresa,\n"+
						"P.F_Medio,\n"+
						"M.F_Operacion,\n"+
						"M.F_Aplicacion,\n"+
						"M.Id_Prestamo,\n"+
						"PG.ID_GRUPO,\n"+
						"G.NOM_GRUPO,\n"+
						"PG.NUM_CICLO,\n"+
						"M.Desc_Movimiento,\n"+
						"'Aplicado' as Estatus,\n"+
						"CASE\n"+
						    "When DESC_MOVIMIENTO = 'Pago Capital'\n"+
						    "Then 'Efectivo'\n"+
						    "ELSE ''\n"+
						"END AS FORMA_PAGO,\n"+
						"'Identificado' as Identificado_NoIdentificado,\n"+
						"CASE\n"+
						      "When Sum(Nvl(Round(M.Imp_Pago,2),0)) = '0'\n"+
						      "Then Sum(Nvl(Round(M.Imp_Concepto,2),0))\n"+
						      "WHEN SUM(NVL(ROUND(M.Imp_Concepto,2),0)) = '0'\n"+
						     "Then Sum(Nvl(Round(M.Imp_Pago,2),0))\n"+
						    "End As Monto,\n"+
						"Case\n"+
						      "When M.Desc_Movimiento Like '%Capital%'\n"+
						        "Then Sum(Nvl(Round(M.Imp_Concepto,2),0))\n"+
						"End As Capital,\n"+
						
						"Case\n"+
						      "When M.Desc_Movimiento Like ('%Interés%')\n"+
						        "Then Sum(Nvl(Round(M.Imp_Concepto,2),0))\n"+
						      "When M.Desc_Movimiento Like ('%Interes%')\n"+
						        "Then SUM(NVL(ROUND(M.Imp_Concepto,2),0))\n"+
						"End As Interes,\n"+
						"Case\n"+
						      "When M.Desc_Movimiento Like ('%Seguro Deudor%')\n"+
						        "Then Sum(Nvl(Round(M.Imp_Concepto,2),0)) \n"+
						"end as Seguro,\n"+
						"Case\n"+
						      "When M.Desc_Movimiento Like ('%Pago Tardío%')\n"+
						        "Then Sum(Nvl(Round(M.Imp_Concepto,2),0))\n"+
						        
						"End As Recargo,\n"+
						"G.ID_SUCURSAL,\n"+
						"S.NOM_SUCURSAL,\n"+
						"S.ID_REGIONAL,\n"+
						"R.NOM_REGIONAL,\n"+
						"1 AS CVE_SECTOR,\n"+
						"'' AS NOM_SECTOR,\n"+
						"3 AS CVE_GIRO,\n"+
						"'' AS NOM_GIRO,\n"+
						"'Individual' TIPO_PRESTAMO,\n"+
						"PG.NUM_LINEA,\n"+
						"L.CVE_FONDEADOR,\n"+
						"F.NOM_FONDEADOR,\n"+
						"PG.CVE_ASESOR_CREDITO,\n"+
						"PA.NOM_COMPLETO NOM_ASESOR,\n"+
						"PG.ID_PRODUCTO,\n"+
						"PO.NOM_PRODUCTO,\n"+
						"'' AS RFC\n"+
						"From V_Mov_Edo_Cta_Gpo M,\n"+
						"Pfin_Parametro P,\n"+
						"SIM_PRESTAMO_GRUPO PG,\n"+
						"SIM_GRUPO G,\n"+
						"SIM_CAT_SUCURSAL S,\n"+
						"SIM_CAT_REGIONAL R,\n"+
						"SIM_CAT_FONDEADOR_LINEA L,\n"+
						"SIM_CAT_FONDEADOR F,\n"+
						"RS_GRAL_USUARIO U,\n"+
						"RS_GRAL_PERSONA PA,\n"+
						"SIM_PRODUCTO PO\n"+
						"WHERE M.F_OPERACION <= (SELECT  F_MEDIO\n"+  
											                    "FROM    PFIN_PARAMETRO\n"+  
											                    "WHERE   CVE_GPO_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+ 
											                    "And Cve_Empresa     = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "'\n"+  
											                    "AND CVE_MEDIO       = 'SYSTEM')\n"+ 
						"And M.Num_Pago_Amortizacion != 0\n"+
						"And P.Cve_Gpo_Empresa = M.Cve_Gpo_Empresa\n"+
						"And P.Cve_Empresa = M.Cve_Empresa\n"+
						"AND PG.CVE_GPO_EMPRESA = M.CVE_GPO_EMPRESA\n"+
						"AND PG.CVE_EMPRESA = M.CVE_EMPRESA\n"+
						"AND PG.ID_PRESTAMO_GRUPO = M.ID_PRESTAMO\n"+
						"AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA\n"+
						"AND G.CVE_EMPRESA = PG.CVE_EMPRESA\n"+
						"AND G.ID_GRUPO = PG.ID_GRUPO\n"+
						"AND S.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA\n"+
						"AND S.CVE_EMPRESA = G.CVE_EMPRESA\n"+
						"AND S.ID_SUCURSAL = G.ID_SUCURSAL\n"+
						"AND R.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA\n"+
						"AND R.CVE_EMPRESA = S.CVE_EMPRESA\n"+
						"AND R.ID_REGIONAL = S.ID_REGIONAL\n"+
						"AND L.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA\n"+
						"AND L.CVE_EMPRESA = PG.CVE_EMPRESA\n"+
						"AND L.NUM_LINEA = PG.NUM_LINEA\n"+
						"AND F.CVE_GPO_EMPRESA = L.CVE_GPO_EMPRESA\n"+
						"AND F.CVE_EMPRESA = L.CVE_EMPRESA\n"+
						"AND F.CVE_FONDEADOR = L.CVE_FONDEADOR\n"+
						"AND U.CVE_GPO_EMPRESA (+) = PG.CVE_GPO_EMPRESA\n"+
						"AND U.CVE_EMPRESA (+) = PG.CVE_EMPRESA\n"+
						"AND U.CVE_USUARIO (+) = PG.CVE_ASESOR_CREDITO\n"+
						"AND PA.CVE_GPO_EMPRESA (+) = U.CVE_GPO_EMPRESA\n"+
						"AND PA.CVE_EMPRESA (+) = U.CVE_EMPRESA\n"+
						"AND PA.ID_PERSONA (+) = U.ID_PERSONA\n"+
						"AND PO.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA\n"+
						"AND PO.CVE_EMPRESA = PG.CVE_EMPRESA\n"+
						"AND PO.ID_PRODUCTO = PG.ID_PRODUCTO\n"+
						
						"GROUP BY M.Cve_Gpo_Empresa, M.Cve_Empresa, P.F_Medio, M.F_Operacion, M.F_Aplicacion, M.Id_Prestamo, PG.ID_GRUPO, G.NOM_GRUPO, PG.NUM_CICLO, M.Desc_Movimiento, 'Aplicado', CASE\n"+
						    "When DESC_MOVIMIENTO = 'Pago Capital'\n"+
						    "Then 'Efectivo'\n"+
						    "ELSE ''\n"+
						"END, 'Identificado', G.ID_SUCURSAL, S.NOM_SUCURSAL, S.ID_REGIONAL, R.NOM_REGIONAL, '', '', '', '', PG.NUM_LINEA, L.CVE_FONDEADOR, F.NOM_FONDEADOR, PG.CVE_ASESOR_CREDITO, PA.NOM_COMPLETO,\n"+ 
						"PG.ID_PRODUCTO, PO.NOM_PRODUCTO\n"+
						
						"UNION ALL\n"+
						
						"Select \n"+
						"M.cve_gpo_Empresa,\n"+
						"M.Cve_Empresa,\n"+
						"P.F_MEDIO,\n"+
						"M.F_Operacion,\n"+
						"M.F_Aplicacion,\n"+
						"M.Id_Prestamo,\n"+
						"PI.ID_CLIENTE,\n"+
						"NC.NOM_COMPLETO,\n"+
						"PI.NUM_CICLO,\n"+
						"M.Desc_Movimiento,\n"+
						"'Aplicado' as Estatus,\n"+
						"CASE\n"+
						    "When DESC_MOVIMIENTO = 'Pago Capital'\n"+
						    "Then 'Efectivo'\n"+
						    "ELSE ''\n"+
						"END AS FORMA_PAGO,\n"+
						"'Identificado' as Identificado_NoIdentificado,\n"+
						"CASE\n"+
						      "When M.Imp_Pago = '0'\n"+
						      "Then M.Imp_Concepto\n"+
						      "When M.Imp_Concepto = '0'\n"+
						     "Then M.Imp_Pago\n"+
						    "End As Monto,\n"+
						"Case\n"+
						      "When M.Desc_Movimiento Like '%Capital%'\n"+
						        "Then M.Imp_Concepto\n"+
						"End As Capital,\n"+
						
						"Case\n"+
						      "When M.Desc_Movimiento Like ('%Interés%')\n"+
						        "Then M.Imp_Concepto\n"+
						      "When M.Desc_Movimiento Like ('%Interes%')\n"+
						        "Then M.Imp_Concepto\n"+
						"end as interes,\n"+
						"Case\n"+
						      "When M.Desc_Movimiento Like ('%Seguro Deudor%')\n"+
						        "Then M.Imp_Concepto  \n"+
						"end as Seguro,\n"+
						"Case\n"+
						      "When M.Desc_Movimiento Like ('%Pago Tardío%')\n"+
						        "Then M.Imp_Concepto\n"+
						        
						"End As Recargo,\n"+
						"PI.ID_SUCURSAL,\n"+
						"S.NOM_SUCURSAL,\n"+
						"S.ID_REGIONAL,\n"+
						"R.NOM_REGIONAL,\n"+
						"N.CVE_CLASE,\n"+
						"C.NOM_CLASE,\n"+
						"C.CVE_SUB_RAMA,\n"+
						"SR.NOM_SUB_RAMA,\n"+
						"'Grupal' TIPO_PRESTAMO,\n"+
						"PI.NUM_LINEA,\n"+
						"L.CVE_FONDEADOR,\n"+
						"F.NOM_FONDEADOR,\n"+
						"PI.CVE_ASESOR_CREDITO,\n"+
						"PA.NOM_COMPLETO NOM_ASESOR,\n"+
						"PI.ID_PRODUCTO,\n"+
						"PO.NOM_PRODUCTO,\n"+
						"NC.RFC\n"+
						"From V_Mov_Edo_Cta_Ind M,\n"+
						"Pfin_Parametro P,\n"+
						"SIM_PRESTAMO PI,\n"+
						"RS_GRAL_PERSONA NC,\n"+
						"SIM_CAT_SUCURSAL S,\n"+
						"SIM_CAT_REGIONAL R,\n"+
						"SIM_CLIENTE_NEGOCIO N,\n"+
						"SIM_CAT_CLASE C,\n"+
						"SIM_CAT_SUB_RAMA SR,\n"+
						"SIM_CAT_FONDEADOR_LINEA L,\n"+
						"SIM_CAT_FONDEADOR F,\n"+
						"RS_GRAL_USUARIO U,\n"+
						"RS_GRAL_PERSONA PA,\n"+
						"SIM_PRODUCTO PO\n"+
						"WHERE M.F_OPERACION <= (SELECT  F_MEDIO\n"+  
											                    "FROM    PFIN_PARAMETRO\n"+  
											                    "Where   Cve_Gpo_Empresa = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+ 
											                    "And Cve_Empresa     = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "'\n"+  
											                    "And Cve_Medio       = 'SYSTEM')\n"+ 
						"And M.Num_Pago_Amortizacion != 0\n"+
						"And P.Cve_Gpo_Empresa = M.Cve_Gpo_Empresa\n"+
						"And P.Cve_Empresa = M.Cve_Empresa\n"+
						"AND PI.CVE_GPO_EMPRESA = M.CVE_GPO_EMPRESA\n"+
						"AND PI.CVE_EMPRESA = M.CVE_EMPRESA\n"+
						"AND PI.ID_PRESTAMO = M.ID_PRESTAMO\n"+
						"AND PI.ID_GRUPO IS NULL\n"+
						"AND NC.CVE_GPO_EMPRESA = PI.CVE_GPO_EMPRESA\n"+
						"AND NC.CVE_EMPRESA = PI.CVE_EMPRESA\n"+
						"AND NC.ID_PERSONA = PI.ID_CLIENTE\n"+
						"AND S.CVE_GPO_EMPRESA = PI.CVE_GPO_EMPRESA\n"+
						"AND S.CVE_EMPRESA = PI.CVE_EMPRESA\n"+
						"AND S.ID_SUCURSAL = PI.ID_SUCURSAL\n"+
						"AND R.CVE_GPO_EMPRESA = S.CVE_GPO_EMPRESA\n"+
						"AND R.CVE_EMPRESA = S.CVE_EMPRESA\n"+
						"AND R.ID_REGIONAL = S.ID_REGIONAL\n"+
						"AND N.CVE_GPO_EMPRESA = PI.CVE_GPO_EMPRESA\n"+
						"AND N.CVE_EMPRESA = PI.CVE_EMPRESA\n"+
						"AND N.ID_PERSONA = PI.ID_CLIENTE\n"+
						"AND N.B_PRINCIPAL = 'V'\n"+
						"AND C.CVE_GPO_EMPRESA = N.CVE_GPO_EMPRESA\n"+
						"AND C.CVE_EMPRESA = N.CVE_EMPRESA\n"+
						"AND C.CVE_CLASE = N.CVE_CLASE\n"+
						"AND SR.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA\n"+
						"AND SR.CVE_EMPRESA = C.CVE_EMPRESA\n"+
						"AND SR.CVE_SUB_RAMA = C.CVE_SUB_RAMA\n"+
						"AND L.CVE_GPO_EMPRESA = PI.CVE_GPO_EMPRESA\n"+
						"AND L.CVE_EMPRESA = PI.CVE_EMPRESA\n"+
						"AND L.NUM_LINEA = PI.NUM_LINEA\n"+
						"AND F.CVE_GPO_EMPRESA = L.CVE_GPO_EMPRESA\n"+
						"AND F.CVE_EMPRESA = L.CVE_EMPRESA\n"+
						"AND F.CVE_FONDEADOR = L.CVE_FONDEADOR\n"+
						"AND U.CVE_GPO_EMPRESA (+) = PI.CVE_GPO_EMPRESA\n"+
						"AND U.CVE_EMPRESA (+) = PI.CVE_EMPRESA\n"+
						"AND U.CVE_USUARIO (+) = PI.CVE_ASESOR_CREDITO\n"+
						"AND PA.CVE_GPO_EMPRESA (+) = U.CVE_GPO_EMPRESA\n"+
						"AND PA.CVE_EMPRESA (+) = U.CVE_EMPRESA\n"+
						"AND PA.ID_PERSONA (+) = U.ID_PERSONA\n"+
						"AND PO.CVE_GPO_EMPRESA = PI.CVE_GPO_EMPRESA\n"+
						"AND PO.CVE_EMPRESA = PI.CVE_EMPRESA\n"+
						"AND PO.ID_PRODUCTO = PI.ID_PRODUCTO\n"+
						"ORDER BY ID_PRESTAMO\n";
        
							 System.out.println("*****************Paso por aqui****************:"+sSql);
		
	    String sTipoReporte = request.getParameter("TipoReporte");
	    System.out.println("TipoReporte:"+sTipoReporte);
		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("Portales/Sam/img/Imagentsys1.gif"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReporteMovimientos.jasper");
		parametros.put("NombreReporte", "Reporte"+sClaveGrupo);
		
		return parametros;		
	}
}
