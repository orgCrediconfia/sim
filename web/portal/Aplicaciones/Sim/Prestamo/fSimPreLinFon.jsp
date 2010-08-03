<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoLineaFondeo">
	<Portal:PaginaNombre titulo="Asignación de Líneas de fondeo" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimPrestamoLineaFondeo' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CvePrestamo' controllongitud='20' controllongitudmax='10' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='Nombre del grupo o cliente' control='Texto' controlnombre='Nombre' controllongitud='30' controllongitudmax='100' editarinicializado='true'/>
		<Portal:Calendario2 etiqueta='Fecha de entrega' contenedor='frmRegistro' controlnombre='FechaDesembolso' controlvalor='${requestScope.registro.campos["FECHA_ENTREGA"]}'  esfechasis='false'/>
		<Portal:FormaElemento etiqueta='# de Línea' control='selector' controlnombre='IdLinea' controlvalor='' editarinicializado='true' obligatorio='false' campoclave="NUM_LINEA" campodescripcion="NUM_LINEA" datosselector='${requestScope.ListaLinea}'/>
	</Portal:Forma>
	<Portal:TablaForma nombre="Consulta" funcion="SimPrestamoLineaFondeo" operacion="AL">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='150' valor='Clave del préstamo'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre del grupo o cliente'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Importe autorizado'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de desembolso'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Líneas de fondeo'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["CVE_PRESTAMO"]}'/>
					<input type='hidden' name='AplicaA' value='<c:out value='${registro.campos["APLICA_A"]}'/>'>
					<input type='hidden' name='IdPrestamo' value='<c:out value='${registro.campos["ID_PRESTAMO"]}'/>'>
				<c:if test='${(registro.campos["APLICA_A"] == "GRUPO")}'>
					<Portal:Columna tipovalor='texto' ancho='250' valor=''>
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NOMBRE"]}' funcion='SimPrestamoConsultaMontoAutorizado' operacion='CR' parametros='IdPrestamo=${registro.campos["ID_PRESTAMO"]}'/>
					</Portal:Columna>
				</c:if>
				<c:if test='${(registro.campos["APLICA_A"] != "GRUPO")}'>
					<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOMBRE"]}'/>
				</c:if>
				<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["IMPORTE_PRESTADO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["FECHA_ENTREGA"]}'/>
				<Portal:FormaElemento etiqueta='' control='selector-horizontal' controlnombre='IdLinea' controlvalor='${registro.campos["NUM_LINEA"]}' editarinicializado='true' obligatorio='false' campoclave="NUM_LINEA" campodescripcion="NUM_LINEA" datosselector='${requestScope.ListaLinea}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<c:if test='${requestScope.ListaBusqueda != null}'>
				<Portal:Boton tipo='submit' etiqueta='Aceptar' />
			</c:if>
		</Portal:FormaBotones>				
	</Portal:TablaForma>	
</Portal:Pagina>
