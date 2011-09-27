<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimGenerarTablaAmortizacion">
	<Portal:PaginaNombre titulo="Tabla de Amortizaci&oacute;n" subtitulo="Consulta de datos"/>
	
	<%
	String sAccesorios = ((request.getParameter("Accesorio")!=null) ? request.getParameter("Accesorio"):"0");
	int iNumAccesorios = (int)Float.valueOf(sAccesorios).floatValue();
	%>
	
	<Portal:Forma tipo='catalogo' funcion='SimGenerarTablaAmortizacion'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='CvePrestamo' controlvalor='${param.CvePrestamo}' controllongitud='20' controllongitudmax='18'/>
		<Portal:FormaElemento etiqueta='Cliente o grupo' control='Texto' controlnombre='Nombre' controlvalor='${param.NomCompleto}' controllongitud='30' controllongitudmax='100'/>
		<Portal:FormaBotones>
			<input type='button' name='Aceptar' value='Búsqueda' onClick='fGenerarTablaAmortizacion()'/>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<c:if test='${(requestScope.registro != null)}'>
		<Portal:TablaLista tipo="consulta" nombre="">
		
			<c:if test='${(registro.campos["NOM_GRUPO"] != null)}'>
				<Portal:FormaElemento etiqueta='Nombre' control='etiqueta-controlreferencia' controlnombre='NomCompleto' controlvalor='${requestScope.registro.campos["NOM_GRUPO"]}' editarinicializado='false'/>
			</c:if>
			<c:if test='${(registro.campos["NOM_COMPLETO"] != null)}'>
				<Portal:FormaElemento etiqueta='Nombre' control='etiqueta-controlreferencia' controlnombre='NomCompleto' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' editarinicializado='false'/>
			</c:if>
		
			<Portal:FormaElemento etiqueta='Sucursal' control='selector' controlnombre='IdSucursal' controlvalor='${registro.campos["ID_SUCURSAL"]}' editarinicializado='false' obligatorio='false' campoclave="ID_SUCURSAL" campodescripcion="NOM_SUCURSAL" datosselector='${requestScope.ListaSucursal}'/>	
			<Portal:FormaElemento etiqueta='IVA' control='etiqueta-controlreferencia' controlnombre='TasaIva' controlvalor='${requestScope.registro.campos["TASA_IVA"]} %' editarinicializado='false'/>
			<Portal:FormaElemento etiqueta='Fecha de Desembolso' control='etiqueta-controlreferencia' controlnombre='FechaEntrega' controlvalor='${requestScope.registro.campos["FECHA_ENTREGA"]}' editarinicializado='false'/>
			<c:if test='${(registro.campos["ID_PERIODICIDAD_PRODUCTO"] == "7") or (registro.campos["ID_PERIODICIDAD_PRODUCTO"] == "8") or (registro.campos["FECHA_REAL"] != "")}'>
				<Portal:FormaElemento etiqueta='Fecha Real' control='etiqueta-controlreferencia' controlnombre='FechaEntrega' controlvalor='${requestScope.registro.campos["FECHA_REAL"]}' editarinicializado='false'/>
			</c:if>
			<Portal:FormaElemento etiqueta='Periodicidad del Producto' control='selector' controlnombre='IdPeriodicidadProducto' controlvalor='${requestScope.registro.campos["ID_PERIODICIDAD_PRODUCTO"]}' editarinicializado='false' obligatorio='false' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>	
			<Portal:FormaElemento etiqueta='Plazo' control='etiqueta-controlreferencia' controlnombre='Plazo' controlvalor='${requestScope.registro.campos["PLAZO"]}' editarinicializado='false'/>
			<Portal:FormaElemento etiqueta='Tasa del Producto' control='etiqueta-controlreferencia' controlnombre='ValorTasa' controlvalor='${registro.campos["VALOR_TASA"]} %' editarinicializado='false'/>
			<Portal:FormaElemento etiqueta='Periodicidad de la tasa' control='selector' controlnombre='IdPeriodicidadTasa' controlvalor='${registro.campos["ID_PERIODICIDAD_TASA"]}' editarinicializado='false' obligatorio='false' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>	
			<Portal:FormaSeparador nombre=""/>
			
			<c:forEach var="registro" items="${requestScope.ListaAccesorios}">
				<Portal:FormaElemento etiqueta='Accesorio' control='etiqueta-controlreferencia' controlnombre='Accesorio' controlvalor='${registro.campos["NOM_ACCESORIO"]}' editarinicializado='false'/>
				<Portal:FormaElemento etiqueta='Forma de aplicaci&oacute;n' control='etiqueta-controlreferencia' controlnombre='FormaAplicacion' controlvalor='${registro.campos["NOM_FORMA_APLICACION"]}' editarinicializado='false'/>
				<Portal:FormaElemento etiqueta='Valor' control='etiqueta-controlreferencia' controlnombre='Valor' controlvalor='${registro.campos["VALOR"]}' editarinicializado='false'/>
				<Portal:FormaElemento etiqueta='Unidad' control='etiqueta-controlreferencia' controlnombre='Unidad' controlvalor='${registro.campos["NOM_UNIDAD"]}' editarinicializado='false'/>
				<Portal:FormaElemento etiqueta='Periodicidad' control='etiqueta-controlreferencia' controlnombre='Periodicidad' controlvalor='${registro.campos["NOM_PERIODICIDAD"]}' editarinicializado='false'/>
			</c:forEach>
		</Portal:TablaLista>
	</c:if>
	
	<Portal:TablaLista tipo="consulta" nombre="Tabla de amortizaci&oacute;n">
		<Portal:TablaListaTitulos> 
		    <Portal:Columna tipovalor='texto' ancho='25' valor='Plazo'/>
		    <Portal:Columna tipovalor='texto' ancho='80' valor='Fecha'/>
		    <Portal:Columna tipovalor='texto' ancho='100' valor='Saldo inicial'/>
		    <Portal:Columna tipovalor='texto' ancho='80' valor='Tasa'/>
		    <Portal:Columna tipovalor='texto' ancho='100' valor='Inter&eacute;s'/>
		    <Portal:Columna tipovalor='texto' ancho='100' valor='Amortizaci&oacuten de Capital'/>
		    <Portal:Columna tipovalor='texto' ancho='100' valor='Amortizaci&oacuten + Inter&eacute;s'/>
		    
		    <c:forEach var="registro" items="${requestScope.ListaTituloAccesorio}">
			<th  width='100' align='left'  nowrap  ><c:out value='${registro.campos["NOM_ACCESORIO"]}'/>
			</th>
		    </c:forEach>
		   
		    <Portal:Columna tipovalor='texto' ancho='100%' valor='Pago total'/>  
		    <Portal:Columna tipovalor='texto' ancho='100%' valor='Saldo final'/>  
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='25' valor='${registro.campos["NUM_PAGO_AMORTIZACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='80' valor='${registro.campos["FECHA_AMORTIZACION"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["IMP_SALDO_INICIAL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='80' valor='${registro.campos["TASA_INTERES"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["INTERES"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["IMP_CAPITAL_AMORT"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100' valor='$ ${registro.campos["IMP_PAGO"]}'/>
					<% for(int i = 0; i < iNumAccesorios; i++){
						String sRegistro = "$ ${registro.campos[\"ACCESORIO_"+ i +"\"]}";%>
						<Portal:Columna tipovalor='moneda' ancho='100' valor='<%=sRegistro%>'/>
					<%}%>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["PAGO_TOTAL"]}'/>  
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["IMP_SALDO_FINAL"]}'/>  
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
	
	<script>
		function fGenerarTablaAmortizacion(){
				document.frmRegistro.action="ProcesaCatalogo?Funcion=SimConsultaTablaAmortizacion&OperacionCatalogo=CT&Filtro=Todos&Consulta=Prestamos&CvePrestamo="+document.frmRegistro.CvePrestamo.value+"&Nombre="+document.frmRegistro.Nombre.value;
				document.frmRegistro.submit();
		}	
	</script>
	
</Portal:Pagina>
