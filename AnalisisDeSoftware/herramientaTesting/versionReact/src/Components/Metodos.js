import React, { Component } from "react";

import * as Calculos from './Calculos'

import '../styles.css';

class Metodos extends Component {

    state = {
        metodoSeleccionado: "",
        lineasTotales: 0,
        lineasCodigo: 0,
        lineasComentario: 0,
        porcentaje: 0,
        complejidad: 0,
        longitud: 0,
        volumen: 0,
        fanIn: 0,
        fanOut: 0,
        resultado: 'Seleccione un metodo y presione el boton Analizar...',
        hayError: false
    }

    handleAnalizarButton = (e) => {
        let metodo = this.props.metodos.find( metodo => metodo.nombre === this.state.metodoSeleccionado );
        let lt = Calculos.CalcularLineasTotales(metodo.lineas);
        let lcod = Calculos.CalcularLineasCodigo(metodo.lineas);
        let lcom = Calculos.CalcularLineasComentadas(metodo.lineas);
        let porc = lt > 0 ? (lcom / lt * 100).toFixed(2) + '%' : '0%';
        let comp = Calculos.CalcularComplejidad(metodo.lineas);
        let [lon, vol] = Calculos.CalcularHalstead(metodo.lineas);
        let fi = Calculos.CalcularFanIn(metodo, this.props.metodos);
        let fo = Calculos.CalcularFanOut(metodo, this.props.metodos);
        let res = '';
        let err = false;

        if ( lt > 0 && (lcom / lt * 100) < 15 ) {
            res = 'Se recomienda un porcentaje de comentarios mayor al 15%. ';
            err = true;
        }

        if ( comp > 11 ) {
            res += 'Se recomienda modularizar el codigo.';
            err = true;
        }

        if ( ! err ) {
            res = 'El código está correctamente modularizado y comentado';
        }

        this.setState({
            lineasTotales: lt,
            lineasCodigo: lcod,
            lineasComentario: lcom,
            porcentaje: porc,
            complejidad: comp,
            longitud: lon,
            volumen: vol,
            fanIn: fi,
            fanOut: fo,
            resultado: res,
            hayError: err
        })

    }

    styleError () {
        return { color: this.state.hayError ? 'red' : 'black' }
    }

    handleSelectMetodo = (e) => {
        this.setState({ 
            metodoSeleccionado: e.target.value,
            lineasTotales: 0,
            lineasCodigo: 0,
            lineasComentario: 0,
            porcentaje: 0,
            complejidad: 0,
            longitud: 0,
            volumen: 0,
            fanIn: 0,
            fanOut: 0,
            resultado: 'Seleccione un metodo y presione el boton Analizar...',
            hayError: false
        })
    }

    render() {
        if ( this.props.metodos.length === 0 ) {
            return null;
        }

        return <div> 
            <h2>Metodos a evaluar</h2>
            <button className="boton" onClick={this.handleAnalizarButton}>Analizar...</button>
            <select onChange={this.handleSelectMetodo}>
                {this.props.metodos.map( (m) => 
                    <option key={m.id} value={m.nombre}>{m.nombre}</option> )}
            </select>
            <table className="tablaResultados">
                <tbody>
                    <tr>
                        <td><b>Lineas totales: {this.state.lineasTotales}</b></td>
                        <td><b>Lineas de codigo: {this.state.lineasCodigo}</b></td>
                        <td><b>Lineas Comentadas: {this.state.lineasComentario}</b></td>
                    </tr>
                    <tr>
                        <td><b>Porcentaje comentado: {this.state.porcentaje}</b></td>
                        <td><b>Complejidad Ciclomática: {this.state.complejidad}</b></td>
                        <td><b>Longitud Helstead: {this.state.longitud}</b></td>
                    </tr>
                    <tr>
                        <td><b>Volumen Helstead: {this.state.volumen}</b></td>
                        <td><b>Fan-In: {this.state.fanIn}</b></td>
                        <td><b>Fan-Out: {this.state.fanOut}</b></td>
                    </tr>
                </tbody>
            </table>
            <br />
            <label style={this.styleError()}><b>{this.state.resultado}</b></label>
        </div>
    }
}

export default Metodos;