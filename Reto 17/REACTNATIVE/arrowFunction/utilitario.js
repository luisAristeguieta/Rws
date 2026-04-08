recuperarTexto = (idComponente) => {
    return document.getElementById(idComponente).value;
}

recuperarInt = (idComponente) => {
   return parseInt(recuperarTexto(idComponente));
}

recuperarFloat = (idComponente) => {
    return parseFloat(recuperarTexto(idComponente));
}