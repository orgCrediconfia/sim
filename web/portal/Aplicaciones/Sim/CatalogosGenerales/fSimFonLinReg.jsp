<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimFondeadorLinea">
	<Portal:PaginaNombre titulo="Catálogo de Fondeador" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimFondeadorLinea' parametros='CveFondeador=${param.CveFondeador}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<Portal:FormaElemento etiqueta='# Línea' control='Texto' controlnombre='NumLinea' controlvalor='${requestScope.registro.campos["NUM_LINEA"]}' controllongitud='75' controllongitudmax='100' editarinicializado='false' obligatorio='true' />
		<input type="hidden" name="IdLinea" value='<c:out value='${requestScope.registro.campos["ID_LINEA"]}'/>' />
		
		<Portal:FormaElemento etiqueta='Monto' control='Texto' controlnombre='Monto' controlvalor='${requestScope.registro.campos["MONTO"]}' controllongitud='75' controllongitudmax='100' editarinicializado='false' obligatorio='true' />
		<c:if test='${(requestScope.registro != null)}'>
			<Portal:FormaElemento etiqueta='Monto disponible' control='Texto' controlnombre='MontoDisponible' controlvalor='${requestScope.registro.campos["MONTO_DISPONIBLE"]}' controllongitud='75' controllongitudmax='100' editarinicializado='false' obligatorio='true' />
		</c:if>
		
		<tr>
			<th>Tasa</th>
			<td>
				<input type='text' name='Tasa' size='9' maxlength='9' value='<c:out value='${requestScope.registro.campos["TASA"]}'/>' onkeydown="CantidadesMonetarias();"> &#37
			</td>
		</tr>
		<Portal:Calendario2 etiqueta='Fecha de inicio' contenedor='frmRegistro' controlnombre='FechaInicio' controlvalor='${requestScope.registro.campos["FECHA_INICIO"]}'  esfechasis='true'/>
		<Portal:Calendario2 etiqueta='Fecha Vigencia' contenedor='frmRegistro' controlnombre='FechaVigencia' controlvalor='${requestScope.registro.campos["FECHA_VIGENCIA"]}'  esfechasis='true'/>
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
</Portal:Pagina>	
