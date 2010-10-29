/**
 * Sistema Central de Control.
 *
 */

package com.rapidsist.sim.prestamo.reportes;

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
 * Esta clase se encarga de administrar la operación consulta del Reporte de líneas de fondeo.
 * Esta clase es llamada por el servlet ProcesaReporteS.
 */

public class SimPrestamoReporteLineaFondeoREP implements ReporteControlIN {

	public Map getParametros(Registro parametrosCatalogo, HttpServletRequest request, CatalogoSL catalogoSL, Context contextoServidor, ServletContext contextoServlet)  throws Exception{
		Map parametros = new HashMap();
		
		//OBTIENE LOS DATOS DEL USUARIO
		HttpSession session = request.getSession(true);
		Usuario usuario = (Usuario)session.getAttribute("Usuario");
		String sCveUsuario = usuario.sCveUsuario;

		
		
		String sSql =   "SELECT \n"+
						"C.ID_PRESTAMO, \n"+
						"C.CVE_PRESTAMO, \n"+
						"C.NOMBRE, \n"+
						"C.NUM_LINEA, \n"+
						"C.MONTO_AUTORIZADO + C.CARGO_INICIAL MONTO_PRESTADO, \n"+
						"AE.IMP_SALDO_TOTAL \n"+
						" FROM V_CREDITO C, \n"+
						"SIM_USUARIO_ACCESO_SUCURSAL US, \n"+
						"(SELECT CVE_GPO_EMPRESA, \n"+
						"	     CVE_EMPRESA, \n"+
						"	     ID_PRESTAMO, \n"+
						"	     SUM(IMP_SALDO_HOY) IMP_SALDO_TOTAL \n"+
						"From V_Sim_Prestamo_Gpo_Res_Edo_Cta \n"+
						"WHERE DESC_MOVIMIENTO IN ('Interés', 'Interés Extra', 'Iva De Intereses', 'Iva Interes Extra', 'Pago Interés', 'Pago Interés Extra', 'Pago Iva De Intereses', 'Pago Iva Interes Extra','Capital', 'Pago Capital','Seguro Deudor','Pago Seguro Deudor','Pago Tardío','Pago Pago Tardío') \n"+
						"GROUP BY CVE_GPO_EMPRESA, \n"+
						"CVE_EMPRESA, \n"+
						"ID_PRESTAMO \n"+
						"Order By Id_Prestamo \n"+
						") AE \n"+
						
						"WHERE C.CVE_GPO_EMPRESA = 'SIM' \n"+
						"AND C.CVE_EMPRESA = 'CREDICONFIA' \n"+
						"AND C.APLICA_A = 'GRUPO' \n"+
						"AND US.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
				        "AND US.CVE_EMPRESA = C.CVE_EMPRESA \n"+
				        "AND US.ID_SUCURSAL = C.ID_SUCURSAL \n"+
				        "AND US.CVE_USUARIO = '" + sCveUsuario + "' \n"+
				        "AND AE.CVE_GPO_EMPRESA (+)   = C.CVE_GPO_EMPRESA \n"+
				        "AND AE.CVE_EMPRESA (+)       = C.CVE_EMPRESA \n"+
				        "AND AE.ID_PRESTAMO (+)       = C.ID_PRESTAMO \n";
		
		if (request.getParameter("IdLinea") != null && !request.getParameter("IdLinea").equals("") && !request.getParameter("IdLinea").equals("null") ){
			sSql = sSql + " AND C.NUM_LINEA = '" + request.getParameter("IdLinea")+"'  \n";
		}	
		
		sSql = sSql + "UNION \n"+
						"SELECT \n"+
						"C.ID_PRESTAMO, \n"+
						"C.CVE_PRESTAMO, \n"+
						"C.NOMBRE, \n"+
						"C.NUM_LINEA, \n"+
						"C.MONTO_AUTORIZADO + C.CARGO_INICIAL MONTO_PRESTADO, \n"+
						"AE.IMP_SALDO_TOTAL \n"+
						" FROM V_CREDITO C, \n"+
						"SIM_USUARIO_ACCESO_SUCURSAL US, \n"+
						"(SELECT CVE_GPO_EMPRESA, \n"+
						"	     CVE_EMPRESA, \n"+
						"	     ID_PRESTAMO, \n"+
						"	     SUM(IMP_SALDO_HOY) IMP_SALDO_TOTAL \n"+
						"From V_SIM_PRESTAMO_RES_EDO_CTA \n"+
						"WHERE DESC_MOVIMIENTO IN ('Interés', 'Interés Extra', 'Iva De Intereses', 'Iva Interes Extra', 'Pago Interés', 'Pago Interés Extra', 'Pago Iva De Intereses', 'Pago Iva Interes Extra','Capital', 'Pago Capital','Seguro Deudor','Pago Seguro Deudor','Pago Tardío','Pago Pago Tardío') \n"+
						"GROUP BY CVE_GPO_EMPRESA, \n"+
						"CVE_EMPRESA, \n"+
						"ID_PRESTAMO \n"+
						"Order By Id_Prestamo \n"+
						") AE \n"+
						
						"WHERE C.CVE_GPO_EMPRESA = 'SIM' \n"+
						"AND C.CVE_EMPRESA = 'CREDICONFIA' \n"+
						"AND C.APLICA_A = 'INDIVIDUAL' \n"+
						"AND US.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
				        "AND US.CVE_EMPRESA = C.CVE_EMPRESA \n"+
				        "AND US.ID_SUCURSAL = C.ID_SUCURSAL \n"+
				        "AND US.CVE_USUARIO = '" + sCveUsuario + "' \n"+
				        "AND AE.CVE_GPO_EMPRESA (+)   = C.CVE_GPO_EMPRESA \n"+
				        "AND AE.CVE_EMPRESA (+)       = C.CVE_EMPRESA \n"+
				        "AND AE.ID_PRESTAMO (+)       = C.ID_PRESTAMO \n";
				
				if (request.getParameter("IdLinea") != null && !request.getParameter("IdLinea").equals("") && !request.getParameter("IdLinea").equals("null") ){
				sSql = sSql + " AND C.NUM_LINEA = '" + request.getParameter("IdLinea")+"'  \n";
				}	
		
		//sSql = sSql + " ORDER BY C.NUM_LINEA, C.NOMBRE \n";
		
		System.out.println("líneas de fondeo"+sSql);
		
		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("/Portales/Sim/CrediConfia/img/CrediConfia.bmp"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/prestamo/SimPrestamoReporteLineaFondeo.jasper");
		return parametros;		
	}
}