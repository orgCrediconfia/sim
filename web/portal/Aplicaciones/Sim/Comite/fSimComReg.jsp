<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimComite" precarga="SimComiteIntegrante SimComitePrestamo">

	<Portal:PaginaNombre titulo="Comit&eacute;" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimComite'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdComite' controlvalor='${requestScope.registro.campos["ID_COMITE"]}' controllongitud='12' controllongitudmax='10' editarinicializado='false'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomComite' controlvalor='${requestScope.registro.campos["NOM_COMITE"]}' controllongitud='50' controllongitudmax='50' editarinicializado='true' obligatorio='false'/>
		<Portal:FormaElemento etiqueta='Sucursal' control='selector' controlnombre='IdSucursal' controlvalor='${requestScope.registro.campos["ID_SUCURSAL"]}' editarinicializado='true' obligatorio='false' campoclave="ID_SUCURSAL" campodescripcion="NOM_SUCURSAL" datosselector='${requestScope.ListaSucursal}'/>	
		<Portal:Calendario2 etiqueta='Fecha' contenedor='frmRegistro' controlnombre='Fecha' controlvalor='${requestScope.registro.campos["FECHA"]}'  esfechasis='true'/>
		<Portal:FormaElemento etiqueta='Folio Acta' control='etiqueta-controloculto' controlnombre='IdFolioActa' controlvalor='${requestScope.registro.campos["ID_FOLIO_ACTA"]}' />
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
	
	<Portal:TablaForma maestrodetallefuncion="SimComiteIntegrante" nombre="Integrantes del Comité" funcion="SimComiteIntegrante" operacion="BA" parametros='EquIdEquipo=${registro.campos["EQU_ID_EQUIPO"]}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%'  valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaIntegrante}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["CVE_INTEGRANTE"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_COMPLETO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='catalogo' etiqueta='Alta' funcion='SimComiteIntegrante' operacion='IN' parametros='&IdComite=${requestScope.registro.campos["ID_COMITE"]}' />
		</Portal:FormaBotones>
	</Portal:TablaForma>
	
	<Portal:TablaForma maestrodetallefuncion="SimComitePrestamo" nombre="Préstamos asignados al Comité" funcion="SimComitePrestamo" operacion="BA" parametros='EquIdEquipo=${registro.campos["EQU_ID_EQUIPO"]}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Clave del préstamo'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre del grupo o cliente'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaPrestamoGrupo}">		
			<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='150' valor=''>
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_PRESTAMO"]}' funcion='SimComitePrestamoMontoAutorizado' operacion='CR' parametros='IdPrestamo=${registro.campos["ID_PRESTAMO"]}&IdComite=${registro.campos["ID_COMITE"]}&Prestamo=Grupo'/>
					</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOMBRE"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<c:forEach var="registro" items="${requestScope.ListaPrestamoIndividual}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='150' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_PRESTAMO"]}' funcion='SimComitePrestamoMontoAutorizado' operacion='CR' parametros='IdPrestamo=${registro.campos["ID_PRESTAMO"]}&IdComite=${registro.campos["ID_COMITE"]}&Prestamo=Individual'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOMBRE"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimComitePrestamo&OperacionCatalogo=CT&Filtro=Todos&IdComite=${registro.campos["ID_COMITE"]}&IdFolioActa=${registro.campos["ID_FOLIO_ACTA"]}&IdSucursal=${registro.campos["ID_SUCURSAL"]}'/>
		</Portal:FormaBotones>
	</Portal:TablaForma>
	
</Portal:Pagina>	