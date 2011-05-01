/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

/**
 * Esta clase contiene los mensajes que son generados en los componentes de infraestructura
 * del portal.
 */
public class MensajeSistema {

	/**
	 * Obtiene el mensaje del sistema en base a su clave.
	 * @param iMensaje Clave del mensaje.
	 * @return Descripción del mensaje.
	 */
	static public String getMensajeSistema(int iMensaje){
		String sMensaje ="";

		//OBTIENE EL MENSAJE EN BASE A LA CLAVE
		switch (iMensaje){

			//MENSAJES DE ADMINISTRACION DE CATALOGOS
			case 100:
				sMensaje = "No tiene permisos de acceso a la función solicitada, o bien la función ha sido bloqueada temporalmente";
				break;
			case 101:
				sMensaje = "No se definió la operación a realizar sobre el catálogo (Alta, Baja, Modifiación, etc.)";
				break;
			case 102:
				sMensaje = "La operación a realizar sobre el catálogo no es válida.";
				break;
			case 103:
				sMensaje = "No se especificó la función que deberá utilizar el procesador de catálogos.";
				break;
			case 104:
				sMensaje = "No se envió el parametro 'Filtro' a la operación de consulta de registros (CT).";
				break;
			case 105:
				sMensaje = "No se especificó la página a donde se debe enviar el control despues de almacenar el evento del usuario";
				break;
			case 106:
				sMensaje = "No se especificó la función que deberá utilizar el procesador de reportes.";
				break;
			case 107:
				sMensaje = "No se encontró la función solicitada en la base de datos.";
				break;
			case 108:
				sMensaje = "No se definio el tipo de reporte que se deberá imprimir, p.e. Pdf, Excel, etc.";
				break;
			case 109:
				sMensaje = "No existe información suficiente para generar el Reporte.";
				break;
			case 110:
				sMensaje = "Se produjo un error en la clase controladora del reporte.";
				break;
		}
		return sMensaje;
	}
}