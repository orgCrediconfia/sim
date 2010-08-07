<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimClienteDocumentacion">
	<Portal:PaginaNombre titulo="Documentaci&oacute;n del Cliente" subtitulo="Consulta de datos"/>
	<Portal:TablaLista tipo="consulta" nombre="Consulta">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Documento'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Fecha de recibido'/>		
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_DOCUMENTO"]}' funcion='SimClienteDocumentacion' operacion='CR' parametros='IdDocumento=${registro.campos["ID_DOCUMENTO"]}&IdPersona=${registro.campos["ID_PERSONA"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_DOCUMENTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["FECHA_RECIBIDO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>	
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimClienteDocumentacion&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${param.IdPersona}'/>
		</Portal:FormaBotones>		
	</Portal:TablaLista>
</Portal:Pagina>	
