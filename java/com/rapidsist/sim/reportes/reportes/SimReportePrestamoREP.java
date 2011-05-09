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
public class SimReportePrestamoREP implements ReporteControlIN {

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
		String sClaveGrupo = request.getParameter("IdPrestamoGrupo");
		
		System.out.println("sClaveGrupo:"+sClaveGrupo);
		
        String sSql ="SELECT \n"+ 
				        "T.Cve_Gpo_Empresa CVE_GPO_EMPRESA,\n"+
				        "T.Cve_Empresa CVE_EMPRESA,\n"+
				        "T.Id_Prestamo ID_PRESTAMO,\n"+
				        "NVL(P.ID_GRUPO,0) CVE_GRUPO,\n"+
				        "NVL(G.NOM_GRUPO,'NO APLICA') GRUPO,\n"+
				        "V.CVE_NOMBRE CVE_NOMBRE,\n"+
				        "PE.NOMBRE_1 NOMBRE_1,\n"+
				        "NVL(PE.NOMBRE_2, ' ') AS NOMBRE_2,\n"+
				        "PE.AP_PATERNO AS AP_PATERNO,\n"+
				        "PE.AP_MATERNO AS AP_MATERNO,\n"+
				        "PE.FECHA_NACIMIENTO AS NACIMIENTO,\n"+
				        "PE.ESTADO_CIVIL,\n"+
				        "PE.REGIMEN_MARITAL,\n"+
				        "TE.TELEFONO,\n"+
				        "PE.CURP,\n"+
				        "PE.RFC,\n"+
				        "PE.NUM_IDENTIFICACION_OFICIAL AS IFE,\n"+
				        "V.NUM_CICLO,\n"+
				        "1 NUM_INTEGRANTES,\n"+
				        "NVL(V.FECHA_REAL,V.FECHA_ENTREGA) INICIO_CICLO,\n"+
				        "V.PLAZO,\n"+
				        "V.PERIODICIDAD_PRODUCTO,\n"+
				        "V.VALOR_TASA,\n"+
				        "E.NOM_ESTATUS_PRESTAMO,\n"+
				        "V.MONTO_AUTORIZADO CANTIDAD_PRESTADA,\n"+
				        "V.MONTO_AUTORIZADO + V.CARGO_INICIAL,\n"+
				        "AVG(NVL(VT.INTERES,0) + NVL(VT.IMP_CAPITAL_AMORT,0)) CAPITAL_INTERES,\n"+ 
				                "AVG(NVL(VT.IMP_ACCESORIO,0)) IMP_ACCESORIO,\n"+
				                "AVG(NVL(VT.INTERES,0) + NVL(VT.IMP_CAPITAL_AMORT,0) + NVL(VT.IMP_ACCESORIO,0)) IMP_PAGO,\n"+
				        "Sum(NVL(T.Imp_Capital_Amort,0)*-1) + Sum(NVL(T.Imp_Interes,0)*-1) + Sum(NVL(T.Imp_Iva_Interes,0)*-1) + Sum(NVL(T.Imp_Interes_Extra,0)*-1) + Sum(NVL(T.Imp_Iva_Interes_Extra,0)*-1) + -SUM(NVL(A.IMP_ACCESORIO,0)) + SUM(NVL(IMP_PAGO_TARDIO,0)*-1) + SUM(NVL(IMP_INTERES_MORA,0)*-1) + SUM(NVL(IMP_IVA_INTERES_MORA,0)*-1) TOTAL_CARGO,\n"+
				        "-Sum(NVL(T.Imp_Capital_Amort_Pagado,0)*-1) + -1*(Sum(NVL(T.Imp_Interes_Pagado,0)*-1) + Sum(NVL(T.Imp_Iva_Interes_Pagado,0)*-1) + Sum(NVL(T.Imp_Interes_Extra_Pagado,0)*-1) + Sum(NVL(T.Imp_Iva_Interes_Extra_Pagado,0)*-1)) + SUM(NVL(A.IMP_ACCESORIO_pagado,0)) + SUM(NVL(T.IMP_PAGO_TARDIO_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_MORA_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA_PAGADO,0)) TOTAL_ABONO,\n"+
				        "-Sum(NVL(T.Imp_Capital_Amort_Pagado,0)*-1) As Abono_Capital,\n"+
				        "-1*(Sum(NVL(T.Imp_Interes_Pagado,0)*-1) + Sum(NVL(T.Imp_Iva_Interes_Pagado,0)*-1) + Sum(NVL(T.Imp_Interes_Extra_Pagado,0)*-1) + Sum(NVL(T.Imp_Iva_Interes_Extra_Pagado,0)*-1)) As ABONO_Interes,\n"+
				        "SUM(NVL(T.IMP_PAGO_TARDIO_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_MORA_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA_PAGADO,0)) AS ABONO_RECARGO,\n"+
				        "SUM(NVL(A.IMP_ACCESORIO_Pagado,0)) AS ABONO_ACCESORIO,\n"+
				        "P.F_ULT_PAGO_REALIZADO,\n"+
				        "AA.IMP_SALDO_HOY,\n"+
				        "V.CVE_ASESOR_CREDITO,\n"+
				        "V.NOM_ASESOR_CREDITO,\n"+
				        "V.FECHA_ENTREGA,\n"+
				        "P.ID_COMITE,\n"+
				        "CO.NOM_COMITE,\n"+
				        "'MEX' CODIGO_PAIS,\n"+
				        "'Mexico' PAIS,\n"+
				        "V.ID_SUCURSAL,\n"+
				        "V.NOM_SUCURSAL,\n"+
				        "V.ID_REGIONAL,\n"+
				        "V.NOM_REGIONAL,\n"+
				        "V.CVE_USUARIO_COORDINADOR,\n"+
				        "V.NOM_COORDINADOR_SUCURSAL,\n"+
				        "DO.NOM_DELEGACION,\n"+
				        "DO.NOM_ASENTAMIENTO,\n"+
				        "DO.NOM_CIUDAD,\n"+
				        "DO.CALLE,\n"+
				        "DO.CODIGO_POSTAL,\n"+
				        "V.CVE_FONDEADOR,\n"+
				        "V.NOM_FONDEADOR,\n"+
				        "V.NUM_LINEA,\n"+
				        "'Individal' TIPO_PRESTAMO,\n"+
				        "CG.ID_GARANTIA,\n"+
				        "CG.DESCRIPCION,\n"+
				        "DECODE(PE.SEXO,'F','0') MUJERES_CON_PRESTAMO,\n"+
				        "DECODE(PE.SEXO,'M','1') HOMBRES_CON_PRESTAMO,\n"+
				        "NOM_CLASE SECTOR,\n"+
				        "NOM_SUB_RAMA GIRO,\n"+
				        "NOM_RAMA ACTIVIDAD_ECONOMICA,\n"+
				        "V.MONTO_AUTORIZADO CANTIDAD_ENTREGADA,\n"+
				        "'01' TIPO_DE_PRODUCTO,\n"+
				        "V.ID_PRODUCTO,\n"+
				        "V.NOM_PRODUCTO,\n"+
				        "EC.NOM_ESCOLARIDAD GRADO_ACADEMICO\n"+
				          "FROM   \n"+
				        "SIM_TABLA_AMORTIZACION T,\n"+
				        "PFIN_CAT_CONCEPTO C,\n"+
				        "Pfin_Cat_Concepto CA,\n"+
				        "Pfin_Cat_Concepto CB,\n"+
				        "Pfin_Cat_Concepto Cc,\n"+
				        "Pfin_Cat_Concepto Cd,\n"+
				        "Sim_Tabla_Amort_Accesorio A,\n"+
				        "SIM_CAT_ACCESORIO CE,\n"+
				        "V_CREDITO V,\n"+
				        "V_TABLA_AMORT_INDIVIDUAL VT,\n"+
				        "SIM_PRESTAMO P,\n"+
				        "SIM_GRUPO G,\n"+
				        "RS_GRAL_PERSONA PE,\n"+
				        "RS_GRAL_TELEFONO TE,\n"+
				        "SIM_CAT_ETAPA_PRESTAMO E,\n"+
				        "(SELECT CVE_GPO_EMPRESA,\n"+
				         "CVE_EMPRESA,\n"+
				         "Id_Prestamo,\n"+
				         "SUM(NVL(IMP_SALDO_HOY,0)) IMP_SALDO_HOY\n"+
				          "From V_SIM_PRESTAMO_RES_EDO_CTA\n"+
				          "WHERE DESC_MOVIMIENTO IN ('Pago Tardío','Pago Pago Tardío','Seguro Deudor','Pago Seguro Deudor','Capital','Pago Capital','Interés', 'Interés Extra', 'Iva De Intereses', 'Iva Interes Extra', 'Pago Interés', 'Pago Interés Extra', 'Pago Iva De Intereses', 'Pago Iva Interes Extra')\n"+
				          "GROUP BY CVE_GPO_EMPRESA, CVE_EMPRESA, Id_Prestamo\n"+
				          "Order By Id_Prestamo\n"+
				         ")AA,\n"+
				         "SIM_COMITE CO,\n"+
				         "RS_GRAL_DOMICILIO DO,\n"+
				         "SIM_CLIENTE_GARANTIA CG,\n"+
				         "SIM_CAT_ESCOLARIDAD EC,\n"+
				         "SIM_CLIENTE_NEGOCIO CN,\n"+
				         "SIM_CAT_CLASE CL,\n"+
				         "SIM_CAT_SUB_RAMA SB,\n"+
				         "SIM_CAT_RAMA RA\n"+
				         "WHERE T.CVE_GPO_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+
				         "And T.Cve_Empresa = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "'\n"+        	  				        	
				          "AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA\n"+
				          "AND T.CVE_EMPRESA = C.CVE_EMPRESA\n"+
				          "And C.Cve_Concepto= 'CAPITA'\n"+
				          "And T.Cve_Gpo_Empresa = Ca.Cve_Gpo_Empresa\n"+ 
				        "And T.Cve_Empresa = CA.Cve_Empresa\n"+
				        "And CA.Cve_Concepto= 'INTERE'\n"+
				          "And T.Cve_Gpo_Empresa = CB.Cve_Gpo_Empresa\n"+ 
				        "And T.Cve_Empresa = CB.Cve_Empresa\n"+
				        "And CB.Cve_Concepto= 'IVAINT'\n"+
				          "And T.Cve_Gpo_Empresa = CC.Cve_Gpo_Empresa\n"+ 
				        "And T.Cve_Empresa = CC.Cve_Empresa\n"+
				        "And CC.Cve_Concepto= 'INTEXT'\n"+
				          "And T.Cve_Gpo_Empresa = CD.Cve_Gpo_Empresa\n"+ 
				        "And T.Cve_Empresa = Cd.Cve_Empresa\n"+
				        "And CD.Cve_Concepto= 'IVAINTEX'\n"+
				          "AND T.CVE_GPO_EMPRESA   = A.CVE_GPO_EMPRESA\n"+ 
				        "AND T.CVE_EMPRESA   = A.CVE_EMPRESA\n"+
				        "AND T.ID_PRESTAMO   = A.ID_PRESTAMO\n"+
				        "And T.Num_Pago_Amortizacion = A.Num_Pago_Amortizacion\n"+ 
				        "AND A.CVE_GPO_EMPRESA   = CE.CVE_GPO_EMPRESA\n"+
				        "And A.Cve_Empresa   = Ce.Cve_Empresa\n"+
				        "And A.Id_Accesorio  = Ce.Id_Accesorio\n"+
				          "AND V.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA\n"+
				          "AND V.CVE_EMPRESA = T.CVE_EMPRESA\n"+
				          "AND V.ID_PRESTAMO = T.ID_PRESTAMO\n"+
				          "AND V.APLICA_A = 'INDIVIDUAL'\n"+
				          "AND VT.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA\n"+
				          "AND VT.CVE_EMPRESA = T.CVE_EMPRESA\n"+
				          "AND VT.ID_PRESTAMO = T.ID_PRESTAMO\n"+
				          "AND P.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA\n"+
				          "AND P.CVE_EMPRESA = T.CVE_EMPRESA\n"+
				          "AND P.ID_PRESTAMO = T.ID_PRESTAMO\n"+
				          "AND G.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA\n"+
				          "AND G.CVE_EMPRESA (+)= P.CVE_EMPRESA\n"+
				          "AND G.ID_GRUPO (+)= P.ID_GRUPO\n"+
				          "AND PE.CVE_GPO_EMPRESA = V.CVE_GPO_EMPRESA\n"+
				          "AND PE.CVE_EMPRESA = V.CVE_EMPRESA\n"+
				          "AND PE.ID_PERSONA = V.CVE_NOMBRE\n"+
				          "AND TE.CVE_GPO_EMPRESA (+)= V.CVE_GPO_EMPRESA\n"+
				          "AND TE.CVE_EMPRESA (+)= V.CVE_EMPRESA\n"+
				          "AND TE.IDENTIFICADOR (+)= V.CVE_NOMBRE\n"+
				          "AND ID_DESC_TEL (+)= '1'\n"+
				          "AND E.CVE_GPO_EMPRESA(+)= V.CVE_GPO_EMPRESA\n"+
				          "AND E.CVE_EMPRESA(+)= V.CVE_EMPRESA\n"+
				        "AND E.ID_ETAPA_PRESTAMO   (+)= V.ID_ETAPA_PRESTAMO\n"+
				          "AND AA.CVE_GPO_EMPRESA(+)= V.CVE_GPO_EMPRESA\n"+
				          "AND AA.CVE_EMPRESA(+)= V.CVE_EMPRESA\n"+
				        "AND AA.ID_PRESTAMO(+)= V.ID_PRESTAMO\n"+
				          "AND CO.CVE_GPO_EMPRESA(+)= P.CVE_GPO_EMPRESA\n"+
				          "AND CO.CVE_EMPRESA(+)= P.CVE_EMPRESA\n"+
				        "AND CO.ID_COMITE  (+)= P.ID_COMITE\n"+
				          "AND DO.CVE_GPO_EMPRESA(+)= V.CVE_GPO_EMPRESA\n"+
				          "AND DO.CVE_EMPRESA(+)= V.CVE_EMPRESA\n"+
				        "AND DO.IDENTIFICADOR  (+)= V.CVE_NOMBRE\n"+
				          "AND DO.CVE_TIPO_IDENTIFICADOR (+)= 'CLIENTE'\n"+
				          "AND DO.DOMICILIO_FISCAL (+)= 'V'\n"+
				          "AND CG.CVE_GPO_EMPRESA(+)= V.CVE_GPO_EMPRESA\n"+
				          "AND CG.CVE_EMPRESA(+)= V.CVE_EMPRESA\n"+
				        "AND CG.ID_PERSONA (+)= V.CVE_NOMBRE\n"+
				          "AND CG.ID_TIPO_GARANTIA = '1'\n"+
				          "AND EC.CVE_GPO_EMPRESA = PE.CVE_GPO_EMPRESA\n"+
				          "AND EC.CVE_EMPRESA = PE.CVE_EMPRESA\n"+
				          "AND EC.ID_ESCOLARIDAD = PE.ID_ESCOLARIDAD\n"+
				          "AND CN.CVE_GPO_EMPRESA = PE.CVE_GPO_EMPRESA\n"+
				          "AND CN.CVE_EMPRESA = PE.CVE_EMPRESA\n"+
				          "AND CN.ID_PERSONA = PE.ID_PERSONA\n"+
				          "AND CN.B_PRINCIPAL = 'V'\n"+
				          "AND CL.CVE_GPO_EMPRESA = CN.CVE_GPO_EMPRESA\n"+
				          "AND CL.CVE_EMPRESA = CN.CVE_EMPRESA\n"+
				          "AND CL.CVE_CLASE = CN.CVE_CLASE\n"+
				          "AND SB.CVE_GPO_EMPRESA = CL.CVE_GPO_EMPRESA\n"+
				          "AND SB.CVE_EMPRESA = CL.CVE_EMPRESA\n"+
				          "AND SB.CVE_SUB_RAMA = CL.CVE_SUB_RAMA\n"+
				          "AND RA.CVE_GPO_EMPRESA = CL.CVE_GPO_EMPRESA\n"+
				          "AND RA.CVE_EMPRESA = CL.CVE_EMPRESA\n"+
				          "AND RA.CVE_RAMA = CL.CVE_RAMA\n"+
				          "GROUP BY T.Cve_Gpo_Empresa, T.Cve_Empresa, T.Id_Prestamo, P.ID_GRUPO, G.NOM_GRUPO,\n"+
				          "V.CVE_NOMBRE,\n"+
				          "PE.NOMBRE_1,\n"+
				          "PE.NOMBRE_2,\n"+
				          "PE.AP_PATERNO,\n"+
				          "PE.AP_MATERNO,\n"+
				          "PE.FECHA_NACIMIENTO,\n"+
				          "PE.ESTADO_CIVIL,\n"+
				          "PE.REGIMEN_MARITAL,\n"+
				          "TE.TELEFONO,\n"+
				          "PE.CURP,\n"+
				          "PE.RFC,\n"+
				          "PE.NUM_IDENTIFICACION_OFICIAL,\n"+
				          "V.NUM_CICLO,\n"+
				          "1,\n"+
				          "NVL(V.FECHA_REAL,V.FECHA_ENTREGA),\n"+
				          "V.PLAZO,\n"+
				          "V.PERIODICIDAD_PRODUCTO,\n"+
				          "V.VALOR_TASA,\n"+
				          "E.NOM_ESTATUS_PRESTAMO,\n"+
				          "V.MONTO_AUTORIZADO,\n"+
				          "V.MONTO_AUTORIZADO + V.CARGO_INICIAL,\n"+
				          "P.F_ULT_PAGO_REALIZADO,\n"+
				          "AA.IMP_SALDO_HOY,\n"+
				          "V.CVE_ASESOR_CREDITO,\n"+
				          "V.NOM_ASESOR_CREDITO,\n"+
				          "V.FECHA_ENTREGA,\n"+
				          "P.ID_COMITE,\n"+
				          "CO.NOM_COMITE,\n"+
				          "'MEX',\n"+
				          "'Mexico',\n"+
				          "V.ID_SUCURSAL,\n"+
				          "V.NOM_SUCURSAL,\n"+
				          "V.ID_REGIONAL,\n"+
				          "V.NOM_REGIONAL,\n"+
				          "V.CVE_USUARIO_COORDINADOR,\n"+
				          "V.NOM_COORDINADOR_SUCURSAL,\n"+
				          "V.CVE_FONDEADOR,\n"+
				          "V.NOM_FONDEADOR,\n"+
				          "V.NUM_LINEA,\n"+
				          "DO.NOM_DELEGACION,\n"+
				          "DO.NOM_ASENTAMIENTO,\n"+
				          "DO.NOM_CIUDAD,\n"+
				          "DO.CALLE,\n"+
				          "DO.CODIGO_POSTAL,\n"+
				          "CG.ID_GARANTIA,\n"+
				          "CG.DESCRIPCION,\n"+
				          "DECODE(PE.SEXO,'F','0'),\n"+
				          "DECODE(PE.SEXO,'M','1'),\n"+
				          "NOM_CLASE,\n"+
				          "NOM_SUB_RAMA,\n"+
				          "NOM_RAMA,\n"+
				          "V.MONTO_AUTORIZADO,\n"+
				          "V.ID_PRODUCTO,\n"+
				          "V.NOM_PRODUCTO,\n"+
				          "EC.NOM_ESCOLARIDAD\n"+
				         
				         "UNION ALL\n"+
				        
				         "SELECT\n"+ 
				         "GD.CVE_GPO_EMPRESA CVE_GPO_EMPRESA,\n"+
				         "Gd.Cve_Empresa CVE_EMPRESA,\n"+
				         "Gd.Id_Prestamo_Grupo ID_PRESTAMO,\n"+
				         "V.CVE_NOMBRE CVE_GRUPO,\n"+
				         "V.NOMBRE GRUPO,\n"+
				         "DECODE(1,1, 0) AS CVE_NOMBRE,\n"+
				         "' ' NOMBRE_1,\n"+
				         "' ' NOMBRE_2,\n"+
				         "' ' AP_PATERNO,\n"+
				         "' ' AP_MATERNO,\n"+
				         " DECODE(10/10/2010,10/10/2010,'') NACIMIENTO,\n"+
				         "' ' ESTADO_CIVIL,\n"+
				         "' ' REGIMEN_MARITAL,\n"+
				         "DECODE(1,1,' ') AS TELEFONO,\n"+
				         "' ' CURP,\n"+
				         "' ' RFC,\n"+
				         "' ' IFE,\n"+
				         "V.NUM_CICLO,\n"+
				         "G.NUM_INTEGRANTES,\n"+
				         "NVL(P.FECHA_REAL,P.FECHA_ENTREGA) INICIO_CICLO,\n"+
				         "V.PLAZO,\n"+
				         "V.PERIODICIDAD_PRODUCTO,\n"+
				         "V.VALOR_TASA,\n"+
				         "E.NOM_ESTATUS_PRESTAMO,\n"+ 
				         "V.MONTO_AUTORIZADO AS CANTIDAD_PRESTADA,\n"+
				         "V.MONTO_AUTORIZADO + V.CARGO_INICIAL,\n"+
				         "AVG(NVL(TG.INTERES,0) + NVL(TG.IMP_CAPITAL_AMORT,0)) CAPITAL_INTERES,\n"+ 
				         "          AVG(NVL(TG.IMP_ACCESORIO,0)) IMP_ACCESORIO,\n"+
				         "AVG(NVL(TG.INTERES,0) + NVL(TG.IMP_CAPITAL_AMORT,0) + NVL(TG.IMP_ACCESORIO,0)) IMP_PAGO,\n"+
				         "Sum(NVL(T.Imp_Capital_Amort,0)*-1) + Sum(NVL(T.Imp_Interes,0)*-1) + Sum(NVL(T.Imp_Iva_Interes,0)*-1) + Sum(NVL(T.Imp_Interes_Extra,0)*-1) + Sum(NVL(T.Imp_Iva_Interes_Extra,0)*-1) + -SUM(NVL(A.IMP_ACCESORIO,0)) + SUM(NVL(IMP_PAGO_TARDIO,0)*-1) + SUM(NVL(IMP_INTERES_MORA,0)*-1) + SUM(NVL(IMP_IVA_INTERES_MORA,0)*-1) TOTAL_CARGO,\n"+
				         "-Sum(NVL(T.Imp_Capital_Amort_Pagado,0)*-1) + -1*(Sum(NVL(T.Imp_Interes_Pagado,0)*-1) + Sum(NVL(T.Imp_Iva_Interes_Pagado,0)*-1) + Sum(NVL(T.Imp_Interes_Extra_Pagado,0)*-1) + Sum(NVL(T.Imp_Iva_Interes_Extra_Pagado,0)*-1)) + SUM(NVL(A.IMP_ACCESORIO_pagado,0)) + SUM(NVL(T.IMP_PAGO_TARDIO_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_MORA_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA_PAGADO,0)) TOTAL_ABONO,\n"+
				         "-Sum(NVL(T.Imp_Capital_Amort_Pagado,0)*-1) As Abono_Capital,\n"+  
				         "-1*(Sum(NVL(T.Imp_Interes_Pagado,0)*-1) + Sum(NVL(T.Imp_Iva_Interes_Pagado,0)*-1) + Sum(NVL(T.Imp_Interes_Extra_Pagado,0)*-1) + Sum(NVL(T.Imp_Iva_Interes_Extra_Pagado,0)*-1)) As ABONO_Interes,\n"+  
				         "SUM(NVL(T.IMP_PAGO_TARDIO_PAGADO,0)) + SUM(NVL(T.IMP_INTERES_MORA_PAGADO,0)) + SUM(NVL(T.IMP_IVA_INTERES_MORA_PAGADO,0)) AS ABONO_RECARGO,\n"+
				         "SUM(NVL(A.IMP_ACCESORIO_pagado,0)) AS ABONO_ACCESORIO,\n"+
				         "P.F_ULT_PAGO_REALIZADO,\n"+
				         "AA.IMP_SALDO_HOY,\n"+
				         "V.CVE_ASESOR_CREDITO,\n"+
				         "V.NOM_ASESOR_CREDITO,\n"+
				         "V.FECHA_ENTREGA,\n"+
				         "DECODE(1,1, 0) ID_COMITE,\n"+
				         "' ' NOM_COMITE,\n"+
				         "'MEX' CODIGO_PAIS,\n"+
				         "'Mexico' PAIS,\n"+
				         "V.ID_SUCURSAL,\n"+
				         "V.NOM_SUCURSAL,\n"+
				         "V.ID_REGIONAL,\n"+
				         "V.NOM_REGIONAL,\n"+
				         "V.CVE_USUARIO_COORDINADOR,\n"+
				         "V.NOM_COORDINADOR_SUCURSAL,\n"+
				         "' ' NOM_DELEGACION,\n"+
				         "' ' NOM_ASENTAMIENTO,\n"+
				         "' ' NOM_CIUDAD,\n"+
				         "' ' CALLE,\n"+
				         "' ' CODIGO_POSTAL,\n"+
				         "V.CVE_FONDEADOR,\n"+
				         "V.NOM_FONDEADOR,\n"+
				         "V.NUM_LINEA,\n"+
				         "'Grupal' TIPO_PRESTAMO,\n"+
				         "0 ID_GARANTIA,\n"+
				         "' ' DESCRIPCION,\n"+
				         "' ' MUJERES_CON_PRESTAMO,\n"+
				         "' ' HOMBRES_CON_PRESTAMO,\n"+
				         "' ' SECTOR,\n"+
				         "' ' GIRO,\n"+
				         "' ' ACTIVIDAD_ECONOMICA,\n"+
				         "V.MONTO_AUTORIZADO CANTIDAD_ENTREGADA,\n"+
				         "'01' TIPO_DE_PRODUCTO,\n"+
				         "V.ID_PRODUCTO,\n"+
				         "V.NOM_PRODUCTO,\n"+
				         "' ' GRADO_ACADEMICO\n"+  
				       "FROM\n"+  
				         "SIM_PRESTAMO_GPO_DET GD,\n"+
				         "Sim_Tabla_Amortizacion T,\n"+
				         "Pfin_Cat_Concepto C,\n"+
				         "Pfin_Cat_Concepto CA,\n"+
				         "Pfin_Cat_Concepto CB,\n"+
				         "Pfin_Cat_Concepto Cc,\n"+
				         "PFIN_CAT_CONCEPTO CD,\n"+
				         "Sim_Tabla_Amort_Accesorio A,\n"+
				         "SIM_CAT_ACCESORIO CE,\n"+
				         "(SELECT CVE_GPO_EMPRESA,\n"+
				         "CVE_EMPRESA,\n"+
				         "Id_Prestamo,\n"+
				         "SUM(NVL(IMP_SALDO_HOY,0)) IMP_SALDO_HOY\n"+
				          "From V_SIM_PRESTAMO_GPO_RES_EDO_CTA\n"+
				          "WHERE DESC_MOVIMIENTO IN ('Pago Tardío','Pago Pago Tardío','Seguro Deudor','Pago Seguro Deudor','Capital','Pago Capital','Interés', 'Interés Extra', 'Iva De Intereses', 'Iva Interes Extra', 'Pago Interés', 'Pago Interés Extra', 'Pago Iva De Intereses', 'Pago Iva Interes Extra')\n"+
				          "GROUP BY CVE_GPO_EMPRESA, CVE_EMPRESA, Id_Prestamo\n"+
				          "Order By Id_Prestamo\n"+
				         ")AA,\n"+
				         "SIM_PRESTAMO_GRUPO P,\n"+
				         "V_TABLA_AMORTIZACION_GRUPAL TG,\n"+
				         "SIM_GRUPO G,\n"+
				         "SIM_CAT_PERIODICIDAD PE,\n"+
				         "SIM_CAT_ETAPA_PRESTAMO E,\n"+
				         "V_CREDITO V\n"+
				       "WHERE GD.CVE_GPO_EMPRESA = '" + parametrosCatalogo.getDefCampo("CVE_GPO_EMPRESA") + "'\n"+ 
				       "And Gd.Cve_Empresa   = '" + parametrosCatalogo.getDefCampo("CVE_EMPRESA") + "'\n"+ 
				       "AND T.CVE_GPO_EMPRESA = GD.CVE_GPO_EMPRESA\n"+
				       "AND T.CVE_EMPRESA = GD.CVE_EMPRESA\n"+
				       "AND T.ID_PRESTAMO = GD.ID_PRESTAMO\n"+
				       "AND T.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA\n"+ 
				       "AND T.CVE_EMPRESA = C.CVE_EMPRESA\n"+
				       "And C.Cve_Concepto= 'CAPITA'\n"+
				       "And T.Cve_Gpo_Empresa = Ca.Cve_Gpo_Empresa\n"+ 
				       "And T.Cve_Empresa = CA.Cve_Empresa\n"+
				       "And CA.Cve_Concepto   = 'INTERE'\n"+
				       "And T.Cve_Gpo_Empresa = CB.Cve_Gpo_Empresa\n"+ 
				       "And T.Cve_Empresa = CB.Cve_Empresa\n"+
				       "And CB.Cve_Concepto   = 'IVAINT'\n"+
				       "And T.Cve_Gpo_Empresa = CC.Cve_Gpo_Empresa\n"+ 
				       "And T.Cve_Empresa = CC.Cve_Empresa\n"+
				       "And CC.Cve_Concepto   = 'INTEXT'\n"+
				       "And T.Cve_Gpo_Empresa = CD.Cve_Gpo_Empresa\n"+ 
				       "And T.Cve_Empresa = CD.Cve_Empresa\n"+
				       "And CD.Cve_Concepto   = 'IVAINTEX'\n"+
				       "AND T.CVE_GPO_EMPRESA = A.CVE_GPO_EMPRESA\n"+ 
				       "AND T.CVE_EMPRESA = A.CVE_EMPRESA\n"+
				       "AND T.ID_PRESTAMO = A.ID_PRESTAMO\n"+
				       "And T.Num_Pago_Amortizacion = A.Num_Pago_Amortizacion\n"+ 
				       "AND A.CVE_GPO_EMPRESA   = CE.CVE_GPO_EMPRESA\n"+
				       "And A.Cve_Empresa   = CE.Cve_Empresa\n"+
				       "And A.Id_Accesorio  = Ce.Id_Accesorio\n"+
				       "AND AA.CVE_GPO_EMPRESA= GD.CVE_GPO_EMPRESA\n"+
				       "AND AA.CVE_EMPRESA= GD.CVE_EMPRESA\n"+
				       "AND AA.ID_PRESTAMO= GD.ID_PRESTAMO_GRUPO\n"+
				       "AND P.CVE_GPO_EMPRESA = GD.CVE_GPO_EMPRESA\n"+
				       "AND P.CVE_EMPRESA = GD.CVE_EMPRESA\n"+
				       "AND P.ID_PRESTAMO_GRUPO   = GD.ID_PRESTAMO_GRUPO\n"+
				       "AND TG.CVE_GPO_EMPRESA= GD.CVE_GPO_EMPRESA\n"+
				       "AND TG.CVE_EMPRESA= GD.CVE_EMPRESA\n"+
				       "AND TG.ID_PRESTAMO_GRUPO  = GD.ID_PRESTAMO_GRUPO\n"+
				       "AND G.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA\n"+
				       "AND G.CVE_EMPRESA = P.CVE_EMPRESA\n"+
				       "AND G.ID_GRUPO= P.ID_GRUPO\n"+
				       "AND PE.CVE_GPO_EMPRESA= P.CVE_GPO_EMPRESA\n"+
				       "AND PE.CVE_EMPRESA= P.CVE_EMPRESA\n"+
				       "AND PE.ID_PERIODICIDAD= P.ID_PERIODICIDAD_PRODUCTO\n"+
				       "AND E.CVE_GPO_EMPRESA= P.CVE_GPO_EMPRESA\n"+
				       "AND E.CVE_EMPRESA= P.CVE_EMPRESA\n"+
				       "AND E.ID_ETAPA_PRESTAMO   = P.ID_ETAPA_PRESTAMO\n"+
				       "AND V.CVE_GPO_EMPRESA= GD.CVE_GPO_EMPRESA\n"+
				       "AND V.CVE_EMPRESA= GD.CVE_EMPRESA\n"+
				       "AND V.ID_PRESTAMO= GD.ID_PRESTAMO_GRUPO\n"+
				       "AND V.APLICA_A = 'GRUPO'\n"+
				       "GROUP BY GD.CVE_GPO_EMPRESA, Gd.Cve_Empresa, Gd.Id_Prestamo_Grupo, V.CVE_NOMBRE, V.NOMBRE,\n"+
				       "V.NUM_CICLO,\n"+
				       "G.NUM_INTEGRANTES,\n"+
				       "NVL(P.FECHA_REAL,P.FECHA_ENTREGA),\n"+
				       "V.PLAZO,\n"+
				       "V.PERIODICIDAD_PRODUCTO,\n"+
				       "V.VALOR_TASA,\n"+
				       "E.NOM_ESTATUS_PRESTAMO,\n"+
				       "V.MONTO_AUTORIZADO,\n"+
				       "V.MONTO_AUTORIZADO + V.CARGO_INICIAL,\n"+
				       "P.F_ULT_PAGO_REALIZADO,\n"+
				       "AA.IMP_SALDO_HOY,\n"+
				       "V.CVE_ASESOR_CREDITO,\n"+
				       "V.NOM_ASESOR_CREDITO,\n"+
				       "V.FECHA_ENTREGA,\n"+
				       "'MEX',\n"+
				       "'Mexico',\n"+
				       "V.ID_SUCURSAL,\n"+
				       "V.NOM_SUCURSAL,\n"+
				       "V.ID_REGIONAL,\n"+
				       "V.NOM_REGIONAL,\n"+
				       "V.CVE_USUARIO_COORDINADOR,\n"+
				       "V.NOM_COORDINADOR_SUCURSAL,\n"+
				       "V.CVE_FONDEADOR,\n"+
				       "V.NOM_FONDEADOR,\n"+
				       "V.NUM_LINEA,\n"+
				       "V.MONTO_AUTORIZADO,\n"+
				       "V.ID_PRODUCTO,\n"+
				       "V.NOM_PRODUCTO\n";			        
				        						
							 System.out.println("*****************Paso por aqui****************:"+sSql);
		
	    String sTipoReporte = request.getParameter("TipoReporte");
	    System.out.println("TipoReporte:"+sTipoReporte);
		parametros.put("Sql", sSql);
		parametros.put("PathLogotipo", contextoServlet.getRealPath("Portales/Sam/img/Imagentsys1.gif"));
		parametros.put("FechaReporte", Fecha2.formatoCorporativoHora(new Date()));
		parametros.put("NomReporte", "/Reportes/Sim/reportes/SimReportePrestamo.jasper");
		parametros.put("NombreReporte", "Reporte"+sClaveGrupo);
		
		return parametros;		
	}
}
