import React, { Component } from "react";

import * as Calculos from './Calculos'

import '../styles.css';

class GetCode extends Component {

    state = {
      lineasTotales: 0,
      lineasComentario: 0,
      lineasCodigo: 0,
      porcentaje: '0%',
      resultado: ''
    }

    cargarTotales = (code) => {
      let lt = Calculos.CalcularLineasTotales(code);
      let lcom = Calculos.CalcularLineasComentadas(code);
      let lcod = Calculos.CalcularLineasCodigo(code);
      let porc = lt > 0 ? (lcom / lt * 100).toFixed(2) + '%' : '0%';
      let res = (lcom / lt * 100).toFixed(2) < 15 ? 'Se recomienda un porcentaje de comentarios mayor a 15%' : '';

      this.setState({ 
        lineasTotales: lt,
        lineasComentario: lcom,
        lineasCodigo: lcod,
        porcentaje: porc,
        resultado: res
      });  
    }

    cargarCodigo = (code) => {
      this.cargarTotales(code);
      this.props.setCode(code);
    }

    dropHandler = (e) => {
      e.preventDefault();

      if (e.dataTransfer.items) {

        if (e.dataTransfer.items.length > 1) {
          alert('Solo es posible cargar un archivo a la vez');
          return;
        }

        let file = e.dataTransfer.items[0].getAsFile();
        let reader = new FileReader();
        reader.onload = () => { this.cargarCodigo(reader.result) }
        reader.readAsText(file);

        e.dataTransfer.items.clear();

      } else {
        e.dataTransfer.clearData();
      }
    }
    
    handleFileChange = (e) => {
      if (e.target.files) {
        let reader = new FileReader();
        reader.onload = () => { this.cargarCodigo(reader.result) }
        reader.readAsText(e.target.files[0]);
      }
    }

    handleClearButton = () => {
      document.getElementById('fileOpen').value = '';
      this.cargarCodigo('');
    }
    
    render() {
        return <div>
          <div id="viewGetCode">
            <textarea
              className="textFile"
              placeholder="Copie el código a analizar, arrastre un archivo a esta zona o pulse el boton Abrir..." 
              onChange={(e) => this.props.setCode(e.target.value)}
              value={this.props.codeText} 
              onDrop={this.dropHandler} />
            <div className="contenedor">
              <b>Lineas Totales: </b><br />
              <input type='text' className='recuadro' value={this.state.lineasTotales} disabled /><br />
              <b>Lineas Comentadas: </b><br />
              <input type='text' className='recuadro' value={this.state.lineasComentario} disabled /><br />
              <b>Lineas de Códogo: </b><br />
              <input type='text' className='recuadro' value={this.state.lineasCodigo} disabled /><br />
              <b>Porcentaje Comentado: </b><br />
              <input type='text' className='recuadro' value={this.state.porcentaje} disabled /><br />
              <br /><label style={{color: 'red'}}><b>{this.state.resultado}</b></label>
            </div>
          </div>
          <br />
          <button className="boton" onClick={this.handleClearButton}>Limpiar...</button>
          <input id='fileOpen' type='file' onChange={this.handleFileChange} />
          <button className="boton"><label htmlFor="fileOpen">Abrir...</label></button>
        </div>
    }

}

export default GetCode;