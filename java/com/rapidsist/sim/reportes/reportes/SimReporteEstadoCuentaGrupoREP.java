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
 * Realiza la consulta para obtener los datos que serï¿½n utilizados para generar el reporte del estado de cuenta grupal.
 */
public class SimReporteEstadoCuentaGrupoREP implements ReporteControlIN {

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

		String sCvePrestamo = request.getParameter("CvePrestamo");
		
		String sSql = 	"SELECT  \n"+
		"C.CVE_GPO_EMPRESA, \n"+
	    "C.CVE_EMPRESA, \n"+
	    "TO_CHAR(P.F_MEDIO,'DD/MM/YYYY') F_MEDIO, \n"+
	    "C.ID_PRESTAMO, \n"+
	    "C.Cve_Prestamo, \n"+ 
	    "C.Cve_Nombre, \n"+
	    "C.NOMBRE, \n"+
	    "C.Id_Producto, \n"+
	    "C.NUM_CICLO, \n"+
	    "C.Nom_Producto, \n"+
	    "C.NOM_METODO, \n"+
	    "C.ID_PERIODICIDAD_PRODUCTO, \n"+
	    "C.PERIODICIDAD_PRODUCTO, \n"+
	    "C.Plazo, \n"+
	    "C.DIA_SEMANA_PAGO, \n"+
	    "C.Valor_Tasa, \n"+
	    "C.Id_Periodicidad_Tasa, \n"+
	    "C.Periodicidad_Tasa, \n"+
	    "Case \n"+
	    "When C.Periodicidad_Tasa = 'Mensual' \n"+
	    "Then C.Valor_Tasa * 12 \n"+
	    "Else (C.VALOR_TASA / (Select Dias \n"+ 
	    "      From Sim_Cat_Periodicidad \n"+
	    "      Where Id_Periodicidad = (Select Id_Periodicidad_Tasa \n"+ 
	    "      From V_Credito \n"+
	    "      Where Cve_Prestamo = '" + (String)request.getParameter("CvePrestamo") + "')) * 30) *12 \n"+
	    "END As TASA_ANUAL, \n"+
	    "C.ID_SUCURSAL, \n"+ 
	    "C.Nom_Sucursal, \n"+ 
	    "C.DIRECCION_SUCURSAL, \n"+ 
	    "C.Tasa_Sucursal, \n"+
	    "TO_CHAR(Nvl(C.Fecha_Real,C.Fecha_Entrega),'DD/MM/YYYY') FECHA_INICIO, \n"+
	    "C.FECHA_FIN, \n"+
	    "C.MONTO_AUTORIZADO, \n"+
	    "C.CARGO_INICIAL, \n"+
	    "NVL(C.MONTO_AUTORIZADO,0) + NVL(C.CARGO_INICIAL,0) MONTO_PRESTADO, \n"+
	    "AA.CUOTA_INTERES, \n"+
		"AB.CUOTA_CAPITAL, \n"+
		"AC.CUOTA_ACCESORIO, \n"+
	    "Ad.Imp_Pago_Hoy Pago_Recargo, \n"+
	    "AE.IMP_SALDO_HOY, \n"+
		"  CASE \n"+
		"    WHEN AE.IMP_SALDO_HOY > 0 \n"+
		"    THEN 0 \n"+
		"    ELSE AE.IMP_SALDO_HOY \n"+
		"  END As SALDO_VENCIDO, \n"+
		"AF.PAGOS, \n"+
		"NVL(AG.SALDO_CUENTA,0) SALDO_CUENTA \n"+
		  "From V_CREDITO C, \n"+
		"PFIN_PARAMETRO P, \n"+
		 " (SELECT CVE_GPO_EMPRESA, \n"+
		"		    CVE_EMPRESA, \n"+
		"		    ID_PRESTAMO_GRUPO, \n"+
		"		    AVG(INTERES) CUOTA_INTERES \n"+
		"		  FROM V_TABLA_AMORTIZACION_GRUPAL \n"+
		"		  GROUP BY CVE_GPO_EMPRESA, CVE_EMPRESA, ID_PRESTAMO_GRUPO \n"+
		"		  ORDER BY ID_PRESTAMO_GRUPO \n"+
		"		  ) AA, \n"+
		"		  (SELECT CVE_GPO_EMPRESA, \n"+
		"		    CVE_EMPRESA, \n"+
		"		    ID_PRESTAMO_GRUPO, \n"+
		"		    AVG(IMP_CAPITAL_AMORT) CUOTA_CAPITAL \n"+
		"		  FROM V_TABLA_AMORTIZACION_GRUPAL \n"+
		"		  GROUP BY CVE_GPO_EMPRESA, CVE_EMPRESA, ID_PRESTAMO_GRUPO \n"+
		"		  ORDER BY ID_PRESTAMO_GRUPO \n"+
		"		  ) AB, \n"+
		"		  (SELECT CVE_GPO_EMPRESA, \n"+
		"		    CVE_EMPRESA, \n"+
		"		    ID_PRESTAMO_GRUPO, \n"+
		"		    AVG(IMP_ACCESORIO) CUOTA_ACCESORIO \n"+
		"		  FROM V_TABLA_AMORTIZACION_GRUPAL \n"+
		"		   GROUP BY CVE_GPO_EMPRESA, CVE_EMPRESA, ID_PRESTAMO_GRUPO \n"+
		"		  ORDER BY ID_PRESTAMO_GRUPO \n"+
		"		  ) AC, \n"+
		  "  (SELECT CVE_GPO_EMPRESA, \n"+
		  "    CVE_EMPRESA, \n"+
		  "    ID_PRESTAMO, \n"+
		  "    SUM(IMP_PAGO_HOY) IMP_PAGO_HOY \n"+
		  "  FROM V_SIM_PRESTAMO_GPO_RES_EDO_CTA \n"+
		  "  WHERE DESC_MOVIMIENTO IN ('Pago Tardío','Pago Pago Tardío') \n"+
		  "  GROUP BY CVE_GPO_EMPRESA, \n"+
		  "    CVE_EMPRESA, \n"+
		  "    ID_PRESTAMO \n"+
		  "  Order By Id_Prestamo \n"+
		  "  ) AD, \n"+
		  "  (Select \n"+
		  "M.Cve_Gpo_Empresa, \n"+
		  "M.Cve_Empresa, \n"+
		  "M.Id_Prestamo, \n"+
		  "CASE \n"+
		  "      When Sum(Nvl(Round(M.Imp_Pago,2),0)) = '0' \n"+
		  "      Then Sum(Nvl(Round(M.Imp_Concepto,2),0)) \n"+
		  "      WHEN SUM(NVL(ROUND(M.Imp_Concepto,2),0)) = '0' \n"+
		  "     Then Sum(Nvl(Round(M.Imp_Pago,2),0)) \n"+
		  "    End As IMP_SALDO_HOY \n"+
		  "From V_Mov_Edo_Cta_Gpo M \n"+
		  "WHERE M.F_OPERACION <= (SELECT  F_MEDIO  \n"+
		  "					                    FROM    PFIN_PARAMETRO  \n"+
		  "					                    WHERE   CVE_GPO_EMPRESA = 'SIM' \n"+
		  "					                    And Cve_Empresa     = 'CREDICONFIA' \n"+ 
		  "					                    AND CVE_MEDIO       = 'SYSTEM') \n"+
		  "And M.Num_Pago_Amortizacion != 0 \n"+
		  "AND (M.IMP_PAGO = '0' AND M.Desc_Movimiento != 'Pago De Prestamo') \n"+
		  "GROUP BY M.Cve_Gpo_Empresa, M.Cve_Empresa, M.Id_Prestamo \n"+
		  "  ) AE, \n"+
		  "(SELECT CVE_GPO_EMPRESA, \n"+
		  "	       CVE_EMPRESA, \n"+
		  "	       ID_PRESTAMO_GRUPO, \n"+
		  "	       COUNT(FECHA_AMORTIZACION) PAGOS \n"+
		  "	FROM V_TABLA_AMORTIZACION_GRUPAL \n"+
		  "	WHERE FECHA_AMORTIZACION <= (SELECT F_MEDIO FROM PFIN_PARAMETRO WHERE CVE_GPO_EMPRESA = 'SIM' AND CVE_EMPRESA = 'CREDICONFIA') \n"+
		  "	GROUP BY CVE_GPO_EMPRESA, CVE_EMPRESA, ID_PRESTAMO_GRUPO \n"+
		  "	ORDER BY ID_PRESTAMO_GRUPO) AF, \n"+
		  "(SELECT \n"+
		  "			 PG.CVE_GPO_EMPRESA, \n"+
	      "   PG.CVE_EMPRESA, \n"+
	      "   PG.ID_PRESTAMO_GRUPO, TO_CHAR(sum(s.sdo_efectivo),'999,999,999.99') SALDO_CUENTA  \n"+
		  "				from sim_prestamo_gpo_det pg,  \n"+
		  "				sim_prestamo p, \n"+
		  "				pfin_saldo s \n"+
		  "				where pg.cve_gpo_empresa =  p.cve_gpo_empresa \n"+
		  "				and pg.cve_empresa =  p.cve_empresa  \n"+
		  "				and pg.id_prestamo = p.id_prestamo \n"+
		  "				and s.cve_gpo_empresa = p.cve_gpo_empresa \n"+
		  "				and s.cve_empresa = p.cve_empresa \n"+
		  "				and s.id_cuenta = p.id_cuenta_referencia GROUP BY PG.CVE_GPO_EMPRESA, PG.CVE_EMPRESA, PG.ID_PRESTAMO_GRUPO)AG \n"+
		  "Where  Aplica_A = 'GRUPO' \n"+
		  "AND P.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
		  "AND P.CVE_EMPRESA = P.CVE_EMPRESA \n"+
		  "AND AA.CVE_GPO_EMPRESA (+)   = C.CVE_GPO_EMPRESA \n"+ 
		  "AND AA.CVE_EMPRESA (+)       = C.CVE_EMPRESA \n"+
		  "And AA.ID_PRESTAMO_GRUPO (+)       = C.Id_Prestamo \n"+
		  "AND AB.CVE_GPO_EMPRESA (+)   = C.CVE_GPO_EMPRESA \n"+
		  "AND AB.CVE_EMPRESA (+)       = C.CVE_EMPRESA \n"+ 
		  "AND AB.ID_PRESTAMO_GRUPO (+)       = C.Id_Prestamo \n"+
		  "AND AC.CVE_GPO_EMPRESA (+)   = C.CVE_GPO_EMPRESA \n"+
		  "AND AC.CVE_EMPRESA (+)       = C.CVE_EMPRESA \n"+
		  "AND AC.ID_PRESTAMO_GRUPO (+)       = C.Id_Prestamo \n"+
		  "AND AD.CVE_GPO_EMPRESA (+)   = C.CVE_GPO_EMPRESA \n"+
		  "AND AD.CVE_EMPRESA (+)       = C.CVE_EMPRESA \n"+
		  "And Ad.Id_Prestamo (+)       = C.Id_Prestamo \n"+
		  "AND AE.CVE_GPO_EMPRESA (+)   = C.CVE_GPO_EMPRESA \n"+
		  "And AE.Cve_Empresa (+)       = C.Cve_Empresa \n"+ 
		  "And AE.Id_Prestamo (+)       = C.Id_Prestamo \n"+
		  "AND AF.CVE_GPO_EMPRESA (+)   = C.CVE_GPO_EMPRESA \n"+
		  "And AF.Cve_Empresa (+)       = C.Cve_Empresa \n"+
		  "And AF.ID_PRESTAMO_GRUPO (+) = C.Id_Prestamo \n"+
		  "AND AG.CVE_GPO_EMPRESA (+)   = C.CVE_GPO_EMPRESA \n"+
		  "And AG.Cve_Empresa (+)       = C.Cve_Empresa \n"+
		  "And AG.ID_PRESTAMO_GRUPO (+) = C.Id_Prestamo \n";
	 
		
		if (!sCvePrestamo.equals("")){
			sSql = sSql + "AND C.Cve_Prestamo = '" + (String)request.getParameter("CvePrestamo") + "'\n";
		}
	
		System.out.println("Estado de cuenta grupal"+sSql);
							
	    String sTipoReporte = request.getParameter("TipoReporte");
		parametros.put("Sql", sSql);
		
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("PathLogotipo", contextoServlet.getRealPath("/Portales/Sim/CrediConfia/img/CrediConfia.bmp"));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReporteEstadoCuentaGrupo.jasper");
		parametros.put("Subreporte1", contextoServlet.getRealPath("/Reportes/Sim/reportes/SimReporteEstadoCuentaMovGrupo.jasper"));
		parametros.put("Subreporte2", contextoServlet.getRealPath("/Reportes/Sim/reportes/SimReporteEstadoCuentaResumenGrupo.jasper"));
		parametros.put("NombreReporte", "Estado de Cuenta");
		                             
		
		return parametros;	
	}
}