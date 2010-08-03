/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

/**
 * Proporciona diferentes estilos para pintar un divisor de página en una hoja HTML.
 */
public class Divisor {

	/**
	 * Divisor de página tipo 1.
	 * @param sRutaContexto Dirección Url de la aplicacion WEB.
	 * @param sTitulo Título que se muestra en el divisor.
	 * @return Código HTML del divisor de página.
	 */
	public static String getDivisor(String sRutaContexto, String sTitulo){
		String sSalida = "<table cellSpacing=0 cellPadding=0 border=0> \n" +
						 " <tr> \n" +
						 "	 <td width='10' nowrap>&nbsp;</td> \n" +
						 "	 <td> \n" +
						 "	 <table class=normal cellSpacing=0 cellPadding=0 width='400' border=0> \n"+
						 "		<tr> \n" +
						 "			<td background='" + sRutaContexto + "/comun/img/set1/bg_title.gif' vAlign=top colspan=3>" +
						 "				<table class=SeparadorDatosNormal  width='100%' border=0 cellspacing=1 cellpadding=2>" +
						 "					<tr>" +
						 "						<td class=SeparadorDatos>" + sTitulo + "</td>" +
						 "					</tr>" +
						 "				</table>" +
						 "			</td>" +
						 "		</tr>" +
						 "	 </table>" +
						 "	 </td>" +
						 " </tr>" +
						 "</table>" +
						 "<br>";
		return sSalida;
	}

	/**
	 * Divisor de página tipo 2.
	 * @param sTitulo Título que se muestra en el divisor.
	 * @return Código HTML del divisor de página.
	 */
	public static String getDivisor2(String sTitulo){
		String sSalida = "      <tr><td colspan='2'><h3>" + sTitulo + "</h3></td></tr>";
		return sSalida;
	}

	/**
	 * Divisor de página tipo 3.
	 * @param sTitulo Título que se muestra en el divisor.
	 * @return Código HTML del divisor de página.
	 */
	public static String getDivisor3(String sTitulo){
		String sSalida = "\t\t\t\t\t\t\t\t\t<tr><td colspan='2'><div class='SeccionBotones'>" + sTitulo + "</div></td></tr>";
		return sSalida;
	}

}