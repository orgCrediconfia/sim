<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionTabla">
	<Portal:PaginaNombre titulo="Asignaci&oacute;n de tablas" subtitulo="Consulta de datos"/>
	<Portal:TablaForma nombre="Tablas disponibles para ser asignadas" funcion='<%=request.getParameter("Funcion")%>' operacion="AL" parametros='CveTabla=${param.CveTabla}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='70' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Nombre en BD'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Descripci&oacute;n'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='70' valor='' control='checkbox' controlnombre='Tabla${registro.campos["CVE_TABLA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CVE_TABLA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_TABLA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["DESC_TABLA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' nombre='btnAlta' etiqueta='Alta de tablas' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>
</Portal:Pagina>