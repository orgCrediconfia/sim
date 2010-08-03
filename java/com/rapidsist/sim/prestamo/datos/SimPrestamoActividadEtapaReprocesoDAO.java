/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta; 
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para regresa las etapas de un credito individual.
 */
 
public class SimPrestamoActividadEtapaReprocesoDAO extends Conexion2 implements OperacionAlta {

	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
			//Verifica si tiene permisos para manipular la etapa.
			sSql =  " SELECT \n"+
					"CVE_FUNCION, \n"+
					"CVE_APLICACION, \n"+
					"CVE_PERFIL \n"+
					"FROM RS_CONF_APLICACION_CONFIG \n"+
					"WHERE CVE_FUNCION = (SELECT \n"+
					"CVE_FUNCION \n"+
					"FROM \n"+
					"SIM_CAT_ETAPA_PRESTAMO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
					") \n"+
					"AND CVE_APLICACION = 'Prestamo' \n"+
					"AND CVE_PERFIL = ( \n"+
					"SELECT \n"+
					"CVE_PERFIL \n"+ 
					"FROM RS_GRAL_USUARIO_PERFIL \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_USUARIO = '" + (String)registro.getDefCampo("CVE_USUARIO") + "' \n"+
					"AND CVE_APLICACION = 'Prestamo') \n";
			ejecutaSql();
			
			if(rs.next()){
				
				//Retrocede etapa.
				//Obtiene las actividades de la etapa que se retroce para inicializarlas.
				sSql =  "SELECT \n"+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_ETAPA_PRESTAMO, \n"+
						"ID_ACTIVIDAD_REQUISITO, \n" +
						"FECHA_REGISTRO, \n" +
						"ORDEN_ETAPA \n" +
						"FROM \n" +
						"SIM_PRESTAMO_ETAPA \n" +
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
						"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
				
				PreparedStatement ps2 = this.conn.prepareStatement(sSql);
				ps2.execute();
				ResultSet rs2 = ps2.getResultSet();
				
				while (rs2.next()){
					registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs2.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs2.getString("ID_ACTIVIDAD_REQUISITO"));
					registro.addDefCampo("ORDEN_ETAPA",rs2.getString("ORDEN_ETAPA")== null ? "": rs2.getString("ORDEN_ETAPA"));
					registro.addDefCampo("FECHA_REGISTRO",rs2.getString("FECHA_REGISTRO")== null ? "": rs2.getString("FECHA_REGISTRO"));
					//Guarda el historial de la etapa y las actividades
					sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
					PreparedStatement ps3 = this.conn.prepareStatement(sSql);
					ps3.execute();
					ResultSet rs3 = ps3.getResultSet();
					if (rs3.next()){
						registro.addDefCampo("ID_HISTORICO",rs3.getString("ID_HISTORICO"));
						
						sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
							"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_HISTORICO, \n" +
							"ID_PRESTAMO, \n" +
							"ID_ACTIVIDAD_REQUISITO, \n" +
							"ID_ETAPA_PRESTAMO, \n"+
							"FECHA_REALIZADA, \n"+
							"FECHA_REGISTRO, \n"+
							"ESTATUS, \n"+
							"CVE_USUARIO, \n"+
							"ORDEN_ETAPA) \n"+
							" VALUES (" +
							"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
							"'', \n"+
							"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
							"'Regresar etapa', \n"+
							"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
							"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "') \n" ;
					
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
					}
				}
					
				//Obtiene la etapa anterior.
				sSql =  " SELECT DISTINCT ID_ETAPA_PRESTAMO, ORDEN_ETAPA \n"+
					"	  FROM SIM_PRESTAMO_ETAPA \n"+
					"	  WHERE ORDEN_ETAPA = (SELECT DISTINCT \n"+
					"	  ORDEN_ETAPA - 1 \n"+
					" 	  FROM SIM_PRESTAMO_ETAPA \n"+
					"	  WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"	  AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"	  AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
					"	  AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "') \n"+
					" 	  AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
				
					ejecutaSql();
					if(rs.next()){
						//Retrocede a la etapa anterior.
						registro.addDefCampo("ID_ETAPA_PRESTAMO",rs.getString("ID_ETAPA_PRESTAMO"));
						registro.addDefCampo("ORDEN_ETAPA",rs.getString("ORDEN_ETAPA"));
						
						sSql =  " UPDATE SIM_PRESTAMO SET \n"+
							"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
						
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
						
						sSql =  " UPDATE SIM_PRESTAMO_ETAPA SET "+
								" FECHA_REGISTRO 		=SYSDATE, \n" +
								" ESTATUS 	 		='Registrada', \n" +
								" COMENTARIO 	 		='' \n" +
								" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
								" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
						
						if (ejecutaUpdate() == 0){
							resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
						}
						
						sSql =  "SELECT \n"+
								"CVE_GPO_EMPRESA, \n"+
								"CVE_EMPRESA, \n"+
								"ID_PRESTAMO, \n"+
								"ID_ACTIVIDAD_REQUISITO, \n"+
								"ID_ETAPA_PRESTAMO, \n"+
								"FECHA_REGISTRO, \n"+
								"FECHA_REALIZADA, \n"+
								"COMENTARIO, \n"+
								"ORDEN_ETAPA, \n"+
								"ESTATUS \n" +
								"FROM \n"+
								"SIM_PRESTAMO_ETAPA \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
								"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
						
							PreparedStatement ps10 = this.conn.prepareStatement(sSql);
							ps10.execute();
							ResultSet rs10 = ps10.getResultSet();
							
							while (rs10.next()){
								registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs10.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs10.getString("ID_ACTIVIDAD_REQUISITO"));
								registro.addDefCampo("ID_ETAPA_PRESTAMO",rs10.getString("ID_ETAPA_PRESTAMO")== null ? "": rs10.getString("ID_ETAPA_PRESTAMO"));
								registro.addDefCampo("ORDEN_ETAPA",rs10.getString("ORDEN_ETAPA")== null ? "": rs10.getString("ORDEN_ETAPA"));
								registro.addDefCampo("FECHA_REGISTRO",rs10.getString("FECHA_REGISTRO")== null ? "": rs10.getString("FECHA_REGISTRO"));
								registro.addDefCampo("COMENTARIO",rs10.getString("COMENTARIO")== null ? "": rs10.getString("COMENTARIO"));
								registro.addDefCampo("ESTATUS",rs10.getString("ESTATUS")== null ? "": rs10.getString("ESTATUS"));
							
								sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
								
								PreparedStatement ps11 = this.conn.prepareStatement(sSql);
								ps11.execute();
								ResultSet rs11 = ps11.getResultSet();	
								if (rs11.next()){
									registro.addDefCampo("ID_HISTORICO",rs11.getString("ID_HISTORICO"));
									//Actualiza la bitácora.
									sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
										"CVE_GPO_EMPRESA, \n" +
										"CVE_EMPRESA, \n" +
										"ID_HISTORICO, \n" +
										"ID_PRESTAMO, \n" +
										"ID_ACTIVIDAD_REQUISITO, \n" +
										"ID_ETAPA_PRESTAMO, \n"+
										"FECHA_REGISTRO, \n"+
										"ORDEN_ETAPA, \n"+
										"COMENTARIO, \n"+
										"ESTATUS) \n" +
										" VALUES (" +
										"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
										"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
										"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
										"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
										"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
										"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
										"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
										"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "', \n" +
										"'" + (String)registro.getDefCampo("COMENTARIO") + "', \n" +
										"'" + (String)registro.getDefCampo("ESTATUS") + "') \n" ;
									
									PreparedStatement ps12 = this.conn.prepareStatement(sSql);
									ps12.execute();
									ResultSet rs12 = ps12.getResultSet();
								}
							}
						
						
						
						
					}
				}	
			/////////////////////////
				
			
				
				sSql =  "SELECT \n"+
						"B_LINEA_FONDEO \n" +
						"FROM \n" +
						"SIM_CAT_ETAPA_PRESTAMO \n" +
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
				PreparedStatement ps3 = this.conn.prepareStatement(sSql);
				ps3.execute();
				ResultSet rs3 = ps3.getResultSet();
				if(rs3.next()){
					String sBLineaFondeo = rs3.getString("B_LINEA_FONDEO");
					
					if (sBLineaFondeo.equals("V")){
						
						sSql =  "SELECT \n"+
								"NUM_LINEA \n" +
								"FROM \n" +
								"SIM_PRESTAMO \n" +
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
						PreparedStatement ps4 = this.conn.prepareStatement(sSql);
						ps4.execute();
						ResultSet rs4 = ps4.getResultSet();
						if(rs4.next()){
							registro.addDefCampo("NUM_LINEA",rs4.getString("NUM_LINEA")== null ? "": rs4.getString("NUM_LINEA"));
						}
						
						sSql =  "SELECT \n"+
								"MONTO_DISPONIBLE \n" +
								"FROM \n" +
								"SIM_CAT_FONDEADOR_LINEA \n" +
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND NUM_LINEA = '" + (String)registro.getDefCampo("NUM_LINEA") + "' \n";
						PreparedStatement ps5 = this.conn.prepareStatement(sSql);
						ps5.execute();
						ResultSet rs5 = ps5.getResultSet();
						
						if(rs5.next()){
							registro.addDefCampo("MONTO_DISPONIBLE",rs5.getString("MONTO_DISPONIBLE")== null ? "": rs5.getString("MONTO_DISPONIBLE"));
						}
						
						sSql =  "SELECT \n"+
								"M.MONTO_AUTORIZADO + NVL(C.CARGO_INICIAL,C.PORCENTAJE_MONTO/100*M.MONTO_AUTORIZADO) IMPORTE_PRESTADO \n"+
								"FROM SIM_PRESTAMO P, \n"+
								"RS_GRAL_PERSONA N, \n"+
								"SIM_CLIENTE_MONTO M, \n"+
								"SIM_PRESTAMO_CARGO_COMISION C \n"+
								"WHERE P.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND P.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND P.ID_PRESTAMO ='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
								"AND P.ID_PRESTAMO NOT IN (SELECT ID_PRESTAMO FROM SIM_PRESTAMO_GPO_DET) \n"+
								"AND N.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
								"AND N.CVE_EMPRESA = P.CVE_EMPRESA \n"+ 
								"AND N.ID_PERSONA = P.ID_CLIENTE \n"+
								"AND M.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
								"AND M.CVE_EMPRESA = P.CVE_EMPRESA \n"+
								"AND M.ID_PRESTAMO = P.ID_PRESTAMO \n"+
								"AND C.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
								"AND C.CVE_EMPRESA = P.CVE_EMPRESA \n"+ 
								"AND C.ID_PRESTAMO = P.ID_PRESTAMO \n"+
								"AND C.ID_FORMA_APLICACION = 1 \n";
						
						PreparedStatement ps6 = this.conn.prepareStatement(sSql);
						ps6.execute();
						ResultSet rs6 = ps6.getResultSet();
						if(rs6.next()){
							registro.addDefCampo("IMPORTE_PRESTADO",rs6.getString("IMPORTE_PRESTADO")== null ? "": rs6.getString("IMPORTE_PRESTADO"));
						}
						
						sSql =  " UPDATE SIM_CAT_FONDEADOR_LINEA SET "+
								" MONTO_DISPONIBLE 		='" + (String)registro.getDefCampo("MONTO_DISPONIBLE") + "' + '" + (String)registro.getDefCampo("IMPORTE_PRESTADO") + "' \n"+
								" WHERE NUM_LINEA		='" + (String)registro.getDefCampo("NUM_LINEA") + "' \n"+
								" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
						PreparedStatement ps7 = this.conn.prepareStatement(sSql);
						ps7.execute();
						ResultSet rs7 = ps7.getResultSet();
						
						sSql =  " UPDATE SIM_PRESTAMO SET \n"+
								"NUM_LINEA = '' \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
						
						PreparedStatement ps8 = this.conn.prepareStatement(sSql);
						ps8.execute();
						ResultSet rs8 = ps8.getResultSet();
						
					
					}	
				}
				
				sSql =  "SELECT \n"+
						"B_AUTORIZAR_COMITE \n" +
						"FROM \n" +
						"SIM_CAT_ETAPA_PRESTAMO \n" +
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
				PreparedStatement ps9 = this.conn.prepareStatement(sSql);
				ps9.execute();
				ResultSet rs9 = ps9.getResultSet();
				if(rs9.next()){
						String sBAutorizarComite = rs9.getString("B_AUTORIZAR_COMITE");
					
					if (sBAutorizarComite.equals("V")){
						sSql =  " UPDATE SIM_CLIENTE_MONTO SET \n"+
								"MONTO_AUTORIZADO = '' \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
						
						PreparedStatement ps10 = this.conn.prepareStatement(sSql);
						ps10.execute();
						ResultSet rs10 = ps10.getResultSet();
						
						sSql =  " UPDATE SIM_PRESTAMO SET \n"+
								"FECHA_ENTREGA = '', \n"+
								"FECHA_REAL = '', \n"+
								"DIA_SEMANA_PAGO = '' \n"+
								"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
						
						PreparedStatement ps11 = this.conn.prepareStatement(sSql);
						ps11.execute();
						ResultSet rs11 = ps11.getResultSet();
						
					}
				}
			
		
		return resultadoCatalogo;
	}
}