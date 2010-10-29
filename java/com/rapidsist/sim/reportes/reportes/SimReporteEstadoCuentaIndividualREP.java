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
 * Realiza la consulta para obtener los datos que serï¿½n utilizados para generar el reporte del estado de cuenta individual.
 */
public class SimReporteEstadoCuentaIndividualREP implements ReporteControlIN {

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
	    "C.ID_PRESTAMO, \n"+
	    "C.Cve_Prestamo, \n"+ 
	    "C.Cve_Nombre, \n"+
	    "C.NOMBRE, \n"+
	    "C.Id_Producto, \n"+
	    "C.Nom_Producto, \n"+
	    "C.NOM_METODO, \n"+
	    "C.ID_PERIODICIDAD_PRODUCTO, \n"+
	    "C.PERIODICIDAD_PRODUCTO, \n"+
	    "C.Plazo, \n"+
	    "C.DIA_SEMANA_PAGO, \n"+
	    "C.Valor_Tasa, \n"+
	    "C.Id_Periodicidad_Tasa, \n"+
	    "C.Periodicidad_Tasa, \n"+
	    "C.ID_SUCURSAL, \n"+ 
	    "C.Nom_Sucursal, \n"+ 
	    "C.Tasa_Sucursal, \n"+
	    "Nvl(C.Fecha_Real,C.Fecha_EntRega) FECHA_INICIO, \n"+
	    "C.FECHA_FIN, \n"+
	    "C.MONTO_AUTORIZADO, \n"+
	    "C.CARGO_INICIAL, \n"+
	    "NVL(C.MONTO_AUTORIZADO,0) + NVL(C.CARGO_INICIAL,0) MONTO_PRESTADO, \n"+
	    "Aa.Pago_Interes, \n"+
	    "Ab.Imp_Pago_Hoy Pago_Capital, \n"+
	    "Ac.Imp_Pago_Hoy Pago_Seguro, \n"+
	    "Ad.Imp_Pago_Hoy Pago_Recargo, \n"+
	    "Ae.Imp_Saldo_Hoy, \n"+
	    "AF.IMP_DEBE_HOY \n"+
		  "From V_CREDITO C, \n"+
		 "(SELECT CVE_GPO_EMPRESA, \n"+
		 "     CVE_EMPRESA, \n"+
		  "    Id_Prestamo, \n"+
		  "    SUM(IMP_PAGO_HOY) PAGO_INTERES \n"+
		  "  FROM V_SIM_PRESTAMO_RES_EDO_CTA \n"+
		  "  WHERE DESC_MOVIMIENTO IN ('Interés', 'Interés Extra', 'Iva De Intereses', 'Iva Interes Extra', 'Pago Interés', 'Pago Interés Extra', 'Pago Iva De Intereses', 'Pago Iva Interes Extra') \n"+
		  "  GROUP BY CVE_GPO_EMPRESA, CVE_EMPRESA, Id_Prestamo \n"+
		  "  ORDER BY ID_PRESTAMO \n"+
		  "  ) AA, \n"+
		  "  (SELECT CVE_GPO_EMPRESA, \n"+
		  "    CVE_EMPRESA, \n"+
		  "    ID_PRESTAMO, \n"+
		  "    SUM(IMP_PAGO_HOY) IMP_PAGO_HOY \n"+
		  "  FROM V_SIM_PRESTAMO_RES_EDO_CTA \n"+
		  "  WHERE DESC_MOVIMIENTO IN ('Capital','Pago Capital') \n"+
		  "  GROUP BY CVE_GPO_EMPRESA, CVE_EMPRESA, ID_PRESTAMO \n"+
		  "  ORDER BY ID_PRESTAMO \n"+
		  "  ) AB, \n"+
		  "  (SELECT CVE_GPO_EMPRESA, \n"+
		  "    CVE_EMPRESA, \n"+
		  "    ID_PRESTAMO, \n"+
		  "    SUM(IMP_PAGO_HOY) IMP_PAGO_HOY \n"+
		  "  FROM V_SIM_PRESTAMO_RES_EDO_CTA \n"+
		  "  WHERE DESC_MOVIMIENTO IN ('Seguro Deudor','Pago Seguro Deudor') \n"+
		  "  GROUP BY CVE_GPO_EMPRESA, \n"+
		  "    CVE_EMPRESA, \n"+
		  "    ID_PRESTAMO \n"+
		  "  ORDER BY ID_PRESTAMO \n"+
		  "  ) AC, \n"+
		  "  (SELECT CVE_GPO_EMPRESA, \n"+
		  "    CVE_EMPRESA, \n"+
		  "    ID_PRESTAMO, \n"+
		  "    SUM(IMP_PAGO_HOY) IMP_PAGO_HOY \n"+
		  "  FROM V_SIM_PRESTAMO_RES_EDO_CTA \n"+
		  "  WHERE DESC_MOVIMIENTO IN ('Pago Tardío','Pago Pago Tardío') \n"+
		  "  GROUP BY CVE_GPO_EMPRESA, \n"+
		  "    CVE_EMPRESA, \n"+
		  "    ID_PRESTAMO \n"+
		  "  Order By Id_Prestamo \n"+
		  "  ) AD, \n"+
		  "  (SELECT CVE_GPO_EMPRESA, \n"+
		  "    CVE_EMPRESA, \n"+
		  "    Id_Prestamo, \n"+
		  "    SUM(IMP_SALDO_HOY) IMP_SALDO_HOY \n"+
		  "  From V_SIM_PRESTAMO_RES_EDO_CTA \n"+
		  "  WHERE DESC_MOVIMIENTO IN ('Pago Tardío','Pago Pago Tardío','Seguro Deudor','Pago Seguro Deudor','Capital','Pago Capital','Interés', 'Interés Extra', 'Iva De Intereses', 'Iva Interes Extra', 'Pago Interés', 'Pago Interés Extra', 'Pago Iva De Intereses', 'Pago Iva Interes Extra') \n"+
		  "  GROUP BY CVE_GPO_EMPRESA, CVE_EMPRESA, Id_Prestamo \n"+
		  "  Order By Id_Prestamo \n"+ 
		  "  ) AE, \n"+
		  "   (SELECT CVE_GPO_EMPRESA, \n"+
		  "    CVE_EMPRESA, \n"+ 
		  "    Id_Prestamo, \n"+
		  "    SUM(IMP_DEBE_HOY) IMP_DEBE_HOY \n"+
		  "  From V_SIM_PRESTAMO_RES_EDO_CTA \n"+
		  "  WHERE DESC_MOVIMIENTO IN ('Pago Tardío','Pago Pago Tardío','Seguro Deudor','Pago Seguro Deudor','Capital','Pago Capital','Interés', 'Interés Extra', 'Iva De Intereses', 'Iva Interes Extra', 'Pago Interés', 'Pago Interés Extra', 'Pago Iva De Intereses', 'Pago Iva Interes Extra') \n"+
		  "  GROUP BY CVE_GPO_EMPRESA, CVE_EMPRESA, Id_Prestamo \n"+
		  "  Order By Id_Prestamo \n"+
		  "  ) Af \n"+
		  "Where  Aplica_A != 'GRUPO' \n"+
		  "AND AA.CVE_GPO_EMPRESA (+)   = C.CVE_GPO_EMPRESA \n"+ 
		  "AND AA.CVE_EMPRESA (+)       = C.CVE_EMPRESA \n"+
		  "And Aa.Id_Prestamo (+)       = C.Id_Prestamo \n"+
		  "AND AB.CVE_GPO_EMPRESA (+)   = C.CVE_GPO_EMPRESA \n"+
		  "AND AB.CVE_EMPRESA (+)       = C.CVE_EMPRESA \n"+ 
		  "AND AB.ID_PRESTAMO (+)       = C.Id_Prestamo \n"+
		  "AND AC.CVE_GPO_EMPRESA (+)   = C.CVE_GPO_EMPRESA \n"+
		  "AND AC.CVE_EMPRESA (+)       = C.CVE_EMPRESA \n"+
		  "AND AC.ID_PRESTAMO (+)       = C.Id_Prestamo \n"+
		  "AND AD.CVE_GPO_EMPRESA (+)   = C.CVE_GPO_EMPRESA \n"+
		  "AND AD.CVE_EMPRESA (+)       = C.CVE_EMPRESA \n"+
		  "And Ad.Id_Prestamo (+)       = C.Id_Prestamo \n"+
		  "AND AE.CVE_GPO_EMPRESA (+)   = C.CVE_GPO_EMPRESA \n"+
		  "And AE.Cve_Empresa (+)       = C.Cve_Empresa \n"+ 
		  "And AE.Id_Prestamo (+)       = C.Id_Prestamo \n"+
		  "AND AF.CVE_GPO_EMPRESA (+)   = C.CVE_GPO_EMPRESA \n"+
		  "And AF.Cve_Empresa (+)       = C.Cve_Empresa \n"+
		  "And AF.Id_Prestamo (+)       = C.Id_Prestamo \n";
	 
		
		if (!sCvePrestamo.equals("")){
			sSql = sSql + "AND C.Cve_Prestamo = '" + (String)request.getParameter("CvePrestamo") + "'\n";
		}
	
		
							
	    String sTipoReporte = request.getParameter("TipoReporte");
		parametros.put("Sql", sSql);
		
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReporteEstadoCuentaIndividual.jasper");
		parametros.put("Subreporte1", contextoServlet.getRealPath("/Reportes/Sim/reportes/SimReporteEstadoCuentaMovIndividual.jasper"));
		parametros.put("NombreReporte", "Estado de Cuenta");
		                             
		
		return parametros;		
	}
}