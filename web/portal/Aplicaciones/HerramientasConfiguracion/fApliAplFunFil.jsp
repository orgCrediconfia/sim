<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionAplicacionFuncion">
	<Portal:PaginaNombre titulo="Funciones por aplicaci&oacute;n" subtitulo="Asignaci&oacute;n de funciones para una aplicaci&oacute;n"/>
	
	<Portal:Forma tipo='busqueda' funcion='HerramientasConfiguracionAplicacionFuncion' operacion='CT' filtro='FuncionesDisponibles'>
		<Portal:FormaElemento etiqueta='Clave aplicaci&oacute;n' control='Texto' controlnombre='CveAplicacion' controlvalor='${param.CveAplicacion}' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Funci&oacute;n' control='Texto' controlnombre='CveFuncion' controlvalor='${param.CveFuncion}' controllongitud='50' controllongitudmax='80' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomFuncion' controlvalor='${param.NomFuncion}' controllongitud='30' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
	</Portal:Forma>

	<Portal:TablaForma nombre="Funciones disponibles para asignarlas a la aplicaci&oacute;n" funcion="HerramientasConfiguracionAplicacionFuncion" operacion="AL" parametros='&CveAplicacion=${param.CveAplicacion}'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='70' valor='Seleccione'/>		
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='50%' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='50%' valor='URL funci&oacuten'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='70' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["CVE_FUNCION"]}' />
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CVE_FUNCION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='50%' valor='${registro.campos["NOM_FUNCION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='50%' valor='${registro.campos["URL_FUNCION"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Alta de funciones' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>

</Portal:Pagina>