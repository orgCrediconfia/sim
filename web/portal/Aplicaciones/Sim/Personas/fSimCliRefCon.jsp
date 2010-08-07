<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimClienteReferencia">
	<Portal:PaginaNombre titulo="Referencias del Cliente" subtitulo="Consulta de datos"/>
	<Portal:TablaLista tipo="consulta" nombre="Consulta">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de verificaci&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Resultado de verificaci&oacute;n'/>		
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='250' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NOM_COMPLETO"]}' funcion='SimClienteReferencia' operacion='CR' parametros='IdPersona=${registro.campos["ID_PERSONA"]}&IdReferencia=${registro.campos["ID_REFERENCIA"]}&IdDomicilio=${registro.campos["ID_DOMICILIO"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_VERIFICACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_RESULTADO_VERIFICACION"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>	
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimClienteReferencia&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${param.IdPersona}'/>
		</Portal:FormaBotones>		
	</Portal:TablaLista>
</Portal:Pagina>	
