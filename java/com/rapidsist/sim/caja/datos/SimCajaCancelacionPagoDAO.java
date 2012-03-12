/**
 * Sistema de administraciï¿½n de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para mostrar movimientos de Caja.
 */
 
public class SimCajaCancelacionPagoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta {
	
	/**
	 * Obtiene un conjunto de registros en base a el filtro de bï¿½squeda.
	 * @param parametros Parï¿½metros que se le envï¿½an a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
		sSql =  "SELECT \n"+
				"T.ID_PRESTAMO, \n"+
				"T.ID_TRANSACCION, \n"+
				"'INDIVIDUAL' APLICA_A, \n"+
				"V.CVE_PRESTAMO, \n"+
				"V.CVE_NOMBRE, \n"+
				"T.NUM_CICLO, \n"+
				"T.FECHA_TRANSACCION, \n"+
				"V.NOM_PRODUCTO, \n"+ 
				"V.NOMBRE, \n"+
				"NVL(ROUND(T.MONTO,2),0) MONTO, \n"+ 
				"TO_CHAR(NVL(ROUND(T.MONTO,2),0),'999,999,999.99') IMPORTE \n"+ 
				"FROM SIM_CAJA_TRANSACCION T, \n"+
				"V_CREDITO V \n"+
				"WHERE T.CVE_GPO_EMPRESA = '" + (String) parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND T.CVE_EMPRESA = '" + (String) parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND T.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
				"AND T.ID_CAJA = '" + (String)parametros.getDefCampo("ID_CAJA") + "' \n"+
				"AND T.CVE_MOVIMIENTO_CAJA = 'PAGOIND' \n"+
				"AND T.FECHA_CANCELACION IS NULL \n"+
				"AND V.CVE_GPO_EMPRESA =  T.CVE_GPO_EMPRESA \n"+
				"AND V.CVE_EMPRESA =  T.CVE_EMPRESA \n"+
				"AND V.ID_PRESTAMO =  T.ID_PRESTAMO \n";
		
		if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
			sSql = sSql + "AND V.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_PRODUCTO") != null) {
			sSql = sSql + "AND V.NOM_PRODUCTO = '" + (String) parametros.getDefCampo("NOM_PRODUCTO") + "' \n";
		}
		if (parametros.getDefCampo("NUM_CICLO") != null) {
			sSql = sSql + "AND T.NUM_CICLO = '" + (String) parametros.getDefCampo("NUM_CICLO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_CLIENTE") != null) {
			sSql = sSql + "AND V.NOMBRE LIKE '%" + (String) parametros.getDefCampo("NOM_CLIENTE") + "%' \n";
		}
		
		sSql = sSql + "UNION ALL \n"+
					"SELECT \n"+
					"T.ID_PRESTAMO_GRUPO, \n"+
					"T.ID_TRANSACCION, \n"+
					"'GRUPO' APLICA_A, \n"+
					"V.CVE_PRESTAMO, \n"+
					"V.CVE_NOMBRE, \n"+
					"T.NUM_CICLO, \n"+
					"T.FECHA_TRANSACCION, \n"+
					"V.NOM_PRODUCTO, \n"+
					"V.NOMBRE, \n"+
					"NVL(ROUND(T.MONTO,2),0) MONTO, \n"+ 
					"TO_CHAR(NVL(ROUND(T.MONTO,2),0),'999,999,999.99') IMPORTE \n"+ 
					"FROM SIM_CAJA_TRANSACCION T, \n"+
					"V_CREDITO V \n"+
					"WHERE T.CVE_GPO_EMPRESA = '" + (String) parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND T.CVE_EMPRESA = '" + (String) parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND T.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
					"AND T.ID_CAJA = '" + (String)parametros.getDefCampo("ID_CAJA") + "' \n"+
					"AND T.CVE_MOVIMIENTO_CAJA = 'PAGOGPO' \n"+
					"AND T.FECHA_CANCELACION IS NULL \n"+
					"AND V.CVE_GPO_EMPRESA =  T.CVE_GPO_EMPRESA \n"+
					"AND V.CVE_EMPRESA =  T.CVE_EMPRESA \n"+
					"AND V.ID_PRESTAMO =  T.ID_PRESTAMO_GRUPO \n";
		
		if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
			sSql = sSql + "AND V.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_PRODUCTO") != null) {
			sSql = sSql + "AND V.NOM_PRODUCTO = '" + (String) parametros.getDefCampo("NOM_PRODUCTO") + "' \n";
		}
		if (parametros.getDefCampo("NUM_CICLO") != null) {
			sSql = sSql + "AND T.NUM_CICLO = '" + (String) parametros.getDefCampo("NUM_CICLO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_CLIENTE") != null) {
			sSql = sSql + "AND V.NOMBRE LIKE '%" + (String) parametros.getDefCampo("NOM_CLIENTE") + "%' \n";
		}
		
		System.out.println("auxilio"+sSql);
		
		ejecutaSql();
		return getConsultaLista();
	}
	

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecuciï¿½n de este mï¿½todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		String sIdMovimiento = "";
		String sTxrespuesta = "";
		//Esta variable mapea los movimientos de caja y las operaciones hechas por pl.
		String sIdTransaccion = "";
		
		//Obtenemos el sequence ID_TRANSACCION que relacionará el movimiento de la caja con los movimientos del credito 
		//en las tablas PFIN_MOVIMIENTO y PFIN_PRE_MOVIMIENTO.
		sSql = "SELECT SQ01_SIM_CAJA_TRANSACCION.nextval as ID_TRANSACCION FROM DUAL";
		ejecutaSql();
		if (rs.next()){
			sIdTransaccion = rs.getString("ID_TRANSACCION");
		}
		
		if (registro.getDefCampo("APLICA_A").equals("INDIVIDUAL")){
			//Cancela un prï¿½stamo indiviudal.
			
			sSql = "SELECT \n"+
					"M.ID_PRESTAMO, \n"+
					"M.ID_MOVIMIENTO, \n"+
					"M.ID_REFERENCIA \n"+
					"FROM \n"+
					"PFIN_MOVIMIENTO M \n"+
					"WHERE M.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND M.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND M.CVE_OPERACION = 'CRPAGOPRES' \n"+
					"AND M.ID_REFERENCIA = '" + (String)registro.getDefCampo("ID_TRANSACCION") + "' \n";
			ejecutaSql();
			if (rs.next()){
				sIdMovimiento = rs.getString("ID_MOVIMIENTO");
			}
			
			CallableStatement sto1 = conn.prepareCall("begin dbms_output.put_line(PKG_CREDITO.fCancelaPago(?,?,?,?,?,?)); end;");
			sto1.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
			sto1.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
			sto1.setString(3, sIdMovimiento);
			sto1.setString(4, (String)registro.getDefCampo("CVE_USUARIO"));
			sto1.setString(5, sIdTransaccion);
			sto1.registerOutParameter(6, java.sql.Types.VARCHAR);
			
			//EJECUTA EL PROCEDIMIENTO ALMACENADO
			sto1.execute();
			sTxrespuesta = sto1.getString(6);
			sto1.close();
			
			if (sTxrespuesta.equals("Movimiento aplicado con exito")){
				
				String sIdMovimientoOperacion = "";
				int iIdMovimientoOperacion = 0;
				
				//OBTENEMOS EL SEQUENCE
				sSql =  "SELECT \n" +
						"CVE_GPO_EMPRESA, \n" +
						"MAX(ID_MOVIMIENTO_OPERACION) ID_MOVIMIENTO_OPERACION \n" +
						"FROM \n" +
						"SIM_CAJA_TRANSACCION \n" +
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND CVE_MOVIMIENTO_CAJA = '" + (String)registro.getDefCampo("CVE_MOVIMIENTO_CAJA") + "' \n"+
						"GROUP BY CVE_GPO_EMPRESA \n";
				ejecutaSql();
					
				if (rs.next()){
					
					sIdMovimientoOperacion = rs.getString("ID_MOVIMIENTO_OPERACION");
					iIdMovimientoOperacion=Integer.parseInt(sIdMovimientoOperacion.trim());
					iIdMovimientoOperacion ++;
					sIdMovimientoOperacion= String.valueOf(iIdMovimientoOperacion);
				}else {
				
					sIdMovimientoOperacion = "1";
				}
				
				sSql =  "INSERT INTO SIM_CAJA_TRANSACCION ( \n"+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_MOVIMIENTO_OPERACION, \n" +
						"ID_TRANSACCION, \n" +
						"ID_SUCURSAL, \n" +
						"ID_CAJA, \n" +
						"CVE_MOVIMIENTO_CAJA, \n" +
						"ID_PRESTAMO, \n"+
						"ID_CLIENTE, \n"+
						"NUM_CICLO, \n"+
						"MONTO, \n"+
						"CVE_USUARIO_CAJERO, \n" +
						"FECHA_TRANSACCION) \n" +
				        "VALUES ( \n"+
						"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
						sIdMovimientoOperacion + ", \n "+
						sIdTransaccion + ", \n "+
						"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
						"'CANPAGO', \n" +
						"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_NOMBRE") + "', \n" +
						"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
						"-'" + (String)registro.getDefCampo("MONTO") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
						"SYSDATE) \n";
				
				//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
				
				sSql = " UPDATE SIM_CAJA_TRANSACCION SET "+
					   " FECHA_CANCELACION    	 = SYSDATE \n" +
					   " WHERE ID_TRANSACCION    	 = '" + (String)registro.getDefCampo("ID_TRANSACCION") + "' \n" +
					   " AND CVE_EMPRESA   	 = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					   " AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
			
		}else if (registro.getDefCampo("APLICA_A").equals("GRUPO")){
			//Cancela un prï¿½stamo grupal.
			String fAplicacion = "";

			sSql = "SELECT TO_CHAR(TO_DATE('"+(String)registro.getDefCampo("F_APLICACION")+"','DD-MM-YYYY'),'DD-MON-YY') AS F_APLICA FROM DUAL \n";
	        
			ejecutaSql();
			if (rs.next()){
				fAplicacion = rs.getString("F_APLICA");
			}

			sSql = "SELECT \n"+
					"M.ID_PRESTAMO, \n"+
					"M.ID_MOVIMIENTO, \n"+
					"M.ID_REFERENCIA \n"+
					"FROM \n"+
					"PFIN_MOVIMIENTO M \n"+
					"WHERE M.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND M.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND M.CVE_OPERACION = 'CRPAGOPRES' \n"+
					"AND M.ID_REFERENCIA = '" + (String)registro.getDefCampo("ID_TRANSACCION") + "' \n";
			ejecutaSql();
			if (rs.next()){
				sIdMovimiento = rs.getString("ID_MOVIMIENTO");
			}
					
				CallableStatement sto1 = conn.prepareCall("begin dbms_output.put_line(PKG_CREDITO.fCancelaPago(?,?,?,?,?,?)); end;");
				sto1.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
				sto1.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
				sto1.setString(3, sIdMovimiento);
				sto1.setString(4, (String)registro.getDefCampo("CVE_USUARIO"));
				sto1.setString(5, sIdTransaccion);
				sto1.registerOutParameter(6, java.sql.Types.VARCHAR);
					
				//EJECUTA EL PROCEDIMIENTO ALMACENADO
				sto1.execute();
				sTxrespuesta = sto1.getString(6);
				sto1.close();
				
				if (sTxrespuesta.equals("Movimiento aplicado con exito")){
					
					String sIdMovimientoOperacion = "";
					int iIdMovimientoOperacion = 0;
					String sSaldoTotal = "";
					float fSaldoTotal = 0;
					String sDeudaMinima = "";
					float fDeudaMinima = 0;
					String sIdEtapaPrestamo = "";
					
					//OBTENEMOS EL SEQUENCE
					sSql =  "SELECT \n" +
							"CVE_GPO_EMPRESA, \n" +
							"MAX(ID_MOVIMIENTO_OPERACION) ID_MOVIMIENTO_OPERACION \n" +
							"FROM \n" +
							"SIM_CAJA_TRANSACCION \n" +
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							"AND CVE_MOVIMIENTO_CAJA = '" + (String)registro.getDefCampo("CVE_MOVIMIENTO_CAJA") + "' \n"+
							"GROUP BY CVE_GPO_EMPRESA \n";
					ejecutaSql();
						
					if (rs.next()){
						
						sIdMovimientoOperacion = rs.getString("ID_MOVIMIENTO_OPERACION");
						iIdMovimientoOperacion=Integer.parseInt(sIdMovimientoOperacion.trim());
						iIdMovimientoOperacion ++;
						sIdMovimientoOperacion= String.valueOf(iIdMovimientoOperacion);
					}else {
					
						sIdMovimientoOperacion = "1";
					}
					
					sSql =  "INSERT INTO SIM_CAJA_TRANSACCION ( \n"+
							"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_MOVIMIENTO_OPERACION, \n" +
							"ID_TRANSACCION, \n" +
							"ID_SUCURSAL, \n" +
							"ID_CAJA, \n" +
							"CVE_MOVIMIENTO_CAJA, \n" +
							"ID_PRESTAMO, \n"+
							"ID_GRUPO, \n"+
							"NUM_CICLO, \n"+
							"MONTO, \n"+
							"CVE_USUARIO_CAJERO, \n" +
							"FECHA_TRANSACCION) \n" +
					        "VALUES ( \n"+
							"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
							sIdMovimientoOperacion + ", \n "+
							sIdTransaccion + ", \n "+
							"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
							"'CANPAGO', \n" +
							"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_NOMBRE") + "', \n" +
							"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
							"-'" + (String)registro.getDefCampo("MONTO") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
							"SYSDATE) \n" ;
					
					//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
					if (ejecutaUpdate() == 0){
						resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
					}
					
					sSql = " UPDATE SIM_CAJA_TRANSACCION SET "+
						   " FECHA_CANCELACION    	 = SYSDATE \n" +
						   " WHERE ID_TRANSACCION    	 = '" + (String)registro.getDefCampo("ID_TRANSACCION") + "' \n" +
						   " AND CVE_EMPRESA   	 = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						   " AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
					//VERIFICA SI DIO DE ALTA EL REGISTRO
					if (ejecutaUpdate() == 0){
						resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
					}
					
					
					//Cambiara el estatus a entregado si es que la cancelación de pago hizo que le crédito dejará de ser liquidado.
					sSql =	"SELECT \n"+	
							"    sum(nvl(imp_neto,0)) SALDO \n"+
							"  FROM  \n"+
							"  SIM_PRESTAMO_GPO_DET G, \n"+
							"  V_SIM_TABLA_AMORT_CONCEPTO A, \n"+
							"  PFIN_CAT_CONCEPTO B \n"+
							  "WHERE G.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
							 "AND G.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
							 "AND G.ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
							 "AND A.CVE_GPO_EMPRESA   = G.CVE_GPO_EMPRESA \n"+
							 "AND A.CVE_EMPRESA       = G.CVE_EMPRESA \n"+
							 "AND A.ID_PRESTAMO       = G.ID_PRESTAMO \n"+
							 "AND B.CVE_GPO_EMPRESA   = A.CVE_GPO_EMPRESA \n"+
							 "AND B.CVE_EMPRESA       = A.CVE_EMPRESA \n"+
							 "AND B.CVE_CONCEPTO      = A.CVE_CONCEPTO \n"+
							 "AND (NVL(A.IMP_ORIGINAL,0) + A.IMP_EXTRAORDINARIO) <> 0 \n";
					
					ejecutaSql();
					if (rs.next()){
						sSaldoTotal = rs.getString("SALDO");
						fSaldoTotal = Float.parseFloat(sSaldoTotal.trim());
						fSaldoTotal = fSaldoTotal < 0 ? -fSaldoTotal : fSaldoTotal;
					}
					
					sSql = " SELECT \n" + 
						  " IMP_DEUDA_MINIMA \n" +
						  "	FROM SIM_PARAMETRO_GLOBAL  \n" +
						  "	WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						  "	AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" ;
					ejecutaSql();
					if (rs.next()){
						sDeudaMinima = rs.getString("IMP_DEUDA_MINIMA");
						fDeudaMinima = (Float.parseFloat(sDeudaMinima));
					}
				
					if (fSaldoTotal >= fDeudaMinima){
						
						
						sSql =  "   SELECT ID_ETAPA_PRESTAMO \n" + 
								"	FROM SIM_PRESTAMO_GRUPO  \n" +
								" WHERE ID_PRESTAMO_GRUPO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
								" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
						ejecutaSql();
						if (rs.next()){
							sIdEtapaPrestamo = rs.getString("ID_ETAPA_PRESTAMO");
						}
						
						if (sIdEtapaPrestamo.equals("8")){
							//La deuda deja de estar liquidada y actualiza el estatus.
								sSql =  "   SELECT ID_PRESTAMO \n" + 
								"	FROM SIM_PRESTAMO_GPO_DET  \n" +
								" WHERE ID_PRESTAMO_GRUPO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
								" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
								" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
							PreparedStatement ps1 = this.conn.prepareStatement(sSql);
							ps1.execute();
							ResultSet rs1 = ps1.getResultSet();
							System.out.println(sSql);
							while (rs1.next()){
								registro.addDefCampo("ID_PRESTAMO_INDIVIDUAL",rs1.getString("ID_PRESTAMO"));
								
								sSql =  " UPDATE SIM_PRESTAMO SET "+
										" ID_ETAPA_PRESTAMO 	= '7' \n" +
										" WHERE ID_PRESTAMO 	='" + (String)registro.getDefCampo("ID_PRESTAMO_INDIVIDUAL") + "' \n"+
										" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
										" AND CVE_GPO_EMPRESA	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
								System.out.println(sSql);
								//VERIFICA SI DIO DE ALTA EL REGISTRO
								PreparedStatement ps2 = this.conn.prepareStatement(sSql);
								ps2.execute();
								ResultSet rs2 = ps2.getResultSet();
							}	
							
							sSql =  " UPDATE SIM_PRESTAMO_GPO_DET SET "+
									" ID_ETAPA_PRESTAMO 		='7' \n" +
									" WHERE ID_PRESTAMO_GRUPO 	='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
									" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
								
							//VERIFICA SI DIO DE ALTA EL REGISTRO
							if (ejecutaUpdate() == 0){
								resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
							}
							
							sSql =  " UPDATE SIM_PRESTAMO_GRUPO SET "+
									" ID_ETAPA_PRESTAMO 		='7' \n" +
									" WHERE ID_PRESTAMO_GRUPO 	='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n"+
									" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
									" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
								
							//VERIFICA SI DIO DE ALTA EL REGISTRO
							if (ejecutaUpdate() == 0){
								resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
							}
							
						}
					}
					
				}
			
		}
		resultadoCatalogo.Resultado.addDefCampo("RESPUESTA", sTxrespuesta);
		
		return resultadoCatalogo;
	}
}