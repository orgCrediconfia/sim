/**
 * Sistema de administración de portales.
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
 
public class SimCajaIngresoCajaDesembolsarDAO extends Conexion2 implements OperacionAlta, OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
		//Consulta los pagos grupales que se han realizado en la sucursal-caja.
		sSql = "SELECT \n"+
				"T.ID_PRESTAMO_GRUPO, \n"+
				"V.CVE_PRESTAMO, \n"+
				"T.ID_TRANSACCION, \n"+
				"'GRUPO' APLICA_A, \n"+
				"V.CVE_PRESTAMO, \n"+
				"T.ID_CLIENTE, \n"+
				"T.ID_GRUPO, \n"+
				"G.NOM_GRUPO, \n"+
				"T.NUM_CICLO, \n"+
				"T.FECHA_TRANSACCION, \n"+
				"V.NOM_PRODUCTO, \n"+
				"V.NOMBRE, \n"+
				"NVL(ROUND(T.MONTO,2),0) MONTO, \n"+ 
				"TO_CHAR(NVL(ROUND(T.MONTO,2),0),'999,999,999.99') IMPORTE \n"+ 
				"FROM SIM_CAJA_TRANSACCION T, \n"+
				"V_CREDITO V, \n"+
				"SIM_GRUPO G \n"+
				"WHERE T.CVE_GPO_EMPRESA = '" + (String) parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND T.CVE_EMPRESA = '" + (String) parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND T.CVE_MOVIMIENTO_CAJA = 'PAGOGPO' \n"+
				"AND T.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
				"AND T.ID_CAJA = '" + (String)parametros.getDefCampo("ID_CAJA") + "' \n"+
				"AND T.FECHA_CANCELACION IS NULL \n"+
				"AND V.CVE_GPO_EMPRESA =  T.CVE_GPO_EMPRESA \n"+
				"AND V.CVE_EMPRESA =  T.CVE_EMPRESA \n"+
				"AND V.ID_PRESTAMO =  T.ID_PRESTAMO_GRUPO \n"+
				"AND G.CVE_GPO_EMPRESA =  T.CVE_GPO_EMPRESA \n"+
				"AND G.CVE_EMPRESA =  T.CVE_EMPRESA \n"+
				"AND G.ID_GRUPO =  T.ID_GRUPO \n";
				
		if (parametros.getDefCampo("CVE_PRESTAMO_GRUPO") != null) {
			sSql = sSql + "AND V.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO_GRUPO") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_GRUPO") != null) {
			sSql = sSql + "AND G.NOM_GRUPO = '" + (String) parametros.getDefCampo("NOM_GRUPO") + "' \n";
		}
		
		ejecutaSql();
		return getConsultaLista();
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
		
		String sIdMovimientoOperacion = "";
		int iIdMovimientoOperacion = 0;
		String sMontoCaja = "";
		float fMontoCaja = 0;
		String sMonto = "";
		float fMonto = 0;
		//Esta variable mapea los movimientos de caja y las operaciones hechas por pl.
		String sIdTransaccion = "";
		String sFMedio = "";
		String sIdPreMovimiento = "";
		String sTxrespuesta1 = "";
		String sTxrespuesta2 = "";
		String sIdCuentaVista = "";
		
		//Obtenemos el sequence ID_TRANSACCION que relacionará el movimiento de la caja con los movimientos del credito 
		//en las tablas PFIN_MOVIMIENTO y PFIN_PRE_MOVIMIENTO.
		sSql = "SELECT SQ01_SIM_CAJA_TRANSACCION.nextval as ID_TRANSACCION FROM DUAL";
		ejecutaSql();
		if (rs.next()){
			sIdTransaccion = rs.getString("ID_TRANSACCION");
		}
		
		sSql = "SELECT TO_CHAR(F_MEDIO,'DD-MON-YYYY') AS F_MEDIO \n"+
			"FROM PFIN_PARAMETRO \n"+
			"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND CVE_MEDIO = 'SYSTEM' \n";
		ejecutaSql();
		if (rs.next()){
			sFMedio = rs.getString("F_MEDIO");
		}
		
			//Obtiene la cuenta del crédito.
			sSql =  "SELECT \n"+
						"PG.ID_PRESTAMO, \n"+
					    "PG.ID_PRESTAMO_GRUPO, \n"+
					    "P.ID_CLIENTE, \n"+
					    "C.ID_CUENTA \n"+
						"FROM \n"+
						"SIM_PRESTAMO_GPO_DET PG, \n"+
					    "SIM_PRESTAMO P, \n"+
					    "PFIN_CUENTA C \n"+
					    "WHERE PG.CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND PG.CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					    "AND P.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
					    "AND P.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
					    "AND P.ID_PRESTAMO = PG.ID_PRESTAMO \n"+
					    "AND C.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
					    "AND C.CVE_EMPRESA = P.CVE_EMPRESA \n"+
					    "AND C.CVE_TIP_CUENTA = 'VISTA' \n"+
					    "AND C.SIT_CUENTA = 'AC' \n"+
					    "AND ID_TITULAR ='" + (String)registro.getDefCampo("ID_PERSONA") + "' \n"+
						"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
						"AND ID_CLIENTE = '" + (String)registro.getDefCampo("ID_PERSONA") + "' \n";
			ejecutaSql();
			if (rs.next()){
				sIdCuentaVista = rs.getString("ID_CUENTA");
				registro.addDefCampo("ID_PRESTAMO",rs.getString("ID_PRESTAMO"));
			}
			
			
			//Obtenemos el sequence ID_PRE_MOVIMIENTO para la tabla PFIN_PRE_MOVIMIENTO.
			sSql = "SELECT SQ01_PFIN_PRE_MOVIMIENTO.nextval as ID_PREMOVIMIENTO FROM DUAL";
			ejecutaSql();
			if (rs.next()){
				sIdPreMovimiento = rs.getString("ID_PREMOVIMIENTO");
			}
			String sCveGpoEmpresa = (String)registro.getDefCampo("CVE_GPO_EMPRESA");
			String sCveEmpresa = (String)registro.getDefCampo("CVE_EMPRESA");
			String sIdPreMovi = sIdPreMovimiento;
			String sFMovimiento = sFMedio;
			String sIdCuenta = sIdCuentaVista;
			String sIdPrestamo = (String)registro.getDefCampo("ID_PRESTAMO");
			String sCveDivisa = "MXP";
			String sCveOperacion = "INGCAJDES";
			String sImpNeto = (String)registro.getDefCampo("IMP_NETO");
			String sCveMedio = "PRESTAMO";
			String sCveMercado = "PRESTAMO";
			String sNota = "Movimiento extraordinario en el saldo de la cuenta";
			String sIdGrupo = "";
			String sCveUsuario = (String)registro.getDefCampo("CVE_USUARIO");
			String sFValor = sFMedio;
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
			System.out.println("sFValor********************************************"+sFMedio);
			System.out.println("sNumPagoAmort"+sNumPagoAmort);
			
			CallableStatement sto = conn.prepareCall("begin PKG_PROCESOS.pGeneraPreMovto(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;");
			System.out.println("1");
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
			sto.setString(15, sFMedio);
			sto.setString(16, sNumPagoAmort);
			sto.setString(17, sIdTransaccion);
			sto.registerOutParameter(18, java.sql.Types.VARCHAR);
			System.out.println("2");
			//EJECUTA EL PROCEDIMIENTO ALMACENADO
			sto.execute();
			sTxrespuesta1 = sto.getString(18);
			sto.close();
			System.out.println("3");
			CallableStatement sto1 = conn.prepareCall("begin dbms_output.put_line(PKG_PROCESADOR_FINANCIERO.pProcesaMovimiento(?,?,?,?,?,?,?)); end;");
			System.out.println("4");
			sto1.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
			sto1.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
			sto1.setString(3, sIdPreMovi);
			sto1.setString(4, "PV");
			sto1.setString(5, (String)registro.getDefCampo("CVE_USUARIO"));
			sto1.setString(6, "F");
			sto1.registerOutParameter(7, java.sql.Types.VARCHAR);
		
			//EJECUTA EL PROCEDIMIENTO ALMACENADO
			sto1.execute();
			sTxrespuesta2  = sto1.getString(7);
			sto1.close();
		
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
				"ID_CLIENTE, \n" +
				"ID_PRESTAMO, \n" +
				"ID_GRUPO, \n" +
				"ID_PRODUCTO, \n" +
				"NUM_CICLO, \n" +
				"CVE_MOVIMIENTO_CAJA, \n" +
				"MONTO, \n" +
				"FECHA_TRANSACCION, \n" +
				"CVE_USUARIO_CAJERO, \n" +
				"USUARIO_ENTREGA, \n" +
				"ID_TRANSACCION_GRUPO, \n" +
				"ID_PRESTAMO_GRUPO) \n" +
		        "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdMovimientoOperacion + ", \n "+
				sIdTransaccion + ", \n "+
				"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
				"'" + (String)registro.getDefCampo("USUARIO_ENTREGA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
				"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_MOVIMIENTO_CAJA") + "', \n" +
				"'" + (String)registro.getDefCampo("IMP_NETO") + "', \n" +
				"SYSDATE, \n" +
				"'" + (String)registro.getDefCampo("CVE_USUARIO_CAJERO") + "', \n" +
				"'" + (String)registro.getDefCampo("USUARIO_ENTREGA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_TRANSACCION_GRUPO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "') \n" ;
		
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		resultadoCatalogo.Resultado.addDefCampo("ID_MOVIMIENTO_OPERACION", sIdMovimientoOperacion);
		
		return resultadoCatalogo;
		
	}
}