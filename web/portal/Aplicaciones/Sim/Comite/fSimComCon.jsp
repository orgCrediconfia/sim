<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimComite">
	<Portal:PaginaNombre titulo="Comit&eacute;" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimComite' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdComite' controllongitud='5' controllongitudmax='5'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomComite' controllongitud='20' controllongitudmax='20'/>
		<Portal:FormaElemento etiqueta='Sucursal' control='selector' controlnombre='IdSucursal' controlvalor='${requestScope.registro.campos["ID_SUCURSAL"]}' editarinicializado='true' obligatorio='false' campoclave="ID_SUCURSAL" campodescripcion="NOM_SUCURSAL" datosselector='${requestScope.ListaSucursal}'/>	
		<Portal:Calendario2 etiqueta='Fecha' contenedor='frmRegistro' controlnombre='Fecha' esfechasis='false'/>
	</Portal:Forma>
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/ProcesaCatalogo?Funcion=SimComite&OperacionCatalogo=IN&Filtro=Alta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Sucursal'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Fecha'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_COMITE"]}' funcion='SimComite' operacion='CR' parametros='IdComite=${registro.campos["ID_COMITE"]}'/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_COMITE"]}'/>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["NOM_SUCURSAL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["FECHA"]}'/>								
			</Portal:TablaListaRenglon>	
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
