suma = (val1, val2) => {
    return val1 + val2;
}

ejecutarSuma = () => {
    console.log("El resultado de la suma es: " + suma(recuperarFloat("num1"), recuperarFloat("num2")));
}

resta = (val1, val2) => {
    return val1 - val2;
}

ejecutarResta = () => {
    console.log("El resultado de la resta es: " + resta(recuperarFloat("num1"), recuperarFloat("num2")));
}