<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimAsignaProductoCiclo">
	<Portal:PaginaNombre titulo="Producto-Ciclo" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimAsignaProductoCiclo' operacion='CT' filtro='Todos' parametros='IdCliente=${param.IdCliente}&IdSucursal=${param.IdSucursal}'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdProducto' controllongitud='5' controllongitudmax='5'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomProducto' controllongitud='20' controllongitudmax='20'/>
		<Portal:Calendario2 etiqueta='Inicio de activaci&oacute;n' contenedor='frmRegistro' controlnombre='FechaInicioActivacion' esfechasis='false'/>
		<Portal:Calendario2 etiqueta='Fin de activaci&oacute;n' contenedor='frmRegistro' controlnombre='FechaFinActivacion' esfechasis='false'/>
	</Portal:Forma>
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/ProcesaCatalogo?Funcion=SimProducto&OperacionCatalogo=IN&Filtro=Alta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='N&uacute;mero de ciclo'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Inicio de activaci&oacute;n'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Fin de activaci&oacute;n'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_PRODUCTO"]}' funcion='SimProducto' operacion='CR' parametros='IdProducto=${registro.campos["ID_PRODUCTO"]}' 
					parametrosregreso='\'${registro.campos["ID_PRODUCTO"]}\', \'${registro.campos["NOM_PRODUCTO"]}\', 
					\'${registro.campos["APLICA_A"]}\', \'${registro.campos["ID_PERIODICIDAD"]}\', \'${registro.campos["CVE_METODO"]}\', 
					\'${registro.campos["NUM_CICLO"]}\', \'${registro.campos["ID_FORMA_DISTRIBUCION"]}\', \'${registro.campos["PLAZO"]}\', \'${registro.campos["TIPO_TASA"]}\', 
					\'${registro.campos["VALOR_TASA"]}\', \'${registro.campos["ID_PERIODICIDAD_TASA"]}\',\'${registro.campos["ID_TASA_REFERENCIA"]}\', 
					\'${registro.campos["MONTO_MAXIMO"]}\', \'${registro.campos["MONTO_MINIMO"]}\', \'${registro.campos["PORC_FLUJO_CAJA"]}\''/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_PRODUCTO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NUM_CICLO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["FECHA_INICIO_ACTIVACION"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["FECHA_FIN_ACTIVACION"]}'/>								
			</Portal:TablaListaRenglon>	
		</c:forEach>
	</Portal:TablaLista>
	
	<script>
		function RegresaDatos(IdProducto, NomProducto, AplicaA, IdPeriodicidadProducto, CveMetodo, NumCiclo, IdFormaDistribucion, Plazo, TipoTasa, ValorTasa, IdPeriodicidadTasa, IdTasaReferencia, MontoMaximo, MontoMinimo, PorcFlujoCaja){
			var padre = window.opener;
			
			padre.document.getElementById('IdProducto').innerHTML = IdProducto;
			padre.document.frmRegistro.IdProducto.value = IdProducto;
			padre.document.getElementById('NomProducto').innerHTML = NomProducto;
			padre.document.getElementById('AplicaA').innerHTML = AplicaA;
			padre.document.frmRegistro.AplicaA.value = AplicaA;
			padre.document.frmRegistro.IdPeriodicidadProducto.value = IdPeriodicidadProducto;
			padre.document.frmRegistro.CveMetodo.value = CveMetodo;
			padre.document.getElementById('NumCiclo').innerHTML = NumCiclo;
			padre.document.frmRegistro.NumCiclo.value = NumCiclo;
			padre.document.frmRegistro.IdFormaDistribucion.value = IdFormaDistribucion;
			padre.document.frmRegistro.Plazo.value = Plazo;
			padre.document.frmRegistro.TipoTasa.value = TipoTasa;
			padre.document.frmRegistro.ValorTasa.value = ValorTasa;
			padre.document.frmRegistro.IdPeriodicidadTasa.value = IdPeriodicidadTasa;
			padre.document.frmRegistro.IdTasaReferencia.value = IdTasaReferencia;
			padre.document.frmRegistro.MontoMaximo.value = MontoMaximo;
			padre.document.frmRegistro.MontoMinimo.value = MontoMinimo;
			padre.document.getElementById('PorcFlujoCaja').innerHTML = PorcFlujoCaja;
			padre.document.frmRegistro.PorcFlujoCaja.value = PorcFlujoCaja;
			
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
	</script>
	
</Portal:Pagina>
