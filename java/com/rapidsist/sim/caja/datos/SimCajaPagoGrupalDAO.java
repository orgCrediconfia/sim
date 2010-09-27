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
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import javax.servlet.http.HttpServletRequest;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.cliente.CatalogoControlConsultaIN;
import com.rapidsist.portal.cliente.CatalogoControlActualizaIN;
import com.rapidsist.portal.cliente.RegistroControl;
import javax.naming.Context;
import javax.servlet.ServletConfig;
import java.rmi.RemoteException;
import java.util.Enumeration;
import com.rapidsist.portal.configuracion.Usuario;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;


/**
 * Administra los accesos a la base de datos para realizar el pago de amortizaciòn del grupo.
 */
 
public class SimCajaPagoGrupalDAO extends Conexion2 implements OperacionAlta {

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		String sMontoAutorizado = new String();
		String sCliente = new String();
		String sPrestamoIndividual = new String();
		String sTxrespuesta3 = "";
		
		if (registro.getDefCampo("DAO_MONTOS") == null){
			System.out.println("DAO_MONTOS im goint to resolve the problem");
		}else{
			System.out.println("DAO_MONTOS YAHOOO 1");
		}
		
		String[] sMontos = (String[]) registro.getDefCampo("DAO_MONTOS");
		String[] sIdCliente = (String[]) registro.getDefCampo("DAO_CLIENTE");
		String[] sIdPrestamoIndividual = (String[]) registro.getDefCampo("DAO_ID_PRESTAMO_IND");
		
		if (sMontos != null) {
			for (int iNumParametro = 0; iNumParametro < sMontos.length; iNumParametro++) {
				
			//OBTIENE LA CLAVE DE LA APLICACION
			sMontoAutorizado = sMontos[iNumParametro];
			sCliente = sIdCliente[iNumParametro];
			sPrestamoIndividual = sIdPrestamoIndividual[iNumParametro];
				
			registro.addDefCampo("MOVIMIENTO","CADA_UNO");
			registro.addDefCampo("IMP_NETO",sMontoAutorizado);
			registro.addDefCampo("ID_CLIENTE",sCliente);
			registro.addDefCampo("ID_PRESTAMO",sPrestamoIndividual);
		
			String sTxrespuesta1 = "";
			String sTxrespuesta2 = "";
			
			String sIdPreMovimiento = "";
			String sFLiquidacion = "";
			String sFechaAplicacion = "";
			String sFechaMovmiento = "";
			String sIdCuentaVista = "";
				
			//OBTENEMOS EL SEQUENCE
			sSql = "SELECT SQ01_PFIN_PRE_MOVIMIENTO.nextval as ID_PREMOVIMIENTO FROM DUAL";
			ejecutaSql();
				
			if (rs.next()){
				sIdPreMovimiento = rs.getString("ID_PREMOVIMIENTO");
			
			}
				
			sSql =  "SELECT TO_CHAR(TO_DATE(F_MEDIO,'DD-MM-YYYY'),'DD-MON-YYYY') AS F_LIQUIDACION \n"+
					"FROM PFIN_PARAMETRO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND CVE_MEDIO = 'SYSTEM' \n";
			ejecutaSql();
				
			if (rs.next()){
				sFLiquidacion = rs.getString("F_LIQUIDACION");
				
			}
			
			sSql = " SELECT TO_CHAR(TO_DATE('" + (String)registro.getDefCampo("FECHA_MOVIMIENTO") + "','DD-MM-YYYY'),'DD-MON-YYYY') F_APLICACION FROM DUAL";
			ejecutaSql();
			
			if (rs.next()){
				sFechaAplicacion = rs.getString("F_APLICACION");
					
			}
			
			//OBTENEMOS LA CUENTA
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
			
			CallableStatement sto = conn.prepareCall("begin PKG_PROCESOS.pGeneraPreMovto(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;");
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
			sto.registerOutParameter(17, java.sql.Types.VARCHAR);
			
			//EJECUTA EL PROCEDIMIENTO ALMACENADO
			sto.execute();
			sTxrespuesta1 = sto.getString(17);
			sto.close();
				
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
			sTxrespuesta2  = sto1.getString(7);
			sto1.close();
			
			CallableStatement sto2 = conn.prepareCall("begin PKG_CREDITO.paplicapagocredito(?,?,?,?,?,?); end;");
			sto2.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
			sto2.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
			sto2.setString(3, (String)registro.getDefCampo("ID_PRESTAMO"));
			sto2.setString(4, (String)registro.getDefCampo("CVE_USUARIO"));
			sto2.setString(5, sFechaAplicacion);
			sto2.registerOutParameter(6, java.sql.Types.VARCHAR);
		
			//EJECUTA EL PROCEDIMIENTO ALMACENADO
			sto2.execute();
			sTxrespuesta3  = sto2.getString(6);
			sto2.close();
			
			// SE AGREGA LA RESPUESTA
			
			resultadoCatalogo.Resultado.addDefCampo("RESPUESTA", sTxrespuesta3);
			}
		}
			
		if (sTxrespuesta3 == null){
					
			//Ingresa el pago grupal en la caja.
			String sIdTransaccion = "";
			int iIdTransaccion = 0;
			
			sSql =  "SELECT \n" +
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_GRUPO, \n" +
					"ID_PRODUCTO, \n" +
					"NUM_CICLO \n" +
					"FROM \n" +
					"SIM_PRESTAMO_GRUPO \n" +
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND ID_PRESTAMO_GRUPO = '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n";
			ejecutaSql();
			if (rs.next()){
				registro.addDefCampo("ID_GRUPO",rs.getString("ID_GRUPO")== null ? "": rs.getString("ID_GRUPO"));
				registro.addDefCampo("ID_PRODUCTO",rs.getString("ID_PRODUCTO")== null ? "": rs.getString("ID_PRODUCTO"));
				registro.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO")== null ? "": rs.getString("NUM_CICLO"));
			}
			
			sSql =  "SELECT \n" +
					"CVE_GPO_EMPRESA, \n" +
					"MAX(ID_TRANSACCION) ID_TRANSACCION \n" +
					"FROM \n" +
					"SIM_CAJA_TRANSACCION \n" +
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND CVE_MOVIMIENTO_CAJA = '" + (String)registro.getDefCampo("CVE_MOVIMIENTO_CAJA") + "' \n"+
					"GROUP BY CVE_GPO_EMPRESA \n";
			ejecutaSql();
				
			if (rs.next()){
				
				sIdTransaccion = rs.getString("ID_TRANSACCION");
				iIdTransaccion=Integer.parseInt(sIdTransaccion.trim());
				iIdTransaccion ++;
				sIdTransaccion= String.valueOf(iIdTransaccion);
			}else {
				
				sIdTransaccion = "1";
			}
			
			sSql =  "INSERT INTO SIM_CAJA_TRANSACCION ( \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_TRANSACCION, \n" +
					"ID_SUCURSAL, \n" +
					"ID_CAJA, \n" +
					"CVE_MOVIMIENTO_CAJA, \n" +
					"MONTO, \n" +
					"FECHA_TRANSACCION, \n" +
					"CVE_USUARIO_CAJERO, \n" +
					"ID_PRESTAMO_GRUPO, \n" +
					"ID_GRUPO, \n" +
					"ID_PRODUCTO, \n" +
					"NUM_CICLO) \n" +
			        "VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					sIdTransaccion + ", \n "+
					"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
					"'PAGOGPO', \n" +
					"'" + (String)registro.getDefCampo("IMPORTE") + "', \n" +
					"SYSDATE, \n" +
					"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
					"'" + (String)registro.getDefCampo("NUM_CICLO") + "') \n" ;
			
			//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
			resultadoCatalogo.Resultado.addDefCampo("ID_TRANSACCION", sIdTransaccion);		
		}	
		return resultadoCatalogo;
	}
}