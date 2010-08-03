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
public class SimReporteResEdoCtaIndREP implements ReporteControlIN {

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

		String sClave = request.getParameter("CvePrestamo");
		String sNombreCompleto =request.getParameter("NomCompleto");
		
		System.out.println("sClave:"+sClave);
		System.out.println("sNombreCompleto:"+sNombreCompleto);
		
		String sSql = "SELECT \n"+
							 "RGP.NOM_COMPLETO,\n"+
							 "P.CVE_PRESTAMO,\n"+
							 "SP.NOM_PRODUCTO,\n"+
							 "P.NUM_CICLO,\n"+
							 "P.ID_PRODUCTO,\n"+
							 "P.ID_CLIENTE,\n"+
							 "SG.NOM_GRUPO,\n"+
							 "SPG.ID_GRUPO,\n"+
							 "PGD.ID_PRESTAMO_GRUPO,\n"+
							 "V.CVE_GPO_EMPRESA,\n"+
							 "V.CVE_EMPRESA,\n"+
							 "V.ID_PRESTAMO,\n"+
							 "V.DESC_MOVIMIENTO,\n"+
							 "V.IMP_DEBE_TOTAL,\n"+
							 "V.IMP_PAGO_TOTAL,\n"+
							 "V.IMP_SALDO_TOTAL,\n"+
							 "V.IMP_DEBE_HOY,\n"+
							 "V.IMP_PAGO_HOY,\n"+
							 "V.IMP_SALDO_HOY \n"+
							 "FROM \n"+
							 "V_SIM_PRESTAMO_RES_EDO_CTA V,\n"+
							 "SIM_PRESTAMO P,\n"+
							 "RS_GRAL_PERSONA RGP,\n"+
							 "SIM_PRODUCTO SP,\n"+
							 "SIM_PRESTAMO_GPO_DET PGD,\n"+
							 "SIM_PRESTAMO_GRUPO SPG,\n"+
							 "SIM_GRUPO SG \n"+
							 // V V_SIM_PRESTAMO_RES_EDO_CTA:
							 /////ESTA VISTA TIENE LOS ID_PRESTAMO INDIVIDUALES
							 // P SIM_PRESTAMO :
							 /////ESTA TABLA CONTIENE EL ID_PRESTAMO INDIVIDUAL Y EL ID_PRODUCTO, ID_CLIENTE, CVE_PRESTAMO, NUM_CICLO
							 // MOTIVO DE LA RELACION:
							 /////SE RELACIONA EL ID_PRESTAMO PARA OBTENER EL ID_PRODUCTO, ID_CLIENTE, NUM_CICLO Y CVE_PRESTAMO DE LA TABLA SIM_PRESTAMO
							 "WHERE V.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA\n"+
							" AND V.CVE_EMPRESA =  P.CVE_EMPRESA\n"+
							" AND V.ID_PRESTAMO = P.ID_PRESTAMO\n"+
							 // P SIM_PRESTAMO:
							 /////ESTA TABLA CONTIENE EL ID_CLIENTE CORRESPONDIENTE
							 // RGP RS_GRAL_PERSONA:
							 /////ESTA TABLA CONTIENE EL ID_PERSONA Y EL NOM_COMPLETO CORRESPONDIENTE
							 //MOTIVO DE LA RELACION
							 /////SE RELACIONA EL ID_CLIENTE CON EL ID_PERSONA PARA OBTENER EL NOM_COMPLETO DE LA TABLA RS_GRAL_PERSONA
							 "AND P.CVE_GPO_EMPRESA = RGP.CVE_GPO_EMPRESA\n"+
							 "AND P.CVE_EMPRESA = RGP.CVE_EMPRESA\n"+
							 "AND P.ID_CLIENTE = RGP.ID_PERSONA\n"+
							 // P SIM_PRESTAMO:
							 /////ESTA TABLA CONTIENE EL ID_PRODUCTO CORRESPONDIENTE
							 // SP SIM_PRODUCTO:
							 /////ESTA TABLA CONTIENE EL ID_PRODUCTO PARA OBTENER EL NOM_PRODUCTO DE LA TABLA SIM_PRODUCTO
							 //MOTIVO DE LA RELACION
							/////SE RELACIONA EL ID_PRODUCTO PARA OBTENER EL NOM_PRODUCTO DE LA TABLA SIM_PRODUCTO
							 "AND P.CVE_GPO_EMPRESA = SP.CVE_GPO_EMPRESA\n"+
							 "AND P.CVE_EMPRESA = SP.CVE_EMPRESA\n"+
							 "AND P.ID_PRODUCTO = SP.ID_PRODUCTO\n"+
							 // TA SIM_TABLA_AMORTIZACION:
							 /////ESTA TABLA CONTIENE EL ID_PRESTAMO INDIVIDUAL CORRESPONDIENTE
							 // PGD SIM_PRESTAMO_GPO_DET:
							 ////ESTA TABLA CONTIENE EL ID_PRESTAMO INDIVIDUAL PARA OBTENER EL ID_PRESTAMO_GRUPO
							 //MOTIVO DE LA RELACION:
							 /////SE RELACIONA EL ID_PRESTAMO PARA OBTENER EL ID_PRESTAMO_GRUPO DE LA TABLA SIM_PRESTAMO_GPO_DET
							 "AND V.CVE_GPO_EMPRESA = PGD.CVE_GPO_EMPRESA\n"+
							" AND V.CVE_EMPRESA = PGD.CVE_EMPRESA\n"+
							" AND V.ID_PRESTAMO = PGD.ID_PRESTAMO\n"+
							 // PGD SIM_PRESTAMO_GPO_DET:
							 /////ESTA TABLA CONTIENE EL ID_PRESTAMO_GRUPO CORRESPONDIENTE
							 //SPG SIM_PRESTAMO_GRUPO:
							 /////ESTA TABLA CONTIENE EL ID_PRESTAMO_GRUPO PARA OBTENER ID_GRUPO
							 // MOTIVO DE LA RELACION:
							 /////SE RELACIONA EL ID_PRESTAMO_GRUPO PARA OBETNER EL ID_GRUPO DE LA TABLA SIM_PRESTAMO_GRUPO
							 "AND PGD.CVE_GPO_EMPRESA = SPG.CVE_GPO_EMPRESA\n"+
							 "AND PGD.CVE_EMPRESA = SPG.CVE_EMPRESA\n"+
							 "AND PGD.ID_PRESTAMO_GRUPO = SPG.ID_PRESTAMO_GRUPO\n"+
							 //SPG SIM_PRESTAMO_GRUPO:
							 /////ESTA TABLA CONTIENE EL ID_GRUPO CORRESPONDIENTE
							 // SG SIM_GRUPO:
							 /////ESTA TABLA CONTIENE EL ID_GRUPO PARA OBTENER EL NOM_GRUPO
							 //MOTIVO DE LA RELACION:
							 /////SE RELACIONA EL ID_GRUPO PARA OBTENER EL NOM_GRUPO DE LA TABLA SIM_GRUPO
							 "AND SPG.CVE_GPO_EMPRESA = SG.CVE_GPO_EMPRESA\n"+
							 "AND SPG.CVE_EMPRESA = SG.CVE_EMPRESA\n"+
							 "AND SPG.ID_GRUPO = SG.ID_GRUPO\n";
		
		if (sClave != null && !sClave.equals("") && !sClave.equals("null") ){								
			sSql = sSql + "AND P.CVE_PRESTAMO = '" + sClave + "' \n";
		}
		if (sNombreCompleto != null && !sNombreCompleto.equals("") && !sNombreCompleto.equals("null") ){
			sSql = sSql + "AND RGP.NOM_COMPLETO = '" + sNombreCompleto + "' \n";
		}		

		 sSql = sSql +
	
		 "ORDER BY  V.ID_PRESTAMO\n";
		
		 System.out.println("*****************Paso por aqui****************:"+sSql);
		
	    String sTipoReporte = request.getParameter("TipoReporte");
	    System.out.println("TipoReporte:"+sTipoReporte);
		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("Portales/Sam/img/Imagentsys1.gif"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReporteResEdoCtaInd.jasper");
		                             
		
		return parametros;		
	}
}
