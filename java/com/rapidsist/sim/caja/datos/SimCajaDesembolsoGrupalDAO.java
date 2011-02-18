/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para la entrega de prestamos grupales.
 */
 
public class SimCajaDesembolsoGrupalDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta, OperacionConsultaRegistro {
	
	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		if (parametros.getDefCampo("CONSULTA").equals("TODOS")){
			
			sSql =  "SELECT \n"+
					"C.CVE_GPO_EMPRESA, \n"+
					"C.CVE_EMPRESA, \n"+
					"C.ID_PRESTAMO ID_PRESTAMO_GRUPO, \n"+
					"C.CVE_PRESTAMO CVE_PRESTAMO_GRUPO, \n"+
					"C.CVE_NOMBRE ID_GRUPO, \n"+
					"C.NOMBRE NOM_GRUPO, \n"+
					"C.ID_PRODUCTO, \n"+
					"C.NOM_PRODUCTO, \n"+
					"C.NUM_CICLO, \n"+
					"TO_CHAR(C.MONTO_AUTORIZADO,'999,999,999.99') MONTO_PRESTADO \n"+
					"FROM V_CREDITO C, \n"+
					"SIM_CAT_ETAPA_PRESTAMO E, \n"+
					"SIM_USUARIO_ACCESO_SUCURSAL US \n"+
					"WHERE C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND C.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
					"AND C.APLICA_A = 'GRUPO' \n"+
					"AND E.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+ 
					"AND E.CVE_EMPRESA = C.CVE_EMPRESA \n"+ 
					"AND E.ID_ETAPA_PRESTAMO = C.ID_ETAPA_PRESTAMO \n"+ 
					"AND E.B_DESEMBOLSO = 'V' \n"+ 
					"AND US.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
		            "AND US.CVE_EMPRESA = C.CVE_EMPRESA \n"+
		            "AND US.ID_SUCURSAL = C.ID_SUCURSAL \n"+
		            "AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n";
			
			if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
				sSql = sSql + "AND C.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
			}
			if (parametros.getDefCampo("NOM_GRUPO") != null) {
				sSql = sSql + " AND UPPER(C.NOMBRE) LIKE '%" + ((String) parametros.getDefCampo("NOM_GRUPO")).toUpperCase() + "%' \n";
			}
			
		}else if (parametros.getDefCampo("CONSULTA").equals("SUMA")){
			sSql =  "SELECT \n"+
					"SUM(PD.MONTO_AUTORIZADO) TOTAL \n"+ 
					"FROM SIM_PRESTAMO_GRUPO PG, \n"+
					"SIM_GRUPO G, \n"+
					"SIM_PRESTAMO_GPO_DET PD, \n"+
					"SIM_PRODUCTO PR, \n"+ 
					"RS_GRAL_PERSONA PE, \n"+
					"SIM_PRESTAMO P, \n"+
					"SIM_CAT_ETAPA_PRESTAMO E \n"+
					"WHERE PG.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND PG.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND PG.ID_GRUPO = '" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
					"AND PG.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
					"AND PG.NUM_CICLO = '" + (String)parametros.getDefCampo("NUM_CICLO") + "' \n"+
					"AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
					"AND G.CVE_EMPRESA = PG.CVE_EMPRESA \n"+ 
					"AND G.ID_GRUPO = PG.ID_GRUPO \n"+
					"AND G.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
					"AND PD.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
					"AND PD.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
					"AND PD.ID_PRESTAMO_GRUPO = PG.ID_PRESTAMO_GRUPO \n"+
					"AND PR.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+ 
					"AND PR.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
					"AND PR.ID_PRODUCTO = PG.ID_PRODUCTO \n"+
					"AND PE.CVE_GPO_EMPRESA = PD.CVE_GPO_EMPRESA \n"+
					"AND PE.CVE_EMPRESA = PD.CVE_EMPRESA \n"+
					"AND PE.ID_PERSONA = PD.ID_INTEGRANTE \n"+
					"AND P.CVE_GPO_EMPRESA = PD.CVE_GPO_EMPRESA \n"+
					"AND P.CVE_EMPRESA = PD.CVE_EMPRESA \n"+
					"AND P.ID_PRESTAMO = PD.ID_PRESTAMO \n"+
					
					"AND E.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+ 
					"AND E.CVE_EMPRESA = PG.CVE_EMPRESA \n"+ 
					"AND E.ID_ETAPA_PRESTAMO = PG.ID_ETAPA_PRESTAMO \n"+ 
					"AND E.B_DESEMBOLSO = 'V' \n"+ 
					
					"AND P.B_ENTREGADO != 'V' \n"+
					"AND P.ID_ETAPA_PRESTAMO != '18' \n";
		}else if (parametros.getDefCampo("CONSULTA").equals("INDIVIDUALES")){
			
		}
				
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
		/*
		sSql =  "SELECT \n"+
				"SUM(M.MONTO_AUTORIZADO) TOTAL \n"+ 
				"FROM SIM_PRESTAMO P, \n"+ 
				"SIM_CLIENTE_MONTO M, \n"+
				"SIM_PRODUCTO PR, \n"+
				"RS_GRAL_PERSONA PE, \n"+
		        "SIM_GRUPO G \n"+
		    	"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND P.ID_GRUPO = '" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
		        "AND P.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
		        "AND P.NUM_CICLO = '" + (String)parametros.getDefCampo("NUM_CICLO") + "' \n"+
		        "AND P.ID_ETAPA_PRESTAMO = '11' \n"+
				"AND P.B_ENTREGADO != 'V' \n"+
				"AND M.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+ 
				"AND M.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND M.ID_PRESTAMO = P.ID_PRESTAMO \n"+
				"AND PE.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+ 
				"AND PE.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND PE.ID_PERSONA = P.ID_CLIENTE \n"+
				"AND PR.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+ 
				"AND PR.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND PR.ID_PRODUCTO = P.ID_PRODUCTO \n"+
		        "AND G.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+ 
				"AND G.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND G.ID_GRUPO = P.ID_GRUPO \n"+
		        "AND P.ID_GRUPO IS NOT NULL \n";
		        */
			
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		resultadoCatalogo.Resultado = new Registro();
		
		String sIdTransaccion = "";
		int iIdTransaccion = 0;
		String sIdTransaccionGrupo = "";
		String sMontoCaja = "";
		float fMontoCaja = 0;
		String sMonto = "";
		float fMonto = 0;
		
		//Obtiene el monto de la caja.
		sSql =  "SELECT \n" +
				"SUM(MONTO) MONTO_CAJA \n" +
				"FROM \n" +
				"SIM_CAJA_TRANSACCION \n" +
				"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND ID_CAJA = '" + (String)registro.getDefCampo("ID_CAJA") + "' \n"+
				"AND ID_SUCURSAL = '" + (String)registro.getDefCampo("ID_SUCURSAL") + "' \n";
		ejecutaSql();
		if (rs.next()){
			sMontoCaja = rs.getString("MONTO_CAJA");
			
			if (sMontoCaja == null){
				sMontoCaja = "0";
			}
			
			fMontoCaja = Float.parseFloat(sMontoCaja.trim());
		}
	
		//Obtiene el monto autorizado.
		sSql =  "SELECT \n"+
				"C.MONTO_AUTORIZADO MONTO_PRESTADO \n"+
				"FROM V_CREDITO C \n"+
				"WHERE C.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND C.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND C.ID_PRESTAMO ='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
				"AND C.APLICA_A = 'GRUPO' \n"+
				"AND C.ID_ETAPA_PRESTAMO != '18' \n";
		ejecutaSql();
		if (rs.next()){
			sMonto = rs.getString("MONTO_PRESTADO");
			fMonto = Float.parseFloat(sMonto.trim());
		}
		
		//Verifica si existen fondos suficientes en la caja para hacer el desembolso.
		if (fMontoCaja < fMonto){
			resultadoCatalogo.mensaje.setClave("FONDO_INSUFICIENTE");
		}else {
		
			sSql = "SELECT SQ01_SIM_CAJA_TRANSACCION_GPO.nextval AS ID_TRANSACCION_GRUPO FROM DUAL";
			
			ejecutaSql();
			if (rs.next()){
				sIdTransaccionGrupo = rs.getString("ID_TRANSACCION_GRUPO");
			}
			
			sSql =  "SELECT \n" +
					"G.CVE_GPO_EMPRESA, \n" +
					"G.CVE_EMPRESA, \n" +
					"D.ID_PRESTAMO, \n" +
					"G.ID_PRESTAMO_GRUPO, \n" +
					"D.MONTO_AUTORIZADO, \n" +
					"G.ID_PRODUCTO, \n" +
					"G.NUM_CICLO, \n" +
					"D.ID_INTEGRANTE, \n" +
					"G.ID_GRUPO, \n" +
					"A.CARGO_INICIAL, \n" +
					"D.MONTO_AUTORIZADO MONTO_PRESTADO \n" +
					"FROM \n" +
					"SIM_PRESTAMO_GRUPO G, \n" +
					"SIM_PRESTAMO_GPO_DET D, \n" +
					"(SELECT \n" + 
					"P.CVE_GPO_EMPRESA, \n" +
					"P.CVE_EMPRESA, \n" +
					"P.ID_PRESTAMO, \n" +
					"P.ID_PRESTAMO_GRUPO, \n" +
					"SUM(C.CARGO_INICIAL) CARGO_INICIAL \n" +
					"FROM SIM_PRESTAMO_GPO_DET P, \n" + 
					"SIM_PRESTAMO_CARGO_COMISION C \n" +
					"WHERE P.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND P.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND P.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
					"AND C.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n" +
					"AND C.CVE_EMPRESA = P.CVE_EMPRESA \n" + 
					"AND C.ID_PRESTAMO = P.ID_PRESTAMO \n" +
					"AND C.ID_FORMA_APLICACION = 1 \n" + 
					"GROUP BY \n" +
					"P.CVE_GPO_EMPRESA, \n" +
					"P.CVE_EMPRESA, \n" +
					"P.ID_PRESTAMO, \n" +
					"P.ID_PRESTAMO_GRUPO)A \n" +
					"WHERE G.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND G.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND G.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
					"AND D.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n" +
					"AND D.CVE_EMPRESA = G.CVE_EMPRESA \n" +
					"AND D.ID_PRESTAMO_GRUPO = G.ID_PRESTAMO_GRUPO \n" +
					"AND A.CVE_GPO_EMPRESA = D.CVE_GPO_EMPRESA \n" +
					"AND A.CVE_EMPRESA = D.CVE_EMPRESA \n" +
					"AND A.ID_PRESTAMO_GRUPO = D.ID_PRESTAMO_GRUPO \n" +
					"AND A.ID_PRESTAMO = D.ID_PRESTAMO \n" ;
			
			PreparedStatement ps1 = this.conn.prepareStatement(sSql);
			ps1.execute();
			ResultSet rs1 = ps1.getResultSet();
			while (rs1.next()){
				
				registro.addDefCampo("ID_PRESTAMO",rs1.getString("ID_PRESTAMO")== null ? "": rs1.getString("ID_PRESTAMO"));
				registro.addDefCampo("ID_PRESTAMO_GRUPO",rs1.getString("ID_PRESTAMO_GRUPO")== null ? "": rs1.getString("ID_PRESTAMO_GRUPO"));
				registro.addDefCampo("MONTO_PRESTADO",rs1.getString("MONTO_PRESTADO")== null ? "": rs1.getString("MONTO_PRESTADO"));
				registro.addDefCampo("ID_PRODUCTO",rs1.getString("ID_PRODUCTO")== null ? "": rs1.getString("ID_PRODUCTO"));
				registro.addDefCampo("NUM_CICLO",rs1.getString("NUM_CICLO")== null ? "": rs1.getString("NUM_CICLO"));
				registro.addDefCampo("ID_INTEGRANTE",rs1.getString("ID_INTEGRANTE")== null ? "": rs1.getString("ID_INTEGRANTE"));
				registro.addDefCampo("ID_GRUPO",rs1.getString("ID_GRUPO")== null ? "": rs1.getString("ID_GRUPO"));
				
				//OBTENEMOS EL SEQUENCE
				sSql =  "SELECT \n" +
						"CVE_GPO_EMPRESA, \n" +
						"MAX(ID_TRANSACCION) ID_TRANSACCION \n" +
						"FROM \n" +
						"SIM_CAJA_TRANSACCION \n" +
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND CVE_MOVIMIENTO_CAJA = '" + (String)registro.getDefCampo("CVE_MOVIMIENTO_CAJA") + "' \n"+
						"GROUP BY CVE_GPO_EMPRESA \n";
				PreparedStatement ps2 = this.conn.prepareStatement(sSql);
				ps2.execute();
				ResultSet rs2 = ps2.getResultSet();
				
				if (rs2.next()){
					
					sIdTransaccion = rs2.getString("ID_TRANSACCION");
					iIdTransaccion=Integer.parseInt(sIdTransaccion.trim());
					iIdTransaccion ++;
					sIdTransaccion= String.valueOf(iIdTransaccion);
				}else {
					
					sIdTransaccion = "1";
				}
				
				//Inserta el movimiento del desembolso grupal por cada préstamo individual.
				sSql =  "INSERT INTO SIM_CAJA_TRANSACCION ( \n"+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_TRANSACCION, \n" +
						"ID_TRANSACCION_GRUPO, \n" +
						"CVE_MOVIMIENTO_CAJA, \n" +
						"ID_PRESTAMO, \n" +
						"ID_PRESTAMO_GRUPO, \n" +
						"ID_PRODUCTO, \n" +
						"NUM_CICLO, \n" +
						"ID_CLIENTE, \n" +
						"ID_GRUPO, \n" +
						"ID_SUCURSAL, \n" +
						"ID_CAJA, \n" +
						"MONTO, \n" +
						"FECHA_TRANSACCION, \n" +
						"CVE_USUARIO_CAJERO) \n" +
				        "VALUES ( \n"+
						"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
						sIdTransaccion + ", \n "+
						sIdTransaccionGrupo + ", \n "+
						"'DESGPO', \n" +
						"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
						"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_INTEGRANTE") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
						"-'" + (String)registro.getDefCampo("MONTO_PRESTADO") + "', \n" +
						"SYSDATE, \n" +
						"'" + (String)registro.getDefCampo("CVE_USUARIO_CAJERO") + "') \n" ;
				
				//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
				PreparedStatement ps3 = this.conn.prepareStatement(sSql);
				ps3.execute();
				ResultSet rs3 = ps3.getResultSet();
			
				//Marca cada prestamo que conforma el crédito grupal como entregado.
				sSql = "UPDATE SIM_PRESTAMO SET \n" +
						"B_ENTREGADO = 'V' \n"+
						"WHERE  CVE_GPO_EMPRESA ='" + (String) registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						" AND CVE_EMPRESA ='" + (String) registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						" AND ID_PRESTAMO ='" + (String) registro.getDefCampo("ID_PRESTAMO") + "' \n";
				
				PreparedStatement ps4 = this.conn.prepareStatement(sSql);
				ps4.execute();
				ResultSet rs4 = ps4.getResultSet();
				
				//Obtiene el identificador de la etapa que representa el desembolso.
				sSql =  "SELECT \n"+
						"ID_ETAPA_PRESTAMO \n"+
						"FROM SIM_CAT_ETAPA_PRESTAMO \n"+
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND B_DESEMBOLSO = 'V' \n";
				ejecutaSql();
				if (rs.next()){
					registro.addDefCampo("ID_ETAPA_PRESTAMO",rs.getString("ID_ETAPA_PRESTAMO"));
				}
			
				//Avanza la etapa.
				//Obtiene las actividades de la etapa que se completa.
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
				
						PreparedStatement ps9 = this.conn.prepareStatement(sSql);
						ps9.execute();
						ResultSet rs9 = ps9.getResultSet();
						
				while (rs9.next()){
					
					registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs9.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs9.getString("ID_ACTIVIDAD_REQUISITO"));
					registro.addDefCampo("ORDEN_ETAPA",rs9.getString("ORDEN_ETAPA")== null ? "": rs9.getString("ORDEN_ETAPA"));
					registro.addDefCampo("FECHA_REGISTRO",rs9.getString("FECHA_REGISTRO")== null ? "": rs9.getString("FECHA_REGISTRO"));
					//Guarda el historial de la etapa y las actividades
					sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
					PreparedStatement ps10 = this.conn.prepareStatement(sSql);
					ps10.execute();
					ResultSet rs10 = ps10.getResultSet();
					if (rs10.next()){
						registro.addDefCampo("ID_HISTORICO",rs10.getString("ID_HISTORICO"));
						
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
							"SYSDATE, \n"+
							"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
							"'Completada', \n"+
							"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
							"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "') \n" ;
						
						PreparedStatement ps11 = this.conn.prepareStatement(sSql);
						ps11.execute();
						ResultSet rs11 = ps11.getResultSet();
					
					}
				}
				
				sSql =  " SELECT DISTINCT ID_ETAPA_PRESTAMO, \n"+
						" ORDEN_ETAPA \n"+
						"	  FROM SIM_PRESTAMO_ETAPA \n"+
						"	  WHERE ORDEN_ETAPA = (SELECT DISTINCT \n"+
						"	  ORDEN_ETAPA + 1 \n"+
						" 	  FROM SIM_PRESTAMO_ETAPA \n"+
						"	  WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"	  AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"	  AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
						"	  AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "') \n"+
						"	  AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
				
				
				PreparedStatement ps14 = this.conn.prepareStatement(sSql);
				ps14.execute();
				ResultSet rs14 = ps14.getResultSet();
				if(rs14.next()){
					registro.addDefCampo("ETAPA_PRESTAMO",rs14.getString("ID_ETAPA_PRESTAMO"));
					sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET \n"+
							"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ETAPA_PRESTAMO") + "', \n"+
							" COMENTARIO 		='' \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
							"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" ;
				
					PreparedStatement ps15 = this.conn.prepareStatement(sSql);
					ps15.execute();
					ResultSet rs15 = ps15.getResultSet();
				
					sSql =  " UPDATE SIM_PRESTAMO SET \n"+
							"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ETAPA_PRESTAMO") + "' \n"+
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" ;
					
					PreparedStatement ps16 = this.conn.prepareStatement(sSql);
					ps16.execute();
					ResultSet rs16 = ps16.getResultSet();
	
					sSql =  " UPDATE SIM_PRESTAMO_ETAPA SET "+
							" FECHA_REGISTRO 		=SYSDATE, \n" +
							" ESTATUS 	 		='Registrada', \n" +
							" COMENTARIO 	 		='' \n" +
							" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
							" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							" AND ID_ETAPA_PRESTAMO	='" + (String)registro.getDefCampo("ETAPA_PRESTAMO") + "' \n";
			
					PreparedStatement ps17 = this.conn.prepareStatement(sSql);
					ps17.execute();
					ResultSet rs17 = ps17.getResultSet();
	
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
							"AND ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ETAPA_PRESTAMO") + "' \n";
				
					PreparedStatement ps18 = this.conn.prepareStatement(sSql);
					ps18.execute();
					ResultSet rs18 = ps18.getResultSet();
				
					while (rs18.next()){
						registro.addDefCampo("ID_ACTIVIDAD_REQUISITO",rs18.getString("ID_ACTIVIDAD_REQUISITO")== null ? "": rs18.getString("ID_ACTIVIDAD_REQUISITO"));
						registro.addDefCampo("ETAPA_PRESTAMO",rs18.getString("ID_ETAPA_PRESTAMO")== null ? "": rs18.getString("ID_ETAPA_PRESTAMO"));
						registro.addDefCampo("ORDEN_ETAPA",rs18.getString("ORDEN_ETAPA")== null ? "": rs18.getString("ORDEN_ETAPA"));
						registro.addDefCampo("FECHA_REGISTRO",rs18.getString("FECHA_REGISTRO")== null ? "": rs18.getString("FECHA_REGISTRO"));
						registro.addDefCampo("COMENTARIO",rs18.getString("COMENTARIO")== null ? "": rs18.getString("COMENTARIO"));
						registro.addDefCampo("ESTATUS",rs18.getString("ESTATUS")== null ? "": rs18.getString("ESTATUS"));
							
						sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
							
						PreparedStatement ps19 = this.conn.prepareStatement(sSql);
						ps19.execute();
						ResultSet rs19 = ps19.getResultSet();	
						if (rs19.next()){
							registro.addDefCampo("ID_HISTORICO",rs19.getString("ID_HISTORICO"));
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
									"'" + (String)registro.getDefCampo("ETAPA_PRESTAMO") + "', \n" +
									"TO_TIMESTAMP('" + (String)registro.getDefCampo("FECHA_REGISTRO") + "','yyyy-MM-dd HH24:MI:SSXFF'), \n" +
									"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "', \n" +
									"'" + (String)registro.getDefCampo("COMENTARIO") + "', \n" +
									"'" + (String)registro.getDefCampo("ESTATUS") + "') \n" ;
						
							PreparedStatement ps20 = this.conn.prepareStatement(sSql);
							ps20.execute();
							ResultSet rs20 = ps20.getResultSet();
						}
					}
	
				}
			
				///
				
				
			}
			
			sSql =  " SELECT DISTINCT D.ID_ETAPA_PRESTAMO \n" +
			"		  FROM SIM_PRESTAMO_GPO_DET D, \n" +
			"		  SIM_PRESTAMO_ETAPA E \n" +
			"		  WHERE D.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			"		  AND D.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
			"		  AND D.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +    
			"		  AND E.CVE_GPO_EMPRESA = D.CVE_GPO_EMPRESA \n" +
			"		 AND E.CVE_EMPRESA = D.CVE_EMPRESA \n" + 
			"		 AND E.ID_PRESTAMO = D.ID_PRESTAMO \n" +
			"		 AND E.ID_ETAPA_PRESTAMO = D.ID_ETAPA_PRESTAMO \n" +
			"		 AND E.ORDEN_ETAPA = ( \n" +
			"		 SELECT MIN(ORDEN_ETAPA) FROM \n" +
			"		 (SELECT D.ID_ETAPA_PRESTAMO, E.ORDEN_ETAPA \n" +
			"		 FROM SIM_PRESTAMO_GPO_DET D, \n" + 
			"		 SIM_PRESTAMO_ETAPA E \n" +
			"		 WHERE D.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			"		 AND D.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
			"		 AND D.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +    
			"		 AND E.CVE_GPO_EMPRESA = D.CVE_GPO_EMPRESA \n" +
			"		 AND E.CVE_EMPRESA = D.CVE_EMPRESA \n" + 
			"		 AND E.ID_PRESTAMO = D.ID_PRESTAMO \n" +
			"		 AND E.ID_ETAPA_PRESTAMO = D.ID_ETAPA_PRESTAMO)) \n";
			
		PreparedStatement ps22 = this.conn.prepareStatement(sSql);
		ps22.execute();
		ResultSet rs22 = ps22.getResultSet();
	
		if(rs22.next()){
			
			//Pasa a la siguiente etapa.
			registro.addDefCampo("ETAPA_PRESTAMO",rs22.getString("ID_ETAPA_PRESTAMO"));
	
			sSql =  " UPDATE SIM_PRESTAMO_GRUPO SET \n"+
				"ID_ETAPA_PRESTAMO = '" + (String)registro.getDefCampo("ETAPA_PRESTAMO") + "', \n"+
				"B_ENTREGADO = 'V' \n"+
				"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" ;
			
			PreparedStatement ps23 = this.conn.prepareStatement(sSql);
			ps23.execute();
			ResultSet rs23 = ps23.getResultSet();
		}
		resultadoCatalogo.Resultado.addDefCampo("ID_TRANSACCION_GRUPO", sIdTransaccionGrupo);
		
	}
		return resultadoCatalogo;
	}
}