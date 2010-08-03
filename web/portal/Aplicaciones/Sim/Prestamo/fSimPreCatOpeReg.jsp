<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimPrestamoCatalogoOperacion" precarga="SimPrestamoCatalogoOperacionConcepto">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de Operaciones" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimPrestamoCatalogoOperacion'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveOperacion' controlvalor='${requestScope.registro.campos["CVE_OPERACION"]}' controllongitud='10' controllongitudmax='10' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='DescCorta' controlvalor='${requestScope.registro.campos["DESC_CORTA"]}' controllongitud='20' controllongitudmax='20' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Descripci&oacute;n' control='Texto' controlnombre='DescLarga' controlvalor='${requestScope.registro.campos["DESC_LARGA"]}' controllongitud='60' controllongitudmax='60' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Afecta Saldo' control='SelectorAfecta' controlnombre='CveAfectaSaldo' controlvalor='${requestScope.registro.campos["CVE_AFECTA_SALDO"]}' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Afecta Cr&eacute;dito' control='SelectorAfecta' controlnombre='CveAfectaCredito' controlvalor='${requestScope.registro.campos["CVE_AFECTA_CREDITO"]}' editarinicializado='false' obligatorio='true' />
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
		
	<Portal:TablaLista maestrodetallefuncion="SimPrestamoCatalogoOperacionConcepto" tipo="alta" nombre="Conceptos relacionados a la Operaci&oacute;n" botontipo="url" url='/ProcesaCatalogo?Funcion=SimPrestamoCatalogoOperacionConcepto&OperacionCatalogo=IN&Filtro=Alta&CveOperacion=${registro.campos["CVE_OPERACION"]}'>
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Concepto'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaConcepto}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_CONCEPTO"]}' funcion='SimPrestamoCatalogoOperacionConcepto' operacion='CR' parametros='CveOperacion=${registro.campos["CVE_OPERACION"]}&CveConcepto=${registro.campos["CVE_CONCEPTO"]}'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["DESC_LARGA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>			
	</Portal:TablaLista>		
	
</Portal:Pagina>	