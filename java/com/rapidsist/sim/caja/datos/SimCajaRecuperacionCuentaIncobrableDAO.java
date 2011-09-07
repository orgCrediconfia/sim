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
import java.sql.CallableStatement;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para mostrar movimientos de Caja.
 */
 
public class SimCajaRecuperacionCuentaIncobrableDAO extends Conexion2 implements OperacionAlta, OperacionConsultaTabla, OperacionConsultaRegistro {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql = "SELECT \n"+
				"V.CVE_GPO_EMPRESA, \n"+
				"V.CVE_EMPRESA, \n"+
				"V.ID_PRESTAMO, \n"+
				"V.CVE_PRESTAMO, \n"+ 
				"V.APLICA_A, \n"+ 
				"V.NOMBRE, \n"+
				"V.CVE_NOMBRE, \n"+
				"V.ID_PRODUCTO, \n"+
				"V.NOM_PRODUCTO, \n"+
				"V.NUM_CICLO, \n"+
				"V.ID_ETAPA_PRESTAMO, \n"+
				"E.NOM_ESTATUS_PRESTAMO \n"+
				"FROM V_CREDITO V, \n"+
				"SIM_CAT_ETAPA_PRESTAMO E, \n"+
				"SIM_USUARIO_ACCESO_SUCURSAL US  \n"+
				"WHERE V.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND V.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND V.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
				"AND V.ID_ETAPA_PRESTAMO = '9' \n"+
				"AND E.CVE_GPO_EMPRESA = V.CVE_GPO_EMPRESA \n"+
				"AND E.CVE_EMPRESA = V.CVE_EMPRESA \n"+
				"AND E.ID_ETAPA_PRESTAMO = V.ID_ETAPA_PRESTAMO \n"+
				"AND US.CVE_GPO_EMPRESA = V.CVE_GPO_EMPRESA \n"+
				"AND US.CVE_EMPRESA = V.CVE_EMPRESA \n"+
				"AND US.ID_SUCURSAL = V.ID_SUCURSAL \n"+
				"AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
				"AND V.APLICA_A != 'INDIVIDUAL_GRUPO' \n";
				
		if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
			sSql = sSql + " AND V.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_COMPLETO") != null) {
			sSql = sSql + " AND UPPER(V.NOMBRE) LIKE'%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase()  + "%' \n";
		}
		
		sSql = sSql + " ORDER BY V.CVE_PRESTAMO \n";
			
		System.out.println("creditos incobrables"+sSql);
		
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
		sSql = " SELECT \n"+
			   " V.CVE_GPO_EMPRESA, \n"+
			   " V.CVE_EMPRESA, \n"+
			   " V.CVE_PRESTAMO, \n" + 
			   " V.NOMBRE \n" + 
			   " FROM V_CREDITO V \n"+
			   " WHERE V.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND V.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND V.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n";
			   
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
				String sCveOperacion = "RECCTAINC";
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
			
				//EJECUTA EL PROCEDIMIENTO ALMACENADO
				sto.execute();
				sTxrespuesta1 = sto.getString(18);
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
				"ID_GRUPO, \n" +
				"ID_PRODUCTO, \n" +
				"NUM_CICLO, \n" +
				"CVE_MOVIMIENTO_CAJA, \n" +
				"MONTO, \n" +
				"FECHA_TRANSACCION, \n" +
				"CVE_USUARIO_CAJERO) \n" +
		        "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdMovimientoOperacion + ", \n "+
				sIdTransaccion + ", \n "+
				"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_CLIENTE") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
				"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
				"'RECCTAINC', \n" +
				"'" + (String)registro.getDefCampo("IMPORTE") + "', \n" +
				"SYSDATE, \n" +
				"'" + (String)registro.getDefCampo("CVE_USUARIO_CAJERO") + "') \n" ;
		
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		resultadoCatalogo.Resultado.addDefCampo("ID_MOVIMIENTO_OPERACION", sIdMovimientoOperacion);
		
		return resultadoCatalogo;
		
	}
}