<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoEstadoCuenta">
	<Portal:PaginaNombre titulo="Consulta el Estado de Cuenta" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimPrestamoEstadoCuenta'>
		<Portal:FormaSeparador nombre="Filtro de b&uacute;squeda"/>
		<Portal:FormaElemento etiqueta='Clave del pr&eacute;stamo' control='Texto' controlnombre='CvePrestamo' controlvalor='${param.CvePrestamo}' controllongitud='19' controllongitudmax='18'/>
		<Portal:FormaElemento etiqueta='Cliente o grupo' control='Texto' controlnombre='Nombre' controlvalor='${param.Nombre}' controllongitud='30' controllongitudmax='100'/>
		<Portal:FormaBotones>
			<input type='button' name='Aceptar' value='B&uacute;squeda' onClick='fBuscarEstadoCuenta()'/>
		</Portal:FormaBotones>
	</Portal:Forma>	

	<Portal:TablaForma nombre="Consulta" funcion="SimPrestamoEstadoCuenta" operacion="AL">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='150' valor='Clave del préstamo'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre del grupo o cliente'/>
			<Portal:Columna tipovalor='texto' ancho='230' valor='Producto'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Ciclo'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["CVE_PRESTAMO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='250' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NOMBRE"]}' funcion='SimPrestamoEstadoCuenta' operacion='CT' parametros='Filtro=Todos&CvePrestamo=${registro.campos["CVE_PRESTAMO"]}&Consulta=EstadoCuenta'/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='230' valor='${registro.campos["ID_PRODUCTO"]} - ${registro.campos["NOM_PRODUCTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NUM_CICLO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>		
	</Portal:TablaForma>	

	<script>
		function fBuscarEstadoCuenta(){
			
				document.frmRegistro.action="ProcesaCatalogo?Funcion=SimPrestamoEstadoCuenta&OperacionCatalogo=CT&Filtro=Todos&Consulta=Prestamos&CvePrestamo="+document.frmRegistro.CvePrestamo.value+"&Nombre="+document.frmRegistro.Nombre.value;
				document.frmRegistro.submit();
			
		}	
	</script>
</Portal:Pagina>
