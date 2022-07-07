import React, { Component } from 'react';

import GetCode from './Components/GetCode';
import Metodos from './Components/Metodos';
import * as Calculos from './Components/Calculos';

import './styles.css';

class App extends Component {

  state = {
    codigo: '',
    listaMetodos: []
  }

  setEstadoCodigo = (code) => {
    let newListaMetodos = Calculos.getListaMetodos(code);

    this.setState({ 
      codigo: code,
      listaMetodos: newListaMetodos
    });
  }

  render(){
    return <div>
      <h1>Herramienta de Gestion de Testing</h1>
      <GetCode setCode={this.setEstadoCodigo} codeText={this.state.codigo} />
      <br />
      <Metodos metodos={this.state.listaMetodos} />
    </div>
  }
}

export default App;
