<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoFechaDesembolso">
	<Portal:PaginaNombre titulo="Fecha de Desembolso" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimPrestamoFechaDesembolso' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CvePrestamo' controllongitud='20' controllongitudmax='10' editarinicializado='true'/>
		<Portal:Calendario2 etiqueta='Fecha de entrega' contenedor='frmRegistro' controlnombre='FechaDesembolso' controlvalor='${requestScope.registro.campos["FECHA_ENTREGA"]}'  esfechasis='false'/>
		<Portal:FormaElemento etiqueta='Nombre del grupo o cliente' control='Texto' controlnombre='Nombre' controllongitud='30' controllongitudmax='100' editarinicializado='true'/>
	</Portal:Forma>
	<Portal:TablaForma nombre="Consulta" funcion="SimPrestamoFechaDesembolso" operacion="MO">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='150' valor='Clave del préstamo'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre del grupo o cliente'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Fecha de desembolso'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Día de pago'/>
		</Portal:TablaListaTitulos>
		
		
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["CVE_PRESTAMO"]}'/>
				<input type='hidden' name='AplicaA' value='<c:out value='${registro.campos["GRUPO"]}'/>'>
				<input type='hidden' name='IdPrestamo' value='<c:out value='${registro.campos["ID_PRESTAMO"]}'/>'>
				<c:if test='${(registro.campos["GRUPO"] == "GRUPO")}'>
					<Portal:Columna tipovalor='texto' ancho='250' valor=''>
						<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NOMBRE"]}' funcion='SimPrestamoConsultaMontoAutorizado' operacion='CR' parametros='IdPrestamo=${registro.campos["ID_PRESTAMO"]}'/>
					</Portal:Columna>
				</c:if>
				<c:if test='${(registro.campos["GRUPO"] != "GRUPO")}'>
					<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOMBRE"]}'/>
				</c:if>
				<c:if test='${(registro.campos["FECHA_ENTREGA"] == null)}'>
					<Portal:Columna tipovalor='texto' ancho='100' valor=''>
						<input class='fondocaptura' maxlength='15' name='FechaDesembolso' 
															 onBlur='ValidaFecha(this)' 
															 onKeyPress='if ( (event.keyCode < 47) || (event.keyCode > 57 ) ) event.returnValue = false;' 
															 size=15 value='<c:out value='${requestScope.registro.campos["FECHA_ENTREGA"]}'/>'>
					</Portal:Columna>
				</c:if>
				<c:if test='${(registro.campos["FECHA_ENTREGA"] != null)}'>
						<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["FECHA_ENTREGA"]}'/>
						<input type='hidden' name='FechaDesembolso' value='<c:out value='${registro.campos["FECHA_ENTREGA"]}'/>'>
				</c:if>
				 
				<c:if test='${(registro.campos["ID_PERIODICIDAD_PRODUCTO"] == "7") or (registro.campos["ID_PERIODICIDAD_PRODUCTO"] == "8")}'>
					<Portal:FormaElemento etiqueta='' control='selector-horizontal' controlnombre='DiaSemanaPago' controlvalor='${registro.campos["DIA_SEMANA_PAGO"]}' editarinicializado='true' obligatorio='false' campoclave="DIA_SEMANA_PAGO" campodescripcion="NOM_DIA_SEMANA_PAGO" datosselector='${requestScope.ListaDiaSemanaPago}'/>
				</c:if>
				
				<c:if test='${(registro.campos["ID_PERIODICIDAD_PRODUCTO"] != "7") && (registro.campos["ID_PERIODICIDAD_PRODUCTO"] != "8")}'>
					<Portal:Columna tipovalor='texto' ancho='100%' valor=''>
							<input type='hidden' name='DiaSemanaPago' value='ninguno'>
					</Portal:Columna>
				</c:if>	
				
			</Portal:TablaListaRenglon>
		</c:forEach>	
		
		<Portal:FormaBotones>
			<c:if test='${requestScope.ListaBusqueda != null}'>
				<Portal:Boton tipo='submit' etiqueta='Aceptar' />
			</c:if>
		</Portal:FormaBotones>				
	</Portal:TablaForma>	
</Portal:Pagina>

	
	
	



