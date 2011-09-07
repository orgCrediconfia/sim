/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Administra los accesos a la base de datos para realizar el pago de amortizaciòn.
 */
 
public class SimCajaPagoIndividualDAO extends Conexion2 implements OperacionAlta {

	private long parse;

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		String sIdMovimientoOperacion = "";
		int iIdMovimientoOperacion = 0;
		String sTxrespuesta1 = "";
		String sTxrespuesta2 = "";
		String sTxrespuesta3 = "";
		String sFechaAplicacion = "";
		String sIdPreMovimiento = "";
		String sFLiquidacion = "";
		String sFechaMovmiento = "";
		String sIdCuentaVista = "";
		String vgFolioGrupo = "";
		
		boolean bBuscaFechaRangoInf = true;
		String sDiasAplicaPago = "";
		int iDiasAplicaPago = 0;
		int iDiasRestados = 0;
		String sFMedio = "";
		String sFRangoInf = "";
		String sDia = "";
		boolean bFechaValida = false;
		
		//Esta variable mapea los movimientos de caja y las operaciones hechas por pl.
		String sIdTransaccion = "";
		
		//Obtiene el parámetro de DIAS_APLICA_PAGO y lo almacena en un entero para poder operarlo.
		sSql = " SELECT \n"+
				"CVE_GPO_EMPRESA, \n"+
				"CVE_EMPRESA, \n"+
				"DIAS_APLICA_PAGO \n"+
				"FROM SIM_PARAMETRO_GLOBAL \n"+
				"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
		ejecutaSql();
		if (rs.next()){
			sDiasAplicaPago = rs.getString("DIAS_APLICA_PAGO");
			iDiasAplicaPago = Integer.parseInt(sDiasAplicaPago);
			System.out.println("iDiasAplicaPago :"+iDiasAplicaPago);
		}
		
		//Obtiene la fecha del medio.
		
		sSql =  "SELECT TO_CHAR(TO_DATE(F_MEDIO,'DD-MM-YY'),'YYYY-MM-dd') AS F_MEDIO \n"+
				"FROM PFIN_PARAMETRO \n"+
				"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND CVE_MEDIO = 'SYSTEM' \n";
		ejecutaSql();
	
		if (rs.next()){
			sFMedio = rs.getString("F_MEDIO");
		}
		
		sFRangoInf = sFMedio;
		System.out.println("fecha del medio :"+sFMedio);
		while (bBuscaFechaRangoInf){
			bBuscaFechaRangoInf = false;
			sSql = " SELECT TO_CHAR(TO_TIMESTAMP( '" + sFRangoInf + "','yyyy-MM-dd HH24:MI:SSXFF') - 1,'YYYY-MM-dd') AS FECHA_RANGO_INF FROM DUAL \n";
			
			ejecutaSql();
			if (rs.next()){
				sFRangoInf = rs.getString("FECHA_RANGO_INF");
			}
			System.out.println(sFRangoInf);
			//Valida que la fecha no sea un día feriado.
			sSql = " SELECT \n" +
			   " F_DIA_FESTIVO \n" +
			   " FROM PFIN_DIA_FESTIVO \n" +
			   " WHERE F_DIA_FESTIVO      	= TO_TIMESTAMP('" + sFRangoInf + "','yyyy-MM-dd') \n" +
			   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND CVE_PAIS 			= 'MX' \n";	
			
			ejecutaSql();
			
			if (rs.next()){
				System.out.println("Es un día feriado por lo tanto debera de restar otra fecha");
				//Es un día feriado por lo tanto debera de restar otra fecha.
				bBuscaFechaRangoInf = true;
			}else {
				System.out.println("Verfica si la fecha es sábado o domingo");
				//Verfica si la fecha es sábado o domingo.
				sSql = " SELECT TO_CHAR(TO_TIMESTAMP('" + sFRangoInf + "','yyyy-MM-dd'),'D') DIA FROM DUAL \n";
				
				ejecutaSql();
				if (rs.next()){
					sDia = rs.getString("DIA");
				}
				if (sDia.equals("6") || sDia.equals("7")){
					System.out.println("La fecha es sábado o domingo por lo que seguira buscando una fecha");
					//La fecha es sábado o domingo por lo que seguira buscando una fecha
					bBuscaFechaRangoInf = true;
				}else {
					System.out.println("El día no es un día feriado ni tampoco un sábado o domingo por lo que se restara.");
					//El día no es un día feriado ni tampoco un sábado o domingo por lo que se restara.
					iDiasRestados++;
					System.out.println("iDiasRestados :"+iDiasRestados);
					//Si las dias que se han restados son igual a los días del campo DIAS_APLICA_PAGO.
					if (iDiasRestados == iDiasAplicaPago){
						System.out.println("Si las dias que se han restados son igual a los días del campo DIAS_APLICA_PAGO.");
						bBuscaFechaRangoInf = false;
					}else{
						bBuscaFechaRangoInf = true;
					}
				}
			}
		}
		
		sSql = " SELECT TO_CHAR(TO_DATE('" + (String)registro.getDefCampo("FECHA_MOVIMIENTO") + "','DD-MM-YYYY'),'yyyy-MM-dd') F_APLICACION FROM DUAL";
		ejecutaSql();
		
		if (rs.next()){
			sFechaAplicacion = rs.getString("F_APLICACION");
		}
		
		
		System.out.println("sFMedio: "+sFMedio);
		System.out.println("sFRangoInf: "+sFRangoInf);
		System.out.println("sFechaAplicacion: "+sFechaAplicacion);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date dFmedio = null;
		Date dFRangoInf = null;
		Date dFechaAplicacion = null;
		
		try {
			dFmedio = dateFormat.parse(sFMedio);
			dFRangoInf = dateFormat.parse(sFRangoInf);
			dFechaAplicacion = dateFormat.parse(sFechaAplicacion);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int iRagInf = dFechaAplicacion.compareTo(dFRangoInf);
		int iRagSup = dFechaAplicacion.compareTo(dFmedio);
		
		if (iRagInf == 0){
			//La fecha de aplicacion es la misma que el rango mínimo permitido.
			bFechaValida = true;
			System.out.println("La fecha es la misma");
		}else if (iRagInf < 0){
			//La fecha de aplicación es menor al rango inferior permitido.
			bFechaValida = false;
			System.out.println("La fecha de aplicación es menor al rango inferior permitido");
		}else if (iRagInf > 0){
			//La fecha de aplicación es mayor al rango inferior permitido.
			if (iRagSup == 0){
				//La fecha de aplicación es la misma al rango superior permitido.
				bFechaValida = true;
			}else if (iRagSup < 0){
				//La fecha de aplicación es menor al rango superior permitido.
				bFechaValida = true;
			}else if (iRagSup > 0){
				//La fecha de aplicación es mayor al rango superior permitido.
				bFechaValida = false;
			}
		}
		
		if (bFechaValida){
			//********************************La fecha para aplicar el pago es valida, por lo que se empieza el pago************************************.
			//Obtenemos el sequence ID_PRE_MOVIMIENTO para la tabla PFIN_PRE_MOVIMIENTO.
			sSql = "SELECT SQ01_PFIN_PRE_MOVIMIENTO.nextval as ID_PREMOVIMIENTO FROM DUAL";
			ejecutaSql();
			if (rs.next()){
				sIdPreMovimiento = rs.getString("ID_PREMOVIMIENTO");
			}
			
			//Obtenemos el sequence ID_TRANSACCION que relacionará el movimiento de la caja con los movimientos del credito 
			//en las tablas PFIN_MOVIMIENTO y PFIN_PRE_MOVIMIENTO.
			sSql = "SELECT SQ01_SIM_CAJA_TRANSACCION.nextval as ID_TRANSACCION FROM DUAL";
			ejecutaSql();
			if (rs.next()){
				sIdTransaccion = rs.getString("ID_TRANSACCION");
			}
			
			//Obtiene la fecha de liquidación que no es más que la fecha del medio en formato 'DD-MON-YYYY'.
			sSql = "SELECT TO_CHAR(F_MEDIO,'DD-MON-YYYY') AS F_LIQUIDACION \n"+
					"FROM PFIN_PARAMETRO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND CVE_MEDIO = 'SYSTEM' \n";
			ejecutaSql();
			if (rs.next()){
				sFLiquidacion = rs.getString("F_LIQUIDACION");
			}
			
			//Obtiene la fecha de aplicación del pago que es la fecha del movimiento capturada por el usuario en formato 'DD-MON-YYYY'.
			sSql = " SELECT TO_CHAR(TO_DATE('" + (String)registro.getDefCampo("FECHA_MOVIMIENTO") + "','DD-MM-YYYY'),'DD-MON-YYYY') F_APLICACION FROM DUAL";
			ejecutaSql();
			if (rs.next()){
				sFechaAplicacion = rs.getString("F_APLICACION");
			}
			
			//Obtenemos la cuenta referencia.
			sSql = "SELECT ID_CUENTA \n"+
					"FROM PFIN_CUENTA \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND CVE_TIP_CUENTA = 'VISTA' \n"+
					"AND SIT_CUENTA = 'AC' \n"+
					"AND ID_TITULAR = (SELECT ID_CLIENTE FROM SIM_PRESTAMO WHERE ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "') \n";
			ejecutaSql();
			if (rs.next()){
				sIdCuentaVista = rs.getString("ID_CUENTA");
			}
			
			//Se preparan los parametros que recibirá el poceso PKG_PROCESOS.pGeneraPreMovto.
			String sCveGpoEmpresa = (String)registro.getDefCampo("CVE_GPO_EMPRESA");
			String sCveEmpresa = (String)registro.getDefCampo("CVE_EMPRESA");
			String sIdPreMovi = sIdPreMovimiento;
			String sFMovimiento = sFLiquidacion;
			String sIdCuenta = sIdCuentaVista;
			String sIdPrestamo = (String)registro.getDefCampo("ID_PRESTAMO");
			String sCveDivisa = "MXP";
			String sCveOperacion = "TEDEPEFE";
			String sImpNeto = (String)registro.getDefCampo("IMP_NETO");
			String sCveMedio = "PRESTAMO";
			String sCveMercado = "PRESTAMO";
			String sNota = "Depósito de efectivo";
			String sIdGrupo = "";
			String sCveUsuario = (String)registro.getDefCampo("CVE_USUARIO");
			String sFValor = sFechaAplicacion;
			String sNumPagoAmort = "0";
			
			System.out.println("sCveGpoEmpresa"+sCveGpoEmpresa);
			System.out.println("sCveEmpresa"+sCveEmpresa);
			System.out.println("sIdPreMovimiento"+sIdPreMovi);
			System.out.println("sFMovimiento"+sFMovimiento);
			System.out.println("sIdCuenta"+sIdCuenta);
			System.out.println("sIdPrestamo"+sIdPrestamo);
			System.out.println("sCveDivisa"+sCveDivisa);
			System.out.println("sCveOperacion"+sCveOperacion);
			System.out.println("sImpNeto"+sImpNeto);
			System.out.println("sCveMedio"+sCveMedio);
			System.out.println("sCveMercado"+sCveMercado);
			System.out.println("sNota"+sNota);
			System.out.println("sIdGrupo"+sIdGrupo);
			System.out.println("sFValor"+sFechaAplicacion);
			System.out.println("sNumPagoAmort"+sNumPagoAmort);
			
			//Llama al proceso PKG_PROCESOS.pGeneraPreMovto.
			CallableStatement sto = conn.prepareCall("begin PKG_PROCESOS.pGeneraPreMovto(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;");
			sto.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
			sto.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
			sto.setString(3, sIdPreMovi);
			sto.setString(4, sFMovimiento);
			sto.setString(5, sIdCuenta);
			sto.setString(6, (String)registro.getDefCampo("ID_PRESTAMO"));
			sto.setString(7, sCveDivisa);
			sto.setString(8, sCveOperacion);
			sto.setString(9, (String)registro.getDefCampo("IMP_NETO"));
			sto.setString(10, sCveMedio);
			sto.setString(11, sCveMercado);
			sto.setString(12, sNota);
			sto.setString(13, sIdGrupo);
			sto.setString(14, (String)registro.getDefCampo("CVE_USUARIO"));
			sto.setString(15, sFechaAplicacion);
			sto.setString(16, sNumPagoAmort);
			sto.setString(17, sIdTransaccion);
			sto.registerOutParameter(18, java.sql.Types.VARCHAR);
			
			//EJECUTA EL PROCEDIMIENTO ALMACENADO
			sto.execute();
			sTxrespuesta1 = sto.getString(18);
			sto.close();
			//Termina el proceso PKG_PROCESOS.pGeneraPreMovto.
			
			System.out.println("sTxrespuestapGeneraPreMovto"+sTxrespuesta1);
			
			//Llama al proceso PKG_PROCESADOR_FINANCIERO.pProcesaMovimiento.
			CallableStatement sto1 = conn.prepareCall("begin dbms_output.put_line(PKG_PROCESADOR_FINANCIERO.pProcesaMovimiento(?,?,?,?,?,?,?)); end;");
			sto1.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
			sto1.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
			sto1.setString(3, sIdPreMovi);
			sto1.setString(4, "PV");
			sto1.setString(5, (String)registro.getDefCampo("CVE_USUARIO"));
			sto1.setString(6, "F");
			sto1.registerOutParameter(7, java.sql.Types.VARCHAR);
			
			//EJECUTA EL PROCEDIMIENTO ALMACENADO
			sto1.execute();
			sTxrespuesta2 = sto1.getString(7);
			sto1.close();
			//Termina el proceso PKG_PROCESADOR_FINANCIERO.pProcesaMovimiento.
			
			System.out.println("sTxrespuestaProcesaMovimiento"+sTxrespuesta2);
			
			//Obtenemos el sequence vgFolioGrupo (no sé para qué).
			sSql = "SELECT SQ02_PFIN_PRE_MOVIMIENTO.NEXTVAL as vgFolioGrupo FROM DUAL";
			ejecutaSql();
			if (rs.next()){
				vgFolioGrupo = rs.getString("vgFolioGrupo");
			}
			
			//Llama al proceso PKG_CREDITO.paplicapagocredito.
			CallableStatement sto2 = conn.prepareCall("begin PKG_CREDITO.paplicapagocredito(?,?,?,?,?,?,?,?); end;");
			sto2.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
			sto2.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
			sto2.setString(3, (String)registro.getDefCampo("ID_PRESTAMO"));
			sto2.setString(4, vgFolioGrupo);
			sto2.setString(5, (String)registro.getDefCampo("CVE_USUARIO"));
			sto2.setString(6, sFechaAplicacion);
			sto2.setString(7, sIdTransaccion);
			sto2.registerOutParameter(8, java.sql.Types.VARCHAR);
			
			//EJECUTA EL PROCEDIMIENTO ALMACENADO
			sto2.execute();
			sTxrespuesta3 = sto2.getString(8);
			sto2.close();
			//Termina el proceso PKG_CREDITO.paplicapagocredito.
			
			//Se añade la respuesta sTxrespuesta3 a "RESPUESTA".
			System.out.println("sTxrespuesta paplicapagocredito"+sTxrespuesta3);
			resultadoCatalogo.Resultado.addDefCampo("RESPUESTA", sTxrespuesta3);
			
			//Verifica si se hizo el pago correctamente.
			if (sTxrespuesta3 == null){
				//El pago se hizo correctamente.
				
				//Se obtienen los atributos de ID_PRESTAMO, ID_PRODUCTO, NUM_CILO Y ID_CLIENTE del crédito que se paga.
				sSql = "SELECT \n" +
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_PRESTAMO, \n" +
						"ID_PRODUCTO, \n" +
						"NUM_CICLO, \n" +
						"ID_CLIENTE \n" +
						"FROM \n" +
						"SIM_PRESTAMO \n" +
						"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
				ejecutaSql();
				if (rs.next()){
					registro.addDefCampo("ID_PRESTAMO",rs.getString("ID_PRESTAMO")== null ? "": rs.getString("ID_PRESTAMO"));
					registro.addDefCampo("ID_PRODUCTO",rs.getString("ID_PRODUCTO")== null ? "": rs.getString("ID_PRODUCTO"));
					registro.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO")== null ? "": rs.getString("NUM_CICLO"));
					registro.addDefCampo("ID_CLIENTE",rs.getString("ID_CLIENTE")== null ? "": rs.getString("ID_CLIENTE"));
				}
			
				//Se obtiene el ID_MOVIMIENTO_OPERACION para la operación de pago individual.
				sSql = "SELECT \n" +
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
			
				sSql = "INSERT INTO SIM_CAJA_TRANSACCION ( \n"+
						"CVE_GPO_EMPRESA, \n" +
						"CVE_EMPRESA, \n" +
						"ID_MOVIMIENTO_OPERACION, \n" +
						"ID_TRANSACCION, \n" +
						"ID_SUCURSAL, \n" +
						"ID_CAJA, \n" +
						"CVE_MOVIMIENTO_CAJA, \n" +
						"MONTO, \n" +
						"FECHA_TRANSACCION, \n" +
						"CVE_USUARIO_CAJERO, \n" +
						"ID_PRESTAMO, \n" +
						"ID_PRODUCTO, \n" +
						"NUM_CICLO, \n" +
						"ID_CLIENTE) \n" +
						"VALUES ( \n"+
						"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
						"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
						sIdMovimientoOperacion + ", \n "+
						sIdTransaccion + ", \n "+
						"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
						"'PAGOIND', \n" +
						"'" + (String)registro.getDefCampo("IMP_NETO") + "', \n" +
						"SYSDATE, \n" +
						"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
						"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
						"'" + (String)registro.getDefCampo("ID_CLIENTE") + "') \n" ;
				
				//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
				resultadoCatalogo.Resultado.addDefCampo("ID_MOVIMIENTO_OPERACION", sIdMovimientoOperacion);
			}else {
				resultadoCatalogo.Resultado.addDefCampo("ID_MOVIMIENTO_OPERACION","null");
			}
		}else{
			//La fecha de transacción no es valida, no se realiza ningún pago.
			resultadoCatalogo.mensaje.setClave("FECHA_PAGO_NO_VALIDA");
		}
	
		
		
		return resultadoCatalogo;
	}
}