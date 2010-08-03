<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimUsuarioRegional">
	<Portal:PaginaNombre titulo="Usuario Regional" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimUsuarioRegional'>
		<Portal:FormaSeparador nombre="Datos"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='CveUsuario' controlvalor='${param.CveUsuario}' controllongitud='2' controllongitudmax='2' editarinicializado='false' obligatorio='true'/>
		<input type="hidden" name="CveGpoEmpresa" value='<c:out value='${param.CveGpoEmpresa}'/>' />
		<Portal:FormaElemento etiqueta='Id Regional' control='selector' controlnombre='IdRegional' controlvalor='${requestScope.registro.campos["ID_REGIONAL"]}' editarinicializado='false' obligatorio='false' campoclave="ID_REGIONAL" campodescripcion="NOM_REGIONAL" datosselector='${requestScope.ListaRegional}'/>
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>	
