<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="HerramientasConfiguracionTablaDependienteCampos" >
	<Portal:PaginaNombre titulo="Tabla dependiente" subtitulo="Consulta"/>

		<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionTablaDependienteCampos'>
				<Portal:FormaSeparador nombre="Campos dependientes"/>
				<Portal:FormaElemento etiqueta='Tabla' control='etiqueta-controloculto' controlnombre='CveTabla' controlvalor='${param.CveTabla}'/>
				<Portal:FormaElemento etiqueta='Tabla dependiente' control='etiqueta-controloculto' controlnombre='CveTablaDependiente' controlvalor='${param.CveTablaDependiente}'/>
				<Portal:FormaBotones>
				</Portal:FormaBotones>
		</Portal:Forma>
		
		<Portal:TablaLista tipo="alta" nombre=" " botontipo="url" url='/ProcesaCatalogo?Funcion=HerramientasConfiguracionTablaDependienteCampos&OperacionCatalogo=IN&CveTabla=${param.CveTabla}&CveTablaDependiente=${param.CveTablaDependiente}'>
		<Portal:TablaListaTitulos> 
		    <Portal:Columna tipovalor='texto' ancho='80' valor='Campo tabla'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Campo tabla dependiente'/>	
		</Portal:TablaListaTitulos>
		
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='80' valor='' controlnombre='CampoLlave'>					
				<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CAMPO_LLAVE"]}' funcion='HerramientasConfiguracionTablaDependienteCampos' operacion='CR' parametros='CveTabla=${param.CveTabla}&CveTablaDependiente=${param.CveTablaDependiente}&CampoLlave=${registro.campos["CAMPO_LLAVE"]}&CampoReferencia=${registro.campos["CAMPO_REFERENCIA"]}'/> 
				</Portal:Columna>																													 
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["CAMPO_REFERENCIA"]}' controlnombre='CampoReferencia'/>
			</Portal:TablaListaRenglon>
		</c:forEach>	
		</Portal:TablaLista>
		
</Portal:Pagina>