<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoGrupalPaginacion">
	<Portal:PaginaNombre titulo="Préstamos Grupales" subtitulo="Consulta de datos"/>
	
	<Portal:TablaForma nombre="Consulta de préstamos" funcion="SimPrestamoGrupal" operacion="IN" parametros="Filtro=Alta">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='50' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Producto'/>	
			<Portal:Columna tipovalor='texto' ancho='50' valor='Ciclo'/>	
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de solicitud'/>	
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de entrega'/>	
			<Portal:Columna tipovalor='texto' ancho='200' valor='Nombre del grupo'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Estatus del pr&eacute;stamo'/>	
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaPrestamoGrupalPaginacion}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='50' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_PRESTAMO_GRUPO"]}' funcion='SimPrestamoGrupal' operacion='CR' parametros='IdPrestamoGrupo=${registro.campos["ID_PRESTAMO_GRUPO"]}&NumCiclo=${registro.campos["NUM_CICLO"]}&IdGrupo=${registro.campos["ID_GRUPO"]}&IdProducto=${registro.campos["ID_PRODUCTO"]}&Filtro=Modificacion'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='80' valor='${registro.campos["ID_PRODUCTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='50' valor='${registro.campos["NUM_CICLO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_SOLICITUD"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_ENTREGA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["NOM_GRUPO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_ESTATUS_PRESTAMO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Alta' />
			<c:if test='${(param.Paginas != 0)}'>
				<input type="button" name="Siguiente" value="Siguiente" onclick="javascript:MM_goToURL('parent','/portal/ProcesaCatalogo?Funcion=SimPrestamoGrupalPaginacion&OperacionCatalogo=CT&Filtro=Todos&Paginas=<c:out value='${param.Paginas}'/>&Superior=<c:out value='${param.Superior}'/>&CvePrestamo=<c:out value='${param.CvePrestamo}'/>&IdProducto=<c:out value='${param.IdProducto}'/>&NumCiclo=<c:out value='${param.NumCiclo}'/>&FechaInicioSolicitud=<c:out value='${param.FechaInicioSolicitud}'/>&FechaFinEntrega=<c:out value='${param.FechaFinEntrega}'/>&NomGrupo=<c:out value='${param.NomGrupo}'/>&IdEstatusPrestamo=<c:out value='${param.IdEstatusPrestamo}'/>');" >
			</c:if>	
			<input type="button" name="Regresar" value="Regresar" onClick="MM_goToURL('parent','/portal/ProcesaCatalogo?Funcion=SimPrestamoGrupal&OperacionCatalogo=IN&Filtro=Inicio')" >
		</Portal:FormaBotones>		
	</Portal:TablaForma>	
</Portal:Pagina>
