/**
 * Sistema de administración de portales.
 *
 */

package com.rapidsist.portal.cliente;

/**
 * Interface que establece el tipo de operaciones que se pueden realizar sobre las clases
 * controladoras (CON).
 */
public interface CatalogoControl{
	public final int CON_ALTA = 1;
	public final int CON_BAJA = 2;
	public final int CON_MODIFICACION = 3;
	public final int CON_CONSULTA_REGISTRO = 4;
	public final int CON_CONSULTA_TABLA = 5;
	public final int CON_INICIALIZACION = 6;
	public final int CON_INICIALIZACION_ALTA = 7;
}