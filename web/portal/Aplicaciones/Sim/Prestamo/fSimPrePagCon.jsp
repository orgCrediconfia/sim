<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoPaginacion">
	<Portal:PaginaNombre titulo="Préstamos" subtitulo="Consulta de datos"/>
	
	<Portal:TablaForma nombre="Consulta de préstamos" funcion="SimPrestamo" operacion="IN" parametros="Filtro=Alta">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Producto'/>	
			<Portal:Columna tipovalor='texto' ancho='50' valor='Ciclo'/>	
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de solicitud'/>	
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de entrega'/>	
			<Portal:Columna tipovalor='texto' ancho='200' valor='Nombre del acreditado'/>	
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Estatus del pr&eacute;stamo'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaPrestamoPaginacion}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='50' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_PRESTAMO"]}' funcion='SimPrestamo' operacion='CR' parametros='IdPrestamo=${registro.campos["ID_PRESTAMO"]}&Alta=No'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_PRODUCTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='50' valor='${registro.campos["NUM_CICLO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_SOLICITUD"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_ENTREGA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_ESTATUS_PRESTAMO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Alta' />
			<c:if test='${(param.Paginas != 0)}'>
				<input type="button" name="Siguiente" value="Siguiente" onclick="javascript:MM_goToURL('parent','/portal/ProcesaCatalogo?Funcion=SimPrestamoPaginacion&OperacionCatalogo=CT&Filtro=Todos&Paginas=<c:out value='${param.Paginas}'/>&Superior=<c:out value='${param.Superior}'/>&CvePrestamo=<c:out value='${param.CvePrestamo}'/>&IdProducto=<c:out value='${param.IdProducto}'/>&NumCiclo=<c:out value='${param.NumCiclo}'/>&FechaInicioSolicitud=<c:out value='${param.FechaInicioSolicitud}'/>&FechaFinEntrega=<c:out value='${param.FechaFinEntrega}'/>&NomCompleto=<c:out value='${param.NomCompleto}'/>&IdEstatusPrestamo=<c:out value='${param.IdEstatusPrestamo}'/>');" >
			</c:if>	
			<input type="button" name="Regresar" value="Regresar" onClick="MM_goToURL('parent','/portal/ProcesaCatalogo?Funcion=SimPrestamo&OperacionCatalogo=IN&Filtro=Inicio')" >
		</Portal:FormaBotones>		
	</Portal:TablaForma>	
</Portal:Pagina>
