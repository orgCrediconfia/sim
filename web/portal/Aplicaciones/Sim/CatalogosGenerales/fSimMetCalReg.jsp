<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoMetodoCalculo">
	<Portal:PaginaNombre titulo="Catálogo de Métodos de Cálculo" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoMetodoCalculo'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveMetodo' controlvalor='${param.CveMetodo}' controllongitud='2' controllongitudmax='2' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomMetodo' controlvalor='${requestScope.registro.campos["NOM_METODO"]}' controllongitud='50' controllongitudmax='80' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Descripci&oacute;n' control='Texto' controlnombre='Descripcion' controlvalor='${requestScope.registro.campos["DESCRIPCION"]}' controllongitud='50' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
</Portal:Pagina>
