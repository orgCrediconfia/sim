<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimClienteNegocio">
	<Portal:PaginaNombre titulo="Negocios del Cliente" subtitulo="Consulta de datos"/>
	<Portal:TablaLista tipo="consulta" nombre="Consulta">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre del negocio'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Tipo del negocio'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Negocio'/>	
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_NEGOCIO"]}' funcion='SimClienteNegocio' operacion='CR' parametros='IdNegocio=${registro.campos["ID_NEGOCIO"]}&IdPersona=${registro.campos["ID_PERSONA"]}&IdDomicilio=${registro.campos["ID_DOMICILIO"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_NEGOCIO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='500' valor='${registro.campos["NOM_TIPO_NEGOCIO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["B_PRINCIPAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>	
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimClienteNegocio&OperacionCatalogo=IN&Filtro=Alta&IdPersona=${param.IdPersona}'/>
		</Portal:FormaBotones>		
	</Portal:TablaLista>
</Portal:Pagina>	
