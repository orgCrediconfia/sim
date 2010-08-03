<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<Portal:Pagina funcion="SimProductoDocumentacionDisponible">

	<Portal:PaginaNombre titulo="Documentaci&oacute;n" subtitulo="Consulta de datos"/>
	
	<Portal:TablaForma nombre="Consulta de documentaci&oacute;n" funcion="SimProductoDocumentacion" operacion="AL" parametros='IdProducto=${param.IdProducto}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='180' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Archivo'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["ID_DOCUMENTO"]}' />		
				<input type="hidden" name="IdDocumento" value='<c:out value='${registro.campos["ID_DOCUMENTO"]}'/>'>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_DOCUMENTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='180' valor='${registro.campos["NOM_DOCUMENTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["ARCHIVO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Alta' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>
</Portal:Pagina>	