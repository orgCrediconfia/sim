<%@ page language="java" import="com.rapidsist.portal.configuracion.Usuario"%>
<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="GeneralesUsuarioCambiarPassword">
	<Portal:PaginaNombre titulo="Cambiar Password" subtitulo="Modificaci&oacuten de datos" subtituloalta=""/>
	
	<Portal:Forma tipo='catalogo' funcion='GeneralesUsuarioCambiarPassword'>
		<Portal:FormaSeparador nombre="Datos usuario"/>

		<%Usuario usuario = (Usuario)session.getAttribute("Usuario");%>
		
		<tr>
			<th>Clave</th>
			<td>
				<%=usuario.sCveUsuario%>
			</td>
		</tr>
		
		<tr>
			<th>Nombre</th>
			<td>
				<%=usuario.sNomCompleto%>
			</td>
		</tr>

		<Portal:FormaElemento etiqueta='Password actual' control='etiqueta'>
			<input type="password" name="PasswordActual" size="50" maxlength="20"/>
		</Portal:FormaElemento>
		
		<Portal:FormaElemento etiqueta='Password nuevo' control='etiqueta'>
			<input type="password" name="Password" size="50" maxlength="20"/>
		</Portal:FormaElemento>
		
		<Portal:FormaElemento etiqueta='Re-ingrese password nuevo' control='etiqueta'>
			<input type="password" name="PasswordConfirma" size="50" maxlength="20" onBlur="validarPasswd()"/>
		</Portal:FormaElemento>
		
		<Portal:FormaBotones>
			<input type="button" name="btnOperaciones" value="Aceptar" onClick="javascript:ValidaPassword();" />
        </Portal:FormaBotones>
	</Portal:Forma>
	
	<script>
	
	function ValidaPassword() {
		var p1 = frmRegistro.Password.value;
		var p2 = frmRegistro.PasswordConfirma.value;

		if (p1==""){
			alert("Password actual no es válido");
	  		return;
		}
		else if (p2==""){
			alert("Password actual no váldio");
	  		return;
		}
		else{
			document.frmRegistro.submit();
		}
	}
	
	function validarPasswd() {
	 var p1 = frmRegistro.Password.value;
	 var p2 = frmRegistro.PasswordConfirma.value;
	
	 var espacios = true;
	 var cont = 0;
	
	 // Este bucle recorre la cadena para comprobar que no todo son espacios
	 while (espacios && (cont < p1.length)) {
		if (p1.charAt(cont) != " ") {
		  espacios = false;
		}
		cont++;
	 }
	  
	 var longitudPassword = frmRegistro.Password.value.length;
	 var ultimosTres = p1.substring(longitudPassword-3,longitudPassword);
	 var primerosTres = p1.substring(0,3);
 
	  
	 if (p1 != p2) { 
		alert("El password nuevo no coincide.");
		frmRegistro.PasswordConfirma.value ="";
		frmRegistro.Password.focus();
		return false;
	 } 
	 
	 else {
		return true; 
	 }
	}
	</script>
	
</Portal:Pagina>