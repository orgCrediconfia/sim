/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.catalogos.OperacionAlta; 
import com.rapidsist.portal.catalogos.OperacionBaja; 
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.Iterator;


/**
 * Administra los accesos a la base de datos para los préstamos.
 */
 
public class SimPrestamoEstatusGrupoDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionConsultaRegistro, OperacionConsultaTabla  {
	

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		sSql =  "SELECT \n"+
			"MIN(ID_ETAPA_PRESTAMO) ID_ESTATUS \n"+
			"FROM SIM_PRESTAMO \n"+
			"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"GROUP BY ID_GRUPO \n"+
			"ORDER BY ID_GRUPO \n";

		ejecutaSql();
		return getConsultaLista();
	}

	/**
	 * Obtiene un registro tomando como base la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql =  "SELECT \n"+
				"P.CVE_GPO_EMPRESA, \n"+
				"P.CVE_EMPRESA, \n"+
				"P.ID_PRESTAMO, \n"+
				"P.ID_CLIENTE, \n"+
				"C.NOM_COMPLETO, \n"+
				"P.ID_GRUPO, \n"+
				"G.NOM_GRUPO, \n"+
				"P.ID_SUCURSAL, \n"+
				"S.TASA_IVA, \n"+
				"P.FECHA_SOLICITUD, \n"+
				"P.FECHA_ENTREGA, \n"+
				"P.ID_COMITE, \n"+
				"P.ID_ETAPA_PRESTAMO, \n"+
				"CE.NOM_ESTATUS_PRESTAMO, \n"+
				"P.CVE_ASESOR_CREDITO, \n"+
				"NA.NOM_COMPLETO NOMBRE_ASESOR, \n"+
				"P.FECHA_ASESOR, \n"+
				"P.CVE_RECUPERADOR, \n"+
				"NR.NOM_COMPLETO NOMBRE_RECUPERADOR, \n"+
				"P.FECHA_RECUPERADOR, \n"+
				"P.ID_PRODUCTO, \n"+
				"PR.NOM_PRODUCTO, \n"+
				"P.APLICA_A, \n"+
				"PR.B_GARANTIA, \n"+
				"P.NUM_CICLO, \n"+
				"P.CVE_METODO, \n"+
				"P.ID_PERIODICIDAD_PRODUCTO, \n"+
				"P.PLAZO, \n"+
				"P.TIPO_TASA, \n"+
				"P.VALOR_TASA, \n"+
				"P.ID_PERIODICIDAD_TASA, \n"+
				"P.ID_TASA_REFERENCIA, \n"+
				"P.MONTO_MAXIMO, \n"+
				"PR.MONTO_MINIMO, \n"+
				"P.PORC_FLUJO_CAJA, \n"+
				"P.ID_FORMA_DISTRIBUCION \n"+
				"FROM \n"+
				"SIM_PRESTAMO P, \n"+
				"RS_GRAL_USUARIO UA, \n"+
				"RS_GRAL_PERSONA NA, \n"+
				"RS_GRAL_USUARIO UR, \n"+
				"RS_GRAL_PERSONA NR, \n"+
				"RS_GRAL_PERSONA C, \n"+
				"SIM_GRUPO G, \n"+
				"SIM_PRODUCTO PR, \n"+
				"SIM_CAT_SUCURSAL S, \n"+
				"SIM_CAT_ETAPA_PRESTAMO CE \n"+
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND P.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
				"AND UA.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND UA.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND UA.CVE_USUARIO (+)= P.CVE_ASESOR_CREDITO \n"+
				"AND NA.CVE_GPO_EMPRESA (+)= UA.CVE_GPO_EMPRESA \n"+
				"AND NA.CVE_EMPRESA (+)= UA.CVE_EMPRESA \n"+
				"AND NA.ID_PERSONA (+)= UA.ID_PERSONA \n"+
				"AND UR.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND UR.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND UR.CVE_USUARIO (+)= P.CVE_RECUPERADOR \n"+
				"AND NR.CVE_GPO_EMPRESA (+)= UR.CVE_GPO_EMPRESA \n"+
				"AND NR.CVE_EMPRESA (+)= UR.CVE_EMPRESA \n"+
				"AND NR.ID_PERSONA (+)= UR.ID_PERSONA \n"+
				"AND C.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND C.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND C.ID_PERSONA (+)= P.ID_CLIENTE \n"+
				"AND G.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND G.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND G.ID_GRUPO (+)= P.ID_GRUPO \n"+
				"AND PR.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND PR.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND PR.ID_PRODUCTO (+)= P.ID_PRODUCTO \n"+
				"AND S.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND S.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND S.ID_SUCURSAL (+)= P.ID_SUCURSAL \n"+
				"AND CE.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"AND CE.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"AND CE.ID_ETAPA_PRESTAMO (+)= P.ID_ETAPA_PRESTAMO \n";				
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		String sIdPrestamo = "";
		
		//OBTENEMOS EL SEQUENCE
		sSql = "SELECT SQ01_SIM_PRESTAMO.nextval as ID_PRESTAMO FROM DUAL";
		ejecutaSql();
		
		if (rs.next()){
			sIdPrestamo = rs.getString("ID_PRESTAMO");
		}

		sSql =  "INSERT INTO SIM_PRESTAMO ( "+
			"CVE_GPO_EMPRESA, \n" +
			"CVE_EMPRESA, \n" +
			"ID_PRESTAMO, \n" +
			"FECHA_SOLICITUD, \n" +
			"ID_CLIENTE, \n" +
			"ID_GRUPO, \n" +
			"ID_SUCURSAL, \n" +
			"ID_COMITE, \n" +
			"CVE_ASESOR_CREDITO, \n" +
			"FECHA_ASESOR_SIST) \n" +
			" VALUES (" +
			"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
			" " + sIdPrestamo +", \n" +
			"SYSDATE, \n" +
			"'" + (String)registro.getDefCampo("ID_PERSONA") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
			"'" + (String)registro.getDefCampo("ID_COMITE") + "', \n" +
			"'" + (String)registro.getDefCampo("CVE_ASESOR_CREDITO") + "', \n" +
			"SYSDATE) \n" ;

		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		resultadoCatalogo.Resultado.addDefCampo("ID_PRESTAMO", sIdPrestamo);
		
		
		return resultadoCatalogo;
	}

		/**
		 * Borra un registro.
		 * @param registro Llave primaria.
		 * @return Objeto que contiene el resultado de la ejecución de este método.
		 * @throws SQLException Si se genera un error al accesar la base de datos.
		 */
		public ResultadoCatalogo baja(Registro registro) throws SQLException{
			ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
			sSql =  " DELETE FROM SIM_PRESTAMO " +
				" WHERE ID_PRESTAMO		='" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
				" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
	
		return resultadoCatalogo;
	}

}