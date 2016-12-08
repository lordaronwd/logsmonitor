import React, { Component } from 'react';
import Statistics from './statistics/Statistics.js'
import Config from './config/Config.js'
import LogMonitorResource from './resource/LogMonitorResource.js'
import './App.css';

class App extends Component {

  constructor() {
    super();
   
    this.state = {
      errors: 0,
      warnings: 0,
      infos: 0,
      editable: false
    };

    this.logMonitorResource = new LogMonitorResource();
    this._toggleConfigWidget = this._toggleConfigWidget.bind(this);
  }


  componentDidMount() {
    setInterval(() => {
      this.logMonitorResource.getStatistics(function(response) {
          if (response.status.code !== 200) {
            console.error("Problem occured when getting log statistics");
          } else {
            this.setState({
              errors: response.entity.errors,
              warnings: response.entity.warnings,
              infos: response.entity.infos
            });
          }
        }.bind(this));
    }, 10);
  }

  _toggleConfigWidget(event) {
    event.preventDefault();
    this.setState({
      editable: !!! this.state.editable
    });
  }

  render() {
    return (
      <div className="app">
        <div className="app-header">
          <h1>Log Monitor</h1>
        </div>
       <Statistics errors={this.state.errors} warnings={this.state.warnings} infos={this.state.infos} />
       {!this.state.editable && <a className="config-link" href="#" onClick={this._toggleConfigWidget}>Change monitoring interval</a>}
       {this.state.editable && <Config onClick={this._toggleConfigWidget} />}
      </div>
    );
  }
}

export default App;
