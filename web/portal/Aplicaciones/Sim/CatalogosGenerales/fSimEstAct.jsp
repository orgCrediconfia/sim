<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimCatalogoEstatusActividad">
	<Portal:PaginaNombre titulo="Asignar actividades o requisitos al préstamo" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoEstatusActividad' operacion='CT' filtro='Todos' parametros='IdEstatusPrestamo=${param.IdEstatusPrestamo}'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdActividadRequisito' controllongitud='5' controllongitudmax='5'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomActividadRequisito' controllongitud='30' controllongitudmax='30'/>
		<tr>
			<th>Aplica a</th>
			<td>
				<select name='AplicaA' size='1'  >
					<option value='null'></option>
					<option value='Individual'   >Individual</option>
					<option value='Grupo'   >Grupo</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>Tipo</th>
			<td>
				<select name='Tipo' size='1'  >
					<option value='null'></option>
					<option value='Actividad'   >Actividad</option>
					<option value='Requisito'   >Requisito</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>Aplicable en ciclo</th>
			<td>
				<select name='AplicaCiclo' size='1'  >
					<option value='null'></option>
					<option value='Todos'   >Todos</option>
					<option value='Par'   >Par</option>
					<option value='Impar'   >Impar</option>
				</select>
			</td>
		</tr>
		
	</Portal:Forma>
	
	<Portal:TablaForma nombre="Consulta de Actividades o Requisito" funcion="SimCatalogoEtapaActividad" operacion="AL" parametros='IdEstatusPrestamo=${param.IdEstatusPrestamo}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Aplica a'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Tipo'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Aplicable en ciclo'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["ID_ACTIVIDAD_REQUISITO"]}' />		
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["ID_ACTIVIDAD_REQUISITO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_ACTIVIDAD_REQUISITO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["APLICA_A"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["TIPO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["APLICA_CICLO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Alta' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>
	
</Portal:Pagina>
