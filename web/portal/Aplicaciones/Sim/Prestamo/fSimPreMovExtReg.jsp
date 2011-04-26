<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimPrestamoMovimientoExtraordinario">
	<Portal:PaginaNombre titulo="Movimiento Extraordinario" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimPrestamoMovimientoExtraordinario' >
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='CvePrestamo' controlvalor='${requestScope.registro.campos["CVE_PRESTAMO"]}' editarinicializado='false' />
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='Nombre' controlvalor='${requestScope.registro.campos["NOMBRE"]}' editarinicializado='false' />
		<Portal:FormaElemento etiqueta='Producto' control='Texto' controlnombre='Producto' controlvalor='${requestScope.registro.campos["NOM_PRODUCTO"]}' editarinicializado='false' />
		<Portal:FormaElemento etiqueta='Ciclo' control='Texto' controlnombre='NumCiclo' controlvalor='${requestScope.registro.campos["NUM_CICLO"]}' editarinicializado='false' />
		<Portal:FormaElemento etiqueta='Movimiento extraordinario' control='selector' controlnombre='CveOperacion' controlvalor='${requestScope.registro.campos["CVE_OPERACION"]}' editarinicializado='true' obligatorio='true' campoclave="CVE_OPERACION" campodescripcion="DESC_LARGA" datosselector='${requestScope.ListaOperaciones}' evento="onchange=fPantalla();" />
		<input type="hidden" name="IdPrestamo" value='<c:out value='${param.IdPrestamo}'/>' />
		<input type="hidden" name="Consulta" value='<c:out value='${param.Consulta}'/>' />
		<Portal:FormaBotones>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<c:if test='${(param.Consulta == "Pantalla")}'>	
		<Portal:TablaLista tipo="consulta" nombre="Saldos">
			<Portal:TablaListaTitulos> 
			    <Portal:Columna tipovalor='texto' ancho='50' valor='Clave del cliente'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Nombre'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Monto préstado'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Capital'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Inter&eacute;s'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='I.V.A.'/>
			    <Portal:Columna tipovalor='texto' ancho='100' valor='Recargos'/>
			    <c:forEach var="registro" items="${requestScope.ListaTituloAccesorio}">
					<th  width='100' align='left'  nowrap  ><c:out value='${registro.campos["NOM_ACCESORIO"]}'/>
					</th>
			    </c:forEach>
			    <Portal:Columna tipovalor='texto' ancho='100%' valor='Total'/>  
			    
			</Portal:TablaListaTitulos>
			<c:forEach var="registro" items="${requestScope.ListaSaldos}">
				<Portal:TablaListaRenglon>
					<Portal:Columna tipovalor='texto' ancho='50' valor='${registro.campos["ID_PERSONA"]}'/>
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_COMPLETO"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["MONTO_AUTORIZADO"]}'/>
					<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["IMP_CAPITAL_AMORT"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["INTERES"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["IVA"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["RECARGO"]}'/>
					<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["IMP_ACCESORIO"]}'/>  
					<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["TOTAL"]}'/>  
				</Portal:TablaListaRenglon>
			</c:forEach>
		</Portal:TablaLista>
	</c:if>
	
	<script>
		function fPantalla(){
			document.frmRegistro.action="ProcesaCatalogo?Funcion=SimPrestamoMovimientoExtraordinario&OperacionCatalogo=CR&Consulta=Pantalla&IdPrestamo="+document.frmRegistro.IdPrestamo.value+"&MovimientoExtraordinario="+document.frmRegistro.CveOperacion.value;
			document.frmRegistro.submit();
		}
	</script>
</Portal:Pagina>


	