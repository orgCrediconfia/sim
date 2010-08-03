<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoConsultaBitacoraActividadEstatus">
	<Portal:PaginaNombre titulo="Bitácora de etapas" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimPrestamoConsultaBitacoraActividadEstatus' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CvePrestamo' controllongitud='19' controllongitudmax='18' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='Nombre del cliente o grupo' control='Texto' controlnombre='Nombre' controllongitud='30' controllongitudmax='100' editarinicializado='true'/>
		
	</Portal:Forma>
	<Portal:TablaLista tipo="consulta" nombre="Consulta de préstamos" botontipo="url" url='/ProcesaCatalogo?Funcion=SimPrestamo&OperacionCatalogo=IN&Filtro=Alta'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>	
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_PRESTAMO"]}' funcion='SimPrestamoConsultaBitacoraActividadEstatus' operacion='CR' parametros='IdPrestamo=${registro.campos["ID_PRESTAMO"]}&AplicaA=${registro.campos["APLICA_A"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOMBRE"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>		
	</Portal:TablaLista>	
	
</Portal:Pagina>
