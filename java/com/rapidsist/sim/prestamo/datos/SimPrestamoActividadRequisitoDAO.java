/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para las actividades o requisitos del préstamo.
 */
 
public class SimPrestamoActividadRequisitoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionModificacion, OperacionBaja {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS DE BANCOS
		
		sSql = " SELECT \n"+
		       " ID_ETAPA_PRESTAMO \n"+
		       " FROM SIM_PRESTAMO \n"+
		       " WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
		       " AND CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
		       " AND ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n";
		ejecutaSql();
		if(rs.next()){
			parametros.addDefCampo("ID_ETAPA_PRESTAMO",rs.getString("ID_ETAPA_PRESTAMO") == null ? "" : rs.getString("ID_ETAPA_PRESTAMO"));
		}
		
		sSql =  " SELECT \n"+
			" 	PAR.CVE_GPO_EMPRESA, \n"+
			" 	PAR.CVE_EMPRESA, \n"+
			" 	PAR.ID_ACTIVIDAD_REQUISITO, \n"+
			" 	PAR.ID_ETAPA_PRESTAMO, \n"+
			" 	PAR.ID_PRESTAMO, \n"+
			" 	PAR.FECHA_REGISTRO, \n"+
			" 	PAR.FECHA_REALIZADA, \n"+
			" 	PAR.ESTATUS, \n"+
			" 	C.NOM_ACTIVIDAD_REQUISITO, \n"+
			" 	E.NOM_ESTATUS_PRESTAMO \n"+
			" FROM SIM_PRESTAMO_ETAPA PAR, \n"+
			"      SIM_CAT_ACTIVIDAD_REQUISITO C, \n"+
			"      SIM_CAT_ETAPA_PRESTAMO E \n"+
			" WHERE PAR.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND PAR.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND PAR.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND C.CVE_GPO_EMPRESA = PAR.CVE_GPO_EMPRESA \n" +
			" AND C.CVE_EMPRESA = PAR.CVE_EMPRESA \n"+
			" AND C.ID_ACTIVIDAD_REQUISITO = PAR.ID_ACTIVIDAD_REQUISITO \n"+
			" AND E.CVE_GPO_EMPRESA = PAR.CVE_GPO_EMPRESA \n" +
			" AND E.CVE_EMPRESA = PAR.CVE_EMPRESA \n"+
			" AND E.ID_ETAPA_PRESTAMO = PAR.ID_ETAPA_PRESTAMO \n"+
			" AND PAR.ID_ETAPA_PRESTAMO = '" + (String)parametros.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
			
		sSql = sSql + " ORDER BY NOM_ACTIVIDAD_REQUISITO \n";
		
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
		sSql =  " SELECT \n"+
			" 	PAR.CVE_GPO_EMPRESA, \n"+
			" 	PAR.CVE_EMPRESA, \n"+
			" 	PAR.ID_ACTIVIDAD_REQUISITO, \n"+
			" 	PAR.ID_ETAPA_PRESTAMO, \n"+
			" 	PAR.ID_PRESTAMO, \n"+
			" 	PAR.FECHA_REGISTRO, \n"+
			" 	PAR.FECHA_REALIZADA, \n"+
			" 	P.APLICA_A, \n"+
			" 	PAR.ESTATUS, \n"+
			" 	PAR.COMENTARIO, \n"+
			"E.B_ENTREGADO, \n"+
			"E.B_CANCELADO, \n"+
			"E.B_AUTORIZAR_COMITE, \n"+
			"E.B_LINEA_FONDEO, \n"+
			"E.B_DESEMBOLSO \n"+
			" FROM SIM_PRESTAMO_ETAPA PAR, \n"+
			" 	   SIM_PRESTAMO P, \n"+
			" 	   SIM_CAT_ETAPA_PRESTAMO E \n"+
			" WHERE PAR.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND PAR.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND PAR.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND PAR.ID_ACTIVIDAD_REQUISITO = '" + (String)parametros.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "' \n"+
			" AND PAR.ID_ETAPA_PRESTAMO = '" + (String)parametros.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
			" AND P.CVE_GPO_EMPRESA = PAR.CVE_GPO_EMPRESA \n" +
			" AND P.CVE_EMPRESA = PAR.CVE_EMPRESA \n" +
			" AND P.ID_PRESTAMO = PAR.ID_PRESTAMO \n" +
			" AND E.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n" +
			" AND E.CVE_EMPRESA = P.CVE_EMPRESA \n" +
			" AND E.ID_ETAPA_PRESTAMO = P.ID_ETAPA_PRESTAMO \n" ;
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
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
		System.out.println("Verifica si tiene permiso para avanzar la etapa"+sSql);
		ejecutaSql();
		if(rs.next()){
			//El usuario puede manipular la etapa y cada una de sus etapas.
			System.out.println("Si puede manipular la etapa"+(String)registro.getDefCampo("ID_ETAPA_PRESTAMO"));
			
			
			//Actualiza los atributos de fecha de registro, estatus, comentario, fecha realizada y usuario en SIM_PRESTAMO_ETAPA.
			sSql =  " UPDATE SIM_PRESTAMO_ETAPA SET \n" +
					" FECHA_REGISTRO	=TO_DATE('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','DD/MM/YYYY'), \n" +
					" ESTATUS			='" + (String)registro.getDefCampo("ESTATUS") + "', \n" ;
					
			if (registro.getDefCampo("FECHA_REALIZADA") != null){
				sSql = sSql + " FECHA_REALIZADA		= SYSDATE, \n" +
					          " CVE_USUARIO		='" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" ;
			}	
			sSql = sSql + " COMENTARIO			='" + (String)registro.getDefCampo("COMENTARIO") + "' \n" +
			 		      " WHERE ID_ACTIVIDAD_REQUISITO  ='" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "' \n" +
			 			  " AND ID_ETAPA_PRESTAMO  ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
			 			  " AND ID_PRESTAMO   		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			 			  " AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			 			  " AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
				  
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			PreparedStatement ps1 = this.conn.prepareStatement(sSql);
			ps1.execute();
			ResultSet rs1 = ps1.getResultSet();
			//Actualiza los atributos de fecha de registro, estatus, comentario, fecha realizada y usuario en SIM_PRESTAMO_ETAPA.
			
			//Actualiza la bitácora.
			sSql =  "SELECT \n"+
				"CVE_GPO_EMPRESA, \n"+
				"CVE_EMPRESA, \n"+
				"ID_PRESTAMO, \n"+
				"ID_ACTIVIDAD_REQUISITO, \n"+
				"ID_ETAPA_PRESTAMO, \n"+
				"FECHA_REGISTRO, \n"+
				"FECHA_REALIZADA, \n"+
				"ORDEN_ETAPA, \n"+
				"COMENTARIO, \n"+
				"CVE_USUARIO, \n"+
				"ESTATUS \n"+
				"FROM \n"+
				"SIM_PRESTAMO_ETAPA \n"+
				" WHERE ID_ACTIVIDAD_REQUISITO  ='" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "' \n" +
			 	" AND ID_ETAPA_PRESTAMO  ='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n" +
			 	" AND ID_PRESTAMO   		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			 	" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			 	" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
				
			PreparedStatement ps2 = this.conn.prepareStatement(sSql);
			ps2.execute();
			ResultSet rs2 = ps2.getResultSet();
			if (rs2.next()){
				registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs2.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs2.getString("ID_ACTIVIDAD_REQUISITO"));
				registro.addDefCampo("ID_ETAPA_PRESTAMO",rs2.getString("ID_ETAPA_PRESTAMO")== null ? "": rs2.getString("ID_ETAPA_PRESTAMO"));
				registro.addDefCampo("ORDEN_ETAPA",rs2.getString("ORDEN_ETAPA")== null ? "": rs2.getString("ORDEN_ETAPA"));
				registro.addDefCampo("FECHA_REGISTRO",rs2.getString("FECHA_REGISTRO")== null ? "": rs2.getString("FECHA_REGISTRO"));
				registro.addDefCampo("FECHA_REALIZADA",rs2.getString("FECHA_REALIZADA")== null ? "": rs2.getString("FECHA_REALIZADA"));
				registro.addDefCampo("COMENTARIO",rs2.getString("COMENTARIO")== null ? "": rs2.getString("COMENTARIO"));
				registro.addDefCampo("CVE_USUARIO",rs2.getString("CVE_USUARIO")== null ? "": rs2.getString("CVE_USUARIO"));
				registro.addDefCampo("ESTATUS",rs2.getString("ESTATUS")== null ? "": rs2.getString("ESTATUS"));
				
				sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
				PreparedStatement ps3 = this.conn.prepareStatement(sSql);
				ps3.execute();
				ResultSet rs3 = ps3.getResultSet();
				if (rs3.next()){
					registro.addDefCampo("ID_HISTORICO",rs3.getString("ID_HISTORICO"));
				}
				
				sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_HISTORICO, \n" +
					"ID_PRESTAMO, \n" +
					"ID_ACTIVIDAD_REQUISITO, \n" +
					"ID_ETAPA_PRESTAMO, \n"+
					"FECHA_REGISTRO, \n"+
					"FECHA_REALIZADA, \n"+
					"COMENTARIO, \n"+
					"ORDEN_ETAPA, \n"+
					"CVE_USUARIO, \n"+
					"ESTATUS) \n" +
					" VALUES (" +
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
					"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
					"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REALIZADA") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
					"'" + (String)registro.getDefCampo("COMENTARIO") + "', \n" +
					"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
					"'" + (String)registro.getDefCampo("ESTATUS") + "') \n" ;
				
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				PreparedStatement ps4 = this.conn.prepareStatement(sSql);
				ps4.execute();
				ResultSet rs4 = ps4.getResultSet();
			}
			//Actualiza la bitácora.
			
			sSql = " SELECT \n"+
			       " ID_ETAPA_PRESTAMO \n"+
			       " FROM SIM_PRESTAMO \n"+
			       " WHERE CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			       " AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			       " AND ID_PRESTAMO ='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
			PreparedStatement ps5 = this.conn.prepareStatement(sSql);
			ps5.execute();
			ResultSet rs5 = ps5.getResultSet();
			
			if(rs5.next()){
				registro.addDefCampo("ID_ETAPA_PRESTAMO",rs5.getString("ID_ETAPA_PRESTAMO"));
			}
			
			sSql =  "SELECT COUNT (*) ESTATUS FROM \n"+
				"( \n"+
				"SELECT DISTINCT \n"+
				"ESTATUS \n"+
				"FROM SIM_PRESTAMO_ETAPA \n"+
				"WHERE ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
				"AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
				"AND ESTATUS = 'Registrada' \n"+
				") \n";
			PreparedStatement ps6 = this.conn.prepareStatement(sSql);
			ps6.execute();
			ResultSet rs6 = ps6.getResultSet();
			
			//Si las actividades del etapa estan concluidas avanza de etapa.
			if(rs6.next()){
				String sEstatus = rs6.getString("ESTATUS");
				
				if (sEstatus.equals("0")){
					//Todas las actividades de la etapa están completas.
				
					sSql =  " SELECT DISTINCT ID_ETAPA_PRESTAMO \n"+
					"	  FROM SIM_PRESTAMO_ETAPA \n"+
					"	  WHERE ORDEN_ETAPA = (SELECT DISTINCT \n"+
					"	  ORDEN_ETAPA + 1 \n"+
					" 	  FROM SIM_PRESTAMO_ETAPA \n"+
					"	  WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"	  AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"	  AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
					"	  AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "') \n"+
					"	  AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
				
					PreparedStatement ps7 = this.conn.prepareStatement(sSql);
					ps7.execute();
					ResultSet rs7 = ps7.getResultSet();
				
					if(rs7.next()){
						//Pasa a la siguiente etapa.
						registro.addDefCampo("ID_ETAPA_PRESTAMO",rs7.getString("ID_ETAPA_PRESTAMO"));
						//Actualiza la etapa del crédito.
						sSql =  " UPDATE SIM_PRESTAMO SET \n"+
							"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
					
						PreparedStatement ps8 = this.conn.prepareStatement(sSql);
						ps8.execute();
						ResultSet rs8 = ps8.getResultSet();
						
						//Actualiza a sysdate y a registrada las etapas-actividades en SIM_PRESTAMO_ETAPA.
						sSql = " SELECT \n"+
						       " ID_ETAPA_PRESTAMO \n"+
						       " FROM SIM_PRESTAMO \n"+
						       " WHERE CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						       " AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						       " AND ID_PRESTAMO ='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
						PreparedStatement ps9 = this.conn.prepareStatement(sSql);
						ps9.execute();
						ResultSet rs9 = ps9.getResultSet();
						if(rs9.next()){
							registro.addDefCampo("ID_ETAPA_PRESTAMO",rs9.getString("ID_ETAPA_PRESTAMO"));
							//Actualiza a sysdate y a registrada las etapas-actividades en SIM_PRESTAMO_ETAPA.
							sSql =  " UPDATE SIM_PRESTAMO_ETAPA SET "+
								" FECHA_REGISTRO 		=SYSDATE, \n" +
								" ESTATUS 	 		='Registrada' \n" +
								" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
								" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
								" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n";
						
							PreparedStatement ps10 = this.conn.prepareStatement(sSql);
							ps10.execute();
							ResultSet rs10 = ps10.getResultSet();
							
							
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
							
							PreparedStatement ps11 = this.conn.prepareStatement(sSql);
							ps11.execute();
							ResultSet rs11 = ps11.getResultSet();
							
							while (rs11.next()){
								registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs11.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs11.getString("ID_ACTIVIDAD_REQUISITO"));
								registro.addDefCampo("ID_ETAPA_PRESTAMO",rs11.getString("ID_ETAPA_PRESTAMO")== null ? "": rs11.getString("ID_ETAPA_PRESTAMO"));
								registro.addDefCampo("ORDEN_ETAPA",rs11.getString("ORDEN_ETAPA")== null ? "": rs11.getString("ORDEN_ETAPA"));
								registro.addDefCampo("FECHA_REGISTRO",rs11.getString("FECHA_REGISTRO")== null ? "": rs11.getString("FECHA_REGISTRO"));
								registro.addDefCampo("COMENTARIO",rs11.getString("COMENTARIO")== null ? "": rs11.getString("COMENTARIO"));
								registro.addDefCampo("ESTATUS",rs11.getString("ESTATUS")== null ? "": rs11.getString("ESTATUS"));
							
								sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
								
								PreparedStatement ps12 = this.conn.prepareStatement(sSql);
								ps12.execute();
								ResultSet rs12 = ps12.getResultSet();	
								if (rs12.next()){
									registro.addDefCampo("ID_HISTORICO",rs12.getString("ID_HISTORICO"));
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
								
									PreparedStatement ps13 = this.conn.prepareStatement(sSql);
									ps13.execute();
									ResultSet rs13 = ps13.getResultSet();
								}
							}
						}
					}
					if (registro.getDefCampo("APLICA_A").equals("Grupo")){
						
					//Como se trata de un prestamo individual que pertenece a un grupo se actualiza el id_etapa_prestamo en SIM_PRESTAMO_GPO_DET.
					sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET \n"+
							"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "' \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
					
						PreparedStatement ps14 = this.conn.prepareStatement(sSql);
						ps14.execute();
						ResultSet rs14 = ps14.getResultSet();
					}
				}
			}//Si las actividades del etapa estan concluidas avanza de etapa.
			
				
		}//El usuario puede manipular la etapa y cada una de sus etapas.
		
		
		return resultadoCatalogo;
	}
	
	/**
	 * Borra un registro.
	 * @param registro Llave primaria.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo baja(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		//BORRA LA FUNCION
		sSql =  " DELETE FROM SIM_PRESTAMO_ETAPA " +
			" WHERE ID_ACTIVIDAD_REQUISITO  ='" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "' \n" +
			" AND ID_PRESTAMO   		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
			
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
}