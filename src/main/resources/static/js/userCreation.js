$(document).ready(function(){
  $('#CPF').mask('000.000.000-00');
});

function validateCPF(cpf) {
  cpf = cpf.replace(/\D/g, '');
  if (cpf.length !== 11) {
    return false;
  }

  if (/^(\d)\1{10}$/.test(cpf)) {
    return false;
  }

  let soma = 0;
  for (let i = 0; i < 9; i++) {
    soma += parseInt(cpf.charAt(i)) * (10 - i);
  }
  let primeiroDigito = 11 - (soma % 11);
  primeiroDigito = primeiroDigito > 9 ? 0 : primeiroDigito;

  if (parseInt(cpf.charAt(9)) !== primeiroDigito) {
    return false;
  }

  soma = 0;
  for (let i = 0; i < 10; i++) {
    soma += parseInt(cpf.charAt(i)) * (11 - i);
  }
  let segundoDigito = 11 - (soma % 11);
  segundoDigito = segundoDigito > 9 ? 0 : segundoDigito;

  if (parseInt(cpf.charAt(10)) !== segundoDigito) {
    return false;
  }
  return true;
}


function validatePassword(password, confirmPassword){

    if(!validatePasswordSize(password)){
        return false;
    }

    if(password !== confirmPassword){
        return false;
    }

    return true;
}

function validatePasswordSize(password){
    if(password == null || password.length < 8){
            return false;
    }

    return true;
}

function validate(){
    const cpf = document.getElementById("CPF").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirm-password").value;

    const isCpsValid = validateCPF(cpf);
    const isPasswordValid = validatePassword(password, confirmPassword);

    if(!isCpsValid){
        alert("CPF inválido");
        return false;
    }

     if(!isPasswordValid){
        let mensagemSenha = "Senha inválida";
        if(validatePasswordSize(password)){
                mensagemSenha += ", a senha deve ter no mínimo 8 caracteres";
            }
        alert(mensagemSenha);
        return false;
    }

    return true;
}

