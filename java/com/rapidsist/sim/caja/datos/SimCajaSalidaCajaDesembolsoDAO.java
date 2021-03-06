/**
 * Sistema de administraci�n de portales.
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
import java.sql.CallableStatement;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para mostrar movimientos de Caja.
 */
 
public class SimCajaSalidaCajaDesembolsoDAO extends Conexion2 implements OperacionAlta, OperacionConsultaTabla, OperacionConsultaRegistro {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de b�squeda.
	 * @param parametros Par�metros que se le env�an a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  "SELECT \n"+
				"T.CVE_GPO_EMPRESA, \n"+
				"T.CVE_EMPRESA, \n"+
				"T.ID_PRESTAMO_GRUPO, \n"+
				"T.ID_PRESTAMO, \n"+
				"PG.CVE_PRESTAMO_GRUPO, \n"+
				"TO_CHAR(SUM(T.MONTO),'999,999,999.99') MONTO, \n"+
				"T.ID_TRANSACCION_GRUPO, \n"+
				"T.ID_GRUPO, \n"+
				"G.NOM_GRUPO, \n"+
				"T.ID_PRODUCTO, \n"+
				"P.NOM_PRODUCTO, \n"+
				"T.NUM_CICLO, \n"+
				"T.ID_SUCURSAL, \n"+
				"T.ID_CAJA, \n"+
				"T.FECHA_TRANSACCION, \n"+
				"T.ID_TRANSACCION \n"+
				"FROM \n"+
				"SIM_CAJA_TRANSACCION T, \n"+
				"SIM_PRESTAMO_GRUPO PG, \n"+
				"SIM_GRUPO G, \n"+
				"SIM_PRODUCTO P \n"+
				"WHERE T.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND T.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND T.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
				"AND T.ID_CAJA = '" + (String)parametros.getDefCampo("ID_CAJA") + "' \n"+
				"AND T.CVE_MOVIMIENTO_CAJA = 'INGCAJDES' \n"+ 
				"AND T.FECHA_CANCELACION IS NULL \n"+ 
				"AND PG.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+
				"AND PG.CVE_EMPRESA = T.CVE_EMPRESA \n"+
				"AND PG.ID_PRESTAMO_GRUPO = T.ID_PRESTAMO_GRUPO \n"+
				"AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
				"AND G.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
				"AND G.ID_GRUPO = PG.ID_GRUPO \n"+
				"AND P.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
				"AND P.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
				"AND P.ID_PRODUCTO = PG.ID_PRODUCTO \n"+
				"GROUP BY T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO_GRUPO, T.ID_PRESTAMO, PG.CVE_PRESTAMO_GRUPO, T.ID_TRANSACCION_GRUPO, T.ID_GRUPO, G.NOM_GRUPO, T.ID_PRODUCTO, P.NOM_PRODUCTO, T.NUM_CICLO, T.ID_SUCURSAL, T.ID_CAJA, T.FECHA_TRANSACCION, T.ID_TRANSACCION \n";

		if (parametros.getDefCampo("CVE_PRESTAMO_GRUPO") != null) {
			sSql = sSql + "AND PG.CVE_PRESTAMO_GRUPO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO_GRUPO") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_GRUPO") != null) {
			sSql = sSql + "AND G.NOM_GRUPO = '" + (String) parametros.getDefCampo("NOM_GRUPO") + "' \n";
		}
		
		sSql = sSql + "ORDER BY T.ID_PRESTAMO_GRUPO \n";
		
		System.out.println("solo los que no han salido"+sSql);
		
		ejecutaSql();
		return getConsultaLista();
	}
	
	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Par�metros que se le env�an a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql = " SELECT \n"+
			   " T.CVE_GPO_EMPRESA, \n"+
			   " T.CVE_EMPRESA, \n"+
			   " T.MONTO IMPORTE, \n" + 
			   " TO_CHAR(T.MONTO,'999,999,999.99') MONTO \n" + 
			   " FROM SIM_CAJA_TRANSACCION T \n"+
			   " WHERE T.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND T.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND T.ID_TRANSACCION = '" + (String)parametros.getDefCampo("ID_TRANSACCION") + "' \n"+
			   " AND T.CVE_MOVIMIENTO_CAJA = 'INGCAJDES' \n";
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecuci�n de este m�todo.
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
		String sIdCuentaVista = "";
		String sIdPreMovimiento = "";
		String sTxrespuesta1 = "";
		String sTxrespuesta2 = "";
		
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
		
		sMonto = (String)registro.getDefCampo("IMPORTE");
		fMonto = Float.parseFloat(sMonto.trim());
		
		if (fMontoCaja < fMonto){
			//La caja no tiene fondos para el hacer el desembolso
			resultadoCatalogo.mensaje.setClave("FONDO_INSUFICIENTE");
		}else {
			
			//Obtenemos el sequence ID_TRANSACCION que relacionar� el movimiento de la caja con los movimientos del credito 
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
				
				//Obtiene la cuenta del cr�dito.
				sSql =  "SELECT ID_CUENTA \n"+
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
				String sCveOperacion = "SALCAJDES";
				String sImpNeto = (String)registro.getDefCampo("IMPORTE");
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
				sto.setString(9, (String)registro.getDefCampo("IMPORTE"));
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
				"ID_PRESTAMO_GRUPO, \n" +
				"ID_SUCURSAL, \n" +
				"ID_CAJA, \n" +
				"ID_CLIENTE, \n" +
				"ID_GRUPO, \n" +
				"ID_PRODUCTO, \n" +
				"NUM_CICLO, \n" +
				"CVE_MOVIMIENTO_CAJA, \n" +
				"MONTO, \n" +
				"FECHA_TRANSACCION, \n" +
				"CVE_USUARIO_CAJERO, \n" +
				"ID_TRANSACCION_GRUPO, \n" +
				"USUARIO_RECIBE) \n" +
		        "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdMovimientoOperacion + ", \n "+
				sIdTransaccion + ", \n "+
				"'" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
				"'" + (String)registro.getDefCampo("USUARIO_RECIBE") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
				"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
				"'SALCAJDES', \n" +
				"-'" + (String)registro.getDefCampo("IMPORTE") + "', \n" +
				"SYSDATE, \n" +
				"'" + (String)registro.getDefCampo("CVE_USUARIO_CAJERO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_TRANSACCION_GRUPO") + "', \n" +
				"'" + (String)registro.getDefCampo("USUARIO_RECIBE") + "') \n" ;
		
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
		
		resultadoCatalogo.Resultado.addDefCampo("ID_MOVIMIENTO_OPERACION", sIdMovimientoOperacion);
		}
		return resultadoCatalogo;
		
	}
}