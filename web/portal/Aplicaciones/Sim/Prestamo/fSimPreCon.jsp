<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamo">
	<Portal:PaginaNombre titulo="Préstamos" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimPrestamo' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdPrestamo' controllongitud='20' controllongitudmax='10' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='Producto' control='Texto' controlnombre='IdProducto' controllongitud='80' controllongitudmax='256' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='Ciclo' control='Texto' controlnombre='NumCiclo' controllongitud='30' controllongitudmax='100' editarinicializado='true'/>
		<Portal:Calendario2 etiqueta='Fecha de solicitud' contenedor='frmRegistro' controlnombre='FechaSolicitud' controlvalor='${requestScope.registro.campos["FECHA_SOLICITUD"]}'  esfechasis='false'/>
		<Portal:Calendario2 etiqueta='Fecha de entrega' contenedor='frmRegistro' controlnombre='FechaEntrega' controlvalor='${requestScope.registro.campos["FECHA_ENTREGA"]}'  esfechasis='false'/>
		<Portal:FormaElemento etiqueta='Nombre del grupo' control='Texto' controlnombre='NomGrupo' controllongitud='30' controllongitudmax='100' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='Nombre del acreditado' control='Texto' controlnombre='NomCompleto' controllongitud='30' controllongitudmax='100' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='Estatus del pr&eacute;stamo' control='selector' controlnombre='IdEstatusPrestamo' controlvalor='${requestScope.registro.campos["ID_ETAPA_PRESTAMO"]}' editarinicializado='true' obligatorio='false' campoclave="ID_ETAPA_PRESTAMO" campodescripcion="NOM_ESTATUS_PRESTAMO" datosselector='${requestScope.ListaVerificacionPrestamo}'/>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta de préstamos" botontipo="url" url='/ProcesaCatalogo?Funcion=SimPrestamo&OperacionCatalogo=IN&Filtro=Alta'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='50' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Producto'/>	
			<Portal:Columna tipovalor='texto' ancho='50' valor='Ciclo'/>	
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de solicitud'/>	
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de entrega'/>	
			<Portal:Columna tipovalor='texto' ancho='200' valor='Nombre del acreditado'/>	
			<Portal:Columna tipovalor='texto' ancho='100' valor='Estatus del pr&eacute;stamo grupal'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='50' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_PRESTAMO"]}' funcion='SimPrestamo' operacion='CR' parametros='IdPrestamo=${registro.campos["ID_PRESTAMO"]}&Alta=No'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='80' valor='${registro.campos["ID_PRODUCTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='50' valor='${registro.campos["NUM_CICLO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_SOLICITUD"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_ENTREGA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_ESTATUS_PRESTAMO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>		
	</Portal:TablaLista>	
</Portal:Pagina>
