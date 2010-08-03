<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimProducto">
	<Portal:PaginaNombre titulo="Producto" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimProducto' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdProducto' controllongitud='5' controllongitudmax='5'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomProducto' controllongitud='20' controllongitudmax='20'/>
		<Portal:Calendario2 etiqueta='Inicio de activaci&oacute;n' contenedor='frmRegistro' controlnombre='FechaInicioActivacion' esfechasis='false'/>
		<Portal:Calendario2 etiqueta='Fin de activaci&oacute;n' contenedor='frmRegistro' controlnombre='FechaFinActivacion' esfechasis='false'/>
	</Portal:Forma>
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/ProcesaCatalogo?Funcion=SimProducto&OperacionCatalogo=IN&Filtro=Alta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Aplica A'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Inicio de activaci&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Fin de activaci&oacute;n'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_PRODUCTO"]}' funcion='SimProducto' operacion='CR' parametros='IdProducto=${registro.campos["ID_PRODUCTO"]}'/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["NOM_PRODUCTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='80' valor='${registro.campos["APLICA_A"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_INICIO_ACTIVACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["FECHA_FIN_ACTIVACION"]}'/>								
			</Portal:TablaListaRenglon>	
		</c:forEach>
		
	</Portal:TablaLista>
</Portal:Pagina>
