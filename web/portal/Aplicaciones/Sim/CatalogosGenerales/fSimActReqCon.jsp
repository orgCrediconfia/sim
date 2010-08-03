<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimCatalogoActividadRequisito">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de actividades o requisitos" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoActividadRequisito' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdActividadRequisito' controllongitud='5' controllongitudmax='5' validadato='cantidades'/>
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
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/Aplicaciones/Sim/CatalogosGenerales/fSimActReqReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Aplica a'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Tipo'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Aplicable en ciclo'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_ACTIVIDAD_REQUISITO"]}' funcion='SimCatalogoActividadRequisito' operacion='CR'parametros='IdActividadRequisito=${registro.campos["ID_ACTIVIDAD_REQUISITO"]}' parametrosregreso='\'${registro.campos["ID_ACTIVIDAD_REQUISITO"]}\',\'${registro.campos["NOM_ACTIVIDAD_REQUISITO"]}\''/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_ACTIVIDAD_REQUISITO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["APLICA_A"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["TIPO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["APLICA_CICLO"]}'/>
			</Portal:TablaListaRenglon>	
		</c:forEach>
	</Portal:TablaLista>
	
	<script>
		function RegresaDatos(IdActividadRequisito,NomActividadRequisito){
			var padre = window.opener;
			padre.document.frmRegistro.IdActividadRequisito.value = IdActividadRequisito;
			padre.document.getElementById('NomActividadRequisito').innerHTML = NomActividadRequisito;
			
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
	</script>
	
</Portal:Pagina>
