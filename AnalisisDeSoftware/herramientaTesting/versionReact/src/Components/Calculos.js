const operadoresArray = ["int", "double", "float", "char", "Integer", "String", "Boolean", "Exception", "new",
                         "if", "for", "while", "do", "switch", "case", "default", "try", "catch", "finally", 
                         "break", "continue", "+", "-", "/", "*", "%", "++", "--", ";", ":", "=", "(", "[", 
                         "{", "?", "return", "&&", "||", "==", "!=", "<=", ">=", "<", ">"];

function TextoSinComentarios ( texto ) {
    return texto.replace(/(\/\*([\s\S]*?)\*\/)|(\/\/(.*)$)/gm, '');
}

export function CalcularLineasTotales ( codigo ) {
    return codigo.split('\n').length - 1;
}

export function CalcularLineasComentadas ( codigo ) {
    return codigo.split('//').length - 1;
    // TODO: faltan comentarios multilinea
}

export function CalcularLineasCodigo ( codigo ) {
    let codigoSinComentarios = TextoSinComentarios(codigo);
    let lineasCodigo = 0; 
    codigoSinComentarios.split('\n').map( linea => {
        if( ! linea.match(/^(\\t|\s)*?$/) ){
            lineasCodigo++;
        }
        return codigoSinComentarios;
    })
    return lineasCodigo;
}

export function CalcularComplejidad ( codigo ) {
    let textoSinComentarios = TextoSinComentarios(codigo);
    return(textoSinComentarios.match(/(\b(if|for|while|case|default|try)\b|\B(\|\||&&)\B)/gm) || []).length + 1;
}

export function CalcularHalstead ( codigo ) {
    let textoSinComentarios = TextoSinComentarios(codigo);
    let operadoresTotales = 0;
    let operadoresUnicos = 0;
    let operandosTotales = 0;
    let operandosUnicos = 0;

    for ( let op of operadoresArray ){
        if ( textoSinComentarios.indexOf(op) !== -1 ){
            operadoresUnicos++;
            operadoresTotales += textoSinComentarios.split(op).length - 1;
        }
    }

    let textoSinOperadores = textoSinComentarios;
    for ( let op of operadoresArray ) {
        while ( textoSinOperadores.indexOf(op) !== -1 ){
            textoSinOperadores = textoSinOperadores.replace(op, "");
        }
    }

    let textoSinSaltos = textoSinOperadores.replace(/\n|\t|\)|\]|\}/gm, "");
    let textoOperandos = textoSinSaltos.replace(/\s+/gm, " ");
    let operandosArray = textoOperandos.split(" ");

    operandosTotales = operadoresArray.length - 1;

    let operandosUnicosArray = [];

    for ( let op of operandosArray ){
        if ( ! operandosUnicosArray.includes(op) ){
            operandosUnicosArray.push(op);
        }
    }

    operandosUnicos = operandosUnicosArray.length - 1;

    let longitud = operadoresTotales + operandosTotales;
    let volumen = parseFloat((operadoresTotales + operandosTotales) * Math.log2(operadoresUnicos + operandosUnicos)).toFixed(2);

    return [longitud, volumen];
}

export function CalcularFanIn ( base, listaMetodos ) {
    let fanInCount = 0;

    for ( let idx = 0; idx < listaMetodos.length; idx++ ) {
        if ( base.nombre !== listaMetodos[idx].nombre ) {
            fanInCount += cantidadApariciones(listaMetodos[idx].lineas, base.nombre);
        }
    }

    return fanInCount;
}

export function CalcularFanOut ( base, listaMetodos ) {
    let fanOutCount = 0;

    for ( let idx = 0; idx < listaMetodos.length; idx++ ) {
        if ( base.nombre !== listaMetodos[idx].nombre ) {
            fanOutCount += cantidadApariciones(base.lineas, listaMetodos[idx].nombre);
        }
    }

    return fanOutCount;
}

export function getListaMetodos ( codigo ) {
    let arrayLineas = codigo.split('\n');
    let inicioMetodo = false;
    let llavesAbiertas = 0;
    let nombreNuevoMetodo = '';
    let lineasNuevoMetodo = '';
    let idxMetodo = 0;
    let arrayMetodos = [];

    for( let idx = 0; idx < arrayLineas.length; idx++ ) {
        let linea = arrayLineas[idx];

        if( ! inicioMetodo && linea.match(/(public|private|protected) +(static)? +([A-z])+ +([A-z])+/)) {
            inicioMetodo = true;
            nombreNuevoMetodo = getNombreMetodo(linea);
            llavesAbiertas += linea.match(/([{*])/) ? linea.split('{').length - 1 : 0;
        }else if( inicioMetodo ) {
            lineasNuevoMetodo = lineasNuevoMetodo + linea + '\n';
            llavesAbiertas += linea.match(/([{*])/) ? linea.split('{').length - 1 : 0;
            llavesAbiertas -= linea.match(/([}*])/) ? linea.split('}').length - 1 : 0;

            if( llavesAbiertas === 0 ) {
                inicioMetodo = false;

                arrayMetodos.push ({ 
                    id: idxMetodo,
                    nombre: nombreNuevoMetodo,
                    lineas: lineasNuevoMetodo
                 })

                 idxMetodo++;
                 nombreNuevoMetodo = '';
                 lineasNuevoMetodo = '';
            }
        }
    }

    return arrayMetodos;
} 

function getNombreMetodo(linea) {
    var definicion = linea.split('(')[0].trim().split(' ');
    var nombre = definicion[definicion.length - 1];
    return nombre;
}

function cantidadApariciones(cadena, patron) {
    let arrayLineas = cadena.split('\n');;
    let cantidad = 0;

    for ( let idx = 0; idx < arrayLineas.length; idx++ ) {
        if ( arrayLineas[idx].indexOf(patron) !== -1 ){
            cantidad++;
        }
    }
    
    return cantidad;
}