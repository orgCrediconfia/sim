/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para la consulta de los saldos de un préstamo para la pantalla de movimientos extraordinarios.
 */
 
public class SimPrestamoMovimientoExtraordinarioMontosAplicadosDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
			int iRubros = 0;
			int iPago = 0;
			int iTabla = 0;
			int iRelacion = 0;
			String sVista = "";
			
			if (parametros.getDefCampo("APLICA_A").equals("INDIVIDUAL")){
			
				sVista = " SELECT \n"+
						" 	P.CVE_GPO_EMPRESA, \n"+
						" 	P.CVE_EMPRESA, \n"+
						" 	P.ID_PRESTAMO, \n"+
						"	P.ID_CLIENTE ID_PERSONA, \n"+
						"	PE.NOM_COMPLETO, \n";
						
				sSql = " SELECT \n"+
						"OC.CVE_GPO_EMPRESA, \n"+
						"OC.CVE_EMPRESA, \n"+
						"OC.CVE_OPERACION, \n"+
						"OC.CVE_CONCEPTO, \n"+
						"C.DESC_LARGA \n"+
						"FROM PFIN_CAT_OPERACION_CONCEPTO OC, \n"+
						"     PFIN_CAT_CONCEPTO C \n"+
						"WHERE OC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"AND OC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND C.CVE_GPO_EMPRESA = OC.CVE_GPO_EMPRESA \n"+
						"AND C.CVE_EMPRESA = OC.CVE_EMPRESA \n"+
						"AND C.CVE_CONCEPTO = OC.CVE_CONCEPTO \n"+
						"AND OC.CVE_OPERACION = '" + (String)parametros.getDefCampo("CVE_OPERACION") + "' \n";
				ejecutaSql();
				while (rs.next()){
					sVista = sVista + " A" + iRubros + ".CVE_CONCEPTO_" + iRubros + ",  \n";
					iRubros++;
				}		
						
						
				sVista = sVista + "TO_CHAR(M.MONTO_AUTORIZADO ,'999,999,999.9999') MONTO_AUTORIZADO \n";
				
				sVista = sVista + "FROM SIM_PRESTAMO P, \n"+
						"     RS_GRAL_PERSONA PE, \n";
						
						
				sSql = " SELECT \n"+
						"OC.CVE_GPO_EMPRESA, \n"+
						"OC.CVE_EMPRESA, \n"+
						"OC.CVE_OPERACION, \n"+
						"OC.CVE_CONCEPTO, \n"+
						"C.DESC_LARGA \n"+
						"FROM PFIN_CAT_OPERACION_CONCEPTO OC, \n"+
						"     PFIN_CAT_CONCEPTO C \n"+
						"WHERE OC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"AND OC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND C.CVE_GPO_EMPRESA = OC.CVE_GPO_EMPRESA \n"+
						"AND C.CVE_EMPRESA = OC.CVE_EMPRESA \n"+
						"AND C.CVE_CONCEPTO = OC.CVE_CONCEPTO \n"+
						"AND OC.CVE_OPERACION = '" + (String)parametros.getDefCampo("CVE_OPERACION") + "' \n";
				ejecutaSql();
				while (rs.next()){
					
					parametros.addDefCampo("CVE_CONCEPTO", rs.getString("CVE_CONCEPTO"));
					
					sVista = sVista + "  ( \n"+
					                     	"SELECT \n"+
												"OC.CVE_GPO_EMPRESA, \n"+
												"OC.CVE_EMPRESA, \n"+
												"OC.CVE_OPERACION, \n"+
												"OC.CVE_CONCEPTO CVE_CONCEPTO_" + iTabla + ", \n"+
												"C.DESC_LARGA \n"+
											"FROM PFIN_CAT_OPERACION_CONCEPTO OC, \n"+
											"     PFIN_CAT_CONCEPTO C \n"+
											"WHERE OC.CVE_GPO_EMPRESA = 'SIM' \n"+
											"AND OC.CVE_EMPRESA = 'CREDICONFIA' \n"+
											"AND C.CVE_GPO_EMPRESA = OC.CVE_GPO_EMPRESA \n"+
											"AND C.CVE_EMPRESA = OC.CVE_EMPRESA \n"+
											"AND C.CVE_CONCEPTO = OC.CVE_CONCEPTO \n"+
											"AND OC.CVE_OPERACION = '" + (String)parametros.getDefCampo("CVE_OPERACION") + "' \n"+
											"AND OC.CVE_CONCEPTO = '" + (String)parametros.getDefCampo("CVE_CONCEPTO") + "' \n"+
					                    ")A" + iTabla + ", \n";
					iTabla++;
				}		
						
						
				sVista = sVista + "  SIM_CLIENTE_MONTO M \n"+
						"WHERE  P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					    "AND    P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					    "AND    P.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
					    "AND    PE.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
					    "AND    PE.CVE_EMPRESA = P.CVE_EMPRESA \n"+
					    "AND    PE.ID_PERSONA = P.ID_CLIENTE \n"+
					    "AND    M.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
					    "AND    M.CVE_EMPRESA = P.CVE_EMPRESA \n"+
					    "AND    M.ID_PRESTAMO = P.ID_PRESTAMO \n"+
					    "AND    M.ID_CLIENTE = P.ID_CLIENTE \n";
				
				sSql = " SELECT \n"+
						"OC.CVE_GPO_EMPRESA, \n"+
						"OC.CVE_EMPRESA, \n"+
						"OC.CVE_OPERACION, \n"+
						"OC.CVE_CONCEPTO, \n"+
						"C.DESC_LARGA \n"+
						"FROM PFIN_CAT_OPERACION_CONCEPTO OC, \n"+
						"     PFIN_CAT_CONCEPTO C \n"+
						"WHERE OC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"AND OC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND C.CVE_GPO_EMPRESA = OC.CVE_GPO_EMPRESA \n"+
						"AND C.CVE_EMPRESA = OC.CVE_EMPRESA \n"+
						"AND C.CVE_CONCEPTO = OC.CVE_CONCEPTO \n"+
						"AND OC.CVE_OPERACION = '" + (String)parametros.getDefCampo("CVE_OPERACION") + "' \n";
				ejecutaSql();
				while (rs.next()){
					sVista = sVista + " AND A" + iRelacion + ".CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA  \n"+
									  " AND A" + iRelacion + ".CVE_EMPRESA = P.CVE_EMPRESA  \n";
					
					iRelacion++;
				}
				
			}else if (parametros.getDefCampo("APLICA_A").equals("GRUPO")){
				
				
				sVista = " SELECT \n"+
						 	"PG.CVE_GPO_EMPRESA, \n"+
						 	"PG.CVE_EMPRESA, \n"+
						 	"PG.ID_PRESTAMO_GRUPO, \n"+ 
						 	"PG.ID_PRESTAMO, \n"+
							"PG.ID_INTEGRANTE ID_PERSONA, \n"+ 
							"PE.NOM_COMPLETO, \n";
							
				sSql = " SELECT \n"+
						"OC.CVE_GPO_EMPRESA, \n"+
						"OC.CVE_EMPRESA, \n"+
						"OC.CVE_OPERACION, \n"+
						"OC.CVE_CONCEPTO, \n"+
						"C.DESC_LARGA \n"+
						"FROM PFIN_CAT_OPERACION_CONCEPTO OC, \n"+
						"     PFIN_CAT_CONCEPTO C \n"+
						"WHERE OC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"AND OC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND C.CVE_GPO_EMPRESA = OC.CVE_GPO_EMPRESA \n"+
						"AND C.CVE_EMPRESA = OC.CVE_EMPRESA \n"+
						"AND C.CVE_CONCEPTO = OC.CVE_CONCEPTO \n"+
						"AND OC.CVE_OPERACION = '" + (String)parametros.getDefCampo("CVE_OPERACION") + "' \n";
				ejecutaSql();
				while (rs.next()){
					sVista = sVista + " A" + iRubros + ".CVE_CONCEPTO_" + iRubros + ",  \n";
					iRubros++;
				}
							
				sVista = sVista + "TO_CHAR(PG.MONTO_AUTORIZADO ,'999,999,999.9999') MONTO_AUTORIZADO \n"+
						    "FROM SIM_PRESTAMO_GPO_DET PG, \n";
				
				sSql = " SELECT \n"+
						"OC.CVE_GPO_EMPRESA, \n"+
						"OC.CVE_EMPRESA, \n"+
						"OC.CVE_OPERACION, \n"+
						"OC.CVE_CONCEPTO, \n"+
						"C.DESC_LARGA \n"+
						"FROM PFIN_CAT_OPERACION_CONCEPTO OC, \n"+
						"     PFIN_CAT_CONCEPTO C \n"+
						"WHERE OC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"AND OC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND C.CVE_GPO_EMPRESA = OC.CVE_GPO_EMPRESA \n"+
						"AND C.CVE_EMPRESA = OC.CVE_EMPRESA \n"+
						"AND C.CVE_CONCEPTO = OC.CVE_CONCEPTO \n"+
						"AND OC.CVE_OPERACION = '" + (String)parametros.getDefCampo("CVE_OPERACION") + "' \n";
				ejecutaSql();
				while (rs.next()){
					
					parametros.addDefCampo("CVE_CONCEPTO", rs.getString("CVE_CONCEPTO"));
					
					sVista = sVista + "  ( \n"+
					                     	"SELECT \n"+
												"OC.CVE_GPO_EMPRESA, \n"+
												"OC.CVE_EMPRESA, \n"+
												"OC.CVE_OPERACION, \n"+
												"OC.CVE_CONCEPTO CVE_CONCEPTO_" + iTabla + ", \n"+
												"C.DESC_LARGA \n"+
											"FROM PFIN_CAT_OPERACION_CONCEPTO OC, \n"+
											"     PFIN_CAT_CONCEPTO C \n"+
											"WHERE OC.CVE_GPO_EMPRESA = 'SIM' \n"+
											"AND OC.CVE_EMPRESA = 'CREDICONFIA' \n"+
											"AND C.CVE_GPO_EMPRESA = OC.CVE_GPO_EMPRESA \n"+
											"AND C.CVE_EMPRESA = OC.CVE_EMPRESA \n"+
											"AND C.CVE_CONCEPTO = OC.CVE_CONCEPTO \n"+
											"AND OC.CVE_OPERACION = '" + (String)parametros.getDefCampo("CVE_OPERACION") + "' \n"+
											"AND OC.CVE_CONCEPTO = '" + (String)parametros.getDefCampo("CVE_CONCEPTO") + "' \n"+
					                    ")A" + iTabla + ", \n";
					iTabla++;
				}
				
				sVista = sVista + "		RS_GRAL_PERSONA PE \n"+
							"WHERE  PG.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							"AND    PG.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND    PG.ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
							"AND    PE.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+ 
							"AND    PE.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
							"AND    PE.ID_PERSONA = PG.ID_INTEGRANTE \n";
				
				sSql = " SELECT \n"+
						"OC.CVE_GPO_EMPRESA, \n"+
						"OC.CVE_EMPRESA, \n"+
						"OC.CVE_OPERACION, \n"+
						"OC.CVE_CONCEPTO, \n"+
						"C.DESC_LARGA \n"+
						"FROM PFIN_CAT_OPERACION_CONCEPTO OC, \n"+
						"     PFIN_CAT_CONCEPTO C \n"+
						"WHERE OC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"AND OC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND C.CVE_GPO_EMPRESA = OC.CVE_GPO_EMPRESA \n"+
						"AND C.CVE_EMPRESA = OC.CVE_EMPRESA \n"+
						"AND C.CVE_CONCEPTO = OC.CVE_CONCEPTO \n"+
						"AND OC.CVE_OPERACION = '" + (String)parametros.getDefCampo("CVE_OPERACION") + "' \n";
				ejecutaSql();
				while (rs.next()){
					sVista = sVista + " AND A" + iRelacion + ".CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA  \n"+
									  " AND A" + iRelacion + ".CVE_EMPRESA = PG.CVE_EMPRESA  \n";
					
					iRelacion++;
				}
			}
			
			sSql = sVista;
			
		ejecutaSql();
		return getConsultaLista();
	}
	
	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql =  "SELECT COUNT (*) CONCEPTOS \n"+
				"FROM ( \n"+
				"	SELECT \n"+
				"	OC.CVE_GPO_EMPRESA, \n"+
				"	OC.CVE_EMPRESA, \n"+
				"	OC.CVE_OPERACION, \n"+
				"	OC.CVE_CONCEPTO, \n"+
				"	C.DESC_LARGA \n"+
				"	FROM PFIN_CAT_OPERACION_CONCEPTO OC, \n"+
				"	     PFIN_CAT_CONCEPTO C \n"+
				"	WHERE OC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				"	AND OC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
				"	AND C.CVE_GPO_EMPRESA = OC.CVE_GPO_EMPRESA \n"+ 
				"	AND C.CVE_EMPRESA = OC.CVE_EMPRESA \n"+
				"	AND C.CVE_CONCEPTO = OC.CVE_CONCEPTO \n"+
				"	AND OC.CVE_OPERACION = '" + (String)parametros.getDefCampo("CVE_OPERACION") + "' \n" +
				") \n";
 			
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
}