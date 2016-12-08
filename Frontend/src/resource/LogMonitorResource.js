import rest from 'rest'
import mime from 'rest/interceptor/mime';

class LogMonitorResource {

  constructor() {
  	this.getStatistics = this.getStatistics.bind(this);
  	this.getMonitoringInterval = this.getMonitoringInterval.bind(this);
  	this.updateMonitoringInterval = this.updateMonitoringInterval.bind(this);
  	this.client = rest.wrap(mime);
  }

  getStatistics(callback) {
  	this.client({
  		path: this._getFullResourcePath('statistics')
  	}).then(callback);

  }

  getMonitoringInterval(callback) {
  	this.client({
  		path: this._getFullResourcePath('monitoring-interval')
  	}).then(callback);
  }

  updateMonitoringInterval(request, callback) {
  	this.client({
  		path: this._getFullResourcePath('monitoring-interval'),
  		entity: request,
  		headers: {
  			   		'Content-Type': 'application/json',
  				  	'Access-Control-Allow-Origin': this._getHostDetails()
  				 },
  		method: 'PUT',
  	}).then(callback);
  }

  _getFullResourcePath(subresource) {
  	return 'http://localhost:8080/logs-monitor/'.concat(subresource);
  }

  _getHostDetails() {
  	return location.protocol.concat("//").concat(window.location.hostname);
  }

}

export default LogMonitorResource;
