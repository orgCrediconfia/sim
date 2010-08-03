<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionTablaDependienteCampos">
	<Portal:PaginaNombre titulo="Tabla dependiente" subtitulo="Campos relacionados" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionTablaDependienteCampos'>
		<Portal:FormaSeparador nombre="Datos"/>
		<Portal:FormaElemento etiqueta='Tabla' control='etiqueta-controloculto' controlnombre='CveTabla' controlvalor='${param.CveTabla}'/>
		<Portal:FormaElemento etiqueta='Tabla dependiente' control='etiqueta-controloculto' controlnombre='CveTablaDependiente' controlvalor='${param.CveTablaDependiente}'/>
		<Portal:FormaElemento etiqueta='Campo tabla' control='Texto' controlnombre='CampoLlave' controlvalor='${param.CampoLlave}' editarinicializado='false'/>
		<Portal:FormaElemento etiqueta='Campo tabla dependiente' control='Texto' controlnombre='CampoReferencia' controlvalor='${param.CampoReferencia}' editarinicializado='false'/>
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/> 	
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>
