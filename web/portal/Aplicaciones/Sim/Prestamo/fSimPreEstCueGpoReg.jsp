<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoEstadoCuentaGrupo" precarga="SimPrestamoEstadoCuentaResumenGrupo">
	<Portal:PaginaNombre titulo="Estado de Cuenta" subtitulo="Consulta de datos"/>
	
		<Portal:Forma tipo='catalogo' funcion='SimPrestamoEstadoCuenta'>
		<Portal:FormaSeparador nombre="Datos generales"/>
			<Portal:FormaElemento etiqueta='Nombre' control='etiqueta-controlreferencia' controlnombre='NomCompleto' controlvalor='${requestScope.registro.campos["NOM_GRUPO"]}' editarinicializado='false'/>
			
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
		<Portal:FormaBotones>
		</Portal:FormaBotones>
	</Portal:Forma>	
	
	<Portal:TablaLista tipo="consulta" nombre="Estado de Cuenta">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='120' valor='Fecha Operaci&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Fecha de Aplicación'/>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Descripci&oacute;n'/>
			<Portal:Columna tipovalor='moneda' ancho='150' valor='Importe'/>
			<Portal:Columna tipovalor='moneda' ancho='100%' valor='Desglose/Saldo Total'/>		
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaEstadoCuenta}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='120' valor='${registro.campos["FECHA_OPERACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["F_APLICACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["DESCRIPCION"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='150' valor='$ ${registro.campos["IMPORTE"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["IMP_DESGLOSE"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<c:forEach var="registro" items="${requestScope.SaldoFecha}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='120' valor='SALDO A LA FECHA'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor=''/>
				<Portal:Columna tipovalor='texto' ancho='200' valor=''/>
				<Portal:Columna tipovalor='moneda' ancho='150' valor=''/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["IMP_DESGLOSE"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
	
	<Portal:TablaForma maestrodetallefuncion="SimPrestamoEstadoCuentaResumenGrupo" nombre="Resumen" funcion="SimPrestamoEstadoCuentaResumenGrupo" operacion="BA" >
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='200' valor='Descripci&oacute;n'/>
			<Portal:Columna tipovalor='moneda' ancho='200' valor='Importe'/>
			<Portal:Columna tipovalor='moneda' ancho='200' valor='Pagado'/>
			<Portal:Columna tipovalor='moneda' ancho='100%' valor='Saldo'/>		
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaEstadoCuentaResumenGrupo}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='200' valor='${registro.campos["DESCRIPCION"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='200' valor='$ ${registro.campos["IMPORTE"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='200' valor='$ ${registro.campos["PAGADO"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["SALDO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<c:forEach var="registro" items="${requestScope.SaldoTotal}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='200' valor='TOTAL'/>
				<Portal:Columna tipovalor='moneda' ancho='200' valor='$ ${registro.campos["IMPORTE_TOTAL"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='200' valor='$ ${registro.campos["PAGO_TOTAL"]}'/>
				<Portal:Columna tipovalor='moneda' ancho='100%' valor='$ ${registro.campos["SALDO_TOTAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>	
		<Portal:FormaBotones>
		</Portal:FormaBotones>		
	</Portal:TablaForma>

</Portal:Pagina>
