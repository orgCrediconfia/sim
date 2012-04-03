/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.Iterator;
import java.sql.CallableStatement;
import java.sql.Connection;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleDriver;

import java.util.*;
import java.sql.ResultSet;


/**
 * Administra los accesos a la base de datos para la Distribución automática del pago grupal.
 */
 
public class SimCajaDistribucionPagoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro {
	

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		LinkedList lista = new LinkedList();
		Registro registro = new Registro();
		
		
		String sImporte = "";
		String sFechaAplicacion = "";
		String sIdPrestamoGrupo = "";
		String sIdCliente = "";
		String sImpDeuda = "";
		float fImpDeuda;
		String sExcedente = "";
		float fExcedente;
		
		sImporte = (String)parametros.getDefCampo("IMPORTE");
		sExcedente = (String)parametros.getDefCampo("EXCEDENTE");
		
		System.out.println("sImporte"+sImporte);
		System.out.println("sExcedente"+sExcedente);
		
		sSql =  "SELECT \n"+ 
				"PG.CVE_GPO_EMPRESA, \n"+ 
				"PG.CVE_EMPRESA, \n"+ 
				"PG.ID_GRUPO, \n"+ 
				"G.NOM_GRUPO, \n"+ 
				"G.ID_COORDINADOR \n"+ 
				"FROM \n"+ 
				"SIM_PRESTAMO_GRUPO PG, \n"+ 
				"SIM_GRUPO G \n"+ 
				"WHERE PG.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				"AND PG.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
				"AND PG.ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
				"AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n" +
				"AND G.CVE_EMPRESA = PG.CVE_EMPRESA \n" +
				"AND G.ID_GRUPO = PG.ID_GRUPO \n" ;
		ejecutaSql();
		if (rs.next()){
			sIdCliente = rs.getString("ID_COORDINADOR");
		}
		
		
		sSql = " SELECT TO_CHAR(TO_DATE('" + (String)parametros.getDefCampo("FECHA_MOVIMIENTO") + "','DD-MM-YYYY'),'DD-MON-YYYY') F_APLICACION FROM DUAL";
		ejecutaSql();
		
		if (rs.next()){
			sFechaAplicacion = rs.getString("F_APLICACION");
			
		}
		
		sIdPrestamoGrupo = (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO");
		String sCallable="begin ? := PKG_CREDITO.fCalculaProporcion(?,?,?,?,?,?); end;";
	    CallableStatement cstmt =conn.prepareCall(sCallable);
	    cstmt.setFetchSize(10000);
	    cstmt.registerOutParameter(1,OracleTypes.CURSOR);
	    cstmt.registerOutParameter(7,java.sql.Types.VARCHAR); //registra parametro de salida
	    cstmt.setString(2, (String)parametros.getDefCampo("CVE_GPO_EMPRESA"));
		cstmt.setString(3, (String)parametros.getDefCampo("CVE_EMPRESA"));
	    cstmt.setString(4, sIdPrestamoGrupo);
	    cstmt.setString(5, sFechaAplicacion);
	    cstmt.setString(6, sImporte);
	    cstmt.setString(7, "");
	    //EJECUTA EL PROCEDIMIENTO ALMACENADO
	    cstmt.executeQuery();
		ResultSet rs = (ResultSet)
		cstmt.getObject(1);

		while (rs.next()){
			registro.addDefCampo("ID_PRESTAMO",rs.getString("ID_PRESTAMO"));
			registro.addDefCampo("ID_CLIENTE",rs.getString("ID_CLIENTE"));
			registro.addDefCampo("NOM_COMPLETO",rs.getString("NOM_COMPLETO"));
			registro.addDefCampo("IMP_DEUDA",rs.getString("IMP_DEUDA"));
			//Pregunta si se trata del presidende del grupo
			if (!sExcedente.equals("0")){
				if (rs.getString("ID_CLIENTE").equals(sIdCliente)){
					sImpDeuda = rs.getString("IMP_DEUDA");
					System.out.println("sImpDeuda************** del presidente: "+sImpDeuda);
					fImpDeuda = (Float.parseFloat(sImpDeuda));
					fExcedente = (Float.parseFloat(sExcedente));
					fImpDeuda = fImpDeuda + fExcedente;
					sImpDeuda = Float.toString(fImpDeuda);
	
					System.out.println("JUNTO CON EL EXCEDENTE RESULTA SER: "+fImpDeuda);
					registro.addDefCampo("IMP_DEUDA",sImpDeuda);
				}
			}
			
			lista.add(registro);
			registro = new Registro();
		}
	
		rs.close();
		cstmt.close();
		return lista;
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
			" 	IMP_VAR_PROPORCION \n"+
			" FROM SIM_PARAMETRO_GLOBAL \n"+
			" WHERE CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n";
	
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
}