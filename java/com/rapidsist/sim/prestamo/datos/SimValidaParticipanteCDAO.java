/**
 * Sistema de administraci�n de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para obtenr el identificador del
 * pr�stamo en base a su clave.
 */

public class SimValidaParticipanteCDAO extends Conexion2 implements
		OperacionConsultaTabla, OperacionConsultaRegistro {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de b�squeda.
	 * 
	 * @param parametros
	 *            Par�metros que se le env�an a la consulta para obtener el
	 *            conjunto de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException
	 *             Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException {

		sSql = "SELECT\n"
				+ "PP.ID_PRESTAMO,\n"
				+ "PP.CVE_TIPO_PERSONA,\n"
				+ "PP.ID_PERSONA\n"
				+ "FROM\n"
				+ "SIM_PRESTAMO_PARTICIPANTE PP\n"
				+ "WHERE PP.CVE_GPO_EMPRESA     = 'SIM'\n"
				+ "AND PP.CVE_EMPRESA         = 'CREDICONFIA'\n"
				+ "AND PP.ID_PRESTAMO         = 2130\n"
				+ "AND (PP.CVE_TIPO_PERSONA = 'GARANTE' OR PP.CVE_TIPO_PERSONA = 'OBLIGADO' OR PP.CVE_TIPO_PERSONA = 'OBLIGADO 2')\n";

		System.out.println("sSql" + sSql);
		ejecutaSql();
		return getConsultaLista();
	}

	public Registro getRegistro(Registro parametros) throws SQLException {

		// EJEMPLO PARA GENERAR UN COUNT
		// ESTE METODO ES LLAMADO DE LA REP SimReporteAnexoAREP
		// sSql =
		// "SELECT COUNT(CVE_ESTADO)TOTAL_ESTADOS FROM RS_GRAL_ESTADO \n";

		ejecutaSql();
		return this.getConsultaRegistro();
	}

}