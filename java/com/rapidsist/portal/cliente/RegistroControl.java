/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import java.util.LinkedList;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;

/**
 * Esta clase tiene por objetivo regresar una respuesta única de todas las clases
 * controladoras (CON) al servlet ProcesaCatalogoS, la cual contiene el resultado sobre
 * la operación realizada en el catálogo, así como la página a donde se debe
 * transferir el control.
 */
public class RegistroControl {
	/**
	 * Página a donde se desea transferir el control.
	 */
	public String sPagina ="";
	/**
	 * Estructura que agrupa el resultado de la operación realizada sobre el catálogo.
	 */
	public LinkedList lista  = null;

	/**
	 * Objeto que contiene la respuesta de la clase CON. Esta respuesta puede agrupar
	 * diferentes objetos LinkedList gracia a que este objeto está basado en un HashMap.
	 */
	public Registro respuesta = new Registro();

	/**
	 * Respuesta del EJB de catálogos.
	 */
	public ResultadoCatalogo resultadoCatalogo = null;
}