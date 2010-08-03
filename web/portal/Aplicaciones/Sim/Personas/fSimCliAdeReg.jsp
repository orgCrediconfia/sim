<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimClienteAdeudo">

	<Portal:PaginaNombre titulo="Adeudos" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimClienteAdeudo' parametros='IdPersona=${param.IdPersona}&IdAdeudo=${param.IdAdeudo}&IdExConyuge=${param.IdExConyuge}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<Portal:FormaElemento etiqueta='Persona o instituci&oacute;n a qui&eacute;n adeuda' control='Texto' controlnombre='AdeudoA' controlvalor='${requestScope.registro.campos["ADEUDO_A"]}' controllongitud='50' controllongitudmax='50' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Saldo actual del cr&eacute;dito o adeudo' control='Texto' controlnombre='SaldoActual' controlvalor='${requestScope.registro.campos["SALDO_ACTUAL"]}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='true' validadato='cantidades' />
		<Portal:FormaElemento etiqueta='Frecuencia de pago' control='selector' controlnombre='FrecuenciaPago' controlvalor='${requestScope.registro.campos["FRECUENCIA_PAGO"]}' editarinicializado='true' obligatorio='true' campoclave="ID_PERIODICIDAD" campodescripcion="NOM_PERIODICIDAD" datosselector='${requestScope.ListaPeriodicidad}'/>			
		<Portal:FormaElemento etiqueta='Monto' control='Texto' controlnombre='MontoPago' controlvalor='${requestScope.registro.campos["MONTO_PAGO"]}' controllongitud='10' controllongitudmax='10' editarinicializado='true' obligatorio='true' validadato='cantidades' />
		<Portal:Calendario2 etiqueta='Fecha' contenedor='frmRegistro' controlnombre='Fecha' controlvalor='${requestScope.registro.campos["FECHA"]}'  esfechasis='true'/>
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>	
