<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimCatalogoDocumentacion">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de Documentaci&oacute;n" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoDocumentacion' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdDocumento' controllongitud='5' controllongitudmax='5' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomDocumento' controllongitud='20' controllongitudmax='20'/>
	</Portal:Forma>
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/ProcesaCatalogo?Funcion=SimCatalogoDocumentacion&OperacionCatalogo=IN&Filtro=Alta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Tipo de documentaci&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Reporte'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_DOCUMENTO"]}' funcion='SimCatalogoDocumentacion' operacion='CR'parametros='IdDocumento=${registro.campos["ID_DOCUMENTO"]}' parametrosregreso='\'${registro.campos["ID_DOCUMENTO"]}\',\'${registro.campos["NOM_DOCUMENTO"]}\''/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_DOCUMENTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_TIPO_DOCUMENTACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_REPORTE"]}'/>
			</Portal:TablaListaRenglon>	
		</c:forEach>
	</Portal:TablaLista>
	
	<script>
		function RegresaDatos(IdDocumento,NomDocumento){
			var padre = window.opener;
			padre.document.frmRegistro.IdDocumento.value = IdDocumento;
			padre.document.getElementById('NomDocumento').innerHTML = NomDocumento;
			
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
	</script>
	
</Portal:Pagina>
