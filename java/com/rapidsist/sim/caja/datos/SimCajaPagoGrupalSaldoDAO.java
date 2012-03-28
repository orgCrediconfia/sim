/**
 * Sistema de administraci�n de portales.
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para dar de alta el grupo al que se le va a 
 * otorgar el pr�stamo grupal, validando el m�nimo de porcentajes fundadores del grupo,
 * as� como que se cumplan los par�metros globales del grupo (M�ximo de integrantes,
 * m�nimo de integrantes, m�ximo de integrantes en riesgo, m�ximo de negocios ambulates y
 * m�ximo de negocios por cat�logos).
 */
 
public class SimCajaPagoGrupalSaldoDAO extends Conexion2 implements OperacionAlta {

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecuci�n de este m�todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		String sImporte = "";
		String sSaldoFecha = "";
		String sSaldoTotal = "";
		String sDiferencia = "";
		float fImporte = 0;
		float fSaldoFecha = 0;
		float fSaldoTotal = 0;
		float fDiferencia = 0;
		float fSaldoCredito = 0;
		
		sImporte = (String)registro.getDefCampo("IMPORTE");
		fImporte = (Float.parseFloat(sImporte.replace(",","")));
		
		if (registro.getDefCampo("SALDO_FECHA") == null){
			sSaldoFecha = "0";
		}else {
			sSaldoFecha = (String)registro.getDefCampo("SALDO_FECHA");
		}
		
		fSaldoFecha = (Float.parseFloat(sSaldoFecha.replace(",","")));
		sSaldoTotal = (String)registro.getDefCampo("SALDO_TOTAL");
		fSaldoTotal = (Float.parseFloat(sSaldoTotal.replace(",",""))); 
		
		fSaldoCredito = fSaldoTotal;
		
		fSaldoCredito = fSaldoCredito < 0 ? -fSaldoCredito : fSaldoCredito;
		
		if (fImporte > fSaldoCredito){
			resultadoCatalogo.Resultado.addDefCampo("SALDO",fSaldoTotal);
			resultadoCatalogo.Resultado.addDefCampo("VALIDA_PAGO_LIMITE","NO");	
		}else{
			//El cr�dito tiene una deuda.
			if (fSaldoFecha < 0){
				sSql =  " SELECT \n"+
						" '"+fImporte+"' + '"+fSaldoFecha+"' AS DIFERENCIA \n"+
						" FROM DUAL \n";
				ejecutaSql();
				if (rs.next()){
					sDiferencia = rs.getString("DIFERENCIA");
					fDiferencia = (Float.parseFloat(sDiferencia));
					if (fDiferencia > 0){
						//El pago excede la deuda actual.
						resultadoCatalogo.Resultado.addDefCampo("DIFERENCIA","El pago se excede por $ "+fDiferencia+" pesos");
						resultadoCatalogo.Resultado.addDefCampo("PAGO_TOTAL","0");
						resultadoCatalogo.Resultado.addDefCampo("SALDO",fSaldoTotal);
					}else{
						//El pago NO excede la deuda actual.
						resultadoCatalogo.Resultado.addDefCampo("PAGO_TOTAL","0");
						resultadoCatalogo.Resultado.addDefCampo("SALDO",fSaldoTotal);
					}
				}		
			}
			//El cr�dito no tiene deuda a la fecha.
			else if (fSaldoFecha >= 0){
				resultadoCatalogo.Resultado.addDefCampo("DIFERENCIA","El pago se excede por $ "+fImporte+" pesos");	
				resultadoCatalogo.Resultado.addDefCampo("PAGO_TOTAL","1");
				resultadoCatalogo.Resultado.addDefCampo("SALDO",fSaldoTotal);
			}
			resultadoCatalogo.Resultado.addDefCampo("VALIDA_PAGO_LIMITE","SI");	
		}
		
		
		return resultadoCatalogo;
	}
}