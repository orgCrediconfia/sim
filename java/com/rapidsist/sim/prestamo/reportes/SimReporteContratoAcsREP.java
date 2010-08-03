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

/**
 * Esta clase se encarga de administrar la operación consulta del Reporte Anexo A
 * Esta clase es llamada por el servlet ProcesaReporteS.
 */

public class SimReporteContratoAcsREP implements ReporteControlIN {

	public Map getParametros(Registro parametrosCatalogo, HttpServletRequest request, CatalogoSL catalogoSL, Context contextoServidor, ServletContext contextoServlet)  throws Exception{
		Map parametros = new HashMap();

		String sSql =   " SELECT \n"+
						"C.CVE_GPO_EMPRESA, \n"+
						"C.CVE_EMPRESA, \n"+
						"C.ID_PRESTAMO, \n"+
						"C.CVE_NOMBRE ID_CLIENTE, \n"+ 
						"C.NUM_CICLO, \n"+ 
						"TO_CHAR(TO_DATE(C.FECHA_ENTREGA),'DD \"de\" MONTH \"de\" YYYY') FECHA_ENTREGA, \n"+
						"C.DIRECCION_SUCURSAL, \n"+ 
						"DC.CALLE||' '||DC.NUMERO_INT||' '||DC.NOM_ASENTAMIENTO||' '||DC.NOM_DELEGACION||' '||DC.NOM_ESTADO||' '||'C.P.'||' '||DC.CODIGO_POSTAL DIRECCION_CLIENTE, \n"+ 
						"C.NOMBRE NOM_CLIENTE, \n"+
						"PA.ID_PERSONA ID_AVAL, \n"+
						"PPA.NOM_COMPLETO NOM_AVAL, \n"+
						"DA.CALLE||' '||DA.NUMERO_INT||' '||DA.NOM_ASENTAMIENTO||' '||DA.NOM_DELEGACION||' '||DA.NOM_ESTADO||' '||'C.P.'||' '||DA.CODIGO_POSTAL DIRECCION_AVAL, \n"+
						"PD.ID_PERSONA ID_DEPOSITARIO, \n"+
						"PPD.NOM_COMPLETO NOM_DEPOSITARIO, \n"+
						"DD.CALLE||' '||DD.NUMERO_INT||' '||DD.NOM_ASENTAMIENTO||' '||DD.NOM_DELEGACION||' '||DD.NOM_ESTADO||' '||'C.P.'||' '||DD.CODIGO_POSTAL DIRECCION_DEPOSITARIO, \n"+ 
						"PG.ID_PERSONA ID_GARANTE, \n"+
						"PPG.NOM_COMPLETO NOM_GARANTE, \n"+
						"DG.CALLE||' '||DG.NUMERO_INT||' '||DG.NOM_ASENTAMIENTO||' '||DG.NOM_DELEGACION||' '||DG.NOM_ESTADO||' '||'C.P.'||' '||DG.CODIGO_POSTAL DIRECCION_GARANTE \n"+
						"FROM \n"+
						"V_CREDITO C, \n"+
						"RS_GRAL_DOMICILIO DC, \n"+ 
						"SIM_PRESTAMO_PARTICIPANTE PA, \n"+
						"RS_GRAL_PERSONA PPA, \n"+
						"RS_GRAL_DOMICILIO DA, \n"+
						"SIM_PRESTAMO_PARTICIPANTE PD, \n"+
						"RS_GRAL_PERSONA PPD,  \n"+
						"RS_GRAL_DOMICILIO DD, \n"+
						"SIM_PRESTAMO_PARTICIPANTE PG, \n"+ 
						"RS_GRAL_PERSONA PPG, \n"+
						"RS_GRAL_DOMICILIO DG \n"+
						"WHERE C.CVE_GPO_EMPRESA = 'SIM' \n"+ 
						"AND C.CVE_EMPRESA = 'CREDICONFIA' \n"+
						"AND C.ID_PRESTAMO = '" + request.getParameter("IdPrestamo")+"' \n"+
						"AND DC.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+ 
						"AND DC.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
						"AND DC.IDENTIFICADOR (+)= C.CVE_NOMBRE \n"+
						"AND DC.DOMICILIO_FISCAL (+)= 'V' \n"+
						"AND PA.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+ 
						"AND PA.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
						"AND PA.ID_PRESTAMO (+)= C.ID_PRESTAMO \n"+
						"AND PA.CVE_TIPO_PERSONA (+)= 'AVAL' \n"+
						"AND PPA.CVE_GPO_EMPRESA (+)= PA.CVE_GPO_EMPRESA \n"+ 
						"AND PPA.CVE_EMPRESA (+)= PA.CVE_EMPRESA \n"+
						"AND PPA.ID_PERSONA (+)= PA.ID_PERSONA \n"+
						"AND DA.CVE_GPO_EMPRESA (+)= PA.CVE_GPO_EMPRESA \n"+ 
						"AND DA.CVE_EMPRESA (+)= PA.CVE_EMPRESA \n"+
						"AND DA.IDENTIFICADOR (+)= PA.ID_PERSONA  \n"+
						"AND DA.DOMICILIO_FISCAL (+)= 'V' \n"+
						"AND PD.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+ 
						"AND PD.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
						"AND PD.ID_PRESTAMO (+)= C.ID_PRESTAMO  \n"+
						"AND PD.CVE_TIPO_PERSONA (+)= 'DEPOSIT' \n"+
						"AND PPD.CVE_GPO_EMPRESA (+)= PD.CVE_GPO_EMPRESA \n"+  
						"AND PPD.CVE_EMPRESA (+)= PD.CVE_EMPRESA \n"+
						"AND PPD.ID_PERSONA (+)= PD.ID_PERSONA \n"+
						"AND DD.CVE_GPO_EMPRESA (+)= PD.CVE_GPO_EMPRESA \n"+ 
						"AND DD.CVE_EMPRESA (+)= PD.CVE_EMPRESA \n"+
						"AND DD.IDENTIFICADOR (+)= PD.ID_PERSONA \n"+
						"AND DD.DOMICILIO_FISCAL (+)= 'V' \n"+
						"AND PG.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+ 
						"AND PG.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
						"AND PG.ID_PRESTAMO (+)= C.ID_PRESTAMO \n"+
						"AND PG.CVE_TIPO_PERSONA (+)= 'GARANTE' \n"+
						"AND PPG.CVE_GPO_EMPRESA (+)= PG.CVE_GPO_EMPRESA \n"+ 
						"AND PPG.CVE_EMPRESA (+)= PG.CVE_EMPRESA \n"+
						"AND PPG.ID_PERSONA (+)= PG.ID_PERSONA \n"+
						"AND DG.CVE_GPO_EMPRESA (+)= PG.CVE_GPO_EMPRESA \n"+ 
						"AND DG.CVE_EMPRESA (+)= PG.CVE_EMPRESA \n"+
						"AND DG.IDENTIFICADOR (+)= PG.ID_PERSONA  \n"+
						"AND DG.DOMICILIO_FISCAL (+)= 'V' \n";
										                
		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("/Portales/Sim/CrediConfia/img/CrediConfia.bmp"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/prestamo/SimReporteContratoACS.jasper");
		//parametros.put("Subreporte1", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReporteContratoACS1.jasper"));
		//parametros.put("Subreporte2", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReporteContratoACS2.jasper"));
		//parametros.put("Subreporte3", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReporteContratoACS3.jasper"));
		//parametros.put("Subreporte4", contextoServlet.getRealPath("/Reportes/Sim/prestamo/SimReporteContratoACS4.jasper"));
		return parametros;		
	}
}