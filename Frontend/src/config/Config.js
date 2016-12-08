import React, { Component } from 'react';
import './Config.css';
import LogMonitorResource from '../resource/LogMonitorResource.js'

class Config extends Component {

  constructor() {
    super();

    this.logMonitorResource = new LogMonitorResource();
 	this.state = {monitoringInterval: 0}
	this._handleIntervalChange = this._handleIntervalChange.bind(this);
	this._updateMonitoringInterval = this._updateMonitoringInterval.bind(this);
  }

  componentDidMount() {
  	this.logMonitorResource.getMonitoringInterval(function(response) {
  	  if (response.status.code !== 200) {
        console.error("Problem occured while getting monitoring interval details");
      } else {
      	this.setState({
    	  monitoringInterval: response.entity.monitoringInterval
        });
      }
    }.bind(this));
  }

  _handleIntervalChange(event) {
  	this.setState({monitoringInterval: event.target.value});
  }

  _updateMonitoringInterval(event) {
  	event.preventDefault();
  	this.logMonitorResource.updateMonitoringInterval({monitoringInterval: this.state.monitoringInterval},
  	  function(response) {
  	    if (response.status.code !== 200) {
          console.error("Problem occured while updating monitoring interval details");
       	}
  	});
  	this.props.onClick(event);
  }

  render() {
    return (
      <div className="config">
        <div>
	      <input className="input-field" type="number" value={this.state.monitoringInterval} onChange={this._handleIntervalChange} />
	      <a className="action-link update" href="#" onClick={this._updateMonitoringInterval}> Update </a>
	      <a className="action-link cancel" href="#" onClick={this.props.onClick}> Cancel </a>
	    </div>
	    <div className="hint-message"><span className="star">*</span>value measured in seconds</div>
      </div>
    );
  }
}

export default Config;
