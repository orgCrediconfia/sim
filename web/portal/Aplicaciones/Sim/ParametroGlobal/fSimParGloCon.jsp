<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimParametroGlobal">
	<Portal:PaginaNombre titulo="Par&aacute;metros Globales del Sistema" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimParametroGlobal'>
		<Portal:FormaSeparador nombre="Par&aacute;metros Globales"/>
		<Portal:FormaElemento etiqueta='Mínimo de integrantes en un grupo' control='etiqueta-controlreferencia' controlnombre='MinimoIntegrantes' controlvalor='${requestScope.registromin.campos["MINIMO_INTEGRANTES"]}' />
		<Portal:FormaElemento etiqueta='Máximo de integrantes en un grupo' control='etiqueta-controlreferencia' controlnombre='MaximoIntegrantes' controlvalor='${requestScope.registromax.campos["MAXIMO_INTEGRANTES"]}' />
		<tr>
			<th>Porcentaje m&iacute;nimo de fundadores del grupo</th>
			<td>
				<input type='text' name='PorcIntFundadores' size='5' maxlength='5' value='<c:out value='${requestScope.registro.campos["PORC_INT_FUNDADORES"]}'/>'> &#37
			</td>
		</tr>
		
		
		<Portal:FormaElemento etiqueta='Deuda m&iacute;nima' control='Texto' controlnombre='DeudaMinima' controlvalor='${requestScope.registro.campos["DEUDA_MINIMA"]}' controllongitud='5' controllongitudmax='5' editarinicializado='true' obligatorio='false' />
		<c:if test='${(requestScope.registro == null)}'>
			<Portal:FormaElemento etiqueta='Cr&eacute;ditos Simult&aacute;neos' control='checkbox' controlnombre='CreditosSimultaneos' controlvalor='${requestScope.registro.campos["CREDITOS_SIMULTANEOS"]}' />
		</c:if>
		<c:if test='${(requestScope.registro.campos["CREDITOS_SIMULTANEOS"] == "V")}'>
			<tr> 
			   	<th>Cr&eacute;ditos Simult&aacute;neos</th>
				<td> 
					<input type='checkbox' name='CreditosSimultaneos'/ checked >
				</td>
			</tr>
		</c:if>
		<c:if test='${(requestScope.registro.campos["CREDITOS_SIMULTANEOS"] == "F")}'>
			<tr> 
			   	<th>Cr&eacute;ditos Simult&aacute;neos</th>
				<td> 
					<input type='checkbox' name='CreditosSimultaneos' />
				</td>
			</tr>
		</c:if>
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>
