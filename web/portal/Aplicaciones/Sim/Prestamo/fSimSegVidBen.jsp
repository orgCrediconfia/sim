<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimSeguroVidaBeneficiario">

	<Portal:PaginaNombre titulo="Beneficiarios" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimSeguroVidaBeneficiario' parametros='IdPrestamo=${param.IdPrestamo}&IdCargoComision=${param.IdCargoComision}&IdBeneficiario=${param.IdBeneficiario}&IdProducto=${param.IdProducto}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<Portal:FormaElemento etiqueta='Apellido Paterno' control='Texto' controlnombre='ApPaterno' controlvalor='${requestScope.registro.campos["AP_PATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Apellido Materno' control='Texto' controlnombre='ApMaterno' controlvalor='${requestScope.registro.campos["AP_MATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Primer Nombre' control='Texto' controlnombre='Nombre1' controlvalor='${requestScope.registro.campos["NOMBRE_1"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Segundo Nombre' control='Texto' controlnombre='Nombre2' controlvalor='${requestScope.registro.campos["NOMBRE_2"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Nombre Completo' control='Texto' controlnombre='NomCompleto' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' controllongitud='60' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Porcentaje' control='Texto' controlnombre='Porcentaje' controlvalor='${requestScope.registro.campos["PORCENTAJE"]}' controllongitud='15' controllongitudmax='15' editarinicializado='true' obligatorio='true' validadato='cantidades'/>
		<Portal:Calendario2 etiqueta='Fecha de nacimiento' contenedor='frmRegistro' controlnombre='FechaNacimiento' controlvalor='${requestScope.registro.campos["FECHA_NACIMIENTO"]}'  esfechasis='true'/>
		<Portal:FormaElemento etiqueta='Parentesco' control='selector' controlnombre='IdParentesco' controlvalor='${requestScope.registro.campos["ID_PARENTESCO"]}' editarinicializado='true' obligatorio='false' campoclave="ID_PARENTESCO" campodescripcion="NOM_PARENTESCO" datosselector='${requestScope.ListaParentesco}'/>			
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>	
