/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para generar las tablas de amortizaciòn.
 */
 
public class SimPrestamoMovimientoExtraordinarioDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro {


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
				" P.CVE_GPO_EMPRESA, \n"+
				" P.CVE_EMPRESA, \n"+
				" P.CVE_PRESTAMO, \n"+
				" P.ID_PRESTAMO, \n"+
				" P.CVE_PRESTAMO, \n"+
				" P.ID_CLIENTE, \n"+
				" N.NOM_COMPLETO \n"+
				" FROM SIM_PRESTAMO P, \n"+
				" RS_GRAL_PERSONA N, \n"+
				" SIM_CAT_ETAPA_PRESTAMO E, \n"+
				" SIM_USUARIO_ACCESO_SUCURSAL US \n"+
				" WHERE P.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND P.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND N.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				" AND N.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				" AND N.ID_PERSONA = P.ID_CLIENTE \n"+
				" AND E.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				" AND E.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				" AND E.ID_ETAPA_PRESTAMO = P.ID_ETAPA_PRESTAMO \n"+
				" AND E.B_ENTREGADO = 'V' \n" +
				" AND US.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
                " AND US.CVE_EMPRESA = P.CVE_EMPRESA \n"+
                " AND US.ID_SUCURSAL = P.ID_SUCURSAL \n"+
                " AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n";
				
		if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
			sSql = sSql + " AND P.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_COMPLETO") != null) {
			sSql = sSql + " AND UPPER(N.NOM_COMPLETO) LIKE'%" + ((String) parametros.getDefCampo("NOM_COMPLETO")).toUpperCase()  + "%' \n";
		}
		
		sSql = sSql + " ORDER BY P.ID_PRESTAMO \n";
				
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
		sSql = "SELECT \n"+
				" P.CVE_GPO_EMPRESA, \n"+
				" P.CVE_EMPRESA, \n"+
				" P.ID_PRESTAMO, \n"+
				" P.CVE_PRESTAMO, \n"+
				" P.ID_CLIENTE, \n"+
				" N.NOM_COMPLETO \n"+
				" FROM SIM_PRESTAMO P, \n"+
				" RS_GRAL_PERSONA N \n"+
				" WHERE P.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND P.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND P.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
				" AND N.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				" AND N.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				" AND N.ID_PERSONA = P.ID_CLIENTE \n";
			   
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
		
		String sTxrespuesta1 = "";
		String sTxrespuesta2 = "";
		String sTxrespuesta3 = "";
		String sTxrespuesta4 = "";
		
		if (registro.getDefCampo("OPERACION").equals("PREMOVTO")) {
			
			String sIdPreMovimiento = "";
			String sFLiquidacion = "";
			String sFechaAplicacion = "";
			String sFechaMovimiento = "";
			String sCuenta = "";
			String sNumAmort = "";
			
			sNumAmort = (String)registro.getDefCampo("NUM_AMORT");
			
			//OBTENEMOS EL SEQUENCE
			sSql = "SELECT SQ01_PFIN_PRE_MOVIMIENTO.nextval as ID_PREMOVIMIENTO FROM DUAL";
			ejecutaSql();
			
			if (rs.next()){
				sIdPreMovimiento = rs.getString("ID_PREMOVIMIENTO");
				
			}
			
			sSql =  "SELECT \n" +
					"ID_CUENTA \n"+
					"FROM \n"+
					"SIM_PRESTAMO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND ID_PRESTAMO = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n";
			
			ejecutaSql();
			
			if (rs.next()){
				sCuenta = rs.getString("ID_CUENTA");
				
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
			
			String sCveGpoEmpresa = (String)registro.getDefCampo("CVE_GPO_EMPRESA");
			String sCveEmpresa = (String)registro.getDefCampo("CVE_EMPRESA");
			String sIdPreMovi = sIdPreMovimiento;
			String sFMovimiento = sFLiquidacion;
			String sIdCuenta = sCuenta;
			String sIdPrestamo = (String)registro.getDefCampo("ID_PRESTAMO");
			String sCveDivisa = "MXP";
			String sCveOperacion = (String)registro.getDefCampo("CVE_OPERACION");
			String sImpNeto = (String)registro.getDefCampo("IMP_NETO");
			String sCveMedio = "PRESTAMO";
			String sCveMercado = "PRESTAMO";
			String sNota = "Movimiento extraordinario";
			String sIdGrupo = "";
			String sCveUsuario = (String)registro.getDefCampo("CVE_USUARIO");
			String sFValor = sFechaAplicacion;
			String sNumPagoAmort = sNumAmort;
			
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
			System.out.println("sCveUsuario"+sCveUsuario);
			System.out.println("sFValor"+sFValor);
			System.out.println("sNumPagoAmort"+sNumPagoAmort);
			
			CallableStatement sto = conn.prepareCall("begin PKG_PROCESOS.pGeneraPreMovto(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;");
			sto.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
			sto.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
			sto.setString(3, sIdPreMovi);
			sto.setString(4, sFMovimiento);
			sto.setString(5, sIdCuenta);
			sto.setString(6, (String)registro.getDefCampo("ID_PRESTAMO"));
			sto.setString(7, sCveDivisa);
			sto.setString(8, (String)registro.getDefCampo("CVE_OPERACION"));
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
			
			// SE AGREGA LA RESPUESTA
		
			//registro.addDefCampo("RESPUESTA",sTxrespuesta);
			resultadoCatalogo.Resultado.addDefCampo("ID_PRE_MOVTO", sIdPreMovimiento);
		}
		else if (registro.getDefCampo("OPERACION").equals("PREMOVTO_DET")) {
		//resultadoCatalogo.Resultado = registro;
			
			String sCveGpoEmpresa = (String)registro.getDefCampo("CVE_GPO_EMPRESA");
			String sCveEmpresa = (String)registro.getDefCampo("CVE_EMPRESA");
			String sIdPreMovi = (String)registro.getDefCampo("ID_PREMOVTO");
			String sCveOperacion = (String)registro.getDefCampo("CVE_OPERACION");
			String sCveConcepto = (String)registro.getDefCampo("CVE_CONCEPTO");
			String sImporteConcepto = (String)registro.getDefCampo("IMPORTE_CONCEPTO");
			
			System.out.println("sCveGpoEmpresa"+sCveGpoEmpresa);
			System.out.println("sCveEmpresa"+sCveEmpresa);
			System.out.println("sIdPreMovimiento"+sIdPreMovi);
			System.out.println("sCveOperacion"+sCveOperacion);
			System.out.println("sCveConcepto"+sCveConcepto);
			System.out.println("sImporteConcepto"+sImporteConcepto);
			
			CallableStatement sto = conn.prepareCall("begin PKG_PROCESOS.pGeneraPreMovtoDet(?,?,?,?,?,?,?); end;");
			
			sto.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
			sto.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
			sto.setString(3, (String)registro.getDefCampo("ID_PREMOVTO"));
			sto.setString(4, (String)registro.getDefCampo("CVE_CONCEPTO"));
			sto.setString(5, (String)registro.getDefCampo("IMPORTE_CONCEPTO"));
			sto.setString(6, "Movimiento extraordinario");
			sto.registerOutParameter(7, java.sql.Types.VARCHAR);
	
			//EJECUTA EL PROCEDIMIENTO ALMACENADO
			sto.execute();
			sTxrespuesta2  = sto.getString(7);
			sto.close();
			
			// SE AGREGA LA RESPUESTA
			
			//registro.addDefCampo("RESPUESTA" ,sTxrespuesta);
			//resultadoCatalogo.Resultado = registro;
		
		}else if (registro.getDefCampo("OPERACION").equals("PROCESA_MOVTO")) {
		//resultadoCatalogo.Resultado = registro;
		
			String sCveGpoEmpresa = (String)registro.getDefCampo("CVE_GPO_EMPRESA");
			String sCveEmpresa = (String)registro.getDefCampo("CVE_EMPRESA");
			String sIdPreMovi = (String)registro.getDefCampo("ID_PREMOVTO");
			String sCveUsuario = (String)registro.getDefCampo("CVE_USUARIO");
		
			String sCallable="begin ? := PKG_PROCESADOR_FINANCIERO.pProcesaMovimiento(?,?,?,?,?,?,?); end;";
		
		    CallableStatement sto =conn.prepareCall(sCallable);
		  
		    sto.registerOutParameter(1,java.sql.Types.NUMERIC);//registra parametro de salida
		    sto.registerOutParameter(8,java.sql.Types.VARCHAR); //registra parametro de salida
			sto.setString(2, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
		    sto.setString(3, (String)registro.getDefCampo("CVE_EMPRESA"));
		    sto.setString(4, (String)registro.getDefCampo("ID_PREMOVTO"));
		    sto.setString(5, "PV");
		    sto.setString(6, (String)registro.getDefCampo("CVE_USUARIO"));
		    sto.setString(7, "F");
		    sto.setString(8, " ");
		
		    //EJECUTA EL PROCEDIMIENTO ALMACENADO
		   
		   	sto.execute();
			long iIdMovimiento  = sto.getLong(1);
			sto.close();
		 
			
			// SE AGREGA LA RESPUESTA
			
			CallableStatement sto2 = conn.prepareCall("begin PKG_CREDITO.pActualizaTablaAmortizacion(?,?,?,?); end;");
			
			sto2.setString(1, (String)registro.getDefCampo("CVE_GPO_EMPRESA"));
			sto2.setString(2, (String)registro.getDefCampo("CVE_EMPRESA"));
			sto2.setLong(3, iIdMovimiento);
			sto2.registerOutParameter(4, java.sql.Types.VARCHAR);
			
			//EJECUTA EL PROCEDIMIENTO ALMACENADO
			sto2.execute();
			sTxrespuesta4  = sto2.getString(4);
			sto2.close();
			
		}
		return resultadoCatalogo;
	}
}